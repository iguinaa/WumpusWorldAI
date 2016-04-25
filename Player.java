import java.util.ArrayList;
import java.util.Random;

/**
 * Created by awills on 4/10/16.
 */

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
    boolean isDead = false;
    boolean hasWon = false;
    boolean heardScream = false;
    ArrayList<Square> unvisited;
    ArrayList<Square> visited;
    ArrayList<Square> fringe;

    /*Scoring:
    * +1000 points for climbing out of the cave with the gold
    * -1000 for falling into a pit or being eaten by the wumpus
    * -1 for each action taken
    * -10 for using up the arrow
    */
    private int score = 0;


    public Player(boolean isHuman)
    {
        unvisited = new ArrayList<>();
        visited = new ArrayList<>();
        fringe = new ArrayList<>();
        querySquare = true;
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
        for(int i = 0; i < gameMap.wumpusMap.length; i++)
        {
            for(int j = 0; j < gameMap.wumpusMap[i].length; j++)
            {
                unvisited.add(gameMap.wumpusMap[i][j]);
            }
        }
    }

    public void initialUpdate()
    {
        querySquare = true;
        needsUpdate = true;
        Game.updatePropertiesString("Score: " + score + "\n");
    }

    @Override
    public void update()
    {

        if(isDead)
        {
            Game.addToLog("DEAD, Score = " + dead() + "\n");
            isDead = false;// TODO(Andrew) Remove, only for testing
        }
        if(isHuman) // TODO(Andrew): Should all of this happen regardless if human or not?
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
            if(currentSquare != null)
            {
                checkSquare(currentSquare);
            }
            gameMap.update(); //FIXME(Andrew): not sure if / Think this works...
//            Game.updateDebugString(Game.getDebugData().toString() + "player game map is player map? " + gameMap.isPlayerMap + "\n");

            needsUpdate = false;


        }
        else
        {

        }
