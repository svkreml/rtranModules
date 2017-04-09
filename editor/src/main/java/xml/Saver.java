package xml;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import structure.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Alex on 27.11.2016.
 */
public class Saver {

    public File getKnownDestination() {
        return knownDestination;
    }

    public void setKnownDestination(File destination) {
        this.knownDestination = destination;
    }

    private File knownDestination;

    public boolean saveAs(R_pro r_pro, File destination) {
        if (destination != null) {
            String progname = StringUtils.removeEnd(destination.getName(), ".rtran");
            r_pro.setProgname(progname);
            JacksonXmlModule module = new JacksonXmlModule();
            module.setDefaultUseWrapper(false);
            XmlMapper xmlMapper = new XmlMapper(module);
            try {
                xmlMapper.writeValue(destination, r_pro);
                Alert successWindow = new Alert(Alert.AlertType.CONFIRMATION);
                successWindow.setTitle("Сохранение файла");
                successWindow.setHeaderText("Файл сохранен");
                successWindow.setContentText("Сохранение прошло успешно!");
                successWindow.showAndWait();
                return true;
            } catch (IOException e) {
                Alert fileIOErrorWindow = new Alert(Alert.AlertType.ERROR);
                fileIOErrorWindow.setTitle("Сохранение файла");
                fileIOErrorWindow.setHeaderText("При сохранении файла произошла ошибка!");
                fileIOErrorWindow.setContentText(e.getMessage());
                fileIOErrorWindow.showAndWait();
                return false;
            }
        } else {
            return false;
        }
    }
    
    public boolean save(R_pro r_pro) {
        String progname = StringUtils.removeEnd(knownDestination.getName(), ".rtran");
        r_pro.setProgname(progname);
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        try {
            xmlMapper.writeValue(knownDestination, r_pro);
            Alert successWindow = new Alert(Alert.AlertType.CONFIRMATION);
            successWindow.setTitle("Сохранение файла");
            successWindow.setHeaderText("Файл сохранен");
            successWindow.setContentText("Сохранение прошло успешно!");
            successWindow.showAndWait();
            return true;
        } catch (IOException e) {
            Alert fileIOErrorWindow = new Alert(Alert.AlertType.ERROR);
            fileIOErrorWindow.setTitle("Сохранение файла");
            fileIOErrorWindow.setHeaderText("При сохранении файла произошла ошибка!");
            fileIOErrorWindow.setContentText(e.getMessage());
            fileIOErrorWindow.showAndWait();
            return false;
        }
    }
    
}
