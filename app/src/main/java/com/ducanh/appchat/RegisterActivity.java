package com.ducanh.appchat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText username,password,email;
    private Button btnRegister;
    FirebaseAuth auth;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ĐĂNG KÝ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username=findViewById(R.id.username);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);

        btnRegister=findViewById(R.id.btn_register);

        auth=FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtUsername=username.getText().toString();
                String txtEmail=email.getText().toString();
                String txtPassword=password.getText().toString();

                if (TextUtils.isEmpty((txtUsername)) || TextUtils.isEmpty((txtEmail))
                || TextUtils.isEmpty((txtPassword))){
                    Toast.makeText(RegisterActivity.this, "Hãy nhập tất cả các dòng",Toast.LENGTH_SHORT).show();

                } else if(txtPassword.length()<6){
                    Toast.makeText(RegisterActivity.this, "Mật khẩu nhiều hơn 6 kí tự",Toast.LENGTH_SHORT).show();
                }else{
                    register(txtUsername,txtEmail,txtPassword);
                }
            }
        });
    }
    private void register(String username,String email,String password){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser=auth.getCurrentUser();
                    assert firebaseUser !=null;
                    String userId=firebaseUser.getUid();

                    reference= FirebaseDatabase.getInstance().getReference("Users").child(userId);
                    HashMap<String , String> hashMap=new HashMap<>();
                    hashMap.put("id",userId);
                    hashMap.put("username",username);
                    hashMap.put("imageURL","default");
                    hashMap.put("status","offline");
                    hashMap.put("search",username.toLowerCase());

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

                }else{
                    Toast.makeText(RegisterActivity.this,
                            "Bạn không thể lập tài khoản với Email này", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}