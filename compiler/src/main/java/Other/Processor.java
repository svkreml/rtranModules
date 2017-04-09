package Other;

import Memories.Memory;

import java.util.Map;
import java.util.Set;

/**
 * Created by Anton on 23.10.2016.
 */

/**
 * Заготовки для единообразного доступа к различным памятям
 */
public class Processor {
    Map<String, Memory> memories;
    public void write(String varName, String value){
        if(memories.containsKey(varName)){
            memories.get(varName).write(value);
            return;
        }else{
            String key=findWagon(varName);
            if(key!=null){
                memories.get(key).write(varName);
                return;
            }
        }
        System.err.println("Нет такой переменной "+varName);
    }
    public String read(String varName){
        if(memories.containsKey(varName)){
            return memories.get(varName).read();
        }else{
            String key=findWagon(varName);
            if(key!=null){
                return memories.get(key).read(varName);
            }
        }
        System.err.println("Нет такой переменной "+varName);
        return null;
    }
    String findWagon(String varName){
        Set<String> keys = memories.keySet();
        for(String key:keys) {
            String[] parts = key.split("/*");
            if (parts.length > 1) {
                for (String part : parts) {
                    if (part.equals(varName)) {
                        return key;
                    }
                }
            }
        }
        return null;
    }
    void clear(String varName){
        if (memories.containsKey(varName)){
            memories.get(varName).clear();
            return;
        }else{
            String wagon=findWagon(varName);
            if (wagon!=null){
                memories.get(wagon).clear();
                return;
            }
        }
        System.err.println("Нет такой переменной "+varName);
    }
}
