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

#define FISIER_REZERVARI "rezervari.xml"
#define FISIER_CAMERE "camere.xml"
#define NR_MAXIM_CAMERE 20
#define PORT 2908
#define MAGIC_NUMBER "57283925634890589234"

/* codul de eroare returnat de anumite apeluri */
extern int errno;
typedef struct thData
{
    int idThread; // id-ul thread-ului tinut in evidenta de acest program
    int cl;       // descriptorul intors de accept
} thData;

void vizualizare_rezervari_client(int fd, char *username);
void vizualizare_rezervari();
int sterge_rezervare(char *command, char *username);
void vizualizare_data(char *date, int fd);
void vizualizare_data2(int fd, char *date_of_arrival, char *date_of_departure);
int rezervare(char *client_name, char *room_id, char *date_of_arrival, char *date_of_departure);
int compare_date(const char *date1, const char *date2);
static void *thread_primire_clienti(void *);
static void *treat(void *); /* functia executata de fiecare thread ce realizeaza comunicarea cu clientii */
void raspunde(void *);
int sterge_tot();

int login_command(char *username) // primeste ca parametru username si returneaza (0, 1 sau 2) respectiv (user not found, user login success sau admin login success)
{
    username[strlen(username) - 1] = '\0';
    char usernames[256];
    int usernames_length;
    int fd = open("usernames.txt", O_RDONLY);

    if (fd == -1)
    {
        perror("Eroare la deschiderea fișierului usernames.txt");
        exit(EXIT_FAILURE);
    }

    usernames_length = read(fd, usernames, 256);

    char *target_name = strtok(usernames, "\n"); // folosește functia strtok() pentru a diviza sirul usernames in cuvinte separate, folosind spațiul ca delimiter.
    int found_name_status = 0;                   // tine evidența daca username a fost găsit în lista de nume de utilizatori sau nu.

    while (target_name && found_name_status == 0)
    { // Prin urmare, ciclul se va opri atunci cand s-a terminat de parcurs lista de nume de utilizatori sau cand s-a găsit deja un nume de utilizator potrivit.
        // printf("%s  %s\n", username, target_name);
        if (strcmp(username, target_name) == 0)
        {                          // daca numele a fost gasit  prin lista de utilizatori
            found_name_status = 1; // cautarea se opreste numele a fost gasit
        }

        target_name = strtok(NULL, "\n");
    }

    if (found_name_status == 1 && strcmp(username, "admin") == 0)
    {
        return 2;
    }
    else
    {
        return found_name_status;
    }
}

int main()
{
    xmlKeepBlanksDefault(0);

    pthread_t thread_clienti;
    pthread_create(&thread_clienti, NULL, &thread_primire_clienti, NULL);

    sleep(1);

    char command[100];
    while (1)
    {
        memset(command, '\0', sizeof(command));
        read(0, command, 100);

        if (strncmp(command, "quit", 4) == 0)
        {
            break;
        }
        else
        {
            printf("Comandă invalidă.\n");
        }
    }

    return 0;
}

// luat INTEGRAL din model curs
static void *thread_primire_clienti(void *)
{
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
        return NULL;
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
        return NULL;
    }

    /* punem serverul sa asculte daca vin clienti sa se conecteze */
    if (listen(sd, 2) == -1)
    {
        perror("[server]Eroare la listen().\n");
        return NULL;
    }
    /* servim in mod concurent clientii...folosind thread-uri */
    while (1)
    {
        int client;
        thData *td; // parametru functia executata de thread
        int length = sizeof(from);

        printf("[server]Asteptam la portul %d...\n", PORT);
        fflush(stdout);

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
};

// luat INTEGRAL din model curs
static void *treat(void *arg)
{
    struct thData tdL;
    tdL = *((struct thData *)arg);
    printf("[thread]- %d - Asteptam mesajul...\n", tdL.idThread);
    fflush(stdout);
    pthread_detach(pthread_self());
    raspunde((struct thData *)arg);
    /* am terminat cu acest client, inchidem conexiunea */
    close((intptr_t)arg);
    return (NULL);
};

