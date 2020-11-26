<?php
if (!isset($_SESSION)) {
    session_start();
}
?>
  <!DOCTYPE HTML> <html lang='en'>
<head><meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="../style/style3.css">
    <title>Profile</title>
</head>

<body>
    <?php include '../navbar.php'; ?>
    <div id="red-canvas">
        <?php
        if (!isset($_SESSION['user_id'])) {
            echo "<div><h1> You are currently just a GUEST here.</h1><br>
            <h1> Would you like to <a href = \"../db/login.php\">log in</a> or <a href = \"../db/register.php\">register</a>?</h1></div>";
        } else {
            echo
                "<div>
                <h1>Hello " . $_SESSION['name'] . "</h1> 
                <h3> Your email: " . $_SESSION['email'] . "</h3>
                <h3> First Name: " . $_SESSION['firstName'] . "</h3>
                <h3> Last Name: " . $_SESSION['lastName'] . "</h3>
            </div>
            <div>
                <h3>Not much to do here</h3><br>
                <p><a href = \"../db/statsPage.php\">Statistics</a></p>
                <p><a href = \"../db/changeEmail.php\">Change email</a></p>
                <p><a href = \"../db/changePassword.php\">Change password</a></p>
                <p><a href = \"../db/signout.php\">Sign Out</a></p>
                <p><a href = \"../db/deleteUser.php\">DELETE ACCOUNT</a></p>";
            echo "</div>";
        }
        ?>
    </div>
</body>

</html>