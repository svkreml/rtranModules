package redactorGui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import redactorGui.alphabets.alphabetRecord;
import redactorGui.alphabets.alphabetsController;
import redactorGui.memoryTypes.memoryTypeRecord;
import redactorGui.memoryTypes.memoryTypesController;
import redactorGui.redactor.Command;
import redactorGui.redactor.Flags;
import redactorGui.redactor.PredicateTypes;
import redactorGui.redactor.RedactorController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tab;
import structure.*;
import xml.Loader;
import xml.LoadingResult;
import xml.Saver;

import java.io.File;
import java.util.List;

/**
 * Created by svkreml on 23.10.2016.
 */
public class RedactorModuleController {
    private RedactorModule redactorModule;

    @FXML
    private Tab redactorTab;

    @FXML
    private Tab memoryTypesTab;

    @FXML
    private Tab alphabetsTab;

    public File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }

    private File path;


    public void setRedactorModule(RedactorModule redactorModule) {
        this.redactorModule = redactorModule;
        this.redactorModule.setRedactorTab(redactorTab);
        this.redactorModule.setMemoryTypesTab(memoryTypesTab);
        this.redactorModule.setAlphabetsTab(alphabetsTab);
        path = null;
    }

    @FXML
    private void initialize() {
        RedactorController command_redactor = new RedactorController();
        memoryTypesController memoryTypes = new memoryTypesController();
        alphabetsController alphabets = new alphabetsController();
    }

    void handleSave() {

        if (path == null) {
            handleSaveAs(path);
        } else {
            Saver saver = new Saver();
            saver.setKnownDestination(path);
            saver.save(redactorModule.getR_pro());
        }
    }

    boolean handleSaveAs(File fileToBeSaved) {
        Saver saver = new Saver();
        return saver.saveAs(redactorModule.getR_pro(), fileToBeSaved);
    }

    void handleLoad(File location) {
        Loader loader = new Loader();
        LoadingResult loadingResult = loader.load(location);
        if (loadingResult == LoadingResult.SUCCESS) {
            redactorModule.clearR_pro();
            R_pro readed = loader.getReaded();
            redactorModule.updateR_pro(readed);
            path = loader.getLocation();
            insertAlg(readed);
            insertMemory(readed);
            insertAbc(readed);
        } else if (loadingResult == LoadingResult.EMPTY) {
            redactorModule.clearR_pro();
            path = loader.getLocation();
        }
    }

    private void insertAbc(R_pro readed) throws NullPointerException {
        ObservableList<alphabetRecord> readedAbc = FXCollections.observableArrayList();
        if (readed.getDescriptive_part().getAlphabet() != null) {
            for (Abc abc : readed.getDescriptive_part().getAlphabet().getAbc()) {
                alphabetRecord record = new alphabetRecord();
                record.setName(abc.getName());
                record.setComments(abc.getDescription());
                record.setShortName(abc.getShort_name());
                record.setValues(abc.getContents());
                readedAbc.add(record);
            }
        }
        redactorModule.getAlphabetsData().removeAll();
        redactorModule.getAlphabetsData().addAll(readedAbc);
    }

    private void insertMemory(R_pro readed) {
        ObservableList<memoryTypeRecord> readedMemory = FXCollections.observableArrayList();
        if (readed.getDescriptive_part().getMemory_block() != null) {
            for (Memory memory : readed.getDescriptive_part().getMemory_block().getMemory()) {
                memoryTypeRecord record = new memoryTypeRecord();

                switch (memory.getType()) {
                    case "Counter":
                        record.setType("Счетчик");
                        record.setName(memory.getName());
                        break;
                    case "Register":
                        record.setType("Регистр");
                        record.setName(memory.getName());
                        break;
                    case "Wagon":
                        record.setType("Вагон");
                        record.setName(String.format("%s | %s", memory.getLeftName(), memory.getRightName()));
                        break;
                    case "Table":
                        record.setType("Таблица");
                        record.setName(memory.getName());
                        break;
                }
                readedMemory.add(record);
            }
        }
        redactorModule.getMemoryTypesData().removeAll();
        redactorModule.getMemoryTypesData().addAll(readedMemory);
    }

    private void insertAlg(R_pro readed) {
        ObservableList<Command> readedCommands = FXCollections.observableArrayList();
        Command currentCommand = new Command();
        if (readed.getAlg() != null) {
            for (Arm arm : readed.getAlg().getArm()) {
                currentCommand.setMetka(arm.getBegin());
                currentCommand.setFlag(Flags.TAG);
                for (Edge edge : arm.getEdge()) {

                    ObservableList<String> memoryOptions = FXCollections.observableArrayList();
                    ObservableList<String> alphabetOptions = FXCollections.observableArrayList();

                    for (memoryTypeRecord record : redactorModule.getMemoryTypesData()) {
                        memoryOptions.add(record.getName());
                    }
                    for (alphabetRecord record : redactorModule.getAlphabetsData()) {
                        alphabetOptions.add(record.getName());
                    }

                    String value;
                    String type = edge.getPredicate().getType();

                    switch (type) {
                        case "memory":
                            currentCommand.setPredicateType(PredicateTypes.MEMORY);
                            value = edge.getPredicate().getContents();
                            currentCommand.setUslovieCenter(value);
                            break;
                        case "alphabet":
                            currentCommand.setPredicateType(PredicateTypes.ALPHABET);
                            value = edge.getPredicate().getContents();
                            currentCommand.setUslovieCenter(value);
                            break;
                        case "expression":
                            currentCommand.setPredicateType(PredicateTypes.EXPRESSION);
                            String memoryLeft = edge.getPredicate().getMemoryLeft().getName();
                            String memoryRight = edge.getPredicate().getMemoryRight().getName();
                            String sign = edge.getPredicate().getSign();
                            currentCommand.setUslovieLeft(memoryLeft);
                            currentCommand.setUslovieCenter(sign);
                            currentCommand.setUslovieRight(memoryRight);
                            break;
                        default:
                            currentCommand.setPredicateType(PredicateTypes.STRING);
                            value = edge.getPredicate().getContents();
                            currentCommand.setUslovieCenter(value);
                            break;
                    }

                    if (currentCommand.getFlag() != Flags.TAG) {
                        currentCommand.setFlag(Flags.CONDITION);
                    }
                    for (Operation operation : edge.getOperation()) {
                        String left = operation.getLeft().getValue();
                        String right = operation.getRight().getValue();
                        String operator = operation.getOperator();
                        //currentCommand.setLinop(left + " " + operator + " " + right);
                        currentCommand.setLinopLeft(left);
                        currentCommand.setLinopCenter(operator);
                        currentCommand.setLinopRight(right);
                        if (currentCommand.getFlag() != Flags.TAG && currentCommand.getFlag() != Flags.CONDITION)
                            currentCommand.setFlag(Flags.OPERATOR);
                        readedCommands.add(currentCommand);
                        currentCommand = new Command();
                    }
                    Command lastOperator = readedCommands.get(readedCommands.size() - 1);
                    lastOperator.setMetkaPerehoda(edge.getEnd());
                    readedCommands.remove(readedCommands.size() - 1);
                    readedCommands.add(lastOperator);
                }

            }
        }
        redactorModule.getCommandData().removeAll();
        redactorModule.getCommandData().addAll(readedCommands);
    }

    /**
     * Закрывает приложение.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }

}
