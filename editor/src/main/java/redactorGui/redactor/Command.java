package redactorGui.redactor;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Alex on 23.10.2016.
 */


@XmlRootElement
public class Command {

    private final StringProperty metka;

    private final StringProperty uslovieLeft;
    private final StringProperty uslovieCenter;
    private final StringProperty uslovieRight;

    private final StringProperty linopLeft;
    private final StringProperty linopCenter;
    private final StringProperty linopRight;

    private final StringProperty metkaPerehoda;
    private final StringProperty comments;
    private Flags flag;
    private PredicateTypes predicateType;

    /**
     * Конструктор по умолчанию.
     */
    public Command() {
        this.metka = new SimpleStringProperty("");

        this.uslovieLeft = new SimpleStringProperty("");
        this.uslovieCenter = new SimpleStringProperty("");
        this.uslovieRight = new SimpleStringProperty("");

        this.linopLeft = new SimpleStringProperty("");
        this.linopCenter = new SimpleStringProperty("");
        this.linopRight = new SimpleStringProperty("");

        this.metkaPerehoda = new SimpleStringProperty("");
        
        this.comments = new SimpleStringProperty("");
    }

    public Command(String metka, 
                   String uslovieLeft, String uslovieCenter, String uslovieRight, 
                   String linopLeft, String linopCenter, String linopRight,
                   String metkaPerehoda, 
                   String comments, 
                   Flags flag) {
        this.metka = new SimpleStringProperty(metka);
        
        this.uslovieLeft = new SimpleStringProperty(uslovieLeft);
        this.uslovieCenter = new SimpleStringProperty(uslovieCenter);
        this.uslovieRight = new SimpleStringProperty(uslovieRight);
        
        this.linopLeft = new SimpleStringProperty(linopLeft);
        this.linopCenter = new SimpleStringProperty(linopCenter);
        this.linopRight = new SimpleStringProperty(linopRight);
        
        this.metkaPerehoda = new SimpleStringProperty(metkaPerehoda);
        
        this.comments = new SimpleStringProperty(comments);
        
        this.flag = flag;
    }

    public String getMetka() {
        return metka.get();
    }

    public void setMetka(String metka) {
        this.metka.set(metka);
    }

    public StringProperty metkaProperty() {
        return metka;
    }

    // -------------------------------------------------------------------

    public String getUslovieLeft() {
        return uslovieLeft.get();
    }

    public void setUslovieLeft(String uslovieLeft) {
        this.uslovieLeft.set(uslovieLeft);
    }

    // -------------------------------------------------------------------

    public String getUslovieCenter() {
        return uslovieCenter.get();
    }

    public void setUslovieCenter(String uslovieCenter) {
        this.uslovieCenter.set(uslovieCenter);
    }

    // -------------------------------------------------------------------

    public String getUslovieRight() {
        return uslovieRight.get();
    }

    public void setUslovieRight(String uslovieRight) {
        this.uslovieRight.set(uslovieRight);
    }

    // -------------------------------------------------------------------

    /**
     * Костыль, связанный с тем, что на 1 колонку таблицы приходится сразу 3 объекта StringProperty
     * Пока не знаю, как это обойти, поэтому
     * @return возвращает конкатенацию 3 частей условия, разделяя их пробелом
     */

    public StringProperty uslovieProperty() {
        //System.out.println(uslovieProperty.get());
        if (uslovieLeft.get().isEmpty() && uslovieRight.get().isEmpty()) {
            return new SimpleStringProperty(uslovieCenter.get());
        } else {
            return new SimpleStringProperty(uslovieLeft.get() + " " + uslovieCenter.get() + " " + uslovieRight.get());
        }
    }

    // -------------------------------------------------------------------

    public String getLinopLeft() {
        return linopLeft.get();
    }

    public void setLinopLeft(String linopLeft) {
        this.linopLeft.set(linopLeft);
    }

    // -------------------------------------------------------------------

    public String getLinopCenter() {
        return linopCenter.get();
    }

    public void setLinopCenter(String linopCenter) {
        this.linopCenter.set(linopCenter);
    }

    // -------------------------------------------------------------------

    public String getLinopRight() {
        return linopRight.get();
    }

    public void setLinopRight(String linopRight) {
        this.linopRight.set(linopRight);
    }

    // -------------------------------------------------------------------

    /**
     * Костыль, связанный с тем, что на 1 колонку таблицы приходится сразу 3 объекта StringProperty
     * Пока не знаю, как это обойти, поэтому
     * @return возвращает конкатенацию 3 частей линейного оператора, разделяя их пробелом
     */

    public StringProperty linopProperty() {
        if (linopLeft.get().isEmpty() && linopRight.get().isEmpty()) {
            return new SimpleStringProperty(linopCenter.get());
        } else {
            return new SimpleStringProperty(linopLeft.get() + " " + linopCenter.get() + " " + linopRight.get());
        }
    }

    // -------------------------------------------------------------------

    public String getMetkaPerehoda() {
        return metkaPerehoda.get();
    }

    public void setMetkaPerehoda(String metkaPerehoda) {
        this.metkaPerehoda.set(metkaPerehoda);
    }

    public StringProperty metkaPerehodaProperty() {
        return metkaPerehoda;
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


    public Flags getFlag() { 
        return flag; 
    }

    public void setFlag(Flags newFlag) { 
        this.flag = newFlag; 
    }

    // -------------------------------------------------------------------

    public PredicateTypes getPredicateType() {
        return predicateType;
    }

    public void setPredicateType(PredicateTypes predicateType) {
        this.predicateType = predicateType;
    }
}
