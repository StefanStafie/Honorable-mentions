#define _XOPEN_SOURCE /* pentru time.h */
#include <strings.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/stat.h>
#include <sys/socket.h>
#include <fcntl.h>
#include <utmp.h>
#include <time.h>
#include <pwd.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <stdbool.h>
#include <libxml/parser.h>
#include <pthread.h>
#include <time.h>
#include <sqlite3.h>

#define SFARSIT_TRANSMISIUNE "Transmisiune Incheiata\0"
#define PORT 2908
#define BAZA_DE_DATE "db.sqlite"
#define FISIER_LOGIN "login_info.txt"
#define COMANDA_LOGIN "SELECT * FROM utilizatori WHERE nume='%s' AND parola='%s';"
#define COMANDA_CREATE "CREATE TABLE IF NOT EXISTS %s (id INTEGER PRIMARY KEY, nume_utilizator TEXT, mesaj TEXT);"
#define COMANDA_INSERT "INSERT INTO %s (nume_utilizator, mesaj) VALUES ('%s', '%s');"
#define COMANDA_SELECT_MESAJE "SELECT * FROM %s;"
#define COMANDA_SELECT_CONTACTE "SELECT nume FROM utilizatori;"
#define COMANDA_RASPUNDE "UPDATE %s SET mesaj = mesaj || '(' || (SELECT mesaj from %s where id = %d) || ')' WHERE id = (SELECT MAX(id) FROM %s);"
#define COMANDA_STERGE "DELETE from %s;"
#define COMANDA_EXISTA_NOTIFICARI "SELECT * FROM notificari WHERE pentru='%s';"
#define COMANDA_CAUTA_NOTIFICARI "SELECT * FROM notificari WHERE pentru='%s';"
#define COMANDA_INSERT_NOTIFICARI "INSERT OR IGNORE INTO notificari (dela, pentru) VALUES ('%s', '%s');"
#define COMANDA_DELETE_NOTIFICARI "DELETE FROM notificari WHERE pentru='%s';"

/* codul de eroare returnat de anumite apeluri */
extern int errno;
typedef struct thData
{
    int idThread; // id-ul thread-ului tinut in evidenta de acest program
    int cl;       // descriptorul intors de accept
} thData;

static sqlite3 *db; // pointer catre baza de date

static void initializare_baza_de_date();
static void create_table(const char *);
static void insert_mesaj(const char *, const char *, const char *);
static void select_mesaje_contact(const char *, const int);
static void afiseaza_contacte(int);
static void afiseaza_mesaje(const char *, const char *, int);
static void scrie_mesaj(const char *, const char *, const char *);
static void raspunde_la_mesaj(const char*, const char*, const char*, int);
static void sterge_mesaje(const char*, const char*);
static int login_db(char *, char *);
void dupa_trimitere_mesaj(const char*, const char*);
void trimite_notificari_mesaje_primite(const char*, int);
static void *treat(void *);