// aici primim comenzi de la client si trimitem raspunsuri
void raspunde(void *arg)
{
    char buf[100];
    char logged_user[100];
    int i = 0, user_authenticated = 0;
    struct thData tdL;
    tdL = *((struct thData *)arg);
    while (1)
    {
        memset(buf, '\0', sizeof(buf));
        int read_chars = 0;
        read_chars = read(tdL.cl, &buf, sizeof(buf));
        if (read_chars <= 0)
        {
            printf("[Thread %d] Error - disconnected\n", tdL.idThread);
            return;
        }

        printf("[Thread %d]Mesajul a fost receptionat: %s\n", tdL.idThread, buf);

        if (strncmp(buf, "login", 5) == 0)
        {
            printf("try login with %s \n", buf + 6);
            user_authenticated = login_command(buf + 6);
            if (user_authenticated > 0)
            {
                strcpy(logged_user, buf + 6);
                write(tdL.cl, "login success\n", 14);
            }
            else
            {
                write(tdL.cl, "login failed\n", 13);
            }
        }
        else
        {
            if (strncmp(buf, "logout", 6) == 0)
            {
                user_authenticated = 0;
                write(tdL.cl, "You have successfully logged out.\n", 34);
            }
            else if (user_authenticated == 1)
            {
                if (strncmp(buf, "vizualizare-rezervari", 21) == 0)
                {
                    vizualizare_rezervari_client(tdL.cl, logged_user);
                }
                else if (strncmp(buf, "vizualizare2", 12) == 0)
                {
                    char command_text[20], date_of_arrival[20], date_of_departure[20];
                    if (sscanf(buf, "%s %s %s", command_text, date_of_arrival, date_of_departure) != 3)
                    {
                        write(tdL.cl, "Invalid command format\n", 23);
                        sleep(1);
                        write(tdL.cl, MAGIC_NUMBER, 20);
                        return;
                    }

                    vizualizare_data2(tdL.cl, date_of_arrival, date_of_departure);
                }
                else if (strncmp(buf, "vizualizare", 11) == 0)
                {
                    char command_text[20], date[20];
                    if (sscanf(buf, "%s %s", command_text, date) != 2)
                    {
                        write(tdL.cl, "Invalid command format\n", 23);
                        sleep(1);
                        write(tdL.cl, MAGIC_NUMBER, 20);
                        return;
                    }

                    vizualizare_data(date, tdL.cl);
                }
                else if (strncmp(buf, "rezervare", 9) == 0)
                {
                    char command_text[20], room_id[20], date_of_arrival[20], date_of_departure[20];
                    if (sscanf(buf, "%s %s %s %s", command_text, room_id, date_of_arrival, date_of_departure) != 4)
                    {
                        write(tdL.cl, "Invalid command format\n", 23);
                        sleep(1);
                        write(tdL.cl, MAGIC_NUMBER, 20);
                        return;
                    }

                    printf("%s:%s:%s\n", room_id, date_of_arrival, date_of_departure);
                    if (rezervare(logged_user, room_id, date_of_arrival, date_of_departure))
                    {
                        write(tdL.cl, "Rezervare efectuata cu succes\n", 30);
                    }
                    else
                    {
                        write(tdL.cl, "Rezervarea nu a putut fi efectuata\n", 35);
                    }
                }
                else if (strncmp(buf, "sterge-rezervare", 16) == 0)
                {
                    if (sterge_rezervare(buf, logged_user))
                    {
                        write(tdL.cl, "Succes\n", 7);
                    }
                    else
                    {
                        write(tdL.cl, "Nu s-a efectuat stergerea\n", 26);
                    }
                }
                else
                {
                    write(tdL.cl, "Invalid command\n", 17);
                }
            }
            else if (user_authenticated == 2)
            {
                // admin este authentificat
                if (strncmp(buf, "vizualizare-rezervari", 21) == 0)
                {
                    vizualizare_rezervari(tdL.cl);
                }
                else if (strncmp(buf, "sterge-tot", 10) == 0)
                {
                    int linii_sterse = sterge_tot();
                    sprintf(buf, "Linii sterse: %d\n", linii_sterse);
                    write(tdL.cl, buf, sizeof(buf));
                }
                else
                {
                    write(tdL.cl, "Invalid command\n", 17);
                }
            }
            else
            {
                write(tdL.cl, "You are not authenticated. Cannot process command\n", 50);
            }
        }

        sleep(1);
        write(tdL.cl, MAGIC_NUMBER, 20);
    }
}

