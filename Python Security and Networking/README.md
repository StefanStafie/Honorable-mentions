# Sistem securizat de transmitere mesaje
Implementati o infrastructura de comunicație ce folosește criptosistemul
AES pentru criptarea traficului intre două noduri A și B cu următoarele
caracteristici:
- se consideră un nod KM (key manager) care deține trei chei pe 128
biți: K1, K2 și K3:
- cheia K1 este asociată cu modul de operare ECB;
- cheia K2 este asociată cu modul de operare CBC (se consideră că
vectorul de inițializare are o valoare fixată, cunoscută in prealabil
de cele două noduri A și B);
- cheia K3 este utilizată pentru criptarea cheilor K1 sau K2. Cheia
K3 este deținută din start de nodurile A, B și KM.
- Pentru a iniția o sesiune de comunicare securizată, nodul A trimite un
mesaj către nodul B ˆın care comunică modul de operare (ECB sau
CBC); de asemenea, nodul A transmite un mesaj nodului KM prin
care cere cheia corespunzatoare (K1 pentru modul de operare ECB,
respectiv K2 pentru modul de operare CBC).
- Nodul B, la primirea mesajului de la nodul A, cere nodului KM cheia
corespunzatoare (K1 pentru modul de operare ECB, respectiv K2 pentru modul de operare CBC).
- nodul KM va cripta cheia cerută (K1 sau K2 in funcție de modul de
operare ales) ca un singur bloc, utilizând criptosistemul AES cu cheia
K3 și va trimite mesajul astfel obținut ca răspuns pentru nodurile A și
B;
- cele două noduri A și B vor decripta mesajul primit de la KM și vor
obține astfel cheia cerută;
- nodul B trimite, după primirea cheii, un mesaj nodului A prin care il
anunță că poate să inceapă comunicarea;
- nodul A criptează conținutul unui fișier text utilizând AES, cheia primită
de la KM și modul de operare ales. A va transmite nodului B blocurile
de criptotext obținute pe rând, iar nodul B va decripta blocurile primite și va afișa rezultatul obținut.

Pasi pentru testare:
  1. Se deschid, in ordinea urmatoare, procesele: main, clientA, clientB (cel mai bine folosind:  click_dreapta -> Run file in python console)
  2. In consola de la clientA se alege modul de lucru (se scrie 1 sau 2) pentru ECB sau CBC
  3. In consola de la main se pot observa request-urile pentru K1 sau K2
  4. In consola de la clientB se poate observa textul transmis de clientA
 
Altele:
  In CryptoService se pot folosi 
    
    - test_ecb()
    - test_cbc()
  pentru testarea criptarilor (separat de comunicare prin network)
