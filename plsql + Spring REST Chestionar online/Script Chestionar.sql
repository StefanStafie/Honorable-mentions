DROP TABLE teste;
/
CREATE TABLE TESTE (
  EMAIL VARCHAR2(50),
  NR_ORDINE NUMBER(2),
  INTREBARE_CURENTA VARCHAR(8),
  RASPUNS1 VARCHAR2(8),
  RASPUNS2 VARCHAR2(8),
  RASPUNS3 VARCHAR2(8),
  RASPUNS4 VARCHAR2(8),
  RASPUNS5 VARCHAR2(8),
  RASPUNS6 VARCHAR2(8),
  RASPUNSURI_CORECTE varchar2(200),
  RASPUNSURI_UTILIZATOR varchar2(200),
  CONSTRAINT email_unique UNIQUE (email, intrebare_curenta)
)
/ 

CREATE OR REPLACE FUNCTION get_scor_intrebare(v_email IN VARCHAR2, v_nr IN NUMERIC) RETURN VARCHAR2 as
    CURSOR cursor_test IS SELECT * FROM teste WHERE email = v_email AND nr_ordine = v_nr; 
    CURSOR rasp_corect (stringg VARCHAR2) is select raspuns from (
                                select distinct(trim(regexp_substr(stringg, '[^,]+', 1, level))) as raspuns  from teste  CONNECT BY instr(stringg, ',', 1, level - 1) > 0);
    CURSOR rasp_user (stringg VARCHAR2) is select raspuns from (
                                select distinct(trim(regexp_substr(stringg, '[^,]+', 1, level))) as raspuns  from teste  CONNECT BY instr(stringg, ',', 1, level - 1) > 0);
    test_row teste%ROWTYPE;
    nr_raspunsuri NUMBER(2,0);--nr de raspunsuri corecte totale ale intrebarii
    nr_raspunsuri_gresite NUMBER(2,0);--nr de raspunsuri gresite ale utilizatorului
    nr_raspunsuri_utilizator NUMBER(2,0);
    nr_raspunsuri_corecte NUMBER(2,0);

    scor NUMBER(2,0);
    rasp_user_row rasp_user%ROWTYPE;
    rasp_corect_row rasp_corect%ROWTYPE;
BEGIN
    scor:=0;
    OPEN cursor_test;
    LOOP
        FETCH cursor_test INTO test_row;
        EXIT WHEN cursor_test%NOTFOUND;
        
        SELECT COUNT(*) INTO nr_raspunsuri from (
                            select distinct(trim(regexp_substr(test_row.raspunsuri_corecte, '[^,]+', 1, level))) as raspuns  
                                    from teste  CONNECT BY instr(test_row.raspunsuri_corecte, ',', 1, level - 1) > 0);
        
        SELECT COUNT(*) INTO nr_raspunsuri_utilizator from (
                            select distinct(trim(regexp_substr(test_row.raspunsuri_utilizator, '[^,]+', 1, level))) as raspuns  
                                    from teste  CONNECT BY instr(test_row.raspunsuri_utilizator, ',', 1, level - 1) > 0);
        
        
        nr_raspunsuri_gresite := nr_raspunsuri_utilizator;
        OPEN rasp_corect(test_row.raspunsuri_corecte); --pentru fiecare raspuns corect, verific daca exista in lista de raspunsuri
        LOOP
            FETCH rasp_corect INTO rasp_corect_row;
            EXIT WHEN rasp_corect%NOTFOUND;
            OPEN rasp_user(test_row.raspunsuri_utilizator);
            LOOP
                FETCH rasp_user INTO rasp_user_row;
                EXIT WHEN rasp_user%NOTFOUND;
                IF(rasp_user_row.raspuns = rasp_corect_row.raspuns) THEN                    
                    nr_raspunsuri_gresite := nr_raspunsuri_gresite -1;
                END IF;
            END LOOP;
            CLOSE rasp_user;            
        END LOOP;
        CLOSE rasp_corect;
        nr_raspunsuri_corecte := nr_raspunsuri_utilizator - nr_raspunsuri_gresite;
        scor := scor + greatest(  (10*(nr_raspunsuri_corecte - (  (nr_raspunsuri-nr_raspunsuri_corecte)  +nr_raspunsuri_gresite )  )/nr_raspunsuri), 0);
    END LOOP;    
    CLOSE cursor_test;
    return scor;
END;
 
