package pl.edu.agh.metal.krugala.burze;

import android.graphics.Color;
import android.graphics.PointF;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class StormsActivity extends ActivityHelper implements AdapterView.OnItemSelectedListener {

    private int radius = 25;
    PointF coords;
    BurzeDzisNet sc = new BurzeDzisNet("206843ff2c57fa8d0ea1ba0c6b18e4080fccb1bd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storms);
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.radius_array, R.layout.spinner_my);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        ((TextView) findViewById(R.id.tvCity)).setText(getIntent().getStringExtra("CITY"));
        coords = (PointF)getIntent().getExtras().get("COORDS");
        ((TextView) findViewById(R.id.tvN)).setText(Float.toString(coords.y) + "N");
        ((TextView) findViewById(R.id.tvE)).setText(Float.toString(coords.x) + "E");
        Animation animation = new Animation(this);
        animation.animation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_storms, menu);
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

    public void findStorms(int radius) {
        try {
            StormsRunnable runnable = new StormsRunnable(coords,radius,sc);
            Thread t1 = new Thread(runnable);
            t1.start();
            t1.join();
            MyComplexTypeBurza myComplexTypeBurza = runnable.getMyComplexTypeBurza();
            String numeral;
            if (myComplexTypeBurza.getLiczba() == 0) {
                ((TextView)findViewById(R.id.tvResult)).setTextColor(Color.WHITE);
                ((TextView) findViewById(R.id.tvResult)).setText(getString(R.string.thereWereNoStrikes));
            }
            else {
                ((TextView)findViewById(R.id.tvResult)).setTextColor(Color.RED);
                if (myComplexTypeBurza.getLiczba() == 1)
                    numeral = getString(R.string.strike1);
                else if(myComplexTypeBurza.getLiczba() > 1 && myComplexTypeBurza.getLiczba() < 5)
                    numeral = getString(R.string.strikes2to5);
                else
                    numeral = getString(R.string.strikesMore);
                ((TextView) findViewById(R.id.tvResult)).setText(getString(R.string.inPeriod) +
                        myComplexTypeBurza.getOkres() + getString(R.string.minutesWeRegistered) +
                        myComplexTypeBurza.getLiczba() + " " + numeral + getString(R.string.closest) +
                        myComplexTypeBurza.getOdleglosc() + getString(R.string.km));
                String direction = myComplexTypeBurza.getKierunek();
                switch (direction) {
                    case "S":
                        direction = getString(R.string.south);
                        break;
                    case "SE":
                        direction = getString(R.string.southEast);
                        break;
                    case "SW":
                        direction = getString(R.string.southWest);
                        break;
                    case "N":
                        direction = getString(R.string.north);
                        break;
                    case "NE":
                        direction = getString(R.string.northEast);
                        break;
                    case "NW":
                        direction = getString(R.string.northWest);
                        break;
                    case "E":
                        direction = getString(R.string.east);
                        break;
                    case "W":
                        direction = getString(R.string.west);
                        break;
                }
                ((TextView) findViewById(R.id.tvResult)).setText(
                        ((TextView) findViewById(R.id.tvResult)).getText() + direction);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (!checkInternetConnection()) {
            Toast.makeText(this, getString(R.string.noConnection), Toast.LENGTH_SHORT).show();
            return;
        }
        String selected = parent.getItemAtPosition(position).toString();
        selected = selected.substring(0, selected.length()-2);
        int radius = Integer.parseInt(selected);
        findStorms(radius);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private boolean checkInternetConnection() {
        MobileInternetConnectionDetector m = new MobileInternetConnectionDetector(this);
        WIFIInternetConnectionDetector w = new WIFIInternetConnectionDetector(this);
        if(!m.checkMobileInternetConn() && !w.checkMobileInternetConn())
            return false;
        else
            return true;
    }

    @Override
    public void changeBackground(final int resId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayout ll = (LinearLayout) findViewById(R.id.stormsLayout);
                if (ll != null)
                    ll.setBackgroundResource(resId);
            }
        });
    }
}