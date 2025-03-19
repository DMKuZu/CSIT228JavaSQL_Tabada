package com.example.midterms.tabada_midterms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModifyController {
    public VBox vbModifyForm;
    public ListView<String> lvDisplay;
    public TextField tfCourseCode;
    public Button btnRemove;
    public Button btnSave;
    public Button btnBack;
    public TextField tfCourseName;
    public Button btnAdd;
    public Button btnRefresh;

    private DatabaseConnection db;

    private void display(){
        lvDisplay.getItems().clear();
        List<String> courses = new ArrayList<>();
        db.retrieve_tblCourses(courses);
        for(String c: courses) lvDisplay.getItems().add(c);
    }
    private void showInfo(String title,String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void initialize() throws IOException {
        db = new DatabaseConnection();

        display();

        lvDisplay.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String courseCode = newValue.substring(newValue.indexOf("]") + 2, newValue.indexOf(" - "));
                String courseName = newValue.substring(newValue.indexOf("-") + 2);
                tfCourseName.setText(courseName);
                tfCourseCode.setText(courseCode);
            }
        });
    }

    @FXML
    public void onBackClick() throws IOException {
        Stage stage = (Stage) vbModifyForm.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage-view.fxml"));
        Scene scene = new Scene(loader.load(), 400, 200);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onRefreshClick(){
        display();
        tfCourseName.clear();
        tfCourseCode.clear();
    }

    @FXML
    public void onRemoveClick(){
        String selectedItem = lvDisplay.getSelectionModel().getSelectedItem();
        if(selectedItem == null) {
            showInfo("No course selected!","Please select a course in the list to remove.");
            return;
        }
        int id = Integer.parseInt(selectedItem.substring(1,selectedItem.indexOf("]")));

        db.delete_tblCourses(id);
        lvDisplay.getItems().remove(selectedItem);
        display();
        tfCourseName.clear();
        tfCourseCode.clear();
    }

    @FXML
    public void onAddClick(){
        String course = tfCourseName.getText();
        String code = tfCourseCode.getText();
        if(course.isEmpty() || code.isEmpty()) {
            showInfo("Incomplete Information!","Please fill out all fields.");
            return;
        }

        if(db.insert_tblCourses(code,course)) display();
        tfCourseName.clear();
        tfCourseCode.clear();
    }

    @FXML
    public void onSaveClick(){
        String selectedItem = lvDisplay.getSelectionModel().getSelectedItem();
        if(selectedItem == null) {
            showInfo("No course selected!","Please select a course in the list to update.");
            return;
        }

        int id = Integer.parseInt(selectedItem.substring(1,selectedItem.indexOf("]")));
        String course = tfCourseName.getText();
        String code = tfCourseCode.getText();
        if(course.isEmpty() || code.isEmpty()) {
            showInfo("Incomplete Information!","Please fill out all fields.");
            return;
        }

        if(db.update_tblCourses(id,course,code)) display();
        tfCourseName.clear();
        tfCourseCode.clear();
    }
}
