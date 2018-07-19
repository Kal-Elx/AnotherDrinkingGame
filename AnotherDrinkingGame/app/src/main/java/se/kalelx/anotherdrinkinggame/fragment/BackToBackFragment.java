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
import se.kalelx.anotherdrinkinggame.java.Player;
import se.kalelx.anotherdrinkinggame.java.Question;

public class BackToBackFragment extends QuestionFragment {

    private static final String KEY_QUESTION = "question key";
    private static final String KEY_PLAYER1 = "player1 key";
    private static final String KEY_PLAYER2 = "player2 key";

    private TextView mNamesTextView;
    private TextView mQuestionTextView;
    private Button mShowQuestionButton;
    private ImageButton mNextQuestionButton;

    public static BackToBackFragment newInstance(Question question, Player player1, Player player2) {

        Bundle args = new Bundle();

        args.putString(KEY_QUESTION, question.getQuestion());
        args.putString(KEY_PLAYER1, player1.getName());
        args.putString(KEY_PLAYER2, player2.getName());
        BackToBackFragment fragment = new BackToBackFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_back_to_back, container, false);

        Bundle args = getArguments();

        mNamesTextView = v.findViewById(R.id.name_textview);
        mNamesTextView.setText((args.getString(KEY_PLAYER1) + " & " + args.getString(KEY_PLAYER2)));

        mShowQuestionButton = v.findViewById(R.id.show_question_button);
        mShowQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuestionTextView.setVisibility(View.VISIBLE);
                mShowQuestionButton.setVisibility(View.INVISIBLE);
            }
        });

        mQuestionTextView = v.findViewById(R.id.question_text_view);
        mQuestionTextView.setText(args.getString(KEY_QUESTION));

        mNextQuestionButton = v.findViewById(R.id.next_question_button);
        mNextQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.nextQuestion();
            }
        });
        mNextQuestionButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.next_question_button_b2b));

        return v;
    }

    @Override
    protected int getRules() {
        return R.string.B2B_rules;
    }

    @Override
    protected int getTitle() {
        return R.string.B2B;
    }
}
