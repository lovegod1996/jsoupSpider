package cn.edu.zoj;

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

import utils.ProxyHost;
import utils.ReadConfig;
import utils.UserAgent;
import utils.ProxyHost.IP;
import Jsoup.JdbcUtils;

public class zoj_acm {
	private static String createPath = "";
	private static String tableName = ReadConfig.tablename;
	private static String fileDirHead = ReadConfig.filedir.endsWith("\\") ? ReadConfig.filedir
			: ReadConfig.filedir + "\\";

	public static void Update(String title, String description, String Input,
			String Output, String difficulty, String Sample_Input,
			String Sample_Output, String other, String tips) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "INSERT INTO acm_zoj"
					+ "(title,describe_,input,output,difficulty,sample_input,sample_output,other,prompt)"
					+ "VALUES(?,?,?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, title);
			ps.setString(2, description);
			ps.setString(3, Input);
			ps.setString(4, Output);
			ps.setString(5, difficulty);
			ps.setString(6, Sample_Input);
			ps.setString(7, Sample_Output);
			ps.setString(8, other);
			ps.setString(9, tips);
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
		int i, m = 1;
		String title = null;
		String other1 = null;
		String other2 = null;
		String[] other = null;
		String description = null;
		String Input = null;
		String Output = null;
		String Sample_Input = null;
		String Sample_Output = null;
		String tips = null;
		String difficulty = null;
		for (i = 1; i < 30; i++) {
			String url = "http://acm.zju.edu.cn/onlinejudge/showProblems.do?contestId=1&pageNumber="
					+ i;
			Document doc = getDoc(url);
			Elements els = doc.select(".list>tbody>tr");

			for (Element element : els) {
				try {
					String id = "http://acm.zju.edu.cn"
							+ element.select(".problemId>a").attr("href")
									.trim();
					String num = element.select(".problemStatus").first()
							.ownText();
					String num1 = num.replace("% (/)", " ");
					double num2 = Double.parseDouble(num1);

					if (num2 > 90)
						difficulty = "入门";
					else if (num2 > 70)
						difficulty = "初级";
					else if (num2 > 30)
						difficulty = "中级";
					else
						difficulty = "高级";

					// System.out.println(difficulty);
					// System.out.println(id);

					Document dc = getDoc(id);
					Elements els1 = dc.select("#content");

					String html = els1.html();
					Elements img = els1.select("img");
					String img_url = null;
					String download_url = null;
					String path = null;
					/*
					 * if(img.size()!=0) System.out.println(img);
					 * System.out.println();
					 */
					for (Element element2 : img) {
						img_url = element2.attr("src");
						download_url = "http://acm.zju.edu.cn/onlinejudge/"
								+ img_url;
						path = downLoad(download_url);
						if (!path.contains("ERROR")) {
							html = html.replace(img_url, path);
						}
					}

					Document document = Jsoup.parse(html);
					// if(img.size()!=0)
					// System.out.println(document);
					Elements elements = document.select("#content_body");
					for (Element element3 : elements) {
						try {
							title = element3.select("center").first().text();
							other = element3.select("center").get(1).text()
									.split("      ");
							other1 = other[0].replace("Time Limit:", "");
							other2 = other[1].replace("Memory Limit:", "");
							
							Elements elements1=element3.select("p");
							for (Element element4 : elements1) {
								
					
							if (!element4.html().contains("Input")
									&& !element4.html()
											.contains("Output")
									&& !element4.html()
											.contains("Sample Input")
									&& !element4.html()
											.contains("Sample Output"))

								description = element4
										.html();
							if(element4.html().contains("Input"))
								Input=element4.html();
							if(element4.html().contains("Output"))
								Output=element4.html();
							if(element4.html().contains("Sample Input"))
								Sample_Input=element4.html();
							if(element4.html().contains("Sample Output"))
								Sample_Output=element4.html();
							
							
							}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println(title);
				System.out.println(other1);
				System.out.println(other2);
				//System.out.println(description);
				 System.out.println(Input);
				 System.out.println(Output);
				 System.out.println(Sample_Input);
				 System.out.println(Sample_Output);
			}

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
