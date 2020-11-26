<?php 

require_once "statistics.php";

function createFeed(){
    $popularCoins=forfeed();
    $file='D:\Projects\TW\htdocs\tw\Resources\db\feed.xml';
    unlink($file);
    //$current=file_get_contents($file);
    $feed="<?xml version=\"1.0\" encoding = \"utf-8\"?>
    <rss version=\"2.0\">
    <channel>
    <title> Coins feed </title>
    <link> http://localhost/tw/Resources/coins/search.php </link>
    <description> This is just a description for the Coins Feed </description>
    <language>en-us </language>
    <image>
    <title> Breathing is fun </title>
    <link>https://i.ytimg.com/vi/2GSfYoGZYW4/</link>
    <url>https://i.ytimg.com/vi/2GSfYoGZYW4/hqdefault.jpg</url>
    <width> 150 </width>
    <height> 124 </height>
    </image>
    <item>
    <title> Most popular coins up so far  </title>
    <link>http://localhost/tw/Resources/coins/search.php </link>
    <description> <![CDATA[ 
    ";
    $endfeed="]]> </description>
    </item>
    </channel>
    </rss>";
    $content='';
    for($i=0;$i<count($popularCoins);$i++){
        for($j=0;$j<count($popularCoins[$i]);$j++){
            $content.=$popularCoins[$i][$j] . "<br>";
        }
        $content.="<br>";
    }
    $end=$feed . $content . $endfeed;

    $ok=file_put_contents($file,$end);
    echo $ok . 'somethingh';
    echo"<br>";

    print_r($popularCoins);
    return $ok;

    

}
createFeed();