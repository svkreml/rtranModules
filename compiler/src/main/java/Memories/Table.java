package Memories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by master on 24.10.2016.
 */
public class Table implements Memory {
//TODO: в write() передаются аргументы в неправильном порядке: сначала должно быть значение, потом имя
    private String tname;
    ArrayList<HashMap<String,String>> table = new ArrayList<>();
//    ArrayList<String> colnames = new ArrayList<>();
    HashMap<String,String> colnames = new HashMap<>();
    private int strnumber=0;
    private int colnumber=0;
//    private int columns;

    public Table(String tname, ArrayList<HashMap<String,String>> table, HashMap<String,String> colnames) {
        this.tname = tname;
        if(table!=null) {
            this.table = table;
        }
        this.colnames = colnames;
    }

    public Table(String tname, HashMap<String,String> colnames) {
        this.tname = tname;
        this.colnames = colnames;
        for(int count = 0; count < 1; count++) {
            table.add(count, colnames);
//            table.get(count).add("");
        }
    }

    public Table(String tname/*, ArrayList<HashMap<String,String>> table,String[] colnames*/) {
        this.tname = tname;
//        if(table!=null) {
//            this.table = table;
//        }
        HashMap<String,String> list = new HashMap<>();
//        for()
//        Collections.addAll(list, colnames);
        this.colnames = list;
    }

    public String getName() {
        return tname;
    }

    public int getColumns() {
        return colnames.size();
    }

    public int getStrNumber() {
        return strnumber;
    }

    public int getColNumber() {
        return colnumber;
    }

    HashMap<String, String> getString(int num) {
        return this.table.get(num);
    }

    public int size() { return this.table.size(); }

    //TODO: для работы с памятью нам необходимо чтение по столбцу. Иначе говоря, обращаться по имени столбца к текущей строке
    public String read(String... args) {
        return this.table.get(strnumber).get(args[0]);
    }

    @Override
    public boolean write(String... args) {
        if(this.table.size()==0){
            this.table.add(new HashMap<String, String>());
        }
        this.table.get(strnumber).put(args[1], args[0]);

        return (this.table.get(strnumber).get(args[1]) != null);
    }

    @Override
    public boolean clear() {
        table.clear();
        return table.isEmpty();
    }

    //    public boolean write(String value) {
//        this.table.get(strnumber).add(colnumber, value);
//        return (this.table.get(strnumber).get(colnumber) != null);
//    }
    @Override
    public boolean addNewStr(String index, String value) {
        this.strnumber++;
        this.table.add(strnumber,new HashMap<String,String>());
        this.table.get(strnumber).put(index, value);
        return (this.table.get(strnumber).get(index) != null);
    }
    @Override
    public boolean insertNewStr(String index, String value) {
        if(strnumber == 0) {
            this.table.add(0,new HashMap<String,String>());
            this.table.get(0).put(index, value);
            return (this.table.get(0).get(index) != null);
        }
        this.strnumber--;
        this.table.add(strnumber,colnames);
        this.table.get(strnumber).put(index, value);
        return (this.table.get(strnumber).get(index) != null);
    }
    @Override
    public boolean searchTrue(String value) {
//        this.write();

        int i, j = 0;
        for (i = 0; i < this.table.size(); i++)
            for (j = 0; j < this.table.get(i).size(); j++)
                if (Objects.equals(this.table.get(i).get(j), value)) {
                    this.strnumber = i;
                    this.colnumber = j;
                    return true;
                }
        return (i == (this.table.size() - 1) && j == (this.table.get(i).size()-1) && this.strnumber != i && this.colnumber != j);
    }

    @Override
    public boolean searchFalse(String value) {
        int i, j = 0;
        for (i = 0; i < this.table.size(); i++)
            for (j = 0; j < this.table.get(i).size(); j++)
                if (Objects.equals(this.table.get(i).get(j), value)) {
                    this.strnumber = i;
                    this.colnumber = j;
                    break;
                }
        return i == (this.table.size() - 1) && j == this.table.get(i).size() && this.strnumber != i && this.colnumber != j;
    }

    public String toString(){
        String answer=null;
        try{
            answer="table: "+tname+"; value: "+table.toString();
        }catch (NullPointerException e){
            answer= "table: "+tname+"; value: null";
        }
        return answer;
    }

}
