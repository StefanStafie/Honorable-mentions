resetBoard();

// Add event listeners to the cells, executes at load
var cells = document.getElementById("board").getElementsByTagName("td");
for (var i = 0; i < cells.length; i++) {
    cells[i].addEventListener("click", function () {
        var row = this.parentNode.rowIndex;
        var col = this.cellIndex;
        playMove(row, col);
    });
}

function CreateRoom() {
    clearFooterMessages();

    // Make an AJAX request to the server to create a room
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4) {
            if (this.status === 200) {
                var response = JSON.parse(this.responseText);
                roomId = response.roomId;
                document.getElementById("currentPlayer").innerHTML = "X";
                document.getElementById("roomIdText").value = roomId;
                document.getElementById("roomId").innerHTML = "Room " + roomId;
                buttonsDiv = document.getElementById("buttons").style.display = "none";;
                gameDiv = document.getElementById("game").style.display = "block";
                document.getElementById("winningPlayer").innerHTML = "";
                console.log(roomId);
            }
            else {
                document.getElementById("winningPlayer").innerHTML = "Error from server!! Status:" + this.status + " " + this.responseText;
            }
        }
    };

    xhttp.open("POST", "http://localhost:8080/api/Room", true);
    xhttp.send();

    addMessageToFooter("Sent: POST http://localhost:8080/api/Room");
}

function JoinRoom() {
    clearFooterMessages();

    // Make an AJAX request to the server to create a room
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4) {
            if (this.status === 200) {
                var response = JSON.parse(this.responseText);
                document.getElementById("winningPlayer").innerHTML = "";
                buttonsDiv = document.getElementById("buttons").style.display = "none";;
                gameDiv = document.getElementById("game").style.display = "block";
                roomId = document.getElementById("roomIdText").value;
                document.getElementById("roomId").innerHTML = "Room " + roomId;
                document.getElementById("currentPlayer").innerHTML = "O";
                console.log(roomId);
                waitOpponent();
            }
            else {
                document.getElementById("winningPlayer").innerHTML = "Error from server!! Status:" + this.status + " " + this.responseText;
            }
        }

    };

    xhttp.open("GET", "http://localhost:8080/api/Room?id=" + document.getElementById("roomIdText").value, true);
    xhttp.send();

    addMessageToFooter("Sent: GET http://localhost:8080/api/Room?id=" + document.getElementById("roomIdText").value);
}

// Function to update the board UI based on the current state
function updateBoard(board) {
    for (var row = 0; row < 3; row++) {
        for (var col = 0; col < 3; col++) {
            var cell = document.getElementById("board").rows[row].cells[col];
            cell.innerHTML = board[row * 3 + col];
            cell.classList.remove("playerX", "playerO");
            if (board[row * 3 + col] === "X") {
                cell.classList.add("playerX");
            } else if (board[row * 3 + col] === "O") {
                cell.classList.add("playerO");
            }
        }
    }
}

// Function to handle cell click event
function playMove(row, col) {
    // Make an AJAX request to the server to play the move
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4) {
            if (this.status === 200) {
                var response = JSON.parse(this.responseText);
                updateBoard(response.board);
                document.getElementById("winningPlayer").innerHTML = "";

                if (response.isEnd) {
                    showWinner(response.message)
                }
                else {
                    waitOpponent()
                }
            } else {
                document.getElementById("winningPlayer").innerHTML = "Error from server!! Status:" + this.status + " " + this.responseText;
            }
        };
    }

    player = document.getElementById("currentPlayer").innerHTML;
    roomId = document.getElementById("roomIdText").value;
    xhttp.open("POST", "http://localhost:8080/api/Room/" + roomId + "/move?player=" + player + "&row=" + row + "&col=" + col, true);
    xhttp.send();

    addMessageToFooter("Sent: POST http://localhost:8080/api/Room/" + roomId + "/move?player=" + player + "&row=" + row + "&col=" + col);
}

function showWinner(message) {
    buttonsDiv = document.getElementById("buttons");
    buttonsDiv.style.display = "block";

    gameDiv = document.getElementById("game");
    gameDiv.style.display = "none";

    document.getElementById("winningPlayer").innerHTML = message;
    resetBoard();
}

function waitOpponent() {
    for (var i = 0; i < cells.length; i++) {
        cells[i].classList.add("unclickable");
    }

    // Make an AJAX request to the server to play the move
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4) {
            if (this.status === 200) {
                var response = JSON.parse(this.responseText);
                updateBoard(response.board);
                for (var i = 0; i < cells.length; i++) {
                    cells[i].classList.remove("unclickable");
                }

                if (response.isEnd) {
                    showWinner(response.message)
                }
            }
            else {
                document.getElementById("winningPlayer").innerHTML = "Error from server!! Status:" + this.status + " " + this.responseText;
            }
        }
    };

    player = document.getElementById("currentPlayer").innerHTML;
    roomId = document.getElementById("roomIdText").value;
    xhttp.open("GET", "http://localhost:8080/api/Room/" + roomId + "/move?player=" + player, true);
    xhttp.send();

    addMessageToFooter("Sent: GET http://localhost:8080/api/Room/" + roomId + "/move?player=" + player);
}

function resetBoard() {
    var cells = document.getElementById("board").getElementsByTagName("td");

    for (var i = 0; i < cells.length; i++) {
        cells[i].innerHTML = '-';
    }
}

function addMessageToFooter(message) {
    var footer = document.getElementById("myFooter");
    var paragraph = document.createElement("p");
    paragraph.textContent = message;
    
    if (footer.firstChild) {
      footer.insertBefore(paragraph, footer.firstChild);
    } else {
      footer.appendChild(paragraph);
    }
  }
  
function clearFooterMessages() {
    var footer = document.getElementById("myFooter");
    while (footer && footer.firstChild) {
        footer.removeChild(footer.firstChild);
    }
}