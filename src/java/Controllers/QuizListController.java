package Controllers;

import DAOs.QuizDAO;
import Models.Role;
import Models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class QuizListController extends HttpServlet {

    public static final int PAGE_LENGTH = 7;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        int page = parseOrDefault(request.getParameter("page"), 1);
        String search = request.getParameter("search") != null ? request.getParameter("search") : "";
        String subjectFilter = request.getParameter("subjects") != null ? request.getParameter("subjects") : "all";
        String quizTypeFilter = request.getParameter("quizTypes") != null ? request.getParameter("quizTypes") : "all";
        String sortField = request.getParameter("sortField") != null ? request.getParameter("sortField") : "quiz_id";
        String sortOrder = request.getParameter("sortOrder") != null ? request.getParameter("sortOrder") : "asc";
        String statusFilter = request.getParameter("statusFilter") != null ? request.getParameter("statusFilter") : "all";
        String quizIdFilter = request.getParameter("quizIds") != null ? request.getParameter("quizIds") : "all";

        QuizDAO dao = new QuizDAO();
        int resultLength;
        List<Map<String, Object>> quizzes;

        if (currentUser.getRole() == Role.ROLE_EXPERT) {
            resultLength = dao.getResultLength(search, subjectFilter, quizTypeFilter, statusFilter, quizIdFilter);
            quizzes = dao.getQuizzesByExpert(currentUser.getUser_id(), page, PAGE_LENGTH, search, subjectFilter, quizTypeFilter, sortField, sortOrder, statusFilter, quizIdFilter);
        } else {
            resultLength = dao.getResultLength(search, subjectFilter, quizTypeFilter, statusFilter, quizIdFilter);
            quizzes = dao.getQuizzes(page, PAGE_LENGTH, search, subjectFilter, quizTypeFilter, sortField, sortOrder, statusFilter, quizIdFilter);
        }

        int pagesNumber = (int) Math.ceil((double) resultLength / PAGE_LENGTH);

        if (page > pagesNumber) {
            page = pagesNumber;
        }
        if (page < 1) {
            page = 1;
        }

        request.setAttribute("quizzes", quizzes);
        request.setAttribute("page", page);
        request.setAttribute("pagesNumber", pagesNumber);
        request.setAttribute("resultLength", resultLength);
        request.setAttribute("search", search);
        request.setAttribute("subjectFilter", subjectFilter);
        request.setAttribute("quizTypeFilter", quizTypeFilter);
        request.setAttribute("sortField", sortField);
        request.setAttribute("sortOrder", sortOrder);
        request.setAttribute("statusFilter", statusFilter);
        request.setAttribute("quizIdFilter", quizIdFilter);

        // Add attributes for filters
        request.setAttribute("quizIds", dao.getAllQuizIds());
        request.setAttribute("subjects", dao.getAllSubjects());
        request.setAttribute("quizTypes", dao.getAllQuizTypes());

        request.getRequestDispatcher("Views/TestContent/QuizListView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            int quizId = Integer.parseInt(request.getParameter("quizId"));
            QuizDAO dao = new QuizDAO();
            dao.deleteQuiz(quizId);
        }
        doGet(request, response);
    }

    private int parseOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
