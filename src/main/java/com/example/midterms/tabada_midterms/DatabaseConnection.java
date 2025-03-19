package com.example.midterms.tabada_midterms;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/csit228f3";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public DatabaseConnection(){
        try(Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            PreparedStatement pstmt = connection.prepareStatement(
                    "INSERT IGNORE INTO courses (code, course) VALUES(?,?)"
            )
        ){
            pstmt.setString(1,"BSCS");
            pstmt.setString(2,"BS Computer Science");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Did not connect to Database.");
        }
    }

    public boolean insert_tblStudents(String studentName, int courseID){
        boolean isCommit = false;
        try(Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            PreparedStatement pstmt = connection.prepareStatement(
                    "insert ignore into students (name, course) values(?,?)"
            ))
        {
            connection.setAutoCommit(false);

            pstmt.setString(1,studentName);
            pstmt.setInt(2,courseID);
            pstmt.executeUpdate();

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
            pstmt.executeUpdate();

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

    public void delete_tblCourses(int id){
        try(Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            PreparedStatement pstmt = connection.prepareStatement(
                    "update courses set deleted_at=? where id=?"
            ))
        {

            pstmt.setDate(1, Date.valueOf(String.valueOf(LocalDateTime.now())));
            pstmt.setInt(2,id);
            pstmt.executeUpdate();
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
                    String code = rsCode.next() ? rsCode.getString("code") : "N/A";

                    pstmtProgram.setInt(1, courseID);
                    ResultSet rsProgram = pstmtProgram.executeQuery();
                    String course = rsProgram.next() ? rsProgram.getString("course") : "N/A";

                    students.add("[" + id + "] " + name + " | " + code + " - " + course);
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

                courses.add("[" + id + "] " + code + " - " + name);
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
