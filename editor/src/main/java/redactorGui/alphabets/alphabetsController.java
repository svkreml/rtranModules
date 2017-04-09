package redactorGui.alphabets;

import com.google.common.io.Resources;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import redactorGui.RedactorModule;
import redactorGui.alphabets.addNewAlphabet.addNewAlphabetController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import structure.R_pro;

import java.io.IOException;

/**
 * Created by Alex on 20.11.2016.
 */
public class alphabetsController {

    @FXML
    private TableView<alphabetRecord> alphabetsTable;

    @FXML
    private TableColumn<alphabetRecord, String> syntermColumn;

    @FXML
    private TableColumn<alphabetRecord, String> nameColumn;

    @FXML
    private TableColumn<alphabetRecord, String> shortNameColumn;

    @FXML
    private TableColumn<alphabetRecord, String> valuesColumn;

    @FXML
    private TableColumn<alphabetRecord, String> commentsColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    private RedactorModule redactorModule;

    public void clear() {
        alphabetsTable.setItems(FXCollections.observableArrayList());
    }

    @FXML
    private void initialize() {
        try {
            Image imageAdd = new Image(Resources.getResource("ic_add_box_black_24dp_1x.png").openStream());
            addButton.setGraphic(new ImageView(imageAdd));

            Image imageEdit = new Image(Resources.getResource("ic_edit_black_24dp_1x.png").openStream());
            editButton.setGraphic(new ImageView(imageEdit));

            Image imageDelete = new Image(Resources.getResource("ic_delete_black_24dp_1x.png").openStream());
            deleteButton.setGraphic(new ImageView(imageDelete));
        } catch (IOException e) {
            e.printStackTrace();
        }

        syntermColumn.setCellValueFactory(cellData -> cellData.getValue().syntermProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        shortNameColumn.setCellValueFactory(cellData -> cellData.getValue().shortNameProperty());
        valuesColumn.setCellValueFactory(cellData -> cellData.getValue().valuesProperty());
        commentsColumn.setCellValueFactory(cellData -> cellData.getValue().commentsProperty());
    }

    public void setRedactorModule(RedactorModule redactorModule) {
        this.redactorModule = redactorModule;
        alphabetsTable.setItems(redactorModule.getAlphabetsData());
    }

    private boolean showAlphabetEditDialog(alphabetRecord alphabet) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RedactorModule.class.getClassLoader().getResource("alphabets/addNewAlphabet/addNewAlphabet.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Редактирование синтерма");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(redactorModule.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            addNewAlphabetController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setRecord(alphabet);
            controller.setRedactorModule(redactorModule);

            dialogStage.showAndWait();

            return controller.isOkClicked();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    private void handleDeleteRecord() {
        int selectedIndex = alphabetsTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            alphabetsTable.getItems().remove(selectedIndex);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            //alert.initOwner(redactorModule.getPrimaryStage());
            alert.setTitle("Ничего не выбрано");
            alert.setHeaderText("Не была выбрана ни одна запись");
            alert.setContentText("Пожалуйста, выберите запись в таблице");
            alert.showAndWait();
        }

        R_pro updated = new R_pro("1.0", redactorModule.getR_pro().getProgname(), redactorModule.getDescriptive_part(), redactorModule.getAlg());
        redactorModule.updateR_pro(updated);

    }

    @FXML
    private void handleAddNewAlphabet() {
        alphabetRecord tempRecord = new alphabetRecord();
        //Command selectedCommand = commandTable.getSelectionModel().getSelectedItem();
        boolean okClicked = showAlphabetEditDialog(tempRecord);
        if(okClicked) {
            redactorModule.getAlphabetsData().add(tempRecord);
        }

        R_pro updated = new R_pro("1.0", redactorModule.getR_pro().getProgname(), redactorModule.getDescriptive_part(), redactorModule.getAlg());
        redactorModule.updateR_pro(updated);

    }

    @FXML
    private void handleEditRecord() {

        alphabetRecord selectedRecord = alphabetsTable.getSelectionModel().getSelectedItem();
        showAlphabetEditDialog(selectedRecord);

        R_pro updated = new R_pro("1.0", redactorModule.getR_pro().getProgname(), redactorModule.getDescriptive_part(), redactorModule.getAlg());
        redactorModule.updateR_pro(updated);

    }
    
}
