package Other;

/**
 * Created by Admin on 25.03.2017.
 */
public class StringBox {
	volatile String value;

	public void setValue(String value) {
		this.value = value;
	}

	public StringBox(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
