package com.uteq.moviles.retrofitvolleyappimpl;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uteq.moviles.retrofitvolleyappimpl.models.Edicion;
import com.uteq.moviles.retrofitvolleyappimpl.services.retrofit.EdicionServiceRetrofit;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button btnRetrofit;
    Button btnVolley;
    EditText txtResults;
    EditText txtId;
    List<Edicion> edicionList;
    private static String URL = "https://revistas.uteq.edu.ec/ws/";
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRetrofit = findViewById(R.id.btnRetrofit);
        btnVolley = findViewById(R.id.btnVolley);
        txtId = findViewById(R.id.txtId);
        txtResults = findViewById(R.id.txtResults);
        queue = Volley.newRequestQueue(this);

        btnRetrofit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServicioRetrofit();
            }
        });

        btnVolley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServicioVolley();
            }
        });
    }

    private void ServicioRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EdicionServiceRetrofit edicionApi = retrofit.create(EdicionServiceRetrofit.class);
        Call<List<Edicion>> edicionCall = edicionApi.findAllIssuesById(txtId.getText().toString());
        edicionCall.enqueue(new Callback<List<Edicion>>() {
            @Override
            public void onResponse(Call<List<Edicion>> call, Response<List<Edicion>> response) {
                if (response.isSuccessful()){
                    imprimirResultados(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<Edicion>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Ocurrió un error al obtener la información", Toast.LENGTH_SHORT);
            }
        });
    }

    private void ServicioVolley(){
        Gson gson = new Gson();
        String volleyUrl = URL + "issues.php?j_id=" + txtId.getText().toString();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, volleyUrl, null, new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Type userListType = new TypeToken<ArrayList<Edicion>>(){}.getType();
                        imprimirResultados(gson.fromJson(response.toString(), userListType));
                    }
                },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Ocurrió un error al obtener la información", Toast.LENGTH_SHORT);
                            }
                        }
                );
        queue.add(jsonArrayRequest);
    }

    private void imprimirResultados(List<Edicion> edicionList){
        txtResults.setText("");
        if (edicionList != null){
            for (Edicion item: edicionList) {
                txtResults.append("----------------------------------------------------" + "\n");
                txtResults.append("issue_id: " + item.getIssue_id() + "\n");
                txtResults.append("volume: " + item.getVolume() + "\n");
                txtResults.append("number: " + item.getNumber() + "\n");
                txtResults.append("year: " + item.getYear()+ "\n");
                txtResults.append("date_published: " + item.getDate_published()+ "\n");
                txtResults.append("title: " + item.getTitle()+ "\n");
                txtResults.append("doi: " + item.getDoi()+ "\n");
                txtResults.append("cover: " + item.getCover()+ "\n");
                txtResults.append("----------------------------------------------------" + "\n");
            }
        }
    }
}