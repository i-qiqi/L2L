package array;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindMaxConsecutiveOnes {
    public static  void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<Integer>(Arrays.asList(new Integer[8]));
//        list.set(2, 2);
//        for(int i = 0; i < 16; i++){
//            list.add(2);
//        }
        System.out.println("val = "+ list.get(3));
        System.out.println("capacity = "+getArrayListCapacity(list));
    }

    public static int getArrayListCapacity(ArrayList<?> arrayList) {
        Class<ArrayList> arrayListClass = ArrayList.class;
        try {
            Field field = arrayListClass.getDeclaredField("elementData");
            field.setAccessible(true);
            Object[] objects = (Object[]) field.get(arrayList);
            return objects.length;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return -1;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return -1;
        }
    }

}
