package com.android.inmoprueba1;

import java.util.List;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

//Clase auxiliar para crear el viewpager de resultado

public class CustomPageAdapter extends PagerAdapter {

	private final List<String> urls;
	private final Context context;

	public CustomPageAdapter(Context context, List<String> urls) {

		super();
		this.urls = urls;
		this.context = context;

	}

	@Override
	public void destroyItem(View collection, int position, Object view) {

		((ViewPager) collection).removeView((LinearLayout) view);

	}

	@Override
	public void finishUpdate(View arg0) {

		// TODO Auto-generated method stub

	}

	@Override
	public int getCount() {

		return urls.size();

	}

	@Override
	public Object instantiateItem(View collection, int position) {

		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(1);

		ImageView image = new ImageView(context);
		image = CargarImagenes.downloadImg(image, urls.get(position));

		linearLayout.addView(image);

		((ViewPager) collection).addView(linearLayout, 0);

		return linearLayout;

	}

	@Override
	public boolean isViewFromObject(View view, Object object) {

		return view == ((LinearLayout) object);

	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public void startUpdate(View arg0) {
		// TODO Auto-generated method stub

	}
}// end class
