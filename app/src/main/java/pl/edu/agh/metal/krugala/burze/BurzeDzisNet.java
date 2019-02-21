package pl.edu.agh.metal.krugala.burze;

import android.graphics.PointF;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Konrad on 2015-06-21.
 * Obsługa API udostępnianego na strone http://burze.dzis.net/
 * Do działania wymagana jest bibliotek ksoap2 dostępna na stronie
 * internetowej: http://kobjects.org/ksoap2/index.html
 */
public class BurzeDzisNet {
    private final String NAMESPACE = "http://burze.dzis.net/soap.php";
    private final String URL = "http://burze.dzis.net/soap.php";
    private final String SOAP_ACTION_KEY_API = "http://burze.dzis.net/soap.php#KeyAPI";
    private final String SOAP_ACTION_MIEJSCOWOSC = "http://burze.dzis.net/soap.php#miejscowosc";
    private final String SOAP_ACTION_OSTRZEZENIA = "http://burze.dzis.net/soap.php#ostrzezenia_pogodowe";
    private final String SOAP_ACTION_SZUKAJ_BURZY = "http://burze.dzis.net/soap.php#szukaj_burzy";
    private final String METHOD_KEY_API = "KeyAPI";
    private final String METHOD_MIEJSCOWOSC = "miejscowosc";
    private final String METHOD_OSTRZEZENIA = "ostrzezenia_pogodowe";
    private final String METHOD_SZUKAJ_BURZY = "szukaj_burzy";
    private PropertyInfo pfApiKey;

    /**
     * Konstruktor inicjalizujący parametr z kluczem do API
     *
     * @param apiKey klucz do API uzyskany ze strony http://burze.dzis.net/
     */
    public BurzeDzisNet(String apiKey) {
        pfApiKey = new PropertyInfo();
        pfApiKey.setName("ApiKeyIn");
        pfApiKey.setValue(apiKey);
        pfApiKey.setType(String.class);
    }

    /**
     * Wykorzystuje listę miejscowości na stronie internetowej http://burze.dzis.net/ do uzyskania
     * współrzędnych. Zwraca punkt (0f, 0f) jeśli miejscowości nie znaleziono.
     *
     * @param city nazwa miejscowości.
     * @return współrzędne podanej miejscowości.
     * @throws IOException
     * @throws XmlPullParserException
     */
    public PointF getCoordinates(String city) throws IOException, XmlPullParserException {
        SoapObject request = new SoapObject(NAMESPACE, METHOD_MIEJSCOWOSC);
        PropertyInfo pfCity = new PropertyInfo();
        pfCity.setName("Miejscowosc");
        pfCity.setValue(city);
        pfCity.setType(String.class);
        request.addProperty(pfCity);
        request.addProperty(pfApiKey);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION_MIEJSCOWOSC, envelope);
            SoapObject respons = (SoapObject) envelope.getResponse();
            SoapPrimitive y = (SoapPrimitive)respons.getProperty(0);
            float yVal = Float.parseFloat((String)y.getValue());
            SoapPrimitive x = (SoapPrimitive)respons.getProperty(1);
            float xVal = Float.parseFloat((String)x.getValue());
            PointF coords = new PointF(xVal, yVal);
            return coords;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Sprawdza czy klucz do API jest poprawny.
     *
     * @return true jeśli uwierzytelnienie zakończyło się sukcesem.
     * @throws IOException
     * @throws XmlPullParserException
     */
    public boolean checkApiKey() throws IOException, XmlPullParserException {
        SoapObject request = new SoapObject(NAMESPACE, METHOD_KEY_API);
        request.addProperty(pfApiKey);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION_KEY_API, envelope);
            boolean response = (boolean) envelope.getResponse();
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Sprawdza czy dla danych współrzędnych istnieją jakieś ostrzeżenia pogodowe
     *
     * @param coords długość(x) i szerokość(y) geograficzna
     * @return ostrzeżenia pogodowe oraz daty okresu na jaki zostały wydane. Patrz: @see pl.edu.agh.metal.krugala.burze.MyComplexTypeOstrzezenia
     * @throws IOException
     * @throws XmlPullParserException
     */
    public MyComplexTypeOstrzezenia getWarnings(PointF coords) throws IOException, XmlPullParserException {
        return getWarnings(coords.x, coords.y);
    }

