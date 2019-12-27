package javabasic;

public class Test extends PTest<Son>{
    public static  void  main(String args[]){
        new Test().execute(new Parent());
    }

    protected void print1(Son s){
        if(s instanceof  Son){
            System.out.println("p -> s = " + s.p);
        }
    }

}
