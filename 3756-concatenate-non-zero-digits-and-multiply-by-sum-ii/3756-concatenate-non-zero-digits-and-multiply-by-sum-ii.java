import java.util.*;

class Solution {
    private static final int MOD = 1_000_000_007;

    public int[] sumAndMultiply(String s, int[][] queries) {
        int n = s.length();

        ArrayList<Integer> positions = new ArrayList<>();
        ArrayList<Integer> digits = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int d = s.charAt(i) - '0';
            if (d != 0) {
                positions.add(i);
                digits.add(d);
            }
        }

        int k = digits.size();

        long[] pow10 = new long[k + 1];
        pow10[0] = 1;
        for (int i = 1; i <= k; i++) {
            pow10[i] = (pow10[i - 1] * 10) % MOD;
        }

        long[] prefixValue = new long[k + 1];
        long[] prefixSum = new long[k + 1];

        for (int i = 0; i < k; i++) {
            prefixValue[i + 1] = (prefixValue[i] * 10 + digits.get(i)) % MOD;
            prefixSum[i + 1] = prefixSum[i] + digits.get(i);
        }

        int[] ans = new int[queries.length];

        for (int i = 0; i < queries.length; i++) {
            int l = queries[i][0];
            int r = queries[i][1];

            int left = lowerBound(positions, l);
            int right = upperBound(positions, r) - 1;

            if (left > right) {
                ans[i] = 0;
                continue;
            }

            int len = right - left + 1;

            long value = (prefixValue[right + 1]
                    - (prefixValue[left] * pow10[len]) % MOD + MOD) % MOD;

            long sum = prefixSum[right + 1] - prefixSum[left];

            ans[i] = (int) ((value * (sum % MOD)) % MOD);
        }

        return ans;
    }

    private int lowerBound(ArrayList<Integer> arr, int target) {
        int l = 0, r = arr.size();
        while (l < r) {
            int mid = l + (r - l) / 2;
            if (arr.get(mid) >= target)
                r = mid;
            else
                l = mid + 1;
        }
        return l;
    }

    private int upperBound(ArrayList<Integer> arr, int target) {
        int l = 0, r = arr.size();
        while (l < r) {
            int mid = l + (r - l) / 2;
            if (arr.get(mid) > target)
                r = mid;
            else
                l = mid + 1;
        }
        return l;
    }
}