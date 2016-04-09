//package sample;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * Created by Andrew on 3/30/2016.
 */
public class WumpusWorldPane extends GamePane
{
    Pane bottom; // should include log
    Map mainPane;
    public WumpusWorldPane()
    {
        super();
        this.mainPane = new Map();
        mainPane.autosize();

        ((GridPane)mainPane).setGridLinesVisible(true);
        this.setCenter(mainPane);
        bottom = new HBox();
        this.setBottom(bottom);
    }
}