// in mare luat din curs
int main()
{
    initializare_baza_de_date();

    struct sockaddr_in server; // structura folosita de server
    struct sockaddr_in from;
    int nr; // mesajul primit de trimis la client
    int sd; // descriptorul de socket
    int pid;
    pthread_t th[100]; // Identificatorii thread-urilor care se vor crea
    int i = 0;

    /* crearea unui socket */
    if ((sd = socket(AF_INET, SOCK_STREAM, 0)) == -1)
    {
        perror("[server]Eroare la socket().\n");
        return -1;
    }
    /* utilizarea optiunii SO_REUSEADDR */
    int on = 1;
    setsockopt(sd, SOL_SOCKET, SO_REUSEADDR, &on, sizeof(on));

    /* pregatirea structurilor de date */
    bzero(&server, sizeof(server));
    bzero(&from, sizeof(from));

    /* umplem structura folosita de server */
    /* stabilirea familiei de socket-uri */
    server.sin_family = AF_INET;
    /* acceptam orice adresa */
    server.sin_addr.s_addr = htonl(INADDR_ANY);
    /* utilizam un port utilizator */
    server.sin_port = htons(PORT);

    /* atasam socketul */
    if (bind(sd, (struct sockaddr *)&server, sizeof(struct sockaddr)) == -1)
    {
        perror("[server]Eroare la bind().\n");
        return -1;
    }

    /* punem serverul sa asculte daca vin clienti sa se conecteze */
    if (listen(sd, 2) == -1)
    {
        perror("[server]Eroare la listen().\n");
        return -1;
    }

    printf("[server]Asteptam la portul %d...\n", PORT);
    fflush(stdout);

    /* servim in mod concurent clientii...folosind thread-uri */
    while (1)
    {
        int client;
        thData *td; // parametru functia executata de thread
        int length = sizeof(from);

        // client= malloc(sizeof(int));
        /* acceptam un client (stare blocanta pina la realizarea conexiunii) */
        if ((client = accept(sd, (struct sockaddr *)&from, &length)) < 0)
        {
            perror("[server]Eroare la accept().\n");
            continue;
        }

        /* s-a realizat conexiunea, se astepta mesajul */

        // int idThread; //id-ul threadului
        // int cl; //descriptorul intors de accept

        td = (struct thData *)malloc(sizeof(struct thData));
        td->idThread = i++;
        td->cl = client;

        pthread_create(&th[i], NULL, &treat, td);

    } // while

    sqlite3_close(db);

    return 0;
}

