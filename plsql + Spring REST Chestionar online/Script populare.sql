DROP TABLE INTREBARI;
DROP TABLE RASPUNSURI;

CREATE TABLE INTREBARI (
  DOMENIU VARCHAR2(5),
  ID VARCHAR2(8) NOT NULL PRIMARY KEY,
  TEXT_INTREBARE VARCHAR2(1000) NOT NULL
)
/

CREATE TABLE RASPUNSURI(
  Q_ID VARCHAR2(8),
  ID VARCHAR2(8) NOT NULL PRIMARY KEY,
  TEXT_RASPUNS VARCHAR2(1000) NOT NULL,
  CORECT VARCHAR2(1) 
)
/






-- Domeniul 1 (geometrie)
INSERT INTO INTREBARI VALUES('D1','Q1','Care din urmatoarele figur geometrice au 4 laturi ?');
INSERT INTO RASPUNSURI VALUES ('Q1','A1','Patrat','1');
INSERT INTO RASPUNSURI VALUES ('Q1','A2','Cerc','0');
INSERT INTO RASPUNSURI VALUES ('Q1','A3','Triunghi','0');
INSERT INTO RASPUNSURI VALUES ('Q1','A4','Romb','1');
INSERT INTO RASPUNSURI VALUES ('Q1','A5','Dreptunghi','1');
INSERT INTO RASPUNSURI VALUES ('Q1','A6','Paralelogram','1');
INSERT INTO RASPUNSURI VALUES ('Q1','A7','Hexagon','0');
INSERT INTO RASPUNSURI VALUES ('Q1','A8','Dodecagon','0');
INSERT INTO RASPUNSURI VALUES ('Q1','A9','Pentagon','0');
INSERT INTO RASPUNSURI VALUES ('Q1','A10','Poligon','0');

INSERT INTO INTREBARI VALUES('D1','Q2','Care din urmatoarele figuri geometrice au mai mult de 4 colturi ?');
INSERT INTO RASPUNSURI VALUES ('Q2','A11','Patrat','0');
INSERT INTO RASPUNSURI VALUES ('Q2','A12','Cerc','1');
INSERT INTO RASPUNSURI VALUES ('Q2','A13','Triunghi','0');
INSERT INTO RASPUNSURI VALUES ('Q2','A14','Romb','0');
INSERT INTO RASPUNSURI VALUES ('Q2','A15','Dreptunghi','0');
INSERT INTO RASPUNSURI VALUES ('Q2','A16','Paralelogram','0');
INSERT INTO RASPUNSURI VALUES ('Q2','A17','Hexagon','1');
INSERT INTO RASPUNSURI VALUES ('Q2','A18','Dodecagon','1');
INSERT INTO RASPUNSURI VALUES ('Q2','A19','Pentagon','1');
INSERT INTO RASPUNSURI VALUES ('Q2','A20','Poligon','0');

INSERT INTO INTREBARI VALUES('D1','Q3','Care din urmatoarele afirmatii sunt adevarate ?');
INSERT INTO RASPUNSURI VALUES ('Q3','A21','Triunghiul are 4 laturi','0');
INSERT INTO RASPUNSURI VALUES ('Q3','A22','Cercul are o infinitate de varfuri','1');
INSERT INTO RASPUNSURI VALUES ('Q3','A23','Suma unghiurilor unui triunghi este de 180 de grade','1');
INSERT INTO RASPUNSURI VALUES ('Q3','A24','Suma unghiurilor unui patrat este de 180 de grade','0');
INSERT INTO RASPUNSURI VALUES ('Q3','A25','Un patrat este un romb cu un unghi drept','1');
INSERT INTO RASPUNSURI VALUES ('Q3','A26','Un poligon este o linie franta inchisa','1');
INSERT INTO RASPUNSURI VALUES ('Q3','A27','Intr-un triunghi dreptunghic suma catetelor este egala cu ipotenuza','0');

