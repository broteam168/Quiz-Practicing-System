package DAOs;

import Contexts.DBContext;
import Models.PricePackage;
import Models.Role;
import Models.Subject;
import Models.SubjectCategory;
import Models.User;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SubjectDAO extends DBContext {

    public static final int SORT_BY_UPDATE = 1;
    public static final int SORT_BY_TITLE = 2;
    public static final int SORT_BY_PRICE = 3;

    public static final int SEARCH_ALL = 1;
    public static final int SEARCH_BY_TITLE = 2;
    public static final int SEARCH_BY_FEATURED = 3;

    public List<Subject> GetSubjectByRole(int role, int userId) throws SQLException {
        if (role == Role.ROLE_ADMIN) {
            List<Subject> subjects = new ArrayList<>();
            String query = "SELECT subject_id, "
                    + "thumbnail, title, tag_line, description "
                    + "FROM Subject";

            // Try-with-resources to ensure the resources are closed after usage
            try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

                // Loop through the result set and populate the list of subjects
                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setSubject_id(rs.getInt("subject_id"));
                    subject.setThumbnail(rs.getString("thumbnail"));
                    subject.setTitle(rs.getString("title"));
                    subject.setTag_line(rs.getString("tag_line"));
                    subject.setDescription(rs.getString("description"));
                    subjects.add(subject);
                }
            }
            return subjects;
        } else {
            List<Subject> subjects = new ArrayList<>();
            String query = "SELECT [Subject].[subject_id], ";
            if (role == Role.ROLE_EXPERT) {
                query += "Expert_Subject.user_id, ";
            }
            query += "thumbnail, title, tag_line, description ";
            query += "FROM Subject";
            if (role == Role.ROLE_EXPERT) {
                query += "  LEFT JOIN [Expert_Subject] ON [Expert_Subject].[subject_id] = [Subject].[subject_id]\n";
            }
            if (role == Role.ROLE_EXPERT) {
                query += "WHERE [user_id] = " + userId;
            }
            System.out.println(query);
            // Try-with-resources to ensure the resources are closed after usage
            try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

                // Loop through the result set and populate the list of subjects
                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setSubject_id(rs.getInt("subject_id"));
                    subject.setThumbnail(rs.getString("thumbnail"));
                    subject.setTitle(rs.getString("title"));
                    subject.setTag_line(rs.getString("tag_line"));
                    subject.setDescription(rs.getString("description"));
                    subjects.add(subject);
                }
            }
            return subjects;
        }

    }

    public List<Subject> GetFeaturedSubjects() throws SQLException {
        List<Subject> subjects = new ArrayList<>();
        String query = "SELECT subject_id, "
                + "thumbnail, title, tag_line, description "
                + "FROM Subject WHERE status = 1 AND is_featured = 1";

        // Try-with-resources to ensure the resources are closed after usage
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            // Loop through the result set and populate the list of subjects
            while (rs.next()) {
                Subject subject = new Subject();
                subject.setSubject_id(rs.getInt("subject_id"));
                subject.setThumbnail(rs.getString("thumbnail"));
                subject.setTitle(rs.getString("title"));
                subject.setTag_line(rs.getString("tag_line"));
                subject.setDescription(rs.getString("description"));
                subjects.add(subject);
            }
        }
        return subjects;
    }

    // Method to get featured subjects (status = 1) ordered randomly
    public List<Subject> GetRandomFeaturedSubjects(int amount) throws SQLException {
        List<Subject> subjects = new ArrayList<>();
        String query = "SELECT TOP " + amount + " subject_id, "
                + "thumbnail, title, tag_line, description "
                + "FROM Subject WHERE status = 1 AND is_featured = 1 ORDER BY NEWID()";

        // Try-with-resources to ensure the resources are closed after usage
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            // Loop through the result set and populate the list of subjects
            while (rs.next()) {
                Subject subject = new Subject();
                subject.setSubject_id(rs.getInt("subject_id"));
                subject.setThumbnail(rs.getString("thumbnail"));
                subject.setTitle(rs.getString("title"));
                subject.setTag_line(rs.getString("tag_line"));
                subject.setDescription(rs.getString("description"));
                subjects.add(subject);
            }
        }
        return subjects;
    }

    // method to get the list of published subjects from the database paginated
    public List<Subject> GetPublishedSubjects(int search, int sort, int page, int pageLength, String searchValue, String categories) {

        List<Subject> subjects = new ArrayList<>();

        String searchOption = GetSearchOption(search);
        String sortOption = GetSortOption(sort);
        String categoryOption = GetCategoryOption(categories);

        int offset = GetOffset(page, pageLength);

        String query = "SELECT DISTINCT "
                + "    s.subject_id,"
                + "    s.title,"
                + "    s.thumbnail,"
                + "    s.tag_line,"
                + "    s.status,"
                + "    s.created_at,"
                + "    s.updated_at,"
                + "    CAST(s.description AS varchar(MAX)) AS description,"
                + "    s.is_featured,"
                + "    s.category_id,"
                + "    pp.list_price,"
                + "    pp.sale_price "
                + "FROM "
                + "    [dbo].[Subject] s "
                + "JOIN "
                + "    [dbo].[PricePackage] pp "
                + "    ON s.subject_id = pp.subject_id "
                + "WHERE "
                + "    pp.sale_price = ( "
                + "        SELECT "
                + "            MIN(sub_pp.sale_price) "
                + "        FROM "
                + "            [dbo].[PricePackage] sub_pp "
                + "        WHERE "
                + "            sub_pp.subject_id = s.subject_id "
                + "         AND pp.status = 1 ) "
                + searchOption + " "
                + categoryOption + " "
                + sortOption + " "
                + "OFFSET ? ROWS "
                + "FETCH NEXT ? ROWS ONLY ";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            // if search option is search by title, modify the search value
            if (search == SEARCH_BY_TITLE) {
                searchValue = "%" + searchValue + "%";
            }

            // if search option is search all, there will be no search_value
            if (search == SEARCH_ALL) {
                stmt.setInt(1, offset);
                stmt.setInt(2, pageLength);
            } else {
                stmt.setString(1, searchValue);
                stmt.setInt(2, offset);
                stmt.setInt(3, pageLength);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

                LocalDateTime create = null;
                LocalDateTime update = null;

                if (rs.getDate("created_at") != null) {
                    create = rs.getTimestamp("created_at").toLocalDateTime();
                }
                if (rs.getDate("updated_at") != null) {
                    update = rs.getTimestamp("updated_at").toLocalDateTime();
                }

                Subject subject = new Subject();
                subject.setSubject_id(rs.getInt("subject_id"));
                subject.setTitle(rs.getString("title"));
                subject.setThumbnail(rs.getString("thumbnail"));
                subject.setTag_line(rs.getString("tag_line"));
                subject.setStatus(rs.getInt("status"));
                subject.setCreated_at(create);
                subject.setUpdated_at(update);
                subject.setDescription(rs.getString("description"));
                subject.setIs_featured(rs.getBoolean("is_featured"));
                subject.setCategory_id(rs.getInt("category_id"));
                subject.setList_price(BigDecimal.valueOf(rs.getDouble("list_price")));
                subject.setSale_price(BigDecimal.valueOf(rs.getDouble("sale_price")));

                subjects.add(subject);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return subjects;
    }

    public Subject getSubjectById(int id) {
        Subject subject = null;

        String query = "SELECT DISTINCT "
                + "    s.subject_id,"
                + "    s.title,"
                + "    s.thumbnail,"
                + "    s.tag_line,"
                + "    s.status,"
                + "    s.created_at,"
                + "    s.updated_at,"
                + "    CAST(s.description AS varchar(MAX)) AS description,"
                + "    s.is_featured,"
                + "    s.category_id,"
                + "    pp.list_price,"
                + "    pp.sale_price "
                + "FROM "
                + "    [dbo].[Subject] s "
                + "JOIN "
                + "    [dbo].[PricePackage] pp "
                + "    ON s.subject_id = pp.subject_id "
                + "WHERE "
                + "    s.subject_id = ? "
                + "    AND pp.sale_price = ( "
                + "        SELECT "
                + "            MIN(sub_pp.sale_price) "
                + "        FROM "
                + "            [dbo].[PricePackage] sub_pp "
                + "        WHERE "
                + "            sub_pp.subject_id = s.subject_id "
                + "         AND pp.status = 1 )";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {

                LocalDateTime create = null;
                LocalDateTime update = null;

                if (rs.getDate("created_at") != null) {
                    create = rs.getTimestamp("created_at").toLocalDateTime();
                }
                if (rs.getDate("updated_at") != null) {
                    update = rs.getTimestamp("updated_at").toLocalDateTime();
                }
                subject = new Subject();
                subject.setSubject_id(rs.getInt("subject_id"));
                subject.setTitle(rs.getString("title"));
                subject.setThumbnail(rs.getString("thumbnail"));
                subject.setTag_line(rs.getString("tag_line"));
                subject.setStatus(rs.getInt("status"));
                subject.setCreated_at(create);
                subject.setUpdated_at(update);
                subject.setDescription(rs.getString("description"));
                subject.setIs_featured(rs.getBoolean("is_featured"));
                subject.setCategory_id(rs.getInt("category_id"));
                subject.setList_price(BigDecimal.valueOf(rs.getDouble("list_price")));
                subject.setSale_price(BigDecimal.valueOf(rs.getDouble("sale_price")));

            }

        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return subject;
    }

    public List<PricePackage> getPricePackagesBySubjectId(int subjectId) throws SQLException {
        List<PricePackage> pricePackages = new ArrayList<>();
        String query = "SELECT * "
                + "     FROM [PricePackage] "
                + "     WHERE [subject_id] = ? "
                + "     AND [status] = 1";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, subjectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PricePackage pricePackage = new PricePackage();
                pricePackage.setPackage_id(rs.getInt("package_id"));
                pricePackage.setSubject_id(rs.getInt("subject_id"));
                pricePackage.setName(rs.getString("name"));
                pricePackage.setList_price(BigDecimal.valueOf(rs.getDouble("list_price")));
                pricePackage.setSale_price(BigDecimal.valueOf(rs.getDouble("sale_price")));
                pricePackage.setDuration(rs.getInt("duration"));
                pricePackages.add(pricePackage);
            }

        }
        return pricePackages;
    }

    public String GetSearchOption(int search) {
        switch (search) {
            case SEARCH_BY_TITLE -> {
                return "AND title LIKE ? ";
            }
            case SEARCH_BY_FEATURED -> {
                return "AND is_featured = ?";
            }
            case SEARCH_ALL -> {
                return "";
            }
            default ->
                throw new Error("GetSearchOption failed");
        }
    }

    public String GetSortOption(int sort) {

        switch (sort) {
            case SORT_BY_UPDATE -> {
                return "ORDER BY updated_at DESC";
            }
            case SORT_BY_TITLE -> {
                return "ORDER BY title ASC";
            }
            case SORT_BY_PRICE -> {
                return "ORDER BY sale_price ASC";
            }
            default ->
                throw new Error("GetSearchOption failed");
        }
    }

    public String GetCategoryOption(String categories) {
        try {
            if (categories != null && !categories.isEmpty()) {
                String[] rawCategoryIds = categories.split(" ");
                int[] categoryIds = new int[rawCategoryIds.length];
                for (int i = 0; i < categoryIds.length; i++) {
                    categoryIds[i] = Integer.parseInt(rawCategoryIds[i]);
                }
                StringBuilder categoryOption = new StringBuilder("AND s.category_id IN (");
                for (int i = 0; i < categoryIds.length; i++) {
                    categoryOption.append(categoryIds[i]);
                    if (i < categoryIds.length - 1) {
                        categoryOption.append(",");
                    }
                }
                categoryOption.append(")");
                return categoryOption.toString();
            }

            return "";
        } catch (NumberFormatException e) {
            return "";
        }

    }

    public int GetOffset(int page, int length) {

        return (page - 1) * length;
    }

    public int GetResultLength(int search, int sort, String searchValue, String categories) {
        List<Subject> list = GetPublishedSubjects(search, sort, 1, Integer.MAX_VALUE, searchValue, categories);

        return list.size();
    }

    public Subject GetPublishedSubjectById(int subject) {
        Subject s = new Subject();
        String query = "SELECT * FROM Subject WHERE subject_id = ? AND status = 1";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, subject);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LocalDateTime create = null;
                LocalDateTime update = null;

                if (rs.getDate("created_at") != null) {
                    create = rs.getTimestamp("created_at").toLocalDateTime();
                }
                if (rs.getDate("updated_at") != null) {
                    update = rs.getTimestamp("updated_at").toLocalDateTime();
                }

                s.setSubject_id(rs.getInt("subject_id"));
                s.setTitle(rs.getString("title"));
                s.setThumbnail(rs.getString("thumbnail"));
                s.setTag_line(rs.getString("tag_line"));
                s.setStatus(rs.getInt("status"));
                s.setCreated_at(create);
                s.setUpdated_at(update);
                s.setDescription(rs.getString("description"));
                s.setIs_featured(rs.getBoolean("is_featured"));
                s.setCategory_id(rs.getInt("category_id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }

    public int GetTotalSubjectsCount() {
        int totalSubjectsCount = 0;
        String query = "SELECT COUNT(subject_id) AS [totalSubjectsCount]"
                + "     FROM [Subject];";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                totalSubjectsCount = rs.getInt("totalSubjectsCount");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return totalSubjectsCount;
    }

    public int GetTodayNewSubjectsCount() {
        int newSubjectsCount = 0;
        String query = "SELECT COUNT(subject_id) AS [newSubjectsCount]"
                + "     FROM [Subject]"
                + "     WHERE CONVERT(DATE, created_at) = CONVERT(DATE, GETDATE());";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                newSubjectsCount = rs.getInt("newSubjectsCount");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newSubjectsCount;
    }

    public boolean CheckUserAccess(int user_id, int subject_id) {

        // user -> registration -> pricepackage -> subject_id
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        boolean result = false;
        String sqlCommand = "SELECT [dbo].[User].user_id\n"
                + "FROM     [dbo].[User] INNER JOIN\n"
                + "  [dbo].[Registration] ON [dbo].[User].user_id = [dbo].[Registration].user_id INNER JOIN\n"
                + "  [dbo].[PricePackage] ON [dbo].[Registration].package_id = [dbo].[PricePackage].package_id INNER JOIN\n"
                + "  [dbo].[Subject] ON [dbo].[PricePackage].subject_id = [dbo].[Subject].subject_id \n"
                + "WHERE [dbo].[User].user_id = ? AND [dbo].[Subject].subject_id = ? "
                + "AND [dbo].[PricePackage].status = 1 "
                + "AND [dbo].[Registration].status = 1 ";
        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, user_id);
            ps.setInt(2, subject_id);
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

    public List<Subject> GetRegisteredSubjects(int user_id) {

        // user -> registration -> pricepackage -> subject_id
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;

        List<Subject> subjects = new ArrayList();

        boolean result = false;
        String sqlCommand = "SELECT DISTINCT "
                + "[dbo].[Subject].subject_id, \n"
                + "[dbo].[Subject].title \n"
                + "FROM     [dbo].[User] INNER JOIN\n"
                + "  [dbo].[Registration] ON [dbo].[User].user_id = [dbo].[Registration].user_id INNER JOIN\n"
                + "  [dbo].[PricePackage] ON [dbo].[Registration].package_id = [dbo].[PricePackage].package_id INNER JOIN\n"
                + "  [dbo].[Subject] ON [dbo].[PricePackage].subject_id = [dbo].[Subject].subject_id \n"
                + "WHERE [dbo].[User].user_id = ? "
                + "AND [dbo].[PricePackage].status = 1 "
                + "AND [dbo].[Registration].status = 1 ";
        // Try-with-resources to ensure the resources are closed after usage
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sqlCommand);
            ps.setInt(1, user_id);
            rs = ps.executeQuery();

            while (rs.next()) {

                Subject subject = new Subject();
                subject.setSubject_id(rs.getInt("subject_id"));
                subject.setTitle(rs.getString("title"));

                subjects.add(subject);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
        }
        return subjects;

    }

    public static void main(String[] args) throws SQLException {
        SubjectDAO dao = new SubjectDAO();
        List<Subject> list = dao.GetRegisteredSubjects(6);
        for (Subject subject : list) {
            System.out.println(subject.getSubject_id());
        }
    }

    public void registerSubjects(int userId, int subjectId, int pricePackageId) throws SQLException {
        String query = "INSERT INTO registered_subjects (user_id, subject_id, price_package_id, registration_time) VALUES (?, ?, ?, NOW())";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            stmt.setInt(1, userId);
            stmt.setInt(2, subjectId);
            stmt.setInt(3, pricePackageId);
            stmt.executeUpdate();
        }
    }

    public void updateRegistration(Subject registedSubjects) throws SQLException {
        String query = "UPDATE registered_subjects SET total_cost = ?, valid_from = ?, valid_to = ?, status = ? WHERE subject_id = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            stmt.setBigDecimal(1, BigDecimal.valueOf(rs.getDouble("total_cost")));
            stmt.setString(2, registedSubjects.getValid_from());
            stmt.setString(3, registedSubjects.getValid_to());
            stmt.setInt(4, registedSubjects.getStatus());
            stmt.setInt(5, registedSubjects.getSubject_id());

            stmt.executeUpdate();
        }
    }

    public List<Subject> getSubjectsByRole(int limit, int offset, String categoryFilter, String statusFilter, String search, String sortOrder, List<String> owners, List<Integer> subjectIds, User currentUser) {
        List<Subject> subjects = new ArrayList<>();
        StringBuilder query = new StringBuilder(
                "SELECT s.subject_id, s.title, s.status, "
                + "(SELECT COUNT(*) FROM SubjectLesson sl WHERE sl.subject_id = s.subject_id) AS num_lessons, "
                + "sc.category_id, sc.category_name, "
                + "STRING_AGG(u.full_name, ', ') AS owners "
                + "FROM Subject s "
                + "JOIN SubjectCategory sc ON s.category_id = sc.category_id "
                + "LEFT JOIN Expert_Subject es ON s.subject_id = es.subject_id "
                + "LEFT JOIN [User] u ON es.user_id = u.user_id AND u.role IN (4, 5) "
                + "WHERE 1=1 ");

        if (subjectIds != null && !subjectIds.isEmpty()) {
            query.append("AND s.subject_id IN (");
            for (int i = 0; i < subjectIds.size(); i++) {
                query.append("?");
                if (i < subjectIds.size() - 1) {
                    query.append(", ");
                }
            }
            query.append(") ");
        }

        if (categoryFilter != null && !categoryFilter.equals("all")) {
            query.append("AND sc.category_name IN (");
            String[] categories = categoryFilter.split(",");
            for (int i = 0; i < categories.length; i++) {
                query.append("?");
                if (i < categories.length - 1) {
                    query.append(", ");
                }
            }
            query.append(") ");
        }

        if (!statusFilter.equals("all")) {
            query.append("AND s.status = ").append(statusFilter).append(" ");
        }

        if (search != null && !search.isEmpty()) {
            query.append("AND s.title LIKE ? ");
        }

        if (currentUser.getRole() == Role.ROLE_EXPERT) {
            query.append("AND u.user_id = ? ");
        }

        if (owners != null && !owners.isEmpty()) {
            query.append("AND (");
            for (int i = 0; i < owners.size(); i++) {
                query.append("u.full_name LIKE ?");
                if (i < owners.size() - 1) {
                    query.append(" OR ");
                }
            }
            query.append(") ");
        }

        if ("asc".equals(sortOrder)) {
            query.append("GROUP BY s.subject_id, s.title, s.status, sc.category_id, sc.category_name ORDER BY num_lessons ASC ");
        } else if ("desc".equals(sortOrder)) {
            query.append("GROUP BY s.subject_id, s.title, s.status, sc.category_id, sc.category_name ORDER BY num_lessons DESC ");
        } else {
            query.append("GROUP BY s.subject_id, s.title, s.status, sc.category_id, sc.category_name ORDER BY s.subject_id ");
        }

        query.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query.toString())) {
            int paramIndex = 1;

            if (subjectIds != null && !subjectIds.isEmpty()) {
                for (Integer id : subjectIds) {
                    ps.setInt(paramIndex++, id);
                }
            }

            if (categoryFilter != null && !categoryFilter.equals("all")) {
                String[] categories = categoryFilter.split(",");
                for (String category : categories) {
                    ps.setString(paramIndex++, category);
                }
            }

            if (search != null && !search.isEmpty()) {
                ps.setString(paramIndex++, "%" + search + "%");
            }

            if (currentUser.getRole() == Role.ROLE_EXPERT) {
                ps.setInt(paramIndex++, currentUser.getUser_id());
            }

            if (owners != null && !owners.isEmpty()) {
                for (String owner : owners) {
                    ps.setString(paramIndex++, "%" + owner + "%");
                }
            }

            ps.setInt(paramIndex++, offset);
            ps.setInt(paramIndex, limit);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Subject subject = new Subject();
                subject.setSubject_id(rs.getInt("subject_id"));
                subject.setTitle(rs.getString("title"));
                subject.setNumLessons(rs.getInt("num_lessons"));
                subject.setStatus(rs.getInt("status"));

                SubjectCategory category = new SubjectCategory();
                category.setCategory_id(rs.getInt("category_id"));
                category.setCategory_name(rs.getString("category_name"));

                subject.setCategory(category);
                subject.setOwner(rs.getString("owners"));

                subjects.add(subject);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return subjects;
    }

    // Method to get the total count of subjects based on the filters
