let userToken;
let userHeader;
let username;
$(document).ready(function() {

    userToken = $("meta[name='_csrfUser']").attr("content");
    userHeader = $("meta[name='_csrf_headerUser']").attr("content");

    $(document).on( 'click', '.usernameLinks', function () {
        username = $(this).data('username');
        getLoggedInUser();
    });

    $(document).on( 'click', '#profileBtn', function () {
        displayLoggedInUserProfile();
    });

    $(document).on( 'click', '#updateAccountBtn', function () {
        let loggedInUserName = $("#loggedInUserName").text();
        $.ajax({
            type: "GET",
            url: "/user",
            data: {username: loggedInUserName},
            success: function (userData) {

                window.location.href = "/edit/"+ userData.id;
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert('failure');

            }
        });

    });


    $(document).on( 'click', '#editProfileBtn', function () {
        $("#userDesc").hide()
        $("#userDescEdit").val($("#userDesc").text())
        $("#userDescEdit").show()
        $("#userSelfButtons").hide()
        $("#userEditButtons").show()
        $('#profileImageDiv').hide();
        $('#profileHoverA').show();
        $("#updateAccountBtn").show()
    });

    $(document).on( 'click', '#cancelBtn', function () {
        $("#userDescEdit").hide()
        $("#userDesc").show()
        $("#userEditButtons").hide()
        $("#userSelfButtons").show()
        $('#profileHoverA').hide();
        $('#profileImageDiv').show();
    });

    $(document).on( 'click', '#saveBtn', function () {
    //    alert("Desc: " +    $("#userDesc").text() + "DescEdit: " + $("#userDescEdit").val())
        saveProfileDesc();
    });

    $('#userModal').on('hidden.bs.modal', function () {
        emptyUserModal();
    });

    $(document).on( 'click', '#addFriendBtn', function () {
      setFriend(true);
    });
    $(document).on( 'click', '#removeFriendBtn', function () {
        setFriend(false);
    });


    $(document).on( 'click', '#friendsBtn', function () {
        constructFriendsList()

    });
});

function getLoggedInUser() {
    let loggedInUserName = $("#loggedInUserName").text();
    //alert(loggedInUserName);
    $.ajax({
        type: "GET",
        url: "/user",
        data: {username: loggedInUserName},
        success: function (userData) {
            setModalData(userData)
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert('failure');

        }
    });

}

function setModalData(loggedInUserData) {
    $.ajax({
        type: "GET",
        url: "/user",
        data: {username: username},
        success: function (userData) {


            if(userData != '' && userData.id != 'undefined'){
                $("#userFullName").text(userData.fullName)
                $("#userCreatedOn").text('Dad since ' + formatUserDate(userData.createdOn))
                $("#userDesc").show()
                $("#userDesc").text(userData.description)
                $('#profileImageDiv').show();
                $('#friendListWrapper').hide();
                //    alert(loggedInUserId + " to " + userData.id)
                if(loggedInUserData.id == userData.id){
                    $('#userOtherButtons').hide();
                    $('#userSelfButtons').show();
                    $("#updateAccountBtn").show()
                    $('#friendsBtn').show();
                }else if( jQuery.inArray(userData.id.toString(), loggedInUserData.friendIds) != -1){ //if friends
                    $('#userTag').show();
                    $('#addFriendBtn').hide();
                    $('#removeFriendBtn').show();
                    $('#userSelfButtons').hide();
                    $('#userOtherButtons').show();
                }
                else{
                    $('#userSelfButtons').hide();
                    $('#userOtherButtons').show();
                    $('#addFriendBtn').show();
                    $('#removeFriendBtn').hide();
                }




            }else{
                emptyUserModal();
                $("#userFullName").text("User does not exist")
            }


        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert('failure');

        }
    });

}


