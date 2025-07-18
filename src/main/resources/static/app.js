// Initialize STOMP client over WebSocket
let stompClient = null;

function setConnected(connected) {
    document.getElementById('connect-btn').disabled = connected;
    document.getElementById('disconnect-btn').disabled = !connected;
    document.getElementById('conversation').style.display = connected ? '' : 'none';
    document.getElementById('greetings').innerHTML = '';
}

function showGreeting(message) {
    const greetings = document.getElementById('greetings');
    const row = greetings.insertRow();
    const cell = row.insertCell(0);
    cell.textContent = message;
}

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

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect(() => {
            setConnected(false);
            console.log('Disconnected');
        });
    }
}

function sendName() {
    const name = document.getElementById('name').value;
    stompClient.send('/app/hello', {}, JSON.stringify({name}));
}

window.addEventListener('DOMContentLoaded', () => {
    setConnected(false);

    document.querySelectorAll('form').forEach(form => {
        form.addEventListener('submit', e => e.preventDefault());
    });

    document.getElementById('connect-btn').addEventListener('click', connect);
    document.getElementById('disconnect-btn').addEventListener('click', disconnect);
    document.getElementById('send').addEventListener('click', sendName);
});