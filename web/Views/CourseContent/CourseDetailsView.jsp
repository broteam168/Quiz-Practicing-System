<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Subject Details</title>
        <link rel="icon" href="./Assets/Images/Common/logo.ico">
        <link rel="stylesheet" href="./Assets/Styles/CourseContent/CourseDetails.css">
        <script src="./Assets/Js/Common/jquery.min.js"></script>
    </head>

    <body>
        <c:set scope="request" var="currentPage" value="Subject List"></c:set>
        <jsp:include page="../Common/SideBarView.jsp"></jsp:include> 
            <div class="main">
                <div class=" course-details">
                    <div class="course-details-header">
                        <div class="course-details-title">
                            <h1>Subject Details</h1>
                        </div>
                    <c:if test="${requestScope.action == 'view'}">
                        <button class="course-details-button" onclick="window.location.href = 'lessons?subject_id=${subject_id}'">View Lessons</button>
                    </c:if>
                    <button class="course-details-button-back" onclick="window.location.href = 'subjectlistedit'">Go Back</button>
                </div>
                <div class="course-details-tab">
                    <button class="course-details-tab-tablinks" onclick="openTab(event, 'Overview')"><bold>Overview</bold></button>
                    <button class="course-details-tab-tablinks" onclick="openTab(event, 'PricePackage')">Price Package</button>
                    <button class="course-details-tab-tablinks" onclick="openTab(event, 'Dimension')">Dimension</button>
                </div>
                <div id="Overview" class="course-details-tab-content">
                    <form id="courseDetails" action="course-details" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="act" value="save">
                        <div class="form-part-1">
                            <div class="form-part-1-left">
                                <input type="hidden" id="subjectId" name="subjectId" value="${subject_id}">
                                <input type="hidden" id="subjectThumbnail" name="subjectThumbnail" value="${currentSubject.getThumbnail()}">
                                <label for="subjectName">Subject Name:</label>
                                <input required pattern='.*[a-zA-Z]+.*' maxlength='50' class="course-input" type="text" id="subjectName" name="subjectName" placeholder="Write subject name here..." value="${currentSubject.getTitle()}"><br><br>
                                <label for="category">Category:</label>
                                <select required id="category" name="category">
                                    <c:if test="${action == 'view'}">
                                        <option value="${currentSubject.getCategoryName()}" selected>${currentSubject.getCategoryName()}</option>
                                    </c:if>
                                    <c:if test="${action == 'create'}">
                                        <option value="" disabled selected>Choose category for subject</option>
                                    </c:if>
                                    <c:forEach var="category" items="${categories}">
                                        <option value="${category}">${category}</option>
                                    </c:forEach>
                                </select><br><br>
                                <div class="sub-part">
                                    <div class="featured-part">
                                        <label for="featured">Featured:</label>
                                        <br>
                                        <span>Featured Subject</span>
                                        <c:if test="${currentUser.getRole() == 5}">
                                            <label class="switch">
                                                <input id ="feature" type="checkbox" name="feature" value="true" <c:if test="${currentSubject.isIs_featured()}">checked</c:if>>
                                                    <span class="slider"></span>
                                                </label>
                                        </c:if>
                                        <c:if test="${currentUser.getRole() == 4}">
                                            <label class="switch expert">
                                                <input disabled="" type="checkbox" name="feature" value="true" <c:if test="${currentSubject.isIs_featured()}">checked</c:if>>
                                                    <span class="slider expert"></span>
                                                </label>
                                        </c:if>
                                    </div>
                                    <div>
                                        <c:if test="${currentUser.getRole() == 5}">
                                            <label for="status">Status:</label>
                                            <br>
                                            <select required class="status" id="status" name="status">
                                                <c:if test="${action == 'view'}">
                                                    <option value="${currentSubject.getStatusName(currentSubject.getStatus())}">${currentSubject.getStatusName(currentSubject.getStatus())}</option>
                                                    <option value="${otherStatus}">${otherStatus}</option>
                                                </c:if>
                                                <c:if test="${action == 'create'}">
                                                    <option value="" disabled selected>Choose status for subject</option>
                                                    <c:forEach var="status" items="${statusList}">
                                                        <option value="${status}">${status}</option>
                                                    </c:forEach>
                                                </c:if>
                                            </select>
                                        </c:if>
                                        <c:if test="${currentUser.getRole() == 4}">
                                            <label for="status">Status:</label>
                                            <br>
                                            <input class="expert status" name="status" type="input" value="${currentSubject.getStatusName(currentSubject.getStatus())}" readonly="">
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                            <div class="form-part-1-right">
                                <label for="thumbnail">Thumbnail:</label>
                                <div class="course-details-thumbnail">
                                    <c:if test="${action == 'view'}">
                                        <img id="thumbnailPreview" src="${currentSubject.getThumbnail()}" alt="Avatar"/>
                                    </c:if>
                                    <c:if test="${action == 'create'}">
                                        <img id="thumbnailPreview" src="" alt="Thumbnail"/>
                                    </c:if>
                                </div>
                                <div class="">
                                    <input type="file" id="thumbnailUpload" name="file" class="image-upload-input">
                                    <label for="thumbnailUpload" class="course-details-thumbnail-label">Choose Thumbnail</label>
                                </div>
                            </div>
                        </div>
                        <br><br>
                        <label for="description">Description:</label>
                        <br>
                        <br>
                        <textarea required maxlength="1000" id="description" name="description" placeholder="Enter description">${currentSubject.getDescription()}</textarea><br><br>
                        <label for="tagline">Tag-line</label>
                        <input required maxlength='70' class="course-input" type="text" id="tagline" name="tagline" placeholder="Write tag-line here..." value="${currentSubject.getTag_line()}"><br><br>
                        <div id="message-course"></div>
                        <c:if test="${action == 'view'}">
                            <div class="submit-part">
                                <button disabled class="submit-butt" onclick='submitCourse()'>Save changes</button>
                            </div>
                        </c:if>
                        <c:if test="${action == 'create'}">
                            <div class="submit-part">
                                <button disabled class="submit-butt" onclick='submitCourse()'>Submit</button>
                            </div>
                        </c:if>
                    </form>
                    <c:if test="${currentUser.getRole() == 5}">
                        <div class="assigned-part" id="assigned-part">
                            <label for="assigned-expert">Assigned Expert</label>
                            <br>
                            <div class="expertListContainer" id="expertListContainer">
                                <c:if test="${expertList.size()>0}">
                                    <c:forEach var="expert" items="${expertList}">
                                        <div class="expert-item">
                                            <input type="hidden" value="${expert.user_id}" name="expertId">
                                            <div class="expert-member" id="expert-${expert.user_id}">
                                                <p>${expert.full_name}</p>
                                                <br/>
                                                Email: <strong>${expert.email}</strong> &ensp;
                                                Phone: <strong>${expert.mobile}</strong> &ensp;
                                                <button class="course-details-button-back unassign" onclick='openNotificationPopup(${expert.user_id})'>Unassign</button>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </c:if>
                            </div>
                            <c:if test="${expertList.size()<1}">
                                <p>No expert assigned for this subject.</p>
                            </c:if>
                            <div id="addExpert-butt">
                                <button class="add-expert-button" onclick="showAddSelection()">Add Expert</button>
                            </div>
                            <div class="unExpert" id="unExpert">
                                <select class="addExpert" id="addExpert" name="addExpert">
                                    <option value="" disabled selected>Choose expert for subject</option>
                                    <c:forEach var="unExpert" items="${unassignedExpertList}">
                                        <option value="${unExpert.user_id}">${unExpert.full_name} - ${unExpert.email} - ${unExpert.mobile}</option>
                                    </c:forEach>
                                </select>
                                <button class="add-expert-button" onclick="assignPopup()">Assign Expert</button>
                            </div>
                        </div>
                    </c:if>
                </div>
                <div id="PricePackage" class="course-details-tab-content">
                    <h3>Price Package</h3>
                    <p>Price package content goes here...</p>
                </div>
                <div id="Dimension" class="course-details-tab-content">
                    <h3>Dimension</h3>
                    <p>Dimension content goes here...</p>
                </div>
                <div id="unassign-popup" class="unassign-popup">
                    <div class="unassign-popup-content">
                        <input type="hidden" id="unassign-id" value="">
                        <h1 id="unassign">Do you want to unassign this expert?</h1>
                        <div class="unassign-confirm">
                            <button class="course-details-button" onclick='unassignExpert()'>Yes</button>
                            <button class="course-details-button" onclick='closePopup()'>No</button>
                        </div>
                    </div>
                </div>
                <div id="assign-popup" class="unassign-popup">
                    <div class="unassign-popup-content">
                        <input type="hidden" id="assign-id" value="">
                        <h1 id="unassign">Do you want to assign this expert?</h1>
                        <div class="unassign-confirm">
                            <button class="course-details-button" onclick='assignExpert()'>Yes</button>
                            <button class="course-details-button" onclick='closePopup()'>No</button>
                        </div>
                    </div>
                </div>
                <div id="update-popup" class="unassign-popup">
                    <div class="unassign-popup-content">
                        <h1 id="unassign">Do you want to save changes?</h1>
                        <div class="unassign-confirm">
                            <button class="course-details-button" onclick='saveChanges()'>Yes</button>
                            <button class="course-details-button" onclick='closePopup()'>No</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="./Assets/Js/Common/lastestjquery.min.js"></script>
        <script src="./Assets/Js/CourseContent/CourseDetails.js"></script>
    </body>

</html>
