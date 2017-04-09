package redactorGui.alphabets;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Alex on 20.11.2016.
 */

@XmlRootElement
public class alphabetRecord {
    private final StringProperty synterm;
    private final StringProperty name;
    private final StringProperty shortName;
    private final StringProperty values;
    private final StringProperty comments;

    public alphabetRecord() {
        this.synterm = new SimpleStringProperty("синтерм");
        this.name = new SimpleStringProperty("");
        this.shortName = new SimpleStringProperty("");
        this.values = new SimpleStringProperty("");
        this.comments = new SimpleStringProperty("");
    }

    public String getSynterm() {
        return synterm.get();
    }

    public void setSynterm(String synterm) {
        this.synterm.set(synterm);
    }

    public StringProperty syntermProperty() {
        return synterm;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getShortName() {
        return shortName.get();
    }

    public void setShortName(String shortName) {
        this.shortName.set(shortName);
    }

    public StringProperty shortNameProperty() {
        return shortName;
    }

    public String getValues() {
        return values.get();
    }

    public void setValues(String values) {
        this.values.set(values);
    }

    public StringProperty valuesProperty() {
        return values;
    }

    public String getComments() {
        return comments.get();
    }

    public void setComments(String comments) {
        this.comments.set(comments);
    }

    public StringProperty commentsProperty() {
        return comments;
    }
    
}
