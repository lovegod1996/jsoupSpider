package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

import utils.ProxyHost.IP;

public class GetDocument {
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
	
	public static Document changeIp(String url) {
		StringBuilder sBuilder = null;
		InputStream inputStream = null;
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
			HttpGet httpGet = new HttpGet(url.trim());

			httpGet.setHeader("User-Agent", UserAgent.getUserAgent());

			httpGet.setHeader("Referer", "http://www.ncbi.nlm.nih.gov/");
		//	httpGet.setHeader("Referer", "http://epub.cnki.net/kns/brief/brief.aspx?curpage=2&RecordsPerPage=50&QueryID=8&ID=&turnpage=1&tpagemode=L&dbPrefix=SCDB&Fields=&DisplayMode=listmode&PageName=ASP.brief_default_result_aspx");
			// httpGet.setHeader("Cookie", ReadConfig.cookie);
			 httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			HttpResponse execute = httpClient.execute(httpGet);
			System.err.println(execute.getStatusLine());
			if (execute.getStatusLine().getStatusCode() != 200) {

				if (ip != null) {
					int code = execute.getStatusLine().getStatusCode();
					System.out.println(ip.ip + ":" + ip.port + "---" + code
							+ "--");
					ProxyHost.removeIp(ip);
				}
				return null;
			}
			BufferedReader bReader = null;
			sBuilder = new StringBuilder();
			HttpEntity entity = execute.getEntity();
			inputStream = entity.getContent();
			bReader = new BufferedReader(new InputStreamReader(inputStream,
					"utf-8"));
			for (String line = bReader.readLine(); line != null; line = bReader
					.readLine()) {
				sBuilder.append(line).append("\r\n");
			}

		} catch (Exception ex) {
			if (ip != null) {
				ProxyHost.removeIp(ip);
			}
			ex.printStackTrace();

		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		// System.out.println("---"+sBuilder.toString());
		Document document = null;
		if (sBuilder != null) {
			document = Jsoup.parse(sBuilder.toString());
		}

		return document;

	}
}
