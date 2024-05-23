public class Main {
    static private int n = 4;
    static int[][] d = {{0,10,15,20},
                 {5,0,9,10},
                 {6,13,0,12},
                 {8,8,9,0}};


    // if mask turns to 1111 it means all cities were checked
    static int ALL_VISITED = (1<<n) -1;


    public static int TSP(int mask, int pos){
        // base case if every cities were visited
        if(mask == ALL_VISITED){
            return d[pos][0];
        }

        int ans = Integer.MAX_VALUE;
        for (int city = 0 ; city< n ;city++){
            //check to see if the city is visited or not
            if (((mask) & (1<<city)) == 0){
                //if going from a to b the new mask will be (0001 | 0010)=(0011)
                int newResult = TSP(mask|(1<<city),city) + d[pos][city];

                ans = Math.min(ans,newResult);
            }
        }
        return ans;
    }


    public static void main(String[] args) {


        System.out.println(TSP(1,0));
    }
}