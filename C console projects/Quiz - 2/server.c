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
#include <oci.h>

#define PORT 2728
#define MAX_NUMBER_OF_QUESTIONS 50
#define MAX_NUMBER_OF_PLAYERS 50
#define MAX_STRING_LENGTH 256
#define MAX_QUESTION_LENGTH MAX_STRING_LENGTH * 6
#define TIME_BETWEEN_QUESTIONS 10

/// declarare structuri
struct Quiz
{
    char question[MAX_STRING_LENGTH];
    char option1[MAX_STRING_LENGTH];
    char option2[MAX_STRING_LENGTH];
    char option3[MAX_STRING_LENGTH];
    char option4[MAX_STRING_LENGTH];
    int correctAnswer;
};

struct Player
{
    int my_index;
    int fd;
    int points;
    int current_question;
    int finished_quiz;
    char name[MAX_STRING_LENGTH];
};

//////// declarare variabile globale
extern int errno;

int players_joined = 0;
int *current_question_index;
int *game_started;
int *sent_last_question;
int *winner_calculated;
int *accepting_clients;
int quiz_count;

struct Quiz quizzes[MAX_NUMBER_OF_QUESTIONS];
struct Player players[MAX_NUMBER_OF_PLAYERS];
struct Player winner;

/// declarare functii
void *start_game(void *arg);
void *accept_clients(void *arg);
void *player_thread(void *arg);
char *conv_addr(struct sockaddr_in address);
void calculate_winner();
int register_user(struct Player *player);
void wait_for_game_start();
void wait_for_winner_calculated();
int quiz_to_player(struct Player *player);
void send_winner_message(int fd);
void get_question(char *p, struct Quiz quiz);

/////// citire comenzi de la tastatura
int main()
{
    current_question_index = (int *)malloc(sizeof(int));
    game_started = (int *)malloc(sizeof(int));
    sent_last_question = (int *)malloc(sizeof(int));
    winner_calculated = (int *)malloc(sizeof(int));
    accepting_clients = (int *)malloc(sizeof(int));
    *current_question_index = -1;
    *game_started = 0;
    *sent_last_question = 0;
    *winner_calculated = 0;
    *accepting_clients = 0;

    char command[MAX_STRING_LENGTH];

    while (1)
    {
        memset(command, '\0', MAX_STRING_LENGTH);

        printf("\nEnter a command: ");
        fgets(command, sizeof(command), stdin);

        if (strncmp(command, "start-game", 10) == 0)
        {
            if(*game_started == 1){
                printf("Game was started already. Cannot start a new game.\n");
                continue;
            }
            
            pthread_t th;
            pthread_create(&th, NULL, &start_game, NULL);
            pthread_join(th, NULL);
        }
        else
        {
            if (strncmp(command, "accept-clients", 14) == 0)
            {
                if(*accepting_clients == 1){
                    printf("Command failed. Cannot start accepting clients.\n");
                    continue;
                }

                pthread_t th;
                pthread_create(&th, NULL, &accept_clients, NULL);
            }
            else
            {
                if (strncmp(command, "quit", 4) == 0)
                {
                    break;
                }
                else
                {
                    printf("Command not found\n");
                }
            }
        }

        sleep(1);
    }

    return 0;
}
//////////////////////////////////////// logica pentru socket si accept /////////////////////////////////

/* functie de convertire a adresei IP a clientului in sir de caractere */
char *conv_addr(struct sockaddr_in address)
{
    static char str[25];
    char port[7];

    strcpy(str, inet_ntoa(address.sin_addr));
    bzero(port, 7);
    sprintf(port, ":%d", ntohs(address.sin_port));
    strcat(str, port);
    return (str);
}

