package filesystem;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.nio.file.Path;

/**
 * Created by svkreml on 26.02.2017.
 */

@JacksonXmlRootElement(localName = "file")
public class ProgramInfo extends FileInfo {

    public ProgramInfo(String name, Path path) {
        super(name, path);
    }
}
