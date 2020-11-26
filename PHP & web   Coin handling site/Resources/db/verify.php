   
<?php
require_once 'db_connection.php';
function verifyEmail()
{
    var_dump($_SESSION);
    if (isset($_SESSION['name']) && isset($_SESSION['password']) && isset($_SESSION['user_id'])) {//daca e logat, nu mai intra pe formular 
        if (verify($_SESSION['name'], $_SESSION['password'], $_SESSION['user_id'])) {
            //header("Location: " . URL . "/Resources/profile/profile.php");//obsolete on azure
            //echo "<script> window.location.replace('" . URL . "/Resources/profile/profile.php') </script>";
        }
        echo "email has already been verified";
    } else {
        //header("Location: " . URL . "/Resources/db/login.php");
        //echo "<script> window.location.replace('" . URL . "/Resources/db/login.php') </script>";
    }
}
verifyEmail();
?>