package Memories;

/**
 * Created by master on 24.10.2016.
 */
public class Register implements Memory {
    public String getname() {
        return rname;
    }

    boolean outReg= false;
    private String rname;
    private String value;

    public Register(String rname, String value, boolean outReg) {
        this.rname = rname;
        this.value = value;
        this.outReg = outReg;
    }

    public String getName() {
        return rname;
    }

    public int size() { return this.value.length(); }

    @Override
    public boolean addNewStr(String index, String value) {
        return false;
    }

    @Override
    public boolean addNewStr() {
        return false;
    }

    @Override
    public boolean insertNewStr(String index, String value) {
        return false;
    }

    @Override
    public boolean insertNewStr() {
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

    @Override
    public String getType() {
        return "reg"+this.outReg;
    }

    boolean write(String register) {
        if(this.value!=null) {
            this.value += register;
        }else{
            this.value=register;
        }
        return true;
    }

    public String read(String...args) {
        return this.value;
    }

    @Override
    public boolean write(String... args) {
        return this.write(args[0]);
    }

    public boolean clear() {
        this.value = null;
        return true;
    }
    public String toString(){
        return "register: "+rname+"; value: "+value;
    }
}
