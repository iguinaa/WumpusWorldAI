

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.layout.*;

/**
 * Created by Andrew on 3/30/2016.
 */
public class WumpusWorldPane extends GamePane
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
        double leftWidth = Math.round(.2*width);
        if(width < leftWidth)
            System.out.println("Wumpus World Pane width too small");

        double centerSide = leftWidth > botHeight ? (width - leftWidth) : (height - botHeight);

        // NOTE: Only edit the reference. Don't use getChildren() to edit child properties if you value your sanity or time.
        options.setPrefSize( leftWidth, leftHeight);
        bottom.setPrefSize(botWidth, botHeight);

        FlowPane main = ((FlowPane)mainPane);

        main.setAlignment(Pos.CENTER);
        main.setPrefSize(centerSide, centerSide);

        // set grid too
        getWorldGrid().setPrefSize(centerSide/2, centerSide/2);
        getWorldGrid().setAlignment(Pos.CENTER);

        getWorldMap().setSquareSize(centerSide/2);

        getPlayerGrid().setPrefSize(centerSide/2, centerSide/2);
        getPlayerGrid().setAlignment(Pos.CENTER);
        getPlayerMap().setSquareSize(centerSide/2);


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
}
