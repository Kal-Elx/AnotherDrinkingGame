package se.kalelx.anotherdrinkinggame.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import se.kalelx.anotherdrinkinggame.R;
import se.kalelx.anotherdrinkinggame.java.Couple;
import se.kalelx.anotherdrinkinggame.java.Database;
import se.kalelx.anotherdrinkinggame.java.Player;
import se.kalelx.anotherdrinkinggame.java.RelationshipStatus;

public class RelationsFragment extends SetupFragment {

    private ImageButton mAddButton;
    private TextView mCoupleCounterTextView;
    private RecyclerView mCouplesList;
    private Adapter mAdapter;
    private Spinner mSpinner1;
    private Spinner mSpinner2;
    private List<Couple> mCouples;
    private Database mDatabase;
    private List<Player> mPlayers;
    private Player mDefaultPlayer;

    public static RelationsFragment newInstance() {

        Bundle args = new Bundle();

        RelationsFragment fragment = new RelationsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_relations, container, false);

        mDatabase = Database.get(getActivity());
        mPlayers = mDatabase.getPlayers();
        mDefaultPlayer = new Player(getString(R.string.relations_spinner_default), null);
        mPlayers.add(mDefaultPlayer);
        mCouples = mDatabase.getCouples();

        mCoupleCounterTextView = v.findViewById(R.id.couple_counter_textview);
        mAddButton = v.findViewById(R.id.add_relation_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player player1 = (Player) mSpinner1.getSelectedItem();
                Player player2 = (Player) mSpinner2.getSelectedItem();

                if (player1.equals(mDefaultPlayer) || player2.equals(mDefaultPlayer)) return;

                if (!player1.equals(player2)) {
                    Couple couple = new Couple(player1, player2);
                    if (!mCouples.contains(couple)) {
                        mCouples.add(0, couple);
                        player1.setRelationshipStatus(RelationshipStatus.DATING);
                        player2.setRelationshipStatus(RelationshipStatus.DATING);
                        updateCoupleCounter();
                        updateUI();
                    } else {
                        Toast.makeText(getActivity(), R.string.couple_already_added, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.no_relation_to_self, Toast.LENGTH_LONG).show();
                }
                clearSpinners();
            }
        });
        mCouplesList = v.findViewById(R.id.player_relations_recyclerview);
        mCouplesList.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSpinner1 = v.findViewById(R.id.spinner1);
        mSpinner2 = v.findViewById(R.id.spinner2);
        populateSpinners();

        updateUI();
        updateCoupleCounter();

        return v;
    }

    private void populateSpinners() {
        ArrayAdapterBlankDefault adapter =
                new ArrayAdapterBlankDefault(getActivity(), R.layout.spinner_dropdown_item,
                        mPlayers);
        adapter.setDropDownViewResource( R.layout.spinner_dropdown_item);

        mSpinner1.setAdapter(adapter);
        mSpinner2.setAdapter(adapter);

        clearSpinners();
    }

    private class ArrayAdapterBlankDefault extends ArrayAdapter {


        public ArrayAdapterBlankDefault(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);
        }

        @Override
        public int getCount() {
            return super.getCount() - 1;
        }
    }

    private void clearSpinners() {
        mSpinner1.setSelection(mPlayers.size()-1);
        mSpinner2.setSelection(mPlayers.size()-1);
    }

    private void updateCoupleCounter() {
        mCoupleCounterTextView.setText(getString(R.string.couple_counter)+ " " + mCouples.size());
    }

    private void updateUI() {
         if (mAdapter == null) {
             mAdapter = new Adapter(mCouples);
             mCouplesList.setAdapter(mAdapter);
         } else {
             mAdapter.setCouples(mCouples);
             mAdapter.notifyDataSetChanged();
         }
    }

    private class Holder extends RecyclerView.ViewHolder {

        private Couple mCouple;
        private final TextView mCoupleTextView;
        private final ImageButton mDeleteButton;

        Holder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.relations_viewholder, parent, false));
            mCoupleTextView = itemView.findViewById(R.id.name_textview);
            mDeleteButton = itemView.findViewById(R.id.delete_button);
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCouples.remove(mCouple);
                    Player player1 = mCouple.getPlayer1();
                    Player player2 = mCouple.getPlayer2();
                    boolean player1IsSingle = true;
                    boolean player2IsSingle = true;
                    for (Couple c : mCouples) {
                        if (c.playerInCouple(player1)) {
                            player1IsSingle = false;
                        }
                        if (c.playerInCouple(player2)) {
                            player2IsSingle = false;
                        }
                    }
                    if (player1IsSingle) {
                        player1.setRelationshipStatus(RelationshipStatus.SINGLE);
                    }
                    if (player2IsSingle) {
                        player2.setRelationshipStatus(RelationshipStatus.SINGLE);
                    }
                    updateCoupleCounter();
                    updateUI();
                }
            });
        }

        void bind(Couple couple) {
            mCouple = couple;
            mCoupleTextView.setText(mCouple.toString());
        }
    }

    private class Adapter extends RecyclerView.Adapter<Holder> {

        List<Couple> mCouples;

        Adapter(List<Couple> couples) {
            mCouples = couples;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(getActivity()), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            holder.bind(mCouples.get(position));
        }

        @Override
        public int getItemCount() {
            return mCouples.size();
        }

        void setCouples(List<Couple> couples) {
            mCouples = couples;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.finished_icon:
                showDoneDialog();
                return true;
            case R.id.information_icon:
                showInstructionsDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int getInstructions() {
        return R.string.add_relations_instructions;
    }

    private void showDoneDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (mCouples.size() > 0) {
            builder.setMessage(getString(R.string.done_with_couples) + getListWithCouples());
        } else {
            builder.setMessage(R.string.done_no_couples);
        }
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                prepareForClose();
                mCallbacks.onAllRelationsAdded();
            }
        });
        builder.setNegativeButton(R.string.no, null);
        builder.create().show();
    }

    private void prepareForClose() {
        mPlayers.remove(mDefaultPlayer);
        mDatabase.setCouples(mCouples);
    }

    private String getListWithCouples() {
        StringBuilder builder = new StringBuilder();
        for (Couple c : mCouples) {
            builder.append("\n" + c.toString());
        }
        return builder.toString();
    }

    @Override
    public void onStop() {
        super.onStop();
        prepareForClose();
    }
}
