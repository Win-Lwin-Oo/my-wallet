package com.winlwinoo.mywallet.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.winlwinoo.mywallet.DataAdapter.CustomGridAdapter;
import com.winlwinoo.mywallet.DataAdapter.CustomRecyclerAdapter;
import com.winlwinoo.mywallet.DataModel.CategoryData;
import com.winlwinoo.mywallet.DataModel.ItemData;
import com.winlwinoo.mywallet.Database.MyWalletDatabase;
import com.winlwinoo.mywallet.R;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment implements View.OnClickListener {
    GridView category_list_grid;
    List<CategoryData> categoryDataList;
    TextView hidden_date,date_title,top_category_title,date_category_title,category_balance_title,all_total_usage_amount,all_item_usage_amount,no_usage_for_today;
    MyWalletDatabase myWalletDatabase;
    private Boolean isFirstTime;
    private ImageView add_new_category, range_date, add_new_item, item_range_date;
    private List<ItemData> itemDataList;

    RelativeLayout main_top_layout,category_detail_layout,category_grid_layout;

    Bitmap bitmap,a_watt,foods,eat_rice,home_pay,health_pay,exercise_pay,cosmetic_pay,school_pay,phone_bill,entertainment,travel_pay;
    RecyclerView item_list_recycler;

    int total_usage_amount=0, total_item_usage_amount=0;
    String date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        myWalletDatabase = new MyWalletDatabase(getContext());

        category_list_grid = view.findViewById(R.id.category_list_grid);
        date_title = view.findViewById(R.id.date_title);
        add_new_category = view.findViewById(R.id.add_new_category);
        range_date = view.findViewById(R.id.range_date);
        item_range_date = view.findViewById(R.id.item_range_date);
        add_new_item = view.findViewById(R.id.add_new_item);
        main_top_layout = view.findViewById(R.id.main_top_layout);
        category_detail_layout = view.findViewById(R.id.category_detail_layout);
        top_category_title = view.findViewById(R.id.top_category_title);
        date_category_title = view.findViewById(R.id.date_category_title);
        category_balance_title = view.findViewById(R.id.category_balance_title);
        all_total_usage_amount = view.findViewById(R.id.all_total_usage_amount);
        all_item_usage_amount = view.findViewById(R.id.all_item_usage_amount);
        item_list_recycler = view.findViewById(R.id.item_list_recycler);
        category_grid_layout = view.findViewById(R.id.category_grid_layout);
        no_usage_for_today = view.findViewById(R.id.no_usage_for_today);
        hidden_date = view.findViewById(R.id.hidden_date);

        item_list_recycler.setHasFixedSize(true);
        item_list_recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        add_new_category.setOnClickListener(this);
        range_date.setOnClickListener(this);
        add_new_item.setOnClickListener(this);
        item_range_date.setOnClickListener(this);

        //get current date
        date = new SimpleDateFormat("d/MM/yyyy", Locale.getDefault()).format(new Date());
        date_title.setText("Report on : "+date);
        date_category_title.setText("Report on : "+date);
        hidden_date.setText(date);

        categoryDataList = new ArrayList<>();
        itemDataList = new ArrayList<>();

        generateCategoryData();
        total_usage_amount = myWalletDatabase.getTotalUsageAmount(date);
        all_total_usage_amount.setText(currencyFormat(total_usage_amount)+" ");
        //Toast.makeText(getContext(),"Amount "+total_usage_amount,Toast.LENGTH_SHORT).show();
        return view;
    }

    private void generateCategoryData() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        isFirstTime = sharedPreferences.getBoolean("isFirstTime",true);

        if (isFirstTime){

            bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.ic_noti);
            a_watt = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.a_watts);
            foods = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.foods);
            eat_rice = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.eate_rice);
            home_pay = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.home_pay);
            health_pay = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.health);
            exercise_pay = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.exercise);
            cosmetic_pay = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.cosmetic);
            school_pay = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.school);
            phone_bill = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.phone_bill_pay);
            entertainment = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.entertainment);
            travel_pay = BitmapFactory.decodeResource(getContext().getResources(),
                    R.mipmap.travel_pay);

            myWalletDatabase.insertCategory("ဖုန်းဘေ",getByte(phone_bill));
            myWalletDatabase.insertCategory("ထမင်းစား",getByte(eat_rice));
            myWalletDatabase.insertCategory("မုန့်စား",getByte(foods));
            myWalletDatabase.insertCategory("အဝတ်စား",getByte(a_watt));
            myWalletDatabase.insertCategory("နေစားရိတ်",getByte(home_pay));
            myWalletDatabase.insertCategory("ကျန်းမာရေး",getByte(health_pay));
            myWalletDatabase.insertCategory("အပျော်အပါး",getByte(entertainment));
            myWalletDatabase.insertCategory("အားကစား",getByte(exercise_pay));
            myWalletDatabase.insertCategory("အလှကုန်",getByte(cosmetic_pay));
            myWalletDatabase.insertCategory("ခရီးသွား",getByte(travel_pay));

            //Toast.makeText(getContext(),"Start :"+myWalletDatabase.getAllCategoryByDate(date).size(),Toast.LENGTH_SHORT).show();
            if (myWalletDatabase.getAllCategoryByDate(date).size()==0){
                category_grid_layout.setVisibility(View.INVISIBLE);
                no_usage_for_today.setVisibility(View.VISIBLE);
            }else {
                no_usage_for_today.setVisibility(View.INVISIBLE);
                category_grid_layout.setVisibility(View.VISIBLE);
            }
            categoryDataList = myWalletDatabase.getAllCategoryByDate(date);

            editor.putBoolean("isFirstTime",false);
            editor.commit();
        }else {
            if (myWalletDatabase.getAllCategoryByDate(date).size()==0){
                category_grid_layout.setVisibility(View.INVISIBLE);
                no_usage_for_today.setVisibility(View.VISIBLE);
            }else {
                no_usage_for_today.setVisibility(View.INVISIBLE);
                category_grid_layout.setVisibility(View.VISIBLE);
            }
            categoryDataList = myWalletDatabase.getAllCategoryByDate(date);
        }

        CustomGridAdapter customGridAdapter = new CustomGridAdapter(getContext(),categoryDataList);
        category_list_grid.setAdapter(customGridAdapter);

        category_list_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                generateItemData(categoryDataList.get(position).getCategory_name(),hidden_date.getText().toString());

                main_top_layout.setVisibility(View.INVISIBLE);
                category_detail_layout.setVisibility(View.VISIBLE);
                top_category_title.setText(categoryDataList.get(position).getCategory_name());
                //category_balance_title.setText("Usage on : "+categoryDataList.get(position).getCategory_name());
                //Toast.makeText(getContext(),categoryDataList.get(position).getCategory_name(),Toast.LENGTH_SHORT).show();

                saveCurrentCategoryName(categoryDataList.get(position).getCategory_name());


            }
        });
    }

    public void generateItemData(String current_category,String current_date) {
        //Toast.makeText(getContext(),"Current date "+current_date,Toast.LENGTH_SHORT).show();

        total_item_usage_amount = myWalletDatabase.getItemUsageAmount(current_category,current_date);
        all_item_usage_amount.setText(currencyFormat(total_item_usage_amount)+" ");
        //Toast.makeText(getContext(),"Amount "+total_item_usage_amount,Toast.LENGTH_SHORT).show();

        itemDataList.clear();
        itemDataList.addAll(myWalletDatabase.getAllItemDataCategory(current_category,current_date));
        CustomRecyclerAdapter customRecyclerAdapter = new CustomRecyclerAdapter(getContext(),itemDataList);
        item_list_recycler.setAdapter(customRecyclerAdapter);
        customRecyclerAdapter.notifyDataSetChanged();
    }

    private void saveCurrentCategoryName(String category_name) {
        SharedPreferences preferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("currentCategory", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("category_name",category_name);
        editor.commit();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_new_category:
                showAddNewItemByCategory();
                break;
            case R.id.range_date:
                showDatePickerForTotalCategory();
                break;
            case R.id.add_new_item:
                showAddNewItemDialog();
                break;
            case R.id.item_range_date:
                showDatePickerForTotalItem();
                break;
        }
    }

    private void showDatePickerForTotalItem() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        final AlertDialog alertDialog;
        final View dialogView = getLayoutInflater().inflate(R.layout.date_picker_dialog,null);

        alertBuilder.setView(dialogView);
        alertDialog = alertBuilder.show();
        alertDialog.setCancelable(false);

        final DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
        Calendar calendar = Calendar.getInstance();
        final Boolean[] isDateChanged = {false};
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MARCH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                isDateChanged[0] = true;
            }
        });
        TextView cancel = dialogView.findViewById(R.id.cancel_date);
        TextView ok = dialogView.findViewById(R.id.ok_date);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDateChanged[0]){
                    String selected_date=datePicker.getDayOfMonth()+"/"+((datePicker.getMonth()+1))+"/"+datePicker.getYear();
                    date_title.setText("Report on : "+selected_date);
                    date_category_title.setText("Report on : "+selected_date);
                    hidden_date.setText(selected_date);

                    String current_category = readCurrentCategory();
                    generateItemData(current_category,selected_date);
                }else {
                    String date = new SimpleDateFormat("d/MM/yyyy", Locale.getDefault()).format(new Date());
                    date_category_title.setText("Report on : "+date);

                    String current_category = readCurrentCategory();
                    generateItemData(current_category,date);
                }

                alertDialog.dismiss();
                //Toast.makeText(getContext(), "Selected date : "+ selected_date[0], Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePickerForTotalCategory() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        final AlertDialog alertDialog;
        final View dialogView = getLayoutInflater().inflate(R.layout.date_picker_dialog,null);

        alertBuilder.setView(dialogView);
        alertDialog = alertBuilder.show();
        alertDialog.setCancelable(false);

        final DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
        Calendar calendar = Calendar.getInstance();
        final boolean[] isDateChanged = {false};
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MARCH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                isDateChanged[0] = true;
            }
        });
        TextView cancel = dialogView.findViewById(R.id.cancel_date);
        TextView ok = dialogView.findViewById(R.id.ok_date);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDateChanged[0]){
                    String selected_date=datePicker.getDayOfMonth()+"/"+((datePicker.getMonth()+1))+"/"+datePicker.getYear();
                    date_title.setText("Report on : "+selected_date);
                    hidden_date.setText(selected_date);

                    total_usage_amount = myWalletDatabase.getTotalUsageAmount(selected_date);
                    all_total_usage_amount.setText(currencyFormat(total_usage_amount)+" ");

                    if (myWalletDatabase.getAllCategoryByDate(selected_date).size()==0){
                        category_grid_layout.setVisibility(View.INVISIBLE);
                        no_usage_for_today.setVisibility(View.VISIBLE);
                    }else {
                        no_usage_for_today.setVisibility(View.INVISIBLE);
                        category_grid_layout.setVisibility(View.VISIBLE);
                    }
                    categoryDataList = myWalletDatabase.getAllCategoryByDate(selected_date);

                    CustomGridAdapter customGridAdapter = new CustomGridAdapter(getContext(),categoryDataList);
                    category_list_grid.setAdapter(customGridAdapter);

                }else {
                    String date = new SimpleDateFormat("d/MM/yyyy", Locale.getDefault()).format(new Date());
                    date_title.setText("Report on : "+date);
                    hidden_date.setText(date);

                    total_usage_amount = myWalletDatabase.getTotalUsageAmount(date);
                    all_total_usage_amount.setText(currencyFormat(total_usage_amount)+" ");

                    if (myWalletDatabase.getAllCategoryByDate(date).size()==0){
                        category_grid_layout.setVisibility(View.INVISIBLE);
                        no_usage_for_today.setVisibility(View.VISIBLE);
                    }else {
                        no_usage_for_today.setVisibility(View.INVISIBLE);
                        category_grid_layout.setVisibility(View.VISIBLE);
                    }

                    categoryDataList = myWalletDatabase.getAllCategoryByDate(date);
                    CustomGridAdapter customGridAdapter = new CustomGridAdapter(getContext(),categoryDataList);
                    category_list_grid.setAdapter(customGridAdapter);

                }
                alertDialog.dismiss();
            }
        });
    }

    private void showAddNewItemByCategory() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        final AlertDialog alertDialog;
        final View dialogView = getLayoutInflater().inflate(R.layout.add_new_item_by_category,null);

        alertBuilder.setView(dialogView);
        alertDialog = alertBuilder.show();
        alertDialog.setCancelable(false);

        List<CategoryData> categoryDataArrayList = new ArrayList<>();
        categoryDataArrayList = myWalletDatabase.getAllCategory();

        String [] category_list = new String[categoryDataArrayList.size()];
        for (int i=0 ; i<categoryDataArrayList.size() ; i++){
            category_list[i] = categoryDataArrayList.get(i).getCategory_name();
        }

        final Spinner category_list_spinner = dialogView.findViewById(R.id.category_list_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,category_list);
        category_list_spinner.setAdapter(adapter);

        ImageView cancel_btn = dialogView.findViewById(R.id.item_cancel_btn_by_category);
        TextView save_new_item = dialogView.findViewById(R.id.save_new_item_by_category);
        TextView today_date = dialogView.findViewById(R.id.today_date_by_category);
        final EditText new_usage_amount = dialogView.findViewById(R.id.new_usage_amount_by_category);
        final EditText new_usage_name = dialogView.findViewById(R.id.new_usage_name_by_category);
        //final String date_data = new SimpleDateFormat("d/MM/yyyy", Locale.getDefault()).format(new Date());
        today_date.setText(hidden_date.getText().toString());

        save_new_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int usage_amount = Integer.parseInt(new_usage_amount.getText().toString().trim());
                    String usage_name = new_usage_name.getText().toString().trim();

                    if (usage_amount == 0 || new_usage_amount.getText().toString().trim().isEmpty() || new_usage_amount.getText().toString().trim().matches("") ){
                        new_usage_amount.setError("Invalid amount");
                    }else if (usage_name.isEmpty() || usage_name.matches("")){
                        new_usage_name.setError("Invalid");
                    }else {

                        String current_category = category_list_spinner.getSelectedItem().toString();
                        //read shared preferences

                        myWalletDatabase.insertItem(usage_name, current_category.trim(),usage_amount,hidden_date.getText().toString());
                        /*int count = myWalletDatabase.getCurrentItemCount(current_category)+1;
                        myWalletDatabase.updateItemCount(current_category,count);*/


                        generateItemData(current_category,hidden_date.getText().toString());
                        restartFragment();
                        //Toast.makeText(getContext(), "Data : "+usage_name+"\n"+current_category+"\n"+usage_amount+"\n"+date, Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("ERROR => ",e.getMessage());
                }
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    private void showAddNewItemDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        final AlertDialog alertDialog;
        final View dialogView = getLayoutInflater().inflate(R.layout.add_new_item,null);

        alertBuilder.setView(dialogView);
        alertDialog = alertBuilder.show();
        alertDialog.setCancelable(false);

        ImageView item_cancel_btn = dialogView.findViewById(R.id.item_cancel_btn);
        TextView save_new_item = dialogView.findViewById(R.id.save_new_item);
        TextView today_date = dialogView.findViewById(R.id.today_date);
        final EditText new_usage_amount = dialogView.findViewById(R.id.new_usage_amount);
        final EditText new_usage_name = dialogView.findViewById(R.id.new_usage_name);
        //final String date_item = new SimpleDateFormat("d/MM/yyyy", Locale.getDefault()).format(new Date());
        today_date.setText(hidden_date.getText().toString());

        save_new_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int usage_amount = Integer.parseInt(new_usage_amount.getText().toString().trim());
                    String usage_name = new_usage_name.getText().toString().trim();

                    if (usage_amount == 0 || new_usage_amount.getText().toString().trim().isEmpty() || new_usage_amount.getText().toString().trim().matches("") ){
                        new_usage_amount.setError("Invalid amount");
                    }else if (usage_name.isEmpty() || usage_name.matches("")){
                        new_usage_name.setError("Invalid");
                    }else {

                        //read shared preferences
                        String current_category = readCurrentCategory();

                        myWalletDatabase.insertItem(usage_name,current_category.trim(),usage_amount,hidden_date.getText().toString());
                        /*int count = myWalletDatabase.getCurrentItemCount(current_category)+1;
                        myWalletDatabase.updateItemCount(current_category,count);*/

                        generateItemData(current_category,hidden_date.getText().toString());
                        //Toast.makeText(getContext(), "Data : "+usage_name+"\n"+current_category+"\n"+usage_amount+"\n"+date, Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("ERROR => ",e.getMessage());
                }
            }
        });

        item_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }

    private String readCurrentCategory(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("currentCategory",Context.MODE_PRIVATE);
        String defaultValue = "DefaultValue";
        String current_category = sharedPreferences.getString("category_name",defaultValue);
        return current_category;
    }

    private void addNewCategoryName(String new_category_name) {

        bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.ic_noti);

        myWalletDatabase.insertCategory(new_category_name.trim(),getByte(bitmap));
        restartFragment();
    }

    //image to byte
    public static byte[] getByte(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,stream);
        return stream.toByteArray();
    }

    private void restartFragment(){
        HomeFragment fragment = (HomeFragment) getFragmentManager().findFragmentById(R.id.nav_frame);
        getFragmentManager().beginTransaction()
                .detach(fragment)
                .attach(fragment)
                .commit();
    }

    private String currencyFormat(int currency_amount){
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
        String currency_format = decimalFormat.format(currency_amount);
        return currency_format;
    }
}
