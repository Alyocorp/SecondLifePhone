package fr.artemis.phone.fragments.responsable.rapports.subfragments.incoherence;

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
 * Onglet des incohérences dans les rapports
 */
public class FragmentRespRapportChantierIncoherence extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        View view = inflater.inflate(R.layout.fragment_resp_rapport_chantier_incoherence, container, false);

        assert null != getActivity();

        ButterKnife.bind(this, view);

        return view;
    }
}
