package structure;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sun.javafx.collections.ImmutableObservableList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import structure.Abc;

import java.util.List;

/**
 * Created by Alex on 27.11.2016.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "alphabet")
public class Alphabet {

    public void setAbc(List<Abc> abc) {
        this.abc.addAll(abc);
    }

    public ObservableList<Abc> getAbc() {
        return abc;
    }

    @JacksonXmlProperty
    ObservableList<Abc> abc = FXCollections.observableArrayList();                         // массив

    public Alphabet() {
    }

    public Alphabet(ObservableList<Abc> abcs) {
        this.abc = abcs;
    }
}
