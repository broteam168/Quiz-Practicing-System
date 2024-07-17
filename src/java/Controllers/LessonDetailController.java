/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.LessonDAO;
import DAOs.PracticeDAO;
import DAOs.QuizDAO;
import Models.Group;
import Models.Lesson;
import Models.Quiz.Quiz;
import Models.User;
import Utils.ValidationUtils;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

public class LessonDetailController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // default action value
            String action = "view";

            // get action
            if (request.getParameter("action") != null) {
                action = request.getParameter("action");
            }

            // getting lesson steps, skip if action is adding new lesson
            if (!action.equals("add")) {

                // if lesson id is null, send error
                if (!ValidationUtils.isValidInteger(request.getParameter("lesson_id"), 0, Integer.MAX_VALUE)) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }

                // if user is not logged in, send error 401
                HttpSession session = request.getSession();
                User user = (User) session.getAttribute("currentUser");
                if (user == null) {
                    response.sendError(401, "Unauthorized");
                    return;
                }

                int lesson_id = Integer.parseInt(request.getParameter("lesson_id"));
                Lesson lesson = new LessonDAO().GetLessonById(lesson_id);

                LessonDAO lessonDAO = new LessonDAO();
                if (!lessonDAO.CheckExpertAccess(user.getUser_id(), lesson.getSubject_id())) {
                    response.sendError(403, "Forbidden");
                    return;
                }

                // set value to request
                request.setAttribute("lesson", lesson);

            }

            request.setAttribute("action", action);

            // redirect to appropriate method
            switch (action) {
                case "view" ->
                    ViewLesson(request, response);
                case "edit" ->
                    EditLesson(request, response);
                case "add" ->
                    AddLesson(request, response);
                default ->
                    ViewLesson(request, response);
            }
        } catch (ServletException | IOException | NumberFormatException e) {
            System.out.println("Error in lesson details: " + e.getMessage());
            response.sendError(400, "Bad Request");
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // processRequest(request, response);

        // get action 
        String action = request.getParameter("action");
        request.setAttribute("action", action);

        // if action is null, send error
        if (action == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } else {
            LessonDAO lessonDAO = new LessonDAO();

            // if action is add
            if (action.equals("add")) {

                Lesson lesson = new Lesson();

                // get lesson_type first
                int lesson_type = Integer.parseInt(request.getParameter("lesson_type"));

                // get subject_id
                int subject_id = Integer.parseInt(request.getParameter("subject_id"));

                List<Group> topics = new PracticeDAO().getTopicsBySubjectId(subject_id);
                List<Quiz> quizzes = new QuizDAO().GetLessonQuizzesBySubjectId(subject_id);

                request.setAttribute("topics", topics);
                request.setAttribute("quizzes", quizzes);

                if (lesson_type == 1) {
                    // add topic by the name first
                    String topic_name = request.getParameter("name");

                    // check existing topic
                    if (lessonDAO.CheckExistedTopicName(topic_name, subject_id)) {
                        // set lesson values
                        lesson.setName(topic_name);
                        lesson.setOrder(Integer.parseInt(request.getParameter("order")));
                        lesson.setLesson_type(lesson_type);
                        lesson.setDescription(request.getParameter("description"));
                        lesson.setSubject_id(subject_id);
                        request.setAttribute("lesson", lesson);
                        request.setAttribute("message", "This topic is existed. Try again.");
                        AddLesson(request, response);
                        return;
                    }

                    lessonDAO.AddTopic(topic_name, subject_id);

                    // get topic_id
                    int topic_id = lessonDAO.GetNewestTopicId();

                    // set lesson values
                    lesson.setName(topic_name);
                    lesson.setOrder(Integer.parseInt(request.getParameter("order")));
                    lesson.setTopic_id(topic_id);
                    lesson.setLesson_type(lesson_type);
                    lesson.setDescription(request.getParameter("description"));
                    lesson.setSubject_id(subject_id);

                } else {
                    lesson.setName(request.getParameter("name"));
                    lesson.setOrder(Integer.parseInt(request.getParameter("order")));
                    lesson.setLesson_type(lesson_type);
                    lesson.setDescription(request.getParameter("description"));
                    lesson.setSubject_id(subject_id);
                    lesson.setTopic_id(Integer.parseInt(request.getParameter("topic_id")));
                    lesson.setStatus(Integer.parseInt(request.getParameter("status")));
                    if (lesson_type == 3) {
                        lesson.setQuiz_id(Integer.parseInt(request.getParameter("quiz_id")));
                    } else {

                        // if lesson type 2 with empty content, send error message
                        if (lesson.getLesson_type() == 2 && lesson.getContent().isEmpty()) {
                            request.setAttribute("lesson", lesson);
                            request.setAttribute("message", "Lesson content cannot be empty. Try again.");
                            EditLesson(request, response);
                            return;
                        }

                        lesson.setContent(request.getParameter("lesson-details"));
                        lesson.setBack_link(request.getParameter("back_link"));
                    }

                    // check existing lesson in same topic
                    if (lessonDAO.CheckExistedLessonName(lesson.getName(), lesson.getTopic_id())) {
                        request.setAttribute("lesson", lesson);
                        request.setAttribute("message", "This lesson is existed. Try again.");
                        AddLesson(request, response);
                        return;
                    }
                }

                // add lesson
                lessonDAO.AddLesson(lesson);

                // get lesson_id
                int lesson_id = lessonDAO.GetNewestLessonId();

                // redirect to lesson detail
                response.sendRedirect("lesson-detail?action=view&message=Added Successfully&lesson_id=" + lesson_id);
            } else if (action.equals("edit")) {
                // similar logic to add lesson, but update instead
                Lesson lesson = new Lesson();
                lesson.setLesson_id(Integer.parseInt(request.getParameter("lesson_id")));
                lesson.setName(request.getParameter("name"));
                lesson.setOrder(Integer.parseInt(request.getParameter("order")));
                lesson.setLesson_type(Integer.parseInt(request.getParameter("lesson_type")));
                lesson.setDescription(request.getParameter("description"));
                lesson.setSubject_id(Integer.parseInt(request.getParameter("subject_id")));
                lesson.setTopic_id(Integer.parseInt(request.getParameter("topic_id")));
                lesson.setStatus(Integer.parseInt(request.getParameter("status")));
                if (lesson.getLesson_type() == 3) {
                    lesson.setQuiz_id(Integer.parseInt(request.getParameter("quiz_id")));
                } else {
                    lesson.setContent(request.getParameter("lesson-details"));
                    lesson.setBack_link(request.getParameter("back_link"));

                    // if lesson type 2 with empty content, send error message
                    if (lesson.getLesson_type() == 2 && lesson.getContent().isEmpty()) {
                        request.setAttribute("lesson", lesson);
                        request.setAttribute("message", "Lesson content cannot be empty. Try again.");
                        EditLesson(request, response);
                        return;
                    }
                }

                // get old lesson
                Lesson oldLesson = lessonDAO.GetLessonById(lesson.getLesson_id());

                if (lesson.getLesson_type() == 1) {
                    String topic_name = request.getParameter("name");
                    // check existing topic
                    if (lessonDAO.CheckExistedTopicName(topic_name, lesson.getSubject_id())) {
                        // if name remain unchanged, then this is not an error
                        if (topic_name.equals(oldLesson.getName())) {
                            // do nothing
                        } else {
                            // set lesson values
                            lesson.setName(topic_name);
                            lesson.setOrder(Integer.parseInt(request.getParameter("order")));
                            lesson.setLesson_type(1);
                            lesson.setDescription(request.getParameter("description"));
                            lesson.setSubject_id(lesson.getSubject_id());
                            request.setAttribute("lesson", lesson);
                            request.setAttribute("message", "This topic is existed. Try again.");
                            EditLesson(request, response);
                            return;
                        }
                    }
                }

                // if oldLesson type is topic and new lesson type is not topic
                if (oldLesson.getLesson_type() == 1 && lesson.getLesson_type() != 1) {
                    // get all lesson that has the same topic_id
                    // if there is only 1 lesson, which is this one, delete the topic
                    int count = lessonDAO.GetLessonCountByTopicId(oldLesson.getTopic_id());

                    if (count == 1) {

                        // if lesson topic is still the same as old lesson topic send error
                        if (lesson.getTopic_id() == oldLesson.getTopic_id()) {
                            request.setAttribute("lesson", lesson);
                            request.setAttribute("message", "To change this topic to other type, please select another topic.");
                            EditLesson(request, response);
                            return;
                        }

                        lessonDAO.UpdateLesson(lesson);
                        boolean deleted = lessonDAO.DeleteTopic(oldLesson.getTopic_id());

                        if (!deleted) {
                            request.setAttribute("lesson", lesson);
                            request.setAttribute("message", "This lesson is referenced by other question(s). Cannot change to other type.");
                            EditLesson(request, response);
                            return;
                        }
                    } // else, set error message and redirect to edit page
                    else {
                        request.setAttribute("lesson", lesson);
                        request.setAttribute("message", "This lesson is referenced by " + (count - 1) + " other lesson(s). Cannot change to other type.");
                        EditLesson(request, response);
                        return;
                    }
                }

                // if oldlesson type is not topic and new lesson type is topic
                if (oldLesson.getLesson_type() != 1 && lesson.getLesson_type() == 1) {
                    // add topic by the name first
                    String topic_name = request.getParameter("name");

                    // check existing topic
                    if (lessonDAO.CheckExistedTopicName(topic_name, lesson.getSubject_id())) {
                        // set lesson values
                        lesson.setName(topic_name);
                        lesson.setOrder(Integer.parseInt(request.getParameter("order")));
                        lesson.setLesson_type(1);
                        lesson.setDescription(request.getParameter("description"));
                        lesson.setSubject_id(lesson.getSubject_id());
                        request.setAttribute("lesson", lesson);
                        request.setAttribute("message", "This topic is existed. Try again.");
                        EditLesson(request, response);
                        return;
                    }

                    lessonDAO.AddTopic(topic_name, lesson.getSubject_id());

                    // get topic_id
                    int topic_id = lessonDAO.GetNewestTopicId();

                    // set lesson values
                    lesson.setName(topic_name);
                    lesson.setOrder(Integer.parseInt(request.getParameter("order")));
                    lesson.setTopic_id(topic_id);
                    lesson.setLesson_type(1);
                    lesson.setDescription(request.getParameter("description"));
                    lesson.setSubject_id(lesson.getSubject_id());
                }

                // check existed lesson 
                if (lessonDAO.CheckExistedLessonName(lesson.getName(), lesson.getLesson_id(), lesson.getTopic_id())) {
                    request.setAttribute("lesson", lesson);
                    request.setAttribute("message", "This lesson is existed. Try again.");
                    EditLesson(request, response);
                    return;
                }

                lessonDAO.UpdateLesson(lesson);

                // update topic if lesson type = 1
                if (lesson.getLesson_type() == 1) {
                    lessonDAO.UpdateTopic(lesson.getTopic_id(), lesson.getName());
                }

                // redirect to lesson detail
                response.sendRedirect("lesson-detail?action=view&message=Updated Successfully&lesson_id=" + lesson.getLesson_id());
            }
        }
    }

    private void ViewLesson(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Lesson lesson = (Lesson) request.getAttribute("lesson");
        List<Group> topics = new PracticeDAO().getTopicsBySubjectId(lesson.getSubject_id());
        List<Quiz> quizzes = new QuizDAO().GetLessonQuizzesBySubjectId(lesson.getSubject_id());

        request.setAttribute("topics", topics);
        request.setAttribute("quizzes", quizzes);

        request.getRequestDispatcher("./Views/CourseContent/LessonDetailView.jsp").forward(request, response);
    }

    private void EditLesson(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Lesson lesson = (Lesson) request.getAttribute("lesson");
        List<Group> topics = new PracticeDAO().getTopicsBySubjectId(lesson.getSubject_id());
        List<Quiz> quizzes = new QuizDAO().GetLessonQuizzesBySubjectId(lesson.getSubject_id());

        request.setAttribute("topics", topics);
        request.setAttribute("quizzes", quizzes);

        request.getRequestDispatcher("./Views/CourseContent/LessonDetailView.jsp").forward(request, response);
    }

    private void AddLesson(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int subject_id = Integer.parseInt(request.getParameter("subject_id"));
        List<Group> topics = new PracticeDAO().getTopicsBySubjectId(subject_id);
        List<Quiz> quizzes = new QuizDAO().GetLessonQuizzesBySubjectId(subject_id);

        request.setAttribute("topics", topics);
        request.setAttribute("quizzes", quizzes);
        request.getRequestDispatcher("./Views/CourseContent/LessonDetailView.jsp").forward(request, response);
    }

}
