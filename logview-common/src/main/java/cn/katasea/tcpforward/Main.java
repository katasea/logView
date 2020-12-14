package cn.katasea.tcpforward;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
	/**
	 * java -jar blog119-0.0.1-SNAPSHOT.jar 127.0.0.1 65111 10.30.1.106 22
	 * 当前服务器ServerSocket的最大连接数
	 */
	private static final int MAX_CONNECTION_NUM = 50;

	public static void main(String[] args) throws UnknownHostException {
		// 启动一个新线程。检查是否要种植程序。
		new Thread(new CheckRunnable()).start();

		// 当前服务器的IP地址和端口号。
		String thisIp = args[0];
		int thisPort = Integer.parseInt(args[1]);

		// 转出去的目标服务器IP地址和端口号。
		String outIp = args[2];
		int outPort = Integer.parseInt(args[3]);

		ServerSocket ss = null;
		try {
			ss = new ServerSocket(thisPort, MAX_CONNECTION_NUM, InetAddress.getByName(thisIp));

			while(true){
				// 用户连接到当前服务器的socket
				Socket s = ss.accept();

				// 当前服务器连接到目的地服务器的socket。
				Socket client = new Socket(outIp, outPort);

				// 读取用户发来的流，然后转发到目的地服务器。
				new Thread(new ReadWriteRunnable(s, client)).start();

				// 读取目的地服务器的发过来的流，然后转发给用户。
				new Thread(new ReadWriteRunnable(client, s)).start();

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != ss) {
					ss.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}