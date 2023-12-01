import numpy as np
import tensorflow as tf
from tensorflow import keras
import string, os
import re
from tqdm import tqdm
import pandas as pd
import matplotlib.pyplot as plt
import pickle
from math import log
from math import log


##### Please don't change this cell #####

# Function to set up distributed training strategy
def setup_distributed_training():
    """
    Set up distributed training strategy for TPU or default strategy for CPU/GPU.

    Returns:
        tf.distribute.Strategy: The distributed training strategy.
    """
    try:
        # Check if TPU is available and configure the strategy accordingly
        tpu = tf.distribute.cluster_resolver.TPUClusterResolver()
        print('Device:', tpu.master())
        tf.config.experimental_connect_to_cluster(tpu)
        tf.tpu.experimental.initialize_tpu_system(tpu)
        strategy = tf.distribute.experimental.TPUStrategy(tpu)
        print('Using TPU strategy.')
    except ValueError:
        # TPU is not available, fall back to CPU/GPU strategy
        strategy = tf.distribute.get_strategy()
        print('Using default strategy for CPU/GPU.')

    print('Number of replicas:', strategy.num_replicas_in_sync)
    return strategy


# Set up the distributed training strategy
strategy = setup_distributed_training()

# Set AUTOTUNE for optimizing performance
AUTOTUNE = tf.data.experimental.AUTOTUNE

print('TensorFlow version:', tf.__version__)

batch_size = 64
epochs = 200
latent_dim = 512

##### Please don't change this cell #####

# Read the dataset and handle errors
try:
    # Load the dataset
    df = pd.read_csv('./topical_chat.csv')[:160000]

    print('Length of dataset:', len(df))

    # Convert the 'message' column to string type
    df['message'] = df['message'].astype(str)

    # Display the first few rows of the dataset
    df.head()
except FileNotFoundError:
    print("Dataset file not found.")
except pd.errors.EmptyDataError:
    print("The dataset file is empty.")
except pd.errors.ParserError:
    print("Error occurred while parsing the dataset.")


##### Please don't change this cell #####

def preprocess_text(text):
    """
    Preprocesses the input text.

    Args:
        text (str): Input text to be preprocessed.

    Returns:
        str: Preprocessed text.
    """
    # Convert to lowercase and replace specific characters
    text = text.lower().replace('\n', ' ').replace('-', ' ').replace(':', ' ').replace(',', '') \
        .replace('"', ' ').replace(".", " ").replace("!", " ").replace("?", " ").replace(";", " ").replace(":", " ")

    # Remove punctuation and extra spaces
    text = ''.join(char for char in text if char not in string.punctuation)
    text = ' '.join(text.split())

    return text


df.message = df.message.apply(preprocess_text)

print(len(df))
df.head()

##### Please don't change this cell #####

# Initialize lists to store input and target texts
input_texts = []
target_texts = []

# Initialize sets to store unique words
input_words_set = set()
target_words_set = set()

# Iterate through the dataset to process conversations
for conversation_index in tqdm(range(1, len(df))):
    # Get the current and previous rows (messages in a conversation)
    current_row = df.iloc[conversation_index]
    previous_row = df.iloc[conversation_index - 1]

    # Check if the conversation IDs match
    if current_row['conversation_id'] == previous_row['conversation_id']:
        input_text = previous_row['message']
        target_text = current_row['message']

        # Check conditions for valid input and target texts
        if (2 < len(input_text.split()) < 30 and
                0 < len(target_text.split()) < 10 and
                input_text and target_text):
            # Add start and end tokens to the target text
            target_text = "bos " + target_text + " eos"

            # Append to input and target text lists
            input_texts.append(input_text)
            target_texts.append(target_text)

            # Update the sets of unique words
            input_words_set.update(input_text.split())
            target_words_set.update(target_text.split())

print("\nNumber of unique input words:", len(input_words_set))
print("Number of unique target words:", len(target_words_set))

##### Please don't change this cell #####

# Convert sets to sorted lists for consistency
input_words = sorted(list(input_words_set))
target_words = sorted(list(target_words_set))