int sterge_tot()
{
    xmlKeepBlanksDefault(0);

    // parseaza xmlDoc
    xmlDoc *doc = xmlReadFile(FISIER_REZERVARI, NULL, 0);
    if (doc == NULL)
    {
        fprintf(stderr, "Failed to parse the document.\n");
        return 0;
    }

    int noduri_sterse = 0;
    xmlNode *root = xmlDocGetRootElement(doc);
    while (root)
    {
        if (root->type == XML_ELEMENT_NODE)
        {
            xmlNode *entry = root->children;
            while (entry)
            {
                if (entry->type == XML_ELEMENT_NODE)
                {

                    xmlNode *to_delete = entry;
                    entry = entry->next;
                    xmlUnlinkNode(to_delete);
                    xmlFreeNode(to_delete);
                    noduri_sterse += 1;
                }
                else
                {
                    entry = entry->next;
                }
            }
        }

        root = root->next;
    }

    xmlSaveFormatFile(FISIER_REZERVARI, doc, 1);

    xmlFreeDoc(doc);
    xmlCleanupParser();

    return noduri_sterse;
}

int sterge_rezervare(char *command, char *username)
{
    xmlKeepBlanksDefault(0);
    // parseaza comanda
    char command_text[20], date_of_arrival[20], room_id[20];
    if (sscanf(command, "%s %s %s", command_text, room_id, date_of_arrival) != 3)
    {
        printf("Invalid command format\n");
        return 0;
    }

    // parseaza xmlDoc
    xmlDoc *doc = xmlReadFile(FISIER_REZERVARI, NULL, 0);
    if (doc == NULL)
    {
        fprintf(stderr, "Failed to parse the document.\n");
        return 0;
    }

    int rezervare_gasita = 0;
    xmlNode *root = xmlDocGetRootElement(doc);
    while (root && !rezervare_gasita)
    {
        if (root->type == XML_ELEMENT_NODE)
        {
            xmlNode *entry = root->children;
            while (entry && !rezervare_gasita)
            {
                if (entry->type == XML_ELEMENT_NODE)
                {

                    xmlNode *xml_username_node = entry->children;
                    xml_username_node = xml_username_node;
                    char *xml_username = (char *)xmlNodeGetContent(xml_username_node);

                    if (strcmp(username, xml_username) == 0) // daca a gasit un username corect
                    {
                        xmlNode *xml_date_of_arrival_node = entry->children;
                        xml_date_of_arrival_node = xml_date_of_arrival_node->next;
                        char *xml_date_of_arrival = (char *)xmlNodeGetContent(xml_date_of_arrival_node);

                        xmlNode *xml_room_id_node = entry->children;
                        xml_room_id_node = xml_room_id_node->next->next->next;
                        char *xml_room_id = (char *)xmlNodeGetContent(xml_room_id_node);

                        if (strcmp(xml_date_of_arrival, date_of_arrival) == 0 && strcmp(xml_room_id, room_id) == 0) // daca date if arrival si room_id se potriveste
                        {
                            xmlUnlinkNode(entry);
                            xmlFreeNode(entry);
                            rezervare_gasita = 1;
                        }
                    }
                }

                entry = entry->next;
            }
        }

        root = root->next;
    }

    xmlSaveFormatFile(FISIER_REZERVARI, doc, 1);

    xmlFreeDoc(doc);
    xmlCleanupParser();

    return rezervare_gasita;
}

