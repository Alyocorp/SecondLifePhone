package fr.artemis.phone.fragments.responsable.equipements.suivi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
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
import fr.artemis.phone.dto.CRMPhoneEquipementSalarieDTO;
import fr.artemis.phone.dto.SalarieLightDTO;
import fr.artemis.phone.fragments.responsable.equipements.suivi.adapter.ExpandableRespEquipementSuiviParEquipementAdapter;
import fr.artemis.phone.fragments.responsable.equipements.suivi.adapter.ExpandableRespEquipementSuiviParSalarieAdapter;
import fr.artemis.phone.webservices.WsCaller;
import fr.artemis.phone.webservices.WsName;
import fr.artemis.phone.webservices.WsUtil;

public class FragmentRespEquipementSuivi extends Fragment implements RecyclerViewExpandableItemManager.OnGroupCollapseListener, RecyclerViewExpandableItemManager.OnGroupExpandListener, WsCaller {

	private static final String[] typeFiltre = new String[] { "Par salarié", "Par équipement" };

	// La liste affichée
	@BindView( R.id.recycler_view_resp_equipement_suivi_salaries )
	RecyclerView mRecyclerViewParSalaries;

	@BindView( R.id.recycler_view_resp_equipement_suivi_equipements )
	RecyclerView mRecyclerViewParEquipements;

	@BindView( R.id.spTypeEquipementFiltre )
	Spinner spTypeEquipementFiltre;

	@BindView( R.id.respEquipementViewFlipper )
	ViewFlipper flipper;

	private RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManagerParSalarie;

	private RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManagerParEquipement;

	// La liste des salariés
	private List<SalarieLightDTO> listeSalaries;

	// La liste de demandes des salariés
	private List<CRMPhoneEquipementSalarieDTO> listePossessions;

	@Nullable
	@Override
	public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
		View view = inflater.inflate( R.layout.fragment_resp_equipements_suivi, container, false );

		ButterKnife.bind( this, view );

		initDatas();

