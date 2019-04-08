package DRKrocks;
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
		
//		String[] fileNames = { "tai100_20_0.fsp", "tai100_20_1.fsp", "tai100_20_2.fsp", "tai100_20_3.fsp", "tai100_20_4.fsp", "tai100_20_5.fsp", "tai100_20_6.fsp", "tai100_20_7.fsp", "tai100_20_8.fsp", "tai100_20_9.fsp" };
		
		String[] fileNames = { "tai100_5_0.fsp", "tai100_10_0.fsp", "tai100_20_0.fsp"};

		String dynamicDirectory = "dynamic/kendall/";
		String[] dynamicProfilePaths = new String[390];
		
		for (int i= 0; i<100; i++)
			dynamicProfilePaths[i] = "dynProfile-n100-c10-K" + (/*(i*3)+1*/i + 1) +".txt";
//		for (int j= 0; j<190; j++)
//			dynamicProfilePaths[200 + j] = "dynProfile-n100-c10-K" + ((j*25)+225/*i + 1*/) +".txt";
//		String[] dynamicProfilePaths ={ 
//				"dynProfile-n100-c10-U1.txt", "dynProfile-n100-c10-U2.txt", "dynProfile-n100-c10-U3.txt", "dynProfile-n100-c10-U4.txt", "dynProfile-n100-c10-U5.txt", 
//				"dynProfile-n100-c10-U6.txt", "dynProfile-n100-c10-U7.txt", "dynProfile-n100-c10-U8.txt", "dynProfile-n100-c10-U9.txt", "dynProfile-n100-c10-U10.txt",
//				"dynProfile-n100-c10-U11.txt", "dynProfile-n100-c10-U12.txt", "dynProfile-n100-c10-U13.txt", "dynProfile-n100-c10-U14.txt", "dynProfile-n100-c10-U15.txt", 
//				"dynProfile-n100-c10-U16.txt", "dynProfile-n100-c10-U17.txt", "dynProfile-n100-c10-U18.txt", "dynProfile-n100-c10-U19.txt",
//				"dynProfile-n100-c10-U25.txt", "dynProfile-n100-c10-U30.txt", "dynProfile-n100-c10-U40.txt",
//				"dynProfile-n100-c10-U20.txt", "dynProfile-n100-c10-U50.txt", "dynProfile-n100-c10-U70.txt", "dynProfile-n100-c10-U90.txt"
////				"dynProfile-n100-c10-K1.txt", "dynProfile-n100-c10-K10.txt", "dynProfile-n100-c10-K20.txt", "dynProfile-n100-c10-K30.txt", "dynProfile-n100-c10-K40.txt", 
////				"dynProfile-n100-c10-K50.txt", "dynProfile-n100-c10-K60.txt", "dynProfile-n100-c10-K70.txt", "dynProfile-n100-c10-K80.txt", "dynProfile-n100-c10-K90.txt",
////				"dynProfile-n100-c10-K100.txt", "dynProfile-n100-c10-K110.txt", "dynProfile-n100-c10-K120.txt", "dynProfile-n100-c10-K130.txt", "dynProfile-n100-c10-K140.txt", 
////				"dynProfile-n100-c10-K150.txt", "dynProfile-n100-c10-K160.txt", "dynProfile-n100-c10-K170.txt", "dynProfile-n100-c10-K180.txt",
////				"dynProfile-n100-c10-K190.txt", "dynProfile-n100-c10-K200.txt", "dynProfile-n100-c10-K250.txt", "dynProfile-n100-c10-K300.txt",
////				"dynProfile-n100-c10-K400.txt", "dynProfile-n100-c10-K500.txt", "dynProfile-n100-c10-K1000.txt", "dynProfile-n100-c10-K2500.txt", "dynProfile-n100-c10-K4950.txt"
//				
//				};

		int fes = 10000 * 100 * 100;	 // 100nk generations --> 100nk*(pop size) evaluations
		int[] truncSizes = { 100 };
		int[] restart = { 0, 1 };
		int elitism = 0;
		
		double[] initialStdevs = { 0.15 };
		double[] coolingParams = { 0.025 };

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
										System.out.println(dynamicFile);
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

		String header = "!/bin/bash\n#$ -S /bin/bash\n#$ -N DRKEDA100\n#$ -q all.q\n#$ -o out/RKEDAc.$TASK_ID.dat"
				+ "\n#$ -e err/RKEDAc.$TASK_ID.dat\n#$ -cwd\n#$ -t 1-" + (counter - 1) + "\n\n";

		String lastline = "./a.out ${PARAMS[$SGE_TASK_ID]}";

		String out = header + content + "\n" + lastline;
		System.out.println(out);
		BufferedWriter br = new BufferedWriter(new FileWriter("./cppFiles/rocks/DRKEDA/GECCO_K100.txt"));
//		BufferedWriter br = new BufferedWriter(new FileWriter("./cppFiles/rocks/DRKEDA/runBatch100_5.txt"));
//		BufferedWriter br = new BufferedWriter(new FileWriter("./cppFiles/rocks/DRKEDA/runBatch100_10.txt"));
//		BufferedWriter br = new BufferedWriter(new FileWriter("./cppFiles/rocks/DRKEDA/runBatch100_20.txt"));
		br.write(out);
		br.close();
	}

}
