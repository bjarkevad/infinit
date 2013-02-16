package com.bva.etracks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


import com.bva.etracks.EtracksMix;


//APIKEY:"2caf759ee096d026c55cbc5e280b6cfacbb4c6f2"

public class ETracksAPI {
	public static String APIKEY = "2caf759ee096d026c55cbc5e280b6cfacbb4c6f2";
	
	//returns all text from Reader as String
	String readAllText(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		
		while((cp = rd.read()) != -1)
			sb.append((char) cp);
		
		return sb.toString();
	}
	
	//returns JSONObject returned from URL
	JSONObject readJSONObjectFromUrl(String url) throws MalformedURLException, IOException, JSONException {
		InputStream is = new URL(url + "?api_key=" + APIKEY).openStream();
		try {		
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAllText(rd);		
			return new JSONObject(jsonText);
		} finally {
			is.close();
		}
	}
	
	public EtracksMix GetNextMix(int mixId) {
		
		JSONObject parent = new JSONObject();
		JSONObject nextMix = new JSONObject();
		EtracksMix resultMix = new EtracksMix();
		
		resultMix.childMix.id = -1;
					
		try {
			parent = readJSONObjectFromUrl("http://8tracks.com/sets/460486803/next_mix.jsonp?mix_id=" + Integer.toString(mixId) + "?api_key=" + APIKEY);
		} catch (Exception e) { 
			android.util.Log.w("readJSONObjectFromUrl exception", e.toString());
			return resultMix;
		}				
		
		try {			
			nextMix = parent.getJSONObject("next_mix");
			
			resultMix.childMix.id = nextMix.getInt("id");
			resultMix.childMix.name = nextMix.getString("name");
			resultMix.childMix.description = nextMix.getString("description");
			resultMix.childMix.plays_count = nextMix.getInt("plays_count");
			resultMix.childMix.likes_count = nextMix.getInt("likes_count");
			resultMix.childMix.path = nextMix.getString("path");
			resultMix.childMix.cover_url = nextMix.getJSONObject("cover_urls").getString("max1024");
			resultMix.childMix.tag_list_cache = nextMix.getString("tag_list_cache");
			resultMix.childMix.first_published_at = nextMix.getString("first_published_at");
			resultMix.childMix.liked_by_current_user = nextMix.getBoolean("liked_by_current_user");
			resultMix.childMix.nsfw = nextMix.getBoolean("nsfw");
			resultMix.childMix.duration = nextMix.getInt("duration");
			resultMix.childMix.tracks_count = nextMix.getInt("tracks_count");
			resultMix.status = nextMix.getString("status");
			
			JSONObject user = nextMix.getJSONObject("user");
			
			resultMix.childMix.user.id = user.getInt("id");
			resultMix.childMix.user.login = user.getString("login");
			resultMix.childMix.user.avatar_url = user.getString("avatar_url");
			resultMix.childMix.user.followed_by_current_user = user.getBoolean("followed_by_current_user");		
			
			resultMix.smart_id = parent.getString("smart_id");
			resultMix.status = parent.getString("status");
			//TODO: errors, notices, api_warning, api_version
			
		} catch(Exception e) {
			android.util.Log.w("jsonMix.getString() exception", e.toString());
			return resultMix;
		}
				
		return resultMix;
	}
}
