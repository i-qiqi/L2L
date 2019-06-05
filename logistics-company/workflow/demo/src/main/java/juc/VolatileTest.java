package juc;


public class VolatileTest {
    public static class Test {
        int a = 0;
        boolean flag = false;

        public synchronized   void write() {
            a = 2; //1
            flag = true; //2
        }

        public  synchronized void multiply() {
            if (flag) { //3
                int ret = a * a;//4
                System.out.println("t2 : ret = " + ret);

            }
        }
    }
    public static void main(String[] args){

        final Test test = new Test();
        new Thread(){
            @Override
            public void run() {
                test.write();
                System.out.println("t1 : a = " + test.a);
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                test.multiply();
                System.out.println("t2 : a = " + test.a);
                System.out.println("t2 : flag = " + test.flag);

            }
        }.start();
    }
}
