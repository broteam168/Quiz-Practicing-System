package DAOs;

import Contexts.DBContext;
import Models.Quiz.Answer;
import Models.Quiz.Question;
import Models.Quiz.QuizRecord;
import Models.Quiz.Quiz;
import Models.Quiz.RecordAnswer;
import Models.Quiz.RecordResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizDAO extends DBContext {

    private static final String SELECT_ALL_QUIZZES = "SELECT * FROM Quizzes WHERE published = true";
    private static final String SELECT_QUIZ_TYPES = "SELECT DISTINCT quizType FROM Quizzes";
    private static final String SELECT_SUBJECTS = "SELECT DISTINCT subject FROM Quizzes";
    private static final String COUNT_QUIZZES = "SELECT COUNT(*) FROM Quizzes WHERE published = true";

    public QuizRecord GetQuizRecordByRecordId(int recordId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        QuizRecord record = null;
        String sqlCommand = "SELECT [record_id]\n"
                + "      ,[user_id]\n"
                + "      ,[quiz_id]\n"
                + "      ,[created_at]\n"
                + "      ,[score]\n"
                + "      ,[finished_at]\n"
                + "      ,[status]\n"
                + "  FROM [dbo].[QuizRecord] WHERE record_id = ?";

        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, recordId);
            rs = ps.executeQuery();

            while (rs.next()) {
                LocalDateTime create = null;
                if (rs.getDate("created_at") != null) {
                    create = rs.getTimestamp("created_at").toLocalDateTime();
                }
                LocalDateTime finish = null;
                if (rs.getDate("finished_at") != null) {
                    finish = rs.getTimestamp("finished_at").toLocalDateTime();
                }
                record = new QuizRecord(rs.getInt("record_id"),
                        rs.getInt("user_id"), rs.getInt("quiz_id"),
                        create,
                        rs.getFloat("score"),
                        finish);
                record.setStatus(rs.getInt("status"));
            }

        } catch (SQLException ex) {
            System.out.println("Errors occur in get user DAO: " + ex.getMessage());
        } finally {
        }
        return record;
    }

    public Quiz GetQuizById(int quizId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        Quiz quiz = null;
        String sqlCommand = "SELECT [quiz_id]\n"
                + "      ,[name]\n"
                + "      ,[duration]\n"
                + "      ,[quiz_type]\n"
                + "      ,[num_questions]\n"
                + "      ,[level]\n"
                + "      ,[status]\n"
                + "      ,[pass_rate]\n"
                + "      ,[description]\n"
                + "      ,[created_at]\n"
                + "      ,[updated_at]\n"
                + "      ,[subject_id]\n"
                + "      ,[pass_condition]\n"
                + "  FROM [dbo].[Quiz] WHERE quiz_id = ?";

        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, quizId);
            rs = ps.executeQuery();

            while (rs.next()) {
                LocalDateTime create = null;
                if (rs.getDate("created_at") != null) {
                    create = rs.getTimestamp("created_at").toLocalDateTime();
                }
                LocalDateTime update = null;
                if (rs.getDate("updated_at") != null) {
                    update = rs.getTimestamp("updated_at").toLocalDateTime();
                }
                quiz = new Quiz(quizId,
                        rs.getString("name"),
                        rs.getInt("duration"),
                        rs.getInt("quiz_type"),
                        rs.getInt("num_questions"),
                        rs.getInt("level"),
                        rs.getInt("status"),
                        rs.getInt("pass_rate"),
                        rs.getString("description"),
                        create,
                        update,
                        rs.getInt("subject_id"),
                        rs.getInt("pass_condition"));
            }

        } catch (SQLException ex) {
            System.out.println("Errors occur in get quiz DAO: " + ex.getMessage());
        } finally {
        }
        return quiz;
    }

    // get all quiz-record of an user for a quiz
    public List<QuizRecord> GetQuizRecordsByUserId(int user_id, int quiz_id, int offset, int length, int filter, int sort) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        String filterOption = "";
        // filter : 0 - all, 1 - pass, 2 - not pass
        switch (filter) {
            case 1:
                filterOption = "AND status = 1";
                break;
            case 2:
                filterOption = "AND (status != 1 or status is null)";
                break;
            default:
                filterOption = "";
                break;
        }

        String sortOption = "";
        // sort : 1 - created_at, 2 - score, 3 - time taken (finished_at - created_at)
        switch (sort) {
            case 1:
                sortOption = "ORDER BY created_at DESC";
                break;
            case 2:
                sortOption = "ORDER BY score DESC";
                break;
            case 3:
                sortOption = "ORDER BY finished_at - created_at DESC";
                break;
            default:
                sortOption = "ORDER BY created_at DESC";
                break;
        }

        List<QuizRecord> records = new ArrayList<>();
        String sqlCommand = "SELECT [record_id]\n"
                + "      ,[user_id]\n"
                + "      ,[quiz_id]\n"
                + "      ,[created_at]\n"
                + "      ,[score]\n"
                + "      ,[status]\n"
                + "      ,[finished_at]\n"
                + "  FROM [dbo].[QuizRecord] WHERE user_id = ? AND quiz_id = ? "
                + filterOption + " " + sortOption + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, user_id);
            ps.setInt(2, quiz_id);
            ps.setInt(3, offset);
            ps.setInt(4, length);
            rs = ps.executeQuery();

            while (rs.next()) {
                LocalDateTime create = null;
                if (rs.getDate("created_at") != null) {
                    create = rs.getTimestamp("created_at").toLocalDateTime();
                }
                LocalDateTime finish = null;
                if (rs.getDate("finished_at") != null) {
                    finish = rs.getTimestamp("finished_at").toLocalDateTime();
                }
                QuizRecord record = new QuizRecord();
                record.setRecord_id(rs.getInt("record_id"));
                record.setUser_id(rs.getInt("user_id"));
                record.setQuiz_id(rs.getInt("quiz_id"));
                record.setCreated_at(create);
                record.setScore(rs.getFloat("score"));
                record.setStatus(rs.getInt("status"));
                record.setFinished_at(finish);
                records.add(record);
            }

        } catch (SQLException ex) {
            System.out.println("Errors occur in get user DAO: " + ex.getMessage());
        } finally {
        }
        return records;
    }

    public Question GetQuestion(int quizId, int order) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        Question result = null;
        String sqlCommand = "SELECT dbo.Question.*, dbo.Quiz_Question.*\n"
                + "FROM     dbo.Question INNER JOIN\n"
                + "                  dbo.Quiz_Question ON dbo.Question.question_id = dbo.Quiz_Question.question_id\n"
                + "WHERE quiz_id = ? AND [order] =?";

        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, quizId);
            ps.setInt(2, order);
            rs = ps.executeQuery();

            while (rs.next()) {

                result = new Question(rs.getInt("question_id"),
                        rs.getInt("type_id"),
                        rs.getInt("topic_id"),
                        rs.getInt("dimension_id"),
                        rs.getInt("status"),
                        rs.getInt("level"),
                        rs.getString("explaination"),
                        rs.getString("content"),
                        rs.getInt("value"));
            }

        } catch (SQLException ex) {
            System.out.println("Errors occur in get quiz DAO: " + ex.getMessage());
        } finally {
        }
        return result;
    }

    public HashMap<Integer, Answer> GetAnswersByQues(int questionId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        HashMap<Integer, Answer> result = new HashMap<Integer, Answer>();
        String sqlCommand = "SELECT [answer_id]\n"
                + "      ,[question_id]\n"
                + "      ,[content]\n"
                + "      ,[is_correct]\n"
                + "  FROM [dbo].[Answer] WHERE question_id = ?";

        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, questionId);
            rs = ps.executeQuery();

            while (rs.next()) {

                Answer answer = new Answer(rs.getInt("answer_id"),
                        questionId,
                        rs.getString("content"),
                        rs.getBoolean("is_correct"));
                result.put(answer.getAnswerId(), answer);
            }

        } catch (SQLException ex) {
            System.out.println("Errors occur in get quiz DAO: " + ex.getMessage());
        } finally {
        }
        return result;
    }

    public HashMap<Integer, RecordAnswer> GetRecordAnswersByQues(int questionId, int recordId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        HashMap<Integer, RecordAnswer> result = new HashMap<>();
        String sqlCommand = "SELECT [id]\n"
                + "      ,[record_id]\n"
                + "      ,[question_id]\n"
                + "      ,[is_flagged]\n"
                + "      ,[content]\n"
                + "      ,[answer_id]\n"
                + "  FROM [dbo].[RecordAnswer]\n"
                + "WHERE record_id = ? AND question_id = ?";

        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, recordId);
            ps.setInt(2, questionId);

            rs = ps.executeQuery();

            while (rs.next()) {

                RecordAnswer answer = new RecordAnswer(rs.getInt("id"),
                        recordId, questionId,
                        rs.getBoolean("is_flagged"),
                        sqlCommand, rs.getInt("answer_id"));
                result.put(answer.getAnswerId(), answer);
                System.out.println(rs.getInt("id"));
            }

        } catch (SQLException ex) {
            System.out.println("Errors occur in get quiz DAO: " + ex.getMessage());
        } finally {
        }
        return result;
    }

    public RecordAnswer getRecordAnswerByQues(int recordId, int questionId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        RecordAnswer result = null;
        String sqlCommand = "SELECT [id]\n"
                + "      ,[record_id]\n"
                + "      ,[question_id]\n"
                + "      ,[is_flagged]\n"
                + "      ,[content]\n"
                + "      ,[answer_id]\n"
                + "  FROM [dbo].[RecordAnswer]\n"
                + "WHERE record_id = ? AND question_id = ?";

        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, recordId);
            ps.setInt(2, questionId);

            rs = ps.executeQuery();

            while (rs.next()) {

                result = new RecordAnswer(rs.getInt("id"),
                        recordId, questionId,
                        rs.getBoolean("is_flagged"),
                        rs.getString("content"), rs.getInt("answer_id"));
            }

        } catch (SQLException ex) {
            System.out.println("Errors occur in get quiz DAO: " + ex.getMessage());
        } finally {
        }
        return result;
    }

    public HashMap<Integer, RecordResult> GetRecordResult(int recordId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        HashMap<Integer, RecordResult> result = new HashMap<>();
        String sqlCommand = "SELECT [record_id]\n"
                + "      ,[quiz_id]\n"
                + "      ,[question_id]\n"
                + "      ,[order]\n"
                + "      ,[answer_id]\n"
                + "      ,[NumberOrCorrect]\n"
                + "      ,[NumberOfAnswer],Mark\n"
                + "  FROM [dbo].[RecordResult] WHERE record_id = ?";

        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, recordId);

            rs = ps.executeQuery();

            while (rs.next()) {

                RecordResult item = new RecordResult(rs.getInt("record_id"),
                        rs.getInt("quiz_id"),
                        rs.getInt("question_id"),
                        rs.getInt("order"), rs.getInt("answer_id"),
                        rs.getInt("NumberOrCorrect"),
                        rs.getInt("NumberOfAnswer"),
                        rs.getInt("Mark"));
                result.put(item.getOrder(), item);
            }

        } catch (SQLException ex) {
            System.out.println("Errors occur in get quiz DAO: " + ex.getMessage());
        } finally {
        }
        return result;
    }

    public void AddMarkQuestion(int recordId, int questionId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        String sqlCommand = "INSERT INTO [dbo].[RecordMark]\n"
                + "           ([record_id]\n"
                + "           ,[question_id]\n"
                + "           )\n"
                + "     VALUES\n"
                + "           (?,?)";
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, recordId);
            ps.setInt(2, questionId);
            ps.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Errors occur in get user DAO: " + ex.getMessage());
        } finally {

        }
    }

    public boolean GetMarkQuestion(int recordId, int questionId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        boolean result = false;
        String sqlCommand = "SELECT [record_id]\n"
                + "      ,[question_id]\n"
                + "      ,[note]\n"
                + "  FROM [dbo].[RecordMark] WHERE record_id = ? AND question_id = ?";

        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, recordId);
            ps.setInt(2, questionId);

            rs = ps.executeQuery();

            while (rs.next()) {
                result = true;
            }

        } catch (SQLException ex) {
            System.out.println("Errors occur in get quiz DAO: " + ex.getMessage());
        } finally {
        }
        return result;
    }

    public void DeleteRecordMark(int record_id, int questionId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        String sqlCommand = "DELETE FROM [dbo].[RecordMark]\n"
                + "      WHERE  record_id = ? AND question_id = ?";

        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, record_id);
            ps.setInt(2, questionId);

            rs = ps.executeQuery();

        } catch (SQLException ex) {
            System.out.println("Errors occur in get quiz DAO: " + ex.getMessage());
        } finally {
        }

    }

    public QuizRecord CreateNewRecord(int user_id, int quiz_id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        QuizRecord record = null;
        String sqlCommand = "INSERT INTO [dbo].[QuizRecord]\n"
                + "           ([user_id]\n"
                + "           ,[quiz_id]\n"
                + "           ,[created_at])\n"
                + "     VALUES\n"
                + "           (?,?,?)";

        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, user_id);
            ps.setInt(2, quiz_id);
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();

            record = GetQuizRecordByRecordId(GetLastRecordId(user_id, quiz_id));
        } catch (SQLException ex) {
            System.out.println("Errors occur in get quiz DAO: " + ex.getMessage());
        } finally {
        }
        return record;
    }

    public void DeleteRecord(int record_id) {
        // delete all record answer, record mark, and then quiz record
        PreparedStatement ps = null;
        Connection connection = null;

        String sqlCommand = "DELETE FROM [dbo].[RecordAnswer] WHERE record_id = ?";
        String sqlCommand2 = "DELETE FROM [dbo].[RecordMark] WHERE record_id = ?";
        String sqlCommand3 = "DELETE FROM [dbo].[QuizRecord] WHERE record_id = ?";

        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, record_id);
            ps.executeUpdate();

            ps = connection.prepareStatement(sqlCommand2);
            ps.setInt(1, record_id);
            ps.executeUpdate();

            ps = connection.prepareStatement(sqlCommand3);
            ps.setInt(1, record_id);
            ps.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Errors occur in get quiz DAO: " + ex.getMessage());
        } finally {
        }

    }

    private int GetLastRecordId(int user_id, int quiz_id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        int result = 0;
        String sqlCommand = "SELECT MAX(record_id) as record_id FROM [dbo].[QuizRecord] WHERE user_id = ? AND quiz_id = ?";

        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, user_id);
            ps.setInt(2, quiz_id);
            rs = ps.executeQuery();

            while (rs.next()) {
                result = rs.getInt("record_id");
            }

        } catch (SQLException ex) {
            System.out.println("Errors occur in get quiz DAO: " + ex.getMessage());
        } finally {
        }
        return result;
    }

    public boolean CheckUserAccess(int user_id, int quiz_id) {

        // user -> registration -> pricepackage -> subject_id
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        boolean result = false;
        String sqlCommand = "SELECT [dbo].[User].user_id\n"
                + "FROM     [dbo].[User] INNER JOIN\n"
                + "  [dbo].[Registration] ON [dbo].[User].user_id = [dbo].[Registration].user_id INNER JOIN\n"
                + "  [dbo].[PricePackage] ON [dbo].[Registration].package_id = [dbo].[PricePackage].package_id INNER JOIN\n"
                + "  [dbo].[Subject] ON [dbo].[PricePackage].subject_id = [dbo].[Subject].subject_id INNER JOIN\n"
                + "  [dbo].[Quiz] ON [dbo].[Subject].subject_id = [dbo].[Quiz].subject_id\n"
                + "WHERE [dbo].[User].user_id = ? AND [dbo].[Quiz].quiz_id = ? "
                + "AND [dbo].[PricePackage].status = 1 "
                + "AND [dbo].[Registration].status = 1 ";
        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, user_id);
            ps.setInt(2, quiz_id);
            rs = ps.executeQuery();

            while (rs.next()) {
                result = true;
            }

        } catch (SQLException ex) {
            System.out.println("Errors occur in get quiz DAO: " + ex.getMessage());
        } finally {
        }
        return result;

    }

    public void UpdateAnswer(int record_id, int questionId, int answerId) {
        DeleteAnswer(record_id, questionId);
        PreparedStatement ps = null;
        Connection connection = null;

        String sqlCommand = "INSERT INTO [dbo].[RecordAnswer]\n"
                + "           ([record_id]\n"
                + "           ,[question_id]\n"
                + "           ,[answer_id])\n"
                + "     VALUES\n"
                + "           (?,?,?)";

        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, record_id);
            ps.setInt(2, questionId);
            ps.setInt(3, answerId);
            ps.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Errors occur in get quiz DAO: " + ex.getMessage());
        } finally {
        }

    }

    public void UpdateAnswer(int record_id, int questionId, String answer) {
        DeleteAnswer(record_id, questionId);
        if (answer.length() == 0) {
            return;
        }
        PreparedStatement ps = null;
        Connection connection = null;

        String sqlCommand = "INSERT INTO [dbo].[RecordAnswer]\n"
                + "           ([record_id]\n"
                + "           ,[question_id]\n"
                + "           ,[content])\n"
                + "     VALUES\n"
                + "           (?,?,?)";

        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, record_id);
            ps.setInt(2, questionId);
            ps.setString(3, answer);
            ps.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Errors occur in get quiz DAO: " + ex.getMessage());
        } finally {
        }
    }

    public void UpdateAnswer(int record_id, int questionId, int[] answer_ids) {
        DeleteAnswer(record_id, questionId);
        PreparedStatement ps = null;
        Connection connection = null;

        String sqlCommand = "INSERT INTO [dbo].[RecordAnswer]\n"
                + "           ([record_id]\n"
                + "           ,[question_id]\n"
                + "           ,[answer_id])\n"
                + "     VALUES\n"
                + "           (?,?,?)";

        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            for (int i = 0; i < answer_ids.length; i++) {
                ps = connection.prepareStatement(sqlCommand);
                ps.setInt(1, record_id);
                ps.setInt(2, questionId);
                ps.setInt(3, answer_ids[i]);
                ps.executeUpdate();
            }

        } catch (SQLException ex) {
            System.out.println("Errors occur in get quiz DAO: " + ex.getMessage());
        } finally {
        }
    }

    public void DeleteAnswer(int record_id, int question_id) {
        PreparedStatement ps = null;
        Connection connection = null;

        String sqlCommand = "DELETE FROM [dbo].[RecordAnswer] WHERE record_id = ? AND question_id = ?";

        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, record_id);
            ps.setInt(2, question_id);
            ps.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Errors occur in get quiz DAO: " + ex.getMessage());
        } finally {
        }
    }

    public void SubmitRecord(int record_id, int quiz_id) {

        int MAX_TIME = 60; // 1 hour

        Quiz quiz = GetQuizById(quiz_id);
        QuizRecord record = GetQuizRecordByRecordId(record_id);

        // set finished_at = min of the current time and the quiz duration + created_at
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime finishedAt = record.getCreated_at().plusMinutes(quiz.getDuration());

        // if currentTime is after the finishedAt more than 1 minute, that means the submit is late
        boolean isLate = false;
        if (currentTime.isAfter(finishedAt.plusMinutes(1))) {
            isLate = true;
        }

        finishedAt = currentTime;

        // if the quiz is not simulation exam, then finishedAt is not having the above constraint
        if (quiz.getQuizType() != 2) {
            finishedAt = currentTime;
            isLate = false;
            // but if finishedAt is after the created_at + MAX_TIME, then it is late
            if (finishedAt.isAfter(record.getCreated_at().plusMinutes(MAX_TIME))) {
                isLate = true;
            }
        }

        PreparedStatement ps = null;
        Connection connection = null;

        String sqlCommand = "UPDATE [dbo].[QuizRecord]\n"
                + "   SET [score] = ?,\n"
                + "       [finished_at] = ?,\n"
                + "       [status] = ?\n"
                + " WHERE record_id = ?";
        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setFloat(1, GetRecordScore(record_id, quiz_id));
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(finishedAt));
            ps.setInt(3, GetRecordScore(record_id, quiz_id) >= quiz.getPassCondition() ? 1 : 0);
            if (isLate) {
                ps.setInt(3, 3); // late
            }
            ps.setInt(4, record_id);
            ps.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Errors occur in get quiz DAO: " + ex.getMessage());
        } finally {
        }

    }

    public int GetRecordScore(int record_id, int quiz_id) {
        HashMap<Integer, RecordResult> recordres = GetRecordResult(record_id);
        int corrects = recordres.values().stream().filter(x -> x.getNumberOrCorrect() > 0).toList().size();
        int total = recordres.size();
        return corrects * 100 / total;
    }

    // get quiz by subject id
    public List<Quiz> GetLessonQuizzesBySubjectId(int subject_id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        List<Quiz> result = new ArrayList<>();
        String sqlCommand = "SELECT * FROM Quiz WHERE subject_id = ? AND quiz_type = 4";

        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, subject_id);
            rs = ps.executeQuery();

            while (rs.next()) {
                Quiz quiz = new Quiz();
                quiz.setQuizId(rs.getInt("quiz_id"));
                quiz.setName(rs.getString("name"));
                quiz.setDuration(rs.getInt("duration"));
                quiz.setQuizType(rs.getInt("quiz_type"));
                quiz.setNumQuestions(rs.getInt("num_questions"));
                quiz.setLevel(rs.getInt("level"));
                quiz.setStatus(rs.getInt("status"));
                quiz.setPassRate(rs.getInt("pass_rate"));
                quiz.setDescription(rs.getString("description"));
//                quiz.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());
//                quiz.setUpdated_at(rs.getTimestamp("updated_at").toLocalDateTime());
                quiz.setSubjectId(rs.getInt("subject_id"));
                quiz.setPassCondition(rs.getInt("pass_condition"));
                result.add(quiz);
            }

        } catch (SQLException ex) {
            System.out.println("Errors occur in get quiz DAO: " + ex.getMessage());
        } finally {
        }
        return result;
    }

    public List<Quiz> GetSimulationExams(String search_value, int subject_id[], int sort, int page, int page_length) {
        // make a condition for query (where clause)
        String condition = "WHERE status = 1 AND quiz_type = 2";
        // if search value is not empty, add search condition
        if (!search_value.isEmpty()) {
            condition += " AND [name] LIKE '%" + search_value + "%'";
        }
        // if subject_id is not empty, add subject_id condition
        if (subject_id.length > 0) {
            // subject_id is an array
            String subject_condition = "AND (";
            for (int i = 0; i < subject_id.length; i++) {
                subject_condition += "subject_id = " + subject_id[i];
                if (i != subject_id.length - 1) {
                    subject_condition += " OR ";
                }
            }
            subject_condition += ")";
            condition += " " + subject_condition;
        }
        // make a sort condition
        String sort_condition = "";
        switch (sort) {
            case 1:
                sort_condition = "ORDER BY created_at DESC, quiz_id ASC";
                break;
            case 2:
                sort_condition = "ORDER BY name ASC, quiz_id ASC";
                break;
            case 3:
                sort_condition = "ORDER BY pass_rate DESC, quiz_id ASC";
                break;
            case 4:
                sort_condition = "ORDER BY level ASC, quiz_id ASC";
                break;
            default:
                sort_condition = "ORDER BY created_at DESC, quiz_id ASC";
                break;
        }
        // make a limit condition
        String limit_condition = "OFFSET " + (page - 1) * page_length + " ROWS FETCH NEXT " + page_length + " ROWS ONLY";
        // make a query
        String sqlCommand = "SELECT * FROM Quiz " + condition + " " + sort_condition + " " + limit_condition;
        // make a list to store result
        List<Quiz> result = new ArrayList<>();
        // Try-with-resources to ensure the resources are closed after usage
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sqlCommand); ResultSet rs = ps.executeQuery();) {
            // get data from result set
            while (rs.next()) {
                Quiz quiz = new Quiz();
                quiz.setQuizId(rs.getInt("quiz_id"));
                quiz.setName(rs.getString("name"));
                quiz.setDuration(rs.getInt("duration"));
                quiz.setQuizType(rs.getInt("quiz_type"));
                quiz.setNumQuestions(rs.getInt("num_questions"));
                quiz.setLevel(rs.getInt("level"));
                quiz.setStatus(rs.getInt("status"));
                quiz.setPassRate(rs.getInt("pass_rate"));
                quiz.setDescription(rs.getString("description"));
                quiz.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());
                quiz.setUpdated_at(rs.getTimestamp("updated_at").toLocalDateTime());
                quiz.setSubjectId(rs.getInt("subject_id"));
                quiz.setPassCondition(rs.getInt("pass_condition"));
                result.add(quiz);
            }
        } catch (SQLException ex) {
            System.out.println("Errors occur in get quiz DAO: " + ex.getMessage());
        }
        return result;

    }

    public List<Map<String, Object>> getQuizzes(int page, int pageLength, String search, String subjectFilter, String quizTypeFilter, String sortField, String sortOrder, String statusFilter, String quizIdFilter) {
        List<Map<String, Object>> quizzes = new ArrayList<>();
        int offset = (page - 1) * pageLength;

        StringBuilder sql = new StringBuilder("SELECT q.quiz_id, q.name, q.duration, q.num_questions, q.level, q.status, q.pass_rate, q.description, q.created_at, q.updated_at, q.subject_id, q.pass_condition, qt.type AS quizTypeName, s.title AS subjectName, (SELECT COUNT(*) FROM QuizRecord qr WHERE qr.quiz_id = q.quiz_id) AS testCount FROM Quiz q JOIN quiztype qt ON q.quiz_type = qt.id JOIN subject s ON q.subject_id = s.subject_id WHERE q.status = 1");

        if (search != null && !search.isEmpty()) {
            sql.append(" AND q.name LIKE ?");
        }
        if (subjectFilter != null && !subjectFilter.equals("all") && !subjectFilter.isEmpty()) {
            sql.append(" AND s.title IN (").append(formatInClause(subjectFilter)).append(")");
        }
        if (quizTypeFilter != null && !quizTypeFilter.equals("all") && !quizTypeFilter.isEmpty()) {
            sql.append(" AND qt.type IN (").append(formatInClause(quizTypeFilter)).append(")");
        }
        if (quizIdFilter != null && !quizIdFilter.equals("all") && !quizIdFilter.isEmpty()) {
            sql.append(" AND q.quiz_id IN (").append(formatInClause(quizIdFilter)).append(")");
        }
        if (statusFilter.equals("testTaken")) {
            sql.append(" AND (SELECT COUNT(*) FROM QuizRecord qr WHERE qr.quiz_id = q.quiz_id) > 0");
        } else if (statusFilter.equals("noTestTaken")) {
            sql.append(" AND (SELECT COUNT(*) FROM QuizRecord qr WHERE qr.quiz_id = q.quiz_id) = 0");
        }

        // Add dynamic sorting
        sql.append(" ORDER BY ").append(sortField).append(" ").append(sortOrder);

        // Add pagination
        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            if (search != null && !search.isEmpty()) {
                stmt.setString(paramIndex++, "%" + search + "%");
            }
            stmt.setInt(paramIndex++, offset);
            stmt.setInt(paramIndex, pageLength);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> quizData = new HashMap<>();
                quizData.put("quizId", rs.getInt("quiz_id"));
                quizData.put("name", rs.getString("name"));
                quizData.put("duration", rs.getInt("duration"));
                quizData.put("quizType", rs.getString("quizTypeName"));
                quizData.put("numQuestions", rs.getInt("num_questions"));
                quizData.put("level", rs.getInt("level"));
                quizData.put("status", rs.getInt("status"));
                quizData.put("passRate", rs.getInt("pass_rate"));
                quizData.put("description", rs.getString("description"));
                quizData.put("createdAt", rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
                quizData.put("updatedAt", rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
                quizData.put("subjectId", rs.getInt("subject_id"));
                quizData.put("passCondition", rs.getInt("pass_condition"));
                quizData.put("subjectName", rs.getString("subjectName"));
                quizData.put("testCount", rs.getInt("testCount"));

                quizzes.add(quizData);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return quizzes;
    }

    private String formatInClause(String filter) {
        // Split the filter string by comma and wrap each item in single quotes
        String[] items = filter.split(",");
        StringBuilder inClause = new StringBuilder();
        for (String item : items) {
            if (inClause.length() > 0) {
                inClause.append(",");
            }
            inClause.append("'").append(item.trim()).append("'");
        }
        return inClause.toString();
    }

    public int getResultLength(String search, String subjectFilter, String quizTypeFilter, String statusFilter, String quizIdFilter) {
        int resultLength = 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Quiz q JOIN subject s ON q.subject_id = s.subject_id JOIN quiztype qt ON q.quiz_type = qt.id WHERE q.status = 1");

        if (search != null && !search.isEmpty()) {
            sql.append(" AND q.name LIKE ?");
        }
        if (subjectFilter != null && !subjectFilter.equals("all") && !subjectFilter.isEmpty()) {
            sql.append(" AND s.title IN (").append(formatInClause(subjectFilter)).append(")");
        }
        if (quizTypeFilter != null && !quizTypeFilter.equals("all") && !quizTypeFilter.isEmpty()) {
            sql.append(" AND qt.type IN (").append(formatInClause(quizTypeFilter)).append(")");
        }
        if (quizIdFilter != null && !quizIdFilter.equals("all") && !quizIdFilter.isEmpty()) {
            sql.append(" AND q.quiz_id IN (").append(formatInClause(quizIdFilter)).append(")");
        }
        if (statusFilter.equals("testTaken")) {
            sql.append(" AND (SELECT COUNT(*) FROM QuizRecord qr WHERE qr.quiz_id = q.quiz_id) > 0");
        } else if (statusFilter.equals("noTestTaken")) {
            sql.append(" AND (SELECT COUNT(*) FROM QuizRecord qr WHERE qr.quiz_id = q.quiz_id) = 0");
        }

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (search != null && !search.isEmpty()) {
                stmt.setString(paramIndex++, "%" + search + "%");
            }
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                resultLength = rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return resultLength;
    }

    public List<Integer> getAllQuizIds() {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT quiz_id FROM Quiz WHERE status = 1";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getInt("quiz_id"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ids;
    }

    public List<String> getAllSubjects() {
        List<String> subjects = new ArrayList<>();
        String sql = "SELECT DISTINCT s.title FROM subject s JOIN Quiz q ON s.subject_id = q.subject_id WHERE q.status = 1";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                subjects.add(rs.getString("title"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return subjects;
    }

    public List<String> getAllQuizTypes() {
        List<String> quizTypes = new ArrayList<>();
        String sql = "SELECT DISTINCT qt.type FROM quiztype qt JOIN Quiz q ON qt.id = q.quiz_type WHERE q.status = 1";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                quizTypes.add(rs.getString("type"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return quizTypes;
    }

    public void deleteQuiz(int quizId) {
    Connection connection = null;
    PreparedStatement ps = null;

    try {
        connection = getConnection();
        connection.setAutoCommit(false); // Begin transaction

        // Step 1: Delete records in RecordMark that reference records in QuizRecord
        ps = connection.prepareStatement("DELETE FROM RecordMark WHERE record_id IN (SELECT record_id FROM QuizRecord WHERE quiz_id = ?)");
        ps.setInt(1, quizId);
        ps.executeUpdate();

        // Step 2: Delete records in RecordAnswer that reference records in QuizRecord
        ps = connection.prepareStatement("DELETE FROM RecordAnswer WHERE record_id IN (SELECT record_id FROM QuizRecord WHERE quiz_id = ?)");
        ps.setInt(1, quizId);
        ps.executeUpdate();

        // Step 3: Delete records in QuizRecord that reference the quiz
        ps = connection.prepareStatement("DELETE FROM QuizRecord WHERE quiz_id = ?");
        ps.setInt(1, quizId);
        ps.executeUpdate();

        // Step 4: Delete records in Quiz_Question that reference the quiz
        ps = connection.prepareStatement("DELETE FROM Quiz_Question WHERE quiz_id = ?");
        ps.setInt(1, quizId);
        ps.executeUpdate();

        // Step 5: Delete the quiz
        ps = connection.prepareStatement("DELETE FROM Quiz WHERE quiz_id = ?");
        ps.setInt(1, quizId);
        ps.executeUpdate();

        connection.commit(); // Commit transaction
    } catch (SQLException ex) {
        if (connection != null) {
            try {
                connection.rollback(); // Rollback transaction on error
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        ex.printStackTrace();
    } finally {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

    public List<Map<String, Object>> getQuizzesByExpert(int expertId, int page, int pageLength, String search, String subjectFilter, String quizTypeFilter, String sortField, String sortOrder, String statusFilter, String quizIdFilter) {
        List<Map<String, Object>> quizzes = new ArrayList<>();
        int offset = (page - 1) * pageLength;

        StringBuilder sql = new StringBuilder("SELECT q.quiz_id, q.name, q.duration, q.num_questions, q.level, q.status, q.pass_rate, q.description, q.created_at, q.updated_at, q.subject_id, q.pass_condition, qt.type AS quizTypeName, s.title AS subjectName, (SELECT COUNT(*) FROM QuizRecord qr WHERE qr.quiz_id = q.quiz_id) AS testCount FROM Quiz q JOIN quiztype qt ON q.quiz_type = qt.id JOIN subject s ON q.subject_id = s.subject_id JOIN Expert_Subject es ON s.subject_id = es.subject_id WHERE es.user_id = ?");

        if (search != null && !search.isEmpty()) {
            sql.append(" AND q.name LIKE ?");
        }
        if (subjectFilter != null && !subjectFilter.equals("all") && !subjectFilter.isEmpty()) {
            sql.append(" AND s.title IN (").append(formatInClause(subjectFilter)).append(")");
        }
        if (quizTypeFilter != null && !quizTypeFilter.equals("all") && !quizTypeFilter.isEmpty()) {
            sql.append(" AND qt.type IN (").append(formatInClause(quizTypeFilter)).append(")");
        }
        if (quizIdFilter != null && !quizIdFilter.equals("all") && !quizIdFilter.isEmpty()) {
            sql.append(" AND q.quiz_id IN (").append(formatInClause(quizIdFilter)).append(")");
        }
        if (statusFilter.equals("testTaken")) {
            sql.append(" AND (SELECT COUNT(*) FROM QuizRecord qr WHERE qr.quiz_id = q.quiz_id) > 0");
        } else if (statusFilter.equals("noTestTaken")) {
            sql.append(" AND (SELECT COUNT(*) FROM QuizRecord qr WHERE qr.quiz_id = q.quiz_id) = 0");
        }

        sql.append(" ORDER BY ").append(sortField).append(" ").append(sortOrder);
        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            stmt.setInt(paramIndex++, expertId);
            if (search != null && !search.isEmpty()) {
                stmt.setString(paramIndex++, "%" + search + "%");
            }
            stmt.setInt(paramIndex++, offset);
            stmt.setInt(paramIndex, pageLength);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> quizData = new HashMap<>();
                quizData.put("quizId", rs.getInt("quiz_id"));
                quizData.put("name", rs.getString("name"));
                quizData.put("duration", rs.getInt("duration"));
                quizData.put("quizType", rs.getString("quizTypeName"));
                quizData.put("numQuestions", rs.getInt("num_questions"));
                quizData.put("level", rs.getInt("level"));
                quizData.put("status", rs.getInt("status"));
                quizData.put("passRate", rs.getInt("pass_rate"));
                quizData.put("description", rs.getString("description"));
                quizData.put("createdAt", rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
                quizData.put("updatedAt", rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
                quizData.put("subjectId", rs.getInt("subject_id"));
                quizData.put("passCondition", rs.getInt("pass_condition"));
                quizData.put("subjectName", rs.getString("subjectName"));
                quizData.put("testCount", rs.getInt("testCount"));

                quizzes.add(quizData);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return quizzes;
    }
    public static void main(String[] args) {
        QuizDAO quizDAO = new QuizDAO();
        
        int quizIdToDelete = 1; // Replace this with an actual quizId from your database that you want to delete
        
        try {
            quizDAO.deleteQuiz(quizIdToDelete);
            System.out.println("Quiz with ID " + quizIdToDelete + " deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error deleting quiz with ID " + quizIdToDelete + ": " + e.getMessage());
        }
    }
}

