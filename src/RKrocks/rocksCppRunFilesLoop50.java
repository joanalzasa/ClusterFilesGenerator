package RKrocks;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

public class rocksCppRunFilesLoop50 {

	public static void main(String[] args) throws IOException {

		int[] populationSizes = new int[] { 500 };

		String fileDirectory =  "taillard_instances/";
		String[] fileNames = { "tai50_5_0.fsp", "tai50_5_1.fsp", "tai50_5_2.fsp", "tai50_5_3.fsp", "tai50_5_4.fsp", "tai50_5_5.fsp", "tai50_5_6.fsp", "tai50_5_7.fsp", "tai50_5_8.fsp", "tai50_5_9.fsp" };
		
//		String[] fileNames = { "tai50_10_0.fsp", "tai50_10_1.fsp", "tai50_10_2C.fsp", "tai50_10_3.fsp", "tai50_10_4.fsp", "tai50_10_5.fsp", "tai50_10_6.fsp", "tai50_10_7.fsp", "tai50_10_8.fsp", "tai50_10_9.fsp" };
		
//		String[] fileNames = { "tai50_20_0.fsp", "tai50_20_1.fsp", "tai50_20_2.fsp", "tai50_20_3.fsp", "tai50_20_4.fsp", "tai50_20_5.fsp", "tai50_20_6.fsp", "tai50_20_7.fsp", "tai50_20_8.fsp", "tai50_20_9.fsp" };

		String dynamicDirectory = "dynamic/";
		String[] dynamicProfilePaths = { "dynProfile-noChange.txt",
				"dynProfile-n50-c3-Cayley2.txt", "dynProfile-n50-c3-Cayley10.txt","dynProfile-n50-c3-Cayley25.txt", "dynProfile-n50-c3-Cayley45.txt",
				"dynProfile-n50-c10-Cayley2.txt", "dynProfile-n50-c10-Cayley10.txt","dynProfile-n50-c10-Cayley25.txt", "dynProfile-n50-c10-Cayley45.txt",
				"dynProfile-n50-c20-Cayley2.txt", "dynProfile-n50-c20-Cayley10.txt","dynProfile-n50-c20-Cayley25.txt", "dynProfile-n50-c20-Cayley45.txt"
				};

		int fes = -1;
		int[] truncSizes = { 50 };
		int elitism = 1;

		double[] initialStdevs = { 0.02 };
		double[] coolingParams = { 0.1 };

		String resultsPath = "results/";
		String saveAs = null;

		Random rand = new Random();

		int nRuns = 20, counter = 1;

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
									long currenttime = new Date().getTime();
									long seed = currenttime + rand.nextInt();
									saveAs = problempath.split("/")[nameInstance].substring(0,
											problempath.split("/")[nameInstance].lastIndexOf("."))
											+ "-"
											+ dynamicFile.split("/")[nameDynFile].substring(0,
													dynamicFile.split("/")[nameDynFile].lastIndexOf("."))
											+ "-" + "currentbest_" + initialStdev + "_" + coolingParam2 + "-" + "elt"
											+ elitism + "--" + i;
									
									content += "PARAMS[" + (counter) + "]=\"" + populationSize + " " + fileDirectory
											+ " " + problempath + " " + dynamicDirectory + " " + dynamicFile + " "
											+ fes + " " + truncSize + " " + elitism + " " + initialStdev + " "
											+ resultsPath + " " + saveAs + " " + coolingParam2 + " " + seed + " " + "\"\n";

									counter++;
								}
							}
						}
					}
				}
			}
		}

		String header = "!/bin/bash\n#$ -S /bin/bash\n#$ -N RKEDAc\n#$ -q all.q\n#$ -o out/RKEDAc.$TASK_ID.dat"
				+ "\n#$ -e err/RKEDAc.$TASK_ID.dat\n#$ -cwd\n#$ -t 1-" + (counter - 1) + "\n\n";

		String lastline = "./a.out ${PARAMS[$SGE_TASK_ID]}";

		String out = header + content + "\n" + lastline;
		System.out.println(out);
		BufferedWriter br = new BufferedWriter(new FileWriter("./rocks/runBatch50_5.txt"));
//		BufferedWriter br = new BufferedWriter(new FileWriter("./rocks/runBatch50_10.txt"));
//		BufferedWriter br = new BufferedWriter(new FileWriter("./rocks/runBatch50_20.txt"));
		br.write(out);
		br.close();
	}

}
