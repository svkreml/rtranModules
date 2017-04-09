package redactorGui.memoryTypes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Alex on 20.11.2016.
 */
@XmlRootElement
public class memoryTypeRecord {
    
    private final StringProperty type;
    private final StringProperty name;
    private final StringProperty comments;

    public memoryTypeRecord() {

        this.type = new SimpleStringProperty("");
        this.name = new SimpleStringProperty("");
        this.comments = new SimpleStringProperty("");
        
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) { 
        this.type.set(type); 
    }

    public StringProperty typeProperty() {
        return type;
    }

    // -------------------------------------------------------------------

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    // -------------------------------------------------------------------

    public String getComments() {
        return comments.get();
    }

    public void setComments(String comments) {
        this.comments.set(comments);
    }

    public StringProperty commentsProperty() {
        return comments;
    }

    // -------------------------------------------------------------------
    
}
