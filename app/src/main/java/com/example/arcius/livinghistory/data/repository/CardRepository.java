package com.example.arcius.livinghistory.data.repository;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.JsonReader;

import com.example.arcius.livinghistory.data.Card;
import com.example.arcius.livinghistory.data.Location;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * http://www.livinghistory.site/get_date.php?date=20181408
 **/

@Singleton
public class CardRepository implements DataInterface {

    private final static String link = "https://www.livinghistory.site/";

    private final static String getDateScript = "get_date.php?date=";

    private final static Map<String, List<Card>> cache = new LinkedHashMap<>();

    private Context context;

    @Inject
    CardRepository() {

    }

    /**
     * Help : https://stackoverflow.com/questions/38372571/android-how-can-i-read-a-text-file-from-a-url
     **/

    @Override
    public void getCards(final LoadCardListener listener, final String id) {
        
        List<Card> cards;

        if (cache.containsKey(id)) {    //Load from cache
            cards = cache.get(id);
            listener.onLoaded(cards);
            for (Card card: cards) {
                if(card.getType() == Card.CardTypes.Image && !card.isPictureReady()) {
                    downloadImage(listener,card);
                }
            }
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

                        for (Card card: cards) {
                            if(card.getType() == Card.CardTypes.Image && !card.isPictureReady()) {
                                System.out.println("I found a Card Picture !");
                                downloadImage(listener,card);
                            }
                        }
                        
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (EOFException e) {          //No Data
                        System.out.println(e.toString());
                        e.printStackTrace();
                        listener.onNoData();
                    } catch (UnknownHostException e) {  //No connection
                        System.out.println(e.toString());
                        e.printStackTrace();
                        listener.onNoConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        connection.disconnect();
                    }
                }
            }).start();
        }
    }
    
    private void downloadImage(final LoadCardListener listener, final Card card) {
        new Thread(new Runnable() {

            Bitmap image;
            HttpURLConnection connection;

            @Override
            public void run() {

                try {
                    URL url = new URL(card.getLinkImage());

                    connection = (HttpURLConnection) url.openConnection();

                    System.out.println("Picture is downloading!");
                    BufferedInputStream in = new BufferedInputStream(connection.getInputStream());

                    image = BitmapFactory.decodeStream(in);
                    if(image == null) System.out.println("Image is null");

                    List<Card> cards = cache.get(card.getDate());   //Find card in cache
                    int index = cards.indexOf(card);
                    card.setImage(image);
                    cards.set(index, card);                         //Replace card in List

                    cache.put(card.getDate(),cards);                //Save it to the cache with downloaded image

                    System.out.println("Picture has been downloaded !");
                    listener.onLoaded(cache.get(card.getDate()));

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (EOFException e) {          //No Data
                    System.out.println(e.toString());
                    e.printStackTrace();
                } catch (UnknownHostException e) {  //No connection
                    System.out.println(e.toString());
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    connection.disconnect();
                }
            }
        }).start();
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
        List<Card> messages = new ArrayList<>();

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
        Location location = null;
        String date = null;


        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "eventID":
                    eventID = reader.nextString();
                    System.out.println("JSON READ : eventID = " + eventID);
                    break;

                case "date":
                    date = reader.nextString();
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
                case "location":
                    location = readLocation(reader);
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        if(fullText == null)    //Simple
            return new Card(eventID, time, mainTitle, location);
        else if (picture == null)
            return new Card(eventID, date, time, mainTitle, fullText, location);    //Without picture
        else
            return new Card(eventID, date, time, mainTitle, fullText, picture, location);  //With picture

    }

    private Location readLocation(JsonReader reader) throws IOException {
        int locationID = 0;
        float latitude = -1;
        float longitude = -1;
        String country = null;
        String name = null;

        reader.beginArray();
        reader.beginObject();
        while (reader.hasNext()) {
            String token = reader.nextName();
            switch (token) {
                case "locationID":
                    locationID = reader.nextInt();
                    break;
                case "latitude":
                    latitude = (float) reader.nextDouble();
                    break;
                case "longitude":
                    longitude = (float) reader.nextDouble();
                    break;
                case "country":
                    country = reader.nextString();
                    System.out.println("JSON READ : country = " + country);
                    break;
                case "name":
                    name = reader.nextString();
                    System.out.println("JSON READ : picture = " + name);
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        reader.endArray();

        return new Location(locationID, latitude, longitude, country, name);
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
