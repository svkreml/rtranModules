package redactorGui.redactor;

import com.google.common.io.Resources;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextAlignment;
import org.apache.commons.lang3.StringUtils;
import redactorGui.RedactorModule;
import redactorGui.redactor.addNewLine.addNewLineController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import structure.R_pro;

import java.io.IOException;

/**
 * Created by svkreml on 23.10.2016.
 */
public class RedactorController {


    @FXML
    private TableView<Command> commandTable;

    @FXML
    private TableColumn<Command, String> metkaColumn;

    @FXML
    private TableColumn<Command, String> uslovieColumn;

    @FXML
    private TableColumn<Command, String> linopColumn;

    @FXML
    private TableColumn<Command, String> metkaPerehodaColumn;

    @FXML
    private TableColumn<Command, String> commentsColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    private RedactorModule redactorModule;

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    public RedactorController() {
    }

    public void clear() {
        commandTable.setItems(FXCollections.observableArrayList());
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

            String emptyMessage = "Здесь будут отображаться команды R-программы в табличном виде." + "\n";
            emptyMessage += "Начать работу над R-программой рекомендуется с заполнения" + "\n";
            emptyMessage += "описательной части (вкладки «Память» и «Синтермы»)." + "\n";

            Label placeholder = new Label(emptyMessage);
            placeholder.setTextAlignment(TextAlignment.CENTER);

            commandTable.setPlaceholder(placeholder);

        } catch (IOException e) {
            e.printStackTrace();
        }

        metkaColumn.setCellValueFactory(cellData -> cellData.getValue().metkaProperty());
        uslovieColumn.setCellValueFactory(cellData -> cellData.getValue().uslovieProperty());
        linopColumn.setCellValueFactory(cellData -> cellData.getValue().linopProperty());
        metkaPerehodaColumn.setCellValueFactory(cellData -> cellData.getValue().metkaPerehodaProperty());
        commentsColumn.setCellValueFactory(cellData -> cellData.getValue().commentsProperty());

        commandTable.setRowFactory(ct -> {
            TableRow<Command> row = new TableRow<>();

            final ContextMenu contextMenu = new ContextMenu();
            MenuItem addAbove = new MenuItem("Добавить команду выше");
            MenuItem addBelow = new MenuItem("Добавить команду ниже");

            try {
                Image arrowUp = new Image(Resources.getResource("ic_arrow_upward_black_24dp_1x.png").openStream());
                addAbove.setGraphic(new ImageView(arrowUp));

                Image arrowDown = new Image(Resources.getResource("ic_arrow_downward_black_24dp_1x.png").openStream());
                addBelow.setGraphic(new ImageView(arrowDown));

            } catch (IOException e) {
                e.printStackTrace();
            }


            addAbove.setOnAction(event -> {

                Command tempCommand = new Command();
                boolean okClicked = showCommandEditDialog(tempCommand);
                if(okClicked) {
                    redactorModule.getCommandData().add(row.getIndex(), tempCommand);
                }

                try {
                    R_pro updated = new R_pro("1.0", redactorModule.getR_pro().getProgname(), redactorModule.getDescriptive_part(), redactorModule.getAlg());
                    redactorModule.updateR_pro(updated);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(redactorModule.getRedactorPane().getScene().getWindow());
                    alert.setTitle("Неправильный порядок команд");
                    alert.setHeaderText("Кажется, последовательность R-команд некорретна...");
                    alert.setContentText("Добавление привело к тому, что на данный момент R-команды " +
                            "расположены в неправильной последовательности. Во избежание ошибок времени выполнения, " +
                            "рекомендуется удалить команду."
                    );
                    alert.showAndWait();
                }



            });

            addBelow.setOnAction(event -> {

                Command tempCommand = new Command();
                boolean okClicked = showCommandEditDialog(tempCommand);
                if(okClicked) {
                    redactorModule.getCommandData().add(row.getIndex() + 1, tempCommand);
                }
                try {
                    R_pro updated = new R_pro("1.0", redactorModule.getR_pro().getProgname(), redactorModule.getDescriptive_part(), redactorModule.getAlg());
                    redactorModule.updateR_pro(updated);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(redactorModule.getRedactorPane().getScene().getWindow());
                    alert.setTitle("Неправильный порядок команд");
                    alert.setHeaderText("Кажется, последовательность R-команд некорретна...");
                    alert.setContentText("Добавление привело к тому, что на данный момент R-команды " +
                            "расположены в неправильной последовательности. Во избежание ошибок времени выполнения, " +
                            "рекомендуется удалить команду."
                    );
                    alert.showAndWait();
                }

            });

            contextMenu.getItems().addAll(addAbove, addBelow);
            row.setContextMenu(contextMenu);

            row.setOnDragDetected(event -> {
                if(!row.isEmpty()) {
                    Integer index = row.getIndex();
                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {

                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    if (row.getIndex() != ((Integer)db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                    }
                }

            });

            row.setOnDragDropped(event -> {

                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                    Command dragged = commandTable.getItems().remove(draggedIndex);

                    int dropIndex ;

                    if (row.isEmpty()) {
                        dropIndex = commandTable.getItems().size() ;
                    } else {
                        dropIndex = row.getIndex();
                    }

                    commandTable.getItems().add(dropIndex, dragged);

                    event.setDropCompleted(true);
                    commandTable.getSelectionModel().select(dropIndex);
                    event.consume();
                    try {
                        R_pro updated = new R_pro("1.0", redactorModule.getR_pro().getProgname(), redactorModule.getDescriptive_part(), redactorModule.getAlg());
                        redactorModule.updateR_pro(updated);
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.initOwner(redactorModule.getRedactorPane().getScene().getWindow());
                        alert.setTitle("Неправильный порядок команд");
                        alert.setHeaderText("Кажется, последовательность R-команд некорретна...");
                        alert.setContentText("Перетаскивание привело к тому, что на данный момент R-команды " +
                                "расположены в неправильной последовательности. Во избежание ошибок времени выполнения, " +
                                "рекомендуется вернуть команду на ее прежнее место."
                        );
                        alert.showAndWait();
                        commandTable.refresh();
                        commandTable.refresh();
                        commandTable.refresh();
                    }
                }

            });

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    handleEditCommand();
                }
            });

