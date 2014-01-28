package es.aitormagan.android.cuentayines;

import es.aitormagan.cuentayines.android.R;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class About extends Activity {

	private final static String TWITTER_USER_NAME = "AitorMagan";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		TextView aboutView = (TextView) findViewById(R.id.aboutView);
		aboutView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					startActivity(new Intent(Intent.ACTION_VIEW, 
							Uri.parse("twitter://user?screen_name=" + TWITTER_USER_NAME)));
				}catch (Exception e) {
					startActivity(new Intent(Intent.ACTION_VIEW, 
							Uri.parse("https://twitter.com/#!/" + TWITTER_USER_NAME)));
				}				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}

}
