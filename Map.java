

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

            //i == y; j == x
            for(int i = 0; i < height; i++) {

                for (int j = 0; j < width; j++) {

                    wumpusMap[j][i] = new Square(isPlayerMap, j, i);
                    this.add(wumpusMap[j][i], j, i);

                }
            }
        }

    }

    //Construct map with specific dimensions
    public Map(int width, int height, boolean blankMap) {

        wumpusMap = new Square[width][height];
        this.width = width;
        this.height = height;

        if (blankMap) {
            seedMap();
        } else {
            //i == y; j == x
            for (int i = 0; i < height; i++) {

                for (int j = 0; j < width; j++) {

                    wumpusMap[j][i] = new Square(isPlayerMap, j, i);
                    this.add(wumpusMap[j][i], j, i);

                }
            }
        }
    }


    private void seedMap(){

    int pitCount = (width * height)/5; //Determine a number of pits to put into the game, trying /5 based on book map
        int x, y;

        //Fill the array with Square objects
        //i == y; j == x
        for(int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {

                wumpusMap[j][i] = new Square(isPlayerMap, j, i);
                this.add(wumpusMap[j][i], j, i);

            }
        }

        //Set starting point (this is location 1, 1 on the map)
        wumpusMap[0][0].isStart = true;

        //Assign pits
        for(int i = 0; i < pitCount; i++)   {

            //exclude protagonist starting point
            do {
                x = random.nextInt(width);
                y = random.nextInt(height);
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
        x = random.nextInt(width);
        y = random.nextInt(height);
//        wumpusMap[x][y].hasWumpus = true;
        wumpusMap[x][y].mapChar = 'W';
        wumpusMap[x][y].setMapChar('W');

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
            wumpusMap[x][y + 1].mapChar = 'S';
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

    }

    public void printMap() {

        for (int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {

                System.out.print(wumpusMap[j][i].mapChar + " ");

            }

            System.out.println();

        }

    }

    public void setSquareSize(double givenPrefHeight)
    {
        this.setPrefSize(givenPrefHeight, givenPrefHeight);
        double cellSize = Math.round(givenPrefHeight / wumpusMap.length);

        //i == y; j == x
        for(int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {

                wumpusMap[j][i].setPrefSide((int)cellSize);

            }
        }
    }

    @Override
    public void update()
    {
        //i == y; j == x
        for(int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {

                wumpusMap[j][i].update();

            }
        }
    }
}
