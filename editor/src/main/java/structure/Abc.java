package structure;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import org.immutables.value.Value;

/**
 * Created by Alex on 27.11.2016.
 */

//@Value.Immutable
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "abc")
public class Abc {

    @JacksonXmlProperty(isAttribute = true)
    String name;

    @JacksonXmlProperty(isAttribute = true)
    String short_name;

    @JacksonXmlProperty(isAttribute = true)
    String description;

    public String getName() {
        return name;
    }

    public String getShort_name() {
        return short_name;
    }

    public String getDescription() {
        return description;
    }

    public String getContents() {
        return contents;
    }

    @JacksonXmlProperty

    @JacksonXmlText
    String contents;

    public Abc(String name, String contents) {
        this.name = name;
        this.contents = contents;
    }

    public Abc(String name, String short_name, String contents) {
        this.name = name;
        this.short_name = short_name;
        this.contents = contents;
    }

    public Abc() {
    }

    public Abc(String name, String short_name, String description, String contents) {
        this.name = name;
        this.short_name = short_name;
        this.description = description;
        this.contents = contents;
    }
}
