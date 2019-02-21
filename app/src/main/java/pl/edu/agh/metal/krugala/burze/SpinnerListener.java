package pl.edu.agh.metal.krugala.burze;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by Konrad on 2015-06-22.
 */
public class SpinnerListener implements AdapterView.OnItemSelectedListener {

    private StormsActivity stormsActivity;

    public SpinnerListener(StormsActivity stormsActivity) {
        this.stormsActivity = stormsActivity;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selected = parent.getItemAtPosition(position).toString();
        selected = selected.substring(0, selected.length()-2);
        int radius = Integer.parseInt(selected);
        stormsActivity.findStorms(radius);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
