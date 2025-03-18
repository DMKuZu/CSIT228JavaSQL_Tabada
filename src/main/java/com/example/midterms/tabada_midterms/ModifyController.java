package com.example.midterms.tabada_midterms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ModifyController {
    public VBox vbModifyForm;
    public ListView lvDisplay;
    public TextField tfCourseCode;
    public Button btnRemove;
    public Button btnSave;
    public Button btnBack;
    public TextField tfCourseName;
    public Button btnAdd;

    @FXML
    public void toHomePage(ActionEvent event) throws IOException {
        Stage stage = (Stage) vbModifyForm.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage-view.fxml"));
        Scene scene = new Scene(loader.load(), 600, 600);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


}
