import java.util.*;

class Solution {
    public List<Integer> remainingMethods(int n, int k, int[][] invocations) {
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int[] e : invocations) {
            graph[e[0]].add(e[1]);
        }

        // Find all suspicious methods
        boolean[] suspicious = new boolean[n];
        Queue<Integer> q = new LinkedList<>();
        q.offer(k);
        suspicious[k] = true;

        while (!q.isEmpty()) {
            int u = q.poll();
            for (int v : graph[u]) {
                if (!suspicious[v]) {
                    suspicious[v] = true;
                    q.offer(v);
                }
            }
        }

        // Check if any outside method invokes a suspicious one
        for (int[] e : invocations) {
            int u = e[0];
            int v = e[1];
            if (!suspicious[u] && suspicious[v]) {
                List<Integer> ans = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    ans.add(i);
                }
                return ans;
            }
        }

        // Remove suspicious methods
        List<Integer> ans = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!suspicious[i]) {
                ans.add(i);
            }
        }

        return ans;
    }
}