/
--calculeaza scorul. Functioneaza la orice moment al chestionarului
CREATE OR REPLACE FUNCTION get_scor(v_email IN VARCHAR2) RETURN VARCHAR2 as
    CURSOR cursor_test IS SELECT * FROM teste WHERE email = v_email; 
    CURSOR rasp_corect (stringg VARCHAR2) is select raspuns from (
                                select distinct(trim(regexp_substr(stringg, '[^,]+', 1, level))) as raspuns  from teste  CONNECT BY instr(stringg, ',', 1, level - 1) > 0);
    CURSOR rasp_user (stringg VARCHAR2) is select raspuns from (
                                select distinct(trim(regexp_substr(stringg, '[^,]+', 1, level))) as raspuns  from teste  CONNECT BY instr(stringg, ',', 1, level - 1) > 0);
    test_row teste%ROWTYPE;
    nr_raspunsuri NUMBER(2,0);--nr de raspunsuri corecte totale ale intrebarii
    nr_raspunsuri_gresite NUMBER(2,0);--nr de raspunsuri gresite ale utilizatorului
    nr_raspunsuri_utilizator NUMBER(2,0);
    nr_raspunsuri_corecte NUMBER(2,0);

    scor NUMBER(2,0);
    rasp_user_row rasp_user%ROWTYPE;
    rasp_corect_row rasp_corect%ROWTYPE;
BEGIN
    scor:=0;
    OPEN cursor_test;
    LOOP
        FETCH cursor_test INTO test_row;
        EXIT WHEN cursor_test%NOTFOUND;
        
        SELECT COUNT(*) INTO nr_raspunsuri from (
                            select distinct(trim(regexp_substr(test_row.raspunsuri_corecte, '[^,]+', 1, level))) as raspuns  
                                    from teste  CONNECT BY instr(test_row.raspunsuri_corecte, ',', 1, level - 1) > 0);
        
        SELECT COUNT(*) INTO nr_raspunsuri_utilizator from (
                            select distinct(trim(regexp_substr(test_row.raspunsuri_utilizator, '[^,]+', 1, level))) as raspuns  
                                    from teste  CONNECT BY instr(test_row.raspunsuri_utilizator, ',', 1, level - 1) > 0);
        
        
        nr_raspunsuri_gresite := nr_raspunsuri_utilizator;
        OPEN rasp_corect(test_row.raspunsuri_corecte); --pentru fiecare raspuns corect, verific daca exista in lista de raspunsuri
        LOOP
            FETCH rasp_corect INTO rasp_corect_row;
            EXIT WHEN rasp_corect%NOTFOUND;
            OPEN rasp_user(test_row.raspunsuri_utilizator);
            LOOP
                FETCH rasp_user INTO rasp_user_row;
                EXIT WHEN rasp_user%NOTFOUND;
                IF(rasp_user_row.raspuns = rasp_corect_row.raspuns) THEN                    
                    nr_raspunsuri_gresite := nr_raspunsuri_gresite -1;
                END IF;
            END LOOP;
            CLOSE rasp_user;            
        END LOOP;
        CLOSE rasp_corect;
        nr_raspunsuri_corecte := nr_raspunsuri_utilizator - nr_raspunsuri_gresite;
        scor := scor + greatest(  (10*(nr_raspunsuri_corecte - (  (nr_raspunsuri-nr_raspunsuri_corecte)  +nr_raspunsuri_gresite )  )/nr_raspunsuri), 0);
    END LOOP;    
    CLOSE cursor_test;
    return scor;
END;
 
/

--returneaza raspunsul generat de server 
CREATE OR REPLACE FUNCTION get_return(v_email IN VARCHAR2, id_intrebare IN VARCHAR2) RETURN VARCHAR2 as
    teste_row teste%ROWTYPE;
    valoare_return VARCHAR2(1000);
    v_text varchar2(200);
BEGIN
    SELECT * INTO teste_row FROM teste WHERE email = v_email AND intrebare_curenta = id_intrebare;
    
    SELECT text_intrebare INTO v_text FROM intrebari where id = id_intrebare;
            valoare_return := '{ 
"id_intrebare":"' || id_intrebare || '"' || ', "intrebare":"' || v_text || '", 
"id_rasp_1":"';
            
            SELECT text_raspuns INTO v_text FROM raspunsuri WHERE id = teste_row.raspuns1;
            valoare_return := valoare_return || teste_row.raspuns2 || '", "raspuns_1":"' || v_text || '",
"id_rasp_2":"';

            SELECT text_raspuns INTO v_text FROM raspunsuri WHERE id = teste_row.raspuns2;
            valoare_return := valoare_return || teste_row.raspuns3 || '", "raspuns_2":"' || v_text || '",
"id_rasp_3":"';

            SELECT text_raspuns INTO v_text FROM raspunsuri WHERE id = teste_row.raspuns3;
            valoare_return := valoare_return || teste_row.raspuns4 || '", "raspuns_3":"' || v_text || '",
