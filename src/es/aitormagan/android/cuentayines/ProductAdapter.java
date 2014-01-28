package es.aitormagan.android.cuentayines;

import java.util.ArrayList;

import es.aitormagan.cuentayines.android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProductAdapter extends BaseAdapter{

	//Atributos
	private ArrayList<Product> list;
	private Context mContext;
	private LayoutInflater inflator;

	/**
	 * Crear el adaptador de la lista
	 * @param context El contexto de la canción
	 * @param list La lista de canciones
	 */
	public ProductAdapter(Context context, ArrayList<Product> list){
		this.mContext = context;
		this.inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list = list;
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		//Attributes
		View v = convertView;
		final MainListHolder mHolder;
		final Product product = list.get(position);

		if (convertView == null){
			mHolder = new MainListHolder();
			v = inflator.inflate(R.layout.product, null);
			mHolder.countView = (TextView) v.findViewById(R.id.productCountView);
			mHolder.nameView = (TextView) v.findViewById(R.id.productNameView);
			mHolder.pricePerUnitView = (TextView) v.findViewById(R.id.productPricePerUnitView);
			mHolder.peopleView = (TextView) v.findViewById(R.id.productPeopleView);
			mHolder.totalPriceView = (TextView) v.findViewById(R.id.productTotalPriceView);
			v.setTag(mHolder);
		} else {
			mHolder = (MainListHolder) v.getTag();
		}


		mHolder.countView.setText(Integer.valueOf(product.getCount()).toString());
		mHolder.nameView.setText(product.getName());
		mHolder.pricePerUnitView.setText(String.format(mContext.getString(R.string.product_price_per_unit_format), 
				product.getPrice(), "€"));
		mHolder.peopleView.setText(String.format(mContext.getString(R.string.product_people_format), 
				product.getPeople()));
		mHolder.totalPriceView.setText(String.format(mContext.getString(R.string.product_total_price_format), 
				product.getTotalPrice(), "€"));

		if (product.getPeople() == 1) {
			mHolder.peopleView.setVisibility(View.INVISIBLE);
		} else {
			mHolder.peopleView.setVisibility(View.VISIBLE);
		}

		return v;
	}

	private class MainListHolder {
		private TextView countView;
		private TextView nameView;
		private TextView pricePerUnitView;
		private TextView peopleView;
		private TextView totalPriceView;
	}

}