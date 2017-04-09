package gui.window.makery.address.model;/**
 * Created by Anton on 05.12.2016.
 */

import Logic.Arm;
import Logic.ArmLine;
import Logic.Condition;
import Logic.Statement;
import Memories.*;
import Other.AllStorage;
import Other.R_machine;
import Other.Storage;
import Other.Tape;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
public class ProgramWindow extends Application {

    public static final String FILE = "FileWork";
    public static final String CONSOLE = "ConsoleWork";
    public static final String MEMORY = "FullMemory";
    private Stage      primaryStage;
    private BorderPane rootLayout;
    private TextArea   textArea;
    private TextField  textField;
    public  String     text;


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
//        this.textArea.appendText("Hello world");
        this.primaryStage.setTitle("R Машина");

        initRootLayout();

//        showPersonOverview();
    }

    public void writeInWindow(String value) {
        textArea.appendText(value);
    }

    public TextArea getTextArea() { return textArea; }
    /**
     * Инициализирует корневой макет.
     */

    @FXML
    public void initRootLayout() {
        rootLayout = new BorderPane();
        // Отображаем сцену, содержащую корневой макет.
        Scene scene = new Scene(rootLayout);

        textArea = new TextArea();
        textField = new TextField();
        rootLayout.setCenter(textArea);
        rootLayout.setBottom(textField);
        textArea.setWrapText(true);
        textArea.appendText("Hello World\n");
        textArea.appendText("Hello World\n");
        textArea.setEditable(false);
        System.out.println(rootLayout.getChildren());
        primaryStage.setScene(scene);
        primaryStage.show();
        File file = new File("data/ResultFile.txt");
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashMap<String, Arm> arms= new HashMap<>();
        HashMap<String, Memory> memories = new HashMap<>();
        HashMap<String, Alphabet> alphabets = new HashMap<>();

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
        statements01.add(new Statement("tab.engineer",Statement.getOperator("<-"),CONSOLE, textArea, textField));
        statements01.add(new Statement("tab.engineer",Statement.getOperator("->"),CONSOLE, textArea));
        statements01.add(new Statement("tab.engineer",Statement.getOperator("->"),FILE));
        statements01.add(new Statement("tab.engineer",Statement.getOperator("<-"),"Kirk"));
        statements01.add(new Statement("tab.engineer",Statement.getOperator("<-"),FILE));
        statements01.add(new Statement(MEMORY,Statement.getOperator("->"),FILE));
        statements01.add(new Statement("tab.engineer",Statement.getOperator("->"),CONSOLE, textArea));
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
        statements12.add(new Statement("RW",Statement.getOperator("->"),"reg2"));
        statements12.add(new Statement(MEMORY,Statement.getOperator("->"),FILE));
        ArmLine arm12 = new ArmLine("1",new Condition("*"),statements12,"#");
        armlines.add(arm12);
        Arm arm1 = new Arm("1", armlines1);
        arms.put("0",arm0);
        arms.put("1",arm1);
        AllStorage allStorage = new AllStorage(new Storage(arms,memories,alphabets),tape);
        R_machine r_machine = new R_machine(allStorage);
        r_machine.run(textArea);
        //Загружаем корневой макет из fxml файла.
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(ProgramWindow.class.getResource("ProgramWindow.fxml"));
//            rootLayout = (AnchorPane) loader.load();
//        rootLayout = new BorderPane();
//        // Отображаем сцену, содержащую корневой макет.
//        Scene scene = new Scene(rootLayout);

//        textArea = new TextArea();
//        TextField field = new TextField();
//        rootLayout.setCenter(textArea);
//        rootLayout.setBottom(field);
//        textArea.setWrapText(true);
//        textArea.appendText("Hello World\n");
//        textArea.appendText("Hello World\n");
//        textArea.setEditable(false);
//        System.out.println(rootLayout.getChildren());
//        primaryStage.setScene(scene);
//        primaryStage.show();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}