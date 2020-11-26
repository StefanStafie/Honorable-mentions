<?php 
require('../fpdf/fpdf.php');
require('statistics.php');

function exportstatistics(){ // Most popular coins with information about THE MOST popular coin
    $pdf=new FPDF();
$pdf->AddPage();
$pdf->SetFont('Times','B',23);
$pdf->Cell(0,10,'STATISTICS',0,1,'C');
$pdf->SetFont('Arial','B',13);
$pdf->Cell(0,10,'Most popular coins in DB :',0,1);
$temp=mostpopularcoins();
$pdf->SetFont('Arial','',13);
for($i=0;$i<count($temp);$i++){
    $pdf->Cell(0,10,$temp[$i],0,1);
}
$temp=mostpopularcoin();
$pdf->SetFont('Arial','B',13);
$pdf->Cell(0,10,$temp[0],0,1);
$pdf->SetFont('Arial','',13);
for($i=1;$i<count($temp)-2;$i++){
    $pdf->Cell(0,10,$temp[$i],0,1);
}
$pdf->Image($temp[$i],10,110,30);
$pdf->Image($temp[$i+1],50,110,30);
//$pdf->Cell(40,0,$temp[0]);
$pdf->Output();

}
function exportPopularCoin(){ // only most popular coins
    $pdf=new FPDF();
$pdf->AddPage();
$pdf->SetFont('Times','B',23);
$pdf->Cell(0,10,'STATISTICS',0,1,'C');
$pdf->SetFont('Arial','B',13);
$pdf->Cell(0,10,'Most popular coin in DB :',0,1);
$temp=mostpopularcoin();
$pdf->SetFont('Arial','',13);
for($i=0;$i<count($temp)-2;$i++){
    $pdf->Cell(0,10,$temp[$i],0,1);
}
$pdf->Image($temp[$i],10,130,30);
$pdf->Image($temp[$i+1],50,130,30);
$pdf->Output();

}

function exportStats(){ // only most popular coins
    $pdf=new FPDF();
$pdf->AddPage();
$pdf->SetFont('Times','B',23);
$pdf->Cell(0,10,'STATISTICS',0,1,'C');
$pdf->SetFont('Arial','B',13);
$pdf->Cell(0,10,'Most popular coins in DB :',0,1);
$temp=mostpopularcoins();
$pdf->SetFont('Arial','',13);
for($i=0;$i<count($temp);$i++){
    $pdf->Cell(0,10,$temp[$i],0,1);
}
$pdf->Output();

}

function exportaveragesfromuser($userid){ // Averages from userdb
    $pdf=new FPDF();
$pdf->AddPage();
$pdf->SetFont('Times','B',23);
$pdf->Cell(0,10,'STATISTICS',0,1,'C');
$pdf->SetFont('Arial','B',13);
$pdf->Cell(0,10,'Average weight of your coins :',0,1);
$temp=averageweightfromusercoins($userid);
$pdf->SetFont('Arial','',13);
$pdf->Cell(0,10,$temp,0,1);
$pdf->SetFont('Arial','B',13);
$pdf->Cell(0,10,'Average diameter of your coins :',0,1);
$temp=averagediameterfromusercoins($userid);
$pdf->SetFont('Arial','',13);
$pdf->Cell(0,10,$temp,0,1);


$pdf->Output();

}

function exportaveragesfromdb(){ // Averages from userdb
    $pdf=new FPDF();
$pdf->AddPage();
$pdf->SetFont('Times','B',23);
$pdf->Cell(0,10,'STATISTICS',0,1,'C');
$pdf->SetFont('Arial','B',13);
$pdf->Cell(0,10,'Average weight of coins :',0,1);
$temp=averageweightfromdb();
$pdf->SetFont('Arial','',13);
$pdf->Cell(0,10,$temp,0,1);
$pdf->SetFont('Arial','B',13);
$pdf->Cell(0,10,'Average diameter of coins :',0,1);
$temp=averagediameterfromdb();
$pdf->SetFont('Arial','',13);
$pdf->Cell(0,10,$temp,0,1);


$pdf->Output();

}

