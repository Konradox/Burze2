package pl.edu.agh.metal.krugala.burze;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends ActivityHelper implements AdapterView.OnItemSelectedListener {

    private static MainActivity mainActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Menu");
        WIFIInternetConnectionDetector wifiCheck = new WIFIInternetConnectionDetector(getApplicationContext());
        MobileInternetConnectionDetector gsmCheck = new MobileInternetConnectionDetector(getApplicationContext());
        if(!wifiCheck.checkMobileInternetConn() && !gsmCheck.checkMobileInternetConn()) {
            showAlertDialog(this, getString(R.string.noInternetConnection), getString(R.string.internetConnectionIsNeeded), false);
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    WIFIInternetConnectionDetector wifiCheck = new WIFIInternetConnectionDetector(getApplicationContext());
                    MobileInternetConnectionDetector gsmCheck = new MobileInternetConnectionDetector(getApplicationContext());
                    while (!wifiCheck.checkMobileInternetConn() && !gsmCheck.checkMobileInternetConn()) {}
                    makeToast(getString(R.string.connected), Toast.LENGTH_LONG);
                }
            });
            t1.start();
        }
        Animation animation = new Animation(this);
        animation.animation();

        //===============================================================
        Spinner spinner = (Spinner)findViewById(R.id.spinnerCountry);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.country_array, R.layout.spinner_my);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        //===============================================================
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

    private void makeToast(final String message, final int duration) {
        mainActivity = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mainActivity.getBaseContext(), message, duration).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void btnWarningsClickAction(View view) {
        Intent showWarnings = new Intent(this, WarningsActivity.class);
        startActivity(showWarnings);
    }

    /**
     * Function to display simple Alert Dialog
     * @param context - application context
     * @param title - alert dialog title
     * @param message - alert message
     * @param status - success/failure (used to set icon)
     * */
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

    public void btnPolandClickAction(View view) {
        if (!checkInternetConnection()) {
            Toast.makeText(this, getString(R.string.noConnection), Toast.LENGTH_SHORT).show();
            return;
        }
//        if (!((CheckBox)findViewById(R.id.checkBoxAnimated)).isChecked()) {
        Intent plMap = new Intent(this, MapActivity.class);
        plMap.putExtra("URL", "https://burze.dzis.net/burze.gif");
        startActivity(plMap);
//        } else {
//            Intent plMap = new Intent(this, MapActivity.class);
//            plMap.putExtra("URL", "https://burze.dzis.net/burze_anim.gif");
//            startActivity(plMap);
//        }
    }

    public void btnEuropeClickAction(View view) {
        if (!checkInternetConnection()) {
            Toast.makeText(this, getString(R.string.noConnection), Toast.LENGTH_SHORT).show();
            return;
        }
//        if (!((CheckBox)findViewById(R.id.checkBoxAnimated)).isChecked()) {
        Intent euMap = new Intent(this, MapActivity.class);
        euMap.putExtra("URL", "https://burze.dzis.net/burze_europa.gif");
        startActivity(euMap);
//        } else {
//            Intent euMap = new Intent(this, MapActivity.class);
//            euMap.putExtra("URL", "https://burze.dzis.net/burze_europa_anim.gif");
//            startActivity(euMap);
//        }
    }

    private boolean checkInternetConnection() {
        MobileInternetConnectionDetector m = new MobileInternetConnectionDetector(this);
        WIFIInternetConnectionDetector w = new WIFIInternetConnectionDetector(this);
        return !(!m.checkMobileInternetConn() && !w.checkMobileInternetConn());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (!checkInternetConnection()) {
            Toast.makeText(this, getString(R.string.noConnection), Toast.LENGTH_SHORT).show();
            return;
        }
        String url = null;
        if(position == 0)
            return;
        else if(position == 1) {
            if (true){//!((CheckBox)findViewById(R.id.checkBoxAnimated)).isChecked()) {
                url = "https://burze.dzis.net/olujno_karta_hrvatske.gif";
            } else {
                url = "https://burze.dzis.net/olujno_karta_hrvatske_animacija.gif";
            }
        } else if(position == 2) {
            if (true){//!((CheckBox)findViewById(R.id.checkBoxAnimated)).isChecked()) {
                url = "https://burze.dzis.net/stormy_mapa_ceska_republika.gif";
            } else {
                url = "https://burze.dzis.net/stormy_mapa_ceska_republika_animace.gif";
            }
        } else if(position == 3) {
            if (true){//!((CheckBox)findViewById(R.id.checkBoxAnimated)).isChecked()) {
                url = "https://burze.dzis.net/stormachtige_kaart_nederland.gif";
            } else {
                url = "https://burze.dzis.net/stormachtige_kaart_nederland_levendig.gif";
            }
        } else if(position == 4) {
            if (true){//!((CheckBox)findViewById(R.id.checkBoxAnimated)).isChecked()) {
                url = "https://burze.dzis.net/sturm_deutschland.gif";
            } else {
                url = "https://burze.dzis.net/sturm_deutschland_lebhaft.gif";
            }
        } else if(position == 5) {
            if (true){//!((CheckBox)findViewById(R.id.checkBoxAnimated)).isChecked()) {
                url = "https://burze.dzis.net/burka_mapa_slovensko.gif";
            } else {
                url = "https://burze.dzis.net/burka_mapa_slovensko_animacie.gif";
            }
        } else if(position == 6) {
            if (true){//!((CheckBox)findViewById(R.id.checkBoxAnimated)).isChecked()) {
                url = "https://burze.dzis.net/slovenija_map_strele.gif";
            } else {
                url = "https://burze.dzis.net/slovenija_map_strele_animacija.gif";
            }
        }

        Intent map = new Intent(this, MapActivity.class);
        map.putExtra("URL", url);
        startActivity(map);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