"id_rasp_4":"';

            SELECT text_raspuns INTO v_text FROM raspunsuri WHERE id = teste_row.raspuns4;
            valoare_return := valoare_return || teste_row.raspuns5 || '", "raspuns_4":"' || v_text || '",
"id_rasp_5":"';

            SELECT text_raspuns INTO v_text FROM raspunsuri WHERE id = teste_row.raspuns5;
            valoare_return := valoare_return || teste_row.raspuns6 || '", "raspuns_5":"' || v_text || '",
"id_rasp_6":"';
            SELECT text_raspuns INTO v_text FROM raspunsuri WHERE id = teste_row.raspuns6;
            valoare_return := valoare_return || teste_row.raspuns1 || '", "raspuns_6":"' || v_text || '"
}';
            return valoare_return;
END get_return;
/

CREATE OR REPLACE FUNCTION urmatoarea_intrebare (v_email IN VARCHAR2, v_raspuns IN VARCHAR2) RETURN VARCHAR2 as
    CURSOR cursor_domeniu is SELECT id FROM   
                                    (SELECT distinct(domeniu), id FROM intrebari ORDER BY dbms_random.value) 
                                    WHERE rownum < 11;
    CURSOR cursor_intrebare (v_intebare VARCHAR2, nu_asta raspunsuri%rowtype) is SELECT id FROM (
                                                         SELECT nu_asta.id as id
                                                         FROM DUAL
                                                         UNION
                                                         SELECT distinct(id)
                                                         FROM (
                                                                  SELECT distinct(id)
                                                                  FROM RASPUNSURI
                                                                  WHERE Q_ID = v_intebare
                                                                    AND id != nu_asta.id
                                                                  ORDER BY dbms_random.value
                                                        )
                                                        WHERE rownum < 6
                                                     )
                                                ORDER BY
                                                  dbms_random.value;
                                    
    CURSOR split_csv (stringg VARCHAR2) is select raspuns from (
                                select distinct(trim(regexp_substr(stringg, '[^,]+', 1, level))) as raspuns  from teste  CONNECT BY instr(stringg, ',', 1, level - 1) > 0)
                                ORDER BY rownum desc;
                                
    nu_asta raspunsuri%rowtype;                            
    v_intebare VARCHAR2(8);
    v_raspuns_bd VARCHAR2(8);
    counter NUMBER;
    v_raspunsuri_corecte VARCHAR(100);
    raspuns_id_intrebare VARCHAR2(8);
    v_id_intrebare VARCHAR2(8);
    v_id_intrebare_adev VARCHAR2(8);
    valoare_return VARCHAR2(1000);
    teste_row teste%ROWTYPE;
    verific varchar2(1);
    v_text varchar2(200);
    v_nr_ordine varchar2(100);
