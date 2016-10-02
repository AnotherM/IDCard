package anotherm4.idcard.Utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by anotherm4 on 2016/10/2.
 */

public class HttpUtils {
    private static final String DEF_CHATSET = "UTF-8";
    private static final int DEF_CONN_TIMEOUT = 30000;
    private static final int DEF_READ_TIMEOUT = 30000;

    public static String Utils(String strUrl, Map params, String method) throws IOException {
        HttpURLConnection conn=null;
        BufferedReader reader=null;
        String rs=null;
        StringBuilder sb=new StringBuilder();
        if(method==null || method.equals("GET")){
            strUrl = strUrl+"?"+urlencode(params);
        }
        URL url = new URL(strUrl);
        conn = (HttpURLConnection) url.openConnection();
        if(method==null || method.equals("GET")){
            conn.setRequestMethod("GET");
        }else{
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
        }
        String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
        conn.setRequestProperty("User-agent", userAgent);
        conn.setUseCaches(false);
        conn.setConnectTimeout(DEF_CONN_TIMEOUT);
        conn.setReadTimeout(DEF_READ_TIMEOUT);
        conn.setInstanceFollowRedirects(false);
        conn.connect();
        if (params!= null && method.equals("POST")) {
            try {
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(urlencode(params));
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        InputStream is = conn.getInputStream();
        reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
        String strRead = null;
        while ((strRead = reader.readLine()) != null) {
            sb.append(strRead);
        }
        rs = sb.toString();
        return rs;
    }
    private static String urlencode(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"",DEF_CHATSET)).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