// o parte din model curs
static void *treat(void *arg)
{
    struct thData tdL;
    tdL = *((struct thData *)arg);

    printf("[thread]- %d - Asteptam mesajul...\n", tdL.idThread);
    fflush(stdout);
    pthread_detach(pthread_self());
    // ----- Aici incepe tratarea comenzilor -------------------------------------------------------------------------

    //declarare variabile
    char buf_citire[100];            // buffer din care se citeste comanda de la client
    char buf_scriere[100];           // buffer din care se trimite raspunsul la client
    int utilizator_autentificat = 0; // 0 pt neutentificat, 1 pt autentificat
    char nume_utilizator[94];        // numele utilizatorului. Initial nesetat
    int fd = tdL.cl; // salvez file descriptor pentru client
    memset(nume_utilizator, '\0', sizeof(nume_utilizator));

    while (1)
    {
        // se citeste comanda de la client
        int read_chars = 0;
        memset(buf_citire, '\0', sizeof(buf_citire));
        read_chars = read(tdL.cl, &buf_citire, sizeof(buf_citire));

        // se verifica daca citirea s-a facut cu succes
        if (read_chars <= 0)
        {
            printf("[Thread %d] Error - disconnected\n", tdL.idThread);
            break;
        }

        // se afiseaza mesajul receptionat
        printf("[Thread %d]Mesajul a fost receptionat: %s", tdL.idThread, buf_citire);

        // se cauta comanda corecta ce trebuie executata:
        switch (utilizator_autentificat)
        {
            // se imparte pe 2 categorii. Daca utilizatorul este conectat si daca nu este conectat
        case 0:
            if (!strncmp(buf_citire, "login ", 6))
            {
                // se incearca login
                char login[6], parola[100];

                // se citeste in cele 3 variabile
                if (sscanf(buf_citire, "%s %s %s", login, nume_utilizator, parola) != 3)
                {
                    // in cazul in care nu s-au putut citi 3 variabile, se trimite mesaj comanda incorecta
                    sprintf(buf_scriere, "Comanda incorecta. Pentru ajutor, scrie help\n");
                    write(fd, buf_scriere, sizeof(buf_scriere));
                    break;
                }

                // se face autentificarea folosind tabela utilizatori din baza de date
                utilizator_autentificat = login_db(nume_utilizator, parola);
                if (utilizator_autentificat)
                {
                    // utilizatorul este autentificat
                    sprintf(buf_scriere, "Buna %s\n", nume_utilizator);
                    write(fd, buf_scriere, sizeof(buf_scriere));

                    trimite_notificari_mesaje_primite(nume_utilizator, fd);
                }
                else
                {
                    // utilizatorul nu e autentificat
                    sprintf(buf_scriere, "Numele sau parola sunt incorecte.\n");
                    write(fd, buf_scriere, sizeof(buf_scriere));
                }
            }
            else if (!strncmp(buf_citire, "help", 4))
            {
                // comanda de help
                sprintf(buf_scriere, "Te rog sa te autentifici pentru a avea acces la comenzi\nHint: login <nume_utilizator> <parola>\n\n");
                write(fd, buf_scriere, sizeof(buf_scriere));
            }
            else
            {
                // caz default, cand nu e recunsocuta nicio comanda
                sprintf(buf_scriere, "Comanda incorecta. Pentru ajutor, scrie help\n");
                write(fd, buf_scriere, sizeof(buf_scriere));
            }
            break;

        case 1:
            if (!strncmp(buf_citire, "logout", 6))
            {
                // logout. Se cere dezautentificarea
                // se trimite mesaj ca utilizatorul sa dezautentificat
                sprintf(buf_scriere, "Bye %s\n", nume_utilizator);
                write(fd, buf_scriere, sizeof(buf_scriere));
                
                // se reseteaza valorile pentru utilizator
                utilizator_autentificat = 0;
                memset(nume_utilizator, '\0', sizeof(nume_utilizator));
            }
            else if (!strncmp(buf_citire, "help", 4))
            {
                // pentru comanda help se trimite in mai multe mesaje ca sa nu se depaseasca limita de 100 caractere pe mesaj
                sprintf(buf_scriere, "Esti autentificat ca: ");
                write(fd, buf_scriere, sizeof(buf_scriere));

                sprintf(buf_scriere, "%s\n", nume_utilizator);
                write(fd, buf_scriere, sizeof(buf_scriere));

                sprintf(buf_scriere, "Comenzi disponibile\nlogout : delogheaza de pe server\n");
                write(fd, buf_scriere, sizeof(buf_scriere));

                sprintf(buf_scriere, "contacte : afiseaza numele contactelor\n");
                write(fd, buf_scriere, sizeof(buf_scriere));

                sprintf(buf_scriere, "mesaje <nume_contact> : afiseaza mesajele din istoric cu contactul <nume_contact>\n");
                write(fd, buf_scriere, sizeof(buf_scriere));

                sprintf(buf_scriere, "scrie <nume_contact> <mesaj> : scrie un <mesaj> către <nume_contact>\n");
                write(fd, buf_scriere, sizeof(buf_scriere));

                sprintf(buf_scriere, "raspunde <nume_contact> <id_mesaj> <mesaj> : raspunde la mesajul <id_mesaj>\n");
                write(fd, buf_scriere, sizeof(buf_scriere));

                sprintf(buf_scriere, "sterge <nume_contact> : sterge istoricul pentru <nume_contact>\n\n");
                write(fd, buf_scriere, sizeof(buf_scriere));
            }
            else if (!strncmp(buf_citire, "contacte", 8))
            {
                // afiseaza o lista cu toate contactele inregistrate in baza de date. Informatiile sunt luate din tabela utilizatori
                afiseaza_contacte(fd);
            }
            else if (!strncmp(buf_citire, "mesaje ", 7))
            {
                // afiseaza o lista cu toate mesajele primite de la un anumit contact.
                char mesaje[7], nume_contact[100];
                if (sscanf(buf_citire, "%s %s", mesaje, nume_contact) != 2)
                {
                    // in cazul in care nu s-a trimis comanda cu toti parametrii corecti
                    sprintf(buf_scriere, "Comanda incorecta. Pentru ajutor, scrie help\n");
                    write(fd, buf_scriere, sizeof(buf_scriere));
                    break;
                }

                // afisarea propriu-zisa
                afiseaza_mesaje(nume_utilizator, nume_contact, fd);
            }
            else if (!strncmp(buf_citire, "scrie ", 6))
            {
                // trimite un mesaj catre un contact
                char scrie[6], nume_contact[100], mesaj[80];
                if (sscanf(buf_citire, "%s %s %s", scrie, nume_contact, mesaj) != 3)
                {
                    // in cazul in care nu s-a trimis comanda cu toti parametrii corecti
                    sprintf(buf_scriere, "Comanda incorecta. Pentru ajutor, scrie help\n");
                    write(fd, buf_scriere, sizeof(buf_scriere));
                    break;
                }

                strcpy(mesaj, strstr(buf_citire, mesaj)); // adauga restul de mesaj (initial s-a luat doar pana la primul spatiu)
                mesaj[strlen(mesaj) - 1] = '\0';          // elimina endline

                // adauga mesajul in tabela corecta
                scrie_mesaj(nume_utilizator, nume_contact, mesaj);

                // trimite mesaj de confirmare catre client
                sprintf(buf_scriere, "trimis:%s\n", mesaj);
                write(fd, buf_scriere, sizeof(buf_scriere));
            }
            else if (!strncmp(buf_citire, "raspunde ", 9))
            {
                // functia de reply. Raspunde la un mesaj din conversatie in functie de id-ul mesajului
                int id_mesaj;
                char raspunde[9], nume_contact[100], mesaj[80];
                if (sscanf(buf_citire, "%s %s %d %s", raspunde, nume_contact, &id_mesaj, mesaj) != 4)
                {
                    // in cazul in care nu s-a trimis comanda cu toti parametrii corecti
                    sprintf(buf_scriere, "Comanda incorecta. Pentru ajutor, scrie help\n");
                    write(fd, buf_scriere, sizeof(buf_scriere));
                    break;
                }

                strcpy(mesaj, strstr(buf_citire, mesaj)); // adauga restul de mesaj (initial s-a luat doar pana la primul spatiu)
                mesaj[strlen(mesaj) - 1] = '\0';          // elimina endline

                // adauga mesajul in tabela corecta si face si conexiunea de reply
                raspunde_la_mesaj(nume_utilizator, nume_contact, mesaj, id_mesaj);

                // trimitere confirmare catre client
                sprintf(buf_scriere, "trimis:%s\n", mesaj);
                write(fd, buf_scriere, sizeof(buf_scriere));
            }
            else if (!strncmp(buf_citire, "sterge ", 7))
            {
                // Sterge tot istoricul de conversatie dintre utilizator si contactul primit ca parametru
                int id_mesaj;
                char sterge[7], nume_contact[100];
                if (sscanf(buf_citire, "%s %s", sterge, nume_contact) != 2)
                {
                    // in cazul in care nu s-a trimis comanda cu toti parametrii corecti
                    sprintf(buf_scriere, "Comanda incorecta. Pentru ajutor, scrie help\n");
                    write(fd, buf_scriere, sizeof(buf_scriere));
                    break;
                }

                // se sterge din tabela folosind DELETE
                sterge_mesaje(nume_utilizator, nume_contact);

                // trimitere confirmare de stergere catre client
                sprintf(buf_scriere, "sters\n");
                write(fd, buf_scriere, sizeof(buf_scriere));
            }
            else
            {
                // default, pentru cazul in care mesajul de la client nu se potriveste la nicio comanda 
                sprintf(buf_scriere, "Comanda incorecta. Pentru ajutor, scrie help\n");
                write(fd, buf_scriere, sizeof(buf_scriere));
            }
            break;
        }

        // se trimite sfarsit transmisiune ca sa afle clientul ca poate sa trimita comenzi din nou
        write(fd, SFARSIT_TRANSMISIUNE, 24);
    }

    // ----- Aici se termina tratarea comenzilor -------------------------------------------------------------------------------------------
    close((intptr_t)arg);
    return (NULL);
};

