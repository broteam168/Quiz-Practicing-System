package DAOs;

import Contexts.DBContext;
import Models.SubjectDimension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DimensionDAO extends DBContext {
    
    public List<SubjectDimension> GetDimensionBySubject(int subjectId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        
        List<SubjectDimension> dimensions = new ArrayList<>();
        String sqlCommand = "SELECT [dimension_id]\n"
                + "      ,[type_id]\n"
                + "      ,[subject_id]\n"
                + "      ,[name]\n"
                + "  FROM [dbo].[SubjectDimension]\n"
                + "  WHERE subject_id = ?";

        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, subjectId);
            rs = ps.executeQuery();
            SubjectDimension dimension = null;
            while (rs.next()) {
                
                dimension = new SubjectDimension(rs.getInt("dimension_id"),
                        rs.getInt("type_id"), rs.getInt("subject_id"),
                        rs.getString("name"));
                dimensions.add(dimension);
            }
            
        } catch (SQLException ex) {
            System.out.println("Errors occur in get dimension DAO: " + ex.getMessage());
        } finally {
        }
        return dimensions;
    }
}
