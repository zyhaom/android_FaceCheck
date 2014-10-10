package com.hl.takepicture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import modle.ResultVO;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tool.FileUtil;
import tool.FtpUtil;
import tool.ImageService;
import tool.StaticParameter;
import tool.XMLParser;
import tool.listView.LazyAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ResultAdapterActivity extends Activity {
	
	public static List<String> ftpUrlLocalPathList = new ArrayList<String>();
	ImageView imageView = null;
	private ListView listView;
	String ftp_dest_path = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_result_adapter);
		
		listView = (ListView) this.findViewById(R.id.result_listView);
		listView.setOnItemClickListener(new ItemClickListener());
		
		Intent intent = getIntent();
		String XML_URL = intent.getStringExtra("XML_URL");
		ftp_dest_path = intent.getStringExtra("ftp_dest_path");
//		ftp_dest_path = "face_check/1/";
		
//		show();//通过读物服务器的xml文件的url获得结果
		showFromXMLContent(XML_URL);//通过读物服务器的xml文件的内容获得结果
//		showFromXML("http://192.168.1.112:8088/html5/result.xml");
		
		//http获得图片
		/*imageView = (ImageView)findViewById(R.id.imageView);
		try {
			// Android 4.0 之后不能在主线程中请求HTTP请求
			//Anexception occured:android.os.NetworkOnMainThreadException
            new Thread(new Runnable(){
                @Override
                public void run() {
                	byte[] data = ImageService.getImage("http://www.baidu.com/img/baidu_jgylogo3.gif?v=08089697.gif");
                	Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                	imageView.setImageBitmap(bitmap);//显示图片
                }
            }).start();
            
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
		}*/
		
		//ftp获得图片
		/*imageView = (ImageView)findViewById(R.id.imageView);
		try {
			// Android 4.0 之后不能在主线程中请求HTTP请求
			//Anexception occured:android.os.NetworkOnMainThreadException
            new Thread(new Runnable(){
                @Override
                public void run() {
                	byte[] data = ImageService.getImage("ftp://192.168.1.112/face_check/1/57.jpg");
                	Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                	imageView.setImageBitmap(bitmap);//显示图片
                }
            }).start();
            
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
		}*/
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
//		System.out.println("ResultAdapterActivity	onPause");
		
