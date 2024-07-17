/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.LessonDAO;
import DAOs.PracticeDAO;
import DAOs.SubjectDAO;
import Models.Group;
import Models.Lesson;
import Models.Subject;
import Models.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

public class LessonListController extends HttpServlet {

    private static final int PAGE_LENGTH = 10;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // default values
            int page = 1;
            int length = PAGE_LENGTH;
            int sort = 1;
            int lesson_type = 0;
            int order = 0;
            int topic = 0;
            int status = -1;
            String name = "";

            // parameters
            String raw_subject_id = request.getParameter("subject_id");
            String raw_page = request.getParameter("page");
            String raw_length = request.getParameter("length");
            String raw_sort = request.getParameter("sort");
            String raw_lesson_type = request.getParameter("lesson_type");
            String raw_order = request.getParameter("order");
            String raw_topic = request.getParameter("topic_id");
            String raw_status = request.getParameter("status");
            String raw_name = request.getParameter("name");

            // if subject_id is not provided, send error
            if (raw_subject_id == null) {
                response.sendError(400, "Bad Request");
                return;
            }

            // parse subject_id
            int subject_id = Integer.parseInt(raw_subject_id);
            // check if subject exists
            Subject subject = new SubjectDAO().getSubjectById(subject_id);
            if (subject == null) {
                response.sendError(404, "Not Found");
                return;
            }

            //TO DO: check user access to subject
            // 1. check if user is logged in
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("currentUser");
            if (user == null) {
                response.sendError(401, "Unauthorized");
                return;
            }

            // 2. check if user is expert assigned to subject or admin
            LessonDAO lessonDAO = new LessonDAO();
            if (!lessonDAO.CheckExpertAccess(user.getUser_id(), subject_id)) {
                response.sendError(403, "Forbidden");
                return;
            }

            // if other parameters are provided, parse them
            if (raw_page != null && !"".equals(raw_page)) {
                page = Integer.parseInt(raw_page);
            }
            if (raw_length != null && !"".equals(raw_length)) {
                length = Integer.parseInt(raw_length);
            }
            if (raw_sort != null && !"".equals(raw_sort)) {
                sort = Integer.parseInt(raw_sort);
            }
            if (raw_lesson_type != null && !"".equals(raw_lesson_type)) {
                lesson_type = Integer.parseInt(raw_lesson_type);
            }
            if (raw_order != null && !"".equals(raw_order)) {
                order = Integer.parseInt(raw_order);
            }
            if (raw_topic != null && !"".equals(raw_topic)) {
                topic = Integer.parseInt(raw_topic);
            }
            if (raw_status != null && !"".equals(raw_status)) {
                status = Integer.parseInt(raw_status);
            }
            if (raw_name != null && !"".equals(raw_name)) {
                name = raw_name;
                // just take 50 characters
                if (name.length() > 50) {
                    name = name.substring(0, 50);
                }
            }

            // get lessons
            List<Lesson> lessons = lessonDAO.GetLessonsList(subject_id, order, topic, lesson_type, status, name, sort, length, page);

            // get total size
            int total_size = lessonDAO.GetLessonsList(subject_id, order, topic, lesson_type, status, name, sort, Integer.MAX_VALUE, 1).size();

            // get total number of pages
            int total_pages = (int) Math.ceil((double) total_size / length);

            // set topics of subject for selecting
            List<Group> topics = new PracticeDAO().getTopicsBySubjectId(subject_id);

            // set attributes
            request.setAttribute("lessons", lessons);
            request.setAttribute("page", page);
            request.setAttribute("length", length);
            request.setAttribute("sort", sort);
            request.setAttribute("lesson_type", lesson_type);
            request.setAttribute("order", order);
            request.setAttribute("topic_id", topic);
            request.setAttribute("status", status);
            request.setAttribute("name", name);
            request.setAttribute("size", total_size);
            request.setAttribute("total_pages", total_pages);
            request.setAttribute("subject", subject);
            request.setAttribute("topics", topics);

            // forward to lesson list page
            request.getRequestDispatcher("./Views/CourseContent/LessonListView.jsp").forward(request, response);
        } catch (ServletException | IOException | NumberFormatException e) {
            System.out.println("Error in lesson list: " + e.getMessage());
            response.sendError(400, "Not Found");
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
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
