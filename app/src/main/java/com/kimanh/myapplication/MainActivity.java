package com.kimanh.myapplication;

import static android.Manifest.permission_group.CAMERA;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import com.kimanh.myapplication.helper.DatabaseHelper;
import com.kimanh.myapplication.helper.MyListAdapter;
import com.kimanh.myapplication.model.Food;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {
    DatabaseHelper mydb;
    ArrayList<Food> arrayListFood=null;//chứa tất cả các phần tử trong csdl
    MyListAdapter adapter=null; //adapter custom
    private ArrayList<Food> FoodCheckedItemList = new ArrayList<Food>();
    SearchView searchView;

    private ListView listfood = null;
    CircleImageView croppedImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mydb = new DatabaseHelper(this);

        searchView  = findViewById(R.id.search);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchView);
//        searchView.setQueryHint("Tìm món ăn tại đây!");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getFoods(newText);
                return false;
            }


        });
        arrayListFood=new ArrayList<>();//Khởi tạo mảng lưu các đối tượng

        listfood = findViewById(R.id.listfood); // ánh xạ từ listview sang Java
        Cursor cursor =mydb.Showdata(); //đổ dữ liệu từ trong sqlite ra cursor

        while (cursor.moveToNext()) {
            Food food = new Food(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),cursor.getString(5));
            arrayListFood.add(food);
        }
        //Truyen arrayadaper qua constructor
        adapter = new MyListAdapter(this, arrayListFood);//gán data mảng vào adapter mà mình custom
        listfood.setAdapter(adapter);
        listfood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                CheckBox itemCheckbox = (CheckBox) view.findViewById(R.id.food_list_item_checkbox);
                boolean checkboxChecked = false;
                if(itemCheckbox.isChecked())
                {
                    itemCheckbox.setChecked(false);
                    checkboxChecked = false;
                }else
                {
                    itemCheckbox.setChecked(true);
                    checkboxChecked = true;
                }

                Food food= new Food();
                food.setMamonan(arrayListFood.get(position).getMamonan());
                food.setTenmonan(arrayListFood.get(position).getTenmonan());
                food.setDiachi(arrayListFood.get(position).getDiachi());
                food.setChongoi(arrayListFood.get(position).getChongoi());
                food.setLoai(arrayListFood.get(position).getLoai());
                food.setAnhmonan(arrayListFood.get(position).getAnhmonan());

                Toast.makeText(getApplicationContext(),"Chọn mã món ăn: " +arrayListFood.get(position).getMamonan() , Toast.LENGTH_SHORT).show();
                addCheckListItem(food,checkboxChecked);

            }


        });
        listfood.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showD(i);
                return false;
            }
        });



    }
    private void showD(int i) {
        final CharSequence[] options = {"Sửa", "Xoá", "Thoát"};
        // chú ý this
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Thông báo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Sửa")) {
                    if (FoodCheckedItemList != null) {
                        int size = FoodCheckedItemList.size();
                        {
                            Food tmpDto = FoodCheckedItemList.get(0);
                            // AddInforActivity.start(getApplicationContext(), tmpDto.getId(), tmpDto.getUserName(), tmpDto.getPassword(), tmpDto.getEmail());
                            Intent intent = new Intent(getApplicationContext(), AddInforActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("MAMONAN", tmpDto.getMamonan());
                            intent.putExtra("TENMONAN", tmpDto.getTenmonan());
                            intent.putExtra("DIACHI", tmpDto.getDiachi());
                            intent.putExtra("CHONGOI", tmpDto.getChongoi());
                            intent.putExtra("LOAI", tmpDto.getLoai());
                            intent.putExtra("ANH", tmpDto.getAnhmonan());
                            intent.putExtra("Flag", "EDIT");
                            startActivity(intent);

                        }
                    }

                } else if (options[item].equals("Xoá")) {
                    Integer delete = mydb.delete(arrayListFood.get(i).getMamonan());
                    Log.v("Xoa", "ok");
                    if (delete > 0) {
                        Toast.makeText(MainActivity.this, "Xoá thành công!", Toast.LENGTH_SHORT).show();
                        arrayListFood.remove(i);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MainActivity.this, "Xoá không thành công!", Toast.LENGTH_SHORT).show();
                    }
                } else if (options[item].equals("Thoát")) {
                    dialog.dismiss();
                }

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private String getFoodsCheckedItemIds()
    {
        StringBuffer retBuf = new StringBuffer();
        if(FoodCheckedItemList !=null)
        {
            int size = FoodCheckedItemList.size();
            for(int i=0;i<size;i++)
            {
                Food tmpDto = FoodCheckedItemList.get(i);
                retBuf.append(tmpDto.getMamonan());
                retBuf.append(" ");
            }
        }
        return retBuf.toString().trim();
    }
    private void addCheckListItem(Food userAccountDto, boolean add)
    {

        if(FoodCheckedItemList != null)
        {
            boolean accountExist = false;
            int existPosition = -1;
            // Loop to check whether the user account dto exist or not.
            int size = FoodCheckedItemList.size();
            for(int i=0;i<size;i++)
            {
                Food tmpDto = FoodCheckedItemList.get(i);
                if(tmpDto.getMamonan().equals(userAccountDto.getMamonan()))
                {
                    accountExist = true;
                    existPosition = i;
                    break;
                }
            }
            if(add)
            {
                // If not exist then add it.
                if(!accountExist)
                {
                    FoodCheckedItemList.add(userAccountDto);
                }
            }else
            {
                // If exist then remove it.
                if(accountExist)
                {
                    if(existPosition!=-1)
                    {
                        FoodCheckedItemList.remove(existPosition);
                    }
                }
            }
        }
    }
    //Cho biết là tham chiếu đến menu mình đã tạo actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        Log.v("1","1");

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId=item.getItemId();//lấy tất cả các item mà đã đạt trong item_actionbar
        if(itemId==R.id.menu_add){ //người dùng nút cộng
            Log.v("add","ok");
            //1 Mở cửa sổ InfoSVActivity
            Intent in=new Intent(this,AddInforActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            in.putExtra("Flag", "ADD");
            startActivity(in);
        } else if(itemId==R.id.menu_delete){
            if(FoodCheckedItemList != null){
                int size = FoodCheckedItemList.size();
                if(size == 0){
                    Toast.makeText(this,"Chọn ít nhất 1 phần tử:",Toast.LENGTH_SHORT).show();
                }else {
                    for (int i=0; i<size; i++) {
                        Food tmOto = FoodCheckedItemList.get(i);
                        Integer delete = mydb.delete(tmOto.getMamonan());
                        if (delete > 0) {
                            FoodCheckedItemList.remove(i);
                            size = FoodCheckedItemList.size();
                            Toast.makeText(this, "Xoá thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Xoá khong thành công!", Toast.LENGTH_SHORT).show();
                        }
                        i--;
                    }
                    arrayListFood.clear();
                    arrayListFood=getAllFoods(mydb);
                    adapter = new MyListAdapter(this, arrayListFood);
                    listfood.setAdapter(adapter);

                }

            }
        }
        else if(itemId==R.id.menu_edit){

            if(FoodCheckedItemList!=null)
            {
                int size = FoodCheckedItemList.size();
                if(size!=1)
                {
                    Toast.makeText(this, "Chọn 1 dòng để chỉnh sửa:", Toast.LENGTH_SHORT).show();
                }else
                {
                    Food tmpDto = FoodCheckedItemList.get(0);
                    // AddInforActivity.start(getApplicationContext(), tmpDto.getId(), tmpDto.getUserName(), tmpDto.getPassword(), tmpDto.getEmail());
                    Intent intent = new Intent(getApplicationContext(), AddInforActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("MAMONAN", tmpDto.getMamonan());
                    intent.putExtra("TENMONAN", tmpDto.getTenmonan());
                    intent.putExtra("DIACHI", tmpDto.getDiachi());
                    intent.putExtra("CHONGOI", tmpDto.getChongoi());
                    intent.putExtra("LOAI", tmpDto.getLoai());
                    intent.putExtra("ANH", tmpDto.getAnhmonan());

                    intent.putExtra("Flag", "EDIT");
                    startActivity(intent);
                }
            }
        }else if(itemId==R.id.menu_logout){

            Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
            Toast.makeText(this, "Đăng xuất thành công.", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public ArrayList<Food> getAllFoods(DatabaseHelper mydb ){

        ArrayList<Food> foods = new ArrayList<>();

        Cursor cursor = mydb.Showdata();
        while (cursor.moveToNext()){
            Food food = new Food(cursor.getString(0), cursor.getString(1), cursor.getString(2),cursor.getString(3), cursor.getString(4),cursor.getString(5));
            foods.add(food);
        }
        return foods;
    }
    private void getFoods(String searchItem){
        arrayListFood.clear();
        mydb = new DatabaseHelper(this);
        Cursor cursor = mydb.searchFoods(searchItem);
        while (cursor.moveToNext()){
            String id = cursor.getString(0);
            String ten = cursor.getString(1);
            String diachi = cursor.getString(2);
            String chongoi= cursor.getString(3);
            String loai = cursor.getString(4);
            String anh = cursor.getString(5);

            Food f = new Food();
            f.setMamonan(id);
            f.setTenmonan(ten);
            f.setChongoi(chongoi);
            f.setDiachi(diachi);
            f.setAnhmonan(anh);
            f.setLoai(loai);

            arrayListFood.add(f);
        }
        listfood.setAdapter(adapter);
    }
}