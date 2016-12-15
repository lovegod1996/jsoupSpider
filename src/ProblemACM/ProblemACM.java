package ProblemACM;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.ProxyHost;
import utils.ReadConfig;
import utils.UserAgent;
import utils.ProxyHost.IP;
import Jsoup.JdbcUtils;

import com.mysql.jdbc.PreparedStatement;

public class ProblemACM {
	private static String createPath = "";
	private static String tableName = ReadConfig.tablename;
	private static String fileDirHead = ReadConfig.filedir.endsWith("\\") ? ReadConfig.filedir
			: ReadConfig.filedir + "\\";

	/*
	 * ...........................................................................
	 * .....
	 */
	private synchronized static String manageFilePath() {
		if (createPath == null || "".endsWith(createPath)) {
			createPath = fileDirHead
					+ UUID.randomUUID().toString().replace("-", "") + "\\";
			new File(createPath).mkdirs(); // 创建此抽象路径名指定的目录，包括所有必需但不存在的父目录。
		}
		File file = new File(createPath);
		int length = file.list().length;
		if (length >= 2000) {
			createPath = fileDirHead
					+ UUID.randomUUID().toString().replace("-", "") + "\\";
			new File(createPath).mkdirs();
		}
		return createPath;
	}

	/*........................................................................... */
	private static String downLoad(String pdf_url) {
		String type = pdf_url.substring(pdf_url.lastIndexOf("."));
		String filedir = manageFilePath()
				+ UUID.randomUUID().toString().replace("-", "") + type;
		BufferedInputStream bufferedInputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		IP ip = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			if (ReadConfig.isProxy) {
				HttpHost proxy = null;
				if (ReadConfig.isuseGoAgent) {
					proxy = new HttpHost(ReadConfig.proxyIp,
							ReadConfig.proxyPort);
				} else {
					ip = ProxyHost.getIp(true);
					proxy = new HttpHost(ip.ip, ip.port);
				}
				httpClient.getParams().setParameter(
						ConnRoutePNames.DEFAULT_PROXY, proxy);
			}
			httpClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 60 * 1000);// 鏉╃偞甯撮弮鍫曟？1閸掑棝鎸?
			httpClient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, 60 * 1000);// 閺佺増宓佹导鐘虹翻閺冨爼妫?鐏忓繑妞?
			httpClient.getParams().setParameter(
					ClientPNames.ALLOW_CIRCULAR_REDIRECTS, false);
			HttpGet httpGet = new HttpGet(pdf_url.trim());
			httpGet.setHeader("Cookie", ReadConfig.cookie);
			httpGet.setHeader("User-Agent", UserAgent.getUserAgent());
			HttpResponse execute = httpClient.execute(httpGet);
			// System.err.println(execute.getStatusLine());
			if (execute.getStatusLine().getStatusCode() != 200) {
				if (ip != null) {
					int code = execute.getStatusLine().getStatusCode();
					// System.out.println(ip.ip + ":" + ip.port + "---" + code +
					// "--");
					ProxyHost.removeIp(ip);
				}
				return "ERROR";
			}
			HttpEntity entity = execute.getEntity();
			InputStream inputStream = entity.getContent();
			bufferedInputStream = new BufferedInputStream(inputStream);
			bufferedOutputStream = new BufferedOutputStream(
					new FileOutputStream(new File(filedir)));
			byte[] buf = new byte[1024];
			for (int len = 0; (len = bufferedInputStream.read(buf)) != -1;) {
				bufferedOutputStream.write(buf, 0, len);
				bufferedOutputStream.flush();
			}
		} catch (Exception e) {
			if (ip != null) {
				ProxyHost.removeIp(ip);
			}
			File file = new File(filedir);
			if (file.exists()) {
				file.delete();
			}
			System.out.println(pdf_url);
			e.printStackTrace();
			return "ERROR";
		} finally {
			try {
				if (bufferedInputStream != null) {
					bufferedInputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (bufferedOutputStream != null) {
					bufferedOutputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("下载完成！");
		return "/Download/"
				+ filedir.replace(fileDirHead, "").replace("\\", "/");
	}

	/* ...................................................................... */
	public static void Update(String title, String description, String input,
			String output, String difficult, String sampleinput,
			String sampleoutput, String other1, String other2) {
		java.sql.Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "INSERT INTO acm_hdu"
					+ "(title,describe_,input,output,difficulty,sample_input,sample_output,other1,other2)"
					+ "VALUES(?,?,?,?,?,?,?,?,?)";
			ps = (PreparedStatement) ((java.sql.Connection) conn)
					.prepareStatement(sql);
			ps.setString(1, title);
			ps.setString(2, description);
			ps.setString(3, input);
			ps.setString(4, output);
			ps.setString(5, difficult);
			ps.setString(6, sampleinput);
			ps.setString(7, sampleoutput);
			ps.setString(8, other1);
			ps.setString(9, other2);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.free(rs, ps, (java.sql.Connection) conn);
		}
	}

	public static Document connect(String url) {
		Document document1 = null;
		int ii = 1;
		while (ii <= 10) { // 请求超时多次请求
			try {
				document1 = // GetDocument.changeIp(url);
				Jsoup.connect(url)
						.userAgent(
								"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
						.ignoreContentType(true).timeout(3000).get();
				return document1;
			} catch (Exception e1) {
				ii++;
			}
		}
		return null;
	}

	public static void main(String[] args) {
		int n = 0;

		String url = null;
		int i = 0;
		for (i = 1; i < 45; i++) {
			url = "http://acm.hdu.edu.cn/listproblem.php?vol=" + i;
			Document Doc = connect(url);
			String[] s = Doc.select("table.table_text>tbody>script").html()
					.split(";");
			String URL = null;
			for (n = 1; n < s.length; n++) {
				try {
					String id = s[n].split(",")[1];
					String L = s[n].split(",")[4];
					String R = s[n].split(",")[5].replace(")", "");
					double numL = Double.parseDouble(L);
					double numR = Double.parseDouble(R);
					String difficult;
					double num = numL / numR;
					if (num > 0.9) {
						difficult = "入门";
					} else if (num > 0.7) {
						difficult = "初级";
					} else if (num > 0.3) {
						difficult = "中级";
					} else {
						difficult = "高级";
					}
					URL = "http://acm.hdu.edu.cn/showproblem.php?pid=" + id;

					Document doc = connect(URL);
					Elements el = doc
							.select("body>table>tbody>tr:nth-child(4)");
					
					String html = el.html();
					Elements imgs = el.select("img");
					// if(imgs.size()!=0)System.out.println(el+"-----");
					for (Element element : imgs) {
						// String img_url = element.attr("src");
						
						String str = null;
						String img_url = null;
						str = element.attr("src");
						img_url = "http://acm.hdu.edu.cn"
								+ str.replace("../..", "");
						
						// System.out.println(element+"---++--");
						/*
						 * if (!element.html().contains("../..")) { str =
						 * element.attr("src"); img_url =
						 * "http://acm.hdu.edu.cn" + str; } else { str =
						 * element.attr("src"); img_url =
						 * "http://acm.hdu.edu.cn" + str.replace("../..", ""); }
						 */

						String path = downLoad(img_url);
						if (!path.contains("ERROR")) {
							
							// html = html.replaceAll(img_url, path);
							html = html.replaceAll(str, path);

						}
					}
					// if(imgs.size()!=0) System.out.println(html);
					Document document = Jsoup.parse(html);

					Elements els = null;
					els = document.select("*");
					// System.out.println(els);
					String description = "";
					String input ="";
					String output ="";
					String sampleinput ="";
					String sampleoutput ="";
					String[] other =null;
					String other1 ="";
					String other2 ="";
					String title ="";
					for (Element element : els) {
						try {
							// String ONE=element.html();
							 description = element
									.select("div[class*=panel_content]").get(0)
									.html();
							// String description=descriptionElement.html();
							Element inputElement = element.select(
									"div[class*=panel_content]").get(1);
							 input = inputElement.html();
							Element outputElement = element.select(
									"div[class*=panel_content]").get(2);
							 output = outputElement.html();
							 sampleinput = element
									.select("div[class*=panel_content]").get(3)
									.text();
							 sampleoutput = element
									.select("div[class*=panel_content]").get(4)
									.text();
							 other = element.select("font>b>span")
									.text().split("(Java/Others)");
							 other1 = other[0].replace("Time Limit:", "")
									.replace("(", "");
							other2 = other[1].replace(
									")    Memory Limit:", "").replace("(", "");
							 title = element.select("h1").text();

							/*Update(title, description, input, output,
									difficult, sampleinput, sampleoutput,
									other1, other2);*/
							/*System.out.println("标题： " + title);
							System.out.println("描述：   " + description);
							// System.out.println("输入    " + input);
							// System.out.println("输出    " + output);
							// System.out.println("样例输入：" + sampleinput);
							// System.out.println("样例输出： " + sampleoutput);
							System.out.println("其他： " + other1);
							System.out.println("其他： " + other2);
							System.out.println();*/
						} catch (Exception e) {
							// e.printStackTrace();
							// System.out.println(URL);
						}
						
					}
					Update(title, description, input, output, difficult, sampleinput, sampleoutput, other1, other2);
					System.out.println("标题： " + title);

					/*
					 * System.out.println(URL); System.out.println(numL);
					 * System.out.println(num); System.out.println(difficult);
					 */
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}
	}

}
