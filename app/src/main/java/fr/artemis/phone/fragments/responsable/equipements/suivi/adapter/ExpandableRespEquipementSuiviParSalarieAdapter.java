package fr.artemis.phone.fragments.responsable.equipements.suivi.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import android.annotation.SuppressLint;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.artemis.phone.R;
import fr.artemis.phone.components.advancedrecyclerview.RecyclerViewAdapterUtils;
import fr.artemis.phone.components.advancedrecyclerview.RecyclerViewExpandableItemManager;
import fr.artemis.phone.components.advancedrecyclerview.base.adapters.AbstractExpandableItemAdapter;
import fr.artemis.phone.components.advancedrecyclerview.base.viewholders.AbstractExpandableItemViewHolder;
import fr.artemis.phone.dto.CRMPhoneEquipementSalarieDTO;
import fr.artemis.phone.dto.SalarieLightDTO;
import fr.artemis.phone.fragments.FragmentProvider;
import fr.artemis.phone.fragments.FragmentTags;
import fr.artemis.phone.webservices.WsCaller;
import fr.artemis.phone.webservices.WsName;
import fr.artemis.phone.webservices.WsUtil;

public class ExpandableRespEquipementSuiviParSalarieAdapter extends AbstractExpandableItemAdapter<ExpandableRespEquipementSuiviParSalarieAdapter.MyGroupViewHolder, ExpandableRespEquipementSuiviParSalarieAdapter.MyChildViewHolder> implements WsCaller {

	// Le manager des objets déroulés ou non
	private RecyclerViewExpandableItemManager mExpandableItemManager;

	// Listener de clic sur les boutons contenus dans les listes déroulées
	private View.OnClickListener mItemOnClickListener = this::onClickItemView;

	// Map contenant le viewholderchild par numero de ligne
	private SparseArray<ExpandableRespEquipementSuiviParSalarieAdapter.MyGroupViewHolder> saViewHolders = new SparseArray<>();

	// Map associant les numeros de lignes (groupPosition) aux identifiants des equipements
	@SuppressLint( "UseSparseArrays" )
	private Map<Integer, SalarieLightDTO> mapLignes = new HashMap<>();

	private Map<Integer, List<CRMPhoneEquipementSalarieDTO>> mapEquipementsParSalarie = new HashMap<>();

	// La liste des demandes d'equipements de salarié
	private List<CRMPhoneEquipementSalarieDTO> listeDemandesEquipements;

	private Fragment fragment;

	/**
	 * Constructeur
	 *
	 * @param expandableItemManager
	 *            Le manager des différentes listes déroulantes
	 */
	public ExpandableRespEquipementSuiviParSalarieAdapter( Fragment fragmentSource, RecyclerViewExpandableItemManager expandableItemManager,
			List<CRMPhoneEquipementSalarieDTO> listeDemandesEquipements ) {
		this.fragment = fragmentSource;
		this.mExpandableItemManager = expandableItemManager;
		this.listeDemandesEquipements = listeDemandesEquipements;

		List<SalarieLightDTO> listeSalaries = new ArrayList<>();

		for ( CRMPhoneEquipementSalarieDTO demandeSalarie : listeDemandesEquipements ) {

			boolean salarieAlreadyAdded = false;
			for ( SalarieLightDTO salarieExistant : listeSalaries ) {
				if ( demandeSalarie.getSalarie().getId().equals( salarieExistant.getId() ) ) {
					salarieAlreadyAdded = true;
				}
			}

			if ( !salarieAlreadyAdded ) {
				listeSalaries.add( demandeSalarie.getSalarie() );
				mapEquipementsParSalarie.put( demandeSalarie.getSalarie().getId(), new ArrayList<>() );
			}

			mapEquipementsParSalarie.get( demandeSalarie.getSalarie().getId() ).add( demandeSalarie );
		}

		int position = 0;
		if ( !listeSalaries.isEmpty() ) {
			for ( SalarieLightDTO salarie : listeSalaries ) {
				mapLignes.put( position, salarie );
				position++;
			}
		}

		// Identifiant de ligne du RecyclerView correspondant aux numéros de ligne du devis
		// Si a false, Le RecyclerView réutilise des instances du contenu des lignes au pif....
		// Du coup, affichage des lignes en double/triple, etc et à des lignes aléatoires
		setHasStableIds( true );
	}

	@Override
	public int getGroupCount() {
		return mapLignes.size();
	}

