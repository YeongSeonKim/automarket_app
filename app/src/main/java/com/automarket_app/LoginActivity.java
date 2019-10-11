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

import androidx.appcompat.app.AppCompatActivity;

import com.automarket_app.VO.UserVO;
import com.automarket_app.util.Helper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private String api_url ="";
    private AlertDialog dialog;

    private  EditText edEmail;
    private  EditText edPassword;
    private  Button btnRegister;
    private  Button btnLogin;

    UserVO vo = null;

    private boolean saveLoginData;
    private String email;
    private String pwd;
    private String userid;
    private CheckBox checkBox;

    public SharedPreferences login_info; // 로그인 정보 폰에 저장,가져오기

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 네트워크 연결상태 체크
        if(NetworkConnection() == false){
            NotConnected_showAlert();
        }

        // api_url
        api_url = Helper.getMetaData(this, "api_url");

        //설정 값 불러오기
        login_info = getSharedPreferences("login_info", MODE_PRIVATE);
        load();

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

        // 이전에 로그인 정보를 저장시킨 기록이 있다면
        if (saveLoginData) {
            edEmail.setText(email);
            edPassword.setText(pwd);
            checkBox.setChecked(saveLoginData);
        }

        // 로그인 버튼
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = edEmail.getText().toString();
                pwd = edPassword.getText().toString();

                // 이메일이랑 비밀번호가 입력되지 않았을때
                if (email.equals("")||pwd.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    dialog = builder.setMessage("이메일과 비밀번호를 입력하지 않았군용. 다시 입력해 주세요!!")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                    //Toast.makeText(getApplicationContext(),"이메일과 비밀번호를 입력하지 않았군용. 다시 입력해 주세요!!",Toast.LENGTH_SHORT).show();
                }

                // Thread를 만들어서 처리해야됨...........UI 관련작업이 아닌이상.... 계속뭐했찌ㅣㅣㅣ
                try {
                    Thread thread = new Thread() {
                        public void run() {
                            try {
                                vo = sendGet(email, pwd);
                                Log.i("automarket_app_login", vo.getEmail() + vo.getPwd());

                                if (vo.getEmail().equals("")) {
                                    DialogMessage();
                                } else {
                                    // 로그인 하기, 첫 로드시 데이터 바인딩
                                    Intent intent = new Intent();
                                    ComponentName cname = new ComponentName("com.automarket_app", "com.automarket_app.MainActivity");
                                    intent.setComponent(cname);
                                    intent.putExtra("email",email);
                                    intent.putExtra("data", vo);
                                    intent.putExtra("api_url",api_url);
                                    startActivity(intent);
                                    Log.i("automarket_app_login", "로그인 성공~~ 메인으로 넘어갓~");
                                }
                            } catch (Exception e) {
                                Log.e("err1","문제");
                                Log.i("err1", e.toString());
                            }
                        }
                    };
                    thread.start();

                    try {
                        thread.join();
                    } catch (Exception e) {
                        Log.e("err2","문제");
                        Log.i("err2", e.toString());
                    }
                } catch (Exception e) {
                    Log.e("err3","문제");
                    Log.i("err3", e.toString());
                }

//                    else {
//                   // 로그인 하기, 첫 로드시 데이터 바인딩
//                    Intent intent = new Intent();
//                    ComponentName cname = new ComponentName("com.automarket_app","com.automarket_app.service.LoginService");
//                    intent.setComponent(cname);
//                    intent.putExtra("email",email);
////                    intent.putExtra("pwd",pwd);
//                    intent.putExtra("api_url",api_url);
//
//                    startService(intent);
//
//                    Log.i("automarket_app", "로그인 페이지로 이동!");
//
//                }
            }
        });
    }

    // 설정값을 불러오는 함수
    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
        saveLoginData = login_info.getBoolean("SAVE_LOGIN_DATA", false);
        email = login_info.getString("EMAIL", "");
        //pwd = login_info.getString("PWD", "");
        userid = login_info.getString("USERID","");

    }


    private void DialogMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setMessage("로그인에 실패하였습니다. 다시 로그인 해주세요!!");
        builder.show();

    }

    private UserVO sendGet(String email, String pwd) throws Exception {

         String receive_data;

        URL url = new URL(api_url + "/api/login.do?email=" + email);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("charset", "utf-8");

//        Log.d("automarket_app_Debug","응답메세지 : " + con.getResponseMessage());

        Map<String, String> map = new HashMap<String, String>();

        map.put("email",email);
        map.put("pwd",pwd);

        //jackson library를 이용하여 json데이터 처리
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(map);

        Log.i("automarket_app_data","data : " + json);

        Log.i("automarket_app", "response 응답응답ㅎ해해ㅐㅎ");

        int responseCode = con.getResponseCode();
        Log.d("automarket_app_Debug","응답코드 : " + con.getResponseCode());

        //기본적으로 stream은 bufferedReader형태로 생성
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String input_line;
        StringBuffer sb = new StringBuffer();
        while ((input_line = br.readLine()) != null) {
            sb.append(input_line);
        }
        receive_data = sb.toString();
        Log.i("automarket_app_login","receive_data :"+ receive_data);
        Log.i("automarket_app_userid","userid :" + vo.getUserid());

        br.close();

        //ArrayList<UserVO> maplist = mapper.readValue(sb.toString(), new TypeReference<List<UserVO>>() {});
        UserVO userObject = mapper.readValue(receive_data, new TypeReference<UserVO>() {});

        Log.i("automarket_app_login", userObject.getEmail());

        // 로그인 정보 폰에 저장,가져오기
        // 저장된 값을 불러오기 위해 같은 네임파일을 찾는닷
//        login_info = getSharedPreferences("login_info", MODE_PRIVATE);
        // 저장을 하기 위해서 editor를 이용해 값을 저장 시켜준닷 , userid 저장하기
        SharedPreferences.Editor editor = login_info.edit();

        // 데이터 저장
        editor.putBoolean("SAVE_LOGIN_DATA", checkBox.isChecked());
        editor.putString("USERID", userObject.getUserid());
        editor.putString("EMAIL", edEmail.getText().toString());
        editor.putString("myObject", json);
        // 최종 커밋 , apply 또는 commit 해야됨
        editor.commit();
        Log.i("automarket_app_save", "로그인 객체 저장 성공");
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
