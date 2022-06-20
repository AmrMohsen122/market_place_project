module parallelProject {
    requires javafx.controls;
    requires javafx.fxml;

    opens parallelProject to javafx.fxml;
    exports parallelProject;
}
