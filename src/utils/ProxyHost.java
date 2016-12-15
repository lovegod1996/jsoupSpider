/**  
 * @author yokoboy
 */
package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author yokoboy
 */
public class ProxyHost {

	private static ProxyHost proxyHost = new ProxyHost();
	private static boolean fromDataBase = false;

	private ProxyHost() {
	}
	

	public class IP {
		public String ip;
		public Integer port;

		@Override
		public String toString() {
			return ip + ":" + port;
		}
		public IP(String ip,Integer port) {
			this.ip = ip;
			this.port = port;
		}
		public IP(){
			
		}

	}

	private static List<ProxyHost.IP> ips = new ArrayList<ProxyHost.IP>();

	private void init() {
		if (ips != null) {
			ips.clear();
		}
		BufferedReader brBufferedReader = null;
		try {
			FileReader fReader = new FileReader(new File("proxyHost.txt"));
			brBufferedReader = new BufferedReader(fReader);
			for (String line = brBufferedReader.readLine(); line != null; line = brBufferedReader.readLine()) {
				line = line.trim();
				if (line.equals("") || line.startsWith("//")) {
					continue;
				}
				String[] ip_port = line.split(":");
				IP p_i = new ProxyHost.IP();
				p_i.ip = ip_port[0];
				p_i.port = Integer.parseInt(ip_port[1].trim());
				ips.add(p_i);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				brBufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static IP getIp() {
		if (ips == null || ips.size() <= 0) {
			proxyHost.init();
		}
		IP ip = ips.get((int) (Math.random() * ips.size()));
		ips.remove(ip);
		return ip;
	}

	public static IP getIp(boolean isfromDataBase) {
		fromDataBase = isfromDataBase;
		if (fromDataBase) {
			if (ips == null || ips.size() <= 0) {
				System.out.println("==========select==============");
				ArrayList<HashMap<String, Object>> dataTable = SQLHelper.selectBySQL("select ip_port from ips where mark>-1 and mark<100000 order by mark limit 100");
				for (HashMap<String, Object> dataRow : dataTable) {
					String[] ip_port = dataRow.get("ip_port").toString().split(":");
					IP p_i = proxyHost.new IP();
					p_i.ip = ip_port[0];
					p_i.port = Integer.parseInt(ip_port[1].trim());
					ips.add(p_i);
				}
			}
			IP ip = ips.get((int) (Math.random() * ips.size()));
			ips.remove(ip);
			return ip;
		} else {
			return getIp();
		}
	}

	public static void removeIp(IP ip) {
		if (ip == null) {
			return;
		}
		if (fromDataBase) {

			SQLHelper.updateBySQL("update ips set mark=mark+1 where ip_port='" + ip.toString() + "'");
		} else {
			removeIp(ip.toString());
		}
	}

	public synchronized static void removeIp(String ip) {
		StringBuilder sbBuilder = new StringBuilder();
		BufferedReader brBufferedReader = null;
		try {
			FileReader fReader = new FileReader(new File("proxyHost.txt"));
			brBufferedReader = new BufferedReader(fReader);
			for (String line = brBufferedReader.readLine(); line != null; line = brBufferedReader.readLine()) {
				line = line.trim();
				if (line.equals("") || line.startsWith("//") || line.equals(ip.toString())) {
					continue;
				}
				sbBuilder.append(line).append("\r\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				brBufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter fwFileWriter = null;
		try {
			fwFileWriter = new FileWriter(new File("proxyHost.txt"));
			fwFileWriter.write(sbBuilder.toString());
			fwFileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fwFileWriter != null) {
				try {
					fwFileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] asd) {

		for (int i = 0; i < 100; i++) {
			System.out.println(getIp(true));
		}
	}
}
