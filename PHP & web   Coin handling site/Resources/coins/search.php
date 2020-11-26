<?php
require_once '../db/db_connection.php';
?>

  <!DOCTYPE HTML> <html lang='en'>

<head><meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="../style/style3.css">
    <link rel="stylesheet" href="../style/singleCoin.css">
    <link rel="stylesheet" href="../style/SearchStyle.css">
    <title>Search</title>
</head>

<body>

    <?php include '../navbar.php'; ?>
    <div id="red-canvas-orizontal">
        <section id='search-bar'>
            <p>You can search for coins here</p>
            <form action="search.php" method="get">
                <input type="text" name="identifier" value="<?php if (isset($_GET['identifier']) && $_GET['identifier'] != "")
                                                                echo $_GET['identifier']; ?>" placeholder="by identifier" maxlength="50">
                <input type="number" name="diameter" value="<?php if (isset($_GET['diameter']) && $_GET['diameter'] != "")
                                                                echo $_GET['diameter']; ?>" placeholder="by diameter" min="0" max="8000">
                <input type="number" name="weight" value="<?php if (isset($_GET['weight']) && $_GET['weight'] != "")
                                                                echo $_GET['weight']; ?>" placeholder="by weight" min="0" max="1012">
                <input type="number" name="axis" value="<?php if (isset($_GET['axis']) && $_GET['axis'] != "")
                                                            echo $_GET['axis']; ?>" placeholder="by axis" min="0" max="360">
                <br>
                <label>results/page:</label>
                <input type="number" name="number" placeholder="no of results" min="1" max="200" value="<?php if (isset($_GET['number']) && $_GET['number'] != "")
                                                                                                            echo $_GET['number']; else echo 20;?>">
                <label>page:</label>
                <input type="number" name="page" placeholder="page" min="1" max="200" value="<?php if (isset($_GET['page']) && $_GET['page'] != "")
                                                                                                    echo $_GET['page']; else echo 1;?>">
                <input type="submit" value="Search" />
            </form>
        </section>
        <a id="compareLink" href="compare.php">Go to compare</a>

        <br><br>

        <?php
        require_once 'CoinFunctions.php';

        if (!isset($_GET['number']) || $_GET['number'] == "")
            $_GET['number'] = 20;
        if (!isset($_GET['page']) || $_GET['page'] == "")
            $_GET['page'] = 1;

        $filter = "WHERE 1=1";
        if (isset($_GET['identifier']) && $_GET['identifier'] != "")
            $filter .= " AND identifier like '%" . $_GET['identifier'] . "%'";

        if (isset($_GET['diameter']) && $_GET['diameter'] != "")
            $filter .= " AND diameter >= " . $_GET['diameter'] . " AND diameter <= " . ++$_GET['diameter'];

        if (isset($_GET['weight']) && $_GET['weight'] != "")
            $filter .= " AND weight >= " . $_GET['weight'] . " AND weight <= " . ++$_GET['weight'];

        if (isset($_GET['axis']) && $_GET['axis'] != "")
            $filter .= " AND axis >= " . $_GET['axis'] . " AND axis <= " . ++$_GET['axis'];

        if ($filter == " ")
            $filter = "";

        $coins = getFirstCoins($filter, $_GET['number'], $_GET['page']);
        if (!$coins) {
            echo "0 results";
        } else {
            echo "Showing items " . ($_GET['number'] * ($_GET['page'] - 1)) . " - " . ($_GET['number'] * ($_GET['page'] - 1) + (int)(count($coins))) . " of " . getFirstCoinsTotalCount($filter)[0][0];
            for ($i = 0; $i < count($coins); $i++) {
                printCoin($coins[$i][4], $coins[$i][2], $coins[$i][1], $coins[$i][3], $coins[$i][6], $coins[$i][0], $coins[$i][5], $coins[$i][11], $coins[$i][14]);
            }
        }


        ?>

    </div>
</body>

</html>