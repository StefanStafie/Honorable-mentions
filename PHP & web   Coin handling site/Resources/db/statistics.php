<?php 
require_once 'db_connection.php';



function mostpopularcoins(){
    $output=array();
    $conn = OpenCon();
    /*check if username exists*/
    $result = $conn->query('SELECT count(1),Identifier FROM user_coins group by Identifier order by 1 DESC limit 25 ');
    $result2 = $result->fetch_all();
    if (count($result2) <= 0) {
        //databaseToSession($result2);
        echo"not wokring";
        return -1;
    } else {
       // echo 'Popular coins: <br>';
        for ($i=0; $i<count($result2); $i++)
            $output[$i]= 'Identifier: ' . $result2[$i][1] . '. Number of users with this coin: '.$result2[$i][0];
        
    }
    CloseCon($conn);
    return $output;
} //SELECT count(1),Identifier FROM user_coins group by Identifier order by 1 DESC

//mostpopularcoins();


function mostpopularcoin(){
    $output=array();
    $conn = OpenCon();
    /*check if username exists*/
    $result = $conn->query('SELECT count(1),Identifier FROM user_coins group by Identifier order by 1 DESC');
    $result2 = $result->fetch_all();
    if (count($result2) <= 0) {
        //databaseToSession($result2);
        echo"not wokring 1";
        return -1;
    }  
    $identifier=$result2[0][1];

    $result = $conn->query('SELECT * FROm coins where Identifier= \''.$identifier . '\'');
    $result2 = $result->fetch_all();
    if (count($result2) <= 0) {
        //databaseToSession($result2);
        echo"not wokring 2";
        return -1;
    }
    else{
        
        $output[0]='Most Popular Coin: ';
        $output[1]= 'Identifier: ' . $result2[0][4];
        $output[2]= 'Diameter: ' .$result2[0][2];
        $output[3]= 'Weight: ' . $result2[0][1];
        $output[4]= 'Axis: ' . $result2[0][3];
        $output[5]='Collection: ' . $result2[0][6];           
        $output[6]= 'More about this coin :' . $result2[0][0];
        $output[7]='More about the collection :' .  $result2[0][5];
              
        $output[8]=$result2[0][11];
        $output[9]=$result2[0][14];
        
        
        //echo "most Popular coin:" . $identifier;
        //printCoin($result2[0][4], $result2[0][2], $result2[0][1], $result2[0][3], $result2[0][6], $result2[0][0], $result2[0][5], $result2[0][11], $result2[0][14]);
    }  //$identifier, $diameter, $weight, $axis, $collection, $coinUrl, $collUrl, $obverse, $reverse

    CloseCon($conn);
    return $output;
}
function forcsv1(){
    $output=array();
    $conn = OpenCon();
    /*check if username exists*/
    $result = $conn->query('SELECT count(1),Identifier FROM user_coins group by Identifier order by 1 DESC limit 25 ');
    $result2 = $result->fetch_all();
    if (count($result2) <= 0) {
        //databaseToSession($result2);
        echo"not wokring";
        return -1;
    }
    CloseCon($conn);
    return $result2;
}
function forcsv2(){
    $output=array();
    $conn = OpenCon();
    $identifier=forcsv1()[0][1];
    $result = $conn->query('SELECT * FROm coins where Identifier= \''.$identifier . '\'');
    $result2 = $result->fetch_all();
    if (count($result2) <= 0) {
        //databaseToSession($result2);
        echo"not wokring 2";
        return -1;
    }
    CloseCon($conn);
    return $result2;
}


function averagediameterfromdb(){
    $conn = OpenCon();

    $result = $conn->query('SELECT avg(diameter) FROM coins limit 2000');
    $result2 = $result->fetch_all();
    if (count($result2) <= 0) {
        //databaseToSession($result2);
        echo"not wokring 1";
        return -1;
    }
    else{
       // echo "Average diameter of coins in DB: ";
        //print_r($result2[0][0]);
        return $result2[0][0];
    }


}
function averageweightfromdb(){
    $conn = OpenCon();

    $result = $conn->query('SELECT avg(weight) FROM coins limit 2000');
    $result2 = $result->fetch_all();
    if (count($result2) <= 0) {
        //databaseToSession($result2);
        echo"not wokring 1";
        return -1;
    }
    else{
       // echo "Average weight of coins in your inventory: ";
        //print_r($result2[0][0]);
        return $result2[0][0];
    }


}
function averageweightfromusercoins($userID){

    $conn = OpenCon();

    $result = $conn->query('SELECT avg(weight) FROM coins join user_coins on coins.identifier=user_coins.Identifier where id_user= \''. $userID .'\'limit 2000');
    $result2 = $result->fetch_all();
    if (count($result2) <= 0) {
        //databaseToSession($result2);
        echo"not wokring 1";
        return -1;
    }
    else{
        //echo "Average weight of coins in DB: ";
       // print_r($result2[0][0]);
        return $result2[0][0];
    }


} 
function averagediameterfromusercoins($userID){
    $conn = OpenCon();

    $result = $conn->query('SELECT avg(diameter) FROM coins join user_coins on coins.identifier=user_coins.Identifier where id_user= \''. $userID .'\'limit 2000');
    $result2 = $result->fetch_all();
    if (count($result2) <= 0) {
        //databaseToSession($result2);
        echo"not wokring 1";
        return -1;
    }
    else{
       // echo "Average diameter of coins in your inventory: ";
        //print_r($result2[0][0]);
        return $result2[0][0];
    }


}



