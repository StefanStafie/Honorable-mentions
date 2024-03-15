/* cliTCPIt.c - Exemplu de client TCP
   Trimite un numar la server; primeste de la server numarul incrementat.

   Autor: Lenuta Alboaie  <adria@info.uaic.ro> (c)
*/
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <errno.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <netdb.h>
#include <string.h>
#include <arpa/inet.h>

#define MAGIC_NUMBER "57283925634890589234"

/* codul de eroare returnat de anumite apeluri */
extern int errno;

/* portul de conectare la server*/
int port;

int main(int argc, char *argv[])
{
    int sd;                    // descriptorul de socket
    struct sockaddr_in server; // structura folosita pentru conectare
                               // mesajul trimis
    int nr = 0;
    char buf[100];

    /* exista toate argumentele in linia de comanda? */
    if (argc != 3)
    {
        printf("Sintaxa: %s <adresa_server> <port>\n", argv[0]);
        return -1;
    }

    /* stabilim portul */
    port = atoi(argv[2]);

    /* cream socketul */
    if ((sd = socket(AF_INET, SOCK_STREAM, 0)) == -1)
    {
        perror("Eroare la socket().\n");
        return errno;
    }

    /* umplem structura folosita pentru realizarea conexiunii cu serverul */
    /* familia socket-ului */
    server.sin_family = AF_INET;
    /* adresa IP a serverului */
    server.sin_addr.s_addr = inet_addr(argv[1]);
    /* portul de conectare */
    server.sin_port = htons(port);

    /* ne conectam la server */
    if (connect(sd, (struct sockaddr *)&server, sizeof(struct sockaddr)) == -1)
    {
        perror("[client]Eroare la connect().\n");
        return errno;
    }

    while (1)
    {
        /* citirea mesajului */
        printf("[client]Introduceti o comanda: ");
        fflush(stdout);
        memset(buf, '\0', sizeof(buf));
        read(0, buf, sizeof(buf));

        if(strncmp(buf, "quit", 4) == 0){
            break;
        }
        printf("[client] Se transmite: %s\n", buf);


        /* trimiterea mesajului la server */
        if (write(sd, &buf, sizeof(buf)) <= 0)
        {
            perror("[client]Eroare la write() spre server.\n");
            return errno;
        }

        /* citirea raspunsului dat de server
           (apel blocant pina cind serverul raspunde) */
        while(1){
            memset(buf, '\0', sizeof(buf));
            if (read(sd, &buf, sizeof(buf)) < 0)
            {
                perror("[client]Eroare la read() de la server.\n");
                return errno;
            }

            if(strncmp(buf, MAGIC_NUMBER, 20) == 0){
                break;
            }

            printf("%s", buf);
        }
        printf("\n");
    }

    /* inchidem conexiunea, am terminat */
    close(sd);
}