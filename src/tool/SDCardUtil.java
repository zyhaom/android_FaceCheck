package tool;

public class SDCardUtil {
	/**
	 * 
	 * @Author:HaoMing(郝明)
	 * @Project_name:newsmanage
	 * @Full_path:cn.itcast.utils.SDCardUtil.java
	 * @Date:@2014 2014-7-31 上午9:55:53
	 * @Return_type:void
	 * @Desc :判断sd卡是否存在
	 */
	public static boolean sdCardExist() {
//		Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
//		Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}

	
	/**
	 * 
	 * @Author:HaoMing(郝明)
	 * @Project_name:newsmanage
	 * @Full_path:cn.itcast.utils.SDCardUtil.java
	 * @Date:@2014 2014-7-31 上午9:56:53
	 * @Return_type:String
	 * @Desc :获取获取sd卡根目录
	 */
	public static String sdDir() {
		return android.os.Environment.getExternalStorageDirectory().toString();
	}
}
