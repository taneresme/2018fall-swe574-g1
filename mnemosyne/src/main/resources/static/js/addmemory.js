function html() {
    // to get the content of the editor as HTML object
    var htmlContent = quill.container.firstChild.innerHTML;
    console.log(htmlContent);
}
var options = {
    modules: {
        toolbar: [
            [{ header: [1, 2, false] }],
            ['bold', 'italic', 'underline'],
            ['image', 'code-block']
        ]
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
let title = '';
function down() {
    title = document.getElementById('nme').value;
    console.log(title);
    document.getElementById('titleSpan').innerHTML = title;
    moveDown(".main");
}
function up() {
    moveUp(".main");
}
// prevent from from doing what it normally does
document.forms.myForm.onsubmit = function(e) {
    e = e || event;
    e.preventDefault();
}

document.forms.myForm.button.onclick = function () {
    return false;
}
