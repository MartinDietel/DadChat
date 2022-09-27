let stompClient = null;
let selectedUsername = null;
let userName = $('#username').text();
let page = 0;

function connect() {
    let socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect(
        {},
        function () {
            stompClient.subscribe('/queue/messages/list', function (message) {
                addMessagesTop(JSON.parse(message.body));
                scrollMessages();
            });
            stompClient.subscribe('/queue/reply', function (message) {
                addMessageBottom(JSON.parse(message.body));
                scrollMessages();
            });

            stompClient.subscribe('/topic/users/list', function (message) {
                console.log(JSON.parse(message.body));
            });

            requestMessagesList();
        },
        function () {
            location.reload();
        }
    );
}

function requestMessagesList() {
    stompClient.send('/app/messages/askList', {}, selectedUsername);
}

function sendMessage() {
    if (selectedUsername == null) {
        alert('Please select a user.');
        return;
    }
    stompClient.send(
        '/app/message',
        {},
        JSON.stringify({
            sender: userName,
            recipient: selectedUsername,
            content: $('#message_text').val(),
        })
    );
    $('#message_text').val('');
}

function setSelectedUser(username) {
    selectedUsername = username;
    document.getElementById('messages_holder').innerHTML = '';
    connect(username);
}

function scrollMessages() {
    setTimeout(function () {
        let myDiv = document.getElementById('messages_holder');
        $('#message_text').focus();
        myDiv.scrollTop = myDiv.scrollHeight;
    }, 100);
}

function addMessagesTop(message) {
    $.each(message, function (value) {
        if (value.sender !== userName) {
            $('#messages_holder').prepend(createMessageLeft(value, ''));
        } else {
            $('#messages_holder').prepend(
                createMessageRight(value, 'class="pull-right"')
            );
        }
    });
}

function addMessageBottom(message) {
    if (message.sender !== userName) {
        $('#messages_holder').append(createMessageLeft(message, ''));
    } else {
        $('#messages_holder').append(
            createMessageRight(message, 'class="pull-right"')
        );
    }
}

function addMessageError(message) {
    $('#messages_holder').append(createMessageRight(message, 'class="error"'));
}

function createMessageRight(message, trClass) {
    return (
        "<tbody class='borderless' id='message_right' style='word-wrap: break-word; align-self: flex-end;' " +
        trClass +
        "><tr style='display: flex; flex-direction: column'><td style='font-size:11px;color:#999;border: unset;padding-top:4px;padding-bottom: 0;' class='borderless date'>" +
        message.date +
        "</td></tr><tr style='display: flex; flex-direction: column'><td class='username strong' style='text-align: center;border: unset;font-weight: bold;padding-top:0;padding-bottom: 0;'>" +
        message.sender +
        "</td></tr><tr style='display:inline-flex'><td style='border: unset'>" +
        linkify(message.content) +
        '</td></tr></tbody>'
    );
}

function createMessageLeft(message, trClass) {
    return (
        "<tbody class='borderless' id='message_left' style='word-wrap: break-word' " +
        trClass +
        "><tr style='display: flex; flex-direction: column'><td style='font-size:11px;color:#999;border: unset;padding-top:4px;padding-bottom: 0;' class='borderless date'>" +
        message.date +
        "</td></tr><tr style='display: flex; flex-direction: column'><td class='username strong' style='text-align: center;border: unset;font-weight: bold;padding-top:0;padding-bottom: 0;'>" +
        message.sender +
        "</td></tr><tr style='display:inline-flex'><td style='border: unset'>" +
        linkify(message.content) +
        '</td></tr></tbody>'
    );
}

function formatNumber(number) {
    if (number < 10) {
        return '0' + number;
    } else {
        return number;
    }
}

/* See https://github.com/jmrware/LinkifyURL */
function linkify(text) {
    // added
    let img_pattern =
        /(https?:\/\/[\w\-\.]+\.[a-zA-Z]{2,3}(?:\/\S*)?(?:[\w])+\.(?:jpg|png|gif|jpeg))/gi;
    let img_replace = '<a href="$1" target="blank"><img src="$1" /></a>';
    text = text.replace(img_pattern, img_replace);
    // /added

    let url_pattern =
        /(\()((?:ht|f)tps?:\/\/[a-z0-9\-._~!$&'()*+,;=:\/?#[\]@%]+)(\))|(\[)((?:ht|f)tps?:\/\/[a-z0-9\-._~!$&'()*+,;=:\/?#[\]@%]+)(\])|(\{)((?:ht|f)tps?:\/\/[a-z0-9\-._~!$&'()*+,;=:\/?#[\]@%]+)(\})|(<|&(?:lt|#60|#x3c);)((?:ht|f)tps?:\/\/[a-z0-9\-._~!$&'()*+,;=:\/?#[\]@%]+)(>|&(?:gt|#62|#x3e);)|((?:^|[^=\s'"\]])\s*['"]?|[^=\s]\s+)(\b(?:ht|f)tps?:\/\/[a-z0-9\-._~!$'()*+,;=:\/?#[\]@%]+(?:(?!&(?:gt|#0*62|#x0*3e);|&(?:amp|apos|quot|#0*3[49]|#x0*2[27]);[.!&',:?;]?(?:[^a-z0-9\-._~!$&'()*+,;=:\/?#[\]@%]|$))&[a-z0-9\-._~!$'()*+,;=:\/?#[\]@%]*)*[a-z0-9\-_~$()*+=\/#[\]@%])/gim;
    let url_replace =
        '$1$4$7$10$13<a href="$2$5$8$11$14" target="blank">$2$5$8$11$14</a>$3$6$9$12';
    return text.replace(url_pattern, url_replace);
}

$(function () {
    $('#messageDiv').on('submit', function (e) {
        e.preventDefault();
    });
    $('#message_text').keyup(function () {
        if (this.keyCode === 13) {
            sendMessage();
        }
    });
    $('#send_message_button').click(function () {
        sendMessage();
    });
    $('#loadMoreBtn').click(function () {
        requestMessagesList();
    });
});
