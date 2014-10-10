package tool.listView;

import java.util.HashMap;
import java.util.List;
import tool.FtpUtil;
import tool.StaticParameter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.hl.takepicture.R;

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private List<HashMap<String, Object>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    public String ftp_dest_path; 
    
    public LazyAdapter(Activity a, List<HashMap<String, Object>> d,String ftp_dest_path) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
        this.ftp_dest_path = ftp_dest_path;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    /**
	 * 
	 * @Author:HaoMing(郝明)
	 * @Project_name:人脸识别
	 * @Full_path:tool.listView.LazyAdapter.java
	 * @Date:@2014 2014-8-18 下午2:51:49
	 * @Return_type:
	 * @Desc : 通过http获得图片
	 */
    /*public View getView(int position, View convertView, ViewGroup parent) {
     
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.item, null);

        TextView semblance = (TextView)vi.findViewById(R.id.result_mould_semblance); 
        TextView name = (TextView)vi.findViewById(R.id.result_mould_name); 
        TextView addr = (TextView)vi.findViewById(R.id.result_mould_addr); 
        TextView linkman = (TextView)vi.findViewById(R.id.result_mould_linkman); 
        TextView tel = (TextView)vi.findViewById(R.id.result_mould_tel); 
        
        
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.imageView_item);
        
        HashMap<String, Object> map = new HashMap<String, Object>();
        map = data.get(position);
        
        // 设置ListView的相关值
        semblance.setText(map.get("semblance").toString());
        name.setText(map.get("name").toString());
        addr.setText(map.get("addr").toString());
        linkman.setText(map.get("linkman").toString());
        tel.setText(map.get("tel").toString());
       
        String url = map.get("picUrl").toString();
        imageLoader.DisplayImage(url, thumb_image);
        return vi;
    }*/
    
    /**
	 * 
	 * @Author:HaoMing(郝明)
	 * @Project_name:人脸识别
	 * @Full_path:tool.listView.LazyAdapter.java
	 * @Date:@2014 2014-8-18 下午2:51:49
	 * @Return_type:
	 * @Desc : 通过发ftp获得图片
	 */
    /*public View getView(int position, View convertView, ViewGroup parent) {
    	if("".equals(ftp_dest_path)){
    		return null;
    	}
    	View vi=convertView;
    	if(convertView==null)
    		vi = inflater.inflate(R.layout.item, null);
    	
    	TextView semblance = (TextView)vi.findViewById(R.id.result_mould_semblance); 
    	TextView name = (TextView)vi.findViewById(R.id.result_mould_name); 
    	TextView addr = (TextView)vi.findViewById(R.id.result_mould_addr); 
    	TextView linkman = (TextView)vi.findViewById(R.id.result_mould_linkman); 
    	TextView tel = (TextView)vi.findViewById(R.id.result_mould_tel); 
    	
    	
    	ImageView thumb_image=(ImageView)vi.findViewById(R.id.imageView_item);
    	
    	HashMap<String, Object> map = new HashMap<String, Object>();
    	map = data.get(position);
    	
    	// 设置ListView的相关值
    	semblance.setText(map.get("semblance").toString());
    	name.setText(map.get("name").toString());
    	addr.setText(map.get("addr").toString());
    	linkman.setText(map.get("linkman").toString());
    	tel.setText(map.get("tel").toString());
    	String url = map.get("picUrl").toString();
    	
//    	imageLoader.DisplayImage(url, thumb_image);
    	
    	byte[] data = FtpUtil.getFileByte(StaticParameter.FTP_IP, StaticParameter.FTP_PORT,
    			StaticParameter.FTP_USERName,StaticParameter.FTP_USERPWD,
    			ftp_dest_path,url.substring(url.lastIndexOf("/")+1),null);
    	Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
    	thumb_image.setImageBitmap(bitmap);//显示图片
    	
    	return vi;
    }
    */
    /**
     * 
     * @Author:HaoMing(郝明)
     * @Project_name:人脸识别
     * @Full_path:tool.listView.LazyAdapter.java
     * @Date:@2014 2014-8-18 下午2:51:49
     * @Return_type:
     * @Desc : 通过发ftp获得图片到本地指定的目录中，再读取本地的图片。
     */
    public View getView(int position, View convertView, ViewGroup parent) {
    	if("".equals(ftp_dest_path)){
    		return null;
    	}
    	View vi=convertView;
    	if(convertView==null)
    		vi = inflater.inflate(R.layout.item, null);
    	
    	TextView semblance = (TextView)vi.findViewById(R.id.result_mould_semblance); 
    	TextView name = (TextView)vi.findViewById(R.id.result_mould_name); 
    	TextView addr = (TextView)vi.findViewById(R.id.result_mould_addr); 
    	TextView linkman = (TextView)vi.findViewById(R.id.result_mould_linkman); 
    	TextView tel = (TextView)vi.findViewById(R.id.result_mould_tel); 
    	
    	
    	ImageView thumb_image=(ImageView)vi.findViewById(R.id.imageView_item);
    	
    	HashMap<String, Object> map = new HashMap<String, Object>();
    	map = data.get(position);
    	
    	// 设置ListView的相关值
    	semblance.setText(map.get("semblance").toString());
    	name.setText(map.get("name").toString());
    	addr.setText(map.get("addr").toString());
    	linkman.setText(map.get("linkman").toString());
    	tel.setText(map.get("tel").toString());
    	String url = map.get("picUrl").toString();
    	
//    	System.out.println("url : "+url);
    	Bitmap bitmap =  BitmapFactory.decodeFile(url);
    	thumb_image.setImageBitmap(bitmap);//显示图片
    	
    	return vi;
    }
     
}