/// @brief se deschide baza de date. Trebuie facut doar o data, la inceputul executiei in thredul principal
static void initializare_baza_de_date()
{
    int rc = sqlite3_open(BAZA_DE_DATE, &db);
    if (rc != SQLITE_OK)
    {
        // se verifica daca baza de date s-a deschis corect
        fprintf(stderr, "Cannot open database: %s\n", sqlite3_errmsg(db));
    }
}

/// @brief callback general, care trimite tot continutul query-ului la client. Mai multe info aici https://www.oreilly.com/library/view/using-sqlite/9781449394592/re272.html
/// @param data 
/// @param argc 
/// @param argv 
/// @param azColName 
/// @return 
static int callback_general(void *data, int argc, char **argv, char **azColName)
{
    //declarare variabile
    int fd = *((int *)data);
    char buf[100];

    // pentru fiecare coloana, se trimite valoarea
    for (int i = 0; i < argc; i++)
    {
        sprintf(buf, "%s ", argv[i]);
        write(fd, buf, sizeof(buf));
    }

    // se trimite endline
    sprintf(buf, "\n");
    write(fd, buf, sizeof(buf));

    return 0;
}

/// @brief callback pentru trimite mesaje. Mai multe info aici https://www.oreilly.com/library/view/using-sqlite/9781449394592/re272.html
/// @param data 
/// @param argc 
/// @param argv 
/// @param azColName 
/// @return 
static int callback_trimite_mesaje(void *data, int argc, char **argv, char **azColName)
{
    //declarare variabile
    int fd = *((int *)data);
    char buf[100];

    // trimitere mesaje formatate catre client (id, nume_utilizator, mesaj)
    sprintf(buf, "%s\t%s:%s\n", argv[0], argv[1], argv[2]);
    write(fd, buf, sizeof(buf));

    return 0;
}

