document.getElementById('clean').onclick = function() {
    console.log(window.location.href.split("?")[0]);
    window.location = window.location.href.split("?")[0];
}