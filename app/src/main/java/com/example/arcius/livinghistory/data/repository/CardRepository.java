package com.example.arcius.livinghistory.data.repository;


import android.util.JsonReader;

import com.example.arcius.livinghistory.data.Card;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * http://www.livinghistory.site/get_date.php?date=20181408
 **/

public class CardRepository implements DataInterface {

    private final static String link = "https://www.livinghistory.site/";

    private final static String getDateScript = "get_date.php?date=";

    private final static Map<String, List<Card>> cache = new LinkedHashMap<>();


    /**
     * Help : https://stackoverflow.com/questions/38372571/android-how-can-i-read-a-text-file-from-a-url
     **/

    @Override
    public void getCards(final LoadCardListener listener, final String id) {

        List<Card> cards;

        if (cache.containsKey(id)) {    //Load from cache
            cards = cache.get(id);
            listener.onLoaded(cards);
        } else {                        //Load from remote database

            new Thread(new Runnable() {

                List<Card> cards;
                HttpURLConnection connection;

                @Override
                public void run() {
                    listener.onLoading();

                    try {
                        URL url = new URL(link + getDateScript + id);

                        connection = (HttpURLConnection) url.openConnection();

                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                        cards = readJsonStream(in);
                        cache.put(id, cards);
                        listener.onLoaded(cards);

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        listener.onNoConnection();
                        e.printStackTrace();
                    } finally {
                        connection.disconnect();
                    }
                }
            }).start();
        }
    }

    private List<Card> readJsonStream(BufferedReader in) throws IOException {
        JsonReader reader = new JsonReader(in);
        try {
            return readCardArray(reader);
        } finally {
            reader.close();
        }
    }

    private List<Card> readCardArray(JsonReader reader) throws IOException {
        List<Card> messages = new ArrayList<Card>();

        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readCard(reader));
        }
        reader.endArray();
        return messages;
    }

    private Card readCard(JsonReader reader) throws IOException {   //TODO
        String eventID = null;
        String time = null;
        String mainTitle = null;
        String fullText = null;
        String picture = null;


        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "eventID":
                    eventID = reader.nextString();
                    System.out.println("JSON READ : eventID = " + eventID);
                    break;
                case "mainTitle":
                    mainTitle = reader.nextString();
                    System.out.println("JSON READ : mainTitle = " + mainTitle);
                    break;
                case "fullText":
                    fullText = reader.nextString();
                    System.out.println("JSON READ : fullText = " + fullText);
                    break;
                case "time":
                    time = reader.nextString();
                    System.out.println("JSON READ : time = " + time);
                    break;
                case "picture":
                    picture = reader.nextString();
                    System.out.println("JSON READ : picture = " + picture);
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        if (picture == null)
            return new Card(eventID, time, mainTitle, fullText);    //Without picture
        else return new Card(eventID, time, mainTitle, fullText, picture);  //With picture

    }

    private List<Double> readDoublesArray(JsonReader reader) throws IOException {   //Used for Map
        List<Double> doubles = new ArrayList<Double>();

        reader.beginArray();
        while (reader.hasNext()) {
            doubles.add(reader.nextDouble());
        }
        reader.endArray();
        return doubles;
    }
}
