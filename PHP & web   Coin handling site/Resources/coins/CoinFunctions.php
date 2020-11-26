<?php
require_once '../db/db_connection.php';
?>

<script>
    function addToInventory(button) {
        let xhr = new XMLHttpRequest();

        let url = "<?php echo URL . "Resources/db/addToInventory.php?coinId=" ?>" + button.classList[0];
        //alert(url);
        xhr.open('GET', url, true);
        xhr.send();

        button.style.backgroundColor = "green";
        button.innerHTML = "Item was added";
        setTimeout(() => {
            button.innerHTML = "Add to inventory";            
            button.style.backgroundColor = "white";
        }, 2000);

        
    }

    function removeFromInventory(button) {
        let xhr = new XMLHttpRequest();

        let url = "<?php echo URL . "Resources/db/removeFromInventory.php?coinId=" ?>" + button.classList[0];
        //alert(url);
        xhr.open('GET', url, true);
        xhr.send();

        button.style.backgroundColor = "green";
        button.innerHTML = "Item was removed";
        setTimeout(() => {
            button.style.display = "none";
        }, 2000);

        
    }

    function addToCompare(button) {
        let xhr = new XMLHttpRequest();
        let url = "<?php echo URL . "Resources/db/addToCompare.php?coinId=" ?>" + button.classList[0];
        //alert(url);
        xhr.open('GET', url, true);
        xhr.send();

        button.style.backgroundColor = "green";
        button.innerHTML = "Item was added";
        setTimeout(() => {
            button.innerHTML = "Add to compare";
            button.style.backgroundColor = "white";
        }, 2000);
        
    }

    const copyToClipboard = button => {
        const el = document.createElement('textarea');
        el.value = button.id;
        document.body.appendChild(el);
        el.select();
        document.execCommand('copy');
        document.body.removeChild(el);

        button.style.backgroundColor = "green";
        button.innerHTML = "Copied to clipboard";
        setTimeout(() => {
            button.innerHTML = "Share";
            button.style.backgroundColor = "white";
        }, 2000);
    };
</script>



<?php
function printCoin($identifier, $diameter, $weight, $axis, $collection, $coinUrl, $collUrl, $obverse, $reverse)
{
    if (substr($obverse, 0, 4) !== "http") {
        $obverse = URL . 'Resources/coins/uploads/' . $obverse;
    }
    echo '<div class="coin">
            <div>
                <p>Identifier: ' . $identifier . '</p>
                <p>Diameter: ' . $diameter . '</p>
                <p>Weight: ' . $weight . '</p>
                <p>Axis: ' . $axis . '</p>
                <p>Collection: ' . $collection . '</p>
            </div>

            <div>
                <p> More about this coin <a href="' . $coinUrl . '">' . $coinUrl . '</a></p>
                <p> More about the collection <a href="' . $collUrl . '">' . $collUrl . '</a></p>
                <button class="' . $identifier . ' inventoryAdd" onClick="addToInventory(this)">Add to inventory</button>
                <button class="' . $identifier . ' compareAdd" onClick="addToCompare(this)">Add to compare</button>
                <button class="' . $identifier . ' share" id = "'.$coinUrl.'" onClick="copyToClipboard(this)">Share</button>
            </div>

            <div>
                <img src="' . $obverse . '" alt= "image unavailable" class = "coinImage">
                <img src="' . $reverse . '" alt= "image unavailable" class = "coinImage">
            </div>
        </div>';
}
function printCoinInventory($identifier, $diameter, $weight, $axis, $collection, $coinUrl, $collUrl, $obverse, $reverse)
{
    if (substr($obverse, 0, 4) !== "http") {
        $obverse = URL . 'Resources/coins/uploads/' . $obverse;
    }
    echo '<div class="coin">
            <div>
                <p>Identifier: ' . $identifier . '</p>
                <p>Diameter: ' . $diameter . '</p>
                <p>Weight: ' . $weight . '</p>
                <p>Axis: ' . $axis . '</p>
                <p>Collection: ' . $collection . '</p>
            </div>

            <div>
                <p> More about this coin <a href="' . $coinUrl . '">' . $coinUrl . '</a></p>
                <p> More about the collection <a href="' . $collUrl . '">' . $collUrl . '</a></p>
                <button class="' . $identifier . ' compareAdd" onClick="removeFromInventory(this)">Remove from inventory</button>
                <button class="' . $identifier . ' compareAdd" onClick="addToCompare(this)">Add to compare</button>
                <button class="' . $identifier . ' share" id = "'.$coinUrl.'" onClick="copyToClipboard(this)">Share</button>
            </div>

            <div>
                <img src="' . $obverse . '" alt= "image unavailable" class = "coinImage">
                <img src="' . $reverse . '" alt= "image unavailable" class = "coinImage">
            </div>
        </div>';
}
function addCoinToInventory($id)
{

    echo "added to inventory";
}

?>
