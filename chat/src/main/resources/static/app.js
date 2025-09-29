const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8082/gs-guide-websocket'
});

stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/public', (msg) => {
        console.log(msg);
        showMessage(msg.body);
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    console.log("Disconnected");
}

function sendName() {
    const name = $("#name").val() || 'Anonymous';
    const text = $("#message").val();
    if (!text) return;
    stompClient.publish({
        destination: "/app/chat",
        body: JSON.stringify({'sender': name, 'message': text})
    });
    $("#message").val("");
}

function showMessage(message) {
    const $list = $("#messages");
    $list.append(`<li>${message}</li>`);
    const box = document.getElementById('chat-box');
    if (box) box.scrollTop = box.scrollHeight;
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    // Auto-connect on page load
    connect();

    // Bind send button and Enter key
    $("#send").click(() => sendName());
    $("#message").on('keypress', (e) => {
        if (e.which === 13) { // Enter
            e.preventDefault();
            sendName();
        }
    });
});