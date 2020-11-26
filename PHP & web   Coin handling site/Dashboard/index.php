<?php
require_once 'db_connection.php';

?>
  <!DOCTYPE HTML> <html lang='en'>
<head><meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="Style/adminStyle.css">
    <title>Dashboard</title>
</head>

<body>

    <div class="left">
        <h1>Sign in</h1>
        <form action="index.php" method="get" autocomplete="off">
            <input type="text" name="name" placeholder="Username">
            <br>
            <input type="password" name="password" placeholder="Password">
            <br><br>
            <input type="submit" value="login" />
        </form>
        <br>
        <?php
        function login()
        {
            if (isset($_GET)) {
                if (count($_GET) >= 2) {
                    if (loginAdmin((string) $_GET['name'], (string) $_GET['password'])) {
                        echo "<script> window.location.replace('" . URL . "/Dashboard/controller.php') </script>";
                    } else {
                        echo "Bad credentials. Try again.";
                    }
                } else {
                    echo "please input username and password";
                }
            }
        }
        login();
        ?>
    </div>



</body>

</html>