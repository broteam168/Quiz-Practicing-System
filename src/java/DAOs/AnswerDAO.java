package DAOs;

import Contexts.DBContext;
import Models.Quiz.Answer;
import Models.Quiz.Question;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnswerDAO extends DBContext {

    public List<Answer> GetAnswerByQuestion(int questionId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        List<Answer> results = new ArrayList<>();

        String sqlCommand = "SELECT *"
                + "FROM    [Answer] "
                + "WHERE question_id =?";

        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, questionId);

            rs = ps.executeQuery();
            Answer tempAnser = null;
            while (rs.next()) {

                tempAnser = new Answer(rs.getInt("answer_id"),
                        rs.getInt("question_id"),
                        rs.getString("content"), rs.getBoolean("is_correct"));
                results.add(tempAnser);
            }

        } catch (SQLException ex) {
            System.out.println("Errors occur in get Answer DAO: " + ex.getMessage());
        } finally {
        }
        return results;
    }

    public void CreateAnswer(int questionId, String content) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        String sqlCommand = "INSERT INTO [dbo].[Answer]\n"
                + "           ([question_id]\n"
                + "           ,[content]\n"
                + "           ,[is_correct])\n"
                + "     VALUES\n"
                + "           (?\n"
                + "           ,?\n"
                + "           ,0) ";
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, questionId);
            ps.setString(2, content);
            ps.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Errors occur in get answer DAO: " + ex.getMessage());
        } finally {

        }
    }

    public void UpdateAnswer(int answer_id, int questionId, String content, boolean isCorrect) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        String sqlCommand = "UPDATE [dbo].[Answer]\n"
                + "   SET [question_id] = ?\n"
                + "      ,[content] =?\n"
                + "      ,[is_correct] = ?\n"
                + " WHERE answer_id = ? ";
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, questionId);
            ps.setString(2, content);
            ps.setBoolean(3, isCorrect);
            ps.setInt(4, answer_id);
            ps.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Errors occur in get answer DAO: " + ex.getMessage());
        } finally {

        }
    }

    public void DeleteAnswer(int answer_id) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        String sqlCommand = "DELETE FROM [dbo].[Answer]\n"
                + "      WHERE answer_id = ? ";
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, answer_id);

            ps.executeUpdate();

       }

    public void UpdateAnswer(int answer_id, boolean isCorrect) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        String sqlCommand = "UPDATE [dbo].[Answer]\n"
                + "   SET "
                + "      [is_correct] = ?\n"
                + " WHERE answer_id = ? ";
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);

            ps.setBoolean(1, isCorrect);
            ps.setInt(2, answer_id);
            ps.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Errors occur in get answer DAO: " + ex.getMessage());
        } finally {

        }
    }
}
