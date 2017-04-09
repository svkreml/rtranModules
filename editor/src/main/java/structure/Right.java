package structure;

/**
 * Created by Alex on 27.11.2016.
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "right")
public class Right {

    public Right() {
    }

    public String getValue() {
        return value;
    }

    @JacksonXmlProperty(isAttribute = true)
    String value;

    public Right(String value) {
        this.value = value;
    }
}