/// @brief callback pentru login. Verifica daca s-a gasit perechea nume_utilizator/parola in baza de date.
/// @param data 
/// @param argc 
/// @param argv 
/// @param azColName 
/// @return 
int login_callback(void *data, int argc, char **argv, char **azColName)
{
    //declarare variabile
    int *found = (int *)data;

    // verifica daca exista 2 coloane in rezultat
    if (argc == 2)
    {
        // se seteaza flag-ul prin pointer, ca sa se modifice si la sursa
        *found = 1;
    }

    return 0;
}

/// @brief functie pentru crearea unei tabele daca nu exista
/// @param nume_canal 
static void create_table(const char *nume_canal)
{
    //declarare variabile
    char comanda_sql[256];
    sprintf(comanda_sql, COMANDA_CREATE, nume_canal);
    
    // executarea sql
    int rc = sqlite3_exec(db, comanda_sql, callback_general, 0, NULL);
    if (rc != SQLITE_OK)
    {
        // in cazul in care a aparut o eroare
        fprintf(stderr, "Cannot create table: %s\n", sqlite3_errmsg(db));
    }
}

/// @brief insereaza un rand in tabela nume_canal reprezentand persoana care a trimis mesajul si mesajul trimis
/// @param nume_canal 
/// @param nume_utilizator 
/// @param mesaj 
static void insert_mesaj(const char *nume_canal, const char *nume_utilizator, const char *mesaj)
{
    //declarare variabile
    char comanda_sql[256];
    sprintf(comanda_sql, COMANDA_INSERT, nume_canal, nume_utilizator, mesaj);

    // executare sql
    int rc = sqlite3_exec(db, comanda_sql, callback_general, 0, NULL);
    if (rc != SQLITE_OK)
    {
        // in cazul in care a aparut o eroare
        fprintf(stderr, "Cannot insert data: %s\n", sqlite3_errmsg(db));
    }
}

