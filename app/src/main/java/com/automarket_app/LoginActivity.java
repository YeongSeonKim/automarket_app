package com.automarket_app;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.automarket_app.VO.UserVO;
import com.automarket_app.util.AES256Util;
import com.automarket_app.util.Helper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class LoginActivity extends AppCompatActivity {

    private String api_url ="";
    private AlertDialog dialog;

    private  EditText edEmail;
    private  EditText edPassword;
    private  Button btnRegister;
    private  Button btnLogin;

    UserVO vo = null;

    private boolean saveLoginData;
    private String email, not_aes_pwd;
    private String save_email,save_pwd,save_userid;
    private CheckBox checkBox;

    public SharedPreferences login_info; // 로그인 정보 폰에 저장,가져오기

    private AES256Util aes256;
    private String key;
    String pwd; // 암호화된 비밀번호

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // string에 있는 키 가져오는
       key = this.getString(R.string.aes_key);

        // 네트워크 연결상태 체크
        if(NetworkConnection() == false){
            NotConnected_showAlert();
        }

        // api_url
        api_url = Helper.getMetaData(this, "api_url");

        // 자동로그인 - 설정 값 불러오기
        login_info = getSharedPreferences("login_info", MODE_PRIVATE);
        load();

        Log.i("automarket_app","login_info" + login_info);

        // 이메일, 비밀번호 입력
        edEmail = (EditText)findViewById(R.id.edEmail_login);
        edPassword = (EditText)findViewById(R.id.edPassword_login);

        // 회원가입, 로그인 버튼
        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnLogin = (Button)findViewById(R.id.btnLogin);

        // 로그인 정보 기억하기 checkbox
        checkBox = (CheckBox)findViewById(R.id.checkBox);

        // 회원가입버튼
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                ComponentName cname = new ComponentName("com.automarket_app","com.automarket_app.RegisterActivity");
                intent.setComponent(cname);

                startActivity(intent);
                // intent를 보내면서 다음 액티비티로부터 데이터(이메일)를 받기 위해서 식별번호 주기
                // startActivityForResult(intent,1000);

                Log.i("automarket_app", "회원가입하기 페이지로 이동!");
            }
        });

        // 이전에 로그인 정보를 저장시킨 기록이 있다면 - 화면에 띄워주는 역할
        if (saveLoginData) {
            edEmail.setText(email);
            edPassword.setText(pwd);
            checkBox.setChecked(saveLoginData);
        }

        Log.i("automarket_app_data","saveLoginData : " + saveLoginData );

        if (save_userid == null){

            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

            this.finish();

        } else if(save_userid != null && !save_userid.equals("")){

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

            this.finish();
        }

        // 로그인 버튼
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = edEmail.getText().toString();
                not_aes_pwd = edPassword.getText().toString();

                // 비밀번호 암호화
                try {
                    aes256 = new AES256Util(key);
                    pwd = aes256.aesEncode(not_aes_pwd);
                    Log.i("automarket_app_login","암호화 비밀번호 : " + pwd);
                } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
                    Log.e("acs_err","문제");
                    e.printStackTrace();
                }

                // 입력창에 비어있을 때
                if (email.equals("") || not_aes_pwd.equals("")){
                    Toast.makeText(getApplicationContext(),"입력창이 비었습니다..! 이메일과 비밀번호 입력 부탁",Toast.LENGTH_SHORT).show();
                }

                // Thread를 만들어서 처리해야됨...........UI 관련작업이 아닌이상.... 계속뭐했찌ㅣㅣㅣ
                try {
                    Thread thread = new Thread() {
                        public void run() {
                            try {
                                //jackson library를 이용하여 json데이터 처리
                                ObjectMapper mapper = new ObjectMapper();
                                String json = mapper.writeValueAsString(vo);

                                vo = sendPostRequest(email, pwd); // get방식에서 post방식으로 변경
                                Log.i("automarket_app_login", "save_email : " + vo.getEmail() +"\n"+ "save_pwd :" +  vo.getPwd() +"\n"+ "save_userid_ :" +  vo.getUserid());

                                if (!(email.equals(vo.getEmail()))||(pwd.equals(vo.getPwd()))) {
                                    Toast.makeText(getApplicationContext(),"로그인 정보가 일치하지 않아요~~! 다시 로그인 해주세요...!",Toast.LENGTH_SHORT).show();
                                } else {
                                    // 로그인 하기, 첫 로드시 데이터 바인딩
                                    Intent intent = new Intent();
                                    ComponentName cname = new ComponentName("com.automarket_app", "com.automarket_app.MainActivity");
                                    intent.setComponent(cname);
                                    intent.putExtra("api_url",api_url);
                                    intent.putExtra("data", json); // json string으로 변환해서 넘기기
                                    startActivity(intent);
                                    Log.i("automarket_app_login", "로그인 성공~~ 메인으로 넘어갓~");
                                }
                            } catch (Exception e) {
                                Log.e("err1","문제");
                                Log.i("err1", e.toString());
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();

                    try {
                        thread.join();
                    } catch (Exception e) {
                        Log.e("err2","문제");
                        Log.i("err2", e.toString());
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    Log.e("err3","문제");
                    Log.i("err3", e.toString());
                    e.printStackTrace();
                }
            }
        });
    }

    // SharedPreferences - 설정값을 불러오는 함수 load()
    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
        saveLoginData = login_info.getBoolean("SAVE_LOGIN_DATA", false);
        save_email = login_info.getString("EMAIL", "");
        save_pwd = login_info.getString("PWD", "");
        save_userid = login_info.getString("USERID","");
    }

    // http 통신 post
    private UserVO sendPostRequest(String email, String pwd) throws Exception {

        Log.i("automarket_app_post","post 통신 접속시작!!!!");

        String receive_data;

        // 로그인 api url
        // http://localhost:8080/automarket/api/login.do
        URL url = new URL(api_url + "/api/login.do");


        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("charset", "utf-8");
//        con.connect();

        OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());

        Map<String, String> map = new HashMap<String, String>();

        map.put("email",email);
        map.put("pwd",pwd);

        //jackson library를 이용하여 json데이터 처리
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(map);

        // request data 확인
        Log.i("automarket_app_data","data : " + json);

        // request json
        out.write(json);
        out.flush();
        out.close();

        //기본적으로 stream은 bufferedReader형태로 생성
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String input_line;
        StringBuffer sb = new StringBuffer();
        while ((input_line = br.readLine()) != null) {
            sb.append(input_line);
        }
        receive_data = sb.toString();
        Log.i("automarket_app_login","receive_data :"+ receive_data);

        br.close();

        // response json
        int responseCode = con.getResponseCode();
        Log.i("automarket_app_data", "response 응답응답ㅎ해해ㅐㅎ");
        Log.d("automarket_app_Debug","응답코드 : " + responseCode); // 200 : 연결성공
        //Log.d("automarket_app_Debug","응답메세지 : " + con.getResponseMessage());

        UserVO userObject = mapper.readValue(receive_data, new TypeReference<UserVO>() {});
        Log.i("automarket_app_uservo" , "userObject : " + userObject);

