package DownloadDome;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.mysql.jdbc.Connection;

class DownMultiThread implements Runnable {
	private static final URLConnection conn = null;
	private String sURL = "";
	private File desFile;
	private long startPose;
	private long endPos;

	/**
 * 
 * 
 * 
 */
	public DownMultiThread(String sUrl, File desFile, long startPos, long endPos) {
		// TODO Auto-generated constructor stub
		this.sURL = sUrl;
		this.desFile = desFile;
		this.startPose = startPos;
		this.endPos = endPos;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			RandomAccessFile out = new RandomAccessFile(desFile, "rw");
			out.seek(startPose);

			//URLConnection conn = null;
			InputStream in = conn.getInputStream(); 
			BufferedInputStream bin = new BufferedInputStream(in);
			byte[] buff = new byte[2048];
			int len = -1;
			len = bin.read(buff);
			while (len != -1) {
				out.write(buff, 0, len);
				len = bin.read(buff);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

public class Download_DXC {

	public static void Download(String url, String dest, int threadNum)
			throws IOException {
		URL downURL = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) downURL.openConnection();
		long fileLength = -1;
		int stateFlagCode = conn.getResponseCode();
		if (stateFlagCode == 200) {
			fileLength = conn.getContentLength();
			conn.disconnect();
		}
		if (fileLength > 0) {
			long byteCounts = fileLength / threadNum + 1;
			File file = new File(dest);
			int i = 0;
			while (i < threadNum) {
				long startPosition = byteCounts * i;
				long endPosition = byteCounts * (i + 1);
				if (i == threadNum - 1) {
					DownMultiThread filetThread = new DownMultiThread(url,
							file, startPosition, 0);
					new Thread(filetThread).start();
				} else {
					DownMultiThread filetThread = new DownMultiThread(url,
							file, startPosition, endPosition);
					new Thread(filetThread).start();
				}
				i++;
			}
			System.out.println("完成网络资源下载！！！！！");
		}

	}

	public static void main(String[] args) {
		String address = "http://academiccommons.columbia.edu/download/fedora_content/download/ac:168503/CONTENT/Hou_columbia_0054D_11772.pdf";
		try {
			Download(address, "D:/Java/Download/", 2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