# Calculate various lengths and quantities
num_encoder_tokens = len(input_words)
num_decoder_tokens = len(target_words)
max_encoder_seq_length = max(len(txt.split()) for txt in input_texts)
max_decoder_seq_length = max(len(txt.split()) for txt in target_texts)

print("Number of samples:", len(input_texts))
print("Number of unique input tokens:", num_encoder_tokens)
print("Number of unique output tokens:", num_decoder_tokens)
print("Max sequence length for inputs:", max_encoder_seq_length)
print("Max sequence length for outputs:", max_decoder_seq_length)

# Create token index dictionaries
input_token_index = {word: i for i, word in enumerate(input_words)}
target_token_index = {word: i for i, word in enumerate(target_words)}

# Initialize arrays for encoder and decoder data
encoder_input_data = np.zeros((len(input_texts), max_encoder_seq_length), dtype="float32")
decoder_input_data = np.zeros((len(input_texts), max_decoder_seq_length), dtype="float32")
decoder_target_data = np.zeros((len(input_texts), max_decoder_seq_length, num_decoder_tokens), dtype="float32")

# Populate the arrays with token indices
for i, (input_text, target_text) in enumerate(zip(input_texts, target_texts)):
    for t, word in enumerate(input_text.split()):
        encoder_input_data[i, t] = input_token_index[word]

    for t, word in enumerate(target_text.split()):
        decoder_input_data[i, t] = target_token_index[word]
        if t > 0:
            decoder_target_data[i, t - 1, target_token_index[word]] = 1.0

print("Encoder Input Data shape:", encoder_input_data.shape)
print("Decoder Input Data shape:", decoder_input_data.shape)
print("Decoder Target Data shape:", decoder_target_data.shape)

##### Please don't change this cell #####

# Define constants and hyperparameters
embedding_size = 100

# Define encoder
with strategy.scope():
    encoder_inputs = keras.Input(shape=(None,))
    encoder_embedding_output = keras.layers.Embedding(num_encoder_tokens, embedding_size)(encoder_inputs)
    encoder_lstm = keras.layers.LSTM(latent_dim, return_state=True)
    encoder_outputs, state_h, state_c = encoder_lstm(encoder_embedding_output)
    encoder_states = [state_h, state_c]

# Define decoder
with strategy.scope():
    decoder_inputs = keras.Input(shape=(None,))
    decoder_embedding = keras.layers.Embedding(num_decoder_tokens, embedding_size)  # Define decoder_embedding here
    decoder_embedding_output = decoder_embedding(decoder_inputs)
    decoder_lstm = keras.layers.LSTM(latent_dim, return_sequences=True, return_state=True)
    decoder_outputs, _, _ = decoder_lstm(decoder_embedding_output, initial_state=encoder_states)
    decoder_dense = keras.layers.Dense(num_decoder_tokens, activation="softmax")
    decoder_outputs = decoder_dense(decoder_outputs)

# Define the model
with strategy.scope():
    model = keras.Model([encoder_inputs, decoder_inputs], decoder_outputs)
    model.compile(optimizer="adam", loss="categorical_crossentropy", metrics=["accuracy"])
    model.summary()

# Training
with strategy.scope():
    history = model.fit([encoder_input_data, decoder_input_data], decoder_target_data, batch_size=batch_size, epochs=30,
                        validation_split=0.1)

# Define the encoder model
encoder_model = keras.Model(encoder_inputs, encoder_states)
encoder_model.summary()

# Define the decoder model
with strategy.scope():
    decoder_state_input_h = keras.Input(shape=(None,))
    decoder_state_input_c = keras.Input(shape=(None,))
    decoder_states_inputs = [decoder_state_input_h, decoder_state_input_c]

    decoder_embedding_output = decoder_embedding(decoder_inputs)

    decoder_outputs2, state_h2, state_c2 = decoder_lstm(decoder_embedding_output, initial_state=decoder_states_inputs)
    decoder_states2 = [state_h2, state_c2]
    decoder_outputs2 = decoder_dense(decoder_outputs2)
    decoder_model = keras.Model([decoder_inputs] + decoder_states_inputs, [decoder_outputs2] + decoder_states2)

