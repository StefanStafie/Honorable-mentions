<?php
require_once 'db_connection.php';

echo addUserCoin($_SESSION['user_id'], $_GET['coinId']);
?>

