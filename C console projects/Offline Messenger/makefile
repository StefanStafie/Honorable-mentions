all:
	gcc server.c -o server -lsqlite3
	gcc client.c -o client

install:
	sudo apt-get update
	sudo apt-get install sqlite3 libsqlite3-dev
clean:
	rm -f cliTcp servTcp