package com.karl.karlconnectandroidbyadbandroidclent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.karl.karlconnectandroidbyadbandroidclent.dbmodel.UserModel;

public class UserAddActivity extends AppCompatActivity {

    Toolbar toolbar;

    EditText et_username;

    EditText et_password;

    EditText et_email;

    Button btn_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_email = findViewById(R.id.et_email);
        btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidForAdd()) {
                    UserModel userModel=new UserModel();
                    userModel.setUsername(et_username.getText().toString());
                    userModel.setPassword(et_password.getText().toString());
                    userModel.setEmail(et_email.getText().toString());
                    userModel.save();

                    finish();
                }
            }
        });

    }

    private boolean ValidForAdd() {

        if(TextUtils.isEmpty(et_username.getText().toString())){
            Toast.makeText(this,"用户名不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }


        if(TextUtils.isEmpty(et_password.getText().toString())){
            Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(et_email.getText().toString())){
            Toast.makeText(this,"邮箱不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
