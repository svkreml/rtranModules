package Other;

import java.util.LinkedList;

/**
 * Created by Admin on 28.10.2016.
 */

/**
 * Класс ленты, реализован на основе очереди и итератора для сохранения возможности вывода в любой момент всей ленты.
 * Чтение из ленты осуществляется методом read;
 */
public class Tape {
    char[] tape;
    public int counter;
//    Queue<Character> tape=new LinkedList<>();
    public Tape(String tapeValue){
        char[] charValue = tapeValue.toCharArray();
        LinkedList<Character> charsList = new LinkedList<>();
        for(Character ch:charValue){
            charsList.add(ch);
        }
        this.tape=tapeValue.toCharArray();
        this.counter=0;
    }
//    Iterator<Character> it;
    public Tape(char[] tape) {
        this.tape = tape;
        this.counter=0;
    }



    public Character read(){
        return tape[this.counter++]; // ОБНОВЛЕНИЕ: Я же правильно понимаю, что указатель тут тоже перемещается?
    }
    public Character readCurrent(){
        return tape[this.counter];
    }
    public int size() {
        return tape.length-counter;
    }
    public String toString(){
        String answer="";
        try {
            for (Character ch : tape) {
                answer += ch;
            }
            return answer;
        }catch (NullPointerException e){
            return null;
        }
    }
//    public static void main(String[] args) {
//        Other.Tape tape = new Other.Tape();
//        System.out.println(tape.read());
//        System.out.println(tape.read());
//    }
}