//        if (userObject.getUserid() == null){
//            Toast.makeText(getApplicationContext(),"로그인에 실패했다요요용",Toast.LENGTH_SHORT).show();
//            return null;
//        }

        // 자동로그인
        // 로그인 정보 폰에 저장,가져오기
        // 저장된 값을 불러오기 위해 같은 네임파일을 찾는닷
//        login_info = getSharedPreferences("login_info", MODE_PRIVATE);
        // 저장을 하기 위해서 editor를 이용해 값을 저장 시켜준닷 , userid 핸드폰에 저장하기
        SharedPreferences.Editor editor = login_info.edit();

        // 데이터 저장
        editor.putBoolean("SAVE_LOGIN_DATA", checkBox.isChecked());
        editor.putString("USERID", userObject.getUserid());
        Log.i("automarket_app_login","userid :"+ userObject.getUserid());
        Log.i("automarket_app_login","email :"+ userObject.getEmail());
        Log.i("automarket_app_login","pwd :"+ userObject.getPwd());
        Log.i("automarket_app_login", "SharedPre(login_info) : " + login_info);
        editor.putString("EMAIL", edEmail.getText().toString());
        editor.putString("PWD", edPassword.getText().toString());
        editor.putString("myObject", json);

        // 최종 커밋 , apply 또는 commit 해야됨
        editor.commit();
        Log.i("automarket_app_save", "로그인 정보 저장 성공");
        return userObject;
    }


    // 네트워크 연결상태 체크
    private void NotConnected_showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
