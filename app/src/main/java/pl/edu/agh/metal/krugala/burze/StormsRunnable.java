package pl.edu.agh.metal.krugala.burze;

import android.graphics.PointF;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Konrad on 2015-06-23.
 */
public class StormsRunnable implements Runnable{
    private PointF coords;
    private int radius;
    private BurzeDzisNet sc;
    private MyComplexTypeBurza myComplexTypeBurza;

    public StormsRunnable(PointF coords, int radius, BurzeDzisNet sc) {
        this.coords = coords;
        this.radius = radius;
        this.sc = sc;
    }

    @Override
    public void run() {
        try {
            myComplexTypeBurza = sc.getLightnings(coords, radius);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public MyComplexTypeBurza getMyComplexTypeBurza() {
        return myComplexTypeBurza;
    }
}
