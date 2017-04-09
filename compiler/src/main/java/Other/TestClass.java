package Other;

import Memories.Counter;
import Memories.Register;
import Memories.Wagon;

import java.util.*;

/**
 * Created by Admin on 28.10.2016.
 * Просто прогон всех конструкторов, чтений и тп
 */
public class TestClass {
    public static void main1(String[] args) {
        Register register = new Register("R1","Val1");
        Wagon wagon = new Wagon("ЛВ","ПВ", new ArrayList<>(Arrays.asList("1","2","3")));
        System.out.println(wagon);
        System.out.println(register);
    }
    public static  String changer( String changer, Integer a){
        a=a++;
               return changer+="a";
    }
    public static void main2(String[] args) {
        String test = "test";
        Integer a = 0;
        changer(test,a);
        System.out.println(test);
        System.out.println(a);
        a = 88;

    }
    static class NewTestClass{
        Integer testInt;

        public NewTestClass(Integer testInt) {
            this.testInt = testInt;
    }
    }
    public static void main3(String[] args) {
        String a = "Hello";
        String b = "world";
        Integer b1= 42;
        changer(a, b1);
        System.out.println(b1);

        HashMap<Integer, String> testMap = new HashMap<>();
        testMap.put(1,a);
        testMap.put(2,b);
        a+="!";
        System.out.println(testMap.get(1));

        HashMap<Integer,Integer> testM2= new HashMap<>();
        Integer testInt = new Integer(10);
        testM2.put(1,testInt);
        testInt++;
        System.out.println(testM2.get(1));
        NewTestClass testVar = new NewTestClass(testM2.get(1));
        testM2.put(1,testM2.get(1)+10);
        System.out.println(testVar.testInt);

        int a1=0;
        String test1 = "nyan";
        String test2=test1;
        System.out.println(compare(test1,test2));
        Integer t1=10;
        Integer t2 = t1;
        compare(t1,t2);

    }
    public static boolean compare( String a, String b){
        a+="a";
        return a==b;
    }
    public static boolean compare(Integer a, Integer b){
        System.out.println(a==b);
        Integer c = new Integer(a);
        System.out.println(c==b);
        return false;
    }

    public static void main4(String[] args) {
        Integer c = 10;
        Integer d = c;
//        c=99;
        c++;
        System.out.println(c==d);
    }
    static class Storage1{
        Integer value;
        public Storage1(Integer value) {
            this.value = value;
        }
        public void set(Integer newValue){
            this.value=newValue;
        }
        public String toString(){
            return value+"";
        }
    }

    public static void main5(String[] args) {
        Storage1 storage1= new Storage1(42);
        storage1.set(10);
        Storage2 storage2= new Storage2(storage1);
        Storage1 storage11 = storage2.getStorage();
        System.out.println(storage1==storage2.getStorage());
        System.out.println(storage1==storage11);
        storage1.set(666);
        System.out.println(storage2.getStorage());
    }
    static class Storage2{
        Storage1 storage1;

        public Storage2(Storage1 storage1) {
            this.storage1 = storage1;
        }
        public Storage1 getStorage(){
            return  this.storage1;
        }
    }

    public static void main6(String[] args) {
        Wagon wagon = new Wagon("LN","RN", new ArrayList<>());
        wagon.write("something","LN");
        System.out.println(wagon);
        Register register = new Register("reg",null);
        register.write("somethigElse", "reg");
        System.out.println(register);
        Counter counter = new Counter("counter",null);
        counter.write("1","counter");
        System.out.println(counter);


    }

    public static void main7(String[] args) {
        Queue<Character> queue = new LinkedList<>();
        Iterator<Character> it = queue.iterator();
        queue.add('a');
        queue.add('b');
        queue.add('c');
        queue.add('d');

    }

//    public static void main(String[] args) {
//        Table table = new Table("tab",null,new String[]{"Type","Value"});
//        table.write("Word","thisWillBeIgnored");
//        System.out.println(table);
////        ArrayList<String> testList = new ArrayList<>();
////        System.out.println(testList.get(0));
//
//    }

}
