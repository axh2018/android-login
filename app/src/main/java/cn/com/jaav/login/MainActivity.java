package cn.com.jaav.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * @author Bryant
 */
public class MainActivity extends AppCompatActivity
{
    private DatabaseHelper helper;
    private EditText username, password;
    private Button login;
    private TextView register;
    private CheckBox checkBox;
    private ImageView eye;
    private Intent intent;
    private Boolean flag;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Boolean eyeFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new DatabaseHelper(this, "User.db", null, 1);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (TextView) findViewById(R.id.register);
        checkBox = (CheckBox) findViewById(R.id.check_box);
        eye = (ImageView) findViewById(R.id.eye);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = pref.getBoolean("remember", false);
        if (isRemember){
            String name = pref.getString("name", "");
            String passwd = pref.getString("passwd", "");
            username.setText(name);
            password.setText(passwd);
            checkBox.setChecked(true);
        }
        flag = false;
        //显示或隐藏密码
        eye.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (eyeFlag == false){
                    eye.setImageResource(R.drawable.eye_open);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eyeFlag = true;
                }
                else {
                    eye.setImageResource(R.drawable.eye_close);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eyeFlag = false;
                }
            }
        });

        //登录，记住密码
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor cursor = db.query("user", null, null, null, null, null, null);
                if (cursor.moveToFirst()){
                    do {
                        String nameData = cursor.getString(cursor.getColumnIndex("name"));
                        String passwdData = cursor.getString(cursor.getColumnIndex("passwd"));
                        if (username.getText().toString().equals(nameData) && password.getText().toString().equals(passwdData)){
                            editor = pref.edit();
                            if (checkBox.isChecked()){
                                editor.putBoolean("remember", true);
                                editor.putString("name", username.getText().toString());
                                editor.putString("passwd", password.getText().toString());
                            }else {
                                editor.clear();
                            }
                            editor.apply();
                            flag = true;
                            Toast.makeText(MainActivity.this, "登录成功!", Toast.LENGTH_LONG).show();
                        }
                    }
                    while (cursor.moveToNext());
                }
                if (!flag){
                    Toast.makeText(MainActivity.this, "登录失败!", Toast.LENGTH_LONG).show();
                }
                cursor.close();
            }
        });
        //注册事件

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });
    }
}