if (!String.format) {
  String.format = function(format) {
    var args = Array.prototype.slice.call(arguments, 1);
    return format.replace(/{(\d+)}/g, function(match, number) { 
      return typeof $(args).get(number) != "undefined"
        ? $(args).get(number)
        : match
      ;
    });
  };
}

function chanceLikingStatus (id) {
    var i = $("#" + id);
    if (i.hasClass("fa-heart-o")){
        /* Liking */
        i.removeClass("fa-heart-o");
        i.addClass("fa-heart");
        i.attr("style", "color:red");
    }
    else{
        /* Disliking */
        i.removeClass("fa-heart");
        i.addClass("fa-heart-o");
        i.removeAttr("style");
    }

    /* Call API to change the liking status */
    $.ajax({
        type : "POST",
        contentType : "application/json",
        url : "/api/memories/" + id + "/like",
        dataType : "json",
        cache : false,
        timeout : 20000,
        success : function (data){console.log(data)},
        error : function (error){console.log(error)}
    });
}

function unfollow(userId){
    $.ajax({
        type : "POST",
        contentType : "application/json",
        url : "/friendships/remove/" + userId,
        dataType : "json",
        cache : false,
        timeout : 20000,
        success : function (data){
            console.log(data);

            $("#li_followed_" + userId).addClass("d-none");
            $("#li_unfollow_button_" + userId).addClass("d-none");
            $("#li_unfollowed_desc_" + userId).removeClass("invisible");
        },
        error : function (error){console.log(error)}
    });
}

function getRand(from, to, fixed) {
    return (Math.random() * (to - from) + from).toFixed(fixed) * 1;
    // .toFixed() returns string, so ' * 1' is a trick to convert to number
}
var map;
function initMap() {
    console.log('init map');
    map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 39.397, lng: 34.644},
        zoom: 5
    });

    for (i = 0; i < 12; i++ ) {
        var marker = new google.maps.Marker({
            position: {lat: getRand(37, 41, 3), lng: getRand(30, 41, 3)},
            map: map,
            title: 'My unforgettable memory!'
        });
    }
}

var app = new annotator.App();
app.include(annotator.ui.main);
app.include(annotator.storage.http);
app.start();
