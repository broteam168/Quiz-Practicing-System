document.addEventListener('DOMContentLoaded', function() {
    const searchSubjectInput = document.getElementById('search-subject');
    const subjectList = document.getElementById('subject-list');
    const searchQuizTypeInput = document.getElementById('search-quizType');
    const quizTypeList = document.getElementById('quizType-list');
    const searchIdInput = document.getElementById('search-id');
    const idList = document.getElementById('id-list');
    const ownerFilterRow = document.getElementById('owner-filter-row');
    
    if (isExpert) {
        ownerFilterRow.style.display = 'none';
    }

    function filterSubjects() {
        const searchValue = searchSubjectInput.value.toLowerCase();
        const labels = subjectList.querySelectorAll('label');
        labels.forEach(label => {
            const text = label.textContent.toLowerCase();
            label.parentElement.style.display = text.includes(searchValue) ? 'block' : 'none';
        });
    }

    function filterQuizTypes() {
        const searchValue = searchQuizTypeInput.value.toLowerCase();
        const labels = quizTypeList.querySelectorAll('label');
        labels.forEach(label => {
            const text = label.textContent.toLowerCase();
            label.parentElement.style.display = text.includes(searchValue) ? 'block' : 'none';
        });
    }

    function filterIds() {
        const searchValue = searchIdInput.value.toLowerCase();
        const labels = idList.querySelectorAll('label');
        labels.forEach(label => {
            const text = label.textContent.toLowerCase();
            label.parentElement.style.display = text.includes(searchValue) ? 'block' : 'none';
        });
    }

    searchSubjectInput.addEventListener('input', filterSubjects);
    searchQuizTypeInput.addEventListener('input', filterQuizTypes);
    searchIdInput.addEventListener('input', filterIds);

    const closeFilterPopup = document.getElementById('close-filter-popup');
    const filterPopupContainer = document.getElementById('filter-popup-container');

    closeFilterPopup.addEventListener('click', function () {
        filterPopupContainer.style.display = 'none';
    });
});
