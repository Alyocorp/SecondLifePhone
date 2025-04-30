package fr.artemis.phone.fragments.responsable.accueil;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import fr.artemis.phone.utils.AndroidUtils;

/**
 * Fragment de l'accueil d'un responsable
 */
public class FragmentRespAccueil extends Fragment {

	@Override
	public void onCreate( @Nullable Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		assert null != getActivity();

		AndroidUtils.hideKeyboard( getActivity() );
	}
}