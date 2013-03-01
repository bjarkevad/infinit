package com.bva.etracks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.bva.etracks.ETracksMix;

//APIKEY:"2caf759ee096d026c55cbc5e280b6cfacbb4c6f2"

public class ETracksAPI {
	static String APIKEY = "?api_key=2caf759ee096d026c55cbc5e280b6cfacbb4c6f2";
	static String PER_PAGE = "?per_page=20";

	public static class SetBitmapFromUrlTask extends AsyncTask<String, Integer, Bitmap> {

		Bitmap bitmap;
		
		public SetBitmapFromUrlTask(Bitmap _bitmap) {
			bitmap = _bitmap;
		}
		
		@Override
		protected Bitmap doInBackground(String... params) {
			try {
		        URL url = new URL(params[0]);
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		        connection.setDoInput(true);
		        connection.connect();
		        InputStream input = connection.getInputStream();
		        Bitmap myBitmap = BitmapFactory.decodeStream(input);
		        return myBitmap;
		    } catch (IOException e) {
		        e.printStackTrace();
		        Log.w("Bitmap exception", e.toString());
		        return null;
		    }		
		}
		
			@Override
			protected void onPostExecute(Bitmap _bitmap) {
				bitmap = _bitmap;
		}
	}
	
	static Bitmap GetBitmapFromUrl(String _url) {
		try {
	        URL url = new URL(_url);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        return myBitmap;
	    } catch (IOException e) {
	        e.printStackTrace();
	        Log.w("Bitmap exception", e.toString());
	        return null;
	    }		
	}

	// returns all text from Reader as String
	static String readAllText(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;

		while ((cp = rd.read()) != -1)
			sb.append((char) cp);

		return sb.toString();
	}

	/**
	 *  Returns JSONObject returned from URL.
	 *  APIKEY query is automagically appended
	 * @param url The url of the JSON text
	 * @return returns the found JSON text as a JSONObject
	 */
	static JSONObject readJSONObjectFromUrl(String url) {

		try {
			DefaultHttpClient defaultClient = new DefaultHttpClient();
			HttpGet httpGetRequest = new HttpGet(url + PER_PAGE + APIKEY);
			HttpResponse httpResponse = defaultClient.execute(httpGetRequest);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					httpResponse.getEntity().getContent(), "UTF-8"));
			String json = reader.readLine();

			//JSONObject resultJSON = new JSONObject(json); 
			
