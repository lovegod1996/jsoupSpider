package biaozhun;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class test {

	public static void main(String[] args) {

		/*
		 * String str = "2015-06-16"; SimpleDateFormat sdf = new
		 * SimpleDateFormat("MM/dd/yyyy"); Date date= new Date(str);
		 * System.out.println(sdf.format(date));
		 */

		String num = "1434446867";
		Date date = new Date(Long.parseLong(num + "000"));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = df.format(date);
		System.out.println(dateStr);
		if (judgeDate(dateStr))
			System.out.println("成功");

		// System.out.println(ChangeDate("1405308353"));
		
		System.out.println(ChangeDate("1396948600"));
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

	public static String ChangeDate(String num) {
		// 将lang型的时期转化为yyyy-MM-dd型
		Date date = new Date(Long.parseLong(num + "000"));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = df.format(date);
		// System.out.println(dateStr);
		return dateStr;

	}
}
