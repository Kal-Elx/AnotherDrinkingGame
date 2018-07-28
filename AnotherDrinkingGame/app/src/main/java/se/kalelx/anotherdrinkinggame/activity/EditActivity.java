package se.kalelx.anotherdrinkinggame.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import se.kalelx.anotherdrinkinggame.R;
import se.kalelx.anotherdrinkinggame.java.Database;

public class EditActivity extends SetupActivity {

    private Database mDatabase = Database.get(this);

    public static Intent newIntent(Context context) {
        return new Intent(context, EditActivity.class);
    }

    @Override
    public void onAllPlayersAdded() {
        super.onAllPlayersAdded();
        mDatabase.cleanCouples();
    }

    @Override
    public void onAllRelationsAdded() {
        mDatabase.playersEdited();
        this.finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.go_back_not_possible);
        builder.setNeutralButton(R.string.ok, null);
        builder.create().show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mDatabase.getPlayers().size() < 2) {
            mDatabase.reset();
            finishAffinity();
        }
        mDatabase.cleanCouples();
        mDatabase.playersEdited();
    }
}
