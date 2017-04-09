 /**
 * Created by Admin on 28.10.2016.
 */
 package Logic;
import Memories.*;
import Other.*;

import java.util.Objects;


// Пока хз как без костылей реализовать множество вариантов в правой части условия,
// буду благодарен за подсказки. Из вариантов через свтитч по enum вариантов или заполнение двух неиспользуемых
// полей null-ами.
// ОБНОВЛЕНИЕ: предлагаю перегрузку функций. В правой части могут быть 3 типа данных: цепочка термов, память, синтерм
// (который по сути, подгружаемый массив термов) см. учебник стр. 45. Однако кое-где может и не быть правой части, так
// что задача, с одной стороны облегчается, с другой усложняется (множество вариантов проверки условия)

public class Condition {
    Alphabet alphabet;
    String text;
    Boolean aBoolean;
    Memory memory;
    Memory memoryleft;
    Memory memoryright;
    String oper;

    public boolean checkABoolean(Tape tape) {
        if(this.memoryright != null && this.oper != null && this.memoryleft != null) return compare(this.memoryleft, this.oper, this.memoryright);
        if(this.alphabet != null) return compare(this.alphabet, tape);
        if(this.text != null) return compare(this.text, tape);
        if(this.memory != null) return compare(this.memory, tape);

        return false;
    }

    public Condition(String text) {
        this.text = text;
    }

    public Condition(Memory memory) {
        this.memory = memory;
    }

    public Condition(Alphabet alphabet) {
        this.alphabet = alphabet;
    }

    public Condition(Memory memoryleft, String oper, Memory memoryright) {
        this.memoryleft = memoryleft;
        this.oper = oper;
        this.memoryright = memoryright;
    }

//  1. Объединяем первый два пункта условий из книги в один, так как первый является подмножеством второго: проверка
//     цепочки термов на соответствие с вимволами входной ленты.
    private boolean compare(String str, Tape tape) {
        if(str.equals("*")){
            aBoolean = true;
            return aBoolean;
        }
        if(str.length()>tape.size()-tape.counter){
            return false;
        }
        for(int i = 0; i < str.length(); i++) {
            if (!(str.charAt(i) == tape.read())){
                aBoolean = false;
                return aBoolean;
            }
        }
        aBoolean = true;
        return aBoolean;
    }

//  2. 3 пункт: сравниваем ленту ввода с памятью.
    private boolean compare(Memory memory, Tape tape) {
        for(int i = 0; i < memory.read().length(); i++) {
            if (!(memory.read().charAt(i) == tape.read())) {
                aBoolean = false;
                return aBoolean;
            }
        }
        aBoolean = true;
        return aBoolean;
    }

//  3. Предикат синтерм: сверка текущего символа входной ленты с любым символом данного алфавита
    private boolean compare(Alphabet alphabet, Tape tape) {
        for (int i = 0; i < alphabet.read().length; i++) {
            if (alphabet.read()[i] == tape.readCurrent()) {
                aBoolean = true;
                return aBoolean;
            }
        }
        aBoolean = false;
        return aBoolean;
    }

//  4. Предикат образец: по символу '!' происходит поиск элемента не схожего с теми, что описаны в операнде
//    public class Expression()
    private boolean compare(char ch, Memory memory, Tape tape) {
        if(ch == '!') {
            String str;
            int count = 0;
            for(int i = 0; i < memory.size(); i++) {
                if(memory.read().charAt(i) != tape.read()){
                    aBoolean = false;
                    return aBoolean;
                }
            }
            aBoolean = true;
            return aBoolean;
        } else if (ch == '?') {
            String buf = null;
            aBoolean = false;
            while (tape.readCurrent() != null) {
                for (int i = 0; i < tape.size(); i++) {
                    if(memory.read().charAt(0) == tape.readCurrent()) {
                        break;
                    } else {
                        tape.read();
                    }
                }
                for(int j = 0; j < memory.size(); j++) {
                    if (memory.read().charAt(j) == tape.readCurrent()) {
                        buf += tape.read();
                    }
                    else break;
                }
                if(Objects.equals(buf, memory.read())) aBoolean = true;
            }
            return aBoolean;
        }
        System.out.println("Error. Wrong symbol-sign.");
        return false;
    }

