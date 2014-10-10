package tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Environment;

public class FileUtil {

	public static final String TAG = "FileUtil";
	
	private Context context;
	
	public FileUtil(Context context) {
		this.context = context;
	}

	/**
	 * 
	 * @Author:HaoMing(郝明)
	 * @Project_name:安卓存储
	 * @Full_path:com.example.service.FileUtil.java
	 * @Date:@2014 2014-7-14 上午10:29:18
	 * @Return_type:void
	 * @Desc : 将指定文件名的文件保存在 ： data/data/<package name>/files/ 下 
	 */
	public void save(String filename, String content) throws Exception {
//		私有操作模式：创建出来的文件只能被本应用访问，其它应用无法访问该文件，另外采用私有操作模式创建的文件，写入文件中的内容会覆盖原文件的内容
//		Context.MODE_APPEND
//		Context.MODE_WORLD_READABLE
//		Context.MODE_WORLD_WRITEABLE
//		Context.MODE_PRIVATE
		FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
		outStream.write(content.getBytes());
//		System.out.println(context.getFilesDir());
//		System.out.println(context.getCacheDir());
//		System.out.println(context.getPackageName());
//		System.out.println(context.getDatabasePath("1.txt"));
		outStream.close();
	}
	
	/**
	 * 
	 * @Author:HaoMing(郝明)
	 * @Project_name:人脸识别
	 * @Full_path:tool.FileUtil.java
	 * @Date:@2014 2014-8-4 下午3:43:22
	 * @Return_type:void
	 * @Desc :重载方法 -- 将指定文件名的文件保存在 ： data/data/<package name>/files/ 下 
	 */
	public static void save(Context context, byte[] data,String filename) throws Exception {
		FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
		outStream.write(data);
		outStream.close();
	}
	
	public void save(String filename, byte[] data) throws Exception {
//		私有操作模式：创建出来的文件只能被本应用访问，其它应用无法访问该文件，另外采用私有操作模式创建的文件，写入文件中的内容会覆盖原文件的内容
//		Context.MODE_APPEND
//		Context.MODE_WORLD_READABLE
//		Context.MODE_WORLD_WRITEABLE
//		Context.MODE_PRIVATE
		FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
		outStream.write(data);
		outStream.close();
	}
	
	/**********************************************/
	
	/**
	 * 
	 * @Author:HaoMing(郝明)
	 * @Project_name:安卓存储
	 * @Full_path:com.example.service.FileUtil.java
	 * @Date:@2014 2014-7-7 下午1:33:12
	 * @Return_type:void
	 * @Desc : 按行读取指定路径的文件。
	 */
	public List<String> readFile(String fullPath) throws Exception{
//		String fullPath = Environment.getExternalStorageDirectory()+"/haom/save0.txt";
		List<String> list = new ArrayList<String>();
		File file = new File(fullPath);
		if(!file.exists()){
			return null;
		}
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		String s = br.readLine();
		while(s!=null){
			list.add(s);
//			System.out.println(s);
			s = br.readLine();
		}
		br.close();
		return list;
	}
	
	
	/**
	 * 
	 * @Author:HaoMing(郝明)
	 * @Project_name:安卓存储
	 * @Full_path:com.example.service.FileUtil.java
	 * @Date:@2014 2014-7-7 上午10:32:08
	 * @Return_type:void
	 * @Desc : 将内容保存至SDCard中指定的目录中
	 */
	public void saveToSDCard(String fileName, String content) throws Exception{
//		Log.i(TAG, Environment.getExternalStorageDirectory().toString()); // /mnt/sdcard
		String fullPath = Environment.getExternalStorageDirectory()+"/haom";
		File file = new File(fullPath);
		if(!file.exists()){
			file.mkdirs();
		}
		file = new File(fullPath,fileName);
		FileOutputStream out = new FileOutputStream(file);
		out.write(content.getBytes());
		out.close();
	}
	
	/**
	 * 
	 * @Author:HaoMing(郝明)
	 * @Project_name:安卓存储
	 * @Full_path:com.example.service.FileUtil.java
	 * @Date:@2014 2014-7-7 上午11:35:14
	 * @Return_type:void
	 * @Desc :删除给定的路径文件或目录
	 */
	public boolean delFile(String fullPath){
		File file = new File(fullPath);
//		file.setExecutable(true,false);
//		file.setExecutable(true);
		if(file.exists()){
			return file.delete();
		}
		return false;
	}
	
	/**
	 * 
	 * @Author:HaoMing(郝明)
	 * @Project_name:安卓存储
	 * @Full_path:com.example.service.FileUtil.java
	 * @Date:@2014 2014-7-7 下午12:14:39
	 * @Return_type:void
	 * @Desc : 删除指定路径下的所有内容
	 */
	public void delAllByDir(String dirPath){
		File path = new File(dirPath);
		if(path.isDirectory()){
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				File temp = files[i];
				if(temp.isFile()){
					delFile(files[i].toString());
				}else{
					delAllByDir(files[i].toString());
				}
			}
			path.delete();
		}
	}
	
	/**
	 * 
	 * @Author:HaoMing(郝明)
	 * @Project_name:安卓存储
	 * @Full_path:com.example.service.FileUtil.java
	 * @Date:@2014 2014-7-7 下午12:10:25
	 * @Return_type:boolean
	 * @Desc :判断全路径下的文件或目录是否存在
	 */
	public boolean existsFile(String fullPath){
		File file = new File(fullPath);
		return file.exists();
	}
	
	
	
}
