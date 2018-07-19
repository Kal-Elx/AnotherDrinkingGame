package se.kalelx.anotherdrinkinggame.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import se.kalelx.anotherdrinkinggame.R;
import se.kalelx.anotherdrinkinggame.fragment.AddPlayersFragment;
import se.kalelx.anotherdrinkinggame.fragment.RelationsFragment;
import se.kalelx.anotherdrinkinggame.fragment.SetupFragment;

public class SetupActivity extends AppCompatActivity implements SetupFragment.Callbacks {

    public static Intent newIntent(Context context) {
        return new Intent(context, SetupActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = AddPlayersFragment.newInstance();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    @Override
    public void onAllPlayersAdded() {
        hideKeyboard(this);
        // Change fragment
        if (findViewById(R.id.fragment_container) != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, RelationsFragment.newInstance()).commit();
        }
    }

    @Override
    public void onAllRelationsAdded() {
        Intent intent = QuestionActivity.newIntent(this);
        startActivity(intent);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
