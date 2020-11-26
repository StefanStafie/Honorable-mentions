# Chestionar Online
Se cere realizarea unui chestionar online folosind doar pl/sql pentru logica programului. 


## A. Primul pas
(livrabil în laboratorul 12, valorând 5 puncte, prezentabil în timpul orelor de laborator via un protocol de comunicare agreat de profesor):

Să se construiască în PLSQL:

O funcție "urmatoarea_intrebare" ce primeste doi parametri:

parametrul email reprezintă adresa de email a unui utilizator care există în sistem (nu este nevoie să verificați, se va verifica în altă parte)
parametrul raspuns (ce poate fi și null). Dacă acest parametru nu este nul, el va fi conține un ID a unei întrebări din testul generat pentru utilizatorul identificat de parametrul email împreună cu ID-urile răspunsurilor pe care acesta le-a considerat corecte pentru acea întrebare.
Funcția va returna fie următoarea întrebare la care utilizatorul nu a răspuns încă, fie punctajul final în cazul în care s-a răspuns la toate întrebările. Pentru aceasta va face următorii pași:

Caută să vadă dacă în baza de date există un test asociat utilizatorului primit având contul ca parametru. Dacă nu există atunci va crea un test (vezi secțiunea "Creare test").
Dacă cel de-al doilea parametru este nenul, atunci el reprezintă răspunsul unei întrebări din test și este adăugat pentru întrebarea corespunzătoare pentru a fi utlizat la calculul punctajului final (vezi secțiunea "Calcul punctaj").
Așa cum s-a precizat și în secțiunea "creare test", întrebările au asociat un număr de ordine pentru testul ce a fost generat. Funcția, în continuare, va prelua următoarea întrebare la care încă nu s-a răspuns și va fi trimisă către utilizator (în cazul când funcția tocmai a generat testul, va întoarce prima întrebare).
Dacă răspunsul dat este pentru ultima întrebare, se va calcula și stoca în baza de date punctajul testului după regulile stipulate în secțiunea "calcul punctaj" și valoarea îi va fi afișată utilizatorului.
Observație: Dacă se apelează funcția având null ca și răspuns atunci nu se va adăuga nimic în baza de date. Apelul de mai multe ori a acestei funcții având valoarea răspunsului null va avea acelasi efect: întoarcerea aceleiași întrebări (următoarea din test la care încă nu s-a răspuns). Nu se va putea reveni la o întrebare răspunsă în trecut și nici nu se va putea sări peste o întrebare pentru a reveni ulterior la ea.


## Creare test
Testul creat pentru un utilizator va fi format dintr-un număr de 10 întrebări de tip grilă din diverse domenii (sau subdomenii ale unui aceluiași domeniu). Cele 10 întrebări vor avea asociat un număr de ordine, astfel încât mereu se va putea găsi care este "următoarea întrebare" la care nu s-a răspuns, sau ultima întrebare la care s-a răspuns.

Pentru generarea unei singure întrebări se va proceda astfel:

Se va alege în mod aleatoriu un domeniu (sau subdomeniu) din care utilizatorul nu a fost încă întrebat nici o întrebare. Din cauză că în fiecare domeniu există mai multe întrebări posibile, se va alege una dintre ele.
Pentru întrebarea aleasă la punctul anterior, într-o tabelă de răspunsuri vor exista mai multe răspunsuri posibile (de exemplu 10 răspunsuri). Se vor prelua 6 răspunsuri care sunt compatibile cu acea întrebare (vezi exemplu), dintre care cel puțin un răspuns va fi corect. Ordinea lor va fi randomizată. ID-urile celor 6 răspunsuri seletate pentru întrebare vor fi memorate într-o tabelă împreună cu ID-ul întrebării, un număr de ordine a apariției întrebării în test și un câmp reprezentând răspunsurile date de utilizator (inițial null). Evident, pentru a ști că întrebarea face parte din testul unui anumit utilizator trebuie să păstrați aici și adresa de email a utilizatorului pentru care a fost generată întrebarea + cele 6 răspunsuri.
Se repetă pașii anteriori de 10 ori, pentru fiecare dintre cele 10 domenii câte o dată.
Funcția va returna o întrebare chiar după crearea testului: aceasta este prima întrebare.
Notă: Tabelele de întrebări și răspunsuri le puteți obține utilizând acest script: https://docs.google.com/document/d/1Ps0-R_4NyDkMK04Jmk3bhZiy4bKg2LIjEq_G4Scojqk/edit?usp=sharing ( acest document poate fi editat de oricine, special pentru a va permite sa adaugati restul domeniilor sau întrebări la cele existente - puneti intrebari usoare ca sa stim cu totii raspunsurile :D; cine face această temă este obligat să adauge în document măcar o întrebare !)


## Obținerea unei întrebări
La apelul funcției cu parametri email + răspuns se va proceda astfel:

