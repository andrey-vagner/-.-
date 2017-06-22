import java.io.*;

public class FileHelper {
	public void fileInput(String filename) throws IOException {
		File file = new File(filename);
		Reader reader = new FileReader(file);
		BufferedReader breader = new BufferedReader(reader);
		String line;
		
		while((line = breader.readLine()) != null) {
			System.out.println(line);
		}       
	}
}
