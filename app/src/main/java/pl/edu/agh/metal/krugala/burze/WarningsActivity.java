package pl.edu.agh.metal.krugala.burze;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;


public class WarningsActivity extends ActivityHelper {
    private static String celcius;
    private static String resultText;
    private String TAG = "PGGURU";
    BurzeDzisNet sc = new BurzeDzisNet("206843ff2c57fa8d0ea1ba0c6b18e4080fccb1bd");
    Button b;
    TextView tvResult;
    EditText etCity, etN, etE;
    String announce;
    PointF coords = null;
    MyComplexTypeOstrzezenia warnings;
    private LocationManager mLocationManager;
    Location myLocation;
    long lastChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        announce = getString(R.string.searching);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100L,
                500.0f, mLocationListener);
        setTitle(getString(R.string.warningsActivityTitle));
        setContentView(R.layout.activity_warnings);

        etCity = (EditText) findViewById(R.id.etCity);
        etN = (EditText) findViewById(R.id.etN);
        etE = (EditText) findViewById(R.id.etE);
        tvResult = (TextView) findViewById(R.id.tv_result);
        b = (Button) findViewById(R.id.btnWarnings);
        LinearLayout ll = (LinearLayout)findViewById(R.id.warLayout);
        Animation animation = new Animation(this);
        animation.animation();
        lastChange = System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_warnings, menu);
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

    public void btnWaringsClickAction(View view) throws XmlPullParserException {
        tvResult.setText(announce);
        if (!checkInternetConnection()) {
            Toast.makeText(this, getString(R.string.noConnection), Toast.LENGTH_SHORT).show();
            return;
        }
        if (etCity.getText().length() == 0 &&
                etE.getText().length() == 0 &&
                etN.getText().length() == 0) {
            Toast.makeText(this, getString(R.string.input), Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if(etCity.getText().length() > 0) {
                Thread t1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            coords = sc.getCoordinates(etCity.getText().toString());
                        } catch (IOException | XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t1.start();
                t1.join();
                if (coords.equals(0f, 0f)) {
                    tvResult.setText(getString(R.string.locationNotFound));
                    return;
                } else {
                    etN.setText(Float.toString(coords.y));
                    etE.setText(Float.toString(coords.x));
                }
            }
            if(etN.getText().length() == 0 || etE.getText().length() == 0)
                etN.setError(getString(R.string.blankField));

            coords = new PointF(Float.parseFloat(etE.getText().toString()),
                    Float.parseFloat(etN.getText().toString()));
            //-20 to 40
            if(coords.x > 40 || coords.x < -20) {
                etE.setError(getString(R.string.coordsBounds));
                return;
            }
            //23 to 69
            if(coords.y > 69 || coords.y < 23) {
                etN.setError(getString(R.string.coordsBounds));
                return;
            }

            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        warnings = sc.getWarnings(coords);
                    } catch (IOException | XmlPullParserException e) {
                        e.printStackTrace();
                    }
                }
            });
            t1.start();
            t1.join();
            tvResult.setText(warningsToString());
            etN.clearFocus();
            etE.clearFocus();
            etCity.clearFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etCity.getWindowToken(), 0);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean checkInternetConnection() {
        MobileInternetConnectionDetector m = new MobileInternetConnectionDetector(this);
        WIFIInternetConnectionDetector w = new WIFIInternetConnectionDetector(this);
        return !(!m.checkMobileInternetConn() && !w.checkMobileInternetConn());
    }

    private String warningsToString() {
        if (warnings.getMroz() == 0 &&
                warnings.getUpal() == 0 &&
                warnings.getWiatr() == 0 &&
                warnings.getOpad() == 0 &&
                warnings.getBurza() == 0 &&
                warnings.getTraba() == 0)
            return getString(R.string.warningsActivityHazardsNotFound);
        String hazards = "";
        //====================================
        if(warnings.getMroz() == 1) {
            hazards += getString(R.string.warningsActivityFirstHazard) + " " + getString(R.string.warningsActivityFrost) + "\n" + getString(R.string.From) + warnings.getMroz_od_dnia() + "\n" + getString(R.string.To) + warnings.getMroz_do_dnia() + "\n";
        } else if (warnings.getMroz() == 2) {
            hazards += getString(R.string.warningsActivitySecondHazard) + " " + getString(R.string.warningsActivityFrost) + "\n" + getString(R.string.From) + warnings.getMroz_od_dnia() + "\n" + getString(R.string.To) + warnings.getMroz_do_dnia() + "\n";
        } else if (warnings.getMroz() == 3) {
            hazards += getString(R.string.warningsActivityThirdHazard) + " " + getString(R.string.warningsActivityFrost) + "\n" + getString(R.string.From) + warnings.getMroz_od_dnia() + "\n" + getString(R.string.To) + warnings.getMroz_do_dnia() + "\n";
        }
        //====================================
        if(warnings.getUpal() == 1) {
            hazards += getString(R.string.warningsActivityFirstHazard) + " " + getString(R.string.warningsActivityHeat) + "\n" + getString(R.string.From) + warnings.getUpal_od_dnia() + "\n" + getString(R.string.To) + warnings.getUpal_do_dnia() + "\n";
        } else if (warnings.getUpal() == 2) {
            hazards += getString(R.string.warningsActivitySecondHazard) + " " + getString(R.string.warningsActivityHeat) + "\n" + getString(R.string.From) + warnings.getUpal_od_dnia() + "\n" + getString(R.string.To) + warnings.getUpal_do_dnia() + "\n";
        } else if (warnings.getUpal() == 3) {
            hazards += getString(R.string.warningsActivityThirdHazard) + " " + getString(R.string.warningsActivityHeat) + "\n" + getString(R.string.From) + warnings.getUpal_od_dnia() + "\n" + getString(R.string.To) + warnings.getUpal_do_dnia() + "\n";
        }
        //====================================
        if(warnings.getWiatr() == 1) {
            hazards += getString(R.string.warningsActivityFirstHazard) + " " + getString(R.string.warningsActivityWind) + "\n" + getString(R.string.From) + warnings.getWiatr_od_dnia() + "\n" + getString(R.string.To) + warnings.getWiatr_do_dnia() + "\n";
        } else if (warnings.getWiatr() == 2) {
            hazards += getString(R.string.warningsActivitySecondHazard) + " " + getString(R.string.warningsActivityWind) + "\n" + getString(R.string.From) + warnings.getWiatr_od_dnia() + "\n" + getString(R.string.To) + warnings.getWiatr_do_dnia() + "\n";
        } else if (warnings.getWiatr() == 3) {
            hazards += getString(R.string.warningsActivityThirdHazard) + " " + getString(R.string.warningsActivityWind) + "\n" + getString(R.string.From) + warnings.getWiatr_od_dnia() + "\n" + getString(R.string.To) + warnings.getWiatr_do_dnia() + "\n";
        }
        //====================================
        if(warnings.getOpad() == 1) {
            hazards += getString(R.string.warningsActivityFirstHazard) + " " + getString(R.string.warningsActivityDrop) + "\n" + getString(R.string.From) + warnings.getOpad_od_dnia() + "\n" + getString(R.string.To) + warnings.getOpad_do_dnia() + "\n";
        } else if (warnings.getOpad() == 2) {
            hazards += getString(R.string.warningsActivitySecondHazard) + " " + getString(R.string.warningsActivityDrop) + "\n" + getString(R.string.From) + warnings.getOpad_od_dnia() + "\n" + getString(R.string.To) + warnings.getOpad_do_dnia() + "\n";
        } else if (warnings.getOpad() == 3) {
            hazards += getString(R.string.warningsActivityThirdHazard) + " " + getString(R.string.warningsActivityDrop) + "\n" + getString(R.string.From) + warnings.getOpad_od_dnia() + "\n" + getString(R.string.To) + warnings.getOpad_do_dnia() + "\n";
        }
        //====================================
        if(warnings.getBurza() == 1) {
            hazards += getString(R.string.warningsActivityFirstHazard) + " " + getString(R.string.warningsActivityStorm) + "\n" + getString(R.string.From) + warnings.getBurza_od_dnia() + "\n" + getString(R.string.To) + warnings.getBurza_do_dnia() + "\n";
        } else if (warnings.getBurza() == 2) {
            hazards += getString(R.string.warningsActivitySecondHazard) + " " + getString(R.string.warningsActivityStorm) + "\n" + getString(R.string.From) + warnings.getBurza_od_dnia() + "\n" + getString(R.string.To) + warnings.getBurza_do_dnia() + "\n";
        } else if (warnings.getBurza() == 3) {
            hazards += getString(R.string.warningsActivityThirdHazard) + " " + getString(R.string.warningsActivityStorm) + "\n" + getString(R.string.From) + warnings.getBurza_od_dnia() + "\n" + getString(R.string.To) + warnings.getBurza_do_dnia() + "\n";
        }
        //====================================
        if(warnings.getTraba() == 1) {
            hazards += getString(R.string.warningsActivityFirstHazard) + " " + getString(R.string.warningsActivityWhirlwind) + "\n" + getString(R.string.From) + warnings.getTraba_od_dnia() + "\n" + getString(R.string.To) + warnings.getTraba_do_dnia() + "\n";
        } else if (warnings.getTraba() == 2) {
            hazards += getString(R.string.warningsActivitySecondHazard) + " " + getString(R.string.warningsActivityWhirlwind) + "\n" + getString(R.string.From) + warnings.getTraba_od_dnia() + "\n" + getString(R.string.To) + warnings.getTraba_do_dnia() + "\n";
        } else if (warnings.getTraba() == 3) {
            hazards += getString(R.string.warningsActivityThirdHazard) + " " + getString(R.string.warningsActivityWhirlwind) + "\n" + getString(R.string.From) + warnings.getTraba_od_dnia() + "\n" + getString(R.string.To) + warnings.getTraba_do_dnia() + "\n";
        }
        return hazards;
    }

    public void btnStormsClickAction(View view) throws XmlPullParserException {
        if (!checkInternetConnection()) {
            Toast.makeText(this, getString(R.string.noConnection), Toast.LENGTH_SHORT).show();
            return;
        }
        btnWaringsClickAction(view);
        if (etN.getText().length() == 0 || etE.getText().length() == 0) {
            showAlertDialog(this, getString(R.string.noData), getString(R.string.coordsOrLocation), true);
            return;
        }
        Intent showStorms = new Intent(this, StormsActivity.class);
        showStorms.putExtra("COORDS", coords);
        showStorms.putExtra("CITY", etCity.getText().toString());
        startActivity(showStorms);
    }

    private WarningsActivity warningsActivity = this;
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            myLocation = location;
            double latitude = myLocation.getLatitude();
            double longitude = myLocation.getLongitude();
            coords = new PointF();
            coords.x = (float) longitude;
            coords.y = (float) latitude;

            if (System.currentTimeMillis()-lastChange > 30000) {
                lastChange = System.currentTimeMillis();
                String ns = Context.NOTIFICATION_SERVICE;
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

                Thread t1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            warnings = sc.getWarnings(coords);
                        } catch (IOException | XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t1.start();
                try {
                    t1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (warnings.getMroz() == 0 &&
                        warnings.getUpal() == 0 &&
                        warnings.getWiatr() == 0 &&
                        warnings.getOpad() == 0 &&
                        warnings.getBurza() == 0 &&
                        warnings.getTraba() == 0)
                    return;

                int icon = R.mipmap.ic_launcher;
                CharSequence tickerText = getString(R.string.newHazard); // ticker-text
                long when = System.currentTimeMillis();
                Context context = getApplicationContext();
                CharSequence contentTitle = getString(R.string.newHazard);
                CharSequence contentText = getString(R.string.newHazard);
                Intent notificationIntent = new Intent(warningsActivity, NotifyMessage.class);
                PendingIntent contentIntent = PendingIntent.getActivity(warningsActivity, 0, notificationIntent, 0);
                Notification notification = new Notification(icon, tickerText, when);
                notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

                mNotificationManager.notify(1, notification);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public void btnLocalizationClickAction(View view) {
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null
                    || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        if (bestLocation != null) {
            double latitude = bestLocation.getLatitude();
            double longitude = bestLocation.getLongitude();
            coords = new PointF();
            coords.x = (float) longitude;
            coords.y = (float) latitude;
            etCity.setText("");
            etE.setText(Double.toString(longitude));
            etN.setText(Double.toString(latitude));
        }
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void changeBackground(final int resId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayout ll = (LinearLayout) findViewById(R.id.warLayout);
                if (ll != null)
                    ll.setBackgroundResource(resId);
            }
        });
    }
}