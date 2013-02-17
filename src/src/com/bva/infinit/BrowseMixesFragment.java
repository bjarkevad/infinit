package com.bva.infinit;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bva.etracks.*;

public class BrowseMixesFragment extends Fragment {

	Button testButton;
	
	public BrowseMixesFragment(){

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_browse_mixes, container, false);
		testButton = (Button) rootView.findViewById(R.id.button1);
		new SearchQueryTask().execute("chillstep");
		//new GetMixesTask().execute("test");
		return rootView;
	}
	
	class GetMixesTask extends AsyncTask<String, Integer, ArrayList<ETracksMix>> {

		@Override
		protected ArrayList<ETracksMix> doInBackground(String... url) {
			ArrayList<ETracksMix> mixes = new ArrayList<ETracksMix>();
			mixes.add(ETracksAPI.GetNextMix(1234));
			return mixes;
		}
				
		@Override
		protected void onPostExecute(ArrayList<ETracksMix> resultMixes) {
			//testButton.setText(resultMixes.get(0).name);
		}			
	}
	
	class SearchQueryTask extends AsyncTask<String, Integer, ETracksQueryResponse> {

		@Override
		protected ETracksQueryResponse doInBackground(String... params) {
			return ETracksAPI.SearchMixes(params[0]);
		}
		
		@Override
		protected void onPostExecute(ETracksQueryResponse response) {
			testButton.setText(Integer.toString(response.mixes.size()));
		}		
		
	}
}