void parseNode(xmlNode *node, int fd)
{
    xmlKeepBlanksDefault(0);
    xmlNode *currentNode = NULL;
    int index = 0;
    char buf[100];
    for (currentNode = node; currentNode; currentNode = currentNode->next)
    { // nivel entry
        if (currentNode->type == XML_ELEMENT_NODE)
        {
            xmlNode *propertyNode = NULL;

            memset(buf, '\0', sizeof(buf));
            sprintf(buf, "Entry %d: ", ++index);
            write(fd, buf, sizeof(buf));

            for (propertyNode = currentNode->children; propertyNode; propertyNode = propertyNode->next)
            { // nivel proprietate
                if (propertyNode->type == XML_ELEMENT_NODE)
                {
                    memset(buf, '\0', sizeof(buf));
                    sprintf(buf, "%s:%s, ", propertyNode->name, xmlNodeGetContent(propertyNode));
                    write(fd, buf, sizeof(buf));
                }
            }

            memset(buf, '\0', sizeof(buf));
            sprintf(buf, "\n");
            write(fd, buf, sizeof(buf));
        }
    }
}

void vizualizare_rezervari_client(int fd, char *username)
{
    xmlKeepBlanksDefault(0);
    // se cauta rezervarile care exista deja pentru username primit ca parametru
    // parseaza xmlDoc
    xmlDoc *doc = xmlReadFile(FISIER_REZERVARI, NULL, 0);
    if (doc == NULL)
    {
        fprintf(stderr, "Failed to parse the document.\n");
        return;
    }
    int index = 0;
    xmlNode *root = xmlDocGetRootElement(doc);
    while (root)
    {
        if (root->type == XML_ELEMENT_NODE)
        {
            xmlNode *entry = root->children;
            while (entry)
            {
                if (entry->type == XML_ELEMENT_NODE)
                {
                    xmlNode *xml_username_node = entry->children;
                    xml_username_node = xml_username_node;
                    char *xml_username = (char *)xmlNodeGetContent(xml_username_node);

                    printf("%s  %s\n", username, xml_username);
                    if (strcmp(username, xml_username) == 0)
                    {
                        xmlNode *propertyNode = NULL;
                        char buf[100];  
                        memset(buf, '\0', sizeof(buf));
                        sprintf(buf, "Entry %d: ", ++index);
                        write(fd, buf, sizeof(buf));

                        for (propertyNode = entry->children; propertyNode; propertyNode = propertyNode->next)
                        { // nivel proprietate
                            if (propertyNode->type == XML_ELEMENT_NODE)
                            {
                                memset(buf, '\0', sizeof(buf));
                                sprintf(buf, "%s:%s, ", propertyNode->name, xmlNodeGetContent(propertyNode));
                                write(fd, buf, sizeof(buf));
                            }
                        }

                        memset(buf, '\0', sizeof(buf));
                        sprintf(buf, "\n");
                        write(fd, buf, sizeof(buf));
                    }
                }

                entry = entry->next;
            }
        }

        root = root->next;
    }
}

void vizualizare_rezervari(int fd)
{
    xmlKeepBlanksDefault(0);
    // parseaza xmlDoc
    xmlDoc *doc = xmlReadFile(FISIER_REZERVARI, NULL, 0);
    if (doc == NULL)
    {
        fprintf(stderr, "Failed to parse the document.\n");
        return;
    }

    xmlNode *root = xmlDocGetRootElement(doc);
    while (root)
    {
        if (root->type == XML_ELEMENT_NODE)
        {
            parseNode(root->children, fd);
        }

        root = root->next;
    }

    // eliobereaza din memorie
    xmlFreeDoc(doc);
    xmlCleanupParser();

    return;
}

