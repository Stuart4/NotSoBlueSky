package com.foobar.jake.notsobluesky;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends Activity {
	private boolean setForRain = false;
	private TextView contentBlurb;
	private Button affirmativeButton;
	private Button negativeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
	contentBlurb = (TextView) findViewById(R.id.statusBlurb);
    	affirmativeButton = (Button) findViewById(R.id.affirmativeButton);
	negativeButton = (Button) findViewById(R.id.negativeButton);
    }

	public void updateRainStatus() {
		boolean rain = rainStatus();
		//GO HOME!!!
		if (setForRain == rain) {
			return;
		}

		//Update interface

		if (rain) {
			contentBlurb.setText(getString(R.string.rain));
			affirmativeButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.affirmativegray, 0);
			negativeButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.negativeblue, 0);
			setForRain = true;
		} else {
			contentBlurb.setText(getString(R.string.notRain));
			affirmativeButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.affirmativeblue, 0);
			negativeButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.negativegray, 0);
			setForRain = false;
		}
	}
	public void affirmativeButtonClicked(View view) {
		Toast.makeText(this, "Response received", Toast.LENGTH_SHORT).show();
		updateRainStatus();
	}

	public void negativeButtonClicked(View view) {
		Toast.makeText(this, "Response received", Toast.LENGTH_SHORT).show();
	}

	private boolean rainStatus() {
		Random rnd = new Random();
		int res = rnd.nextInt(2);
		if (res == 1)
			return true;
		return false;
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
