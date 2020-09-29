
package com.jiantaokeji.answerliu.mvvm_core.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 辅助工具类
 * @创建时间： 2015年11月24日 上午11:46:50
 * @项目名称： AMapLocationDemo2.x
 * @author hongming.wang
 * @文件名称: Utils.java
 * @类型名称: Utils
 */
public class Utils {

	/**
	 * UUID
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().toUpperCase();
	}

	private static SimpleDateFormat sdf = null;
	public  static String formatUTC(long l, String strPattern) {
		if (TextUtils.isEmpty(strPattern)) {
			strPattern = "yyyy-MM-dd HH:mm:ss";
		}
		if (sdf == null) {
			try {
				sdf = new SimpleDateFormat(strPattern, Locale.CHINA);
			} catch (Throwable e) {
			}
		} else {
			sdf.applyPattern(strPattern);
		}
		return sdf == null ? "NULL" : sdf.format(l);
	}

	/**
	 * 获取app的名称
	 * @param context
	 * @return
	 */
	public static String getAppName(Context context) {
		String appName = "";
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			appName =  context.getResources().getString(labelRes);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return appName;
	}


	/**
	 * 正则：手机号（精确）
	 * 移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、172、178、182、183、184、187、188、198
	 * 联通：130、131、132、145、146、148、155、156、175、176、185、186
	 * 电信：133、153、166、173、177、180、181、189、199
	 * 全球星：1349
	 * 虚拟运营商：170、171
	 * 电信和工信部的卫星通信号段：1740、1740
	 * 以下未匹配
	 * 十三位移动物联网：144，十三位电信物联网：141
	 * 船舶通信导航公司客服电话：10098
	 */
	public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5-8])|(15[0-3,5-9])|(16[6])|(17[0-8])|(18[0-9])|(19[8,9]))\\d{8}$";
	/**
	 * 正则：身份证号码18位
	 */
	public static final String REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";


	/**
	 * 验证手机号（精确）
	 *
	 * @param phone 待验证的手机号
	 * @return {@code true}: 匹配<br>{@code false}: 不匹配
	 */
	public static boolean isMobileExact(@NonNull final CharSequence phone) {
		return isMatch(REGEX_MOBILE_EXACT, phone);
	}

	/**
	 * 验证身份证号码18位
	 *
	 * @param input 待验证文本
	 * @return {@code true}: 匹配<br>{@code false}: 不匹配
	 */
	public static boolean isIDCard18(@NonNull final CharSequence input) {
		// 对身份证号进行简单判断
		if (!isMatch(REGEX_ID_CARD18, input)) {
			return false;
		}
		// 1-17位相乘因子数组
		int[] factor = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
		// 18位随机码数组
		char[] random = "10X98765432".toCharArray();
		// 计算1-17位与相应因子乘积之和
		int total = 0;
		for (int i = 0; i < 17; i++) {
			total += Character.getNumericValue(input.charAt(i)) * factor[i];
		}
		// 判断随机码是否相等
		return random[total % 11] == input.charAt(17);
	}

	/**
	 * 判断是否匹配正则
	 *
	 * @param regex 正则表达式
	 * @param input 要匹配的字符串
	 * @return {@code true}: 匹配<br>{@code false}: 不匹配
	 */
	public static boolean isMatch(@NonNull final String regex, @NonNull final CharSequence input) {
		return !isEmpty(input) && Pattern.matches(regex, input);
	}

	/**
	 * 判断字符串是否为null或长度为0
	 *
	 * @param input 待校验字符串
	 * @return {@code true}: 空<br> {@code false}: 不为空
	 */
	public static boolean isEmpty(CharSequence input) {
		return input == null || input.length() == 0;
	}


	public static <T> T getMapValue(HashMap<String, T> map, String key){
		T t=null;
		if (map!=null){
			t = map.get(key);
		}
		return t;
	}



}
