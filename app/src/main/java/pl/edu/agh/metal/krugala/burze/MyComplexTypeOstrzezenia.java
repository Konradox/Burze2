package pl.edu.agh.metal.krugala.burze;

/**
 * Created by Konrad on 2015-06-21.
 * od_dnia - String - początek obowiązywania ostrzeżenia
 * do_dnia - String - koniec obowiązywania ostrzeżenia
 * Wydane otrzeżenia są typu int. 0 oznacza brak ostrzeżeń. 1,2,3 oznaczają kolejno
 * I, II, III stopień zagrożenia. Możliwe ostrzeżenia:
 * mróz, upał, wiatr, opady, burza, trąba powietrzna.
 */
public class MyComplexTypeOstrzezenia {
    private String od_dnia;
    private String do_dnia;
    private int mroz;
    private int upal;
    private int wiatr;
    private int opad;
    private int burza;
    private int traba;

    private String mroz_od_dnia;
    private String upal_od_dnia;
    private String wiatr_od_dnia;
    private String opad_od_dnia;
    private String burza_od_dnia;
    private String traba_od_dnia;

    private String mroz_do_dnia;
    private String upal_do_dnia;
    private String wiatr_do_dnia;
    private String opad_do_dnia;
    private String burza_do_dnia;
    private String traba_do_dnia;

    public String getMroz_od_dnia() {
        return mroz_od_dnia;
    }

    public void setMroz_od_dnia(String mroz_od_dnia) {
        this.mroz_od_dnia = mroz_od_dnia;
    }

    public String getTraba_do_dnia() {
        return traba_do_dnia;
    }

    public void setTraba_do_dnia(String traba_do_dnia) {
        this.traba_do_dnia = traba_do_dnia;
    }

    public String getBurza_do_dnia() {
        return burza_do_dnia;
    }

    public void setBurza_do_dnia(String burza_do_dnia) {
        this.burza_do_dnia = burza_do_dnia;
    }

    public String getOpad_do_dnia() {
        return opad_do_dnia;
    }

    public void setOpad_do_dnia(String opad_do_dnia) {
        this.opad_do_dnia = opad_do_dnia;
    }

    public String getWiatr_do_dnia() {
        return wiatr_do_dnia;
    }

    public void setWiatr_do_dnia(String wiatr_do_dnia) {
        this.wiatr_do_dnia = wiatr_do_dnia;
    }

    public String getUpal_do_dnia() {
        return upal_do_dnia;
    }

    public void setUpal_do_dnia(String upal_do_dnia) {
        this.upal_do_dnia = upal_do_dnia;
    }

    public String getMroz_do_dnia() {
        return mroz_do_dnia;
    }

    public void setMroz_do_dnia(String mroz_do_dnia) {
        this.mroz_do_dnia = mroz_do_dnia;
    }

    public String getTraba_od_dnia() {
        return traba_od_dnia;
    }

    public void setTraba_od_dnia(String traba_od_dnia) {
        this.traba_od_dnia = traba_od_dnia;
    }

    public String getBurza_od_dnia() {
        return burza_od_dnia;
    }

    public void setBurza_od_dnia(String burza_od_dnia) {
        this.burza_od_dnia = burza_od_dnia;
    }

    public String getOpad_od_dnia() {
        return opad_od_dnia;
    }

    public void setOpad_od_dnia(String opad_od_dnia) {
        this.opad_od_dnia = opad_od_dnia;
    }

    public String getWiatr_od_dnia() {
        return wiatr_od_dnia;
    }

    public void setWiatr_od_dnia(String wiatr_od_dnia) {
        this.wiatr_od_dnia = wiatr_od_dnia;
    }

    public String getUpal_od_dnia() {
        return upal_od_dnia;
    }

    public void setUpal_od_dnia(String upal_od_dnia) {
        this.upal_od_dnia = upal_od_dnia;
    }

    public String getOd_dnia() {
        return od_dnia;
    }

    public void setOd_dnia(String od_dnia) {
        this.od_dnia = od_dnia;
    }

    public String getDo_dnia() {
        return do_dnia;
    }

    public void setDo_dnia(String do_dnia) {
        this.do_dnia = do_dnia;
    }

    public int getMroz() {
        return mroz;
    }

    public void setMroz(int mroz) {
        this.mroz = mroz;
    }

    public int getUpal() {
        return upal;
    }

    public void setUpal(int upal) {
        this.upal = upal;
    }

    public int getWiatr() {
        return wiatr;
    }

    public void setWiatr(int wiatr) {
        this.wiatr = wiatr;
    }

    public int getOpad() {
        return opad;
    }

    public void setOpad(int opad) {
        this.opad = opad;
    }

    public int getBurza() {
        return burza;
    }

    public void setBurza(int burza) {
        this.burza = burza;
    }

    public int getTraba() {
        return traba;
    }

    public void setTraba(int traba) {
        this.traba = traba;
    }

    @Override
    public String toString() {
        return "MyComplexTypeOstrzezenia{" +
                "od_dnia='" + od_dnia + '\'' +
                ", do_dnia='" + do_dnia + '\'' +
                ", mroz=" + mroz +
                ", upal=" + upal +
                ", wiatr=" + wiatr +
                ", opad=" + opad +
                ", burza=" + burza +
                ", traba=" + traba +
                '}';
    }
}
