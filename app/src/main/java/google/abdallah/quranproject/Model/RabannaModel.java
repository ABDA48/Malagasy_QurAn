package google.abdallah.quranproject.Model;

import android.media.MediaPlayer;

public class RabannaModel {
    String number;
    String rabanna;
    String place;
    String translation;
    int audio;
    MediaPlayer mediaPlayer;

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public void setAudio(int audio) {
        this.audio = audio;
    }

    public int getAudio() {
        return audio;
    }

    public RabannaModel() {
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRabanna() {
        return rabanna;
    }

    public void setRabanna(String rabanna) {
        this.rabanna = rabanna;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public RabannaModel(String number, String rabanna, String place) {
        this.number = number;
        this.rabanna = rabanna;
        this.place = place;
    }
}
