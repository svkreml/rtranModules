package gui.window.makery.address.model;/**
 * Created by Anton on 05.12.2016.
 */

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
public class ProgramWindow extends Application {

    private Stage      primaryStage;
    private Stage      myStage;
    private BorderPane rootLayout;
    private TextArea   textArea;
    private TextField  textField;
    private Button     inputButton;
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

    public TextArea  getTextArea() { return textArea; }
    public TextField getTextField() { return textField; }
    public Button    getInputButton() {return inputButton; }
    public Stage     getPrimaryStage() {
        return primaryStage;
    }
    /**
     * Инициализирует корневой макет.
     */

    @FXML
    public void initRootLayout() {
        rootLayout = new BorderPane();
        // Отображаем сцену, содержащую корневой макет.


        GridPane grid = new GridPane();
        grid.setPadding(new Insets(2));
        grid.setHgap(2);
        grid.setVgap(2);

        textArea = new TextArea();
        textField = new TextField();
        inputButton = new Button("Ввод");
        grid.addRow(0, new Label("Ход программы:"), textArea);
        grid.addRow(1, new Label("Поле ввода:"), textField);
//        rootLayout.setCenter(textArea);
//        rootLayout.setBottom(textField);
        grid.addColumn(2, new Label(""), inputButton);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textField, Priority.ALWAYS);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        Scene scene = new Scene(grid);
        myStage = new Stage();
        myStage.setScene(scene);
        myStage.initModality(Modality.WINDOW_MODAL);
        myStage.initOwner(primaryStage);
        myStage.setResizable(false);
        myStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}