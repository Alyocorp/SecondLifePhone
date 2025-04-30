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
import fr.artemis.phone.dto.CRMPhoneEquipementDTO;
import fr.artemis.phone.dto.CRMPhoneEquipementSalarieDTO;
import fr.artemis.phone.fragments.FragmentProvider;
import fr.artemis.phone.fragments.FragmentTags;
import fr.artemis.phone.webservices.WsCaller;
import fr.artemis.phone.webservices.WsName;
import fr.artemis.phone.webservices.WsUtil;

public class ExpandableRespEquipementSuiviParEquipementAdapter extends AbstractExpandableItemAdapter<ExpandableRespEquipementSuiviParEquipementAdapter.MyGroupViewHolder, ExpandableRespEquipementSuiviParEquipementAdapter.MyChildViewHolder> implements WsCaller {

	// Le manager des objets déroulés ou non
	private RecyclerViewExpandableItemManager mExpandableItemManager;

	// Listener de clic sur les boutons contenus dans les listes déroulées
	private View.OnClickListener mItemOnClickListener = this::onClickItemView;

	// Map contenant le viewholderchild par numero de ligne
	private SparseArray<ExpandableRespEquipementSuiviParEquipementAdapter.MyGroupViewHolder> saViewHolders = new SparseArray<>();

	// Map associant les numeros de lignes (groupPosition) aux identifiants des equipements
	@SuppressLint( "UseSparseArrays" )
	private Map<Integer, CRMPhoneEquipementDTO> mapLignes = new HashMap<>();

	private Map<Integer, List<CRMPhoneEquipementSalarieDTO>> mapSalarieParEquipements = new HashMap<>();

	// La liste des demandes d'equipements de salarié
	private List<CRMPhoneEquipementSalarieDTO> listeDemandesEquipements;

	// Le fragment source
	private Fragment fragment;

