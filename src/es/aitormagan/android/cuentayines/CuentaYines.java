package es.aitormagan.android.cuentayines;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import es.aitormagan.cuentayines.android.R;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CuentaYines extends SherlockActivity {

	//Storage
	private ProductStorage storage;

	//Internal Elements
	private ArrayList<Product> products = new ArrayList<Product>();
	private ProductAdapter productAdapter;

	//UI elements
	private ListView productsView;
	private TextView totalView;
	private TextView addNewCatsView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cuenta_yines);
		
		//Get products
		storage = new ProductStorage(this);
		products = storage.getProducts();

		initializeView();	//Initialize view
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		initializeView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.cuenta_yines, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle item selection
		switch (item.getItemId()) {
		case R.id.reset_counters:
			for (Product product: products) {
				product.setCountToZero();
			}
			updateView();
			break;
		case R.id.delete_all_products:
			products.clear();
			updateView();
			break;
		case R.id.new_product:
			editOrCreateProduct(null);
			break;
		case R.id.about:
			Intent intent = new Intent(this, About.class);
			startActivity(intent);
		}

		return true;
	}

	@Override
	public void onBackPressed() {
		storage.saveProducts(products);	//Save current products
		super.onBackPressed();			//Default back action
	}

	@Override
	protected void onStop() {
		storage.saveProducts(products);	//Save current products
		super.onStop();					//Default stop action
	}

	private void initializeView() {

		//Get Views
		productsView = (ListView) findViewById(R.id.productsView);
		totalView = (TextView) findViewById(R.id.totalView);
		addNewCatsView = (TextView) findViewById(R.id.addNewCategoriesView);

		//Associate adapter to the list
		productAdapter = new ProductAdapter(this, this.products);
		productsView.setAdapter(productAdapter);

		//Add listener to the list
		productsView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) { 
				final Product product = (Product) productsView.getItemAtPosition(position);    
				product.incrementCount();
				updateView();
			}
		});

		productsView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id) {
				final Product product = (Product) productsView.getItemAtPosition(position);    
				AlertDialog.Builder builder = new AlertDialog.Builder(CuentaYines.this);
				builder.setTitle(R.string.options);

				final CharSequence[] items = { 
						getString(R.string.decrease_one), 	//0
						getString(R.string.set_to_zero), 	//1
						getString(R.string.edit_product), 	//2
						getString(R.string.remove_product)	//3
				};

				builder.setTitle(R.string.options)
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch(which) {
						case 0:	//Decrease one
							product.decreaseCount();
							break;
						case 1:	//Set count to zero
							product.setCountToZero();
							break;
						case 2:	//Edit product
							editOrCreateProduct(product);
							break;
						case 3:	//Remove product
							products.remove(product);
							break;
						}
						updateView();

					}
				});

				builder.show();
				return true;
			}
		});

		updateView();
	}

	private void editOrCreateProduct(final Product product) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.new_product, null);
		final EditText productNameView = (EditText) dialogView.findViewById(R.id.new_product_name);
		final EditText productPriceView = (EditText) dialogView.findViewById(R.id.new_product_price);
		final EditText productPeopleView = (EditText) dialogView.findViewById(R.id.new_product_people);

		if (product != null) {
			productNameView.setText(product.getName());
			productPriceView.setText(Float.valueOf(product.getPrice()).toString());
			productPeopleView.setText(Integer.valueOf(product.getPeople()).toString());
		}

		int titleID = (product == null) ? R.string.new_product : R.string.edit_product;

		//Create dialog
		builder.setTitle(titleID)
		.setView(dialogView)
		.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {

				String productName = productNameView.getText().toString().trim();
				String productPriceStr = productPriceView.getText().toString();
				String productPeopleStr = productPeopleView.getText().toString();

				if (productName.equals("") ) {
					Toast.makeText(CuentaYines.this, R.string.error_empty_cat_name, Toast.LENGTH_LONG).show();
				} else if (productPriceStr.equals("")) {
					Toast.makeText(CuentaYines.this, R.string.error_empty_price_name, Toast.LENGTH_LONG).show();
				} else {

					if (productPeopleStr.equals("")) {
						productPeopleStr = "1";
					}

					try {

						int productPeople = Integer.valueOf(productPeopleStr);
						float productPrice = Float.valueOf(productPriceStr);

						if (productPeople <= 0) {
							Toast.makeText(CuentaYines.this, R.string.error_zero_people, Toast.LENGTH_LONG).show();
						} else {
							Product newProd = new Product(productName, productPrice, productPeople);
							
							if (product == null && !products.contains(newProd)) {
								products.add(newProd);
							} else if (product != null && (!products.contains(newProd) || newProd.equals(product))) {
								product.setName(productName);
								product.setPrice(productPrice);
								product.setPeople(productPeople);
							} else {
								Toast.makeText(CuentaYines.this, R.string.error_duplicate_product, Toast.LENGTH_LONG).show();
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
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});   

		builder.show();
	}

	private void updateView() {

		float total = 0;
		for (Product product: products) {
			total += product.getTotalPrice();
		}

		if (products.size() == 0) {
			addNewCatsView.setVisibility(View.VISIBLE);
			totalView.setVisibility(View.INVISIBLE);
			productsView.setVisibility(View.INVISIBLE);
		} else {
			addNewCatsView.setVisibility(View.INVISIBLE);
			totalView.setVisibility(View.VISIBLE);
			productsView.setVisibility(View.VISIBLE);
		}

		productAdapter.notifyDataSetChanged();
		totalView.setText(String.format(getString(R.string.total_format), total, "€"));

	}
}
