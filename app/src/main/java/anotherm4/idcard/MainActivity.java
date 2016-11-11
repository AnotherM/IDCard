package anotherm4.idcard;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class MainActivity extends AppCompatActivity {

    private static final String DEF_CHATSET = "UTF-8";
    private FirebaseAnalytics firebaseAnalytics;
    private EditText etInput;
    private TextView tvResult;
    private ProgressBar pgProcess;
    private JSONObject object;
    private String result;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                pgProcess.setVisibility(View.GONE);
                try {
                    if (object.getInt("error_code") == 0) {
                        String status = String.valueOf(object.optJSONObject("result").optString("tips"));
                        if (status != "") {
                            tvResult.setText(getString(R.string.status) + status);
                        } else {
                            String area = String.valueOf(object.optJSONObject("result").optString("area"));
                            String sex = String.valueOf(object.optJSONObject("result").optString("sex"));
                            String birthday = String.valueOf(object.optJSONObject("result").optString("birthday"));

                            tvResult.setText(getString(R.string.area) + area + "\n" + getString(R.string.sex) + sex + "\n" + getString(R.string.birthday) + birthday);
                        }
                    } else {
                        tvResult.setText(String.valueOf(object.get("reason")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (tvResult.getText().equals("")) {
                    tvResult.setText(getString(R.string.try_again));
                }
            }
        }
    };
    private String url;

    public static String Utils(String strUrl, Map params, String method) throws IOException {
        HttpURLConnection conn;
        BufferedReader reader;
        String rs;
        StringBuilder sb = new StringBuilder();
        if (method == null || method.equals("GET")) {
            strUrl = strUrl + "?" + urlencode(params);
        }
        URL url = new URL(strUrl);
        conn = (HttpURLConnection) url.openConnection();
        if (method == null || method.equals("GET")) {
            conn.setRequestMethod("GET");
        } else {
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
        }
        conn.setUseCaches(false);
        conn.setConnectTimeout(5000 * 3);
        conn.setReadTimeout(5000 * 3);
        conn.setInstanceFollowRedirects(false);
        conn.connect();
        if (params != null && (method != null && method.equals("POST"))) {
            try {
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(urlencode(params));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        InputStream is = conn.getInputStream();
        reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
        String strRead;
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
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", DEF_CHATSET)).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        etInput = (EditText) findViewById(R.id.et_input);
        tvResult = (TextView) findViewById(R.id.tv_result);
        pgProcess = (ProgressBar) findViewById(R.id.pg_process);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "name");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "content");
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public void btnInfo(View view) {
        pgProcess.setVisibility(View.VISIBLE);
        url = "http://apis.juhe.cn/idcard/index";//请求接口地址
        netThread();
    }

    public void btnLeak(View view) {
        pgProcess.setVisibility(View.VISIBLE);
        url = "http://apis.juhe.cn/idcard/leak";//请求接口地址
        netThread();
    }

    public void btnLoss(View view) {
        pgProcess.setVisibility(View.VISIBLE);
        url = "http://apis.juhe.cn/idcard/loss";//请求接口地址
        netThread();
    }

    public void netThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request();
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
            }
        }).start();
    }

    public void Request() {
        Map<String, String> params = new HashMap<>();//请求参数
        params.put("cardno", etInput.getText().toString());//身份证号码
        params.put("dtype", "");//返回数据格式：json或xml,默认json
        params.put("key", getString(R.string.APP_KEY));//你申请的key

        try {
            result = Utils(url, params, "GET");
            object = new JSONObject(result);
            if (object.getInt("error_code") == 0) {
                Log.d("Success", String.valueOf(object.get("result")));
            } else {
                Log.d("Error", object.get("error_code") + ":" + object.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
