//package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Created by Andrew on 4/3/2016.
 */
public class Square extends HBox implements Updateable
{
    public static final int DEFAULT_SIDE = 100; // Default side length


    public int prefSide;
    // TODO(andrew): edit these if time allows
    // some of these can stack and some cant. ex: wumpus && pit == FALSE
    boolean hasWumpus = false;
    boolean hasGold = false;
    boolean hasStench = false;
    boolean hasBreeze = false;
    boolean hasPit = false;
    boolean isStart = false;
    Image testBG;
    ImageView testBGView;
    char mapChar;
    //player knowledge
    private boolean wasVisited; // NOTE: should these be stored here? could store in player, but then couldnt update images easily
    private int wumpusDangerScore = 0, pitDangerScore = 0;

    public Square ()
    {
        super(10); // NOTE: 10 pixels border?
        this.mapChar = 'X';
        testBG = new Image("blank.jpg");
        testBGView = new ImageView();
        testBGView.setImage(testBG);
        testBGView.setFitWidth(Square.DEFAULT_SIDE);
        testBGView.setPreserveRatio(true);
        this.getChildren().add(testBGView);
        setLayout();
    }


    public Square (char mapChar)
    {
        super(10); // NOTE: 10 pixels border?
        this.mapChar = mapChar;
        setBG();
        this.getChildren().add(testBGView);
    }

    public void setLayout() {
        // TODO: Stub

    }

    public void setMapChar(char mapChar)
    {
        this.mapChar = mapChar;
        setBG();
    }

    void setBG()
    {
        String bgPath = "";
        if(mapChar == 'X')
            bgPath = "blank.jpg";
        else
            bgPath = "notblank.jpg";

        testBG = new Image(bgPath);
        testBGView.setImage(testBG);
        testBGView.setFitWidth(Square.DEFAULT_SIDE);
        testBGView.setPreserveRatio(true);
    }

    public void setPrefSide(int prefSide)
    {
        this.prefSide = prefSide;
        this.setPrefWidth((double)prefSide);
        this.setPrefHeight((double)prefSide);
        this.testBGView.setFitWidth(prefSide);
    }


    @Override
    public void update()
    {
        //TODO(Andrew): Update images based on 'knowledge' not fact


    }

    public boolean isWasVisited() {
        return wasVisited;
    }

    public void setWasVisited(boolean wasVisited) {
        this.wasVisited = wasVisited;
    }

    public int getPitDangerScore() {
        return pitDangerScore;
    }

    public void setPitDangerScore(int pitDangerScore) {
        this.pitDangerScore = pitDangerScore;
    }

    public int getWumpusDangerScore() {
        return wumpusDangerScore;
    }

    public void setWumpusDangerScore(int wumpusDangerScore) {
        this.wumpusDangerScore = wumpusDangerScore;
    }
}
