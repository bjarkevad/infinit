package com.bva.etracks;

public class ETracksTrackResponse {
	
	public String status;	
	public String errors;
	public String notices;
	public ETracksSet set = new ETracksSet();
	
	public class ETracksSet {
		public Boolean at_beginning;
		public Boolean at_end;
		public Boolean at_last_track;
		public Boolean skip_allowed;
				
		public ETracksTrack track = new ETracksTrack();
		
		public class ETracksTrack {
			public int id;
			public String name;
			public String performer;
			public String release_name;
			public int year;
			public String url;
			public Boolean faved_by_current_user;
			public String buy_link;
			public String buy_icon;
			public String you_tube_id;
			public String you_tube_status;
			public int play_duration;				
		}
		
	}
}
