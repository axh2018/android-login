package cn.com.jaav.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author Bryant
 */
public class Register extends AppCompatActivity
{
    private EditText name, passwd;
    private DatabaseHelper helper;
    private Button realRegister;
    private ImageView eyeRegister;
    private Boolean eyeFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = (EditText) findViewById(R.id.name);
        passwd = (EditText)findViewById(R.id.passwd);
        realRegister = (Button) findViewById(R.id.real_register);
        eyeRegister = (ImageView) findViewById(R.id.eye_register);
        helper = new DatabaseHelper(this, "User.db", null, 1);

        //显示或隐藏密码
        eyeRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (eyeFlag == false){
                    eyeRegister.setImageResource(R.drawable.eye_open);
                    passwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eyeFlag = true;
                }
                else {
                    eyeRegister.setImageResource(R.drawable.eye_close);
                    passwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eyeFlag = false;
                }
            }
        });

        //注册事件
        realRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name", name.getText().toString());
                values.put("passwd", passwd.getText().toString());
                db.insert("user", null, values);
                values.clear();
                Toast.makeText(Register.this, "注册成功,即将跳转登录", Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(Register.this, MainActivity.class));
            }
        });
    }
}