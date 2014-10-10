package com.hl.takepicture;

import tool.ImageService;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class ResultActivity extends Activity {
	
	ImageView imageView1 = null;
	ImageView imageView2 = null;
	ImageView imageView3 = null;
	ImageView imageView4 = null;
	ImageView imageView5 = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_result);
		
		imageView1 = (ImageView)findViewById(R.id.imageView1);
		imageView2 = (ImageView)findViewById(R.id.imageView2);
		imageView3 = (ImageView)findViewById(R.id.imageView3);
		imageView4 = (ImageView)findViewById(R.id.imageView4);
		imageView5 = (ImageView)findViewById(R.id.imageView5);
		
		try {
			// Android 4.0 之后不能在主线程中请求HTTP请求
			//Anexception occured:android.os.NetworkOnMainThreadException
            new Thread(new Runnable(){
                @Override
                public void run() {
                	byte[] data = ImageService.getImage("http://192.168.1.112/wxeg_xml/img/2_1.jpg");
                	Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                	imageView1.setImageBitmap(bitmap);//显示图片
                	imageView2.setImageBitmap(bitmap);//显示图片
                	imageView3.setImageBitmap(bitmap);//显示图片
                	imageView4.setImageBitmap(bitmap);//显示图片
                	imageView5.setImageBitmap(bitmap);//显示图片
                }
            }).start();
            
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
		}
	}

}
