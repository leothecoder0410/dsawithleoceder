/**
 * @param {number[]} nums
 * @return {number}
 */
var uniqueXorTriplets = function(nums) {
    const MAX = 2048;

    // suffixPair[i][x] = true if x can be formed by nums[j] ^ nums[k]
    // where i <= j <= k
    const suffixPair = Array.from({ length: nums.length + 1 }, () => new Uint8Array(MAX));

    for (let i = nums.length - 1; i >= 0; i--) {
        // Copy suffixPair[i + 1]
        suffixPair[i].set(suffixPair[i + 1]);

        // Add pairs (i, k)
        for (let k = i; k < nums.length; k++) {
            suffixPair[i][nums[i] ^ nums[k]] = 1;
        }
    }

    const ans = new Uint8Array(MAX);

    for (let i = 0; i < nums.length; i++) {
        for (let x = 0; x < MAX; x++) {
            if (suffixPair[i][x]) {
                ans[nums[i] ^ x] = 1;
            }
        }
    }

    let count = 0;
    for (let x = 0; x < MAX; x++) {
        if (ans[x]) count++;
    }

    return count;
};