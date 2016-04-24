//package sample;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * Created by Andrew on 4/3/2016.
 */
public class Square extends HBox implements Updateable
{
    public static final int DEFAULT_SIDE = 100; // Default side length
    // Characters
    // W, S, P, B, G, A, V
    public static final int NUM_IMAGES = 8;
    public static Image[] images = new Image[NUM_IMAGES];
    static String[] imagePaths = {
            "ganon.png", // W 0
            "stench.png", // S 1
            "Sarlacc.jpg", // P 2
            "breeze.png", // B 3
            "gold.png", // G 4
            "linkbow.png",  // A 5
            "tile.png",  // !(V) 6   could use bush.png
            "mystery2.png" //for empty squares on main map  //TODO Andrew: how do I use this?
    };
    public int prefSide;
    public int x;
    public int y;

    public boolean isStart()
    {
        return isStart;
    }

    public void setAsStart()
    {
        isStart = true;

        //Set visisted if this is the start square since it will always be true
        setMapChar('V');
        updateLayout();
    }

    public boolean attrsNeedUpdate;
    public boolean percNeedsUpdate;
    public boolean isStart = false;// This must be settable by player or map
    public boolean isPlayerMap = false;
    // NOTE(Andrew): perceptions should be a subset of attributes.
    public ArrayList<Character> attributes;
    public ArrayList<Character> perceptions;
    StackPane imgContainer;
    ImageView[] imgViews;
    Image testBG;
    ImageView testBGView;
    char mapChar;   // NOTE(Andrew): deprecated but supported
    private boolean hasWumpus = false;
    private boolean hasGold = false;    //Note: This also provides a perception "glitter"
    private boolean hasPit = false;
    private boolean hasPlayer = false;
    // Map-able Perceptions
    private boolean hasStench = false;
    private boolean hasBreeze = false;
    //player knowledge
    private boolean wasVisited = false; // NOTE: should these be stored here? could store in player, but then couldnt update images easily
    private int wumpusDangerScore = 0, pitDangerScore = 0, totalDangerScore = 0;

    public Square(boolean isPlayerMap, int x, int y)
    {
        super(10); // NOTE: 10 pixels border?

        this.mapChar = 'X';

        this.x = x;
        this.y = y;
        attributes = new ArrayList<Character>();
        perceptions = new ArrayList<Character>();
        attrsNeedUpdate = false;
        percNeedsUpdate = false;
        this.isPlayerMap = isPlayerMap;
        try
        {
            for (int i = 0; i < imagePaths.length; i++)
            {
                images[i] = new Image(imagePaths[i]);
            }
        } catch (Exception ex)
        {
            System.out.println("Hit Exception. Probably file not found");
            System.out.println(ex);
        }

//        testBG = new Image("blank.jpg");
//        testBGView = new ImageView();
//        testBGView.setImage(testBG);
//        testBGView.setFitWidth(Square.DEFAULT_SIDE);
//        testBGView.setPreserveRatio(true);
        imgContainer = new StackPane();
        imgViews = new ImageView[NUM_IMAGES];
        for (int i=0; i< imgViews.length; i++)
        {
            imgViews[i] = new ImageView();
            imgViews[i].setImage(images[i]);
            imgViews[i].setFitWidth(Square.DEFAULT_SIDE);
            imgViews[i].setPreserveRatio(true);
            imgContainer.getChildren().add(imgViews[i]);
        }
//        this.getChildren().add(testBGVew);
        this.getChildren().add(imgContainer);
        setLayout();

        //Set visited for main map so tiles show up
        if(!isPlayerMap)
            setMapChar('V');

        updateAttributes();
        updatePerceptions();
        setBG();
        dbgHoverTest();
    }

    public ArrayList<Character> getAttributes() {
        return attributes;
    }

    public ArrayList<Character> getPerceptions() {
        return perceptions;
    }

    public void setLayout()
    {
        this.setPadding(new Insets(2,2,2,2));
        Game.dbgPane(imgContainer, Color.DARKSLATEGRAY); // TODO: Remove
        if(x == 0 && y == 1)
        {
            Game.dbgPane(imgContainer, Color.GREEN); // TODO: Remove
        }
        else if(x == 1 && y == 0)
            Game.dbgPane(imgContainer, Color.RED); // TODO: Remove
    }

    public void updateLayout()
    {
        if(isStart)
        {
            Game.dbgPane(imgContainer, Color.WHEAT);
        }
    }


//    public Square(char mapChar)
//    {
//        super(10); // NOTE: 10 pixels border?
//        this.mapChar = mapChar;
//        setBG();
//        this.getChildren().add(testBGView);
//    }

