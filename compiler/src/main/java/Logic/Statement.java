package Logic;

import Memories.Memory;
import Other.FileWorker;
import Other.R_machine;
import Other.Storage;
import Other.Tape;
import SPO.Processor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

/**
 * Левая и правая часть - строки, их надо парсить
 * Оператор представлен массивом из 4 символов
 */
public class Statement {
    public static final String FILE = "FILE";
    public static final String CONSOLE = "CONSOLE";
    public static final String MEMORY = "MEMORY";
    public static final String ROW = "ROW";
    public static final String END = "END";
//    private TextArea textArea;
//    private TextField textField;

//    String lefStr, rightStr;
//    Memories.Memory leftMem,rightMem;
//    Operator operator;
//
//    public Logic.Statement(String lefStr, Operator operator, Memories.Memory rightMem) {
//        this.lefStr = lefStr;
//        this.rightMem = rightMem;
//        this.operator = operator;
//        this.rightStr=null;
//        this.leftMem=null;
//    }
//    public Logic.Statement(Memories.Memory leftMem, Operator operator, Memories.Memory rightMem) {
//        this.lefStr = null;
//        this.rightMem = rightMem;
//        this.operator = operator;
//        this.rightStr=null;
//        this.leftMem=leftMem;
//    }
//    public Logic.Statement(Memories.Memory leftMem, Operator operator, String rightStr) {
//        this.lefStr = null;
//        this.rightMem = null;
//        this.operator = operator;
//        this.rightStr=rightStr;
//        this.leftMem=leftMem;
//    } R_machine.window.getTextArea().appendText(


    /**
     *  В языке представлены 3(4) вида операторов (по длине).
     *  Общая структура оператора такова: левая часть, середина, правая часть.
     *  Левая и правая части зарезервированы под оператор очистки памяти("/") и могут отстутствовать.
     *  Основная часть может состоять из 1 или двух символов и присутствует всегда.
     * @return экземпляр класса Оператор
     */
    public static Operator getOperator(String strOp){
        char[] chars = strOp.toCharArray();
        switch (chars.length){
            case(3):
                if(chars[0]=='/'){
                    return new Operator(chars[0],(""+chars[1]+chars[2]).toCharArray());
                }else
                    return new Operator((""+chars[0]+chars[1]).toCharArray(),chars[2]);
            case (4):
                return new Operator(chars[0],(""+chars[1]+chars[2]).toCharArray(),chars[3]);
            case (2):
                return new Operator(chars);
            case(1):
                return new Operator(chars[0]);
            default:
                return null;

        }
    }

//    public static boolean searchTrue(String table) {
//        return true
//    }
    /**
     * У нас есть 2 базовых вида выражений: чтение/запись (<-, ->), для каждого из которых можно задать очистку ячейки памяти.
     * Т.о., у нас есть три основных поля для этого варианта (очистка левого операнда, стрелка, очистка правого операнда)
     * Для второго варианта(=^ вставка, =. добавление,&= поиск по совпадению, &~ поиск по несовпадению) нам нужно только одно поле, поэтому 1 и 3 заполняются null-ами.
     * ОБНОВЛЕНИЕ: надо, по идее, еще учитывать такие операторы как ==, !=, <, <=, >, >=, они, как и &= и &~ становятся в одно поле.
     */
    public static class Operator{
        Character left;
        char[] middle;
        Character right;

        private Operator(char ch1, char[] ch2, char ch3){
            left = ch1;
            middle = ch2;
            right=ch3;
        }
        private Operator(char ch1, char[] ch2){
            left = ch1;
            middle = ch2;
            right=' ';
        }
        private Operator(char[] ch2, char ch3){
            left = ' ';
            middle = ch2;
            right=ch3;

        }
        public Operator(char[] ch2){
            left = ' ';
            middle = ch2;
            right=' ';
        }
        public Operator(char ch2){
            left = ' ';
            middle = new char[ch2];
            right=' ';
        }
        public String toString(){
            String answer="";
            if(left!=null){
                answer+=left;
            }
            if(middle!=null){
                answer+=String.copyValueOf(middle);
            }
            if(right!=null){
                answer+=right;
            }
            return answer;
        }
    }
//    public static class RightSide{
//        String stringRight;
//        Memories.Memory memoryRight;
//        RightSide(String text){
//            stringRight = text;
//            memoryRight=null;
//        }
//        RightSide(Memories.Memory rightSide){
//            stringRight=null;
//            memoryRight=rightSide;
//        }
//    }

