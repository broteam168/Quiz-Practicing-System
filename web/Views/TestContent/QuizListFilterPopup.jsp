<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quiz List</title>
    <link rel="stylesheet" href="./Assets/Styles/TestContent/QuizListFilterPopup.css">
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
                <form id="filterForm" class="filter-form">
                    <input type="hidden" name="filter" value="true">

                    <div class="filter-row">
                        <label for="search-id">Search ID</label>
                        <input type="text" id="search-id" placeholder="Enter quiz ID">
                        <div class="checkboxes-dropdown" id="id-list">
                            <c:forEach items="${quizIds}" var="id">
                                <div>
                                    <label>
                                        <input type="checkbox" name="quizId" value="${id}" />
                                        ${id}
                                    </label>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <div class="filter-row">
                        <label for="search-subject">Subject</label>
                        <input type="text" id="search-subject" placeholder="Enter subject name">
                        <div class="checkboxes-dropdown" id="subject-list">
                            <c:forEach items="${subjects}" var="subject">
                                <div>
                                    <label>
                                        <input type="checkbox" name="subjects" value="${subject}" />
                                        ${subject}
                                    </label>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <div class="filter-row">
                        <label for="search-quizType">Quiz Type</label>
                        <input type="text" id="search-quizType" placeholder="Enter quiz type">
                        <div class="checkboxes-dropdown" id="quizType-list">
                            <c:forEach items="${quizTypes}" var="quizType">
                                <div>
                                    <label>
                                        <input type="checkbox" name="quizTypes" value="${quizType}" />
                                        ${quizType}
                                    </label>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <button class="filter-btn" type="button">Filter</button>
                </form>
            </div>
        </div>
    </div>

    <script src="./Assets/Js/QuizListFilterPopup.js"></script>
</body>

</html>
