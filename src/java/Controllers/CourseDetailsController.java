/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.SubjectCategoryDAO;
import DAOs.SubjectDAO;
import DAOs.UserDAO;
import Models.Subject;
import Models.User;
import Models.SubjectCategory;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author uchih
 */
@MultipartConfig
public class CourseDetailsController extends HttpServlet {

    private static final String UPLOAD_DIR = "Assets/Images/Subject/";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            request.getRequestDispatcher("./Views/CourseContent/CourseDetailsView.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            System.out.println("after update = " + action);
            if (action.equals("view")) {
                String subjectId = request.getParameter("subject_id");
                if (subjectId == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid subject_id");
                    return;
                }
                int subject_id = (Integer.parseInt(request.getParameter("subject_id")));
                Subject subject = GetSubjectById(subject_id);
                List<String> allCategories = GetSubjectCategoryList();
                List<String> categories = GetCategoriesExceptChosen(subject.getCategoryName(), allCategories);
                String otherStatus = GetOtherStatus(subject.getStatus());
                List<User> expertList = GetExpertListBySubjectId(subject_id);
                List<User> unassignedExpertList = GetUnassinedExpertListBySubjectId(subject_id);

                request.setAttribute("subject_id", subject_id);
                request.setAttribute("currentSubject", subject);
                request.setAttribute("categories", categories);
                request.setAttribute("otherStatus", otherStatus);
                request.setAttribute("expertList", expertList);
                request.setAttribute("action", "view");
                request.setAttribute("unassignedExpertList", unassignedExpertList);
                request.getRequestDispatcher("./Views/CourseContent/CourseDetailsView.jsp").forward(request, response);
            } else if (action.equals("create")) {
                List<String> allCategories = GetSubjectCategoryList();
                List<String> statusList = GetStatusList();
                request.setAttribute("categories", allCategories);
                request.setAttribute("action", "create");
                request.setAttribute("statusList", statusList);
                request.getRequestDispatcher("./Views/CourseContent/CourseDetailsView.jsp").forward(request, response);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            String act = request.getParameter("act");
            System.out.println("act " + act);
            if (act != null) {
                if (act.equals("unassign")) {
                    String expertId = request.getParameter("expertId");
                    System.out.println("expertId" + expertId);
                    if (expertId != null) {
                        int user_id = Integer.parseInt(expertId);
                        int subject_id = Integer.parseInt(request.getParameter("subjectId"));
                        boolean success = UnassignExpert(user_id, subject_id);
                        System.out.println("success : " + success);
                        // Prepare JSON response
                        JsonObject jsonResponse = new JsonObject();
                        jsonResponse.addProperty("success", success);
                        if (!success) {
                            jsonResponse.addProperty("message", "Failed to unassign expert.");
                        } else {
                            // If unassignment was successful, retrieve the expert details
                            User expert = GetExpertById(user_id);
                            if (expert != null) {
                                JsonObject expertJson = new JsonObject();
                                expertJson.addProperty("user_id", expert.getUser_id());
                                expertJson.addProperty("full_name", expert.getFull_name());
                                expertJson.addProperty("email", expert.getEmail());
                                expertJson.addProperty("mobile", expert.getMobile());
                                expertJson.addProperty("role", expert.getRole());

                                jsonResponse.add("expert", expertJson);
                            }
                            // Send JSON response back to client
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            out.print(jsonResponse.toString());
                            out.flush();
                        }
                    }
                } else if (act.equals("assign")) {
                    String expertId = request.getParameter("expertId");
                    if (expertId != null) {
                        System.out.println("expertId" + expertId);
                        int user_id = Integer.parseInt(expertId);
                        int subject_id = Integer.parseInt(request.getParameter("subjectId"));
                        boolean success = AssignExpert(user_id, subject_id);
                        // Prepare JSON response
                        JsonObject jsonResponse = new JsonObject();
                        jsonResponse.addProperty("success", success);
                        if (!success) {
                            jsonResponse.addProperty("message", "Failed to assign expert.");
                        } else {
                            // If unassignment was successful, retrieve the expert details
                            User expert = GetExpertById(user_id);
                            if (expert != null) {
                                JsonObject expertJson = new JsonObject();
                                expertJson.addProperty("user_id", expert.getUser_id());
                                expertJson.addProperty("full_name", expert.getFull_name());
                                expertJson.addProperty("email", expert.getEmail());
                                expertJson.addProperty("mobile", expert.getMobile());
                                expertJson.addProperty("role", expert.getRole());

                                jsonResponse.add("expert", expertJson);
                            }
                        }
                        // Send JSON response back to client
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        out.print(jsonResponse.toString());
                        out.flush();
                    }
                } else if (act.equals("save")) {
                    // Retrieve form parameters
                    String subjectId = request.getParameter("subjectId");
                    String subjectName = request.getParameter("subjectName");
                    String category = request.getParameter("category");
                    String featureParam = request.getParameter("feature");
                    boolean isFeatured = "true".equals(featureParam);
                    String status = request.getParameter("status");
                    String avatar = request.getParameter("subjectThumbnail");
                    String description = request.getParameter("description");
                    String tagline = request.getParameter("tagline");
                    // Handle file upload
                    String applicationPath = request.getServletContext().getRealPath("");
                    String uploadFilePath = Paths.get(applicationPath, UPLOAD_DIR).toString();
                    File uploadDirectory = new File(uploadFilePath);
                    // Check if the directory exists
                    if (!uploadDirectory.exists()) {
                        uploadDirectory.mkdirs();
                    }
                    Part filePart = request.getPart("file");
                    String avatarPath;
                    // Check if the file has been uploaded and is not empty
                    if (filePart != null && filePart.getSize() > 0) {
                        String fileName = "thumb" + subjectId + ".jpg";
                        File oldFile = new File(Paths.get(uploadFilePath, fileName).toString());
                        // Delete the old file if it exists
                        if (oldFile.exists()) {
                            oldFile.delete();
                        }
                        // Save the new file
                        filePart.write(Paths.get(uploadFilePath, fileName).toString());
                        avatarPath = Paths.get(UPLOAD_DIR, fileName).toString();
                        avatarPath = avatarPath.replace("\\", "/");
                    } else {
                        avatarPath = avatar;
                    }
                    UpdateCourse(subjectId, subjectName, category, isFeatured, status, avatarPath, tagline, description);
                    response.sendRedirect("http://localhost:8080/app/course-details?subject_id=" + subjectId + "&action=view");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Subject GetSubjectById(int subject_id) {
        SubjectDAO dao = new SubjectDAO();
        return dao.getSubjectById(subject_id);
    }

    private List<String> GetSubjectCategoryList() {
        SubjectDAO dao = new SubjectDAO();
        return dao.getAllCategories();
    }

    private List<String> GetCategoriesExceptChosen(String subject_name, List<String> allCategories) {
        for (int i = 0; i < allCategories.size(); i++) {
            if (allCategories.get(i).equals(subject_name)) {
                allCategories.remove(i);
                return allCategories;
            }
        }
        return null;
    }

    private String GetOtherStatus(int status) {
        if (status == 0) {
            return "Published";
        } else {
            return "Unpublished";
        }
    }

    private List<User> GetExpertListBySubjectId(int subjectId) {
        SubjectDAO dao = new SubjectDAO();
        return dao.GetExpertListBySubjectId(subjectId);
    }

    private List<String> GetStatusList() {
        List<String> statusList = new ArrayList<>();
        statusList.add("Published");
        statusList.add("Unpublished");
        return statusList;
    }

    private boolean UnassignExpert(int user_id, int subject_id) {
        SubjectDAO dao = new SubjectDAO();
        dao.UnassignExpert(user_id, subject_id);
        return true;
    }

    private List<User> GetUnassinedExpertListBySubjectId(int subject_id) {
        SubjectDAO dao = new SubjectDAO();
        return dao.GetExpertListUnAssignedFromSubjectId(subject_id);
    }

    private boolean AssignExpert(int user_id, int subject_id) {
        SubjectDAO dao = new SubjectDAO();
        dao.AssignExpert(user_id, subject_id);
        return true;
    }

    private User GetExpertById(int user_id) {
        UserDAO dao = new UserDAO();
        return dao.GetUserById(user_id);
    }

    private void UpdateCourse(String subjectId, String subjectName, String category, boolean isFeatured, String statusText, String avatarPath, String tagline, String description) {
        SubjectDAO dao = new SubjectDAO();
        int subject_id = Integer.parseInt(subjectId);
        int status = GetStatus(statusText);
        int category_id = GetCategoryIdByName(category);
        dao.UpdateCourse(subject_id, avatarPath, subjectName, tagline, status, description, isFeatured, category_id);
    }

    private int GetStatus(String statusText) {
        if (statusText.equals("Unpublished")) {
            return 0;
        } else {
            return 1;
        }
    }

    private int GetCategoryIdByName(String category) {
        SubjectCategoryDAO dao = new SubjectCategoryDAO();
        List<SubjectCategory> categoryList = dao.GetAllCategories();
        for (int i = 0; i < categoryList.size(); i++) {
            if (category.equals(categoryList.get(i).getCategory_name())) {
                return categoryList.get(i).getCategory_id();
            }
        }
        return -1;
    }
}
