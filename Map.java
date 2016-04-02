/**
 * Created by awills on 4/2/16.
 */

//Generate map to play wumpusworld game on
public class Map {

    private int width;
    private int height;

    //Constructor
    Map(){

        Square[][] wumpusMap = new Square[4][4];
        width = 4;
        height = 4;

    }

    //Construct map with specific dimensions
    Map(int width, int height){

        Square[][] wumpusMap = new Square[width][height];
        this.width = width;
        this.height = height;

    }

    private void seedMap(){

    }

    public void printMap(){

        for(int i = 0; i < height; i++){

            for(int j = 0; j < width; j++){

                System.out.print("X ");

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

    }


}
