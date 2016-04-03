package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("Wumpus World");

        /* test code */
//        Pane root = new BorderPane();
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();
        /* end test */

        Map wumpusMap = new Map();
        wumpusMap.printMap();




        Game wumpus = new Game(primaryStage);
        wumpus.run();


    }


    public static void main(String[] args)
    {
        launch(args);
    }
}
