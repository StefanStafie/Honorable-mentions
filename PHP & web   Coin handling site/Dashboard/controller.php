<?php
require_once 'db_connection.php';

?>
  <!DOCTYPE HTML> <html lang='en'>
<head><meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="Style/adminStyle.css">
    <title>Dashboard->DatabaseController</title>
</head>

<body>
    <h3>Control your database here</h3>
    <div class="left">

        <form action="controller.php" method="POST" autocomplete="off">
            <input id="command" type="text" name="command" placeholder="sql command">
            <input type="submit" value="Execute" />
        </form>
    </div>
    <br>
    <div id="result">
        <pre>
        <?php
        if (isset($_POST)) {
            if (count($_POST) >= 1) {
                if (loginAdmin((string) $_SESSION['name'], (string) $_SESSION['password'])) {
                    $raspuns = executeSql($_POST['command']);
                    echo '<table style="width:100%">';
                    foreach ($raspuns as $rasp) {
                        echo '<tr>';
                        foreach ($rasp as $r) {
                            echo "<td>" . $r . "</td>";
                        }
                        echo "</tr>";
                    }
                } else {
                    echo "Bad credentials. Please login again.";
                }
            } else {
                echo "Enter a command";
            }
        }

        ?>
    </pre>
    </div>


</body>

</html>