            return row;
        });

    }

    private boolean showCommandEditDialog(Command command) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RedactorModule.class.getClassLoader().getResource("redactor/addNewLine/addNewLine.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Редактирование команды");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(redactorModule.getRedactorPane().getScene().getWindow());
            dialogStage.setResizable(false);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            addNewLineController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setRedactorModule(redactorModule);
            controller.setCommand(command);
            controller.loadOptions();

            dialogStage.showAndWait();

            return controller.isOkClicked();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    private void handleDeleteCommand() {
        int selectedIndex = commandTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            commandTable.getItems().remove(selectedIndex);
            try {
                R_pro updated = new R_pro("1.0", redactorModule.getR_pro().getProgname(), redactorModule.getDescriptive_part(), redactorModule.getAlg());
                redactorModule.updateR_pro(updated);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(redactorModule.getRedactorPane().getScene().getWindow());
                alert.setTitle("Неправильный порядок команд");
                alert.setHeaderText("Кажется, последовательность R-команд некорретна...");
                alert.setContentText("Удаление команды привело к тому, что на данный момент R-команды " +
                        "расположены в неправильной последовательности. Если есть возможность, " +
                        "рекомендуется восстановить команду на ее прежнем месте."
                );
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(redactorModule.getRedactorPane().getScene().getWindow());
            alert.setTitle("Ничего не выбрано");
            alert.setHeaderText("Не была выбрана ни одна команда");
            alert.setContentText("Пожалуйста, выберите команду в таблице");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAddNewLineCommand() {
        Command tempCommand = new Command();
        //Command selectedCommand = commandTable.getSelectionModel().getSelectedItem();
        boolean okClicked = showCommandEditDialog(tempCommand);
        if(okClicked) {
            redactorModule.getCommandData().add(tempCommand);
        }
        try {
            R_pro updated = new R_pro("1.0", redactorModule.getR_pro().getProgname(), redactorModule.getDescriptive_part(), redactorModule.getAlg());
            redactorModule.updateR_pro(updated);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(redactorModule.getRedactorPane().getScene().getWindow());
            alert.setTitle("Неправильный порядок команд");
            alert.setHeaderText("Кажется, последовательность R-команд некорретна...");
            alert.setContentText("Добавление команды привело к тому, что на данный момент R-команды " +
                    "расположены в неправильной последовательности. Во избежание ошибок времени выполнения, " +
                    "рекомендуется удалить команду."
            );
            alert.showAndWait();
        }
    }

    @FXML
    private void handleEditCommand() {

        int selectedIndex = commandTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Command selectedCommand = commandTable.getSelectionModel().getSelectedItem();
            boolean okClicked = showCommandEditDialog(selectedCommand);
            if(okClicked) {
                redactorModule.getCommandData().set(selectedIndex, selectedCommand);
            }

            try {
                R_pro updated = new R_pro("1.0", redactorModule.getR_pro().getProgname(), redactorModule.getDescriptive_part(), redactorModule.getAlg());
                redactorModule.updateR_pro(updated);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(redactorModule.getRedactorPane().getScene().getWindow());
                alert.setTitle("Неправильный порядок команд");
                alert.setHeaderText("Кажется, последовательность R-команд некорретна...");
                alert.setContentText("Редактирование команды привело к тому, что на данный момент R-команды " +
                        "расположены в неправильной последовательности. Во избежание ошибок времени выполнения, " +
                        "рекомендуется вернуть команду к ее прежнему состоянию."
                );
                alert.showAndWait();
            }


        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(redactorModule.getRedactorPane().getScene().getWindow());
            alert.setTitle("Ничего не выбрано");
            alert.setHeaderText("Не была выбрана ни одна команда");
            alert.setContentText("Пожалуйста, выберите команду в таблице");
            alert.showAndWait();
        }
    }

    public void setRedactorModule(RedactorModule redactorModule) {
        this.redactorModule = redactorModule;
        commandTable.setItems(redactorModule.getCommandData());
    }


}
