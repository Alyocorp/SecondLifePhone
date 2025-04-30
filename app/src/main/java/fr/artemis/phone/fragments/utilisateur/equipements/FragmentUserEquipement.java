package fr.artemis.phone.fragments.utilisateur.equipements;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.artemis.phone.R;
import fr.artemis.phone.components.advancedrecyclerview.RecyclerViewExpandableItemManager;
import fr.artemis.phone.components.advancedrecyclerview.SimpleListDividerDecorator;
import fr.artemis.phone.components.advancedrecyclerview.anim.animator.GeneralItemAnimator;
import fr.artemis.phone.components.advancedrecyclerview.anim.animator.RefactoredDefaultItemAnimator;
import fr.artemis.phone.dto.CRMPhoneEquipementDTO;
import fr.artemis.phone.dto.CRMPhoneEquipementSalarieDTO;
import fr.artemis.phone.dto.CRMPhoneEquipementTailleTypeDTO;
import fr.artemis.phone.fragments.utilisateur.equipements.adapter.ExpandableUserEquipementAdapter;
import fr.artemis.phone.utils.SessionPhone;
import fr.artemis.phone.webservices.WsCaller;
import fr.artemis.phone.webservices.WsName;
import fr.artemis.phone.webservices.WsUtil;

public class FragmentUserEquipement extends Fragment implements RecyclerViewExpandableItemManager.OnGroupCollapseListener, RecyclerViewExpandableItemManager.OnGroupExpandListener, WsCaller {

	// La liste affichée
	@BindView( R.id.recycler_view_user_equipement )
	RecyclerView mRecyclerView;

	@BindView( R.id.btAddUserEquipement )
	FloatingActionButton btAddUserEquipement;

	// La liste des equipements existants
	private List<CRMPhoneEquipementDTO> listeEquipements;

	// La liste des equipements du salarié
	private List<CRMPhoneEquipementSalarieDTO> listeEquipementsSalaries;

	// La liste des tailles possibles
	private List<CRMPhoneEquipementTailleTypeDTO> listeTypesTailles;

	// Le gestionnaire d'elements contenus dans la liste de lignes de devis (gère les manipulations utilisateurs)
	private RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager;

	@Nullable
	@Override
	public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
		View view = inflater.inflate( R.layout.fragment_user_equipements, container, false );

		ButterKnife.bind( this, view );

		initDatas();

		return view;
	}

	@Override
	public void onGroupCollapse( int groupPosition, boolean fromUser, Object payload ) {
		adjustScrollPositionOnGroupExpanded( groupPosition );
	}

	@Override
	public void onGroupExpand( int groupPosition, boolean fromUser, Object payload, boolean lineInserted ) {
		adjustScrollPositionOnGroupExpanded( groupPosition );
	}

	/**
	 * Initialisation des données
	 */
	private void initDatas() {
		try {
			WsUtil.getListeEquipements( this, null );
			WsUtil.getListeTypesTailles( this, null );
			WsUtil.getListeEquipementsSalarie( this, null, SessionPhone.getInstance().getUserDto().getId() );

		} catch ( Exception ex ) {
			ex.printStackTrace();
			FirebaseCrashlytics.getInstance().recordException(ex);
			Log.d( "TAG", "Erreur lors de la recuperation de la liste des equipements." );
		}
	}

	/**
	 * Si une ligne est ouverte, on déclenche le scroll pour afficher la ligne si nécéssaire
	 *
	 * @param groupPosition La position de la ligne ouverte
	 */
	private void adjustScrollPositionOnGroupExpanded( int groupPosition ) {
		if ( null != getActivity() ) {
			int childItemHeight = 360;
			int topBottomMargin = (int) ( getActivity().getResources().getDisplayMetrics().density * 16 ); // top-spacing: 16dp

			mRecyclerViewExpandableItemManager.scrollToGroup( groupPosition, childItemHeight, topBottomMargin, topBottomMargin );
		} else {
			throw new IllegalStateException( "Pas d'activité en cours" );
		}
	}

	@SuppressWarnings( "unchecked" )
	@Override
	public void notifyResponse( WsName wsName, Map<String, Object> callbackResources, Object response ) {
		switch ( wsName ) {
			case GET_LISTE_EQUIPEMENTS: {
				this.listeEquipements = (List<CRMPhoneEquipementDTO>) response;
				break;
			}
			case GET_LISTE_TYPE_TAILLES: {
				this.listeTypesTailles = (List<CRMPhoneEquipementTailleTypeDTO>) response;
				break;
			}
			case GET_LISTE_EQUIPEMENTS_SALARIE: {
				this.listeEquipementsSalaries = (List<CRMPhoneEquipementSalarieDTO>) response;
				break;
			}
			default:
				throw new IllegalStateException( "Reception d'une reponse de webservice provenant de : " + wsName + ". Les données associées sont : " + response );
		}
		if ( null != listeEquipements && null != listeTypesTailles && null != listeEquipementsSalaries ) {
			// Le gestionnaire de la liste des lignes d'equipement
			RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager( requireContext() );

			mRecyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager();
			mRecyclerViewExpandableItemManager.setOnGroupExpandListener( this );
			mRecyclerViewExpandableItemManager.setOnGroupCollapseListener( this );

			// L'adapter du contenu des equipements
			ExpandableUserEquipementAdapter adapter = new ExpandableUserEquipementAdapter( this, mRecyclerViewExpandableItemManager, listeEquipements, listeEquipementsSalaries, listeTypesTailles );

			// L'adapter de la liste des equipements
			RecyclerView.Adapter mWrappedAdapter = mRecyclerViewExpandableItemManager.createWrappedAdapter( adapter );

			final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();

			animator.setSupportsChangeAnimations( false );

			mRecyclerView.setLayoutManager( mLayoutManager );
			mRecyclerView.setAdapter( mWrappedAdapter );
			mRecyclerView.setItemAnimator( animator );
			mRecyclerView.setHasFixedSize( false );

			mRecyclerView.addItemDecoration( new SimpleListDividerDecorator( ContextCompat.getDrawable( requireContext(), R.drawable.list_divider_h ), true ) );

			mRecyclerViewExpandableItemManager.attachRecyclerView( mRecyclerView );

			btAddUserEquipement.setOnClickListener( v -> adapter.handleOnClickGroupItemAddBelowButton() );
		}
	}

	@Override
	public Fragment getFragmentSource() {
		return this;
	}
}