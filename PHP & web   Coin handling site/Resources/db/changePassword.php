<?php
require_once 'db_connection.php';
?>

 
<head><meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="../style/style3.css">
    <title>Profile->Change Password</title>
</head>

<body>
    <?php include '../navbar.php'; ?>
    <div id="red-canvas">
        <div>
            <h1>Change Password</h1>

            <form action="changePassword.php" method="get">
                <br>
                <input type="text" name="oldpassword" placeholder="Old password" maxlength="50">
                <br>
                <input type="password" name="password" placeholder="New password" maxlength="50" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" title="Must contain at least one  number and one uppercase and lowercase letter, and at least 8 or more characters">
                <br>
                <input type="password" name="password2" placeholder="Repeat new password" maxlength="50">
                <br>
                <input type="submit" value="Submit">
            </form>
            <?php
            function chEmail()
            {
                if (isset($_GET) && count($_GET) >= 3) {
                    if ((string) $_GET['password'] == (string) $_GET['password2']) {
                        if (changePassword((string) $_GET['oldpassword'], (string) $_GET['password'])) {
                            //header("Location: " . URL . "/Resources/profile/profile.php"); //obsolete on azure
                            //echo "<script> window.location.replace('" . URL . "/Resources/profile/profile.php') </script>";
                        } else {
                            echo "bad old password";
                        }
                    } else {
                        echo "passwords don't match";
                    }
                } else {
                    echo "please input passwords";
                }
            }
            chEmail();

            ?>
        </div>
    </div>
</body>



