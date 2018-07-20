package se.kalelx.anotherdrinkinggame.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import se.kalelx.anotherdrinkinggame.R;
import se.kalelx.anotherdrinkinggame.java.Database;
import se.kalelx.anotherdrinkinggame.java.Gender;
import se.kalelx.anotherdrinkinggame.java.Player;

public class AddPlayersFragment extends SetupFragment {

    private EditText mNameInput;
    private Spinner mGenderSpinner;
    private ImageButton mAddButton;
    private TextView mPlayerCounterTextView;
    private RecyclerView mPlayerList;
    private Adapter mAdapter;
    private List<Player> mPlayers;


    public static AddPlayersFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AddPlayersFragment fragment = new AddPlayersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_players, container, false);

        mPlayers = new ArrayList<>();

        mPlayerList = v.findViewById(R.id.player_recyclerview);
        mPlayerList.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        mNameInput = v.findViewById(R.id.name_edittext);
        mNameInput.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mAddButton.performClick();
                    return true;
                }
                return false;
            }
        });
        mPlayerCounterTextView = v.findViewById(R.id.player_counter_textview);

        mGenderSpinner = v.findViewById(R.id.gender_spinner);

        mAddButton = v.findViewById(R.id.add_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameInput.getText().toString();
                if (name.length() > 0 && nameNotTaken(name)) {
                    mPlayers.add(0, new Player(name, getGender()));
                    mNameInput.getText().clear();
                    updateUI();
                } else {
                    int message;
                    if (name.length() > 0) {
                        message = R.string.player_name_not_unique;
                    } else {
                        message = R.string.no_player_name;
                    }
                    Toast t = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
                    t.show();
                }
            }
        });

        updateUI();

        return v;
    }

    private boolean nameNotTaken(String name) {
        for (Player player : mPlayers) {
            if (player.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    private Gender getGender() {
        String gender = mGenderSpinner.getSelectedItem().toString();

        if (gender.equals(getString(R.string.female))) {
            return Gender.FEMALE;
        } else if (gender.equals(getString(R.string.male))) {
            return Gender.MALE;
        } else {
            return Gender.OTHER;
        }
    }

    private void updateUI() {
        mPlayerCounterTextView.setText(getString(R.string.player_counter)+ " " + mPlayers.size());
        if (mAdapter == null) {
            mAdapter = new Adapter(mPlayers);
            mPlayerList.setAdapter(mAdapter);
        } else {
            mAdapter.setPlayers(mPlayers);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected int getInstructions() {
        return R.string.add_players_instructions;
    }

    private class Holder extends RecyclerView.ViewHolder {

        private Player mPlayer;
        private final TextView mPlayerName;
        private final ImageView mGenderImageView;
        private final ImageButton mDeleteButton;

        Holder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.setup_viewholder, parent, false));
            mPlayerName = itemView.findViewById(R.id.name_textview);
            mGenderImageView = itemView.findViewById(R.id.gender_image_view);
            mDeleteButton = itemView.findViewById(R.id.delete_button);
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPlayers.remove(mPlayer);
                    updateUI();
                }
            });
        }

        void bind(Player player) {
            mPlayer = player;
            mPlayerName.setText(mPlayer.getName());
            switch (player.getGender()) {
                case FEMALE:
                    mGenderImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_femenine));
                    break;
                case MALE:
                    mGenderImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_masculine));
                    break;
                case OTHER:
                    mGenderImageView.setImageResource(android.R.color.transparent);
            }
        }
    }

    private class Adapter extends RecyclerView.Adapter<Holder> {

        List<Player> mPlayers;

        Adapter(List<Player> players) {
            mPlayers = players;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new Holder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            holder.bind(mPlayers.get(position));
        }

        @Override
        public int getItemCount() {
            return mPlayers.size();
        }

        void setPlayers(List<Player> players) {
            mPlayers = players;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.finished_icon:
                if (mPlayers.size() >= 2) {
                    showDoneDialog();
                } else  {
                    showNotEnoughPlayersDialog();
                }
                return true;
            case R.id.information_icon:
                showInstructionsDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showNotEnoughPlayersDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.not_enough_players);
        builder.setNeutralButton(R.string.ok, null);
        builder.create().show();
    }

    private void showDoneDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.done_with_players);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Database.get(getActivity()).setPlayers(mPlayers);
                mCallbacks.onAllPlayersAdded();
            }
        });
        builder.setNegativeButton(R.string.no, null);
        builder.create().show();
    }
}
