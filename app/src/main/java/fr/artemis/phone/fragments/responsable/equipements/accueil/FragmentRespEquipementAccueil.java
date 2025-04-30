package fr.artemis.phone.fragments.responsable.equipements.accueil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.artemis.phone.R;
import fr.artemis.phone.activities.main.MainActivity;

/**
 * Fragment d'accueil des equipements pour un responsable
 */
public class FragmentRespEquipementAccueil extends Fragment {

	@BindView( R.id.btEquipementDemandes )
	Button btEquipementDemandes;

	@BindView( R.id.btEquipementListe )
	Button btEquipementListe;

	@BindView( R.id.btEquipementSuivi )
	Button btEquipementSuivi;

	@Nullable
	@Override
	public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
		View view = inflater.inflate( R.layout.fragment_resp_equipements_accueil, container, false );

		ButterKnife.bind( this, view );

		initEvents();

		return view;
	}

	/**
	 * Initialisation des evenements
	 */
	private void initEvents() {

		assert null != getActivity();

		MainActivity activity = (MainActivity) getActivity();

		btEquipementListe.setOnClickListener( v -> activity.showEquipementsRespListe() );

		btEquipementDemandes.setOnClickListener( v -> activity.showEquipementsRespDemandes() );

		btEquipementSuivi.setOnClickListener( v -> activity.showEquipementsRespSuivi() );
	}
}