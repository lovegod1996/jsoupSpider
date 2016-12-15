package cn.edu.scut;

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

public class scut {
	private static String createPath = "";
	private static String tableName = ReadConfig.tablename;
	private static String fileDirHead = ReadConfig.filedir.endsWith("\\") ? ReadConfig.filedir
			: ReadConfig.filedir + "\\";
	/*.........................................................*/
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

	/*....................................................................*/
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
		// System.out.println("完成！");
		return "/Download/"
				+ filedir.replace(fileDirHead, "").replace("\\", "/");
	}
/*..............................................................................*/
	
	
	public static void Update(String title, String description, String Input,
			String Output, String difficulty, String Sample_Input,
			String Sample_Output, String num1, String num2,String tips) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "INSERT INTO acm_scut"
					+ "(title,describe_,input,output,difficulty,sample_input,sample_output,other1,other2,prompt)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, title);
			ps.setString(2, description);
			ps.setString(3, Input);
			ps.setString(4, Output);
			ps.setString(5, difficulty);
			ps.setString(6, Sample_Input);
			ps.setString(7, Sample_Output);
			ps.setString(8, num1);
			ps.setString(9, num2);
			ps.setString(10, tips);
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
	int i=1002;
	
	int m=1;
	for(i=1002;i<2353;i++)
	{
		String url="http://222.201.146.216/JudgeOnline/problem.php?id="+i;
		Document doc=getDoc(url);
		Elements els=doc.select("#main");
		for (Element element : els) {
			try{
				
			
			
			String title=element.select("center>h2").text().replace(i+":", "");
			String[] L=element.select("center").first().ownText().split(" ");
			String num1=L[0]+" Src";
			String num2=L[1].replace("Sec  ", "")+"  MB";
			String  num4=L[3];
		   String[] Z=num4.split("  ");
			String up=Z[0];
		    String su=Z[1];
			double numR=Double.parseDouble(su);
			double numL=Double.parseDouble(up);
			double num=numR/numL;
			String difficulty=null;
			if(num>0.9)
				difficulty="入门";
			else if(num>0.7)
			difficulty="初级";
			else if(num>0.3)
				difficulty="中级";
			else
				difficulty="高级";
			String description=element.select(".content").first().html();
			if(description==null){
			description=element.select(".content>p").first().html();
			}
			String Input=element.select(".content").get(1).html();
			if(Input==null){
				Input=element.select(".content>p").get(1).html();
			}
			String Output=element.select(".content").get(2).html();
			if(Output==null)
			{
				Output=element.select(".content>p").get(2).html();
			}
			String Sample_Input=element.select(".content").get(3).text();
			String Sample_Output=element.select(".content").get(4).text();
			String tips=element.select(".content").get(5).text();
			
			
			
			
		//	Update(title, description, Input, Output, difficulty, Sample_Input, Sample_Output, num1, num2, tips);
			
			
			System.out.println(title);
			System.out.println(num1);
			System.out.println(num2);
			System.out.println(difficulty);
			System.out.println("描述：" +description);
			System.out.println("输入： "+Input);
			System.out.println("输出： "+Output);
			System.out.println("样例输入："+Sample_Input);
			System.out.println("样例输出： "+Sample_Output);
			System.out.println("提示： "+tips);
			System.out.println(m++);
			System.out.println();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

		
		
		
		
		
		
	}

}
