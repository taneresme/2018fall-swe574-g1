var memory = {};
function html() {
    // to get the content of the editor as HTML object
    var htmlContent = quill.container.firstChild.innerHTML;
    return htmlContent;
}
var options = {
    modules: {
        // toolbar: [
        //     [{ header: [1, 2, false] }],
        //     ['bold', 'italic', 'underline'],
        //     ['image', 'code-block']
        // ]
        toolbar: false,
    },
    placeholder: 'Compose an epic...',
    readOnly: false,
    theme: 'snow'
};
var quill = new Quill('#editor', options);
onePageScroll(".main", {
    sectionContainer: "section",     // sectionContainer accepts any kind of selector in case you don't want to use section
    easing: "ease",                  // Easing options accepts the CSS3 easing animation such "ease", "linear", "ease-in",
                                     // "ease-out", "ease-in-out", or even cubic bezier value such as "cubic-bezier(0.175, 0.885, 0.420, 1.310)"
    animationTime: 1000,             // AnimationTime let you define how long each section takes to animate
    pagination: false,                // You can either show or hide the pagination. Toggle true for show, false for hide.
    updateURL: false,                // Toggle this true if you want the URL to be updated automatically when the user scroll to each page.
    beforeMove: function(index) {},  // This option accepts a callback function. The function will be called before the page moves.
    afterMove: function(index) {},   // This option accepts a callback function. The function will be called after the page moves.
    loop: false,                     // You can have the page loop back to the top/bottom when the user navigates at up/down on the first/last page.
    keyboard: false,                  // You can activate the keyboard controls
    responsiveFallback: false        // You can fallback to normal page scroll by defining the width of the browser in which
                                     // you want the responsive fallback to be triggered. For example, set this to 600 and whenever
                                     // the browser's width is less than 600, the fallback will kick in.
});


// View only
var options2 = {
    modules: {
        toolbar: false,
    },
    readOnly: true,
    theme: 'snow'
};
var quill2 = new Quill('#editor-view', options2);

let title = '';
function titleDown() {
    title = document.getElementById('nme').value;
    if (title !== null && title !== '') {
        console.log('Title is ', title);
        document.getElementById('titleSpan').innerHTML = title;
        createTitle(title);
        goTo(2);
    }   else {
        console.log('Title is empty. Title is required.');
    }
}
function up() {
    moveUp(".main");
}
function goTo(index) {
    moveTo(".main", index);
}
// prevent from from doing what it normally does
document.forms.myForm.onsubmit = function(e) {
    e = e || event;
    e.preventDefault();
}

document.forms.myForm1.onsubmit = function(e) {
    e = e || event;
    e.preventDefault();
}

var slider = document.getElementById("myRange");
var output = document.getElementById("km");
output.innerHTML = slider.value + ' km'; // Display the default slider value

// Update the current slider value (each time you drag the slider handle)
slider.oninput = function() {
    output.innerHTML = this.value + ' km';
} 

function makeVisible(index) {
    var zero = document.getElementById('location-0');
    var one = document.getElementById('location-1');
    var two = document.getElementById('location-2');
    zero.style.display = 'none';
    one.style.display = 'none';
    two.style.display = 'none';
    switch (index) {
        case 0:
            zero.style.display = 'block';
            return;
        case 1:
            one.style.display = 'block';
            return;
        case 2:
            two.style.display = 'block';
            return;
        default:
            one.style.display = 'block';
            return;
    }
}

var savedLocations = [];
var paths = [];
var lat = 0;
var lon = 0;
var address = '';
let map = null;
function writeLat() {
    console.log('lat, lon ', lat, lon, address);
}

function guid() {
  function s4() {
    return Math.floor((1 + Math.random()) * 0x10000)
      .toString(16)
      .substring(1);
  }
  return s4() + s4();
}

function createAwesome(content) {
    var i = document.createElement("i");
    i.className = 'fas fa-' + content;
    return i;
}


function showLocations() {
    var div = document.getElementById("location-list");
    div.style.display = 'block';
    div.innerHTML = '';
    var h2 = document.createElement("h2");
    var textnode = document.createTextNode("MY LOCATIONS");         // Create a text node
    div.appendChild(h2);
    h2.appendChild(textnode);
    savedLocations.forEach(loc => {
        var node = document.createElement("LI");                 // Create a <li> node
        var textnode = document.createTextNode(' ' + loc.address);         // Create a text node
        var awesome;
        if (loc.locType === 0) {
            awesome = createAwesome('map-marker-alt');
        } else if (loc.locType === 1) {
            awesome = createAwesome('route');
        } else {
            awesome = createAwesome('circle');
        }
        var span = document.createElement("span");
        span.onclick = function() { deleteLoc(loc.id); };
        span.appendChild(createAwesome('times'));
        node.appendChild(span);
        node.appendChild(awesome);
        node.appendChild(textnode);
        div.appendChild(node);
    });
}

function deleteLoc(id) {
    console.log('Delete location');
    for (i = 0; i < savedLocations.length; i++ ) {
        console.log(id, savedLocations[i].id);
        if (id == savedLocations[i].id) {
            savedLocations.splice(i, 1);
            break;
        }
    }
    showLocations();
}

function saveLoc(locType) {
    var radius = null;
    if (locType === 2) {
        var slider = document.getElementById("myRange");
        radius = slider.value * 1000;
    }
    var uuid = guid();
    if (locType === 1) {
        paths.push(uuid);
    }
    savedLocations.push({
        id: uuid,
        locType: locType,
        lat: lat,
        lon: lon,
        radius: radius,
        address: address
    });
    console.log('The point is saved');
    showLocations();
}

