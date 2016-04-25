//package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;


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
    static GamePane gamePane;    // root pane == container for a scene
    Scene scene;// This scene/canvas handle
    boolean isRunning = true;
    Player player;




    // This is for data
    public static StringProperty KB = new SimpleStringProperty(
            "Current Knowledge:   \n"
    );

    public static String getKB()
    {
        return KB.get();
    }

    public static StringProperty KBProperty()
    {
        return KB;
    }

    public static String getProperties()
    {
        return properties.get();
    }

    public static StringProperty propertiesProperty()
    {
        return properties;
    }

    public static String getDebugData()
    {
        return debugData.get();
    }

    public static StringProperty debugDataProperty()
    {
        return debugData;
    }

    public static StringProperty properties = new SimpleStringProperty(
            "Current Stats:   \n"
    );

    public static StringProperty debugData = new SimpleStringProperty(
            "Debug Information:   \n"
    );
    public static TextArea log = new TextArea();




    public static void updatePropertiesString(ArrayList<String> properties)
    {
        String text = new String();
        for(String s : properties)
        {
            text = text + s + "\n";
        }

       Game.properties.setValue(
               "Current Stats:   \n" + text
       );
    }

    public static void updateDebugString(ArrayList<String> debugData)
    {
        String text = new String();
        for(String s : debugData)
        {
            text = text + s + "\n";
        }
        Game.debugData.setValue(
                "Debug Information:   \n" + text
        );
    }

    public static void updateKBString(ArrayList<String> KB)
    {
        String text = new String();
        for(String s : KB)
        {
            text = text + s + "\n";
        }

        Game.KB.setValue(
                "Current Knowledge:   \n" + text
        );
    }

    public static void updateKBString(String text)
    {
        Game.KB.setValue(
                "Current Stats:   \n" + text
        );
    }

    public static void updatePropertiesString(String text)
    {
        Game.properties.setValue(
                "Current Stats:   \n" + text
        );
    }

    public static void updateDebugString(String text)
    {
        Game.debugData.setValue(
                /*"Debug Information:   \n" +*/ text
        );
    }
    public static void appendDebugString(String text)
    {
        Game.debugData.setValue(
                Game.debugData.getValueSafe() + text
        );
    }

    public static void addToLog(String log)
    {
        Game.log.appendText(log);
    }


    Button btnUpdate = new Button("Update");


//    StringProperty KB = new SimpleStringProperty(
//            "# Infected: " + numInfected + "\n" +
//            "Initial % Immune: " + percentImmune.get() + "%\n" +
//            "# Vaccinated: " + numVaccinated + "\n" +
//            "Total Alive: " + (TOTAL_CELLS - numInfected) + "\t\n" +
//            "# Immune: " + numImmune + "\n" +
//            "Last Iteration completed: " + numIterations + "\n");
//
//    StringProperty log = new SimpleStringProperty("Results of Last Run:\n"+
//            "\tNumber saved: " +
//            "\n\t# Non-Immune Saved: " +
//            "\n\tPercent Saved: " +
//            "\n\tNumber of Turns Required: "
//    );

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
        dbgPane((Pane)(gamePane.getMainPane()), Color.rgb(74, 186, 145));
        dbgPane((Pane)gamePane.getBottom(), Color.rgb(73, 75, 75));
        dbgPane((Pane)gamePane.getLeft(), Color.rgb(13, 146, 99));

        boolean isPerson = true;    //TODO(Andrew) change this when algorithm is done
        player = new Player(isPerson);
        player.setMap(((WumpusWorldPane)gamePane).getPlayerMap());

        scene = new Scene(gamePane, WIDTH, HEIGHT );
        log.setEditable(false);
        updateDebugString("REAL PLAYER DATA: " + player.currentX + ", " + player.currentY +" \n");

        setPlayerKeyboardEvents();

        primaryStage.setScene(scene);
        primaryStage.show();
        player.initialUpdate();
        updateHumanPlayer();
        return 0;
    }

    public void buildGamePane()
    {
//        gamePane.setPrefSize(WIDTH, HEIGHT);
//        gamePane.setMinSize(500,500);
        gamePane.setAlignment(gamePane.getMainPane(), Pos.CENTER);
        ((WumpusWorldPane)gamePane).setLayout(WIDTH, HEIGHT);
        ((WumpusWorldPane)gamePane).options.getChildren().add(btnUpdate);
        btnUpdate.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                update();
            }
        });

    }

    public void setPlayerKeyboardEvents()
    {
//        boolean valid = false;
        gamePane.setOnKeyReleased( e ->
        {
            KeyCode code = e.getCode();
            if(code == KeyCode.W || code == KeyCode.UP)
            {
                // go up
                player.handleHumanCommand('u');
//                valid = true;
            }
            else if(code == KeyCode.S || code == KeyCode.DOWN)
            {
                // go down
                player.handleHumanCommand('d');
//                valid = true;
            }
            else if(code == KeyCode.A || code == KeyCode.LEFT)
            {
                // go left
                player.handleHumanCommand('l');
//                valid = true;
            }
            else if(code == KeyCode.D || code == KeyCode.RIGHT)
            {
                // go right
                player.handleHumanCommand('r');
//                valid = true;
            }
            else if(code == KeyCode.SPACE)
            {
                // Fire iff has arrow
                player.handleHumanCommand('f');
//                valid = true;
            }
            else if(code == KeyCode.ESCAPE)
            {
                // Leave iff at 0,0
                player.handleHumanCommand('e');
//                valid = true;
            }
            else if(code == KeyCode.F)
            {
                // Leave iff at 0,0
                player.handleHumanCommand('g');
//                valid = true;
            }
            if (player.needsUpdate)
            {
                addToLog("Want to update player\n");
                updateHumanPlayer();
            }
        });


    }

    public static boolean wumpusDeadCheck(Square s){
    	//System.out.println("in wumpus dead check");
    	if(((WumpusWorldPane) gamePane).getWorldMap().wumpusMap[s.x][s.y].getAttributes().contains('W')){
    		
    		return true;
    		}
    	else
    		return false;
    }
    
    public void updateHumanPlayer()
    {
        if(player.querySquare)
        {
            System.out.println("query square");
            player.currentSquare = ((WumpusWorldPane) gamePane).getWorldMap().wumpusMap[player.currentX][player.currentY];
            if(player.currentSquare.getAttributes().contains('W') | player.currentSquare.getAttributes().contains('P'))
            {
                player.isDead = true;
            }
            player.querySquare = false;
        }
        player.update();
        updateDebugString("REAL PLAYER DATA: " + player.currentX + ", " + player.currentY +" \n");
        ((WumpusWorldPane)gamePane).passPlayer(player);
         ((WumpusWorldPane)gamePane).update();
//        if(player.querySquare)
//        {
//            System.out.println("query square");
//            player.currentSquare = ((WumpusWorldPane) gamePane).getWorldMap().wumpusMap[player.currentX][player.currentY];
//            player.querySquare = false;
//        }

    }

    @Override
    public void update()
    {
        System.out.println("Updating");
        updateAgentPlayer();
        ((WumpusWorldPane)gamePane).update();
    }

    public void dbgUpdateText(String s)
    {
        updateKBString(s);
        updatePropertiesString(s);
        updateDebugString(s);
        addToLog(s);
    }

    public void updateAgentPlayer()
    {
        player.update();
    }

    public static void dbgPane(Pane p, Color c)
    {
        p.setBackground(new Background(new BackgroundFill(c, CornerRadii.EMPTY, new Insets( 0, 0, 0, 0) )));
    }

}
