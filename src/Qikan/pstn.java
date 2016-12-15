package Qikan;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.SQLHelper;

public class pstn {
	public static Document getDoc(String BootURL) { // 获取box
		Document document = null;
		int ii = 10;
		String userAgent = "Mozilla/5.0 (Windows NT 6.1; rv:22.0) Gecko/20100101 Firefox/22.0";
		//System.out.println(BootURL);
		while (document == null && ii != 0) {
			ii--;
			try {
				document = Jsoup.connect(BootURL).userAgent(userAgent)
						.timeout(3000).get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return document;
	}
	public static void main(String[] args) {
		String url="";
		String Btitle="";
		String Did="";
		String data="";
		String Bpaguer="";
		String title="";
		String paguer="";
		String down="";
		String author="";
		String source="";
		String time="";
		String content="";
		String keyword="";
		String page="";
		String Abstract="";
		
		for(int i=1;i<3;i++){
			url="http://www.ptsn.net.cn/article_new/sr_result.php?categories_id=186e5080-64a8-07ff-16f1-44b1be9f1eee&fromDate=&toDate=&selectStr=&page_currentPage="+i;
			Document document1=getDoc(url);
			Elements elements1=document1.select("table tr:nth-child(4) tr");
		//	System.out.println(elements1.size());
			for (Element element : elements1) {
				try{
					if(!element.html().contains("<a"))
						continue;
					Btitle=element.select("td:nth-child(2)").text();
					Bpaguer="http://www.ptsn.net.cn"+element.select("td:nth-child(2) a").attr("href");
					Did=element.select("td:nth-child(3)").text();
					data=element.select("td:nth-child(4)").text();
					Document document2=null;
					Elements elements2=null;
					Document document3=null;
					Elements elements3=null;
					Document document4=null;
					Elements elements4=null;
					try{
						document2=getDoc(Bpaguer);
						elements2=document2.select("table[width=96%] tr");
						//elements3=document2.select("td[valign=top]:nth-child(2)>div[class=css2]:nth-child(2)");
						//System.out.println(elements3);
					}catch(NullPointerException en){
						en.printStackTrace();
					}
					for (Element element2 : elements2) {
						try{
							if(element2.text().contains("r"))
								continue;
							title=element2.select("a").text();
							paguer="http://www.ptsn.net.cn"+element2.select("a").attr("href");
                            
							try{
								document3=getDoc(paguer);
								elements3=document3.select("body>table");
							}catch(NullPointerException en){
								en.printStackTrace();
							}
							System.out.println(elements3.size());
							                  //判断body标签下的个数还分辨不同的网页格式
							if(elements3.size()==9){
								document4=getDoc(paguer);
								elements4=document4.select("body");
								//System.out.println(elements4);
								for (Element element3 : elements4) {
									try{
										if(element3.select("table:nth-child(7) td:nth-child(1)").text().contains("作者:"))
										author=element3.select("table:nth-child(7) td:nth-child(2)").text();
										if(element3.select("table:nth-child(7) td:nth-child(3)").text().contains("来源:"))
										source=element3.select("table:nth-child(7) td:nth-child(4)").text();
										if(element3.select("table:nth-child(7) td:nth-child(5)").text().contains("发布时间:"))
										time=element3.select("table:nth-child(7) td:nth-child(6)").text();
										
										content=element3.select("table").get(7).text();
										down="http://www.ptsn.net.cn"+element3.select("td[width=98%]>a").attr("href");
										
										Object[] objects = { down, author, source, time,
												content, title, paguer, Btitle, Bpaguer, Did,data};
										String str = "insert into pstn (download_url,author,source,time,content,title,paguer,Btitle,Bpaguer,Did,data) values (?,?,?,?,?,?,?,?,?,?,?)";
										//SQLHelper.updateBySQL(str, objects);
										System.out.println(title+ "...........添加完成！");
										System.out.println();
										/*System.out.println(author);
										System.out.println(source);
										System.out.println(time);
										System.out.println(content);
										System.out.println(down);*/
									}catch(Exception e){
										e.printStackTrace();
									}
								}
							}else if(elements3.size()==8){
								document4=getDoc(paguer);
								elements4=document4.select("body");
								//System.out.println(elements4);
								for (Element element3 : elements4) {
									try{
										if(element3.select("table:nth-child(7) td:nth-child(1)").text().contains("作者:"))
										author=element3.select("table:nth-child(7) td:nth-child(2)").text();
										if(element3.select("table:nth-child(7) td:nth-child(1)").text().contains("来源:"))
										source=element3.select("table:nth-child(7) td:nth-child(2)").text();
										if(element3.select("table:nth-child(7) td:nth-child(3)").text().contains("来源:"))
										source=element3.select("table:nth-child(7) td:nth-child(4)").text();
										if(element3.select("table:nth-child(7) td:nth-child(5)").text().contains("发布时间:"))
										time=element3.select("table:nth-child(7) td:nth-child(6)").text();
										if(element3.select("table:nth-child(7) td:nth-child(3)").text().contains("发布时间:"))
											time=element3.select("table:nth-child(7) td:nth-child(4)").text();
										content=element3.select("table").get(7).text();
										
										
										Object[] objects = {  author, source, time,
												content, title, paguer, Btitle, Bpaguer, Did,data};
										String str = "insert into pstn (author,source,time,content,title,paguer,Btitle,Bpaguer,Did,data) values (?,?,?,?,?,?,?,?,?,?)";
										//SQLHelper.updateBySQL(str, objects);
										System.out.println(title + "...........添加完成！");
										System.out.println();
										//down="http://www.ptsn.net.cn"+element3.select("td[width=98%]>a").attr("href");
										//System.out.println(author);
										//System.out.println(source);
										//System.out.println(time);
										//System.out.println(content);
										//System.out.println(down);
									}catch(Exception e){
										e.printStackTrace();
									}
								}
							}else if(elements3.size()==5){
								
								document4=getDoc(paguer);
								elements4=document4.select(".FormTABLE tr");
								System.out.println(elements4.size());
								for (Element element3 : elements4) {
									try{
										if(element3.text().contains("作者"))
										author=element3.select("td").text();
										if(element3.text().contains("来源"))
											source=element3.select("td").text();
										if(element3.text().contains("发布时间"))
											time=element3.select("td").text();
										if(element3.text().contains("关键词"))
											keyword=element3.select("td").text();
										if(element3.text().contains("页数"))
											page=element3.select("td").text();
										if(element3.text().contains("摘要"))
											Abstract=element3.select("td").text();
										if(element3.text().contains("全文下载"))
											down="http://www.ptsn.net.cn"+element3.select("td td[width=98%] a").first().attr("href");
											
										/*System.out.println(author);
										System.out.println(source);
										System.out.println(time);
										System.out.println(content);
										System.out.println(down);*/
									}catch(Exception e){
										e.printStackTrace();
									}
								}
								Object[] objects = { down, author, source, time,
										 title, paguer, Btitle, Bpaguer, Did,data,page,Abstract,keyword};
								String str = "insert into pstn (download_url,author,source,time,title,paguer,Btitle,Bpaguer,Did,data,page,Abstract,keyword) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
								//SQLHelper.updateBySQL(str, objects);
								System.out.println(title + "...........添加完成！");
								System.out.println();
								
								System.out.println(author);
								System.out.println(source);
								System.out.println(time);
								System.out.println(content);
								System.out.println(down);
								System.out.println(keyword);
								System.out.println(page);
								System.out.println(Abstract);
							}
							System.out.println(title);
							System.out.println(paguer);
							System.out.println();
						}catch(Exception e){
							e.printStackTrace();
						}
					}
					System.out.println(Btitle);
					System.out.println(Bpaguer);
					System.out.println(Did);
					System.out.println(data);
					System.out.println();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
}
