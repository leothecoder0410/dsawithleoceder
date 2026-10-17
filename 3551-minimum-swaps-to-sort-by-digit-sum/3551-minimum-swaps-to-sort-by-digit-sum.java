import java.util.Arrays;

class Solution {
    
    private int getDigitSum(int n) {
        int sum = 0;
        while (n > 0) {
            sum += n % 10;
            n /= 10;
        }
        return sum;
    }

    public int minSwaps(int[] nums) {
        int n = nums.length;
        
        // Store each number along with its digit sum and original index
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(nums[i], getDigitSum(nums[i]), i);
        }

        // Sort based on digit sum, then by value
        Arrays.sort(items, (a, b) -> {
            if (a.digitSum != b.digitSum) {
                return Integer.compare(a.digitSum, b.digitSum);
            }
            return Integer.compare(a.value, b.value);
        });

        // Find cycles
        boolean[] visited = new boolean[n];
        int swaps = 0;

        for (int i = 0; i < n; i++) {
            if (visited[i] || items[i].originalIndex == i) {
                continue;
            }

            // Traverse the cycle
            int cycleLength = 0;
            int curr = i;
            while (!visited[curr]) {
                visited[curr] = true;
                curr = items[curr].originalIndex;
                cycleLength++;
            }

            if (cycleLength > 1) {
                swaps += cycleLength - 1;
            }
        }

        return swaps;
    }

    private static class Item {
        int value;
        int digitSum;
        int originalIndex;

        Item(int value, int digitSum, int originalIndex) {
            this.value = value;
            this.digitSum = digitSum;
            this.originalIndex = originalIndex;
        }
    }
}