    // W, S, P, B, G, A, V
    public void setMapChar(char mapChar)
    {
        this.mapChar = mapChar;
        switch (mapChar)
        {
            case 'W':
            {
                this.hasWumpus = true;
            }
            break;
            case 'S':
            {
                this.hasStench = true;
            }
            break;
            case 'P':
            {
                this.hasPit = true;
            }
            break;
            case 'B':
            {
                this.hasBreeze = true;
            }
            break;
            case 'G':
            {
                this.hasGold = true;
            }
            break;
            case 'A':
            {
                this.hasPlayer = true;
            }
            break;
            case 'V':
            {
                this.wasVisited = true;
            }
            break;
            default:
            {
                System.out.println("Invalid Character");
            }
        }
        attrsNeedUpdate = true;
        percNeedsUpdate = true;
//        updateAttributes();
//        updatePerceptions();
//        setBG();
    }

    private void updateAttributes()
    {
        // W, S, P, B, G, A, V
        int[] flagForRemoval = new int[15];
        for(int j = 0; j < flagForRemoval.length; j++)
        {
            flagForRemoval[j] = -1;
        }
        int arrCounter = 0;

        if (this.hasWumpus)
        {
            if (!attributes.contains(Character.valueOf('W')))
            {
                attributes.add(new Character('W'));
            }
        }
        else if(!this.hasWumpus)
        {
            if (attributes.contains(Character.valueOf('W')))
            {
                for(int i=0; i < attributes.size(); i++)
                {
                    if(attributes.get(i).charValue() == 'W')
                    {
                        flagForRemoval[arrCounter] = i;
                        arrCounter++;
                    }

                }
            }
        }
        for(int j = 0; j <= arrCounter; j++)
        {
            if( flagForRemoval[j] != -1)
                attributes.remove(j);
        }
        for(int j = 0; j < flagForRemoval.length; j++)
        {
            flagForRemoval[j] = -1;
        }
        arrCounter = 0;



        if (this.hasStench)
        {
            if (!attributes.contains(Character.valueOf('S')))
            {
                attributes.add(new Character('S'));
            }
        }

        if (this.hasPit)
        {
            if (!attributes.contains(Character.valueOf('P')))
            {
                attributes.add(new Character('P'));
            }
        }

        if (this.hasBreeze)
        {
            if (!attributes.contains(Character.valueOf('B')))
            {
                attributes.add(new Character('B'));
            }
        }

        if (this.hasGold)
        {
            if (!attributes.contains(Character.valueOf('G')))
            {
                attributes.add(new Character('G'));
            }
        }
        else if(!this.hasGold)
        {
            if (attributes.contains(Character.valueOf('G')))
            {
                for(int i=0; i < attributes.size(); i++)
                {
                    if(attributes.get(i).charValue() == 'G')
                    {
                        flagForRemoval[arrCounter] = i;
                        arrCounter++;
                    }

                }
            }
        }
        for(int j = 0; j <= arrCounter; j++)
        {
            if( flagForRemoval[j] != -1)
                attributes.remove(j);
        }
        for(int j = 0; j < flagForRemoval.length; j++)
        {
            flagForRemoval[j] = -1;
        }
        arrCounter = 0;




        if (this.hasPlayer)
        {
            if (!attributes.contains(Character.valueOf('A')))
            {
                attributes.add(new Character('A'));
            }
        }
        else if(!this.hasPlayer)
        {
            if (attributes.contains(Character.valueOf('A')))
            {
                for(int i=0; i < attributes.size(); i++)
                {
                    if(attributes.get(i).charValue() == 'A')
                    {
                        flagForRemoval[arrCounter] = i;
                        arrCounter++;
                    }

                }
            }
        }
        for(int j = 0; j <= arrCounter; j++)
        {
            if( flagForRemoval[j] != -1)
                attributes.remove(j);
        }
        for(int j = 0; j < flagForRemoval.length; j++)
        {
            flagForRemoval[j] = -1;
        }
        arrCounter = 0;


        if (this.wasVisited)
        {
            if (!attributes.contains(Character.valueOf('V')))
            {
                attributes.add(new Character('V'));
            }
        }

        attrsNeedUpdate = false;

    }

    private void updatePerceptions()
    {
        // S, B, G
        if (this.hasStench)
        {
            if (!perceptions.contains(Character.valueOf('S')))
            {
                perceptions.add(new Character('S'));
            }
        }

        if (this.hasBreeze)
        {
            if (!perceptions.contains(Character.valueOf('B')))
            {
                perceptions.add(new Character('B'));
            }
        }
        if (this.hasGold)
        {
            if (!perceptions.contains(Character.valueOf('G')))
            {
                perceptions.add(new Character('G'));
            }
        }

        percNeedsUpdate = false;
    }

//    public void addMapChar(char mapChar)
//    {
//        this.mapChar = mapChar;
//        switch (mapChar)
//        {
//            case 'W':
//            {
//                this.hasWumpus = true;
//            }
//            break;
//            case 'S':
//            {
//                this.hasStench = true;
//            }
//            break;
//            case 'P':
//            {
//                this.hasPit = true;
//            }
//            break;
//            case 'B':
//            {
//                this.hasBreeze = true;
//            }
//            break;
//            case 'G':
//            {
//                this.hasGold = true;
//            }
//            break;
//            case 'A':
//            {
//                this.hasPlayer = true;
//            }
//            break;
//            case 'V':
//            {
//                this.wasVisited = true;
//            }
//            break;
//            default:
//            {
//                System.out.println("Invalid Character");
//            }
//        }
//        updateAttributes();
//        updatePerceptions();
//        setBG();
//    }

