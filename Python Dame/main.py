import time as t
import PySimpleGUI as sg
import numpy as np
from io import BytesIO
from PIL import Image, ImageDraw


# reprezentarea stărilor, starea inițială (inițializarea jocului),
def init_tabla_joc():
    return [['o', 'o', 'o', 'o'],
            ['', '', '', ''],
            ['', '', '', 'c'],
            ['c', 'c', 'c', '']]


# verificarea stării finale
def verifica_stare_finala(tabla_joc):
    if tabla_joc[0] == ['c', 'c', 'c', 'c']:
        return 'c'
    if tabla_joc[3] == ['o', 'o', 'o', 'o']:
        return 'o'
    return False


# generarea mutărilor posibile
def mutare_valida(tabla_joc, i1, j1, i2, j2):
    if i1 - 1 <= i2 <= i1 + 1 and j1 - 1 <= j2 <= j1 + 1:
        if tabla_joc[i2][j2] == "":
            return True
    return False


def generare_mutari(tabla_joc, jucator):
    mutari = []
    for i1 in range(4):
        for j1 in range(4):
            if tabla_joc[i1][j1] == jucator:
                for i2 in range(4):
                    for j2 in range(4):
                        if mutare_valida(tabla_joc, i1, j1, i2, j2):
                            x = [y[:] for y in tabla_joc]
                            muta(x, i1, j1, i2, j2)
                            mutari.append([x, i1 * 4 + j1, i2 * 4 + j2])
    return mutari


# evaluarea stărilor (cu funcția de evaluare euristică) pentru căutarea pe un singur nivel
def functie_evaluare(tabla_joc):
    sum = 12
    for i in range(4):
        for j in range(4):
            if tabla_joc[i][j] != '':
                sum = sum - i
    return sum


def muta(mat, i1, j1, i2, j2):
    mat[i2][j2] = mat[i1][j1]
    mat[i1][j1] = ''


# interfață minimală
def print_matrix(matrix):
    for i in matrix:
        print(i)


def button_image(width, height, color='red'):
    im = Image.new(
        mode='RGBA', size=(width, height), color=(255, 255, 255, 0))
    image = ImageDraw.Draw(im, mode='RGBA')

    radius = height // 2
    image.ellipse((0, 0, height, height), fill=color)
    image.ellipse((width - height, 0, width, height), fill=color)
    image.rectangle((radius, 0, width - radius, height), fill=color)

    with BytesIO() as output:
        im.save(output, format="PNG")
        data = output.getvalue()
    return data


def init_layout():
    menu_def = [['File', ['New Game', 'Exit', ]],
                ['About', ['Authors', ], ],
                ]
    return [[sg.Menu(menu_def)],
            [sg.Button("", key='12', button_color=('gray', 'gray'), image_data=button_image(100, 100, 'red')),
             sg.Button("", key='13', button_color=('gray', 'gray'), image_data=button_image(100, 100, 'red')),
             sg.Button("", key='14', button_color=('gray', 'gray'), image_data=button_image(100, 100, 'red')),
             sg.Button("", key='15', button_color=('gray', 'gray'), image_data=button_image(100, 100, 'gray'))],
            [sg.Button("", key='8', button_color=('gray', 'gray'), image_data=button_image(100, 100, 'gray')),
             sg.Button("", key='9', button_color=('gray', 'gray'), image_data=button_image(100, 100, 'gray')),
             sg.Button("", key='10', button_color=('gray', 'gray'), image_data=button_image(100, 100, 'gray')),
             sg.Button("", key='11', button_color=('gray', 'gray'), image_data=button_image(100, 100, 'red'))],
            [sg.Button("", key='4', button_color=('gray', 'gray'), image_data=button_image(100, 100, 'gray')),
             sg.Button("", key='5', button_color=('gray', 'gray'), image_data=button_image(100, 100, 'gray')),
             sg.Button("", key='6', button_color=('gray', 'gray'), image_data=button_image(100, 100, 'gray')),
             sg.Button("", key='7', button_color=('gray', 'gray'), image_data=button_image(100, 100, 'gray'))],
            [sg.Button("", key='0', button_color=('gray', 'gray'), image_data=button_image(100, 100, 'green')),
             sg.Button("", key='1', button_color=('gray', 'gray'), image_data=button_image(100, 100, 'green')),
             sg.Button("", key='2', button_color=('gray', 'gray'), image_data=button_image(100, 100, 'green')),
             sg.Button("", key='3', button_color=('gray', 'gray'), image_data=button_image(100, 100, 'green'))]
            ]


# jucare
def mutare_calculator(tabla_joc):
    mutari = generare_mutari(tabla_joc, "c")
    valori = []
    for i in mutari:
        valori.append(functie_evaluare(i[0]))
    return mutari[np.argmax(valori)]


####################################### aici incepe lab 6 ##############################################

def mutare_calculator_minimax(tabla_joc, levels):
    if levels == 0:  # daca este nod frunza, se returneaza tabla
        return tabla_joc, 0, 0

    mutari1 = generare_mutari(tabla_joc, "c")  # genereaza mutari pentru calculator
    valori1 = []  # valorile functiei de evaluare MAX
    if verifica_stare_finala(tabla_joc):  # daca e stare finala, se returneaza, deoarece nu poate fi o mutare mai buna
        return tabla_joc, 0, 0

    for i in mutari1:  # iterare pe nivel 1, se alege maximul
        if verifica_stare_finala(i[0]):  # daca e stare finala, se returneaza, deoarece nu poate fi o mutare mai buna
            return i[0], i[1], i[2]
        mutari2 = generare_mutari(i[0], "o")  # genereaza mutari pentru om
        valori2 = []  # valorile functiei de evaluare MIN
        for j in mutari2:  # iterare pe nivel 2, se alege minimul
            valori2.append(functie_evaluare_imbunatatita(mutare_calculator_minimax(j[0], levels - 1)[0]))
        valori1.append(valori2[np.argmin(valori2)])

    return mutari1[np.argmax(valori1)]


