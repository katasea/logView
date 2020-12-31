package cn.katasea.main.service.util;

import cn.katasea.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
/**
 * @author katasea
 * 2020/12/29 14:01
 */
@Slf4j
public class CommandUtil {
	public static String executeWindows(String cmd) throws IOException {
		String result = "";
		if(CommonUtil.isNotEmpty(cmd)) {
			Process proc = null;
			try {
				proc = Runtime.getRuntime().exec("cmd");
			} catch (IOException e) {
				e.printStackTrace();
			} if (proc != null) {
				BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(),"GBK"));
				PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream(),"GBK")), true);
				String[] cmdArr = cmd.split("&");
				if(cmdArr.length == 0) {
					cmdArr = new String[]{cmd};
				}

				try {
					boolean skip = false;
					for(String cmdStr : cmdArr) {
						out.println(cmdStr);
						if(cmdStr.contains(".bat")) {
							skip = true;
							result = "正在后台执行启动命令，请稍候用netstat -ano 查看是否启动成功！";
						}
						if(cmdStr.contains("date")) {
							throw new Exception("系统不支持执行阻塞性cmd语句");
						}
					}
					out.println("exit");
					String line;

					while ((line = in.readLine()) != null && !skip) {
						log.info("输出信息：{}",line);
						result += line + "\n";
					}
					proc.waitFor(3, TimeUnit.SECONDS);

				} catch (Exception e) {
					e.printStackTrace();
					log.error("执行cmd脚本报错了，错误：{}",e.getMessage());
				}finally {
					in.close();
					out.close();
					proc.destroy();
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
				process.waitFor(3, TimeUnit.SECONDS);
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
