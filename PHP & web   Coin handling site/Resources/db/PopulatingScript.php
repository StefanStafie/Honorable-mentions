<?php
require_once 'db_connection.php';


function populate(){


    $listNames= array('Adrian','Alex','Alexandru','Alin','Andreas','Andrei','Aurelian','Beniamin','Bogdan','Camil','Catalin','Cezar','Ciprian','Claudiu','Codrin','Constantin','Corneliu','Cosmin','Costel','Cristian','Damian','Dan','Daniel','Danut','Darius','Denise','Dimitrie','Dorian','Dorin','Dragos','Dumitru','Eduard','Elvis','Emil','Ervin','Eugen','Eusebiu','Fabian','Filip','Florian','Florin','Gabriel','George','Gheorghe','Giani','Giulio','Iaroslav','Ilie','Ioan','Ion','Ionel','Ionut','Iosif','Irinel','Iulian','Iustin','Laurentiu','Liviu','Lucian','Marian','Marius','Matei','Mihai','Mihail','Nicolae','Nicu','Nicusor','Octavian','Ovidiu','Paul','Petru','Petrut','Radu','Rares','Razvan','Richard','Robert','Roland','Rolland','Romanescu','Sabin','Samuel','Sebastian','Sergiu','Silviu','Stefan','Teodor','Teofil','Theodor','Tudor','Vadim','Valentin','Valeriu','Vasile','Victor','Vlad','Vladimir','Vladut');
    $listNames2=array('Ababei','Acasandrei','Adascalitei','Afanasie','Agafitei','Agape','Aioanei','Alexandrescu','Alexandru','Alexe','Alexii','Amarghioalei','Ambroci','Andonesei','Andrei','Andrian','Andrici','Andronic','Andros','Anghelina','Anita','Antochi','Antonie','Apetrei','Apostol','Arhip','Arhire','Arteni','Arvinte','Asaftei','Asofiei','Aungurenci','Avadanei','Avram','Babei','Baciu','Baetu','Balan','Balica','Banu','Barbieru','Barzu','Bazgan','Bejan','Bejenaru','Belcescu','Belciuganu','Benchea','Bilan','Birsanu','Bivol','Bizu','Boca','Bodnar','Boistean','Borcan','Bordeianu','Botezatu','Bradea','Braescu','Budaca','Bulai','Bulbuc-aioanei','Burlacu','Burloiu','Bursuc','Butacu','Bute','Buza','Calancea','Calinescu','Capusneanu','Caraiman','Carbune','Carp','Catana','Catiru','Catonoiu','Cazacu','Cazamir','Cebere','Cehan','Cernescu','Chelaru','Chelmu','Chelmus','Chibici','Chicos','Chilaboc','Chile','Chiriac','Chirila','Chistol','Chitic','Chmilevski','Cimpoesu','Ciobanu','Ciobotaru','Ciocoiu','Ciofu','Ciornei','Citea','Ciucanu','Clatinici','Clim','Cobuz','Coca','Cojocariu','Cojocaru','Condurache','Corciu','Corduneanu','Corfu','Corneanu','Corodescu','Coseru','Cosnita','Costan','Covatariu','Cozma','Cozmiuc','Craciunas','Crainiceanu','Creanga','Cretu','Cristea','Crucerescu','Cumpata','Curca','Cusmuliuc','Damian','Damoc','Daneliuc','Daniel','Danila','Darie','Dascalescu','Dascalu','Diaconu','Dima','Dimache','Dinu','Dobos','Dochitei','Dochitoiu','Dodan','Dogaru','Domnaru','Dorneanu','Dragan','Dragoman','Dragomir','Dragomirescu','Duceac','Dudau','Durnea','Edu','Eduard','Eusebiu','Fedeles','Ferestraoaru','Filibiu','Filimon','Filip','Florescu','Folvaiter','Frumosu','Frunza','Galatanu','Gavrilita','Gavriliuc','Gavrilovici','Gherase','Gherca','Ghergu','Gherman','Ghibirdic','Giosanu','Gitlan','Giurgila','Glodeanu','Goldan','Gorgan','Grama','Grigore','Grigoriu','Grosu','Grozavu','Gurau','Haba','Harabula','Hardon','Harpa','Herdes','Herscovici','Hociung','Hodoreanu','Hostiuc','Huma','Hutanu','Huzum','Iacob','Iacobuta','Iancu','Ichim','Iftimesei','Ilie','Insuratelu','Ionesei','Ionesi','Ionita','Iordache','Iordache-tiroiu','Iordan','Iosub','Iovu','Irimia','Ivascu','Jecu','Jitariuc','Jitca','Joldescu','Juravle','Larion','Lates','Latu','Lazar','Leleu','Leon','Leonte','Leuciuc','Leustean','Luca','Lucaci','Lucasi','Luncasu','Lungeanu','Lungu','Lupascu','Lupu','Macariu','Macoveschi','Maftei','Maganu','Mangalagiu','Manolache','Manole','Marcu','Marinov','Martinas','Marton','Mataca','Matcovici','Matei','Maties','Matrana','Maxim','Mazareanu','Mazilu','Mazur','Melniciuc-puica','Micu','Mihaela','Mihai','Mihaila','Mihailescu','Mihalachi','Mihalcea','Mihociu','Milut','Minea','Minghel','Minuti','Miron','Mitan','Moisa','Moniry-abyaneh','Morarescu','Morosanu','Moscu','Motrescu','Motroi','Munteanu','Murarasu','Musca','Mutescu','Nastaca','Nechita','Neghina','Negrus','Negruser','Negrutu','Nemtoc','Netedu','Nica','Nicu','Oana','Olanuta','Olarasu','Olariu','Olaru','Onu','Opariuc','Oprea','Ostafe','Otrocol','Palihovici','Pantiru','Pantiruc','Paparuz','Pascaru','Patachi','Patras');
    $listUsernames=array('AbsolutePrime',
    'Abunexec'
    ,'Actionia'
    ,'ActionTelevision'
    ,'Advicetrex'
    ,'Alfrect'
    ,'Arixirtu'
    ,'AspectReport'
    ,'Autoreet'
    ,'Balleyer'
    ,'Baserv'
    ,'BoutiqueChoose'
    ,'BoutiqueChronicle'
    ,'Camerame'
    ,'Cellia'
    ,'Centreon'
    ,'Cerepsto'
    ,'Chomsonic'
    ,'Choosect'
    ,'Cloudie'
    ,'Curaditi'
    ,'DailyStory'
    ,'DiaryStandard'
    ,'Doopause'
    ,'Dotea'
   ,'Episodewave'
    ,'EssentialLetter'
   ,'ExpertSpy'
    ,'Faburnet'
    ,'Factedit'
    ,'Felorchy'
    ,'Flexpost'
    ,'Flickbarr'
    ,'Focusufer'
    ,'FootageUpdate'
    ,'Fritherar'
   ,'Fulnetup'
    ,'Gossiplo'
    ,'Halmasa'
    ,'HotData'
    ,'HouseExpert'
    ,'Hubertvan'
    ,'InDigest'
    ,'Infland'
    ,'InsiderHeadline'
    ,'Intingdo'
    ,'Keraterr'
    ,'LensTalent'
    ,'Lettericew'
    ,'Letterrion'
    ,'LiveReady'
    ,'MachineEnter'
    ,'Machire'
    ,'Matizene'
    ,'Merzette'
    ,'Micronasc'
    ,'MoviesSpecialised'
    ,'Ofuthbro'
    ,'Patrome'
    ,'Picksma'
    ,'PictureSalon'
    ,'Picturexa'
    ,'Pineerbr'
    ,'PlaceSpecial'
    ,'PlotsCartoons'
,'PointLocal','Pointri','Princer','Prosysta','Ronimson','Scanwor'
    ,'Scenescuri','Scenesser'
   , 'Scorsill','Screenbe');
    //print_r($listNames);
    //echo "<br>";
    $conn = OpenCon();
    for ($i=0; $i<250;$i++){
        $lastName=$listNames2[rand(0,count($listNames2)-1)];
        $firstName=$listNames[rand(0,count($listNames)-1)];
        $username=$listUsernames[rand(0,count($listUsernames)-1)];
        $password='parolaepica' . $i;
        $email = $firstName . $lastName . '@mail.com';
       // print_r($listUsernames[rand(0,count($listUsernames))]);
       
        $stmt = $conn->prepare('INSERT INTO users (first_name, last_name, username, password, email) VALUES (?,?,?,?,?)');
        $stmt->bind_param('sssss', $firstName, $lastName, $username, $password, $email);
        $stmt->execute();
      
    }
    CloseCon($conn);
}
//populate();

