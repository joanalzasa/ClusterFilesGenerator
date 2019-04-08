package PLxookik;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

public class xookikRunFilePL100 {

	public static void main(String[] args) throws IOException {

		String[] fileNames = { "data/taillard_instances/tai100_10_0.fsp" };
//		String[] fileNames = { "data/taillard_instances/tai100_5_0.fsp","data/taillard_instances/tai100_5_1.fsp",
//				"data/taillard_instances/tai100_5_2.fsp","data/taillard_instances/tai100_5_3.fsp","data/taillard_instances/tai100_5_4.fsp",
//				"data/taillard_instances/tai100_5_5.fsp","data/taillard_instances/tai100_5_6.fsp","data/taillard_instances/tai100_5_7.fsp",
//				"data/taillard_instances/tai100_5_8.fsp","data/taillard_instances/tai100_5_9.fsp"};
//		String[] fileNames = { "data/taillard_instances/tai100_10_0.fsp","data/taillard_instances/tai100_10_1.fsp",
//				"data/taillard_instances/tai100_10_2.fsp","data/taillard_instances/tai100_10_3.fsp","data/taillard_instances/tai100_10_4.fsp",
//				"data/taillard_instances/tai100_10_5.fsp","data/taillard_instances/tai100_10_6.fsp","data/taillard_instances/tai100_10_7.fsp",
//				"data/taillard_instances/tai100_10_8.fsp","data/taillard_instances/tai100_10_9.fsp"};
//		String[] fileNames = { "data/taillard_instances/tai100_20_0.fsp","data/taillard_instances/tai100_20_1.fsp",
//				"data/taillard_instances/tai100_20_2.fsp","data/taillard_instances/tai100_20_3.fsp","data/taillard_instances/tai100_20_4.fsp",
//				"data/taillard_instances/tai100_20_5.fsp","data/taillard_instances/tai100_20_6.fsp","data/taillard_instances/tai100_20_7.fsp",
//				"data/taillard_instances/tai100_20_8.fsp","data/taillard_instances/tai100_20_9.fsp"};
//		String[] dynamicProfilePaths = { "data/dynamic/dynProfile-noChange.txt",
//				"data/dynamic/dynProfile-n100-c3-Cayley2.txt", "data/dynamic/dynProfile-n100-c3-Cayley4.txt","data/dynamic/dynProfile-n100-c3-Cayley10.txt", "dynProfile-n20-c3-Cayley18.txt",
//				"data/dynamic/dynProfile-n100-c10-Cayley2.txt", "data/dynamic/dynProfile-n100-c10-Cayley4.txt","data/dynamic/dynProfile-n100-c10-Cayley10.txt", "dynProfile-n20-c10-Cayley18.txt",
//				"data/dynamic/dynProfile-n100-c20-Cayley2.txt", "data/dynamic/dynProfile-n100-c20-Cayley4.txt","data/dynamic/dynProfile-n100-c20-Cayley10.txt", "dynProfile-n20-c20-Cayley18.txt"
//				};
		String[] dynamicProfilePaths = { "data/dynamic/dynProfile-n100-c3-Cayley10.txt" };

		
		int[] rates = {3,4,5,6,7,8};
		
		String problemType = "PFSP";
		String model = "PL";
		String distance = "C";
		int inverse = 0;
		
		String saveAs = null;

		Random rand = new Random();

		int nRuns = 1, counter = 1;

		String content = "";

		for (String problempath : fileNames) {
			int nameInstance = problempath.split("/").length - 1;
			for (String dynamicFile : dynamicProfilePaths) {
				int nameDynFile = dynamicFile.split("/").length - 1;
				for (int rate:rates) {
					for (int i = 0; i < nRuns; i++) {
						long currenttime = new Date().getTime();
						long seed = currenttime + rand.nextInt();
						
						saveAs = "results/progress-" + problempath.split("/")[nameInstance].substring(0,
								problempath.split("/")[nameInstance].lastIndexOf("."))
								+ "-"
								+ dynamicFile.split("/")[nameDynFile].substring(0,
										dynamicFile.split("/")[nameDynFile].lastIndexOf("."))
								+ "-PL-ratio" + rate +"-elt1--" + i + ".csv";
						
						content += "PARAMS[" + (counter) + "]=\"" + "-o " + saveAs + " -p " + dynamicFile +  " -s " + seed + 
								" -i " + problempath + " -t " + problemType + " -m " + model + " -d "+ distance + " -v " + inverse 
								+ " -r "+ rate + "\"\n";

//						-o results/PL-c3-r1.2.csv -p data/dynamic/dynProfile-n20-c3-Cayley10.txt -s 1 -i data/taillard_instances/tai20_10_0.fsp -t PFSP -m PL -d C -v 0 -r 0.8
						counter++;
					}
				}
			}
		}


		String header = "!/bin/bash\n#$ -S /bin/bash\n#$ -N PLEDAc\n#$ -q serial.q\n#$ -o out/RKEDAc.$TASK_ID.dat"
				+ "\n#$ -e err/RKEDAc.$TASK_ID.dat\n#$ -cwd\n#$ -t 1-" + (counter - 1) + "\n\n";

		String lastline = "./a.out ${PARAMS[$SGE_TASK_ID]}";

		String out = header + content + "\n" + lastline;
		System.out.println(out);
//		BufferedWriter br = new BufferedWriter(new FileWriter("PL/cppFiles/runBatch20_5.txt"));
		BufferedWriter br = new BufferedWriter(new FileWriter("PL/cppFiles/runBatch20_10.txt"));
//		BufferedWriter br = new BufferedWriter(new FileWriter("PL/cppFiles/runBatch20_20.txt"));
		br.write(out);
		br.close();
	}

}
