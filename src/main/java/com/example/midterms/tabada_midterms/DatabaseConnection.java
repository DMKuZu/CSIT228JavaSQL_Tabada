package com.example.midterms.tabada_midterms;

import java.sql.*;
import java.time.LocalDateTime;


public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/csit228f3";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public void insertData(String tableName, String value1, String value2){
        String query;
        if(tableName.toLowerCase().equals("students")){
            query = "insert into students (studentName, course) values(?,?)";
        }
        else if(tableName.toLowerCase().equals("courses")){
            query = "insert into courses (courseCode, courseName) values(?,?)";
        }
        else{
            return;
        }
        try(Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setString(1,value1);
            preparedStatement.setString(2,value2);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void deleteData(String tableName, String course){
        String query;
        if(tableName.toLowerCase().equals("students")){
            query = "update students set delete_at=?";
        }
        else if(tableName.toLowerCase().equals("courses")){
            query = "insert into courses (courseCode, courseName) values(?,?)";
        }
        else{
            return;
        }
        try(Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setDate(1, Date.valueOf(String.valueOf(LocalDateTime.now())));
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void retrieveData(){

    }

    public void updateData(){

    }

    package com.csit228.guisql;

import java.sql.*;

    public class InsertData {
        public static final String URL = "jdbc:mysql://localhost:3306/csit228f3";
        public static final String USERNAME = "root";
        public static final String PASSWORD = "";

        public static void main(String[] args) {
        /*try{
            Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            System.out.println("DATABASE CONNECTION SUCCESSFUL");
            Statement statement = connection.createStatement();
            String query = "create table users ( " +
                    "id int not null auto_increment primary key," +
                    "name varchar(50) not null," +
                    "email varchar(100) not null," +
                    "password varchar(50) null)";
            statement.execute(query);
            connection.close();
            System.out.println("CONNECTION CLOSED");

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }*/
            Connection connection = null;
            try{
                connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "insert into users (name,email) values (?,?)"
                );

                preparedStatement.setString(1,"John");
                preparedStatement.setString(2,"test@account.com");

            }catch (SQLException e){
                System.out.println(e.getMessage());
            }

        }
    }

    /*public class InsertData {
        public static final String URL = "jdbc:mysql://localhost:3306/csit228f3";
        public static final String USERNAME = "root";
        public static final String PASSWORD = "";

        public static void main(String[] args) {
            try{
                Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
                System.out.println("DATABASE CONNECTION SUCCESSFUL");
                Statement statement = connection.createStatement();
                String query = "create table users ( " +
                        "id int not null auto_increment primary key," +
                        "name varchar(50) not null," +
                        "email varchar(100) not null," +
                        "password varchar(50) null)";
                statement.execute(query);
                connection.close();
                System.out.println("CONNECTION CLOSED");

            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
            Connection connection = null;
            try{
                connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "insert into users (name,email) values (?,?)"
                );

                preparedStatement.setString(1,"John");
                preparedStatement.setString(2,"test@account.com");

            }catch (SQLException e){
                System.out.println(e.getMessage());
            }

        }
    }*/
    /*public class DeleteData {
    public static final String URL = "jdbc:mysql://localhost:3306/csit228f3";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";
    public static void main(String[] args) {
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "delete from users where id=?"
            );

            int id = 2;
            preparedStatement.setInt(1,id);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}*/
    /*package com.csit228.guisql;

import java.sql.*;

public class UpdateData {
    public static final String URL = "jdbc:mysql://localhost:3306/csit228f3";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";
    public static void main(String[] args) {
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "update users set name=?, email=? where id=?"
            );
            String name = "John Winston W. Tabada";
            String email = "test@account.com";
            int id = 2;
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,email);
            preparedStatement.setInt(3,id);

            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
*/
    /*package com.csit228.guisql;

import java.sql.*;

public class RetrieveData {
    public static final String URL = "jdbc:mysql://localhost:3306/csit228f3";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";
    public static void main(String[] args) {
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            Statement statement = connection.createStatement();
            String query = "select * from users";

            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                System.out.printf("[%d] %s - %s\n",id,name,email);
            }
        }catch (SQLException e){
                e.printStackTrace();
        }
    }
}
*/
}
