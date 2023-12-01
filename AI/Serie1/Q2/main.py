import random
import numpy as np
import matplotlib
import time

matplotlib.use('TkAgg')
import matplotlib.pyplot as plt

Alphabet = np.array(['a', 'c', 'g', 't'])


def initialize_state(n):
    if n <= 0:
        return np.array([])

    random_vector = np.array([random.choice(Alphabet) for _ in range(n)])
    return random_vector


def calculate_evaluation(genomes, state):
    max_distance = 0

    for genome in genomes:
        distance = np.count_nonzero(genome != state)
        max_distance = max(max_distance, distance)

    return max_distance


def get_neighbor_state(state):
    neighbor_state = state.copy()
    mutant_index = random.randint(0, len(neighbor_state) - 1)

    next_char = neighbor_state[mutant_index]
    while next_char == neighbor_state[mutant_index]:
        next_char = random.choice(Alphabet)

    neighbor_state[mutant_index] = next_char
    return neighbor_state


def simulated_annealing(genomes, initial_state, alpha, initial_temp, max_iteration, min_temperature):
    curr_state = initial_state
    i = 0
    temperature = initial_temp
    evaluations = []
    best_state = None
    best_value = float('INF')

    while i < max_iteration and temperature > min_temperature:
        new_state = get_neighbor_state(curr_state)

        current_evaluation = calculate_evaluation(genomes, curr_state)
        new_evaluation = calculate_evaluation(genomes, new_state)

        evaluations.append(current_evaluation)

        if new_evaluation < current_evaluation:
            curr_state = new_state
            if new_evaluation < best_value:
                best_state = new_state
                best_value = new_evaluation

        else:
            if random.random() < np.exp(-(new_evaluation - current_evaluation) / temperature):
                curr_state = new_state

        temperature *= alpha
        i += 1

    return best_state, best_value, evaluations


##############################################
# no need to the any thing                   #
##############################################

def go_to_next(number, alphabet_length):
    idx = len(number) - 1
    while idx >= 0:
        if number[idx] < alphabet_length - 1:
            number[idx] += 1
            return True
        else:
            number[idx] = 0
            idx -= 1
    return False


def brute_force(genomes):
    n = len(genomes[0])
    curr_state_index = np.zeros(n).astype(int)
    best_state = None
    best_value = float('INF')

    while True:
        new_result = calculate_evaluation(genomes, Alphabet[curr_state_index])

        if new_result < best_value:
            best_value = new_result
            best_state = Alphabet[curr_state_index]

        if not go_to_next(curr_state_index, len(Alphabet)):
            break

    return best_state, best_value


def draw_results(evaluations):
    ##############################################
    # no need to the any thing                   #
    ##############################################
    plt.plot(evaluations)
    plt.title('Simulated Annealing algorithm')
    plt.ylabel('value')
    plt.xlabel('iteration')
    plt.show()


###################### Test

genomes_array = np.array([
    [['g', 'c', 'a', 't', 'c'],
     ['g', 'a', 'c', 't', 'c'],
     ['c', 'a', 'c', 'g', 'c']],
    [['a', 'c', 'g', 'g', 'g', 'a', 'c'],
     ['a', 'g', 'g', 'c', 'g', 'a', 'g'],
     ['c', 'g', 'g', 'g', 'g', 't', 'c']],
    [['c', 'c', 'a', 'c', 't', 'a', 'g', 'c', 'a'],
     ['c', 't', 'a', 'g', 't', 'c', 't', 'c', 't'],
     ['c', 't', 'c', 'c', 't', 'c', 'c', 'c', 'g']]], dtype=object)

for genomes in genomes_array:
    initial_state = initialize_state(len(genomes[0]))
    start_time_sa = time.time()
    result_dna_sa, result_value_sa, _ = simulated_annealing(genomes, initial_state, 0.9, 500, 1000, 1e-3)
    middle_time = time.time()
    result_dna_bf, result_value_bf = brute_force(genomes)
    end_time_bf = time.time()

    print(f'Simulated Annealing found solution {result_dna_sa} with value {result_value_sa} in %.3f milliseconds' % (
            (middle_time - start_time_sa) * 1000))
    print(f'Brute Force         found solution {result_dna_bf} with value {result_value_bf} in %.3f milliseconds' % (
            (end_time_bf - middle_time) * 1000))

example_genomes = np.array([['c', 'c', 'a', 'c', 't', 'a', 'g', 'g', 'a'],
                            ['c', 't', 'a', 'g', 't', 'c', 't', 'g', 'a'],
                            ['c', 't', 'c', 'c', 't', 'c', 'c', 'g', 'a']])
initial_state = initialize_state(len(example_genomes[0]))

result_dna, result_value, evaluations = simulated_annealing(example_genomes, initial_state, 0.9, 500, 1000, 1e-3)
print(f'results: {result_dna} and  radius: {result_value}')
draw_results(evaluations)

###########


result_dna, result_value, evaluations = simulated_annealing(example_genomes, initial_state, 0.8, 500, 1000, 1e-3)
print(f'results: {result_dna} and  radius: {result_value}')
draw_results(evaluations)

###########

result_dna, result_value, evaluations = simulated_annealing(example_genomes, initial_state, 1, 500, 1000, 1e-3)
print(f'results: {result_dna} and  radius: {result_value}')
draw_results(evaluations)

###########


