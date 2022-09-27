let pos;
let map;
let bounds;
let infoWindow;
let currentInfoWindow;
let service;
let infoPane;
let token;
let header;

function initMap() {

    token = $("meta[name='_csrf']").attr("content");
    header = $("meta[name='_csrf_header']").attr("content");
    bounds = new google.maps.LatLngBounds();
    infoWindow = new google.maps.InfoWindow;
    currentInfoWindow = infoWindow;
    infoPane = document.getElementById('panel');


    var params = new URLSearchParams(window.location.search),
        zipcode = params.get("zipcode"),
        searchType = params.get("searchType");

    $('#searchInput').val(zipcode);
    $('#searchDropdown').text(searchType);

    $('#searchInput').show();

    if ((zipcode != null) && (zipcode != '')) {

        var geocoder = new google.maps.Geocoder();
        var address = zipcode;
        geocoder.geocode({'address': address}, function (results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                pos = {
                    lat: results[0].geometry.location.lat(),
                    lng: results[0].geometry.location.lng()
                };


                map = new google.maps.Map(document.getElementById('map'), {
                    center: pos,
                    zoom: 15
                });
                bounds.extend(pos);


                infoWindow.open(map);
                map.setCenter(pos);

                // Call Places Nearby Search on user's location
                getNearbyPlaces(pos, searchType);
            } else {
                alert("Request failed.")
            }
        });


    } else if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(position => {
            pos = {
                lat: position.coords.latitude,
                lng: position.coords.longitude
            };
            map = new google.maps.Map(document.getElementById('map'), {
                center: pos,
                zoom: 15
            });
            bounds.extend(pos);


            infoWindow.open(map);
            map.setCenter(pos);

            // Call Places Nearby Search on user's location
            getNearbyPlaces(pos, searchType);
        }, () => {
            // Browser supports geolocation, but user has denied permission
            handleLocationError(true, infoWindow);
        });
    } else {
        // Browser doesn't support geolocation
        handleLocationError(false, infoWindow);
    }


}


// Perform a Places Nearby Search Request
function getNearbyPlaces(position, searchType) {

    let request = {
        location: position,
        rankBy: google.maps.places.RankBy.DISTANCE,
        keyword: searchType
    };

    service = new google.maps.places.PlacesService(map);
    service.nearbySearch(request, nearbyCallback);
}

// Handle the results (up to 20) of the Nearby Search
function nearbyCallback(results, status) {
    if (status == google.maps.places.PlacesServiceStatus.OK) {

        createMarkers(results);
    }
}

// Set markers at the location of each place result
function createMarkers(places) {
    places.forEach(place => {
        let marker = new google.maps.Marker({
            position: place.geometry.location,
            map: map,
            title: place.name
        });


        google.maps.event.addListener(marker, 'click', () => {
            let request = {
                placeId: place.place_id,
                fields: ['name', 'formatted_address', 'geometry',
                    'website', 'photos', 'formatted_phone_number']
            };

            /* Only fetch the details of a place when the user clicks on a marker.
             * If we fetch the details for all place results as soon as we get
             * the search response, we will hit API rate limits. */
            service.getDetails(request, (placeResult, status) => {
                checkReviews(place.place_id, place.name);
                showDetails(placeResult, marker, status);

            });
        });


        // Adjust the map bounds to include the location of this marker
        bounds.extend(place.geometry.location);
    });

    map.fitBounds(bounds);
}


// Builds an InfoWindow to display details above the marker
function showDetails(placeResult, marker, status) {
    if (status == google.maps.places.PlacesServiceStatus.OK) {
        let placeInfowindow = new google.maps.InfoWindow();


        placeInfowindow.setContent('<div><strong>' + placeResult.name +
            '</strong><br><div id="markerRatingDiv">' + 'Rating: ' + "None" + '</div></div>');
        placeInfowindow.open(marker.map, marker);
        currentInfoWindow.close();
        currentInfoWindow = placeInfowindow;
        showPanel(placeResult);
    } else {
        console.log('showDetails failed: ' + status);
    }
}


