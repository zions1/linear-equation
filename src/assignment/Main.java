package assignment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Application class using JavaFX libraries.
 *
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage the primary stage.
     * @throws Exception if the errors appeared.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("assignment.fxml"));
        primaryStage.setTitle("Advanced Java Assignment");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * The main method of application.
     * @param args the command-line arguments as an array od {@code String}
     */
    public static void main(String[] args) {
        launch(args);
    }
}
