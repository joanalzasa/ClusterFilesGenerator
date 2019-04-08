package RKxookik;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

public class generateCppRunFilesLoop100 {

	public static void main(String[] args) throws IOException {

		int[] populationSizes = new int[] { 1000 };

		String fileDirectory =  "taillard_instances/";
//		String[] fileNames = { "tai100_5_0.fsp", "tai100_5_1.fsp", "tai100_5_2.fsp", "tai100_5_3.fsp", "tai100_5_4.fsp", "tai100_5_5.fsp", "tai100_5_6.fsp", "tai100_5_7.fsp", "tai100_5_8.fsp", "tai100_5_9.fsp" };
		
//		String[] fileNames = { "tai100_10_0.fsp", "tai100_10_1.fsp", "tai100_10_2.fsp", "tai100_10_3.fsp", "tai100_10_4.fsp", "tai100_10_5.fsp", "tai100_10_6.fsp", "tai100_10_7.fsp", "tai100_10_8.fsp", "tai100_10_9.fsp" };
		
		String[] fileNames = { "tai100_20_0.fsp", "tai100_20_1.fsp", "tai100_20_2.fsp", "tai100_20_3.fsp", "tai100_20_4.fsp", "tai100_20_5.fsp", "tai100_20_6.fsp", "tai100_20_7.fsp", "tai100_20_8.fsp", "tai100_20_9.fsp" };

		String dynamicDirectory = "dynamic/";
		String[] dynamicProfilePaths = {
//				"dynProfile-n100-c3-Cayley2.txt", "dynProfile-n100-c3-Cayley20.txt","dynProfile-n100-c3-Cayley50.txt", "dynProfile-n100-c3-Cayley90.txt",
				"dynProfile-n100-c10-Cayley20.txt","dynProfile-n100-c10-Cayley50.txt"
//				"dynProfile-n100-c20-Cayley2.txt", "dynProfile-n100-c20-Cayley20.txt","dynProfile-n100-c20-Cayley50.txt", "dynProfile-n100-c20-Cayley90.txt"
				};

		int fes = 100 * 100 * 10 * 1000;	 // 100nk generations --> 100nk*(pop size) evaluations
		int[] truncSizes = { 100 };
		int elitism = 0;
		
		double[] initialStdevs = { 0.15 };
//		double[] coolingParams = { 0.025 };

		String resultsPath = "results/";
		String saveAs = null;
		Random rand = new Random();

		int nRuns = 10, counter = 1;

		String content = "";

		for (int populationSize : populationSizes) {
			for (String problempath : fileNames) {
				int nameInstance = problempath.split("/").length - 1;
				for (String dynamicFile : dynamicProfilePaths) {
					int nameDynFile = dynamicFile.split("/").length - 1;
					for (int truncSize : truncSizes) {
						for (double initialStdev : initialStdevs) {
							for (int i = 0; i < nRuns; i++) {
								long currenttime = new Date().getTime();
								long seed = i;
								
								saveAs = problempath.split("/")[nameInstance].substring(0,
										problempath.split("/")[nameInstance].lastIndexOf("."))
										+ "-"
										+ dynamicFile.split("/")[nameDynFile].substring(0,
												dynamicFile.split("/")[nameDynFile].lastIndexOf("."))
										+ "-" + "currentbest_" + initialStdev + "-" + "elt"
										+ elitism + "--" + i;
								
								content += "PARAMS[" + (counter) + "]=\"" + populationSize + " " + fileDirectory
										+ " " + problempath + " " + dynamicDirectory + " " + dynamicFile + " "
										+ fes + " " + truncSize + " " + elitism + " " + initialStdev + " "
										+ resultsPath + " " + saveAs + " " + seed + " " + "\"\n";

								counter++;
							}				
						}
					}
				}
			}
		}

		String header = "!/bin/bash\n#$ -S /bin/bash\n#$ -N RKEDA100\n#$ -q serial.q\n#$ -o out/RKEDAc.$TASK_ID.dat"
				+ "\n#$ -e err/RKEDAc.$TASK_ID.dat\n#$ -cwd\n#$ -t 1-" + (counter - 1) + "\n\n";

		String lastline = "./a.out ${PARAMS[$SGE_TASK_ID]}";

		String out = header + content + "\n" + lastline;
		System.out.println(out);
//		BufferedWriter br = new BufferedWriter(new FileWriter("./cppFiles/RKEDA/runBatch100_5.txt"));
//		BufferedWriter br = new BufferedWriter(new FileWriter("./cppFiles/RKEDA/runBatch100_10.txt"));
		BufferedWriter br = new BufferedWriter(new FileWriter("./cppFiles/RKEDA/runBatch100_20.txt"));
		br.write(out);
		br.close();
	}

}
