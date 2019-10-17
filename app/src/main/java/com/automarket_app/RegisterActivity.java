package com.automarket_app;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.automarket_app.VO.UserVO;
import com.automarket_app.util.AES256Util;
import com.automarket_app.util.Helper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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

    private String re_email, re_name, re_pwd, re_RepeatPwd;
    private String re_deviceid = "";
    private String re_adminflag = "0";

    private AES256Util aes256;
    private String key;
    String pwd; // 암호화된 비밀번호

    private List<UserVO> re_user_list;
    private UserVO userVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // string에 있는 키 가져오는
        key = this.getString(R.string.aes_key);

        // 네트워크 연결상태 체크
        if (NetworkConnection() == false) {
            NotConnected_showAlert();
        }

        api_url = Helper.getMetaData(this, "api_url");

        // 이메일 입력, 중복체크
        etEmail = (EditText) findViewById(R.id.etEmail_register);
        btn_email_check = (Button) findViewById(R.id.btn_email_check);

        // 이름입력
        etName = (EditText) findViewById(R.id.etName_register);

        // 비밀번호, 비밀번호 재입력
        etPassword = (EditText) findViewById(R.id.etPassword_register);
        etRepeatPassword = (EditText) findViewById(R.id.etRepeatPassword_register);

        // 가입, 취소 버튼
        btnDone = (Button) findViewById(R.id.btnDone);
        btnCancel = (Button) findViewById(R.id.btnCancel);


        // 1. 이메일 중복체크버튼
        btn_email_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                re_email = etEmail.getText().toString();
                if (validate) {
                    return;//검증 완료
//                    final ObjectMapper mapper = new ObjectMapper();
//                    Intent intent = new Intent();
//                    ComponentName cname = new ComponentName("com.automarket_app", "com.automarket_app.service.RegisterEmailCheckService");
//                    intent.setComponent(cname);
//                    //intent.putExtra("emailData", json);
//                    intent.putExtra("api_url", api_url);
//                    startService(intent);
                }
                //ID 값을 입력하지 않았다면
                if (re_email.equals("")) {
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

        // 회원가입 시작
            // 가입버튼
            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // 입력 회원정보 가져오기
                    re_email = etEmail.getText().toString();
                    re_name = etName.getText().toString();
                    re_pwd = etPassword.getText().toString();
                    re_RepeatPwd = etRepeatPassword.getText().toString();

                    // MAC주소
                    re_deviceid = getMacAddress();
                    Log.i("automarket_app", "deviceid : " + re_deviceid);

                    // 중복버튼 체크 했는지 안했는지 확인
//                if (!validate){
//                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
//                    dialog = builder.setMessage("이메일 중복체크를 해주세용~~")
//                            .setNegativeButton("OK", null)
//                            .create();
//                    dialog.show();
//                    return;
//                }

                    // 입력 칸 한칸이라도 비어있을 경우
                    if (re_email.length() == 0 || re_name.length() == 0 || re_pwd.length() == 0 || re_RepeatPwd.length() == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        dialog = builder.setMessage("빈칸을 채워서 입력해주세요!!!")
                                .setNegativeButton("OK", null)
                                .create();
                        dialog.show();
                        return;
                    }

                    // 비밀번호 암호화
                    try {
                        aes256 = new AES256Util(key);
                        pwd = aes256.aesEncode(re_pwd);
                        Log.i("automarket_app_login", "암호화 비밀번호 : " + pwd);
                    } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
                        Log.e("acs_err", "문제");
                        e.printStackTrace();
                    }

               try {
                // 입력한 값 json 형태로.. Service에 값 전달
                HashMap<String,Object> map = new HashMap<String,Object>();

                    map.put("email",re_email);
                    map.put("name",re_name);
                    map.put("pwd",re_pwd);
                    map.put("deviceid",re_deviceid);
                    map.put("adminflag",re_adminflag);

                final ObjectMapper mapper = new ObjectMapper();

                String json="";
                try{
                    json = mapper.writeValueAsString(map);

                    Log.i("automarket_app_data", "data : " + json);
                }catch (Exception e) {
                    e.printStackTrace();
                    Log.e("err", "에러발생이다" + e);
                }

                    // 서비스 시작
                    Intent intent = new Intent();
                    ComponentName cname = new ComponentName("com.automarket_app", "com.automarket_app.service.RegisterService");
                    intent.setComponent(cname);
                    intent.putExtra("register_user_vo", json);
                    intent.putExtra("api_url", api_url);
                    startService(intent);

               } catch (Exception e) {
                   e.printStackTrace();
               }

                    Toast.makeText(getApplicationContext(),"회원가입성공!!",Toast.LENGTH_SHORT).show();
                    Log.i("automarket_app_register", "회원가입됬다아아ㅏ아ㅏㅏ");
                }
            });


            // 취소버튼
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("automarket_app", "데이터가 Activity에 도달");
        if (intent.getAction() != null && intent.getAction().equals("register")) {
            String register_userList = intent.getExtras().getString("RegisterUserdata");

            final ObjectMapper mapper = new ObjectMapper();
            try {
//                HashMap<String,Object> map = mapper.readValue(register_userList, new TypeReference<HashMap<String,Object>>() {});
//                map.put("email", re_email);
//                map.put("name", re_name);
//                map.put("pwd", pwd);
//                map.put("deviceid",re_deviceid);
//                map.put("adminflag", re_adminflag);
//
////               final String json = mapper.writeValueAsString(map);
//
//                Log.i("automarket_app_map","hashmap : "+ map);
//
//                map.get("status"); // json key,value값 ("msg","status")들어오는데 ststus는 int형
//                                   // status가 1이면 회원가입 성공 , 토스트메세지

                userVO = mapper.readValue(register_userList, new TypeReference<UserVO>() {});

               final String json = mapper.writeValueAsString(userVO);

                Log.i("automarket_app","json : " + json);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

