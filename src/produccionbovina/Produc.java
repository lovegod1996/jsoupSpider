package produccionbovina;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.SQLHelper;

public class Produc {

	public void Analyze(String collUrl, String collection, String pageurl,
			String seccoll) {
		try {
			Document document = null;
			Elements elements = null;
			Elements elements1 = null;
			String title = "";
			String tit[] = null;
			String author = "";
			String page = "";
			String down = "";
			String cambio = "";

			try {
				document = getDoc(pageurl);
				elements = document.select(".WordSection1>div[align=center]");
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(elements.size());
			elements.remove(elements.size() - 1);
			System.out.println(elements.size());
			try {
				elements1 = elements.select(".MsoNormalTable tr");
			} catch (Exception e) {
				e.printStackTrace();
			}
			String[] pa = pageurl.split("/");
			String ml = pa[pa.length - 1];

			String url = pageurl.replace(ml, "");
			for (Element element : elements1) {
				try {
					if (!element.text().contains("PDF"))
						continue;
					tit = element.select("td:nth-child(2)").text().split("-");
					title = tit[0];
					author = tit[1];
					page = element.select("td:nth-child(3)").text();
					down = url
							+ element.select("td:nth-child(4) a").attr("href")
									.trim();
					cambio = element.select("td:nth-child(5)").text();

					Object[] objs = { collection, seccoll, pageurl, title,
							author, page, cambio, down, collUrl };
					String str = "insert into produccionbovina (collection,sertitle,pageurl,title,author,page,cambio,download_url,URL) values (?,?,?,?,?,?,?,?,?)";
					 SQLHelper.updateBySQL(str, objs); // 保存
					System.out.println(pageurl + "........正在抓取");

					//System.out.println(title);
					//System.out.println(author);
					//System.out.println(page);
				//	System.out.println(down);
				//	System.out.println(cambio);
					System.out.println();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void Analy(String collUrl, String collection, String pageurl) {
		try {
			Document document = null;
			Elements elements = null;
			Elements elements1 = null;
			String title = "";
			String tit[] = null;
			String author = "";
			String page = "";
			String down = "";
			String cambio = "";

			try {
				document = getDoc(pageurl);
				elements = document.select(".WordSection1>div[align=center]");
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(elements.size());
			elements.remove(elements.size() - 1);
			System.out.println(elements.size());
			try {
				elements1 = elements.select(".MsoNormalTable tr");
			} catch (Exception e) {
				e.printStackTrace();
			}
			String[] pa = pageurl.split("/");
			String ml = pa[pa.length - 1];

			String url = pageurl.replace(ml, "");
			for (Element element : elements1) {
				try {
					if (!element.text().contains("PDF"))
						continue;
					tit = element.select("td:nth-child(2)").text().split("-");
					title = tit[0];
					author = tit[1];
					page = element.select("td:nth-child(3)").text();
					down = url
							+ element.select("td:nth-child(4) a").attr("href")
									.trim();
					cambio = element.select("td:nth-child(5)").text();

					Object[] objs = { collection, pageurl, title, author, page,
							cambio, down, collUrl };
					String str = "insert into produccionbovina (collection,pageurl,title,author,page,cambio,download_url,URL) values (?,?,?,?,?,?,?,?)";
					 SQLHelper.updateBySQL(str, objs); // 保存
					System.out.println(pageurl + "........正在抓取");

				//	System.out.println(title);
				//	System.out.println(author);
				//	System.out.println(page);
				//	System.out.println(down);
				//	System.out.println(cambio);
					System.out.println();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Document getDoc(String BootURL) { // 获取box
		Document document = null;
		int ii = 10;
		String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:41.0) Gecko/20100101 Firefox/41.0";
		while (document == null && ii != 0) {
			ii--;
			try {
				document = Jsoup.connect(BootURL).userAgent(userAgent)
						.timeout(100000).get();
				// System.out.println(document);
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		return document;
	}

	public static void main(String[] args) {
		Produc p = new Produc();
		String collection = "";
		String collUrl = "";
		String collURL = "";
		String down = "";
		String[] coll = null;
		String co = "";
		String pageurl = "";
		String url = "http://www.produccionbovina.com.ar/portal.htm";
		Document docment = getDoc(url);
		// System.out.println(docment);
		Elements elements = docment
				.select("[class=MsoNormalTable]>tbody>tr:nth-child(4)>td>p");
		System.out.println(elements.size());
		for (Element element : elements) {
			try {
				// System.out.println(element);
				Elements ele = element.select("P>span");
				int i = ele.size();
				if (i == 0)
					continue;
				if (element.select("b>a") == null)
					continue;
				collection = element.select("b a").text();
				collUrl = element.select("b a").attr("href").trim();
				coll = collUrl.split("\\.");
				co = coll[1];
				String str = "htm";
				int idex = 0;
				if ((idex = co.compareTo("pdf")) == 0) {
					down = "http://www.produccionbovina.com.ar/" + collUrl;
					
					Object[] objs = { collection,
							 down, collUrl };
					String str1 = "insert into produccionbovina (collection,download_url,URL) values (?,?,?)";
					 SQLHelper.updateBySQL(str1, objs); // 保存
					System.out.println(collUrl + "........正在抓取");
					
					System.out.println(down);
				} else {
					collURL = "http://www.produccionbovina.com.ar/" + collUrl;
					System.out.println(collURL);

					Document document1 = null;
					Elements elements1 = null;
					try {
						document1 = getDoc(collURL);
					} catch (NullPointerException en) {
						en.printStackTrace();
					}
					elements1 = document1.select(".WordSection1>div");
					int j = elements1.size();
					System.out.println(j);
					String[] pa = collUrl.split("/");
					String pag = pa[0];

					if (j == 1) {

						Elements elements2 = elements1.select(".MsoNormal a");
						System.out.println(elements2.size());
						for (Element element2 : elements2) {
							try {
								String page1 = "http://www.produccionbovina.com.ar/"
										+ element2.select("a").attr("href")
												.replace("../", "");
								String seccoll = element2.select("a").text();
								Document document2 = null;
								try {
									document2 = getDoc(page1);
									if (document2 == null) {

										String page2 = "http://www.produccionbovina.com.ar/"
												+ pag
												+ "/"
												+ element2.select("a")
														.attr("href")
														.replace("../", "");
										Document document3 = null;
										try {
											document3 = getDoc(page2);
										} catch (Exception e) {
											e.printStackTrace();
										}
										Elements elements4 = document3
												.select(".WordSection1");
										if (elements4.size() == 1) {
											pageurl = page2;
											p.Analyze(collURL, collection,
													pageurl, seccoll);

											System.out.println(pageurl);
											System.out.println();
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								Elements elements3 = document2
										.select(".WordSection1");
								if (elements3.size() == 1) {
									pageurl = page1;

									p.Analyze(collURL, collection, pageurl,
											seccoll);

									System.out.println(pageurl);
									System.out.println();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					} else if (j == 2 || j == 3) {

						Elements elements2 = elements1.select(".MsoNormal a");
						System.out.println(elements2.size());
						if (elements2.size() >= 35) {
							pageurl = collURL;

							p.Analy(collURL, collection, pageurl);

						} else {
							for (Element element2 : elements2) {
								try {
									String page = "http://www.produccionbovina.com.ar/"
											+ element2.select("a").attr("href")
													.replace("../", "");
									String seccoll = element2.select("a")
											.text();
									Document document2 = null;
									try {
										document2 = getDoc(page);
										if (document2 == null) {
											String page2 = "http://www.produccionbovina.com.ar/"
													+ pag
													+ "/"
													+ element2.select("a")
															.attr("href")
															.replace("../", "");
											Document document3 = null;
											try {
												document3 = getDoc(page2);
											} catch (Exception e) {
												e.printStackTrace();
											}
											Elements elements4 = document3
													.select(".WordSection1");
											if (elements4.size() == 1) {
												pageurl = page2;

												p.Analyze(collURL, collection,
														pageurl, seccoll);

												System.out.println(pageurl);
												System.out.println();
											}
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
									Elements elements3 = document2
											.select(".WordSection1");
									if (elements3.size() == 1) {
										pageurl = page;

										p.Analyze(collURL, collection, pageurl,
											seccoll);

										System.out.println(page);
										System.out.println();
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					} else {
						pageurl = collURL;
						
						p.Analy(collURL, collection, pageurl);

						System.out.println(pageurl);
					}
				}
				//System.out.println(co);
				System.out.println(collection);
				System.out.println(collUrl);
				System.out.println();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