void *accept_clients(void *arg)
{
    *accepting_clients = 1;

    pthread_t th[MAX_NUMBER_OF_PLAYERS];

    struct sockaddr_in server;
    struct sockaddr_in from;
    int sd, client;
    int optval = 1;
    unsigned int len;

    // creare socket
    if ((sd = socket(AF_INET, SOCK_STREAM, 0)) == -1)
    {
        perror("[server] Eroare la socket().\n");
        pthread_exit(NULL);
    }

    setsockopt(sd, SOL_SOCKET, SO_REUSEADDR, &optval, sizeof(optval));

    bzero(&server, sizeof(server));
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = htonl(INADDR_ANY);
    server.sin_port = htons(PORT);

    if (bind(sd, (struct sockaddr *)&server, sizeof(struct sockaddr)) == -1)
    {
        perror("[server] Eroare la bind().\n");
        pthread_exit(NULL);
    }

    if (listen(sd, 5) == -1)
    {
        perror("[server] Eroare la listen().\n");
        pthread_exit(NULL);
    }

    printf("[server] Asteptam la  %s:%d...\n", inet_ntoa(server.sin_addr), PORT);
    fflush(stdout);

    int flags = fcntl(sd, F_GETFL);
    fcntl(sd, F_SETFL, flags | O_NONBLOCK); // sets socket to nonblocking

    // cat timp timp inca nu a inceput jocul, se accepta clienti
    while (!(*game_started))
    {
        len = sizeof(from);
        bzero(&from, sizeof(from));

        // accepta client
        client = accept(sd, (struct sockaddr *)&from, &len);

        if (client < 1)
        {
            // nu a fost acceptat un client. Se verifica daca a fost din cauza ca am setat accept ca O_NONBLOCK
            if (!(errno == EWOULDBLOCK))
            {
                perror("error when accepting connection");
            }
        }
        else
        {
            printf("Un client a fost acceptat cu ip: %s si port %d\nEnter a command: ", inet_ntoa(from.sin_addr), from.sin_port);
            fflush(stdout);
            struct Player *p;
            p = (struct Player *)malloc(sizeof(struct Player));
            p->fd = client;
            p->my_index = players_joined;
            players[players_joined] = *p;
            pthread_create(&th[players_joined], NULL, &player_thread, p);
            players_joined+=1;
        }
    }

    close(sd);
    pthread_exit(NULL);
}

/// //////////////////// logica pentru joc in sine ////////////////////////////////////////////