function showPanel(placeResult) {
    // If infoPane is already open, close it
    if (infoPane.classList.contains("open")) {
        infoPane.classList.remove("open");
    }

    // Clear the previous details
    while (infoPane.lastChild) {
        infoPane.removeChild(infoPane.lastChild);
    }


    if (placeResult.photos) {
        let firstPhoto = placeResult.photos[0];
        let photo = document.createElement('img');
        photo.classList.add('hero');
        photo.src = firstPhoto.getUrl();
        infoPane.appendChild(photo);
    }


    let name = document.createElement('h3');
    name.classList.add('place');
    name.textContent = placeResult.name;
    name.id = "placeTitle"
    infoPane.appendChild(name);

    let rating = document.createElement('p');
    rating.classList.add('details');
    rating.id = 'ratingText';
    rating.textContent = `Rating: 'None' \u272e`;
    infoPane.appendChild(rating);

    let address = document.createElement('p');
    address.classList.add('details');
    address.textContent = placeResult.formatted_address;
    infoPane.appendChild(address);

    if (placeResult.formatted_phone_number) {
        let phoneNumber = document.createElement('p');
        phoneNumber.classList.add('details');
        phoneNumber.textContent = placeResult.formatted_phone_number;
        infoPane.appendChild(phoneNumber);

    }

    if (placeResult.website) {
        let websitePara = document.createElement('p');
        let websiteLink = document.createElement('a');
        let websiteUrl = document.createTextNode(placeResult.website);
        websiteLink.appendChild(websiteUrl);
        websiteLink.title = placeResult.website;
        websiteLink.href = placeResult.website;
        websitePara.appendChild(websiteLink);
        infoPane.appendChild(websitePara);
    }


    infoPane.classList.add("open");
}

function checkReviews(placeId, placeName) {

    $.ajax({
        type: "POST",
        headers: {
            [header]: token,
        },
        url: "/checkReviews",
        data: {
            "placeId": placeId
        },
        success: function (reviewList) {
            var starAverage = "None";

            if (reviewList.length <= 0) {
                $("#ratingText").text('No ratings yet :(');

                let reviewHeader = document.createElement('h3');
                reviewHeader.id = 'reviewHeader';
                reviewHeader.innerHTML = "Reviews &nbsp <button style='float: right;' type=\"button\" class=\"btn btn-primary\" data-toggle=\"modal\" data-target=\"#addReviewModal\" data-placeName=\"" + placeName + "\" data-placeId=\"" + placeId + "\">  Add Review </button>";
                infoPane.appendChild(reviewHeader);
            } else {
                var starSum = 0;
                $.each(reviewList, function (index) {
                    starSum += reviewList[index].reviewStars;
                });
                starAverage = round(starSum / reviewList.length, 1);
                document.getElementById("ratingText").innerHTML = 'Rating: ' + starAverage + ' <span class="fa fa-star checked"></span>'//add reviews to panel
                document.getElementById("markerRatingDiv").innerHTML = 'Rating: ' + starAverage + ' <span class="fa fa-star checked"></span>'//add reviews to marker info


                let reviewHeader = document.createElement('h3');
                reviewHeader.id = 'reviewHeader';
                reviewHeader.innerHTML = "Reviews &nbsp <button style='float: right;' type=\"button\" class=\"btn btn-primary\" data-toggle=\"modal\" data-target=\"#addReviewModal\" data-placeName=\"" + placeName + "\" data-placeId=\"" + placeId + "\">  Add Review </button>";
                infoPane.appendChild(reviewHeader);


                $.each(reviewList, function (index) {
                    let reviewText = document.createElement('p');
                    reviewText.classList.add('reviews');
                    reviewText.innerHTML = "<div class=\"card\" style=\"width: 18rem;\"> <div class=\"card-body\">" +
                        " <h5 class=\"card-title\">" + reviewList[index].username + "</h5>" + "&nbsp; &nbsp; &nbsp;"

                        + "<h6 class=\"card-subtitle mb-2 text-muted\">" + formatDate(reviewList[index].timestamp) + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + reviewList[index].reviewStars + " <span class=\"fa fa-star checked\"></span> </h6>"
                        + "<br> &nbsp;"
                        + reviewList[index].reviewText + "</div>" + "\n</div>";
                    infoPane.appendChild(reviewText);
                });
            }


        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert('failure' + response);

        }


    });


}

