

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Created by Andrew on 3/30/2016.
 */
public class WumpusWorldPane extends GamePane implements Updateable
{
    HBox bottom; // should include log
    VBox options;

    Map worldMap;
    int worldMapIndex;

    Map playerMap;
    int playerMapIndex;

    public WumpusWorldPane(int width, int height)
    {
        super();
//        this.setMainPane(new Map());
        this.setMainPane(new FlowPane());
        bottom = new HBox();
        options = new VBox();
        this.setCenter((FlowPane)mainPane);
        this.setBottom(bottom);
        this.setLeft(options);

        worldMap = new Map(true);
        worldMapIndex = 0;

        playerMap = new Map(false);
        playerMapIndex = 1;

        initPanes(width, height);
        setLayout(width, height);
    }

    /** This should add all child nodes in panes*/
    public void initPanes(int w, int h)
    {
        this.getMainPane().getChildren().add(worldMapIndex, worldMap);
        getWorldGrid().setGridLinesVisible(true);

        this.getMainPane().getChildren().add(playerMapIndex, playerMap);
        getPlayerGrid().setGridLinesVisible(true);
//        StackPane sKB = new StackPane();
//        StackPane sProperties = new StackPane();
//        StackPane sDbgData = new StackPane();
//        Game.dbgPane(sKB, Color.HONEYDEW);
//        Game.dbgPane(sProperties, Color.RED);
//        Game.dbgPane(sDbgData, Color.VIOLET);
        Text KB = new Text();
        Text properties = new Text();
        Text dbgData = new Text();
//        sKB.getChildren().add(KB);
//        sProperties.getChildren().add(properties);
//        sDbgData.getChildren().add(dbgData);
        KB.textProperty().bind(Game.KB);
        properties.textProperty().bind(Game.properties);
        dbgData.textProperty().bind(Game.debugData);
        properties.setFill(Color.rgb(212, 206, 70));
        KB.setFill(Color.rgb(212, 206, 70));
        dbgData.setFill(Color.rgb(212, 206, 70));
//        bottom.setAlignment(Pos.CENTER);

        bottom.getChildren().addAll(KB, properties, Game.log, dbgData);
//      this.getMainPane().getChildren().addAll(worldMap, playerMap);

    }

    /** This method should contain all resizing attempts */
    public void setLayout(int width, int height)
    {
        // size calculations
        double botWidth = width;
        double botHeight = 200;
        if(height < botHeight)
            System.out.println("Wumpus World Pane height too small");

        double leftHeight = height-botHeight;
        double leftWidth = Math.round(.1*width);
        if(width < leftWidth)
            System.out.println("Wumpus World Pane width too small");

        double centerSide = leftWidth > botHeight ? (width - leftWidth) : (height - botHeight);

        // NOTE: Only edit the reference. Don't use getChildren() to edit child properties if you value your sanity or time.
        options.setPrefSize( leftWidth, leftHeight);
        options.setAlignment(Pos.TOP_CENTER);
        options.setSpacing(10.0);
        options.setPadding(new Insets(5,5,5,5));
        bottom.setPrefSize(botWidth, botHeight);
        bottom.setSpacing(10.0);
        bottom.setPadding(new Insets(5,5,5,5));

        FlowPane main = ((FlowPane)mainPane);
        main.setHgap(10.0);
//        main.setSpacing(10.0);
        main.setPadding(new Insets(5,5,5,5));
        main.setAlignment(Pos.CENTER);
        main.setPrefSize(centerSide, centerSide);

        // set grid too
        getWorldGrid().setPrefSize(centerSide/2, centerSide/2);
        getWorldGrid().setAlignment(Pos.CENTER);
        getWorldGrid().setPadding(new Insets(5,5,5,5));
        getWorldMap().setSquareSize(centerSide/2);

        getPlayerGrid().setPrefSize(centerSide/2, centerSide/2);
        getPlayerGrid().setAlignment(Pos.CENTER);
        getPlayerGrid().setPadding(new Insets(5,5,5,5));
        getPlayerMap().setSquareSize(centerSide/2 - 20);

    }

    public void passPlayer(Player p)
    {
        Game.updateDebugString(Game.getDebugData().toString() + "In passPlayer + Player is at  " + p.currentX + ", " + p.currentY + "\n");
        for(int i = 0 ; i < getWorldMap().wumpusMap.length; i++)
        {
            for(int j=0; j < getWorldMap().wumpusMap[i].length; j++)
            {
                if(i == p.currentX && j == p.currentY)
                {
                    getWorldMap().wumpusMap[i][j].setMapChar('A');
//                    Game.addToLog("Player Loc = " + i + ", " + j + "\n");
//                    getWorldMap().wumpusMap[i][j].update();
                }
                else
                {
                    getWorldMap().wumpusMap[i][j].removeMapChar('A');
//                    getWorldMap().wumpusMap[i][j].update();
//                    Game.addToLog("Not Player\n");
                }
            }

        }

//        for(int i = 0 ; i < getPlayerMap().wumpusMap.length; i++)
//        {
//            for(int j=0; j < getPlayerMap().wumpusMap[i].length; j++)
//            {
//                if(i == p.currentX && j == p.currentY)
//                {
//                    getPlayerMap().wumpusMap[i][j].setMapChar('A');
//                }
//                else
//                {
//                    getPlayerMap().wumpusMap[i][j].removeMapChar('A');
//                }
//            }
//
//        }

    }

    public GridPane getWorldGrid()
    {
        return ((GridPane)worldMap);
    }

    public Map getWorldMap()
    {
        return worldMap;
    }

    public GridPane getPlayerGrid()
    {
        return ((GridPane)playerMap);
    }

    public Map getPlayerMap()
    {
        return playerMap;
    }

    @Override
    public void update()
    {
        worldMap.update();
    }
}
