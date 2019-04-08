package PLrocks;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

public class rocksRunFilePL500 {

	public static void main(String[] args) throws IOException {

//		String[] fileNames = { "data/taillard_instances/tai500_10_0.fsp" };
		String[] fileNames = { "data/taillard_instances/tai500_20_0.fsp","data/taillard_instances/tai500_20_4.fsp"};

		String[] dynamicProfilePaths = { "data/dynamic/dynProfile-noChange.txt",
				"data/dynamic/dynProfile-n500-c3-Cayley2.txt", "data/dynamic/dynProfile-n500-c3-Cayley100.txt","data/dynamic/dynProfile-n500-c3-Cayley250.txt", "data/dynamic/dynProfile-n500-c3-Cayley450.txt",
				"data/dynamic/dynProfile-n500-c10-Cayley2.txt", "data/dynamic/dynProfile-n500-c10-Cayley100.txt","data/dynamic/dynProfile-n500-c10-Cayley250.txt", "data/dynamic/dynProfile-n500-c10-Cayley450.txt",
				"data/dynamic/dynProfile-n500-c20-Cayley2.txt", "data/dynamic/dynProfile-n500-c20-Cayley100.txt","data/dynamic/dynProfile-n500-c20-Cayley250.txt", "data/dynamic/dynProfile-n500-c20-Cayley450.txt"
				};
//		String[] dynamicProfilePaths = { "data/dynamic/dynProfile-n200-c3-Cayley10.txt" };
		
		String problemType = "PFSP";
		String model = "PL";
		String distance = "C";
		int inverse = 0;
		
		int[] ratio = {3}; 
		String saveAs = null;

		Random rand = new Random();

		int nRuns = 20, counter = 1;

		String content = "";

		for (String problempath : fileNames) {
			int nameInstance = problempath.split("/").length - 1;
			for (String dynamicFile : dynamicProfilePaths) {
				int nameDynFile = dynamicFile.split("/").length - 1;
				for (int rate:ratio) {	
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
									" -i " + problempath + " -t " + problemType + " -m " + model + " -d "+ distance + " -v " + inverse + " -r "+ rate +"\"\n";
	
							counter++;
						
					}
				}
			}
		}

		String header = "!/bin/bash\n#$ -S /bin/bash\n#$ -N PLEDAc\n#$ -q all.q\n#$ -o out/RKEDAc.$TASK_ID.dat"
				+ "\n#$ -e err/RKEDAc.$TASK_ID.dat\n#$ -cwd\n#$ -t 1-" + (counter - 1) + "\n\n";

		String lastline = "./a.out ${PARAMS[$SGE_TASK_ID]}";

		String out = header + content + "\n" + lastline;
		System.out.println(out);
		BufferedWriter br = new BufferedWriter(new FileWriter("PL/rocks/runBatch500_20.txt"));
		br.write(out);
		br.close();
	}

}
