package com.example.arcius.livinghistory.data.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.arcius.livinghistory.data.Card;
import com.example.arcius.livinghistory.data.repository.local.entity.Event;
import com.example.arcius.livinghistory.data.repository.local.entity.Location;
import com.example.arcius.livinghistory.data.repository.local.entity.Picture;
import com.example.arcius.livinghistory.data.repository.local.entity.Source;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


final class JsonReader {

    static void saveImage(String imageName, Bitmap image, Context context) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
            Log.d("Save Image", "Image saved");
        } catch (Exception e) {
            Log.e("Save Iamge", "Image not saved", e);
        }
    }

    static List<Card> readJsonStream(BufferedReader in) throws IOException {
        android.util.JsonReader reader = new android.util.JsonReader(in);
        try {
            return readCardArray(reader);
        } finally {
            reader.close();
        }
    }

    private static List<Card> readCardArray(android.util.JsonReader reader) throws IOException {
        List<Card> messages = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readCard(reader));
        }
        reader.endArray();
        return messages;
    }

    private static Card readCard(android.util.JsonReader reader) throws IOException {   //TODO
        Integer eventID = null;
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
                    eventID = reader.nextInt();
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

        if (fullText == null)    //Simple
            return new Card( new Event(eventID, date, time, mainTitle), location);
        else if (picture == null)
            return new Card( new Event(eventID, date, time, mainTitle, fullText), location, source);    //Without picture
        else
            return new Card( new Event(eventID, date, time, mainTitle, fullText), picture, location, source);  //With picture

    }

    private static Location readLocation(android.util.JsonReader reader) throws IOException {
        Integer locationID = null;
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

    private static Source readSource(android.util.JsonReader reader) throws IOException {
        Integer sourceID = null;
        Integer eventID = null;
        String sourceName = null;
        String sourceLink = null;
        String sourceTitle = null;

        reader.beginArray();
        reader.beginObject();
        while (reader.hasNext()) {
            String token = reader.nextName();
            switch (token) {
                case "sourceID":
                    sourceID = reader.nextInt();
                    break;
                case "eventID":
                    eventID = reader.nextInt();
                    break;
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

        return new Source(sourceID, eventID, sourceName, sourceLink, sourceTitle);
    }

    private static Picture readPicture(android.util.JsonReader reader) throws IOException {
        Integer pictureID = null;
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
