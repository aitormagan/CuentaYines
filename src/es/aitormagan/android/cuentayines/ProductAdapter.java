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

	private class MainListHolder {
		private TextView countView;
		private TextView nameView;
		private TextView pricePerUnitView;
		private TextView peopleView;
		private TextView totalPriceView;
	}

	//Attributes
	private ArrayList<Product> list;
	private Context context;
	private LayoutInflater inflator;

	/**
	 * 
	 * @param context Activity context
	 * @param list Product list
	 */
	public ProductAdapter(Context context, ArrayList<Product> list){
		this.context = context;
		this.list = list;
		this.inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		View view = convertView;
		final MainListHolder listHolder;
		final Product product = list.get(position);

		//Get view
		if (view == null){
			listHolder = new MainListHolder();
			view = inflator.inflate(R.layout.product, null);
			listHolder.countView = (TextView) view.findViewById(R.id.productCountView);
			listHolder.nameView = (TextView) view.findViewById(R.id.productNameView);
			listHolder.pricePerUnitView = (TextView) view.findViewById(R.id.productPricePerUnitView);
			listHolder.peopleView = (TextView) view.findViewById(R.id.productPeopleView);
			listHolder.totalPriceView = (TextView) view.findViewById(R.id.productTotalPriceView);
			view.setTag(listHolder);
		} else {
			listHolder = (MainListHolder) view.getTag();
		}

		//Set content on the element
		listHolder.countView.setText(Integer.valueOf(product.getCount()).toString());
		listHolder.nameView.setText(product.getName());
		listHolder.pricePerUnitView.setText(String.format(context.getString(R.string.product_price_per_unit_format), 
				product.getPrice(), "€"));
		listHolder.peopleView.setText(String.format(context.getString(R.string.product_people_format), 
				product.getPeople()));
		listHolder.totalPriceView.setText(String.format(context.getString(R.string.product_total_price_format), 
				product.getTotalPrice(), "€"));

		if (product.getPeople() == 1) {
			listHolder.peopleView.setVisibility(View.INVISIBLE);
		} else {
			listHolder.peopleView.setVisibility(View.VISIBLE);
		}

		return view;
	}
}