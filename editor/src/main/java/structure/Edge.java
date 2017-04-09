package structure;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Created by Alex on 27.11.2016.
 */

//@Value.Immutable
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "edge")
public class Edge {

    public String getEnd() {
        return end;
    }

    @JacksonXmlProperty(isAttribute = true)
    String end;

    public Edge() {
    }

    public Predicate getPredicate() {
        return predicate;
    }

    @JacksonXmlProperty
    Predicate predicate;

    public ObservableList<Operation> getOperation() {
        return operation;
    }

    @JacksonXmlProperty
    ObservableList<Operation> operation = FXCollections.observableArrayList();     // массив

    public void setOperation(List<Operation> operation) {
        this.operation.addAll(operation);
    }

    public Edge(Predicate predicate, Operation operation) {
        this.predicate = predicate;
        this.operation.add(operation);
    }

    public void addOperation(Operation operation) {
        this.operation.add(operation);
    }

    public void addEnd(String end) {
        this.end = end;
    }
}
