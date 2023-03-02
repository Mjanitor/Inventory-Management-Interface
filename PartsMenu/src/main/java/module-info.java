module janitor.PartsMenu {
    requires javafx.controls;
    requires javafx.fxml;


    opens janitor.PartsMenu to javafx.fxml;
    exports janitor.PartsMenu;
}