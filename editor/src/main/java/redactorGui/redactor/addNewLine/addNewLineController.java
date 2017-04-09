package redactorGui.redactor.addNewLine;

import com.google.common.io.Resources;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import redactorGui.RedactorModule;
import redactorGui.alphabets.alphabetRecord;
import redactorGui.memoryTypes.memoryTypeRecord;
import redactorGui.redactor.Command;
import redactorGui.redactor.Flags;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import redactorGui.redactor.PredicateTypes;

import java.io.IOException;

/**
 * Created by Alex on 24.10.2016.
 */
public class addNewLineController {

    private ObservableList<String> predicateOptions = FXCollections.observableArrayList();
    private ObservableList<String> memoryOptions = FXCollections.observableArrayList();
    private ObservableList<String> alphabetOptions = FXCollections.observableArrayList();
    private ObservableList<String> operatorOptions = FXCollections.observableArrayList();
    private ObservableList<String> funcOptions = FXCollections.observableArrayList();
    //private ObservableList<String> predOperOptions = FXCollections.observableArrayList();


    private ObservableList<String> metkaPerehodaOptions = FXCollections.observableArrayList();

    @FXML
    private TextField metkaField;

    // TODO: 13.02.2017 разбить на 3 ComboBox'a
    @FXML
    private ComboBox uslovieFieldLeft;

    @FXML
    private ComboBox uslovieFieldCenter;

    @FXML
    private ComboBox uslovieFieldRight;

    //private ComboBox uslovieField;

    @FXML
    private ComboBox linopFieldLeft;

    @FXML
    private ComboBox linopFieldCenter;

    @FXML
    private ComboBox linopFieldRight;

    @FXML
    private ComboBox metkaPerehodaField;

    @FXML
    private TextArea commentsArea;

    @FXML
    private Button doneButton;

    @FXML
    private Button cancelButton;

    private Stage dialogStage;
    private Command command;
    private boolean okClicked = false;
    private RedactorModule redactorModule;

    public void loadOptions() {

        // Загрузить опции для поля "Условие"

        for (memoryTypeRecord record : redactorModule.getMemoryTypesData()) {
            if (record.getType().equals("Вагон")) {
                memoryOptions.add(record.getName().split(" | ")[0]);
                memoryOptions.add(record.getName().split(" | ")[2]);
            } else {
                memoryOptions.add(record.getName());
            }
        }
        for (alphabetRecord record : redactorModule.getAlphabetsData()) {
            alphabetOptions.add(record.getName());
        }

        //predicateOptions.addAll(memoryOptions);
        //predicateOptions.addAll(alphabetOptions);

        uslovieFieldLeft.setItems(memoryOptions);
        TextFields.bindAutoCompletion(uslovieFieldLeft.getEditor(), uslovieFieldLeft.getItems());

        predicateOptions.addAll(alphabetOptions);

        predicateOptions.add("<");
        predicateOptions.add(">");
        predicateOptions.add("<=");
        predicateOptions.add(">=");
        predicateOptions.add("==");
        predicateOptions.add("!=");
        predicateOptions.add("*");

        uslovieFieldCenter.setItems(predicateOptions);
        TextFields.bindAutoCompletion(uslovieFieldCenter.getEditor(), uslovieFieldCenter.getItems());

        uslovieFieldRight.setItems(memoryOptions);
        TextFields.bindAutoCompletion(uslovieFieldRight.getEditor(), uslovieFieldRight.getItems());

        // Загрузить опции для поля "Метка перехода"

        for (Command record : redactorModule.getCommandData()) {
            metkaPerehodaOptions.add(record.getMetka());
        }

        metkaPerehodaField.setItems(metkaPerehodaOptions);
        TextFields.bindAutoCompletion(metkaPerehodaField.getEditor(), metkaPerehodaField.getItems());

        // Загрузить опции линейных операторов


        linopFieldLeft.setItems(memoryOptions);
        TextFields.bindAutoCompletion(linopFieldLeft.getEditor(), linopFieldLeft.getItems());

        //operatorOptions.add("|-");
        //operatorOptions.add("-|");
        operatorOptions.add("&=");
        operatorOptions.add("~=");
        operatorOptions.add("^");
        operatorOptions.add("^=");
        operatorOptions.add(":");
        operatorOptions.add(":=");
        operatorOptions.add("->");
        operatorOptions.add("<-");
        operatorOptions.add("/");
        operatorOptions.add("*");

        linopFieldCenter.setItems(operatorOptions);
        TextFields.bindAutoCompletion(linopFieldCenter.getEditor(), linopFieldCenter.getItems());
        funcOptions.add("sqrt()");

        // TODO: 13.02.2017 добавить и в левую часть 
        funcOptions.add("FILE");
        funcOptions.add("CONSOLE");
        funcOptions.add("MEMORY");

        linopFieldRight.setItems(funcOptions);
        TextFields.bindAutoCompletion(linopFieldRight.getEditor(), linopFieldRight.getItems());
    }

