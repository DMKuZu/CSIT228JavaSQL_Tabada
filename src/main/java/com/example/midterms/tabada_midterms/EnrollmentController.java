package com.example.midterms.tabada_midterms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class EnrollmentController {
    public ListView<String> lvDisplay;
    public TextField tfName;
    public Button btnRemove;
    public Button btnEnroll;
    public Button btnBack;
    public ComboBox<String> cbCourse;
    public VBox vbEnrollmentForm;
    StringBuilder sb = new StringBuilder();

    public void initialize(){

    }

    @FXML
    public void toHomePage(ActionEvent event) throws IOException {
        Stage stage = (Stage) vbEnrollmentForm.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage-view.fxml"));
        Scene scene = new Scene(loader.load(), 600, 600);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void onRemoveClick(ActionEvent event){
        String student = lvDisplay.getSelectionModel().getSelectedItem();
        lvDisplay.getItems().remove(student);
    }

    @FXML
    public void onEnrollClick(){
        sb.setLength(0);
        String name = tfName.getText();
        String course = cbCourse.getValue();

        if(name.isBlank() || course == null){
            return;
        }
        for(int i = 0; i < lvDisplay.getItems().size(); i++){
            if(lvDisplay.getItems().get(i).contains(name)) return;
        }
        sb.append(String.format("- %s (%s)",name,course));

        lvDisplay.getItems().add(String.valueOf(sb));
        tfName.setText("");
    }

}