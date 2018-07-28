package se.kalelx.anotherdrinkinggame.java;

import java.util.Objects;

public class Couple {
    private final Player mPlayer1;
    private final Player mPlayer2;

    public Couple(Player player1, Player player2) {
        mPlayer1 = player1;
        mPlayer2 = player2;
    }

    public boolean playerInCouple(Player player) {
        return mPlayer1.equals(player) || mPlayer2.equals(player);
    }

    /*
    NOTE: The input player has to be a part of the couple.
     */
    public Player getPartnerFor(Player player) {
        if (mPlayer1.equals(player)) {
            return mPlayer2;
        } else {
            return mPlayer1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Couple couple = (Couple) o;
        return Objects.equals(mPlayer1, couple.mPlayer1) && Objects.equals(mPlayer2, couple.mPlayer2) ||
                Objects.equals(mPlayer2, couple.mPlayer1) && Objects.equals(mPlayer1, couple.mPlayer2);
    }

    public Player getPlayer1() {
        return mPlayer1;
    }

    public Player getPlayer2() {
        return mPlayer2;
    }

    @Override
    public int hashCode() {

        return Objects.hash(mPlayer1, mPlayer2);
    }

    @Override
    public String toString() {
        return mPlayer1.getName() + " & " + mPlayer2.getName();
    }
}
