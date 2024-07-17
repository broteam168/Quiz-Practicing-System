document.addEventListener('DOMContentLoaded', function () {
    const filterForm = document.getElementById('mainFilterForm');
    const sortOrderInput = document.getElementById('sortOrder');
    const sortFieldInput = document.getElementById('sortField');
    const idHeader = document.getElementById('idHeader');
    const levelHeader = document.getElementById('levelHeader');
    const numQuestionsHeader = document.getElementById('numQuestionsHeader');
    const durationHeader = document.getElementById('durationHeader');
    const passRateHeader = document.getElementById('passRateHeader');
    const allQuizzesButton = document.getElementById('allQuizzes');
    const noTestTakenButton = document.getElementById('noTestTaken');
    const testTakenButton = document.getElementById('testTaken');
    const searchInput = document.getElementById('search');
    const searchTermInput = document.getElementById('searchTerm');
    const clearSearchBtn = document.getElementById('clearSearchBtn');
    const searchBtn = document.getElementById('searchBtn');
    const clearFilterBtn = document.getElementById('clearFilterBtn');
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');
    const pageSelect = document.getElementById('pageSelect');
    const filterModal = document.getElementById('filter-popup-container');
    const openFilterModalBtn = document.getElementById('openFilterModal');
    const closeFilterPopupBtn = document.getElementById('close-filter-popup');
    const filterBtn = document.querySelector('.filter-btn');

    let currentSortOrder = sortOrderInput.value || 'asc';
    let currentSortField = sortFieldInput.value || '';

    function updatePage(page) {
        document.getElementById('page').value = page;
        filterForm.submit();
    }

    function updateActiveTab(activeTab) {
        const tabs = document.querySelectorAll('.main__navigation-item');
        tabs.forEach(tab => {
            tab.classList.remove('active');
        });
        activeTab.classList.add('active');
    }

    function updateSortIcons() {
        const sortFields = {
            'idHeader': 'quiz_id',
            'levelHeader': 'level',
            'numQuestionsHeader': 'num_questions',
            'durationHeader': 'duration',
            'passRateHeader': 'pass_rate'
        };

        Object.keys(sortFields).forEach(field => {
            const header = document.getElementById(field);
            if (header) {
                const sortIcons = header.querySelector('.sort-icons');
                if (sortIcons) {
                    if (sortFields[field] === currentSortField) {
                        if (currentSortOrder === 'asc') {
                            sortIcons.innerHTML = '▲';
                        } else if (currentSortOrder === 'desc') {
                            sortIcons.innerHTML = '▼';
                        }
                    } else {
                        sortIcons.innerHTML = '▲▼';
                    }
                }
            }
        });
    }

    function handleSortClick(field, sortFieldValue) {
        if (currentSortField === sortFieldValue) {
            currentSortOrder = currentSortOrder === 'asc' ? 'desc' : 'asc';
        } else {
            currentSortField = sortFieldValue;
            currentSortOrder = 'asc';
        }
        sortFieldInput.value = currentSortField;
        sortOrderInput.value = currentSortOrder;
        updateSortIcons();
        filterForm.submit();
    }

    if (idHeader) {
        idHeader.addEventListener('click', function () {
            handleSortClick('idHeader', 'quiz_id');
        });
    }

    if (levelHeader) {
        levelHeader.addEventListener('click', function () {
            handleSortClick('levelHeader', 'level');
        });
    }

    if (numQuestionsHeader) {
        numQuestionsHeader.addEventListener('click', function () {
            handleSortClick('numQuestionsHeader', 'num_questions');
        });
    }

    if (durationHeader) {
        durationHeader.addEventListener('click', function () {
            handleSortClick('durationHeader', 'duration');
        });
    }

    if (passRateHeader) {
        passRateHeader.addEventListener('click', function () {
            handleSortClick('passRateHeader', 'pass_rate');
        });
    }

    if (allQuizzesButton) {
        allQuizzesButton.addEventListener('click', function () {
            document.getElementById('statusFilter').value = 'all';
            updateActiveTab(allQuizzesButton);
            filterForm.submit();
        });
    }

    if (noTestTakenButton) {
        noTestTakenButton.addEventListener('click', function () {
            document.getElementById('statusFilter').value = 'noTestTaken';
            updateActiveTab(noTestTakenButton);
            filterForm.submit();
        });
    }

    if (testTakenButton) {
        testTakenButton.addEventListener('click', function () {
            document.getElementById('statusFilter').value = 'testTaken';
            updateActiveTab(testTakenButton);
            filterForm.submit();
        });
    }

    if (searchBtn) {
        searchBtn.addEventListener('click', function (e) {
            e.preventDefault();
            searchTermInput.value = searchInput.value; // Ensure the search term is captured
            filterForm.submit(); // Submit the form to perform the search
        });
    }

    if (clearSearchBtn) {
        clearSearchBtn.addEventListener('click', function () {
            searchInput.value = '';
            searchTermInput.value = '';
            filterForm.submit();
        });
    }

    if (clearFilterBtn) {
        clearFilterBtn.addEventListener('click', function () {
            document.getElementById('subjects').value = '';
            document.getElementById('quizTypes').value = '';
            document.getElementById('quizIds').value = '';
            filterForm.submit();
        });
    }

    function checkFilters() {
        const subjects = document.getElementById('subjects').value;
        const quizTypes = document.getElementById('quizTypes').value;
        const quizIds = document.getElementById('quizIds').value;

        if (subjects || quizTypes || quizIds) {
            clearFilterBtn.style.display = 'block';
        } else {
            clearFilterBtn.style.display = 'none';
        }
    }

    if (prevBtn) {
        prevBtn.addEventListener('click', function () {
            const currentPage = parseInt(document.getElementById('page').value, 10);
            if (currentPage > 1) {
                updatePage(currentPage - 1);
            }
        });
    }

    if (nextBtn) {
        nextBtn.addEventListener('click', function () {
            const currentPage = parseInt(document.getElementById('page').value, 10);
            updatePage(currentPage + 1);
        });
    }

    if (pageSelect) {
        pageSelect.addEventListener('change', function () {
            updatePage(this.value);
        });
    }

    if (openFilterModalBtn) {
        openFilterModalBtn.addEventListener('click', function () {
            filterModal.style.display = 'block';
        });
    }

    if (closeFilterPopupBtn) {
        closeFilterPopupBtn.addEventListener('click', function () {
            filterModal.style.display = 'none';
        });
    }

    if (filterBtn) {
        filterBtn.addEventListener('click', function (e) {
            e.preventDefault();

            // Gather selected filters
            const selectedIds = Array.from(document.querySelectorAll('#id-list input[type="checkbox"]:checked')).map(cb => cb.value);
            const selectedSubjects = Array.from(document.querySelectorAll('#subject-list input[type="checkbox"]:checked')).map(cb => cb.value);
            const selectedQuizTypes = Array.from(document.querySelectorAll('#quizType-list input[type="checkbox"]:checked')).map(cb => cb.value);

            // Clear existing hidden inputs
            const existingHiddenInputs = filterForm.querySelectorAll('input[type="hidden"]');
            existingHiddenInputs.forEach(input => input.remove());

            // Create hidden inputs for the selected filters
            const hiddenIdInput = document.createElement('input');
            hiddenIdInput.type = 'hidden';
            hiddenIdInput.name = 'quizIds';
            hiddenIdInput.value = selectedIds.join(',');

            const hiddenSubjectInput = document.createElement('input');
            hiddenSubjectInput.type = 'hidden';
            hiddenSubjectInput.name = 'subjects';
            hiddenSubjectInput.value = selectedSubjects.join(',');

            const hiddenQuizTypeInput = document.createElement('input');
            hiddenQuizTypeInput.type = 'hidden';
            hiddenQuizTypeInput.name = 'quizTypes';
            hiddenQuizTypeInput.value = selectedQuizTypes.join(',');

            // Append the hidden inputs to the main filter form
            filterForm.appendChild(hiddenIdInput);
            filterForm.appendChild(hiddenSubjectInput);
            filterForm.appendChild(hiddenQuizTypeInput);

            filterForm.submit(); // Submit the main filter form with the new filter parameters
        });
    }

    window.updatePage = updatePage;

    updateSortIcons();

    // Set the initial active tab based on the statusFilter value
    const statusFilter = document.getElementById('statusFilter').value;
    if (statusFilter === 'all') {
        updateActiveTab(allQuizzesButton);
    } else if (statusFilter === 'noTestTaken') {
        updateActiveTab(noTestTakenButton);
    } else if (statusFilter === 'testTaken') {
        updateActiveTab(testTakenButton);
    }

    const deleteButtons = document.querySelectorAll('.delete-btn');
    const deletePopup = document.getElementById('deleteConfirmationModal');
    const deleteQuizId = document.getElementById('deleteQuizId');
    const closePopup = document.getElementById('closePopup');
    const cancelDelete = document.getElementById('cancelDelete');

    deleteButtons.forEach(button => {
        button.addEventListener('click', function (e) {
            e.preventDefault();
            const quizId = this.getAttribute('data-id');
            deleteQuizId.value = quizId;
            deletePopup.style.display = 'flex';
        });
    });

    closePopup.addEventListener('click', function () {
        deletePopup.style.display = 'none';
    });

    cancelDelete.addEventListener('click', function () {
        deletePopup.style.display = 'none';
    });

    window.onclick = function (event) {
        if (event.target == deletePopup) {
            deletePopup.style.display = 'none';
        }
    }

    checkFilters(); // Initial check on page load to determine if the clear filter button should be displayed
});
