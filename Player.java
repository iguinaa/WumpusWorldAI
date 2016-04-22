import java.util.Random;

/**
 * Created by awills on 4/10/16.
 */
public class Player implements Updateable
{

    boolean hasArrow = true;
    boolean hasGold = false;
    int currentX = 0;
    int currentY = 0;
    Map gameMap;
    Random random = new Random();

    /*Scoring:
    * +1000 points for climbing out of the cave with the gold
    * -1000 for falling into a pit or being eaten by the wumpus
    * -1 for each action taken
    * -10 for using up the arrow
    */
    int score = 0;


    public Player(/*Map currentMap*/) {

        gameMap = new Map(false);

        System.out.println("Current player square: " + currentX + ", " + currentY);

        //Move right since that is the default direction for player to move
        move('r');

        //TODO hand off to analyze
    }

    @Override
    public void update()
    {
        //TODO(Andrew): Get current square
        //TODO(Andrew):
        //TODO(Andrew): Check Square
        //TODO(Andrew): Check Square
        //TODO(Andrew): Check Square
        //TODO(Andrew): Check Square
        //TODO(Andrew): Check Square
    }

    private void checkSquare() {

        if (gameMap.wumpusMap[currentX][currentY].hasWumpus) {

            //TODO print to GUI
            System.out.println("Wumpus got you!");
            System.exit(0);

        }

        if (gameMap.wumpusMap[currentX][currentY].hasPit) {

            //TODO print to GUI
            System.out.println("You fell in a pit!");
            System.exit(0);

        }

        //track stench to try to identify where wumpus is
        if (gameMap.wumpusMap[currentX][currentY].hasStench) {

            //change score of suspect squares if the square hasn't already been visited and is therefore safe
            if ((currentX != gameMap.getWidth() - 1) && (!gameMap.wumpusMap[currentX + 1][currentY].isWasVisited())) {
                gameMap.wumpusMap[currentX + 1][currentY].setWumpusDangerScore(-1);

            }

            if ((currentX != 0) && (!gameMap.wumpusMap[currentX - 1][currentY].isWasVisited())) {
                gameMap.wumpusMap[currentX - 1][currentY].setWumpusDangerScore(-1);

            }

            if ((currentY != gameMap.getHeight() - 1) && (!gameMap.wumpusMap[currentX][currentY + 1].isWasVisited())) {
                gameMap.wumpusMap[currentX][currentY + 1].setWumpusDangerScore(-1);

            }

            if ((currentY != 0) && (!gameMap.wumpusMap[currentX][currentY - 1].isWasVisited())) {
                gameMap.wumpusMap[currentX][currentY - 1].setWumpusDangerScore(-1);

            }

        }

        //TODO reset scores when wumpus is found?

        //track breeze to try to identify pits
        if (gameMap.wumpusMap[currentX][currentY].hasBreeze) {

            //change score of suspect squares
            //change score of suspect squares if the square hasn't already been visited and is therefore safe
            if ((currentX != gameMap.getWidth() - 1) && (!gameMap.wumpusMap[currentX + 1][currentY].isWasVisited())) {
                gameMap.wumpusMap[currentX + 1][currentY].setPitDangerScore(-1);

            }

            if ((currentX != 0) && (!gameMap.wumpusMap[currentX - 1][currentY].isWasVisited())) {
                gameMap.wumpusMap[currentX - 1][currentY].setPitDangerScore(-1);

            }

            if ((currentY != gameMap.getHeight() - 1) && (!gameMap.wumpusMap[currentX][currentY + 1].isWasVisited())) {
                gameMap.wumpusMap[currentX][currentY + 1].setPitDangerScore(-1);

            }

            if ((currentY != 0) && (!gameMap.wumpusMap[currentX][currentY - 1].isWasVisited())) {
                gameMap.wumpusMap[currentX][currentY - 1].setPitDangerScore(-1);

            }


        }

        //TODO grab gold
        if (gameMap.wumpusMap[currentX][currentY].hasGold) {

            hasGold = true;

        }
    }

    private void move(char direction)   {

        switch (direction)  {

            case 'r':   currentX++;
                        break;
            case 'l':   currentX--;
                        break;
            case 'u':   currentY++;
                        break;
            case 'd':   currentY--;
                        break;
            default:    break;
        }

        System.out.println("Current player square: " + currentX + ", " + currentY);

        //Subtract from score for movement
        score--;

    }

    //TODO shoot wumpus

    //TODO map dungeon

    //TODO process data

    //TODO leave dungeon
    private void escape() {

    }


}
