package tool;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;


public class UtilsSendStr {
	
	/**
     * 通过Httppost传递参数
     * @author haom
     * @date 2013-06-13 14:14:31
     * @param urlString 地址
     * @param code 编码
     * @param heads 请求
     * @param xml 要传的参数
     * @return String
     * @throws Exception
     */
    public static String httpPost(String urlString, String code, HashMap<String, String> heads,String xml) throws Exception
    {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(urlString);
        if (heads != null)
        {
            for (HashMap.Entry<String, String> entry : heads.entrySet())
            {
                post.setHeader(entry.getKey(), entry.getValue());
            }
        }
        StringEntity entity = new StringEntity(xml, code);
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        HttpEntity httpEntity = response.getEntity();
        InputStream is = httpEntity.getContent();
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = "";
        while ((line = br.readLine()) != null)
        {
            sb.append(line);
        }
        return sb.toString();
    }
    
    /**
     * @author haom
     * @param urlString url地址
     * @param xml 传递xml数据
     * @param hashMap 设置头参数
     * @return 服务器返回结果
     * @throws Exception
     */
    public static String sendXml(String urlString, String xml, HashMap<String, String> hashMap)throws Exception {
        byte[] data = xml.getBytes();
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(10000);
        hashMap.put("Content-Type", "text/xml; charset=UTF-8");
        hashMap.put("Content-Length", String.valueOf(data.length));
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        for (String key : hashMap.keySet()) {
            conn.setRequestProperty(key, hashMap.get(key));
        }
        conn.connect();
        OutputStream outStream = conn.getOutputStream();
        outStream.write(data);
        outStream.flush();
        outStream.close();
        
        String returnXml = "";
        System.out.println("conn.getResponseCode() : "+conn.getResponseCode());
        if (conn.getResponseCode() == 200)
        {
            InputStream inputStream = conn.getInputStream();
            BufferedReader dataInputStream = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            String line = "";
            while ((line = dataInputStream.readLine()) != null) {
                returnXml += line;
            }
            return returnXml;
        }
        return null;
    }
}
