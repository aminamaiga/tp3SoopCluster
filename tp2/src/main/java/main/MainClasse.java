package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import clustering.PerformCallGraph;
import graphs.StaticCallGraph;
import processors.ASTProcessor;
import spoon.CallGraphBySpoon;

public class MainClasse extends AbstractMain {
	// String path = "D:/RenduParAminataMaiga/RenduParAminataMaiga/src/";

	@Override
	public void menu() {
		StringBuilder builder = new StringBuilder();
		builder.append("1.  Clustering Coupling.");
		builder.append("\n2. Clustering Coupling by Spoon.");
		builder.append("\n3. Help menu.");
		builder.append("\n" + QUIT + ". To quit.");

		System.out.println(builder);
	}

	public static void main(String[] args) {
		MainClasse main = new MainClasse();
		BufferedReader inputReader;
		PerformCallGraph performCallGraph = null;
		try {
			inputReader = new BufferedReader(new InputStreamReader(System.in));
			if (args.length < 1)
				setTestProjectPath(inputReader);
			else
				verifyTestProjectPath(inputReader, args[0]);
			String userInput = "";

			do {
				main.menu();
				userInput = inputReader.readLine();
				main.processUserInput(userInput, performCallGraph);
				Thread.sleep(3000);

			} while (!userInput.equals(QUIT));

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void processUserInput(String userInput, ASTProcessor processor) {
		PerformCallGraph performCallGraph = (PerformCallGraph) processor;
		CallGraphBySpoon callGraphBySpoon = new CallGraphBySpoon(TEST_PROJECT_PATH);
		try {
			switch (userInput) {
			case "1":
				performCallGraph = StaticCallGraph.createCallGraph(TEST_PROJECT_PATH);
				performCallGraph.performCluster();
				break;

			case "2":
				// doing
				Map<String, List<String>> infos = callGraphBySpoon.getClassesInfos();
				Map<String, List<Integer>> g = callGraphBySpoon.grouping(infos);
				callGraphBySpoon.dendro(g, g.size());
				break;

			case "3":
				return;

			case QUIT:
				System.out.println("Bye...");
				return;

			default:
				System.err.println("Sorry, wrong input. Please try again.");
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