		return view;
	}

	/**
	 * Initialisation des données necessaires
	 */
	private void initDatas() {
		try {
			WsUtil.getListeSalariesActifs( this, null );
			WsUtil.getListeEquipementsSalarieSuivi( this, null );

			// TODO verifier si le code suivant est utile ???
//			// Les liste des equipements demandés par les salariés
//			List<CRMPhoneEquipementDTO> listeEquipements = new ArrayList<>();
//
//			boolean equipementAlreadyAdded;
//
//			if ( !listePossessions.isEmpty() ) {
//				for ( CRMPhoneEquipementSalarieDTO demande : listePossessions ) {
//					if ( null != demande.getEquipement() ) {
//						equipementAlreadyAdded = false;
//						for ( CRMPhoneEquipementDTO equipementExistant : listeEquipements ) {
//							if ( equipementExistant.getId().equals( demande.getEquipement().getId() ) ) {
//								equipementAlreadyAdded = true;
//							}
//						}
//
//						if ( !equipementAlreadyAdded ) {
//							listeEquipements.add( demande.getEquipement() );
//						}
//					}
//				}
//			}
		} catch ( Exception ex ) {
			ex.printStackTrace();
			FirebaseCrashlytics.getInstance().recordException( ex );
			Log.d( "TAG", "Erreur lors de la recuperation de la liste des equipements." );
		}
	}

	private void initView() {
		assert null != getContext();

		// Injection de la liste des types de client dans le spinner
		ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>( getContext(), R.layout.spinner_item, typeFiltre );
		spTypeEquipementFiltre.setAdapter( adapterSpinner );
		spTypeEquipementFiltre.setSelection( 0 );

		List<String> listeSalariesString = new ArrayList<>();

		boolean addSalarieToList;
		for ( SalarieLightDTO salarie : listeSalaries ) {
			addSalarieToList = false;
			if ( null != listePossessions && !listePossessions.isEmpty() ) {
				for ( CRMPhoneEquipementSalarieDTO demande : listePossessions ) {
					if ( demande.getSalarie().getId().equals( salarie.getId() ) ) {
						addSalarieToList = true;
						break;
					}
				}
			}

			if ( addSalarieToList && !listeSalariesString.contains( salarie.getNom() + " " + salarie.getPrenom() ) ) {
				listeSalariesString.add( salarie.getNom() + " " + salarie.getPrenom() );
			}
		}
	}

	private void initEvents() {
		// Affichage de la bonne fiche suivant la selection utilisateur ( par salarié / par equipement )
		spTypeEquipementFiltre.setOnItemSelectedListener( new Spinner.OnItemSelectedListener() {

			@Override
			public void onItemSelected( AdapterView<?> parent, View view, int position, long id ) {
				if ( null != view ) {
					switch ( ( (AppCompatTextView) view ).getText().toString() ) {
						case "Par salarié" : {
							flipper.setDisplayedChild( 0 );
							// Le gestionnaire de la liste des lignes de demande d'equipement par salarié
							RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager( requireContext() );

							mRecyclerViewExpandableItemManagerParEquipement = new RecyclerViewExpandableItemManager();
							mRecyclerViewExpandableItemManagerParEquipement.setOnGroupExpandListener( FragmentRespEquipementSuivi.this );
							mRecyclerViewExpandableItemManagerParEquipement.setOnGroupCollapseListener( FragmentRespEquipementSuivi.this );

							// L'adapter du contenu des equipements
							ExpandableRespEquipementSuiviParSalarieAdapter adapter = new ExpandableRespEquipementSuiviParSalarieAdapter( FragmentRespEquipementSuivi.this,
									mRecyclerViewExpandableItemManagerParEquipement, listePossessions );

							// L'adapter de la liste des equipements
							RecyclerView.Adapter mWrappedAdapter = mRecyclerViewExpandableItemManagerParEquipement.createWrappedAdapter( adapter );

							final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();

							animator.setSupportsChangeAnimations( false );

							mRecyclerViewParEquipements.setLayoutManager( mLayoutManager );
							mRecyclerViewParEquipements.setAdapter( mWrappedAdapter );
							mRecyclerViewParEquipements.setItemAnimator( animator );
							mRecyclerViewParEquipements.setHasFixedSize( false );

							mRecyclerViewParEquipements.addItemDecoration( new SimpleListDividerDecorator( ContextCompat.getDrawable( requireContext(), R.drawable.list_divider_h ), true ) );

							mRecyclerViewExpandableItemManagerParEquipement.attachRecyclerView( mRecyclerViewParEquipements );

							break;
						}

						case "Par équipement" : {
							flipper.setDisplayedChild( 1 );

							// Le gestionnaire de la liste des lignes de demande d'equipement par salarié
							RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager( requireContext() );

							mRecyclerViewExpandableItemManagerParEquipement = new RecyclerViewExpandableItemManager();
							mRecyclerViewExpandableItemManagerParEquipement.setOnGroupExpandListener( FragmentRespEquipementSuivi.this );
							mRecyclerViewExpandableItemManagerParEquipement.setOnGroupCollapseListener( FragmentRespEquipementSuivi.this );

							// L'adapter du contenu des equipements
							ExpandableRespEquipementSuiviParEquipementAdapter adapter = new ExpandableRespEquipementSuiviParEquipementAdapter( FragmentRespEquipementSuivi.this,
									mRecyclerViewExpandableItemManagerParEquipement, listePossessions );

							// L'adapter de la liste des equipements
							RecyclerView.Adapter mWrappedAdapter = mRecyclerViewExpandableItemManagerParEquipement.createWrappedAdapter( adapter );

							final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();

							animator.setSupportsChangeAnimations( false );

							mRecyclerViewParEquipements.setLayoutManager( mLayoutManager );
							mRecyclerViewParEquipements.setAdapter( mWrappedAdapter );
							mRecyclerViewParEquipements.setItemAnimator( animator );
							mRecyclerViewParEquipements.setHasFixedSize( false );

							mRecyclerViewParEquipements.addItemDecoration( new SimpleListDividerDecorator( ContextCompat.getDrawable( requireContext(), R.drawable.list_divider_h ), true ) );

							mRecyclerViewExpandableItemManagerParEquipement.attachRecyclerView( mRecyclerViewParEquipements );

							break;
						}
					}
				} else {
					flipper.setDisplayedChild( 0 );
				}
			}

			@Override
			public void onNothingSelected( AdapterView<?> parent ) {}
		} );
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

			mRecyclerViewExpandableItemManagerParSalarie.scrollToGroup( groupPosition, childItemHeight, topBottomMargin, topBottomMargin );
		} else {
			throw new IllegalStateException( "Pas d'activité en cours" );
		}
	}

	@Override
	public void notifyResponse( WsName wsName, Map<String, Object> callbackResources, Object response ) {
		switch ( wsName ) {
			case GET_LISTE_SALARIES_ACTIFS :
				this.listeSalaries = (List<SalarieLightDTO>) response;
				if ( null != listePossessions && null != listeSalaries ) {
					initView();

					initEvents();
				}
				break;
			case GET_LISTE_EQUIPEMENTS_SALARIE_POSSEDES :
				this.listePossessions = (List<CRMPhoneEquipementSalarieDTO>) response;
				if ( null != listePossessions && null != listeSalaries ) {
					initView();

					initEvents();
				}
				break;
			default :
				throw new IllegalStateException( "Reception d'une reponse de webservice provenant de : " + wsName + ". Les données associées sont : " + response );
		}

		if ( null != listeSalaries && null != listePossessions ) {
			RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager( requireContext() );

			mRecyclerViewExpandableItemManagerParSalarie = new RecyclerViewExpandableItemManager();
			mRecyclerViewExpandableItemManagerParSalarie.setOnGroupExpandListener( this );
			mRecyclerViewExpandableItemManagerParSalarie.setOnGroupCollapseListener( this );

			// L'adapter du contenu des equipements
			ExpandableRespEquipementSuiviParSalarieAdapter adapter = new ExpandableRespEquipementSuiviParSalarieAdapter( this, mRecyclerViewExpandableItemManagerParSalarie, listePossessions );

			// L'adapter de la liste des equipements
			RecyclerView.Adapter mWrappedAdapter = mRecyclerViewExpandableItemManagerParSalarie.createWrappedAdapter( adapter );

			final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();

			animator.setSupportsChangeAnimations( false );

			mRecyclerViewParSalaries.setLayoutManager( mLayoutManager );
			mRecyclerViewParSalaries.setAdapter( mWrappedAdapter );
			mRecyclerViewParSalaries.setItemAnimator( animator );
			mRecyclerViewParSalaries.setHasFixedSize( false );

			mRecyclerViewParSalaries.addItemDecoration( new SimpleListDividerDecorator( ContextCompat.getDrawable( requireContext(), R.drawable.list_divider_h ), true ) );

			mRecyclerViewExpandableItemManagerParSalarie.attachRecyclerView( mRecyclerViewParSalaries );
		}
	}

	@Override
	public Fragment getFragmentSource() {
		return this;
	}
}