<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quiz List</title>
        <link rel="stylesheet" href="./Assets/Styles/TestContent/QuizList.css">
    </head>
    <body>
        <c:set scope="request" var="currentPage" value="Quiz List"/>
        <jsp:include page="../Common/SideBarView.jsp"></jsp:include>
            <div class="content">
                <div class="header">
                    <div>
                        <h1>Quizzes List</h1>
                        <p>View and manage quizzes</p>
                    </div>
                </div>
                <div class="tab-container">
                    <div class="main__navigation">
                        <div class="main__navigation-item main__navigation-select" id="allQuizzes">
                            <div class="menu-select"></div>
                            All Quizzes
                        </div>
                        <div class="main__navigation-item" id="noTestTaken">
                            <div class="menu-select"></div>
                            No test taken
                        </div>
                        <div class="main__navigation-item" id="testTaken">
                            <div class="menu-select"></div>
                            Test Taken
                        </div>
                    </div>
                </div>
                <div class="main-content">
                    <div class="filters">
                        <div class="search-label">
                            <h2>Quizzes</h2>
                        </div>
                        <div class="actions new-quiz-container">
                            <button class="new-quiz-btn" onclick="window.location.href = 'newQuiz.jsp'">New Quiz</button>
                        </div>
                    </div>
                    <div class="filters search-bar">
                        <div class="search">
                            <input type="text" placeholder="Search for quiz name here" id="search" name="search" value="${search}">
                    </div>
                    <div class="actions">
                        <c:if test="${!empty search}">
                            <button class="clear-btn" id="clearSearchBtn">Clear Search</button>
                        </c:if>
                        <c:if test="${!empty subjects || !empty quizTypes || !empty quizIds}">
                            <button class="clear-btn" id="clearFilterBtn">Clear Filter</button>
                        </c:if>                        
                        <button class="search-btn" id="searchBtn">Search</button>
                        <button class="filters-btn" id="openFilterModal">Filters</button>
                    </div>
                </div>
                <div class="quiz-table">
                    <table>
                        <thead>
                            <tr>
                                <th class="sortable" id="idHeader">ID <span class="sort-icons">▲▼</span></th>
                                <th>Name</th>
                                <th>Subject</th>
                                <th class="sortable" id="levelHeader">Level <span class="sort-icons">▲▼</span></th>
                                <th class="sortable" id="numQuestionsHeader">Number of Questions <span class="sort-icons">▲▼</span></th>
                                <th class="sortable" id="durationHeader">Duration <span class="sort-icons">▲▼</span></th>
                                <th class="sortable" id="passRateHeader">Pass Rate <span class="sort-icons">▲▼</span></th>
                                <th>Quiz Type</th>
                                <th>Option</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${quizzes == null || quizzes.size() == 0}">
                                <tr>
                                    <td colspan="9">No quizzes found.</td>
                                </tr>
                            </c:if>
                            <c:forEach var="quiz" items="${quizzes}">
                                <tr class="quiz-row" data-title="${quiz.name}">
                                    <td>${quiz.quizId}</td>
                                    <td>${quiz.name}</td>
                                    <td>${quiz.subjectName}</td>
                                    <td>${quiz.level}</td>
                                    <td>${quiz.numQuestions}</td>
                                    <td>${quiz.duration} mins</td>
                                    <td>${quiz.passRate}%</td>
                                    <td>${quiz.quizType}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${quiz.testCount > 0}">
                                                <span>Test Taken</span>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="quizDetails.jsp?quizId=${quiz.quizId}">Edit</a>
                                                <a href="#" class="delete-btn" data-id="${quiz.quizId}">Delete</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="pagination">
                    <div class="info">
                        <p>${page * PAGE_LENGTH - PAGE_LENGTH + 1} - ${page * PAGE_LENGTH} of ${resultLength}</p>
                    </div>
                    <div class="actions">
                        <p>The page you're on</p>
                        <select id="pageSelect">
                            <c:forEach begin="1" end="${pagesNumber}" var="i">
                                <option value="${i}" ${i == page ? 'selected' : ''}>${i}</option>
                            </c:forEach>
                        </select>
                        <button id="prevBtn" ${page == 1 ? 'disabled' : ''}>&lt;</button>
                        <button id="nextBtn" ${page == pagesNumber ? 'disabled' : ''}>&gt;</button>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="/Views/TestContent/QuizListFilterPopup.jsp"></jsp:include>
            <form id="mainFilterForm" action="quizlist" method="get" hidden>
                <input type="hidden" name="statusFilter" id="statusFilter" value="${statusFilter}">
            <input type="hidden" name="search" id="searchTerm" value="${search}">
            <input type="hidden" name="page" id="page" value="${page}">
            <input type="hidden" name="sortOrder" id="sortOrder" value="${sortOrder}">
            <input type="hidden" name="sortField" id="sortField" value="${sortField}">
            <input type="hidden" name="subjects" id="subjects" value="${subjectFilter}">
            <input type="hidden" name="quizTypes" id="quizTypes" value="${quizTypeFilter}">
            <input type="hidden" name="quizIds" id="quizIds" value="${quizIdFilter}">
        </form>


        <!-- Modal HTML -->
        <div id="deleteConfirmationModal" class="delete-popup-container">
            <div class="delete-popup">
                <div class="delete-popup__header">
                    <span class="delete-popup__header-title">Confirm Deletion</span>
                    <span class="delete-popup__header-close" id="closePopup">&times;</span>
                </div>
                <div class="delete-popup__content">
                    <p>Are you sure you want to delete this quiz?</p>
                    <form method="post" action="quizlist" id="deleteForm">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="quizId" id="deleteQuizId">
                        <div class="delete-popup__actions">
                            <button type="submit" class="confirm-btn">Yes</button>
                            <button type="button" class="cancel-btn" id="cancelDelete">No</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <script src="./Assets/Js/QuizList.js"></script>
        <script src="./Assets/Js/QuizListEditPopup.js"></script>
    </body>
</html>
