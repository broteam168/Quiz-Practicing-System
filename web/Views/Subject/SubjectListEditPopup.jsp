<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Subject List</title>
    <link rel="stylesheet" href="./Assets/Styles/Subject/PopupFilter.css">
</head>

<body>
    <div id="filter-popup-container" class="popup-container">
        <div class="popup">
            <div class="popup__header">
                <div class="popup__header-title">Filter</div>
                <div class="popup__header-close" id="close-filter-popup">
                    <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 48 52" fill="none">
                        <path d="M35.9999 38.9999L24 26M24 26L12 13M24 26L36.0001 13M24 26L12 39.0001" stroke="#1D2026"
                              stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                    </svg>
                </div>
            </div>
            <div class="popup__content">
                <form action="subjectlistedit" method="get" id="filterForm" class="filter-form">
                    <input type="hidden" name="filter" value="true">

                    <div class="filter-row">
                        <label for="search-id">Search ID</label>
                        <input type="hidden" id="search-id" name="search-id">
                        <input type="text" id="filter-id" placeholder="Enter subject ID">
                        <div id="id-tags" class="tags-container"></div>
                        <div class="checkboxes-dropdown" id="id-list">
                            <c:forEach items="${ids}" var="id">
                                <div>
                                    <label>
                                        <input type="checkbox" name="subjectId[]" value="${id}" />
                                        ${id}
                                    </label>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <div class="filter-row">
                        <label for="search-category">Category</label>
                        <input type="hidden" id="search-category" name="search-category">
                        <input type="text" id="filter-category" placeholder="Enter category name">
                        <div id="category-tags" class="tags-container"></div>
                        <div class="checkboxes-dropdown" id="category-list">
                            <c:forEach items="${categories}" var="category">
                                <div>
                                    <label>
                                        <input type="checkbox" name="categories[]" value="${category}" />
                                        ${category}
                                    </label>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <div class="filter-row" id="owner-filter-row" style="display: none;">
                        <label for="search-owner">Owner</label>
                        <input type="hidden" id="search-owner" name="search-owner">
                        <input type="text" id="filter-owner" placeholder="Enter owner name">
                        <div id="owner-tags" class="tags-container"></div>
                        <div class="checkboxes-dropdown" id="owner-list">
                            <c:forEach items="${owners}" var="owner">
                                <div>
                                    <label>
                                        <input type="checkbox" name="owner[]" value="${owner}" />
                                        ${owner}
                                    </label>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <button class="filter-btn" type="submit">Filter</button>
                </form>
            </div>
        </div>
    </div>

    <script>
        const isExpert = ${isExpert};
    </script>
    <script src="./Assets/Js/SubjectListEditPopup.js"></script>
</body>

</html>
