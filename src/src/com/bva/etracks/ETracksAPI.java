package com.bva.etracks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bva.etracks.ETracksMix;

//APIKEY:"2caf759ee096d026c55cbc5e280b6cfacbb4c6f2"

public class ETracksAPI {
	public static String APIKEY = "?api_key=2caf759ee096d026c55cbc5e280b6cfacbb4c6f2";

	// returns all text from Reader as String
	static String readAllText(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;

		while ((cp = rd.read()) != -1)
			sb.append((char) cp);

		return sb.toString();
	}

	// returns JSONObject returned from URL
	static JSONObject readJSONObjectFromUrl(String url) {

		try {
			DefaultHttpClient defaultClient = new DefaultHttpClient();
			HttpGet httpGetRequest = new HttpGet(url + APIKEY);
			HttpResponse httpResponse = defaultClient.execute(httpGetRequest);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					httpResponse.getEntity().getContent(), "UTF-8"));
			String json = reader.readLine();

			JSONObject resultJSON = new JSONObject(json); 
			
			return new JSONObject(json);

		} catch (Exception e) {
			android.util.Log.w("readJSONObjectFromUrl", e.toString());
			return null;
		}
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
				"max1024");
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

		JSONObject parent = new JSONObject();
		JSONArray jsonMixes = new JSONArray();
		ETracksQueryResponse resultResponse = new ETracksQueryResponse();
		resultResponse.mixes = new ArrayList<ETracksMix>();

		searchQuery = searchQuery.replace(' ', '+');

		resultResponse.url = "http://8tracks.com/mixes.jsonp?q=" + searchQuery;

		if ((parent = readJSONObjectFromUrl(resultResponse.url)) == null)
			return resultResponse;

		resultResponse = GetResponseFromJSON(parent);

		if ((jsonMixes = parent.optJSONArray("mixes")) == null)
			return resultResponse;

		for (int i = 0; i < jsonMixes.length(); ++i)
			resultResponse.mixes
					.add(GetMixFromJSON(jsonMixes.optJSONObject(i)));

		return resultResponse;
	}

	static public ETracksQueryResponse SearchMixesByTag(String tag) {
		JSONObject parent = new JSONObject();
		JSONArray jsonMixes = new JSONArray();
		ETracksQueryResponse resultResponse = new ETracksQueryResponse();
		resultResponse.mixes = new ArrayList<ETracksMix>();

		tag = tag.replace(' ', '+');

		resultResponse.url = "http://8tracks.com/mixes.jsonp?tag=" + tag;

		if ((parent = readJSONObjectFromUrl(resultResponse.url)) == null)
			return resultResponse;

		resultResponse = GetResponseFromJSON(parent);

		if ((jsonMixes = parent.optJSONArray("mixes")) == null)
			return resultResponse;

		for (int i = 0; i < jsonMixes.length(); ++i)
			resultResponse.mixes
					.add(GetMixFromJSON(jsonMixes.optJSONObject(i)));

		return resultResponse;
	}

	static public ETracksQueryResponse NextPage(
			ETracksQueryResponse previousQuery) {
		if (previousQuery.next_page == -1)
			return null;

		JSONObject parent = new JSONObject();
		JSONArray jsonMixes = new JSONArray();
		ETracksQueryResponse resultResponse = new ETracksQueryResponse();
		resultResponse.mixes = new ArrayList<ETracksMix>();

		resultResponse.url = previousQuery.url;

		if ((parent = readJSONObjectFromUrl(previousQuery.url + "&page="
				+ previousQuery.next_page)) == null)
			return resultResponse;

		resultResponse = GetResponseFromJSON(parent);

		if ((jsonMixes = parent.optJSONArray("mixes")) == null)
			return resultResponse;

		for (int i = 0; i < jsonMixes.length(); ++i)
			resultResponse.mixes
					.add(GetMixFromJSON(jsonMixes.optJSONObject(i)));

		return resultResponse;
	}
}
