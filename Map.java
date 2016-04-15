/**
 * Created by Alexander Wills on 4/2/16.
 */

import javafx.scene.layout.GridPane;

//Generate map to play wumpusworld game on
public class Map extends GridPane{

    Square[][] wumpusMap;
    private int width;
    private int height;
    private int def;	//set to 0 for a default map, 1 for random

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

    //Construct map for a default map
    Map(int def) {
    	wumpusMap = new Square[4][4];
        width = 4;
        height = 4;
    	if (def == 1)
			seedMap();
    	else
    		defaultMap();
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

            }

            if (x != 0) {
                wumpusMap[x - 1][y].hasBreeze = true;
                wumpusMap[x - 1][y].mapChar = 'B';

            }

            if (y != height - 1) {
                wumpusMap[x][y + 1].hasBreeze = true;
                wumpusMap[x][y + 1].mapChar = 'B';

            }

            if (y != 0) {
                wumpusMap[x][y - 1].hasBreeze = true;
                wumpusMap[x][y - 1].mapChar = 'B';

            }

            //Assign pit
            wumpusMap[x][y].hasPit = true;
            wumpusMap[x][y].mapChar = 'P';

        }

        //Assign wumpus location
        x = (int) (Math.random() * (width - 1) + 1);
        y = (int) (Math.random() * (height - 1) + 1);
        wumpusMap[x][y].hasWumpus = true;
        wumpusMap[x][y].mapChar = 'W';

        //assign stench
        if (x != width - 1) {
            wumpusMap[x + 1][y].hasStench = true;
            wumpusMap[x + 1][y].mapChar = 'S';

        }

        if (x != 0) {
            wumpusMap[x - 1][y].hasStench = true;
            wumpusMap[x - 1][y].mapChar = 'S';

        }

        if (y != height - 1) {
            wumpusMap[x][y + 1].hasStench = true;
            wumpusMap[x][y + 1].mapChar = 'S';

        }

        if (y != 0) {
            wumpusMap[x][y - 1].hasStench = true;
            wumpusMap[x][y - 1].mapChar = 'S';

        }

        //assign gold
        x = (int) (Math.random() * (width - 1) + 1);
        y = (int) (Math.random() * (height - 1) + 1);
        wumpusMap[x][y].hasGold = true;
        wumpusMap[x][y].mapChar = 'G';

    }

    private void defaultMap() {

        //Fill the array with Square objects
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                wumpusMap[j][i] = new Square();
            }
        }

        //Set starting point (this is location 1, 1 on the map)
        wumpusMap[0][0].isStart = true;

        //Assign breeze first, to (1,0) (3,0) (2,1) (1,2) (3,2) (2,3)
        wumpusMap[1][0].hasBreeze = true;
        wumpusMap[1][0].mapChar = 'B';
        wumpusMap[3][0].hasBreeze = true;
        wumpusMap[3][0].mapChar = 'B';
        wumpusMap[2][1].hasBreeze = true;
        wumpusMap[2][1].mapChar = 'B';
        wumpusMap[1][2].hasBreeze = true;
        wumpusMap[1][2].mapChar = 'B';
        wumpusMap[3][2].hasBreeze = true;
        wumpusMap[3][2].mapChar = 'B';
        wumpusMap[2][3].hasBreeze = true;
        wumpusMap[2][3].mapChar = 'B';

        //Assign pits to (2,0) (2,2) (3,3)
        wumpusMap[2][0].hasPit = true;
        wumpusMap[2][0].mapChar = 'P';
        wumpusMap[2][2].hasPit = true;
        wumpusMap[2][2].mapChar = 'P';
        wumpusMap[3][3].hasPit = true;
        wumpusMap[3][3].mapChar = 'P';

        //Assign wumpus location to (0,2)
        wumpusMap[0][2].hasWumpus = true;
        wumpusMap[0][2].mapChar = 'W';

        //assign stench to (0,3) (0,1) (1,2)
        wumpusMap[0][3].hasStench = true;
        wumpusMap[0][3].mapChar = 'S';
        wumpusMap[0][1].hasStench = true;
        wumpusMap[0][1].mapChar = 'S';
        wumpusMap[1][2].hasStench = true;
        wumpusMap[1][2].mapChar = 'S';

        //assign gold to (1,2)
        wumpusMap[1][2].hasGold = true;
        wumpusMap[1][2].mapChar = 'G';
    }



    public void printMap() {

        for (int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {

                System.out.print(wumpusMap[j][i].mapChar + " ");

            }

            System.out.println();

        }

    }

    private class Square {

        boolean hasWumpus = false;
        boolean hasGold = false;
        boolean hasStench = false;
        boolean hasBreeze = false;
        boolean hasPit = false;
        boolean isStart = false;

        char mapChar = 'X';
    }


}}