package pl.edu.agh.metal.krugala.burze;

import java.util.Random;

/**
 * Created by Konrad on 2015-06-23.
 */
public class Animation {

    private ActivityHelper activityHelper;

    public Animation(ActivityHelper activityHelper) {
        this.activityHelper = activityHelper;
    }

    public void animation() {
        Thread ani = new Thread(new Runnable() {
            @Override
            public void run() {
                Random r = new Random();
                while(true) {
                    try {
                        int rand = r.nextInt(1500);
                        if (rand < 100) {
                            activityHelper.changeBackground(R.drawable.background2);
                            Thread.sleep(500);
                            activityHelper.changeBackground(R.drawable.background);
                        } else if(rand < 200) {
                            activityHelper.changeBackground(R.drawable.background3);
                            Thread.sleep(500);
                            activityHelper.changeBackground(R.drawable.background);
                        } else if(rand < 300) {
                            activityHelper.changeBackground(R.drawable.background4);
                            Thread.sleep(500);
                            activityHelper.changeBackground(R.drawable.background);
                        } else if(rand < 400) {
                            activityHelper.changeBackground(R.drawable.background5);
                            Thread.sleep(500);
                            activityHelper.changeBackground(R.drawable.background);
                        } else if(rand < 500) {
                            activityHelper.changeBackground(R.drawable.background6);
                            Thread.sleep(500);
                            activityHelper.changeBackground(R.drawable.background);
                        } else if(rand < 600) {
                            activityHelper.changeBackground(R.drawable.background7);
                            Thread.sleep(500);
                            activityHelper.changeBackground(R.drawable.background);
                        } else if(rand < 700) {
                            activityHelper.changeBackground(R.drawable.background8);
                            Thread.sleep(500);
                            activityHelper.changeBackground(R.drawable.background);
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        ani.start();
    }
}
