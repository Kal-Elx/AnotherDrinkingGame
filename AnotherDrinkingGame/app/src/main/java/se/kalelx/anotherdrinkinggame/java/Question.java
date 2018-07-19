package se.kalelx.anotherdrinkinggame.java;

public class Question {
    private final String mQuestion;
    private final QuestionType mType;

    public Question(String question, QuestionType type) {
        mQuestion = question;
        mType = type;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public QuestionType getType() {
        return mType;
    }
}
