<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Subject List</title>
        <link rel="stylesheet" href="./Assets/Styles/Subject/SubjectList.css">
    </head>
    <body>
        <c:set scope="request" var="currentPage" value="Subject List"/>
        <jsp:include page="../Common/SideBarView.jsp"></jsp:include>
        <div class="content">
            <div class="header">
                <div>
                    <h1>Subject List</h1>
                    <p>View and manage subject</p>
                </div>
            </div>
            <div class="tab-container">
                <div class="main__navigation">
                    <div class="main__navigation-item ${statusFilter == 'all' ? 'main__navigation-select' : ''}" id="all">
                        <div class="menu-select"></div>
                        All Subject
                    </div>
                    <div class="main__navigation-item ${statusFilter == '1' ? 'main__navigation-select' : ''}" id="published">
                        <div class="menu-select"></div>
                        Published
                    </div>
                    <div class="main__navigation-item ${statusFilter == '0' ? 'main__navigation-select' : ''}" id="unpublished">
                        <div class="menu-select"></div>
                        Unpublished
                    </div>
                </div>
            </div>
            <div class="main-content">
                <div class="filters">
                    <div class="search-label">
                        <h2>Subject</h2>
                    </div>
                    <div class="actions new-subject-container">
                        <c:if test="${!isExpert}">
                            <button class="new-subject-btn" onclick="window.location.href = 'newSubject.jsp'">New Subject</button>
                        </c:if>
                    </div>
                </div>
                <div class="filters search-bar">
                    <div class="search">
                        <input type="text" placeholder="Search for subject name here" id="search" name="search" value="${search}">
                    </div>
                    <div class="actions">
                        <c:if test="${!empty search}">
                            <button class="clear-btn" id="clearSearchBtn">Clear Search</button>
                        </c:if>
                        <c:if test="${categoryFilter != 'all' || !empty param['owner[]'] || !empty param['subjectId[]']}">
                            <button class="clear-btn" id="clearFilterBtn">Clear Filter</button>
                        </c:if>
                        <button class="search-btn" id="searchBtn">Search</button>
                        <button class="filters-btn" id="openFilterModal">Filters</button>
                    </div>
                </div>
                <div class="subject-table">
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Subject Name</th>
                                <th>Category</th>
                                <th class="sortable" id="numLessonsHeader">Number of Lessons <span class="sort-icons">▲▼</span></th>
                                <th>Owner</th>
                                <th>Status</th>
                                <th>Options</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${subjects == null || subjects.size() == 0}">
                                <tr>
                                    <td colspan="7">No subjects found.</td>
                                </tr>
                            </c:if>
                            <c:forEach var="subject" items="${subjects}">
                                <tr class="subject-row" data-title="${subject.title}" data-owner="${subject.owner}">
                                    <td>${subject.subject_id}</td>
                                    <td>${subject.title}</td>
                                    <td>${subject.category.category_name}</td>
                                    <td>${subject.numLessons}</td>
                                    <td>${subject.owner}</td>
                                    <td><span class="status ${subject.status == 1 ? 'published' : 'unpublished'}">${subject.getStatusName(subject.status)}</span></td>
                                    <td>
                                        <a href="course-details?subject_id=${subject.subject_id}&action=view">...</a>
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
            <jsp:include page="/Views/Subject/SubjectListEditPopup.jsp"></jsp:include>
            <form id="mainFilterForm" action="subjectlistedit" method="get" hidden>
                <input type="hidden" name="categoryFilter" id="categoryFilter" value="${categoryFilter}">
                <input type="hidden" name="statusFilter" id="statusFilter" value="${statusFilter}">
                <input type="hidden" name="search" id="searchTerm" value="${search}">
                <input type="hidden" name="page" id="page" value="${page}">
                <input type="hidden" name="sortOrder" id="sortOrder" value="${sortOrder}">
                <c:if test="${!isExpert}">
                    <c:forEach var="owner" items="${paramValues['owner[]']}">
                        <input type="hidden" name="owner[]" value="${owner}">
                    </c:forEach>
                </c:if>
                <c:forEach var="category" items="${paramValues['categories[]']}">
                    <input type="hidden" name="categories[]" value="${category}">
                </c:forEach>
                <c:forEach var="subjectId" items="${paramValues['subjectId[]']}">
                    <input type="hidden" name="subjectId[]" value="${subjectId}">
                </c:forEach>
            </form>

            <script src="./Assets/Js/SubjectListEdit.js"></script>
            <script src="./Assets/Js/FilterPopup.js"></script>
        </div>
    </body>
</html>