function exportcoinsinfo($userid){
    $pdf=new FPDF();
    $pdf->AddPage();
    $pdf->SetFont('Times','B',23);
$pdf->Cell(0,10,'STATISTICS',0,1,'C');
    $pdf->SetFont('Arial','B',13);
    $pdf->Cell(0,10,'Your coins :',0,1);
    $temp=infoaboutyourcoins($userid);
    $pdf->SetFont('Arial','',13);
    for($i=0;$i<count($temp);$i++){
        $pdf->Cell(0,10,'Identifier : ' . $temp[$i][3],0,1);
        $pdf->Cell(0,10,'Weight : ' . $temp[$i][0],0,1);
        $pdf->Cell(0,10,'Diameter :' . $temp[$i][1],0,1);
        $pdf->Cell(0,10,'Axis :' . $temp[$i][2],0,1);
        $pdf->Cell(0,10,'Collection :' .$temp[$i][4],0,1);
        $pdf->Cell(0,10,'',0,1);
    }
    
    $pdf->Output();

}

function exportrarity ($identifier){
    $pdf=new FPDF();
    $pdf->AddPage();
    $pdf->SetFont('Times','B',23);
$pdf->Cell(0,10,'STATISTICS',0,1,'C');
    $pdf->SetFont('Arial','B',13);
    $pdf->Cell(0,10,'How many other people have that coin :',0,1);
    $temp=rarity($identifier);
    $pdf->SetFont('Arial','',13);

     $pdf->Cell(0,10,$temp,0,1);
    
    $pdf->Output();

}

function  tocsv($userid){ //info about your coins
    
    //$out = fopen('php://output', 'w');
    //fputcsv($out, array('this','is some', 'csv "stuff", you know.'));
    //fclose($out);
    /*$conn = OpenCon();
    
        $result = $conn->query('SELECT weight,diameter,axis,identifier,collection FROM coins limit 2000');
        $result2 = $result->fetch_all();
        if (count($result2) <= 0) {
            //databaseToSession($result2);
            echo"not wokring 1";
            return -1;
        }
    */
    $result2=infoaboutyourcoins($userid);
    $delimiter = ",";
        $filename = "YourCoins" . date('Y-m-d') . ".csv";
        
        //create a file pointer
        $f = fopen('php://memory', 'w');
        
        //set column headers
        $fields = array('Identifier', 'Diameter', 'Axis', 'Weight', 'Collection');
        fputcsv($f, $fields, $delimiter);
        
        //output each row of the data, format line as csv and write to file pointer
        for ($i=0;$i<count($result2);$i++){
            $lineData = array($result2[$i][3], $result2[$i][1], $result2[$i][2], $result2[$i][0], $result2[$i][4]);
            fputcsv($f, $lineData, $delimiter);
        }
        
        //move back to beginning of file
        fseek($f, 0);
        
        //set headers to download file rather than displayed
        header('Content-Type: text/csv');
        header('Content-Disposition: attachment; filename="' . $filename . '";');
        
        //output all remaining data on a file pointer
        fpassthru($f);
    
    
    }


function  tocsv2(){ // most popular coins

    $result2=forcsv1();
    $delimiter = ",";
        $filename = "PopularCoins" . date('Y-m-d') . ".csv";
        
        //create a file pointer
        $f = fopen('php://memory', 'w');
        
        //set column headers
        $fields = array('Identifier', 'Users',);
        fputcsv($f, $fields, $delimiter);
        
        //output each row of the data, format line as csv and write to file pointer
        for ($i=0;$i<count($result2);$i++){
            $lineData = array($result2[$i][1], $result2[$i][0]);
            fputcsv($f, $lineData, $delimiter);
        }
        
        //move back to beginning of file
        fseek($f, 0);
        
        //set headers to download file rather than displayed
        header('Content-Type: text/csv');
        header('Content-Disposition: attachment; filename="' . $filename . '";');
        
        //output all remaining data on a file pointer
        fpassthru($f);
    
    
    }
