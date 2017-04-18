package redactorGui;

import com.google.common.io.Resources;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import org.apache.commons.lang3.StringUtils;
import redactorGui.alphabets.alphabetRecord;
import redactorGui.alphabets.alphabetsController;
import redactorGui.memoryTypes.memoryTypeRecord;
import redactorGui.memoryTypes.memoryTypesController;
import redactorGui.redactor.Command;
import redactorGui.redactor.PredicateTypes;
import redactorGui.redactor.RedactorController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import structure.*;
import xml.Loader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RedactorModule {


    private Tab redactorTab;
    private Tab memoryTypesTab;
    private Tab alphabetsTab;
    private AnchorPane redactorPane;
    private RedactorModuleController controller;

    public AnchorPane getRedactorPane() {
        return redactorPane;
    }

    public void setCommandData(ObservableList<Command> commandData) {
        this.commandData = commandData;
    }

    private ObservableList<Command> commandData = FXCollections.observableArrayList();
    private ObservableList<memoryTypeRecord> memoryTypesData = FXCollections.observableArrayList();
    private ObservableList<alphabetRecord> alphabetsData = FXCollections.observableArrayList();

    private R_pro r_pro; // в этом объекте хранится сама программа Р-тран

    public R_pro getR_pro() {
        return r_pro;
    }

    public void updateR_pro(R_pro r_pro) {
        this.r_pro = r_pro;
    }

    void clearR_pro() {
        commandData.clear();
        memoryTypesData.clear();
        alphabetsData.clear();
    }

    public void load(File file) {
        controller.handleLoad(file);
    }

    public void save() {
        controller.handleSave();
    }

    public void saveAs(File destination) {
        controller.handleSaveAs(destination);
    }
    

    void setRedactorTab(Tab redactorTab) {
        this.redactorTab = redactorTab;
    }

    void setMemoryTypesTab(Tab memoryTypesTab) {
        this.memoryTypesTab = memoryTypesTab;
    }

    void setAlphabetsTab(Tab alphabetsTab) {
        this.alphabetsTab = alphabetsTab;
    }

    public RedactorModule() {
        r_pro = new R_pro();
        r_pro.setProgname("Без названия");

        setDefaultAlphabets();
    }

    void setDefaultAlphabets() {
        alphabetRecord russian = new alphabetRecord();
        russian.setName("русский алфавит");
        russian.setShortName("рус");
        russian.setValues("АаБбВвГгДдЕеЁёЖжЗзИиЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщЪъЫыЬьЭэЮюЯя");

        alphabetRecord english = new alphabetRecord();
        english.setName("английский алфавит");
        english.setShortName("англ");
        english.setValues("AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz");

        alphabetRecord numbers = new alphabetRecord();
        numbers.setName("цифры");
        numbers.setShortName("цфр");
        numbers.setValues("0123456789");

        alphabetRecord punctuationMarks = new alphabetRecord();
        punctuationMarks.setName("знаки пунктуации");
        punctuationMarks.setShortName("пункт");
        punctuationMarks.setValues(".,;:!?‐-‒–—―[](){}⟨⟩„“«»“”‘’‹›\"");

        alphabetsData.setAll(russian, english, numbers, punctuationMarks);
        try {
            R_pro updated = new R_pro("1.0", getR_pro().getProgname(), getDescriptive_part(), getAlg());
            updateR_pro(updated);
        } catch (Exception e) {
            return;
        }
    }

    public ObservableList<Command> getCommandData() {
        return commandData;
    }
    public ObservableList<memoryTypeRecord> getMemoryTypesData() { return memoryTypesData; }
    public ObservableList<alphabetRecord> getAlphabetsData() { return alphabetsData; }

    public void init(AnchorPane availableArea, File fileToBeOpened) {
        this.redactorPane = availableArea;
        initRedactorPane();
        showRedactor();
        showMemoryTypes();
        showAlphabets();
        load(fileToBeOpened);
    }


    private void initRedactorPane() {
        try {
            // Загружаем корневой макет из fxml файла.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Resources.getResource("RedactorWindow.fxml"));
            TabPane tabs = loader.load();
            AnchorPane.setBottomAnchor(tabs, 0.0);
            AnchorPane.setTopAnchor(tabs, 0.0);
            AnchorPane.setLeftAnchor(tabs, 0.0);
            AnchorPane.setRightAnchor(tabs, 0.0);
            redactorPane.getChildren().add(tabs);
            controller = loader.getController();
            controller.setRedactorModule(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showRedactor() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Resources.getResource("redactor/Redactor.fxml"));
            AnchorPane redactor = loader.load();

            // Помещаем сведения о командах в центр корневого макета.
            redactorTab.setContent(redactor);

            Image redactorImage = new Image(Resources.getResource("ic_format_list_numbered_black_24dp_1x.png").openStream());
            redactorTab.setGraphic(new ImageView(redactorImage));

            RedactorController redactorController = loader.getController();
            redactorController.setRedactorModule(this);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showMemoryTypes() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Resources.getResource("memoryTypes/memoryTypes.fxml"));
            AnchorPane memoryTypes = loader.load();

            memoryTypesTab.setContent(memoryTypes);

            Image memoryImage = new Image(Resources.getResource("ic_memory_black_24dp_1x.png").openStream());
            memoryTypesTab.setGraphic(new ImageView(memoryImage));


            memoryTypesController memoryController = loader.getController();
            memoryController.setRedactorModule(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlphabets() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Resources.getResource("alphabets/alphabets.fxml"));
            AnchorPane alphabets = loader.load();
            alphabetsTab.setContent(alphabets);

            Image abcImage = new Image(Resources.getResource("ic_sort_by_alpha_black_24dp_1x.png").openStream());
            alphabetsTab.setGraphic(new ImageView(abcImage));

            alphabetsController abcController = loader.getController();
            abcController.setRedactorModule(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Memory> getMemory() {
        ObservableList<Memory> memory = FXCollections.observableArrayList();
        for (memoryTypeRecord record : memoryTypesData) {

            switch (record.getType()) {
                case "Счетчик":
                    memory.add(new Memory("Counter", record.getName()));
                    break;
                case "Регистр":
                    memory.add(new Memory("Register", record.getName(), record.getOutType()));
                    break;
                case "Вагон":
                    String lv = record.getName().split("\\*")[0];
                    String pv = record.getName().split("\\*")[1];
                    memory.add(new Memory("Wagon", lv, pv));
                    break;
                case "Таблица":
                    memory.add(new Memory("Table", record.getName()));
                    break;
            }

        }
        return memory;
    }

    private ObservableList<Abc> getAbcs() {
        ObservableList<Abc> abcs = FXCollections.observableArrayList();
        for(alphabetRecord record : alphabetsData) {
            abcs.add(new Abc(record.getName(), record.getShortName(), record.getComments(), record.getValues()));
        }
        return abcs;
    }

    public Descriptive_part getDescriptive_part() {
        Memory_block memory_block = new Memory_block(getMemory());
        Alphabet alphabet = new Alphabet(getAbcs());
        return new Descriptive_part(memory_block, alphabet);
    }

    private Operation getOperation(Command record) {
        String linopLeft = record.getLinopLeft();
        String linopCenter = record.getLinopCenter();
        String linopRight = record.getLinopRight();

        Left left;
        String operator;
        Right right;
        Operation operation;
        List<String> knownOperators = new ArrayList<>();

        knownOperators.add("&=");
        knownOperators.add("~=");
        knownOperators.add("^=");
        knownOperators.add(".=");
        knownOperators.add("<-");
        knownOperators.add("->");
        knownOperators.add("/<-");
        knownOperators.add("<-/");
        knownOperators.add("/->");
        knownOperators.add("->/");
        knownOperators.add("*");
        knownOperators.add("END");

        for(String oper : knownOperators) {
            if (linopCenter.equals(oper)) {
                left = new Left(linopLeft);
                operator = oper;

                // Правая часть (содержащая выражение) не будет содержать в себе пробелов:

                right = new Right(StringUtils.remove(linopRight, " "));
                operation = new Operation(left, operator, right);
                return operation;
            }
        }

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(getRedactorPane().getScene().getWindow());
        alert.setTitle("Ошибка в линейном операторе");
        alert.setHeaderText("По всей видимости, такого линейного оператора не существует");
        alert.setContentText("Во избежание ошибок времени выполнения, исправьте линейный оператор.");
        alert.showAndWait();

        operation = new Operation(new Left(linopLeft), linopCenter, new Right(linopRight));
        return operation;
    }

    public Alg getAlg() throws Exception {
        Alg alg = new Alg();
        int curArm = -1;
        int curEdge = 0;
        for (Command record : commandData) {
            Predicate predicate;
            switch (record.getFlag()) {
                case TAG:

                    Operation operation = getOperation(record);

                    predicate = getPredicate(record);

                    curEdge = 0;

                    Edge edge = new Edge(predicate, operation);
                    edge.addEnd(record.getMetkaPerehoda());

                    Arm arm = new Arm(record.getMetka());
                    arm.addEdge(edge);
                    alg.addArm(arm);
                    curArm++;
                    break;

                case CONDITION:

                    operation = getOperation(record);

                    predicate = getPredicate(record);

                    curEdge++;

                    edge = new Edge(predicate, operation);
                    edge.addEnd(record.getMetkaPerehoda());

                    alg.getArm(curArm).addEdge(edge);
                    break;

                case OPERATOR:

                    operation = getOperation(record);

                    alg.getArm(curArm).getEdge(curEdge).addOperation(operation);
                    alg.getArm(curArm).getEdge(curEdge).addEnd(record.getMetkaPerehoda());

                    break;
            }
        }
        return alg;
    }

    private Predicate getPredicate(Command record) {
        String type;
        Predicate predicate;
        switch (record.getPredicateType()) {
            case ALPHABET:
                type = "alphabet";
                predicate = new Predicate(type, record.getUslovieCenter());
                break;
            case EXPRESSION:
                type = "expression";
                Memory memoryLeft = new Memory(record.getUslovieLeft());
                String sign = record.getUslovieCenter();
                Memory memoryRight = new Memory(record.getUslovieRight());
                predicate = new Predicate(type, memoryLeft, sign, memoryRight);
                break;
            case MEMORY:
                type = "memory";
                predicate = new Predicate(type, record.getUslovieCenter());
                break;
            default:
                type = "string";
                predicate = new Predicate(type, record.getUslovieCenter());
                break;
        }
        return predicate;
    }


}

