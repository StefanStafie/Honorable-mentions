<?php require_once '../db/db_connection.php'; ?>
  <!DOCTYPE HTML> <html lang='en'>
<head><meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="../style/style3.css">
    <link rel="stylesheet" href="../style/singleCoin.css">
    <title>Compare</title>
</head>

<body>

    <?php include '../navbar.php'; ?>
    <div id="red-canvas-orizontal">
        <div>
            <form action="compare.php" method="get">
                <input type="text" name="identifier1" placeholder="identifier" maxlength="50">
                <input type="submit" value="Compare" />
                <input type="text" name="identifier2" placeholder="identifier" maxlength="50">
            </form>
        </div>
        <?php
        require_once 'CoinFunctions.php';
        $coin1 = null;
        $coin2 = null;
        if (isset($_GET)) {

            if (isset($_GET['identifier1']) && $_GET['identifier1'] != "") {
                $coin1 = findCoin($_GET['identifier1']);
            }

            if (isset($_GET['identifier2']) && $_GET['identifier2'] != "") {
                $coin2 = findCoin($_GET['identifier2']);
            }

            if (isset($_SESSION['compareIndex'])) {
                if (isset($_SESSION['compare1'])) {
                    if ($coin1 == null) {
                        $coin1 = findCoin($_SESSION['compare1']);
                    } else if ($coin2 == null) {
                        $coin2 = findCoin($_SESSION['compare1']);
                    }
                }
                if (isset($_SESSION['compare2'])) {
                    if ($coin1 == null) {
                        $coin1 = findCoin($_SESSION['compare2']);
                    } else if ($coin2 == null) {
                        $coin2 = findCoin($_SESSION['compare2']);
                    }
                }
            }
        }
        
        if (substr($coin1[0][11], 0, 4) !== "http") {
            $coin1[0][11] = URL . 'Resources/coins/uploads/' . $coin1[0][11];
        }
        
        if (substr($coin2[0][11], 0, 4) !== "http") {
            $coin2[0][11] = URL . 'Resources/coins/uploads/' . $coin2[0][11];
        }
        if ($coin1 && $coin2 ) {
            echo '<div class="cico">
                <div class="vertical-coin">
                <p>identifier: ' . $coin1[0][4] . '</p>
                <p>weight: ' . $coin1[0][1] . '</p>
                <p>diameter: ' . $coin1[0][2] . '</p>
                <p>axis: ' . $coin1[0][3] . '</p>
                <p><a href="' . $coin1[0][0] . '">Link here</a></p>
                <img src="' . $coin1[0][11] . '" alt="image unavailable" class="coinImage compare">
                <img src="' . $coin1[0][14] . '" alt="image unavailable" class="coinImage compare">
            </div>
            <div style = "height:100px; width:70px"></div>
            <div class="vertical-coin">
                <p>identifier: ' . $coin2[0][4] . '</p>
                <p>weight: ' . $coin2[0][1] . '</p>
                <p>diameter: ' . $coin2[0][2] . '</p>
                <p>axis: ' . $coin2[0][3] . '</p>
                <p><a href="' . $coin2[0][0] . '">Link here</a></p>
                <img src="' . $coin2[0][11] . '" alt="image unavailable" class="coinImage compare">
                <img src="' . $coin2[0][14] . '" alt="image unavailable" class="coinImage compare">
            </div>
        </div>';
        } else {
            echo "please input valid identifiers";
        }
        ?>
    </div>
</body>

</html>