package DownloadDome;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


class Ticket implements Runnable//extends Thread
{
	private  int tick = 100;
Object obj=new Object();
	public void run() {
		while (true) {
			synchronized (obj) 
			{
			if (tick > 0) {
				try{
					Thread.sleep(10);
				}catch(Exception e){}
			       String urlAddr="http://dl05.80s.im:920/1506/[何炅]栀子花开/[何炅]栀子花开_bd.mp4";
				try{
					URL url=new URL(urlAddr);                        //创建URL对象
					URLConnection urlConn=url.openConnection();   //获得链接对象
					urlConn.connect();                           //打开到url引用资源的通信连接
					InputStream in=urlConn.getInputStream();         //获得输入流对象
					String filePath=url.getFile();                //获得完整路径
					int pos=filePath.lastIndexOf("/");             //获得路径中最后一个斜杠的位置
					String fileName=filePath.substring(pos+1);          //截取文件名
					FileOutputStream out=new FileOutputStream("D:/Java/Download/"+fileName);   //创建输出流对象
					byte[] bytes=new byte[2048];            //声明存放下载内容的字节数组
					int len=in.read(bytes);                 //从输入流中读取内容
					System.out.println("开始下载.......");
					while(len!=-1){
						out.write(bytes, 0, len);          //将读取的内容写到输出流
						len=in.read(bytes);             //继续从输入流中读取内容
						//System.out.println("正在下载........请稍等.......");
					}
					out.close();
					in.close();
					System.out.println("下载完毕！！");
				}catch(Exception e){
					e.printStackTrace();
				}	
			}
			}
		}
	}
}

public class DXC {

	public static void main(String[] args) {
		Ticket t=new Ticket();
for(int i=0;i<20;i++){
	Thread t1 =new Thread(t);//创建了一个线程
	t1.start();
}
	}

}
