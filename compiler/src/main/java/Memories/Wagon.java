package Memories;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by master on 24.10.2016.
 */
public class Wagon implements Memory {
    private String leftname;
    private String rightname;
    private String wname;
    ArrayList<String> value = new ArrayList<String>();

    public Wagon(String leftname, String rightname, ArrayList<String> value) {
        this.leftname = leftname;
        this.rightname = rightname;
        this.wname = leftname + "*" + rightname;
        if(value!=null) {
            this.value = value;
        }
    }

    public Wagon(String leftname, String rightname) {
        this.leftname = leftname;
        this.rightname = rightname;
        this.wname = leftname + "*" + rightname;
    }

    public String getName() {
        return this.wname;
    }

    String getLeftName() {
        return this.leftname;
    }

    String getRightName() {
        return this.rightname;
    }

    public int size() {return this.value.size();}

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

    boolean write(String value, String name) {
        if(Objects.equals(name, this.leftname)) {
            this.value.add(0, value);
        } else if (Objects.equals(name, this.rightname)) {
            this.value.add(value);
        } else System.out.println("Error. Wrong name. Operation can not be continued."); // Добавить в Operation, отсюда убрать.
        return (this.value.get(0) != null || this.value.get(this.value.size()-1) != null);
    }

    boolean write(String value) {
        this.value.add(0, value);
        this.value.add(value);
        return (this.value.get(0) != null && this.value.get(this.value.size()-1) != null);
    }
    @Override
    public String read(String... args) {
        String name = args[0];
        String value = null;
        if (Objects.equals(name, this.leftname)) {
            value = this.value.get(0);
            this.value.remove(0);
        } else if (Objects.equals(name, this.rightname)) {
            value = this.value.get(this.value.size()-1);
            this.value.remove(this.value.size()-1);
        } else System.out.println("Error. Wrong name. Operation can not be continued."); // Добавить в Operation, отсюда убрать.
        return value;
    }


    @Override
    public boolean write(String... args) {

        switch (args.length){
            case(1):{
                return this.write(args[0]);
            }
            case(2):{
                return this.write(args[0],args[1]);
            }
            default:
                System.err.println("WTF? Invalid count of args");
                return false;
        }

    }

    public boolean clear() {
        this.value.clear();
        return (this.value == null);
    }
    public String toString() {
        String answer = null;
        try {
            answer = "wagon: " + leftname + "*" + rightname + "; value:" + value.toString();
        } catch (NullPointerException e) {
            answer = "wagon: " + leftname + "*" + rightname + "; value: null";
        }
        return answer;
    }
}
