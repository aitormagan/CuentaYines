package es.aitormagan.android.cuentayines;

import java.util.ArrayList;

import es.aitormagan.cuentayines.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CuentaYines extends Activity {

	//Preferences
	//private static final String PREFS_NAME = "CuentaYinesPrefs";

	//Internal Elements
	private ArrayList<Category> categories = new ArrayList<Category>();
	private CategoryAdapter categoryAdapter;

	//UI elements
	private ListView categoriesView;
	private TextView totalView;
	private TextView addNewCatsView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cuenta_yines);

		// Restore preferences
		//SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

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
		case R.id.reset_counts_menu:
			for (Category category: categories) {
				category.setCountToZero();
			}
			updateView();
			break;
		case R.id.delete_all_categories_menu:
			categories.clear();
			updateView();
			break;
		case R.id.new_category_menu:
			editOrCreateCategory(null);
			break;
		}

		return true;
	}

	/*@Override
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

	}*/

	private void editOrCreateCategory(final Category category) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Get the layout inflater
		LayoutInflater inflater = this.getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.new_category, null);
		final EditText categoryNameView = (EditText) dialogView.findViewById(R.id.new_category_name);
		final EditText categoryPriceView = (EditText) dialogView.findViewById(R.id.new_category_price);
		final EditText categoryPeopleView = (EditText) dialogView.findViewById(R.id.new_category_people);

		if (category != null) {
			categoryNameView.setText(category.getName());
			categoryPriceView.setText(Float.valueOf(category.getPrice()).toString());
			categoryPeopleView.setText(Integer.valueOf(category.getPeople()).toString());
		}

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setTitle(R.string.new_category)
		.setView(dialogView)
		// Add action buttons
		.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {

				String categoryName = categoryNameView.getText().toString();
				String categoryPriceStr = categoryPriceView.getText().toString();
				String categoryPeopleStr = categoryPeopleView.getText().toString();

				if (categoryName.equals("") ) {
					Toast.makeText(CuentaYines.this, R.string.error_empty_cat_name, Toast.LENGTH_LONG).show();
				} else if (categoryPriceStr.equals("")) {
					Toast.makeText(CuentaYines.this, R.string.error_empty_price_name, Toast.LENGTH_LONG).show();
				} else {
					
					if (categoryPeopleStr.equals("")) {
						categoryPeopleStr = "1";
					}

					try {

						int categoryPeople = Integer.valueOf(categoryPeopleStr);
						float categoryPrice = Float.valueOf(categoryPriceStr);

						if (categoryPeople <= 0) {
							Toast.makeText(CuentaYines.this, R.string.error_zero_people, Toast.LENGTH_LONG).show();
						} else {
							if (category != null) {
								category.setName(categoryName);
								category.setPrice(categoryPrice);
								category.setPeople(categoryPeople);
							} else {
								categories.add(new Category(categoryName, categoryPrice, categoryPeople));
							}

							//View need to be updated
							updateView();
						}

					} catch (Exception e) {
						Toast.makeText(CuentaYines.this, R.string.error_unexpected_input, Toast.LENGTH_LONG).show();
					}


				}
			}
		})
		.setNegativeButton(R.string.cancel_string, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});   

		builder.show();
	}

	private void showOrHideAddNewCategoriesView() {
		if (categories.size() == 0) {
			addNewCatsView.setVisibility(View.VISIBLE);
			totalView.setVisibility(View.INVISIBLE);
			categoriesView.setVisibility(View.INVISIBLE);
		} else {
			addNewCatsView.setVisibility(View.INVISIBLE);
			totalView.setVisibility(View.VISIBLE);
			categoriesView.setVisibility(View.VISIBLE);
		}
	}

	private void initializeView() {

		//Get Views
		categoriesView = (ListView) findViewById(R.id.categoriesView);
		totalView = (TextView) findViewById(R.id.totalView);
		addNewCatsView = (TextView) findViewById(R.id.addNewCategoriesView);




		//FIXME: Load from original source. Remove
		categories.add(new Category("Yin", 1.2f));
		categories.add(new Category("Tercio", 1.5f));
		categories.add(new Category("Cocacola", 1.5f));
		categories.add(new Category("Cubata", 4f));
		categories.add(new Category("Bravas", 4f, 4));

		//Asociar el adapter de la lista
		categoryAdapter = new CategoryAdapter(this, this.categories);
		categoriesView.setAdapter(categoryAdapter);

		//Añadir listener para la lista
		categoriesView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) { 
				final Category category = (Category) categoriesView.getItemAtPosition(position);    
				category.incrementCount();
				updateView();
			}
		});

		categoriesView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id) {
				final Category category = (Category) categoriesView.getItemAtPosition(position);    

				AlertDialog.Builder builder = new AlertDialog.Builder(CuentaYines.this);
				builder.setTitle(R.string.options);

				final CharSequence[] items = { getString(R.string.decrease_one), 
						getString(R.string.set_to_zero), getString(R.string.remove_category),
						getString(R.string.edit_category) }; 

				builder.setTitle(R.string.options)
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch(which) {
						case 0:
							category.decreaseCount();
							break;
						case 1:
							category.setCountToZero();
							break;
						case 2:
							categories.remove(category);
							break;
						case 3:
							editOrCreateCategory(category);
							break;
						}

						updateView();

					}
				});

				builder.show();

				return true;
			}
		});
		
		//Show correct views
		showOrHideAddNewCategoriesView();
		updateView();
	}

	private void updateView() {

		float total = 0;
		for (Category category: categories) {
			total += category.getTotalPrice();
		}

		showOrHideAddNewCategoriesView();

		categoryAdapter.notifyDataSetChanged();
		totalView.setText(String.format(getString(R.string.total), total, "€"));

	}


	private void save() {
		/*//Save preferences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(YINES_COUNT_PREF, yines);
		editor.putFloat(YINES_PRICE_PREF, yinPrice);

		// Commit the edits!
		editor.commit();*/
	}

}
