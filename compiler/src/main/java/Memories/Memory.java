
package Memories;
/**
 * Created by Anton on 16.10.2016.
 */
public interface Memory {
    String type = "memory";
    String read(String... args);
    boolean write(String... args);
    boolean clear();
    String getName();
    int size();


    //    public boolean write(String value) {
//        this.table.get(strnumber).add(colnumber, value);
//        return (this.table.get(strnumber).get(colnumber) != null);
//    }
    boolean addNewStr(String index, String value);

    boolean insertNewStr(String index, String value);

    boolean searchTrue(String value);

    boolean searchFalse(String value);
}
