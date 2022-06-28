module parallelProject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    opens parallelProject to javafx.fxml;
    exports parallelProject;
}
