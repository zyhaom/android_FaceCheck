package com.hl.takepicture;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class EntranceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.layout_entrance);
		
		
	}
	
	/**
	 * 
	 * @Author:HaoMing(郝明)
	 * @Project_name:takepicture
	 * @Full_path:com.hl.takepicture.EntranceActivity.java
	 * @Date:@2014 2014-7-28 上午11:15:48
	 * @Return_type:void
	 * @Desc : 进入拍照界面
	 */
	public void entrance(View view){
		switch (view.getId()) {
		case R.id.btn_entrance:
			// 页面切换
			Intent intent = new Intent();
			intent.setClass(EntranceActivity.this, TakPicActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
}
