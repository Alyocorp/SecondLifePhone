package fr.artemis.phone.fragments.responsable.equipements.demandes;

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

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.artemis.phone.R;
import fr.artemis.phone.components.advancedrecyclerview.RecyclerViewExpandableItemManager;
import fr.artemis.phone.components.advancedrecyclerview.SimpleListDividerDecorator;
import fr.artemis.phone.components.advancedrecyclerview.anim.animator.GeneralItemAnimator;
import fr.artemis.phone.components.advancedrecyclerview.anim.animator.RefactoredDefaultItemAnimator;
import fr.artemis.phone.dto.CRMPhoneEquipementSalarieDTO;
import fr.artemis.phone.dto.SalarieLightDTO;
import fr.artemis.phone.fragments.responsable.equipements.demandes.adapter.ExpandableRespEquipementDemandesParEquipementAdapter;
import fr.artemis.phone.fragments.responsable.equipements.demandes.adapter.ExpandableRespEquipementDemandesParSalarieAdapter;
import fr.artemis.phone.webservices.WsCaller;
import fr.artemis.phone.webservices.WsName;
import fr.artemis.phone.webservices.WsUtil;

public class FragmentRespEquipementDemande extends Fragment implements RecyclerViewExpandableItemManager.OnGroupCollapseListener, RecyclerViewExpandableItemManager.OnGroupExpandListener, WsCaller {

	private static final String[] typeFiltre = new String[] { "Par salarié", "Par équipement" };

	// La liste affichée
	@BindView( R.id.recycler_view_resp_equipement_demandes_salaries )
	RecyclerView mRecyclerViewParSalaries;

	@BindView( R.id.recycler_view_resp_equipement_demandes_equipements )
	RecyclerView mRecyclerViewParEquipements;

	@BindView( R.id.spTypeEquipementFiltre )
	Spinner spTypeEquipementFiltre;

	@BindView( R.id.spEquipementSalarie )
	Spinner spEquipementSalarie;

	@BindView( R.id.respEquipementViewFlipper )
	ViewFlipper flipper;

	private RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManagerParSalarie;

	private RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManagerParEquipement;

	// La liste des salariés
	private List<SalarieLightDTO> listeSalaries = null;

	// La liste de demandes des salariés
	private List<CRMPhoneEquipementSalarieDTO> listeDemandes = null;

	@Nullable
	@Override
	public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
		View view = inflater.inflate( R.layout.fragment_resp_equipements_demandes, container, false );

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
			WsUtil.getListeEquipementsSalarieActives( this, null );

