package DAOs;

import Contexts.DBContext;
import Models.Level;
import Models.Quiz.Answer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LevelDAO extends DBContext {

    public List<Level> GetLevel() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        List<Level> results = new ArrayList<>();

        String sqlCommand = "SELECT  [id]\n"
                + "      ,[type]\n"
                + "      ,[description]\n"
                + "  FROM [dbo].[LevelType]";

        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
           

            rs = ps.executeQuery();
            Level tempLevel = null;
            while (rs.next()) {

                tempLevel = new Level(rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("description"));
                results.add(tempLevel);
            }

        } catch (SQLException ex) {
            System.out.println("Errors occur in get Level DAO: " + ex.getMessage());
        } finally {
        }
        return results;
    }
}
