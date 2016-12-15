package DownloadDome;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;







public class DuandianXuchaung {
	
public void actionPerformend(){
	try{
	String 	urlAddress="";
		URL url=new URL(urlAddress);
		HttpURLConnection connection=(HttpURLConnection) url.openConnection();
		connection.connect();
		int totalLength = connection.getContentLength();
		connection.disconnect();
	//	tf_totalLength.setText(String .valueOf(b));
		
	}catch(Exception e){
		e.printStackTrace();
	}
}
	
	public void download(long startPosition,long endPosition){
		try{
			String urlAddress = null;
			URL url=new URL(urlAddress);
			HttpURLConnection connection=(HttpURLConnection) url.openConnection();
			connection.setRequestProperty("User-Agent", "NrtFox");
			String rangProperty="bytes="+startPosition+"-";
			if(endPosition>0){
				rangProperty+=endPosition;
			}
			connection.setRequestProperty("RANGE", rangProperty);
			connection.connect();
			InputStream in=connection.getInputStream();
			String file=url.getFile();
			String name=file.substring(file.lastIndexOf('/')+1);
			FileOutputStream out=new FileOutputStream("D:/Java/Download/"+name,true);
			byte[] buff=new byte[2048];
			int len=0;
			len=in.read(buff);
			while(len!=-1){
				out.write(buff,0,len);
                len=in.read(buff);
			
			}
			out.close();
			in.close();
			connection.disconnect();
			//if(readTopos>0&&readTopos==totalLength){
				System.out.println("下载完成");
				System.exit(0);
			//}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
