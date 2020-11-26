<?php require_once 'db_connection.php';
?>
  <!DOCTYPE HTML> <html lang='en'>
<head><meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="../Style/style3.css">
    <link rel="stylesheet" href="../Style/LoginRegister.css">
    <title>Profile->Stats</title>
</head>

<body>
    <?php include '../navbar.php'; ?>
    <section id="red-canvas">
        <div class="left">
            <h1>Select your statistics?</h1>
            <form action="export.php" method="get">
                <label>Format: </label>
                <select id="do" name="format">
                    <option value="CSV">CSV</option>
                    <option value="PDF">PDF</option>
                </select><br>
                <label>What kind of statistics?</label>
                <select id="do" name="do">
                    <option value="AveragesUser"> Average weight and diameter of your coins</option>
                    <option value="AverageDB">Average weight and diameter of every coin</option>
                    <option value="MostPopularCoin">The most popular coin</option>
                    <option value="PopularCoins">Popular coins</option>
                    <option value="YourCoins">Your coins</option>
                </select><br>
                <input type="submit" value="Get stats" />
            </form>
        </div>
    </section>
</body>

</html>