/// @brief verifica daca utilizatorul exista in baza de date si e identificat de parola
/// @param nume_utilizator 
/// @param parola 
/// @return 1 daca a fost gasit utilizator cu parola ca in parametri, 0 altfel
static int login_db(char *nume_utilizator, char *parola)
{
    //declarare variabile
    int rc;
    int found = 0;
    char comanda_sql[100];

    // trimitere sql
    snprintf(comanda_sql, sizeof(comanda_sql), COMANDA_LOGIN, nume_utilizator, parola);
    rc = sqlite3_exec(db, comanda_sql, login_callback, &found, NULL);

    if (rc != SQLITE_OK)
    {
        // in cazul in care a aparut o eroare
        fprintf(stderr, "SQL error: %s\n", sqlite3_errmsg(db));
    }

    return found;
}



// -------------- Aici se termina functii pentru baza de date -----------------------------------------------------------------------------------

// --------------Aici incep functiile ajutatoare pentru comenzi ----------------------------------------------------------------------------

/// @brief interogheaza tabela UTILIZATORI si trimite toate valorile nume_utilizator catre client prin fd
/// @param fd file descriptor pentru client
static void afiseaza_contacte(int fd)
{
    //declarare variabile
    char comanda_sql[256];

    sprintf(comanda_sql, COMANDA_SELECT_CONTACTE);
    int rc = sqlite3_exec(db, comanda_sql, callback_general, &fd, NULL);
    if (rc != SQLITE_OK)
    {
        fprintf(stderr, "Cannot select data: %s\n", sqlite3_errmsg(db));
    }
}

/// @brief se calculeaza numele canalului (tabelei) bazat pe numele utilizatorului logat si numele contactului. Rezultatul se pune in nume canal
/// @param nume_utilizator 
/// @param nume_contact 
/// @param nume_canal 
static void denumire_canal(const char *nume_utilizator, const char *nume_contact, char *nume_canal)
{
    if (strcmp(nume_utilizator, nume_contact) > 0)
    {
        strcpy(nume_canal, nume_utilizator);
        strcat(nume_canal, nume_contact);
    }
    else
    {
        strcpy(nume_canal, nume_contact);
        strcat(nume_canal, nume_utilizator);
    }
}

/// @brief 
/// @param nume_utilizator 
/// @param nume_contact 
/// @param fd 
static void afiseaza_mesaje(const char *nume_utilizator, const char *nume_contact, int fd)
{
    //declarare variabile
    char nume_canal[100];
    char comanda_sql[256];
    denumire_canal(nume_utilizator, nume_contact, nume_canal);
    sprintf(comanda_sql, COMANDA_SELECT_MESAJE, nume_canal);
    int rc = sqlite3_exec(db, comanda_sql, callback_trimite_mesaje, &fd, NULL);
    if (rc != SQLITE_OK)
    {
        fprintf(stderr, "Cannot select data: %s\n", sqlite3_errmsg(db));
    }
}

/// @brief trimite mesaj de la utilizatorul autentificat la contact
/// @param nume_utilizator 
/// @param nume_contact 
/// @param mesaj 
static void scrie_mesaj(const char *nume_utilizator, const char *nume_contact, const char *mesaj)
{
    //declarare variabile
    char nume_canal[100];
    denumire_canal(nume_utilizator, nume_contact, nume_canal);// se calculeaza numele tabelei

    // se creeaza tabela, daca nu exista
    create_table(nume_canal);

    // se trimite mesajul propriu-zis
    insert_mesaj(nume_canal, nume_utilizator, mesaj);

    // se adauga in coada de notificari
    dupa_trimitere_mesaj(nume_utilizator, nume_contact);
}

