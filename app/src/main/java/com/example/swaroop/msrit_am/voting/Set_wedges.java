package com.example.swaroop.msrit_am.voting;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.example.swaroop.msrit_am.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

/**
 * Created by swaroop on 11/19/2016.
 */

public class Set_wedges {
    Context context;

    public Set_wedges(Context context) {
        this.context = context;
    }

    void setSubject(String string)
    {   try{
        TextView subject=(TextView)((Activity)context).findViewById(R.id.subject);
        subject.setText(string);}
    catch (Exception e)
    {}
    }

    void setPiechart(String bunk,String donot_bunk,String total_number)
    {  try{
        int bunk_no=Integer.parseInt(bunk);
        int nonbunk_no=Integer.parseInt(donot_bunk);
        int total_num=Integer.parseInt(total_number);

        PieChart pieChart=(PieChart)((Activity)context).findViewById(R.id.piechart);

        pieChart.setRotationEnabled(true);
        pieChart.setCenterText("Bunk Stats");
        pieChart.setCenterTextSize(7);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setDrawEntryLabels(true);

        ArrayList<PieEntry> onpie=new ArrayList<>();
        ArrayList<String> x=new ArrayList<>();
        ArrayList<Integer> colours =new ArrayList<>();

        onpie.add(new PieEntry(bunk_no,0));
        onpie.add(new PieEntry(nonbunk_no,1));
        onpie.add(new PieEntry(total_num-nonbunk_no-bunk_no,2));

        x.add("BUNK");
        x.add("DO NOT BUNK");
        x.add("NOT RESPONDED");

        PieDataSet pieDataSet=new PieDataSet(onpie,"Green-bunk  Red-do not bunk  Yellow-not responded");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(10);

        colours.add(Color.GREEN);
        colours.add(Color.RED);
        colours.add(Color.YELLOW);

        pieDataSet.setColors(colours);

        Legend legend=pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

        PieData pieData =new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();}
    catch (Exception e)
    {}








    }







}
