package com.bva.infinit.test;

import java.util.ArrayList;

import junit.framework.TestCase;

//import com.bva.etracks.EtracksJSON;
import com.bva.etracks.ETracksAPI;
import com.bva.etracks.ETracksMix;
import com.bva.etracks.ETracksQueryResponse;

public class ETracksAPITest extends TestCase {
	
	public ETracksAPITest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public final void testGetNextMix_valid() {
		ETracksMix mix = ETracksAPI.GetNextMix(1234);
		assertFalse(mix.id == -1);
	}
	
	public final void testSearchMixes_valid() {
		ArrayList<ETracksMix> mixes = new ArrayList<ETracksMix>();
		
		mixes = ETracksAPI.SearchMixes("chillstep").mixes;
		
		assertTrue(!mixes.isEmpty());
	}
	
	public final void testSearchMixesByTag_valid() {
		ArrayList<ETracksMix> mixes = new ArrayList<ETracksMix>();
		
		mixes = ETracksAPI.SearchMixesByTag("chill").mixes;
		
		assertTrue(!mixes.isEmpty());		
	}
	
	public final void testNextPage_valid() {		
		ETracksQueryResponse response = ETracksAPI.NextPage(ETracksAPI.SearchMixes("chillstep"));
		
		assertTrue(!response.mixes.isEmpty());
	}
	
	
}