    public void write(String varName, String value, HashMap<String, Memory> memories){
        if(memories.containsKey(varName)){
            memories.get(varName).write(value);
            return;
        }else{
//            Pattern p = Pattern.compile("([A-z]|[a-z]|[0-9])+[.]");
//            Matcher m = p.matcher(varName);
//            if(m.matches()) {
//                System.out.println("I am in!!!");
            String tablename = null;
            String index = null;
            for(int i = 0; i < varName.length(); i++) {
                if(varName.charAt(i) == '.') {
                    tablename = varName.substring(0,i);
                    index = varName.substring(i+1, varName.length());
                    break;
                }
            }
            String key = findTable(tablename, memories);
            if(key!=null) {
                memories.get(key).write(value,index);
                return;
            }
//            }
            key=findWagon(varName, memories);
            if(key!=null){
                memories.get(key).write(value,varName);
                return;
            }
        }
        R_machine.window.getTextArea().appendText("Нет такой переменной "+varName+"\n");
        System.err.println("Нет такой переменной "+varName);
    }

    public void add(HashMap<String, Memory> memories, String varName, String index, String value) {
        if(findTable(varName, memories)!=null)
            memories.get(varName).addNewStr(index, value);
    }

    public void add(HashMap<String, Memory> memories, String varName) {
        if(findTable(varName, memories)!=null)
            memories.get(varName).addNewStr();
    }

    public void insert(HashMap<String, Memory> memories, String varName, String index, String value) {
        if(findTable(varName, memories)!=null)
            memories.get(varName).insertNewStr(index, value);
    }

    public void insert(HashMap<String, Memory> memories, String varName) {
        if(findTable(varName, memories)!=null)
            memories.get(varName).insertNewStr();
    }

    public void searchTrue(HashMap<String, Memory> memories, String varName, String value) {
        if(findTable(varName, memories)!=null)
            memories.get(findTable(varName, memories)).searchTrue(value);
    }

    public void searchFalse(HashMap<String, Memory> memories, String varName, String value) {
        if(findTable(varName, memories)!=null)
            memories.get(findTable(varName, memories)).searchFalse(value);
    }

    public String read(String varName, HashMap<String, Memory> memories){
        if(memories.containsKey(varName)){
            return memories.get(varName).read();
        }else{
            String tablename = null;
            String index = null;
            for(int i = 0; i < varName.length(); i++) {
                if(varName.charAt(i) == '.') {
                    tablename = varName.substring(0,i);
                    index = varName.substring(i+1, varName.length());
                    break;
                }
            }
            String key = findTable(tablename, memories);
            if(key!=null) {
                return memories.get(key).read(index);
            }
            key=findWagon(varName,memories);
            if(key!=null){
                return memories.get(key).read(varName);
            }
        }
        return varName;
    }

    public String findWagon(String varName, HashMap<String, Memory> memories){
        Set<String> keys = memories.keySet();
        for(String key:keys) {
            String[] parts = key.split("\\*");
            if (parts.length > 1) {
                for (String part : parts) {
                    if (part.equals(varName)) {
                        return key;
                    }
                }
            } else if (parts.length == 1)
                if(parts[0].equals(varName))
                    return key;
        }
        return null;
    }

    public String findTable(String varName, HashMap<String, Memory> memories) {
        Set<String> keys = memories.keySet();
        for(String key: keys) {
            if(Objects.equals(key, varName))
                return key;
        }
        return null;
    }

    void clear(String varName, HashMap<String, Memory> memories){
        if (memories.containsKey(varName)){
            memories.get(varName).clear();
            return;
        }else{
            String wagon=findWagon(varName, memories);
            if (wagon!=null){
                memories.get(wagon).clear();
                return;
            }
        }
        R_machine.window.getTextArea().appendText("Нет такой переменной "+varName+"\n");
        System.err.println("Нет такой переменной "+varName);
    }

//    public void doStatement(Other.Storage storage, Other.Tape tape){
//       if(this.operator.middle.toString().contains("<")){
//           if(leftMem!=null){
//               if(this.operator.left.toString().contains("/")){
//                   leftMem.clear();
//               }
//               if(rightMem!=null){
//                   leftMem.write(rightMem.read(rightMem.getName()),leftMem.getName());
//               }
//           }else{
//               System.err.println("");
//           }
//       }
//    }

