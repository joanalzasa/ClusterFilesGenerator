package DRKrocks;
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
	
//		String[] fileNames = { "tai20_20_0.fsp", "tai20_20_1.fsp", "tai20_20_2.fsp", "tai20_20_3.fsp", "tai20_20_4.fsp", "tai20_20_5.fsp", "tai20_20_6.fsp", "tai20_20_7.fsp", "tai20_20_8.fsp", "tai20_20_9.fsp" };

		String[] fileNames = { "tai20_5_0.fsp", "tai20_10_0.fsp", "tai20_20_0.fsp"};
		
		String dynamicDirectory = "dynamic/kendall/";
		String[] dynamicProfilePaths = new String[190];
		
		for (int i= 0; i<dynamicProfilePaths.length; i++)
			dynamicProfilePaths[i] = "dynProfile-n20-c10-K" + (i+1) +".txt";
//				
//				"dynProfile-n20-c10-C1.txt", "dynProfile-n20-c10-C2.txt", "dynProfile-n20-c10-C3.txt", "dynProfile-n20-c10-C4.txt", "dynProfile-n20-c10-C5.txt", 
//				"dynProfile-n20-c10-C6.txt", "dynProfile-n20-c10-C7.txt", "dynProfile-n20-c10-C8.txt", "dynProfile-n20-c10-C9.txt", "dynProfile-n20-c10-C10.txt",
//				"dynProfile-n20-c10-C11.txt", "dynProfile-n20-c10-C12.txt", "dynProfile-n20-c10-C13.txt", "dynProfile-n20-c10-C14.txt", "dynProfile-n20-c10-C15.txt", 
//				"dynProfile-n20-c10-C16.txt", "dynProfile-n20-c10-C17.txt", "dynProfile-n20-c10-C18.txt", "dynProfile-n20-c10-C19.txt"
				

		int fes = 10000 * 20 * 20;  // 100nk generations --> 100nk*(pop size) evaluations
		int[] truncSizes = { 20 };
		int[] restart = { 0, 1 };
		int elitism = 0;
		
		double[] initialStdevs = { 0.15 };
		double[] coolingParams = { 0.1  };

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
										System.out.println(counter);
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

		String header = "!/bin/bash\n#$ -S /bin/bash\n#$ -N DRKEDA20\n#$ -q all.q\n#$ -o out/RKEDAc.$TASK_ID.dat"
				+ "\n#$ -e err/RKEDAc.$TASK_ID.dat\n#$ -cwd\n#$ -t 1-" + (counter - 1) + "\n\n";

		String lastline = "./a.out ${PARAMS[$SGE_TASK_ID]}";

		String out = header + content + "\n" + lastline;
//		System.out.println(out);
		BufferedWriter br = new BufferedWriter(new FileWriter("./cppFiles/rocks/DRKEDA/GECCO_K20.txt"));
//		BufferedWriter br = new BufferedWriter(new FileWriter("./cppFiles/rocks/DRKEDA/runBatch20_5.txt"));
//		BufferedWriter br = new BufferedWriter(new FileWriter("./cppFiles/rocks/DRKEDA/runBatch20_10.txt"));
//		BufferedWriter br = new BufferedWriter(new FileWriter("./cppFiles/rocks/DRKEDA/runBatch20_20.txt"));
		br.write(out);
		br.close();
	}

}
