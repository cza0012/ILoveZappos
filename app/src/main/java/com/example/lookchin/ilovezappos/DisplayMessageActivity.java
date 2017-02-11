package com.example.lookchin.ilovezappos;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.app.Activity;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.lookchin.ilovezappos.databinding.ActivityDisplayMessageBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;

import okhttp3.OkHttpClient;

public class DisplayMessageActivity extends AppCompatActivity {

    private OkHttpClient client;
    private String colorWay = "";
    public String json;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_display_message);
        final ActivityDisplayMessageBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_display_message);
        binding.setAddcart(this);
        Intent intent = getIntent();
        final String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        client = new OkHttpClient();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    json = ApiCall.GET(client,
                            "https://api.zappos.com/Search?term="
                                    +message+"&key=b743e26728e16b81da139182bb2094357c31d331");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
        int count = 0;
        String result = "";
        while(json == null){

        }
        if (json != null){
            //mDialog.dismiss();
            try {
                JSONObject objJson = new JSONObject(json);
                JSONArray jsonArray = (JSONArray) objJson.get("results");
                JSONObject shoesObj = (JSONObject) jsonArray.get(0);
                colorWay = colorWay + shoesObj.get("colorId").toString();
                for(int i = 1; i < jsonArray.length(); i++){
                    JSONObject iObj = (JSONObject) jsonArray.get(i);
                    if(shoesObj.get("productId").toString().equals(iObj.get("productId").toString())){
                        colorWay = colorWay + "," + iObj.get("colorId").toString();
                    }
                }
                Shoes theShoes = new Shoes(shoesObj.get("brandName").toString(),
                        shoesObj.get("thumbnailImageUrl").toString(),
                        shoesObj.get("productId").toString(),
                        shoesObj.get("originalPrice").toString(),
                        shoesObj.get("styleId").toString(),
                        colorWay,
                        shoesObj.get("price").toString(),
                        shoesObj.get("percentOff").toString(),
                        shoesObj.get("productUrl").toString(),
                        shoesObj.get("productName").toString());
                binding.setShoes(theShoes);
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(result);
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_message);
        layout.addView(textView);
    }
    public void addCartAnimate(View view){
        ScaleAnimation naruto = new ScaleAnimation(0,1,0,1);
        naruto.setFillBefore(true);
        naruto.setFillAfter(true);
        naruto.setFillEnabled(true);
        naruto.setDuration(247);
        naruto.setInterpolator(new OvershootInterpolator());
        FloatingActionButton foaltButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        foaltButton.startAnimation(naruto);
        count += 1;
        TextView basket = (TextView) findViewById(R.id.textView);
        if(count > 1){
            basket.setText("You copped "+count+" pairs.");
        }else{
            basket.setText("You coped a pair.");
        }

    }
}
