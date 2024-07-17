<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="Models.QuestionDetail" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <link rel="icon" href="./Assets/Images/Common/logo.ico">
        <link rel="stylesheet" href="./Assets/Styles/Expert/questionDetails.css"/>
        <title>Question Details</title>
        <script src="./Assets/Js/Common/jquery.min.js"  type="text/javascript" defer></script>

        <script src="./Assets/Js/Expert/mainDetails2.js"></script>

        <script>

            var jsAtt = '<%= ((QuestionDetail)request.getAttribute("question")).getDimensionId() %>';
            var jsAtt2 = '<%= ((QuestionDetail)request.getAttribute("question")).getLessonId() %>';
            var questionId = '<%= ((QuestionDetail)request.getAttribute("question")).getQuestionId() %>';
        </script>
        <c:if test="${param.scroll =='end'}">
            <script >

                var scroll = '<%=request.getParameter("scroll") %>';

            </script>
        </c:if>
    </head>
    <body>
        <c:set scope="request" var="currentPage" value="Dashboard"></c:set>
        <jsp:include page="../Common/SideBarView.jsp"></jsp:include> 

            <div class="main">
                <h1 style="color: red;margin-top: 20px;">${param.message}</h1>
            <h1 style="color: green;margin-top: 20px;">${param.message2}</h1>
            <form action="question-details" method="POST" enctype="multipart/form-data" >   
                <input type="hidden" value="${param.action}" name="action">
                <input type="hidden" value="${requestScope.question.questionId}" name="id">

                <div class="header">
                    <div class="header__title">

                        <c:if test="${param.action == 'edit'}">
                            <div class="header__title-text">Question #${requestScope.question.questionId}  
                            </div>
                        </c:if>
                        <c:if test="${param.action == 'add'}">
                            <div class="header__title-text">New Question
                            </div>
                        </c:if>
                        <div class="header__title-subtext">View and edit registration detail</div>
                    </div>
                    <div class="header__action">
                        <a href="question-details?action=add"><div class="header__action-add"><svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 14 14" fill="none">
                                <path d="M1 7H7M7 7H13M7 7V13M7 7V1" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                                </svg>Add New</div></a>
                        <button type="submit" id="btnSaveAll" class="header__action-save">
                            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 18 18" fill="none">
                            <path d="M17.71 6.29L11.71 0.29C11.6178 0.200049 11.5092 0.128743 11.39 0.0799999C11.266 0.0296169 11.1338 0.00249808 11 0H3C2.20435 0 1.44129 0.316071 0.87868 0.87868C0.316071 1.44129 0 2.20435 0 3V15C0 15.7956 0.316071 16.5587 0.87868 17.1213C1.44129 17.6839 2.20435 18 3 18H15C15.7956 18 16.5587 17.6839 17.1213 17.1213C17.6839 16.5587 18 15.7956 18 15V7C18.0008 6.86839 17.9755 6.73793 17.9258 6.61609C17.876 6.49426 17.8027 6.38344 17.71 6.29ZM6 2H10V4H6V2ZM12 16H6V13C6 12.7348 6.10536 12.4804 6.29289 12.2929C6.48043 12.1054 6.73478 12 7 12H11C11.2652 12 11.5196 12.1054 11.7071 12.2929C11.8946 12.4804 12 12.7348 12 13V16ZM16 15C16 15.2652 15.8946 15.5196 15.7071 15.7071C15.5196 15.8946 15.2652 16 15 16H14V13C14 12.2044 13.6839 11.4413 13.1213 10.8787C12.5587 10.3161 11.7956 10 11 10H7C6.20435 10 5.44129 10.3161 4.87868 10.8787C4.31607 11.4413 4 12.2044 4 13V16H3C2.73478 16 2.48043 15.8946 2.29289 15.7071C2.10536 15.5196 2 15.2652 2 15V3C2 2.73478 2.10536 2.48043 2.29289 2.29289C2.48043 2.10536 2.73478 2 3 2H4V5C4 5.26522 4.10536 5.51957 4.29289 5.70711C4.48043 5.89464 4.73478 6 5 6H11C11.2652 6 11.5196 5.89464 11.7071 5.70711C11.8946 5.51957 12 5.26522 12 5V3.41L16 7.41V15Z" fill="white"/>
                            </svg>Save Changes</button>
                        <a href="question"><div class="header__action-close"><svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20" fill="none">
                                <path d="M19 19L10 10M10 10L1 1M10 10L19.0001 1M10 10L1 19.0001" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                                </svg>Cancel</div></a>
                    </div>
                </div>
                <div class="registration">

                    <div class="registration-title">Question details</div>
                    <div class="registration-column">
                        <div class="registration-info">
                            <div class="info">
                                <div class="info__label">Subject  
                                </div>
                                <div class="info__input">

                                    <select id="subject" name="subject">
                                        <c:forEach items="${requestScope.subjects}" var="subject">
                                            <c:if test="${subject.subject_id == requestScope.question.subjectId}">
                                                <option selected value="${subject.subject_id}">${subject.title}</option>
                                            </c:if>  
                                            <c:if test="${subject.subject_id != requestScope.question.subjectId}">
                                                <option  value="${subject.subject_id}">${subject.title}</option>
                                            </c:if>  
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="info">
                                <div class="info__label">Subject Dimension</div>
                                <div class="info__input">
                                    <select id="dimension" name="dimension">

                                    </select>
                                </div>
                            </div>
                            <div class="info">
                                <div class="info__label">Lesson</div>
                                <div class="info__input">
                                    <select id="lesson"  name="lesson">

                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="registration-info">

                            <div class="info" >
                                <div class="info__label">Level</div>
                                <div class="info__input">
                                    <select id="level" name="level">
                                        <c:forEach items="${requestScope.levels}" var="level">
                                            <c:if test="${level.id == requestScope.question.level}">
                                                <option value="${level.id}" selected>${level.type}</option>
                                            </c:if>
                                            <c:if test="${level.id != requestScope.question.level}">
                                                <option value="${level.id}">${level.type}</option>
                                            </c:if>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="info" >
                                <div class="info__label">Status</div>
                                <div class="info__input">
                                    <select id="status" name="status">
                                        <c:if test="${requestScope.question.status==0}"> 
                                            <option value=0 selected>Draft</option>
                                        </c:if>
                                        <c:if test="${requestScope.question.status!=0}"> 
                                            <option value=0>Draft</option>
                                        </c:if>
                                        <c:if test="${requestScope.question.status==1}"> 
                                            <option value=1 selected>Published</option>
                                        </c:if>
                                        <c:if test="${requestScope.question.status!=1}"> 
                                            <option value=1>Published</option>
                                        </c:if>
                                        <c:if test="${requestScope.question.status==2}"> 
                                            <option value=2 selected>Unpublished</option>
                                        </c:if>
                                        <c:if test="${requestScope.question.status!=2}"> 
                                            <option value=2>Unpublished</option>
                                        </c:if>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <div class="registration-info">
                            <div class="info" style="width: 100%; padding-right: 120px">
                                <div class="info__label">Content</div>
                                <div class="info__input2" style="min-width: 100%">
                                    <textarea name="content" class="editor" required minlength="50" ${action eq "view" ? "disabled":""}>${requestScope.question.content}</textarea>
                                </div>
                            </div>
                        </div>
                        <div class="registration-info">
                            <div class="info" style="width: 100%; padding-right: 120px">
                                <div class="info__label">Explaination</div>
                                <div class="info__input2">
                                    <textarea name="Explaination" class="editor" required minlength="50" ${action eq "view" ? "disabled":""}>${requestScope.question.expaination}</textarea>
                                </div>
                            </div>
                        </div>

                        <div class="registration-info">
                            <div class="info" style="width: 100%; padding-right: 120px" >
                                <div class="info__label">Media</div>
                                <div class="info__input" style="width: 100%"  >
                                    <input id="link" name="link" value="${requestScope.question.media}">
                                    <input  accept="image/*,video/*,audio/*" id="uploadfile" name="uploadfile" type="file" style="display: none">
                                </div>
                            </div>
                        </div>
                        <p id="statusMessage">Enter link or upload file to continue (Accept video, audio, image)</p>
                        <div class="registration-info">
                            <button type="button" id="btnUpload" class="header__action-save">
                                Upload Media</button>

                            <button type="button" id="btnPreview" class="header__action-save">
                                Preview</button>   
                        </div>
                        <div id="preview"></div>
                    </div>
                </div>
            </form>
            <c:if test="${param.action == 'edit'}">
                <div class="customer" style="height: fit-content; padding-bottom: 40px">
                    <div id="btnAddAnswer" style="display:flex; align-items: center;justify-content: space-between;margin-right: 100px">
                        <div class="registration-title" style="margin:auto 0 ;">Answer details</div>
                        <div class="header__action-add"><svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 14 14" fill="none">
                            <path d="M1 7H7M7 7H13M7 7V13M7 7V1" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                            </svg>Add New</div>
                    </div>
                    <div class="registration-column" style="margin-top: 40px;">
                        <c:forEach items="${requestScope.answers}" var="answer" varStatus="loop">
                            <div class="registration-info">
                                <div class="info" style="width: 100%">
                                    <div class="info__label" style="display: flex;align-items: center;gap: 5px;">
                                        Answer ${loop.index+1}
                                        <c:if test="${answer.is_correct  == true}">
                                            <div class="ovarall__title-status">
                                                Correct
                                            </div>
                                        </c:if>
                                    </div>
                                    <div class="answer">
                                        <form action='question-details' method="POST" style="display: flex;flex-grow: 1; gap:20px;">
                                            <div class="info__input22">
                                                <input name="content" value="${answer.content}"/>
                                            </div>
                                            <input name="answerId" type="hidden" value="${answer.answerId}">
                                            <c:if test="${answer.is_correct  == true}">
                                                <input name="correct" type="hidden" value="Correct">

                                            </c:if> 
                                            <input type="hidden" value="saveAnswer" name="action">
                                            <input type="hidden" value="${requestScope.question.questionId}" name="qeustionId">
                                            <button class="btnEdit">Save</button>
                                        </form>
                                        <form class="deleteForm" action='question-details' method="POST">
                                            <input type="hidden" value="deleteAnswer" name="action">
                                            <input type="hidden" value="${requestScope.question.questionId}" name="qeustionId">
                                            <input name="answerId" type="hidden" value="${answer.answerId}">
                                            <button class="btnDelete">Delete</button>
                                        </form>
                                        <c:if test="${answer.is_correct  == true}">
                                            <form  action='question-details' method="POST">
                                                <input type="hidden" value="markAnswer" name="action">
                                                <input type="hidden" value="${requestScope.question.questionId}" name="qeustionId">
                                                <input name="answerId" type="hidden" value="${answer.answerId}">
                                                <input type="hidden" name="status" value="0">
                                                <button class="btnMark">Mark as incorrect</button>
                                            </form>
                                        </c:if>
                                        <c:if test="${answer.is_correct  != true}">
                                            <form  action='question-details' method="POST">
                                                <input type="hidden" value="markAnswer" name="action">
                                                <input type="hidden" value="${requestScope.question.questionId}" name="qeustionId">
                                                <input name="answerId" type="hidden" value="${answer.answerId}">
                                                <input  type="hidden" name="status" value="1">
                                                <button class="btnMark">Mark as correct</button>
                                            </form>
                                        </c:if>
                                    </div>                        
                                </div>
                            </div>
                        </c:forEach>
                        <div class="registration-info" id="addContent" style="display: none;">
                            <div class="info" style="width: 100%">
                                <form action='question-details' method="POST">
                                    <input type="hidden" value="addAnswer" name="action">
                                    <input type="hidden" value="${requestScope.question.questionId}" name="qeustionId">
                                    <div class="info__label" style="display: flex;align-items: center;gap: 5px;">
                                        New answer
                                    </div>
                                    <div class="answer">
                                        <div class="info__input22">
                                            <input value="${param.content}" name="content" required/>
                                        </div> 
                                        <button class="btnEdit" type="submit">Save</button>
                                        <button class="btnDelete" id="btnCancel">Cancel</button>      
                                    </div> 
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>

        <script src="./Assets/Js/Common/ckeditor/ckeditor.js"></script>
        <script>
                CKEDITOR.replace('content');
                CKEDITOR.replace('Explaination');
        </script>
    </body>
</html>
