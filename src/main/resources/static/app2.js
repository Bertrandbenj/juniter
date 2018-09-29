var stompClient2 = null;

function setConnected2(connected) {
    $("#connect2").prop("disabled", connected);
    $("#disconnect2").prop("disabled", !connected);
    if (connected) {
        $("#conversation2").show();
    }
    else {
        $("#conversation2").hide();
    }
    $("#greetings2").html("");
}

function connect2() {
    var socket = new SockJS('/endpoint');
    stompClient2 = Stomp.over(socket);
    stompClient2.connect({}, function (frame) {
        setConnected2(true);
        console.log('Connected: ' + frame);
        stompClient2.subscribe('g1.duniter.org:20903', function (greeting) {
            showGreeting2(JSON.parse(greeting.body).content);
        });
    });
}

function disconnect2() {
    if (stompClient2 !== null) {
        stompClient2.disconnect();
    }
    setConnected2(false);
    console.log("Disconnected");
}

function sendName2() {
    stompClient2.send("/juniter/hello", {}, JSON.stringify({'name': $("#name2").val()}));
}

function showGreeting2(message) {
    $("#greetings2").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("#form2").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect2" ).click(function() { connect2(); });
    $( "#disconnect2" ).click(function() { disconnect2(); });
    $( "#sendZ" ).click(function() { sendName2(); });
});