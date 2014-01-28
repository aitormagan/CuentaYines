package es.aitormagan.android.cuentayines;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import es.aitormagan.cuentayines.android.R;

import android.content.Context;
import android.widget.Toast;

public class ProductStorage {

	private Context context;
	private static final String PRODUCTS_FILE = "productsFile.bin";

	public ProductStorage(Context context) {
		this.context = context;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Product> getProducts() {
		ArrayList<Product> products = new ArrayList<Product>();

		try {
			FileInputStream fis = context.openFileInput(PRODUCTS_FILE);
			ObjectInputStream ois = new ObjectInputStream(fis);
			products = (ArrayList<Product>) ois.readObject();
		} catch (Exception e) {
			//Nothing to do...
		}

		return products;
	}

	public void saveProducts(ArrayList<Product> products) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(context.
					openFileOutput(PRODUCTS_FILE, Context.MODE_PRIVATE));
			oos.writeObject(products);
		} catch (Exception e) {
			Toast.makeText(context, R.string.error_empty_price_name, Toast.LENGTH_LONG).show();
		}
	}
}
