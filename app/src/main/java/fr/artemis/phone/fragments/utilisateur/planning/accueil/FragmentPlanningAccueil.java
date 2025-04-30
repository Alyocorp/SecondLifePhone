package fr.artemis.phone.fragments.utilisateur.planning.accueil;

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
 * Fragment d'accueil du module planning
 */
public class FragmentPlanningAccueil extends Fragment {


	@BindView( R.id.btPlanningSemaine )
	Button btPlanningSemaine;

	@BindView( R.id.btPlanningSynchronise )
	Button btPlanningSynchronise;

	@Nullable
	@Override
	public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
		View view = inflater.inflate( R.layout.fragment_planning_accueil, container, false );

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

		btPlanningSemaine.setOnClickListener( v -> activity.showPlanningSemaine() );

		btPlanningSynchronise.setOnClickListener( v -> activity.showPlanningSynchronise() );
	}
}