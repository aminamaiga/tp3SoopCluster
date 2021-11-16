package clustering;

import java.util.List;

public class Infos {

	String classeName;
	String methods;

	public Infos(String classe, String methods) {
		this.classeName = classe;
		this.methods = methods;
	}

	public String getClasse() {
		return classeName;
	}

	public void setClasse(String classe) {
		this.classeName = classe;
	}

	public String getMethods() {
		return methods;
	}

	public void setMethods(String methods) {
		this.methods = methods;
	}

	@Override
	public String toString() {
		return "Infos [classe=" + classeName + ", methods=" + methods + ", getClasse()=" + getClasse()
				+ ", getMethods()=" + getMethods() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

}