void read_quizzes_from_oracle(){
 OCIEnv *envhp;
    OCIError *errhp;
    OCIServer *srvhp;
    OCISession *authp;
    OCISvcCtx *svchp;
    OCIStmt *stmthp = (OCIStmt *)0;
    OCIDefine *defnp1 = (OCIDefine *)0, *defnp2 = (OCIDefine *)0,
             *defnp3 = (OCIDefine *)0, *defnp4 = (OCIDefine *)0,
             *defnp5 = (OCIDefine *)0, *defnp6 = (OCIDefine *)0;

    sword status;

    // Initialize OCI environment
    OCIInitialize(OCI_DEFAULT, (dvoid *)0, (dvoid * (*)(dvoid *, size_t))0,
                  (dvoid * (*)(dvoid *, dvoid *, size_t))0,
                  (void (*)(dvoid *, dvoid *))0 );

    OCIEnvInit( &envhp, OCI_DEFAULT, (size_t)0, (dvoid **)0 );

    // Handle errors
    OCIHandleAlloc( (dvoid *)envhp, (dvoid **)&errhp, OCI_HTYPE_ERROR,
                    (size_t)0, (dvoid **)0 );

    // Create a server context
    OCIHandleAlloc( (dvoid *)envhp, (dvoid **)&srvhp, OCI_HTYPE_SERVER,
                    (size_t)0, (dvoid **)0 );

    // Create a server handle
    OCIServerAttach( srvhp, errhp, (text *)"localhost:1521", strlen("localhost:1521"), OCI_DEFAULT);

    // Create a service context
    OCIHandleAlloc( (dvoid *)envhp, (dvoid **)&svchp, OCI_HTYPE_SVCCTX,
                    (size_t)0, (dvoid **)0 );

    // Set server attribute in the service context
    OCIAttrSet( (dvoid *)svchp, OCI_HTYPE_SVCCTX, (dvoid *)srvhp, (ub4)0,
                OCI_ATTR_SERVER, errhp );

    // Allocate user session handle
    OCIHandleAlloc( (dvoid *)envhp, (dvoid **)&authp, OCI_HTYPE_SESSION,
                    (size_t)0, (dvoid **)0 );

    // Set user session attributes
    OCIAttrSet( (dvoid *)authp, OCI_HTYPE_SESSION, (dvoid *)"new_user", (ub4)strlen("new_user"),
                OCI_ATTR_USERNAME, errhp );
    OCIAttrSet( (dvoid *)authp, OCI_HTYPE_SESSION, (dvoid *)"password123", (ub4)strlen("password123"),
                OCI_ATTR_PASSWORD, errhp );

    // Begin user session
    OCISessionBegin( svchp, errhp, authp, OCI_CRED_RDBMS, OCI_DEFAULT );

    // Set session attributes in the service context
    OCIAttrSet( (dvoid *)svchp, OCI_HTYPE_SVCCTX, (dvoid *)authp, (ub4)0,
                OCI_ATTR_SESSION, errhp );

    // Your SQL query
    const char *sql_query = "SELECT question, option1, option2, option3, option4, correctAnswer FROM quiz_table";

    // Prepare SQL statement
    OCIStmtPrepare( svchp, &stmthp, errhp, (text *)sql_query, strlen(sql_query),
                    OCI_NTV_SYNTAX, OCI_DEFAULT );

    // Define output variables
    OCIHandleAlloc( (dvoid *)envhp, (dvoid **)&defnp1, OCI_HTYPE_DEFINE,
                    (size_t)sizeof(text), (dvoid **)quizzes[0].question );
    OCIHandleAlloc( (dvoid *)envhp, (dvoid **)&defnp2, OCI_HTYPE_DEFINE,
                    (size_t)sizeof(text), (dvoid **)quizzes[0].option1 );
    OCIHandleAlloc( (dvoid *)envhp, (dvoid **)&defnp2, OCI_HTYPE_DEFINE,
                    (size_t)sizeof(text), (dvoid **)quizzes[0].option2 );
    OCIHandleAlloc( (dvoid *)envhp, (dvoid **)&defnp2, OCI_HTYPE_DEFINE,
                    (size_t)sizeof(text), (dvoid **)quizzes[0].option3 );
    OCIHandleAlloc( (dvoid *)envhp, (dvoid **)&defnp2, OCI_HTYPE_DEFINE,
                    (size_t)sizeof(text), (dvoid **)quizzes[0].option4 );
    OCIHandleAlloc( (dvoid *)envhp, (dvoid **)&defnp2, OCI_HTYPE_DEFINE,
                    (size_t)sizeof(text), (dvoid **)quizzes[0].correctAnswer );
    
    OCIStmtDefineByPos( stmthp, &defnp1, errhp, 1, (dvoid *)quizzes[0].question, sizeof(text), SQLT_STR, (dvoid *)0, (ub2 *)0, (ub2 *)0, OCI_DEFAULT );
    OCIStmtDefineByPos( stmthp, &defnp2, errhp, 2, (dvoid *)quizzes[0].option1, sizeof(text), SQLT_STR, (dvoid *)0, (ub2 *)0, (ub2 *)0, OCI_DEFAULT );
    OCIStmtDefineByPos( stmthp, &defnp3, errhp, 3, (dvoid *)quizzes[0].option2, sizeof(text), SQLT_STR, (dvoid *)0, (ub2 *)0, (ub2 *)0, OCI_DEFAULT );
    OCIStmtDefineByPos( stmthp, &defnp4, errhp, 4, (dvoid *)quizzes[0].option3, sizeof(text), SQLT_STR, (dvoid *)0, (ub2 *)0, (ub2 *)0, OCI_DEFAULT );
    OCIStmtDefineByPos( stmthp, &defnp5, errhp, 5, (dvoid *)quizzes[0].option4, sizeof(text), SQLT_STR, (dvoid *)0, (ub2 *)0, (ub2 *)0, OCI_DEFAULT );
    OCIStmtDefineByPos( stmthp, &defnp6, errhp, 6, (dvoid *)quizzes[0].correctAnswer, sizeof(text), SQLT_STR, (dvoid *)0, (ub2 *)0, (ub2 *)0, OCI_DEFAULT );
    
    // Execute the statement
    status = OCIStmtExecute( svchp, stmthp, errhp, (ub4)1, (ub4)0,
                             (CONST OCISnapshot *)NULL, (OCISnapshot *)NULL, OCI_COMMIT_ON_SUCCESS );

    if (status != OCI_SUCCESS) {
        // Handle error
        fprintf(stderr, "Error executing SQL statement.\n");
        return 0;
    }

    // Clean up resources
    OCISessionEnd( svchp, errhp, authp, OCI_DEFAULT );
    OCIServerDetach( srvhp, errhp, OCI_DEFAULT );
    OCIHandleFree( (dvoid *)envhp, OCI_HTYPE_ENV );
    OCIHandleFree( (dvoid *)errhp, OCI_HTYPE_ERROR );
}