void vizualizare_data(char *date, int fd)
{
    xmlKeepBlanksDefault(0);
    // partea 1, se cauta rezervarile care exista deja pentru data primita ca parametru si se pastreaza in variabila "camere_ocupate"
    // parseaza xmlDoc
    xmlDoc *doc = xmlReadFile(FISIER_REZERVARI, NULL, 0);
    if (doc == NULL)
    {
        fprintf(stderr, "Failed to parse the document.\n");
        return;
    }

    // adauga room_id al camerelor ocupate in ziua data ca parametru
    char *camere_ocupate[NR_MAXIM_CAMERE];
    int index_camere_ocupate = 0;
    xmlNode *root = xmlDocGetRootElement(doc);
    while (root)
    {
        if (root->type == XML_ELEMENT_NODE)
        {
            xmlNode *entry = root->children;
            while (entry)
            {
                if (entry->type == XML_ELEMENT_NODE)
                {
                    xmlNode *xml_date_of_arrival_node = entry->children;
                    xml_date_of_arrival_node = xml_date_of_arrival_node->next;
                    char *xml_date_of_arrival = (char *)xmlNodeGetContent(xml_date_of_arrival_node);

                    xmlNode *xml_date_of_departure_node = entry->children;
                    xml_date_of_departure_node = xml_date_of_departure_node->next->next;
                    char *xml_date_of_departure = (char *)xmlNodeGetContent(xml_date_of_departure_node);

                    xmlNode *xml_room_id_node = entry->children;
                    xml_room_id_node = xml_room_id_node->next->next->next;
                    char *xml_room_id = (char *)xmlNodeGetContent(xml_room_id_node);

                    printf("%s %s\n", xml_date_of_arrival, xml_date_of_departure);
                    if (compare_date(xml_date_of_arrival, date) <= 0 && compare_date(xml_date_of_departure, date) >= 0)
                    { // exista o rezervare care incepe inainte sa ajung si se termina dupa ce ajung (sau in exact aceeasi zi)
                        camere_ocupate[index_camere_ocupate++] = xml_room_id;
                    }
                }

                entry = entry->next;
            }
        }

        root = root->next;
    }

    xmlFreeDoc(doc);
    xmlCleanupParser();

    // Partea 2, se afiseaza toate camerele care nu au room_id printre cele afisate
    // parseaza xmlDoc
    doc = xmlReadFile(FISIER_CAMERE, NULL, 0);
    if (doc == NULL)
    {
        fprintf(stderr, "Failed to parse the document.\n");
        return;
    }

    root = xmlDocGetRootElement(doc);
    while (root)
    {
        if (root->type == XML_ELEMENT_NODE)
        {
            xmlNode *entry = root->children;
            while (entry)
            {
                if (entry->type == XML_ELEMENT_NODE)
                {
                    xmlNode *xml_room_id_node = entry->children;
                    xml_room_id_node = xml_room_id_node;
                    char *xml_room_id = (char *)xmlNodeGetContent(xml_room_id_node);

                    xmlNode *xml_comfort_type_node = entry->children;
                    xml_comfort_type_node = xml_comfort_type_node->next;
                    char *xml_comfort_type = (char *)xmlNodeGetContent(xml_comfort_type_node);

                    xmlNode *xml_price_node = entry->children;
                    xml_price_node = xml_price_node->next->next;
                    char *xml_price = (char *)xmlNodeGetContent(xml_price_node);

                    int camera_nu_e_disponibila = 0;
                    for (int i = 0; i < index_camere_ocupate; i++)
                    {
                        if (strcmp(xml_room_id, camere_ocupate[i]) == 0)
                        {
                            camera_nu_e_disponibila = 1;
                        }
                    }

                    if (!camera_nu_e_disponibila)
                    {
                        char mesaj[100];
                        sprintf(mesaj, "room_id: %s, comfort_type: %s, price: %s\n", xml_room_id, xml_comfort_type, xml_price);
                        write(fd, mesaj, sizeof(mesaj));
                    }
                }

                entry = entry->next;
            }
        }

        root = root->next;
    }

    xmlFreeDoc(doc);
    xmlCleanupParser();
}