function populateUserCoins(){


    $popularCoins=array('HCR24033' ,'HCR24032' ,'HCR24031' ,'HCR24030' ,'HCR24029' ,'HCR24028' ,'HCR24027' ,'HCR24026' ,'HCR24025' ,'HCR24024' ,'HCR24023' ,'HCR24022' ,'HCR24021' ,'HCR24019' ,'HCR24018' ,'HCR24013' ,'HCR24012','HCR23890' ,'HCR23888' ,'HCR23886' ,'HCR23885' ,'HCR23880' ,'HCR23879' ,'HCR23878' ,'HCR23874' ,'HCR23873' ,'HCR23872' ,'HCR23871' ,'HCR23870' ,'HCR23869' ,'HCR23868' ,'HCR23865' ,'HCR23864' ,'HCR23862' ,'HCR23784' ,'HCR23783' ,'HCR23782' ,'HCR23781' ,'HCR23780' ,'HCR23779' ,'HCR23778' ,'HCR23777' ,'HCR23776' ,'HCR23775' ,'HCR23774' ,'HCR23773' ,'HCR23772' ,'HCR23771','1841,0730.227' ,'1841,0726.419' ,'1841,0726.1252' ,'1841,0726.1251' ,'1841,0726.1250' ,'1841,0726.1249' ,'1841,0726.1246' ,'1841,0726.1245' ,'1841,0726.1244' ,'1841,0726.1243' ,'1841,0726.1242' ,'1841,0726.420' ,'1841,0726.421','1841,0726.1098' ,'1841,0726.1096' ,'1841,0726.1095' ,'1841,0726.1094' ,'1841,0726.1109' ,'1841,0726.1118' ,'1841,0726.1119' ,'1841,0726.1206' ,'1841,0726.1205' ,'1841,0726.1204','WT.1438' ,'WT.1437' ,'WT.1435' ,'TWN,Inv.7' ,'TWN,Inv.10' ,'TOW.414' ,'THO.1830' ,'THO.1822' ,'THO.1815' ,'THO.1705' ,'THO.1696' ,'THO.1433' ,'THO,III.1549' ,'TC,p5.1.Osc' ,'TC,p237.2.CleMA' ,'TC,p19.20.Rom','RPK,p89C.6.Ant' ,'RPK,p89C.5.Ant' ,'RPK,p85C.9.AleIV' ,'RPK,p85C.12.AleIV' ,'RPK,p85C.11.AleIV' ,'RPK,p85C.10.AleIV' ,'RPK,p85B.3.AleV' );
    $conn = OpenCon();
    $totalusers=540;
    //$stmt= $conn->query('select identifier from coins order by identifier DESC limit 200');
    //$result2 = $stmt->fetch_all();
    for ($i=0;$i<2500;$i++){
        $user=rand(0,$totalusers);
        $identifier=$popularCoins[rand(0,count($popularCoins)-1)];
        $stmt = $conn->prepare('INSERT INTO user_coins(id_user,Identifier) VALUES (?,?)');
        $stmt->bind_param('ss', $user,$identifier);
        $stmt->execute();

    }
    for ($i=0;$i<250;$i++){
        $user=rand(0,$totalusers);
        $identifier='HCR24033';
        $stmt = $conn->prepare('INSERT INTO user_coins(id_user,Identifier) VALUES (?,?)');
        $stmt->bind_param('ss', $user,$identifier);
        $stmt->execute();

    }
}

//populateUserCoins();