package com.bva.infinit;

import java.util.ArrayList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.bva.etracks.*;



public class BrowseMixesFragment extends Fragment {
	
	enum BrowseMixesType {
		BrowseLatest,
		
	}
	
	BrowseMixesType type = BrowseMixesType.BrowseLatest;

	//Controls
	View rootView;
	GridView mixesGridView;
	ProgressBar progressSpinner;
	
	ETracksMixAdapter adapter;
	
	//Content
	ArrayList<ETracksMix> mixes = new ArrayList<ETracksMix>();
	
	
	public BrowseMixesFragment() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		rootView = inflater.inflate(R.layout.fragment_browse_mixes, container, false);
		
		mixesGridView = (GridView) rootView.findViewById(R.id.mixesGridView);
		progressSpinner = (ProgressBar) rootView.findViewById(R.id.mixesProgressBar);
		adapter = new ETracksMixAdapter(this.getActivity(), mixes);
		mixesGridView.setAdapter(adapter);
		
		// Create a progress bar to display while the list loads
        progressSpinner.setIndeterminate(true);
        mixesGridView.setEmptyView(progressSpinner);

		
		new GetMixesTask().execute("");
		
		return rootView;
	}

	
	/**
	 *  Calls ETracksAPI.GetNextMix(PARAM);
	 *  Usage: new GetNextMixTask().execute("PARAM"); where PARAM = current playlist ID
	 *  
	 * @author Bjarke Vad - bjarke.vad90@gmail.com
	 *
	 */
	//TODO: pass id param
	class GetNextMixTask extends AsyncTask<String, Integer, ArrayList<ETracksMix>> {

		@Override
		protected ArrayList<ETracksMix> doInBackground(String... id) {
			ArrayList<ETracksMix> mixes = new ArrayList<ETracksMix>();
			mixes.add(ETracksAPI.GetNextMix(1234));
			return mixes;
		}
				
		@Override
		protected void onPostExecute(ArrayList<ETracksMix> resultMix) {
			progressSpinner.setProgress(100);
		}			
	}
	
	class SearchQueryTask extends AsyncTask<String, Integer, ETracksQueryResponse> {

		@Override
		protected ETracksQueryResponse doInBackground(String... params) {
			return ETracksAPI.SearchMixes(params[0]);
		}
		
		@Override
		protected void onPostExecute(ETracksQueryResponse response) {
		}		
		
	}
	
	class GetMixesTask extends AsyncTask<String, Integer, ETracksQueryResponse> {
		
		@Override
		protected ETracksQueryResponse doInBackground(String... params) {
			return ETracksAPI.GetMixes();
		}
		
		@Override
		protected void onPostExecute(ETracksQueryResponse response) {
			adapter.mixes = response.mixes;
			adapter.notifyDataSetChanged();
			mixesGridView.invalidateViews();
		}
	}
}