    private boolean compare(char ch, Alphabet alphabet, Tape tape) {
        if(ch == '!') {
            int i = 0;
            aBoolean = true;
            while(aBoolean) {
                char cursymbol = tape.read();
                while (alphabet.read()[i] != cursymbol && i < alphabet.read().length) {
                    i++;
                }
                if (i >= alphabet.read().length) aBoolean = false;
                aBoolean = true;
            }
            return !aBoolean;
        } else if (ch == '?') {
            int i = 0;
            aBoolean = true;
            while(aBoolean) {
                char cursymbol = tape.read();
                while (alphabet.read()[i] != cursymbol && i < alphabet.read().length) {
                    i++;
                }
                if (i >= alphabet.read().length) aBoolean = true;
                aBoolean = false;
            }
            aBoolean = true;
            while(aBoolean) {
                char cursymbol = tape.read();
                for(int k = 0; k < alphabet.read().length; k++) {
                    if(alphabet.read()[k] == cursymbol) {
                        break;
                    }
                }
                if(i >= alphabet.read().length) aBoolean = false;
                else aBoolean = true;
            }
            return !aBoolean;
        }
        System.out.println("Error. Wrong symbol-sign.");
        return (aBoolean = false);
    }

    private boolean compare(Memory memoryleft, String oper, Memory memoryright) {
        switch (oper) {
            case "==":
                aBoolean = (Objects.equals(memoryleft.read(), memoryright.read()));
                break;
            case "!=":
                aBoolean = (!Objects.equals(memoryleft.read(), memoryright.read()));
                break;
            case  "<":
                aBoolean = (new Integer(memoryleft.read()) < new Integer(memoryright.read()));
                break;
            case "<=":
                aBoolean = (new Integer(memoryleft.read()) <= new Integer(memoryright.read()));
                break;
            case  ">":
                aBoolean = (new Integer(memoryleft.read()) > new Integer(memoryright.read()));
                break;
            case ">=":
                aBoolean = (new Integer(memoryleft.read()) >= new Integer(memoryright.read()));
                break;
        }
        return aBoolean;
    }

    private boolean compare(String strleft, String oper, String strright) {
        switch (oper) {
            case "==":
                aBoolean = (Objects.equals(strleft, strright));
                break;
            case "!=":
                aBoolean = (!Objects.equals(strleft, strright));
                break;
            case  "<":
                aBoolean = (new Integer(strleft) < new Integer(strright));
                break;
            case "<=":
                aBoolean = (new Integer(strleft) <= new Integer(strright));
                break;
            case  ">":
                aBoolean = (new Integer(strleft) > new Integer(strright));
                break;
            case ">=":
                aBoolean = (new Integer(strleft) >= new Integer(strright));
                break;
        }
        return aBoolean;
    }

    private boolean compare(Memory memoryleft, String oper, String strright) {
        switch (oper) {
            case "==":
                aBoolean = (Objects.equals(memoryleft.read(), strright));
                break;
            case "!=":
                aBoolean = (!Objects.equals(memoryleft.read(), strright));
                break;
            case  "<":
                aBoolean = (new Integer(memoryleft.read()) < new Integer(strright));
                break;
            case "<=":
                aBoolean = (new Integer(memoryleft.read()) <= new Integer(strright));
                break;
            case  ">":
                aBoolean = (new Integer(memoryleft.read()) > new Integer(strright));
                break;
            case ">=":
                aBoolean = (new Integer(memoryleft.read()) >= new Integer(strright));
                break;
        }
        return aBoolean;
    }

    private boolean compare(String strleft, String oper, Memory memoryright) {
        switch (oper) {
            case "==":
                aBoolean = (Objects.equals(strleft, memoryright.read()));
                break;
            case "!=":
                aBoolean = (!Objects.equals(strleft, memoryright.read()));
                break;
            case  "<":
                aBoolean = (new Integer(strleft) < new Integer(memoryright.read()));
                break;
            case "<=":
                aBoolean = (new Integer(strleft) <= new Integer(memoryright.read()));
                break;
            case  ">":
                aBoolean = (new Integer(strleft) > new Integer(memoryright.read()));
                break;
            case ">=":
                aBoolean = (new Integer(strleft) >= new Integer(memoryright.read()));
                break;
        }
        return aBoolean;
    }

    public String toString(){
        if(this.memoryright != null && this.oper != null && this.memoryleft != null) return (this.memoryleft+ this.oper+ this.memoryright);
        if(this.alphabet != null) return this.alphabet.getFullname();
        if(this.text != null) return this.text;
        if(this.memory != null) return this.memory.toString();
        return null;
    }



}
