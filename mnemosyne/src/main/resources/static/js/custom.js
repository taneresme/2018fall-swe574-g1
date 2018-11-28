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

function getRand(from, to, fixed) {
    return (Math.random() * (to - from) + from).toFixed(fixed) * 1;
    // .toFixed() returns string, so ' * 1' is a trick to convert to number
}
var map;
function initMap() {
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
