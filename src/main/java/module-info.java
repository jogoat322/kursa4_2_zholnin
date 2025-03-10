module org.example.sea_batl_30 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.sea_batl_30 to javafx.fxml;
    exports org.example.sea_batl_30;
}