package com.bva.infinit.test;

import junit.framework.TestCase;

//import com.bva.etracks.EtracksJSON;
import com.bva.etracks.ETracksAPI;
import com.bva.etracks.EtracksMix;

public class ETracksAPITest extends TestCase {

	ETracksAPI etj;
	
	public ETracksAPITest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		etj = new ETracksAPI();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public final void testGetNextMix_valid() {
		EtracksMix mix = etj.GetNextMix(2000);
		assertFalse(mix.childMix.id == -1);
	}
}
