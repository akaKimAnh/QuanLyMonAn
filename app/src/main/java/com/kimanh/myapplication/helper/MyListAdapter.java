package com.kimanh.myapplication.helper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kimanh.myapplication.R;
import com.kimanh.myapplication.model.Food;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;



public class MyListAdapter extends ArrayAdapter<Food> {

    private ArrayList<Food> listfood;

    private static class ViewHolder {
        TextView txtMamonan;
        TextView txtTenmonan;
        TextView txtDiachi;
        TextView txtChongoi;
        TextView txtLoai;
        CircleImageView anhmonan;
    }

    //ViewHolder.txtMamonan="001"
    public MyListAdapter(Activity context, ArrayList<Food> data) {
        super(context, R.layout.layout_item_food, data);
        // TODO Auto-generated constructor stub

        //listfood chứa 3 object
        this.listfood = data;
        Log.d("listfood", "" + listfood);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Food dataModel = getItem(position);
        ViewHolder viewHolder; //cục bộ
        final View result;
        //convertView biến quản lý cho biết các view đã xuất hiện hoặc tồn tại chưa
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            //khác null
            convertView = inflater.inflate(R.layout.layout_item_food, parent, false);
            //Ánh xạ và gán view vào biến của thộc tính
            viewHolder.txtMamonan = convertView.findViewById(R.id.item_txtmamonan);
            viewHolder.txtTenmonan = convertView.findViewById(R.id.item_txttenmonan);
            viewHolder.txtDiachi = convertView.findViewById(R.id.item_txtdiachi);
            viewHolder.txtChongoi = convertView.findViewById(R.id.item_txtchongoi);
            viewHolder.txtLoai = convertView.findViewById(R.id.item_txtloai);
            viewHolder.anhmonan = convertView.findViewById(R.id.imageViewmonan);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //viewHolder.txtMamonan,... khác null đều có nghĩa là nó đã cấp
        viewHolder.txtMamonan.setText(dataModel.getMamonan());
        viewHolder.txtTenmonan.setText(dataModel.getTenmonan());
        viewHolder.txtDiachi.setText(dataModel.getDiachi());
        viewHolder.txtChongoi.setText(dataModel.getChongoi());
        viewHolder.txtLoai.setText(dataModel.getLoai());
        viewHolder.anhmonan.setImageBitmap(StringToBitMap(dataModel.getAnhmonan()));
        return convertView;

    }

    ;

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}