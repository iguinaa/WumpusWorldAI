import java.util.ArrayList;
import java.util.Random;

/**
 * Created by awills on 4/10/16.
 */
enum Action
{
    move,
    turnL,
    turnR,
    grab,
    shoot,
}

public class Player implements Updateable
{

    char facingDirection; // r, l, u, d
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


    public Player(/*Map currentMap*/)
    {

        gameMap = new Map(false);

        System.out.println("Current player square: " + currentX + ", " + currentY);

        facingDirection = 'r';

        //Move right since that is the default direction for player to move
        int hitBump = moveForward(); //TODO(Andrew): should probably move this out of constructor

        //TODO hand off to analyze
    }

    @Override
    public void update()
    {
//        performNextAction();  //NOTE: Maybe move somewhere else in the order?
//        perceiveEnvironment();
//        updateKnowledge();
//        DetermineNextAction();
    }

    private void checkSquare(Square s) {
        ArrayList<Character> seen = s.getPerceptions();
//        if (seen.contains()) {
//
//            //TODO print to GUI
//            //TODO(Andrew) Update Map only to update GUI.
//            System.out.println("Wumpus got you!");
//            System.exit(0);
//
//        }

//        if (gameMap.wumpusMap[currentX][currentY].hasPit) {
//
//            //TODO print to GUI
//            System.out.println("You fell in a pit!");
//            System.exit(0);
//
//        }

        //TODO track if wumpus or pit so you lose 1000 points and game over

        //TODO track stench to try to identify where wumpus is
        if (seen.contains('S')) {

            //TODO print to GUI
            System.out.println("You fell in a pit!");
            score -= 1000;
            //exit();

        }

        //track stench to try to identify where wumpus is
        if (seen.contains('S')) {

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

        //TODO track breeze to try to identify pits
        if (seen.contains('B')) {

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
        if (seen.contains('G')) {

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

    /** Agent Actions */
    public void grab()
    {
        //TODO stub
    }
    public int shoot()
    {
        //TODO Stub
        hasArrow = false;
        score -= 10;

        //TODO check if wumpus is dead and update map

        return 0;
    }
    public char turn90L()
    {
        //TODO stub
        char newDirection = 'r';
        return newDirection;
    }
    public char turn90R()
    {
        //TODO stub
        char newDirection = 'r';
        return newDirection;
    }
    public int moveForward()
    {
        //TODO stub
        move(facingDirection);
        return 0;
    }

    /** Perceive environment */



    //TODO shoot wumpus

    //TODO map dungeon

    //TODO process data

    //TODO leave dungeon
    private void escape() {

        System.out.println("Final Score: " + score);

    }


}
