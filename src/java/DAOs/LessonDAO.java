package DAOs;

import Contexts.DBContext;
import Models.Lesson;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;


public class LessonDAO extends DBContext {
    
    public List<Lesson> GetLessonsList(int subject_id, int order, int topic_id, int lesson_type, int status, String name, int sort, int length, int page) {
        List<Lesson> lessons = new ArrayList<>();
        String filterOption = "subject_id = " + subject_id;
        if (order != 0) {
            filterOption += " AND [order] = " + order;
        }
        if (topic_id != 0) {
            filterOption += " AND [topic_id] = " + topic_id;
        }
        if (lesson_type != 0) {
            filterOption += " AND lesson_type = " + lesson_type;
        }
        if (status != -1) {
            filterOption += " AND status = " + status;
        }
        if (!name.equals("")) {
            filterOption += " AND name LIKE '%" + name + "%'";
        }
        // sort option, 1 is sort by order asc, 2 is sort by order desc, default is sort by order
        // use string sortOption for this
        String sortOption = "";
        sortOption = switch (sort) {
            case 1 ->
                " ORDER BY [topic_id] ASC, [order] ASC, lesson_type ASC, lesson_id ASC";
            case 2 ->
                " ORDER BY [order]";
            default ->
                " ORDER BY [topic_id] ASC, [order] ASC, lesson_type ASC, lesson_id ASC";
        };

        // offset part for pagination, use fetch next
        // calculate offset
        int offset = (page - 1) * length;
        String offsetPart = " OFFSET " + offset + " ROWS FETCH NEXT " + length + " ROWS ONLY";

        try {
            // get connection
            Connection conn = getConnection();
            // query string
            String query = "SELECT * FROM subjectlesson WHERE " + filterOption + sortOption + offsetPart;
            System.out.println("query in lesson dao: " + query);
            // prepare statement
            PreparedStatement ps = conn.prepareStatement(query);
            // execute query
            ResultSet rs = ps.executeQuery();
            // get data
            while (rs.next()) {
                Lesson lesson = new Lesson();
                lesson.setLesson_id(rs.getInt("lesson_id"));
                lesson.setSubject_id(rs.getInt("subject_id"));
                lesson.setOrder(rs.getInt("order"));
                lesson.setLesson_type(rs.getInt("lesson_type"));
                lesson.setName(rs.getString("name"));
                lesson.setContent(rs.getString("content"));
                lesson.setBack_link(rs.getString("back_link"));
                lesson.setStatus(rs.getInt("status"));
                lessons.add(lesson);
            }
            // close connection
            rs.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return lessons;
    }

    // get lesson by id
    public Lesson GetLessonById(int lesson_id) {
        Lesson lesson = new Lesson();
        try {
            // get connection
            Connection conn = getConnection();
            // query string
            String query = "SELECT * FROM subjectlesson WHERE lesson_id = ?;";
            // prepare statement
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, lesson_id);
            // execute query
            ResultSet rs = ps.executeQuery();
            // get data
            if (rs.next()) {
                lesson.setLesson_id(rs.getInt("lesson_id"));
                lesson.setSubject_id(rs.getInt("subject_id"));
                lesson.setOrder(rs.getInt("order"));
                lesson.setLesson_type(rs.getInt("lesson_type"));
                lesson.setName(rs.getString("name"));
                lesson.setContent(rs.getString("content"));
                lesson.setBack_link(rs.getString("back_link"));
                lesson.setStatus(rs.getInt("status"));
                lesson.setTopic_id(rs.getInt("topic_id"));
                lesson.setQuiz_id(rs.getInt("quiz_id"));
                lesson.setDescription(rs.getString("description"));
            }
            // close connection
            rs.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return lesson;
    }

    public boolean CheckExpertAccess(int user_id, int subject_id) {
        boolean result = false;
        try {
            // get connection
            Connection conn = getConnection();
            // query string
            String query = "EXEC CheckEditAccessToSubject @user_id = ? , @subject_id = ? ;";
            // prepare statement
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, user_id);
            ps.setInt(2, subject_id);
            // execute query
            ResultSet rs = ps.executeQuery();
            // get data
            if (rs.next()) {
                result = rs.getBoolean(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return result;
    }


    public List<Lesson> GetLessonBySubject(int subjectId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        List<Lesson> lessons = new ArrayList<>();
        String sqlCommand = "SELECT  [lesson_id]\n"
                + "      ,[lesson_type]\n"
                + "      ,[name]\n"
                + "      ,[topic_id]\n"
                + "      ,[subject_id]\n"
                + "      ,[status]\n"
                + "      ,[back_link]\n"
                + "      ,[content]\n"
                + "      ,[order]\n"
                + "      ,[description]\n"
                + "      ,[quiz_id]\n"
                + "  FROM [SubjectLesson]"
                + "WHERE subject_id = ?";

        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, subjectId);
            rs = ps.executeQuery();
            Lesson lesson = null;
            while (rs.next()) {
                lesson = new Lesson();
                lesson.setLesson_id(rs.getInt("lesson_id"));
                lesson.setSubject_id(rs.getInt("subject_id"));
                lesson.setOrder(rs.getInt("order"));
                lesson.setLesson_type(rs.getInt("lesson_type"));
                lesson.setName(rs.getString("name"));
                lesson.setContent(rs.getString("content"));
                lesson.setStatus(rs.getInt("status"));
                lessons.add(lesson);
            }

        } catch (SQLException ex) {
            System.out.println("Errors occur in get lesson DAO: " + ex.getMessage());
        } finally {
        }
        return lessons;
    }
    public void AddTopic(String topic_name, int subject_id) {
        try {
            // get connection
            Connection conn = getConnection();
            // query string
            String query = "INSERT INTO SubjectTopic (name, subject_id) VALUES (?, ?);";
            // prepare statement
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, topic_name);
            ps.setInt(2, subject_id);
            // execute query
            ps.executeUpdate();
            // close connection
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // update topic
    public void UpdateTopic(int topic_id, String topic_name) {
        try {
            // get connection
            Connection conn = getConnection();
            // query string
            String query = "UPDATE SubjectTopic SET name = ? WHERE id = ?;";
            // prepare statement
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, topic_name);
            ps.setInt(2, topic_id);
            // execute query
            ps.executeUpdate();
            // close connection
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean DeleteTopic(int topic_id) {
        try {
            // get connection
            Connection conn = getConnection();
            // query string
            String query = "DELETE FROM SubjectTopic WHERE id = ?;";
            // prepare statement
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, topic_id);
            // execute query
            ps.executeUpdate();
            // close connection
            conn.close();
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // check existed topic name in same subject
    public boolean CheckExistedTopicName(String topic_name, int subject_id) {
        boolean result = false;
        try {
            // get connection
            Connection conn = getConnection();
            // query string
            String query = "SELECT * FROM SubjectTopic WHERE name = ? AND subject_id = ?;";
            // prepare statement
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, topic_name);
            ps.setInt(2, subject_id);
            // execute query
            ResultSet rs = ps.executeQuery();
            // get data
            if (rs.next()) {
                result = true;
            }
            // close connection
            rs.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return result;
    }

    public int GetLessonCountByTopicId(int topic_id){
        int count = 0;
        try {
            // get connection
            Connection conn = getConnection();
            // query string
            String query = "SELECT COUNT(lesson_id) AS count FROM subjectlesson WHERE topic_id = ?;";
            // prepare statement
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, topic_id);
            // execute query
            ResultSet rs = ps.executeQuery();
            // get data
            if (rs.next()) {
                count = rs.getInt("count");
            }
            // close connection
            rs.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return count;
    }

    // get newest topic id
    public int GetNewestTopicId() {
        int topic_id = 0;
        try {
            // get connection
            Connection conn = getConnection();
            // query string
            String query = "SELECT TOP 1 id FROM SubjectTopic ORDER BY id DESC;";
            // prepare statement
            PreparedStatement ps = conn.prepareStatement(query);
            // execute query
            ResultSet rs = ps.executeQuery();
            // get data
            if (rs.next()) {
                topic_id = rs.getInt("id");
            }
            // close connection
            rs.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return topic_id;
    }

    public void AddLesson(Lesson lesson) {
        try {
            // get connection
            Connection conn = getConnection();
            // query string
            String query = "INSERT INTO subjectlesson (subject_id, [order], lesson_type, name, content, back_link, status, topic_id, quiz_id, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            // prepare statement
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, lesson.getSubject_id());
            ps.setInt(2, lesson.getOrder());
            ps.setInt(3, lesson.getLesson_type());
            ps.setString(4, lesson.getName());
            if (lesson.getLesson_type() == 2) {
                ps.setString(5, lesson.getContent());
                ps.setString(6, lesson.getBack_link());
            } else {
                ps.setString(5, "");
                ps.setString(6, "");
            }

            ps.setInt(7, lesson.getStatus());
            ps.setInt(8, lesson.getTopic_id());
            if (lesson.getLesson_type() == 3) {
                ps.setInt(9, lesson.getQuiz_id());
            } else {
                ps.setNull(9, Types.NULL);
            }
            ps.setString(10, lesson.getDescription());

            System.out.println(ps.toString());
            // execute query
            ps.executeUpdate();
            // close connection
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int GetNewestLessonId() {
        int lesson_id = 0;
        try {
            // get connection
            Connection conn = getConnection();
            // query string
            String query = "SELECT TOP 1 lesson_id FROM subjectlesson ORDER BY lesson_id DESC;";
            // prepare statement
            PreparedStatement ps = conn.prepareStatement(query);
            // execute query
            ResultSet rs = ps.executeQuery();
            // get data
            if (rs.next()) {
                lesson_id = rs.getInt("lesson_id");
            }
            // close connection
            rs.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return lesson_id;
    }

    public void UpdateLesson(Lesson lesson) {
        try {
            // get connection
            Connection conn = getConnection();
            // query string
            String query = "UPDATE subjectlesson SET [order] = ?, lesson_type = ?, name = ?, content = ?, back_link = ?, status = ?, topic_id = ?, quiz_id = ?, description = ? WHERE lesson_id = ?;";
            // prepare statement
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, lesson.getOrder());
            ps.setInt(2, lesson.getLesson_type());
            ps.setString(3, lesson.getName());
            if (lesson.getLesson_type() == 2) {
                ps.setString(4, lesson.getContent());
                ps.setString(5, lesson.getBack_link());
            } else {
                ps.setString(4, "");
                ps.setString(5, "");
            }
            ps.setInt(6, lesson.getStatus());
            ps.setInt(7, lesson.getTopic_id());
            if (lesson.getLesson_type() == 3) {
                ps.setInt(8, lesson.getQuiz_id());
            } else {
                ps.setNull(8, Types.NULL);
            }
            ps.setString(9, lesson.getDescription());
            ps.setInt(10, lesson.getLesson_id());
            // execute query
            ps.executeUpdate();
            // close connection
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // check existed lesson name in same topic
    public boolean CheckExistedLessonName(String lesson_name, int topic_id) {
        boolean result = false;
        try {
            // get connection
            Connection conn = getConnection();
            // query string
            String query = "SELECT * FROM subjectlesson WHERE name = ? AND topic_id = ?;";
            // prepare statement
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, lesson_name);
            ps.setInt(2, topic_id);
            // execute query
            ResultSet rs = ps.executeQuery();
            // get data
            if (rs.next()) {
                result = true;
            }
            // close connection
            rs.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return result;

    }

    // check existed lesson name in same topic, but pass if the duplicated id is the same as the lesson id
    public boolean CheckExistedLessonName(String lesson_name, int lesson_id, int topic_id) {
        boolean result = false;
        try {
            // get connection
            Connection conn = getConnection();
            // query string
            String query = "SELECT * FROM subjectlesson WHERE name = ? AND topic_id = ? AND lesson_id != ?;";
            // prepare statement
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, lesson_name);
            ps.setInt(2, topic_id);
            ps.setInt(3, lesson_id);
            // execute query
            ResultSet rs = ps.executeQuery();
            // get data
            if (rs.next()) {
                result = true;
            }
            // close connection
            rs.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return result;
    }

    // main to test
    public static void main(String[] args) {
        LessonDAO lessonDAO = new LessonDAO();

        // test delete topic
        lessonDAO.DeleteTopic(44);

    }
}
