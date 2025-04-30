package fr.artemis.phone.fragments;

import androidx.fragment.app.Fragment;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FragmentProvider {

	private static FragmentProvider instance = null;
	private final Map<String, Fragment> mapFragments;

	private FragmentProvider() {
		mapFragments = new HashMap<>();
	}

	private static FragmentProvider getInstance() {
		if ( null == instance ) {
			instance = new FragmentProvider();
		}
		return instance;
	}

	public static void addFragment( String tag, Fragment fragment ) {
		FragmentProvider.getInstance().mapFragments.put( tag, fragment );
	}

	public static void removeFragment( String tag ) {
		FragmentProvider.getInstance().mapFragments.remove( tag );
	}

	public static Fragment getFragment( String tag ) {
		return FragmentProvider.getInstance().mapFragments.get( tag );
	}

	public static Collection<Fragment> getFragments() {
		return FragmentProvider.getInstance().mapFragments.values();
	}
}