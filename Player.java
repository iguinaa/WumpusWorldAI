import java.util.*;
import java.util.function.Consumer;

/**
 * Created by awills on 4/10/16.
 */

public class Player implements Updateable {

    public boolean SAFETY_ON = true;
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
    ArrayList<Square> safe;
    Stack<Square> pathToGoal;


    Square currentGoal;
    Square start;
    ArrayList<Square> resultActions;
    public boolean lookingForGold;

    /*Scoring:
    * +1000 points for climbing out of the cave with the gold
    * -1000 for falling into a pit or being eaten by the wumpus
    * -1 for each action taken
    * -10 for using up the arrow
    */
    public int score = 0;


    public Player(boolean isHuman) {
        lookingForGold = true;
        pathToGoal = new Stack<>();
        unvisited = new ArrayList<>();
        visited = new ArrayList<>();
        fringe = new ArrayList<>();
        safe = new ArrayList<>();
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

    public void setMap(Map m) {
        gameMap = m;
        for (int i = 0; i < gameMap.wumpusMap.length; i++) {
            for (int j = 0; j < gameMap.wumpusMap[i].length; j++) {
                unvisited.add(gameMap.wumpusMap[i][j]);
            }
        }
    }

    public void initialUpdate() {
        querySquare = true;
        needsUpdate = true;
        Game.updatePropertiesString("Score: " + score + "\n");
    }

    @Override
    public void update() {
        if (isDead) {

            Game.addToLog("DEAD, Score = " + dead() + "\n");
//            isDead = false;// TODO(Andrew) Remove, only for testing
        }
        if (isHuman) // TODO(Andrew): Should all of this happen regardless if human or not?
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
            if (currentSquare != null) {
                checkSquare(currentSquare);
            }
            gameMap.update(); //FIXME(Andrew): not sure if / Think this works...
//            Game.updateDebugString(Game.getDebugData().toString() + "player game map is player map? " + gameMap.isPlayerMap + "\n");

            needsUpdate = false;


        } else {
            if (currentSquare != null) {
                checkSquare(currentSquare);
            }
            gameMap.update(); //FIXME(Andrew): not sure if / Think this works...
//            Game.updateDebugString(Game.getDebugData().toString() + "player game map is player map? " + gameMap.isPlayerMap + "\n");

            needsUpdate = false;
        }
//        performNextAction();  this would take multiple steps...
//        perceiveEnvironment();    Now: needsUpdate = true; querySquare = true;
//        updateKnowledge();    Now: checkSquare()
//        DetermineNextAction(); requires calculation of heuristic for each Square in fringe.
        Game.updatePropertiesString("Score: " + this.score + "\n");
    }

    /**
     * These are the next possible squares to investigate
     */
    private void updateFringe() {
        if (currentSquare != null) {
            if (fringe.contains(gameMap.wumpusMap[currentSquare.x][currentSquare.y]))
                fringe.remove(gameMap.wumpusMap[currentSquare.x][currentSquare.y]);

            if ((currentX != gameMap.getNumCols() - 1) &&
                    unvisited.contains(gameMap.wumpusMap[currentX + 1][currentY]) &&
                    !(fringe.contains(gameMap.wumpusMap[currentX + 1][currentY]))) {
                fringe.add(gameMap.wumpusMap[currentX + 1][currentY]);
            }

            if ((currentX != 0) &&
                    (unvisited.contains(gameMap.wumpusMap[currentX - 1][currentY])) &&
                    !(fringe.contains(gameMap.wumpusMap[currentX - 1][currentY]))) {
                fringe.add(gameMap.wumpusMap[currentX - 1][currentY]);
            }

            if ((currentY != gameMap.getNumRows() - 1) &&
                    (unvisited.contains(gameMap.wumpusMap[currentX][currentY + 1])) &&
                    !(fringe.contains(gameMap.wumpusMap[currentX][currentY + 1]))) {
                fringe.add(gameMap.wumpusMap[currentX][currentY + 1]);
            }

            if ((currentY != 0) &&
                    (unvisited.contains(gameMap.wumpusMap[currentX][currentY - 1])) &&
                    !(fringe.contains(gameMap.wumpusMap[currentX][currentY - 1]))) {
                fringe.add(gameMap.wumpusMap[currentX][currentY - 1]);
            }
        } else {
            System.out.println("tried to update fringe with null current square");
        }
    }