function displayLoggedInUserProfile() {
    let loggedInUserName = $("#loggedInUserName").text();
    $.ajax({
        type: "GET",
        url: "/user",
        data: {username: loggedInUserName},
        success: function (userData) {
            $("#userFullName").text(userData.fullName)
            $("#userCreatedOn").text('Dad since ' + formatUserDate(userData.createdOn))
            $("#userDesc").show()
            $("#userDesc").text(userData.description)
            $('#profileImageDiv').show();
            $('#userOtherButtons').hide();
            $('#userSelfButtons').show();
            $("#updateAccountBtn").show()
            $('#friendsBtn').show();
            $('#friendListWrapper').hide();
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert('failure');

        }
    });

}


function saveProfileDesc() {
    let loggedInUserName = $("#loggedInUserName").text();
    let description = $("#userDescEdit").val();
    $.ajax({
        type: "POST",
        headers: {
            [userHeader]: userToken,
        },
        url: "/saveUserDesc",
        data: {username: loggedInUserName, description: description},
        success: function () {
            $("#userDescEdit").hide()
            $("#userDesc").text($("#userDescEdit").val())
            $("#userDesc").show()
            $("#userEditButtons").hide()
            $("#userSelfButtons").show()
            $('#profileHoverA').hide();
            $('#profileImageDiv').show();
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("Error saving profile description");

        }
    });

}
function setFriend(adding){
    let loggedInUserName = $("#loggedInUserName").text();
    $.ajax({
        type: "POST",
        headers: {
            [userHeader]: userToken,
        },
        url: "/setFriend",
        data: {adderUsername: loggedInUserName, friendUsername: username, adding: adding},
        success: function () {
            if(adding){
                $('#userTag').show();
                $('#addFriendBtn').hide();
                $('#removeFriendBtn').show();
            }else{
                $('#userTag').hide();
                $('#addFriendBtn').show();
                $('#removeFriendBtn').hide();
            }


        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("Error saving profile description");

        }
    });
}

function formatUserDate(timestamp) {
    let t = timestamp.split(/[.]/);
    let months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
    let monthName = months[t[1] - 1];
    let formattedDate = monthName + " " + t[2] + ", " + t[0];
    return formattedDate;
}

function emptyUserModal() {
    $('#profileHoverA').hide();
    $('#profileImageDiv').hide();
    $("#userFullName").text("");
    $("#userCreatedOn").text('');
    $("#userDesc").text('');
    $('#userOtherButtons').hide();
    $('#userSelfButtons').hide();
    $('#userEditButtons').hide();
    $("#userDescEdit").hide()
    $("#updateAccountBtn").hide()
    $("#userDesc").hide()
    $("#userDescEdit").text('')
    $('#userTag').hide();
    $('#addFriendBtn').hide();
    $('#removeFriendBtn').hide();
    $('#friendsBtn').hide();
    $('#friendListWrapper').hide();
}

function constructFriendsList(){

    let loggedInUserName = $("#loggedInUserName").text();
    $('#friendList').empty();

    $.ajax({
        type: "GET",
        url: "/getFriends",
        data: {username: loggedInUserName},
        success: function (friendList) {

            if(friendList === null || friendList.length == 0) {
                alert("You have no friends");
            }else {
                emptyUserModal();
                $.each(friendList, function (index) {

                    $("#friendListItemName0").text(friendList[index].fullName);
                    $("#friendListUsername0").text(friendList[index].username);
                    $("#listFriendProfileBtn0").attr('data-username', friendList[index].username);

                    let $friendListItemClone = $('#friendListItem0').clone();
                    changeIdNumberOnElementAndChildrenOfUserProfile($friendListItemClone, index + 1);
                    $friendListItemClone.appendTo("#friendList").show();
                })
                $('#friendListWrapper').show();
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert('error when showing friends list');

        }
    });



}



function changeIdNumberOnElementAndChildrenOfUserProfile(element, newIdNumber) {
    let $element = $(element),
        $elementChildren = $element.children(),
        oldId = $element.attr("id"),
        newId = (oldId) ? oldId.replace(/\d+$/, newIdNumber) : null;

    if (newId) {
        $element.attr("id", newId);
    }

    if ($elementChildren.length > 0) { // recursively call function on children
        $elementChildren.each(function(i, child) {
            changeIdNumberOnElementAndChildrenOfUserProfile(child, newIdNumber);
        });
    }
}