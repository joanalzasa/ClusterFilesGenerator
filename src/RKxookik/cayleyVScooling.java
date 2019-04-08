package RKxookik;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

public class cayleyVScooling {

	public static void main(String[] args) throws IOException {

		int[] populationSizes = new int[] { 200 };

		String fileDirectory =  "taillard_instances/";
		
		String[] fileNames = { "tai20_10_0.fsp" };
		
		String dynamicDirectory = "dynamic/";
		String[] dynamicProfilePaths = { "dynProfile-noChange.txt",
				//"dynProfile-n20-c3-Cayley2.txt", "dynProfile-n20-c3-Cayley4.txt","dynProfile-n20-c3-Cayley10.txt", "dynProfile-n20-c3-Cayley18.txt",
//				"dynProfile-n20-c10-Cayley2.txt", "dynProfile-n20-c10-Cayley4.txt","dynProfile-n20-c10-Cayley10.txt", "dynProfile-n20-c10-Cayley18.txt",
				//"dynProfile-n20-c20-Cayley2.txt", "dynProfile-n20-c20-Cayley4.txt","dynProfile-n20-c20-Cayley10.txt", "dynProfile-n20-c20-Cayley18.txt"
				};
		int fes = -1;
		int[] truncSizes = { 20 };
		int elitism = 0;

		double[] initialStdevs = { 0, 0.02, 0.04, 0.06, 0.08, 0.1 };
		double[] coolingParams = {  0.02, 0.04, 0.06, 0.08, 0.1, 0.12 };

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
//									long seed = currenttime + rand.nextInt();
									long seed = 1;
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
											+ resultsPath + " " + saveAs + " " + coolingParam2 + " " + seed + " " + "a\"\n";

									counter++;
								}
							}
						}
					}
				}
			}
		}

		String header = "!/bin/bash\n#$ -S /bin/bash\n#$ -N RKEDAc\n#$ -q serial.q\n#$ -o out/RKEDAc.$TASK_ID.dat"
				+ "\n#$ -e err/RKEDAc.$TASK_ID.dat\n#$ -cwd\n#$ -t 1-" + (counter - 1) + "\n\n";

		String lastline = "./a.out ${PARAMS[$SGE_TASK_ID]}";

		String out = header + content + "\n" + lastline;
		System.out.println(out);
		BufferedWriter br = new BufferedWriter(new FileWriter("./cppFiles/runBatchcayleyVScooling.txt"));
		br.write(out);
		br.close();
	}

}
