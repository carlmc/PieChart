package com.example.cmunayll.chartjson;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

/**
 * Created by cmunayll on 11/04/2018.
 */

public class GastoFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Gasto> gastos;
    private GastoAdapter adapter;

    public GastoFragment() {
        //construct
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        RecyclerView.LayoutManager layoutManager;
        view = inflater.inflate(R.layout.rv_gastos, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        GsonBuilder builder = new GsonBuilder();
                        Gson mGson = builder.create();
                        gastos = Arrays.asList(mGson.fromJson(response, Gasto[].class));
                        adapter = new GastoAdapter(gastos, R.layout.rv_gastos_item);
                        recyclerView.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //error response
                    }
                });
        requestQueue.add(stringRequest);

        return view;
    }

}
