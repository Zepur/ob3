package DFS;

import DFS.AbstractGraph;
import DFS.UnweightedGraph;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Zepur on 29.10.2015.
 */
public class AbstractGraph_TEST<V> {

    private String[] vertices = {"Saturn","Tellus","Jupiter","Venus","Neptun","Merkur"};
    private int[][] edges = {
            {0,1}, {0,3}, {0,5},
            {1,0}, {1,4}, {1,5},
            {2,3}, {2,4},
            {3,0}, {3,2},
            {4,1}, {4,2}, {4,5},
            {5,0}, {5,1}, {5,4}
    };
    private int[][] edgesNotAllConnected = {
            {0,1}, {0,3},
            {1,0}, {1,3}, {1,5},
            {3,0}, {3,1}, {3,4},
            {4,5}, {4,3},
            {5,1}, {5,4}
    };
    private int[][] edgesNonCyclic = {
            {0,1},
            {1,0}, {1,3},
            {3,1}, {3,2},
            {2,3}, {2, 4}, {2,5},
            {4,2},
            {5,2}
    };
    private int[][] edgesAlternativeConnections = {
            {0, 1}, {0, 2},
            {1, 0}, {1, 4},
            {2, 0}, {2, 3},
            {3, 2}, {3, 4}, {3, 5},
            {4, 1}, {4, 3},
            {5,3}
    };

    private UnweightedGraph connectedCyclicGraph, unConnectedGraph, connectedNonCyclicGraph, connectedAlternativeGraph;

    @Before
    public void setUp(){
        connectedCyclicGraph = new UnweightedGraph((V[])vertices, edges);
        unConnectedGraph = new UnweightedGraph((V[]) vertices, edgesNotAllConnected);
        connectedNonCyclicGraph = new UnweightedGraph((V[]) vertices, edgesNonCyclic);
        connectedAlternativeGraph = new UnweightedGraph((V[]) vertices, edgesAlternativeConnections);
    }

    @Test
    public void dfs__dequeStartWith0__ExpectGetSizeIs6(){
        int expected = 6;
        connectedCyclicGraph.dfs(0);
        int result = connectedCyclicGraph.getSize();
        assertEquals(expected, result);
    }

    @Test
    public void dfs__dequeStartWith0__ExpectIndexOf4Is2(){
        AbstractGraph<V>.Tree tree = connectedCyclicGraph.dfs(0);
        Deque<Integer> result  = tree.getSearchOrder();
        List<Integer> expected = Arrays.asList(0, 1, 4, 2, 3, 5);
        for(int i = 0; i < result.size(); i++){
            assertEquals(expected.get(i), result.poll());
        }
    }

    @Test
    public void getPath__connectedGraph5to3__ExpectMerkurSaturnJupiter(){
        String[] expectedPath = {"Merkur","Saturn","Venus"};
        List<V> resultPath = connectedCyclicGraph.getPath(5,3);
        for(int i = 0; i < resultPath.size(); i++){
            assertEquals(expectedPath[(expectedPath.length - 1 - i)], resultPath.get(i));
        }
    }

    @Test
    public void getPath__connectedAlternativeGraph0to5__ExpectSatJupVenMer(){
        String[] expectedPath = {"Saturn", "Jupiter", "Venus", "Merkur"};
        List<V> resultPath = connectedAlternativeGraph.getPath(0,5);
        for(int i = 0; i < resultPath.size(); i++){
            System.out.println(i + ": " + resultPath.get(i) + " = " + expectedPath[(expectedPath.length-1 - i)]);
            assertEquals(expectedPath[(expectedPath.length-1-i)], resultPath.get(i));
        }
    }

    @Test
    public void isCyclic__CyclicGraph__ExpectTRUE(){
        boolean result = connectedCyclicGraph.isCyclic();
        assertTrue(result);
    }

    @Test
    public void isCyclic__unConnectedGraph__ExpectFASLE(){
        boolean result = unConnectedGraph.isCyclic();
        assertFalse(result);
    }

    @Test
    public void isCyclic__nonCyclicGraph__ExpectFALSE(){
        boolean result = connectedNonCyclicGraph.isCyclic();
        assertFalse(result);
    }

    @Test
    public void isConnected__notFullyConnected__ExpectFALSE(){
        boolean result = unConnectedGraph.isConnected();
        assertFalse(result);
    }

    @Test
    public void isConnected__fullyConnected__ExpectTRUE(){
        boolean result = connectedCyclicGraph.isConnected();
        assertTrue(result);
    }

    @Test
    public void getDegree__connectedCyclicGraphVertex4__Expect2(){
        int expect = 2;
        int result = connectedCyclicGraph.getDegree(2);
        assertEquals(expect, result);
    }

    @Test
    public void getDegree__unConnectedGraphUnconnectedVertex__Expect0(){
        int expected = 0;
        int result = unConnectedGraph.getDegree(2);
        assertEquals(expected, result);
    }

/*

    @Test
    public void dfs__dequeStartWith4__ExpectDequeOrder4_1_0_3_2_5(){
        dequeStartWith4 = connectedCyclicGraph.dfs(4);
        Integer[] expected = {4, 1, 0, 3, 2, 5};
        int traversalIndex = 0;
        while(!dequeStartWith4.isEmpty()){
            Integer result = dequeStartWith4.pollLast();
            assertEquals(expected[traversalIndex], result);
            traversalIndex++;
        }
    }

    @Test
    public void getPath__4to5__Expect4_5(){
        List<Integer> result= connectedCyclicGraph.getPath(4, 5);
        Integer expected1 = 4;
        Integer expected2 = 5;
        assertEquals(expected1, result.get(0));
        assertEquals(expected2, result.get(1));
    }

    @Test
    public void isCyclic__cyclicInstance__ExpectTRUE(){
        boolean result = connectedCyclicGraph.isCyclic();
        assertTrue(result);
    }


    */
}