//        performNextAction();  this would take multiple steps...
//        perceiveEnvironment();    Now: needsUpdate = true; querySquare = true;
//        updateKnowledge();    Now: checkSquare()
//        DetermineNextAction(); requires calculation of heuristic for each Square in fringe.
        Game.updatePropertiesString("Score: " + this.score + "\n");
    }

    /** These are the next possible squares to investigate*/
    private void updateFringe()
    {
        if(currentSquare != null)
        {
            if(fringe.contains(gameMap.wumpusMap[currentSquare.x][currentSquare.y]))
                fringe.remove(gameMap.wumpusMap[currentSquare.x][currentSquare.y]);

            if ( (currentX != gameMap.getNumCols() - 1) &&
                    unvisited.contains(gameMap.wumpusMap[currentX + 1][currentY]) &&
                    !(fringe.contains(gameMap.wumpusMap[currentX + 1][currentY])) )
            {
                fringe.add(gameMap.wumpusMap[currentX + 1][currentY]);
            }

            if ((currentX != 0) &&
                    (unvisited.contains(gameMap.wumpusMap[currentX - 1][currentY])) &&
                    !(fringe.contains(gameMap.wumpusMap[currentX - 1][currentY])))
            {
                fringe.add(gameMap.wumpusMap[currentX - 1][currentY]);
            }

            if ((currentY != gameMap.getNumRows() - 1) &&
                    ( unvisited.contains(gameMap.wumpusMap[currentX][currentY + 1])) &&
                    !(fringe.contains(gameMap.wumpusMap[currentX][currentY + 1])))
            {
                fringe.add(gameMap.wumpusMap[currentX][currentY + 1]);
            }

            if ((currentY != 0) &&
                    (unvisited.contains(gameMap.wumpusMap[currentX][currentY - 1])) &&
                    !(fringe.contains(gameMap.wumpusMap[currentX][currentY - 1])))
            {
                fringe.add(gameMap.wumpusMap[currentX][currentY - 1]);
            }
        }
        else
        {
            System.out.println("tried to update fringe with null current square");
        }
    }

    private void checkSquare(Square s) {
        ArrayList<Character> seen = currentSquare.getPerceptions();
        String sees = "Player sees: ";
        for(int i =0; i < seen.size(); i++)
        {
            sees = sees + seen.get(i).toString();
        }
        sees = sees + "\n";
        Game.addToLog(sees);
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

        //Add to visited squares, remove from unvisited
        if(unvisited.contains(gameMap.wumpusMap[currentSquare.x][currentSquare.y]))
            unvisited.remove(gameMap.wumpusMap[currentSquare.x][currentSquare.y]);

        for(Character c : seen)
        {
            gameMap.wumpusMap[currentSquare.x][currentSquare.y].setMapChar(c.charValue());
        }
        updateFringe();

        for (Square theSquare: visited)
        {
            theSquare.resetWumpusDangerScore();
            theSquare.resetPitDangerScore();
        }

        if(!(visited.contains(gameMap.wumpusMap[currentSquare.x][currentSquare.y]))) {
            if (seen.contains('S')) {

                boolean resetScore = false; //do we need to reset the wumpus score for other squares?

                //change score of suspect squares if the square hasn't already been visited and is therefore safe
                if ((currentX != gameMap.getNumCols() - 1) && unvisited.contains(gameMap.wumpusMap[currentX + 1][currentY])) {
                    gameMap.wumpusMap[currentX + 1][currentY].setWumpusDangerScore(-1);
                    if(gameMap.wumpusMap[currentX + 1][currentY].getWumpusDangerScore() < -2) {
                        gameMap.wumpusMap[currentX + 1][currentY].setWumpusDangerScore(-1000);
                    }
                }

                if ((currentX != 0) && (unvisited.contains(gameMap.wumpusMap[currentX - 1][currentY]))) {
                    gameMap.wumpusMap[currentX - 1][currentY].setWumpusDangerScore(-1);
                    if(gameMap.wumpusMap[currentX - 1][currentY].getWumpusDangerScore() < -2){
                        gameMap.wumpusMap[currentX - 1][currentY].setWumpusDangerScore(-1000);
                    }

                }

                if ((currentY != gameMap.getNumRows() - 1) && (unvisited.contains(gameMap.wumpusMap[currentX][currentY + 1]))) {
                    gameMap.wumpusMap[currentX][currentY + 1].setWumpusDangerScore(-1);
                    if(gameMap.wumpusMap[currentX][currentY + 1].getWumpusDangerScore() < -2){
                        gameMap.wumpusMap[currentX][currentY + 1].setWumpusDangerScore(-1000);
                    }

                }

                if ((currentY != 0) && (unvisited.contains(gameMap.wumpusMap[currentX][currentY - 1]))) {
                    gameMap.wumpusMap[currentX][currentY - 1].setWumpusDangerScore(-1);
                    if(gameMap.wumpusMap[currentX][currentY - 1].getWumpusDangerScore() < -2){
                        gameMap.wumpusMap[currentX][currentY - 1].setWumpusDangerScore(-1000);
                    }

                }

            }

            //track breeze to try to identify pits
            if (seen.contains('B')) {

                //change score of suspect squares if the square hasn't already been visited (and is therefore safe)
                if ((currentX != gameMap.getNumCols() - 1) && unvisited.contains(gameMap.wumpusMap[currentX + 1][currentY])) {
                    gameMap.wumpusMap[currentX + 1][currentY].setPitDangerScore(-1);

                }

                if ((currentX != 0) && (unvisited.contains(gameMap.wumpusMap[currentX - 1][currentY]))) {
                    gameMap.wumpusMap[currentX - 1][currentY].setPitDangerScore(-1);

                }

                if ((currentY != gameMap.getNumRows() - 1) && (unvisited.contains(gameMap.wumpusMap[currentX][currentY + 1]))) {
                    gameMap.wumpusMap[currentX][currentY + 1].setPitDangerScore(-1);

                }

                if ((currentY != 0) && (unvisited.contains(gameMap.wumpusMap[currentX][currentY - 1]))) {
                    gameMap.wumpusMap[currentX][currentY - 1].setPitDangerScore(-1);

                }

            }
            //TODO reset scores when pits are identified?

            visited.add(gameMap.wumpusMap[currentSquare.x][currentSquare.y]);
        }

        //grab gold
        if (seen.contains('G')) {

            grab();
            //TODO head to exit
        }
    }

    private void move(char direction)
    {
        prevX = currentX;
        prevY = currentY;

        switch (direction)  {

            case 'r':
                if (currentX < gameMap.getNumCols() - 1) {
                    gameMap.wumpusMap[currentX][currentY].removeMapChar('A');
                    currentX++;
                    gameMap.wumpusMap[currentX][currentY].setMapChar('V');
                    gameMap.wumpusMap[currentX][currentY].setMapChar('A');

                }
                        break;
            case 'l':
                if (currentX != 0) {
                    gameMap.wumpusMap[currentX][currentY].removeMapChar('A');
                    currentX--;
                    gameMap.wumpusMap[currentX][currentY].setMapChar('V');
                    gameMap.wumpusMap[currentX][currentY].setMapChar('A');
                }
                        break;
            case 'u':
                if (currentY < gameMap.getNumRows() - 1) {
                    gameMap.wumpusMap[currentX][currentY].removeMapChar('A');
                    currentY++;
                    gameMap.wumpusMap[currentX][currentY].setMapChar('V');
                    gameMap.wumpusMap[currentX][currentY].setMapChar('A');
                }
                        break;
            case 'd':
                if (currentY != 0) {
                    gameMap.wumpusMap[currentX][currentY].removeMapChar('A');
                    currentY--;
                    gameMap.wumpusMap[currentX][currentY].setMapChar('V');
                    gameMap.wumpusMap[currentX][currentY].setMapChar('A');
                }
                        break;

            default:
                System.out.println("unexpected input in Player.move()");
                break;
        }
        querySquare = true;

        System.out.println("Current player square: " + currentX + ", " + currentY);

        //Subtract from score for movement
        score--;

    }
    public void handleHumanCommand(char action) // u = up, d = down, l = left, r = right, f = fire, e = exit maze, g=grab gold
    {
        switch (action)
        {
            case 'u':
            case 'd':
            case 'l':
            case 'r':
            {
                if (facingDirection != action)
                {
                    facingDirection = action;
                    Game.addToLog("Now facing: '" + facingDirection + "'\n");
                } else
                {
                    move(action);
                }
                needsUpdate = true;
            }
            break;

            case 'f':
            {
            	if(!hasArrow){
            		Game.addToLog("Unable to shoot arrow. Already fired.\n");
            	}
            	else{
            		shoot();
                	needsUpdate = true;
                }
            }
            break;

            case 'e':
            {
                if (currentX == 0 && currentY == 0)
                {
                    // TODO(Andrew): Exit Maze
                    hasWon = true;
                    //Add 1000 points it player got the gold out
                    if(hasGold)
                        score += 1000;

                    Game.addToLog("SUCCESSFUL ESCAPE! score = " + this.score + "\n");
                    Game.updatePropertiesString(Game.getProperties() + "\nSUCCESSFUL ESCAPE! score = " + this.score + "\n");
                }
                needsUpdate = true;
            }
            break;

//            case 'g':
//            {
//                if (currentX == 0 && currentY == 0)
//                {
//                    // TODO(Andrew): Exit Maze
//                }
//                needsUpdate = true;
//            }
//            break;

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
        if(currentSquare != null)
        {
            if(currentSquare.getAttributes().contains('G'))
            {
                currentSquare.removeMapChar('G');
                Game.addToLog("Picked up gold!");
                hasGold = true;
            }
        }
        System.out.println("Attempted to grab gold\n");
    }
    public int shoot()
    {
        //TODO Stub
    	Square wumpusCheckSquare = currentSquare;
        hasArrow = false;
        score -= 10;
        Game.addToLog("shot arrow\nfacingdirection "+facingDirection+"\n");
        if(facingDirection == 'r'){
        	for(int wc = wumpusCheckSquare.x; wc < gameMap.getNumRows(); wc++){
        		wumpusCheckSquare.x = wc;
        		System.out.println(wumpusCheckSquare.x + " "+wumpusCheckSquare.y+"\n");
        		shotWumpus(wumpusCheckSquare);
        	}
        }
        else if(facingDirection =='u'){
        	for(int wc = wumpusCheckSquare.y; wc < gameMap.getNumCols(); wc++){
        		wumpusCheckSquare.y = wc;
        		shotWumpus(wumpusCheckSquare);
        	}
        }
        else if(facingDirection == 'd'){
        	for(int wc = wumpusCheckSquare.y; wc < gameMap.getNumCols(); wc--){
        		wumpusCheckSquare.y = wc;
        		shotWumpus(wumpusCheckSquare);
        	}
        }
        else{ //direction l
        	for(int wc = wumpusCheckSquare.x+1; wc < gameMap.getNumRows(); wc--){
        		wumpusCheckSquare.x = wc;
        		shotWumpus(wumpusCheckSquare);
        	}
        }
        //TODO check if wumpus is dead and update map

        return 0;
    }
   
    public void shotWumpus(Square s){
    	if(Game.wumpusDeadCheck(s)){
			Game.addToLog("Shot wumpus\n");
			heardScream = true;
		}
    }

    /******************************************************/


    /** returns Score */
    public int dead()
    {
        int returnScore = 0;
        if(isDead)
        {
            if (currentSquare.getAttributes().contains('W'))
            {
                System.out.println("Wumpus got you!");
                Game.addToLog("Wumpus got you!\n");
            }
            else if (currentSquare.getAttributes().contains('P'))
            {

                System.out.println("You fell in a pit!");
                Game.addToLog("You fell in a pit!\n");
            }
            score += -1000;
            returnScore = score;
        }
        else
        {
            System.out.println("Someone called dead while the player was still alive");
        }
        return  returnScore;
    }

    //TODO shoot wumpus

    //TODO process data

    //TODO leave dungeon
    private void escape() {

        System.out.println("Final Score: " + score);

    }


    public int getManhattanDistFromCurrent(Square to)
    {
        int dist = 0;
        dist = Math.abs(to.x - currentSquare.x) + Math.abs(to.y - currentSquare.y);
        return dist;
    }

}
