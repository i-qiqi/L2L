import java.util.PriorityQueue;

public class Test {

//    public static void main(String[] args){
//        String aa = "ab";
//        String a= new String("ab");
//        String b= new String("ab");
//        String bb = "ab";
//        // == 基本类型比较的是值， 引用类型比较的是对象的内存地址
//        if(aa == bb){
//            System.out.println("aa == bb");
//        }
//
//        //String的equals()被覆盖过
//        //object 的 equals 方法是比较的对象的内存地址，
//        // 而 String 的 equals 方法比较的是对象的值。
//        if(a == b){
//            System.out.println("a == b");
//        }
//
//        if(a.equals(b)){
//            System.out.println("a EQ b");
//        }
//
//        if(aa.equals(bb)){
//            System.out.println("aa EQ bb");
//        }
//    }

//    public static void main(String[] args) {
//        int[] arr = { 1, 2, 3, 4, 5 };
//        System.out.println(arr[0]);
//        change(arr);
//        System.out.println(arr[0]);
//    }
//
//    public static void change(int[] array) {
//        // 将数组的第一个元素变为0
//        array[0] = 0;
//    }

    public static void main(String[] args){
        Test t = new Test();

        System.out.println("num = "+t.nthUglyNumber(1400));
    }

    public long nthUglyNumber(long n) {
        PriorityQueue<Long> q = new PriorityQueue<Long>();
        q.offer(1l);
        long num = 1l;
        long cnt = 0l;
        while(cnt != n){
            num = q.poll();
            while(!q.isEmpty() && q.peek() == num) num = q.poll(); //清空重复的
            q.offer(num*2);
            q.offer(num*3);
            q.offer(num*5);
            ++cnt;
        }

        return num;
    }

//    public int nthUglyNumber(int n) {
//        PriorityQueue<Integer> q = new PriorityQueue<Integer>();
//        q.offer(1);
//        int num = 1;
//        int cnt = 0;
//        while(cnt != n){
//            num = poll(q);
//            q.offer(num*2);
//            q.offer(num*3);
//            q.offer(num*5);
//            ++cnt;
//            System.out.println("num = "+num + " count = "+cnt);
//        }
//
//        return num;
//    }
//
//    public int poll(PriorityQueue<Integer> q){
//        int num = q.peek();
//        q.poll();
//        System.out.println("num = "+ q.peek());
//        while(!q.isEmpty()){
//            if(q.peek() == num){
//                q.poll();
//            }else {
//                break;
//            }
//        }
//        return num;
//    }
}
