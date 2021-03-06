package xml;

import com.ctc.wstx.exc.WstxEOFException;
import com.ctc.wstx.exc.WstxUnexpectedCharException;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import structure.Abc;
import structure.R_pro;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

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
    public static String readFile(Path path, Charset encoding) throws IOException {

        byte[] encoded = Files.readAllBytes(path);
        return new String(encoded, encoding);
    }
    public LoadingResult load(File location) {
        this.location = location;
        if (location != null) {
            JacksonXmlModule module = new JacksonXmlModule();
            module.setDefaultUseWrapper(false);
            XmlMapper xmlMapper = new XmlMapper(module);
            try {
//                BufferedReader buffer = new BufferedReader(new FileReader(location));
//                String program = buffer.lines().collect(Collectors.joining());
                String program = readFile(location.toPath(), Charset.forName("UTF-8"));
                readed = xmlMapper.readValue(program, R_pro.class);

                for (Abc abc : readed.getDescriptive_part().getAlphabet().getAbc()) {
                    if (abc.getContents() == null) abc.setContents(" ");
                }
            } catch (IOException e) {

                if (e.getCause().getClass().equals(WstxUnexpectedCharException.class)) {
                    Alert emptyFileErrorWindow = new Alert(Alert.AlertType.ERROR);
                    emptyFileErrorWindow.setTitle("Ошибка открытия файла");
                    emptyFileErrorWindow.setHeaderText("При открытии файла произошла ошибка!");
                    emptyFileErrorWindow.setContentText("Файл поврежден!");
                    emptyFileErrorWindow.showAndWait();
                    return LoadingResult.BROKEN;
                } else if (e.getCause().getClass().equals(WstxEOFException.class)) {
                    return LoadingResult.EMPTY;
                }
            }
            return LoadingResult.SUCCESS;
        } else {
            return LoadingResult.NULL;
        }
    }

}
