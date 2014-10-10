package com.hl.takepicture;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;
import tool.FileUtil;
import tool.FtpUtil;
import tool.NetUtil;
import tool.SDCardUtil;
import tool.StaticParameter;
import tool.UtilsSendStr;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

public class TakPicActivity extends Activity {
	private Camera camera;
	FileUtil fileUtil;
	private static String commFilePath = "";
	private ProgressBar pb;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
		setContentView(R.layout.layout_takepic);
		
		pb = (ProgressBar) this.findViewById(R.id.wait_bar);
		pb.setVisibility(View.GONE);
		SurfaceView surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);
		surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceView.getHolder().setFixedSize(176, 144);
		surfaceView.getHolder().setKeepScreenOn(true);
		surfaceView.getHolder().addCallback(new SurfaceCallback());
		fileUtil = new FileUtil(this.getBaseContext());
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
//		System.out.println("onStart");
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
//		System.out.println("onRestart");
		pb = (ProgressBar) this.findViewById(R.id.wait_bar);
		pb.setVisibility(View.GONE);
	}

	/**
	 * 
	 * @author 回调方法类
	 * 
	 */
	private final class SurfaceCallback implements Callback {
		public void surfaceCreated(SurfaceHolder holder) {
			try {
				camera = Camera.open();// 打开摄像头
				Camera.Parameters parameters = camera.getParameters();
				// System.out.println(parameters.flatten());//可列出摄像机参数
				parameters.setPictureFormat(PixelFormat.JPEG);// 设置照片的格式
				parameters.setPreviewSize(800, 480);
				parameters.setPreviewFrameRate(5);
//				parameters.setPictureSize(1024, 768);
				parameters.setPictureSize(640, 480);
				parameters.setJpegQuality(80);
//				camera.setDisplayOrientation(270);
				camera.setParameters(parameters);// 设置摄像头的参数.否则前面的设置无效
				camera.setPreviewDisplay(holder);
				camera.startPreview();// 开始预览
				camera.autoFocus(null);// 初始化对焦
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			// android:screenOrientation="sensor"
			// System.out.println("surfaceChanged turn le ");
			if (camera != null) {
				
//				System.out.println(getWindowManager().getDefaultDisplay().getRotation());
//				System.out.println(Surface.ROTATION_270);
//				int rotation = getWindowManager().getDefaultDisplay().getRotation();
//				int degrees = 0;
//				switch (rotation) {
//				case Surface.ROTATION_0:
//					degrees = 0;
//					break;
//				case Surface.ROTATION_90:
//					degrees = 90;
//					break;
//				case Surface.ROTATION_180:
//					degrees = 180;
//					break;
//				case Surface.ROTATION_270:
//					degrees = 270;
//					break;
//				}
//				
//				camera.setDisplayOrientation(degrees);
			}
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			if (camera != null) {
				camera.release();
				camera = null;
			}
		}
	}

	/**
	 * 
	 * @Author:HaoMing(郝明)
	 * @Project_name:takepicture
	 * @Full_path:com.hl.takepicture.EntranceActivity.java
	 * @Date:@2014 2014-7-28 上午11:15:48
	 * @Return_type:void
	 * @Desc : 拍照相关的按钮事件
	 */
	public void takepicture(View view) {
		switch (view.getId()) {
		case R.id.take_pic:
			camera.takePicture(null, null, new MyPictureCallback());
			break;
		case R.id.cancle_pic:
			// 1.删除文件
			System.out.println("delFile : " + fileUtil.delFile(commFilePath));
			commFilePath = "";
			// 2.使界面可继续拍照
			camera.startPreview();
			break;
		case R.id.send_pic:
			// Android 4.0 之后不能在主线程中请求HTTP请求
			// Anexception occured:android.os.NetworkOnMainThreadException
			/*
			 * new Thread(new Runnable(){
			 * 
			 * @Override public void run() { // 1.发送文件给服务器 UploadImage.upload(new File(commFilePath),"http://192.168.1.112:8088/html5/UploadFileServlet");
			 * 
			 * //2.删除文件 fileUtil.delFile(commFilePath); commFilePath=""; // 3.显示结果界面 Intent intent = new Intent(); intent.setClass(TakPicActivity.this, ResultAdapterActivity.class);
			 * startActivity(intent); } }).start();
			 */
			if (NetUtil.isNetworkAvailable(getApplicationContext())) {// 网络通畅
				
				pb.setVisibility(View.VISIBLE);
					 
				
				// 1.发送文件给服务器
				// FtpUtil.ftpUpload("125.76.161.141", "21", "weixin", "weixin", "FileFromAndroid", commFilePath);
				// FtpUtil.ftpUpload("192.168.1.112", "21", "weixin", "weixin", "FileFromAndroid", "/mnt/sdcard-ext/", commFilePath);
				FtpUtil.ftpUpload(StaticParameter.FTP_IP, StaticParameter.FTP_PORT, StaticParameter.FTP_USERName, StaticParameter.FTP_USERPWD, StaticParameter.FTP_PIC_PATH, commFilePath);
				// 2. 请求服务器执行人脸识别操作。接收返回结果。
				String XML_URL="";
				try {
//					XML_URL = UtilsSendStr.httpPost("http://192.168.1.112:8088/html5/servletTest","UTF-8",null,new File(commFilePath).getName());
					XML_URL = UtilsSendStr.httpPost("http://"+StaticParameter.WEB_IP+":"+StaticParameter.WEB_PORT+"/"+StaticParameter.APP_NAME+"/FaceCheck","UTF-8",null,new File(commFilePath).getName());
//					System.out.println("XML_URL : "+XML_URL);
				} catch (Exception e) {
				}
				
				// 3.删除文件
				fileUtil.delFile(commFilePath);
				commFilePath = "";
				// 4.显示结果界面
				if("".equals(XML_URL)){
					Toast.makeText(TakPicActivity.this, R.string.no_return_result, Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}
				Intent intent = new Intent();
				intent.putExtra("XML_URL", XML_URL);
				intent.putExtra("ftp_dest_path", "face_check/1/");
				
				intent.setClass(TakPicActivity.this, ResultAdapterActivity.class);
				startActivity(intent);
				//pb.setVisibility(View.GONE);
			} else {
				Toast.makeText(TakPicActivity.this, R.string.net_fail, Toast.LENGTH_LONG).show();
				return;
			}
			break;
		default:
			break;
		}
	}

	@SuppressLint("SdCardPath")
	private final class MyPictureCallback implements PictureCallback {
		public void onPictureTaken(byte[] data, Camera camera) {
			try {
				// System.out.println("path : " + Environment.getExternalStorageDirectory().toString());
				if (SDCardUtil.sdCardExist()) {// 有sd卡的情况
					// File jpgFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis()+".jpg");
					File jpgFile = new File(Environment.getExternalStorageDirectory(), UUID.randomUUID() + ".jpg");
					FileOutputStream outStream = new FileOutputStream(jpgFile);
					outStream.write(data);
					outStream.close();
					commFilePath = jpgFile.getPath();
				} else {
					// jpgFile = new File("/mnt/sdcard-ext", System.currentTimeMillis() + ".jpg");
					// jpgFile = new File("/mnt/sdcard-ext", UUID.randomUUID() + ".jpg");
					String fileName = UUID.randomUUID() + ".jpg";
					FileUtil.save(getApplicationContext(), data, fileName);
					commFilePath = "/data/data/com.hl.takepicture/files/" + fileName;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @Author:HaoMing(郝明)
	 * @Project_name:takepicture
	 * @Full_path:com.hl.takepicture.EntranceActivity.java
	 * @Date:@2014 2014-7-28 上午11:15:48
	 * @Return_type:void
	 * @Desc : 单击屏幕可对焦
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			/*
			 * System.out.println("您单击了屏幕");
			 * 
			 * if (SDCardUtil.sdCardExist()) { System.out.println("存在sd卡，根目录是： " + SDCardUtil.sdDir()); }else{ System.out.println("no 存在sd卡 "); }
			 * 
			 * if (NetUtil.isNetworkAvailable(getApplicationContext())) { System.out.println("网络连接 ok"); }
			 * 
			 * if (NetUtil.isWifi(getApplicationContext())) { System.out.println("判断是wifi"); } else { System.out.println("判断是3g"); } if (NetUtil.isWifiEnabled(getApplicationContext())) {
			 * System.out.println("WIFI had 打开"); }
			 * 
			 * if (NetUtil.is3rd(getApplicationContext())) { System.out.println("has 3G网络"); }else{ System.out.println("no 3G网络"); } if (NetUtil.isGpsEnabled(getApplicationContext())) {
			 * System.out.println("GPS had 打开"); }
			 */
			camera.autoFocus(null);
			return true;
		}
		return super.onTouchEvent(event);
	}
}
