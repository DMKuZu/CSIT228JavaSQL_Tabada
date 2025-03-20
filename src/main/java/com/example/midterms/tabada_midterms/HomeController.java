package com.example.midterms.tabada_midterms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {
    public Button btnEnrollmentForm;
    public Button btnCourseForm;
    public VBox vbHomePage;

    @FXML
    public void toEnrollmentForm() throws IOException {
        Stage stage = (Stage) vbHomePage.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("enrollment-view.fxml"));
        Scene scene = new Scene(loader.load(), 600, 400);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void toCourseForm() throws IOException {
        Stage stage = (Stage) vbHomePage.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("modify-view.fxml"));
        Scene scene = new Scene(loader.load(), 600, 400);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
