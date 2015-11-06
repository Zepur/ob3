package DFS;

public class RunTime {

    public static void main(String[] args){
        int n = 10;
        for(int i = 1; i <= n; i++ )
            for( int j = i; j <= i; j++ )
                System.out.println(i + " and " + j);
    }
}
