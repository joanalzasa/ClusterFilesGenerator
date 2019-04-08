package DRKxookik;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

public class generateCppRunFilesLoop20 {

	public static void main(String[] args) throws IOException {

		int[] populationSizes = new int[] { 200 };

		String fileDirectory =  "taillard_instances/";
//		String[] fileNames = { "tai20_5_0.fsp", "tai20_5_1.fsp", "tai20_5_2.fsp", "tai20_5_3.fsp", "tai20_5_4.fsp", "tai20_5_5.fsp", "tai20_5_6.fsp", "tai20_5_7.fsp", "tai20_5_8.fsp", "tai20_5_9.fsp" };
		
//		String[] fileNames = { "tai20_10_0.fsp", "tai20_10_1.fsp", "tai20_10_2.fsp", "tai20_10_3.fsp", "tai20_10_4.fsp", "tai20_10_5.fsp", "tai20_10_6.fsp", "tai20_10_7.fsp", "tai20_10_8.fsp", "tai20_10_9.fsp" };
	
		String[] fileNames = { "tai20_20_0.fsp", "tai20_20_1.fsp", "tai20_20_2.fsp", "tai20_20_3.fsp", "tai20_20_4.fsp", "tai20_20_5.fsp", "tai20_20_6.fsp", "tai20_20_7.fsp", "tai20_20_8.fsp", "tai20_20_9.fsp" };

		String dynamicDirectory = "dynamic/";
		String[] dynamicProfilePaths = {
//				"dynProfile-n20-c3-Cayley2.txt", "dynProfile-n20-c3-Cayley4.txt","dynProfile-n20-c3-Cayley10.txt", "dynProfile-n20-c3-Cayley18.txt",
				"dynProfile-n20-c10-Cayley4.txt","dynProfile-n20-c10-Cayley10.txt",
//				"dynProfile-n20-c20-Cayley4.txt"
				};

		int fes = 100 * 20 * 10 * 200;  // 100nk generations --> 100nk*(pop size) evaluations
		int[] truncSizes = { 20 };
		int elitism = 0;
		
		double[] initialStdevs = { 0.04 };
		double[] coolingParams = { 0.1  };

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
							for (double coolingParam2 : coolingParams) {
								for (int i = 0; i < nRuns; i++) {
									long currenttime = new Date().getTime();
									long seed = i;
									
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

		String header = "!/bin/bash\n#$ -S /bin/bash\n#$ -N DRKEDA20\n#$ -q serial.q\n#$ -o out/RKEDAc.$TASK_ID.dat"
				+ "\n#$ -e err/RKEDAc.$TASK_ID.dat\n#$ -cwd\n#$ -t 1-" + (counter - 1) + "\n\n";

		String lastline = "./a.out ${PARAMS[$SGE_TASK_ID]}";

		String out = header + content + "\n" + lastline;
		System.out.println(out);
//		BufferedWriter br = new BufferedWriter(new FileWriter("./cppFiles/DRKEDA/runBatch20_5.txt"));
//		BufferedWriter br = new BufferedWriter(new FileWriter("./cppFiles/DRKEDA/runBatch20_10.txt"));
		BufferedWriter br = new BufferedWriter(new FileWriter("./cppFiles/DRKEDA/runBatch20_20.txt"));
		br.write(out);
		br.close();
	}

}
