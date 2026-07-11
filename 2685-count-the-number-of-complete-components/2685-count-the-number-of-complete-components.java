import java.util.*;

class Solution {
    public int countCompleteComponents(int n, int[][] edges) {

        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int[] edge : edges) {
            graph[edge[0]].add(edge[1]);
            graph[edge[1]].add(edge[0]);
        }

        boolean[] visited = new boolean[n];
        int completeComponents = 0;

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                int[] result = dfs(i, graph, visited);
                int nodes = result[0];
                int degreeSum = result[1];

                if (degreeSum == nodes * (nodes - 1)) {
                    completeComponents++;
                }
            }
        }

        return completeComponents;
    }

    private int[] dfs(int node, List<Integer>[] graph, boolean[] visited) {
        visited[node] = true;

        int nodes = 1;
        int degreeSum = graph[node].size();

        for (int neighbor : graph[node]) {
            if (!visited[neighbor]) {
                int[] res = dfs(neighbor, graph, visited);
                nodes += res[0];
                degreeSum += res[1];
            }
        }

        return new int[]{nodes, degreeSum};
    }
}