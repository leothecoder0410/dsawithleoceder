class Solution {

    // Prime exponent limits for t <= 1e14
    int A, B, C, D;

    int SB, SC, SD;
    int totalStates;
    int[] dp;

    // Factor contribution of digits 1..9
    int[][] factors = {
        {0, 0, 0, 0}, // 0 - never used
        {0, 0, 0, 0}, // 1
        {1, 0, 0, 0}, // 2
        {0, 1, 0, 0}, // 3
        {2, 0, 0, 0}, // 4
        {0, 0, 1, 0}, // 5
        {1, 1, 0, 0}, // 6
        {0, 0, 0, 1}, // 7
        {3, 0, 0, 0}, // 8
        {0, 2, 0, 0}  // 9
    };

    public String smallestNumber(String num, long t) {

        // 1. Factor t
        long x = t;

        A = B = C = D = 0;

        while (x % 2 == 0) {
            A++;
            x /= 2;
        }

        while (x % 3 == 0) {
            B++;
            x /= 3;
        }

        while (x % 5 == 0) {
            C++;
            x /= 5;
        }

        while (x % 7 == 0) {
            D++;
            x /= 7;
        }

        // t contains a prime factor other than 2,3,5,7
        if (x != 1) {
            return "-1";
        }

        // 2. Build minimum-digit DP
        buildDP();

        int n = num.length();

        /*
         * Prefix factor counts.
         * pref2[i] = number of factors of 2 in num[0..i-1]
         * capped at the required exponent.
         */
        int[] pref2 = new int[n + 1];
        int[] pref3 = new int[n + 1];
        int[] pref5 = new int[n + 1];
        int[] pref7 = new int[n + 1];

        boolean[] zeroPrefix = new boolean[n + 1];

        for (int i = 0; i < n; i++) {
            int digit = num.charAt(i) - '0';

            pref2[i + 1] = Math.min(A, pref2[i]);
            pref3[i + 1] = Math.min(B, pref3[i]);
            pref5[i + 1] = Math.min(C, pref5[i]);
            pref7[i + 1] = Math.min(D, pref7[i]);

            zeroPrefix[i + 1] = zeroPrefix[i] || digit == 0;

            if (digit != 0) {
                pref2[i + 1] = Math.min(A,
                        pref2[i + 1] + factors[digit][0]);

                pref3[i + 1] = Math.min(B,
                        pref3[i + 1] + factors[digit][1]);

                pref5[i + 1] = Math.min(C,
                        pref5[i + 1] + factors[digit][2]);

                pref7[i + 1] = Math.min(D,
                        pref7[i + 1] + factors[digit][3]);
            }
        }

        // 3. If num itself is valid, return it
        if (!zeroPrefix[n]
                && pref2[n] >= A
                && pref3[n] >= B
                && pref5[n] >= C
                && pref7[n] >= D) {
            return num;
        }

        /*
         * 4. Find the rightmost position that can be increased.
         *
         * Changing the rightmost possible position gives the
         * smallest number >= num.
         */
        for (int i = n - 1; i >= 0; i--) {

            // Prefix before i must be zero-free
            if (zeroPrefix[i]) {
                continue;
            }

            int original = num.charAt(i) - '0';

            for (int digit = Math.max(1, original + 1); digit <= 9; digit++) {

                int need2 = Math.max(
                        0,
                        A - pref2[i] - factors[digit][0]
                );

                int need3 = Math.max(
                        0,
                        B - pref3[i] - factors[digit][1]
                );

                int need5 = Math.max(
                        0,
                        C - pref5[i] - factors[digit][2]
                );

                int need7 = Math.max(
                        0,
                        D - pref7[i] - factors[digit][3]
                );

                int requiredDigits = getDP(
                        need2, need3, need5, need7
                );

                int remaining = n - i - 1;

                if (requiredDigits <= remaining) {

                    StringBuilder ans = new StringBuilder(n);

                    // Original prefix
                    ans.append(num, 0, i);

                    // Increased digit
                    ans.append(digit);

                    // Smallest possible suffix
                    buildSmallestSuffix(
                            ans,
                            remaining,
                            need2,
                            need3,
                            need5,
                            need7
                    );

                    return ans.toString();
                }
            }
        }

        /*
         * 5. No answer with the same length.
         *
         * Any number with length > n is automatically > num.
         *
         * Minimum possible length is max(n + 1, minimum digits
         * required to create the digit product).
         */
        int minimumLength = getDP(A, B, C, D);

        if (minimumLength >= Integer.MAX_VALUE / 2) {
            return "-1";
        }

        int length = Math.max(n + 1, minimumLength);

        StringBuilder ans = new StringBuilder(length);

        buildSmallestSuffix(
                ans,
                length,
                A,
                B,
                C,
                D
        );

        return ans.toString();
    }

    /*
     * dp[state] = minimum number of digits required
     * to satisfy the prime exponent requirements of that state.
     */
    private void buildDP() {

        SB = B + 1;
        SC = C + 1;
        SD = D + 1;

        totalStates = (A + 1) * (B + 1) * (C + 1) * (D + 1);

        dp = new int[totalStates];

        int INF = 1_000_000;

        for (int i = 0; i < totalStates; i++) {
            dp[i] = INF;
        }

        // No factors required -> zero digits
        dp[0] = 0;

        /*
         * States are processed in increasing exponent order.
         * Every transition goes to a state with at least one
         * smaller exponent.
         */
        for (int a = 0; a <= A; a++) {
            for (int b = 0; b <= B; b++) {
                for (int c = 0; c <= C; c++) {
                    for (int d = 0; d <= D; d++) {

                        if (a == 0 && b == 0 && c == 0 && d == 0) {
                            continue;
                        }

                        int best = INF;

                        for (int digit = 2; digit <= 9; digit++) {

                            int fa = factors[digit][0];
                            int fb = factors[digit][1];
                            int fc = factors[digit][2];
                            int fd = factors[digit][3];

                            if (a < fa ||
                                b < fb ||
                                c < fc ||
                                d < fd) {
                                continue;
                            }

                            int na = a - fa;
                            int nb = b - fb;
                            int nc = c - fc;
                            int nd = d - fd;

                            int previous = getDP(na, nb, nc, nd);

                            if (previous != INF) {
                                best = Math.min(best, previous + 1);
                            }
                        }

                        setDP(a, b, c, d, best);
                    }
                }
            }
        }
    }

    private int getDP(int a, int b, int c, int d) {
        int index = (((a * SB + b) * SC + c) * SD + d);
        return dp[index];
    }

    private void setDP(int a, int b, int c, int d, int value) {
        int index = (((a * SB + b) * SC + c) * SD + d);
        dp[index] = value;
    }

    /*
     * Construct the lexicographically smallest suffix of exactly
     * 'length' digits that satisfies the required factors.
     */
    private void buildSmallestSuffix(
            StringBuilder ans,
            int length,
            int need2,
            int need3,
            int need5,
            int need7) {

        for (int pos = 0; pos < length; pos++) {

            int remaining = length - pos - 1;

            for (int digit = 1; digit <= 9; digit++) {

                int n2 = Math.max(
                        0,
                        need2 - factors[digit][0]
                );

                int n3 = Math.max(
                        0,
                        need3 - factors[digit][1]
                );

                int n5 = Math.max(
                        0,
                        need5 - factors[digit][2]
                );

                int n7 = Math.max(
                        0,
                        need7 - factors[digit][3]
                );

                if (getDP(n2, n3, n5, n7) <= remaining) {

                    ans.append(digit);

                    need2 = n2;
                    need3 = n3;
                    need5 = n5;
                    need7 = n7;

                    break;
                }
            }
        }
    }
}