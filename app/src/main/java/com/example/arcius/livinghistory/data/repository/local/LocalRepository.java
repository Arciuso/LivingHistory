package com.example.arcius.livinghistory.data.repository.local;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

import com.example.arcius.livinghistory.data.Card;
import com.example.arcius.livinghistory.data.repository.local.entity.Event;
import com.example.arcius.livinghistory.data.repository.local.entity.Location;
import com.example.arcius.livinghistory.data.repository.local.entity.Picture;
import com.example.arcius.livinghistory.data.repository.local.entity.Source;

import java.util.ArrayList;
import java.util.List;

public class LocalRepository {

    private static volatile LocalDatabase database;

    public LocalRepository(Context context) {
        getDatabase(context);
    }

    private static void getDatabase(Context context) {
        if(database == null) {
            synchronized (LocalDatabase.class) {
                if (database == null) {
                    database = Room.databaseBuilder(context, LocalDatabase.class, "local_db").allowMainThreadQueries().build();
                }
            }
        }
    }

    public List<Card> getCards(String date) {

        Log.d("Local-db", "Get Cards called");

        List<Integer> eventIDList = database.eventDao().getEventIDsByDate(date);

        Log.d("Local-db", "Loaded " + eventIDList.size() + " event/s");

        List<Card> cardList = new ArrayList<>();

        for( int eventID : eventIDList) {
            Card card = getCard(eventID);
            cardList.add(card);
        }

        return cardList;
    }

    public boolean containsEvent(String date) {
        List<String> list = database.eventDao().getAllDates();
        return list.contains(date);
    }

    public void insertCards(List<Card> cards ) {

        Log.d("Local-db", "Insert Cards called");

        for(Card card : cards) {
            Card.CardTypes type = card.getType();

            if(type == Card.CardTypes.Single ) {                //SIMPLE
                Log.d("Local-db", "Insert SINGLE Card to Local-db with eventID : " + card.getEventID());
                card.getEvent().log();
                card.getLocation().log();
                if(!containsLocation(card.getLocation().locationID)) //If location is already saved in local-db
                    database.locationDao().insertLocation(card.getLocation());
                database.eventDao().insertEvent(card.getEvent());
            } else if ( type == Card.CardTypes.Classic ) {      //CLASSIC
                Log.d("Local-db", "Insert CLASSIC Card to Local-db with eventID : " + card.getEventID());
                card.getEvent().log();
                card.getLocation().log();
                card.getSource().log();
                if(!containsLocation(card.getLocation().locationID)) //If location is already saved in local-db
                    database.locationDao().insertLocation(card.getLocation());
                database.eventDao().insertEvent(card.getEvent());
                database.sourceDao().insertSource(card.getSource());
            } else {                                            //PICTURE
                Log.d("Local-db", "Insert PICTURE Card to Local-db with eventID : " + card.getEventID());
                card.getEvent().log();
                card.getLocation().log();
                card.getSource().log();
                card.getPicture().log();
                if(!containsLocation(card.getLocation().locationID)) //If location is already saved in local-db
                    database.locationDao().insertLocation(card.getLocation());
                database.eventDao().insertEvent(card.getEvent());
                database.sourceDao().insertSource(card.getSource());
                database.pictureDao().insertPicture(card.getPicture());
            }
        }
    }

    public Card getCard(int eventID) {
        Event event = database.eventDao().getEvent(eventID);

        Location location = database.locationDao().getLocation(event.locationID);

        Log.d("Local-db", "Get Card with eventID : ( " + eventID + " ) called");

        if (event.fullText == null) {      //SIMPLE
            Log.d("Local-db", "Card SIMPLE loaded from local-db");
            return new Card(event, location);
        } else if (event.pictureID == null) { //CLASSIC
            Log.d("Local-db", "Card CLASSIC loaded from local-db");
            Source source = database.sourceDao().getSource(eventID);
            return new Card(event, location, source);
        } else {                            //PICTURE
            Log.d("Local-db", "Card PICTURE loaded from local-db");
            Picture picture = database.pictureDao().getPicture(event.pictureID);
            Source source = database.sourceDao().getSource(eventID);
            return new Card(event, picture, location, source);
        }
    }

    private boolean containsLocation(int locationID) {
        List<Integer> list = database.locationDao().getLocationIDs();
        return list.contains(locationID);
    }
}