INSERT INTO INTREBARI VALUES('D1','Q4','Care din urmatoarele figuri au toate laturile egale ?');
INSERT INTO RASPUNSURI VALUES ('Q4','A28','Triunghiul dreptunghic','0');
INSERT INTO RASPUNSURI VALUES ('Q4','A29','Triunghiul isoscel','0');
INSERT INTO RASPUNSURI VALUES ('Q4','A30','Triunghiul echilateral','1');
INSERT INTO RASPUNSURI VALUES ('Q4','A31','Patratul','1');
INSERT INTO RASPUNSURI VALUES ('Q4','A32','Rombul','1');
INSERT INTO RASPUNSURI VALUES ('Q4','A33','Dreptunghiul','0');
INSERT INTO RASPUNSURI VALUES ('Q4','A34','Hexagonul neregulat','0');

-- Domeniul 2 (numere)
INSERT INTO INTREBARI VALUES('D2','Q5','Care din urmatoarele sunt numere prime ?');
INSERT INTO RASPUNSURI VALUES ('Q5','A35','3','1');
INSERT INTO RASPUNSURI VALUES ('Q5','A36','7','1');
INSERT INTO RASPUNSURI VALUES ('Q5','A37','5','1');
INSERT INTO RASPUNSURI VALUES ('Q5','A38','13','1');
INSERT INTO RASPUNSURI VALUES ('Q5','A39','17','1');
INSERT INTO RASPUNSURI VALUES ('Q5','A40','9','0');
INSERT INTO RASPUNSURI VALUES ('Q5','A41','16','0');
INSERT INTO RASPUNSURI VALUES ('Q5','A42','22','0');
INSERT INTO RASPUNSURI VALUES ('Q5','A43','121','0');
INSERT INTO RASPUNSURI VALUES ('Q5','A44','14','0');

INSERT INTO INTREBARI VALUES('D2','Q6','Care din urmatoarele sunt numere pare ?');
INSERT INTO RASPUNSURI VALUES ('Q6','A45','2','1');
INSERT INTO RASPUNSURI VALUES ('Q6','A46','4','1');
INSERT INTO RASPUNSURI VALUES ('Q6','A47','6','1');
INSERT INTO RASPUNSURI VALUES ('Q6','A48','12','1');
INSERT INTO RASPUNSURI VALUES ('Q6','A49','100','1');
INSERT INTO RASPUNSURI VALUES ('Q6','A50','13','0');
INSERT INTO RASPUNSURI VALUES ('Q6','A51','15','0');
INSERT INTO RASPUNSURI VALUES ('Q6','A52','1','0');
INSERT INTO RASPUNSURI VALUES ('Q6','A53','7','0');
INSERT INTO RASPUNSURI VALUES ('Q6','A54','9','0');

INSERT INTO INTREBARI VALUES('D2','Q7','Care din urmatoarele numere sunt din sirul lui Fibonacci ?');
INSERT INTO RASPUNSURI VALUES ('Q7','A55','1','1');
INSERT INTO RASPUNSURI VALUES ('Q7','A56','2','1');
INSERT INTO RASPUNSURI VALUES ('Q7','A57','3','1');
INSERT INTO RASPUNSURI VALUES ('Q7','A58','5','1');
INSERT INTO RASPUNSURI VALUES ('Q7','A59','8','1');
INSERT INTO RASPUNSURI VALUES ('Q7','A60','13','1');
INSERT INTO RASPUNSURI VALUES ('Q7','A61','21','1');
INSERT INTO RASPUNSURI VALUES ('Q7','A62','22','0');
INSERT INTO RASPUNSURI VALUES ('Q7','A63','23','0');




