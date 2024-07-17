package Controllers;

import DAOs.QuestionDAO;
import Models.Question;
import Models.RegistrationDetails;
import Models.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

public class QuestionListController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        int role = ((User) session.getAttribute("currentUser")).getRole();
        int userId = ((User) session.getAttribute("currentUser")).getUser_id();
        System.out.println(role);
        QuestionDAO dao = new QuestionDAO();

        String action = request.getParameter("action");
        if (action != null && action.equals("show")) {
            String id = request.getParameter("question_id");
            if (id != null) {
                int idValue = Integer.parseInt(id);
                dao.UpdateStatusQuestion(idValue, 1);
            }
            response.sendRedirect("question");
        } else if (action != null && action.equals("hide")) {
            String id = request.getParameter("question_id");
            if (id != null) {
                int idValue = Integer.parseInt(id);
                dao.UpdateStatusQuestion(idValue, 2);
            }
            response.sendRedirect("question");
        } else {
            String subjectName = request.getParameter("subject_name");
            String subjectLesson = request.getParameter("subject_lesson");
            String subjectDimension = request.getParameter("subject_dimension");
            String subjectLevel = request.getParameter("subject_level");
            String status = request.getParameter("status");
            String content = request.getParameter("content");

            int statusValue = 3;

            if (subjectName == null) {
                subjectName = "";
            }
            if (subjectLesson == null) {
                subjectLesson = "";
            }
            if (subjectDimension == null) {
                subjectDimension = "";
            }
            if (subjectLevel == null) {
                subjectLevel = "";
            }
            if (content == null) {
                content = "";
            }
            if (status != null && Integer.parseInt(status) != 3) {
                statusValue = Integer.parseInt(status);
            }

            int size = dao.GetQuestionSize(role, userId, content, subjectName, subjectLesson, subjectDimension, subjectLevel, statusValue);

            request.setAttribute("size", size);
            int pageSize = 10;
            int numberOfPage = size % 10 == 0 ? size / 10 : Math.round((float) size / pageSize + 0.5f);
            request.setAttribute("numberOfPage", numberOfPage);

            ///Handle page index
            int page = 1;
            String pageRaw = request.getParameter("page");
            if (pageRaw != null) {
                int tempPage = Integer.parseInt(pageRaw);
                if (tempPage > 0 && tempPage <= numberOfPage) {
                    page = tempPage;
                }
            }
            request.setAttribute("page", page);
            List<Question> questions = dao.GetQuestionsPage(role, userId,page, pageSize, content, subjectName, subjectLesson, subjectDimension, subjectLevel, statusValue);
            request.setAttribute("page", page);
            request.setAttribute("questions", questions);

            request.getRequestDispatcher("./Views/Expert/QuestionListView.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
