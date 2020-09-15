import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {

        int N=4;
        //return hit and sunk ships

        //System.out.println( battleShip(4,"1B 2C,2D 4D","2B 2D 3D 4D 4A")[0] + " & " + battleShip(4,"1B 2C,2D 4D","2B 2D 3D 4D 4A")[1] ); //1,1
        System.out.println( battleShip(12,"1A 2A,12A 12A", "12A")[0] + " & " +  battleShip(12,"1A 2A,12A 12A", "12A")[1]); //0,1

    }

    // return hit and sunk ships
    public static int[] battleShip(int n, String s, String t){

        int[] res = new int[2];

        String[][] board = new String[n][n];

        for(String[] sArr : board)
            Arrays.fill(sArr,"**");

        //mark and count ships
        //ship number -> count
        Map<Integer,Integer> map = new HashMap<>();
        Set<Integer> shipHit = new HashSet<>();

        //mark ships
        int shipNum=1;
        String[] ships = s.split(",");

        for(String str : ships) {

            //these don't cover 2 digit positions
            //int startS = start.charAt(0)-'1';

            //break by ','
            String start =  str.split(" ")[0];
            String end   =  str.split(" ")[1];

            int rowS = getRow(start);

            int rowE = getRow(end);

            int colS = getCol(start);

            int colE = getCol(end);

            for(int i = rowS ; i <= rowE ; i++) {
                for(int j = colS ; j <= colE ; j++ ) {

                    board[i][j] = shipNum+"#";
                    map.put(shipNum,map.getOrDefault(shipNum,0)+1);
                }
            }

            shipNum++;
        }

//        count into map -> merged with loop above, line 57
//        for(int i = 0 ; i < board.length ; i++) {
//            for (int j = 0; j < board[0].length ; j++) {
//
//                String str = board[i][j];
//
//                //ship number start from 1, so -'0'
//                if(Character.isDigit(str.charAt(0))) {
//                    map.put(str.charAt(0)-'0',map.getOrDefault(str.charAt(0)-'0',0)+1);
//                }
//            }
//        }

        //mark attacks and count hit and sunk
        String[] attacks = t.split(" ");

        for(String str : attacks) {

            //do not work for double digit
            //int start = str.charAt(0)-'1';

            int start = getRow(str);

            int end = getCol(str);
            //we got coordinates of attack

            String pos = board[start][end];

            //if there is a ship
            if(Character.isDigit(pos.charAt(0))) {

                //ship number start from 1 so -'0'
                int shipNumber = pos.charAt(0)-'0';

                //reduce count of ship in map since it is hit
                //later we detect oif count is zero, means it is sunk
                int freq = map.get(shipNumber);
                freq--;
                map.put(shipNumber,freq);

                //used to detect number hit but not sunk
                shipHit.add(shipNumber);

            }
        }

        int sunk = 0;

        for(Map.Entry<Integer,Integer> e : map.entrySet()){

            int shipNumber = e.getKey();

            if(map.get(shipNumber) <= 0) {
                sunk++;
                shipHit.remove(shipNumber);
            }

        }

        int hit = shipHit.size();

        res[0]=hit;
        res[1]=sunk;

        return res;

    }

    public static int getRow(String s){

        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(s);

        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            sb.append(m.group());
        }

        return Integer.parseInt(sb.toString())-1;

    }

    public static int getCol(String s) {

        Pattern p = Pattern.compile("[A-Z]+");
        Matcher m = p.matcher(s);

        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            sb.append(m.group());
        }

        return sb.toString().charAt(0)-'A';
    }

}
