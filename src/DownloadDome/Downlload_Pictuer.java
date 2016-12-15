package DownloadDome;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Downlload_Pictuer {
public static void download(String urlAddr){               //从指定网址下载文件
	try{
		URL url=new URL(urlAddr);                        //创建URL对象
		URLConnection urlConn=url.openConnection();   //获得链接对象
		urlConn.connect();                           //打开到url引用资源的通信连接
		InputStream in=urlConn.getInputStream();         //获得输入流对象
		String filePath=url.getFile();                //获得完整路径
		int pos=filePath.lastIndexOf("/");             //获得路径中最后一个斜杠的位置
		String fileName=filePath.substring(pos+1);          //截取文件名
		System.out.println(fileName);
		FileOutputStream out=new FileOutputStream("D:/Java/Download/"+fileName);   //创建输出流对象
		byte[] bytes=new byte[1024];            //声明存放下载内容的字节数组
		int len=in.read(bytes);                 //从输入流中读取内容
		System.out.println("开始下载.......");
		while(len!=-1){
			out.write(bytes, 0, len);          //将读取的内容写到输出流
			len=in.read(bytes);             //继续从输入流中读取内容
			System.out.println(bytes);
			System.out.println(len);
		}
		out.close();
		in.close();
		System.out.println("下载完毕！！");
	}catch(Exception e){
		e.printStackTrace();
	}
}
	
	
	
	
	
	public static void main(String[] args) {
		String address="http://dl05.80s.im:920/1506/%5B%E4%BD%95%E7%82%85%5D%E6%A0%80%E5%AD%90%E8%8A%B1%E5%BC%80/%5B%E4%BD%95%E7%82%85%5D%E6%A0%80%E5%AD%90%E8%8A%B1%E5%BC%80_bd.mp4";
		download(address);
	}

}
