package RKrocks;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

public class rocksCppRunFilesLoop200 {

	public static void main(String[] args) throws IOException {

		int[] populationSizes = new int[] { 2000 };

		String fileDirectory =  "taillard_instances/";
		
		String[] fileNames = { "tai200_10_0.fsp"/*, "tai200_10_1.fsp", "tai200_10_2.fsp", "tai200_10_3.fsp", "tai200_10_4.fsp", "tai200_10_5.fsp", "tai200_10_6.fsp", "tai200_10_7.fsp", "tai200_10_8.fsp", "tai200_10_9.fsp"*/};
	
//		String[] fileNames = { "tai200_20_0.fsp", "tai200_20_1.fsp", "tai200_20_2.fsp", "tai200_20_3.fsp", "tai200_20_4.fsp", "tai200_20_5.fsp", "tai200_20_6.fsp", "tai200_20_7.fsp", "tai200_20_8.fsp", "tai200_20_9.fsp" };

		String dynamicDirectory = "dynamic/";
		String[] dynamicProfilePaths = { "dynProfile-noChange.txt",
				"dynProfile-n200-c3-Cayley2.txt", "dynProfile-n200-c3-Cayley40.txt","dynProfile-n200-c3-Cayley100.txt", "dynProfile-n200-c3-Cayley180.txt",
//				"dynProfile-n200-c10-Cayley2.txt", "dynProfile-n200-c10-Cayley40.txt","dynProfile-n200-c10-Cayley100.txt", "dynProfile-n200-c10-Cayley180.txt",
//				"dynProfile-n200-c20-Cayley2.txt", "dynProfile-n200-c20-Cayley40.txt","dynProfile-n200-c20-Cayley100.txt", "dynProfile-n200-c20-Cayley180.txt"
				};

		int fes = -1;
		int[] truncSizes = { 200 };
		int elitism = 1;

		double[] initialStdevs = { 0.002, 0.004, 0.006, 0.008, 0.01 };
		double[] coolingParams = { 0.002, 0.004, 0.006, 0.008, 0.01 };

		String resultsPath = "results/";
		String saveAs = null;

		Random rand = new Random();

		int nRuns = 3, counter = 1;

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
		BufferedWriter br = new BufferedWriter(new FileWriter("./RK/rocks/runBatch200_cooling.txt"));
//		BufferedWriter br = new BufferedWriter(new FileWriter("./RK/rocks/runBatch200_10.txt"));
//		BufferedWriter br = new BufferedWriter(new FileWriter("./RK/rocks/runBatch200_20.txt"));
		br.write(out);
		br.close();
	}

}
