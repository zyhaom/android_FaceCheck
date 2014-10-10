package tool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUtil {
	private static FTPClient ftpClient = new FTPClient();

	/**
	 * 
	 * @Author:HaoMing(郝明)
	 * @Project_name:人脸识别
	 * @Full_path:tool.FtpUtil.java
	 * @Date:@2014 2014-8-18 下午5:18:21
	 * @Return_type:void
	 * @Desc :连接ftp的方法
	 */
	private static void connectFtp(String ip, String port, String username, String password) {
		try {
			if (!ftpClient.isConnected()) {
				ftpClient.connect(ip, Integer.parseInt(port));
				ftpClient.login(username, password);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 
	 * @Author:HaoMing(郝明)
	 * @Project_name:人脸识别
	 * @Full_path:tool.FtpUtil.java
	 * @Date:@2014 2014-8-18 下午5:18:11
	 * @Return_type:void
	 * @Desc :断开ftp的方法
	 */
	public static void disConnctFtp() {
		try {
			if (ftpClient != null && ftpClient.isConnected()) {
				ftpClient.logout();
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Description: 从FTP服务器下载文件
	 * 
	 * @Version1.0
	 * @param ip
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param remotePath
	 *            FTP服务器上的相对路径
	 * @param fileName
	 *            要下载的文件名
	 * @param localPath
	 *            下载后保存到本地的路径
	 * @return
	 */
	public static byte[] getFileByte(String ip, String port, String username, String password, String remotePath, String fileName, String localPath) {
		byte[] b = null;
		try {
			int reply;
			FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
			conf.setServerLanguageCode("zh");
			// ftpClient.connect(ip, Integer.parseInt(port));
			// 如果采用默认端口，可以使用ftp.connect(ip)的方式直接连接FTP服务器
			// ftpClient.login(username, password);// 登录
			connectFtp(ip, port, username, password);
			// 设置文件传输类型为二进制
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			// 获取ftp登录应答代码
			reply = ftpClient.getReplyCode();
			// 验证是否登陆成功
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				System.err.println("FTP server refused connection.");
				return null;
			}
			// 转移到FTP服务器目录至指定的目录下
			// ftpClient.changeWorkingDirectory(new String(remotePath.getBytes(encoding), "iso-8859-1"));
			ftpClient.changeWorkingDirectory(remotePath);
			ftpClient.setBufferSize(1024);
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			// 获取文件列表
			FTPFile[] fs = ftpClient.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					// File localFile = new File(localPath + "/" + ff.getName());
					// OutputStream is = new FileOutputStream(localFile);
					// ftpClient.retrieveFile(ff.getName(), is);
					// is.close();
					InputStream ins = ftpClient.retrieveFileStream(ff.getName());
					b = toByteArray(ins);
					break;
				}
			}
			// ftpClient.logout();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return b;
	}

	/**
	 * 
	 * @Author:HaoMing(郝明)
	 * @Project_name:人脸识别
	 * @Full_path:tool.FtpUtil.java
	 * @Date:@2014 2014-8-18 下午2:51:49
	 * @Return_type:byte[]
	 * @Desc :inputstream转byte数组
	 */
	private static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
		return output.toByteArray();
	}

	/**
	 * 通过ftp上传文件
	 * 
	 * @param url
	 *            ftp服务器地址 如： 192.168.1.112
	 * @param port
	 *            端口如 ： 21
	 * @param username
	 *            登录名
	 * @param password
	 *            密码
	 * @param remotePath
	 *            上到ftp服务器的磁盘路径
	 * @param fileNamePath
	 *            要上传的文件路径
	 * @param fileName
	 *            要上传的文件名
	 * @return
	 */
	public static String ftpUpload(String url, String port, String username, String password, String remotePath, String fileNamePath, String fileName) {
		FTPClient ftpClient = new FTPClient();
		FileInputStream fis = null;
		String returnMessage = "0";
		try {
			ftpClient.connect(url, Integer.parseInt(port));
			boolean loginResult = ftpClient.login(username, password);
			int returnCode = ftpClient.getReplyCode();
			if (loginResult && FTPReply.isPositiveCompletion(returnCode)) {// 如果登录成功
				ftpClient.makeDirectory(remotePath);
				// 设置上传目录
				ftpClient.changeWorkingDirectory(remotePath);
				ftpClient.setBufferSize(1024);
				ftpClient.setControlEncoding("UTF-8");
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				ftpClient.enterLocalPassiveMode();
				fis = new FileInputStream(fileNamePath + fileName);
				ftpClient.storeFile(fileName, fis);
				returnMessage = "1"; // 上传成功
			} else {// 如果登录失败
				returnMessage = "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("FTP客户端出错！", e);
		} finally {
			// IOUtils.closeQuietly(fis);
			try {
				ftpClient.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("关闭FTP连接发生异常！", e);
			}
		}
		return returnMessage;
	}

	/**
	 * 
	 * @Author:HaoMing(郝明)
	 * @Project_name:人脸识别
	 * @Full_path:tool.FtpUtil.java
	 * @Date:@2014 2014-8-4 上午10:32:53
	 * @Return_type:String
	 * @Desc : 重载的方法:将 fileNamePath和 fileName 合并成一个参数
	 */
	public static String ftpUpload(String url, String port, String username, String password, String remotePath, String fileFullPath) {
		FTPClient ftpClient = new FTPClient();
		FileInputStream fis = null;
		String returnMessage = "0";
		try {
			ftpClient.connect(url, Integer.parseInt(port));
			boolean loginResult = ftpClient.login(username, password);
			int returnCode = ftpClient.getReplyCode();
			if (loginResult && FTPReply.isPositiveCompletion(returnCode)) {// 如果登录成功
				ftpClient.makeDirectory(remotePath);
				// 设置上传目录
				ftpClient.changeWorkingDirectory(remotePath);
				ftpClient.setBufferSize(1024);
				ftpClient.setControlEncoding("UTF-8");
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				ftpClient.enterLocalPassiveMode();
				fis = new FileInputStream(fileFullPath);
				ftpClient.storeFile(new File(fileFullPath).getName(), fis);
				returnMessage = "1"; // 上传成功
			} else {// 如果登录失败
				returnMessage = "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("FTP客户端出错！", e);
		} finally {
			// IOUtils.closeQuietly(fis);
			try {
				ftpClient.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("关闭FTP连接发生异常！", e);
			}
		}
		return returnMessage;
	}

	public static void main(String[] args) {
		// ftpUpload("192.168.1.112", "21", "weixin", "weixin", "FileFromAndroid", "D:/html5/", "android.jpg");
		// System.out.println(123);
	}
}
