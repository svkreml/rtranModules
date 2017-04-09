package structure;

/**
 * Created by Alex on 27.11.2016.
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "left")
public class Left {

    public Left() {
    }

    public String getValue() {
        return value;
    }

    @JacksonXmlProperty(isAttribute = true)
    String value;

    public Left(String value) {
        this.value = value;
    }
}
