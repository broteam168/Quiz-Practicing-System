package DAOs;

import Contexts.DBContext;
import Models.Question;
import Models.QuestionDetail;
import Models.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QuestionDAO extends DBContext {

    public int GetQuestionSize(int role, int userId, String content,
            String subjectName,
            String subjectLesson,
            String subjectDimension,
            String subjectLevel,
            int status) {

        int count = 0;
        String query = "SELECT COUNT(*) FROM (SELECT Question.question_id,\n"
                + "                	   Question.content,\n"
                + "                   ISNULL([Subject].title,'') as title,\n"
                + "                  ISNULL(SubjectLesson.[name],'') as lesson,\n"
                + "                    ISNULL(SubjectDimension.[name],'') as dimension,\n"
                + "                    ISNULL(LevelType.[type],'') as type,\n"
                + "                  Question.[status],\n";
        if (role == Role.ROLE_EXPERT) {
            query += "Expert_Subject.user_id ,";
        }
        query += "                ROW_NUMBER() OVER(ORDER BY question_id) as rowIndex\n"
                + "               FROM Question\n"
                + "               LEFT JOIN SubjectLesson ON SubjectLesson.lesson_id = Question.lesson_id\n"
                + "               LEFT JOIN SubjectDimension ON SubjectDimension.dimension_id = Question.dimension_id\n"
                + "               LEFT JOIN [Subject] ON [Subject].subject_id = Question.subject_id\n"
                + "               LEFT JOIN LevelType ON LevelType.id = Question.[level]\n";
        if (role == Role.ROLE_EXPERT) {
            query += "  LEFT JOIN [Expert_Subject] ON [Expert_Subject].[subject_id] = [Subject].[subject_id]\n";
        }
        query += "                )as temp\n"
                + "             WHERE content LIKE ?\n"
                + "		AND LOWER(title) LIKE LOWER(?)\n"
                + "		AND LOWER(lesson) LIKE LOWER(?)\n"
                + "		AND LOWER(dimension) LIKE LOWER(?)\n"
                + "		AND LOWER(type) LIKE LOWER(?)\n";
        if (status != 3) {
            query += "AND [status] = " + status;
        }
        if (role == Role.ROLE_EXPERT) {
            query += "AND [user_id] = " + userId;
        }

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + content + "%");
            stmt.setString(2, "%" + subjectName + "%");
            stmt.setString(3, "%" + subjectLesson + "%");
            stmt.setString(4, "%" + subjectDimension + "%");
            stmt.setString(5, "%" + subjectLevel + "%");

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);

            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public List<Question> GetQuestionsPage(int role, int userId,int page, int pageSize,
            String content,
            String subjectName,
            String subjectLesson,
            String subjectDimension,
            String subjectLevel,
            int status) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        Question question = null;
        List<Question> result = new ArrayList<>();

        int startIndex = (page - 1) * pageSize + 1;
        int endIndex = (page) * pageSize;

          String query = "Select * from (SELECT *,ROW_NUMBER() OVER(ORDER BY question_id) as rowIndex FROM (SELECT Question.question_id,\n"
                + "                	   Question.content,\n"
                + "                   ISNULL([Subject].title,'') as title,\n"
                + "                  ISNULL(SubjectLesson.[name],'') as lesson,\n"
                + "                    ISNULL(SubjectDimension.[name],'') as dimension,\n"
                + "                    ISNULL(LevelType.[type],'') as type,\n"
                + "                  Question.[status]\n";
        if (role == Role.ROLE_EXPERT) {
            query += ",Expert_Subject.user_id ";
        }
        query += "                \n"
                + "               FROM Question\n"
                + "               LEFT JOIN SubjectLesson ON SubjectLesson.lesson_id = Question.lesson_id\n"
                + "               LEFT JOIN SubjectDimension ON SubjectDimension.dimension_id = Question.dimension_id\n"
                + "               LEFT JOIN [Subject] ON [Subject].subject_id = Question.subject_id\n"
                + "               LEFT JOIN LevelType ON LevelType.id = Question.[level]\n";
        if (role == Role.ROLE_EXPERT) {
            query += "  LEFT JOIN [Expert_Subject] ON [Expert_Subject].[subject_id] = [Subject].[subject_id]\n";
        }
        query += "                ) as temp\n"
                + "             WHERE content LIKE ?\n"
                + "		AND LOWER(title) LIKE LOWER(?)\n"
                + "		AND LOWER(lesson) LIKE LOWER(?)\n"
                + "		AND LOWER(dimension) LIKE LOWER(?)\n"
                + "		AND LOWER(type) LIKE LOWER(?)\n";
        if (status != 3) {
            query += "AND [status] = " + status;
        }
        if (role == Role.ROLE_EXPERT) {
            query += "AND [user_id] = " + userId;
        }
        query += " ) as temp2 WHERE (rowIndex between ? AND ?)";
       try {
            connection = getConnection();
            ps = connection.prepareStatement(query);
            ps.setInt(6, startIndex);
            ps.setInt(7, endIndex);
            ps.setString(1, "%" + content + "%");
            ps.setString(2, "%" + subjectName + "%");
            ps.setString(3, "%" + subjectLesson + "%");
            ps.setString(4, "%" + subjectDimension + "%");
            ps.setString(5, "%" + subjectLevel + "%");
            rs = ps.executeQuery();
            Question item = null;
            while (rs.next()) {

                item = new Question(rs.getInt("question_id"),
                        rs.getString("content"),
                        rs.getString("title"),
                        rs.getString("lesson"),
                        rs.getString("dimension"),
                        rs.getString("type"),
                        rs.getInt("status"));

                result.add(item);
            }

            return result;
        } catch (SQLException ex) {
            System.out.println("Errors occur in get quesion DAO: " + ex.getMessage());
        } finally {
            if (connection != null) {
            }
        }
        return null;
    }

    public void UpdateStatusQuestion(int questionId, int status) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        String sqlCommand = "UPDATE [dbo].[Question]\n"
                + "          SET [status] = ? \n"
                + "         WHERE question_id = ? ";
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, status);
            ps.setInt(2, questionId);
            ps.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Errors occur in get Question DAO: " + ex.getMessage());
        } finally {

        }

    }

    public QuestionDetail GetQuestionById(int questionId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        QuestionDetail question = null;
        String query = "SELECT Question.question_id,\n"
                + "	   Question.dimension_id,\n"
                + "	   Question.lesson_id,\n"
                + "	   Question.content,\n"
                + "	   Question.explaination,\n"
                + "	   Question.subject_id,\n"
                + "	   Question.[level],\n"
                + "	   Question.[type_id],Question.[status],[media],media_type\n"
                + "FROM Question where question_id =?";

        try {
            connection = getConnection();
            ps = connection.prepareStatement(query);
            ps.setInt(1, questionId);
            rs = ps.executeQuery();
            QuestionDetail item = null;
            if (rs.next()) {

                question = new QuestionDetail(rs.getInt("question_id"),
                        rs.getInt("subject_id"),
                        rs.getInt("dimension_id"),
                        rs.getInt("lesson_id"),
                        rs.getInt("status"),
                        rs.getString("content"),
                        rs.getString("explaination"),
                        rs.getString("media"),
                        rs.getInt("type_id"),
                        rs.getInt("level"),
                        rs.getString("media_type"));

            }

            return question;
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Errors occur in get quesion DAO: " + ex.getLocalizedMessage());
        } finally {
            if (connection != null) {
            }
        }
        return null;

    }

    public void UpdateQuestionDetail(QuestionDetail detail) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        String sqlCommand = "UPDATE [dbo].[Question]\n"
                + "   SET \n"
                + "       [dimension_id] = ?\n"
                + "	  ,[status] = ?\n"
                + "      ,[level] = ?\n"
                + "      ,[explaination] = ?\n"
                + "      ,[content] = ?\n"
                + "      ,[lesson_id] = ?\n"
                + "      ,[subject_id] =?\n"
                + "	  ,[media] = ?\n"
                + " WHERE question_id = ?\n"
                + "\n"
                + "\n"
                + "";
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            if (detail.getDimensionId() == 0) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, detail.getDimensionId());
            }

            ps.setInt(2, detail.getStatus());

            if (detail.getLevel() == 0) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, detail.getLevel());
            }

            ps.setString(4, detail.getExpaination());
            ps.setString(5, detail.getContent());
            if (detail.getLessonId() == 0) {
                ps.setNull(6, java.sql.Types.INTEGER);
            } else {
                ps.setInt(6, detail.getLessonId());
            }
            ps.setInt(7, detail.getSubjectId());
            ps.setString(8, detail.getMedia());
            ps.setInt(9, detail.getQuestionId());
            ps.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Errors occur in get answer DAO: " + ex.getMessage());
        } finally {

        }
    }

    public QuestionDetail AddQuestionDetail(QuestionDetail detail) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        String sqlCommand = "	INSERT INTO [dbo].[Question]\n"
                + "			   ([type_id]\n"
                + "			   ,[topic_id]\n"
                + "			   ,[dimension_id]\n"
                + "			   ,[status]\n"
                + "			   ,[level]\n"
                + "			   ,[explaination]\n"
                + "			   ,[content]\n"
                + "			   ,[value]\n"
                + "			   ,[lesson_id]\n"
                + "			   ,[subject_id]\n"
                + "			   ,[media]\n"
                + "			   ,[media_type])\n"
                + "		 VALUES\n"
                + "			   (2\n"
                + "			   ,null\n"
                + "			   ,?\n"
                + "			   ,?\n"
                + "			   ,?\n"
                + "			   ,?\n"
                + "			   ,?\n"
                + "			   ,1\n"
                + "			   ,?\n"
                + "			   ,?\n"
                + "			   ,?\n"
                + "			   ,null)";
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);
            if (detail.getDimensionId() == 0) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, detail.getDimensionId());
            }

            ps.setInt(2, detail.getStatus());

            if (detail.getLevel() == 0) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, detail.getLevel());
            }

            ps.setString(4, detail.getExpaination());
            ps.setString(5, detail.getContent());
            if (detail.getLessonId() == 0) {
                ps.setNull(6, java.sql.Types.INTEGER);
            } else {
                ps.setInt(6, detail.getLessonId());
            }
            ps.setInt(7, detail.getSubjectId());
            ps.setString(8, detail.getMedia());
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                System.out.println("Generated ID: " + generatedId);
                detail.setQuestionId(generatedId);
            }
        } catch (SQLException ex) {
            System.out.println("Errors occur in get question DAO: " + ex.getMessage());
        } finally {

        }
        return detail;
    }
}
