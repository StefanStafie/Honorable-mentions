import numpy as np


# introducerea datelor. Primele 2 coloane pt valori x(input). A treia valoare pt output asteptat (label)
def read_file_to_matrix(name="train.txt"):
    labels, values = [], []
    for line in open(name):
        line = line.replace('\n', '')
        line = line.split(' ')
        values.append([float(i) for i in line[:-1]])
        labels.append(int(line[-1]))
    new_values = []
    for i in values:
        line_values = []
        for j in i:
            j = j * 0.8
            j = j + 0.1
            line_values.append(j)
        new_values.append(line_values)
    return new_values, labels


# functia de activare sigmoida
def sigmoid(x):
    return 1 / (1 + np.exp(-x))


# clasa pentru reteaua neuronala
class Neural_network:

    def __init__(self, lr_value):
        self.input_count = 2
        self.hidden_count = 2
        self.output_count = 1
        self.lr_value = lr_value
        self.input_weights = np.random.normal(0, 0.5, (5, 2))  # varianta cu 10 neuroni in strat ascuns
        self.hidden_weights = np.random.normal(0, 0.5, (1, 5))
        self.delta_input_weights = np.zeros((5, 2))
        self.delta_hidden_weights = np.zeros((1, 5))
        self.input_bias = np.zeros((5,1))
        self.hidden_bias = np.zeros((1,1))
        self.MSE = 0
        pass

    def train_entry(self, data, label):
        # propagarea inainte
        hidden_layer = sigmoid(np.dot(self.input_weights, data)) + self.input_bias # (5, 1) = (5, 2) dot (2, 1)
        output = sigmoid(np.dot(self.hidden_weights, hidden_layer))  + self.hidden_bias# (1, 1) = (1, 5) dot (5, 1)
        activated = output > 0.5
        # Eroare medie patratica pe epoca
        self.MSE += ((label - activated) ** 2) / self.input_count * self.output_count

        # propagare inapoi pt stratul de iesire
        result_errors = output * (1 - output) * (label - output)  # calcul eroare
        # print(result_errors.shape,np.transpose(hidden_layer).shape, self.delta_hidden_weights.shape)
        self.delta_hidden_weights += self.lr_value * np.dot(result_errors,
                                                            np.transpose(hidden_layer))  # actualizare ponderi
        # propagare inapoi pt stratul ascuns
        hidden_errors = hidden_layer * (1 - hidden_layer) * np.dot(np.transpose(self.hidden_weights),
                                                                   result_errors)  # calcul eroare
        self.delta_input_weights += self.lr_value * np.dot(hidden_errors, np.transpose(data))  # actualizare ponderi
        pass

    # prezicere rezultat al datelor de test in functie de reteaua antrenata anterior
    def predict(self, data):
        data = np.reshape(data, (len(data), 2))
        hidden_layer = sigmoid(np.dot(self.input_weights, np.transpose(data)))
        result = sigmoid(np.dot(self.hidden_weights, hidden_layer))
        return np.where(result > 0.5, 1, 0), result


nn = Neural_network(0.1)  # initializeaza retea neuronala cu lr=0.5


def train(data, labels, iterations):
    for iteration_counter in range(iterations):  # procedura iterativa de antrenare
        nn.delta_input_weights = np.zeros((5, 2))
        nn.delta_hidden_weights = np.zeros((1, 5))
        nn.MSE = 0
        for i in range(len(data)):
            data[i] = np.reshape(data[i], (2, 1))  # (2,1)
            nn.train_entry(data[i], labels[i])
        if nn.MSE == 0:
            print(f"{iteration_counter} iterations have past")
            return
        nn.input_weights += nn.delta_input_weights
        nn.hidden_weights += nn.delta_hidden_weights


def run(input_file, iterations):
    data, labels = read_file_to_matrix(input_file)  # citire date
    train(data, labels, iterations)  # antrenare
    print(nn.predict([[0, 0], [0, 1], [1, 0], [1, 1]]))


if __name__ == '__main__':
    run("train.txt", 50000)