	@Override
	public int getChildCount( int groupPosition ) {
		return 1;
	}

	@Override
	public long getGroupId( int groupPosition ) {
		return Objects.requireNonNull( mapLignes.get( groupPosition ) ).getId();
	}

	@Override
	public long getChildId( int groupPosition, int childPosition ) {
		// Numero d'equipement + 10000 histoire de ne pas les confondre avec les lignes d'equipement
		return Objects.requireNonNull( mapLignes.get( groupPosition ) ).getId() + 10000;
	}

	@Override
	@NonNull
	public ExpandableRespEquipementSuiviParSalarieAdapter.MyGroupViewHolder onCreateGroupViewHolder( @NonNull ViewGroup parent, int viewType ) {
		final LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
		final View v = inflater.inflate( R.layout.list_resp_equipement_suivi_par_salarie, parent, false );

		return new ExpandableRespEquipementSuiviParSalarieAdapter.MyGroupViewHolder( v, mItemOnClickListener );
	}

	@Override
	@NonNull
	public ExpandableRespEquipementSuiviParSalarieAdapter.MyChildViewHolder onCreateChildViewHolder( @NonNull ViewGroup parent, int viewType ) {
		final LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
		final View v = inflater.inflate( R.layout.item_resp_equipement_suivi_expanded_par_salarie, parent, false );
		return new ExpandableRespEquipementSuiviParSalarieAdapter.MyChildViewHolder( v, mItemOnClickListener );
	}

	@SuppressLint( "SetTextI18n" )
	@Override
	public void onBindGroupViewHolder( @NonNull ExpandableRespEquipementSuiviParSalarieAdapter.MyGroupViewHolder holder, int groupPosition, int viewType ) {

		SalarieLightDTO salarie = mapLignes.get( groupPosition );
		holder.tvEquipementSuiviEquipementParSalarie.setText( salarie.getNom() + " " + salarie.getPrenom() );

		saViewHolders.put( groupPosition, holder );

		// Ligne cliquable
		holder.itemView.setClickable( true );
	}

	@Override
	public void onBindChildViewHolder( @NonNull ExpandableRespEquipementSuiviParSalarieAdapter.MyChildViewHolder holder, int groupPosition, int childPosition, int viewType ) {

		SalarieLightDTO salarie = mapLignes.get( groupPosition );

		holder.setListeEquipementsDuSalarie( mapEquipementsParSalarie.get( salarie.getId() ) );
	}

	@Override
	public boolean onCheckCanExpandOrCollapseGroup( @NonNull ExpandableRespEquipementSuiviParSalarieAdapter.MyGroupViewHolder holder, int groupPosition, int x, int y, boolean expand ) {
		return false;
	}