BEGIN
    
    IF(v_raspuns is null) THEN
        counter:=0;
        OPEN cursor_domeniu; --pentru fiecare domeniu iau o intrebare si 6 raspunsuri posibile
        LOOP
            counter := counter + 1;
            --o noua inregistrare pentru acest domeniu
           
            FETCH cursor_domeniu INTO v_intebare;
            EXIT WHEN cursor_domeniu%NOTFOUND;
            
            INSERT INTO teste (email, nr_ordine, raspunsuri_utilizator, intrebare_curenta) values (v_email, counter, null, v_intebare); 
            
            SELECT * into nu_asta FROM raspunsuri WHERE q_id = v_intebare AND corect = '1' AND rownum<2 ORDER BY dbms_random.value;
            --inserez raspunsuri
            OPEN cursor_intrebare(v_intebare, nu_asta);
                FETCH cursor_intrebare INTO v_raspuns_bd;
                UPDATE teste SET raspuns1 = v_raspuns_bd WHERE email = v_email AND intrebare_curenta = v_intebare;
                FETCH cursor_intrebare INTO v_raspuns_bd;
                UPDATE teste SET raspuns2 = v_raspuns_bd WHERE email = v_email AND intrebare_curenta = v_intebare;
                FETCH cursor_intrebare INTO v_raspuns_bd;
                UPDATE teste SET raspuns3 = v_raspuns_bd WHERE email = v_email AND intrebare_curenta = v_intebare;
                FETCH cursor_intrebare INTO v_raspuns_bd;
                UPDATE teste SET raspuns4 = v_raspuns_bd WHERE email = v_email AND intrebare_curenta = v_intebare;
                FETCH cursor_intrebare INTO v_raspuns_bd;
                UPDATE teste SET raspuns5 = v_raspuns_bd WHERE email = v_email AND intrebare_curenta = v_intebare;
                FETCH cursor_intrebare INTO v_raspuns_bd;
                UPDATE teste SET raspuns6 = v_raspuns_bd WHERE email = v_email AND intrebare_curenta = v_intebare;
            CLOSE cursor_intrebare;           
            
            --compun raspunsurile corecte
            SELECT * INTO teste_row FROM teste WHERE email = v_email AND intrebare_curenta = v_intebare;
            v_raspunsuri_corecte :='';
            
            SELECT corect INTO verific FROM raspunsuri WHERE id = teste_row.raspuns1;
            IF(verific = '1') THEN
                v_raspunsuri_corecte := v_raspunsuri_corecte || ',' || teste_row.raspuns1;
            END IF;
            SELECT corect INTO verific FROM raspunsuri WHERE id = teste_row.raspuns2;
            IF(verific = '1') THEN
                v_raspunsuri_corecte := v_raspunsuri_corecte || ',' || teste_row.raspuns2;
            END IF;
            SELECT corect INTO verific FROM raspunsuri WHERE id = teste_row.raspuns3;
            IF(verific = '1') THEN
                v_raspunsuri_corecte := v_raspunsuri_corecte || ',' || teste_row.raspuns3;
            END IF;
            SELECT corect INTO verific FROM raspunsuri WHERE id = teste_row.raspuns4;
            IF(verific = '1') THEN
                v_raspunsuri_corecte := v_raspunsuri_corecte || ',' || teste_row.raspuns4;
            END IF;                
            SELECT corect INTO verific FROM raspunsuri WHERE id = teste_row.raspuns5;
            IF(verific = '1') THEN
                v_raspunsuri_corecte := v_raspunsuri_corecte || ',' || teste_row.raspuns5;
            END IF;
            SELECT corect INTO verific FROM raspunsuri WHERE id = teste_row.raspuns6;
            IF(verific = '1') THEN
                v_raspunsuri_corecte := v_raspunsuri_corecte || ',' || teste_row.raspuns6;
            END IF;
            UPDATE teste SET raspunsuri_corecte = SUBSTR(v_raspunsuri_corecte, 2) WHERE email = v_email and intrebare_curenta = v_intebare;            
            
        END LOOP;
        CLOSE cursor_domeniu; 
        
        SELECT intrebare_curenta INTO v_id_intrebare FROM teste WHERE nr_ordine = 1 AND email = v_email;
        return  get_return(v_email, v_id_intrebare);
    ELSE
        select raspuns INTO v_id_intrebare_adev from (
                select distinct(trim(regexp_substr(v_raspuns, '[^,]+', 1, level))) as raspuns  from teste  CONNECT BY instr('', ',', 1, level - 1) > 0)
                ORDER BY rownum desc;
        
        select nr_ordine, intrebare_curenta into v_nr_ordine, v_id_intrebare from (select nr_ordine, intrebare_curenta from teste where email=v_email AND raspunsuri_utilizator is null ORDER BY NR_ORDINE) where rownum=1;
        
        UPDATE teste SET raspunsuri_utilizator = (substr(v_raspuns,length(v_id_intrebare_adev)+2)) where email = v_email AND nr_ordine=v_nr_ordine AND raspunsuri_utilizator is null;
        IF((substr(v_raspuns,length(v_id_intrebare_adev)+2)) is null) THEN
            SELECT intrebare_curenta INTO v_id_intrebare FROM teste WHERE nr_ordine = (SELECT nr_ordine FROM teste WHERE intrebare_curenta = v_id_intrebare AND email = v_email) AND email = v_email;
        ELSE
            SELECT intrebare_curenta INTO v_id_intrebare FROM teste WHERE nr_ordine = (SELECT nr_ordine+1 FROM teste WHERE intrebare_curenta = v_id_intrebare AND email = v_email) AND email = v_email;
        END IF;
        return get_return(v_email, v_id_intrebare);
    END IF;
    EXCEPTION
    WHEN NO_DATA_FOUND THEN
            return get_scor(v_email);
END urmatoarea_intrebare;
/

    
set serveroutput on;
DECLARE
    result varchar2(1000);
BEGIN
    --result := urmatoarea_intrebare('master', null);
    result := urmatoarea_intrebare('test1', 'Q3,dqw');
    DBMS_OUTPUT.PUT_LINE(result);
END;
/

select raspuns from (
                select distinct(trim(regexp_substr('dsa,asdd,aaa,vbb,ccc', '[^,]+', 1, level))) as raspuns  from teste  CONNECT BY instr('', ',', 1, level - 1) > 0)
                ORDER BY rownum desc;
select nr_ordine from (select nr_ordine from teste where email='mario@yahoo' AND raspunsuri_utilizator is null ORDER BY NR_ORDINE) where rownum=1 
  
truncate  table teste;
select * from teste;