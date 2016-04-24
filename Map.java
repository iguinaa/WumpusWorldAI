

import javafx.scene.layout.GridPane;

/**
 * Created by Alexander Wills on 4/2/16.
 */

import java.util.Random;

//Generate map to play wumpusworld game on
public class Map extends GridPane implements Updateable
{

    public Square[][] wumpusMap;
    Random random = new Random();
    private int width;

    public int getNumRows()
    {
        return height;
    }

    public int getNumCols()
    {
        return width;
    }

    private int height;
    public boolean isPlayerMap;



    //Constructor
    Map(boolean seeded) { //boolean tells us to seed the map or not

        wumpusMap = new Square[4][4];
        width = 4;
        height = 4;

        if (seeded) {
            isPlayerMap = false;
            seedMap();
        }
        else{
            isPlayerMap = true;

            //i == intended x; j == intended y
            for(int i = 0; i < height; i++) {

                for (int j = 0; j < height; j++) {

                    wumpusMap[i][j] = new Square(isPlayerMap, i, j);
                    this.add(wumpusMap[i][j], toRealX(i), toRealY(j));

                }
            }

            wumpusMap[0][0].setAsStart();
            wumpusMap[0][0].setMapChar('A');
//            for(int i = 0; i < height; i++) {
//
//                for (int j = 0; j < height; j++) {
//
//                    wumpusMap[toRealX(i)][toRealY(j)] = new Square(isPlayerMap, i, j);
//                    this.add(wumpusMap[toRealX(i)][toRealY(j)], toRealX(i), toRealY(j));
//
//                }
//            }
        }

    }

    //Construct map with specific dimensions
    public Map(int width, int height, boolean blankMap) {

        wumpusMap = new Square[width][height];
        this.width = width;
        this.height = height;

        if (blankMap) {
            seedMap();
        }
        else
        {
            //i == x; j == y
            for(int i = 0; i < height; i++) {

                for (int j = 0; j < height; j++) {

                    wumpusMap[i][j] = new Square(isPlayerMap, i, j);
                    this.add(wumpusMap[i][j], toRealX(i), toRealY(j));

                }

            }

            wumpusMap[0][0].setAsStart();
            wumpusMap[0][0].setMapChar('A');
            initialUpdate();
//            for(int i = 0; i < height; i++) {
//
//                for (int j = 0; j < height; j++) {
//
//                    wumpusMap[toRealX(i)][toRealY(j)] = new Square(isPlayerMap, i, j);
//                    this.add(wumpusMap[toRealX(i)][toRealY(j)], toRealX(i), toRealY(j));
//
//                }
//            }
        }
    }


