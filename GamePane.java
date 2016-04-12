

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * Created by Andrew on 3/29/2016.
 */
public class GamePane extends BorderPane
{
    Pane mainPane;

    public GamePane()
    {

    }

    public GamePane getThisPane()
    {
        return this;
    }

    public Pane getMainPane()
    {
        return this.mainPane;
    }

    public void setMainPane(Pane p)
    {
        this.mainPane = p;
    }

}
