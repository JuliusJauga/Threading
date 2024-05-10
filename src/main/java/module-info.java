module com.example.threading {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.opencsv;


    opens com.example.threading to javafx.fxml;
    exports com.example.threading;
}