package com.winlwinoo.mywallet.Fragment;


import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.winlwinoo.mywallet.DataModel.PieChartData;
import com.winlwinoo.mywallet.Database.MyWalletDatabase;
import com.winlwinoo.mywallet.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DashboardFragment extends Fragment implements View.OnClickListener {

    BarChart bar_chart;
    PieChart pie_chart;
    Button bar_chart_tab,pie_chart_tab;
    ImageView select_date_for_pie_chart;
    TextView date_dashboard_title,no_usage_for_today,hidden_date,dashboard_balance_amount;
    RelativeLayout bar_chart_layout,pie_chart_layout;
    MyWalletDatabase myWalletDatabase;
    List<PieChartData> pieChartDataList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        myWalletDatabase = new MyWalletDatabase(getContext());
        pieChartDataList = new ArrayList<>();

        bar_chart = view.findViewById(R.id.bar_chart);
        pie_chart = view.findViewById(R.id.pie_chart);

        bar_chart_tab = view.findViewById(R.id.bar_chart_tab);
        pie_chart_tab = view.findViewById(R.id.pie_chart_tab);

        bar_chart_layout = view.findViewById(R.id.bar_chart_layout);
        pie_chart_layout = view.findViewById(R.id.pie_chart_layout);

        select_date_for_pie_chart = view.findViewById(R.id.select_date_for_pie_chart);
        date_dashboard_title = view.findViewById(R.id.date_dashboard_title);
        no_usage_for_today = view.findViewById(R.id.no_usage_for_today);
        hidden_date = view.findViewById(R.id.hidden_date);
        dashboard_balance_amount = view.findViewById(R.id.dashboard_balance_amount);

        String date_str = new SimpleDateFormat("d-MMM-yyyy", Locale.getDefault()).format(new Date());
        date_dashboard_title.setText(date_str);
        String date = new SimpleDateFormat("d/MM/yyyy", Locale.getDefault()).format(new Date());
        hidden_date.setText(date);

        bar_chart_tab.setOnClickListener(this);
        pie_chart_tab.setOnClickListener(this);
        select_date_for_pie_chart.setOnClickListener(this);

        showBarChart();
        showPieChart(date);

        return view;
    }

    private void showPieChart(String selected_date) {
        int total_amount=0;
        pieChartDataList = myWalletDatabase.getPieChartData(selected_date);
        if (pieChartDataList.size() == 0){
            pie_chart_layout.setVisibility(View.INVISIBLE);
            no_usage_for_today.setVisibility(View.VISIBLE);
        }
        else {
            no_usage_for_today.setVisibility(View.INVISIBLE);
            pie_chart_layout.setVisibility(View.VISIBLE);

            pie_chart.setUsePercentValues(true);
            pie_chart.getDescription().setEnabled(false);
            pie_chart.setExtraOffsets(5,10,5,5);

            pie_chart.setDragDecelerationFrictionCoef(0.99f);
            pie_chart.setDrawHoleEnabled(true);
            pie_chart.setHoleColor(Color.WHITE);
            pie_chart.setTransparentCircleRadius(61f);

            ArrayList<PieEntry> pieEntries = new ArrayList<>();

            //price , category_name
            for (int i=0 ; i<pieChartDataList.size();i++){
                pieEntries.add(new PieEntry(pieChartDataList.get(i).getPrise(),pieChartDataList.get(i).getCategory_name()));
                total_amount+=pieChartDataList.get(i).getPrise();
            }

            dashboard_balance_amount.setText(total_amount+"");

            PieDataSet pieDataSet = new PieDataSet(pieEntries,"");
            pieDataSet.setSliceSpace(2f);
            pieDataSet.setSelectionShift(5f);
            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

            pie_chart.setCenterText(date_dashboard_title.getText().toString()+"\n"+"(%)");
            pie_chart.setCenterTextSize(18);
            Legend l = pie_chart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setWordWrapEnabled(true);
            l.setDrawInside(false);
            l.setYOffset(5f);

            //legend.setYOffset(0f);

            pie_chart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
            PieData pieData = new PieData(pieDataSet);
            pieData.setValueTextSize(8f);
            pieData.setValueTextColor(Color.YELLOW);

            pie_chart.setData(pieData);

        }
    }

    private void showBarChart() {
        ArrayList barEntry = new ArrayList();

        barEntry.add(new BarEntry(1, 1000f));
        barEntry.add(new BarEntry(2, 2500f));
        barEntry.add(new BarEntry(3, 12500f));
        barEntry.add(new BarEntry(4, 3000f));

        BarDataSet bardataSet = new BarDataSet(barEntry, getResources().getString(R.string.category_list));
        bardataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        bar_chart.animateY(1000);

        BarData data = new BarData(bardataSet);
        bar_chart.setData(data);
        bar_chart.getDescription().setEnabled(false);
        bar_chart.getAxisRight().setEnabled(false);

        String[] month = new String[]{"Jan","Feb","Mar","April","May","June"};
        XAxis xAxis = bar_chart.getXAxis();
        xAxis.setLabelCount(4);
        xAxis.setValueFormatter(new MyXAxisValueFormatter(month));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawGridLines(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bar_chart_tab:
                bar_chart_layout.setVisibility(View.VISIBLE);
                pie_chart_layout.setVisibility(View.INVISIBLE);

                bar_chart_tab.setBackgroundResource(R.drawable.selected_bar_chart_tab_style);
                pie_chart_tab.setBackgroundResource(R.drawable.pie_chart_tab_style);
                break;

            case R.id.pie_chart_tab:
                bar_chart_layout.setVisibility(View.INVISIBLE);
                showPieChart(hidden_date.getText().toString());

                bar_chart_tab.setBackgroundResource(R.drawable.bar_chart_tab_style);
                pie_chart_tab.setBackgroundResource(R.drawable.selected_pie_chart_tab_style);
                break;

            case R.id.select_date_for_pie_chart:
                showDataPickerForPieChartData();
                break;
        }
    }

    private void showDataPickerForPieChartData() {
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
                    date_dashboard_title.setText("Report on : "+selected_date);
                    hidden_date.setText(selected_date);
                    Log.i("Selected Date => ",selected_date);
                    showPieChart(selected_date);
                }else {
                    String date = new SimpleDateFormat("d/MM/yyyy", Locale.getDefault()).format(new Date());
                    date_dashboard_title.setText("Report on : "+date);
                    hidden_date.setText(date);
                    Log.i("Date => ",date);
                    showPieChart(date);
                }

                alertDialog.dismiss();
                //Toast.makeText(getContext(), "Selected date : "+ selected_date[0], Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter{

        private String[] values;

        public MyXAxisValueFormatter(String[] values) {
            this.values = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return values[(int)value];
        }
    }

}
