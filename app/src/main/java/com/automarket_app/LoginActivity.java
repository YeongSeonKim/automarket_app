package com.automarket_app;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.EditText;

import com.automarket_app.VO.UserVO;
import com.automarket_app.util.Helper;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private String api_url ="";
    private AlertDialog dialog;

    List<UserVO> userList = new ArrayList<UserVO>();
    UserVO vo = null;

    public SharedPreferences login_info; // 1.폰에 저장된 로그인 정보 가져오기

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 네트워크 연결상태 체크
        if(NetworkConnection() == false){
            NotConnected_showAlert();
        }

        api_url = Helper.getMetaData(this, "api_url");

        // 이메일, 비밀번호 입력
        final EditText edEmail = (EditText)findViewById(R.id.edEmail_login);
        final EditText edPassword = (EditText)findViewById(R.id.edPassword_login);

        // 회원가입, 로그인 버튼
        Button btnRegister = (Button)findViewById(R.id.btnRegister);
        Button btnLogin = (Button)findViewById(R.id.btnLogin);

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

        // 로그인 버튼
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = edEmail.getText().toString();
                final String pwd = edPassword.getText().toString();

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


                //login_info = getSharedPreferences("preferences", MODE_PRIVATE);

                // Thread를 만들어서 처리해야됨...........UI 관련작업이 아닌이상.... 계속뭐했찌ㅣㅣㅣ
                try {
                    Thread thread = new Thread() {
                        public void run() {
                            try {
                                vo = sendGet(email, pwd);
                                Log.i("LOGIN", vo.getEmail());

                                if (vo.getEmail().equals("")) {
                                    DialogMessage();
                                } else {
                                    Intent intent = new Intent();
                                    ComponentName componentName = new ComponentName("com.automarket_app", "com.automarket_app");
                                    intent.setComponent(componentName);
                                    intent.putExtra("data", vo);
                                    startActivity(intent);
                                    Log.i("LOGIN", vo.getEmail());
                                    Log.i("LOGIN", "로그인 성공!!");
                                }
                            } catch (Exception e) {
                                Log.i("err", e.toString());
                            }
                        }
                    };
                    thread.start();

                    try {
                        thread.join();
                    } catch (Exception e) {
                        Log.i("err", "문제발생");
                    }
                } catch (Exception e) {
                    Log.i("err", e.toString());
                }





//                    else {
//                    // 로그인 하기, 첫 로드시 데이터 바인딩
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

    private void DialogMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        dialog = builder.setMessage("입력하신 정보가 일치하지 않습니다. 다시 입력해 주세요!!")
                .setNegativeButton("OK", null)
                .create();
        dialog.show();

    }

    private UserVO sendGet(String email, String pwd) throws Exception {

//        String receivedata;
//        String sendMsg;

        URL url = new URL(api_url + "/api/login.do?email=" + email);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("charset", "utf-8");

        Log.d("automarket_app_LOG","응답코드 : " + con.getResponseCode());
        Log.d("automarket_app_LOG","응답메세지 : " + con.getResponseMessage());



        return null;
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
