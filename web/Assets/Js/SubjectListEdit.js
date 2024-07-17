document.addEventListener('DOMContentLoaded', function() {
    const filterForm = document.getElementById('mainFilterForm');
    const sortOrderInput = document.getElementById('sortOrder');
    const sortHeader = document.getElementById('numLessonsHeader');
    const publishedButton = document.getElementById('published');
    const unpublishedButton = document.getElementById('unpublished');
    const allButton = document.getElementById('all');
    const searchInput = document.getElementById('search');
    const searchTermInput = document.getElementById('searchTerm');
    const clearButton = document.getElementById('clearSearchBtn');
    const searchBtn = document.getElementById('searchBtn');
    const clearFilterBtn = document.getElementById('clearFilterBtn');
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');
    const pageSelect = document.getElementById('pageSelect');
    const userRole = 'EXPERT'; // Replace with the actual role value from your server

    let currentSortOrder = sortOrderInput.value;

    function updatePage(page) {
        document.getElementById('page').value = page;
        filterForm.submit();
    }

    function updateActiveTab(activeTab) {
        const tabs = document.querySelectorAll('.main__navigation-item');
        tabs.forEach(tab => {
            tab.classList.remove('main__navigation-select');
        });
        activeTab.classList.add('main__navigation-select');
    }

    function updateSortIcons() {
        const sortIcons = sortHeader.querySelector('.sort-icons');
        sortIcons.classList.remove('active');
        if (currentSortOrder === 'asc') {
            sortIcons.classList.add('active');
            sortIcons.innerHTML = '▲';
        } else if (currentSortOrder === 'desc') {
            sortIcons.classList.add('active');
            sortIcons.innerHTML = '▼';
        } else {
            sortIcons.innerHTML = '▲▼';
        }
    }

    function searchSubjects() {
        searchTermInput.value = searchInput.value;
        document.getElementById('page').value = 1; // Reset to page 1 on search
        filterForm.submit();
    }

    function clearFilters() {
        document.getElementById('categoryFilter').value = 'all';
        document.getElementById('statusFilter').value = 'all';
        document.querySelectorAll('input[name="owner[]"]').forEach(input => input.remove());
        document.querySelectorAll('input[name="categories[]"]').forEach(input => input.remove());
        document.querySelectorAll('input[name="subjectId[]"]').forEach(input => input.remove());
        document.getElementById('search-id').value = '';
        filterForm.submit();
    }

    filterForm.addEventListener('submit', function(e) {
        const categories = document.querySelectorAll('input[name="categories[]"]');
        categories.forEach(category => {
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'categories[]';
            input.value = category.value;
            filterForm.appendChild(input);
        });
        const owners = document.querySelectorAll('input[name="owner[]"]');
        owners.forEach(owner => {
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'owner[]';
            input.value = owner.value;
            filterForm.appendChild(input);
        });
        const subjectIds = document.querySelectorAll('input[name="subjectId[]"]');
        subjectIds.forEach(subjectId => {
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'subjectId[]';
            input.value = subjectId.value;
            filterForm.appendChild(input);
        });
    });

    if (publishedButton) {
        publishedButton.addEventListener('click', function() {
            document.getElementById('statusFilter').value = '1';
            updateActiveTab(publishedButton);
            searchSubjects();
        });
    }

    if (unpublishedButton) {
        unpublishedButton.addEventListener('click', function() {
            document.getElementById('statusFilter').value = '0';
            updateActiveTab(unpublishedButton);
            searchSubjects();
        });
    }

    if (allButton) {
        allButton.addEventListener('click', function() {
            document.getElementById('statusFilter').value = 'all';
            updateActiveTab(allButton);
            searchSubjects();
        });
    }

    if (sortHeader) {
        sortHeader.addEventListener('click', function() {
            if (currentSortOrder === '') {
                currentSortOrder = 'asc';
            } else if (currentSortOrder === 'asc') {
                currentSortOrder = 'desc';
            } else {
                currentSortOrder = '';
            }
            sortOrderInput.value = currentSortOrder;
            updateSortIcons();
            searchSubjects();
        });
    }

    if (searchBtn) {
        searchBtn.addEventListener('click', function(e) {
            e.preventDefault();
            searchSubjects();
        });
    }

    if (clearButton) {
        clearButton.addEventListener('click', function() {
            searchInput.value = '';
            searchSubjects();
        });
    }

    if (clearFilterBtn) {
        clearFilterBtn.addEventListener('click', function() {
            clearFilters();
        });
    }

    if (prevBtn) {
        prevBtn.addEventListener('click', function() {
            const currentPage = parseInt(document.getElementById('page').value, 10);
            if (currentPage > 1) {
                updatePage(currentPage - 1);
            }
        });
    }

    if (nextBtn) {
        nextBtn.addEventListener('click', function() {
            const currentPage = parseInt(document.getElementById('page').value, 10);
            updatePage(currentPage + 1);
        });
    }

    if (pageSelect) {
        pageSelect.addEventListener('change', function() {
            updatePage(this.value);
        });
    }

    window.updatePage = updatePage;

    updateSortIcons();
});
