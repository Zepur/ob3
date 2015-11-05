package DFS;

import java.util.*;

public abstract class AbstractGraph<V> implements Graph<V> {
    protected List<V> vertices = new ArrayList<>();
    protected List<List<Edge>> neighbors = new ArrayList<>();

    protected AbstractGraph() {
    }

    protected AbstractGraph(V[] vertices, int[][] edges) {
        for (int index = 0; index < vertices.length; index++)
            addVertex(vertices[index]);

        createAdjacencyLists(edges);
    }

    protected AbstractGraph(List<V> vertices, List<Edge> edges) {
        for (int index = 0; index < vertices.size(); index++)
            addVertex(vertices.get(index));

        createAdjacencyLists(edges);
    }

    protected AbstractGraph(List<Edge> edges, int numberOfVertices) {
        for (int i = 0; i < numberOfVertices; i++)
            addVertex((V)(new Integer(i)));

        createAdjacencyLists(edges);
    }

    protected AbstractGraph(int[][] edges, int numberOfVertices) {
        for (int index = 0; index < numberOfVertices; index++)
            addVertex((V)(new Integer(index)));

        createAdjacencyLists(edges);
    }

    private void createAdjacencyLists(int[][] edges) {
        for (int i = 0; i < edges.length; i++) {
            addEdge(edges[i][0], edges[i][1]);
        }
    }

    private void createAdjacencyLists(List<Edge> edges) {
        for (Edge edge: edges) {
            addEdge(edge.startVertex, edge.endVertex);
        }
    }


    @Override
    public int getSize() {
        return vertices.size();
    }

    @Override
    public List<V> getVertices() {
        return vertices;
    }

    @Override
    public V getVertex(int index) {
        return vertices.get(index);
    }

    @Override
    public int getIndex(V v) {
        return vertices.indexOf(v);
    }

    @Override
    public List<Integer> getNeighbors(int index) {
        List<Integer> result = new ArrayList<>();
        for (Edge edge : neighbors.get(index))
            result.add(edge.endVertex);

        return result;
    }

    @Override
    public int getDegree(int vertexIndex) {
        return neighbors.get(vertexIndex).size();
    }

    @Override
    public void printEdges() {
        for (int index = 0; index < neighbors.size(); index++) {
            System.out.print(getVertex(index) + " (" + index + "): ");
            for (Edge edge : neighbors.get(index)) {
                System.out.print("(" + getVertex(edge.startVertex) + ", " +
                        getVertex(edge.endVertex) + ") ");
            }
            System.out.println();
        }
    }

    @Override
    public void clear() {
        vertices.clear();
        neighbors.clear();
    }

    @Override
    public boolean addVertex(V vertex) {
        if (!vertices.contains(vertex)) {
            vertices.add(vertex);
            neighbors.add(new ArrayList<>());
            return true;
        }
        else {
            return false;
        }
    }

