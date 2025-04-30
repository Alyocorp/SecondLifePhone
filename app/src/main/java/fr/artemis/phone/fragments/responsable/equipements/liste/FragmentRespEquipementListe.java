package fr.artemis.phone.fragments.responsable.equipements.liste;

import java.util.List;
import java.util.Map;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.artemis.phone.R;
import fr.artemis.phone.components.advancedrecyclerview.RecyclerViewExpandableItemManager;
import fr.artemis.phone.components.advancedrecyclerview.SimpleListDividerDecorator;
import fr.artemis.phone.components.advancedrecyclerview.anim.animator.GeneralItemAnimator;
import fr.artemis.phone.components.advancedrecyclerview.anim.animator.RefactoredDefaultItemAnimator;
import fr.artemis.phone.dto.CRMPhoneEquipementDTO;
import fr.artemis.phone.dto.CRMPhoneEquipementTailleTypeDTO;
import fr.artemis.phone.fragments.responsable.equipements.liste.adapter.ExpandableRespEquipementListeAdapter;
import fr.artemis.phone.webservices.WsCaller;
import fr.artemis.phone.webservices.WsName;
import fr.artemis.phone.webservices.WsUtil;

/**
 * Fragment de la gestion des equipements
 */
public class FragmentRespEquipementListe extends Fragment implements RecyclerViewExpandableItemManager.OnGroupCollapseListener, RecyclerViewExpandableItemManager.OnGroupExpandListener, WsCaller {

	// La liste affichée
	@BindView( R.id.recycler_view_resp_equipement )
	RecyclerView mRecyclerView;

	@BindView( R.id.btAddEquipement )
	FloatingActionButton btAddEquipement;

	// La liste des equipements existants
	private List<CRMPhoneEquipementDTO> listeEquipements;

	// La liste des types de tailles existants
	private List<CRMPhoneEquipementTailleTypeDTO> listeTypesTailles;

	// Le gestionnaire d'elements contenus dans la liste de lignes de devis (gère les manipulations utilisateurs)
	private RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager;

	@Nullable
	@Override
	public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
		View view = inflater.inflate( R.layout.fragment_resp_equipements_liste, container, false );

		ButterKnife.bind( this, view );

		initDatas();

		return view;
	}

	private void initDatas() {
		try {
			WsUtil.getListeEquipements( this, null );
			WsUtil.getListeTypesTailles( this, null );

		} catch ( Exception ex ) {
			ex.printStackTrace();
			FirebaseCrashlytics.getInstance().recordException(ex);
			Log.d( "TAG", "Erreur lors de la recuperation de la liste des equipements." );
		}
	}

	/**
	 * Initialisation de la vue
	 */
	private void initView() {
		if ( null != getView() ) {
			// Le gestionnaire de la liste des lignes d'equipement
			RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager( requireContext() );

			mRecyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager();
			mRecyclerViewExpandableItemManager.setOnGroupExpandListener( this );
			mRecyclerViewExpandableItemManager.setOnGroupCollapseListener( this );

			// L'adapter du contenu des equipements
			ExpandableRespEquipementListeAdapter adapter = new ExpandableRespEquipementListeAdapter( this, mRecyclerViewExpandableItemManager, listeEquipements, listeTypesTailles );

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

			btAddEquipement.setOnClickListener( v -> adapter.handleOnClickGroupItemAddBelowButton() );
		} else {
			throw new IllegalStateException( "Impossible de charger la vue." );
		}
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
	 * Si une ligne est ouverte, on déclenche le scroll pour afficher la ligne si nécéssaire
	 *
	 * @param groupPosition
	 *            La position de la ligne ouverte
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

	@Override
	public void notifyResponse( WsName wsName, Map<String, Object> callbackResources, Object response ) {

		switch ( wsName ) {

			case GET_LISTE_EQUIPEMENTS : {
				this.listeEquipements = (List<CRMPhoneEquipementDTO>) response;

				if ( null != listeEquipements && null != listeTypesTailles ) {
					initView();
				}
				break;
			}
			case GET_LISTE_TYPE_TAILLES : {
				this.listeTypesTailles = (List<CRMPhoneEquipementTailleTypeDTO>) response;
				if ( null != listeEquipements && null != listeTypesTailles ) {
					initView();
				}
				break;
			}
			default :
				throw new IllegalStateException( "Reception d'une reponse de webservice provenant de : " + wsName + ". Les données associées sont : " + response );
		}
	}

	@Override
	public Fragment getFragmentSource() {
		return this;
	}
}