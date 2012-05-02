package settings;

public class ViabilityPair implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Float value;
	private Vocabulary.Param param;

	public ViabilityPair(Float value, Vocabulary.Param param) {
		this.value = value;
		this.param = param;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public void setParam(Vocabulary.Param param) {
		this.param = param;
	}

	public Float getValue() {
		return value;
	}

	public Vocabulary.Param getParam() {
		return param;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;

		ViabilityPair pair = (ViabilityPair) obj;
		return pair.value == value && pair.param == param;
	}

	public int hashCode() {
		return value.hashCode() * 1000 + param.hashCode();
	}
}