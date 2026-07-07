class Solution {
    public long sumAndMultiply(int n) {
        long x = 0;
        long sum = 0;

        String str = String.valueOf(n);

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            if (ch != '0') {
                int digit = ch - '0';
                x = x * 10 + digit;
                sum += digit;
            }
        }

        return x * sum;
    }
}