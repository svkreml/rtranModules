package structure;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

/**
 * Created by Alex on 27.11.2016.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "predicate")
public class Predicate {

    public Predicate() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JacksonXmlProperty(isAttribute = true)
    String type;

    @JacksonXmlProperty(isAttribute = true)
    String name;

    @JacksonXmlProperty(isAttribute = true)
    String value;

    public Memory getMemoryLeft() {
        return memoryLeft;
    }

    public String getSign() {
        return sign;
    }

    public Memory getMemoryRight() {
        return memoryRight;
    }

    @JacksonXmlProperty(localName = "left")
    Memory memoryLeft;

    @JacksonXmlProperty
    String sign;

    @JacksonXmlProperty(localName = "right")
    Memory memoryRight;

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    @JacksonXmlProperty
    @JacksonXmlText

    String contents;


    public Predicate(String type, String contents) {
        this.type = type;
        this.contents = contents;
    }

    public Predicate(String type, String nameOrValue, boolean setValue) {
        this.type = type;
        if (setValue) {
            this.value = nameOrValue;
        } else {
            this.name = nameOrValue;
        }
    }

    public Predicate(String type, Memory memoryLeft, String sign, Memory memoryRight) {
        this.type = type;
        this.memoryLeft = memoryLeft;
        this.sign = sign;
        this.memoryRight = memoryRight;
    }


}
