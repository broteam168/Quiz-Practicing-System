
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="./Assets/Styles/Expert/Popup.css"/>

    </head>
    <body>
        <div class="popup" id="popup">
            <div class="filter">
                <div class="filter__header">
                    <div class="filter__header-title">Filter questions
                    </div>
                    <div class="filter__header-close" id="clode_popup">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="28" viewBox="0 0 24 28" fill="none">
                        <path d="M18 20.8127L12 13.9044M12 13.9044L6 6.99609M12 13.9044L18 6.99609M12 13.9044L6 20.8128" stroke="#1D2026" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    </div>
                </div>
                <form action="question" method="GET"><div class="filter__subject">
                        <div class="filter__subject-label">Subject</div>
                        <div class="filter__subject-input">
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="19" viewBox="0 0 20 19" fill="none">
                            <path fill-rule="evenodd" clip-rule="evenodd" d="M8.89605 1.90596e-08C7.47736 0.000114835 6.07928 0.322519 4.81844 0.940314C3.5576 1.55811 2.47056 2.45338 1.64802 3.55143C0.825473 4.64949 0.291278 5.91848 0.0899992 7.25255C-0.111279 8.58661 0.0261946 9.94706 0.490952 11.2204C0.955709 12.4937 1.73427 13.643 2.76169 14.5723C3.7891 15.5017 5.03557 16.1842 6.3971 16.5628C7.75864 16.9415 9.19575 17.0053 10.5886 16.7491C11.9813 16.4928 13.2894 15.9239 14.4037 15.0897L18.2262 18.7209C18.4236 18.902 18.688 19.0022 18.9624 19C19.2368 18.9977 19.4994 18.8931 19.6934 18.7088C19.8875 18.5244 19.9976 18.275 20 18.0143C20.0023 17.7536 19.8968 17.5025 19.7062 17.3149L15.8837 13.6837C16.9178 12.4375 17.5617 10.9398 17.7417 9.36216C17.9216 7.78451 17.6304 6.1906 16.9013 4.76284C16.1722 3.33508 15.0347 2.13116 13.619 1.28886C12.2033 0.446551 10.5665 -0.000106534 8.89605 1.90596e-08ZM2.09261 8.45164C2.09261 6.73754 2.8094 5.09365 4.08529 3.8816C5.36118 2.66955 7.09166 1.98862 8.89605 1.98862C10.7004 1.98862 12.4309 2.66955 13.7068 3.8816C14.9827 5.09365 15.6995 6.73754 15.6995 8.45164C15.6995 10.1657 14.9827 11.8096 13.7068 13.0217C12.4309 14.2337 10.7004 14.9147 8.89605 14.9147C7.09166 14.9147 5.36118 14.2337 4.08529 13.0217C2.8094 11.8096 2.09261 10.1657 2.09261 8.45164Z" fill="#280559"/>
                            </svg><input placeholder="Enter subject name" name="subject_name" value="${param.subject_name}"></div>       
                    </div>
                    <div class="filter__email">
                        <div class="filter__subject-label">Lesson</div>
                        <div class="filter__subject-input">
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="19" viewBox="0 0 20 19" fill="none">
                            <path fill-rule="evenodd" clip-rule="evenodd" d="M8.89605 1.90596e-08C7.47736 0.000114835 6.07928 0.322519 4.81844 0.940314C3.5576 1.55811 2.47056 2.45338 1.64802 3.55143C0.825473 4.64949 0.291278 5.91848 0.0899992 7.25255C-0.111279 8.58661 0.0261946 9.94706 0.490952 11.2204C0.955709 12.4937 1.73427 13.643 2.76169 14.5723C3.7891 15.5017 5.03557 16.1842 6.3971 16.5628C7.75864 16.9415 9.19575 17.0053 10.5886 16.7491C11.9813 16.4928 13.2894 15.9239 14.4037 15.0897L18.2262 18.7209C18.4236 18.902 18.688 19.0022 18.9624 19C19.2368 18.9977 19.4994 18.8931 19.6934 18.7088C19.8875 18.5244 19.9976 18.275 20 18.0143C20.0023 17.7536 19.8968 17.5025 19.7062 17.3149L15.8837 13.6837C16.9178 12.4375 17.5617 10.9398 17.7417 9.36216C17.9216 7.78451 17.6304 6.1906 16.9013 4.76284C16.1722 3.33508 15.0347 2.13116 13.619 1.28886C12.2033 0.446551 10.5665 -0.000106534 8.89605 1.90596e-08ZM2.09261 8.45164C2.09261 6.73754 2.8094 5.09365 4.08529 3.8816C5.36118 2.66955 7.09166 1.98862 8.89605 1.98862C10.7004 1.98862 12.4309 2.66955 13.7068 3.8816C14.9827 5.09365 15.6995 6.73754 15.6995 8.45164C15.6995 10.1657 14.9827 11.8096 13.7068 13.0217C12.4309 14.2337 10.7004 14.9147 8.89605 14.9147C7.09166 14.9147 5.36118 14.2337 4.08529 13.0217C2.8094 11.8096 2.09261 10.1657 2.09261 8.45164Z" fill="#280559"/>
                            </svg><input name="subject_lesson" placeholder="Enter subject lesson" value="${param.subject_lesson}"></div>       
                    </div>
                    <div class="filter__email">
                        <div class="filter__subject-label">Dimension</div>
                        <div class="filter__subject-input">
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="19" viewBox="0 0 20 19" fill="none">
                            <path fill-rule="evenodd" clip-rule="evenodd" d="M8.89605 1.90596e-08C7.47736 0.000114835 6.07928 0.322519 4.81844 0.940314C3.5576 1.55811 2.47056 2.45338 1.64802 3.55143C0.825473 4.64949 0.291278 5.91848 0.0899992 7.25255C-0.111279 8.58661 0.0261946 9.94706 0.490952 11.2204C0.955709 12.4937 1.73427 13.643 2.76169 14.5723C3.7891 15.5017 5.03557 16.1842 6.3971 16.5628C7.75864 16.9415 9.19575 17.0053 10.5886 16.7491C11.9813 16.4928 13.2894 15.9239 14.4037 15.0897L18.2262 18.7209C18.4236 18.902 18.688 19.0022 18.9624 19C19.2368 18.9977 19.4994 18.8931 19.6934 18.7088C19.8875 18.5244 19.9976 18.275 20 18.0143C20.0023 17.7536 19.8968 17.5025 19.7062 17.3149L15.8837 13.6837C16.9178 12.4375 17.5617 10.9398 17.7417 9.36216C17.9216 7.78451 17.6304 6.1906 16.9013 4.76284C16.1722 3.33508 15.0347 2.13116 13.619 1.28886C12.2033 0.446551 10.5665 -0.000106534 8.89605 1.90596e-08ZM2.09261 8.45164C2.09261 6.73754 2.8094 5.09365 4.08529 3.8816C5.36118 2.66955 7.09166 1.98862 8.89605 1.98862C10.7004 1.98862 12.4309 2.66955 13.7068 3.8816C14.9827 5.09365 15.6995 6.73754 15.6995 8.45164C15.6995 10.1657 14.9827 11.8096 13.7068 13.0217C12.4309 14.2337 10.7004 14.9147 8.89605 14.9147C7.09166 14.9147 5.36118 14.2337 4.08529 13.0217C2.8094 11.8096 2.09261 10.1657 2.09261 8.45164Z" fill="#280559"/>
                            </svg><input name="subject_dimension" placeholder="Enter subject dimension" value="${param.subject_dimension}"></div>       
                    </div>
                    <div class="filter__email">
                        <div class="filter__subject-label">Level</div>
                        <div class="filter__subject-input">
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="19" viewBox="0 0 20 19" fill="none">
                            <path fill-rule="evenodd" clip-rule="evenodd" d="M8.89605 1.90596e-08C7.47736 0.000114835 6.07928 0.322519 4.81844 0.940314C3.5576 1.55811 2.47056 2.45338 1.64802 3.55143C0.825473 4.64949 0.291278 5.91848 0.0899992 7.25255C-0.111279 8.58661 0.0261946 9.94706 0.490952 11.2204C0.955709 12.4937 1.73427 13.643 2.76169 14.5723C3.7891 15.5017 5.03557 16.1842 6.3971 16.5628C7.75864 16.9415 9.19575 17.0053 10.5886 16.7491C11.9813 16.4928 13.2894 15.9239 14.4037 15.0897L18.2262 18.7209C18.4236 18.902 18.688 19.0022 18.9624 19C19.2368 18.9977 19.4994 18.8931 19.6934 18.7088C19.8875 18.5244 19.9976 18.275 20 18.0143C20.0023 17.7536 19.8968 17.5025 19.7062 17.3149L15.8837 13.6837C16.9178 12.4375 17.5617 10.9398 17.7417 9.36216C17.9216 7.78451 17.6304 6.1906 16.9013 4.76284C16.1722 3.33508 15.0347 2.13116 13.619 1.28886C12.2033 0.446551 10.5665 -0.000106534 8.89605 1.90596e-08ZM2.09261 8.45164C2.09261 6.73754 2.8094 5.09365 4.08529 3.8816C5.36118 2.66955 7.09166 1.98862 8.89605 1.98862C10.7004 1.98862 12.4309 2.66955 13.7068 3.8816C14.9827 5.09365 15.6995 6.73754 15.6995 8.45164C15.6995 10.1657 14.9827 11.8096 13.7068 13.0217C12.4309 14.2337 10.7004 14.9147 8.89605 14.9147C7.09166 14.9147 5.36118 14.2337 4.08529 13.0217C2.8094 11.8096 2.09261 10.1657 2.09261 8.45164Z" fill="#280559"/>
                            </svg><input name="subject_level" placeholder="Enter question level" value="${param.subject_level}"></div>       
                    </div>
                    <input type="hidden" value="${param.content}" name="content"/>
                    <div class="filter__status">
                        <div class="filter__status-label">
                            Status
                        </div>
                        <div class="filter__status-select">
                            <select name="status">
                                <c:if test="${param.status == 0}"><option selected value=0>Draft</option></c:if>
                                <c:if test="${param.status != 0}"><option value=0>Draft</option></c:if>
                                <c:if test="${param.status == 1}"><option selected value=1>Published</option></c:if>
                                <c:if test="${param.status != 1}"><option value=1>Published</option></c:if>
                                <c:if test="${param.status == 2}"><option selected value=2>Unpublished</option></c:if>
                                <c:if test="${param.status != 2}"><option value=2>Unpublished</option></c:if>
                                <c:if test="${param.status == 3 || param.status == null}"><option selected value=3>All status</option></c:if>
                                <c:if test="${param.status != 3 && param.status != null}"><option value=3>All status</option></c:if>

                            </select>
                        </div>
                    </div>
                    <button class="filter__submit" type="submit">
                        <svg xmlns="http://www.w3.org/2000/svg" width="21" height="21" viewBox="0 0 21 21" fill="none">
                        <path d="M10.1932 13.6104C10.3291 13.6104 10.4565 13.5893 10.5754 13.5472C10.6943 13.5058 10.8048 13.4349 10.9067 13.3347L13.5569 10.7279C13.7438 10.5441 13.8372 10.3101 13.8372 10.0261C13.8372 9.74199 13.7438 9.50805 13.5569 9.32424C13.37 9.14043 13.1322 9.04852 12.8434 9.04852C12.5546 9.04852 12.3167 9.14043 12.1299 9.32424L11.2125 10.2266V7.01824C11.2125 6.73417 11.115 6.49589 10.9199 6.30339C10.7242 6.11155 10.482 6.01564 10.1932 6.01564C9.90436 6.01564 9.66244 6.11155 9.46741 6.30339C9.2717 6.49589 9.17385 6.73417 9.17385 7.01824V10.2266L8.25646 9.32424C8.06959 9.14043 7.83175 9.04852 7.54294 9.04852C7.25414 9.04852 7.01629 9.14043 6.82942 9.32424C6.64255 9.50805 6.54911 9.74199 6.54911 10.0261C6.54911 10.3101 6.64255 10.5441 6.82942 10.7279L9.47964 13.3347C9.58157 13.4349 9.692 13.5058 9.81092 13.5472C9.92984 13.5893 10.0573 13.6104 10.1932 13.6104ZM10.1932 20.0521C8.78311 20.0521 7.458 19.7888 6.21783 19.2621C4.97766 18.736 3.89889 18.0219 2.9815 17.1195C2.06412 16.2172 1.33802 15.1561 0.803221 13.9362C0.267741 12.7164 0 11.413 0 10.0261C0 8.63912 0.267741 7.33574 0.803221 6.1159C1.33802 4.89606 2.06412 3.83497 2.9815 2.93262C3.89889 2.03028 4.97766 1.31575 6.21783 0.789051C7.458 0.263017 8.78311 0 10.1932 0C11.6032 0 12.9283 0.263017 14.1685 0.789051C15.4087 1.31575 16.4874 2.03028 17.4048 2.93262C18.3222 3.83497 19.0483 4.89606 19.5831 6.1159C20.1186 7.33574 20.3863 8.63912 20.3863 10.0261C20.3863 11.413 20.1186 12.7164 19.5831 13.9362C19.0483 15.1561 18.3222 16.2172 17.4048 17.1195C16.4874 18.0219 15.4087 18.736 14.1685 19.2621C12.9283 19.7888 11.6032 20.0521 10.1932 20.0521ZM10.1932 18.0469C12.4526 18.0469 14.3768 17.2659 15.9656 15.7038C17.5536 14.1411 18.3477 12.2485 18.3477 10.0261C18.3477 7.80362 17.5536 5.91103 15.9656 4.3483C14.3768 2.78624 12.4526 2.00521 10.1932 2.00521C7.93368 2.00521 6.00989 2.78624 4.42179 4.3483C2.83302 5.91103 2.03863 7.80362 2.03863 10.0261C2.03863 12.2485 2.83302 14.1411 4.42179 15.7038C6.00989 17.2659 7.93368 18.0469 10.1932 18.0469Z" fill="#280559"/>
                        </svg>
                        <input type="submit" >
                    </button>
                        <form>
                            <a href="question"><h5>Clear filter</h5></a>
                        </form> 
                         </div>
                        </form>
                        
                       


                        </div>  
                        </div>
                        </body>
                        </html>
