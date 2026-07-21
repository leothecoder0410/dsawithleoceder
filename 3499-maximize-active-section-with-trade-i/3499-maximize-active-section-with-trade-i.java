class Solution {
    public int maxActiveSectionsAfterTrade(String s) {
        int ones = 0;
        for (char c : s.toCharArray()) {
            if (c == '1') ones++;
        }

        String t = "1" + s + "1";
        int m = t.length();

        int ans = ones;

        int i = 1;
        while (i < m - 1) {
            if (t.charAt(i) != '1') {
                i++;
                continue;
            }

            int l = i;
            while (i < m - 1 && t.charAt(i) == '1') i++;
            int r = i - 1;

            // 1-block must be surrounded by 0's
            if (t.charAt(l - 1) == '0' && t.charAt(r + 1) == '0') {

                int removed = r - l + 1;

                int leftZeros = 0;
                int p = l - 1;
                while (p >= 0 && t.charAt(p) == '0') {
                    leftZeros++;
                    p--;
                }

                int rightZeros = 0;
                p = r + 1;
                while (p < m && t.charAt(p) == '0') {
                    rightZeros++;
                    p++;
                }

                int gain = leftZeros + removed + rightZeros;

                ans = Math.max(ans, ones - removed + gain);
            }
        }

        return ans;
    }
}