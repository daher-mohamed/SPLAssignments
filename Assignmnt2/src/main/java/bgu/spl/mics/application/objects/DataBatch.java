package src.main.java.bgu.spl.mics.application.objects;
/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit(including public methods and constructors).
 */
public class DataBatch {
    private Data data;
	private int StartIndex;
    private boolean ISprocced;
    public DataBatch(Data data, int startIndex) {
		super();
		this.data = data;
		StartIndex = startIndex;
		ISprocced = false;
	}
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
	public int getStartIndex() {
		return StartIndex;
	}
	public void setStartIndex(int startIndex) {
		StartIndex = startIndex;
	}
	public boolean isISprocced() {
		return ISprocced;
	}
	public void setISprocced(boolean iSprocced) {
		ISprocced = iSprocced;
	}

}
