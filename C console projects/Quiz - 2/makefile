#fisier folosit pentru compilarea fisierelor sursa

all:
	gcc server.c -o server -lclntsh -lm -ldl
	gcc client.c -o client
clean:
	rm -f server client

install:
	sudo apt-get install libaio1
