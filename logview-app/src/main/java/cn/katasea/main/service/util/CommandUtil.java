package cn.katasea.main.service.util;

import cn.katasea.util.CommonUtil;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
/**
 * @author katasea
 * 2020/12/29 14:01
 */

public class CommandUtil {
	private static Process proc = null;
	public static String executeWindows(String cmd) throws IOException {
		String result = "";
		if(CommonUtil.isNotEmpty(cmd)) {
			Process proc = null;
			try {
				proc = Runtime.getRuntime().exec("cmd.exe");
			} catch (IOException e) {
				e.printStackTrace();
			} if (proc != null) {
				BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(),"GBK"));
				PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream(),"GBK")), true);
				String[] cmdArr = cmd.split("&");
				if(cmdArr.length == 0) {
					cmdArr = new String[]{cmd};
				}
				for(String cmdStr : cmdArr) {
					out.println(cmdStr);
				}
				out.println("exit");
				try { String line;
					while ((line = in.readLine()) != null) {
						System.out.println(line);
						result += line + "\n";
					}
					proc.waitFor();
					in.close();
					out.close();
					proc.destroy();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	public static String run(String command) throws IOException {
		Scanner input = null;
		String result = "";
		Process process = null;
		try {
			process = Runtime.getRuntime().exec( new String[]{"bash","-c",command});
			try {
				//等待命令执行完成
				process.waitFor(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			InputStream is = process.getInputStream();
			input = new Scanner(is);
			while (input.hasNextLine()) {
				result += input.nextLine() + "\n";
			}
			result = result; //加上命令本身，打印出来
		} finally {
			if (input != null) {
				input.close();
			}
			if (process != null) {
				process.destroy();
			}
		}
		return result;
	}

	public static void main(String[] args) throws IOException {
//		System.out.println(CommandUtil.run("netstat -ano|findstr '11211'"));
//		System.out.println(CommandUtil.run("dir"));
//		System.out.println(CommandUtil.execute("netstat -ano"));
		System.out.println(CommandUtil.executeWindows("cd E:/windows/project/ylz_soft/电子票据接口平台-发布环境/  & start 1启动电子票据接口平台.bat"));
//		System.out.println(CommandUtil.execute("E:"));
//		System.out.println(CommandUtil.execute("dir"));
	}

}
