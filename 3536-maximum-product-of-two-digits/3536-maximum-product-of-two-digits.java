class Solution {
    public int maxProduct(int n) {
        int[] digits = new int[10];
        int len = 0;

        while (n > 0) {
            digits[len++] = n % 10;
            n /= 10;
        }

        int ans = 0;
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                ans = Math.max(ans, digits[i] * digits[j]);
            }
        }

        return ans;
    }
}