	/**
	 * Listener de selection utilisateur
	 *
	 * @param v
	 *            La source de l'evenement
	 */
	private void onClickItemView( View v ) {
		RecyclerView.ViewHolder vh = RecyclerViewAdapterUtils.getViewHolder( v );
		if ( null != vh ) {
			int flatPosition = vh.getAdapterPosition();

			if ( flatPosition == RecyclerView.NO_POSITION ) {
				return;
			}

			long expandablePosition = mExpandableItemManager.getExpandablePosition( flatPosition );
			int groupPosition = RecyclerViewExpandableItemManager.getPackedPositionGroup( expandablePosition );

			if ( vh instanceof MyChildViewHolder && null != ( (MyChildViewHolder) vh ).mapBtIdDemandeAdd.get( v.getId() ) ) {
				Integer idDemande = ( (MyChildViewHolder) vh ).mapBtIdDemandeAdd.get( v.getId() );

				CRMPhoneEquipementSalarieDTO demandeToAdd = null;

				for ( CRMPhoneEquipementSalarieDTO demande : listeDemandesEquipements ) {
					if ( demande.getId().equals( idDemande ) ) {
						demandeToAdd = demande;
					}
				}

				if ( null != demandeToAdd ) {
					demandeToAdd.setQuantitePossedee( demandeToAdd.getQuantitePossedee() + 1 );

					( (MyChildViewHolder) vh ).updateQuantity( demandeToAdd );
				} else {
					throw new IllegalStateException( "Demande introuvable." );
				}

			} else if ( vh instanceof MyChildViewHolder && null != ( (MyChildViewHolder) vh ).mapBtIdDemandeRemove.get( v.getId() ) ) {
				Integer idDemande = ( (MyChildViewHolder) vh ).mapBtIdDemandeRemove.get( v.getId() );

				CRMPhoneEquipementSalarieDTO demandeToRemove = null;

				for ( CRMPhoneEquipementSalarieDTO demande : listeDemandesEquipements ) {
					if ( demande.getId().equals( idDemande ) ) {
						demandeToRemove = demande;
					}
				}

				if ( null != demandeToRemove ) {
					if ( demandeToRemove.getQuantitePossedee() > 0 ) {
						demandeToRemove.setQuantitePossedee( demandeToRemove.getQuantitePossedee() - 1 );

						( (MyChildViewHolder) vh ).updateQuantity( demandeToRemove );
					}
				} else {
					throw new IllegalStateException( "Demande introuvable." );
				}
			} else {

				switch ( v.getId() ) {

					case R.id.layoutLine : {
						handleOnClickGroupItemContainerView( groupPosition );
						break;
					}

					case R.id.btSaveNbEquipements : {
						MyChildViewHolder vhChild = (MyChildViewHolder) vh;

						// WARNING: en théorie, une seule taille pour un equipement mais si plusieures tailles, appel WS en boucle (latence)
						if ( null != vhChild.mapEquipements && !vhChild.mapEquipements.isEmpty() ) {
							for ( Map.Entry<CRMPhoneEquipementSalarieDTO, TextView> entry : vhChild.mapEquipements.entrySet() ) {
								try {
									WsUtil.saveEquipementSalarie( this, null, entry.getKey() );
									Toast.makeText( FragmentProvider.getFragment( FragmentTags.EQUIPEMENTS_RESP_SUIVI.getTagName() ).getContext(), "Mise à jour effectuée.", Toast.LENGTH_LONG ).show();
								} catch ( Exception ex ) {
									ex.printStackTrace();
									FirebaseCrashlytics.getInstance().recordException( ex );
									Log.d( "TAG", "Erreur lors de la l'enregistrement de la demande d'equipement." );
								}
							}
						}

						break;
					}

					default :
						throw new IllegalStateException( "Clic sur un element de l'IHM inconnu : " + v.getId() );
				}
			}
		} else {
			throw new IllegalStateException( "ViewHolder à null..." );
		}
	}

	/**
	 * Ouverture ou fermeture d'une ligne de devis
	 *
	 * @param groupPosition
	 *            Le numero de ligne
	 */
	private void handleOnClickGroupItemContainerView( int groupPosition ) {
		if ( mExpandableItemManager.isGroupExpanded( groupPosition ) ) {
			mExpandableItemManager.collapseGroup( groupPosition );
		} else {
			mExpandableItemManager.collapseAll();
			mExpandableItemManager.scrollToGroup( groupPosition, 150 );
			mExpandableItemManager.expandGroup( groupPosition );
		}
	}

	/**
	 * Gestionnaire de l'IHM
	 * Classe abstraite afin de gérer des vues du type container contenant des contenus(MyChildViewHolder)
	 * et des contenants(MyGroupViewHolder)
	 */
	private static abstract class MyBaseViewHolder extends AbstractExpandableItemViewHolder {

		private MyBaseViewHolder( View v ) {
			super( v );

			ButterKnife.bind( this, v );
		}
	}

	/**
	 * Lignes des devis
	 */
	static class MyGroupViewHolder extends ExpandableRespEquipementSuiviParSalarieAdapter.MyBaseViewHolder {

		@BindView( R.id.tvEquipementSuiviEquipementParSalarie )
		TextView tvEquipementSuiviEquipementParSalarie;

		/**
		 * Constructeur
		 *
		 * @param v
		 *            Le layout utilisé pour cette IHM
		 * @param mItemOnClickListener
		 *            Le listener de clic
		 */
		private MyGroupViewHolder( View v, View.OnClickListener mItemOnClickListener ) {
			super( v );

			v.setOnClickListener( mItemOnClickListener );
		}
	}

	/**
	 * Le contenu de la liste déroulant si cliqué par l'utilisateur
	 */
	static class MyChildViewHolder extends ExpandableRespEquipementSuiviParSalarieAdapter.MyBaseViewHolder {

		private Map<CRMPhoneEquipementSalarieDTO, TextView> mapEquipements = new HashMap<>();

		// Map contenant les boutons de diminution de quantité associés aux identifiants des demandes salariés
		Map<Integer, Integer> mapBtIdDemandeRemove = new HashMap<>();

