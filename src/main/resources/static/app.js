/**
 * STOMP client instance for WebSocket communication.
 * Used to manage the WebSocket connection and subscriptions.
 * @type {Stomp.Client|null}
 */
let stompClient = null;

/**
 * Updates the UI to reflect the connection status.
 * Enables or disables the connect and disconnect buttons based on the connection state.
 * Toggles the visibility of the conversation section and clears greetings when connected.
 * @param {boolean} connected - Indicates whether the client is connected.
 */
function setConnected(connected) {
    document.getElementById('connect-btn').disabled = connected;
    document.getElementById('disconnect-btn').disabled = !connected;
    document.getElementById('conversation').style.display = connected ? '' : 'none';
    document.getElementById('greetings').innerHTML = '';
}

/**
 * Displays a greeting message in the greetings table.
 * Creates a new row in the table and inserts the message into it.
 * @param {string} message - The greeting message to display.
 */
function showGreeting(message) {
    const greetings = document.getElementById('greetings');
    const row = greetings.insertRow();
    const cell = row.insertCell(0);
    cell.textContent = message;
}

/**
 * Establishes a WebSocket connection to the server and subscribes to the greetings' topic.
 * Updates the UI upon successful connection and listens for incoming messages.
 * Handles connection errors by logging them and updating the UI.
 */
function connect() {
    const socket = new WebSocket('ws://localhost:8080/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    }, function (error) {
        console.error('WebSocket error:', error);
        setConnected(false);
    });
}

/**
 * Disconnects the STOMP client from the WebSocket server.
 * Updates the UI to reflect the disconnected state.
 * Logs the disconnection event.
 */
function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect(() => {
            setConnected(false);
            console.log('Disconnected');
        });
    }
}

/**
 * Sends the entered name to the server via the STOMP client.
 * The server is expected to respond with a greeting message.
 */
function sendName() {
    const name = document.getElementById('name').value;
    stompClient.send('/app/hello', {}, JSON.stringify({name}));
}

/**
 * Initializes the application when the DOM content is fully loaded.
 * Sets the initial UI state, prevents default form submission, and binds event listeners
 * to the connect, disconnect, and send buttons.
 */
window.addEventListener('DOMContentLoaded', () => {
    setConnected(false);

    document.querySelectorAll('form').forEach(form => {
        form.addEventListener('submit', e => e.preventDefault());
    });

    document.getElementById('connect-btn').addEventListener('click', connect);
    document.getElementById('disconnect-btn').addEventListener('click', disconnect);
    document.getElementById('send').addEventListener('click', sendName);
});