<?php
if (!isset($_SESSION)) {
    session_start();
}
require_once 'config.php';
function OpenCon()
{
    $conn = new mysqli(DBHOST, DBUSER, DBPASS, DB) or die("Connect failed: %s\n" . $conn->error);
    return $conn;
}

function CloseCon($conn)
{
    $conn->close();
}

function databaseToSession($worker)
{
    $_SESSION['user_id'] = $worker[0][0];
    $_SESSION['name'] = $worker[0][1];
    $_SESSION['password'] = $worker[0][2];
    $_SESSION['firstName'] = $worker[0][3];
    $_SESSION['lastName'] = $worker[0][4];
    $_SESSION['email'] = $worker[0][5];
    $_SESSION['verified'] = $worker[0][6];
}
function registerUser($firstName, $lastName, $name, $password, $email)
{
    $conn = OpenCon();
    /*check if username exists*/
    $result = $conn->query('Select id FROM users WHERE username = \'' . $name . '\'');
    if (count($result->fetch_all()) > 0) {
        echo "Username is already used.";
        CloseCon($conn);
        return false;
    } else {
        /*if user does not exist, create him*/
        $conn = OpenCon();
        $stmt = $conn->prepare('INSERT INTO users (first_name, last_name, username, password, email) VALUES (?,?,?,?,?)');
        $stmt->bind_param('sssss', $firstName, $lastName, $name, $password, $email);
        $stmt->execute();
        $result = $conn->query('Select * FROM users WHERE username = \'' . $name . '\' AND password = \'' . $password . '\'');
        $result2 = $result->fetch_all();
        databaseToSession($result2);
        CloseCon($conn);
        return true;
    }
}

function loginUser($name, $password)
{
    $conn = OpenCon();
    /*check if username exists*/
    $result = $conn->query('Select * FROM users WHERE username = \'' . $name . '\' AND password = \'' . $password . '\'');
    $result2 = $result->fetch_all();
    if (count($result2) > 0) {
        databaseToSession($result2);
        return true;
    } else {
        /*if user does not exist, error*/
        echo "Bad username and password.";
        return false;
    }
    CloseCon($conn);
}

function changeEmail($email, $password)
{
    $conn = OpenCon();
    $sql = 'UPDATE users SET email = \'' . $email . '\' 
                                where id = \'' . $_SESSION['user_id'] .  '\' AND password = \'' . $password . '\'';


    if (mysqli_query($conn, $sql)) {
        if (mysqli_affected_rows($conn) < 1) {
            return false;
        }
        $result = $conn->query('Select * FROM users WHERE username = \'' . $_SESSION['name'] . '\' AND password = \'' . $password . '\'');
        $result2 = $result->fetch_all();
        databaseToSession($result2);
        return true;
    } else {
        echo "Error updating record: " . mysqli_error($conn);
        return false;
    }
}

function changePassword($old, $password)
{
    $conn = OpenCon();
    $sql = 'UPDATE users SET password = \'' . $password . '\' 
                                where id = \'' . $_SESSION['user_id'] .  '\' AND password = \'' . $old . '\'';


    if (mysqli_query($conn, $sql)) {
        if (mysqli_affected_rows($conn) < 1) {
            return false;
        }
        $_SESSION['password'] = $password;
        return true;
    } else {
        echo "Error updating record: " . mysqli_error($conn);
        return false;
    }
}

function verify($name, $password, $code)
{
    $conn = OpenCon();
    $sql = 'UPDATE users SET verified = \'1\' 
                                where username = \'' . $name .  '\' AND password = \'' . $password . '\' AND id = \'' . $code . '\' ';


    if (mysqli_query($conn, $sql)) {
        if (mysqli_affected_rows($conn) < 1) {
            return false;
        }
        $_SESSION['verified'] = '1';
        return true;
    } else {
        echo "Error updating record: " . mysqli_error($conn);
        return false;
    }
}
function deleteUser($id)
{
    $conn = OpenCon();
    $sql = 'DELETE FROM users WHERE id=' . $id;

    if (mysqli_query($conn, $sql)) {
        if (mysqli_affected_rows($conn) < 1) {
            return false;
        }
        return true;
    } else {
        echo "Error updating record: " . mysqli_error($conn);
        return false;
    }
}