    @FXML
    private void initialize() {
        try {
            Image doneImage = new Image(Resources.getResource("ic_done_black_24dp_1x.png").openStream());
            doneButton.setGraphic(new ImageView(doneImage));

            Image cancelImage = new Image(Resources.getResource("ic_cancel_black_24dp_1x.png").openStream());
            cancelButton.setGraphic(new ImageView(cancelImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setRedactorModule(RedactorModule redactorModule) {
        this.redactorModule = redactorModule;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCommand(Command command) {
        this.command = command;
        metkaField.setText(command.getMetka());
        //uslovieField.setValue(command.getUslovie());

        /**
         * Определяем, как данные из объекта Command вставить в поля окна добавления команды
         */

        String uslovieLeft = command.getUslovieLeft();
        String uslovieCenter = command.getUslovieCenter();
        String uslovieRight = command.getUslovieRight();

        if (uslovieLeft.isEmpty() && uslovieRight.isEmpty()) {
            uslovieFieldCenter.setValue(uslovieCenter);
        } else {
            uslovieFieldLeft.setValue(uslovieLeft);
            uslovieFieldCenter.setValue(uslovieCenter);
            uslovieFieldRight.setValue(uslovieRight);
        }

        String linopLeft = command.getLinopLeft();
        String linopCenter = command.getLinopCenter();
        String linopRight = command.getLinopRight();
        
        if (linopLeft.isEmpty() && linopRight.isEmpty()) {
            linopFieldCenter.setValue(linopCenter);
        } else {
            linopFieldLeft.setValue(linopLeft);
            linopFieldCenter.setValue(linopCenter);
            linopFieldRight.setValue(linopRight);
        }

        metkaPerehodaField.setValue(command.getMetkaPerehoda());
        commentsArea.setText(command.getComments());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if(isInputValid()) {
            command.setMetka(metkaField.getText());

            // Проверяем, пусты или нет левое и правое поля

            boolean uslovieLeftEmpty = uslovieFieldLeft.getValue() == null || uslovieFieldLeft.getValue().toString().length() == 0;
            boolean uslovieRightEmpty = uslovieFieldRight.getValue() == null || uslovieFieldRight.getValue().toString().length() == 0;

            if (uslovieLeftEmpty && uslovieRightEmpty) {
                command.setUslovieCenter(uslovieFieldCenter.getValue().toString());
            } else {
                command.setUslovieLeft(uslovieFieldLeft.getValue().toString());
                command.setUslovieCenter(uslovieFieldCenter.getValue().toString());
                command.setUslovieRight(uslovieFieldRight.getValue().toString());
            }
            
            if (linopFieldCenter.getValue().toString().equals("*")) {
                command.setLinopCenter(linopFieldCenter.getValue().toString());
            } else {
                command.setLinopLeft(linopFieldLeft.getValue().toString());
                command.setLinopCenter(linopFieldCenter.getValue().toString());
                command.setLinopRight(linopFieldRight.getValue().toString());
            }
            
            command.setMetkaPerehoda(metkaPerehodaField.getValue().toString());
            command.setComments(commentsArea.getText());
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    @FXML
    private boolean isInputValid() {
        String errorMessage = "";

        boolean metkaNotEmpty = !(metkaField.getText() == null || metkaField.getText().length() == 0);
        
        //boolean uslovieNotEmpty = !(uslovieField.getValue().toString() == null || uslovieField.getValue().toString().length() == 0);

        boolean uslovieLeftNotEmpty = !(uslovieFieldLeft.getValue() == null || uslovieFieldLeft.getValue().toString().length() == 0);
        boolean uslovieCenterNotEmpty = !(uslovieFieldCenter.getValue() == null || uslovieFieldCenter.getValue().toString().length() == 0);
        boolean uslovieRightNotEmpty = !(uslovieFieldRight.getValue() == null || uslovieFieldRight.getValue().toString().length() == 0);

        boolean uslovieIsOkay = (uslovieLeftNotEmpty && uslovieCenterNotEmpty && uslovieRightNotEmpty) || uslovieCenterNotEmpty;
        
        boolean linopLeftNotEmpty = !(linopFieldLeft.getValue() == null || linopFieldLeft.getValue().toString().length() == 0);
        boolean linopCenterNotEmpty = !(linopFieldCenter.getValue() == null || linopFieldCenter.getValue().toString().length() == 0);
        boolean linopRightNotEmpty = !(linopFieldRight.getValue() == null || linopFieldRight.getValue().toString().length() == 0);
        
        boolean linopIsOkay = (linopLeftNotEmpty && linopCenterNotEmpty && linopRightNotEmpty) || linopFieldCenter.getValue().toString().equals("*");

        if (linopIsOkay) {
            command.setFlag(Flags.OPERATOR);
            if (uslovieIsOkay) {
                command.setFlag(Flags.CONDITION);
                command.setPredicateType(determinePredicateType());
                if (metkaNotEmpty) {
                    command.setFlag(Flags.TAG);
                }
            } else if (metkaNotEmpty) {
                errorMessage += "Не заполнено поле 'Условие'!\n";
            }
        } else {
            errorMessage += "Не заполнено поле 'Линейный оператор'!\n";
        }

        if(errorMessage.length() == 0) {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Информация о команде");
//            alert.setHeaderText("Флаг: ");
//            alert.setContentText(command.getFlag().toString());
//            alert.showAndWait();
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Поле не заполнено");
            alert.setHeaderText("Пожалуйста, заполните поле");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }

    private PredicateTypes determinePredicateType() {
        String value = uslovieFieldCenter.getValue().toString();
        if (memoryOptions.contains(value)) {
            return PredicateTypes.MEMORY;
        } else if (alphabetOptions.contains(value)) {
            return PredicateTypes.ALPHABET;
        } else if (value.contains(">") || value.contains("<") || value.contains("=")) {
            return PredicateTypes.EXPRESSION;
        } else {
            return PredicateTypes.STRING;
        }
    }

}