void vizualizare_data2(int fd, char *date_of_arrival, char *date_of_departure)
{
    xmlKeepBlanksDefault(0);
    // partea 1, se cauta rezervarile care exista deja si sunt facute intre datele primite ca parametru si se pastreaza in variabila "camere_ocupate"
    // parseaza xmlDoc
    xmlDoc *doc = xmlReadFile(FISIER_REZERVARI, NULL, 0);
    if (doc == NULL)
    {
        fprintf(stderr, "Failed to parse the document.\n");
        return;
    }

    // adauga room_id al camerelor ocupate in ziua data ca parametru
    char *camere_ocupate[NR_MAXIM_CAMERE];
    int index_camere_ocupate = 0;
    xmlNode *root = xmlDocGetRootElement(doc);
    while (root)
    {
        if (root->type == XML_ELEMENT_NODE)
        {
            xmlNode *entry = root->children;
            while (entry)
            {
                if (entry->type == XML_ELEMENT_NODE)
                {
                    xmlNode *xml_date_of_arrival_node = entry->children;
                    xml_date_of_arrival_node = xml_date_of_arrival_node->next;
                    char *xml_date_of_arrival = (char *)xmlNodeGetContent(xml_date_of_arrival_node);

                    xmlNode *xml_date_of_departure_node = entry->children;
                    xml_date_of_departure_node = xml_date_of_departure_node->next->next;
                    char *xml_date_of_departure = (char *)xmlNodeGetContent(xml_date_of_departure_node);

                    xmlNode *xml_room_id_node = entry->children;
                    xml_room_id_node = xml_room_id_node->next->next->next;
                    char *xml_room_id = (char *)xmlNodeGetContent(xml_room_id_node);

                    if (compare_date(xml_date_of_arrival, date_of_departure) <= 0 && compare_date(xml_date_of_departure, date_of_arrival) >= 0)
                    { // exista o rezervare care incepe inainte sa plec eu si se termina dupa ce ajung eu, deci exista conflict
                        camere_ocupate[index_camere_ocupate++] = xml_room_id;
                    }
                }

                entry = entry->next;
            }
        }

        root = root->next;
    }

    xmlFreeDoc(doc);
    xmlCleanupParser();

    // Partea 2, se afiseaza toate camerele care nu au room_id printre cele afisate
    // parseaza xmlDoc
    doc = xmlReadFile(FISIER_CAMERE, NULL, 0);
    if (doc == NULL)
    {
        fprintf(stderr, "Failed to parse the document.\n");
        return;
    }

    root = xmlDocGetRootElement(doc);
    while (root)
    {
        if (root->type == XML_ELEMENT_NODE)
        {
            xmlNode *entry = root->children;
            while (entry)
            {
                if (entry->type == XML_ELEMENT_NODE)
                {
                    xmlNode *xml_room_id_node = entry->children;
                    xml_room_id_node = xml_room_id_node;
                    char *xml_room_id = (char *)xmlNodeGetContent(xml_room_id_node);

                    xmlNode *xml_comfort_type_node = entry->children;
                    xml_comfort_type_node = xml_comfort_type_node->next;
                    char *xml_comfort_type = (char *)xmlNodeGetContent(xml_comfort_type_node);

                    xmlNode *xml_price_node = entry->children;
                    xml_price_node = xml_price_node->next->next;
                    char *xml_price = (char *)xmlNodeGetContent(xml_price_node);

                    int camera_nu_e_disponibila = 0;
                    for (int i = 0; i < index_camere_ocupate; i++)
                    {
                        if (strcmp(xml_room_id, camere_ocupate[i]) == 0)
                        {
                            camera_nu_e_disponibila = 1;
                        }
                    }

                    if (!camera_nu_e_disponibila)
                    {
                        char mesaj[100];
                        sprintf(mesaj, "room_id: %s, comfort_type: %s, price: %s\n", xml_room_id, xml_comfort_type, xml_price);
                        write(fd, mesaj, sizeof(mesaj));
                    }
                }

                entry = entry->next;
            }
        }

        root = root->next;
    }

    xmlFreeDoc(doc);
    xmlCleanupParser();
}

int compare_date(const char *date1, const char *date2)
{
    struct tm tm1 = {0};
    struct tm tm2 = {0};

    // Parse date strings into tm structures
    if (strptime(date1, "%Y-%m-%d", &tm1) == 0 || strptime(date2, "%Y-%m-%d", &tm2) == 0)
    {
        fprintf(stderr, "Error parsing date strings\n");
        return 0; // or some default value
    }

    // Convert tm structures to time_t
    time_t time1 = mktime(&tm1);
    time_t time2 = mktime(&tm2);

    // Compare dates
    if (time1 < time2)
    {
        return -1;
    }
    else if (time1 > time2)
    {
        return 1;
    }
    else
    {
        return 0;
    }
}

