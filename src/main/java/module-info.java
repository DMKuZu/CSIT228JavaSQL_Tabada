module com.example.midterms.tabada_midterms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.midterms.tabada_midterms to javafx.fxml;
    exports com.example.midterms.tabada_midterms;
}