    //
    String leftArg;
    String rightArg;
    Operator operator;

//    public Statement(String leftArg, Operator operator, String rightArg, TextArea textArea) {
//        this.leftArg = leftArg;
//        this.operator = operator;
//        this.rightArg = rightArg;
//        this.textArea = textArea;
//    }

//    public Statement(String leftArg, Operator operator, String rightArg, TextArea textArea, TextField textField) {
//        this.leftArg = leftArg;
//        this.operator = operator;
//        this.rightArg = rightArg;
//        this.textArea = textArea;
//        this.textField = textField;
//    }

    public Statement(String leftArg, Operator operator, String rightArg) {
        this.leftArg = leftArg;
        this.operator = operator;
        this.rightArg = rightArg;
    }

    public synchronized boolean doStatement(Storage storage, Tape tape) throws InterruptedException {
        if(String.valueOf(this.operator.middle).contains("*")){
            return false;
        }
        if(String.valueOf(this.operator.middle).contains("<")){
            if(Objects.equals(this.leftArg, CONSOLE)) {
                if(Objects.equals(this.rightArg, MEMORY)) {
                    String buftext = "";
                    Set<String> names = storage.getMemories().keySet();
                    for(String name:names){
                        buftext += storage.getMemories().get(name).toString() + "\n";
                    }
                    R_machine.window.getTextArea().appendText(buftext + "\n");
                } else {
                    if(Objects.equals(storage.getMemories().get(rightArg).getType(), "tab") || Objects.equals(storage.getMemories().get(rightArg).getType(), "wag")) {
                        R_machine.window.getTextArea().appendText(storage.getMemories().get(rightArg).toString()+ "\n");
                    } else {
                        R_machine.window.getTextArea().appendText(rightArg + ": " + read(rightArg, storage.getMemories()) + "\n");
                    }
//                    R_machine.window.getTextArea().appendText(rightArg + ": " + read(rightArg, storage.getMemories()) + "\n");
                }
            } else if (Objects.equals(this.leftArg, FILE)) {
                if (Objects.equals(this.rightArg, MEMORY)) {
                    String buftext = "";
                    Set<String> names = storage.getMemories().keySet();
                    for(String name:names){
                        buftext += storage.getMemories().get(name).toString() + "\n";
                    }
                    FileWorker.appendDump(R_machine.file, buftext);
                } else {
                    try {
                        FileWorker.appendData(R_machine.file, rightArg, read(rightArg, storage.getMemories()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if(this.operator.left=='/'){
                    clear(this.leftArg,storage.getMemories());
                }
                if(Objects.equals(this.rightArg, FILE)) {
                    String buffer = FileWorker.searchValueByTarget(R_machine.file, leftArg);
//                    R_machine.window.getTextArea().appendText(buffer+"\n");
                    System.out.println(buffer);
                    write(this.leftArg,buffer,storage.getMemories());
                } else if (Objects.equals(this.rightArg, CONSOLE)) {
                    R_machine.window.getInputButton().setOnMouseClicked(event -> {
                        System.out.println("Here");
                        write(leftArg, R_machine.window.getTextField().getText(), storage.getMemories());
                        R_machine.window.getTextField().clear();
                    });

                } else {
                    write(this.leftArg, Processor.count(leftArg, rightArg, storage.getMemories()), storage.getMemories());
                }
                if(this.operator.right.equals('/')){
                    clear(this.rightArg,storage.getMemories());
                }
            }
            return false;
        }else if(String.valueOf(this.operator.middle).contains(">")) {
            if (Objects.equals(this.rightArg, CONSOLE)) {
                if(Objects.equals(this.leftArg, MEMORY)) {
                    String buftext = "";
                    Set<String> names = storage.getMemories().keySet();
                    for(String name:names){
                        buftext += storage.getMemories().get(name).toString() + "\n";
                    }
                    R_machine.window.getTextArea().appendText(buftext + "\n");
                } else {
                    if(Objects.equals(storage.getMemories().get(leftArg).getType(), "tab") || Objects.equals(storage.getMemories().get(leftArg).getType(), "wag")) {
                        R_machine.window.getTextArea().appendText(storage.getMemories().get(leftArg).toString()+ "\n");
                    } else {
                        R_machine.window.getTextArea().appendText(leftArg + ": " + read(leftArg, storage.getMemories()) + "\n");
                    }
                }
            } else if (Objects.equals(this.rightArg, FILE)) {
                if (Objects.equals(this.leftArg, MEMORY)) {
                    String buftext = "";
                    Set<String> names = storage.getMemories().keySet();
                    for(String name:names){
                        buftext += storage.getMemories().get(name).toString() + "\n";
                    }
                    System.out.println(buftext);
                    FileWorker.appendDump(R_machine.file, buftext);
                } else {
                    try {
                        FileWorker.appendData(R_machine.file, leftArg, read(leftArg, storage.getMemories()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if(this.operator.right.equals('/')){
                    clear(rightArg,storage.getMemories());
                }
                if(Objects.equals(this.leftArg, FILE)) {
                    String buffer = FileWorker.searchValueByTarget(R_machine.file, this.leftArg);
                    write(this.leftArg,buffer,storage.getMemories());
                } else if (Objects.equals(this.leftArg, CONSOLE)) {
                    R_machine.window.getInputButton().setOnMouseClicked(event -> {
                        System.out.println("Here");
                        write(rightArg, R_machine.window.getTextField().getText(), storage.getMemories());
                        R_machine.window.getTextField().clear();
                        Thread.currentThread().notify();
                    });
                    Thread.currentThread().wait();
                } else {
                    write(rightArg, Processor.count(rightArg, leftArg, storage.getMemories()), storage.getMemories());
                }
                if(this.operator.left.equals('/')){
                    clear(leftArg,storage.getMemories());
                }
            }
            return false;
        } else if(String.valueOf(this.operator.middle).contains("&=")) {
            searchTrue(storage.getMemories(),leftArg,rightArg);
            return false;
        } else if(String.valueOf(this.operator.middle).contains("~=")) {
            searchFalse(storage.getMemories(),leftArg,rightArg);
            return false;
        } else if(String.valueOf(this.operator.middle).contains("^=") && String.valueOf(this.rightArg).equals(ROW)) {
//            String tablename = null;
//            String index = null;
//            for(int i = 0; i < leftArg.length(); i++) {
//                if(leftArg.charAt(i) == '.') {
//                    tablename = leftArg.substring(0,i);
//                    index = leftArg.substring(i+1, leftArg.length());
//                    break;
//                }
//            }
//            insert(storage.getMemories(),tablename,index, rightArg);
            String tablename = leftArg;
            insert(storage.getMemories(),tablename);
            return false;
        } else if(String.valueOf(this.operator.middle).contains(".=") && String.valueOf(this.rightArg).equals(ROW)) {
//            String tablename = null;
//            String index = null;
//            for(int i = 0; i < leftArg.length(); i++) {
//                if(leftArg.charAt(i) == '.') {
//                    tablename = leftArg.substring(0,i);
//                    index = leftArg.substring(i+1, leftArg.length());
//                    break;
//                }
//            }
//            add(storage.getMemories(),tablename,index, rightArg);
            String tablename = leftArg;
            add(storage.getMemories(),tablename);
            return false;
        } else if(String.valueOf(this.operator).contains(END)) {
            return true;
        }
//        else if(String.valueOf(this.operator.middle).contains(":=") && String.valueOf(this.rightArg).equals(COLUMN)) {
//            String tablename = leftArg;
//            add(storage.getMemories(),tablename);
//        } else if(String.valueOf(this.operator.middle).contains(":") && String.valueOf(this.rightArg).equals(COLUMN)) {
//            String tablename = leftArg;
//            insert(storage.getMemories(),tablename);
//        }
        return false;
    }
    //    }
    public String toString(){
        return leftArg.toString()+operator.toString()+rightArg.toString();
    }
//    public static void main(String[] args) {
//        Logic.Statement statement=new Logic.Statement(new Memories.Wagon("ЛВ","ПВ",new ArrayList<String>(Arrays.asList("first,Second"))),new Operator("<-".toCharArray()),new Memories.Register(""));
//
//    }
}
