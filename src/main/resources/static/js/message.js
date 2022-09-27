let sent_audio = new Audio("./sounds/message_sent.mp3");
let received_audio = new Audio("./sounds/message_received.mp3");

// function _(element) {
//
//     return document.getElementById(element);
// }
//
// let label_contacts = _("label_contacts");
// label_contacts.addEventListener("click", get_contacts);
//
// let label_chat = _("label_chat");
// label_chat.addEventListener("click", get_chats);
//
// let label_settings = _("label_settings");
// label_settings.addEventListener("click", get_settings);


// document.querySelector('#welcomeForm').addEventListener('submit', connect, true)
// document.querySelector('#send_message_button').addEventListener('submit', sendMessage, true)

// var stompClient = null;
//
// function connect() {
//         const Stomp = require("stompjs");
//         var SockJS = require("sockjs-client");
//         SockJS = new SockJS('http://localhost:8082/ws');
//         stompClient = Stomp.over(SockJS);
//         stompClient.connect({}, onConnected, onError);
// }
//
//
// function onConnected() {
//     stompClient.subscribe('/user/' + currentUser.id + "/queue/messages", onMessageReceived);
// }
//
// function sendMessage(msg) {
//
//     // if (msg.trim() !== "") {
//         const chatMessage = {
//             senderId : currentUser.id,
//             receiverId : activeContact.id,
//             senderName : currentUser.name,
//             receiverName : activeContact.name,
//             content : msg,
//             timestamp: new Date(),
//         };
//
//         stompClient.send("/app/chat", {}, JSON
//             .stringify(chatMessage));
//     // }
// }
//
//
//
// function onMessageReceived(payload) {
//     var message = JSON.parse(payload.body);
//
//     var messageElement = document.createElement('li');
//
//     //Change these to be more like the data types in the php app
//     if (message.type === 'newUser') {
//         messageElement.classList.add('event-data');
//         message.content = message.sender + 'has joined the chat';
//     } else if (message.type === 'Leave') {
//         messageElement.classList.add('event-data');
//         message.content = message.sender + 'has left the chat';
//     } else {
//         messageElement.classList.add('message-data');
//
//         var element = document.createElement('i');
//         var text = document.createTextNode(message.sender[0]);
//         element.appendChild(text);
//
//         messageElement.appendChild(element);
//
//         var usernameElement = document.createElement('span');
//         var usernameText = document.createTextNode(message.sender);
//         usernameElement.appendChild(usernameText);
//         messageElement.appendChild(usernameElement);
//     }
//
//     var textElement = document.createElement('p');
//     var messageText = document.createTextNode(message.content);
//     textElement.appendChild(messageText);
//
//     messageElement.appendChild(textElement);
//
//     document.querySelector('#messages_holder').appendChild(messageElement);
//     document.querySelector('#messages_holder').scrollTop = document
//         .querySelector('#messages_holder').scrollHeight;
//
// }

const url = 'http://localhost:8082';
let stompClient;
let selectedUser = document.getElementById("#username").innerText;
let newMessages = new Map();

function connectToChat() {
    console.log("connecting to chat...")
    let socket = new SockJS(url + '/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to: " + frame);
        stompClient.subscribe("/topic/messages/" + selectedUser, function (response) {
            let data = JSON.parse(response.body);
            if (selectedUser) {
                render(data.message, selectedUser);
            } else {
                newMessages.set(selectedUser, data.message);
                $('#userNameAppender_' + selectedUser).append('<span id="newMessage_' + selectedUser + '" style="color: red">+1</span>');
            }
        });
    });
}

function sendMsg(from, text) {
    stompClient.send("/app/chat/" + selectedUser, {}, JSON.stringify({
        selectedUser: from,
        message: text
    }));
}

// function registration() {
//     let userName = document.getElementById("userName").value;
//     $.get(url + "/registration/" + userName, function (response) {
//         connectToChat(userName);
//     }).fail(function (error) {
//         if (error.status === 400) {
//             alert("Login is already busy!")
//         }
//     })
// }

// function selectUser(userName) {
//     console.log("selecting users: " + userName);
//     selectedUser = userName;
//     let isNew = document.getElementById("newMessage_" + userName) !== null;
//     if (isNew) {
//         let element = document.getElementById("newMessage_" + userName);
//         element.parentNode.removeChild(element);
//         render(newMessages.get(userName), userName);
//     }
//     $('#selectedUserId').html('');
//     $('#selectedUserId').append('Chat with ' + userName);
// }

function fetchAll() {
    $.get(url + "/fetchAllUsers", function (response) {
        let users = response;
        let usersTemplateHTML = "";
        for (let i = 0; i < users.length; i++) {
            usersTemplateHTML = usersTemplateHTML + '<a href="#" onclick="selectUser(\'' + users[i] + '\')"><li class="clearfix">\n' +
                '                <img src="https://rtfm.co.ua/wp-content/plugins/all-in-one-seo-pack/images/default-user-image.png" width="55px" height="55px" alt="avatar" />\n' +
                '                <div class="about">\n' +
                '                    <div id="userNameAppender_' + users[i] + '" class="name">' + users[i] + '</div>\n' +
                '                    <div class="status">\n' +
                '                        <i class="fa fa-circle offline"></i>\n' +
                '                    </div>\n' +
                '                </div>\n' +
                '            </li></a>';
        }
        $('#usersList').html(usersTemplateHTML);
    });
}