    protected boolean addEdge(Edge edgeToBeAdded) {
        if (edgeToBeAdded.startVertex < 0 || edgeToBeAdded.startVertex > getSize() - 1)
            throw new IllegalArgumentException("No such index: " + edgeToBeAdded.startVertex);

        if (edgeToBeAdded.endVertex < 0 || edgeToBeAdded.endVertex > getSize() - 1)
            throw new IllegalArgumentException("No such index: " + edgeToBeAdded.endVertex);

        if (!neighbors.get(edgeToBeAdded.startVertex).contains(edgeToBeAdded)) {
            neighbors.get(edgeToBeAdded.startVertex).add(edgeToBeAdded);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean addEdge(int startVertex, int endVertex) {
        return addEdge(new Edge(startVertex, endVertex));
    }


    public static class Edge {
        public int startVertex;
        public int endVertex;

        public Edge(int startVertex, int endVertex) {
            this.startVertex = startVertex;
            this.endVertex = endVertex;
        }

        public boolean equals(Object edgeObject) {
            return startVertex == ((Edge)edgeObject).startVertex && endVertex == ((Edge)edgeObject).endVertex;
        }
    }

    /*
    getStack(vertex endVertex)
        create stack
        create isVisited
        while stack has fewer elements than vertices
            if endVertex is not yet visited
                push endVertex on stack
                for each neighbor of endVertex
                    set endVertex to neighbor
       create a tree with endVertex, stack
       return tree
     */

    @Override
    public Tree dfs(int root){
        Deque<Integer> stack = new ArrayDeque<>();
        int[] parent = new int[vertices.size()];
        for(int index = 0; index < parent.length; index++) parent[index] = -1;
        boolean[] isVisited = new boolean[vertices.size()];
        stack.addLast(root);
        parent[root] = -1;
        isVisited[root] = true;
        while(stack.size() < vertices.size()){
            for(Edge edge : neighbors.get(root)) {
                root = edge.endVertex;
                if (!isVisited[root]) {
                    parent[edge.endVertex] = edge.startVertex;
                    stack.addLast(root);
                    isVisited[root] = true;
                    break;
                } else { continue; }
            }
        }
        return new Tree(root, parent, stack);
    }

    public List<V> getPath(int startVertex, int endVertex){
        return bfs(startVertex).getPath(endVertex);
    }

    public boolean isCyclic(){
        if(!isConnected()) return false;
        if(vertices.size() <= 2) return false;
        Map<String, String> test = removeDuplicateEdges();
        return (test.size() >= vertices.size());
    }

    private Map<String, String> removeDuplicateEdges() {
        Map<String, String> singleEdgedMap = new HashMap<>();
        for(int i = 0; i < vertices.size(); i++) {
            for (Edge e : neighbors.get(i)) {
                String string = getVertex(e.startVertex) +"."+ getVertex(e.endVertex);
                String reversedString = getVertex(e.endVertex) +"."+ getVertex(e.startVertex);
                if(!singleEdgedMap.containsKey(string) && !singleEdgedMap.containsKey(reversedString)){
                    singleEdgedMap.put(string, reversedString);
                }
            }
        }
        return singleEdgedMap;
    }

    public boolean isConnected(){
        Tree tree = bfs(0);
        return (tree.searchOrder.size() == vertices.size());
    }

    @Override /** Starting bfs search from vertex endVertex */
    /** To be discussed in Section 28.7 */
    public Tree bfs(int v) {
        Deque<Integer> searchOrder = new ArrayDeque<>();
        int[] parent = new int[vertices.size()];
        for (int i = 0; i < parent.length; i++)
            parent[i] = -1; // Initialize parent[i] to -1

        java.util.LinkedList<Integer> queue =
                new java.util.LinkedList<>(); // list used as a queue
        boolean[] isVisited = new boolean[vertices.size()];
        queue.offer(v); // Enqueue endVertex
        isVisited[v] = true; // Mark it visited

        while (!queue.isEmpty()) {
            int u = queue.poll(); // Dequeue to startVertex
            searchOrder.add(u); // startVertex searched
            for (Edge e: neighbors.get(u)) {
                if (!isVisited[e.endVertex]) {
                    queue.offer(e.endVertex); // Enqueue w
                    parent[e.endVertex] = u; // The parent of w is startVertex
                    isVisited[e.endVertex] = true; // Mark it visited
                }
            }
        }
        return new Tree(v, parent, searchOrder);
    }

/** Tree inner class inside the DFS.AbstractGraph class */
    /** To be discussed in Section 28.5 */
    public class Tree {
        private int root; // The root of the tree
        private int[] parent; // Store the parent of each vertex
        private Deque<Integer> searchOrder; // Store the search order

        /** Construct a tree with root, parent, and searchOrder */
        public Tree(int root, int[] parent, Deque<Integer> searchOrder) {
            this.root = root;
            this.parent = parent;
            this.searchOrder = searchOrder;
        }

        /** Return the root of the tree */
        public int getRoot() {
            return root;
        }

        /** Return the parent of vertex endVertex */
        public int getParent(int v) {
            return parent[v];
        }

        /** Return an array representing search order */
        public Deque<Integer> getSearchOrder() {
            return searchOrder;
        }

        /** Return number of vertices found */
        public int getNumberOfVerticesFound() {
            return searchOrder.size();
        }

        /** Return the path of vertices from a vertex to the root */
        public List<V> getPath(int index) {
            ArrayList<V> path = new ArrayList<>();

            do {
                path.add(vertices.get(index));
                index = parent[index];
            }
            while (index != -1);

            return path;
        }

        /** Print a path from the root to vertex endVertex */
        public void printPath(int index) {
            List<V> path = getPath(index);
            System.out.print("A path from " + vertices.get(root) + " to " +
                    vertices.get(index) + ": ");
            for (int i = path.size() - 1; i >= 0; i--)
                System.out.print(path.get(i) + " ");
        }

        /** Print the whole tree */
        public void printTree() {
            System.out.println("Root is: " + vertices.get(root));
            System.out.print("Edges: ");
            for (int i = 0; i < parent.length; i++) {
                if (parent[i] != -1) {
                    // Display an edge
                    System.out.print("(" + vertices.get(parent[i]) + ", " +
                            vertices.get(i) + ") ");
                }
            }
            System.out.println();
        }
    }
}