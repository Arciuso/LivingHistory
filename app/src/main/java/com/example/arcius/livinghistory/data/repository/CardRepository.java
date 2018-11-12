package com.example.arcius.livinghistory.data.repository;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.JsonReader;
import android.util.Log;

import com.example.arcius.livinghistory.data.Card;
import com.example.arcius.livinghistory.data.Location;
import com.example.arcius.livinghistory.data.Picture;
import com.example.arcius.livinghistory.data.Source;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

    private final Context context;  //TODO to saving images to internal storage !

    @Inject
    CardRepository(Context context) {
        this.context = context;
    }


    @Override
    public void getCards(final LoadCardListener listener, final String id) {

        List<Card> cards;

        if (cache.containsKey(id)) {    //Load from cache
            cards = cache.get(id);
            listener.onLoaded(cards);
            for (Card card: cards) {
                if(card.getType() == Card.CardTypes.Image && !card.isPictureReady(context)) {
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

                        if(!cards.isEmpty()) {
                            cache.put(id, cards);
                            listener.onLoaded(cards);
                        }

                       for (Card card: cards) {
                            if(card.getType() == Card.CardTypes.Image && !card.isPictureReady(context)) {
                                downloadImage(listener,card);
                            }
                        }
                        
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (EOFException e) {          //No Data
                        Log.d("Get Cards","There is no data", e);
                        e.printStackTrace();
                        listener.onNoData();
                    } catch (UnknownHostException e) {  //No connection
                        Log.d("Get Cards","No internet connection", e);
                        e.printStackTrace();
                        listener.onNoConnection();
                    } catch (IOException e) {
                        Log.d("Get Cards","Unknown error", e);
                        e.printStackTrace();
                    } finally {
                        connection.disconnect();
                    }
                }
            }).start();
        }
    }

    @Override
    public Card getCard(String date, String eventID) {
        List<Card> cards;

        cards = cache.get(date);

        for (Card card : cards) {
            if(card.getEventID().equals(eventID)) {
                return card;
            }
        }

        return null;
    }

    @Override
    public Bitmap loadImage(String imageName) {
        Bitmap bitmap = null;
        FileInputStream fileInputStream;
        try {
            fileInputStream    = this.context.openFileInput(imageName);
            bitmap      = BitmapFactory.decodeStream(fileInputStream);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void downloadImage(final LoadCardListener listener, final Card card) {


        new Thread(new Runnable() {

            String imageName;
            Bitmap image;
            HttpURLConnection connection;

            @Override
            public void run() {

                try {
                    URL url = new URL(card.getLinkImage());

                    connection = (HttpURLConnection) url.openConnection();

                    Log.d("Download Image","Begin downloading");
                    BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
                    Log.d("Download Image","Buffer loaded");
                    image = BitmapFactory.decodeStream(in);
                    if(image == null) Log.d("Download Image","Image is null");
                    else Log.d("Download Image","Image is not null");

                    imageName = card.getDate() + "-" + card.getEventID();

                    saveImage(imageName, image);                    //Save bitmap as PNG ( internal storage )

                    List<Card> cards = cache.get(card.getDate());   //Find card in cache
                    int index = cards.indexOf(card);
                    cards.set(index, card);                         //Replace card in List

                    cache.put(card.getDate(),cards);                //Save it to the cache

                    listener.onPicLoaded(cache.get(card.getDate()));

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (EOFException e) {          //No Data
                    Log.d("Download Image","There is no data", e);
                    e.printStackTrace();
                } catch (UnknownHostException e) {  //No connection
                    Log.d("Download Image","No internet connection", e);
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d("Download Image","Unknown error", e);
                } finally {
                    connection.disconnect();
                }
            }
        }).start();
    }

    private void saveImage(String imageName, Bitmap image) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = this.context.openFileOutput(imageName, Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
            Log.d("Save Image","Image saved");
        } catch (Exception e) {
            Log.d("Save Iamge","Image not saved",e);
            e.printStackTrace();
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
        Picture picture = null;
        Source source = null;
        Location location = null;
        String date = null;


        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "eventID":
                    eventID = reader.nextString();
                    break;
                case "date":
                    date = reader.nextString();
                    break;
                case "mainTitle":
                    mainTitle = reader.nextString();
                    break;
                case "fullText":
                    fullText = reader.nextString();
                    break;
                case "time":
                    time = reader.nextString();
                    break;
                case "picture":
                    picture = readPicture(reader);
                    break;
                case "source":
                    source = readSource(reader);
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
            return new Card(eventID, date, time, mainTitle, fullText, location, source);    //Without picture
        else
            return new Card(eventID, date, time, mainTitle, fullText, picture, location, source);  //With picture

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
                    break;
                case "name":
                    name = reader.nextString();
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

    private Source readSource(JsonReader reader) throws IOException {
        String sourceName = null;
        String sourceLink = null;
        String sourceTitle = null;

        reader.beginArray();
        reader.beginObject();
        while (reader.hasNext()) {
            String token = reader.nextName();
            switch (token) {
                case "link":
                    sourceLink = reader.nextString();
                    break;
                case "name":
                    sourceName = reader.nextString();
                    break;
                case "title":
                    sourceTitle = reader.nextString();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        reader.endArray();

        return new Source(sourceName, sourceLink, sourceTitle);
    }

    private Picture readPicture(JsonReader reader) throws IOException {
        int pictureID = 0;
        String link = null;
        String source = null;
        String title = null;

        reader.beginArray();
        reader.beginObject();
        while (reader.hasNext()) {
            String token = reader.nextName();
            switch (token) {
                case "pictureID":
                    pictureID = reader.nextInt();
                    break;
                case "link":
                    link = reader.nextString();
                    break;
                case "source":
                    source = reader.nextString();
                    break;
                case "title":
                    title = reader.nextString();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        reader.endArray();

        return new Picture(pictureID, link, source, title);
    }
}
