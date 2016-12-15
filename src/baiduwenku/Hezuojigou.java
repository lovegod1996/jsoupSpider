package baiduwenku;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.SQLHelper;

public class Hezuojigou {
	public static Document getDoc(String BootURL) { // 获取box
		Document document = null;
		int ii = 10;
		String userAgent = "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0";
		while (document == null && ii != 0) {
			ii--;
			try {
				document = Jsoup.connect(BootURL).userAgent(userAgent)
						.timeout(100000).get();
				// System.out.println(document);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return document;
	}

	public static boolean judgeDate(String date) {
		String[] str = date.split("-");
		int year = Integer.parseInt(str[0]);
		if (str[1].startsWith("0"))
			str[1].replace("0", "");
		int month = Integer.parseInt(str[1]);
		if (year >= 2013 || (year == 2012 && month > 5))
			return true;
		return false;
	}

	public static boolean judge(String isFree1, String isFree2, String isFree3) {
		if (isFree1.contains("￥") // 看看是否需要下载劵
				|| !isFree2.replaceAll("[^\\x00-\\xff]", "").trim().equals("0")
				|| isFree3.contains("￥")) {
			// System.out.println(" 收费 --> " + data.getTitleURL());
			return false;
		} else
			return true;
	}

	public static void main(String[] args) {
		for (int i = 0; i <= 3300; i += 30) {
			String Url = "http://wenku.baidu.com/org/more?pn=" + i;
			Document docment = getDoc(Url);
			Elements elements = docment.select(".org-list a");
			for (Element element : elements) {
				try {
					String url = element.select("a").attr("href").trim();
					String author = element.select("a").attr("title");
					Document docments = null;
					try {
						docments = getDoc(url);
					} catch (NullPointerException en) {
						en.printStackTrace();
					}
					String pa = docments
							.select(".doc-menu li:nth-child(1) span").text()
							.replace("（", "").replace("）", "");
					// System.out.println(pa);
					int page = 0;
					try {
						page = Integer.parseInt(pa);
						if (page > 6000)
							page = 6000;
					} catch (Exception e) {
						System.err
								.println("~~~~~~~~~~~~~~~~~~~~~~~~出错啦！！！（该机构无文档）");
					}

					String[] ur = url.split("=");
					String organ = ur[1];

					for (int j = 0; j <= page; j += 20) {
						String URL = "http://wenku.baidu.com/o/" + organ
								+ "?tab=1&od=1&view=0&pay=0&cid=0&pn=" + j;
						Document document1 = null;
						try {
							document1 = getDoc(URL);
						} catch (NullPointerException en) {
							en.printStackTrace();
						}
						Elements elemnts1 = document1.select(".doc-detail tr");

						if (elemnts1 == null) {
							Elements elemnts2 = document1.select(".doc-msg li");
							for (Element element2 : elemnts2) {
								try {
									String title = element2.select(".title")
											.text();
									String pageurl = "http://wenku.baidu.com"
											+ element2.select(".title a")
													.attr("href").trim()
											+ ".html";
									String docid = pageurl.replace(
											"http://wenku.baidu.com/view/", "")
											.replace(".html", "");
									String date = element2.select(
											".upload-time").text();
									Document document2 = null;
									try {
										document2 = getDoc(pageurl);
									} catch (NullPointerException en) {
										en.printStackTrace();
									}
									String isFree1 = document2.select(
											".goods-price").text();// ￥1.00
									String isFree2 = document2.select(
											".btn-download").text();// 1下载劵
									String isFree3 = document2.select(
											".discribe-text>p:nth-child(2)")
											.text();// ppt要钱这种：http://wenku.baidu.com/view/bc61079d6c175f0e7dd1377d.html

									if (judge(isFree1, isFree2, isFree3)) {
										if (judgeDate(date)) {
											Object[] objs = { title, pageurl,
													date, author, docid };
											String str = "insert into wenku_hdx (title,pageurl,date,author,docid) values (?,?,?,?,?)";
											SQLHelper.updateBySQL(str, objs); // 保存
											System.out.println(docid
													+ "正在抓取....");
										} else {
											System.out.println(docid
													+ "超过时间，不能下载.....");
										}
									} else {
										System.out.println(docid
												+ "需要付费，不能下载.....");
									}

								} catch (Exception e) {
									e.printStackTrace();
								}
							}

						} else {

							for (Element element1 : elemnts1) {
								try {
									if (element1.select("td").size() == 0)
										continue;
									String pageurl = "http://wenku.baidu.com"
											+ element1
													.select("td:nth-child(1) a")
													.attr("href").trim()
											+ ".html";
									String title = element1.select(
											"td:nth-child(1) a").attr("title");
									String pay = element1.select(
											"td:nth-child(2)").text();
									String downs = element1.select(
											"td:nth-child(4)").text();
									String date = element1.select(
											"td:nth-child(6)").text();
									String docid = pageurl.replace(
											"http://wenku.baidu.com/view/", "")
											.replace(".html", "");

									if (pay.compareTo("免费") == 0) {
										if (judgeDate(date)) {
											Object[] objs = { title, pageurl,
													date, downs, author, docid };
											String str = "insert into wenku_hdx (title,pageurl,date,downloads,author,docid) values (?,?,?,?,?,?)";
											SQLHelper.updateBySQL(str, objs); // 保存
											System.out.println(docid
													+ "正在抓取....");
										} else {
											System.out.println(docid
													+ "超过时间，不能下载.....");
										}
									} else {
										System.out.println(docid
												+ "需要付费，不能下载.....");
									}

									/*
									 * System.out.println(pageurl);
									 * System.out.println(title);
									 * System.out.println(pay);
									 * System.out.println(downs);
									 * System.out.println(date);
									 */
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							System.out.println("这是" + author + "第"
									+ (j / 20 + 1) + "页");
						}
					}
					/*
					 * System.out.println(organ); System.out.println(url);
					 */

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}
