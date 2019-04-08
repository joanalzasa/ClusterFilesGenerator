package RKxookik;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class generateJavaRunFilesLoop100 {

	public static void main(String[] args) throws IOException {

		String[] fileNames = { "data/taillard_instances/tai100_5_0.fsp","data/taillard_instances/tai100_5_1.fsp",
				"data/taillard_instances/tai100_5_2.fsp","data/taillard_instances/tai100_5_3.fsp","data/taillard_instances/tai100_5_4.fsp",
				"data/taillard_instances/tai100_5_5.fsp","data/taillard_instances/tai100_5_6.fsp","data/taillard_instances/tai100_5_7.fsp",
				"data/taillard_instances/tai100_5_8.fsp","data/taillard_instances/tai100_5_9.fsp"};
////		String[] fileNames = { "data/taillard_instances/tai100_10_0.fsp","data/taillard_instances/tai100_10_1.fsp",
//				"data/taillard_instances/tai100_10_2.fsp","data/taillard_instances/tai100_10_3.fsp","data/taillard_instances/tai100_10_4.fsp",
//				"data/taillard_instances/tai100_10_5.fsp","data/taillard_instances/tai100_10_6.fsp","data/taillard_instances/tai100_10_7.fsp",
//				"data/taillard_instances/tai100_10_8.fsp","data/taillard_instances/tai100_10_9.fsp"};
//		String[] fileNames = { "data/taillard_instances/tai100_20_0.fsp","data/taillard_instances/tai100_20_1.fsp",
//				"data/taillard_instances/tai100_20_2.fsp","data/taillard_instances/tai100_20_3.fsp","data/taillard_instances/tai100_20_4.fsp",
//				"data/taillard_instances/tai100_20_5.fsp","data/taillard_instances/tai100_20_6.fsp","data/taillard_instances/tai100_20_7.fsp",
//				"data/taillard_instances/tai100_20_8.fsp","data/taillard_instances/tai100_20_9.fsp"};

		String[] dynamicProfilePaths = { "data/dynamic/dynProfile-noChange.txt", "data/dynamic/dynProfile-n100-c3-Cayley5.txt", "data/dynamic/dynProfile-n100-c10-Cayley10.txt" };


		String coolingSchedule = "azizi_adaptive_currentbest";
		double[] initialStdevs = { 0.005 };
		double[] coolingParams = { 0.01 };

		int[] populationSizes = new int[] { 500 };

		int fes = -1;
		int[] truncSizes = { 2 };
		int elitism = 0;

		String resultsPath = "./results/";
		String saveAs = null;

		String diversity = "cayley"; //null, "cayley" or "hamming"
		
		int nRuns = 10, counter = 1;

		String content = "";

		for (int populationSize : populationSizes) {
			for (String problempath : fileNames) {
				int nameInstance = problempath.split("/").length - 1;
				for (String dynamicFile : dynamicProfilePaths) {
					int nameDynFile = dynamicFile.split("/").length - 1;
					for (int truncSize : truncSizes) {
						for (double initialStdev : initialStdevs) {
							for (double coolingParam2 : coolingParams) {
								for (int i = 0; i < nRuns; i++) {
									saveAs = problempath.split("/")[nameInstance].substring(0,
											problempath.split("/")[nameInstance].lastIndexOf("."))
											+ "-"
											+ dynamicFile.split("/")[nameDynFile].substring(0,
													dynamicFile.split("/")[nameDynFile].lastIndexOf("."))
											+ "-" + "currentbest_" + initialStdev + "_" + coolingParam2 + "-" + "elt"
											+ elitism + "--" + i;
									content += "PARAMS[" + (counter) + "]=\"" + populationSize + " " + problempath + " "
											+ dynamicFile + " " + fes + " " + truncSize + " " + elitism + " "
											+ initialStdev + " " + resultsPath + " " + saveAs + " " + coolingSchedule
											+ " " + coolingParam2 + " " + diversity + " " + "a" + "\"\n";

									counter++;
								}
							}
						}
					}
				}
			}
		}

		String header = "!/bin/bash\n#$ -N dynRKEDA\n#$ -q serial.q\n#$ -o out/dynRKEDA.$TASK_ID.dat"
				+ "\n#$ -e err/dynRKEDA.$TASK_ID.dat\n#$ -cwd\n#$ -t 1-" + (counter - 1) + "\n\n";

		String lastline = "java launchers.RKEDADynamicMain ${PARAMS[$SGE_TASK_ID]}";

		String out = header + content + "\n" + lastline;
		System.out.println(out);
		BufferedWriter br = new BufferedWriter(new FileWriter("./javaFiles/runBatch100_5.txt"));
//		BufferedWriter br = new BufferedWriter(new FileWriter("./javaFiles/runBatch100_10.txt"));
//		BufferedWriter br = new BufferedWriter(new FileWriter("./javaFiles/runBatch100_20.txt"));
		br.write(out);
		br.close();
	}

}
