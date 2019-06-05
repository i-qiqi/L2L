package designPattern.singleton;
//version 1.0
//饿汉模式
//public class Singleton{
//    private static Singleton instance = new Singleton();
//    public static Singleton  getSingleton(){
//        return instance;
//    }
//}

//version 2.0
//懒汉模式
//public  class Singleton{
//    private  static Singleton instance = null;
//    public synchronized static  Singleton getSingleton(){
//        if(instance == null){ //只需创建时候的同步，后面获取不需要同步
//            return instance = new Singleton();
//        }
//        return instance;
//    }
//}

//version 3.0 DCL
//public class Singleton{
//    private volatile static Singleton instance = null;
//    public static  Singleton getSingleton(){
//        if(instance == null){
//            synchronized (Singleton.class){
//                if(instance == null){
//                    return instance = new Singleton();
//                }
//            }
//        }
//        return instance;
//    }
//}

//version 4.0 内部类
//public class Singleton{
//    //利用ClassLoader本身的机制来避免多个线程同时实例化该变量
//    private static  class SingletonHolder{
//        public static  Singleton singleton = new Singleton();
//    }
//
//    public static Singleton getSingleton(){
//        return SingletonHolder.singleton;
//    }
//}


