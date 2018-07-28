package se.kalelx.anotherdrinkinggame.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import se.kalelx.anotherdrinkinggame.R;
import se.kalelx.anotherdrinkinggame.fragment.BackToBackFragment;
import se.kalelx.anotherdrinkinggame.fragment.DirectQuestionFragment;
import se.kalelx.anotherdrinkinggame.fragment.GeneralQuestionFragment;
import se.kalelx.anotherdrinkinggame.fragment.MissionFragment;
import se.kalelx.anotherdrinkinggame.java.Database;
import se.kalelx.anotherdrinkinggame.java.Player;
import se.kalelx.anotherdrinkinggame.java.Question;
import se.kalelx.anotherdrinkinggame.java.QuestionCallback;
import se.kalelx.anotherdrinkinggame.java.QuestionType;

public class QuestionActivity extends AppCompatActivity implements QuestionCallback {

    private Database mDatabase;
    private List<Player> mPlayers;
    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    private final Random mRandomGenerator = new Random();
    private Player mPlayerWithTurn;

    public static Intent newIntent(Context context) {
        return new Intent(context, QuestionActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        mDatabase = Database.get(this);
        mDatabase.createQuestions();
        mPlayers = mDatabase.getPlayers();
        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.fragment_container);
        if (mFragment == null) {
            showQuestion();
        }
    }

    private void showQuestion() {
        int index = mRandomGenerator.nextInt(mPlayers.size());
        mPlayerWithTurn = mPlayers.get(index);
        Question question = mDatabase.getQuestionFor(mPlayerWithTurn);
        int toolbarColor = 0;
        switch (question.getType()) {
            case NHIE:
                mFragment = GeneralQuestionFragment.newInstance(question);
                toolbarColor = R.color.NHIE_color;
                break;
            case PG:
                mFragment = GeneralQuestionFragment.newInstance(question);
                toolbarColor = R.color.PG_color;
                break;
            case RYME:
                mFragment = DirectQuestionFragment.newInstance(question, mPlayerWithTurn);
                toolbarColor = R.color.RYME_color;
                break;
            case CATEGORY:
                mFragment = DirectQuestionFragment.newInstance(question, mPlayerWithTurn);
                toolbarColor = R.color.CATEGORY_color;
                break;
            case B2B_COUPLE:
                mFragment = BackToBackFragment.newInstance(question, mPlayerWithTurn, mDatabase.getPartnerFor(mPlayerWithTurn));
                toolbarColor = R.color.B2B_color;
                break;
            case B2B_FEMALE:
            case B2B_MALE:
            case B2B_GENERAL:
                mFragment = BackToBackFragment.newInstance(question, mPlayerWithTurn, getTeammateFor(mPlayerWithTurn, question.getType()));
                toolbarColor = R.color.B2B_color;
                break;
            case MISSION_MALE:
            case MISSION_FEMALE:
            case MISSION_GENERAL:
            case MISSION_COUPLE:
                mFragment = MissionFragment.newInstance(question, mPlayerWithTurn);
                toolbarColor = R.color.MISSION_color;
                break;
        }
        mFragmentManager.beginTransaction().replace(R.id.fragment_container, mFragment).commit();
        changeToolbarColor(toolbarColor);
    }

    private Player getTeammateFor(Player player, QuestionType type) {
        List<Player> possibleTeammates = null;
        switch (type) {
            case B2B_GENERAL:
                possibleTeammates = new ArrayList<>(mPlayers);
                break;
            case B2B_FEMALE:
                possibleTeammates = new ArrayList<>(mDatabase.getFemales());
                break;
            case B2B_MALE:
                possibleTeammates = new ArrayList<>(mDatabase.getMales());
                break;
        }
        possibleTeammates.remove(player);
        return possibleTeammates.get(mRandomGenerator.nextInt(possibleTeammates.size()));
    }

    private void changeToolbarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(color)));
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(R.string.finnish_game);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                finish();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void nextQuestion() {
        showQuestion();
    }

    @Override
    public void editPlayers() {
        Intent intent = EditActivity.newIntent(this);
        startActivity(intent);
    }
}