function round(value, precision) {
    var multiplier = Math.pow(10, precision || 0);
    return Math.round(value * multiplier) / multiplier;
}

function formatDate(timestamp) {
    let t = timestamp.split(/[- :]/);
    let months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
    let monthName = months[t[1] - 1];
    let formattedDate = monthName + " " + t[2] + ", " + t[0];
    return formattedDate;
}


$(document).ready(function (e) {

    var starFieldsetValue;
    var placeId;

    $('#addReviewModal').on('show.bs.modal', function (event) {
        let button = $(event.relatedTarget) // Button that triggered the modal
        let placeName = button.data('placename')
        placeId = button.data('placeid')

        var modal = $(this)
        modal.find('.modal-title').text('Add review for ' + placeName)

    });


    $('#starRating input:radio').on('change', function () {
        starFieldsetValue = $(this).val();
    });


    $("#submitReviewButton").click(function () {
        let addReviewText = $('#addReviewText').val();

        let review = { placeId: placeId, reviewStars: starFieldsetValue, reviewText: addReviewText};

        $.ajax({
            type: "POST",
            headers: {
                [header]: token,
            },
            url: "addReview/",
            data: review,
            success: function (response) {
                //$("#first-rate" + starFieldsetValue).prop('checked', false);
                $('#addReviewModal').modal('toggle');
                $('#addReviewText').val('');
                $('#SuccessModal').modal('toggle');


                let reviewText = document.createElement('p');
                reviewText.classList.add('reviews');
                reviewText.id = "newReviewCard" + response.username;
                reviewText.innerHTML = "<div class=\"card\" style=\"width: 18rem;\"> <div class=\"card-body\">" +
                    " <h5 class=\"card-title\">" + response.username + "</h5>" + "&nbsp; &nbsp; &nbsp;"

                    + "<h6 class=\"card-subtitle mb-2 text-muted\">" + formatDate(response.timestamp) + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + review.reviewStars + " <span class=\"fa fa-star checked\"></span> </h6>"
                    + "<br> &nbsp;"
                    + review.reviewText + "</div>" + "\n</div>";
                infoPane.appendChild(reviewText);
                $("#newReviewCard" + response.username).insertAfter('#reviewHeader');
                updateReviewStars(review.placeId);
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                $('#addReviewModal').modal('toggle');
                $('#ErrorModal').modal('toggle');

            }
        });
    });

    function updateReviewStars(placeId) {//update reviews stars after adding review

        $.ajax({
            type: "POST",
            headers: {
                [header]: token,
            },
            url: "/checkReviews",
            data: {
                "placeId": placeId
            },
            success: function (reviewList) {
                var starSum = 0;
                $.each(reviewList, function (index) {
                    starSum += reviewList[index].reviewStars;
                });

                let starAverage = round(starSum / reviewList.length, 1);
                document.getElementById("ratingText").innerHTML = 'Rating: ' + starAverage + ' <span class="fa fa-star checked"></span>'//add reviews to panel
                document.getElementById("markerRatingDiv").innerHTML = 'Rating: ' + starAverage + ' <span class="fa fa-star checked"></span>'//add reviews to marker info
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                $('#ErrorModal').modal('toggle');
            }

        });
    }


});

