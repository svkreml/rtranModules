package xml;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import structure.R_pro;

import java.io.*;

/**
 * Created by Alex on 28.11.2016.
 */
public class Loader {

    public R_pro getReaded() {
        return readed;
    }

    R_pro readed;

    private File location;

    public File getLocation() {
        return location;
    }

    public void setLocation(File location) {
        this.location = location;
    }

    public Loader() {
    }

    public LoadingResult load(File location) {
        this.location = location;
        if (location != null) {
            JacksonXmlModule module = new JacksonXmlModule();
            module.setDefaultUseWrapper(false);
            XmlMapper xmlMapper = new XmlMapper(module);
            try {
                BufferedReader read = new BufferedReader(new FileReader(location));
                String program = read.readLine();
                readed = xmlMapper.readValue(program, R_pro.class);
            } catch (IOException e) {
                Alert emptyFileErrorWindow = new Alert(Alert.AlertType.ERROR);
                emptyFileErrorWindow.setTitle("Ошибка открытия файла");
                emptyFileErrorWindow.setHeaderText("При открытии файла произошла ошибка!");
                emptyFileErrorWindow.setContentText("Файл поврежден!");
                emptyFileErrorWindow.showAndWait();
                //e.printStackTrace();
                return LoadingResult.BROKEN;
            } catch (NullPointerException e) {
                //System.out.println(this);
                return LoadingResult.EMPTY;
            }
            return LoadingResult.SUCCESS;
        } else {
            return LoadingResult.NULL;
        }
    }

}
