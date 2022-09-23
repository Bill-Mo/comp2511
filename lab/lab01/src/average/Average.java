package average;

public class Average {
    /**
     * Returns the average of an array of numbers
     * 
     * @param the array of integer numbers
     * @return the average of the numbers
     */
    public Average() {
    }
    
    public float computeAverage(int[] nums) {
        float result = 0;
        // Add your code
        for (int i = 0; i < nums.length; i++) {
            result += nums[i];
        }
        result = result / nums.length;
        return result;
    }

    public static void main(String[] args) {
        // Add your code
        int[] numbers = {1, 3, 4, 52, -10};
        Average avg = new Average();
        float result = avg.computeAverage(numbers);
        System.out.println(result);
        
    }
}
