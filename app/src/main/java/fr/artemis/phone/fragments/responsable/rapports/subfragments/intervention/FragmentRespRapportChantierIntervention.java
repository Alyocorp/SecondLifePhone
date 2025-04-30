package fr.artemis.phone.fragments.responsable.rapports.subfragments.intervention;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import fr.artemis.phone.R;

/**
 * Onglet des rapports de chantier par intervention
 */
public class FragmentRespRapportChantierIntervention extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        View view = inflater.inflate(R.layout.fragment_resp_rapport_chantier_intervention, container, false);

        assert null != getActivity();

        ButterKnife.bind(this, view);

        return view;
    }
}
