package clustering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.LongStream;

import loggers.ConsoleLogger;
import loggers.FileLogger;
import loggers.LogRequest;
import loggers.StandardLogRequestLevel;
import processors.ASTProcessor;
import vizualise.DendrogramPanel;

public abstract class PerformCallGraph extends ASTProcessor {
	/* ATTRIBUTES */
	private Set<String> methods = new HashSet<>();
	private Map<String, Map<String, Integer>> invocations = new HashMap<>();
	private FileLogger loggerChain;

	/* CONSTRUCTOR */
	public PerformCallGraph(String projectPath) {
		super(projectPath);
		setLoggerChain();
	}

	/* METHODS */
	protected void setLoggerChain() {
		loggerChain = new FileLogger(StandardLogRequestLevel.DEBUG);
		loggerChain.setNextLogger(new ConsoleLogger(StandardLogRequestLevel.INFO));
	}

	public Set<String> getMethods() {
		return methods;
	}

	public long getNbMethods() {
		return methods.size();
	}

	public long getNbInvocations() {
		return invocations.keySet().stream().map(source -> invocations.get(source))
				.map(destination -> destination.values()).flatMap(Collection::stream)
				.flatMapToLong(value -> LongStream.of((long) value)).sum();
	}

	public Map<String, Map<String, Integer>> getInvocations() {
		return invocations;
	}

	public boolean addMethod(String method) {
		return methods.add(method);
	}

	public boolean addMethods(Set<String> methods) {
		return methods.addAll(methods);
	}

	public void addInvocation(String source, String destination) {

		if (invocations.containsKey(source)) {

			if (invocations.get(source).containsKey(destination)) {
				int numberOfArrows = invocations.get(source).get(destination);
				invocations.get(source).put(destination, numberOfArrows + 1);
			}

			else {
				methods.add(destination);
				invocations.get(source).put(destination, 1);
			}
		}

		else {
			methods.add(source);
			methods.add(destination);
			invocations.put(source, new HashMap<String, Integer>());
			invocations.get(source).put(destination, 1);
		}
	}

	public void addInvocation(String source, String destination, int occurrences) {
		methods.add(source);
		methods.add(destination);

		if (!invocations.containsKey(source))
			invocations.put(source, new HashMap<String, Integer>());

		invocations.get(source).put(destination, occurrences);
	}

	public void addInvocations(Map<String, Map<String, Integer>> map) {
		for (String source : map.keySet())
			for (String destination : map.get(source).keySet())
				this.addInvocation(source, destination, map.get(source).get(destination));
	}

	void dendro(Map<String, List<Integer>> couple) {
		int l = couple.size();
		String[] names = new String[l];
		List<String> namesArray = new ArrayList<>();
		List<Integer> distanceArray = new ArrayList<>();

		double[][] distances = new double[l][l];

		for (Map.Entry<String, List<Integer>> entry : couple.entrySet()) {
			String key = entry.getKey();
			List<Integer> val = entry.getValue();
			namesArray.add(key);
			distanceArray.addAll(val);
		}

		for (int i = 0; i < couple.size(); i++) {
			names[i] = namesArray.get(i);
			System.out.println(names[i]);
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

	public String performCluster() {
		ExtactInformations extactInformations = new ExtactInformations();

		String[] source_split;

		for (String source : invocations.keySet()) {
			Infos infos = new Infos("", "");
			if (source.contains(".")) {
				source_split = source.split("\\.");
				infos = new Infos(source_split[source_split.length - 1], "");
			}

			for (String destination : invocations.get(source).keySet()) {
				infos.setMethods(infos.getMethods() + " " + destination);
			}

			extactInformations.add(infos);
		}

		List<MapReduce> mapReduces = extactInformations.mostLinked();
		Map<String, Integer> clust = new HashMap<>();
		Map<String, List<Integer>> couple = new HashMap<>();

		for (int i = mapReduces.size() - 1; i >= 0; i--) {
			System.out.println(mapReduces.get(i).toString());
			clust.putIfAbsent(mapReduces.get(i).getClasseName(), mapReduces.get(i).getCompter());
			String[] split = mapReduces.get(i).getClasseName().split(" ");
			couple.putIfAbsent(split[0], new ArrayList<>());
			couple.putIfAbsent(split[1], new ArrayList<>());
		}

		for (Map.Entry<String, List<Integer>> c : couple.entrySet()) {
			String coupleKey = c.getKey();
			List<Integer> coupleValue = c.getValue();
			for (Map.Entry<String, Integer> entry : clust.entrySet()) {
				String key = entry.getKey();
				Integer val = entry.getValue();
				if (key.contains(coupleKey)) {
					coupleValue.add(val);
				} else {
					coupleValue.add(1);
				}
			}
		}

		dendro(couple);
		return "";
	}

	public void print() {
		loggerChain.log(new LogRequest(this.toString(), StandardLogRequestLevel.INFO));
	}

}
