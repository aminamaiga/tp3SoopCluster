package spoon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import clustering.AverageLinkage;
import clustering.Cluster;
import clustering.ClusteringAlgorithm;
import clustering.DendrogramFrame;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.visitor.Query;
import spoon.reflect.visitor.filter.TypeFilter;
import vizualise.DendrogramPanel;

public class CallGraphBySpoon {

	public String PROJECT_PATH;
	Launcher launcher;

	public CallGraphBySpoon(String PROJECT_PATH) {
		this.PROJECT_PATH = PROJECT_PATH;
		launcher = new Launcher();
		launcher.addInputResource(PROJECT_PATH);
		launcher.buildModel();
	}

	public Map<String, List<String>> getClassesInfos() {
		Map<String, List<String>> couples = new HashMap<>();
		int ltype;

		CtModel model = launcher.getModel();
		ltype = model.getAllTypes().size();
		System.out.println("total of classes " + ltype);

		for (CtType<?> type : model.getAllTypes()) {
			String key = type.getQualifiedName();
			List<String> values = new ArrayList<>();
			if ((couples.get(key) != null)) {
				values = couples.get(key);
			} else {
				couples.putIfAbsent(key, values);
			}
			for (CtMethod<?> method : type.getAllMethods()) {
				for (CtInvocation<?> invocation : Query.getElements(method,
						new TypeFilter<CtInvocation<?>>(CtInvocation.class))) {
					if (invocation.getTarget() != null && invocation.getTarget().getType() != null
							&& invocation.getTarget().getType().getTypeDeclaration() != null) {
						System.out.println(" invocations "
								+ invocation.getTarget().getType().getTypeDeclaration().getQualifiedName());
						;
						values.add(invocation.getTarget().getType().getTypeDeclaration().getQualifiedName());
					}

				}

			}

		}

		return couples;
	}

	public Map<String, List<Integer>> grouping(Map<String, List<String>> couple) {
		Map<String, List<Integer>> groups = new HashMap<>();
		for (Map.Entry<String, List<String>> entry : couple.entrySet()) {
			List<Integer> distance = new ArrayList<>();
			String key = entry.getKey();
			for (Map.Entry<String, List<String>> e2 : couple.entrySet()) {
				String key2 = e2.getKey();
				List<String> val2 = e2.getValue();
				int c = counterItem(key2, val2);
				distance.add(c);
				
				System.out.println("couple : (" + key + " " + key2 +" ; "+ c + ")");
			}
			groups.put(key, distance);
		}

		return groups;
	}

	public int counterItem(String className, List<String> classesName) {
		int c = 0;
		for (String classe : classesName) {
			if (classe.contains(className)) {
				c++;
			}
		}
		return c;
	}

	public void dendro(Map<String, List<Integer>> couple, int nbrClasses) {

		String[] names = new String[nbrClasses];
		List<String> namesArray = new ArrayList<>();
		List<Integer> distanceArray = new ArrayList<>();

		double[][] distances = new double[nbrClasses][nbrClasses];

		for (Map.Entry<String, List<Integer>> entry : couple.entrySet()) {
			String key = entry.getKey();
			List<Integer> val = entry.getValue();
			namesArray.add(key);
			distanceArray.addAll(val);
		}

		for (int i = 0; i < couple.size(); i++) {
			names[i] = namesArray.get(i);
			for (int j = 0; j < couple.size(); j++) {
				distances[i][j] = distanceArray.get(i);
			}
		}

		ClusteringAlgorithm alg = new ClusteringAlgorithm();
		Cluster cluster = alg.performClustering(distances, names, new AverageLinkage());

		DendrogramPanel dp = new DendrogramPanel();
		dp.setModel(cluster);

		new DendrogramFrame(cluster);
	}
}