Dacă răspuns este null: se verifică dacă utilizatorul are un test deja creat. Dacă există test pentru utilizator, se va returna următoarea întrebare (în ordinea dată la crearea testului). Dacă nu, se va crea testul și se va returna prima întrebare.
Dacă răspuns nu este nul, acesta va conține IDul întrebării la care s-a răspuns împreună cu IDurile răspunsurilor considerate corecte. Se va verifica dacă nu există răspuns pentru acea întrebare și dacă într-adevăr nu există, ID-urile răspunsurilor se vor stoca pentru o analiză ulterioră (când se va calcula punctajul total). După aceasta se caută întrebarea următoare. Dacă există o următoare întrebare, ea va fi returnată clientului.
Indiferent de valoarea parametrului răspuns, dacă nu există o următoare întrebare înseamnă că testul s-a terminat și va fi întors un alt tip de răspuns reprezentând punctajul obținut (vezi secțiunea "Calcul punctaj" pentru detalii).

##Calcul punctaj
Pentru fiecare întrebare din testul unui utilizator, se va confrunta câmpul de răspunsuri corecte cu cel de răspunsuri date de utilizator. Fiecare întrebare va valora 10 puncte ce vor fi distribuite uniform între răspunsurile corecte alese pentru acea întrebare (sa zicem X; unde X = 10/nr_raspunsuri_corecte). Răspunsurile corecte vor aduce un punctaj pozitiv (+X) utilizatorului în timp ce cele incorecte vor scadea din punctajul total pentru acea întrebare (-X). Nici o întrebare nu va fi punctată negativ (minim=0).

Suma acestor punctaje va reprezenta punctajul final.

De exemplu, să zicem că prima întrebare a avut 10 răspunsuri posibile: 3 corecte și 7 incorecte. La generare s-a ales 6 răspunsuri: random un răspuns corect și încă 5 (din cele 9 rămase). Din cele 5 alese în partea a doua s-a mai ales încă un răspuns corect. Așadar la utilizator au ajuns două răspunsuri corecte și 4 incorecte. Deoarece sunt 2 răspunsuri corecte și găsirea acestora trebuie să aducă cele 10 puncte utilizatorului, fiecare răspuns va valora câte 5 puncte. Dacă utilizatorul bifează doar cele două răspunsuri corecte, el va obține 5 + 5 = 10 puncte. Dacă va bifa pe cele două corecte dar și unul incorect, el va obține 5 + 5 - 5 = 5 puncte. Dacă bifează toate răspunsurile, el totuși nu va primi -10 puncte ci nu va primi nimic (0 puncte). Si acesta este punctajul pentru prima întrebare. Dacă la a doua întrebare au fost selectate din întâmplare 3 răspunsuri corecte, fiecare va valora +3.33 puncte și fiecare răspuns gresit bifat va valora -3.33 etc.


##Exemplu
S-a autentificat (printr-o verificare a tabelei utilizatori), utilizatorul cu adresa de email "ion@info.uaic.ro". Se apelează funcția urmatoarea_intrebare având ca parametru doar adresa de email. Din cauză că nu există un test asociat acestui utilizator, se va purcede la crearea lui: Se alege pentru început domeniul geometrie și din cele 5 întrebări disponibile în acest domeniu, a fost aleasă întrebarea "Care din următoarele figuri geometrice au 4 laturi ?". Răspunsurile posibile pentru această întrebare sunt: *pătrat, cerc, triunghi, pentagon, *romb, *dreptunghi, *patrulater, hexagon, octogon, decagon, dodecagon. Sistemul trebuie să aleagă un răspuns corect și apoi încă 5 răspunsuri aleatoare. Spre exemplu a fost ales răspunsul *patrat ca fiind corect (în baza de date există un marker care identifică răspunsul corect, în exemplu le-am marcat cu *) împreună cu alte 5 raspunsuri: triunghi, *romb, hexagon, octogon și dodecagon.

Pentru că aceste valori să fie identificate, se vor utiliza ID-urile lor.

De exemplu, în tabela de teste va fi adăugat un rând conținând valorile: 'ion@info.uaic.ro', 1, 23, 153, 149, 219, 74, 45, 317, '74,149', null

Având semnificațiile:

identificare utilizaotrului
numărul de ordine al întrebării
ID-ul întrebării
cele 6 IDuri ale răspunurilor alese (în ordinea în care vor fi date utilizatorului - dar neapărat o ordine randomizată)
IDurile răspunsurilor corecte (ca și CSV)
IDurile răsunsurilir date de utilizator, ca și CSV - inițial null pentru că nu a răspuns la întrebare
Procesul va fi repetat până la adăugarea a 10 întrebări din domenii diferite în test. Atenție, dintr-un domeniu poate fi aleasă doar o întrebare, deși pot exista mai multe întrebări (putea pica și altă întrebare de geometrie).

