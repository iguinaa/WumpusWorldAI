//package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * Created by Andrew on 4/3/2016.
 */
public class Square extends HBox implements Updateable
{
    public static final int DEFAULT_SIDE = 100; // Default side length

    public int prefSide;
    public int x;
    public int y;


    // TODO(andrew): edit these if time allows
    // some of these can stack and some cant. ex: wumpus && pit == FALSE
    // objects in world
    private boolean hasWumpus = false;
    private boolean hasGold = false;    //Note: This also provides a perception "glitter"
    private boolean hasPit = false;
    private boolean hasPlayer = false;

    // Map-able Perceptions
    private boolean hasStench = false;
    private boolean hasBreeze = false;

    //player knowledge
    private boolean wasVisited; // NOTE: should these be stored here? could store in player, but then couldnt update images easily
    public boolean isStart = false;// This must be settable by player or map
    public boolean isPlayerMap = false;


    // NOTE(Andrew): perceptions should be a subset of attributes.
    public ArrayList<Character> attributes;
    public ArrayList<Character> perceptions;

    public ArrayList<Character> getAttributes()
    {
        return attributes;
    }

    public ArrayList<Character> getPerceptions()
    {
        return perceptions;
    }


    // Characters
    // W, S, P, B, G, A, V
    public static final int NUM_IMAGES = 7;
    static String[] imagePaths = {
            "ganon.png", // W 0
            "notblank.jpg", // S 1
            "notblank.jpg", // P 2
            "notblank.jpg", // B 3
            "notblank.jpg", // G 4
            "linkbow.png",  // A 5
            "mystery2.png"  // !(V) 6   could use bush.png
    };

    public static Image[] images = new Image[NUM_IMAGES];
    StackPane imgContainer;
    ImageView[] imgViews;

    Image testBG;
    ImageView testBGView;

    char mapChar;

    public Square(boolean isPlayerMap, int x, int y)
    {
        super(10); // NOTE: 10 pixels border?
        this.mapChar = 'V';
        this.x = x;
        this.y = y;
        attributes = new ArrayList<Character>();
        perceptions = new ArrayList<Character>();
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
        setMapChar('V');
        setBG();
    }

    public void setLayout()
    {
        // TODO: Stub

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
        updateAttributes();
        updatePerceptions();
        setBG();
    }

    private void updateAttributes()
    {
        // W, S, P, B, G, A, V
        if (this.hasWumpus)
        {
            if (!attributes.contains(Character.valueOf('W')))
            {
                attributes.add(new Character('W'));
            }
        }
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
        if (this.hasPlayer)
        {
            if (!attributes.contains(Character.valueOf('A')))
            {
                attributes.add(new Character('A'));
            }
        }
        if (this.wasVisited)
        {
            if (!attributes.contains(Character.valueOf('V')))
            {
                attributes.add(new Character('V'));
            }
        }
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
    }

    public void addMapChar(char mapChar)
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
        setBG();
    }

    public void removeMapChar(char mapChar)
    {
        this.mapChar = mapChar;
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
        setBG();
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
        for (int j = 0; j < attributes.size(); j++)
        {
            int i = enumChars(attributes.get(j));
            imgViews[i].setFitWidth(Square.DEFAULT_SIDE);
            imgContainer.getChildren().add(imgViews[i]);
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


    }
}
