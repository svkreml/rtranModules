package Other;

import Logic.Arm;
import Memories.Alphabet;
import Memories.Memory;

import java.util.HashMap;
import java.util.Set;

/**
 * То что мы получаем от редактора: алгоритм программы и список(только список!) всех памятей.
 * Предварительно заполнить память пока нельзя!
 */
public class Storage {
    public HashMap<String, Arm> getArms() {
        return arms;
    }

    public HashMap<String, Memory> getMemories() {
        return memories;
    }

    public HashMap<String, Alphabet> getAlphabets() {
        return alphabets;
    }


    /**
     * Ключ - значение номера/названия вершины
     */
    HashMap<String, Arm> arms;
    /**
     * Ключ - название памяти(для вагона - полное название, например 'ЛВ*ПВ'
     */
    HashMap<String, Memory> memories;
    HashMap<String, Alphabet> alphabets;

    public Storage(HashMap<String, Arm> arms, HashMap<String, Memory> memories, HashMap<String, Alphabet> alphabets) {
        this.arms = arms;
        this.memories = memories;
        this.alphabets = alphabets;
        //this.tape = tape;
    }
    public void printStorage(){
        System.out.println("----STORAGE----");
        //System.out.println("TAPE: "+tape);
        System.out.println("ALPHABETS: ");
        Set<String> alphNames=alphabets.keySet();
        for(String name:alphNames){
            System.out.println(alphabets.get(name));
        }
        System.out.println("MEMORIES: ");
        Set<String> memoriesNames=memories.keySet();
        for(String name:memoriesNames){
            System.out.println(memories.get(name));
        }
        System.out.println("ALGORITHM: ");
        Set<String> armNames= arms.keySet();
        for(String name:armNames){
            arms.get(name).printArm();
        }
    }
}
