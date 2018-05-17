package com.example.cmunayll.chartjson;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private PieChart pieChart;
    private TextView amount;
    private TextView date;
    private TextView transaction;
    private Button mes;
    private Button popup;

    private RelativeLayout linear;

    ArrayList xVals, yVals;
    Float cantidad;
    float suma = 0;
    int total;

    Dialog myDialog;

    private Context context;
    private Activity activity;
    private PopupWindow popupWindow;

    String[] months = new String[]{"Enero", "Febrero","Marzo", "Abril", "Mayo"};
    Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pieChart = findViewById(R.id.pieChart);
        amount = findViewById(R.id.amount);
        date = findViewById(R.id.date);
        transaction = findViewById(R.id.transaction);
        mes = findViewById(R.id.btnMes);
        popup = findViewById(R.id.btnPopup);

        context = getApplicationContext();
        activity = MainActivity.this;
        linear = findViewById(R.id.linear);

        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater(); //(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View custom = inflater.inflate(R.layout.popup_information, null);

                /*popupWindow = new PopupWindow(custom, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);



                ImageButton close = custom.findViewById(R.id.ib_close);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });

                popupWindow.showAtLocation(linear, Gravity.CENTER, 0, 0);*/

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setView(custom)
                .setNegativeButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });




        mes.setText(months[cal.get(Calendar.MONTH)]+" "+cal.get(Calendar.YEAR));
        mes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMonths(0);
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenedor_gasto_lista, new GastoFragment())
                .commit();


        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);

        pieChart.setHoleColor(Color.WHITE);
        //pieChart.setTransparentCircleRadius(60f);
        pieChart.animateY(1000, Easing.EasingOption.EaseInCubic);

        load_data_from_server();

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(5f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date data = new Date();
        date.setText(simpleDateFormat.format(data));



    }

    public void load_data_from_server() {
        final int[] coloreando = new int[] {Color.rgb(102, 255, 102),
                Color.CYAN, Color.rgb(255, 255, 102),
                Color.RED, Color.rgb(102, 178, 255)};

        //xVals = new ArrayList<String>();
        yVals = new ArrayList<PieEntry>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String amount = jsonObject.getString("amount").trim();
                                String type = jsonObject.getString("type").trim();


                                cantidad = Float.valueOf(amount);

                                total = jsonArray.length();
                                yVals.add(new PieEntry(cantidad, type));

                                suma = suma + cantidad;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        PieDataSet dataSet = new PieDataSet(yVals, "");
                        dataSet.setSliceSpace(2f);
                        dataSet.setColors(ColorTemplate.createColors(coloreando));

                        PieData data = new PieData(dataSet);
                        data.setValueFormatter(new PercentFormatter());
                        data.setValueTextSize(10f);
                        data.setValueTextColor(Color.BLACK);

                        pieChart.setData(data);
                        pieChart.invalidate();

                        amount.setText("S/ "+String.valueOf(suma));
                        transaction.setText(String.valueOf(total));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {
                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public Dialog showMonths(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int check = 4;

        builder.setTitle("Selecciona un mes")
        .setPositiveButton("GUARDAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        })
        /*.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "Cancel Clicked", Toast.LENGTH_SHORT).show();
            }
        })*/
        .setSingleChoiceItems(months, check, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        mes.setText(months[cal.get(Calendar.MONTH)-4]+" "+cal.get(Calendar.YEAR));
                        break;
                    case 1:
                        mes.setText(months[cal.get(Calendar.MONTH)-3]+" "+cal.get(Calendar.YEAR));
                        break;
                    case 2:
                        mes.setText(months[cal.get(Calendar.MONTH)-2]+" "+cal.get(Calendar.YEAR));
                        break;
                    case 3:
                        mes.setText(months[cal.get(Calendar.MONTH)-1]+" "+cal.get(Calendar.YEAR));
                        break;
                    case 4:
                        mes.setText(months[cal.get(Calendar.MONTH)]+" "+cal.get(Calendar.YEAR));
                        break;
                }
            }
        }).create();

        /*builder.setItems(months, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });*/

        AlertDialog dialog = builder.create();
        dialog.show();

        return null;
    }


}