    private void seedMap(){

    int pitCount = (width * height)/5; //Determine a number of pits to put into the game, trying /5 based on book map
        int x, y;

        //Fill the array with Square objects
        //i == x; j == y
        for(int i = 0; i < width; i++) {

            for (int j = 0; j < height; j++) {

                wumpusMap[i][j] = new Square(isPlayerMap, i, j);
                this.add(wumpusMap[i][j], toRealX(i), toRealY(j));

            }
        }
//        for(int i = 0; i < width; i++) {
//
//            for (int j = 0; j < height; j++) {
//
//                wumpusMap[toRealX(i)][toRealY(j)] = new Square(isPlayerMap, i, j);
//                this.add(wumpusMap[toRealX(i)][toRealY(j)], toRealX(i), toRealY(j));
//
//            }
//        }

        //Set starting point (this is location 1, 1 on the map)
//        wumpusMap[0][0].isStart = true;
        wumpusMap[0][0].setAsStart();
        wumpusMap[0][0].setMapChar('A');

        //Assign pits
        for(int i = 0; i < pitCount; i++)   {

            //exclude protagonist starting point
            do {
                x = random.nextInt(width);
                y = random.nextInt(height);
                System.out.println("Testing Rand Efficiency Pit"); // TODO: Remove
            } while (x == 0 && y == 0);

            System.out.println("x " + x + " y " + y);

            //Assign breeze first so it doesn't write over pit

            // in the Square class to set its char appropriately once all stats are assigned
            if(x != width - 1) {
//                wumpusMap[x + 1][y].hasBreeze = true;
                wumpusMap[x + 1][y].setMapChar('B');

            }

            if(x != 0) {
//                wumpusMap[x - 1][y].hasBreeze = true;
                wumpusMap[x - 1][y].setMapChar('B');

            }

            if(y != height - 1) {
//                wumpusMap[x][y + 1].hasBreeze = true;
                wumpusMap[x][y + 1].setMapChar('B');

            }

            if(y != 0) {
//                wumpusMap[x][y - 1].hasBreeze = true;
                wumpusMap[x][y - 1].setMapChar('B');

            }

            //Assign pit
//            wumpusMap[x][y].hasPit = true;
            wumpusMap[x][y].setMapChar('P');
        }

        //Assign wumpus location
        do
        {
            x = random.nextInt(width);
            y = random.nextInt(height);
            System.out.println("Testing Rand Efficiency Wumpus"); // TODO: Remove
        } while( x==0 && y==0);

//        wumpusMap[x][y].hasWumpus = true;
        wumpusMap[x][y].mapChar = 'W';
        wumpusMap[x][y].setMapChar('W');
        wumpusMap[x][y].setMapChar('S');

        //assign stench

        if (x != width - 1) {
//            wumpusMap[x + 1][y].hasStench = true;
//            wumpusMap[x + 1][y].mapChar = 'S';
            wumpusMap[x + 1][y].setMapChar('S');
        }

        if (x != 0) {
//            wumpusMap[x - 1][y].hasStench = true;
//            wumpusMap[x - 1][y].mapChar = 'S';
            wumpusMap[x - 1][y].setMapChar('S');
        }

        if (y != height - 1) {
//            wumpusMap[x][y + 1].hasStench = true;
//            wumpusMap[x][y + 1].mapChar = 'S';
            wumpusMap[x][y + 1].setMapChar('S');
        }

        if (y != 0) {
//            wumpusMap[x][y - 1].hasStench = true;
//            wumpusMap[x][y - 1].mapChar = 'S';
            wumpusMap[x][y - 1].setMapChar('S');
        }

        //assign gold
        x = random.nextInt(width);
        y = random.nextInt(height);
//        wumpusMap[x][y].hasGold = true;
//        wumpusMap[x][y].mapChar = 'G';
        wumpusMap[x][y].setMapChar('G');

        initialUpdate();

    }

    public void printMap() {

        for (int i = 0; i < height; i++) {

            for (int j = 0; j < height; j++)
            {

                System.out.print(wumpusMap[i][j].mapChar + " ");

            }

            System.out.println();

        }

//        for (int i = 0; i < height; i++) {
//
//            for (int j = 0; j < height; j++)
//            {
//
//                System.out.print(wumpusMap[toRealX(i)][toRealY(j)].mapChar + " ");
//
//            }
//
//            System.out.println();
//
//        }
    }

    public void setSquareSize(double givenPrefHeight)
    {
        this.setPrefSize(givenPrefHeight, givenPrefHeight);
        double cellSize = Math.round(givenPrefHeight / wumpusMap.length);

        //i == x; j == y
        for(int i = 0; i < width; i++) {

            for (int j = 0; j < height; j++) {

                wumpusMap[i][j].setPrefSide((int)cellSize);

            }
        }
//        for(int i = 0; i < width; i++) {
//
//            for (int j = 0; j < height; j++) {
//
//                wumpusMap[toRealX(i)][toRealY(j)].setPrefSide((int)cellSize);
//
//            }
//        }
    }

    public void initialUpdate()
    {

        for(int i = 0; i < width; i++) {

            for (int j = 0; j < height; j++) {

                wumpusMap[i][j].initialUpdate();

            }
        }
    }


    @Override
    public void update()
    {
        //i == x; j == y
        for(int i = 0; i < width; i++) {

            for (int j = 0; j < height; j++) {

                wumpusMap[i][j].update();

            }
        }
//        for(int i = 0; i < width; i++) {
//
//            for (int j = 0; j < height; j++) {
//
//                wumpusMap[toRealX(i)][toRealY(j)].update();
//
//            }
//        }
    }


    // REAL MAP LAYOUT
    // START POS: 0,MAX(j)
    // 0,0  1,0  2,0
    // 0,1  1,1  2,1
    // 0,2  1,2  2,2
    
    // INTENDED MAP LAYOUT
    // 0,2  1,2  2,2
    // 0,1  1,1  2,1
    // 0,0  1,0  2,0
    public int toRealX(int intended_x)
    {
        return intended_x;
    }
    public int toRealY(int intended_y)
    {
        return (height-1-intended_y);
    }
    public int toIntendedX(int real_x)
    {
        return real_x;
    }
    public int toIntendedY(int real_y)
    {
        return (Math.abs(real_y-height-1));
    }
}

