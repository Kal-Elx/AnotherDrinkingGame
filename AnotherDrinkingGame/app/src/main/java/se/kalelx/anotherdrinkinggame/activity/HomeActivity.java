package se.kalelx.anotherdrinkinggame.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import se.kalelx.anotherdrinkinggame.R;


public class HomeActivity extends AppCompatActivity {

    private Button mStartButton;
    private Button mRulesButton;
    private Button mSettingsButton;

    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, HomeActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mStartButton = findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = SetupActivity.newIntent(HomeActivity.this);
                startActivity(i);
            }
        });

        mRulesButton = findViewById(R.id.rules_button);
        mRulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = RulesActivity.newIntent(HomeActivity.this);
                startActivity(i);
            }
        });

        mSettingsButton = findViewById(R.id.settings_button);
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = SettingsActivity.newIntent(HomeActivity.this);
                startActivity(i);
            }
        });
    }
}