După crearea întregului test, se va selecta prima întrebare (cea care este dată ca exemplu) și va fi dată utilizatorului într-un format preferat de voi. De exemplu, dacă ar fi să îi trimiteți un JSON cu întrebarea, acesta ar arăta astfel (nu este neapărat să fie format json sau codificarea să fie exact aceasta):

 { 
"id_intrebare":23, "intrebare":"Care din următoarele figuri geometrice au 4 laturi ?", 
"id_rasp_1":153, "raspuns_1":"dodecagon",
"id_rasp_2":149, "raspuns_2":"patrat",
"id_rasp_3":219, "raspuns_3":"octogon",
"id_rasp_4":74,  "raspuns_4":"romb",
"id_rasp_5":45,  "raspuns_5":"hexagon",
"id_rasp_6":317, "raspuns_6":"triunghi",
}  
Evident, în acest format de intrebare nu se vor trimite și ID-urile ce indică răspunsurile corecte - ele trebuie să fie știute doar de către serverul de baze de date si vor fi utilizate în calculul punctajului. Răspunsul furnizat ca și parametru la un nou apel al funcției urmatoarea_intrebare poate spre exemplu avea formatul "23:149,219,74".

Apelul ulterior al funcției având parametri ca prim parametru "ion@info.uaic.ro" poate avea ca răspuns aceeași primă întrebare dacă nu s-a furnizat nici un răspuns sau a doua întrebare dacă cel de-al doilea parametru este "23:149,219,74", caz în care se va verifica că 23 este o întrebare la care nu s-a răspuns, se va stoca răspunsul în locul câmpului ce era null ma sus '149,219,74' și se va genera un JSON pentru întrebarea cu numărul de ordine 2 pentru a fi returnat către client.

Dacă acesta funcția nu găsește o întrebare următoare (pentru că nu mai sunt întrebări), se va calcula punctajul ca fiind suma punctajului fiecărei întrebăi in parte.

Pentru că în final vor fi acordate 100 de puncte, fiecare dintre cele 10 întrebări existente în test va valora câte 10 puncte. Fiecare răspuns corect va fi punctat identic. Din acest motiv, în exemplul nostru în care sunt două răspunsuri corecte, fiecare va valora 5 puncte. De asemenea, fiecare răspuns incorect va trebui penalizat, penalizare se face cu aceeași valoare (de 5 puncte în acest caz), dar fără a deveni negativ. Pentru răspunsul dat de Ion ("23:149,219,74"), punctajele pentru fiecare răspuns vor fi așadar de +5, -5, +5. Punctele obținute în urma răspunsului fiind de 5.

Se face suma dintre punctajele acordate la fiecare întrebare și funcția urmatoarea_intrebare va furniza în final un json de tipul {"Total":95} (la celelalte întrebări s-a răspuns perfect).


## B. Pasul al doilea
(livrabil în săptămâna 13, valorând 5 puncte, prezentabil în timpul orelor de laborator via un protocol de comunicare agreat de profesor):

Să se realizeze o aplicație web simplă, respectând paradigma REST, având două rute / puncte de intrare / entry points:

## API REST - (partea 1)
Aplicația client face un apel către un prim entry point din APIul REST având ca parametru (dat prin POST) o adresa de email.
Primul entry point din API-ul REST va primi ca parametru o adresa de email și va returna o valoare construită ca hash peste adresa de email și timpul curent al serverului. Aceste două valori (adresa de email și hashul vor fi stocate într-o bază de date pe server). Valoarea hashului returnat împreună cu adresa de email vor fi stocate la client în localStorage (vezi https://developer.mozilla.org/en-US/docs/Web/API/Window/localStorage).
## API REST - (partea 2)
Aplicația client va trimite către un al doilea entry point din API-ul REST o cerere conținând adresa de email și hashul (tot prin POST pentru că va modifica starea serverului).
Al doilea entry point va verifica dacă cele două informații recepționate există în baza de date și, dacă aceste există și se validează că utilizatorul are hashul corect, se va apela funcția de generare a unei întrebări (din PLSQL). Răspunsul dat de către această funcție va fi furnizat către client care va randa întrebarea și o va afișa în pagina web (sau punctajul total în cazul în care răspunsul, tot de tip JSON, oferă această informație).
Dacă există deja o întrebare afișătă și se dorește a se da răspunsul la aceasta, se va apela aceeași rută având ca parametri pe lângă adresa de email + hashul de mai sus și un șir de caractere reprezentând răspunsul la întrebare (poate fi json, csv, etc. - aveți grijă doar ca în final să ajungă într-un format cunoscut la funcția "urmatoarea_intrebare" creată anterior).
Acest punct de intrare poate returna fie următorea întrebare, fie punctajul total. Afisați-le în mod corespunzător în aplicația client.

## Bonus
(2 puncte) Afișați utilizatorului nu numai punctajul final ci și care erau răspunsurile corecte + ce a bifat el împreună cu punctajul obțiunt sau scăzut pentru fiecare răspuns în parte.