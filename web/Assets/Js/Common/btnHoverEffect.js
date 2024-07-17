// function to make a hover effect, swap the background color and the text color of the btn class
function btnHoverEffect() {
    const btns = document.querySelectorAll('.btn');
    btns.forEach((btn) => {
        btn.addEventListener('mouseover', () => {
            // set transition speed to 0s
            btn.style.transition = '0s';
            const currentStyle = window.getComputedStyle(btn);
            const currentBgColor = currentStyle.backgroundColor;
            const currentTextColor = currentStyle.color;
            btn.style.backgroundColor = currentTextColor;
            btn.style.color = currentBgColor;
            // set border
            btn.style.border = '1px solid';
            // set border color to the same as the text color
            btn.style.borderColor = currentBgColor;
            // if btn has svg as child, change the fill color to the same as the text color, border color is the same as the text color
            const svg = btn.querySelector('svg');
            if (svg) {
                svg.style.fill = currentTextColor;
                // path stroke color is the same as the text color
                const paths = svg.querySelectorAll('path');
                paths.forEach(path => {
                    path.style.stroke = currentBgColor;
                });
            }
        });
        btn.addEventListener('mouseout', () => {
            const currentBgColor = btn.style.backgroundColor;
            const currentTextColor = btn.style.color;
            btn.style.backgroundColor = currentTextColor;
            btn.style.color = currentBgColor;
            // set border
            btn.style.border = '1px solid';
            // set border color to the same as the text color
            btn.style.borderColor = currentBgColor;
            // svg
            const svg = btn.querySelector('svg');
            if (svg) {
                svg.style.fill = currentTextColor;
                const paths = svg.querySelectorAll('path');
                paths.forEach(path => {
                    path.style.stroke = currentBgColor;
                });
            }
        });
    });
}

// load the function when the page is loaded
document.addEventListener('DOMContentLoaded', () => {
    // set all border button to 1px first
    const btns = document.querySelectorAll('.btn');
    btns.forEach(btn => {
        btn.style.border = '1px solid';
    });
    btnHoverEffect();
});