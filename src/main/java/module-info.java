module parallelProject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens parallelProject to javafx.fxml;
    exports parallelProject;
    exports socket;
    opens socket to javafx.fxml;
}
