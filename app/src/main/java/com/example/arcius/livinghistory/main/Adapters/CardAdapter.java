package com.example.arcius.livinghistory.main.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arcius.livinghistory.R;
import com.example.arcius.livinghistory.data.Card;
import com.example.arcius.livinghistory.main.MainContract;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    MainContract.View view;
    Context context;

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

    class ViewHolderPic extends CardAdapter.ViewHolder {

        ImageView image;

        ViewHolderPic(View view) {
            super(view);

            this.image = view.findViewById(R.id.image);

            this.card = view.findViewById(R.id.card_pic);

            this.time = view.findViewById(R.id.time);
            this.mainTitle = view.findViewById(R.id.mainTitle);
            this.fullText = view.findViewById(R.id.fullText);
        }
    }

    public CardAdapter(MainContract.View view, Context context) {
        this.view = view;
        this.context = context;
        this.cards = new ArrayList<>();
    }

    public void clear() {
        cards.clear();
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
                return new CardAdapter.ViewHolder(cardView);

            case 1:    //Image
                cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_pic, parent, false);
                return new CardAdapter.ViewHolderPic(cardView);
            default:    //Classic
                cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
                return new CardAdapter.ViewHolder(cardView);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Card card = cards.get(position);

        switch (card.getType().getValue()){
            case 0 :    //Classic
                final CardAdapter.ViewHolder classicHolder = (CardAdapter.ViewHolder) holder;

                classicHolder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view.showCard(card.getEventID());
                    }
                });

                classicHolder.time.setText(card.getTime());
                classicHolder.mainTitle.setText(card.getMainTitle());
                classicHolder.fullText.setText(card.getFullText());

                break;
            case 1 :    //Image
                final CardAdapter.ViewHolderPic picHolder = (CardAdapter.ViewHolderPic) holder;

                picHolder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view.showCard(card.getEventID());
                    }
                });

                picHolder.time.setText(card.getTime());
                picHolder.mainTitle.setText(card.getMainTitle());
                picHolder.fullText.setText(card.getFullText());

                picHolder.image.setImageDrawable(context.getResources().getDrawable(card.getResourceImage()));

                break;
        }
    }

    @Override
    public int getItemCount() {
        System.out.println("CARDS SIZE : " + cards.size());
        return cards.size();
    }
}