##### Please don't change this cell #####

# Reverse-lookup token index
reverse_input_token_index = dict((i, word) for word, i in input_token_index.items())
reverse_target_token_index = dict((i, word) for word, i in target_token_index.items())


# %%
##### Please don't change this cell #####
##### Please read this function #####

def generate_text(input_text, max_encoder_seq_length, input_token_index,
                  target_token_index, encoder_model, decoder_model,
                  reverse_target_token_index, max_response_length=50):
    """
    Generate a response based on the input text.
    Args:
        input_text (str): The input text to generate a response for.
        max_encoder_seq_length (int): Maximum length of the encoder sequence.
        input_token_index (dict): Dictionary mapping input words(tokens) to indices.
        target_token_index (dict): Dictionary mapping target words(tokens) to indices.
        encoder_model: The encoder model.
        decoder_model: The decoder model.
        reverse_target_token_index (dict): Dictionary mapping indices to target words(tokens).
        max_response_length (int): Maximum length of the generated response.
    Returns:
        str: The generated response.
    """
    input_seq = np.zeros((1, max_encoder_seq_length), dtype="float32")

    # Tokenize the input text and create the input sequence
    for t, word in enumerate(input_text.split()):
        # Convert words to their respective token indices
        input_seq[0, t] = input_token_index[word]

    # Encode the input as state vectors
    states_value = encoder_model.predict(input_seq)
    # Initialize the target sequence with the start character
    target_seq = np.zeros((1, 1))
    target_seq[0, 0] = target_token_index['bos']  # 'bos' indicates the start of a sentence

    # Sampling loop to generate the response
    generated_sentence = ''
    for _ in range(max_response_length):
        output_tokens, h, c = decoder_model.predict([target_seq] + states_value)
        # Sample a token by selecting the one with the highest probability (greedy method)
        sampled_token_index = np.argmax(output_tokens[0, -1, :])
        sampled_token = reverse_target_token_index[sampled_token_index]

        # Exit condition: either hit max length or find the stop character
        if sampled_token == 'eos' or len(generated_sentence) > max_response_length:
            break
        else:
            generated_sentence += ' ' + sampled_token

        # Update the target sequence (of length 1) with the sampled token
        target_seq[0, 0] = sampled_token_index

        # Update the states for the next iteration
        states_value = [h, c]

    return generated_sentence


# %%
num_sequences_to_generate = 10

for seq_index in range(num_sequences_to_generate):
    input_text = input_texts[seq_index]
    generated_sentence = generate_text(input_text, max_encoder_seq_length,
                                       input_token_index, target_token_index,
                                       encoder_model, decoder_model,
                                       reverse_target_token_index)

    print("input sentence:", input_text)
    print("generated sentence:", generated_sentence)


##### please implement this function (use beam search algorithm) #####

def generate_text_with_beam_search(input_text, number_of_words, beam_search_n,
                                   max_encoder_seq_length, input_token_index, target_token_index,
                                   encoder_model, decoder_model, reverse_target_token_index):
    """
    Generate text using beam search.
    Args:
        input_text (str): The initial text to start the generation.
        number_of_words (int): The number of words to generate.
        beam_search_n (int): Beam width for the search.
        max_encoder_seq_length (int): Maximum length of the encoder sequence.
        input_token_index (dict): Dictionary mapping input words(tokens) to indices.
        target_token_index (dict): Dictionary mapping target words(tokens) to indices.
        encoder_model: The encoder model.
        decoder_model: The decoder model.
        reverse_target_token_index (dict): Dictionary mapping indices to target_words.
    Returns:
        None
    """


# %%
##### please run this cell after implement generate_text_with_beam_search function #####

num_sequences_to_generate = 5
number_of_words = 5
beam_search_n = 2

for seq_index in range(num_sequences_to_generate):
    input_text = input_texts[seq_index]
    print("input sentence:", input_text)
    generated_sentence = generate_text_with_beam_search(input_text, number_of_words, beam_search_n,
                                                        max_encoder_seq_length, input_token_index, target_token_index,
                                                        encoder_model, decoder_model, reverse_target_token_index)
