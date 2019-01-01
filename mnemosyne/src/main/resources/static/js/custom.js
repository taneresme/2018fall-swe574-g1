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

const URL = 'http://localhost:5100';

var app = new annotator.App();
app.include(annotator.ui.main);
app.include(annotator.storage.http, {
    prefix: URL,
    urls: {
        create: '/create/' + window.location.href,
        search: '/search'
    }
});
app.include(customAnn);
app.start();

anno.addHandler('onAnnotationCreated', function(annotation) {
    imageToW3C(annotation);
});

function customAnn() {
    return {
        annotationCreated: function (annotation) {
            app.notify("Annotation is created");
        }
    };
};

setTimeout(() => {
    var memoryView = document.getElementById('editor-view');
    var images = memoryView.getElementsByTagName('img');
    for (var i=0; i < images.length; i++) {
        anno.makeAnnotatable(images[i]);
    }


    var tr = { "@context": "http://www.w3.org/ns/anno.jsonld",
            "id": "file:///home/tugcan/scripts/html/index.html",
            "type": "Annotation",
            "body": {
                "type": "TextualBody",
                "value": "amina kodugumun projesi\n",
                "ceator": "user1"
            },
            "target": {
                "id": "http://ihg.scene7.com/is/image/ihg/holiday-inn-the-colony-4629618286-4x3#xywh=0.3,0.6104868913857678,0.17,0.19850187265917604",
                "type": "Image",
                "format": "image/jpeg",
                "ceator": "user2"
            }
        };

    W3CtoImage(tr);
}, 2000);

function annoToW3C(comment, quote, range) {
    var ann = {
        "@context": "http://www.w3.org/ns/anno.jsonld",
        "id": "http://mayonez/memory/3",
        "type": "Annotation",
        "body": {
            "type": "TextualBody",
            "value": "This is the annotation comment",
            "creator": "user"
        },
        "target": {
            "type": "TextQuoteSelector",
            "exact": "These are the words that",
            "prefix": 0,
            "suffix": 24,
            "refinedBy": {
            "type": "TextPositionSelector",
            "start": "/p[1]",
            "end": "/p[1]"
            }
        }
    }
    ann.body.value = comment;
    ann.target.exact = quote;
    ann.target.prefix = range.startOffset;
    ann.target.suffix = range.endOffset;
    ann.target.refinedBy.start = range.start;
    ann.target.refinedBy.end = range.end;
    console.log(ann);
}

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
            "ceator": "user2"
        }
    };
    ann.body.value = annotation.text;
    rng = annotation.shapes[0].geometry;
    ann.id = annotation.context;
    ann.target.id = annotation.src + '#xywh=' + rng.x + ',' + rng.y + ',' + rng.width + ',' + rng.height;
    console.log(ann);
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
        app.annotations.load();
    }, 500);
}

