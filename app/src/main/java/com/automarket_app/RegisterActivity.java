package com.automarket_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.automarket_app.util.Helper;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    String api_url ="";
    private AlertDialog dialog;
    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        api_url = Helper.getMetaData(this, "api_url");

        // 이메일 입력, 중복체크
        final EditText etEmail_register = (EditText)findViewById(R.id.etEmail_register);
        Button btn_email_check = (Button)findViewById(R.id.btn_email_check);

        // 이름입력
        final EditText etName_register = (EditText)findViewById(R.id.etName_register);

        // 비밀번호, 비밀번호 재입력
        final EditText etPassword_register = (EditText)findViewById(R.id.etPassword_register);
        final EditText etRepeatPassword_register = (EditText)findViewById(R.id.etRepeatPassword_register);

        // 가입, 취소 버튼
        Button btnDone = (Button)findViewById(R.id.btnDone);
        Button btnCancel = (Button)findViewById(R.id.btnCancel);

        // 이메일 중복체크버튼
        btn_email_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserEmail = etEmail_register.getText().toString();
                if(validate){
                    return;//검증 완료
                }
                //ID 값을 입력하지 않았다면
                if(UserEmail.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("Email is empty")
                            .setPositiveButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }
            }
        });



        // 가입버튼
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 입력 회원정보 가져오기
                String UserEmail = etEmail_register.getText().toString();
                String name = etName_register.getText().toString();
                String Pwd = etPassword_register.getText().toString();
                String RepeatPwd = etRepeatPassword_register.getText().toString();
                //String DeviceId = ;

                // Email 중복체크 했는지 확인
//                if (!validate){
//                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
//                    dialog = builder.setMessage("First Check Email please")
//                            .setNegativeButton("OK", null)
//                            .create();
//                    dialog.show();
//                    return;
//                }

                // 입력 칸 한칸이라도 비어있을 경우
                if(UserEmail.equals("")||name.equals("")||Pwd.equals("")||RepeatPwd.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("빈칸을 채워서 입력해주세요!!!")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }

                // 비밀번호 입력이랑 비밀번호 재입력 한게 같은지 확인


                String result =  UserEmail + "\n" + name + "\n" + Pwd + "\n" + RepeatPwd;
                //HashMap<String,String> map  = new HashMap<String,String>();
                Log.i("automarket_app_register",result);

                // 회원가입 시작
                //Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                //startActivity(intent);
                Intent intent = new Intent();
                ComponentName cname = new ComponentName("com.automarket_app","com.automarket_app.service.RegisterService");
                intent.putExtra("api_url",api_url);
                intent.setComponent(cname);
                startService(intent);

                Log.i("automarket_app_register","회원가입됬다아아ㅏ아ㅏㅏ");
            }
        });

        // 취소버튼
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }
        });
    }
}
