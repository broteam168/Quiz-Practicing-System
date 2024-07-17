

document.addEventListener('DOMContentLoaded', function () {

    loadPage()
    loadPopup();
});

function loadPage() {
    const percentageSquares = document.querySelectorAll('.percentage-square');
    percentageSquares.forEach(square => {
        const percentageText = square.querySelector('.percentage').textContent.trim();
        const percentage = parseInt(percentageText.replace('%', '').trim(), 10);
        console.log("percent: ", percentage)

        if (percentage >= 65) {
            square.classList.add('percentage-100-65');
        } else if (percentage >= 45) {
            square.classList.add('percentage-64-45');
        } else {
            square.classList.add('percentage-44-0');
        }
    });
}

function loadPopup() {
    const items_need_colored = document.querySelectorAll('.colored-text')
    // if text is easy -> green
    // if text is medium -> yellow
    // if text is hard -> red
    items_need_colored.forEach(item => {
        const text = item.textContent.trim();
        if (text === 'Easy') {
            item.classList.add('easy');
            // remove other
            item.classList.remove('medium');
            item.classList.remove('hard');
        } else if (text === 'Medium') {
            item.classList.add('medium');
            // remove other
            item.classList.remove('easy');
            item.classList.remove('hard');
        } else if (text === 'Hard') {
            item.classList.add('hard');
            // remove other
            item.classList.remove('easy');
            item.classList.remove('medium');
        } else {
            // this is percentage
            const percentage = parseInt(text.replace('%', '').trim(), 10);
            if (percentage >= 65) {
                item.classList.add('easy');
                item.classList.remove('medium');
                item.classList.remove('hard');
            } else if (percentage >= 45) {
                item.classList.add('medium');
                item.classList.remove('easy');
                item.classList.remove('hard');
            } else {
                item.classList.add('hard');
                item.classList.remove('easy');
                item.classList.remove('medium');
            }
        }
    });
}

function handleFilterChange() {
    const filterSelect = document.getElementById('filter');
    const filterOption = filterSelect.value;
    // Set the selected filter option in a hidden input for form submission
    document.getElementById('filter-input').value = filterOption;
    document.getElementById('page-input').value = 1;
    document.getElementById('form').submit();
}

function handleSearch() {
    document.getElementById('search-input').value = document.getElementById('search').value.trim();
    document.getElementById('form').submit();
}

function clearSearch() {
    document.getElementById('search').value = '';
    document.getElementById('search-input').value = '';
    document.getElementById('form').submit();
}

function handleSortChange() {
    const sortSelect = document.getElementById('sort');
    const sortOption = sortSelect.value;
    // Set the selected sort option in a hidden input for form submission
    document.getElementById('sort-input').value = sortOption;
    document.getElementById('page-input').value = 1;
    document.getElementById('form').submit();
}


