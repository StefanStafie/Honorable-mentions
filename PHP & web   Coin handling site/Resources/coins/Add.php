  <!DOCTYPE HTML> <html lang='en'>
<head><meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="../style/style3.css">
    <link rel="stylesheet" href="../style/singleCoin.css">
    <link rel="stylesheet" href="../style/AddStyle.css">
    <link rel="stylesheet" href="../style/SearchStyle.css">

    <title>Search</title>
</head>

<body>

    <?php include '../navbar.php'; ?>
    <div id="red-canvas-orizontal">
        <section id='search-bar'>
            <p>Add a coin to the database</p>
            <form action="add.php" method="post" enctype="multipart/form-data">
                <table>
                    <tr>
                        <td><label>Weight:</label></td>
                        <td><input type="text" name="weight" placeholder="weight" maxlength="20" required></td>
                        <td><label>Diameter:</label></td>
                        <td><input type="text" name="diameter" placeholder="diameter" maxlength="20" required></td>
                    </tr>
                    <tr>
                        <td><label>Collection link:</label></td>
                        <td><input type="text" name="collUrl" placeholder="collection url" maxlength="50"></td>
                        <td><label>Collection:</label></td>
                        <td><input type="text" name="collection" placeholder="collection" maxlength="20"></td>
                    </tr>
                    <tr>
                        <td><label>Axis:</label></td>
                        <td><input type="text" name="axis" placeholder="axis" maxlength="20"></td>
                        <td><label>More details link:</label></td>
                        <td><input type="text" name="coinUrl" placeholder="coin url" maxlength="50"></td>

                    </tr>
                    <tr>
                        <td><label>Image:</label></td>
                        <td><input type="file" name="obv" id="obv" accept="image/jpg, image/jpeg" required></td>
                        <td><label>identifier:</label></td>
                        <td><input type="text" name="identifier" placeholder="try a long number" required></td>
                    </tr>

                </table>
                <br><br>
                <input type="submit" name="submit" value="Add coin" />
            </form>
        </section>
        <br><br>

        <?php
        require_once 'CoinFunctions.php';
        require_once '../db/db_connection.php';


        if (isset($_POST) && isset($_POST["submit"])) {
            $target_dir = "uploads/";
            $target_file = $target_dir . basename($_FILES["obv"]["name"]);
            $uploadOk = 1;
            $imageFileType = strtolower(pathinfo($target_file, PATHINFO_EXTENSION));

            // Check if image file is a actual image or fake image

            $check = getimagesize($_FILES["obv"]["tmp_name"]);
            if ($check == false) {
                echo "File is not an image.";
                $uploadOk = 0;
            } else {
                // Check if file already exists
                if (file_exists($target_file)) {
                    echo "Sorry, file already exists.";
                    $uploadOk = 0;
                }

                // Check file size
                if ($_FILES["obv"]["size"] > 500000) {
                    echo "Sorry, your file is too large.";
                    $uploadOk = 0;
                }

                // Allow certain file formats
                if (
                    $imageFileType != "jpg" && $imageFileType != "png" && $imageFileType != "jpeg"
                    && $imageFileType != "gif"
                ) {
                    echo "Sorry, only JPG and JPEG are allowed.";
                    $uploadOk = 0;
                }

                // Check if $uploadOk is set to 0 by an error
                if ($uploadOk == 0) {
                    echo "Sorry, your file was not uploaded.";
                    // if everything is ok, try to upload file
                } else {
                    if (move_uploaded_file($_FILES["obv"]["tmp_name"], $target_file)) {
                        if (addCoin($_POST['diameter'], $_POST['weight'], $_POST['axis'], $_POST['collection'], $_POST['coinUrl'], $_POST['collUrl'], $_FILES['obv']["name"], $_POST['identifier']))
                            echo "coin added";
                        else
                            echo "coin could not be added. Try a different one";
                    } else {
                        echo "Sorry, there was an error uploading your file.";
                    }
                }
            }
        }
        ?>

    </div>
</body>

</html>