package com.winlwinoo.mywallet.DataAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.winlwinoo.mywallet.DataModel.ItemData;
import com.winlwinoo.mywallet.Database.MyWalletDatabase;
import com.winlwinoo.mywallet.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder> {

    private Context context;
    private List<ItemData> itemDataList = new ArrayList<>();

    public CustomRecyclerAdapter(Context context, List<ItemData> itemDataList) {
        this.context = context;
        this.itemDataList = itemDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        final ItemData itemData = itemDataList.get(i);

        myViewHolder.item_name.setText(itemData.getUsage_name());
        myViewHolder.item_date.setText(itemData.getUsage_date());
        myViewHolder.item_price.setText(currencyFormat(itemData.getUsage_amount())+" Ks");

    }

    @Override
    public int getItemCount() {
        return itemDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView item_name,item_date,item_price;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            item_name = itemView.findViewById(R.id.item_name);
            item_date = itemView.findViewById(R.id.item_date);
            item_price = itemView.findViewById(R.id.item_price);
        }
    }

    private String currencyFormat(int currency_amount){
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
        String currency_format = decimalFormat.format(currency_amount);
        return currency_format;
    }
}
