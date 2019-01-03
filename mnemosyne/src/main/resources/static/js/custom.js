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
        i.attr("style", "color:red;");
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

function searchInJs() {
    var input = document.getElementById("search_box");
    var filter = input.value.toLowerCase();
    var nodes = document.getElementsByClassName('search_target');

    for (i = 0; i < nodes.length; i++) {
        if (nodes[i].innerText.toLowerCase().includes(filter)) {
            nodes[i].style.display = "block";
        } else {
            nodes[i].style.display = "none";
        }
    }
}

function getRand(from, to, fixed) {
    return (Math.random() * (to - from) + from).toFixed(fixed) * 1;
    // .toFixed() returns string, so ' * 1' is a trick to convert to number
}
var map;

/*var loc = $.parseJSON(locations);

function initMap() {
    console.log('init map');

    map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 39.397, lng: 34.644},
        zoom: 5
    });

    for (i = 0; i < loc.len; i++ ) {
        var marker = new google.maps.Marker({
            position: {lat: parseFloat(loc[i].latitude), lng: parseFloat(loc[i].longitude)},
            map: map,
            title: 'My unforgettable memory!'
        });
    }
}*/


var loc = $.parseJSON(locations);
var markers = [];

function initMap() {
    console.log('init map');

    map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: parseFloat(loc[0].latitude), lng: parseFloat(loc[0].longitude)},
        zoom: 5
    });

    for (i = 0; i < loc.length; i++ ) {
        var marker = new google.maps.Marker({
            position: {lat: parseFloat(loc[i].latitude), lng: parseFloat(loc[i].longitude)},
            url: '/memories/' + loc[i].memoryId,
            title: loc[i].memoryName,
            animation:google.maps.Animation.DROP
        });
        markers.push(marker);
    }

    for (i = 0; i < markers.length; i++ ) {
        markers[i].setMap(map);
    }

    google.maps.event.addDomListener(window, 'load', initMap);
    for (i = 0; i < markers.length; i++ ) {
        google.maps.event.addListener(markers[i], 'click', function() {window.location.href = marker.url;});
    }
}



var URL = 'https://annotatorconverter.tugcan.net';

var app = new annotator.App();
app.include(annotator.ui.main);
app.include(annotator.storage.http, {
    prefix: URL,
    urls: {
        create: '/create/' + userName + '/' + window.location.href,
        search: '/search'
    }
});
app.start()
    .then(function () {
        app.annotations.load();
});

anno.addHandler('onAnnotationCreated', function(annotation) {
    imageToW3C(annotation);
});

setTimeout(() => {
    var memoryView = document.getElementById('editor-view');
    var images = memoryView.getElementsByTagName('img');
    for (var i=0; i < images.length; i++) {
        anno.makeAnnotatable(images[i]);
    }
}, 2000);


function imageToW3C(annotation) {
    var ann = {
        "@context": "http://www.w3.org/ns/anno.jsonld",
        "id": "file:///home/tugcan/scripts/html/index.html",
        "type": "Annotation",
        "generator": {
            "homepage": "http://mnemosyne.ml",
            "id": "http://mnemosyne.ml/id",
            "name": "Mnemosyne",
            "type": "1.0"
        },
        "body": {
            "type": "TextualBody",
            "value": "This is my comment",
            "ceator": "user1"
        },
        "target": {
            "id": "https://imagessl.etstur.com/files/resources/images/otel/otelKategori/yurt_ici_otelleri.jpg#xywh=0.4,0.24344569288389514,0.2525,0.30711610486891383",
            "type": "Image",
            "format": "image/jpeg",
            "creator": "user2"
        }
    };
    ann.body.value = annotation.text;
    rng = annotation.shapes[0].geometry;
    ann.id = annotation.context;
    ann.generator.id = window.location.href;
    ann.target.id = annotation.src + '#xywh=' + rng.x + ',' + rng.y + ',' + rng.width + ',' + rng.height;
    ann.target.creator = userName;
    console.log(ann);
    axios.post('http://annotationserver.xtptzahyma.us-east-1.elasticbeanstalk.com/annotations', ann, {headers: {'Content-Type':'application/ld+json'}})
    .then(response => {
        console.log(response);
    })
    .catch(error => {
        console.log(error);
    })
}

function W3CtoImage(el) {
    var w = {'src': '', 'text': '',
    'shapes':
        [{
            'type': 'rect',
            'geometry':
                {'x': 0, 'y': 0, 'width': 0, 'height': 0}
        }]}
    var index = el.target.id.lastIndexOf('#');
    w.src = el.target.id.substring(0, index);
    w.text = el.body.value;
    var index = el.target.id.lastIndexOf('=');
    var rng = el.target.id.substring(index + 1).split(',');
    w.shapes[0].geometry.x = rng[0];
    w.shapes[0].geometry.y = rng[1];
    w.shapes[0].geometry.width = rng[2];
    w.shapes[0].geometry.height = rng[3];

    setTimeout(() => {
        anno.addAnnotation(w);
    }, 500);
}

function divideData(data) {
    data.forEach(el => {
        if ( !('generator' in el) || !('target' in el) || !('body' in el) ) {
        return;
    }
        if ( !('id' in el.generator) || el.target.type !== 'Image') {
            return;
        }
        if (el.generator.id === window.location.href) {
            W3CtoImage(el);
        }
    })
}

setTimeout(() => {
    // axios.get('http://annotationserver.xtptzahyma.us-east-1.elasticbeanstalk.com/annotations')
    axios.get('http://annotationserver.xtptzahyma.us-east-1.elasticbeanstalk.com/annotations')
    .then(response => {
        divideData(response.data);
    })
    .catch(error => {
        console.log('Error ', error);
    })
    }, 2400);