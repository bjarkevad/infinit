package com.bva.infinit;

import java.util.ArrayList;
import java.util.Locale;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.TypedValue;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BrowseMixes extends SlidingFragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_mixes);

        //SlidingMenu
        setBehindContentView(R.layout.slidingmenu_navigation);
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 270, r.getDisplayMetrics());
        getSlidingMenu().setBehindWidth((int) px);
        setSlidingActionBarEnabled(false);
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //

        //Testing ListView
        ListView lv = (ListView)findViewById(R.id.slidingmenu_navigation_listview);
        ArrayList<String> menuItems = new ArrayList<String>();
        menuItems.add("TEST");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuItems);
        lv.setAdapter(arrayAdapter);
        lv.setBackgroundColor(getResources().getColor(R.color.actionbar_dark_grey));
        //

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.mainPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				// TODO Auto-generated method stub
				switch (index) {
					case 0:
						getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
						break;
					default:
						getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
						break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.browse, menu);
        return true;
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

        	switch(position) {
        		case 3:
        			return new BrowseMixesFragment();

        		default:
        			return new BrowseMixesFragment();
        	}

        	/*Fragment fragment = new TestSectionFragment();
            Bundle args = new Bundle();
            args.putInt(TestSectionFragment.ARG_SECTION_NUMBER, position + 1);
            fragment.setArguments(args);*/
            //return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
            	case 0:
            		return getString(R.string.title_searchmixes).toUpperCase(l);
                case 1:
                    return getString(R.string.title_favorites).toUpperCase(l);
                case 2:
                    return getString(R.string.title_listeninghistory).toUpperCase(l);
                case 3:
                    return getString(R.string.title_browsemixes).toUpperCase(l);
            }
            return null;
        }
    }
}