package se.kalelx.anotherdrinkinggame.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import se.kalelx.anotherdrinkinggame.R;

public class RulesActivity extends AppCompatActivity {

    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, RulesActivity.class);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
    }
}
