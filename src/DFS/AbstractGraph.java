package DFS;

import java.util.*;

public abstract class AbstractGraph<V> implements Graph<V> {
    protected List<V> vertices = new ArrayList<>();
    protected List<List<Edge>> neighbors = new ArrayList<>();

    protected AbstractGraph(){
    }

    protected AbstractGraph(V[] vertices, int[][] edges){
        for (V vertice : vertices)
            addVertex(vertice);
        createAdjacencyLists(edges);
    }

    protected AbstractGraph(List<V> vertices, List<Edge> edges){
        for (V vertice : vertices)
            addVertex(vertice);
        createAdjacencyLists(edges);
    }

    protected AbstractGraph(List<Edge> edges, int numberOfVertices){
        for (int i = 0; i < numberOfVertices; i++)
            addVertex((V)(new Integer(i)));

        createAdjacencyLists(edges);
    }

    protected AbstractGraph(int[][] edges, int numberOfVertices){
        for (int index = 0; index < numberOfVertices; index++)
            addVertex((V)(new Integer(index)));

        createAdjacencyLists(edges);
    }

    private void createAdjacencyLists(int[][] edges){
        for (int[] edge : edges) {
            addEdge(edge[0], edge[1]);
        }
    }

    private void createAdjacencyLists(List<Edge> edges){
        for (Edge edge: edges){
            addEdge(edge.startVertex, edge.endVertex);
        }
    }


    @Override
    public int getSize(){
        return vertices.size();
    }

    @Override
    public V getVertex(int index){
        return vertices.get(index);
    }

    @Override
    public int getDegree(int vertexIndex){
        return neighbors.get(vertexIndex).size();
    }

    @Override
    public boolean addVertex(V vertex){
        if (!vertices.contains(vertex)){
            vertices.add(vertex);
            neighbors.add(new ArrayList<>());
            return true;
        }
        else {
            return false;
        }
    }

