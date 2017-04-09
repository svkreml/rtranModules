package redactorGui.alphabets.addNewAlphabet;

import com.google.common.io.Resources;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import redactorGui.RedactorModule;
import redactorGui.alphabets.alphabetRecord;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Alex on 21.11.2016.
 */
public class addNewAlphabetController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField shortNameField;

    @FXML
    private TextField valuesField;

    @FXML
    private TextArea commentsArea;

    @FXML
    private Button doneButton;

    @FXML
    private Button cancelButton;

    private Stage dialogStage;
    private alphabetRecord alphabet;
    private boolean okClicked = false;
    private RedactorModule redactorModule;

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

    public void setRecord(alphabetRecord alphabet) {
        this.alphabet = alphabet;
        nameField.setText(alphabet.getName());
        shortNameField.setText(alphabet.getShortName());
        valuesField.setText(alphabet.getValues());
        commentsArea.setText(alphabet.getComments());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if(isInputValid()) {
            alphabet.setName(nameField.getText());
            alphabet.setShortName(shortNameField.getText());
            alphabet.setValues(valuesField.getText());
            alphabet.setComments(commentsArea.getText());
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

        for (alphabetRecord record : redactorModule.getAlphabetsData()) {
            if(nameField.getText().equals(record.getName()) || shortNameField.getText().equals(record.getShortName())) {
                errorMessage += "Синтерм с таким именем уже существует!\n";
            }
        }

        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errorMessage += "Укажите название синтерма!\n";
        }

        if (valuesField.getText() == null || valuesField.getText().length() == 0) {
            errorMessage += "Укажите набор символов!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Поля не заполнены");
            alert.setHeaderText("Пожалуйста, исправьте следующие ошибки:");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }

    }

}
