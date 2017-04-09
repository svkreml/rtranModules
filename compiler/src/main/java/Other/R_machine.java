package Other;

import Logic.Arm;
import Logic.ArmLine;
import Logic.Condition;
import Logic.Statement;
import Memories.*;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Anton on 16.10.2016.
 * здесь будет непосредственный обработчик
 * или не здесь
 * ОБНОВЛЕНИЕ: по замыслу здесь
 */
public class R_machine extends Thread implements Runnable{
    AllStorage allStorage;
    Storage storage;

    public Tape getTape() {
        return tape;
    }

    Tape tape;
    int i=0;
    private Stage primaryStage;
    private Pane rootLayout;
    private TextArea textArea;
    private WorkExchange workExchange=null;
    private volatile StopType stopType = null;
    public volatile StringBox currentNumber = new StringBox(null);
    private boolean end = false;

    public R_machine(AllStorage allStorage, StringBox currentNumber, Condition currentCondition, Statement currenntStatement, boolean end) {
        this.allStorage=allStorage;
        this.storage=allStorage.getStorage();
        this.tape=allStorage.getTape();
        this.currentNumber = currentNumber;
        this.currentCondition = currentCondition;
        this.currenntStatement = currenntStatement;
        this.end = end;
    }

    public R_machine(AllStorage allStorage, WorkExchange workExchange) {
        this.allStorage=allStorage;
        this.storage=allStorage.getStorage();
        this.tape=allStorage.getTape();
        this.workExchange = workExchange;
    }

    public synchronized void setCurrentCondition(Condition currentCondition) {
        this.currentCondition = currentCondition;
    }

    private volatile Condition currentCondition = null;

    public synchronized void setCurrenntStatement(Statement currenntStatement) {
        this.currenntStatement = currenntStatement;
    }

    public synchronized void setCurrentNumber(String currentNumber) {
        this.currentNumber.setValue(currentNumber);
    }

    private volatile Statement currenntStatement = null;

    public synchronized void setStopType(StopType stopType) {
        this.stopType = stopType;
    }
    public synchronized StopType getStopType() {
        return stopType;
    }

    public synchronized Condition getCurrentCondition() {
        return currentCondition;
    }

    public synchronized Statement getCurrenntStatement() {
        return currenntStatement;
    }

    public synchronized String getCurrentNumber() {
        return this.currentNumber.getValue();
    }
    public R_machine(AllStorage allStorage) {
        this.allStorage=allStorage;
        this.storage=allStorage.getStorage();
        this.tape=allStorage.getTape();
    }


