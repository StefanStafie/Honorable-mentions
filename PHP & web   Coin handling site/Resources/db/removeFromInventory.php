<?php
require_once 'db_connection.php';

echo removeUserCoin($_SESSION['user_id'], $_GET['coinId']);
?>

