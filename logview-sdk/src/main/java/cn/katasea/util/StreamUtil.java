package cn.katasea.util;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class StreamUtil {
     public static String readInputStream(InputStream inputStream)throws IOException {
    	 BufferedReader reader = new BufferedReader(new InputStreamReader(
 				inputStream, "UTF-8"));
 		String str = "", sCurrentLine = "";
 		while ((sCurrentLine = reader.readLine()) != null) {
 			str = str + sCurrentLine;
 		}
 		reader.close();
 		return str;
     }

	public static String readInputStream(InputStreamReader inputStreamReader)throws IOException {
		 BufferedReader reader = new BufferedReader(inputStreamReader);
	 		String str = "", sCurrentLine = "";
	 		while ((sCurrentLine = reader.readLine()) != null) {
	 			str = str + sCurrentLine;
	 		}
	 		reader.close();
	 		return str;
	}
}