    protected boolean addEdge(Edge edgeToBeAdded){
        if (edgeToBeAdded.startVertex < 0 || edgeToBeAdded.startVertex > getSize() - 1)
            throw new IllegalArgumentException("No such index: " + edgeToBeAdded.startVertex);

        if (edgeToBeAdded.endVertex < 0 || edgeToBeAdded.endVertex > getSize() - 1)
            throw new IllegalArgumentException("No such index: " + edgeToBeAdded.endVertex);

        if (!neighbors.get(edgeToBeAdded.startVertex).contains(edgeToBeAdded)){
            neighbors.get(edgeToBeAdded.startVertex).add(edgeToBeAdded);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean addEdge(int startVertex, int endVertex){
        return addEdge(new Edge(startVertex, endVertex));
    }

    public static class Edge {
        public int startVertex;
        public int endVertex;

        public Edge(int startVertex, int endVertex){
            this.startVertex = startVertex;
            this.endVertex = endVertex;
        }

        public boolean equals(Object edgeObject){
            return (startVertex == ((Edge)edgeObject).startVertex) && (endVertex == ((Edge)edgeObject).endVertex);
        }
    }

    /*
    dfs(vertex startPointVertex)
        stack = create stack<Integer>
        isVisited create isVisited boolean[]
        while stack has fewer elements than number of vertices
            for each edge connected to the startPointVertex
                set startPointVertex = edges endpoint
                if endVertex IS visited
                    continue to next
                if endVertex IS NOT visited
                    push endVertex on stack
                    set visited startPointVertex
                    set parent of edges endpoint to edges startpoint
       create a tree with endVertex, stack
       return tree
     */

    @Override
    public Tree dfs(int startVertex){
        Deque<Integer> stack = new ArrayDeque<>();
        int[] parent = createParentArrayWithAllElementsAsNegativeOne();
        boolean[] isVisited = new boolean[vertices.size()];
        stack.addLast(startVertex);
        parent[startVertex] = -1;
        isVisited[startVertex] = true;
        while(stack.size() < vertices.size()){
            for(Edge edge : neighbors.get(startVertex)){
                startVertex = edge.endVertex;
                if (!isVisited[startVertex]){
                    parent[edge.endVertex] = edge.startVertex;
                    stack.addLast(startVertex);
                    isVisited[startVertex] = true;
                    break;
                }
            }
        }
        return new Tree(startVertex, parent, stack);
    }

    public List<V> getPath(V startElement, V endElement){
        if(!vertices.contains(startElement))
            throw new IllegalArgumentException("The vertex \"" + startElement + "\" does not exist.");
        if(!vertices.contains(endElement))
            throw new IllegalArgumentException("The vertex \"" + endElement + "\" does not exist.");
        int startVertex = vertices.indexOf(startElement);
        int endVertex = vertices.indexOf(endElement);
        return getPath(startVertex, endVertex);
    }

    public List<V> getPath(int startVertex, int endVertex){
        if(startVertex >= vertices.size())
            throw new IllegalArgumentException("The vertex \"" + startVertex + "\" does not exist.");
        if(endVertex >= vertices.size())
            throw new IllegalArgumentException("The vertex \"" + endVertex + "\" does not exist.");
        return bfs(startVertex).getPath(endVertex);
    }

    public boolean isCyclic(){
        if(!isConnected())
            return false;
        if(vertices.size() <= 2)
            return false;
        Map<String, String> edgeMapNoDuplicates = removeDuplicateEdges();
        return (edgeMapNoDuplicates.size() >= vertices.size());
    }

    private Map<String, String> removeDuplicateEdges(){
        Map<String, String> edgeMapNoDuplicates = new HashMap<>();
        for(int i = 0; i < vertices.size(); i++){
            for (Edge e : neighbors.get(i)){
                String edgeStructure = getVertex(e.startVertex) +"."+ getVertex(e.endVertex);
                String edgeStructureReversed = getVertex(e.endVertex) +"."+ getVertex(e.startVertex);
                if(!edgeMapNoDuplicates.containsKey(edgeStructure) && !edgeMapNoDuplicates.containsKey(edgeStructureReversed)){
                    edgeMapNoDuplicates.put(edgeStructure, edgeStructureReversed);
                }
            }
        }
        return edgeMapNoDuplicates;
    }

    public boolean isConnected(){
        Tree bfsTree = bfs(0);
        return (bfsTree.searchOrder.size() == vertices.size());
    }

    @Override
    public Tree bfs(int startVertex){
        Deque<Integer> searchOrder = new ArrayDeque<>();
        int[] parent = createParentArrayWithAllElementsAsNegativeOne();
        java.util.LinkedList<Integer> queue = new java.util.LinkedList<>();
        boolean[] isVisited = new boolean[vertices.size()];
        queue.offer(startVertex);
        isVisited[startVertex] = true;

        while (!queue.isEmpty()){
            int u = queue.poll();
            searchOrder.add(u);
            for (Edge e: neighbors.get(u)){
                if (!isVisited[e.endVertex]){
                    queue.offer(e.endVertex);
                    parent[e.endVertex] = u;
                    isVisited[e.endVertex] = true;
                }
            }
        }
        return new Tree(startVertex, parent, searchOrder);
    }

    private int[] createParentArrayWithAllElementsAsNegativeOne(){
        int[] parent = new int[vertices.size()];
        for (int index = 0; index < parent.length; index++)
            parent[index] = -1;
        return parent;
    }

    public class Tree {
        private int root;
        private int[] parent;
        private Deque<Integer> searchOrder;

        public Tree(int root, int[] parent, Deque<Integer> searchOrder){
            this.root = root;
            this.parent = parent;
            this.searchOrder = searchOrder;
        }

        public Deque<Integer> getSearchOrder(){
            return searchOrder;
        }

        public List<V> getPath(int index){
            ArrayList<V> path = new ArrayList<>();

            do {
                path.add(vertices.get(index));
                index = parent[index];
            }
            while (index != -1);

            return path;
        }
    }
}