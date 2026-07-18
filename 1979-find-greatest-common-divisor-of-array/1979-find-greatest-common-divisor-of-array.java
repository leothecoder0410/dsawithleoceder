class Solution {
    public int findGCD(int[] nums) {
        int min = nums[0];
        int max = nums[0];

        // Find minimum and maximum elements
        for (int num : nums) {
            if (num < min) {
                min = num;
            }
            if (num > max) {
                max = num;
            }
        }

        // Return GCD of min and max
        return gcd(min, max);
    }

    // Euclidean Algorithm
    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}