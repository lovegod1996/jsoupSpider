package cn.edu.hunnu;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Jsoup.JdbcUtils;
import utils.ProxyHost;
import utils.ReadConfig;
import utils.UserAgent;
import utils.ProxyHost.IP;

public class hunnu_acm {
	private static String createPath = "";
	private static String tableName = ReadConfig.tablename;
	private static String fileDirHead = ReadConfig.filedir.endsWith("\\") ? ReadConfig.filedir
			: ReadConfig.filedir + "\\";

	public static void Update(String title, String other, String judget,
			String description, String input, String output,
			String sampleinput, String sampleoutput, String difficult) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "INSERT INTO acm_hunnu"
					+ "(title,describe_,input,output,difficulty,sample_input,sample_output,other,prompt)"
					+ "VALUES(?,?,?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, title);
			ps.setString(2, description);
			ps.setString(3, input);
			ps.setString(4, output);
			ps.setString(5, difficult);
			ps.setString(6, sampleinput);
			ps.setString(7, sampleoutput);
			ps.setString(8, other);
			ps.setString(9, judget);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}

	}

	public static Document getDoc(String BootURL) { // 获取box
		Document document = null;
		int ii = 10;
		String userAgent = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36";
		while (document == null && ii != 0) {
			ii--;
			try {
				document = Jsoup.connect(BootURL).userAgent(userAgent)
						.timeout(100000).get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return document;
	}

	public static void main(String[] args) {
		int i = 1, m = 1;
		Document doc = null;
		Elements els = null;
		for (i = 10000; i < 11560; i++) {
			String url = "http://acm.hunnu.edu.cn/online/?action=problem&type=show&id="
					+ i + "&courseid=96";
			try {
				doc = getDoc(url);
				els = doc.select("body>table>tbody>tr:nth-child(2)>td:nth-child(2)");
			} catch (NullPointerException en) {
				en.printStackTrace();
			}
			String html = els.html();
			Elements img = els.select("img");
			for (Element element2 : img) {
				String img_url = element2.attr("src");
				String download_url = "http://acm.hunnu.edu.cn/online/"
						+ img_url;
				String path = downLoad(download_url);
				if (!path.contains("ERROR")) {
					html = html.replaceAll(img_url, path);
				}
			}
			// System.out.println(html);
			Document document = Jsoup.parse(html);
			//System.out.println(html);
			String T = "";
			String A = "";
			Elements elements = document.select("table[bgcolor=#FFFFFF]>tbody");
			//System.out.println(elements);
			String title = "";
			String other = "";
			String[] diffi = null;
			String difficult = "";
			String description = "";
			String input = "";
			String output = "";
			String sampleinput = "";
			String sampleoutput = "";
			String judge = "";
			String judget = null;
			//System.out.println(elements);
			for (Element element : elements) {
				try{
			title = element.select("tr").first().text();
				other = element.select("tr").get(1).text();
				diffi = element.select("tr>td").get(2).ownText().split(",");
				T = diffi[0];
				if (diffi.length == 2)
					A = diffi[1];
				double numT = Double.parseDouble(T);
				double numA = Double.parseDouble(A);
				double num = numA / numT;
				difficult = null;
				if (num > 0.9) {
					difficult = "入门";
				} else if (num > 0.7) {
					difficult = "初级";
				} else if (num > 0.3) {
					difficult = "中级";
				} else
					difficult = "高级";

				// System.out.println(T);
				// System.out.println(A);
				description = element.select("tr").get(5).html();
				input = element.select("tr").get(7).html();
				output = element.select("tr").get(9).html();
				sampleinput = element.select("tr").get(11).text();
				sampleoutput = element.select("tr").get(13).text();
				judge = element.select("tr").get(14).text();

				if (judge != "Judge Tips") {
					judget = null;
				} else {
					judget = element.select("tr").get(15).text();
				}
				//System.out.println("标题：  " + title);
				/*
				 * Update(title, other, judget, description, input, output,
				 * sampleinput, sampleoutput, difficult);
				 * 
				 * System.out.println("标题：  " + title);
				 * 
				 * System.out.println("其他： " + other); System.out.println("难度： "
				 * + difficult); System.out.println("描述： " + description);
				 * System.out.println("输入： " + input); System.out.println("输出： "
				 * + output); System.out.println("样例输入：" + sampleinput);
				 * System.out.println("样例输出： " + sampleoutput);
				 * System.out.println("提示： " + judget); System.out.println(url);
				 */
				// System.out.println(els.size());
				}catch(Exception e){
					e.printStackTrace();
				}
				}
			 System.out.println("标题：  " + title);
			 System.out.println(m++);
			// System.out.println("其他： " + other);
			// System.out.println("描述： " + description);
			Update(title, other, judget, description, input, output,
			 sampleinput, sampleoutput, difficult);
		}
	}

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

}
