package Other;

/**
 * Created by master on 14.11.2016.
 */
public class AllStorage {
    Storage storage;
    Tape tape;

    public AllStorage(Storage storage, Tape tape) {
        this.storage = storage;
        this.tape = tape;
    }
    public Storage getStorage(){
        return storage;
    }
    public Tape getTape(){
        return tape;
    }
}