-- Domeniul 3 (Flori)
INSERT INTO INTREBARI VALUES('D3','Q8','Care flori sunt sau pot fi albe ?');
INSERT INTO RASPUNSURI VALUES ('Q8','A64','Ghiocelul','1');
INSERT INTO RASPUNSURI VALUES ('Q8','A65','Margareta','1');
INSERT INTO RASPUNSURI VALUES ('Q8','A66','Trandafirul','1');
INSERT INTO RASPUNSURI VALUES ('Q8','A67','Floarea de soc','1');
INSERT INTO RASPUNSURI VALUES ('Q8','A68','Papadia','0');
INSERT INTO RASPUNSURI VALUES ('Q8','A69','Floarea de cires','1');
INSERT INTO RASPUNSURI VALUES ('Q8','A70','Crinul','1');
INSERT INTO RASPUNSURI VALUES ('Q8','A71','Bumbisorul','1');
INSERT INTO RASPUNSURI VALUES ('Q8','A72','Floarea soarelui','0');

INSERT INTO INTREBARI VALUES('D3','Q9','Care din urmatorele nume de fete sunt si nume de flori ?');
INSERT INTO RASPUNSURI VALUES ('Q9','A73','Crina','1');
INSERT INTO RASPUNSURI VALUES ('Q9','A74','Margareta','1');
INSERT INTO RASPUNSURI VALUES ('Q9','A75','Lacramioara','1');
INSERT INTO RASPUNSURI VALUES ('Q9','A76','Madalina','0');
INSERT INTO RASPUNSURI VALUES ('Q9','A77','Maria','0');
INSERT INTO RASPUNSURI VALUES ('Q9','A78','Larisa','0');
INSERT INTO RASPUNSURI VALUES ('Q9','A79','Georgiana','0');
INSERT INTO RASPUNSURI VALUES ('Q9','A80','Brandusa','1');
INSERT INTO RASPUNSURI VALUES ('Q9','A81','Ana','0');

-- Domeniul 4 (Teoria numerelor) 
INSERT INTO INTREBARI VALUES('D4','Q10','Cate litere are alfabetul roman?');
INSERT INTO RASPUNSURI VALUES ('Q10','A82',26,'0');
INSERT INTO RASPUNSURI VALUES ('Q10','A83',31,'1');
INSERT INTO RASPUNSURI VALUES ('Q10','A84','Ce e asta?','0');
INSERT INTO RASPUNSURI VALUES ('Q10','A85','26.5','0');
INSERT INTO RASPUNSURI VALUES ('Q10','A86','-12','0');
INSERT INTO RASPUNSURI VALUES ('Q10','A87','Pe toate.','0');

-- Domeniul 5 (Existentialism)
INSERT INTO INTREBARI VALUES('D5','Q11','Pe ce planeta traiesti?');
INSERT INTO RASPUNSURI VALUES ('Q11','A88','Pe pamant.','1');
INSERT INTO RASPUNSURI VALUES ('Q11','A89','Traiesc in nori.','0');
INSERT INTO RASPUNSURI VALUES ('Q11','A90','Pe juiter.','0');
INSERT INTO RASPUNSURI VALUES ('Q11','A91','Depinde.','0');
INSERT INTO RASPUNSURI VALUES ('Q11','A92','Pe luna.','0');
INSERT INTO RASPUNSURI VALUES ('Q11','A93','Pe toate.','0');



-- Domeniul 6 (Animale)
INSERT INTO INTREBARI VALUES('D6','Q12','Care animal este mamifer ?');
INSERT INTO RASPUNSURI VALUES ('Q12','A94','Ariciul','1');
INSERT INTO RASPUNSURI VALUES ('Q12','A95','Capra neagra','1');
INSERT INTO RASPUNSURI VALUES ('Q12','A96','Lupul','1');
INSERT INTO RASPUNSURI VALUES ('Q12','A97','Ursul','1');
INSERT INTO RASPUNSURI VALUES ('Q12','A98','Gaina','0');
INSERT INTO RASPUNSURI VALUES ('Q12','A99','Barza','0');
INSERT INTO RASPUNSURI VALUES ('Q12','A100','Sarpele','0');
INSERT INTO RASPUNSURI VALUES ('Q12','A101','Delfinul','1');
INSERT INTO RASPUNSURI VALUES ('Q12','A102','Broasca testoasa','0');

