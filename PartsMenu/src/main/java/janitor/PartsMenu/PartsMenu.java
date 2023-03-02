package janitor.PartsMenu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main PartsMenu class that initializes the main application window.
 *
 * JavaDoc Location: ..\PartsMenu\JavaDocs\index.html
 *
 * @author Mike Janitor
 */
public class PartsMenu extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PartsMenu.class.getResource("Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(  ), 1263, 450);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main application initiation.
     *
     * @param args Command arguments
     */
    public static void main(String[] args) {
        launch();
    }
}