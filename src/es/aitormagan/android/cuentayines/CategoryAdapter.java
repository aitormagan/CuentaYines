package es.aitormagan.android.cuentayines;

import java.util.ArrayList;

import es.aitormagan.cuentayines.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CategoryAdapter extends BaseAdapter{

	//Atributos
	private ArrayList<Category> list;
	private Context mContext;
	private LayoutInflater inflator;

	/**
	 * Crear el adaptador de la lista
	 * @param context El contexto de la canción
	 * @param list La lista de canciones
	 */
	public CategoryAdapter(Context context, ArrayList<Category> list){
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
		final Category category = list.get(position);

		if (convertView == null){
			mHolder = new MainListHolder();
			v = inflator.inflate(R.layout.category, null);
			mHolder.countView = (TextView) v.findViewById(R.id.categoryCountView);
			mHolder.nameView = (TextView) v.findViewById(R.id.categoryNameView);
			mHolder.additionalInfoView = (TextView) v.findViewById(R.id.categoryAdditionalInfoView);
			v.setTag(mHolder);
		} else {
			mHolder = (MainListHolder) v.getTag();
		}


		mHolder.countView.setText(Integer.valueOf(category.getCount()).toString());
		mHolder.nameView.setText(category.getName());
		
		if (category.getPeople() == 1) {
			mHolder.additionalInfoView.setText(String.format(mContext.getString(R.string.category_info_1p), 
					category.getPrice(), "€", category.getTotalPrice()));
		} else {
			mHolder.additionalInfoView.setText(String.format(mContext.getString(R.string.category_info), 
					category.getPrice(), "€", category.getPeople(), category.getTotalPrice()));
		}

		return v;
	}

	private class MainListHolder {
		private TextView countView;
		private TextView nameView;
		private TextView additionalInfoView;
	}

}