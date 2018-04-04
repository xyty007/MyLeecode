package main.java;

// haven't solved

public class ContainerWithMostWater {

    public int maxArea(int[] height) {
        int max = 0;
        boolean[] abandoned = getAbandon(height);
        int front = nextValueIndex(0, abandoned);
        int behind = lastValueIndex(height.length-1, abandoned);
        do{
            int temp;
            if(height[front]>height[behind]){
                temp = (behind-front)*height[behind];
                behind = lastValueIndex(behind-1, abandoned);
            }else if(height[front]<height[behind]){
                temp = (behind-front)*height[front];
                front = nextValueIndex(front+1, abandoned);
            }else{
                temp = (behind-front)*height[front];
                behind = lastValueIndex(behind-1, abandoned);
                front = nextValueIndex(front+1, abandoned);
            }
            max = max<temp?temp:max;
            if(front>=behind||front==-1||behind==-1) break;
        }while (true);
        return max;
    }

    public boolean[] getAbandon(int[] array) {
        boolean[] abandoned = new boolean[array.length];
        int i = 0;
        while (i < array.length) {
            int pre, curr, next;
            pre = nextValueIndex(i, abandoned);
            curr = nextValueIndex(pre + 1, abandoned);
            next = nextValueIndex(curr + 1, abandoned);
            if (next == -1) {
                break;
            } else {
                if (array[pre] >= array[curr] && array[curr] <= array[next]) {
                    abandoned[curr] = true;
                    if (array[pre] <= array[next]) {
                        for (int j = i - 1; j >= 0; j--) {
                            if (!abandoned[j]) {
                                i = j;
                                break;
                            }
                        }
                    }
                } else {
                    i = nextValueIndex(i + 1, abandoned);
                }
            }
        }
        return abandoned;
    }

    public int nextValueIndex(int startIndex, boolean[] abandoned) {
        if (startIndex >= abandoned.length)
            return -1;
        for (int i = startIndex; i < abandoned.length; i++) {
            if (!abandoned[i]) {
                return i;
            }
        }
        return -1;
    }

    public int lastValueIndex(int startIndex, boolean[] abandoned) {
        if (startIndex < 0)
            return -1;
        for (int i = startIndex; i >=0; i--) {
            if (!abandoned[i]) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        ContainerWithMostWater cwmw = new ContainerWithMostWater();
        System.out.println(cwmw.maxArea(new int[]{3, 2, 1, 3}));
    }

}