    public void removeMapChar(char mapChar)
    {
        switch (mapChar)
        {
            case 'W':
            {
                this.hasWumpus = false;
            }
            break;
            case 'S':
            {
                this.hasStench = false;
            }
            break;
            case 'P':
            {
                this.hasPit = false;
            }
            break;
            case 'B':
            {
                this.hasBreeze = false;
            }
            break;
            case 'G':
            {
                this.hasGold = false;
            }
            break;
            case 'A':
            {
                this.hasPlayer = false;
            }
            break;
            case 'V':
            {
                this.wasVisited = false;
            }
            break;
            default:
            {
                System.out.println("Invalid Character");
            }
        }
        attrsNeedUpdate = true;
        percNeedsUpdate = true;
//        setBG();
    }

    void setBG()
    {
//        String bgPath = "";
//        if (mapChar == 'X')
//            bgPath = "blank.jpg";
//        else
//            bgPath = "notblank.jpg";
        //FIXME(Andrew): Wumpus appears on blank squares.
        for(ImageView img : imgViews)
        {
            imgContainer.getChildren().remove(img);
        }

        if(!attributes.contains(Character.valueOf('V')))
        {
            imgContainer.getChildren().add(imgViews[NUM_IMAGES - 1]);
        }
        else
        {
            for (int j = 0; j < attributes.size(); j++)
            {
                int i = enumChars(attributes.get(j));
                imgViews[i].setFitWidth(Square.DEFAULT_SIDE);
                imgContainer.getChildren().add(imgViews[i]);
            }
        }
//        testBG = new Image(bgPath);
//        testBGView.setImage(testBG);
//        testBGView.setFitWidth(Square.DEFAULT_SIDE);
//        testBGView.setPreserveRatio(true);
    }


    // W, S, P, B, G, A, V
    public int enumChars(char c)
    {
        int arrayIndex = -1;
        switch (c)
        {
            case 'W':
            {
                arrayIndex = 0;
            }
            break;
            case 'S':
            {
                arrayIndex = 1;

            }
            break;
            case 'P':
            {
                arrayIndex = 2;

            }
            break;
            case 'B':
            {
                arrayIndex = 3;

            }
            break;
            case 'G':
            {
                arrayIndex = 4;

            }
            break;
            case 'A':
            {
                arrayIndex = 5;

            }
            break;
            case 'V':
            {
                arrayIndex = 6;

            }
            break;
            default:
            {
                System.out.println("Invalid Character");
            }
        }
        return arrayIndex;
    }

    public void setPrefSide(int prefSide)
    {
        this.prefSide = prefSide;
        this.setPrefWidth((double)prefSide);
        this.setPrefHeight((double)prefSide);
        this.imgContainer.setPrefSize((double)prefSide, (double)prefSide);
    }


    @Override
    public void update()
    {
        //TODO(Andrew): Update images based on 'knowledge' not fact
        updateAttributes();
        updatePerceptions();
        setBG();

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
        this.pitDangerScore += pitDangerScore;
        totalDangerScore = this.pitDangerScore + wumpusDangerScore;
    }

    public void resetPitDangerScore() {

        pitDangerScore = 0;

    }

    public int getWumpusDangerScore() {
        return wumpusDangerScore;
    }

    //Change score up or down based on new data
    public void setWumpusDangerScore(int wumpusDangerScore) {
        this.wumpusDangerScore += wumpusDangerScore;
        totalDangerScore = this.wumpusDangerScore + pitDangerScore;
    }

    public void resetWumpusDangerScore() {
        wumpusDangerScore = 0;
    }

    public int getTotalDangerScore() {
        return totalDangerScore;
    }

    public void dbgHoverTest()
    {
        this.setOnMouseEntered(new EventHandler<MouseEvent>
                () {

            @Override
            public void handle(MouseEvent t) {
                show(true);
            }
        });

        this.setOnMouseExited(new EventHandler<MouseEvent>
                () {

            @Override
            public void handle(MouseEvent t) {
                show(false);
            }
        });
    }

    public void show(boolean show)  // TODO(Andrew): Comment out for release
    {
        if(show)
        {
            String out = "";
            for (Character c : attributes)
            {
                out = out + c.toString() + ", ";
            }
            out = out + "\n";
            Game.addToLog(out);
        }
    }


}