-- Domeniul 7 (Povesti)
INSERT INTO INTREBARI VALUES('D7','Q13','Cati pitici avea Cenusareasa ?');
INSERT INTO RASPUNSURI VALUES ('Q13','A103','0','1');
INSERT INTO RASPUNSURI VALUES ('Q13','A104','Niciunul','1');
INSERT INTO RASPUNSURI VALUES ('Q13','A105','1','0');
INSERT INTO RASPUNSURI VALUES ('Q13','A106','3','0');
INSERT INTO RASPUNSURI VALUES ('Q13','A107','7','0');
INSERT INTO RASPUNSURI VALUES ('Q13','A108','8','0');
INSERT INTO RASPUNSURI VALUES ('Q13','A109','Pe toti','0');
INSERT INTO RASPUNSURI VALUES ('Q13','A110','Unul si bun','0');
INSERT INTO RASPUNSURI VALUES ('Q13','A111','Nu stiu','0');


-- Domeniul 8 (Istorie)
INSERT INTO INTREBARI VALUES('D8','Q14','Cine a fost Alexandru Ioan Cuza ?');
INSERT INTO RASPUNSURI VALUES ('Q14','A112','a fost primul domnitor al Principatelor Unite si al statului national Romania','1');
INSERT INTO RASPUNSURI VALUES ('Q14','A113','un om inrobit de doua patimi Iubirea pentru patrie si cea pentru femei frumoase','1');
INSERT INTO RASPUNSURI VALUES ('Q14','A114','un om pasionat de cai','1');
INSERT INTO RASPUNSURI VALUES ('Q14','A115','un pictor roman','0');
INSERT INTO RASPUNSURI VALUES ('Q14','A116','primul scriitor de opera literara','0');
INSERT INTO RASPUNSURI VALUES ('Q14','A117','a fost ultimul domnitor al Principatelor Unite si al statului national Romania','0');
INSERT INTO RASPUNSURI VALUES ('Q14','A118','un domnitor roman nascut in anul 1820','1');
INSERT INTO RASPUNSURI VALUES ('Q14','A119','un domnitor roman nascut in anul 1859','0');
INSERT INTO RASPUNSURI VALUES ('Q14','A120','Domnitor in 1859-1866','1');

-- Domeniul 9 (Scriitori)
INSERT INTO INTREBARI VALUES('D9','Q15','Care este/sunt scriitori romani ?');
INSERT INTO RASPUNSURI VALUES ('Q15','A121','Mihai Eminescu','1');
INSERT INTO RASPUNSURI VALUES ('Q15','A122','Ion Luca Caragiale','1');
INSERT INTO RASPUNSURI VALUES ('Q15','A123','Mircea Cartarescu','1');
INSERT INTO RASPUNSURI VALUES ('Q15','A124','Mircea Eliade','1');
INSERT INTO RASPUNSURI VALUES ('Q15','A125','Ion Creanga','1');
INSERT INTO RASPUNSURI VALUES ('Q15','A126','Liviu Rebreanu','1');
INSERT INTO RASPUNSURI VALUES ('Q15','A127','Nicolae Grigorescu','0');
INSERT INTO RASPUNSURI VALUES ('Q15','A128','Nicolae Tonitza','0');
INSERT INTO RASPUNSURI VALUES ('Q15','A129','Stefan Luchian','0');
INSERT INTO RASPUNSURI VALUES ('Q15','A130','Ion Andreescu','0');


