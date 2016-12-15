package MyUtils;

import utils.SQLHelper;

public class Catch_Words {

	public static void main(String[] args) {
		String[] str=new String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z" };
		for(int i=0;i<str.length;i++){
			String L=str[i];
			Object[] objs = { L};
			String str1 = "insert into word_ (words) values (?)";
	    SQLHelper.updateBySQL(str1, objs); // 保存
			for(int j=0;j<str.length;j++){
				String LL=str[i]+str[j];
				Object[] obj = { LL};
				String str2 = "insert into word_ (words) values (?)";
		    SQLHelper.updateBySQL(str2, obj); // 保存
				for(int k=0;k<str.length;k++){
					String LLL=str[i]+str[j]+str[k];
					Object[] objs1 = { LLL};
					String str3 = "insert into word_ (words) values (?)";
			    SQLHelper.updateBySQL(str3, objs1); // 保存
				}
			}
		}

	}

}
