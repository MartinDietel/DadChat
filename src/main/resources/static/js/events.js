$(document).ready(function (e) {

    receiveEvents();

    $(".right-corder-container-button").hover(function() {
        $(".long-text").addClass("show-long-text");
    }, function () {
        $(".long-text").removeClass("show-long-text");
    });


    $("#filterAgeDropdown").change(function(){
        searchEvents();
    });
    // $("#submitEventButton").click(function () {
    //     let title = $('#addEventTitle').val();
    //     let eventTime = $('#addEventTime').val();
    //     let eventAddress = $('#addEventAddress').val();
    //     let eventZipcode = $('#addEventZipcode').val();
    //     let category = $('#addEventCategory').val();
    //     let description = $('#addEventDescription').val();
    //     let ageDropdown = $('#ageDropdown').val();
    //     let event = {username: makeRandomUsername(), title: title, eventTime: eventTime, address: eventAddress,zipcode: eventZipcode, category: category, description: description,childrenAgeRange: ageDropdown};
    //
    //     $.ajax({
    //         type: "POST",
    //         url: "/addEvent",
    //         data: event,
    //         success: function (response) {
    //             $('#addEventModal').modal('toggle');
    //             console.log(response);
    //             location.reload();//will be removed when fully finished
    //
    //
    //         },
    //         error: function (XMLHttpRequest, textStatus, errorThrown) {
    //
    //
    //         }
    //
    //
    //     });
    //
    //
    // });

});


function searchEvents(ageFilter){
    let searchVal = $("#searchInputField").val();
    let ageRange = $("#filterAgeDropdown").val();

    $.ajax({
        type: "GET",
        url: "/searchEvents",
        data: {searchString: searchVal,
        ageFilter: ageRange},
        success: function (eventList) {

            $("#eventList").empty();
            createEventCards(eventList);

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert('failure');

        }


    });
}

function receiveEvents() {

    $.ajax({
        type: "GET",
        url: "/receiveUpcomingEvents",
        data: null,
        success: function (eventList) {

            createEventCards(eventList);

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert('failure');

        }


    });
}

function createEventCards(eventList) {
    if (eventList.length <= 0) {
        $("#eventCategory0").text(":(");
        $("#eventTitle0").text("No upcoming events");
        $("#eventDescription0").text("No one has any events");
        $("#eventDate0").text("");
        $("#eventTime0").text("");
        $("#eventAddress0").text("");
        $("#eventChildrenAgeRange0").text("");
        $("#eventTimestampAndUsername0").text("");

        var $eventCardClone = $('#eventCard0').clone();
        changeIdNumberOnElementAndChildren($eventCardClone, 1);
        $eventCardClone.appendTo("#eventList").show();
    } else {

        eventList = eventList.sort((a, b) => b.eventId - a.eventId)
        $.each(eventList, function (index) {

            $("#eventCategory0").text(eventList[index].category);
            $("#eventTitle0").text(eventList[index].title);
            $("#eventAddress0").text(eventList[index].address + ", " + eventList[index].zipcode);
            $("#eventMapLink0").attr("href",'https://www.google.com/maps/search/' + eventList[index].address + ", " + eventList[index].zipcode);
            $("#eventDescription0").text(eventList[index].description);


            var eventDateTime = new Date(eventList[index].eventDate);

            eventDateTime.setDate(eventDateTime.getDate() + 1);//for some reason goes back a day when making date
            let eventDaysAway =  ((eventDateTime - Date.now())/3600000)/24;
            var tomorrow = new Date(Date.now()+1);
            tomorrow.setDate(tomorrow.getDate() + 1);
            if(eventDateTime.toDateString()  === new Date(Date.now()).toDateString()){
                $("#eventTime0").text("Today at " + eventList[index].eventTime);
                $("#eventTime0").css("color", "Chartreuse");
            }else if(eventDateTime.toDateString()  === tomorrow.toDateString()){
                $("#eventTime0").text("Tomorrow at " + eventList[index].eventTime);
                $("#eventTime0").css("color", "Green");
            }else if(eventDaysAway < 0){
                return true;
            }
            else{
                $("#eventTime0").text("Happening in " + eventDaysAway.toFixed(0) + " days on " + formatDate(eventList[index].eventDate) + " at " + eventList[index].eventTime);
                $("#eventTime0").css("color", "white");
            }

            $("#eventChildrenAgeRange0").text(eventList[index].childrenAgeRange);
            let timestamp = new Date(eventList[index].createdOn);
            let hours =  (Date.now() - timestamp)/3600000;
            let minutes = hours * 60;
            let days =  hours/24;
            if(days >= 30){
                $("#eventTimestampAndUsername0").text((days / 30).toFixed(0) + " months ago by " +  eventList[index].username);
            }else if(hours >= 24){
                $("#eventTimestampAndUsername0").text((hours / 24).toFixed(0) + " days ago by " +  eventList[index].username);
            }else if(minutes >= 60){
                $("#eventTimestampAndUsername0").text(hours.toFixed(0) + " hours ago by " +  eventList[index].username);
            }else{
                $("#eventTimestampAndUsername0").text(minutes.toFixed(0) + " minutes ago by " +  eventList[index].username);
            }

            var $eventCardClone = $('#eventCard0').clone();
            changeIdNumberOnElementAndChildren($eventCardClone,  index + 1);
            $eventCardClone.appendTo("#eventList").show();
        });
    }
}

function changeIdNumberOnElementAndChildren(element, newIdNumber) {
    var $element = $(element),
        $elementChildren = $element.children(),
        oldId = $element.attr("id"),
        newId = (oldId) ? oldId.replace(/\d+$/, newIdNumber) : null;

    if (newId) {
        $element.attr("id", newId);
    }

    if ($elementChildren.length > 0) { // recursively call function on children
        $elementChildren.each(function(i, child) {
            changeIdNumberOnElementAndChildren(child, newIdNumber);
        });
    }
}

function formatDate(timestamp) {
    let t = timestamp.split(/[- :]/);
    let months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
    let monthName = months[t[1] - 1];
    let formattedDate = monthName + " " + t[2] + ", " + t[0];
    return formattedDate;
}


