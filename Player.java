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
            score -= 1000;
            exit();

        }

        if (gameMap.wumpusMap[currentX][currentY].hasPit) {

            //TODO print to GUI
            System.out.println("You fell in a pit!");
            score -= 1000;
            exit();

        }

        //TODO track stench to try to identify where wumpus is
        if (gameMap.wumpusMap[currentX][currentY].hasStench) {

        }

        //TODO track breeze to try to identify pits
        if (gameMap.wumpusMap[currentX][currentY].hasBreeze) {

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
    private void shootWumpus(char direction) {

        hasArrow = false;
        score -= 10;

    }

    //TODO map dungeon

    //TODO process data

    //TODO leave dungeon
    private void escape() {

        if (hasGold)
            score += 1000;

        exit();

    }

    private void exit() {

        System.out.println("Final Score: " + score);
        System.exit(0);

    }


}
