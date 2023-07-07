package com.kimanh.myapplication;


import static android.Manifest.permission_group.CAMERA;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.kimanh.myapplication.helper.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class AddInforActivity extends AppCompatActivity {
    Spinner spinner;
    ArrayList<String> arraylistLoai;
    String loai="";
    EditText txtmamonan,txttenmonan,txtdiachi;
    Button btnluu,btnlamlai;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    DatabaseHelper mydb=null;
    Bitmap myBitmap;
    Uri picUri;

    CircleImageView croppedImageView;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 107;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_food);
        mydb=new DatabaseHelper(this);
        btnluu=(Button)findViewById(R.id.btnluu);
        btnlamlai=(Button)findViewById(R.id.btnlamlai);
        txtmamonan=(EditText)findViewById(R.id.txtmamonan);
        txttenmonan= findViewById(R.id.txttenmonan);
        txtdiachi= findViewById(R.id.txtdiachi);
        radioSexGroup=(RadioGroup)findViewById(R.id.radiogroupsex);
        croppedImageView=(CircleImageView)findViewById(R.id.img_profile);
        spinner =findViewById(R.id.spinnerloai);
        txtmamonan.requestFocus();

        //b1.
        arraylistLoai=new ArrayList<String>();
        arraylistLoai.add("Món khô");
        arraylistLoai.add("Món Nước");
        arraylistLoai.add("Buffet");
        arraylistLoai.add("Nước uống");
        //adapter
        ArrayAdapter<String> adapterloai =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arraylistLoai);
        adapterloai.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterloai);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                loai = arraylistLoai.get(position);
                Log.v("loai",loai);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                loai = "";
               Log.v("loai",loai);
            }
        });



        //get du lieu tu MainActivity
        Intent intent = getIntent();

        //final int userID.getStringExtra("MaSv")
        String mamonan = intent.getStringExtra("MAMONAN");
        String tenmonan = intent.getStringExtra("TENMONAN");
        String diachi = intent.getStringExtra("DIACHI");
        String chongoi = intent.getStringExtra("CHONGOI");
        String loaimonan = intent.getStringExtra("LOAI");
        String anh = intent.getStringExtra("ANH");
