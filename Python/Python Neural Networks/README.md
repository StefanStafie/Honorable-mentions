

# 1. Learning boolean functions
Implement backpropagation algorithm, for a multilayer perceptron neural network with 1 hidden layer and 5 neurons in the hidden layer, with the purpose of leaning boolean functions

The program permits introducing the parameters of the algorithm (learn.txt), the learning rate and a maximum number of epochs

The program learns what it has as input and prints the number of epochs it needed to completely learn it.

# 2. Learning digits (without hidden layer)
Implement feedforward and backpropagation for a single layer neural network that uses 10 perceptrons to identify digits.
Digits are taken as 28x28 pixel images in grayscale.
- Learns digits based on the mnist dataset.
- low accuracy (90%)
- low learning time (1 minute)

# 3. Learning digits (with hidden layer)
Implement feedforward and backpropagation for a multilayer neural network that uses 10 perceptrons to identify digits.
Digits are taken as 28x28 pixel images in grayscale.
There is one hidden layer with 100 neurons
- Learns digits based on the mnist dataset.
- high accuracy (97%+)
- high learning time (10 minute+)