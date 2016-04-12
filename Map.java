

import javafx.scene.layout.GridPane;

/**
 * Created by awills on 4/2/16.
 */

//Generate map to play wumpusworld game on
public class Map extends GridPane
{

    private int width;
    private int height;

    Square[][] wumpusMap;


    //Constructor
    Map(){

        wumpusMap = new Square[4][4];
        width = 4;
        height = 4;

        seedMap();

    }

    //Construct map with specific dimensions
    Map(int width, int height){

        wumpusMap = new Square[width][height];
        this.width = width;
        this.height = height;

        seedMap();
    }

    private void seedMap(){

        int pitCount = (width * height)/5; //Determine a number of pits to put into the game, trying /5 based on book map
        int x, y;

        for(int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {

                wumpusMap[i][j] = new Square();
                this.add(wumpusMap[i][j], i, j);

            }
        }

        //Assign pits
        for(int i = 0; i < pitCount; i++)   {

            // TODO(Andrew): exclude protagonist starting point
            x = (int)(Math.random() * width);
            y = (int)(Math.random() * height);

            //Assign breeze first so it doesn't write over pit
            //TODO still writing over it, need to handle this in a separate loop, maybe in a method in the Square class to set its char appropriately once all stats are assigned
            if(x != width - 1) {
                wumpusMap[x + 1][y].hasBreeze = true;
                wumpusMap[x + 1][y].setMapChar('B');

            }

            if(x != 0) {
                wumpusMap[x - 1][y].hasBreeze = true;
                wumpusMap[x - 1][y].setMapChar('B');

            }

            if(y != height - 1) {
                wumpusMap[x][y + 1].hasBreeze = true;
                wumpusMap[x][y + 1].setMapChar('B');

            }

            if(y != 0) {
                wumpusMap[x][y - 1].hasBreeze = true;
                wumpusMap[x][y - 1].setMapChar('B');

            }

            //Assign pit
            wumpusMap[x][y].hasPit = true;
            wumpusMap[x][y].setMapChar('P');

        }

    }

    public void printMap(){

        for(int i = 0; i < height; i++){

            for(int j = 0; j < width; j++){

                System.out.print(wumpusMap[j][i].mapChar + " ");

            }

            System.out.println();

        }

    }




}