    private void checkSquare(Square s) {
        ArrayList<Character> seen = currentSquare.getPerceptions();
        String sees = "Player sees: ";
        for (int i = 0; i < seen.size(); i++) {
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
        if (unvisited.contains(gameMap.wumpusMap[currentSquare.x][currentSquare.y]))
            unvisited.remove(gameMap.wumpusMap[currentSquare.x][currentSquare.y]);

        for (Character c : seen) {
            gameMap.wumpusMap[currentSquare.x][currentSquare.y].setMapChar(c.charValue());
        }
        updateFringe();

        for (Square theSquare : visited) {
            theSquare.resetWumpusDangerScore();
            theSquare.resetPitDangerScore();
        }

        if (!(visited.contains(gameMap.wumpusMap[currentSquare.x][currentSquare.y]))) {
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

        if(!(seen.contains('B') || seen.contains('S')))
        {
            if ((currentX != gameMap.getNumCols() - 1)) {
                safe.add(gameMap.wumpusMap[currentX + 1][currentY]);
            }
            if ((currentX != 0) && (unvisited.contains(gameMap.wumpusMap[currentX - 1][currentY]))) {
                safe.add(gameMap.wumpusMap[currentX - 1][currentY]);
            }
            if ((currentY != gameMap.getNumRows() - 1) && (unvisited.contains(gameMap.wumpusMap[currentX][currentY + 1]))) {
                safe.add(gameMap.wumpusMap[currentX][currentY + 1]);
            }
            if ((currentY != 0) && (unvisited.contains(gameMap.wumpusMap[currentX][currentY - 1]))) {
                safe.add(gameMap.wumpusMap[currentX][currentY - 1]);
            }
        }

//        for(Square n: safe)
//        {
//            if ((currentX != gameMap.getNumCols() - 1) && unvisited.contains(gameMap.wumpusMap[currentX + 1][currentY])) {
//                gameMap.wumpusMap[currentX + 1][currentY].setPitDangerScore(0);
//                gameMap.wumpusMap[currentX + 1][currentY].setWumpusDangerScore(0);
//
//
//            }
//
//            if ((currentX != 0) && (unvisited.contains(gameMap.wumpusMap[currentX - 1][currentY]))) {
//                gameMap.wumpusMap[currentX - 1][currentY].setPitDangerScore(0);
//                gameMap.wumpusMap[currentX - 1][currentY].setWumpusDangerScore(0);
//
//            }
//
//            if ((currentY != gameMap.getNumRows() - 1) && (unvisited.contains(gameMap.wumpusMap[currentX][currentY + 1]))) {
//                gameMap.wumpusMap[currentX][currentY + 1].setPitDangerScore(0);
//                gameMap.wumpusMap[currentX][currentY + 1].setWumpusDangerScore(0);
//
//            }
//
//            if ((currentY != 0) && (unvisited.contains(gameMap.wumpusMap[currentX][currentY - 1]))) {
//                gameMap.wumpusMap[currentX][currentY - 1].setPitDangerScore(0);
//                gameMap.wumpusMap[currentX][currentY - 1].setWumpusDangerScore(0);
//
//            }
//        }

        //grab gold
        if (seen.contains('G')) {

            grab();
            //TODO head to exit
        }
    }

    private void move(char direction) {
        prevX = currentX;
        prevY = currentY;

        switch (direction) {

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

    public int isWorthIt()
    {
        int bestDScore = -100000;
        for(Square s : fringe)
        {
            if(s.getTotalDangerScore() > bestDScore );
            {
                bestDScore = s.getTotalDangerScore();
            }
        }
        return bestDScore;
    }
    public Square getCurrentGoal() {
        // TODO(Andrew): Need overall goal for longer actions
        Square goal = null;
        boolean firstRun = true;
        if(lookingForGold && !fringe.isEmpty() && isWorthIt() > -3)
        {

            if(SAFETY_ON)
            {
                for (Square s : fringe)
                {
                    if (safe.contains(s) && unvisited.contains(s))
                    {
                        goal = s;
                        break;
                    } else if (firstRun)
                    {
                        Game.addToLog("initial: " + s.x + ", " + s.y + "\n");
                        goal = s;
                        firstRun = false;
                    } else if (s.getTotalDangerScore() * 30 - getManhattanDistFromCurrent(s) > goal.getTotalDangerScore() * 30 - getManhattanDistFromCurrent(goal))
                    {
                        goal = s;
                    } else
                    {
                        Game.appendDebugString("not good enough goal");
                        Game.addToLog("square: " + s.x + ", " + s.y + " not good enough goal\n");
                        //                int f = random.nextInt(4);
                        //                goal = s;
                    }
                }
            }
            else
                {
                    /** Backup of old version    ************ */
                    for (Square s : fringe)
                    {
                        if (firstRun)
                        {
                            Game.addToLog("initial: " + s.x + ", " + s.y + " not good enough goal\n");
                            goal = s;
                            firstRun = false;
                        } else if (safe.contains(s))
                        {
                            goal = s;
                        } else if (s.getTotalDangerScore() * 30 - getManhattanDistFromCurrent(s) > goal.getTotalDangerScore() * 30 - getManhattanDistFromCurrent(goal))
                        {
                            goal = s;
                        } else
                        {
                            Game.appendDebugString("not good enough goal");
                            Game.addToLog("square: " + s.x + ", " + s.y + " not good enough goal\n");
    //                int f = random.nextInt(4);
    //                goal = s;
                        }
                }
            }
        }
        else
        {
            for (Square s : visited)
            {
                if(s.x == 0 && s.y == 0)
                {
                    goal = s;
                    break;
                }
            }
        }
        currentGoal = goal;
        return goal;
    }

    public void generateActionsToGoal()
    {
         for(Square s : visited)
        {
            s.isVisited = false;
        }
        for(Square s: unvisited)
        {
            s.isVisited = false;
        }
        currentSquare.isVisited = false;
        gameMap.wumpusMap[currentSquare.x][currentSquare.y].isVisited = false;

        ArrayList<Character> charsToGoal = new ArrayList<>();
        Stack<Square> s = new Stack<>();
        Square start = currentSquare;
        Game.addToLog("start = " + start.x + ", " + start.y + "\n" );
        DFS(start, s);
        if(s.peek().x == currentGoal.x  && s.peek().y == currentGoal.y)
        {
            System.out.println("Found Solution");
        }
        else
        {
            System.out.println("Failed To Find Solution");
        }
        while(!s.isEmpty())
        {
            pathToGoal.push(s.pop());
        }

        /*
        Square v;
        s.push(start);
        pathToGoal.push(start);
        while(!s.isEmpty())
        {
            v = s.pop();
            Game.addToLog("v = " + v.x + ", " + v.y + "\n" );
            Game.addToLog("v = " + currentGoal.x + ", " + currentGoal.y + "\n" );

            if(v.x == currentGoal.x && v.y == currentGoal.y)
            {
                pathToGoal.push(v);
                while(!s.isEmpty()) {
                    pathToGoal.push(s.pop());
                }

                Game.addToLog("Found Successful Path!\n");
                return;
            }
            if(!v.isVisited)
            {
                v.isVisited = true;
                ArrayList<Square> neigh = getNeighbors(v);
                int i = 0;
                for(Square n : neigh)
                {
                    Game.addToLog("neighbor[" + i + "] = " + n.x + ", " + n.y + "\n" );
                    s.push(n);
                    i++;
                }
            }

        }
        Game.addToLog("PATHFIND FAIL!\n");
        */

    }
    public Stack<Square> DFS(Square v, Stack<Square> pathSoFar)
    {
        v.isVisited = true;
        if(v.x == currentGoal.x && v.y == currentGoal.y)
        {
            pathSoFar.push(v);
            return pathSoFar;
        }
        ArrayList<Square> neigh = getNeighbors(v);
        int i = 0;
        for(Square n : neigh)
        {
            Game.addToLog("neighbor[" + i + "] = " + n.x + ", " + n.y + "\n" );
            if(!n.isVisited)
            {
                pathSoFar.push(n);
                pathSoFar = DFS(n, pathSoFar);
                if(pathSoFar.peek().x == currentGoal.x  && pathSoFar.peek().y == currentGoal.y)
                {
                    return pathSoFar;
                }
            }
            i++;
        }
        return pathSoFar;
    }

    public ArrayList<Square> getNeighbors(Square from)
    {
        ArrayList<Square> neigh = new ArrayList<>();
        boolean foundLast = false;
        for(Square s : fringe)
        {

            System.out.println("getManhattanDistance from current " + currentSquare.x + ", " + currentSquare.y + "==" + getManhattanDistance(from, s));
            System.out.println("getManhattanDistance between " + from.x + ", " + from.y + " :: " + s.x + ", " + s.y + " == " + getManhattanDistance(from, s));
            // TODO GO HERE
            if(getManhattanDistance(from, s) == 1 && (s.x == currentGoal.x && s.y == currentGoal.y))
            {

//                System.out.println("getManhattanDistance between " + from.x + ", " + from.y + " :: " + s.x + ", " + s.y + " == " + getManhattanDistance(from, s));
                neigh.add(s);
                foundLast = true;
            }
        }
        if(!foundLast)
        {
            for (Square s : visited)
            {
                if (getManhattanDistance(from, s) == 1)
                {
                    neigh.add(s);
                }
            }
        }
//        for(Square s : unvisited)
//        {
//            if(getManhattanDistFromCurrent(s) == 1) {
//                neigh.add(s);
//            }
//        }
        return neigh;
    }

    public void generateEvent() {

//        getCurrentGoal();
//        generateActionsToGoal();
//        Game.addToLog("currentGoal = " + currentGoal.x + ", " + currentGoal.y  + "\n");

        if(pathToGoal.isEmpty())
        {
            getCurrentGoal();
            generateActionsToGoal();
            Game.addToLog("currentGoal = " + currentGoal.x + ", " + currentGoal.y  + "\n");
        }
        int i = 0;
        if (!pathToGoal.isEmpty()) {
            Square s = pathToGoal.pop();
            Game.addToLog("path[" + i + "]: " + s.x + "," +s.y + "\n");
            if (s.x > currentSquare.x && s.y == currentSquare.y) {
//                if(! (facingDirection == 'r'))
//                    handleHumanCommand('r');
                Game.addToLog("attempting move: r\n");
                handleAgentCommand('r');
            } else if (s.x == currentSquare.x && s.y > currentSquare.y) {
//                if(! (facingDirection == 'u'))
//                    handleHumanCommand('u');
                handleAgentCommand('u');
                Game.addToLog("attempting move: u\n");
            } else if (s.x < currentSquare.x && s.y == currentSquare.y) {
//                if(! (facingDirection == 'l'))
//                    handleHumanCommand('l');
                handleAgentCommand('l');
                Game.addToLog("attempting move: l\n");
            } else if (s.x == currentSquare.x && s.y < currentSquare.y) {
//                if(! (facingDirection == 'd'))
//                    handleHumanCommand('d');
                handleAgentCommand('d');
                Game.addToLog("attempting move: d\n");
            }
            else if( (hasGold || !(isWorthIt() > -3)) && currentSquare.x == 0 && currentSquare.y == 0)
            {
                if(!(isWorthIt() > -3 ))
                {
                    Game.addToLog("agent thinks its not worth it\n");
                }
                handleAgentCommand('e');
            }
            else if ( s.x == currentSquare.x && s.y == currentSquare.y )
            {
                Game.addToLog("Chose not to move...");
            }
            else
            {
                Game.addToLog("TRIED DIAGONAL! BADDDD\n" +
                        "DUMPING PATH TO GOAL\n");
                pathToGoal.push(s);
                dumpPathToGoal();
                getCurrentGoal(); // Attempt to recover
            }
        }
    }
    public void dumpPathToGoal()
    {
        int i = 0;
        Square from = currentSquare;
        while(!pathToGoal.isEmpty())
        {
            Square to = pathToGoal.pop();
            Game.addToLog("step "+ i + ": from, to == " + from.x + ", " + from.y + " :: " + to.x + ", " + to.y + "\n");
            i++;
        }
    }


    public void handleAgentCommand(char action) // u = up, d = down, l = left, r = right, f = fire, e = exit maze, g=grab gold
    {
        switch (action) {
            case 'u':
            case 'd':
            case 'l':
            case 'r':
            {
                    move(action);

                needsUpdate = true;
            }
            break;

            case 'f': {
                shoot();
                needsUpdate = true;
            }
            break;

            case 'e': {
                if (currentSquare.x == 0 && currentSquare.y == 0) {
                    // TODO(Andrew): Exit Maze
                    handleHumanCommand('e');
                    hasWon = true;
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

            default: {
                Game.addToLog("Bad input, which should never happen\n");
            }
            break;
        }
    }
    public void handleHumanCommand(char action) // u = up, d = down, l = left, r = right, f = fire, e = exit maze, g=grab gold
    {
        switch (action) {
            case 'u':
            case 'd':
            case 'l':
            case 'r': {
                if (facingDirection != action) {
                    facingDirection = action;
                    Game.addToLog("Now facing: '" + facingDirection + "'\n");
                } else {
                    move(action);
                }
                needsUpdate = true;
            }
            break;

            case 'f': {
            	if(!hasArrow){
            		Game.addToLog("Unable to shoot arrow. Already fired.\n");
            	}
            	else{
            		shoot();
                	needsUpdate = true;
                }
            }
            break;

            case 'e': {
                if (currentX == 0 && currentY == 0) {

                    // TODO(Andrew): Exit Maze
                    hasWon = true;
                    //Add 1000 points it player got the gold out
                    if(hasGold)
                        score += 1000;

                    Game.addToLog("SUCCESSFUL ESCAPE! score = " + this.score + "\n");
                    Game.updatePropertiesString(Game.getProperties() + "\nSUCCESSFUL ESCAPE! score = " + this.score + "\n");
                    escape();
                }
                else{
                	Game.addToLog("Unable to escape at this time. Must be in start location [0,0]\n");
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

            default: {
                Game.addToLog("Bad input, which should never happen\n");
            }
            break;
        }
    }

    /**
     * Agent Actions
     */
    public void grab() {
        if (currentSquare != null) {
            if (currentSquare.getAttributes().contains('G')) {
                currentSquare.removeMapChar('G');
                Game.addToLog("Picked up gold!");
                hasGold = true;
                lookingForGold = false;
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
        Game.addToLog("shot arrow\nfacingdirection " + facingDirection + "\n");
        if (facingDirection == 'r')
        {
            for (int wc = wumpusCheckSquare.x; wc < gameMap.getNumRows(); wc++)
            {
                wumpusCheckSquare.x = wc;
                System.out.println(wumpusCheckSquare.x + " " + wumpusCheckSquare.y + "\n");
                shotWumpus(wumpusCheckSquare);
            }
        }
        else if (facingDirection == 'u')
        {
            for (int wc = wumpusCheckSquare.y; wc < gameMap.getNumCols(); wc++)
            {
                wumpusCheckSquare.y = wc;
                shotWumpus(wumpusCheckSquare);
            }
        } else if (facingDirection == 'd')
        {
            for (int wc = wumpusCheckSquare.y; wc < gameMap.getNumCols(); wc--)
            {
                wumpusCheckSquare.y = wc;
                shotWumpus(wumpusCheckSquare);

            }
        }
        else{ //direction l
            for (int wc = wumpusCheckSquare.x + 1; wc < gameMap.getNumRows(); wc--)
            {
                wumpusCheckSquare.x = wc;
                shotWumpus(wumpusCheckSquare);
            }
        }

        //TODO check if wumpus is dead and update map
        return 0;
    }

        public void shotWumpus (Square s)
        {
            if (Game.wumpusDeadCheck(s))
            {
                Game.addToLog("Shot wumpus\n");
                heardScream = true;
            }
        }

        /******************************************************/


        /**
         * returns Score
         */
        public int dead () {
        int returnScore = 0;
        if (isDead)
        {

            if (currentSquare.getAttributes().contains('P'))
            {
                if (currentSquare.getAttributes().contains('W'))
                {
                    System.out.println("Wumpus got you!");
                    Game.addToLog("Wumpus got you!\n");
                    Game.nowDead(1);
                }
                System.out.println("You fell in a pit!");
                Game.addToLog("You fell in a pit!\n");
                Game.nowDead(2);
            }

            score += -1000;
            returnScore = score;
        } else
        {
            System.out.println("Someone called dead while the player was still alive");
        }
        return returnScore;
    }

        //TODO shoot wumpus

        //TODO process data

        //TODO leave dungeon
        private void escape () {

        System.out.println("Final Score: " + score);
        Game.addToLog("Final Score: " + score + "\n");
        Game.escapeStage(score);
    }


        public int getManhattanDistFromCurrent (Square to){
        int dist = 0;
        dist = Math.abs(to.x - currentSquare.x) + Math.abs(to.y - currentSquare.y);
        return dist;
    }

        public int getManhattanDistance (Square from, Square to){
        int dist = 0;
        dist = Math.abs(to.x - from.x) + Math.abs(to.y - from.y);
        return dist;
    }


}
