/**
 * Created by awills on 4/10/16.
 */
public class Player {

    boolean hasArrow = true;
    boolean hasGold = false;
    int currentX = 0;
    int currentY = 0;
    Map gameMap;

    public Player() {

    }

    private void checkSquare() {

        //TODO end game if player landed on wumpus
        if (gameMap.wumpusMap[currentX][currentY].hasWumpus) {


        }

        //TODO end game if player is in pit
        if (gameMap.wumpusMap[currentX][currentY].hasPit) {

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

    //TODO shoot wumpus

    //TODO map dungeon

    //TODO process data

    //TODO leave dungeon
    private void escape() {

    }

}
