package Logic;

import Memories.Memory;
import Other.FileWorker;
import Other.Storage;
import Other.Tape;
import SPO.Processor;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

/**
 * Левая и правая часть - строки, их надо парсить
 * Оператор представлен массивом из 4 символов
 */
public class Statement {
    public static final String FILE = "FileWork";
    public static final String CONSOLE = "ConsoleWork";
    public static final String MEMORY = "FullMemory";
    private TextArea textArea;
    private TextField textField;

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
//    }


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
        System.err.println("Нет такой переменной "+varName);
    }

    public void add(HashMap<String, Memory> memories, String varName, String index, String value) {
        if(findTable(varName, memories)!=null)
            memories.get(varName).addNewStr(index, value);
    }

    public void insert(HashMap<String, Memory> memories, String varName, String index, String value) {
        if(findTable(varName, memories)!=null)
            memories.get(varName).insertNewStr(index, value);
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

    public Statement(String leftArg, Operator operator, String rightArg, TextArea textArea) {
        this.leftArg = leftArg;
        this.operator = operator;
        this.rightArg = rightArg;
        this.textArea = textArea;
    }

    public Statement(String leftArg, Operator operator, String rightArg, TextArea textArea, TextField textField) {
        this.leftArg = leftArg;
        this.operator = operator;
        this.rightArg = rightArg;
        this.textArea = textArea;
        this.textField = textField;
    }

    public Statement(String leftArg, Operator operator, String rightArg) {
        this.leftArg = leftArg;
        this.operator = operator;
        this.rightArg = rightArg;
    }

    public void doStatement(Storage storage, Tape tape){
        if(String.valueOf(this.operator.middle).contains("<")){
            if(Objects.equals(this.leftArg, CONSOLE)) {
                textArea.appendText(rightArg + ": " + read(rightArg,storage.getMemories()) + "\n");
            } else if (Objects.equals(this.leftArg, FILE)) {
                if (Objects.equals(rightArg, MEMORY)) {
                    String buftext = "";
                    Set<String> names = storage.getMemories().keySet();
                    for(String name:names){
                        buftext += storage.getMemories().get(name).toString() + "\n";
                    }
                    FileWorker.appendDump("data/ResultFile.txt", buftext);
                } else {
                    try {
                        FileWorker.appendData("data/ResultFile.txt", rightArg, read(rightArg, storage.getMemories()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if(this.operator.left=='/'){
                    clear(this.leftArg,storage.getMemories());
                }
                if(Objects.equals(this.rightArg, FILE)) {
                    String buffer = FileWorker.searchValueByTarget("data/ResultFile.txt", leftArg);
                    System.out.println(buffer);
                    write(this.leftArg,buffer,storage.getMemories());
                } else if (Objects.equals(this.rightArg, CONSOLE)) {
                    System.out.println("Here");
                    textField.setOnKeyPressed(event -> {
                        if(event.getCode() == KeyCode.ENTER) {
                            System.out.println("Here");
                            write(leftArg, textField.getText(), storage.getMemories());
                            textField.clear();
                        }
                    });
                } else {
                    write(this.leftArg, Processor.count(rightArg, storage.getMemories()), storage.getMemories());
                }
                if(this.operator.right.equals('/')){
                    clear(this.rightArg,storage.getMemories());
                }
            }

        }else if(String.valueOf(this.operator.middle).contains(">")) {
            if (Objects.equals(this.rightArg, CONSOLE)) {
                textArea.appendText(leftArg + ": " + read(leftArg,storage.getMemories()) + "\n");
            } else if (Objects.equals(this.rightArg, FILE)) {
                if (Objects.equals(this.leftArg, MEMORY)) {
                    String buftext = "";
                    Set<String> names = storage.getMemories().keySet();
                    for(String name:names){
                        buftext += storage.getMemories().get(name).toString() + "\n";
                    }
                    System.out.println(buftext);
                    FileWorker.appendDump("data/ResultFile.txt", buftext);
                } else {
                    try {
                        FileWorker.appendData("data/ResultFile.txt", leftArg, read(leftArg, storage.getMemories()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if(this.operator.right.equals('/')){
                    clear(rightArg,storage.getMemories());
                }
                if(Objects.equals(this.leftArg, FILE)) {
                    String buffer = FileWorker.searchValueByTarget("data/ResultFile.txt", this.leftArg);
                    write(this.leftArg,buffer,storage.getMemories());
                } else if (Objects.equals(this.leftArg, CONSOLE)) {
                    System.out.println("Here");
                    final String[] text = {""};
                    this.textField.addEventHandler(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent event) {
                            if(event.getCode() == KeyCode.ENTER) {
                                System.out.println("Here");
                                text[0] = textField.getText();
                                textField.clear();
                            }
                        }
                    });
//                    this.textField.addEventHandler(KeyEvent.KEY_TYPED, event -> {
//                        if(event.getCode() == KeyCode.ENTER) {
//                            System.out.println("Here");
//                            text[0] = this.textField.getText();
//                            this.textField.clear();
//                        }
//                    });
                    write(rightArg, text[0], storage.getMemories());
                } else {
                    write(rightArg, Processor.count(leftArg, storage.getMemories()), storage.getMemories());
                }
                if(this.operator.left.equals('/')){
                    clear(leftArg,storage.getMemories());
                }
            }
        } else if(String.valueOf(this.operator.middle).contains("&=")) {
            searchTrue(storage.getMemories(),leftArg,rightArg);
        } else if(String.valueOf(this.operator.middle).contains("~=")) {
            searchFalse(storage.getMemories(),leftArg,rightArg);
        } else if(String.valueOf(this.operator.middle).contains("^=")) {
            String tablename = null;
            String index = null;
            for(int i = 0; i < leftArg.length(); i++) {
                if(leftArg.charAt(i) == '.') {
                    tablename = leftArg.substring(0,i);
                    index = leftArg.substring(i+1, leftArg.length());
                    break;
                }
            }
            insert(storage.getMemories(),tablename,index, rightArg);
        } else if(String.valueOf(this.operator.middle).contains(".=")) {
            String tablename = null;
            String index = null;
            for(int i = 0; i < leftArg.length(); i++) {
                if(leftArg.charAt(i) == '.') {
                    tablename = leftArg.substring(0,i);
                    index = leftArg.substring(i+1, leftArg.length());
                    break;
                }
            }
            add(storage.getMemories(),tablename,index, rightArg);
        } else if(String.valueOf(this.operator.middle).contains("|-")) {
            textArea.appendText(rightArg + read(leftArg,storage.getMemories()) + "\n");
            System.out.println(rightArg.toString() + read(leftArg,storage.getMemories()));
            if(this.operator.left.equals('/')){
                clear(this.rightArg,storage.getMemories());
            }
        } else if(String.valueOf(this.operator.middle).contains("-|")) {
            if(this.operator.left=='/'){
                clear(this.leftArg,storage.getMemories());
            }
            if(rightArg != null) {
                textArea.appendText(rightArg);
                System.out.print(rightArg);
            }
//            final String[] value = new String[0];
//            textField.addEventHandler(KeyEvent.VK_ENTER, (EventHandler<KeyEvent>) event -> value[0] = textField.getText());

//            addKeyListener(new KeyAdapter() {
//                public void formKeyPressed(java.awt.event.KeyEvent evt) {
//                    if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
//                        value[0] = textField.getText();
//                    }
//                }
//            });
//            write(this.leftArg,Processor.count(value[0],storage.getMemories()),storage.getMemories());
        }
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
