package com.bva.etracks;

import java.util.ArrayList;

public class ETracksQueryResponse {

	public String url;
	public int id;
	public String path;
	public String restful_url;
	public int total_entries;
	public int page;
	public int per_page;
	public int next_page;
	public int previous_page;
	public int total_pages;
	public String canonical_path;
	public String name;
	public String smart_id;
	
	public ArrayList<ETracksMix> mixes;
}