// function get_data(find,type){
//
//     let xml = new XMLHttpRequest();
//     xml.onload = function(){
//
//         if(xml.readyState === 4 || xml.status === 200){
//
//             handle_result(xml.responseText, type);
//         }
//
//     }
//
//     let data = {};
//     data.find = find;
//     data.data_type = type;
//     data = JSON.stringify(data);
//
//     xml.open("POST", "php/api.php", true);
//     xml.send(data);
//
// }

// function handle_result(result,type) {
//     alert(result)
//     if (result.trim() !== "") {
//
//         let inner_right_panel = _("inner_right_panel");
//         inner_right_panel.style.overflow = "visible";
//
//         let obj = JSON.parse(result);
//         if (typeof(obj.logged_in) !== "undefined" && !obj.logged_in) {
//             window.location = "login.html";
//         } else {
//
//             switch(obj.data_type) {
//
//                 case "user_info":
//
//                     let username = _("username");
//                     let email = _("email");
//                     let profile_image = _("profile_image");
//
//                     username.innerHTML =  obj.username;
//                     email.innerHTML =  obj.email;
//                     profile_image.src =  obj.image;
//                     break;
//
//                 case "contacts":
//
//                     let inner_left_panel_contacts = _("inner_left_panel");
//
//                     inner_right_panel.style.overflow = "hidden";
//                     inner_left_panel_contacts.innerHTML =  obj.message;
//                     break;
//
//                 case "chats_refresh":
//
//                     SEEN_STATUS = false;
//                     let messages_holder_refresh = _("messages_holder");
//                     messages_holder_refresh.innerHTML =  obj.messages;
//                     if(typeof obj.new_message != 'undefined'){
//                         if(obj.new_message){
//                             received_audio.play();
//
//                             setTimeout(function(){
//                                 messages_holder_refresh.scrollTo(0,messages_holder_refresh.scrollHeight);
//                                 let message_text_refresh = _("message_text");
//                                 message_text_refresh.focus();
//                             },100);
//
//                         }
//                     }
//
//                     break;
//
//                 case "send_message":
//
//                     sent_audio.play();
//
//                 case "chats":
//
//                     SEEN_STATUS = false;
//                     let inner_left_panel_chats = _("inner_left_panel");
//
//                     inner_left_panel_chats.innerHTML = obj.user;
//                     inner_right_panel.innerHTML =  obj.messages;
//
//                     let messages_holder = _("messages_holder");
//
//                     setTimeout(function() {
//                         messages_holder.scrollTo(0,messages_holder.scrollHeight);
//                         let message_text = _("message_text");
//                         message_text.focus();
//                     },100);
//
//                     if(typeof obj.new_message != 'undefined'){
//                         if(obj.new_message){
//                             received_audio.play();
//                         }
//                     }
//
//                     break;
//
//                 case "settings":
//
//                     let inner_left_panel_settings = _("inner_left_panel");
//
//                     inner_left_panel_settings.innerHTML =  obj.message;
//                     break;
//
//                 case "send_image":
//
//                     alert(obj.message);
//                     break;
//
//                 case "save_settings":
//
//                     alert(obj.message);
//                     // get_data({},"user_info");
//                     // get_settings(true);
//                     break;
//
//             }
//         }
//     }
// }


// get_data({},"user_info");
// get_data({},"contacts");

// let radio_contacts = _("radio_contacts");
// radio_contacts.checked = true;

// function get_contacts()
// {
//     get_data({},"contacts");
// }
//
// function get_chats(e)
// {
//     get_data({},"chats");
// }
//
// function get_settings(e)
// {
//     get_data({},"settings");
// }

// function send_message(e)
// {
//
//     let message_text = _("message_text");
//     if(message_text.value.trim() === ""){
//
//         alert("please type something to send");
//
//     }

    // get_data({
    //
    //     message:message_text.value.trim(),
    //     userId :CURRENT_CHAT_USER
    //
    // },"send_message");


function enter_pressed(e)
{
    if(e.keyCode === 13)
    {
        sendMessage(event);
    }

    SEEN_STATUS = true;
}

