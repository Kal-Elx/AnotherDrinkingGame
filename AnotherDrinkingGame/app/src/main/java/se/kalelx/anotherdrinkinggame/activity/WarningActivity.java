package se.kalelx.anotherdrinkinggame.activity;

import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import se.kalelx.anotherdrinkinggame.R;

public class WarningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(R.string.drink_responsibly);
        builder.setMessage(R.string.warning_message);
        builder.setPositiveButton(R.string.continue_to_game, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(HomeActivity.newIntent(WarningActivity.this));
            }
        });
        builder.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WarningActivity.this.finish();
                System.exit(0);
            }
        });
        builder.create().show();
    }
}
