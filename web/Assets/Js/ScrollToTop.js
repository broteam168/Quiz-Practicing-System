var scrollToTopBtn = document.getElementById("scrollToTopBtn");

window.onscroll = function () {
    scrollFunction();
};

function scrollFunction() {
    if (document.body.scrollTop > 800 || document.documentElement.scrollTop > 800) {
        scrollToTopBtn.style.display = "block";
    } else {
        scrollToTopBtn.style.display = "none";
    }
}

// When the user clicks on the button, scroll to the top of the document
function scrollToTop() {
    var currentPosition = document.documentElement.scrollTop || document.body.scrollTop;
    console.log("Current Position: ", currentPosition);

    if (currentPosition > 0) {
        window.scrollBy(0, -50); // Scroll up by 50 pixels each step
        setTimeout(scrollToTop, 10); // Repeat the function every 10 milliseconds
    }
}

