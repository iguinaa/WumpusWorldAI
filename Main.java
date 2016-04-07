public class Main {

    public static void main(String[] args) {

        Map wumpusMap;

        if (args.length == 2) {

            wumpusMap = new Map(Integer.valueOf(args[0]), Integer.valueOf(args[1]));

        } else {

            wumpusMap = new Map();
        }

        wumpusMap.printMap();

    }
}