function sendMail($email)
{ //this doesn't work. Need to setup server.
    // The message
    $message = "Line 1\r\nLine 2\r\nLine 3";

    // In case any of our lines are larger than 70 characters, we should use wordwrap()
    $message = wordwrap($message, 70, "\r\n");

    // Send
    mail('stefanstafie99@gmail.com', 'My Subject', "vjkavsjadkbhasvhiBEJCBFKSDBFSDJKBFJKSBFJKSBJK");
}

//-------------------------------------------------------COINS------------------------------------

function getFirstCoins($filter, $limit, $page)
{
    $conn = OpenCon();
    $result = $conn->query("Select * FROM coins " . $filter . " LIMIT " . $limit. " OFFSET " . $limit*($page-1));
    if (!$result)
        return false;
    return $result->fetch_all();
    CloseCon($conn);
}
function getFirstCoinsTotalCount($filter)
{
    $conn = OpenCon();
    $result = $conn->query("Select count(*) FROM coins " . $filter);
    if (!$result)
        return false;
    return $result->fetch_all();
    CloseCon($conn);
}

function getMyCoins($filter, $limit, $page)
{
    $conn = OpenCon();
    $result = $conn->query("SELECT * FROM coins c join user_coins u on c.identifier = u.id_coin where u.id_user = " .  $_SESSION['user_id'] . " " . $filter . " LIMIT " . $limit. " OFFSET " . $limit*($page-1));
    if (!$result)
        return false;
    return $result->fetch_all();
    CloseCon($conn);
}
function getMyCoinsTotalCount($filter)
{
    $conn = OpenCon();
    $result = $conn->query("SELECT count(*) FROM coins c join user_coins u on c.identifier = u.id_coin where u.id_user = " .  $_SESSION['user_id'] . " " . $filter );
    if (!$result)
        return false;
    return $result->fetch_all();
    CloseCon($conn);
}

function findCoin($identifier)
{
    $conn = OpenCon();
    $result = $conn->query("SELECT * FROM coins c WHERE identifier = '" . $identifier . "' LIMIT 1");
    if (!$result)
        return false;
    return $result->fetch_all();
    CloseCon($conn);
}
function addCoin($diameter, $weight, $axis, $collection, $coinUrl, $collUrl, $obverse, $reverse)
{
    $conn = OpenCon();
    $stmt = $conn->prepare('INSERT INTO coins (diameter, weight, axis, collection, object, col_uri, obv_ref, rev_ref) VALUES (?,?,?,?,?,?,?,?)');
    $stmt->bind_param('ssssssss', $diameter, $weight, $axis, $collection, $coinUrl, $collUrl, $obverse, $reverse);
    $worked = $stmt->execute();
    CloseCon($conn);
    return $worked;
}

//--------------------------------------- USER_COINS ------------------------------

function addUserCoin($userId, $coinId) {
    $conn = OpenCon();
    $stmt = $conn->prepare("INSERT INTO user_coins (id_user, id_coin) VALUES (?,?)");
    $stmt->bind_param("ss", $userId, $coinId); 
    $worked = $stmt->execute();
    CloseCon($conn);
    return $worked;

}

//admin

function loginAdmin($name, $password){
    $conn = OpenCon();
    /*check if username exists*/
    $result = $conn->query('Select * FROM admins WHERE username = \'' . $name . '\' AND password = \'' . $password . '\'');
    $result2 = $result->fetch_all();
    if (count($result2) > 0) {
        $_SESSION['name'] = $result2[0][0];
        $_SESSION['password'] = $result2[0][1];
        return true;
    } else {
        /*if user does not exist, error*/
        echo "Bad username and password.";
        return false;
    }
    CloseCon($conn);
}

function executeSql($sql){
    $conn = OpenCon();
    /*check if username exists*/
    $result = $conn->query($sql);
    if(gettype($result) == "boolean"){
        if($result){
            $result2 = array(array("It has been done", "success", "great job"));
        } else {
            $result2 = array(array("Error", "Something wrong"));
        }
    } else{
        $result2 = $result->fetch_all();
    }
    return $result2;
    CloseCon($conn);
}