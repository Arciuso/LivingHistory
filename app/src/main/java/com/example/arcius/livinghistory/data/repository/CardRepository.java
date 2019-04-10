package com.example.arcius.livinghistory.data.repository;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.arcius.livinghistory.data.Card;
import com.example.arcius.livinghistory.data.repository.local.LocalRepository;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.example.arcius.livinghistory.data.repository.JsonReader.readJsonStream;
import static com.example.arcius.livinghistory.data.repository.JsonReader.saveImage;


/**
 * http://www.livinghistory.site/get_date.php?date=20181408
 **/

@Singleton
public class CardRepository implements DataInterface {

    private final static String link = "https://www.livinghistory.site/";

    private final static String getDateScript = "get_date.php?date=";

    private final Context context;

    private LocalRepository localRepository;

    @Inject
    CardRepository(Context context) {
        this.context = context;
        this.localRepository = new LocalRepository(context);
    }

    @Override
    public void getCards(final LoadCardListener listener, final String date) {

        List<Card> cards;

        if (localRepository.containsEvent(date)) { //Load from local
            Log.d("CardRepository", "LocalRepo contains cards with date : " + date);
            cards = localRepository.getCards(date);
            listener.onLoaded(cards);
            for (Card card : cards) {
                if (card.getType() == Card.CardTypes.Image && !card.isPictureReady(context)) {
                    downloadImage(listener, card);
                }
            }
        } else {                        //Load from remote database

            new Thread(new Runnable() {

                List<Card> cards;
                HttpURLConnection connection;

                @Override
                public void run() {
                    listener.onLoading();

                    Log.d("Get Cards", "ZIVOOOOOT");

                    try {
                        URL url = new URL(link + getDateScript + date);

                        connection = (HttpURLConnection) url.openConnection();

                        connection.setConnectTimeout(3000);
                        connection.setReadTimeout(3000);

                        int respCode = connection.getResponseCode();

                        Log.d("Get Cards", "Connection response code : " + respCode);

                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                        cards = readJsonStream(in);

                        if (!cards.isEmpty()) {
                            localRepository.insertCards(cards);
                            listener.onLoaded(cards);
                        }

                        for (Card card : cards) {
                            if (card.getType() == Card.CardTypes.Image && !card.isPictureReady(context)) {
                                downloadImage(listener, card);
                            }
                        }

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (EOFException e) {          //No Data
                        Log.d("Get Cards", "There is no data", e);
                        e.printStackTrace();
                        listener.onNoData();
                    } catch (UnknownHostException | SocketTimeoutException e) {  //No connection
                        Log.d("Get Cards", "No internet connection", e);
                        e.printStackTrace();
                        listener.onNoConnection();
                    } catch (IOException e) {
                        Log.d("Get Cards", "Unknown error", e);
                        e.printStackTrace();
                    } finally {
                        Log.d("Get Cards", "Disconnected !!!");
                        if(connection != null) connection.disconnect();
                    }
                }
            }).start();
        }
    }

    @Override
    public Card getCard(int eventID) {
        return localRepository.getCard(eventID);
    }

    @Override
    public Bitmap loadImage(String imageName) {
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

    private void downloadImage(final LoadCardListener listener, final Card card) {

        new Thread(new Runnable() {

            String imageName;
            Bitmap image;
            HttpURLConnection connection;

            @Override
            public void run() {

                try {
                    URL url = new URL(card.getLinkImage());

                    Log.d("Download Image", "Connecting to : " + url.toString());

                    connection = (HttpURLConnection) url.openConnection();

                    Log.d("Download Image", "Begin downloading");
                    BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
                    Log.d("Download Image", "Buffer loaded");
                    image = BitmapFactory.decodeStream(in);
                    if (image == null) Log.d("Download Image", "Image is null");
                    else Log.d("Download Image", "Image is not null");

                    imageName = card.getDate() + "-" + card.getEventID();

                    saveImage(imageName, image, context);                    //Save bitmap as PNG ( internal storage )

                    List<Card> cards = localRepository.getCards(card.getDate());

                    listener.onPicLoaded(cards);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (EOFException e) {          //No Data
                    Log.e("Download Image", "There is no data", e);
                } catch (UnknownHostException e) {  //No connection
                    Log.e("Download Image", "No internet connection", e);
                } catch (IOException e) {
                    Log.e("Download Image", "Unknown error", e);
                } finally {
                    if(connection != null) connection.disconnect();
                }
            }
        }).start();
    }
}
