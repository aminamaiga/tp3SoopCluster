package clustering;

import java.util.ArrayList;
import java.util.List;

public class ExtactInformations {
	public List<Infos> classes = new ArrayList<>();

	public void add(Infos infos) {
		classes.add(infos);
	}

	public List<MapReduce> mostLinked() {
		List<MapReduce> mapReduce = new ArrayList<MapReduce>();
		boolean newCouple = true;
		for (Infos infos : classes) {
			String c1Name = infos.getClasse().split("::")[0];
			for (Infos c : classes) {
				String c2Name = c.getClasse().split("::")[0];
				if (!c1Name.equals(c2Name)) {
					newCouple = true;
					for (MapReduce mapReduce2 : mapReduce) {
						if (mapReduce2.getClasseName().equals(c1Name + " " + c2Name)) {
							if (c.getMethods().contains(c1Name)) {
								mapReduce2.setCompter(mapReduce2.getCompter() + 1);
								newCouple = false;
								break;
							}
						}
					}
					if (newCouple) {
						if (c.getMethods().contains(c1Name)) {
							mapReduce.add(new MapReduce(c1Name + " " + c2Name, 1));
						}
					}
				}
			}
		}

		return mapReduce;
	}

}
