package tool;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageService {
	/**
	 * 获取网络图片的数据
	 * @param path 网络图片路径
	 * @return
	 */
	public static byte[] getImage(String path){
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();//基于HTTP协议连接对象
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			if(conn.getResponseCode() == 200){
				InputStream inStream = conn.getInputStream();
				return read(inStream);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 读取流中的数据
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] read(InputStream inStream) throws Exception{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while( (len = inStream.read(buffer)) != -1){
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}
}
