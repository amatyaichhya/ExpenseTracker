package com.dristi.kharcha;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class LineChartFragment extends Fragment {

    LineChartView houselinechart, eatinglinechart, personallinechart, grocerylinechart,shoppinglinechart,otherslinechart;
    LineChartView utilitieslinechart, medicallinechart, eduationlinechart,entertainmentlinechart,clothinglinechart,transportationlinechart;

    String date;

    DatabaseHelper databaseHelper;

    SharedPreferences preferences;

    int counth = 0,counto = 0,counts = 0,countt = 0,counte = 0,countg = 0,countp = 0,countu = 0,countent = 0,countm = 0,countedu = 0,countc = 0;

    int set;

    CardView others, shopping, transportation, household, eatingout, grocery, personal, utilities, entertainment, medical, education, clothing;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.line_chart,null);

        others = view.findViewById(R.id.others);
        shopping = view.findViewById(R.id.shopping);
        transportation = view.findViewById(R.id.transportation);
        household = view.findViewById(R.id.household);
        eatingout = view.findViewById(R.id.eatingout);
        grocery = view.findViewById(R.id.grocery);
        personal = view.findViewById(R.id.personal);
        utilities = view.findViewById(R.id.utilities);
        entertainment = view.findViewById(R.id.entertainment);
        medical = view.findViewById(R.id.medical);
        clothing = view.findViewById(R.id.clothing);
        education = view.findViewById(R.id.education);

        houselinechart = view.findViewById(R.id.housechart);
        eatinglinechart = view.findViewById(R.id.eatingchart);
        grocerylinechart = view.findViewById(R.id.grocerychart);
        personallinechart = view.findViewById(R.id.personalchart);
        utilitieslinechart = view.findViewById(R.id.utilitieschart);
        medicallinechart = view.findViewById(R.id.medicalchart);
        eduationlinechart = view.findViewById(R.id.educationchart);
        entertainmentlinechart = view.findViewById(R.id.entertainmentchart);
        shoppinglinechart = view.findViewById(R.id.shoppingchart);
        clothinglinechart = view.findViewById(R.id.clothingchart);
        otherslinechart = view.findViewById(R.id.otherschart);
        transportationlinechart = view.findViewById(R.id.transportationchart);

        set = 0;

        databaseHelper = new DatabaseHelper(getActivity());

        preferences = getActivity().getSharedPreferences("Date",0);

