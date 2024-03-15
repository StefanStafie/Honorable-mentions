#include <sys/types.h>
#include <sys/socket.h>
#include <sys/time.h>
#include <netinet/in.h>
#include <unistd.h>
#include <errno.h>
#include <stdio.h>
#include <arpa/inet.h>
#include <string.h>
#include <stdlib.h>
#include <pthread.h>
#include <fcntl.h>

#define MAX_STRING_LENGTH 256
#define MAX_QUESTION_LENGTH MAX_STRING_LENGTH * 6 

// variabile globale
extern int errno;

int port;

int main(int argc, char *argv[])
{
    int sd;
    struct sockaddr_in server;
    char request[MAX_STRING_LENGTH], response[MAX_QUESTION_LENGTH];

    // verifica argumente
    if (argc != 4)
    {
        printf("Sintaxa: %s [IP] [port] [nume]\n", argv[0]);
        return -1;
    }

    port = atoi(argv[2]);
    if ((sd = socket(AF_INET, SOCK_STREAM, 0)) == -1)
    {
        perror("[client] Eroare la socket().\n");
        return errno;
    }

    server.sin_family = AF_INET;
    server.sin_addr.s_addr = inet_addr(argv[1]);
    server.sin_port = htons(port);
    
    if (connect(sd, (struct sockaddr *)&server, sizeof(struct sockaddr)) == -1)
    {
        perror("[client] Eroare la connect().\n");
        return errno;
    }

    // trimite nume jucator
    if (write(sd, argv[3], MAX_STRING_LENGTH) <= 0)
    {
        perror("Eroare la write() catre server.\n");
        return 0;
    }        


    memset(request, '\0', MAX_STRING_LENGTH);
    if (read(sd, request, MAX_STRING_LENGTH) <= 0)
    {
        perror("Eroare la read() de la server.\n");
        return 0;
    }

    if(strcmp(request, argv[3]) != 0){
        printf("A fost o eroare la comunicarea numelui catre server. Exit\n");
        return 0;
    }

    if (write(sd, "ok", 3) <= 0)
    {
        perror("Eroare la write() catre server.\n");
        return 0;
    }

    printf("Conectat. Se asteapta inceperea jocului...\n");

    // primeste intrebari de la server si trimite raspunsuri
    while(1){
        memset(response, '\0', MAX_QUESTION_LENGTH);
        if (read(sd, response, MAX_QUESTION_LENGTH) <= 0)
        {
            perror("Eroare la read() de la server.\n");
            return 0;
        }

        printf("%s", response);
        if(strncmp(response, "The winner is", 13) == 0){
            break;
        }

        if(strncmp(response, "There was an error with player", 30) == 0){
            break;
        }


        memset(request, '\0', MAX_STRING_LENGTH);
        printf("\nYour answer: ");
        fgets(request, MAX_STRING_LENGTH, stdin);

        if (write(sd, request, MAX_STRING_LENGTH) <= 0)
        {
            perror("Eroare la write() catre server.\n");
            return 0;
        }
    }

    close(sd);

    printf("Press any key to exit.\n");
    getchar();
}