// setInterval(function(){
//
//     let radio_chat = _("radio_chat");
//     let radio_contacts = _("radio_contacts");
//
//     if(CURRENT_CHAT_USER !== "" && radio_chat.checked)
//     {
//         get_data({
//             userId:CURRENT_CHAT_USER,
//             seen:SEEN_STATUS
//         },"chats_refresh");
//     }
//
//
//     if(radio_contacts.checked)
//     {
//         get_data({},"contacts");
//     }
//
//
// },5000);
//
// function set_seen(e){
//
//     SEEN_STATUS = true;
// }
//
// function delete_message(e)
// {
//
//     if(confirm("Are you sure you want to delete this message??")){
//
//         let msgId = e.target.getAttribute("msgId");
//         get_data({
//             rowId:msgId
//         },"delete_message");
//
//         get_data({
//             userId:CURRENT_CHAT_USER,
//             seen:SEEN_STATUS
//         },"chats_refresh");
//     }
// }
//
// function delete_thread(e)
// {
//
//     if(confirm("Are you sure you want to delete this whole thread??")){
//
//         get_data({
//             userId:CURRENT_CHAT_USER
//         },"delete_thread");
//
//         get_data({
//             userId:CURRENT_CHAT_USER,
//             seen:SEEN_STATUS
//         },"chats_refresh");
//     }
// }

// function collect_data(){
//
//     let save_settings_button = _("save_settings_button");
//     save_settings_button.disabled = true;
//     save_settings_button.value = "Loading...Please wait..";
//
//     let myform = _("myform");
//     let inputs = myform.getElementsByTagName("INPUT");
//
//     let data = {};
//     for (let i = inputs.length - 1; i >= 0; i--) {
//
//     let key = inputs[i].name;
//
//     switch(key){
//
//         case "username":
//         data.username = inputs[i].value;
//         break;
//
//         case "email":
//         data.email = inputs[i].value;
//         break;
//
//         case "password":
//         data.password = inputs[i].value;
//         break;
//
//         case "password2":
//         data.password2 = inputs[i].value;
//         break;
//         }
//     }
//
//     send_data(data,"save_settings");

// }

// function send_data(data,type){
//
//     let xml = new XMLHttpRequest();
//
//     xml.onload = function(){
//
//     if(xml.readyState === 4 || xml.status === 200){
//
//         handle_result(xml.responseText);
//         let save_settings_button = _("save_settings_button");
//         save_settings_button.disabled = false;
//         save_settings_button.value = "Save Settings";
//         }
//     }
//
//     data.data_type = type;
//     let data_string = JSON.stringify(data);
//
//     xml.open("POST","api.php",true);
//     xml.send(data_string);
// }


// function upload_profile_image(files){
//
//     let filename = files[0].name;
//     let ext_start = filename.lastIndexOf(".");
//     let ext = filename.substr(ext_start + 1,3);
//     if(!(ext === "jpg" || ext === "JPG")){
//
//     alert("This file type is not allowed");
//     return;
//     }
//
//     let change_image_button = _("change_image_button");
//     change_image_button.disabled = true;
//     change_image_button.innerHTML = "Uploading Image...";
//
//     let myform = new FormData();
//
//     let xml = new XMLHttpRequest();
//
//     xml.onload = function(){
//
//     if(xml.readyState === 4 || xml.status === 200){
//
//         alert("Your profile picture was successfully changed!");
//         get_data({},"user_info");
//         get_settings(true);
//         change_image_button.disabled = false;
//         change_image_button.innerHTML = "Change Image";
//
//         }
//     }
//
//     myform.append('file',files[0]);
//     myform.append('data_type',"change_profile_image");
//
//     xml.open("POST","uploader.php",true);
//     xml.send(myform);
// }


// function handle_drag_and_drop(e){
//
//     if(e.type === "dragover"){
//
//     e.preventDefault();
//     e.target.className = "dragging";
//
//     }else if(e.type === "dragleave"){
//
//     e.target.className = "";
//
//     }else if(e.type === "drop"){
//
//     e.preventDefault();
//     e.target.className = "";
//
//     upload_profile_image(e.dataTransfer.files);
//     }else{
//
//     e.target.className = "";
//     }
// }


// function start_chat(e){
//
//     let userId = e.target.getAttribute("userId");
//
//     if(e.target.id === "") {
//     userId = e.target.parentNode.getAttribute("userId");
//     }
//
//     CURRENT_CHAT_USER = userId;
//
//     let radio_chat = _("radio_chat");
//     radio_chat.checked = true;
//     get_data({userId:CURRENT_CHAT_USER}, "chats");
// }

// function send_image(files) {
//
//     let myform = new FormData();
//     let xml = new XMLHttpRequest();
//
//     xml.onload = function(){
//
//     if(xml.readyState === 4 || xml.status === 200){
//
//     handle_result(xml.responseText, "send_image");
//     get_data({
//     userId:CURRENT_CHAT_USER,
//     seen:SEEN_STATUS
//     },"chats_refresh");
//
//         }
//     }
//
//     myform.append('file',files[0]);
//     myform.append('data_type',"send_image");
//     myform.append('userId', CURRENT_CHAT_USER);
//
//     xml.open("POST","uploader.php",true);
//     xml.send(myform);
    // for (let i = 0; i < Things.length; i++) {
    //     Things[i]
    // }
// }

// function close_image(e){
//
//     e.target.className = "image_off";
// }

// function image_show(e){
//
//     let image = e.target.src;
//     let image_viewer = _("image_viewer");
//
//     image_viewer.innerHTML = "<img src='"+image+"' style='width:100%' />";
//     image_viewer.className = "image_on";
// }
