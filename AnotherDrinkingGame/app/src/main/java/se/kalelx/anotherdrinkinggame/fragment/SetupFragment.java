package se.kalelx.anotherdrinkinggame.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;

import se.kalelx.anotherdrinkinggame.R;

public abstract class SetupFragment extends Fragment {

    protected Callbacks mCallbacks;

    public interface Callbacks {
        void onAllPlayersAdded();
        void onAllRelationsAdded();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (AddPlayersFragment.Callbacks) context;
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
        inflater.inflate(R.menu.fragment_setup, menu);
    }

    protected void showInstructionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getInstructions());
        builder.setNeutralButton(R.string.ok, null);
        builder.setTitle(R.string.instructions);
        builder.create().show();
    }

    protected abstract int getInstructions();
}
