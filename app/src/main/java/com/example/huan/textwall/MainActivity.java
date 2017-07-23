package com.example.huan.textwall;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.huan.textwall.model.TextItem;
import com.example.huan.textwall.view.TextWall;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TextWall textWall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textWall = (TextWall) findViewById(R.id.tw_test);

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onStart() {
        super.onStart();
        final String[] texts = {"聪明","活泼","可爱","善解人意","美丽","大方","帅气","稳重","乐观","多愁善感","果断勇敢"};
        textWall.post(new Runnable(){
            @Override
            public void run() {

                List<TextItem> textItems = new ArrayList<TextItem>();
                for (int i = 0; i < 30; i++) {
                    TextItem item = new TextItem();
                    item.setIndex(10);
                    item.setValue(texts[i%10]);
                    textItems.add(item);
                }
                textWall.setData(textItems, MainActivity.this);
                Log.d("aaaaa",textWall.getWidth()+"dp");
            }
        });

    }
}
