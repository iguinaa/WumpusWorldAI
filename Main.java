
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("Wumpus World");

        Game wumpus = new Game(primaryStage);
        wumpus.run();
    }


    /*public static void main(String[] args)
    {
        launch(args);
    }*/
}
