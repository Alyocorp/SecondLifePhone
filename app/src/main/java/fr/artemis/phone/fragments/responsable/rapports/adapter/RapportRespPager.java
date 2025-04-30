package fr.artemis.phone.fragments.responsable.rapports.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import fr.artemis.phone.fragments.FragmentProvider;

public class RapportRespPager extends FragmentStatePagerAdapter {

	// Map contenant le titre de l'onglet associé au nom du provider de données
	private LinkedHashMap<String, String> mapTabsData;

	/**
	 * Constructeur
	 *
	 * @param fm
	 *            Le gestionnaire de fragments
	 * @param behavior
	 *            Le comportements du contenu des onglets (Lifecycle RESUME uniquement sur l'onglet en cours)
	 * @param mapTabsData
	 *            La map associant le titre de l'onglet avec le provider de données
	 */
	public RapportRespPager( FragmentManager fm, int behavior, LinkedHashMap<String, String> mapTabsData ) {
		super( fm, behavior );
		this.mapTabsData = mapTabsData;
	}

	@NonNull
	@Override
	public Fragment getItem( int position ) {
		if ( position < mapTabsData.size() ) {
			return FragmentProvider.getFragment( new ArrayList<>( mapTabsData.values() ).get( position ) );
		} else {
			throw new IllegalStateException( "Tentative d'instanciation d'un onglet inexistant..." );
		}
	}

	@Override
	public int getCount() {
		return mapTabsData.size();
	}

	@Override
	public CharSequence getPageTitle( int position ) {
		return new ArrayList<>( mapTabsData.keySet() ).get( position );
	}
}