package com.example.javapassmtdua;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class DetailPage extends AppCompatActivity {

    Intent i;
    EPLListFood eplListFood;
    TextView tv_nameMenu, tv_decsMenu;
    ImageView iv_imgMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        i = getIntent();
        eplListFood = (EPLListFood)  i.getParcelableExtra("myfood");

        tv_nameMenu= findViewById(R.id.tv_nameMenu);
        tv_nameMenu.setText(eplListFood.getStrName());

        tv_decsMenu= findViewById(R.id.tv_decs_Menu);
        tv_decsMenu.setText(eplListFood.getStrDesc());

        iv_imgMenu= findViewById(R.id.iv_imgMenu);
//        ivImgLogoTeam.setImageResource(eplTeamModel.getStrTeamBadge());
        Glide.with(DetailPage.this)
                .load(eplListFood.getStrImage())
                .apply(new RequestOptions().override(350, 550))
                .into(iv_imgMenu);
    }
}