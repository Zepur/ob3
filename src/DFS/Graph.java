package DFS;

public interface Graph<V> {
    int getSize();

    V getVertex(int index);

    int getDegree(int v);

    boolean addVertex(V vertex);

    boolean addEdge(int u, int v);

    AbstractGraph<V>.Tree dfs(int v);

    AbstractGraph<V>.Tree bfs(int v);

}