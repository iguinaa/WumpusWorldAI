/**
 * Created by Alexander Wills on 4/2/16.
 */

import javafx.scene.layout.GridPane;

//Generate map to play wumpusworld game on
public class Map extends GridPane{

    public Square[][] wumpusMap;
    private int width;
    private int height;

    //Constructor
    Map() {

        wumpusMap = new Square[4][4];
        width = 4;
        height = 4;

        seedMap();

    }

    //Construct map with specific dimensions
    Map(int width, int height) {

        wumpusMap = new Square[width][height];
        this.width = width;
        this.height = height;

        seedMap();
    }

    private void seedMap() {

        int pitCount = (width * height) / 5; //Determine a number of pits to put into the game, trying /5 based on book map
        int x, y;

        //Fill the array with Square objects
        for (int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {

                wumpusMap[j][i] = new Square();

            }
        }

        //Set starting point (this is location 1, 1 on the map)
        wumpusMap[0][0].isStart = true;

        //Assign pits
        for (int i = 0; i < pitCount; i++) {

            //Prevent pit from generating in starting location
            x = (int) (Math.random() * (width - 1) + 1);
            y = (int) (Math.random() * (height - 1) + 1);

            //Assign breeze first so it doesn't write over pit
            if (x != width - 1) {
                wumpusMap[x + 1][y].hasBreeze = true;
                wumpusMap[x + 1][y].mapChar = 'B';
                wumpusMap[x][y].setMapChar('G');
            }

            if (x != 0) {
                wumpusMap[x - 1][y].hasBreeze = true;
                wumpusMap[x - 1][y].mapChar = 'B';
                wumpusMap[x][y].setMapChar('G');
            }

            if (y != height - 1) {
                wumpusMap[x][y + 1].hasBreeze = true;
                wumpusMap[x][y + 1].mapChar = 'B';
                wumpusMap[x][y].setMapChar('G');
            }

            if (y != 0) {
                wumpusMap[x][y - 1].hasBreeze = true;
                wumpusMap[x][y - 1].mapChar = 'B';
                wumpusMap[x][y].setMapChar('G');
            }

            //Assign pit
            wumpusMap[x][y].hasPit = true;
            wumpusMap[x][y].mapChar = 'P';
            wumpusMap[x][y].setMapChar('G');
        }

        //Assign wumpus location
        x = (int) (Math.random() * (width - 1) + 1);
        y = (int) (Math.random() * (height - 1) + 1);
        wumpusMap[x][y].hasWumpus = true;
        wumpusMap[x][y].mapChar = 'W';
        wumpusMap[x][y].setMapChar('G');
        //assign stench
        if (x != width - 1) {
            wumpusMap[x + 1][y].hasStench = true;
            wumpusMap[x + 1][y].mapChar = 'S';
            wumpusMap[x][y].setMapChar('G');
        }

        if (x != 0) {
            wumpusMap[x - 1][y].hasStench = true;
            wumpusMap[x - 1][y].mapChar = 'S';
            wumpusMap[x][y].setMapChar('G');
        }

        if (y != height - 1) {
            wumpusMap[x][y + 1].hasStench = true;
            wumpusMap[x][y + 1].mapChar = 'S';
            wumpusMap[x][y].setMapChar('G');
        }

        if (y != 0) {
            wumpusMap[x][y - 1].hasStench = true;
            wumpusMap[x][y - 1].mapChar = 'S';
            wumpusMap[x][y].setMapChar('G');
        }

        //assign gold
        x = (int) (Math.random() * (width - 1) + 1);
        y = (int) (Math.random() * (height - 1) + 1);
        wumpusMap[x][y].hasGold = true;
        wumpusMap[x][y].mapChar = 'G';
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

}