package db;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.*;

public class DatabaseConnection {
    Connection conn;

    public DatabaseConnection() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:java_project.db");
            if (!conn.equals(null)) {
                System.out.println("Successful connection");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean insertNewUserRec(String username, String password) {
        byte[] salt;
        try {
            salt = generateSalt();
        } catch (NoSuchAlgorithmException ex) {
            return false;
        }
        String hashedPassword = getHashedPswd(salt, password);
        if (hashedPassword != null) {
            String insertSql = "Insert into login values (?, ?, ?)";
            PreparedStatement insStmt = null;

            try {
                insStmt = conn.prepareStatement(insertSql);
                insStmt.setString(1, username);
                insStmt.setString(2, hashedPassword);
                insStmt.setString(3, Base64.getEncoder().encodeToString(salt));
                insStmt.execute();
                insStmt.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean updateUserPass(String username, String password) {
        byte[] salt;
        try {
            salt = generateSalt();
        } catch (NoSuchAlgorithmException ex) {
            return false;
        }
        String hashedPassword = getHashedPswd(salt, password);
        if (hashedPassword != null) {
            String updateQuery = "Update login set hashed_password = ?, salt = ? where username=?";
            PreparedStatement psStmt = null;

            try {
                //conn.setAutoCommit(false);
                psStmt = conn.prepareStatement(updateQuery);
                psStmt.setString(1, hashedPassword);
                psStmt.setString(2, Base64.getEncoder().encodeToString(salt));
                psStmt.setString(3, username);
                psStmt.executeUpdate();
                //conn.commit();
                psStmt.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    private byte[] generateSalt() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    private String getHashedPswd(byte[] salt, String pswd) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte bytedata[] = md.digest(pswd.getBytes());
            String hashPswd = bytesToHex(bytedata);
            return hashPswd;

        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }

    public boolean isUserPresent(String username, String password) {
        ResultSet rs;
        PreparedStatement ps;
        try {
            Statement stmt = conn.createStatement();
            ps = conn.prepareStatement(" select * from login where username=?");
            ps.setString(1, username);
            //ps.setString(2, password);

            rs = ps.executeQuery();
            if (rs.next()) {
                String hashPswd = rs.getString("hashed_password");
                String salt = rs.getString("salt");
                String hashToCheck = getHashedPswd(Base64.getDecoder().decode(salt), password);
                rs.close();
                ps.close();
                if (hashToCheck.equals(hashPswd)) {
                    return true;
                } else {
                    return false;
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isDuplicateUserRec(String username) {
        ResultSet rs;
        PreparedStatement ps;
        try {
            Statement stmt = conn.createStatement();
            ps = conn.prepareStatement(" select * from login where username=?");
            ps.setString(1, username);
            //ps.setString(2, password);

            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteUser(String username) {
        String sql = "DELETE FROM login WHERE username = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, username);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean insertNews(final Map<String, String> newsArticles, final String genre) {
        String insertQuery = "insert into news_scrape(genre, link, summary) " +
                "values(?, ?, ?) ";
        for (Map.Entry<String, String> val : newsArticles.entrySet()) {
            PreparedStatement pt = null;
            if (!checkIfValueExists("link", val.getKey())) {
                try {
                    pt = conn.prepareStatement(insertQuery);
                    pt.setString(1, genre);
                    pt.setString(2, val.getKey());
                    pt.setString(3, val.getValue());
                    //pt.addBatch();
                    pt.executeUpdate();
                    pt.close();
                } catch (SQLException e) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkIfValueExists(String column, String value) {
        String selectQuery = "SELECT 1 FROM news_scrape WHERE link = ?";
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;

        try {
            pstmt = conn.prepareStatement(selectQuery);
            pstmt.setString(1, value);
            resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                resultSet.close();
                pstmt.close();
                return true;
            } else {
                resultSet.close();
                pstmt.close();
                return false;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void deleteGenre(String genre) {
        String sql = "DELETE FROM news_scrape WHERE genre = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, genre);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public List<NewsRecords> getNewsBasedOnOffset(int newsCount, String genre, int offset) {
        String query = "select id, genre, link, summary from news_scrape where genre = ? " +
                "limit -1 offset " + offset;
        PreparedStatement stmt=null;
        ResultSet rs = null;
        List<NewsRecords> result = new ArrayList<NewsRecords>();

        try {

            stmt = conn.prepareStatement(query);
            stmt.setString(1, genre);

            rs =stmt.executeQuery();

            while (rs.next() && newsCount-->0) {
                NewsRecords nr = new NewsRecords(rs.getInt("id"), rs.getString("genre"),
                        rs.getString("link"), rs.getString("summary"));
                result.add(nr);
            }
        } catch (SQLException e) {
            return null;
        }


        return result;
    }

    public List<NewsRecords> getNews(int newsCount, String genre) {
        return getNewsBasedOnOffset(newsCount, genre, 0);

    }

    public void recordSaveForLater(String username, int news_id) {
        String query = "select is_save_later from user_news_mapping where user_name =? and news_id = ?";
        PreparedStatement stmt=null;
        ResultSet rs = null;

        List<NewsRecords> result = new ArrayList<NewsRecords>();

        try {

            stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setInt(2, news_id);
            rs =stmt.executeQuery();

            if (!rs.next()) {
                String insertQuery = "insert into user_news_mapping (user_name, news_id, is_save_later, is_read_later) " +
                        "values(?,?,?,?)";
                PreparedStatement insStmt = null;

                try {
                    insStmt = conn.prepareStatement(insertQuery);
                    insStmt.setString(1, username);
                    insStmt.setInt(2, news_id);
                    insStmt.setInt(3, 1);
                    insStmt.setInt(4, 0);
                    insStmt.execute();
                    insStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                String updateQuery = "update user_news_mapping set is_save_later=? where user_name=? and news_id=?";
                PreparedStatement upPstmt = null;

                try {
                    int saveLater = rs.getInt("is_save_later");
                    saveLater = (saveLater == 1) ? 0 : 1;
                    upPstmt = conn.prepareStatement(updateQuery);
                    upPstmt.setInt(1, saveLater);
                    upPstmt.setString(2, username);
                    upPstmt.setInt(3, news_id);
                    upPstmt.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void recordReadMore(String username, int news_id) {
        String query = "select is_read_later from user_news_mapping where user_name =? and news_id = ?";
        PreparedStatement stmt=null;
        ResultSet rs = null;

        List<NewsRecords> result = new ArrayList<NewsRecords>();

        try {

            stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setInt(2, news_id);
            rs =stmt.executeQuery();

            if (!rs.next()) {
                String insertQuery = "insert into user_news_mapping (user_name, news_id, is_save_later, is_read_later)" +
                        "values(?,?,?,?)";
                PreparedStatement insStmt = null;

                try {
                    insStmt = conn.prepareStatement(insertQuery);
                    insStmt.setString(1, username);
                    insStmt.setInt(2, news_id);
                    insStmt.setInt(3, 0);
                    insStmt.setInt(4, 1);
                    insStmt.execute();
                    insStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteUserNewsMapping(String username) {
        String query = "delete from user_news_mapping where user_name = ?";
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String getLinkForNews(int newsid) {
        String query = "select link from news_scrape where id =? ";
        PreparedStatement stmt=null;
        ResultSet rs = null;

        List<NewsRecords> result = new ArrayList<NewsRecords>();

        try {

            stmt = conn.prepareStatement(query);
            stmt.setInt(1, newsid);
            rs =stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("link");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<NewsRecords> getNewsMarkedForSaveLater(String username) {
        String query = "select news_id, genre, link, summary " +
                "from user_news_mapping " +
                "inner join news_scrape on news_scrape.id = user_news_mapping.news_id " +
                "where user_name=? and is_save_later=1 ";
        PreparedStatement stmt=null;
        ResultSet rs = null;
        List<NewsRecords> result = new ArrayList<NewsRecords>();
        int newsCount = 4;

        try {

            stmt = conn.prepareStatement(query);
            stmt.setString(1, username);

            rs =stmt.executeQuery();

            while (rs.next() && newsCount-->0) {
                NewsRecords nr = new NewsRecords(rs.getInt("news_id"), rs.getString("genre"),
                        rs.getString("link"), rs.getString("summary"));
                result.add(nr);
            }
        } catch (SQLException e) {
            return null;
        }

        return result;
    }

    public List<NewsReadData> getNewsReadCountByGenre(String user_name) {
        List<NewsReadData> dataList = new ArrayList<>();
        String query = "SELECT unm.user_name, ns.genre, COUNT(*) as read_count " +
                "FROM user_news_mapping unm " +
                "JOIN news_scrape ns ON unm.news_id = ns.id " +
                "WHERE unm.is_read_later = 1 and unm.user_name=?" +
                "GROUP BY ns.genre";
        PreparedStatement stmt;
        ResultSet rs;

        try  {
            stmt = conn.prepareStatement(query);
            stmt.setString(1, user_name);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String username = rs.getString("user_name");
                String genre = rs.getString("genre");
                int readCount = rs.getInt("read_count");
                dataList.add(new NewsReadData(username, genre, readCount));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public static void main(String args[]) {
        DatabaseConnection db = new DatabaseConnection();
        //db.insertNewUserRec("se", "se");
        db.deleteUser("m");
        db.deleteUser("a");

        db.deleteGenre("politics");
        db.deleteGenre("business");
        db.deleteGenre("world");

        db.deleteGenre("genre");

        db.deleteUserNewsMapping("Sakthi Uma maheswari");

        db.closeConnection();
    }
}
