package google.abdallah.quranproject.Model;

public class PrayerModel  {
    String Fajr;
    String Dhuhr;
    String Asr;
    String Maghrib;
    String Isha;
    String Sunset;
    String Imsak;
    String dateGregore;String dateHijri;
    public PrayerModel() {
    }
    public PrayerModel(String fajr, String dhuhr, String asr, String maghrib, String isha, String sunset, String imsak) {
        Fajr = fajr;
        Dhuhr = dhuhr;
        Asr = asr;
        Maghrib = maghrib;
        Isha = isha;
        Sunset = sunset;
        Imsak = imsak;
    }

    public String getDateHijri() {
        return dateHijri;
    }

    public void setDateHijri(String dateHijri) {
        this.dateHijri = dateHijri;
    }

    public String getDateGregore() {
        return dateGregore;
    }

    public void setDateGregore(String dateGregore) {
        this.dateGregore = dateGregore;
    }

    public String getFajr() {
        return Fajr;
    }

    public void setFajr(String fajr) {
        Fajr = fajr;
    }

    public String getDhuhr() {
        return Dhuhr;
    }

    public void setDhuhr(String dhuhr) {
        Dhuhr = dhuhr;
    }

    public String getAsr() {
        return Asr;
    }

    public void setAsr(String asr) {
        Asr = asr;
    }

    public String getMaghrib() {
        return Maghrib;
    }

    public void setMaghrib(String maghrib) {
        Maghrib = maghrib;
    }

    public String getIsha() {
        return Isha;
    }

    public void setIsha(String isha) {
        Isha = isha;
    }

    public String getSunset() {
        return Sunset;
    }

    public void setSunset(String sunset) {
        Sunset = sunset;
    }

    public String getImsak() {
        return Imsak;
    }

    public void setImsak(String imsak) {
        Imsak = imsak;
    }
}
