package structure;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sun.javafx.collections.ImmutableObservableList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Created by Alex on 27.11.2016.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "columnsName")
public class ColumnsName {

    public void setColumn(List column) {
        this.column.addAll(column);
    }

    @JacksonXmlProperty
    ObservableList<String> column = FXCollections.observableArrayList();             // массив

    public ColumnsName(ObservableList<String> column) {
        this.column = column;
    }
}