-- Domeniul 10 (Filme)
INSERT INTO INTREBARI VALUES('D10','Q16','Care dintre urmatoarele filme il au drept regizor pe Quentin Tarantino ?');
INSERT INTO RASPUNSURI VALUES ('Q16','A131','From Dusk till Dawn','1');
INSERT INTO RASPUNSURI VALUES ('Q16','A132','Pulp Fiction','1');
INSERT INTO RASPUNSURI VALUES ('Q16','A133','Django','1');
INSERT INTO RASPUNSURI VALUES ('Q16','A134','Eyes wide shut','0');
INSERT INTO RASPUNSURI VALUES ('Q16','A135','Terminator','0');
INSERT INTO RASPUNSURI VALUES ('Q16','A136','Godfather','0');
INSERT INTO RASPUNSURI VALUES ('Q16','A137','Kill Bill','1');
INSERT INTO RASPUNSURI VALUES ('Q16','A138','Ender�s Game','0');
INSERT INTO RASPUNSURI VALUES ('Q16','A139','Coboram la prima','0');
INSERT INTO RASPUNSURI VALUES ('Q16','A140','The Notebook','0');

-- Domeniul 11 (Geografie)
INSERT INTO INTREBARI VALUES('D11','Q17','Cu ce tari se invecineaza Romania?');
INSERT INTO RASPUNSURI VALUES ('Q17','A141','Ucraina','1');
INSERT INTO RASPUNSURI VALUES ('Q17','A142','Rusia','0');
INSERT INTO RASPUNSURI VALUES ('Q17','A143','Austria','0');
INSERT INTO RASPUNSURI VALUES ('Q17','A144','Ungaria','1');
INSERT INTO RASPUNSURI VALUES ('Q17','A145','Serbia','1');
INSERT INTO RASPUNSURI VALUES ('Q17','A146','Germania','0');
INSERT INTO RASPUNSURI VALUES ('Q17','A147','Bulgaria','1');
INSERT INTO RASPUNSURI VALUES ('Q17','A148','Republica Moldova','1');
INSERT INTO RASPUNSURI VALUES ('Q17','A149','Spania','0');
INSERT INTO RASPUNSURI VALUES ('Q17','A150','Macedonia','0');

-- Tot domeniul 5 (Existentialism)
INSERT INTO INTREBARI VALUES('D5','Q18','Care este raspunsul la orice intrebare de tip DA/NU?');
INSERT INTO RASPUNSURI VALUES ('Q18','A151','Tu ce crezi?','1');
INSERT INTO RASPUNSURI VALUES ('Q18','A152','Probabil.','1');
INSERT INTO RASPUNSURI VALUES ('Q18','A153','Nu stiu.','0');
INSERT INTO RASPUNSURI VALUES ('Q18','A154','Da.','0');
INSERT INTO RASPUNSURI VALUES ('Q18','A155','Nu.','0');
INSERT INTO RASPUNSURI VALUES ('Q18','A156','Depinde.','0');


--domeniul 7 povesti
INSERT INTO INTREBARI VALUES('D7','Q19','Cine a scris capra cu trei iezi? ');
INSERT INTO RASPUNSURI VALUES ('Q19','A157','Killa Fonic','0');
INSERT INTO RASPUNSURI VALUES ('Q19','A158','Ion Creanga','1');
INSERT INTO RASPUNSURI VALUES ('Q19','A159','Este povestire populara','0');
INSERT INTO RASPUNSURI VALUES ('Q19','A160','Vasile Alecsandri','0');
INSERT INTO RASPUNSURI VALUES ('Q19','A161','Cristian Tudor Popescu','0');
INSERT INTO RASPUNSURI VALUES ('Q19','A162','Mihail Sadoveanu','0');

-- domeniul 11 geografie
INSERT INTO INTREBARI VALUES('D11','Q20','Care tara este si continent?');
INSERT INTO RASPUNSURI VALUES('Q20', 'A163', 'Romania', '0');
INSERT INTO RASPUNSURI VALUES('Q20', 'A164', 'Australia', '1');
INSERT INTO RASPUNSURI VALUES('Q20', 'A165', 'Statele Unite ale Americii', '0');
INSERT INTO RASPUNSURI VALUES('Q20', 'A166', 'China', '0');
INSERT INTO RASPUNSURI VALUES('Q20', 'A167', 'Africa de Sud', '0');
INSERT INTO RASPUNSURI VALUES('Q20', 'A168', 'Rusia', '0');

