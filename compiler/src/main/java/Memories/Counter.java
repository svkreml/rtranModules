
package Memories;
/**
 * Created by master on 24.10.2016.
 */
public class Counter implements Memory {
    private String cname;
    private Integer counter;

    public Counter(String cname, Integer value) {
        this.cname = cname;
        this.counter = value;
    }

    public String getName() {
        return cname;
    }
    public int size() { return this.size(); }

    @Override
    public boolean addNewStr(String index, String value) {
        return false;
    }

    @Override
    public boolean insertNewStr(String index, String value) {
        return false;
    }
    @Override
    public boolean searchTrue(String value) {
        return false;
    }

    @Override
    public boolean searchFalse(String value) {
        return false;
    }

    boolean write(int counter) {
        this.counter = counter;
        return (this.counter != 0);
    }

    public String read(String...args) {
        return this.counter+"";
    }

    @Override
    public boolean write(String... args) {
        return write(Integer.parseInt(args[0]));
    }

    public boolean clear() {
        this.counter = 0;
        return true;
    }

    public String toString(){
        return "counter: "+cname+"; value: "+counter;
    }
}
