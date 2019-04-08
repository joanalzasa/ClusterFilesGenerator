package DRKrocks;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

public class generateCppRunFilesLoop200 {

	public static void main(String[] args) throws IOException {

		int[] populationSizes = new int[] { 2000 };

		String fileDirectory =  "taillard_instances/";

//		String[] fileNames = { "tai200_10_0.fsp", "tai200_10_1.fsp", "tai200_10_2.fsp", "tai200_10_3.fsp", "tai200_10_4.fsp", "tai200_10_5.fsp", "tai200_10_6.fsp", "tai200_10_7.fsp", "tai200_10_8.fsp", "tai200_10_9.fsp" };
	
//		String[] fileNames = { "tai200_20_0.fsp", "tai200_20_1.fsp", "tai200_20_2.fsp", "tai200_20_3.fsp", "tai200_20_4.fsp", "tai200_20_5.fsp", "tai200_20_6.fsp", "tai200_20_7.fsp", "tai200_20_8.fsp", "tai200_20_9.fsp" };
		
		String[] fileNames = { "tai200_10_0.fsp", "tai200_20_0.fsp"};

		String dynamicDirectory = "dynamic/kendall/";
		String[] dynamicProfilePaths = {
//				"dynProfile-n200-c10-C1.txt", "dynProfile-n200-c10-C2.txt", "dynProfile-n200-c10-C3.txt", "dynProfile-n200-c10-C4.txt", "dynProfile-n200-c10-C5.txt", 
//				"dynProfile-n200-c10-C10.txt", "dynProfile-n200-c10-C15.txt", "dynProfile-n200-c10-C20.txt", "dynProfile-n200-c10-C25.txt", "dynProfile-n200-c10-C30.txt",
//				"dynProfile-n200-c10-C40.txt", "dynProfile-n200-c10-C50.txt", "dynProfile-n200-c10-C60.txt", "dynProfile-n200-c10-C70.txt", "dynProfile-n200-c10-C80.txt", 
//				"dynProfile-n200-c10-C100.txt", "dynProfile-n200-c10-C150.txt", "dynProfile-n200-c10-C190.txt"
//				"dynProfile-n200-c10-C27.txt", "dynProfile-n200-c10-C32.txt", "dynProfile-n200-c10-C35.txt"
				"dynProfile-n200-c10-K1.txt","dynProfile-n200-c10-K50.txt","dynProfile-n200-c10-K100.txt","dynProfile-n200-c10-K150.txt",
				"dynProfile-n200-c10-K200.txt","dynProfile-n200-c10-K500.txt","dynProfile-n200-c10-K1000.txt","dynProfile-n200-c10-K2500.txt",
				"dynProfile-n200-c10-K5000.txt","dynProfile-n200-c10-K10000.txt","dynProfile-n200-c10-K19900.txt",
				};
		
		int fes = 10000 * 200 * 200;	 // 100nk generations --> 100nk*(pop size) evaluations
		int[] truncSizes = { 200 };
		int[] restart = { 0, 1 };
		int elitism = 0;
		
		double[] initialStdevs = { 0.004 };
		double[] coolingParams = { 0.01 };

		String resultsPath = "results/";
		String saveAs = null;

		Random rand = new Random();

		int nRuns = 30, counter = 1;

		String content = "";

		for (int populationSize : populationSizes) {
			for (String problempath : fileNames) {
				int nameInstance = problempath.split("/").length - 1;
				for (String dynamicFile : dynamicProfilePaths) {
					int nameDynFile = dynamicFile.split("/").length - 1;
					for (int truncSize : truncSizes) {
						for (double initialStdev : initialStdevs) {
							for (double coolingParam2 : coolingParams) {
								for (int algorithm:restart){
									for (int i = 0; i < nRuns; i++) {
										long currenttime = new Date().getTime();
										long seed = i;
										
										saveAs = problempath.split("/")[nameInstance].substring(0,
												problempath.split("/")[nameInstance].lastIndexOf("."))
												+ "-"
												+ dynamicFile.split("/")[nameDynFile].substring(0,
														dynamicFile.split("/")[nameDynFile].lastIndexOf("."))
												+ "-" + "currentbest_" + initialStdev + "_" + coolingParam2 + "-r" + algorithm +"-elt"
												+ elitism + "--" + i;
										
										content += "PARAMS[" + (counter) + "]=\"" + populationSize + " " + fileDirectory
												+ " " + problempath + " " + dynamicDirectory + " " + dynamicFile + " "
												+ fes + " " + truncSize + " " + elitism + " " + initialStdev + " "
												+ resultsPath + " " + saveAs + " " + coolingParam2 + " " + seed + " " + algorithm +"\"\n";
	
										counter++;
									}
								}
							}
						}
					}
				}
			}
		}

		String header = "!/bin/bash\n#$ -S /bin/bash\n#$ -N DRKEDA200K\n#$ -q all.q\n#$ -o out/RKEDAc.$TASK_ID.dat"
				+ "\n#$ -e err/RKEDAc.$TASK_ID.dat\n#$ -cwd\n#$ -t 1-" + (counter - 1) + "\n\n";

		String lastline = "./a.out ${PARAMS[$SGE_TASK_ID]}";

		String out = header + content + "\n" + lastline;
		System.out.println(out);
		BufferedWriter br = new BufferedWriter(new FileWriter("./cppFiles/rocks/DRKEDA/GECCO_K200.txt"));
//		BufferedWriter br = new BufferedWriter(new FileWriter("./cppFiles/rocks/DRKEDA/runBatch200_10.txt"));
//		BufferedWriter br = new BufferedWriter(new FileWriter("./cppFiles/rocks/DRKEDA/runBatch200_20.txt"));
		br.write(out);
		br.close();
	}

}
