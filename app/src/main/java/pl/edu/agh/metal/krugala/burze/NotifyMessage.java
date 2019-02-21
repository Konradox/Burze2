package pl.edu.agh.metal.krugala.burze;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Konrad on 2015-06-23.
 */
public class NotifyMessage extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView txt=new TextView(this);

        txt.setText("Activity after click on notification");
        setContentView(txt);
    }
}
