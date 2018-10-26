package se.kalelx.anotherdrinkinggame.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import se.kalelx.anotherdrinkinggame.R;
import se.kalelx.anotherdrinkinggame.java.Database;
import se.kalelx.anotherdrinkinggame.java.Player;
import se.kalelx.anotherdrinkinggame.java.Question;

public class MissionFragment extends QuestionFragment {

    private static final String KEY_QUESTION = "question key";
    private static final String KEY_PLAYER = "player key";

    private TextView mTitleTextView;
    private TextView mNameTextView;
    private TextView mQuestionTextView;
    private Button mShowMissionButton;
    private ImageButton mNextQuestionButton;
    private String mPlayerName;
    private boolean mMissionIsSecret;
    private Database mDatabase;

    public static MissionFragment newInstance(Question question, Player player) {
        
        Bundle args = new Bundle();
        args.putString(KEY_QUESTION, question.getQuestion());
        args.putString(KEY_PLAYER, player.getName());
        
        MissionFragment fragment = new MissionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mission, container, false);

        mDatabase = Database.get(getActivity());

        Bundle args = getArguments();
        String question = args.getString(KEY_QUESTION);

        mTitleTextView = v.findViewById(R.id.title_text_view);

        mPlayerName = args.getString(KEY_PLAYER);
        mNameTextView = v.findViewById(R.id.name_textview);
        mNameTextView.setText(mPlayerName);

        mShowMissionButton = v.findViewById(R.id.show_mission_button);
        mQuestionTextView = v.findViewById(R.id.question_text_view);

        mMissionIsSecret = checkIfMissionIsSecret(question);
        if (mMissionIsSecret) {
            mTitleTextView.setText(R.string.MISSION_secret_title);
            mTitleTextView.setTextSize(36);
            mQuestionTextView.setVisibility(View.INVISIBLE);
            mShowMissionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mShowMissionButton.setVisibility(View.INVISIBLE);
                    mQuestionTextView.setVisibility(View.VISIBLE);
                }
            });
        } else {
            mTitleTextView.setText(R.string.MISSION_title);
            mShowMissionButton.setVisibility(View.INVISIBLE);
        }

        mQuestionTextView.setText(handleString(question));

        mNextQuestionButton = v.findViewById(R.id.next_question_button);
        mNextQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.nextQuestion();
            }
        });

        mNextQuestionButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.next_question_button_mission));

        return v;
    }

    private boolean checkIfMissionIsSecret(String question) {
        return question.contains("{S}");
    }

    private String handleString(String question) {
        question = question.replace("{S}", "");
        if (question.contains("*")) {
            return mDatabase.getPlayerToQuestion(question, mPlayerName);
        } else {
            return question;
        }
    }

    @Override
    protected int getRules() {
        if (mMissionIsSecret) {
            return R.string.MISSION_secret_rules;
        } else {
            return R.string.MISSION_not_secret_rules;
        }
    }

    @Override
    protected int getTitle() {
        if (mMissionIsSecret) {
            return R.string.MISSION_secret;
        } else {
            return R.string.MISSION;
        }
    }
}
