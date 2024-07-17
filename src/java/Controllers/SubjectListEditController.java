package Controllers;

import DAOs.SubjectDAO;
import Models.Role;
import Models.Subject;
import Models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubjectListEditController extends HttpServlet {

    public static final int PAGE_LENGTH = 7;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        try {
            int page = parseOrDefault(request.getParameter("page"), 1);
            String[] categoryArray = request.getParameterValues("categories[]");
            String categoryFilter = (categoryArray != null && categoryArray.length > 0) ? String.join(",", categoryArray) : "all";
            String statusFilter = request.getParameter("statusFilter") != null ? request.getParameter("statusFilter") : "all";
            String search = request.getParameter("search") != null ? request.getParameter("search") : "";
            String sortOrder = request.getParameter("sortOrder");
            String[] subjectIdArray = request.getParameterValues("subjectId[]");
            List<Integer> subjectIds = new ArrayList<>();
            if (subjectIdArray != null) {
                for (String id : subjectIdArray) {
                    try {
                        subjectIds.add(Integer.parseInt(id));
                    } catch (NumberFormatException e) {
                        // handle exception if needed
                    }
                }
            }
            List<String> owners = request.getParameterValues("owner[]") != null ? Arrays.asList(request.getParameterValues("owner[]")) : null;

            SubjectDAO subjectDAO = new SubjectDAO();
            int resultLength = subjectDAO.GetResultLength(categoryFilter, statusFilter, search, owners, subjectIds, currentUser);
            int pagesNumber = (int) Math.ceil((double) resultLength / PAGE_LENGTH);
            page = Math.min(page, pagesNumber);
            page = Math.max(page, 1);  // Ensure page is at least 1

            List<Subject> subjects = subjectDAO.getSubjectsByRole(PAGE_LENGTH, (page - 1) * PAGE_LENGTH, categoryFilter, statusFilter, search, sortOrder, owners, subjectIds, currentUser);
            List<String> allOwners = currentUser.getRole() == Role.ROLE_ADMIN ? subjectDAO.getAllOwners() : new ArrayList<>();
            List<String> allCategories = subjectDAO.getAllCategories();
            List<Integer> allIds = currentUser.getRole() == Role.ROLE_ADMIN ? subjectDAO.getAllSubjectIds() : subjectDAO.getSubjectIdsByExpert(currentUser.getUser_id());

            request.setAttribute("page", page);
            request.setAttribute("pagesNumber", pagesNumber);
            request.setAttribute("subjects", subjects);
            request.setAttribute("categoryFilter", categoryFilter);
            request.setAttribute("statusFilter", statusFilter);
            request.setAttribute("search", search);
            request.setAttribute("sortOrder", sortOrder);
            request.setAttribute("resultLength", resultLength);
            request.setAttribute("owners", allOwners);
            request.setAttribute("categories", allCategories);
            request.setAttribute("ids", allIds);
            request.setAttribute("isExpert", currentUser.getRole() == Role.ROLE_EXPERT);

            request.getRequestDispatcher("Views/Subject/SubjectListEditView.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace(); // Or use a logger to log this exception
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private int parseOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