function mostpopularcollection(){
    $conn = OpenCon();

    $result = $conn->query('SELECT count(collection) as nr,collection from coins join user_coins on coins.identifier=user_coins.Identifier group by collection order by 1');
    $result2 = $result->fetch_all();
    if (count($result2) <= 0) {
        //databaseToSession($result2);
        echo"not wokring 1";
        return -1;
    }else{
        $collection=$result2[0][1];
    }
    return $collection;
    //print_r($collection);

    

}

function infoaboutyourcoins($userID){
    $conn = OpenCon();

    $result = $conn->query('SELECT weight,diameter,axis,coins.identifier,collection from coins join user_coins on coins.identifier=user_coins.Identifier where user_coins.ID_user=' . $userID );

    $result2 = $result->fetch_all();
    if (count($result2) <= 0) {
        //databaseToSession($result2);
        echo"not wokring 1";
        return -1;
    }else{
       
    
    //print_r($result2);
    return $result2;
    }
    //print_r($collection);

}

function rarity($identifier){ //AKA how many other users have that coin
    $conn = OpenCon();

    $result = $conn->query('SELECT count(identifier) from user_coins where identifier= \'' . $identifier . '\'');
    $result2 = $result->fetch_all();
    if (count($result2) <= 0) {
        //databaseToSession($result2);
        echo"not wokring 1";
        return -1;
    }else{
        $number=$result2[0][0] -1;
    }
    //print_r($number);
    return $number;


}

function forexport1(){
    $output=array();
    $conn = OpenCon();
    /*check if username exists*/
    $result = $conn->query('SELECT count(1),Identifier FROM user_coins group by Identifier order by 1 DESC limit 3 ');
    $result2 = $result->fetch_all();
    if (count($result2) <= 0) {
        //databaseToSession($result2);
        echo"not wokring";
        return -1;
    } else {
       // echo 'Popular coins: <br>';
        for ($i=0; $i<count($result2); $i++)
            $output[$i]= $result2[$i][1];
        
    }
    CloseCon($conn);
    return $output;

}
function forfeed(){
    $output=array();
    $conn = OpenCon();
    $temp=forexport1();
    $final=array();
    for ($i=0;$i<count($temp);$i++){
    $result = $conn->query('SELECT * FROm coins where Identifier= \''.$temp[$i] . '\'');
    $result2 = $result->fetch_all();
    if (count($result2) <= 0) {
        //databaseToSession($result2);
        echo"not wokring 2";
        return -1;
    }
    else{
        
        $output[0]='Most Popular Coin: ';
        $output[1]= 'Identifier: ' . $result2[0][4];
        $output[2]= ' Diameter: ' .$result2[0][2];
        $output[3]= '  Weight: ' . $result2[0][1];
        $output[4]= '   Axis: ' . $result2[0][3];
        $output[5]='     Collection: ' . $result2[0][6];           
        $output[6]= 'More about this coin :' . $result2[0][0];
        $output[7]='More about the collection :' .  $result2[0][5];
              
        $output[8]= "<img src= \"" .$result2[0][11] . "\" alt=\"\" height=\"150\" width=\"150\">";
        $output[9]="<img src= \"" .$result2[0][14] . "\" alt=\"\" height=\"150\" width=\"150\">";
        
        
           } 
           $final[$i]=$output;
        } 
    
    CloseCon($conn);
    print_r($final);
    return $final;

}
//forfeed();
//tocsv();
/*echo '<br><br>';
//mostpopularcoin();
echo '<br><br>';
averagediameterfromdb();
echo '<br><br>';
averageweightfromdb();
echo '<br><br>';
//averageweightfromusercoins(1);
echo '<br><br>';
averagediameterfromusercoins(1);*/
//mostpopularcollection();
//rarity('HCR24033');
//infoaboutyourcoins(1);
forcsv2();