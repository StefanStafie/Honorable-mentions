# tis a backup

import pickle
import gzip

import matplotlib.pyplot as plt
import numpy as np

f = gzip.open('mnist.pkl.gz', 'rb')
(img_train, lab_train), (img_valid, lab_valid), (img_test, lab_test) = pickle.load(f, encoding='latin')
f.close()


def print_image(img):
    fig, axs = plt.subplots(2, 5, squeeze=True)
    for i in range(10):
        axs[int(i / 5), int(i % 5)].imshow(np.reshape(img[i], (28, 28)), cmap='gray')
        axs[int(i / 5), int(i % 5)].set_title(i)
        axs[int(i / 5), int(i % 5)].set_xticklabels([])
    plt.show()


def sigmoid(x):
    x = np.clip(x, -500, 500)
    return 1 / (1 + np.exp(-x))


def softmax(x):
    if np.exp(x).sum() == 0:
        return np.exp(x) / 0.00000001
    return np.exp(x) / np.exp(x).sum()


def sigmoid_derivative(result):
    return result * (1.0 - result)


class Neural_network:

    def __init__(self, input_count, hidden_count, output_count, bias_value, lr_value):
        self.input_count = input_count
        self.hidden_count = hidden_count
        self.output_count = output_count
        self.bias_value = bias_value
        self.lr_value = lr_value
        # (100, 784), (100, 1), (10, 100), (10, 1)
        self.input_weights = np.random.normal(0, 1 / np.sqrt(self.input_count), (self.hidden_count, self.input_count))
        self.input_bias = np.zeros((100, 1)) + bias_value
        self.hidden_weights = np.random.normal(0, 1 / np.sqrt(self.hidden_count),
                                               (self.output_count, self.hidden_count))
        self.hidden_bias = np.zeros((10, 1)) + bias_value

    def train_entry(self, image, label):
        image = np.reshape(image, (784, 1))
        label = np.reshape(label, (10, 1))
        # feedforward
        hidden_image = sigmoid(np.dot(self.input_weights, image) + self.input_bias)
        result = softmax(np.dot(self.hidden_weights, hidden_image) + self.hidden_bias)

        # cost function derivatives
        result_errors = (label - result)
        hidden_errors = sigmoid_derivative(hidden_image) * np.dot(self.hidden_weights.T, result_errors)


        # backpropagation
        self.hidden_weights += self.lr_value * np.dot(result_errors, hidden_image.T) #(100, 10) = (100, 1) * (1, 10)
        self.input_weights += self.lr_value * np.dot(hidden_errors, image.T)
        self.hidden_bias += self.lr_value * result_errors
        self.input_bias += self.lr_value * hidden_errors
        pass

    def print_bad(self, images, labels):
        images = np.array(images, ndmin=2).T
        # feedforward
        hidden_image = sigmoid(np.dot(self.input_weights, images) + self.input_bias)
        result = softmax(np.dot(self.hidden_weights, hidden_image) + self.hidden_bias)
        predicted = np.argmax(np.transpose(result), axis=1)
        for i in range(len(predicted)):
            if predicted[i] != labels[i]:
                print(f"{predicted[i]} -> {labels[i]} : {result[predicted[i]][i]} - {result[labels[i]][i]}")
        return predicted

    def test_predicted(self, images):
        images = np.array(images, ndmin=2).T
        # feedforward
        hidden_image = sigmoid(np.dot(self.input_weights, images) + self.input_bias)
        result = softmax(np.dot(self.hidden_weights, hidden_image) + self.hidden_bias)
        predicted = np.argmax(np.transpose(result), axis=1)
        return predicted

    def test_accuracy(self, images, labels):
        images = np.array(images, ndmin=2).T
        # feedforward
        hidden_image = sigmoid(np.dot(self.input_weights, images) + self.input_bias)
        result = softmax(np.dot(self.hidden_weights, hidden_image) + self.hidden_bias)
        predicted = np.argmax(np.transpose(result), axis=1)
        correct = predicted == labels
        return sum(correct) / len(labels)


def run(data, labels, iterations=2):
    for iteration_counter in range(iterations):
        print(f"iteration {iteration_counter}")
        for (i, image) in enumerate(data):
            targets = np.zeros(10)
            targets[int(labels[i])] = 1
            network.train_entry(image, targets)
        #network.print_bad(img_test, lab_test)
        print(network.test_accuracy(img_test, lab_test))
        print(network.test_predicted(img_test[:20]), lab_test[:20])


network = Neural_network(784, 100, 10, 0.1, 0.0001)
if __name__ == '__main__':
    run(img_train, lab_train, 6)
