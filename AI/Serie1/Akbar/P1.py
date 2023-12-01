import pandas as pd
import numpy as np

#load the data set and fill nan value with 0
# TODO (load dataset)
df = pd.read_csv('data.csv')
df = df.fillna(0)

from keras.datasets import mnist
(x_train, y_train), (x_test, y_test) = mnist.load_data()