    public void checkArm(Arm curretArm) {
        ArrayList<ArmLine> lines = curretArm.getLines();
        for(ArmLine line:lines){
            if(line.compare(tape)){

            }
        }
    }
    public boolean endOfAlgorythm(){
        return this.end;
    }
    public void continueWork() throws InterruptedException {
        while(true) {
            workExchange.sendResult(new R_machine(this.allStorage, this.currentNumber, this.currentCondition, this.currenntStatement, this.end));
            this.stopType = this.workExchange.getWork();
            return;
        }

    }
    public  void mainWork() throws InterruptedException {
        loop:while (true) {
            this.currenntStatement = null;
            this.currentCondition = null;
            if (this.end) {
                continueWork();
            }
            String endNumber = null;
            Arm firstArm = null;
            HashMap<String, Arm> arms = this.allStorage.getStorage().arms;
            if (currentNumber.getValue() == null) {
                if (arms.containsKey("0")) {
                    this.setCurrentNumber("0");
                } else {
                    System.err.println("Невозможно обработать алгоритм без нулевой вершины");
                    System.exit(-1);
                }
            }
            if (stopType == StopType.NODE) {
                try {
                    continueWork();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            firstArm = arms.get(currentNumber.getValue());
            ArrayList<ArmLine> lines = firstArm.getLines();//обход ребер одной вершины ( в данном случае первой, т.е. с номером "0"
            for (ArmLine line : lines) {
                this.currentCondition = line.getCondition();
                if (stopType == StopType.CONDITION) {
                    try {
                        continueWork();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (line.compare(this.tape)) { //Если условие в данном ребре истинно...
                    endNumber = line.getEndArmNumber();
                    for (Statement statement : line.getStatements()) { //выполнение всех выражений (операций) , перечисленных в ребре
                        this.setCurrenntStatement(statement);
                        if (stopType == StopType.STATEMENT) {
                            try {
                                continueWork();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        statement.doStatement(storage, tape);
                    }
                    this.currenntStatement = null;
                    this.currentCondition = null;
                    char tapeCurrent = this.tape.readCurrent();
                    if (tapeCurrent == '#') {
                        Set<String> names = this.allStorage.storage.getMemories().keySet();
                        for (String name : names) {
                            System.out.println(this.allStorage.storage.getMemories().get(name));
                        }
                        this.end = true;
                        try {
                            continueWork();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        this.setCurrentNumber(endNumber);
                    }
                    continue loop; //Если программа продолжается ( т.е. не был указан конец ("#"), переход к обработке узла с номером, указанным в ребре.
//
                }
            }
        }
    }

    /**
     * Основной метод запуска отладчика
     */
    public synchronized void run(){
        while(true){
            try{
                this.stopType=workExchange.getWork();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(this.stopType==null){
                continue;
            }else{
                break;
            }
        }
        try {
            mainWork();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * Метод для запуска РМашины без отладчика (простой прогон)
     * @throws InterruptedException
     */
    public synchronized void simpleRun() throws InterruptedException {
        loop:while (true) {
            this.currenntStatement = null;
            this.currentCondition = null;
            String endNumber = null;
            Arm firstArm = null;
            HashMap<String, Arm> arms = this.allStorage.getStorage().arms;
            if (currentNumber.getValue() == null) {
                if (arms.containsKey("0")) {
                    this.setCurrentNumber("0");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("Невозможно обработать алгоритм без нулевой вершины");
                    System.exit(-1);
                }
            }
            firstArm = arms.get(currentNumber.getValue());
            ArrayList<ArmLine> lines = firstArm.getLines();//обход ребер одной вершины ( в данном случае первой, т.е. с номером "0"
            for (ArmLine line : lines) {
                this.currentCondition = line.getCondition();
                if (line.compare(this.tape)) { //Если условие в данном ребре истинно...
                    endNumber = line.getEndArmNumber();
                    for (Statement statement : line.getStatements()) { //выполнение всех выражений (операций) , перечисленных в ребре
                        this.setCurrenntStatement(statement);
                        statement.doStatement(storage, tape);
                    }
                    this.currenntStatement = null;
                    this.currentCondition = null;
                    char tapeCurrent = this.tape.readCurrent();
                    if (tapeCurrent == '#') {
                        Set<String> names = this.allStorage.storage.getMemories().keySet();
                        for (String name : names) {
                            System.out.println(this.allStorage.storage.getMemories().get(name));
                        }
                        this.end = true;
                    } else {
                        this.setCurrentNumber(endNumber);
                    }
                    if(end){
                        return;
                    }else {
                        continue loop;
                    }
                }
            }
        }
    }
    public synchronized void run(TextArea textArea){
        this.textArea = textArea;
        HashMap<String, Arm> arms = this.allStorage.getStorage().arms;
        Arm firstArm=null;
        if(arms.containsKey("0")){
            firstArm=arms.get("0");
        }else{
            System.err.println("Невозможно обработать алгоритм без нулевой вершины");
            System.exit(-1);
        }
        ArrayList<ArmLine> lines = firstArm.getLines();//обход ребер одной вершины ( в данном случае первой, т.е. с номером "0"
        String endNumber =null;
        for(ArmLine line:lines){
            if(line.compare(this.tape)){ //Если условие в данном ребре истинно...
                endNumber = line.getEndArmNumber();
                for(Statement statement:line.getStatements()){ //выполнение всех выражений (операций) , перечисленных в ребре
                    statement.doStatement(storage,tape);
                }
               //if ( this.allStorage.getTape().)
//                if(Objects.equals(currentNumber, "#")) { //если указано, что следующая вершина конечная - вывод состояний вех памятей и возврат из обхода
//                    System.out.println("Конец программы");
//                    Set<String> names = this.allStorage.storage.getMemories().keySet();
//                    for(String name:names){
//                        System.out.println(this.allStorage.storage.getMemories().get(name));
//                    }
//                    return;
//                }
//                if(currentNumber == "#") {
//                    System.out.println("Конец программы");
//                    Set<String> names = this.allStorage.storage.getMemories().keySet();
//                    for(String name:names){
//                        System.out.println(this.allStorage.storage.getMemories().get(name));
//                    }
//                    return;
//                }
                char tapeCurrent=this.tape.readCurrent();
                if(tapeCurrent=='#'){
                    if (stopType == StopType.END){
                        try {
                            Thread.sleep(100);
                            System.out.println("End of algorythm.");
                            this.wait();
                        } catch (InterruptedException e) {

                        }
                    }
                    textArea.appendText("Конец программы\n");
                    System.out.println("Конец программы");
//                    try {
//                        FileWriter file = new FileWriter("data/ResultFile.txt");
//                        String buftext = "";
//                        Set<String> names = this.allStorage.storage.getMemories().keySet();
//                        for(String name:names){
//                            buftext += this.allStorage.storage.getMemories().get(name).toString() + "\n";
//                            textArea.appendText(String.valueOf(this.allStorage.storage.getMemories().get(name) + "\n"));
//                            System.out.println(this.allStorage.storage.getMemories().get(name));
//                        }
//                        file.write(buftext);
//                        file.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    return;
                }
                this.currentNumber.setValue(endNumber);
                run(); //Если программа продолжается ( т.е. не был указан конец ("#"), переход к обработке узла с номером, указанным в ребре.
                return;
            }
        }
        if (endNumber==null){
            System.err.println("Нет выхода из вершины под номером 0");

        }

    }
//    public void run(String armNumber){
//        HashMap<String, Arm> arms = this.allStorage.getStorage().arms;
//        Arm firstArm=null;
//        if(arms.containsKey(armNumber)){
//            firstArm=arms.get(armNumber);
//        }else{
//            System.err.println("Ошибка в алгоритме: не существует вершины под номером: "+armNumber);
//            System.exit(-1);
//        }
//        ArrayList<ArmLine> lines = firstArm.getLines();
//        for(ArmLine line:lines){
//            if(line.compare(this.tape)){
//                String currentNumber = line.getEndArmNumber();
//                for(Statement statement:line.getStatements()){
//                    statement.doStatement(storage,tape);
//                }
//                char tapeCurrent=this.tape.readCurrent();
//                if(tapeCurrent=='#'){
//                    System.out.println("Конец программы");
//                    Set<String> names = this.allStorage.storage.getMemories().keySet();
//                    for(String name:names){
//                        System.out.println(this.allStorage.storage.getMemories().get(name));
//                    }
//                    return;
//                }
//
////                if(currentNumber == "#") {
////                    System.out.println("Конец программы");
////                    Set<String> names = this.allStorage.storage.getMemories().keySet();
////                    for(String name:names){
////                        System.out.println(this.allStorage.storage.getMemories().get(name));
////                    }
////                    return;
////                }
//                run(currentNumber);
//                return;
//            }
//        }
//
//    }

    public synchronized void run(String armNumber){
        HashMap<String, Arm> arms = this.allStorage.getStorage().arms;
        Arm firstArm=null;
        if(arms.containsKey(armNumber)){
            firstArm=arms.get(armNumber);
        }else{
            System.err.println("Ошибка в алгоритме: не существует вершины под номером: "+armNumber);
            System.exit(-1);
        }
        ArrayList<ArmLine> lines = firstArm.getLines();
        String endNumber = null;
        for(ArmLine line:lines){
            if(line.compare(this.tape)){

                endNumber = line.getEndArmNumber();
                for(Statement statement:line.getStatements()){
                    statement.doStatement(storage,tape);
                }
                char tapeCurrent=this.tape.readCurrent();
                if(tapeCurrent=='#'){
                    try {
                        this.textArea.appendText("Конец программы");
                        System.out.println("Конец программы");
                        Set<String> names = this.allStorage.storage.getMemories().keySet();
                        for (String name : names) {
                            this.textArea.appendText(String.valueOf(this.allStorage.storage.getMemories().get(name)));
                            System.out.println(this.allStorage.storage.getMemories().get(name));
                        }
                    }catch(NullPointerException e1){
                        System.out.println("Конец программы");
                        Set<String> names = this.allStorage.storage.getMemories().keySet();
                        for (String name : names) {
                            System.out.println(this.allStorage.storage.getMemories().get(name));
                        }
                    }
                    return;
                }

//                if(currentNumber == "#") {
//                    System.out.println("Конец программы");
//                    Set<String> names = this.allStorage.storage.getMemories().keySet();
//                    for(String name:names){
//                        System.out.println(this.allStorage.storage.getMemories().get(name));
//                    }
//                    return;
//                }
                run(endNumber);
                return;
            }
        }
        if (endNumber==null){
            System.err.println("Нет выхода из вершины под номером "+armNumber);
            System.out.println("Конец программы");
            Set<String> names = this.allStorage.storage.getMemories().keySet();
            for (String name : names) {
                System.out.println(this.allStorage.storage.getMemories().get(name));
            }
        }

    }

    public HashMap<String,Memory> getMemories(){
        return this.allStorage.storage.getMemories();

    }
    public void printMemories(){
        Set<String> names = this.allStorage.storage.getMemories().keySet();
        for(String name:names){
            System.out.println(this.allStorage.storage.getMemories().get(name));
        }
    }
    public String stringMemories(){
        String answer="";
        Set<String> names = this.allStorage.storage.getMemories().keySet();
        for(String name:names){
            answer+=(this.allStorage.storage.getMemories().get(name)+"\n");
        }
        return answer;
    }

    public static void main(String args[]) throws FileNotFoundException {
        HashMap<String, Arm> arms= new HashMap<>();
        HashMap<String,Memory> memories = new HashMap<>();
        HashMap<String,Alphabet> alphabets = new HashMap<>();

        //Cоздание ленты ввода
        Tape tape = new Tape("t#");
        Alphabet alphabet = new Alphabet("Alphabet", "Alph","abcdefghi".toCharArray());
        alphabets.put(alphabet.getFullname(),alphabet);

        //Создание памятей пустыми (начальное сстояние)
        Wagon wag1 = new Wagon("LW","RW", null);
        Register reg1 = new Register("reg1",null);
        Register reg2 = new Register("reg2", null);
        Table table = new Table("tab"/*,new String[]{"0","1"}*/);


        memories.put(reg1.getname(),reg1);
        memories.put(reg2.getname(),reg2);
        memories.put("LW*RW",wag1);
        memories.put("tab",table);


        ArrayList<ArmLine> armlines = new ArrayList<>();
        ArrayList<Statement> statements01 = new ArrayList<>();
        statements01.add(new Statement("LW",Statement.getOperator("<-"),"Cat"));
        statements01.add(new Statement("RW",Statement.getOperator("<-"),"Dog"));
        statements01.add(new Statement("LW*RW",Statement.getOperator("<-"),"Animals"));
        statements01.add(new Statement("reg2",Statement.getOperator("<-"),"13/3-6"));
        statements01.add(new Statement("tab.engineer",Statement.getOperator("<-"),"Scotty"));
        ArmLine arm01 = new ArmLine("0",new Condition("t"),statements01,"1");
        armlines.add(arm01);

        ArrayList<Statement> statements02 = new ArrayList<>();
        statements02.add(new Statement("reg1",Statement.getOperator("<-"),"nop"));
        statements02.add(new Statement("reg2",Statement.getOperator("<-"),"Kirk"));
        ArmLine arm02 = new ArmLine("0",new Condition("test"),statements02,"2");
        armlines.add(arm02);
        Arm arm0 = new Arm("0", armlines);

        ArrayList<ArmLine> armlines1 = new ArrayList<>();
        ArrayList<Statement> statements12 = new ArrayList<>();
        statements12.add(new Statement("reg1",Statement.getOperator("<-"),"test"));
        statements12.add(new Statement("tab.engineer",Statement.getOperator("<-"),"Scotty"));
        statements12.add(new Statement("tab.woman",Statement.getOperator("<-"),"Kate"));
        statements12.add(new Statement("tab.0",Statement.getOperator("^="),"Nik"));
        statements12.add(new Statement("tab.commander",Statement.getOperator(".="),"Spok"));
        statements12.add(new Statement("tab.key",Statement.getOperator("<-"),"Nik"));
//        statements12.add(new Statement("tab.Name",Statement.getOperator("&="),"Nik"));
        statements12.add(new Statement("tab.key",Statement.getOperator("|-"),"tab.key = "));
        statements12.add(new Statement("reg1",Statement.getOperator("/-|"),"Input value of reg1: "));
        statements12.add(new Statement("RW",Statement.getOperator("->"),"reg2"));
        ArmLine arm12 = new ArmLine("1",new Condition("*"),statements12,"#");
        armlines.add(arm12);
        Arm arm1 = new Arm("1", armlines1);
        arms.put("0",arm0);
        arms.put("1",arm1);
        AllStorage allStorage = new AllStorage(new Storage(arms,memories,alphabets),tape);
        R_machine r_machine = new R_machine(allStorage);
        r_machine.run();

    }

}
