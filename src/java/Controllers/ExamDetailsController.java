/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.QuizDAO;
import Models.Quiz.Quiz;
import Models.Quiz.QuizRecord;
import Models.User;
import Utils.ValidationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author uchih
 */
public class ExamDetailsController extends HttpServlet {

    public static final int PAGE_LENGTH = 5;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null){
                throw new Exception("Not logged in");
            }
            int page = 1;
            if (ValidationUtils.isValidInteger(request.getParameter("page"), 0, Integer.MAX_VALUE)){
                page = Integer.parseInt(request.getParameter("page"));
            }
            int filter = 0;
            if (ValidationUtils.isValidInteger(request.getParameter("filter"), 0, Integer.MAX_VALUE)){
                filter = Integer.parseInt(request.getParameter("filter"));
            }
            int sort = 1;
            if (ValidationUtils.isValidInteger(request.getParameter("sort"), 0, Integer.MAX_VALUE)){
                sort = Integer.parseInt(request.getParameter("sort"));
            }
            
            int offset = (page - 1) * PAGE_LENGTH;
            
            int quiz_id = Integer.parseInt(request.getParameter("quiz_id"));
            
            List<QuizRecord> recordList = GetRecordList(currentUser.getUser_id(), quiz_id, offset, PAGE_LENGTH, filter, sort);
            List<QuizRecord> totalRecordList = GetRecordList(currentUser.getUser_id(), quiz_id, 0, Integer.MAX_VALUE, filter, sort);
            
            int total_pages = totalRecordList.size() / PAGE_LENGTH;
            if (totalRecordList.size() % PAGE_LENGTH != 0){
                total_pages++;
            }
            
            System.out.println("Exams record total: " + totalRecordList.size());
            
            Quiz quiz = GetQuizById(quiz_id);
            
            request.setAttribute("quiz", quiz);
            request.setAttribute("recordList", recordList);
            request.setAttribute("page", page);
            request.setAttribute("sort", sort);
            request.setAttribute("filter", filter);
            request.setAttribute("pages_number", total_pages);
            request.getRequestDispatcher("Views/Customer/ExamDetailsView.jsp").forward(request, response);
        }catch (Exception e){
            response.sendError(404);
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

    private List<QuizRecord> GetRecordList(int user_id, int quiz_id, int offset, int length, int filter, int sort) {
        QuizDAO dao = new QuizDAO();
        Quiz quiz = dao.GetQuizById(quiz_id);
        return quiz.GetQuizRecordsByUserId(user_id, offset, length, filter, sort);
    }
    
    private Quiz GetQuizById(int quiz_id){
        QuizDAO dao = new QuizDAO();
        return dao.GetQuizById(quiz_id);
    }
}
