package com.example.arcius.livinghistory.main.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.arcius.livinghistory.R;
import com.example.arcius.livinghistory.data.Card;
import com.example.arcius.livinghistory.main.MainContract;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private MainContract.View view;
    private Context context;

    private List<Card> cards;

    class ViewHolderSingle extends RecyclerView.ViewHolder {

        CardView card;

        TextView time;
        TextView mainTitle;
        TextView countryText;


        ViewHolderSingle(View view) {
            super(view);

            this.card = view.findViewById(R.id.card);

            this.time = view.findViewById(R.id.time);
            this.mainTitle = view.findViewById(R.id.mainTitle);
            this.countryText = view.findViewById(R.id.countryText);
        }

    }

    class ViewHolderClassic extends ViewHolderSingle {

        TextView fullText;


        ViewHolderClassic(View view) {
            super(view);

            this.fullText = view.findViewById(R.id.fullText);
        }

    }

    class ViewHolderPic extends ViewHolderClassic {

        ImageView image;
        ProgressBar progressBar;
        TextView source;
        TextView title;

        ViewHolderPic(View view) {
            super(view);

            this.image = view.findViewById(R.id.image);
            this.progressBar = view.findViewById(R.id.progressBar);
            this.source = view.findViewById(R.id.imageSource);
            this.title = view.findViewById(R.id.imageTitle);
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
            case Single:
                return Card.CardTypes.Single.getValue();
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
                return new ViewHolderClassic(cardView);
            case 1:    //Image
                cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_pic, parent, false);
                return new ViewHolderPic(cardView);
            default:    //Classic / Single
                cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_simple, parent, false);
                return new ViewHolderClassic(cardView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Card card = cards.get(position);

        switch (card.getType().getValue()){
            case 0 :    //Classic
                final ViewHolderClassic classicHolder = (ViewHolderClassic) holder;

                classicHolder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view.showCard(card.getDate(), card.getEventID());
                    }
                });

                classicHolder.time.setText(card.getTime());
                classicHolder.mainTitle.setText(card.getMainTitle());
                classicHolder.fullText.setText(card.getFullText());
                classicHolder.countryText.setText(card.getCountry());

                break;
            case 1 :    //Image
                final CardAdapter.ViewHolderPic picHolder = (CardAdapter.ViewHolderPic) holder;

                String imageName = card.getDate() + "-" + card.getEventID();

                if(card.isPictureReady(context)) {
                    picHolder.progressBar.setVisibility(View.GONE);
                    picHolder.image.setVisibility(View.VISIBLE);
                    picHolder.image.setImageBitmap(loadImage(imageName));

                    picHolder.card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {               //Open-able only if picture is already loaded
                            view.showCard(card.getDate(), card.getEventID());
                        }
                    });

                } else {
                    picHolder.progressBar.setVisibility(View.VISIBLE);
                    picHolder.image.setVisibility(View.GONE);
                }

                picHolder.time.setText(card.getTime());
                picHolder.mainTitle.setText(card.getMainTitle());
                picHolder.fullText.setText(card.getFullText());
                picHolder.countryText.setText(card.getCountry());
                picHolder.title.setText(card.getTitleImage());
                picHolder.source.setText(card.getSourceImage());

                break;
            case 2 :    //Single
                final ViewHolderSingle singleHolder = (ViewHolderSingle) holder;

                singleHolder.time.setText(card.getTime());
                singleHolder.mainTitle.setText(card.getMainTitle());
                singleHolder.countryText.setText(card.getCountry());

                break;

        }
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    private Bitmap loadImage(String imageName) {
        Bitmap bitmap = null;
        FileInputStream fileInputStream;
        try {
            fileInputStream = this.context.openFileInput(imageName);
            bitmap = BitmapFactory.decodeStream(fileInputStream);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}