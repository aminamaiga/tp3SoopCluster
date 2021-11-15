package clustering;

public class MapReduce {
	String classeName;
	int compter;

	public MapReduce(String classeName, int compter) {
		super();
		this.classeName = classeName;
		this.compter = compter;
	}

	public String getClasseName() {
		return classeName;
	}

	public void setClasseName(String classeName) {
		this.classeName = classeName;
	}

	public int getCompter() {
		return compter;
	}

	public void setCompter(int compter) {
		this.compter = compter;
	}

	@Override
	public String toString() {
		return "Couple [classeName=" + classeName + ", linkage=" + compter + "]";
	}

}
