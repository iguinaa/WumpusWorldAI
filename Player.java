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
    int prevX = -1;
    int prevY = -1;
    boolean isHuman;
    Map gameMap;
    Square currentSquare;
    Random random = new Random();
    boolean needsUpdate; // aka Has performed action
    boolean querySquare;

    /*Scoring:
    * +1000 points for climbing out of the cave with the gold
    * -1000 for falling into a pit or being eaten by the wumpus
    * -1 for each action taken
    * -10 for using up the arrow
    */
    int score = 0;


    public Player(boolean isHuman)
    {
        this.isHuman = isHuman;
        gameMap = new Map(false);
        currentSquare = null;
        System.out.println("Current player square: " + currentX + ", " + currentY);

        facingDirection = 'r';
        needsUpdate = false;
        //Move right since that is the default direction for player to move
        // NOTE(Andrew) discussed that this should be removed. Just assume perfect knowledge of map bounds
        //int hitBump = moveForward();

        //TODO hand off to analyze (this probably isn't necessary now but we'll see)
    }

    public void setMap(Map m)
    {
        gameMap = m;
    }


    @Override
    public void update()
    {
        if(isHuman)
        {
           // do stuff
//            for(int i = 0 ; i < getPlayerMap().wumpusMap.length; i++)
//        {
//            for(int j=0; j < getPlayerMap().wumpusMap[i].length; j++)
//            {
//                if(i == p.currentX && j == p.currentY)
//                {
//                    getPlayerMap().wumpusMap[i][j].setMapChar('A');
//                }
//                else
//                {
//                    getPlayerMap().wumpusMap[i][j].removeMapChar('A');
//                }
//            }
//
//        }
            gameMap.update(); //FIXME(Andrew): not sure if / how this is working but its definitely doing something
            Game.updateDebugString(Game.getDebugData().toString() + "player game map is player map? " + gameMap.isPlayerMap + "\n");
             needsUpdate = false;

            return;
        }
//        performNextAction();  //NOTE: Maybe move somewhere else in the order?
//        perceiveEnvironment();
//        updateKnowledge();
//        DetermineNextAction();
    }

    private void checkSquare(Square s) {
        ArrayList<Character> seen = currentSquare.getPerceptions();
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

        //track breeze to try to identify pits
        if (seen.contains('B')) {

            //change score of suspect squares if the square hasn't already been visited (and is therefore safe)
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

        //grab gold
        if (seen.contains('G')) {

            hasGold = true;

            //TODO head to exit
        }
    }

    private void move(char direction)
    {
        prevX = currentX;
        prevY = currentY;
        gameMap.wumpusMap[currentX][currentY].removeMapChar('A');
        switch (direction)  {

            case 'r':
                if (currentX < gameMap.getNumCols() - 1) {
                    currentX++;
                    gameMap.wumpusMap[currentX][currentY].setMapChar('V');
                    gameMap.wumpusMap[currentX][currentY].setMapChar('A');
                }
                        break;
            case 'l':
                if (currentX != 0) {
                    currentX--;
                    gameMap.wumpusMap[currentX][currentY].setMapChar('V');
                    gameMap.wumpusMap[currentX][currentY].setMapChar('A');
                }
                        break;
            case 'u':
                if (currentY < gameMap.getNumRows() - 1) {
                    currentY++;
                    gameMap.wumpusMap[currentX][currentY].setMapChar('V');
                    gameMap.wumpusMap[currentX][currentY].setMapChar('A');
                }
                        break;
            case 'd':
                if (currentY != 0) {
                    currentY--;
                    gameMap.wumpusMap[currentX][currentY].setMapChar('V');
                    gameMap.wumpusMap[currentX][currentY].setMapChar('A');
                }
                        break;

            default:
                System.out.println("unexpected input in Player.move()");
                break;
        }

        System.out.println("Current player square: " + currentX + ", " + currentY);

        //Subtract from score for movement
        score--;

    }
    public void handleHumanCommand(char action) // u = up, d = down, l = left, r = right, f = fire, e = exit maze
    {
        switch (action)
        {
            case 'u':
            case 'd':
            case 'l':
            case 'r':
            {
                Game.addToLog("shouldve moved a square");
                if (facingDirection != action)
                {
                    Game.addToLog("WASTN FACING THE RIGHT WAY!\n");
                    facingDirection = action;
                } else
                {
                    Game.addToLog("We're about to enter the moveeeee method\n");
                    move(action);
                }
                needsUpdate = true;
            }
            break;

            case 'f':
            {
                shoot();
                needsUpdate = true;
            }
            break;

            case 'e':
            {
                if (currentX == 0 && currentY == 0)
                {
                    // TODO(Andrew): Exit Maze
                }
                needsUpdate = true;
            }
            break;

            default:
            {
                Game.addToLog("Bad input, which should never happen\n");
            }
            break;
        }
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
