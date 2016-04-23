//package sample;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * Created by Andrew on 3/29/2016.
 */
public class Game implements Runnable, Updateable
{
    public static final int WIDTH = 1400;
    public static final int HEIGHT = 900;
    public final int NUM_ROWS = 4;
    public final int NUM_COLS = 4;
    public final int TOTAL_CELLS = NUM_ROWS * NUM_COLS;

    Stage primaryStage; // Window handle
    GamePane gamePane;    // root pane == container for a scene
    Scene scene;// This scene/canvas handle
    boolean isRunning = true;

    public Game(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
    }

    @Override
    public void run()
    {
        int error = -1;
        try
        {
            error = this.initialize();
//            error = this.loadContent();
        }
        catch(Exception ex)
        {
            System.out.println("Hit error. error code: " + error + " " + ex);
        }
        System.out.println("Should be running");




    }

    /** This creates all constructs and inits stuff for the game
     * */
    public int initialize()
    {
        gamePane = new WumpusWorldPane(WIDTH, HEIGHT);
        buildGamePane();
        dbgPane((Pane)(gamePane.getMainPane()), Color.CYAN);
        dbgPane((Pane)gamePane.getBottom(), Color.DARKKHAKI);
        dbgPane((Pane)gamePane.getLeft(), Color.INDIANRED);
        scene = new Scene(gamePane, WIDTH, HEIGHT );

        primaryStage.setScene(scene);
        primaryStage.show();

        return 0;
    }

    public void buildGamePane()
    {
//        gamePane.setPrefSize(WIDTH, HEIGHT);
//        gamePane.setMinSize(500,500);
        gamePane.setAlignment(gamePane.getMainPane(), Pos.CENTER);
        ((WumpusWorldPane)gamePane).setLayout(WIDTH, HEIGHT);
        gamePane.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                update();
            }
        });
    }

    @Override
    public void update()
    {
        System.out.println("Updating");

    }
    public static void dbgPane(Pane p, Color c)
    {
        p.setBackground(new Background(new BackgroundFill(c, CornerRadii.EMPTY, new Insets( 0, 0, 0, 0) )));
    }

}