/// @brief Se executa o actualizare la nivelul bazei de date astfel incat sa se poata  
/// @param nume_canal 
/// @param id_mesaj 
void adaugare_mesaj_raspuns(const char *nume_canal, int id_mesaj)
{
    //declarare variabile
    char comanda_sql[256];

    // trimitere comanda sql
    sprintf(comanda_sql, COMANDA_RASPUNDE, nume_canal, nume_canal, id_mesaj, nume_canal);
    int rc = sqlite3_exec(db, comanda_sql, callback_general, 0, NULL);
    
    if (rc != SQLITE_OK)
    {
        // in cazul in care nu s-a putut executa comanda
        fprintf(stderr, "Cannot insert data: %s\n", sqlite3_errmsg(db));
    }
}

/// @brief Functia de reply. Se permite raspunderea la un mesaj identificat prin id_mesaj.
/// @param nume_utilizator 
/// @param nume_contact 
/// @param mesaj 
/// @param id_mesaj 
static void raspunde_la_mesaj(const char *nume_utilizator, const char *nume_contact, const char *mesaj, int id_mesaj)
{
    //declarare variabile
    char nume_canal[100];

    // compunere string pentru denumirea canalului (tabelei) de comunicare
    denumire_canal(nume_utilizator, nume_contact, nume_canal);

    // se creaza tabela, in cazul in care nu exista
    create_table(nume_canal);

    // se trimite mesajul (INSERT)
    insert_mesaj(nume_canal, nume_utilizator, mesaj);

    // se modifica astfel incat mesajul curent sa contina si mesajul la care se raspunde
    adaugare_mesaj_raspuns(nume_canal, id_mesaj);
}

/// @brief Sterge mesajele dintre utilizator si contact. Mesajele sunt sterse pentru ambele parti. Se face prin comanda DELETE din sql.
/// @param nume_utilizator 
/// @param nume_contact 
static void sterge_mesaje(const char* nume_utilizator, const char* nume_contact)
{
    //declarare variabile
    char comanda_sql[256];
    char nume_canal[100];

    // compunere string pentru denumirea canalului (tabelei) de comunicare
    denumire_canal(nume_utilizator, nume_contact, nume_canal);
    sprintf(comanda_sql, COMANDA_STERGE, nume_canal);

    // executia comenzii sql
    int rc = sqlite3_exec(db, comanda_sql, callback_general, 0, NULL);
    if (rc != SQLITE_OK)
    {
        fprintf(stderr, "Cannot delete data: %s\n", sqlite3_errmsg(db));
    }
}

// --------------Aici se termina functiile ajutatoare pentru comenzi -----------------------------------------------------------------------

/// ---------------------------aici incepe notificare la login ------------------------------------

int notificare_callback(void *data, int argc, char **argv, char **azColName)
{
    //declarare variabile
    int *found = (int *)data;

    // verifica daca exista 2 coloane in rezultat
    if (argc == 2)
    {
        // se seteaza flag-ul prin pointer, ca sa se modifice si la sursa
        *found = 1;
    }

    return 0;
}

static int callback_trimite_notificari(void *data, int argc, char **argv, char **azColName)
{
    //declarare variabile
    int fd = *((int *)data);
    char buf[100];

    // trimitere numele de la persoanele cu notificari
    sprintf(buf, "%s, ", argv[0]);
    write(fd, buf, sizeof(buf));

    return 0;
}

void dupa_trimitere_mesaj(const char* nume_utilizator, const char* nume_contact){

    //declarare variabile
    char comanda_sql[256];
    sprintf(comanda_sql, COMANDA_INSERT_NOTIFICARI, nume_utilizator, nume_contact);

    // executare sql
    int rc = sqlite3_exec(db, comanda_sql, callback_general, 0, NULL);
    if (rc != SQLITE_OK)
    {
        // in cazul in care a aparut o eroare
        fprintf(stderr, "Cannot insert data: %s\n", sqlite3_errmsg(db));
    }
}

