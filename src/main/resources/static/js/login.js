// function _(element){
//
//     return document.getElementById(element);
// }

// let login_button = _("login_button");
// login_button.addEventListener("click",collect_data);
//
// function collect_data(e){
//
//     e.preventDefault();
//     login_button.disabled = true;
//     login_button.value = "Loading...Please wait..";
//
//     let myform = _("myform");
//     let inputs = myform.getElementsByTagName("INPUT");
//
//     let data = {};
//     for (let i = inputs.length - 1; i >= 0; i--) {
//
//         let key = inputs[i].name;
//
//         switch(key){
//
//             case "email":
//                 data.email = inputs[i].value;
//                 break;
//
//             case "password":
//                 data.password = inputs[i].value;
//                 break;
//
//         }
//     }
//
//     send_data(data,"login");
//
// }
//
// function send_data(data,type){
//
//     let xml = new XMLHttpRequest();
//
//     xml.onload = function(){
//
//         if(xml.readyState === 4 || xml.status === 200){
//             handle_result(xml.responseText);
//             login_button.disabled = false;
//             login_button.value = "Login";
//         }
//     }
//
//     data.data_type = type;
//     let data_string = JSON.stringify(data);
//
//     xml.open("POST","./api.php",true);
//     xml.send(data_string);
// }

// function handle_result(result){
//
//
//     let data = JSON.parse(result);
//     console.log(data);
//     if(data.data_type === "info"){
//         window.location = "./index.html";
//     }
    // else{
    //     let error = _("error");
    //     error.innerHTML = data.message;
    //     error.style.display = "block";
    //
    // }
// }