			// TODO vérifier si présence du bloc dessous utile ????
//			// Les liste des equipements demandés par les salariés
//			List<CRMPhoneEquipementDTO> listeEquipements = new ArrayList<>();
//
//			boolean equipementAlreadyAdded;
//
//			if ( !listeDemandes.isEmpty() ) {
//				for ( CRMPhoneEquipementSalarieDTO demande : listeDemandes ) {
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
			if ( null != listeDemandes && !listeDemandes.isEmpty() ) {
				for ( CRMPhoneEquipementSalarieDTO demande : listeDemandes ) {
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

		ArrayAdapter<String> adapterSpinnerSalaries = new ArrayAdapter<>( getContext(), R.layout.spinner_item, listeSalariesString );
		spEquipementSalarie.setAdapter( adapterSpinnerSalaries );
	}

	private void initEvents() {
		// Affichage de la bonne fiche suivant la selection utilisateur ( par salarié / par equipement )
		spTypeEquipementFiltre.setOnItemSelectedListener( new Spinner.OnItemSelectedListener() {

			@Override
			public void onItemSelected( AdapterView<?> parent, View view, int position, long id ) {
				if ( null != view ) {
					switch ( ( (AppCompatTextView) view ).getText().toString() ) {
						case "Par salarié" :
							flipper.setDisplayedChild( 0 );
							break;
						case "Par équipement" :
							flipper.setDisplayedChild( 1 );

							// Le gestionnaire de la liste des lignes de demande d'equipement par salarié
							RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager( requireContext() );

							mRecyclerViewExpandableItemManagerParEquipement = new RecyclerViewExpandableItemManager();
							mRecyclerViewExpandableItemManagerParEquipement.setOnGroupExpandListener( FragmentRespEquipementDemande.this );
							mRecyclerViewExpandableItemManagerParEquipement.setOnGroupCollapseListener( FragmentRespEquipementDemande.this );

							// L'adapter du contenu des equipements
							ExpandableRespEquipementDemandesParEquipementAdapter adapter = new ExpandableRespEquipementDemandesParEquipementAdapter( mRecyclerViewExpandableItemManagerParEquipement,
									listeDemandes );

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
				} else {
					flipper.setDisplayedChild( 0 );
				}
			}

			@Override
			public void onNothingSelected( AdapterView<?> parent ) {}
		} );

		// Suivant le salarié selectionné, on recharge la liste des demandes d'equipements
		spEquipementSalarie.setOnItemSelectedListener( new Spinner.OnItemSelectedListener() {

			@Override
			public void onItemSelected( AdapterView<?> parent, View view, int position, long id ) {
				if ( null != view ) {

					SalarieLightDTO salarie = matchSalarieFromString( ( (AppCompatTextView) view ).getText().toString() );

					List<CRMPhoneEquipementSalarieDTO> listeDemandesDuSalarie = new ArrayList<>();

					for ( CRMPhoneEquipementSalarieDTO demande : listeDemandes ) {
						if ( demande.getSalarie().getId().equals( salarie.getId() ) ) {
							listeDemandesDuSalarie.add( demande );
						}
					}

					RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager( requireContext() );

					mRecyclerViewExpandableItemManagerParSalarie = new RecyclerViewExpandableItemManager();
					mRecyclerViewExpandableItemManagerParSalarie.setOnGroupExpandListener( FragmentRespEquipementDemande.this );
					mRecyclerViewExpandableItemManagerParSalarie.setOnGroupCollapseListener( FragmentRespEquipementDemande.this );

					// L'adapter du contenu des equipements
					ExpandableRespEquipementDemandesParSalarieAdapter adapter = new ExpandableRespEquipementDemandesParSalarieAdapter( FragmentRespEquipementDemande.this,
							mRecyclerViewExpandableItemManagerParSalarie, listeDemandesDuSalarie );

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
			public void onNothingSelected( AdapterView<?> parent ) {}
		} );

		if ( null != listeDemandes && !listeDemandes.isEmpty() ) {
			spEquipementSalarie.setSelection( 0 );
		}
	}

	@Override
	public void onViewCreated( @NonNull View view, @Nullable Bundle savedInstanceState ) {
		super.onViewCreated( view, savedInstanceState );

		if ( null != getView() ) {
			// Le gestionnaire de la liste des lignes de demande d'equipement par salarié
			RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager( requireContext() );

			mRecyclerViewExpandableItemManagerParSalarie = new RecyclerViewExpandableItemManager();
			mRecyclerViewExpandableItemManagerParSalarie.setOnGroupExpandListener( this );
			mRecyclerViewExpandableItemManagerParSalarie.setOnGroupCollapseListener( this );

			// L'adapter du contenu des equipements
			ExpandableRespEquipementDemandesParSalarieAdapter adapter = new ExpandableRespEquipementDemandesParSalarieAdapter( this, mRecyclerViewExpandableItemManagerParSalarie, listeDemandes );

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

			mRecyclerViewExpandableItemManagerParSalarie.scrollToGroup( groupPosition, childItemHeight, topBottomMargin, topBottomMargin );
		} else {
			throw new IllegalStateException( "Pas d'activité en cours" );
		}
	}

	/**
	 * Retourne l'instance du salarié qui correspond à son identité (nom prenom
	 *
	 * @param identite
	 *            L'identité du salarié
	 * @return Le salarié correspondant à l'identité
	 */
	private SalarieLightDTO matchSalarieFromString( String identite ) {
		if ( null != identite ) {
			for ( SalarieLightDTO salarie : listeSalaries ) {
				if ( ( salarie.getNom() + " " + salarie.getPrenom() ).equals( identite ) ) {
					return salarie;
				}
			}
		}

		throw new IllegalStateException( "Le salarié " + identite + " n'existe pas dans la liste des salariés affichés {#][~#}@^|}" );
	}

	@Override
	public Fragment getFragmentSource() {
		return this;
	}

	@Override
	public void notifyResponse( WsName wsName, Map<String, Object> callbackResources, Object response ) {
		switch ( wsName ) {
			case GET_LISTE_SALARIES_ACTIFS :
				this.listeSalaries = (List<SalarieLightDTO>) response;

				if ( null != listeSalaries && null != listeDemandes ) {
					initView();

					initEvents();
				}
				break;
			case GET_LISTE_EQUIPEMENTS_SALARIE_ACTIVES :
				this.listeDemandes = (List<CRMPhoneEquipementSalarieDTO>) response;

				if ( null != listeSalaries && null != listeDemandes ) {
					initView();

					initEvents();
				}

				break;
			default :
				throw new IllegalStateException( "Reception d'une reponse de webservice provenant de : " + wsName + ". Les données associées sont : " + response );
		}
	}
}