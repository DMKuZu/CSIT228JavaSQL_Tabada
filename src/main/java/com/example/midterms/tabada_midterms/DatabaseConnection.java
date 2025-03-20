package com.example.midterms.tabada_midterms;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class DatabaseConnection {
    private String URL = "jdbc:mysql://localhost:3306";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private void createDatabase(Connection connection) throws SQLException{
        try (Statement statement = connection.createStatement()) {
            String dbName = "csit228f3"; //set whatever dbname you want

            ResultSet resultSet = statement.executeQuery(
                    "SELECT SCHEMA_NAME FROM information_schema.SCHEMATA WHERE SCHEMA_NAME = '"+dbName+"'"
            );
            if (!resultSet.next()) {
                statement.executeUpdate("CREATE DATABASE "+dbName);
            }
            if(!URL.contains("/"+dbName)) URL += "/"+dbName;
        }
    }
    private void createCourses(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS courses (" +
                            "id INT PRIMARY KEY AUTO_INCREMENT," +
                            "code VARCHAR(10) NOT NULL UNIQUE," +
                            "course VARCHAR(255) NOT NULL," +
                            "deleted_at DATE DEFAULT NULL" +
                            ")"
            );
        }
    }
    private void createStudents(Connection connection) throws SQLException{
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS students (" +
                            "id INT PRIMARY KEY AUTO_INCREMENT," +
                            "name VARCHAR(255) NOT NULL," +
                            "course INT NOT NULL," +
                            "deleted_at DATE DEFAULT NULL," +
                            "FOREIGN KEY (course) REFERENCES courses(id)" +
                            "ON DELETE CASCADE " +
                            "ON UPDATE CASCADE" +
                            ")"
            );
        }
    }
    private void setInitialCourses(Connection connection) throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT IGNORE INTO courses (code, course) VALUES(?,?)"
        )) {
            pstmt.setString(1, "BSCS");
            pstmt.setString(2, "BS Computer Science");
            pstmt.executeUpdate();
        }
    }

    public DatabaseConnection(){
        try(Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD)) {
            createDatabase(connection);
        }
        catch (SQLException e) {
            System.out.println("Database not created " + e.getMessage());
        }

        try(Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD)) {
            createCourses(connection);
            createStudents(connection);
            setInitialCourses(connection);
        } catch (SQLException e) {
            System.out.println("Did not connect to Database. " + e.getMessage());
        }
    }

    public boolean insert_tblStudents(String studentName, int courseID){
        boolean isCommit = false;
        try(Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            PreparedStatement pstmt = connection.prepareStatement(
                    "insert into students (name, course) values(?,?)"
            ))
        {
            connection.setAutoCommit(false);

            pstmt.setString(1,studentName);
            pstmt.setInt(2,courseID);

            int rowsAffected = pstmt.executeUpdate();
            if(rowsAffected > 0) {
                connection.commit();
                isCommit = true;
            }
            else connection.rollback();
        }catch (SQLException e){
            System.out.println("From insert students" + e.getMessage());
        }
        return isCommit;
    }

    public boolean insert_tblCourses(String courseCode, String courseName){
        boolean isCommit = false;
        try(Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            PreparedStatement pstmt = connection.prepareStatement(
                    "insert ignore into courses (code, course) values(?,?)"
            ))
        {
            connection.setAutoCommit(false);

            pstmt.setString(1,courseCode);
            pstmt.setString(2,courseName);

            int rowsAffected = pstmt.executeUpdate();
            if(rowsAffected > 0) {
                connection.commit();
                isCommit = true;
            }
            else connection.rollback();
        }catch (SQLException e){
            System.out.println("From insert courses" + e.getMessage());
        }
        return isCommit;
    }

    public void delete_tblStudents(int id){
        try(Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            PreparedStatement pstmt = connection.prepareStatement(
                    "update students set deleted_at=? where id=?"
            ))
        {
            pstmt.setDate(1, Date.valueOf(String.valueOf(LocalDate.now())));
            pstmt.setInt(2,id);
            pstmt.executeUpdate();
        }catch (SQLException e){
            System.out.println("From delete students" +e.getMessage());
        }
    }

    private void cascade_delete(int courseID){
        try(Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            PreparedStatement pstmt = connection.prepareStatement(
                    "update students set deleted_at=? where course=?"
            ))
        {
            pstmt.setDate(1, Date.valueOf(String.valueOf(LocalDate.now())));
            pstmt.setInt(2,courseID);
            pstmt.executeUpdate();
        }catch (SQLException e){
            System.out.println("From cascade delete students" +e.getMessage());
        }
    }

    public void delete_tblCourses(int id){
        try(Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            PreparedStatement pstmt = connection.prepareStatement(
                    "update courses set deleted_at=? where id=?"
            ))
        {

            pstmt.setDate(1, Date.valueOf(String.valueOf(LocalDate.now())));
            pstmt.setInt(2,id);
            pstmt.executeUpdate();
            cascade_delete(id);
        }catch (SQLException e){
            System.out.println("From delete courses" +e.getMessage());
        }
    }

    public void retrieve_tblStudents(List<String> students){
        String queryStudents = "SELECT * FROM students WHERE deleted_at IS NULL";
        String queryCode = "SELECT code FROM courses WHERE id = ? && deleted_at IS NULL";
        String queryProgram = "SELECT course FROM courses WHERE id = ? && deleted_at IS NULL";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(queryStudents)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int courseID = resultSet.getInt("course");

                try (PreparedStatement pstmtCode = connection.prepareStatement(queryCode);
                     PreparedStatement pstmtProgram = connection.prepareStatement(queryProgram)) {

                    pstmtCode.setInt(1, courseID);
                    ResultSet rsCode = pstmtCode.executeQuery();
                    String code = rsCode.getString("code");

                    pstmtProgram.setInt(1, courseID);
                    ResultSet rsProgram = pstmtProgram.executeQuery();
                    String course = rsProgram.getString("course");

                    students.add("[" + id + "] " + name + " | " + code + " | " + course);
        }
            }
        }catch (SQLException e){
            System.out.println("From retrieve students" +e.getMessage());
        }
    }

    public void retrieve_tblCourses(List<String> courses){
        String query = "SELECT * FROM courses WHERE deleted_at IS NULL";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("course");
                String code = resultSet.getString("code");

                courses.add("[" + id + "] " + code + " | " + name);
            }
        }catch (SQLException e){
            System.out.println("From retrieve courses" +e.getMessage());
        }
    }

    public boolean update_tblStudents(int id, String studentName, int courseID){
        boolean isCommit = false;
        try(Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            PreparedStatement pstmt = connection.prepareStatement(
                    "update students set name=?, course=? where id=?"
            ))
        {
            connection.setAutoCommit(false);

            pstmt.setString(1,studentName);
            pstmt.setInt(2,courseID);
            pstmt.setInt(3,id);

            int rowsAffected = pstmt.executeUpdate();
            if(rowsAffected > 0) {
                connection.commit();
                isCommit = true;
            }
            else connection.rollback();
        }catch (SQLException e){
            System.out.println("From update students" +e.getMessage());
        }
        return isCommit;
    }

    public boolean update_tblCourses(int id, String courseName, String courseCode){
        boolean isCommit = false;
        try(Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            PreparedStatement pstmt = connection.prepareStatement(
                    "update courses set course=?, code=? where id=?"
            ))
        {
            connection.setAutoCommit(false);

            pstmt.setString(1,courseName);
            pstmt.setString(2,courseCode);
            pstmt.setInt(3,id);

            int rowsAffected = pstmt.executeUpdate();
            if(rowsAffected > 0) {
                connection.commit();
                isCommit = true;
            }
            else connection.rollback();
        }catch (SQLException e){
            System.out.println("From update courses" + e.getMessage());
        }
        return isCommit;
    }
}