--domeniul 9, scriitori
INSERT INTO INTREBARI VALUES('D9','Q21','Care din urmatoarele opere au fost scrise de Mircea Eliade?');
INSERT INTO RASPUNSURI VALUES('Q21','A169','Maitreyi','1');
INSERT INTO RASPUNSURI VALUES('Q21','A170','Romanul adolescentului miop','1');
INSERT INTO RASPUNSURI VALUES('Q21','A171','Noaptea de Sanziene','1');
INSERT INTO RASPUNSURI VALUES('Q21','A172','Domnisoara Christina','1');
INSERT INTO RASPUNSURI VALUES('Q21','A173','La tiganci','1');
INSERT INTO RASPUNSURI VALUES('Q21','A174','Pe culmile disperarii','0');
INSERT INTO RASPUNSURI VALUES('Q21','A175','Demiurgul cel rau','0');
INSERT INTO RASPUNSURI VALUES('Q21','A176','Despre neajunsul de a te fi nascut','0');

-- Domeniul 12 (Muzica)
INSERT INTO INTREBARI VALUES('D12','Q22','Care din urmatoarele sunt note muzicale valide ?');
INSERT INTO RASPUNSURI VALUES ('Q22','A177','Do','1');
INSERT INTO RASPUNSURI VALUES ('Q22','A178','Re','1');
INSERT INTO RASPUNSURI VALUES ('Q22','A179','Mi','1');
INSERT INTO RASPUNSURI VALUES ('Q22','A180','Fa','1');
INSERT INTO RASPUNSURI VALUES ('Q22','A181','Sol','1');
INSERT INTO RASPUNSURI VALUES ('Q22','A182','La','1');
INSERT INTO RASPUNSURI VALUES ('Q22','A183','Si','1');
INSERT INTO RASPUNSURI VALUES ('Q22','A184','Ri','0');
INSERT INTO RASPUNSURI VALUES ('Q22','A185','Mu','0');
INSERT INTO RASPUNSURI VALUES ('Q22','A186','Lo','0');

--INSERT INTO INTREBARI VALUES(�D12�, �Q24�, �Care dintre urmatorii artisti sunt francezi?�);
--INSERT INTO RASPUNSURI VALUES(�Q24�, �A197�, �Stromae�, �1�);
--INSERT INTO RASPUNSURI VALUES(�Q24�, �A198�, �Indila�, �1�);
--INSERT INTO RASPUNSURI VALUES(�Q24�, �A199�, �Pomme�, �1�);
--INSERT INTO RASPUNSURI VALUES(�Q24�, �A200�, �Black M�, �1�);
--INSERT INTO RASPUNSURI VALUES(�Q24�, �A201�, �Eminem�, �0�);
--INSERT INTO RASPUNSURI VALUES(�Q24�, �A202�, �Illenium�, �0�);
--INSERT INTO RASPUNSURI VALUES(�Q24�, �A203�, �Billie Eilish�, �0�);
--INSERT INTO RASPUNSURI VALUES(�Q24�, �A204�, �Martin Garrix�, �0�);
--INSERT INTO RASPUNSURI VALUES(�Q24�, �A205�, �Alan Walker�, �0�);
--INSERT INTO RASPUNSURI VALUES(�Q24�, �A206�, �Faruko�, �0�);

INSERT INTO INTREBARI VALUES('D12', 'Q24', 'Care dintre urmatorii artisti sunt francezi?');
INSERT INTO RASPUNSURI VALUES('Q24', 'A197', 'Stromae', '1');
INSERT INTO RASPUNSURI VALUES('Q24', 'A198', 'Indila', '1');
INSERT INTO RASPUNSURI VALUES('Q24', 'A199', 'Pomme', '1');
INSERT INTO RASPUNSURI VALUES('Q24', 'A200', 'Black M', '1');
INSERT INTO RASPUNSURI VALUES('Q24', 'A201', 'Eminem', '0');
INSERT INTO RASPUNSURI VALUES('Q24', 'A202', 'Illenium', '0');
INSERT INTO RASPUNSURI VALUES('Q24', 'A203', 'Billie Eilish', '0');
INSERT INTO RASPUNSURI VALUES('Q24', 'A204', 'Martin Garrix', '0');
INSERT INTO RASPUNSURI VALUES('Q24', 'A205', 'Alan Walker', '0');
INSERT INTO RASPUNSURI VALUES('Q24', 'A206', 'Faruko', '0');



