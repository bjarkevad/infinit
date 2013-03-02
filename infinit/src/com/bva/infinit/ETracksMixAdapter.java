package com.bva.infinit;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.bva.etracks.ETracksMix;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ETracksMixAdapter extends BaseAdapter {

	ArrayList<ETracksMix> mixes;
	Context context;
	
	public ETracksMixAdapter(Context _context, List<ETracksMix> _mixes) {
		context = _context;
		mixes = (ArrayList<ETracksMix>) _mixes;
	}
	
	@Override
	public int getCount() {
		return mixes.size();
	}

	@Override
	public Object getItem(int position) {
		return mixes.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO return what? 
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/*Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, r.getDisplayMetrics());*/
		
        MixViewHolder holder;
        RelativeLayout mixViewLayout;
        ETracksMix mix = (ETracksMix) getItem(position);
        
        if(convertView == null)
        {
        	mixViewLayout = new RelativeLayout(context);
        	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	inflater.inflate(R.layout.cardview_mix, mixViewLayout, true);
        	
        	holder = new MixViewHolder();
        	holder.mixImage = (ImageView) mixViewLayout.findViewById(R.id.mixImage);
        	holder.mixName = (TextView) mixViewLayout.findViewById(R.id.mixName);
        	
        	mixViewLayout.setTag(holder);
        }
        else {
        	mixViewLayout = (RelativeLayout) convertView;
        	
        	holder = (MixViewHolder) mixViewLayout.getTag();
        }       
        /*RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mixViewLayout.setLayoutParams(params);*/
 
        holder.mixName.setText(mix.name);
        holder.mixImage.setImageBitmap(mix.cover_image);
    	return mixViewLayout;
	}
	
	protected static class MixViewHolder {
		ImageView mixImage;
		TextView mixName;
	}
	
	
}