void *start_game(void *arg)
{
    printf("Reading quizzes from data base.\n");

/*
    FILE *file = fopen(QUIZ_FILE, "r");
    if (file == NULL)
    {
        fprintf(stderr, "Error opening file.\n");
        return 0;
    }

    quiz_count = 0;

    while (quiz_count < MAX_NUMBER_OF_QUESTIONS &&
           fscanf(file, "%99[^,],%99[^,],%99[^,],%99[^,],%99[^,],%d\n",
                  quizzes[quiz_count].question, quizzes[quiz_count].option1,
                  quizzes[quiz_count].option2, quizzes[quiz_count].option3,
                  quizzes[quiz_count].option4, &quizzes[quiz_count].correctAnswer) == 6)
    {
        quiz_count+=1;
    }

    fclose(file);
*/
    printf("Finished reading quizzes from database.\n");

    for (int i = 0; i < quiz_count; i+=1)
    {
        printf("Quiz %d: [Question: %s, Option 1: %s, Option 2: %s, Option 3: %s, Option 4: %s, Correct Answer: %d]\n", i + 1, quizzes[i].question, quizzes[i].option1, quizzes[i].option2, quizzes[i].option3, quizzes[i].option4, quizzes[i].correctAnswer);
    }

    printf("\nStarting Game\n");

    *game_started = 1;
    *current_question_index = 0;
    while (*current_question_index < quiz_count)
    {
        printf("\nCurrent question index: %d\n", *current_question_index);
        sleep(TIME_BETWEEN_QUESTIONS);
        *current_question_index += 1;
    }

    *sent_last_question = 1;
    sleep(TIME_BETWEEN_QUESTIONS);

    printf("\n --- GAME FINISHED ---\n");

    calculate_winner();
    printf("The winner is %s with %d points.\n", winner.name, winner.points);
    
    pthread_exit(NULL);
};

void calculate_winner()
{
    int max_points = -1;

    printf("\n\nScores for each player:\n");
    for (int i = 0; i < players_joined; i++)
    {
        printf("Player %s with %d points\n", players[i].name, players[i].points);
        if (players[i].points > max_points)
        {
            strcpy(winner.name, players[i].name);
            winner.points = players[i].points;
            max_points = winner.points;
        }
    }

    *winner_calculated = 1;
}