def mutare_calculator_alfabeta(tabla_joc, levels, prev_beta):
    if levels == 0:  # daca este nod frunza, se returneaza tabla
        return tabla_joc, 0, 0

    mutari1 = generare_mutari(tabla_joc, "c")  # genereaza mutari pentru calculator
    valori1 = []  # valorile functiei de evaluare MAX
    if verifica_stare_finala(tabla_joc):  # daca e stare finala, se returneaza, deoarece nu poate fi o mutare mai buna
        return tabla_joc, 0, 0
    alfa = -1000000
    for i in mutari1:  # iterare pe nivel 1, se alege maximul
        if verifica_stare_finala(i[0]):  # daca e stare finala, se returneaza, deoarece nu poate fi o mutare mai buna
            return i[0], i[1], i[2]
        mutari2 = generare_mutari(i[0], "o")  # genereaza mutari pentru om
        valori2 = []  # valorile functiei de evaluare MIN
        beta = 1000000
        for j in mutari2:  # iterare pe nivel 2, se alege minimul
            valori2.append(functie_evaluare_imbunatatita(mutare_calculator_alfabeta(j[0], levels - 1, beta)[0]))
            if beta > valori2[-1]:  # daca se gaseste un minim mai bun, se actualizeaza
                beta = valori2[-1]
            if alfa > beta:  # daca alfa > beta, nu mai are rost sa continuam pe ramura asta
                break
        valori1.append(valori2[np.argmin(valori2)])
        if alfa < valori1[-1]:  # daca se gaseste un maxim mai bun, se actualizeaza
            alfa = valori1[-1]
        if alfa > prev_beta:  # daca alfa > prev_beta, nu mai are rost sa continuam pe ramura asta
            return i[0], i[1], i[2]
    return mutari1[np.argmax(valori1)]


def functie_evaluare_imbunatatita(tabla_joc):
    sum = 0
    for i in range(4):
        for j in range(4):
            if tabla_joc[i][j] != '':
                sum -= i
                if tabla_joc[i][j] == 'c':
                    if i == 3:  # modificator care favorizeaza mutarea piesei din pozitia initiala
                        sum -= 2  # modificator = 2 pt calculator si 1 pt om deoarece se prefera iesirea propriei piese
                        # decat blocarea piesei oponentului
                else:
                    if i == 0:
                        sum += 1

    for i in np.transpose(tabla_joc):  # favorizeaza aranjarea pieselor cate 1 pe fiecare coloana
        if "c" in i:
            sum += 0.01 # valoare foarte mica
        if "o" in i:
            sum -= 0.01

    return sum


####################################### aici se termina lab 6 ####################################################
def play():
    tabla_joc = init_tabla_joc()
    layout = init_layout()
    window = sg.Window('Dame', layout)
    while True:
        print(functie_evaluare_imbunatatita(tabla_joc))
        while True:  # read first click of move
            event, values = window.read()
            if event == 'New Game':  # verifica celalte butoane
                window.close()
                play()
                return
            if event == 'Authors':
                sg.popup("Stafie Stefan\nStefanica Catalin\nIA uaic.info.ro 2020")
                continue
            if event == 'Exit':
                window.close()
                return

            pressed1 = event
            i1 = int(int(pressed1) / 4)
            j1 = int(int(pressed1) % 4)
            if tabla_joc[i1][j1] == 'o':
                break
        window[f'{pressed1}'].update(image_data=button_image(100, 100, 'orange'))

        event, values = window.read()  # read second click of move
        pressed2 = event
        i2 = int(int(pressed2) / 4)
        j2 = int(int(pressed2) % 4)
        if not mutare_valida(tabla_joc, i1, j1, i2, j2):  # if second click not good, go back
            window[f'{pressed1}'].update(image_data=button_image(100, 100, 'green'))
            continue
        else:  # if second click good, proceed
            muta(tabla_joc, i1, j1, i2, j2)
            window[f'{pressed2}'].update(image_data=button_image(100, 100, 'green'))
            window[f'{pressed1}'].update(image_data=button_image(100, 100, 'gray'))
            if verifica_stare_finala(tabla_joc):
                sg.popup("A castigat omul")
                window.close()
                play()
                return

            # aux = mutare_calculator((tabla_joc)) # mutare calculator
            # aux = mutare_calculator_minimax(tabla_joc, 3)
            aux = mutare_calculator_alfabeta(tabla_joc, 2, 10000)
            tabla_joc = aux[0]
            trecut = aux[1]
            prezent = aux[2]
            window[f'{trecut}'].update(image_data=button_image(100, 100, 'gray'))
            window[f'{prezent}'].update(image_data=button_image(100, 100, 'red'))

            if verifica_stare_finala(tabla_joc):
                window.refresh()
                sg.popup("A castigat Computerul")
                play()
                return

        if event in (sg.WIN_CLOSED, 'Exit'):
            break


if __name__ == '__main__':
    play()