		// Map contenant les boutons d'ajout de quantité associés aux identifiants des demandes salarié
		Map<Integer, Integer> mapBtIdDemandeAdd = new HashMap<>();

		@BindView( R.id.layoutSuiviParSalarie )
		LinearLayout layoutSuiviParSalarie;

		@BindView( R.id.btSaveNbEquipements )
		Button btSaveNbEquipements;

		private View.OnClickListener onClickListener;

		/**
		 * Constructeur
		 *
		 * @param v
		 *            Le layout de ligne enfant
		 * @param mItemOnClickListener
		 *            Le listener de clics
		 */
		private MyChildViewHolder( View v, View.OnClickListener mItemOnClickListener ) {
			super( v );
			this.onClickListener = mItemOnClickListener;

			btSaveNbEquipements.setOnClickListener( mItemOnClickListener );
		}

		public void setListeEquipementsDuSalarie( List<CRMPhoneEquipementSalarieDTO> listeEquipements ) {

			layoutSuiviParSalarie.removeAllViews();

			LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams( 0, LinearLayout.LayoutParams.WRAP_CONTENT, 1 );

			LinearLayout.LayoutParams lp1Bt = new LinearLayout.LayoutParams( 0, LinearLayout.LayoutParams.WRAP_CONTENT, 1 );
			lp1Bt.setMargins( 0, 0, 0, 0 );

			LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams( 0, LinearLayout.LayoutParams.WRAP_CONTENT, 4 );

			for ( CRMPhoneEquipementSalarieDTO equipement : listeEquipements ) {

				LinearLayout layoutLine = new LinearLayout( itemView.getContext() );
				layoutLine.setOrientation( LinearLayout.HORIZONTAL );
				layoutLine.setWeightSum( 8 );

				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );

				layoutLine.setLayoutParams( lp );

				TextView tvEquipement = new TextView( itemView.getContext() );
				tvEquipement.setText( equipement.getEquipement().getEquipement() );
				tvEquipement.setLayoutParams( lp4 );

				TextView tvTaille = new TextView( itemView.getContext() );
				tvTaille.setText( equipement.getTaille().getTaille() );
				tvTaille.setLayoutParams( lp1 );
				tvTaille.setGravity( Gravity.CENTER );

				Button btRemoveQte = new Button( itemView.getContext() );
				btRemoveQte.setText( R.string.moins );
				btRemoveQte.setLayoutParams( lp1Bt );
				btRemoveQte.setHeight( 36 );
				btRemoveQte.setId( View.generateViewId() );

				TextView tvQuantite = new TextView( itemView.getContext() );
				tvQuantite.setText( String.valueOf( equipement.getQuantitePossedee() ) );
				tvQuantite.setLayoutParams( lp1 );
				tvQuantite.setGravity( Gravity.CENTER );

				Button btAddQte = new Button( itemView.getContext() );
				btAddQte.setText( R.string.plus );
				btAddQte.setLayoutParams( lp1Bt );
				btAddQte.setHeight( 36 );
				btAddQte.setId( View.generateViewId() );

				layoutLine.addView( tvEquipement );
				layoutLine.addView( tvTaille );
				layoutLine.addView( btRemoveQte );
				layoutLine.addView( tvQuantite );
				layoutLine.addView( btAddQte );

				layoutSuiviParSalarie.addView( layoutLine );

				btAddQte.setOnClickListener( onClickListener );
				btRemoveQte.setOnClickListener( onClickListener );

				mapBtIdDemandeAdd.put( btAddQte.getId(), equipement.getId() );
				mapBtIdDemandeRemove.put( btRemoveQte.getId(), equipement.getId() );

				mapEquipements.put( equipement, tvQuantite );
			}
		}

		public void updateQuantity( CRMPhoneEquipementSalarieDTO equipement ) {
			mapEquipements.get( equipement ).setText( String.valueOf( equipement.getQuantitePossedee() ) );
		}
	}

	@Override
	public void notifyResponse( WsName wsName, Map<String, Object> callbackResources, Object response ) {
		switch ( wsName ) {
			case SAVE_EQUIPEMENT_SALARIE :
				// Nothing to do
				break;
			default :
				throw new IllegalStateException( "Reception d'une reponse de webservice provenant de : " + wsName + ". Les données associées sont : " + response );
		}
	}

	@Override
	public Fragment getFragmentSource() {
		return fragment;
	}
}