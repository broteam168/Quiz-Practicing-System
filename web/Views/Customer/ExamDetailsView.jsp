<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Simulation Exam</title>
        <link rel="stylesheet" href="./Assets/Styles/Customer/quizhandle.css"/>
        <link rel="stylesheet" href="./Assets/Styles/Practice/PracticeList.css">
        <link rel="stylesheet" href="./Assets/Styles/main.css">
        <link rel="stylesheet" href="./Assets/Styles/Customer/simulation-exam.css">
        <link rel="icon" href="./Assets/Images/Common/logo.ico">
        <link rel="stylesheet" href="./Assets/Styles/Customer/exam-popup.css"/>
        <link rel="stylesheet" href="./Assets/Styles/Customer/exam-details.css"/>


    </head>
    <body>
        <!-- Set current page variable -->
        <c:set scope="request" var="currentPage" value="Practices"/>
        <!-- Include the header view -->
        <jsp:include page="../Common/HeaderView.jsp"></jsp:include>

            <main class="main-content">
                    <div class="back-to-simulation" onclick="redirectToSimulation()">
                        <a href="simulation-exam">
                            <svg class="back-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512"><!--!Font Awesome Free 6.5.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc.--><path fill="#ffffff" d="M9.4 233.4c-12.5 12.5-12.5 32.8 0 45.3l160 160c12.5 12.5 32.8 12.5 45.3 0s12.5-32.8 0-45.3L109.2 288 416 288c17.7 0 32-14.3 32-32s-14.3-32-32-32l-306.7 0L214.6 118.6c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0l-160 160z"/></svg>
                            <span>Back</span>
                        </a>
                    </div>
                <div class="main-content-title">
                    <p>START SIMULATION EXAM</p>
                </div>
                <div class="exam-detail">
                    <div class="exam-detail-half">
                        <p>Subject : <strong id="subject-name">${quiz.getSubjectName()}</strong></p>
                    <p>Exam : <strong id="exam-name">${quiz.getName()}</strong></p>
                    <p>Duration : <strong id="duration">${quiz.getDuration()} Mins</strong></p>
                </div>
                <div class="exam-detail-half">
                    <p>Level : <strong class="colored-text" id="level">${quiz.getLevel() == 1 ? "Easy" : quiz.getLevel() == 2 ? "Medium" : "Hard"}</strong></p>
                    <p>Question number : <strong id="num-questions">${quiz.getNumQuestions()}</strong></p>
                    <p>Pass rate : <strong class="colored-text" id="pass-rate">${quiz.getPassRate()}%</strong></p>
                </div>
            </div>
            <div class="start-exam">
                <div class="btn view-btn" onclick="showPopup()"><p>Start</p></div>
            </div>

            <hr>

            <header class="header">
                <h2>Summary of your previous attempts</h2>
                <!-- Filter and sort sections are grouped together for better vertical alignment -->
                <div class="filter-sort-area">
                    <div class="filter-by">
                        <label for="filter">Filter by:</label>
                        <select id="filter" name="filter" onchange="handleFilterChange()">
                            <option value="0" ${filter == '0' ? 'selected' : ''}>All</option>
                            <option value="1" ${filter == '1' ? 'selected' : ''}>Passed</option>
                            <option value="2" ${filter == '2' ? 'selected' : ''}>Not Passed</option>
                        </select>
                    </div>
                    <div class="sort-by">
                        <label for="sort">Sort by:</label>
                        <select id="sort" name="sort" onchange="handleSortChange()">
                            <option value="1" ${sort == '1' ? 'selected' : ''} >Recently</option>
                            <option value="2" ${sort == '2' ? 'selected' : ''} >Score</option>
                            <option value="3" ${sort == '3' ? 'selected' : ''} >Time Taken</option>
                        </select>
                    </div>
                </div>
            </header>
            <section class="exam-detail-list">
                <c:if test="${not empty recordList}">
                    <table>
                        <thead>
                            <tr>
                                <th>Started On</th>
                                <th>Score</th>
                                <th>Time Taken</th>
                                <th>Status</th>
                                <th>Review</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="record" items="${requestScope.recordList}">
                                <c:set var="is_submitted" value="${record.finished_at != null}"></c:set>
                                    <tr>
                                        <td>
                                            <div class="datetime">
                                            ${record.created_at}
                                        </div>
                                    </td>
                                    <td>
                                        <div class="correct-info correct">
                                            <div class="percentage-square">
                                                <p class="percentage" hidden>
                                                    ${ is_submitted ? record.score : 50} %
                                                </p>
                                                <p class="questions">
                                                    ${is_submitted ? record.score : "Not Submitted"}
                                                </p>
                                            </div>
                                        </div>
                                    </td>
                                    <td>
                                        ${ is_submitted ? record.getDuration() : "Not Submitted"}
                                    </td>
                                    <td>
                                        <div class="correct-info correct">
                                            <div class="percentage-square">
                                                <p class="percentage" hidden>
                                                    ${ is_submitted ? record.status * 100 : 50} %
                                                </p>
                                                <p class="questions">
                                                    ${ is_submitted ? record.status == 1 ? 'Pass' : 'Fail' : "Not Submitted"}
                                                </p>
                                            </div>
                                        </div>
                                    </td>
                                    <td class="action-column">
                                        <c:if test="${is_submitted}">
                                            <a class="btn view-btn" href="quiz-review?record_id=${record.record_id}&order=1">
                                                <p>View</p>
                                            </a>  
                                        </c:if>
                                        <c:if test="${!is_submitted}">
                                            <span class="btn view-btn invisible">
                                                <p>View</p>
                                            </span>  
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>
                <c:if test="${empty recordList}">
                    <br>
                    <h1 class="error-message">There are no attempts for this simulation exam.</h1>
                </c:if>
            </section>
            <br/><!-- comment -->
            <br/><!-- comment -->
            <div class="pagination">
                <c:if test="${page > 1}">
                    <div class="button" onclick="handlePageClick(1)">
                        <div class="arrow"><<</div>
                    </div>
                    <div class="button" onclick="handlePageClick(${page - 1})">
                        <div class="arrow"><</div>
                    </div>
                </c:if>
                <div id="pagination" class="pages" data-current-page="${page}" data-total-pages="${pages_number}">
                    <c:forEach begin="1" end="${pages_number}" var="p">
                        <div class="${page == p ? 'current-page' : 'page'}" data-page="${p}">
                            <span class="${page == p ? 'current-page-number' : 'page-number'}"
                                  onclick="handlePageClick(${p})">
                                ${p}
                            </span>
                        </div>
                    </c:forEach>
                </div>
                <c:if test="${page < pages_number}">
                    <div class="button" onclick="handlePageClick(${page + 1})">
                        <div class="arrow">></div>
                    </div>
                    <div class="button" onclick="handlePageClick(${pages_number})">
                        <div class="arrow">>></div>
                    </div>
                </c:if>
            </div>
        </main>
        <!-- Hidden form for handling searches and pagination -->
        <form id="form" action="exam-details" hidden>
            <input name="quiz_id" value="${quiz.getQuizId()}">
            <input id="filter-input" name="filter" value="${filter}">
            <input id="sort-input" name="sort" value="${sort}">
            <input id="page-input" name="page" value="${page}">
        </form>

        <div id="submit-overlay" class="submit-overlay"></div>
        <div id="submit-popup" class="submit-popup">
            <div id="popupMessage"></div>
            <div class="confirm-actions">
                <div class="start-btn btn" onclick="startExam(${quiz.getQuizId()})">Yes</div>
                <div class="start-btn btn" onclick="hidePopup()">Cancel</div>
            </div>

        </div>

        <jsp:include page="../Common/FooterView.jsp"></jsp:include>
        <script src="./Assets/Js/"></script>
        <script src="./Assets/Js/Pagination.js"></script>
        <script src="./Assets/Js/simulation-exam.js"></script>
        <script src="./Assets/Js/ScoreFormatter.js"></script> 
        <script src="./Assets/Js/FormatDateTime.js"></script> 
        <script src="./Assets/Js/ExamDetails.js"></script> 

    </body>
</html>
