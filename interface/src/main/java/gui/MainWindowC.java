package gui;

import Other.StarterMain;
import gui.help.HelpWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.Explorer;
import project.LastOpened;
import project.MyTab;
import project.ProjectR;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by svkreml on 26.02.2017.
 */
public class MainWindowC {
    MainApp mainApp;
    @FXML
    Button btnSettings;
    @FXML
    Button btnPlay;
    @FXML
    Button btnBug;
    @FXML
    Button btnSave;
    @FXML
    TextArea testTextArea;
    @FXML
    TabPane redactorTabs;
    TreeC treeController;
    Explorer explorer;
    @FXML
    private TextFlow errorConsole;
    @FXML
    private AnchorPane treeViewPane;

    static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public AnchorPane getTreeViewPane() {
        return treeViewPane;
    }

    public TextFlow getErrorConsole() {
        return errorConsole;
    }

    public TextArea getTestTextArea() {
        return testTextArea;
    }

    public TabPane getRedactorTabs() {
        return redactorTabs;
    }

    public void setB(boolean bool) {
        btnSettings.setDisable(bool);
        btnPlay.setDisable(bool);
        btnBug.setDisable(bool);
        btnSave.setDisable(bool);
    }

    public void newProject(ActionEvent actionEvent) {
        try {
            // Загружаем корневой макет из fxml файла.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("NewProject.fxml"));
            GridPane NewProject = loader.load();

            Stage newProjectStage = new Stage();
            newProjectStage.setTitle("Создание нового проекта");
            newProjectStage.initModality(Modality.WINDOW_MODAL);
            newProjectStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(NewProject);
            newProjectStage.setScene(scene);
            NewProjectC controller = loader.getController();
            controller.setNewProjectStage(newProjectStage);
            controller.setMainApp(mainApp);
            newProjectStage.showAndWait();
            mainApp.getPrimaryStage().show();
            //fixme
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openProject(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Открытие проекта");
        File defaultDirectory = new File("c:/");
        chooser.setInitialDirectory(defaultDirectory);
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("R-tran", "project.rpro"));
        File projectDirectory = chooser.showOpenDialog(mainApp.getPrimaryStage());
        if (projectDirectory == null) return;
        openPr(projectDirectory);
    }

    public void openPr(File projectDirectory) {
        mainApp.setProjectR(new ProjectR(projectDirectory, false));
        LastOpened.save(projectDirectory);
        TreeC treeController = mainApp.getTreeController();
        treeController.setRedactorTabs(redactorTabs);
        treeController.setExplorer();
        treeController.openProject(projectDirectory.toPath());
        explorer = treeController.getExplorer();
    }

    public void openPrLast(ActionEvent actionEvent) {
        File projectDirectory = LastOpened.load();
        if (projectDirectory == null) return;
        openPr(projectDirectory);
    }

    public void handleSave(ActionEvent actionEvent) {
    }

    public void closeProject(ActionEvent actionEvent) {
// FIXME: 27.02.2017 Не работает почему-то...
        setB(true);
        List<MyTab> tabs = (List) redactorTabs.getTabs();
        System.out.println("tabs.size() = " + tabs.size());
        for (int i = 0; i < tabs.size(); i++) {
            System.out.println("tabs = " + tabs.get(i).getText());
            if (explorer.saveDialog()) explorer.saveTab(tabs.get(i));
            redactorTabs.getTabs().remove(0);
        }
        explorer.getTreeView().setRoot(null);
    }

    public void closeProgram(ActionEvent actionEvent) {
    }

    public void help(ActionEvent actionEvent) {
        new HelpWindow(mainApp, null);
    }

    public void about(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("О программе");
        alert.setHeaderText("R-tran Редактор");
        alert.setContentText("версия 1.0.0\nИВБО-03-13 МИРЭА кафедра ВОСХОД");
        alert.showAndWait();
    }

    public void saveTextArea(ActionEvent actionEvent) {
        explorer.saveTab();
    }

    public void settings(ActionEvent actionEvent) {
        try {
            // if(pr==null) return;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("ProjSettings.fxml"));
            AnchorPane NewProject = loader.load();
            ProjSettingsC controller = loader.getController();
            Stage settingsStage = new Stage();
            controller.setStage(settingsStage);
            settingsStage.setTitle("Настройки проекта");
            settingsStage.initModality(Modality.WINDOW_MODAL);
            settingsStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(NewProject);
            settingsStage.setScene(scene);
            controller.setStage(settingsStage);
            controller.setProjFile(mainApp.getProjectR().getProjFile());
            controller.init(mainApp);

            settingsStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void debugRmachine(ActionEvent actionEvent) {
        runM(true);
    }

    @FXML
    public void runRmachine(ActionEvent actionEvent) {
        runM(false);
    }

    private void runM(boolean debugType) {
        String lenta = null;
        String file = null;
        if (!mainApp.getProjectR().getProjFile().getRunType().equals("console")) {
            try {
                lenta = Explorer.readFile(Paths.get(mainApp.getProjectR().getProjFile().getPath().toString(),mainApp.getProjectR().getProjFile().getRunType(), mainApp.getProjectR().getProjFile().getLentaPath()), Charset.defaultCharset());
                file = String.valueOf(Paths.get(mainApp.getProjectR().getProjFile().getPath().toString(),mainApp.getProjectR().getProjFile().getRunType(), mainApp.getProjectR().getProjFile().getInputPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (lenta == null || lenta.equals("")) lenta = null;
        System.out.println(mainApp.getProjectR().getProjFile().getPath() + "\\program.rtran");
        System.out.println("lenta = " + lenta);

        //костыль
        //lenta = "perfectapple#";
        try {
            StarterMain starterMain = new StarterMain(mainApp.getProjectR().getProjFile().getPath() + "\\program.rtran", debugType, lenta, file, this.mainApp.getPrimaryStage());
//            starterMain.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
//perfectapple#