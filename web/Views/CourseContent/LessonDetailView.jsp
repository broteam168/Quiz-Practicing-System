<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lesson Detail</title>
        <link rel="stylesheet" href="./Assets/Styles/Expert/LessonList.css" />
        <link rel="stylesheet" href="./Assets/Styles/Expert/LessonDetail.css" />
        <link rel="stylesheet" href="./Assets/Styles/Marketing/Dashboard.css" />

        <link rel="icon" href="./Assets/Images/Common/logo.ico">
        <script src="./Assets/Js/Common/jquery.min.js" type="text/javascript" defer></script>
        <script src="./Assets/Js/Common/dataTables.js" type="text/javascript" defer></script>
        <script src="./Assets/Js/Common/dataTables.fixedColumns.js" type="text/javascript" defer></script>
        <script src="./Assets/Js/Common/fixedColumns.dataTables.js" type="text/javascript" defer></script>

        <script src="./Assets/Js/Sales/mainList.js"></script>
        <link rel="stylesheet" href="./Assets/Styles/Sale/Popup.css" />

    </head>

    <body>
        <c:set scope="request" var="currentPage" value="Subject List"></c:set>
        <jsp:include page="../Common/SideBarView.jsp"></jsp:include>

            <div class="main">
                <form action="lesson-detail" id="detail_form" method="POST" onsubmit="validateVideoLink(event)" >
                    <input id="action-input" name="action" value="${action}" hidden />
                <input id="lesson_id-input" name="lesson_id" value="${lesson.lesson_id}" hidden />
                <input id="subject_id-input" name="subject_id" value="${lesson != null ? lesson.subject_id : param.subject_id}" hidden />
                <div class="heading">
                    <span class="lesson-details">Lesson Details</span>
                    <div class="tns">

                        <c:if test="${param.action == 'view'}">
                            <div class="buttons btn" onclick="processAction()">
                                <span class="save">
                                    Edit
                                </span>
                            </div>
                        </c:if>
                        <c:if test="${param.action == 'add'}">
                            <button id="saveButton" type="submit" onclick="validateVideoLink(event)" class="buttons btn">
                                <span class="save">
                                    Create
                                </span>
                            </button>
                        </c:if>
                        <c:if test="${param.action == 'edit'}">
                            <button id="saveButton" type="submit" onclick="validateVideoLink(event)" class="buttons btn">
                                <span class="save">
                                    Save
                                </span>
                            </button>
                        </c:if>

                        <div class="buttons-1 btn" onclick="cancelAction()"><span class="go-back" >
                                ${action eq "view" ? "Go Back" : "Cancel"}
                            </span></div>
                    </div>
                </div>
                <p class="error_message">${requestScope.message}</p>
                <p class="success_message">${param.message}</p>
                <div class="frame1">
                    <div class="left-side">
                        <div class="input-field">
                            <label for="lesson-name" class="label">Name</label>
                            <!-- pattern should be no trailing space and at least 1 char required -->
                            <input type="text" id="lesson-name" class="input-field-3" placeholder="Write lesson’s name here..."
                                   value="${lesson.name}"
                                   name="name"
                                   maxlength="120"
                                   pattern="^\S.*\S$|^\S$"
                                   required
                                   onblur="this.value = this.value.trim()"
                                   ${action eq "view" ? "disabled":""} />
                        </div>
                        <div class="category" id="topic_select">
                            <label for="topic-select" class="topic-label">Topic</label>
                            <select id="topic-select" class="select-field" name="topic_id" ${action eq "view" ? "disabled":""}>
                                <c:forEach var="topic" items="${topics}">
                                    <option value="${topic.id}" ${lesson.topic_id == topic.id ? "selected" : ""}>
                                        ${topic.name}
                                    </option>
                                </c:forEach>
                            </select>

                        </div>
                        <div class="input-field">
                            <label for="description" class="label">Summary</label>
                            <input type="text" id="description" class="input-field-3" placeholder="Write lesson’s summary here..."
                                   value="${lesson.description}"
                                   name="description"
                                   maxlength="120"
                                   pattern="^\S.*\S$|^\S$"
                                   onblur="this.value = this.value.trim()"
                                   required
                                   ${action eq "view" ? "disabled":""} />
                        </div>
                    </div>
                    <div class="right-side">

                        <div class="category-7">
                            <label for="type-select" class="type-label">Type</label>
                            <select id="type-select" name="lesson_type" class="select1" onchange="displayFields()" ${action eq "view" ? "disabled":""}>
                                <option value="2" ${lesson.lesson_type == 2 ? "selected" : ""}>Lesson</option>
                                <option value="3" ${lesson.lesson_type == 3 ? "selected" : ""}>Quiz</option>
                                <option value="1" ${lesson.lesson_type == 1 ? "selected" : ""}>Topic</option>
                            </select>
                        </div>

                        <div class="input-field-a">
                            <label for="order-input" class="order">Order</label>
                            <input type="number" id="order-input" class="input-field-b" 
                                   placeholder="Write lesson's order here..."
                                   value="${lesson.order}" name="order"
                                   required
                                   min="1"
                                   ${action eq "view" ? "disabled":""}/>
                        </div>

                        <div class="category-7">
                            <label class="type-label">Status</label>
                            <select id="status-select" name="status" class="select1" onchange="loadStatus()" ${action eq "view" ? "disabled":""}>
                                <option value="0" ${lesson.status == 0 ? "selected" : ""}><p>Inactive</p></option>
                                <option value="1" ${lesson.status == 1 ? "selected" : ""}><p>Active</p></option>
                            </select>
                        </div>

                    </div>
                </div>
                <div class="frame-d" id="video-input">
                    <div class="input-field-e">
                        <label for="video-link" class="video-link">Video Link</label>
                        <input type="url" id="video-link" class="input-field-f" placeholder="https://www.youtube.com/embed/..."
                               value="${lesson.back_link}"
                               name="back_link"
                               maxlength="99"
                               onblur="this.value = this.value.trim(); checkVideoLink()"
                               required
                               ${action eq "view" ? "disabled":""}/>


                        <!-- iframe to view the video -->
                        <iframe width="560" height="315" 
                                id="video-iframe"
                                src=""
                                title="YouTube video player"
                                frameborder="0" 
                                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                                allowfullscreen>
                        </iframe>
                        <!-- this div hide in the back of iframe -->
                        <div class="video-not-available" id="videoNotAvailable">Video not available</div>
                    </div>
                </div>
                <div class="category" id="quiz-input">
                    <label for="quiz-select" class="topic-label">Quiz</label>
                    <select id="quiz-select" class="select-field"
                            name="quiz_id"
                            onchange="loadQuizDetail()"
                            ${action eq "view" ? "disabled":""}>
                        <c:forEach var="quiz" items="${quizzes}">
                            <option value="${quiz.quizId}" 
                                    data-name="${quiz.name}" data-level="${quiz.level}" data-duration="${quiz.duration}" data-pass-rate="${quiz.passCondition}" data-question-number="${quiz.numQuestions}"
                                    ${lesson.quiz_id == quiz.quizId ? "selected" : ""}>
                                ${quiz.name}
                            </option>
                        </c:forEach>
                    </select>

                    <label class="topic-label quiz_info">Selected Quiz</label>
                    <div class="quiz_detail">
                        <p id="quiz_name">Quiz title</p>
                        <br/>
                        Level: <strong id="quiz_level">Medium</strong> &ensp;
                        Duration: <strong id="duration">45 mins</strong> &ensp;
                        Pass rate: <strong id="pass_rate">50%</strong> &ensp;
                        Question number: <strong id="question_number">15</strong>

                    </div>

                </div>
                <div class="input-field-10" id="content-input">
                    <label for="lesson-details" class="video-link content-head">Content</label>
                    <textarea name="lesson-details" class="editor" required minlength="50" ${action eq "view" ? "disabled":""}>${lesson.content}</textarea>
                </div>
                <br/>
                <br/>
            </form>
        </div>
        <div id="pre-loader" class="process-loader">
            <div class="loader"></div>
        </div>
        <script src="./Assets/Js/Common/ckeditor/ckeditor.js"></script>
        <script>
                                var editor = CKEDITOR.replace('lesson-details');
                                // onchange event
                                editor.on('change', function () {
                                    document.getElementById('saveButton').style.display = 'block';
                                });
        </script>
        <script src="./Assets/Js/Common/btnHoverEffect.js"></script>
        <script src="./Assets/Js/Expert/LessonDetail.js"></script>
    </body>

</html>