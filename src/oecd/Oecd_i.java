package oecd;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.SQLHelper;

public class Oecd_i {
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "http://www.oecd-ilibrary.org/#";
		String Country = null;
		String Url = "";
		String title = "";
		String pageurl = "";
		String Content = "";
		String type = "";
		String down = "";
		String URL = "";
		Document document = getDoc(url);
		Elements elements = document.select("#countriesList>.cList li");
		System.out.println(elements.size());
		for (Element element : elements) {
			try {
				Country = element.select("a").text();
				Url = "http://www.oecd-ilibrary.org"
						+ element.select("a").attr("href").trim();
				Document document1 = null;
				Elements elements1 = null;
				Elements elemenst2 = null;
				Elements elements3 = null;
				Elements elements4 = null;
				try {
					document1 = getDoc(Url);
				} catch (NullPointerException en) {
					en.printStackTrace();
				}
				elements1 = document1.select(".rightalign");
				if (elements1.html().contains("li")) {
					elements3 = elements1.select("li");
					int m = elements3.size();
					String str = elements3.get(m - 2).text();
					int n = Integer.parseInt(str);
					char[] cha = Country.toCharArray();
					for (int i = 1; i < cha.length; i++) {
						System.out.println(cha[i] + " ");
						for (int j = 0; j < n; j++) {
							URL = "http://www.oecd-ilibrary.org/search?value9=&value7=&value8=&value5=&value6=&value3=&value4=&value1=*&value2="
									+ Character.toLowerCase(cha[0])
									+ cha[i]
									+ "&option14=&option1=fullText&option15=&option2=country&option3=&option16=&option4=&option17=&option18=&option19=&noRedirect=true&option10=&value20=http%3a%2f%2foecd.metastore.ingenta.com%2fns%2fIGO&option11=&option12=&option13=&value10=&value11=&value12=&value13=&value14=&value15=&value16=&value17=&value18=&value19=&option9=&option8=&option7=&option6=&option5=&option20=excludeImprintType&pageSize=20&page="
									+ j;
							Document document2 = null;
							try {
								document2 = getDoc(URL);
							} catch (NullPointerException en) {
								en.printStackTrace();
							}
							elements4 = document2
									.select(".content>.listing>tbody>tr");
							for (Element element3 : elements4) {
								try {
									Content = element3.select(
											"[class=type nowrap box3]>strong")
											.text();
									type = element3.select(
											"[class=type nowrap box3]>em")
											.text();
									title = element3.select(".articleItem ")
											.text();
									pageurl = "http://www.oecd-ilibrary.org"
											+ element3.select(".articleItem")
													.attr("href");
									Object[] objs = { Url, Country, Content, type,
											title, pageurl };
									String str1 = "insert into oecd_ilibrary (Url,country,content,type,title,pageurl) values (?,?,?,?,?,?)";
										SQLHelper.updateBySQL(str1, objs); // 保存
										System.out.println(pageurl + "........正在抓取");
									/*System.out.println(Content);
									System.out.println(type);
									System.out.println(title);
									System.out.println(pageurl);*/
									System.out.println();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

						}
					}
				} else {
					elemenst2 = document1.select(".content>.listing>tbody>tr");
					for (Element element3 : elemenst2) {
						try {
							Content = element3.select(
									"[class=type nowrap box3]>strong").text();
							type = element3.select(
									"[class=type nowrap box3]>em").text();
							title = element3.select(".articleItem ").text();
							pageurl = "http://www.oecd-ilibrary.org"
									+ element3.select(".articleItem").attr(
											"href");

							Object[] objs = { Url, Country, Content, type,
									title, pageurl };
							String str = "insert into oecd_ilibrary (Url,country,content,type,title,pageurl) values (?,?,?,?,?,?)";
							SQLHelper.updateBySQL(str, objs); // 保存
							System.out.println(pageurl + "........正在抓取");
							/*System.out.println(Content);
							System.out.println(type);
							System.out.println(title);
							System.out.println(pageurl);*/
							System.out.println();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}

				/*System.out.println(Country);
				System.out.println(Url);
				System.out.println();*/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
