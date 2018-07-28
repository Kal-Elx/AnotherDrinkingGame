package se.kalelx.anotherdrinkinggame.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import se.kalelx.anotherdrinkinggame.R;
import se.kalelx.anotherdrinkinggame.java.QuestionCallback;

public abstract class QuestionFragment extends Fragment {

    protected QuestionCallback mCallbacks;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (QuestionCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_question, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.information_icon:
                showRulesDialog();
                return true;
            case R.id.edit_players_icon:
                showEditPlayersDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showEditPlayersDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.edit_players_message);
        builder.setTitle(R.string.edit_players_title);
        builder.setPositiveButton(R.string.continue_to_game, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCallbacks.editPlayers();
            }
        });
        builder.setNegativeButton(R.string.go_back, null);
        builder.create().show();
    }

    private void showRulesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getRules());
        builder.setTitle(getTitle());
        builder.setNeutralButton(R.string.ok, null);
        builder.create().show();
    }

    protected abstract int getRules();

    protected abstract int getTitle();
}