//    public int GetResultLength(String categoryFilter, String statusFilter, String search) {
    public int GetResultLength(String categoryFilter, String statusFilter, String search, List<String> owners, List<Integer> subjectIds, User currentUser) {
        int resultLength = 0;
        StringBuilder query = new StringBuilder(
                "SELECT COUNT(DISTINCT s.subject_id) AS result_length "
                + "FROM Subject s "
                + "JOIN SubjectCategory sc ON s.category_id = sc.category_id "
                + "LEFT JOIN Expert_Subject es ON s.subject_id = es.subject_id "
                + "LEFT JOIN [User] u ON es.user_id = u.user_id AND u.role IN (4, 5) "
                + "WHERE 1=1 ");

        if (subjectIds != null && !subjectIds.isEmpty()) {
            query.append("AND s.subject_id IN (");
            for (int i = 0; i < subjectIds.size(); i++) {
                query.append("?");
                if (i < subjectIds.size() - 1) {
                    query.append(", ");
                }
            }
            query.append(") ");
        }

        if (categoryFilter != null && !categoryFilter.equals("all")) {
            query.append("AND sc.category_name IN (");
            String[] categories = categoryFilter.split(",");
            for (int i = 0; i < categories.length; i++) {
                query.append("?");
                if (i < categories.length - 1) {
                    query.append(", ");
                }
            }
            query.append(") ");
        }

        if (!statusFilter.equals("all")) {
            query.append("AND s.status = ").append(statusFilter).append(" ");
        }

        if (search != null && !search.isEmpty()) {
            query.append("AND s.title LIKE ? ");
        }

        if (currentUser.getRole() == Role.ROLE_EXPERT) {
            query.append("AND u.user_id = ? ");
        }

        if (owners != null && !owners.isEmpty()) {
            query.append("AND (");
            for (int i = 0; i < owners.size(); i++) {
                query.append("u.full_name LIKE ?");
                if (i < owners.size() - 1) {
                    query.append(" OR ");
                }
            }
            query.append(") ");
        }

        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query.toString())) {
            int paramIndex = 1;

            if (subjectIds != null && !subjectIds.isEmpty()) {
                for (Integer id : subjectIds) {
                    ps.setInt(paramIndex++, id);
                }
            }

            if (categoryFilter != null && !categoryFilter.equals("all")) {
                String[] categories = categoryFilter.split(",");
                for (String category : categories) {
                    ps.setString(paramIndex++, category);
                }
            }

            if (search != null && !search.isEmpty()) {
                ps.setString(paramIndex++, "%" + search + "%");
            }

            if (currentUser.getRole() == Role.ROLE_EXPERT) {
                ps.setInt(paramIndex++, currentUser.getUser_id());
            }

            if (owners != null && !owners.isEmpty()) {
                for (String owner : owners) {
                    ps.setString(paramIndex++, "%" + owner + "%");
                }
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                resultLength = rs.getInt("result_length");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultLength;
    }

    public List<Subject> getAllSubjectDetails() throws SQLException {
        List<Subject> subjects = new ArrayList<>();
        String query = "SELECT s.subject_id, s.title, sc.category_name, u.full_name as owner "
                + "FROM Subject s "
                + "JOIN SubjectCategory sc ON s.category_id = sc.category_id "
                + "LEFT JOIN Expert_Subject es ON s.subject_id = es.subject_id "
                + "LEFT JOIN [User] u ON es.user_id = u.user_id AND u.role IN (4, 5)";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Subject subject = new Subject();
                subject.setSubject_id(rs.getInt("subject_id"));
                subject.setTitle(rs.getString("title"));

                SubjectCategory category = new SubjectCategory();
                category.setCategory_name(rs.getString("category_name"));
                subject.setCategory(category);

                subject.setOwner(rs.getString("owner"));
                subjects.add(subject);
            }
        }
        return subjects;
    }

    // Method to fetch all owners
    public List<String> getAllOwners() {
        List<String> owners = new ArrayList<>();
        String query = "SELECT DISTINCT u.full_name "
                + "FROM [User] u "
                + "JOIN Expert_Subject es ON u.user_id = es.user_id "
                + "WHERE u.role IN (4, 5)";  // Ensure the user role is for 'expert' or 'admin'

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                owners.add(rs.getString("full_name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return owners;
    }

    // Method to fetch all categories
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String query = "SELECT DISTINCT category_name FROM SubjectCategory";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                categories.add(rs.getString("category_name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return categories;
    }

    public List<Integer> getAllSubjectIds() {
        List<Integer> ids = new ArrayList<>();
        String query = "SELECT subject_id FROM Subject";

        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt("subject_id"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ids;
    }

    public List<Integer> getSubjectIdsByExpert(int expertId) {
        List<Integer> ids = new ArrayList<>();
        String query = "SELECT DISTINCT s.subject_id "
                + "FROM Subject s "
                + "JOIN Expert_Subject es ON s.subject_id = es.subject_id "
                + "WHERE es.user_id = ?";

        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, expertId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt("subject_id"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ids;
    }

    public List<User> GetExpertListBySubjectId(int subjectId) {
        List<User> expertList = new ArrayList<>();
        String query = "SELECT [User].user_id, [User].full_name, [User].mobile, [User].email FROM [User] "
                + "JOIN [Expert_Subject] ON [User].user_id = [Expert_Subject].user_id\n"
                + "  WHERE [Expert_Subject].subject_id = ? AND [User].role = 4;";
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, subjectId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUser_id(rs.getInt("user_id"));
                user.setFull_name(rs.getString("full_name"));
                user.setMobile(rs.getString("mobile"));
                user.setEmail(rs.getString("email"));
                expertList.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return expertList;
    }

    public void UnassignExpert(int expert_id, int subject_id) {
        String query = "DELETE "
                + "     FROM [Expert_Subject]"
                + "     WHERE user_id = ? AND subject_id = ?;";
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, expert_id);
            ps.setInt(2, subject_id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<User> GetExpertListUnAssignedFromSubjectId(int subject_id) {
        List<User> expertList = new ArrayList<>();
        String query = "SELECT [User].user_id, [User].full_name, [User].email, [User].mobile\n"
                + "FROM [User] \n"
                + "LEFT JOIN [Expert_Subject] ON [User].user_id = [Expert_Subject].user_id\n"
                + "                           AND [Expert_Subject].subject_id = ?\n"
                + "WHERE [User].role = 4\n"
                + "      AND [Expert_Subject].user_id IS NULL;";
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, subject_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUser_id(rs.getInt("user_id"));
                user.setFull_name(rs.getString("full_name"));
                user.setMobile(rs.getString("mobile"));
                user.setEmail(rs.getString("email"));
                expertList.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return expertList;
    }

    public void AssignExpert(int user_id, int subject_id) {
        String query = "INSERT INTO [Expert_Subject] VALUES(?, ?)";
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, user_id);
            ps.setInt(2, subject_id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void UpdateCourse(int subject_id, String avatarPath, String subjectName, String tagline, int status, String description, boolean isFeatured, int category_id) {
        String query = "UPDATE [Subject] "
                + "     SET thumbnail = ?, "
                + "         title = ?, "
                + "         tag_line = ?, "
                + "         status = ?, "
                + "         updated_at = ?, "
                + "         description = ?, "
                + "         is_featured = ?, "
                + "         category_id = ? "
                + "     WHERE subject_id = ?;";
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, avatarPath);
            ps.setString(2, subjectName);
            ps.setString(3, tagline);
            ps.setInt(4, status);
            ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            ps.setString(6, description);
            ps.setBoolean(7, isFeatured);
            ps.setInt(8, category_id);
            ps.setInt(9, subject_id);
            
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
