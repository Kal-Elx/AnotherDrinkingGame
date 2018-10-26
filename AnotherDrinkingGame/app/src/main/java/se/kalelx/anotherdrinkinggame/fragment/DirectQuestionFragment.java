package se.kalelx.anotherdrinkinggame.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import se.kalelx.anotherdrinkinggame.R;
import se.kalelx.anotherdrinkinggame.java.Player;
import se.kalelx.anotherdrinkinggame.java.Question;

public class DirectQuestionFragment extends QuestionFragment {

    private static final String KEY_QUESTION = "question key";
    private static final String KEY_TITLE = "title key";
    private static final String KEY_PLAYER = "player key";

    private TextView mNameTextView;
    private TextView mTitleTextView;
    private TextView mQuestionTextView;
    private ImageButton mNextQuestionButton;
    private int mTitle;

    public static DirectQuestionFragment newInstance(Question question, Player player) {

        int title = 0;
        switch (question.getType()) {
            case RHYME:
                title = R.string.RHYME_title;
                break;
            case CATEGORY:
                title = R.string.CATEGORY_title;
                break;
        }

        Bundle args = new Bundle();

        args.putInt(KEY_TITLE, title);
        args.putString(KEY_QUESTION, question.getQuestion());
        args.putString(KEY_PLAYER, player.getName());

        DirectQuestionFragment fragment = new DirectQuestionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_direct_question, container, false);

        Bundle args = getArguments();

        mNameTextView = v.findViewById(R.id.name_textview);
        mNameTextView.setText(args.getString(KEY_PLAYER));

        mTitle = args.getInt(KEY_TITLE);
        mTitleTextView = v.findViewById(R.id.title_text_view);
        mTitleTextView.setText(mTitle);

        mQuestionTextView = v.findViewById(R.id.question_text_view);
        mQuestionTextView.setText(args.getString(KEY_QUESTION));

        mNextQuestionButton = v.findViewById(R.id.next_question_button);
        mNextQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.nextQuestion();
            }
        });

        changeColorOfButton();

        return v;
    }

    private void changeColorOfButton() {
        int background = R.drawable.next_question_button;
        switch (mTitle) {
            case R.string.RHYME_title:
                background = R.drawable.next_question_button_rhyme;
                break;
            case R.string.CATEGORY_title:
                background = R.drawable.next_question_button_category;
                break;
        }
        mNextQuestionButton.setBackground(ContextCompat.getDrawable(getActivity(), background));
    }

    @Override
    protected int getRules() {
        switch (mTitle) {
            case R.string.RHYME_title:
                return R.string.RHYME_rules;
            case R.string.CATEGORY_title:
                return R.string.CATEGORY_rules;
            default:
                return 0;
        }
    }

    @Override
    protected int getTitle() {
        switch (mTitle) {
            case R.string.RHYME_title:
                return R.string.RHYME;
            case R.string.CATEGORY_title:
                return R.string.CATEGORY;
            default:
                return 0;
        }
    }
}