//        if(set == 0){
//            setDate(preferences.getString("Date",""));
//            set++;
//        }

        household.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makechart("Household");

                if(counth == 1){
                    houselinechart.setVisibility(View.GONE);
                    counth --;
                }
                else{
                    houselinechart.setVisibility(View.VISIBLE);
                    counth ++;
                }
            }
        });

        eatingout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makechart("Eating-out");

                if(counte == 1){
                    eatinglinechart.setVisibility(View.GONE);
                    counte --;
                }
                else{
                    eatinglinechart.setVisibility(View.VISIBLE);
                    counte ++;
                }
            }
        });
        grocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makechart("Grocery");

                if(countg == 1){
                    grocerylinechart.setVisibility(View.GONE);
                    countg --;
                }
                else{
                    grocerylinechart.setVisibility(View.VISIBLE);
                    countg ++;
                }

            }
        });
        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makechart("Personal");

                if(countp == 1){
                    personallinechart.setVisibility(View.GONE);
                    countp --;
                }
                else{
                    personallinechart.setVisibility(View.VISIBLE);
                    countp ++;
                }

            }
        });
        utilities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makechart("Utilities");

                if(countu == 1){
                    utilitieslinechart.setVisibility(View.GONE);
                    countu --;
                }
                else{
                    utilitieslinechart.setVisibility(View.VISIBLE);
                    countu ++;
                }


            }
        });
        medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makechart("Medical");

                if(countm == 1){
                    medicallinechart.setVisibility(View.GONE);
                    countm --;
                }
                else{
                    medicallinechart.setVisibility(View.VISIBLE);
                    countm ++;
                }

            }
        });
        education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makechart("Education");

                if(countedu == 1){
                    eduationlinechart.setVisibility(View.GONE);
                    countedu --;
                }
                else{
                    eduationlinechart.setVisibility(View.VISIBLE);
                    countedu ++;
                }

            }
        });
        entertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makechart("Entertainment");

                if(countent == 1){
                    entertainmentlinechart.setVisibility(View.GONE);
                    countent --;
                }
                else{
                    entertainmentlinechart.setVisibility(View.VISIBLE);
                    countent ++;
                }

            }
        });
        clothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makechart("Clothing");

                if(countc == 1){
                    clothinglinechart.setVisibility(View.GONE);
                    countc --;
                }
                else{
                    clothinglinechart.setVisibility(View.VISIBLE);
                    countc ++;
                }

            }
        });
        transportation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makechart("Transportation");

                if(countt == 1){
                    transportationlinechart.setVisibility(View.GONE);
                    countt --;
                }
                else{
                    transportationlinechart.setVisibility(View.VISIBLE);
                    countt ++;
                }

            }
        });

        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makechart("Shopping");

                if(counts == 1){
                    shoppinglinechart.setVisibility(View.GONE);
                    counts --;
                }
                else{
                    shoppinglinechart.setVisibility(View.VISIBLE);
                    counts ++;
                }

            }
        });

        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makechart("Others");

                if(counto == 1){
                    otherslinechart.setVisibility(View.GONE);
                    counto --;
                }
                else{
                    otherslinechart.setVisibility(View.VISIBLE);
                    counto ++;
                }

            }
        });



        makechart("Household");
        makechart("Eating-out");
        makechart("Grocery");
        makechart("Personal");
        makechart("Utilities");
        makechart("Medical");
        makechart("Education");
        makechart("Entertainment");
        makechart("Shopping");
        makechart("Transportation");
        makechart("Others");

        return view;
    }

    public void makechart(String category){

        setDate("2019-08-05");

        String[] axisData = {"1" , "2", "3" , "4" , "5" };
        int[] yAxisData =  new int[5];

        for(int i=0; i<5; i++){
            yAxisData[i] = databaseHelper.getlinecategorytotal(category, getDate());
            setDate(getnewdate(getDate()));
        }

        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();

        Line line = new Line(yAxisValues).setColor(Color.parseColor("#FFF46E72"));

        for(int i = 0; i<axisData.length;i++){
            axisValues.add(i,new AxisValue(i).setLabel(axisData[i]));
        }

        for(int i = 0; i<yAxisData.length;i++){
            yAxisValues.add(new PointValue(i,yAxisData[i]));
        }

        List lines = new ArrayList();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        switch (category){
            case "Household":
                houselinechart.setLineChartData(data);
                break;

            case "Eating-out":
                eatinglinechart.setLineChartData(data);
                break;

            case "Personal":
                personallinechart.setLineChartData(data);
                break;

            case "Grocery":
                grocerylinechart.setLineChartData(data);
                break;

            case "Utilities":
                utilitieslinechart.setLineChartData(data);
                break;

            case "Medical":
                medicallinechart.setLineChartData(data);
                break;

            case "Education":
                eduationlinechart.setLineChartData(data);
                break;

            case "Entertainment":
                entertainmentlinechart.setLineChartData(data);
                break;
            case "Clothing":
                clothinglinechart.setLineChartData(data);
                break;
            case "Transportation":
                transportationlinechart.setLineChartData(data);
                break;
            case "Shopping":
                shoppinglinechart.setLineChartData(data);
                break;
            case "Others":
                otherslinechart.setLineChartData(data);
                break;
            default:
                break;
        }

        Axis axis = new Axis();
        axis.setValues(axisValues);
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        data.setAxisYLeft(yAxis);

        axis.setTextSize(12);
        axis.setTextColor(Color.BLACK);

        yAxis.setTextColor(Color.BLACK);
        yAxis.setTextSize(12);

        axis.setName("Weeks");
        yAxis.setName("Expenses");

    }

    public String getnewdate(String date){

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        try{
            calendar.setTime(df.parse(date));
        }
        catch (ParseException e){
            e.printStackTrace();
        }

        calendar.add(Calendar.DATE,7);
        String newdate = df.format(calendar.getTime());
        return newdate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String datevalue){
        date = datevalue;
    }
}
