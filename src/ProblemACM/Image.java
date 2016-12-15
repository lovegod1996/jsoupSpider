package ProblemACM;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
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

import utils.GetDocument;
import utils.ProxyHost;
import utils.ReadConfig;
import utils.SQLHelper;
import utils.UserAgent;
import utils.ProxyHost.IP;

public class Image {
	private static String createPath = "";
	private static String tableName = ReadConfig.tablename;
	private static String fileDirHead = ReadConfig.filedir.endsWith("\\") ? ReadConfig.filedir
			: ReadConfig.filedir + "\\";
	public static void main(String[] args) {
		
		
		String url = "http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1038";
		
		Document doc = connect(url);
		Elements el = doc.select("#content");
		
        String html = el.html();
        
        Elements imgs = el.select("img");
        for (Element element : imgs) {
        	//String img_url = element.attr("src");
        
        	 String str = element.attr("src");
        	// System.out.println(str);
        	  String img_url = "http://acm.zju.edu.cn/onlinejudge/" +str;
        	
        	String path = downLoad(img_url);
        	if(!path.contains("ERROR"))
        		
		    {
        		//System.out.println(path);
		    	html = html.replace(str, path);
		    //	html = html.replaceAll(str, path);
		    	
		    }
        	if(html.contains("showImage.do?name=0000%2F1038%2F1038.gif"))
        		System.out.println("-----------------------------");
		}
        Document document = Jsoup.parse(html);
        
        System.out.println(document);
        
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
	
    private  static String downLoad(String pdf_url) {
		String	type = pdf_url.substring(pdf_url.lastIndexOf("."));
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
		return "/Download/"+filedir.replace(fileDirHead, "").replace("\\", "/");
	}
	
	public static Document connect(String url) {
		Document document1 = null;
		int ii = 1;
		while (ii <= 10) { // 请求超时多次请求
			try {
				document1 = //GetDocument.changeIp(url);
				Jsoup
						.connect(url)
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
}
