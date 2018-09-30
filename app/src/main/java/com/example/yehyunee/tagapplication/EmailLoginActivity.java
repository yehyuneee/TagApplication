package com.example.yehyunee.tagapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.yehyunee.tagapplication.util.CommandUtil;
import com.example.yehyunee.tagapplication.util.PreshareUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class EmailLoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mEmailIn;
    RelativeLayout mEmailSignIn;

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    private String email = "";
    private String password = "";

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        mContext = this;

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();

        mEmailIn = (EditText) findViewById(R.id.email_init_edit_text);
        mEmailSignIn = (RelativeLayout) findViewById(R.id.email_sign_in_layout);

        mEmailSignIn.setOnClickListener(this);

    }

    /**
     * 이메일 체크
     */
    public void signIn() {
        email = mEmailIn.getText().toString();
        password = "password";

        if(CommandUtil.isValidEmail(mContext, email) && CommandUtil.isValidPasswd(mContext, password)) {
            loginUser(email, password);
        }
    }

    /**
     * 이메일 체크 (파이어베이스 연동)
     */
    private void loginUser(final String email, String password)
    {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 유효한 이메일
                            Toast.makeText(EmailLoginActivity.this, "이미 가입된 이메일입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            // 유효하지 않은 이메일
                            Intent intent = new Intent(EmailLoginActivity.this, EmailSignOutActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            overridePendingTransition(R.anim.ani_slide_in_right, R.anim.ani_slide_out_left);
                        }
                    }
                });
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("firebase fail" , e.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_slide_in_left, R.anim.ani_slide_out_right);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.email_sign_in_layout:
                // 이메일 가입
                signIn();
                break;
       }
    }
}
