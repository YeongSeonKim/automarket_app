package com.automarket_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.automarket_app.VO.UserVO;
import com.automarket_app.util.AES256Util;
import com.automarket_app.util.Helper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btn_email_check;
    private EditText etName;
    private EditText etPassword;
    private EditText etRepeatPassword;
    private Button btnDone;
    private Button btnCancel;

    String api_url ="";
    private AlertDialog dialog;
    private boolean validate = false;

    String email, name, pwd, RepeatPwd, deviceid;

    private AES256Util aes256;
    private String key;

    class RegisterRunnable implements Runnable {

        private String email, name, pwd;
        private Handler handler;

        public RegisterRunnable(String email, String name, String pwd, Handler handler) {
            this.email = email;
            this.name = name;
            this.pwd = pwd;
            this.handler = handler;
        }

        @Override
        public void run() {

            String receive_data;

            try {
                // http://localhost:8080/automarket/register.do
                //  String url = "http://localhost:8080/automarket/register.do";
                URL url = new URL(api_url + "/register.do");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());

                //기본적으로 stream은 bufferedReader형태로 생성
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = null;
                StringBuffer sb = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                receive_data = sb.toString();
                Log.i("automarket_app_register","receive_data :"+ receive_data);

                br.close();

                Map<String, String> map = new HashMap<String, String>();

                map.put("email",email);
                map.put("name",name);
                map.put("pwd",pwd);
                map.put("deviceid",deviceid);

                //jackson library를 이용하여 json데이터 처리
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(map);

                Log.i("automarket_app_data","data : " + json);

                Log.i("automarket_app_data", "response 응답응답ㅎ해해ㅐㅎ");

                int responseCode = con.getResponseCode();
                Log.d("automarket_app_Debug","응답코드 : " + responseCode);



            } catch (Exception e) {
                Log.e("err","문제");
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 네트워크 연결상태 체크
        if(NetworkConnection() == false){
            NotConnected_showAlert();
        }

        api_url = Helper.getMetaData(this, "api_url");

        // 이메일 입력, 중복체크
        etEmail = (EditText)findViewById(R.id.etEmail_register);
        btn_email_check = (Button)findViewById(R.id.btn_email_check);

        // 이름입력
        etName = (EditText)findViewById(R.id.etName_register);

        // 비밀번호, 비밀번호 재입력
        etPassword = (EditText)findViewById(R.id.etPassword_register);
        etRepeatPassword = (EditText)findViewById(R.id.etRepeatPassword_register);

        // 가입, 취소 버튼
        btnDone = (Button)findViewById(R.id.btnDone);
        btnCancel = (Button)findViewById(R.id.btnCancel);


        // 1. 이메일 중복체크버튼
        btn_email_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString();
                if(validate){
                    return;//검증 완료
                }
                //ID 값을 입력하지 않았다면
                if(email.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("이메일 입력칸이 비었습니다.")
                            .setPositiveButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }
            }
        });


        // 2. 비밀번호 일치 검사 ( 비밀번호 입력이랑 비밀번호 재입력 한게 같은지 확인 )
        etRepeatPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String Pwd = etPassword.getText().toString();
                String RepeatPwd = etRepeatPassword.getText().toString();

                // 비밀번호 입력이랑 비밀번호 재입력 한게 같은지 확인
                if (Pwd.equals(RepeatPwd)) {
                    etPassword.setTextColor(getColor(R.color.colorGreen));
                    etRepeatPassword.setTextColor(getColor(R.color.colorGreen));
                } else {
                    etPassword.setTextColor(getColor(R.color.colorRed));
                    etRepeatPassword.setTextColor(getColor(R.color.colorRed));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 가입버튼
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 입력 회원정보 가져오기
                email = etEmail.getText().toString();
                name = etName.getText().toString();
                pwd = etPassword.getText().toString();
                RepeatPwd = etRepeatPassword.getText().toString();

                // MAC주소
                deviceid = getMacAddress();
                Log.i("automarket_app", "deviceid : " + deviceid);

                // Email 중복체크 했는지 확인
//                if (!validate){
//                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
//                    dialog = builder.setMessage("이메일 중복체크를 해주세용~~")
//                            .setNegativeButton("OK", null)
//                            .create();
//                    dialog.show();
//                    return;
//                }

                // 입력 칸 한칸이라도 비어있을 경우
                if(email.equals("")||name.equals("")||pwd.equals("")||RepeatPwd.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("빈칸을 채워서 입력해주세요!!!")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }


                //HashMap<String,String> map  = new HashMap<String,String>();
                //Log.i("automarket_app_register",result);

                // 회원가입 시작
                //Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                //startActivity(intent);
                Intent intent = new Intent();
                ComponentName cname = new ComponentName("com.automarket_app","com.automarket_app.service.RegisterService");
                intent.setComponent(cname);
                intent.putExtra("api_url",api_url);
                startService(intent);

                Log.i("automarket_app_register","회원가입됬다아아ㅏ아ㅏㅏ");
            }
        });

        // 취소버튼
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                startActivity(intent);
                finish();
            }
        });
    }

    // MAC Address 가져오기
    public static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            //handle exception
            Log.i("automarket_app","err : " + e);
            Log.e("automarket_app", e.toString());
        }
        return "";
    }


    // 네트워크 연결상태 체크
    private void NotConnected_showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("네트워크 연결 오류");
        builder.setMessage("사용 가능한 무선네트워크가 없습니다.\n" + "먼저 무선네트워크 연결상태를 확인해 주세요.")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish(); // exit
                        //application 프로세스를 강제 종료
                        android.os.Process.killProcess(android.os.Process.myPid() );
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private boolean NetworkConnection() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
        boolean isMobileAvailable = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isAvailable();
        boolean isMobileConnect = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        boolean isWifiAvailable = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isAvailable();
        boolean isWifiConnect = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        if ((isWifiAvailable && isWifiConnect) || (isMobileAvailable && isMobileConnect)){
            return true;
        }else{
            return false;
        }
    }
}

