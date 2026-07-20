public class Ex05 {

    public int sum(int[] arr) {
        int total = 0;
        for(int i = 0; i < arr.length; i++){
            total = total + arr[i];
        }
        return total;
    }

    public int max(int[] arr) {
        int max  = Integer.MIN_VALUE;
        for(int i = 0; i < arr.length; i++){
        if(arr[i] > max)
        max = arr[i];
        }
        return max;
    }

    public int count(int[] arr, int target) {
        int count = 0;
        for(int i = 0; i < arr.length; i++){
        if(arr[i] == target)
        count ++;
        }
        return count;
    }

    public int[] reverse(int[] arr) {
        int[] newarr = new int[arr.length];
        for(int i = 0; i < arr.length; i++){
        newarr[arr.length -1 -i] = arr[i];
        }
        return newarr;
    }
}