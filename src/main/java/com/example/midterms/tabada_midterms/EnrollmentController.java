package com.example.midterms.tabada_midterms;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentController {
    public ListView<String> lvDisplay;
    public TextField tfName;
    public Button btnRemove;
    public Button btnEnroll;
    public Button btnBack;
    public ComboBox<String> cbCourse;
    public VBox vbEnrollmentForm;
    public Button btnUpdate;
    public Button btnFilter;
    public Button btnRefresh;

    private DatabaseConnection db;

    private void display(){
        lvDisplay.getItems().clear();
        List<String> students = new ArrayList<>();
        db.retrieve_tblStudents(students);
        for(String s: students) lvDisplay.getItems().add(s);
    }
    private void showInfo(String title,String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private String findItem(String course){
        for(String item : cbCourse.getItems()){
            if(item.contains(course.trim())){
                return item;
            }
        }
        return null;
    }
    private boolean filterStudent(String name, List<String> students) {
        boolean hasStudent = false;
        lvDisplay.getItems().clear();

        for(String s: students) {
            if(s.toLowerCase().contains(name.toLowerCase().trim())){
                lvDisplay.getItems().add(s);
                hasStudent = true;
            }
        }
        return hasStudent;
    }

    public void initialize(){
        db = new DatabaseConnection();

        List<String> courses = new ArrayList<>();
        db.retrieve_tblCourses(courses);
        for(String c: courses) cbCourse.getItems().add(c);

        display();

        cbCourse.getSelectionModel().selectFirst();

        lvDisplay.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String studentName = newValue.substring(newValue.indexOf("]") + 2, newValue.indexOf(" | "));
                String studentCourse = newValue.substring(newValue.indexOf("-") + 2);
                tfName.setText(studentName);
                cbCourse.getSelectionModel().select(findItem(studentCourse));
            }
        });
    }

    @FXML
    public void onBackClick() throws IOException {
        Stage stage = (Stage) vbEnrollmentForm.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage-view.fxml"));
        Scene scene = new Scene(loader.load(), 400, 200);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onRemoveClick(){
        String selectedItem = lvDisplay.getSelectionModel().getSelectedItem();
        if(selectedItem == null) {
            showInfo("No student selected!","Please select a student in the list to remove.");
            return;
        }
        int id = Integer.parseInt(selectedItem.substring(1,selectedItem.indexOf("]")));

        db.delete_tblStudents(id);
        lvDisplay.getItems().remove(selectedItem);
        display();
    }

    @FXML
    public void onEnrollClick(){
        String name = tfName.getText();
        String course = cbCourse.getValue();
        if(name.isEmpty() || course == null) {
            showInfo("Incomplete Information!","Please fill out all fields.");
            return;
        }
        int courseID = Integer.parseInt(course.substring(1,course.indexOf("]")));

        if(db.insert_tblStudents(name,courseID)) display();
        tfName.clear();
    }

    @FXML
    public void onUpdateClick(){
        String selectedItem = lvDisplay.getSelectionModel().getSelectedItem();
        if(selectedItem == null) {
            showInfo("No student selected!","Please select a student in the list to update.");
            return;
        }

        int id = Integer.parseInt(selectedItem.substring(1,selectedItem.indexOf("]")));
        String name = tfName.getText();
        String course = cbCourse.getValue();
        if(name.isEmpty() || course == null) {
            showInfo("Incomplete Information!","Please fill out all fields.");
            return;
        }

        int courseID = Integer.parseInt(course.substring(1,course.indexOf("]")));

        if(db.update_tblStudents(id,name,courseID)) display();
        tfName.clear();
    }

    @FXML
    public void onFilterClick(){
        String name = tfName.getText();
        if(name.isEmpty()) {
            showInfo("Incomplete Information!","Please type the name of the student.");
            return;
        }

        List<String> students = new ArrayList<>();
        db.retrieve_tblStudents(students);

        if(!filterStudent(name,students)) {
            showInfo("Student not found!","The student does not exist.");
            display();
        }
    }

    @FXML
    public void onRefreshClick(){
        display();
        tfName.clear();
    }

}