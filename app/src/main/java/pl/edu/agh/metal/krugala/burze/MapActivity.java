package pl.edu.agh.metal.krugala.burze;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MapActivity extends ActivityHelper {

    ImageView imageView;
    String mapURL = null;
    MapActivity mapActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        imageView = (ImageView)findViewById(R.id.imageView);
        mapURL = getIntent().getStringExtra("URL");
        mapActivity = this;
        //setContentView(new MYGIFView(mapActivity, mapURL, mapActivity));
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (checkInternetConnection()) {
                        refreshImage();

                    }
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t1.start();
        Animation animation = new Animation(this);
        animation.animation();
        //setContentView(new MYGIFView(this, mapURL, this));
    }

    private void refreshImage() {
        final URL[] myFileUrl = {null};
        final HttpURLConnection[] conn = {null};
        try {
            myFileUrl[0] = new URL (mapURL);
            conn[0] = (HttpURLConnection) myFileUrl[0].openConnection();
            conn[0].setDoInput(true);
            conn[0].connect();
            InputStream is = conn[0].getInputStream();
            setMap(BitmapFactory.decodeStream(is));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMap(final Bitmap bitmap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeBackground(final int resId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayout ll = (LinearLayout) findViewById(R.id.mainLayout);
                if (ll != null)
                    ll.setBackgroundResource(resId);
            }
        });
    }

    private boolean checkInternetConnection() {
        MobileInternetConnectionDetector m = new MobileInternetConnectionDetector(this);
        WIFIInternetConnectionDetector w = new WIFIInternetConnectionDetector(this);
        return !(!m.checkMobileInternetConn() && !w.checkMobileInternetConn());
    }
}