--Tot domeniul 9 (Scriitori)
INSERT INTO INTREBARI VALUES ('D9','Q23','Care dintre urmatoarele poezii sunt scrise de Mihai Eminescu?');
INSERT INTO RASPUNSURI VALUES('Q23','A187','Floare albastra','1');
INSERT INTO RASPUNSURI VALUES('Q23','A188','Luceafarul','1');
INSERT INTO RASPUNSURI VALUES('Q23','A189','Iarna pe ulita','0');
INSERT INTO RASPUNSURI VALUES('Q23','A190','Somnoroase pasarele','1');
INSERT INTO RASPUNSURI VALUES('Q23','A191','Plumb','0');
INSERT INTO RASPUNSURI VALUES('Q23','A192','Si daca...','1');
INSERT INTO RASPUNSURI VALUES('Q23','A193','Testament','0');
INSERT INTO RASPUNSURI VALUES('Q23','A194','Acceleratul','0');
INSERT INTO RASPUNSURI VALUES('Q23','A195','De pe-o buna dimineata','0');
INSERT INTO RASPUNSURI VALUES('Q23','A196','Rapsodii de toamna','0');



--Domeniul 13 TIC:
INSERT INTO INTREBARI VALUES('D13', 'Q25', 'Care dintre urmatoarele sunt dispozitive periferice de iesire?');
INSERT INTO RASPUNSURI VALUES('Q25', 'A207', 'Tastatura', '0');
INSERT INTO RASPUNSURI VALUES('Q25', 'A208', 'Imprimanta', '1');
INSERT INTO RASPUNSURI VALUES('Q25', 'A209', 'Difuzor', '1');
INSERT INTO RASPUNSURI VALUES('Q25', 'A210', 'Mouse', '0');
INSERT INTO RASPUNSURI VALUES('Q25', 'A211', 'Microfon', '0');
INSERT INTO RASPUNSURI VALUES('Q25', 'A212', 'Monitor', '1');
INSERT INTO RASPUNSURI VALUES('Q25', 'A213', 'Camera video', '0');
INSERT INTO RASPUNSURI VALUES('Q25', 'A214', 'Plotter', '1');
INSERT INTO RASPUNSURI VALUES('Q25', 'A215', 'Trackball', '0');
INSERT INTO RASPUNSURI VALUES('Q25', 'A216', 'Scanner', '0');



--Domeniul 14 Informatica:
INSERT INTO INTREBARI VALUES('D14', 'Q26', 'Care dintre urmatoarele tipuri de sortare au o complexitate medie O(n*log(n))?');
INSERT INTO RASPUNSURI VALUES('Q26', 'A217', 'Bubble Sort', '0');
INSERT INTO RASPUNSURI VALUES('Q26', 'A218', 'Quick Sort', '1');
INSERT INTO RASPUNSURI VALUES('Q26', 'A219', 'Insertion Sort', '0');
INSERT INTO RASPUNSURI VALUES('Q26', 'A220', 'Heap Sort', '1');
INSERT INTO RASPUNSURI VALUES('Q26', 'A221', 'Merge Sort', '1');
INSERT INTO RASPUNSURI VALUES('Q26', 'A222', 'Bucket Sort', '0');
INSERT INTO RASPUNSURI VALUES('Q26', 'A223', 'Selection Sort', '0');