function writeLocations() {
    console.log(locationPoints);
    console.log(locationRoutes);
    console.log(locationRadius);
}
    
function initAutocomplete() {
    map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 39.397, lng: 34.644},
        zoom: 5,
        mapTypeId: 'roadmap'
    });

    var input = document.getElementById('pac-input');
    var searchBox = new google.maps.places.SearchBox(input);
    // map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

    map.addListener('bounds_changed', function() {
        searchBox.setBounds(map.getBounds());
    });

    var markers = [];
    searchBox.addListener('places_changed', function() {
        var places = searchBox.getPlaces();

        if (places.length == 0) {
        return;
        }
        console.log('places ', places);
        lat = places[0].geometry.location.lat();
        lon = places[0].geometry.location.lng();
        address = places[0].formatted_address;
        var locationWarning = document.getElementById('location-no');
        locationWarning.style.display = 'none';
        var locationNames = document.getElementsByName('location-name');
        locationNames.forEach(loc => {
            loc.innerHTML = 'Selected location: ' + address;
        });
        var locationSaveButtons = document.getElementsByName('location-save');
        locationSaveButtons.forEach(loc => {
            loc.style.display = 'block';
        });

        markers.forEach(function(marker) {
        marker.setMap(null);
        });
        markers = [];

        var bounds = new google.maps.LatLngBounds();
        places.forEach(function(place) {
        if (!place.geometry) {
            console.log("Returned place contains no geometry");
            return;
        }
        var icon = {
            url: place.icon,
            size: new google.maps.Size(71, 71),
            origin: new google.maps.Point(0, 0),
            anchor: new google.maps.Point(17, 34),
            scaledSize: new google.maps.Size(25, 25)
        };

        markers.push(new google.maps.Marker({
            map: map,
            icon: icon,
            title: place.name,
            position: place.geometry.location
        }));

        if (place.geometry.viewport) {
            // Only geocodes have viewport.
            bounds.union(place.geometry.viewport);
        } else {
            bounds.extend(place.geometry.location);
        }
        });
        map.fitBounds(bounds);
    });
}


let cityCircle = null;
function drawCircle() {
    if (lat === 0 && lon === 0) {
        return;
    }
    var slider = document.getElementById("myRange");
    var radius = slider.value;
    console.log('Slider value is ', radius);
    if (cityCircle) {
        cityCircle.setMap(null);
    }
    cityCircle = new google.maps.Circle({
    strokeColor: '#FF0000',
    strokeOpacity: 0.8,
    strokeWeight: 2,
    fillColor: '#FF0000',
    fillOpacity: 0.35,
    map: map,
    center: {lat: lat, lng: lon},
    radius: parseFloat(radius * 1000)
    });
}


function dates() {
    var e = document.getElementById("year-select");
    var year = e.options[e.selectedIndex].value;
    var e = document.getElementById("month-select");
    var month = e.options[e.selectedIndex].value;
    var e = document.getElementById("month-select");
    var day = e.options[e.selectedIndex].value;
    var e = document.getElementById("duration");
    var duration = e.value;
    var e = document.getElementById("period-select");
    var period = e.options[e.selectedIndex].value;
    if (duration !== '') {
        goTo(4);
        memory['year'] = year;
        memory['month'] = month;
        memory['day'] = month;
        memory['period'] = period;
        memory['duration'] = duration;
        patch();
    }
    console.log(year, month, day, period, duration);
}

document.forms.myForm.onsubmit = function(e) {
    e = e || event;
    e.preventDefault();
}
// Year
var select = document.getElementById("year-select");
for(var i = new Date().getFullYear(); i >= 1930; i--) {
    var el = document.createElement("option");
    el.textContent = i;
    el.value = i;
    select.appendChild(el);
}
// Month
var select = document.getElementById("month-select");
var options = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
var el = document.createElement("option");
el.textContent = 'None';
el.value = null;
select.appendChild(el);
for(var i = 1; i <= options.length; i++) {
    var opt = options[i];
    var el = document.createElement("option");
    el.textContent = opt;
    el.value = i;
    select.appendChild(el);
}
// Day
var select = document.getElementById("day-select");
var el = document.createElement("option");
el.textContent = 'None';
el.value = null;
select.appendChild(el);
for(var i = 1; i < 32; i++) {
    var el = document.createElement("option");
    el.textContent = i;
    el.value = i;
    select.appendChild(el);
}
// Duration
var select = document.getElementById("period-select");
var options = ["Minutes", "Hours", "Days", "Weeks", "Months", "Years", "Decades"];
for(var i = 0; i < options.length; i++) {
    var opt = options[i];
    var el = document.createElement("option");
    el.textContent = opt;
    el.value = opt;
    select.appendChild(el);
}
// Integer checker for duration
function isNumber(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if ( (charCode > 31 && charCode < 48) || charCode > 57) {
        return false;
    }
    return true;
}

// Requests

// Load axios
// const axios = require('axios');
function createTitle(title) {
    axios.post('/api/memories/create?title=' + title)
        .then(function (response) {
            memory = response.data;
            console.log(memory);
        })
        .catch(function (error) {
            console.log(error);
        })
}
function patch() {
    axios.patch('/api/memories/update', memory)
        .then(function (response) {
            console.log('Update successful');
        })
        .catch(function (error) {
            console.log(error);
        })
}

function saveMemory(process) {
    if (process == 1) {
        memory['published'] = true;
        memory['type'] = 'PUBLIC';
    }
    // memory['text'] = html();
    memory['text'] = quill.getText();

    console.log(quill.getText());
    setTimeout(function() {
        console.log(memory);
        patch();
    }, 500);
    setTimeout(function() {
        if (process == 1) {
            document.location.href = '/home';
        }
    }, 4000);
}