int verificare_camera_exista(char *room_id)
{
    // verifica daca camera cu room_id din aprametru exista in xml. Returneaza 1 daca exista, 0 altfel
    // parseaza xmlDoc
    xmlDoc *doc = xmlReadFile(FISIER_CAMERE, NULL, 0);
    if (doc == NULL)
    {
        fprintf(stderr, "Failed to parse the document.\n");
        return 0;
    }

    int camera_exista = 0;

    xmlNode *root = xmlDocGetRootElement(doc);
    while (root)
    {
        if (root->type == XML_ELEMENT_NODE)
        {
            xmlNode *entry = root->children;
            while (entry)
            {
                if (entry->type == XML_ELEMENT_NODE)
                {
                    xmlNode *xml_room_id_node = entry->children;
                    xml_room_id_node = xml_room_id_node;
                    char *xml_room_id = (char *)xmlNodeGetContent(xml_room_id_node);
                    printf("%s %s\n", xml_room_id, room_id);
                    if (strcmp(xml_room_id, room_id) == 0)
                    {
                        camera_exista = 1;
                    }
                }

                entry = entry->next;
            }
        }

        root = root->next;
    }

    xmlFreeDoc(doc);
    xmlCleanupParser();

    return camera_exista;
}

int verificare_camera_libera(char *room_id, char *date_of_arrival, char *date_of_departure)
{
    // verifica daca camera cu room_id este libera in perioada precizata. Returneaza 1 daca e libera, 0 altfel
    // parseaza xmlDoc
    xmlDoc *doc = xmlReadFile(FISIER_REZERVARI, NULL, 0);
    if (doc == NULL)
    {
        fprintf(stderr, "Failed to parse the document.\n");
        return 0;
    }

    int camera_libera = 1;

    xmlNode *root = xmlDocGetRootElement(doc);
    while (root)
    {
        if (root->type == XML_ELEMENT_NODE)
        {
            xmlNode *entry = root->children;
            while (entry)
            {
                if (entry->type == XML_ELEMENT_NODE)
                {
                    xmlNode *xml_date_of_arrival_node = entry->children;
                    xml_date_of_arrival_node = xml_date_of_arrival_node->next;
                    char *xml_date_of_arrival = (char *)xmlNodeGetContent(xml_date_of_arrival_node);

                    xmlNode *xml_date_of_departure_node = entry->children;
                    xml_date_of_departure_node = xml_date_of_departure_node->next->next;
                    char *xml_date_of_departure = (char *)xmlNodeGetContent(xml_date_of_departure_node);

                    xmlNode *xml_room_id_node = entry->children;
                    xml_room_id_node = xml_room_id_node->next->next->next;
                    char *xml_room_id = (char *)xmlNodeGetContent(xml_room_id_node);

                    if (compare_date(xml_date_of_arrival, date_of_departure) <= 0 && compare_date(xml_date_of_departure, date_of_arrival) >= 0)
                    { // exista o rezervare care incepe inainte sa plec eu si se termina dupa ce ajung eu, deci exista conflict
                        camera_libera = 0;
                    }
                }

                entry = entry->next;
            }
        }

        root = root->next;
    }

    xmlFreeDoc(doc);
    xmlCleanupParser();

    return camera_libera;
}

int rezervare(char *client_name, char *room_id, char *date_of_arrival, char *date_of_departure)
{
    if (!verificare_camera_exista(room_id))
    {
        printf("Camera ceruta nu exista\n");
        return 0;
    }

    if (!verificare_camera_libera(room_id, date_of_arrival, date_of_departure))
    {
        printf("camera ceruta nu e libera\n");
        return 0;
    }

    xmlDoc *doc = xmlReadFile(FISIER_REZERVARI, NULL, 0); // deschide xml
    if (doc == NULL)
    {
        fprintf(stderr, "Failed to parse the document.\n");
        return 0;
    }

    xmlNode *root = xmlDocGetRootElement(doc);

    xmlNodePtr entry = xmlNewChild(root, NULL, "entry", NULL);

    // adauga valori pentru entry
    xmlNewChild(entry, NULL, "client_name", client_name);
    xmlNewChild(entry, NULL, "date_of_arrival", date_of_arrival);
    xmlNewChild(entry, NULL, "date_of_departure", date_of_departure);
    xmlNewChild(entry, NULL, "room_id", room_id);

    xmlSaveFormatFile(FISIER_REZERVARI, doc, 1);

    xmlFreeDoc(doc); // elibereaza din memorie
    xmlCleanupParser();

    return 1;
}
