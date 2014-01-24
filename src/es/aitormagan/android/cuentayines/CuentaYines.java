package es.aitormagan.android.cuentayines;

import es.aitormagan.cuentayines.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class CuentaYines extends Activity {
	
	//Preferences
	private static final String PREFS_NAME = "CuentaYinesPrefs";
	private static final String YINES_COUNT_PREF = "yinesCount";
	
	//Intern variables
	private int yines = 0;
	
	//UI elements
	private TextView yinesCountView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta_yines);
        
        // Restore preferences
        System.out.println(this.yines);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
       	this.yines = settings.getInt(YINES_COUNT_PREF, 0);
        
        this.initializeView();	//Initialize view
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cuenta_yines, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.deduct_yin_menu:
            	this.yines = this.yines > 0 ? this.yines - 1 : this.yines;
                break;
            case R.id.reset_yines_menu:
            	this.yines = 0;
                break;
        }
        
        updateYinesView();
        return true;
    }
    
    @Override
    public void onBackPressed() {
    	
    	//Safe current yines count
    	this.safeYines();
    	
    	//Execute default action
    	super.onBackPressed();
    }
    
    @Override
    protected void onStop() {

    	//Safe current yines count
    	this.safeYines();
    	
    	//Execute default action
    	super.onStop();
      
    }
    
    private void initializeView() {
    	this.yinesCountView = (TextView) findViewById(R.id.yinesCount);
    	updateYinesView();
    	
    	//Increase count on tap
    	this.yinesCountView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				yines++;
				updateYinesView();
			}
		});
    }
    
    private void updateYinesView() {
    	this.yinesCountView.setText(Integer.valueOf(this.yines).toString());
    }
    
    private void safeYines() {
        //Save preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(YINES_COUNT_PREF, this.yines);
        
        // Commit the edits!
        editor.commit();
    }
    
}
