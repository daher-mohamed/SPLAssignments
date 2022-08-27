package src.main.java.bgu.spl.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Data {
    /**
     * Enum representing the Data type.
     */
    enum Type {
        Images, Text, Tabular
    }
	public String name;
	private Type s_type;
	private String type;
    private int size;
	//private int processed;
    
    public Data(Type type, int processed, int size) {
		super();
		this.s_type = type;
		//this.processed = processed;
		this.size = size;
	}

	public String getType() {
		return type;

	}

	public void setType(Type type) {
		this.s_type = type;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