    /**
     * Sprawdza czy dla danych współrzędnych istnieją jakieś ostrzeżenia pogodowe
     *
     * @param x długość geograficzna
     * @param y szerokość geograficzna
     * @return ostrzeżenia pogodowe oraz daty okresu na jaki zostały wydane. Patrz: @see pl.edu.agh.metal.krugala.burze.MyComplexTypeOstrzezenia
     * @throws IOException
     * @throws XmlPullParserException
     */
    public MyComplexTypeOstrzezenia getWarnings(float x, float y) throws IOException, XmlPullParserException {
        SoapObject request = new SoapObject(NAMESPACE, METHOD_OSTRZEZENIA);
        PropertyInfo pfY = new PropertyInfo();
        pfY.setName("Y");
        pfY.setValue(Float.toString(y));
        pfY.setType(float.class);
        request.addProperty(pfY);

        PropertyInfo pfX = new PropertyInfo();
        pfX.setName("X");
        pfX.setValue(Float.toString(x));
        pfX.setType(float.class);
        request.addProperty(pfX);
        request.addProperty(pfApiKey);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION_OSTRZEZENIA, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();
            MyComplexTypeOstrzezenia result = new MyComplexTypeOstrzezenia();
            result.setOd_dnia(response.getProperty(0).toString());
            result.setDo_dnia(response.getProperty(1).toString());
            result.setMroz(Integer.parseInt(response.getProperty(2).toString()));
            result.setMroz_od_dnia(response.getProperty(3).toString());
            result.setMroz_do_dnia(response.getProperty(4).toString());
            result.setUpal(Integer.parseInt(response.getProperty(5).toString()));
            result.setUpal_od_dnia(response.getProperty(6).toString());
            result.setUpal_do_dnia(response.getProperty(7).toString());
            result.setWiatr(Integer.parseInt(response.getProperty(8).toString()));
            result.setWiatr_od_dnia(response.getProperty(9).toString());
            result.setWiatr_do_dnia(response.getProperty(10).toString());
            result.setOpad(Integer.parseInt(response.getProperty(11).toString()));
            result.setOpad_od_dnia(response.getProperty(12).toString());
            result.setOpad_do_dnia(response.getProperty(13).toString());
            result.setBurza(Integer.parseInt(response.getProperty(14).toString()));
            result.setBurza_od_dnia(response.getProperty(15).toString());
            result.setBurza_do_dnia(response.getProperty(16).toString());
            result.setTraba(Integer.parseInt(response.getProperty(17).toString()));
            result.setTraba_od_dnia(response.getProperty(18).toString());
            result.setTraba_do_dnia(response.getProperty(19).toString());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Sprawdza liczbę doziemnych wyładowań, odległość i kierunek najbliższego oraz czas pomiaru.
     * Dane na temat wyładowań brane są z obszaru o kształcie okręgu ze środkiem w punkcie o podanych
     * współrzędnych i podanym promieniu.
     *
     * @param coords Współrzędne punkt pomiarowego
     * @param radius
     * @return Dane o doziemnych wyładowaniach zarejestrowanych w podanym rejonie. Patrz: @see pl.edu.agh.metal.krugala.burze.MyComplexTypeBurza
     * @throws IOException
     * @throws XmlPullParserException
     */
    public MyComplexTypeBurza getLightnings(PointF coords, int radius) throws IOException, XmlPullParserException {
        return getLightnings(coords.x, coords.y, radius);
    }

    /**
     * Sprawdza liczbę doziemnych wyładowań, odległość i kierunek najbliższego oraz czas pomiaru.
     * Dane na temat wyładowań brane są z obszaru o kształcie okręgu ze środkiem w punkcie o podanych
     * współrzędnych i podanym promieniu.
     *
     * @param x
     * @param y
     * @param radius
     * @return Dane o doziemnych wyładowaniach zarejestrowanych w podanym rejonie. Patrz: @see pl.edu.agh.metal.krugala.burze.MyComplexTypeBurza
     * @throws IOException
     * @throws XmlPullParserException
     */
    public MyComplexTypeBurza getLightnings(float x, float y, int radius) throws IOException, XmlPullParserException {
        SoapObject request = new SoapObject(NAMESPACE, METHOD_SZUKAJ_BURZY);
        PropertyInfo pfY = new PropertyInfo();
        pfY.setName("Y");
        pfY.setValue(Float.toString(y));
        pfY.setType(String.class);
        request.addProperty(pfY);

        PropertyInfo pfX = new PropertyInfo();
        pfX.setName("X");
        pfX.setValue(Float.toString(x));
        pfX.setType(String.class);
        request.addProperty(pfX);

        PropertyInfo pfRadius = new PropertyInfo();
        pfRadius.setName("Radius");
        pfRadius.setValue(Integer.toString(radius));
        pfRadius.setType(int.class);
        request.addProperty(pfRadius);
        request.addProperty(pfApiKey);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION_SZUKAJ_BURZY, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();
            MyComplexTypeBurza result = new MyComplexTypeBurza();
            result.setLiczba(Integer.parseInt(response.getProperty(0).toString()));
            result.setOdleglosc(Float.parseFloat(response.getProperty(1).toString()));
            if (response.getProperty(2) != null)
                result.setKierunek(response.getProperty(2).toString());
            else
                result.setKierunek("");
            result.setOkres(Integer.parseInt(response.getProperty(3).toString()));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