	/**
	 * Constructeur
	 *
	 * @param expandableItemManager
	 *            Le manager des différentes listes déroulantes
	 */
	public ExpandableRespEquipementSuiviParEquipementAdapter( Fragment fragment, RecyclerViewExpandableItemManager expandableItemManager,
			List<CRMPhoneEquipementSalarieDTO> listeDemandesEquipements ) {
		this.fragment = fragment;
		this.mExpandableItemManager = expandableItemManager;
		this.listeDemandesEquipements = listeDemandesEquipements;

		List<CRMPhoneEquipementDTO> listeEquipementsPossedes = new ArrayList<>();

		boolean alreadyAdded;
		for ( CRMPhoneEquipementSalarieDTO equipementPossede : listeDemandesEquipements ) {
			alreadyAdded = false;
			for ( CRMPhoneEquipementDTO equipementExistant : listeEquipementsPossedes ) {
				if ( equipementPossede.getEquipement().getId().equals( equipementExistant.getId() ) ) {
					alreadyAdded = true;
				}
			}
			if ( !alreadyAdded ) {
				listeEquipementsPossedes.add( equipementPossede.getEquipement() );
				mapSalarieParEquipements.put( equipementPossede.getEquipement().getId(), new ArrayList<>() );
			}

			mapSalarieParEquipements.get( equipementPossede.getEquipement().getId() ).add( equipementPossede );
		}

		int position = 0;
		if ( !listeEquipementsPossedes.isEmpty() ) {
			for ( CRMPhoneEquipementDTO equipement : listeEquipementsPossedes ) {
				mapLignes.put( position, equipement );
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
	public ExpandableRespEquipementSuiviParEquipementAdapter.MyGroupViewHolder onCreateGroupViewHolder( @NonNull ViewGroup parent, int viewType ) {
		final LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
		final View v = inflater.inflate( R.layout.list_resp_equipement_suivi_par_equipement, parent, false );

		return new ExpandableRespEquipementSuiviParEquipementAdapter.MyGroupViewHolder( v, mItemOnClickListener );
	}

	@Override
	@NonNull
	public ExpandableRespEquipementSuiviParEquipementAdapter.MyChildViewHolder onCreateChildViewHolder( @NonNull ViewGroup parent, int viewType ) {
		final LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
		final View v = inflater.inflate( R.layout.item_resp_equipement_suivi_expanded_par_equipement, parent, false );
		return new ExpandableRespEquipementSuiviParEquipementAdapter.MyChildViewHolder( v, mItemOnClickListener );
	}

	@SuppressLint( "SetTextI18n" )
	@Override
	public void onBindGroupViewHolder( @NonNull ExpandableRespEquipementSuiviParEquipementAdapter.MyGroupViewHolder holder, int groupPosition, int viewType ) {

		CRMPhoneEquipementDTO equipement = mapLignes.get( groupPosition );
		holder.tvEquipementSuiviEquipementParEquipement.setText( equipement.getEquipement() );

		saViewHolders.put( groupPosition, holder );

		// Ligne cliquable
		holder.itemView.setClickable( true );
	}

	@Override
	public void onBindChildViewHolder( @NonNull ExpandableRespEquipementSuiviParEquipementAdapter.MyChildViewHolder holder, int groupPosition, int childPosition, int viewType ) {

		CRMPhoneEquipementDTO equipement = mapLignes.get( groupPosition );

		// TODO injecter les salariés avec leurs equipements
		holder.setListeSalariePossedantEquipement( mapSalarieParEquipements.get( equipement.getId() ) );
	}

	@Override
	public boolean onCheckCanExpandOrCollapseGroup( @NonNull ExpandableRespEquipementSuiviParEquipementAdapter.MyGroupViewHolder holder, int groupPosition, int x, int y, boolean expand ) {
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

			if ( vh instanceof ExpandableRespEquipementSuiviParEquipementAdapter.MyChildViewHolder
					&& null != ( (ExpandableRespEquipementSuiviParEquipementAdapter.MyChildViewHolder) vh ).mapBtIdDemandeAdd.get( v.getId() ) ) {
				Integer idDemande = ( (ExpandableRespEquipementSuiviParEquipementAdapter.MyChildViewHolder) vh ).mapBtIdDemandeAdd.get( v.getId() );

				CRMPhoneEquipementSalarieDTO demandeToAdd = null;

				for ( CRMPhoneEquipementSalarieDTO demande : listeDemandesEquipements ) {
					if ( demande.getId().equals( idDemande ) ) {
						demandeToAdd = demande;
					}
				}

				if ( null != demandeToAdd ) {
					demandeToAdd.setQuantitePossedee( demandeToAdd.getQuantitePossedee() + 1 );

					( (ExpandableRespEquipementSuiviParEquipementAdapter.MyChildViewHolder) vh ).updateQuantity( demandeToAdd );
				} else {
					throw new IllegalStateException( "Demande introuvable." );
				}

			} else if ( vh instanceof ExpandableRespEquipementSuiviParEquipementAdapter.MyChildViewHolder
					&& null != ( (ExpandableRespEquipementSuiviParEquipementAdapter.MyChildViewHolder) vh ).mapBtIdDemandeRemove.get( v.getId() ) ) {
				Integer idDemande = ( (ExpandableRespEquipementSuiviParEquipementAdapter.MyChildViewHolder) vh ).mapBtIdDemandeRemove.get( v.getId() );

				CRMPhoneEquipementSalarieDTO demandeToRemove = null;

				for ( CRMPhoneEquipementSalarieDTO demande : listeDemandesEquipements ) {
					if ( demande.getId().equals( idDemande ) ) {
						demandeToRemove = demande;
					}
				}

				if ( null != demandeToRemove ) {
					if ( demandeToRemove.getQuantitePossedee() > 0 ) {
						demandeToRemove.setQuantitePossedee( demandeToRemove.getQuantitePossedee() - 1 );

						( (ExpandableRespEquipementSuiviParEquipementAdapter.MyChildViewHolder) vh ).updateQuantity( demandeToRemove );
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
						ExpandableRespEquipementSuiviParEquipementAdapter.MyChildViewHolder vhChild = (ExpandableRespEquipementSuiviParEquipementAdapter.MyChildViewHolder) vh;

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
	static class MyGroupViewHolder extends ExpandableRespEquipementSuiviParEquipementAdapter.MyBaseViewHolder {

		@BindView( R.id.tvEquipementSuiviEquipementParEquipement )
		TextView tvEquipementSuiviEquipementParEquipement;

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
	static class MyChildViewHolder extends ExpandableRespEquipementSuiviParEquipementAdapter.MyBaseViewHolder {

		private Map<CRMPhoneEquipementSalarieDTO, TextView> mapEquipements = new HashMap<>();

		// Map contenant les boutons de diminution de quantité associés aux identifiants des demandes salariés
		Map<Integer, Integer> mapBtIdDemandeRemove = new HashMap<>();

		// Map contenant les boutons d'ajout de quantité associés aux identifiants des demandes salarié
		Map<Integer, Integer> mapBtIdDemandeAdd = new HashMap<>();

		@BindView( R.id.layoutSuiviParEquipement )
		LinearLayout layoutSuiviParEquipement;

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

		public void setListeSalariePossedantEquipement( List<CRMPhoneEquipementSalarieDTO> listeDemandes ) {
			layoutSuiviParEquipement.removeAllViews();

			LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams( 0, LinearLayout.LayoutParams.WRAP_CONTENT, 1 );

			LinearLayout.LayoutParams lp1Bt = new LinearLayout.LayoutParams( 0, LinearLayout.LayoutParams.WRAP_CONTENT, 1 );
			lp1Bt.setMargins( 0, 0, 0, 0 );

			LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams( 0, LinearLayout.LayoutParams.WRAP_CONTENT, 4 );

			for ( CRMPhoneEquipementSalarieDTO demande : listeDemandes ) {
				LinearLayout layoutLine = new LinearLayout( itemView.getContext() );
				layoutLine.setOrientation( LinearLayout.HORIZONTAL );
				layoutLine.setWeightSum( 8 );

				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );

				layoutLine.setLayoutParams( lp );

				TextView tvSalarie = new TextView( itemView.getContext() );
				tvSalarie.setText( demande.getSalarie().getNom() + " " + demande.getSalarie().getPrenom() );
				tvSalarie.setLayoutParams( lp4 );

				TextView tvTaille = new TextView( itemView.getContext() );
				tvTaille.setText( demande.getTaille().getTaille() );
				tvTaille.setLayoutParams( lp1 );
				tvTaille.setGravity( Gravity.CENTER );

				Button btRemoveQte = new Button( itemView.getContext() );
				btRemoveQte.setText( R.string.moins );
				btRemoveQte.setLayoutParams( lp1Bt );
				btRemoveQte.setHeight( 36 );
				btRemoveQte.setId( View.generateViewId() );

				TextView tvQuantite = new TextView( itemView.getContext() );
				tvQuantite.setText( String.valueOf( demande.getQuantitePossedee() ) );
				tvQuantite.setLayoutParams( lp1 );
				tvQuantite.setGravity( Gravity.CENTER );

				Button btAddQte = new Button( itemView.getContext() );
				btAddQte.setText( R.string.plus );
				btAddQte.setLayoutParams( lp1Bt );
				btAddQte.setHeight( 36 );
				btAddQte.setId( View.generateViewId() );

				layoutLine.addView( tvSalarie );
				layoutLine.addView( tvTaille );
				layoutLine.addView( btRemoveQte );
				layoutLine.addView( tvQuantite );
				layoutLine.addView( btAddQte );

				layoutSuiviParEquipement.addView( layoutLine );

				btAddQte.setOnClickListener( onClickListener );
				btRemoveQte.setOnClickListener( onClickListener );

				mapBtIdDemandeAdd.put( btAddQte.getId(), demande.getId() );
				mapBtIdDemandeRemove.put( btRemoveQte.getId(), demande.getId() );

				mapEquipements.put( demande, tvQuantite );
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