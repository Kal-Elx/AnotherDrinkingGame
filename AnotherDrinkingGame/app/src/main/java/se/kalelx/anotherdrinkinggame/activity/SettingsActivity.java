package se.kalelx.anotherdrinkinggame.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import se.kalelx.anotherdrinkinggame.R;
import se.kalelx.anotherdrinkinggame.java.Database;

public class SettingsActivity extends AppCompatActivity {

    private Database mDatabase;
    private Switch mSwitchNHIE;
    private Switch mSwitchPG;
    private Switch mSwitchB2B;
    private Switch mSwitchMISSION;
    private Switch mSwitchRYME;
    private Switch mSwitchCATEGORY;
    
    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, SettingsActivity.class);
        return i;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDatabase.clearQuestions();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mDatabase = Database.get(this);
        findSwitches();
        checkSwitches();
        setListeners();
    }

    private void handleNoneIsChecked() {
        if (!(mDatabase.getSwitchStatusForNHIE() || mDatabase.getSwitchStatusForPG() || mDatabase.getSwitchStatusForB2B()
                || mDatabase.getSwitchStatusForMISSION() || mDatabase.getSwitchStatusForCATEGORY() || mDatabase.getSwitchStatusForRYME())) {
            mSwitchNHIE.setChecked(true);
            Toast.makeText(this, R.string.settings_one_checked, Toast.LENGTH_SHORT).show();
        }
    }

    private void setListeners() {
        mSwitchNHIE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mDatabase.setSwitchStatusForNHIE(isChecked);
                handleNoneIsChecked();
            }
        });
        mSwitchPG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mDatabase.setSwitchStatusForPG(isChecked);
                handleNoneIsChecked();
            }
        });
        mSwitchB2B.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mDatabase.setSwitchStatusForB2B(isChecked);
                handleNoneIsChecked();
            }
        });
        mSwitchMISSION.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mDatabase.setSwitchStatusForMISSION(isChecked);
                handleNoneIsChecked();
            }
        });
        mSwitchRYME.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mDatabase.setSwitchStatusForRYME(isChecked);
                handleNoneIsChecked();
            }
        });
        mSwitchCATEGORY.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mDatabase.setSwitchStatusForCATEGORY(isChecked);
                handleNoneIsChecked();
            }
        });
    }

    private void checkSwitches() {
        mSwitchNHIE.setChecked(mDatabase.getSwitchStatusForNHIE());
        mSwitchPG.setChecked(mDatabase.getSwitchStatusForPG());
        mSwitchB2B.setChecked(mDatabase.getSwitchStatusForB2B());
        mSwitchMISSION.setChecked(mDatabase.getSwitchStatusForMISSION());
        mSwitchRYME.setChecked(mDatabase.getSwitchStatusForRYME());
        mSwitchCATEGORY.setChecked(mDatabase.getSwitchStatusForCATEGORY());
    }

    private void findSwitches() {
        mSwitchNHIE = findViewById(R.id.switch_NHIE);
        mSwitchPG = findViewById(R.id.switch_PG);
        mSwitchB2B = findViewById(R.id.switch_B2B);
        mSwitchMISSION = findViewById(R.id.switch_MISSION);
        mSwitchRYME = findViewById(R.id.switch_RYME);
        mSwitchCATEGORY = findViewById(R.id.switch_CATEGORY);
    }
}
