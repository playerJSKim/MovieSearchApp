package com.flow.NaverMovie_KJS;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    Animation anim;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        startSplash();
    }

    private void startSplash(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
                textView = findViewById(R.id.textView5);
                textView.startAnimation(anim);

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);

                finish();
            }
        },2000);
    }
}