INSERT INTO INTREBARI VALUES('D14', 'Q27', 'Care dintre urmatoarele raspunsuri sunt limbaje de programare?');
INSERT INTO RASPUNSURI VALUES('Q27', 'A224', 'HTML', '0');
INSERT INTO RASPUNSURI VALUES('Q27', 'A225', 'C', '1');
INSERT INTO RASPUNSURI VALUES('Q27', 'A226', 'CSS', '0');
INSERT INTO RASPUNSURI VALUES('Q27', 'A227', 'Java', '1');
INSERT INTO RASPUNSURI VALUES('Q27', 'A228', 'C#', '1');
INSERT INTO RASPUNSURI VALUES('Q27', 'A229', 'C++', '1');
INSERT INTO RASPUNSURI VALUES('Q27', 'A230', 'Python', '1');
INSERT INTO RASPUNSURI VALUES('Q27', 'A231', 'Semnelor', '0');
INSERT INTO RASPUNSURI VALUES('Q27', 'A232', 'Autostopul', '0');


INSERT INTO INTREBARI VALUES('D14','Q28','Care dintre variante reprezinta un cuvant intalnit des din domeniul informaticii?');
INSERT INTO RASPUNSURI VALUES ('Q28','A233','Byte','1');
INSERT INTO RASPUNSURI VALUES ('Q28','A234','Eroare','1');
INSERT INTO RASPUNSURI VALUES ('Q28','A235','Aplicatie','1');
INSERT INTO RASPUNSURI VALUES ('Q28','A236','Spatiu','1');
INSERT INTO RASPUNSURI VALUES ('Q28','A237','Timp','1');
INSERT INTO RASPUNSURI VALUES ('Q28','A238','Volum','0');
INSERT INTO RASPUNSURI VALUES ('Q28','A239','Acid Ribonucleic','0');
INSERT INTO RASPUNSURI VALUES ('Q28','A240','Restanta','0');
INSERT INTO RASPUNSURI VALUES ('Q28','A241','Arsen','0');
INSERT INTO RASPUNSURI VALUES ('Q28','A242','Impedanta','0');



INSERT INTO INTREBARI VALUES('D14','Q29','Care este maestrul xiaolin?');
INSERT INTO RASPUNSURI VALUES ('Q29','A243','Omni','1');
INSERT INTO RASPUNSURI VALUES ('Q29','A244','Kai','1');
INSERT INTO RASPUNSURI VALUES ('Q29','A245','Raimundo','1');
INSERT INTO RASPUNSURI VALUES ('Q29','A246','Kimiko','1');
INSERT INTO RASPUNSURI VALUES ('Q29','A247','Wuya','0');
INSERT INTO RASPUNSURI VALUES ('Q29','A248','Jack','0');
INSERT INTO RASPUNSURI VALUES ('Q29','A249','Chase','0');
INSERT INTO RASPUNSURI VALUES ('Q29','A250','Master Fung','0');
INSERT INTO RASPUNSURI VALUES ('Q29','A251','IDK','0');
INSERT INTO RASPUNSURI VALUES ('Q29','A252','Bruce Lee','0');

INSERT INTO INTREBARI VALUES('D10','Q30','Ce personaje au murit Avengers: Endgame?');
INSERT INTO RASPUNSURI VALUES ('Q30','A301','Black Widow','1');
INSERT INTO RASPUNSURI VALUES ('Q30','A302','Tony Stark','1');
INSERT INTO RASPUNSURI VALUES ('Q30','A303','Hawkeye','0');
INSERT INTO RASPUNSURI VALUES ('Q30','A304','Captain America','0');
INSERT INTO RASPUNSURI VALUES ('Q30','A305','Scarlet Witch','0');
INSERT INTO RASPUNSURI VALUES ('Q30','A306','Roket','0');
INSERT INTO RASPUNSURI VALUES ('Q30','A307','Gamora','0');
INSERT INTO RASPUNSURI VALUES ('Q30','A308','Spider man','0');
INSERT INTO RASPUNSURI VALUES ('Q30','A309','IDK','0');
INSERT INTO RASPUNSURI VALUES ('Q30','A310','Thanos','1');



COMMIT;