			return new JSONObject(json);

		} catch (Exception e) {
			android.util.Log.w("readJSONObjectFromUrl", e.toString());
			return null;
		}
	}
	
	static Boolean GetStatusResponseFromUrl(String url) {
		
		if(readJSONObjectFromUrl(url).optString("status") == "200 OK")
			return true;
		
		return false;
	}
	
	
	
	static ETracksMix GetMixFromJSON(JSONObject jsonMix) {

		ETracksMix resultMix = new ETracksMix();

		resultMix.id = jsonMix.optInt("id");
		resultMix.name = jsonMix.optString("name");
		resultMix.description = jsonMix.optString("description");
		resultMix.plays_count = jsonMix.optInt("plays_count");
		resultMix.likes_count = jsonMix.optInt("likes_count");
		resultMix.path = jsonMix.optString("path");
		resultMix.cover_url = jsonMix.optJSONObject("cover_urls").optString(
				"sq100");
		
		resultMix.cover_image = GetBitmapFromUrl(resultMix.cover_url);
		resultMix.tag_list_cache = jsonMix.optString("tag_list_cache");
		resultMix.first_published_at = jsonMix.optString("first_published_at");
		resultMix.liked_by_current_user = jsonMix
				.optBoolean("liked_by_current_user");
		resultMix.nsfw = jsonMix.optBoolean("nsfw");
		resultMix.duration = jsonMix.optInt("duration");
		resultMix.tracks_count = jsonMix.optInt("tracks_count");

		// user
		JSONObject user = jsonMix.optJSONObject("user");
		if (user != null) {
			resultMix.user.id = user.optInt("id");
			resultMix.user.login = user.optString("login");
			resultMix.user.avatar_url = user.optString("avatar_url");
			resultMix.user.followed_by_current_user = user
					.optBoolean("followed_by_current_user");
		}

		return resultMix;
	}

	static ETracksQueryResponse GetResponseFromJSON(JSONObject jsonResponse) {

		ETracksQueryResponse resultResponse = new ETracksQueryResponse();
		JSONArray jsonMixes = new JSONArray();

		resultResponse.id = jsonResponse.optInt("id");
		resultResponse.path = jsonResponse.optString("path");
		resultResponse.restful_url = jsonResponse.optString("restful_url");
		resultResponse.total_entries = jsonResponse.optInt("total_entries");
		resultResponse.page = jsonResponse.optInt("page");
		resultResponse.per_page = jsonResponse.optInt("per_page");
		resultResponse.next_page = jsonResponse.optInt("next_page");
		resultResponse.previous_page = jsonResponse.optInt("previous_page");
		resultResponse.total_pages = jsonResponse.optInt("total_pages");
		resultResponse.canonical_path = jsonResponse
				.optString("canonical_path");
		resultResponse.name = jsonResponse.optString("name");
		resultResponse.smart_id = jsonResponse.optString("smart_id");
		
		if ((jsonMixes = jsonResponse.optJSONArray("mixes")) == null)
			return resultResponse;

		for (int i = 0; i < jsonMixes.length(); ++i)
			resultResponse.mixes
					.add(GetMixFromJSON(jsonMixes.optJSONObject(i)));

		return resultResponse;
	}
	
	static ETracksQueryResponse GetResponseFromURL(String url) {
		ETracksQueryResponse resultResponse = new ETracksQueryResponse();
		
		resultResponse = GetResponseFromJSON(readJSONObjectFromUrl(url));
		
		return resultResponse;
	}
	
	static ETracksTrackResponse GetTrackResponseFromURL(String url) {
		
		ETracksTrackResponse resultResponse = new ETracksTrackResponse();
		JSONObject jsonResponse = readJSONObjectFromUrl(url);
		JSONObject jsonSetResponse = jsonResponse.optJSONObject("set");
		JSONObject jsonTrackResponse = jsonSetResponse.optJSONObject("track");
		
		resultResponse.status = jsonResponse.optString("status");
		resultResponse.errors = jsonResponse.optString("errors");
		
		if(resultResponse.status != "200 OK")
			return resultResponse;
		
		resultResponse.set.at_beginning = jsonSetResponse.optBoolean("at_beginning");
		resultResponse.set.at_end = jsonSetResponse.optBoolean("at_end");
		resultResponse.set.at_last_track = jsonSetResponse.optBoolean("at_last_track");
		resultResponse.set.skip_allowed = jsonSetResponse.optBoolean("skip_allowed");
		
		resultResponse.set.track.id = jsonTrackResponse.optInt("id");
		
		resultResponse.set.track.name = jsonTrackResponse.optString("name");
		resultResponse.set.track.performer = jsonTrackResponse.optString("performer");
		resultResponse.set.track.release_name = jsonTrackResponse.optString("release_name");
		resultResponse.set.track.year = jsonTrackResponse.optInt("year");
		resultResponse.set.track.url = jsonTrackResponse.optString("url");
		resultResponse.set.track.faved_by_current_user = jsonTrackResponse.optBoolean("faved_by_current_user");
		resultResponse.set.track.buy_link = jsonTrackResponse.optString("buy_link");
		resultResponse.set.track.buy_icon = jsonTrackResponse.optString("buy_icon");
		resultResponse.set.track.you_tube_id = jsonTrackResponse.optString("you_tube_id");
		resultResponse.set.track.you_tube_status = jsonTrackResponse.optString("you_tube_status");
		resultResponse.set.track.play_duration = jsonTrackResponse.optInt("play_duration");		
		
		return resultResponse;
	}
	
	static public ETracksQueryResponse GetMixes() {
		ETracksQueryResponse resultResponse = new ETracksQueryResponse();
		
		resultResponse = GetResponseFromURL("http://8tracks.com/mixes.jsonp");
		 
		return resultResponse;
	}
	

	// TODO: set id
	static public ETracksMix GetNextMix(int mixId) {

		JSONObject nextMix = new JSONObject();
		ETracksMix resultMix = new ETracksMix();
		// Error code
		resultMix.id = -1;

		if ((nextMix = readJSONObjectFromUrl("http://8tracks.com/sets/460486803/next_mix.jsonp?mix_id="
				+ Integer.toString(mixId))) == null)
			return resultMix;

		try {
			nextMix = nextMix.getJSONObject("next_mix");
			// TODO: Query info: smart_id, status, errors, notices, api_warning,
			// api_version
		} catch (Exception e) {
			android.util.Log.w("jsonMix.getString() exception", e.toString());
			// return with id=-1 (error)
			return resultMix;
		}

		resultMix = GetMixFromJSON(nextMix);

		return resultMix;
	}

	static public ETracksQueryResponse SearchMixes(String searchQuery) {

		ETracksQueryResponse resultResponse = new ETracksQueryResponse();

		searchQuery = searchQuery.replace(' ', '+');
		resultResponse = GetResponseFromURL("http://8tracks.com/mixes.jsonp?q=" + searchQuery);

		return resultResponse;
	}

	static public ETracksQueryResponse SearchMixesByTag(String tag) {
		ETracksQueryResponse resultResponse = new ETracksQueryResponse();

		tag = tag.replace(' ', '+');
		resultResponse = GetResponseFromURL("http://8tracks.com/mixes.jsonp?tag=" + tag); 

		return resultResponse;
	}

	static public ETracksQueryResponse NextPage(ETracksQueryResponse previousQuery) {
		if (previousQuery.next_page == -1)
			return null;

		ETracksQueryResponse resultResponse = new ETracksQueryResponse();
		
		resultResponse = GetResponseFromURL(previousQuery.url + "&page=" + previousQuery.next_page);
		resultResponse.url = previousQuery.url;

		return resultResponse;
	}
	
	static public int GetNewPlayToken()
	{	
		JSONObject jsonObject = readJSONObjectFromUrl("http://8tracks.com/sets/new.jsonp");
		
		if(jsonObject.optString("status") != "200 OK")
			return -1;
		return jsonObject.optInt("play-token");
	}
	
	static public ETracksTrackResponse GetTrack(int playToken, int mixId){
		return GetTrackResponseFromURL("http://8tracks.com/sets/" + Integer.toString(playToken) +  "/play.jsonp?mix_id=" + Integer.toString(mixId));
	}

	static public ETracksTrackResponse GetNextTrack(int playToken, int mixId){
		return GetTrackResponseFromURL("http://8tracks.com/sets/" + Integer.toString(playToken) +  "/next.jsonp?mix_id=" + Integer.toString(mixId));
	}

	static public ETracksTrackResponse SkipTrack(int playToken, int mixId){
		return GetTrackResponseFromURL("http://8tracks.com/sets/" + Integer.toString(playToken) +  "/skip.jsonp?mix_id=" + Integer.toString(mixId));
	}
	
	
	static public Boolean ReportSong(String track_id, String mix_id) {
		return GetStatusResponseFromUrl("http://8tracks.com/sets/874076615/report.jsonp?track_id=" + track_id + "&mix_id=" + mix_id);
	}
}
