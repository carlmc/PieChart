package com.example.cmunayll.chartjson;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private PieChart pieChart;
    private TextView amount;
    private TextView date;
    private TextView transaction;

    ArrayList xVals, yVals;
    Float cantidad;
    float suma = 0;
    int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pieChart = findViewById(R.id.pieChart);
        amount = findViewById(R.id.amount);
        date = findViewById(R.id.date);
        transaction = findViewById(R.id.transaction);


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
        final int[] coloreando = new int[] {Color.rgb(255, 102, 102), Color.CYAN, Color.rgb(204, 204, 0), Color.BLUE, Color.rgb(250, 131, 13)};

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
                        //dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
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

}
