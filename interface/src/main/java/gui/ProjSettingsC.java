package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.ProjFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Observable;

/**
 * Created by svkreml on 26.02.2017.
 */
public class ProjSettingsC {
    @FXML
    public RadioButton radioTestRun;
    @FXML
    public RadioButton radioInputFiles;
    @FXML
    public RadioButton radioConsole;
    @FXML
    public ChoiceBox chooseFile;
    @FXML
    public ChoiceBox chooseInputFile;
    Stage stage;
    ProjFile projFile;

    @FXML
    TextField textField;
    @FXML
    CheckBox checkBox;
    MainApp mainApp;
    String folder;
    ObservableList<String> files = FXCollections.observableArrayList();

    public void init(MainApp mainApp) {
        this.mainApp = mainApp;
        //textField.setText(projFile.getLentaPath());
        //checkBox.setSelected(projFile.isLenta());
        setRadio(projFile.getRunType());
        //if(projFile.getLentaPath()==null)projFile.setLentaPath("Тестовые данные");
        setChooseInputFile();
        if (!checkRadio().equals("console")) {
            setChooseFile(mainApp.getProjectR().getProjFile().getPath().toString() + "\\" + checkRadio());

        } else {
            chooseFile.setDisable(true);
           // chooseInputFile.setDisable(true);
        }
    }

    public String checkRadio() {
        if (radioConsole.isSelected())
            return "console";
        if (radioInputFiles.isSelected())
            return "Входные данные";
        if (radioTestRun.isSelected())
            return "Тестовые данные";
        return null;
    }

    public void setRadio(String type) {
        if (type == null || type.equals("console")) {
            radioConsole.setSelected(true);
        } else if (type.equals("Входные данные")) {
            radioInputFiles.setSelected(true);
            folder = "Входные данные";
        } else if (type.equals("Тестовые данные")) {
            radioTestRun.setSelected(true);
            folder = "Тестовые данные";
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void Apply(ActionEvent actionEvent) {
        // if(checkBox.isSelected())

        // projFile.setLenta(checkBox.isSelected());
        if(getLentaPath().equals("")) setRadio("console");
        projFile.setRunType(checkRadio());
        projFile.setLentaPath(getLentaPath());
        projFile.setInputPath(getInputPath());
        projFile.save();
    }

    public void Quit(ActionEvent actionEvent) {
        {
            stage.close();
        }
    }

    public void setProjFile(ProjFile projFile) {
        this.projFile = projFile;
    }

    public void setConsoleInput(ActionEvent actionEvent) {
        chooseFile.setDisable(true);
      //  chooseInputFile.setDisable(true);
    }

    public void setFileInput(ActionEvent actionEvent) {
        setChooseFile(mainApp.getProjectR().getProjFile().getPath().toString() + "\\Входные данные");
        folder = "Входные данные";
    }

    public void setFileTest(ActionEvent actionEvent) {
        setChooseFile(mainApp.getProjectR().getProjFile().getPath().toString() + "\\Тестовые данные");
        folder = "Тестовые данные";
    }

    void setChooseFile(String file) {
        // if(file==null) file="Тестовые данные";
        files = FXCollections.observableArrayList();
        chooseFile.setDisable(false);
        try {
            Files.list(Paths.get(file)).forEach(path -> {
                files.add(path.getFileName().toString());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        chooseFile.setItems(files);
        for (String s : files) {
            if(s.equals(projFile.getLentaPath()))
                chooseFile.getSelectionModel().select(s);
        }
    }
    void setChooseInputFile() {
        // if(file==null) file="Тестовые данные";
        String file = mainApp.getProjectR().getProjFile().getPath().toString()+"\\Входные данные";
        files = FXCollections.observableArrayList();
        //chooseInputFile.setDisable(false);
        try {
            Files.list(Paths.get(file)).forEach(path -> {
                files.add(path.getFileName().toString());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        chooseInputFile.setItems(files);
        for (String s : files) {
            if(s.equals(projFile.getInputPath()))
                chooseInputFile.getSelectionModel().select(s);
        }
    }

    String getLentaPath() {
        String s;
        if (chooseFile.getSelectionModel().getSelectedItem() == null) {
            s = "";
        } else s = chooseFile.getSelectionModel().getSelectedItem().toString();
        return s;
    }
    String getInputPath() {
        String s;
        if (chooseInputFile.getSelectionModel().getSelectedItem() == null) {
            s = "";
        } else s = chooseInputFile.getSelectionModel().getSelectedItem().toString();
        return s;
    }
}
