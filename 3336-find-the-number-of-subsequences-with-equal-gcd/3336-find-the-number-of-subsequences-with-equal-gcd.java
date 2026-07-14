class Solution {
    static final int MOD = 1_000_000_007;

    public int subsequencePairCount(int[] nums) {
        int max = 0;
        for (int x : nums) {
            max = Math.max(max, x);
        }

        long[][] dp = new long[max + 1][max + 1];
        dp[0][0] = 1;

        for (int x : nums) {
            long[][] next = new long[max + 1][max + 1];

            for (int g1 = 0; g1 <= max; g1++) {
                for (int g2 = 0; g2 <= max; g2++) {
                    long ways = dp[g1][g2];
                    if (ways == 0) continue;

                    // Skip current element
                    next[g1][g2] = (next[g1][g2] + ways) % MOD;

                    // Put in seq1
                    int ng1 = (g1 == 0) ? x : gcd(g1, x);
                    next[ng1][g2] = (next[ng1][g2] + ways) % MOD;

                    // Put in seq2
                    int ng2 = (g2 == 0) ? x : gcd(g2, x);
                    next[g1][ng2] = (next[g1][ng2] + ways) % MOD;
                }
            }

            dp = next;
        }

        long ans = 0;
        for (int g = 1; g <= max; g++) {
            ans = (ans + dp[g][g]) % MOD;
        }

        return (int) ans;
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return a;
    }
}