//       croppedImageView.setImageBitmap(StringToBitMap(anh));//Câu lệnh để lấy hình ảnh dạng chuỗi chuyển sang bitmap và gán ra IMageView
        String flag = intent.getStringExtra("Flag");
        if (flag.equals("EDIT")) {

            if (mamonan != null) {
                if (!mamonan.equals("")) {
                    croppedImageView.setImageBitmap(StringToBitMap(anh));//Câu lệnh để lấy hình ảnh dạng chuỗi chuyển sang bitmap và gán ra IMageView
                    txtmamonan.setText(mamonan);
                    txtmamonan.setEnabled(false);
                    txttenmonan.setText(tenmonan);
                    txtdiachi.setText(diachi);
                    if (chongoi.equalsIgnoreCase("Ở Lại")) {
                        radioSexButton = findViewById(R.id.radioButtonOLai);
                        radioSexGroup.check(radioSexButton.getId());
                    } else {
                        radioSexButton = findViewById(R.id.radioButtonMangDi);
                        radioSexGroup.check(radioSexButton.getId());
                    }
                }
                selectValue(spinner, loaimonan);

            }
        }
        btnluu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag.equals("ADD")){
                    int selectedId = radioSexGroup.getCheckedRadioButtonId();
                    radioSexButton = (RadioButton) findViewById(selectedId);

                    String str = "";
                    if (myBitmap != null) {
                        str= BitMapToString(myBitmap);
                    } else {
                        str = "";
                    }
                    Log.v("bitmapttttt", "" + str);

                    Boolean Inserted = mydb.insertData(txtmamonan.getText().toString(), txttenmonan.getText().toString(),txtdiachi.getText().toString() ,radioSexButton.getText().toString(),loai,str);
                    if (Inserted) {
                        Toast.makeText(AddInforActivity.this, "Dữ liệu thêm thành công!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(AddInforActivity.this, "Dữ liệu thêm không thành công!", Toast.LENGTH_SHORT).show();
                    }

                    finish();// đóng cửa hiện tại
                    Intent in = new Intent(getApplicationContext(), MainActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //xóa đi cửa sổ parent
                    startActivity(in);//mở lên cửa sổ MainActivity
                }else if (flag.equals("EDIT")){


                    int selectedId = radioSexGroup.getCheckedRadioButtonId();
                    radioSexButton = (RadioButton) findViewById(selectedId);

                    String str = "";
                    if (myBitmap != null) {
                        str= BitMapToString(myBitmap);
                    } else {
                        str = anh;
                    }
                    Log.v("bitmapttttt", "" + str);
                    Boolean Updated = mydb.update(txtmamonan.getText().toString(), txttenmonan.getText().toString(),txtdiachi.getText().toString(), radioSexButton.getText().toString(), loai, str);
                    if (Updated) {
                        Toast.makeText(AddInforActivity.this, "Dữ liệu cập nhập thành công!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(AddInforActivity.this, "Dữ liệu cập nhập không thành công!", Toast.LENGTH_SHORT).show();
                    }


                    finish();// đóng cửa hiện tại
                    Intent in = new Intent(getApplicationContext(), MainActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //xóa đi cửa sổ parent
                    startActivity(in);//mở lên cửa sổ MainActivity
                }

            }

        });
        btnlamlai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txttenmonan.setText("");
                txttenmonan.requestFocus();
                txtdiachi.setText("");
                radioSexButton=(RadioButton)findViewById(R.id.radioButtonOLai);
                radioSexGroup.check(radioSexButton.getId());
                spinner.setSelection(0);

            }
        });
        //bat su kien khi click vao anh
        croppedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_layoutitem_food,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.replay){
            Intent in=new Intent(this,MainActivity.class);
            startActivity(in);
        }

        return super.onOptionsItemSelected(item);
    }
    private void selectValue(Spinner spinner, Object value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
    //ham dialog
    private void selectImage(){
        final CharSequence [] options = {"Chụp Ảnh","Chọn ảnh từ thư viện","Thoát"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddInforActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Chụp Ảnh"))
                {
                    permissions.add(CAMERA);
                    permissionsToRequest = findUnAskedPermissions(permissions);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if (permissionsToRequest.size() > 0)
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, ALL_PERMISSIONS_RESULT);
                    }

                    startActivityForResult(getPickImageChooserIntent(), 200);//dialog chup anh

                }
                else if (options[item].equals("Chọn ảnh từ thư viện"))
                {
                    permissions.add(CAMERA);
                    permissionsToRequest = findUnAskedPermissions(permissions);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (permissionsToRequest.size() > 0)
                            requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
                    }
                    startActivityForResult(getPickImageChooserIntentFile(), 200);//dialog chon anh tu file
                }
                else if (options[item].equals("Thoát")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    //ham hoi quyen
    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }
    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }
    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }
    public Intent getPickImageChooserIntent() {
        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();
        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();
        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
        }
        allIntents.add(0,captureIntent);
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);

        }
        Intent chooserIntent = Intent.createChooser(captureIntent, "Select source");
        //chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, captureIntent);

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
        Log.v("allIntents",""+allIntents.size());
        return chooserIntent;
    }
    //ham lay anh tu SDcard
    public Intent getPickImageChooserIntentFile() {
        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
        }
        Intent chooserIntent = Intent.createChooser(galleryIntent, "Select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
        Log.v("allIntents",""+allIntents.size());
        return chooserIntent;

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap;
        if (resultCode == Activity.RESULT_OK) {
            Log.v("picUri",""+data);
            if (getPickImageResultUri(data) != null) {
                picUri = getPickImageResultUri(data);
                // Log.v("picUri",""+picUri.getPath());
                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);//hình ảnh trong thư mục mà chụp
                    Log.v("myBitmap",""+myBitmap);
                    //   myBitmap = rotateImageIfRequired(myBitmap, picUri);
                    myBitmap = getResizedBitmap(myBitmap, 500); //nén ảnh lại

                    //Đoạn lệnh hiển thị ảnh lên circleimageview
                    croppedImageView.setImageBitmap(myBitmap);
                    //  imageView.setImageBitmap(myBitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {

                Log.v("picUri","null");
                bitmap = (Bitmap) data.getExtras().get("data");

                myBitmap = bitmap;

                if (croppedImageView != null) {
                    croppedImageView.setImageBitmap(myBitmap);
                }

                // imageView.setImageBitmap(myBitmap);

            }

        }

    }
    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }


        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    public static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public  static Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
