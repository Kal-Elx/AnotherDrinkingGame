package se.kalelx.anotherdrinkinggame.java;

public class Player {

    private final String mName;
    private final Gender mGender;
    private RelationshipStatus mRelationshipStatus;

    public Player(String name, Gender gender) {
        mName = name;
        mGender = gender;
        mRelationshipStatus = RelationshipStatus.SINGLE;
    }

    public String getName() {
        return mName;
    }

    public Gender getGender() {
        return mGender;
    }

    public boolean isSingle() {
        return mRelationshipStatus == RelationshipStatus.SINGLE;
    }

    public void setDating() {
        mRelationshipStatus = RelationshipStatus.DATING;
    }

    @Override
    public String toString() {
        return mName;
    }
}
