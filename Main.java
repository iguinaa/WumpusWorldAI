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
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Wumpus World");

        Map wumpusMap = new Map(0);	//send 0 for a default map, 1 for random
        wumpusMap.printMap();


        Game wumpus = new Game(primaryStage);
        wumpus.run();


    }
}