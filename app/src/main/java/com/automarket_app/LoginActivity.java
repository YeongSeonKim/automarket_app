package com.automarket_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.automarket_app.VO.UserListVO;
import com.automarket_app.VO.UserVO;
import com.automarket_app.util.Helper;

public class LoginActivity extends AppCompatActivity {

    String api_url ="";
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        api_url = Helper.getMetaData(this, "api_url");

        final EditText edEmail_login = (EditText)findViewById(R.id.edEmail_login);
        final EditText edPassword_login = (EditText)findViewById(R.id.edPassword_login);


        Button btnRegister = (Button)findViewById(R.id.btnRegister);
        Button btnLogin = (Button)findViewById(R.id.btnLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                Log.i("register", "회원가입 하기이이!");

                LoginActivity.this.startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edEmail_login.getText().toString();
                String pwd = edPassword_login.getText().toString();


                // email이랑 pwd가 db에있는 정보와 같은지 확인
                // 같으면 로그인되고 안되면 다시 입력하라고 알려주기
                if (email.equals("")||pwd.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    dialog = builder.setMessage("입력하신 정보가 일치하지 않습니다. 다시 입력해 주세요!!")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                    //Toast.makeText(getApplicationContext(),"입력하신정보가 틀립니다.다시입력해주세요",Toast.LENGTH_SHORT).show();
                }



                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("api_url",api_url);

                Log.i("login", "로그인 성공ㅇㅇ!");
                startActivity(intent);
            }
        });
    }
}
