package anotherm4.idcard;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import anotherm4.idcard.Utils.HttpUtils;

public class MainActivity extends AppCompatActivity {
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    private static final String APPKEY = "2aeb5d90b1e18aa1e39e71e9f6d44dc5";
    public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
    public EditText etInput;
    public TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);}
        etInput = (EditText) findViewById(R.id.et_input);
        tvResult = (TextView) findViewById(R.id.tv_result);
    }

    public void btnInfo(View view) {
        infoRequest();
    }

    public void btnLeak(View view) {
        leakRequest();
    }

    public void btnLoss(View view) {
        leakRequest();
    }


    public void infoRequest() {
        String result = null;
        String url = "http://apis.juhe.cn/idcard/index";//请求接口地址
        Map<String, String> params = new HashMap<String, String>();//请求参数
        params.put("cardno", etInput.getText().toString());//身份证号码
        params.put("dtype", "");//返回数据格式：json或xml,默认json
        params.put("key", APPKEY);//你申请的key

        try {
            result = HttpUtils.Utils(url, params, "GET");
            JSONObject object = new JSONObject(result);
            if (object.getInt("error_code") == 0) {
                Log.d("InfoError", String.valueOf(object.get("result")));
                tvResult.setText(String.valueOf(object.get("result")));
            } else {
                Log.d("InfoError",object.get("error_code") + ":" + object.get("reason"));
                tvResult.setText((String)object.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void leakRequest() {
        String result = null;
        String url = "http://apis.juhe.cn/idcard/leak";//请求接口地址
        Map<String, String> params = new HashMap<String, String>();//请求参数
        params.put("cardno", etInput.getText().toString());//身份证号码
        params.put("dtype", "");//返回数据格式：json或xml,默认json
        params.put("key", APPKEY);//你申请的key

        try {
            result = HttpUtils.Utils(url, params, "GET");
            JSONObject object = new JSONObject(result);
            if (object.getInt("error_code") == 0) {
               Log.d("LeakError",String.valueOf(object.get("result")));
                tvResult.setText(String.valueOf(object.get("result")));
            } else {
                Log.d("LeakError",object.get("error_code") + ":" + object.get("reason"));
                tvResult.setText((String)object.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void safeRequest() {
        String result = null;
        String url = "http://apis.juhe.cn/idcard/loss";//请求接口地址
        Map<String, String> params = new HashMap<String, String>();//请求参数
        params.put("cardno", etInput.getText().toString());//身份证号码
        params.put("dtype", "");//返回数据格式：json或xml,默认json
        params.put("key", APPKEY);//你申请的key

        try {
            result = HttpUtils.Utils(url, params, "GET");
            JSONObject object = new JSONObject(result);
            if (object.getInt("error_code") == 0) {
                Log.d("LossError",String.valueOf(object.get("result")));
                tvResult.setText(String.valueOf(object.get("result")));
            } else {
                Log.d("LossError",object.get("error_code") + ":" + object.get("reason"));
                tvResult.setText((String)object.get("reason"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