function  tocsv3(){ //info about most popular coin

    $result2=forcsv2();
    $delimiter = ",";
        $filename = "MostPopularCoin" . date('Y-m-d') . ".csv";
        
        //create a file pointer
        $f = fopen('php://memory', 'w');
        $fields = array('Identifier', 'Diameter', 'Weight', 'Axis', 'Collection','MoreInfo');
        fputcsv($f, $fields, $delimiter);
        
        for ($i=0;$i<count($result2);$i++){
            $lineData = array($result2[$i][4], $result2[$i][2], $result2[$i][1], $result2[$i][3], $result2[$i][6],$result2[$i][0]);
            fputcsv($f, $lineData, $delimiter);
        }

        fseek($f, 0);
        
        //set headers to download file rather than displayed
        header('Content-Type: text/csv');
        header('Content-Disposition: attachment; filename="' . $filename . '";');
        
        //output all remaining data on a file pointer
        fpassthru($f);
    
    
    }
function  tocsvaveragesuser($userid){ //AveragesAboutYourCoins

    $result2=averageweightfromusercoins($userid);
    $result3=averagediameterfromusercoins($userid);
    $delimiter = ",";
        $filename = "AverageFromUser" . date('Y-m-d') . ".csv";
        
        //create a file pointer

        $f = fopen('php://memory', 'w');
        $fields = array('Average weight', 'Average diameter');
        fputcsv($f, $fields, $delimiter);
        
            $lineData = array($result2, $result3);
            fputcsv($f, $lineData, $delimiter);
        

        fseek($f, 0);
        
        //set headers to download file rather than displayed
        header('Content-Type: text/csv');
        header('Content-Disposition: attachment; filename="' . $filename . '";');
        
        //output all remaining data on a file pointer
        fpassthru($f);
    
    
    }
    function  tocsvaveragesdb(){ //AveragesAboutYourCoins

        $result2=averageweightfromdb($userid);
        $result3=averagediameterfromdb($userid);
        $delimiter = ",";
            $filename = "AverageFromDB" . date('Y-m-d') . ".csv";
            
            //create a file pointer
            $f = fopen('php://memory', 'w');
            $fields = array('Average weight', 'Average diameter');
            fputcsv($f, $fields, $delimiter);
            $lineData = array($result2, $result3);
            
             fputcsv($f, $lineData, $delimiter);
            
    
            fseek($f, 0);
            
            //set headers to download file rather than displayed
            header('Content-Type: text/csv');
            header('Content-Disposition: attachment; filename="' . $filename . '";');
            
            //output all remaining data on a file pointer
            fpassthru($f);
        
        
        }

function REST(){
    if (isset($_GET)){
        if(isset($_GET['do'])){
            switch ($_GET['format']){
                case 'CSV':
                    switch($_GET['do']){
                        case 'AveragesUser': 
                            tocsvaveragesuser($_SESSION['user_id']);
                        break;

                        case  'AverageDB':
                            tocsvaveragesdb();
                        break;

                        case 'MostPopularCoin':
                            tocsv3();
                        break;

                        case 'PopularCoins':
                            tocsv2();
                        break;
                        case 'YourCoins':
                            tocsv($_SESSION['user_id']);
                        break;

                    }
                break;
                case 'PDF':
                    switch($_GET['do']){
                        case 'AveragesUser': 
                            exportaveragesfromuser($_SESSION['user_id']);
                        break;

                        case  'AverageDB':
                            exportaveragesfromdb();
                        break;

                        case 'MostPopularCoin':
                            exportPopularCoin();
                        break;

                        case 'PopularCoins':
                            exportstatistics();
                        break;
                        case 'YourCoins':
                            exportcoinsinfo($_SESSION['user_id']);
                        break;

                    }
                break;
            }
        }
        else{
            echo'where the DO at??';
            return -1;
        }
    }else{
        echo'Where GET at??';
        return -1;
    }
}
REST();



