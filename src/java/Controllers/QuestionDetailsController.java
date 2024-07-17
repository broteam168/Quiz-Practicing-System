package Controllers;

import DAOs.AnswerDAO;
import DAOs.DimensionDAO;
import DAOs.LessonDAO;
import DAOs.LevelDAO;
import DAOs.QuestionDAO;
import DAOs.SubjectDAO;
import Models.Lesson;
import Models.QuestionDetail;
import Models.Quiz.Answer;
import Models.Role;
import Models.Subject;
import Models.SubjectDimension;
import Models.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@MultipartConfig
public class QuestionDetailsController extends HttpServlet {

    private static final String UPLOAD_DIR = "Assets/Media/Question/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        int role = ((User) session.getAttribute("currentUser")).getRole();
        int userId = ((User) session.getAttribute("currentUser")).getUser_id();
        String action = request.getParameter("action");
        if (action != null && action.equals("edit")) {
            String questionIdStr = request.getParameter("question_id");
            if (questionIdStr != null) {
                int id = Integer.parseInt(questionIdStr);
                QuestionDAO dao = new QuestionDAO();
                //// Get subject by role
                SubjectDAO subjectDao = new SubjectDAO();
                List<Subject> allSubjects;
                try {
                    allSubjects = subjectDao.GetSubjectByRole(role, userId);
                    request.setAttribute("subjects", allSubjects);

                } catch (SQLException ex) {
                    Logger.getLogger(QuestionDetailsController.class.getName()).log(Level.SEVERE, null, ex);
                }

                LevelDAO leveldao = new LevelDAO();
                List<Models.Level> levels = leveldao.GetLevel();
                request.setAttribute("levels", levels);

                QuestionDetail currentQuestion = dao.GetQuestionById(id);
                request.setAttribute("question", currentQuestion);

                AnswerDAO anserDAO = new AnswerDAO();
                List<Answer> answers = anserDAO.GetAnswerByQuestion(id);
                request.setAttribute("answers", answers);

                request.getRequestDispatcher("./Views/Expert/QuestionDetailsView.jsp").forward(request, response);
            } else {
                response.sendRedirect("question");
            }
        } else if (action != null && action.equals("dimension")) {
            String id = request.getParameter("subjectId");

            if (id != null) {
                int idValue = Integer.parseInt(id);
                DimensionDAO dimensionDAO = new DimensionDAO();
                List<SubjectDimension> dimensions = dimensionDAO.GetDimensionBySubject(idValue);
                JSONArray jsonArray = new JSONArray();
                for (SubjectDimension subjectDimension : dimensions) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("dimension_id", subjectDimension.getDimension_id());
                        jsonObject.put("type_id", subjectDimension.getType_id());
                        jsonObject.put("subject_id", subjectDimension.getSubject_id());
                        jsonObject.put("name", subjectDimension.getName());
                        jsonArray.put(jsonObject);
                    } catch (JSONException ex) {
                        Logger.getLogger(QuestionDetailsController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                JSONObject jsonResponse = new JSONObject();
                try {
                    jsonResponse.put("subjectDimensions", jsonArray);
                    jsonResponse.put("status", "success");
                    jsonResponse.put("message", "Subject dimensions retrieved successfully");
                } catch (JSONException ex) {
                    Logger.getLogger(QuestionDetailsController.class.getName()).log(Level.SEVERE, null, ex);
                }
                response.setHeader("Access-Control-Allow-Origin", "*"); // Adjust this to your needs
                response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
                response.setHeader("Access-Control-Max-Age", "3600");
                response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

                response.setContentType("application/json");
                PrintWriter out = response.getWriter();

                out.print(jsonResponse);
                out.flush();
            }
        } else if (action != null && action.equals("lesson")) {
            String id = request.getParameter("subjectId");

            if (id != null) {
                int idValue = Integer.parseInt(id);
                LessonDAO lessonDAO = new LessonDAO();
                List<Lesson> lessons = lessonDAO.GetLessonBySubject(idValue);
                JSONArray jsonArray = new JSONArray();
                for (Lesson lesson : lessons) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("lesson_id", lesson.getLesson_id());
                        jsonObject.put("lesson_type", lesson.getLesson_type());
                        jsonObject.put("name", lesson.getName());
                        jsonArray.put(jsonObject);
                    } catch (JSONException ex) {
                        Logger.getLogger(QuestionDetailsController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                JSONObject jsonResponse = new JSONObject();
                try {
                    jsonResponse.put("lessons", jsonArray);
                    jsonResponse.put("status", "success");
                    jsonResponse.put("message", "Subject dimensions retrieved successfully");
                } catch (JSONException ex) {
                    Logger.getLogger(QuestionDetailsController.class.getName()).log(Level.SEVERE, null, ex);
                }
                response.setHeader("Access-Control-Allow-Origin", "*"); // Adjust this to your needs
                response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
                response.setHeader("Access-Control-Max-Age", "3600");
                response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

                response.setContentType("application/json");
                PrintWriter out = response.getWriter();

                out.print(jsonResponse);
                out.flush();
            }
        } else if (action != null && action.equals("add")) {

            SubjectDAO subjectDao = new SubjectDAO();
            List<Subject> allSubjects;
            try {
                System.out.println("role:" + role);
                allSubjects = subjectDao.GetSubjectByRole(role, userId);
                request.setAttribute("subjects", allSubjects);

            } catch (SQLException ex) {
                Logger.getLogger(QuestionDetailsController.class.getName()).log(Level.SEVERE, null, ex);
            }

            LevelDAO leveldao = new LevelDAO();
            List<Models.Level> levels = leveldao.GetLevel();
            request.setAttribute("levels", levels);

            QuestionDetail currentQuestion = new QuestionDetail();

            request.setAttribute("question", currentQuestion);

            List<Answer> answers = new ArrayList<>();
            request.setAttribute("answers", answers);

            request.getRequestDispatcher("./Views/Expert/QuestionDetailsView.jsp").forward(request, response);

        } else {
            response.sendRedirect("question");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action != null && action.equals("addAnswer")) {
            String content = request.getParameter("content");
            String questionId = request.getParameter("qeustionId");
            if (content != null && questionId != null) {
                AnswerDAO dao = new AnswerDAO();
                dao.CreateAnswer(Integer.parseInt(questionId), content);
                response.sendRedirect("question-details?action=edit&question_id=" + questionId + "&message2=Add successfully&scroll=end");
            } else {
                response.sendRedirect("question-details?action=edit&question_id=" + questionId + "&message=Some error occur");
            }
        } else if (action != null && action.equals("saveAnswer")) {
            String content = request.getParameter("content");
            String questionId = request.getParameter("qeustionId");
            String answerId = request.getParameter("answerId");
            boolean correct = request.getParameter("correct") != null
                    && request.getParameter("correct") == "Correct";
            if (content != null && questionId != null) {
                AnswerDAO dao = new AnswerDAO();
                dao.UpdateAnswer(Integer.parseInt(answerId),
                        Integer.parseInt(questionId),
                        content,
                        correct);
                response.sendRedirect("question-details?action=edit&question_id=" + questionId + "&message2=Save successfully&scroll=end");

            } else {
                response.sendRedirect("question-details?action=edit&question_id=" + questionId + "&message=Some error occur");
            }
        } else if (action != null && action.equals("deleteAnswer")) {
            String answerId = request.getParameter("answerId");
            String questionId = request.getParameter("qeustionId");
            if (answerId != null) {

                try {
                    AnswerDAO dao = new AnswerDAO();
                    dao.DeleteAnswer(Integer.parseInt(answerId));
                    response.sendRedirect("question-details?action=edit&question_id=" + questionId + "&message2=Save successfully&scroll=end");
                } catch (Exception ex) {
                    response.sendRedirect("question-details?action=edit&question_id=" + questionId + "&message=Some error occur. Answer in question taken cannot be deleted");

                }
            } else {
                response.sendRedirect("question-details?action=edit&question_id=" + questionId + "&message=Some error occur");
            }
        } else if (action != null && action.equals("markAnswer")) {
            String answerId = request.getParameter("answerId");
            String questionId = request.getParameter("qeustionId");
            boolean status = "1".equals(request.getParameter("status"));
            if (answerId != null) {
                AnswerDAO dao = new AnswerDAO();
                dao.UpdateAnswer(Integer.parseInt(answerId),
                        status);
                response.sendRedirect("question-details?action=edit&question_id=" + questionId + "&message2=Save successfully&scroll=end");

            } else {
                response.sendRedirect("question-details?action=edit&question_id=" + questionId + "&message=Some error occur");
            }
        } else if (action != null && action.equals("edit")) {

            String questionId = request.getParameter("id");
            String subjectId = request.getParameter("subject");
            String dimensionId = request.getParameter("dimension");
            String lessonId = request.getParameter("lesson");
            String status = request.getParameter("status");
            String Level = request.getParameter("level");

            String content = request.getParameter("content");
            String explain = request.getParameter("Explaination");
            String link = request.getParameter("link");
            if (questionId != null && subjectId != null && status != null && !"".equals(content)
                    ) {
                String applicationPath = request.getServletContext().getRealPath("");
                String uploadFilePath = Paths.get(applicationPath, UPLOAD_DIR).toString() + "/" + questionId;
                File uploadDirectory = new File(uploadFilePath);
                //check if the directory is exist
                if (!uploadDirectory.exists()) {
                    uploadDirectory.mkdirs();
                }
                Part filePart = request.getPart("uploadfile");
                String filePath = "";
                if (filePart != null && filePart.getSize() > 0) {
                    String[] parts = link.split("/");
                    String fileName = parts[parts.length - 1];
                    File oldFile = new File(Paths.get(uploadFilePath, fileName).toString());
                    //check if the old file exists
                    if (oldFile.exists()) {
                        //if old file exist, delete the old file
                        oldFile.delete();
                    }
                    try {
                        filePart.write(Paths.get(uploadFilePath, fileName).toString());
                        System.out.println("File uploaded successfully");

                    } catch (IOException e) {
                        e.printStackTrace();
                        response.sendRedirect("question-details?action=edit&question_id=" + questionId + "&message=Upload media failed");

                    }

                    filePath = Paths.get(UPLOAD_DIR + "/" + questionId, fileName).toString();

                } else {
                    filePath = link;

                }
                int questionIdVal = Integer.parseInt(questionId);
                int subjectIdVal = Integer.parseInt(subjectId);
                int dimensionIdVal = Integer.parseInt(dimensionId);
                int lessonIdVal = Integer.parseInt(lessonId);
                int statusVal = Integer.parseInt(status);
                int levelVal = Integer.parseInt(Level);
                QuestionDetail detail = new QuestionDetail(questionIdVal,
                        subjectIdVal,
                        dimensionIdVal,
                        lessonIdVal,
                        statusVal, content, explain, filePath, 0, levelVal);
                QuestionDAO quesDao = new QuestionDAO();
                quesDao.UpdateQuestionDetail(detail);
                response.sendRedirect("question-details?action=edit&question_id=" + questionId + "&message2=Save successfully");

            } else {
                response.sendRedirect("question-details?action=edit&question_id=" + questionId + "&message=Please enter enough information");
            }
        } else if (action != null && action.equals("add")) {

            String subjectId = request.getParameter("subject");
            String dimensionId = request.getParameter("dimension");
            String lessonId = request.getParameter("lesson");
            String status = request.getParameter("status");
            String Level = request.getParameter("level");

            String content = request.getParameter("content");
            String explain = request.getParameter("Explaination");
            String link = request.getParameter("link");
            if (subjectId != null && status != null && !("").equals(content) && !("").equals(explain)) {

                //// Handle add 
                int subjectIdVal = Integer.parseInt(subjectId);
                int dimensionIdVal = Integer.parseInt(dimensionId);
                int lessonIdVal = Integer.parseInt(lessonId);
                int statusVal = Integer.parseInt(status);
                int levelVal = Integer.parseInt(Level);

                QuestionDetail detail = new QuestionDetail(1,
                        subjectIdVal,
                        dimensionIdVal,
                        lessonIdVal,
                        statusVal, content, explain, "", 0, levelVal);
                QuestionDAO quesDao = new QuestionDAO();
                QuestionDetail newDetail = quesDao.AddQuestionDetail(detail);
                /////

                String applicationPath = request.getServletContext().getRealPath("");
                String uploadFilePath = Paths.get(applicationPath, UPLOAD_DIR).toString() + "/" + newDetail.getQuestionId();
                File uploadDirectory = new File(uploadFilePath);
                //check if the directory is exist
                if (!uploadDirectory.exists()) {
                    uploadDirectory.mkdirs();
                }
                Part filePart = request.getPart("uploadfile");
                String filePath = "";
                if (filePart != null && filePart.getSize() > 0) {
                    String[] parts = link.split("/");
                    String fileName = parts[parts.length - 1];

                    filePath = Paths.get(UPLOAD_DIR + "/" + newDetail.getQuestionId(), fileName).toString();

                    File oldFile = new File(Paths.get(uploadFilePath, fileName).toString());
                    //check if the old file exists
                    if (oldFile.exists()) {
                        //if old file exist, delete the old file
                        oldFile.delete();
                    }
                    try {
                        filePart.write(Paths.get(uploadFilePath, fileName).toString());
                        System.out.println("File uploaded successfully");

                    } catch (IOException e) {
                        e.printStackTrace();
                        response.sendRedirect("question-details?action=add&question_id=" + newDetail.getQuestionId() + "&message=Upload media failed");

                    }
                } else {
                    filePath = link;

                }
                newDetail.setMedia(filePath);
                quesDao.UpdateQuestionDetail(newDetail);
                response.sendRedirect("question-details?action=edit&question_id=" + newDetail.getQuestionId() + "&message2=Save successfully");

            } else {
                response.sendRedirect("question-details?action=add&message=Please enter enough information");
            }
        } else {

        }
    }

    public void ResponseJSON(HttpServletRequest request,
            HttpServletResponse response,
            JSONObject data, String message) throws IOException, JSONException {
        response.setHeader("Access-Control-Allow-Origin", "*"); // Adjust this to your needs
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        out.print(data);
        out.flush();
    }
}
