package com.kimanh.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kimanh.myapplication.helper.DatabaseHelper;

public class DangKiActivity extends AppCompatActivity {
    EditText edTenDangNhapDK, edMatKhauDK, edMatKhauDK2;
    Button btnDongYDK;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangki_activity);

        edTenDangNhapDK = findViewById(R.id.edTenDangNhapDK);
        edMatKhauDK = findViewById(R.id.edMatKhauDK);
        edMatKhauDK2 = findViewById(R.id.edMatKhauDK2);
        btnDongYDK = findViewById(R.id.btnDongYDK);

        databaseHelper = new DatabaseHelper(this);

        btnDongYDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tendangnhap = edTenDangNhapDK.getText().toString();
                String matkhau = edMatKhauDK.getText().toString();
                String nhaplaimatkhau = edMatKhauDK2.getText().toString();

                if (tendangnhap.equals("")|| matkhau.equals("")|| nhaplaimatkhau.equals(""))
                    Toast.makeText(DangKiActivity.this, "Không được để trống!", Toast.LENGTH_SHORT).show();
                else {
                    if (matkhau.equals(nhaplaimatkhau)){
                        Boolean checkTendangnhapMatkhau = databaseHelper.checkTendangnhap(tendangnhap);

                        if(checkTendangnhapMatkhau == false){
                            Boolean insert = databaseHelper.insertDataUser(tendangnhap, matkhau);

                            if (insert == true){
                                Toast.makeText(DangKiActivity.this, "Đăng kí thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(DangKiActivity.this, "Đăng kí không thành công!", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(DangKiActivity.this, "Tên tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(DangKiActivity.this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}