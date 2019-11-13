package com.winlwinoo.mywallet.DataAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.winlwinoo.mywallet.DataModel.CategoryData;
import com.winlwinoo.mywallet.R;

import java.util.ArrayList;
import java.util.List;


public class CustomGridAdapter extends BaseAdapter {
    private Context context;
    private String name[];
    private String status[];
    private List<CategoryData> categoryData = new ArrayList<>();

    public CustomGridAdapter(Context context, String[] name , String[] status) {
        this.context = context;
        this.name = name;
        this.status = status;
    }

    public CustomGridAdapter(Context context, List<CategoryData> categoryData){
        this.context = context;
        this.categoryData = categoryData;
    }

    @Override
    public int getCount() {
        return categoryData.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        v = inflater.inflate(R.layout.custom_category_list,null);
        ImageView category_image = v.findViewById(R.id.category_image);
        TextView category_name = v.findViewById(R.id.category_name);
        TextView total_item_count = v.findViewById(R.id.total_item_count);
        RelativeLayout total_item_count_layout = v.findViewById(R.id.total_item_count_layout);

        category_name.setText(categoryData.get(position).getCategory_name());
        category_image.setImageBitmap(getImage(categoryData.get(position).getImage()));

        if (categoryData.get(position).getItem_count() !=0){
            total_item_count.setText(categoryData.get(position).getItem_count()+"");
            total_item_count_layout.setVisibility(View.VISIBLE);
        }
        return v;
    }

    //byte to image
    public static Bitmap getImage(byte[] image){
        return BitmapFactory.decodeByteArray(image,0,image.length);
    }
}
