package com.bva.etracks;

//Follows 8tracks api naming scheme

public class EtracksMix {

	
public String smart_id;
public String status;
public int errors;
public int notices;	
public EtracksChildMix childMix = new EtracksChildMix();

	public class EtracksChildMix {
		
		public int id;
		public String name;
		public String description;
		public int plays_count;
		public int likes_count;
		public String path;
		public String cover_url;
		public String restful_url;
		public String tag_list_cache;
		public String first_published_at;
		public Boolean liked_by_current_user;
		public Boolean nsfw;
		public int duration;
		public int tracks_count;
		public EtracksUser user = new EtracksUser();

		public class EtracksUser {
			int id;
			String login;
			String avatar_url;
			Boolean followed_by_current_user;	
		}
	}
}
