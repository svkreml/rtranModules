package structure;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * Created by Alex on 27.11.2016.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "arm")
public class Arm {

    public String getBegin() {
        return begin;
    }

    @JacksonXmlProperty(isAttribute = true)
    String begin;

    public ObservableList<Edge> getEdge() {
        return edge;
    }

    @JacksonXmlProperty
    ObservableList<Edge> edge = FXCollections.observableArrayList();         // массив

    public Arm() {
    }

    public Arm(String begin) {
        this.begin = begin;
    }

    public void setEdge(ArrayList<Edge> edge) {
        this.edge.addAll(edge);
    }

    public void addEdge(Edge edge) {
        this.edge.add(edge);
    }

    public void updateEdge(Edge edge) {
        this.edge.add(this.edge.size()-1, edge);
    }

    public Edge getEdge(int index) {
        return edge.get(index);
    }

}
