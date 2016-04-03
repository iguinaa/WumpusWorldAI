/**
 * Created by awills on 4/2/16.
 */

//Generate map to play wumpusworld game on
public class Map {

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

                wumpusMap[j][i] = new Square();

            }
        }

        //Assign pits
        for(int i = 0; i < pitCount; i++)   {

            x = (int)(Math.random() * width);
            y = (int)(Math.random() * height);

            wumpusMap[x][y].hasPit = true;
            wumpusMap[x][y].mapChar = 'P';  //TODO remove after testing
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

    private class Square {

        boolean hasWumpus = false;
        boolean hasGold = false;
        boolean hasStench = false;
        boolean hasBreeze = false;
        boolean hasPit = false;

        char mapChar = 'X';
    }


}
