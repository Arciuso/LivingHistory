package com.example.arcius.livinghistory.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arcius.livinghistory.R;
import com.example.arcius.livinghistory.data.Card;
import com.example.arcius.livinghistory.event.EventActivity;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements MainContract.View {

    MainContract.Presenter presenter;
    CardAdapter adapter;

    public MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_frag, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.RecyclerViewCards);

        adapter = new CardAdapter();
        recyclerView.setAdapter(adapter);

        presenter.initData();

        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void showCard(String eventID) {
        Intent intent = new Intent(this.getContext(), EventActivity.class);
        intent.putExtra(EventActivity.EXTRA_EVENT_ID, eventID);
        startActivity(intent);
    }

    @Override
    public void showCards() {

    }

    @Override
    public void showRefresh() {

    }

    @Override
    public void addData(List<Card> cards) {
        System.out.println("DATA COUNT ! BEFORE : " + adapter.getItemCount());

        for (Card card : cards) {
            adapter.add(card);
            System.out.println("DATA ADDED !");
        }

        adapter.notifyDataSetChanged();

        System.out.println("DATA COUNT ! AFTER : " + adapter.getItemCount());
    }

    @Override
    public void addData(Card card) {
        adapter.add(card);

        adapter.notifyDataSetChanged();
    }

    private class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<Card> cards;

        class ViewHolder extends RecyclerView.ViewHolder {

            CardView card;

            TextView time;
            TextView mainTitle;
            TextView fullText;

            ViewHolder(View view) {
                super(view);

                this.card = view.findViewById(R.id.card);

                this.time = view.findViewById(R.id.time);
                this.mainTitle = view.findViewById(R.id.mainTitle);
                this.fullText = view.findViewById(R.id.fullText);
            }

            /* TODO Pridat dalsi ViewHolder pre iny typ Card */
        }

        class ViewHolderPic extends ViewHolder {

            ImageView image;

            ViewHolderPic(View view) {
                super(view);

                this.image = view.findViewById(R.id.image);
            }
        }

        CardAdapter() {
            this.cards = new ArrayList<>();
        }

        public void add(Card card) {
            cards.add(card);
        }

        @Override
        public int getItemViewType(int position) {
            Card card = this.cards.get(position);

            switch (card.getType()) {
                case Classic:
                    return Card.CardTypes.Classic.getValue();
                case Image:
                    return Card.CardTypes.Image.getValue();
            }

            return 0;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View cardView;

            switch (viewType) {
                case 0:    //Classic
                    cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
                    return new ViewHolder(cardView);

                case 1:    //Image
                    cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_pic, parent, false);
                    return new ViewHolderPic(cardView);
                default:    //Classic
                    cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
                    return new ViewHolder(cardView);

            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            final Card card = cards.get(position);

            switch (card.getType().getValue()){
                case 0 :    //Classic
                    ViewHolder classicHolder = (ViewHolder) holder;

                    classicHolder.card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showCard(card.getEventID());
                        }
                    });

                    classicHolder.time.setText(card.getTime());
                    classicHolder.mainTitle.setText(card.getMainTitle());
                    classicHolder.fullText.setText(card.getFullText());

                    break;
                case 2 :    //Image
                    ViewHolderPic picHolder = (ViewHolderPic) holder;

                    picHolder.card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showCard(card.getEventID());
                        }
                    });

                    picHolder.time.setText(card.getTime());
                    picHolder.mainTitle.setText(card.getMainTitle());
                    picHolder.fullText.setText(card.getFullText());

                    picHolder.image.setImageDrawable(getResources().getDrawable(card.getResourceImage()));
            }
        }

        @Override
        public int getItemCount() {
            System.out.println("CARDS SIZE : " + cards.size());
            return cards.size();
        }
    }

}
