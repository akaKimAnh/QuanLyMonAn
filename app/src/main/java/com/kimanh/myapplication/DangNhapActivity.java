package com.kimanh.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kimanh.myapplication.helper.DatabaseHelper;

public class DangNhapActivity extends AppCompatActivity {
    EditText edTenDangNhapDN, edMatKhauDN;
    CheckBox ckRemember;
    Button btnDongYDN, btnDK;
    DatabaseHelper databaseHelper;

    SharedPreferences pref;// khai báo
    SharedPreferences.Editor editor;//chỉnh sửa dữ liệu
    public static final String MyPREFERENCES="MYPREFS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangnhap_activity);

        edTenDangNhapDN = findViewById(R.id.edTenDangNhapDN);
        edMatKhauDN = findViewById(R.id.edMatKhauDN);
        ckRemember = findViewById(R.id.ckRemember);
        btnDongYDN = findViewById(R.id.btnDongYDN);
        btnDK = findViewById(R.id.btnDK);

        databaseHelper = new DatabaseHelper(this);

        pref=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //Đọc thông tin từ share ra
        String tendangnhap=pref.getString("tendangnhap","");
        String matkhau=pref.getString("matkhau","");
        Log.v("username",tendangnhap);
        if (!tendangnhap.equals("")){
            edTenDangNhapDN.setText(tendangnhap);
            edMatKhauDN.setText(matkhau);

        }

        btnDongYDN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tendangnhap = edTenDangNhapDN.getText().toString();
                String matkhau = edMatKhauDN.getText().toString();

                if (tendangnhap.equals("")||matkhau.equals(""))
                    Toast.makeText(DangNhapActivity.this, "Không được để trống!", Toast.LENGTH_SHORT).show();
                else {
                    Boolean checkTendangnhapMatkhau = databaseHelper.checkTendangnhapMatkhau(tendangnhap, matkhau);

                    if (checkTendangnhapMatkhau == true){
                        if(ckRemember.isChecked()){
                            //lưu thông tin xuống sharepreferences
                            editor=pref.edit(); //chỉnh sửa file  MYPREFS.xml
                            editor.putString("tendangnhap",tendangnhap); //ghi thông tin vào fields USERNAME='admin'
                            editor.putString("matkhau",matkhau);
                            editor.commit();
                        }else
                        {
                            //xóa preferences
                            editor=pref.edit();
                            editor.clear();
                            editor.commit();
                        }
                        Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                    }else {
                        Toast.makeText(DangNhapActivity.this, "Đăng nhập thất bại, vui lòng nhập lại tên đăng nhâp và mật khẩu!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        btnDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taotaikhoan();
            }

            private void taotaikhoan() {
                Intent i = new Intent(DangNhapActivity.this,DangKiActivity.class);
                startActivity(i);
            }
        });

    }

}
