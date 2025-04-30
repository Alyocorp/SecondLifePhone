package fr.artemis.phone.fragments.utilisateur.accueil;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import fr.artemis.phone.utils.AndroidUtils;

/**
 * Fragment d'accueil d'un utilisateur
 */
public class FragmentUserAccueil extends Fragment {

	@Override
	public void onCreate( @Nullable Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		assert null != getActivity();

		AndroidUtils.hideKeyboard( getActivity() );
	}
}