//////// logica pentru jucatori. Aici ajungi dupa ce te conectezi //////////////////
void *player_thread(void *arg)
{
    struct Player *player;
    player = (struct Player *)arg;

    if (!register_user(player))
    {
        printf("Player identified by file descriptor %d could not register and was removed from game.", player->fd);
        pthread_exit(NULL);
    }

    wait_for_game_start();

    if (!quiz_to_player(player))
    {
        pthread_exit(NULL);
    }

    player->finished_quiz = 1;
    players[player->my_index] = *player;

    wait_for_winner_calculated();

    send_winner_message(player->fd);

    close(player->fd);
    pthread_exit(NULL);
}

int register_user(struct Player *player)
{
    char request[MAX_STRING_LENGTH];

    if (read(player->fd, request, MAX_STRING_LENGTH) <= 0)
    {
        perror("Eroare la read() de la client.\n");
        return 0;
    }

    strcpy(player->name, request);

    if (write(player->fd, player->name, MAX_STRING_LENGTH) <= 0)
    {
        perror("[server] Eroare la write() catre client.\n");
        return 0;
    }

    if (read(player->fd, request, MAX_STRING_LENGTH) <= 0)
    {
        perror("Eroare la read() de la client.\n");
        return 0;
    }

    if (strncmp(request, "ok", 2) == 0)
    {
        return 1;
    }

    player->points = 0;
    player->finished_quiz = 0;

    return 0;
}

void wait_for_game_start()
{
    while (*game_started == 0)
    {
        sleep(1);
    }
}

void wait_for_winner_calculated()
{
    while (*winner_calculated == 0)
    {
        sleep(1);
    }
}

int quiz_to_player(struct Player *player)
{
    player->current_question = 0;
    char question_text[MAX_QUESTION_LENGTH], response[MAX_STRING_LENGTH];
    int player_response;

    // ramane in loop atat timp cat nu s-a terminat quiz-ul
    while (player->current_question < quiz_count)
    {
        // se verifica daca jucatorul a ramas in urma cu intrebarile
        if (player->current_question < *current_question_index)
        {
            printf("There was an error with player %s session. He was removed from game\n", player->name);
            write(player->fd, "There was an error with player session. You failed to answer on time. You were removed from game.\n", 98);
            return 0;
        }

        // asteapta sa fie trimisa urmatoarea intrebare
        while (player->current_question > *current_question_index)
        {
            sleep(1);
        }

        // obtine intrebarea ca sir de caractere
        memset(question_text, '\0', MAX_QUESTION_LENGTH);
        get_question(question_text, quizzes[player->current_question]);

        // trimite intrebarea la client
        if (write(player->fd, question_text, MAX_QUESTION_LENGTH) <= 0)
        {
            perror("[server] Eroare la write() catre client.\n");
            return 0;
        }

        // citeste raspunsul
        memset(response, '\0', MAX_STRING_LENGTH);
        if (read(player->fd, response, MAX_STRING_LENGTH) <= 0)
        {
            perror("Eroare la read() de la client.\n");
            return 0;
        }

        // extrage raspunsul ca numar
        player_response = 0;
        if (!(sscanf(response, "%1d", &player_response) == 1))
        {
            printf("There was an error with player %s session. He was removed from game\n", player->name);
            write(player->fd, "There was an error with player session. You answered with invalid number. You were removed from game.\n", 103);
            return 0;
        }

        // daca e raspuns corect, se adauga la puncte
        if (player_response == quizzes[player->current_question].correctAnswer)
        {
            player->points+=1;
        }

        // jucatorul trece la intrebarea urm.
        player->current_question+=1;
    }
}

void send_winner_message(int fd)
{
    char winner_response[2 * MAX_STRING_LENGTH];
    sprintf(winner_response, "The winner is %s with %d points.\n", winner.name, winner.points);

    if (write(fd, winner_response, 2 * MAX_STRING_LENGTH) <= 0)
    {
        perror("[server] Eroare la write() catre client.\n");
        return;
    }
}

void get_question(char *p, struct Quiz quiz)
{
    sprintf(p, "%s\n\t1.%s\n\t2.%s\n\t3.%s\n\t4.%s", quiz.question, quiz.option1, quiz.option2, quiz.option3, quiz.option4);
}
