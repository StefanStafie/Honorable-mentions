<?php
require_once 'db_connection.php';
if (isset($_SESSION['compareIndex'])) {
  if ($_SESSION['compareIndex'] == 1) {
    $_SESSION['compare1'] = $_GET['coinId'];
  } else {
    $_SESSION['compare2'] = $_GET['coinId'];
  }
} else {
  $_SESSION['compareIndex'] = 2;
  $_SESSION['compare1'] = $_GET['coinId'];
}