//		删除从ftp下载的图片
		FileUtil fileUtil = new FileUtil(this.getBaseContext());
		for (String ftpUrlLocalPath:ftpUrlLocalPathList) {
			fileUtil.delFile(ftpUrlLocalPath);
		}
		
		FtpUtil.disConnctFtp();//关闭FTP
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	public void returnPhoto(View view){
		Intent intent = new Intent();
		intent.setClass(ResultAdapterActivity.this, TakPicActivity.class);
		startActivity(intent);
		FtpUtil.disConnctFtp();//关闭FTP
	}
	
	/**
	 * 
	 * @Author:HaoMing(郝明)
	 * @Project_name:人脸识别
	 * @Full_path:com.hl.takepicture.ResultAdapterActivity.java
	 * @Date:@2014 2014-8-18 下午3:26:34
	 * @Return_type:void
	 * @Desc :通过读物服务器的xml文件的内容获得结果
	 */
	private void showFromXMLContent(String s){
		List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();
		
		XMLParser parser = new XMLParser();
		String xml = "";
		try {
			xml = new String(s.getBytes(), "UTF-8");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Document doc = parser.getDomElement(xml); // 获取 DOM 节点
		NodeList result = doc.getElementsByTagName("element");
		
		ftpUrlLocalPathList.clear();
		for (int i = 0; i < result.getLength(); i++) {
			// 新建一个 HashMap
			HashMap<String, Object> map = new HashMap<String, Object>();
			Element e = (Element) result.item(i);
			// 每个子节点添加到HashMap 键 => 值
			map.put("picUrl", parser.getValue(e, "picUrl"));
			map.put("semblance", parser.getValue(e, "semblance"));
			map.put("name", parser.getValue(e, "name"));
			map.put("addr", parser.getValue(e, "addr"));
			map.put("linkman", parser.getValue(e, "linkman"));
			map.put("tel", parser.getValue(e, "tel"));

			//根据picUrl将图片通过ftp下载到客户端，供显示页面使用。
			try {
				String url = parser.getValue(e, "picUrl");
				String fileName = UUID.randomUUID() + ".jpg";
				byte[] data = FtpUtil.getFileByte(StaticParameter.FTP_IP, StaticParameter.FTP_PORT,
						StaticParameter.FTP_USERName,StaticParameter.FTP_USERPWD,
						ftp_dest_path,url.substring(url.lastIndexOf("/")+1),null);
				FileUtil.save(getApplicationContext(), data, fileName);
				ftpUrlLocalPathList.add("/data/data/com.hl.takepicture/files/" + fileName);
				map.put("picUrl", "/data/data/com.hl.takepicture/files/" + fileName);
			} catch (Exception e2) {}
			
			// HashList添加到数组列表
			resultList.add(map);
			
		}
		FtpUtil.disConnctFtp();//关闭FTP

		LazyAdapter adapter = new LazyAdapter(ResultAdapterActivity.this, resultList,ftp_dest_path);

		listView.setAdapter(adapter);
	}
	
	/**
	 * 
	 * @listView 中 每个item点击的事件类
	 *
	 */
	private final class ItemClickListener implements OnItemClickListener {
		@SuppressWarnings("unused")
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			//System.out.println(position);
			if(true){
				TextView tel = (TextView) view.findViewById(R.id.result_mould_tel);
				TextView linkman = (TextView) view.findViewById(R.id.result_mould_linkman);
				TextView name = (TextView) view.findViewById(R.id.result_mould_name);
				TextView semblance = (TextView) view.findViewById(R.id.result_mould_semblance);
				TextView addr = (TextView) view.findViewById(R.id.result_mould_addr);
				Toast.makeText(getApplicationContext(),
								"相似度："+semblance.getText() + "\n" 
								+ "姓名："+name.getText() + "\n" 
								+ "地址："+addr.getText() + "\n"
								+ "联系人："+linkman.getText() + "\n"
								+ "联系电话："+tel.getText(),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 
	 * @Author:HaoMing(郝明)
	 * @Project_name:人脸识别
	 * @Full_path:com.hl.takepicture.ResultAdapterActivity.java
	 * @Date:@2014 2014-8-6 上午9:54:20
	 * @Return_type:void
	 * @Desc :显示结果列表的重载方法 内容来源是url得到的xml文件
	 */
	private void showFromXML(String XML_URL){
		List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();
		
		XMLParser parser = new XMLParser();
		String xml = parser.getXmlFromUrl(XML_URL); // 从网络获取xml
		try {
			xml = new String(xml.getBytes(), "UTF-8");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Document doc = parser.getDomElement(xml); // 获取 DOM 节点
		NodeList result = doc.getElementsByTagName("element");
		
		for (int i = 0; i < result.getLength(); i++) {
			// 新建一个 HashMap
			HashMap<String, Object> map = new HashMap<String, Object>();
			Element e = (Element) result.item(i);
			// 每个子节点添加到HashMap 键 => 值
			map.put("picUrl", parser.getValue(e, "picUrl"));
			map.put("semblance", parser.getValue(e, "semblance"));
			map.put("name", parser.getValue(e, "name"));
			map.put("addr", parser.getValue(e, "addr"));
			map.put("linkman", parser.getValue(e, "linkman"));
			map.put("tel", parser.getValue(e, "tel"));

			// HashList添加到数组列表
			resultList.add(map);
		}

		
		LazyAdapter adapter = new LazyAdapter(ResultAdapterActivity.this, resultList,"");

		listView.setAdapter(adapter);
	}

	/**
	 * 
	 * @Author:HaoMing(郝明)
	 * @Project_name:人脸识别
	 * @Full_path:com.hl.takepicture.ResultAdapterActivity.java
	 * @Date:@2014 2014-8-6 上午9:54:04
	 * @Return_type:void
	 * @Desc :显示结果列表的测试方法
	 */
	private void show() {
		List<ResultVO> results = new ArrayList<ResultVO>();
		results.add(new ResultVO("http://192.168.1.112/wxeg_xml/img/2_1.jpg", "99.9%", "张三1", "广西省**市1县*************", "张三1", "0710-11112222"));
		results.add(new ResultVO("http://api.androidhive.info/music/images/rihanna.png", "95.9%", "张三2", "广西省**市2县", "张三2", "0710-11112222"));
		results.add(new ResultVO("http://api.androidhive.info/music/images/arrehman.png", "94.7%", "张三3", "广西省**市3县", "张三3", "0710-11112222"));
		results.add(new ResultVO("http://api.androidhive.info/music/images/dido.png", "93.2%", "张三4", "广西省**市4县", "张三4", "0710-11112222"));
		results.add(new ResultVO("http://api.androidhive.info/music/images/enrique.png", "91.1%", "张三5", "广西省**市5县", "张三5", "0710-11112222"));
		results.add(new ResultVO("http://api.androidhive.info/music/images/enrique.png", "91.1%", "张三5", "广西省**市5县", "张三5", "0710-11112222"));
		results.add(new ResultVO("http://api.androidhive.info/music/images/enrique.png", "91.1%", "张三5", "广西省**市5县", "张三5", "0710-11112222"));
		results.add(new ResultVO("http://api.androidhive.info/music/images/enrique.png", "91.1%", "张三5", "广西省**市5县", "张三5", "0710-11112222"));
		
		List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();
		
		for (ResultVO result : results) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("picUrl", result.getPicUrl());
//			item.put("picUrl", R.drawable.cancle_pic);
			item.put("semblance", result.getSameTime());
			item.put("name", result.getName());
			item.put("addr", result.getAddr());
			item.put("linkman", result.getLinkman());
			item.put("tel", result.getTel());
			resultList.add(item);
		}
		
		LazyAdapter adapter = new LazyAdapter(ResultAdapterActivity.this, resultList,"");
//		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.item, new String[] { "picUrl", "text" }, new int[] { R.id.imageView_item, R.id.result_text });

		listView.setAdapter(adapter);

	}
}
