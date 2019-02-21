package pl.edu.agh.metal.krugala.burze;

/**
 * Created by Konrad on 2015-06-22.
 * liczba - int - ilość wyładowań atmosferycznych.
 * odleglosc - float - odległość najbliższego wyładowania w kilometrach.
 * kierunek - string - kierunek do najbliższego wyładowania (S,E,W,N,NE,NW,SE,SW)
 * okres - int - okres pomiaru wyrażony w minutach
 */
public class MyComplexTypeBurza {
    private int liczba;
    private float odleglosc;
    private String kierunek;
    private int okres;

    public int getLiczba() {
        return liczba;
    }

    public void setLiczba(int liczba) {
        this.liczba = liczba;
    }

    public float getOdleglosc() {
        return odleglosc;
    }

    public void setOdleglosc(float odleglosc) {
        this.odleglosc = odleglosc;
    }

    public String getKierunek() {
        return kierunek;
    }

    public void setKierunek(String kierunek) {
        this.kierunek = kierunek;
    }

    public int getOkres() {
        return okres;
    }

    public void setOkres(int okres) {
        this.okres = okres;
    }

    @Override
    public String toString() {
        return "MyComplexTypeBurza{" +
                "liczba=" + liczba +
                ", odleglosc=" + odleglosc +
                ", kierunek=" + kierunek +
                ", okres=" + okres +
                '}';
    }
}
