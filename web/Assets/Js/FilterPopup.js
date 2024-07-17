document.addEventListener('DOMContentLoaded', function() {
    const filterButton = document.getElementById('openFilterModal');
    const closeButton = document.getElementById('close-filter-popup');
    const filterPopupContainer = document.getElementById('filter-popup-container');
    const filterCategoryInput = document.getElementById('filter-category');
    const searchCategoryInput = document.getElementById('search-category');
    const categoryList = document.getElementById('category-list');
    const filterOwnerInput = document.getElementById('filter-owner');
    const searchOwnerInput = document.getElementById('search-owner');
    const ownerList = document.getElementById('owner-list');
    const filterIdInput = document.getElementById('filter-id');
    const searchIdInput = document.getElementById('search-id');
    const idList = document.getElementById('id-list');
    const filterForm = document.getElementById('filterForm');
    const idTagsContainer = document.getElementById('id-tags');
    const categoryTagsContainer = document.getElementById('category-tags');
    const ownerTagsContainer = document.getElementById('owner-tags');

    // Filter button functionality
    if (filterButton) {
        filterButton.addEventListener('click', function() {
            filterPopupContainer.style.display = 'flex';
        });
    }

    if (closeButton) {
        closeButton.addEventListener('click', function() {
            filterPopupContainer.style.display = 'none';
        });
    }

    window.addEventListener('click', function(event) {
        if (event.target === filterPopupContainer) {
            filterPopupContainer.style.display = 'none';
        }
    });

    // Prevent the Enter key from submitting the form
    function preventEnterSubmit(event) {
        if (event.key === 'Enter') {
            event.preventDefault();
        }
    }

    // Create tag element
    function createTag(value, container, checkbox) {
        const tag = document.createElement('div');
        tag.className = 'tag';
        tag.textContent = value;

        const removeBtn = document.createElement('span');
        removeBtn.className = 'remove-tag';
        removeBtn.textContent = ' x';
        removeBtn.addEventListener('click', () => {
            container.removeChild(tag);
            checkbox.checked = false;
            updateInputValue();
        });

        tag.appendChild(removeBtn);
        container.appendChild(tag);
    }

    // Update the input value based on selected checkboxes for IDs
    function updateInputValue() {
        const selectedIds = Array.from(idCheckboxes)
            .filter(checkbox => checkbox.checked)
            .map(checkbox => checkbox.value);
        searchIdInput.value = selectedIds.join(',');

        idTagsContainer.innerHTML = '';
        selectedIds.forEach(id => {
            const checkbox = Array.from(idCheckboxes).find(cb => cb.value === id);
            createTag(id, idTagsContainer, checkbox);
        });

        const selectedCategories = Array.from(categoryCheckboxes)
            .filter(checkbox => checkbox.checked)
            .map(checkbox => checkbox.value);
        searchCategoryInput.value = selectedCategories.join(',');

        categoryTagsContainer.innerHTML = '';
        selectedCategories.forEach(category => {
            const checkbox = Array.from(categoryCheckboxes).find(cb => cb.value === category);
            createTag(category, categoryTagsContainer, checkbox);
        });

        const selectedOwners = Array.from(ownerCheckboxes)
            .filter(checkbox => checkbox.checked)
            .map(checkbox => checkbox.value);
        searchOwnerInput.value = selectedOwners.join(',');

        ownerTagsContainer.innerHTML = '';
        selectedOwners.forEach(owner => {
            const checkbox = Array.from(ownerCheckboxes).find(cb => cb.value === owner);
            createTag(owner, ownerTagsContainer, checkbox);
        });
    }

    // Update the input value based on selected checkboxes for IDs
    const idCheckboxes = idList.querySelectorAll('input[type="checkbox"]');
    idCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', updateInputValue);
    });

    // Update the input value based on selected checkboxes for categories
    const categoryCheckboxes = categoryList.querySelectorAll('input[type="checkbox"]');
    categoryCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', updateInputValue);
    });

    // Update the input value based on selected checkboxes for owners
    const ownerCheckboxes = ownerList.querySelectorAll('input[type="checkbox"]');
    ownerCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', updateInputValue);
    });

    // Dynamic filtering function for IDs
    filterIdInput.addEventListener('input', () => {
        const filter = filterIdInput.value.toLowerCase();
        idCheckboxes.forEach(checkbox => {
            const label = checkbox.parentElement.parentElement;
            if (checkbox.value.toLowerCase().startsWith(filter)) {
                label.style.display = 'block';
            } else {
                label.style.display = 'none';
            }
        });
    });

    // Dynamic filtering function for categories
    filterCategoryInput.addEventListener('input', () => {
        const filter = filterCategoryInput.value.toLowerCase();
        categoryCheckboxes.forEach(checkbox => {
            const label = checkbox.parentElement.parentElement;
            if (checkbox.value.toLowerCase().startsWith(filter)) {
                label.style.display = 'block';
            } else {
                label.style.display = 'none';
            }
        });
    });

    // Dynamic filtering function for owners
    filterOwnerInput.addEventListener('input', () => {
        const filter = filterOwnerInput.value.toLowerCase();
        ownerCheckboxes.forEach(checkbox => {
            const label = checkbox.parentElement.parentElement;
            if (checkbox.value.toLowerCase().startsWith(filter)) {
                label.style.display = 'block';
            } else {
                label.style.display = 'none';
            }
        });
    });

    // Prevent Enter key from submitting the form
    filterIdInput.addEventListener('keydown', preventEnterSubmit);
    filterCategoryInput.addEventListener('keydown', preventEnterSubmit);
    filterOwnerInput.addEventListener('keydown', preventEnterSubmit);
    filterForm.addEventListener('keydown', preventEnterSubmit);
});
