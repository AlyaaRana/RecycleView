package com.example.javapassmtdua;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DataAdapter.DataAdapterListener {

    RecyclerView rv_listext;
    ArrayList<EPLListFood> ListDataEPLFood;

    private ImageView ivDelete;
    private DataAdapter adapterData;

    public void getEPLOnline(){
        RecyclerView rv = findViewById(R.id.rv_listext);
        ProgressBar pb = findViewById(R.id.pbloading_img);
        String url = "https://www.themealdb.com/api/json/v1/1/categories.php";
        AndroidNetworking.get(url)
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("succes ", "onResponse : "+jsonObject.toString());
                        try {
                            JSONArray jsonArrayEPLTeam = jsonObject.getJSONArray("categories");
                            for (int i = 0; i < jsonArrayEPLTeam.length(); i++) {
                                EPLListFood myFood = new EPLListFood();
                                JSONObject jsonFood = jsonArrayEPLTeam.getJSONObject(i);
                                myFood.setStrNumber(jsonFood.getString("idCategory"));
                                myFood.setStrName(jsonFood.getString("strCategory"));
                                myFood.setStrImage(jsonFood.getString("strCategoryThumb"));
                                myFood.setStrDesc(jsonFood.getString("strCategoryDescription"));
                                ListDataEPLFood.add(myFood);
                        }

                            rv_listext = findViewById(R.id.rv_listext);


                            adapterData = new DataAdapter(getApplicationContext(), ListDataEPLFood,MainActivity.this);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            rv_listext.setHasFixedSize(true);
                            rv_listext.setLayoutManager(mLayoutManager);
                            rv_listext.setAdapter(adapterData);

                            pb.setVisibility(View.GONE);
                            rv.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }


                    @Override
                    public void onError(ANError anError) {
                        Log.d("failed","onError : "+anError.toString());
                    }
                    });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListDataEPLFood = new ArrayList<>();
        getEPLOnline();

    }

    @Override
    public void onDataSelected(EPLListFood myFood) {
        Toast.makeText(this, "selected name"+ myFood.getStrName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, DetailPage.class);
        intent.putExtra("myfood", myFood);
        startActivity(intent);
        //finish();
    }

    @Override
    public void onDataLongClicked(EPLListFood myFood) {
        // Tampilkan opsi menu delete di sini
        showDeleteMenu(myFood);
    }

    private void showDeleteMenu(EPLListFood myFood) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Item");
        builder.setMessage("Are you sure you want to delete this item?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Lakukan operasi penghapusan item di sini
                deleteItem(myFood);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteItem(EPLListFood myFood) {
        // Lakukan operasi penghapusan item sesuai dengan data yang diberikan
        ListDataEPLFood.remove(myFood);
        adapterData.notifyDataSetChanged();
    }

}