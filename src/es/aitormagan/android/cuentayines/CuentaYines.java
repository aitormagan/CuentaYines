package es.aitormagan.android.cuentayines;

import es.aitormagan.cuentayines.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class CuentaYines extends Activity {

	//Preferences
	private static final String PREFS_NAME = "CuentaYinesPrefs";
	private static final String YINES_COUNT_PREF = "yinesCount";
	private static final String YINES_PRICE_PREF = "yinesPrice";

	//Intern variables
	private int yines = 0;
	private float yinPrice = 0f;

	//UI elements
	private TextView yinesCountView;
	private TextView priceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cuenta_yines);

		// Restore preferences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		yines = settings.getInt(YINES_COUNT_PREF, 0);
		yinPrice = settings.getFloat(YINES_PRICE_PREF, 1.2f);

		initializeView();	//Initialize view

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.cuenta_yines, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.deduct_yin_menu:
			yines = yines > 0 ? yines - 1 : yines;
			updateYinesView();
			break;
		case R.id.reset_yines_menu:
			yines = 0;
			updateYinesView();
			break;
		case R.id.set_yin_price_menu:
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			//Add properties (title and view)
			builder.setTitle(R.string.set_yin_price_string);
			final EditText input = new EditText(this);
			input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
			input.setHint(R.string.yin_price_string);
			builder.setView(input);

			// Add the buttons
			builder.setPositiveButton(R.string.ok_string, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					yinPrice = Float.parseFloat(input.getText().toString());
					updateYinesView();
				}
			});
			builder.setNegativeButton(R.string.cancel_string, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			
			builder.show();

			break;
		}

		return true;
	}

	@Override
	public void onBackPressed() {

		//Safe current yines count
		safeYines();

		//Execute default action
		super.onBackPressed();
	}

	@Override
	protected void onStop() {

		//Safe current yines count
		safeYines();

		//Execute default action
		super.onStop();

	}

	private void initializeView() {

		yinesCountView = (TextView) findViewById(R.id.yinesCountView);
		priceView = (TextView) findViewById(R.id.priceView);

		updateYinesView();

		//Increase count on tap
		yinesCountView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				yines++;
				updateYinesView();
			}
		});
	}

	private void updateYinesView() {
		float totalPrice = yinPrice * yines;	//Get yin price
		
		//Update view
		yinesCountView.setText(Integer.valueOf(yines).toString());
		priceView.setText(String.format(getString(R.string.total_string), totalPrice, "€"));
	}

	private void safeYines() {
		//Save preferences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(YINES_COUNT_PREF, yines);
		editor.putFloat(YINES_PRICE_PREF, yinPrice);

		// Commit the edits!
		editor.commit();
	}

}