void trimite_notificari(const char* nume_utilizator, int fd){
    //declarare variabile
    char comanda_sql[256];

    sprintf(comanda_sql, COMANDA_CAUTA_NOTIFICARI, nume_utilizator);
    int rc = sqlite3_exec(db, comanda_sql, callback_trimite_notificari, &fd, NULL);
    if (rc != SQLITE_OK)
    {
        fprintf(stderr, "Cannot select data: %s\n", sqlite3_errmsg(db));
    }
}

int are_notificari(const char* nume_utilizator){
    //declarare variabile
    int rc;
    int found = 0;
    char comanda_sql[100];

    // trimitere sql
    snprintf(comanda_sql, sizeof(comanda_sql), COMANDA_EXISTA_NOTIFICARI, nume_utilizator);
    rc = sqlite3_exec(db, comanda_sql, notificare_callback, &found, NULL);

    if (rc != SQLITE_OK)
    {
        // in cazul in care a aparut o eroare
        fprintf(stderr, "SQL error: %s\n", sqlite3_errmsg(db));
    }

    return found;
}

void stergere_notificari(const char* nume_utilizator){
    //declarare variabile
    char comanda_sql[256];
    sprintf(comanda_sql, COMANDA_DELETE_NOTIFICARI, nume_utilizator);

    // executare sql
    int rc = sqlite3_exec(db, comanda_sql, callback_general, 0, NULL);
    if (rc != SQLITE_OK)
    {
        // in cazul in care a aparut o eroare
        fprintf(stderr, "Cannot delete data: %s\n", sqlite3_errmsg(db));
    }
}

/// @brief Verifica daca utilizatorul arre mesaje si apoi trimite numele utilizatorilor
/// @param nume_utilizator 
/// @param fd 
void trimite_notificari_mesaje_primite(const char* nume_utilizator, int fd){
    char buf_trimitere[100];
    if(are_notificari(nume_utilizator))
    {
        sprintf(buf_trimitere, "Aveti notificari de la: ");
        write(fd, buf_trimitere, sizeof(buf_trimitere));

        trimite_notificari(nume_utilizator, fd);

        stergere_notificari(nume_utilizator);
    }
    else
    {
        sprintf(buf_trimitere, "Nu aveti notificari...\n");
        write(fd, buf_trimitere, sizeof(buf_trimitere));
    }
}

// ---------------------------------aici se termina notificare la login ---------------------------------
 
/// @brief obsolete: nu se mai foloseste pentru ca folosesc fisier sqlite in loc de txt.
/// @param nume_utilizator 
/// @param parola 
/// @return 1 daca e authentificat cu succes. 0 altfel.
static int my_login(char *nume_utilizator, char *parola)
{                                          
    // se deschide login_info.txt
    FILE *file = fopen(FISIER_LOGIN, "r"); 

    // se verifica daca fisierul a fost deschis corect. Daca nu, exit
    if (file == NULL)
    {
        fprintf(stderr, "Error opening file\n");
        exit(EXIT_FAILURE);
    }

    char line[100];
    int found = 0;

    // se citeste o linie intreaga din fisier
    while (fgets(line, sizeof(line), file) != NULL)
    {
        // variabilele in care se citeste ce era in fisier
        char nume_utilizator_fisier[50];
        char parola_fisier[50];

        if (sscanf(line, "%s %s", nume_utilizator_fisier, parola_fisier) == 2)
        {
            // se compara numele si parola din fisier cu cele primite de la client
            if (strcmp(nume_utilizator, nume_utilizator_fisier) == 0 && strcmp(parola, parola_fisier) == 0)
            {
                // se ridica flag-ul, daca s-a gasit o pereche ce se potriveste
                found = 1;
                // se iese din bucla. Nu are rost sa verifice si celelalte valori din fisier
                break;
            }
        }
    }

    // se inchide fisierul
    fclose(file);

    // se returneaza valoarea flag-ului
    return found;
}
