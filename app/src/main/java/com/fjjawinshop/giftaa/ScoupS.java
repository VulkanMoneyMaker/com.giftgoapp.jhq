package com.fjjawinshop.giftaa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;


import io.reactivex.Observable;

public class ScoupS extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Observable.timer(3, TimeUnit.SECONDS)
               .subscribe(__ -> openWebGame());
    }


    private void openWebGame() {
        Intent intent = new Intent(this, Show.class);
        startActivity(intent);
        finish();
    }
}