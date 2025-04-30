package fr.artemis.phone.fragments.responsable.equipements.demandes.adapter;

import android.annotation.SuppressLint;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.artemis.phone.R;
import fr.artemis.phone.components.advancedrecyclerview.RecyclerViewAdapterUtils;
import fr.artemis.phone.components.advancedrecyclerview.RecyclerViewExpandableItemManager;
import fr.artemis.phone.components.advancedrecyclerview.base.adapters.AbstractExpandableItemAdapter;
import fr.artemis.phone.components.advancedrecyclerview.base.viewholders.AbstractExpandableItemViewHolder;
import fr.artemis.phone.dto.CRMPhoneEquipementDTO;
import fr.artemis.phone.dto.CRMPhoneEquipementSalarieDTO;

public class ExpandableRespEquipementDemandesParEquipementAdapter extends AbstractExpandableItemAdapter<ExpandableRespEquipementDemandesParEquipementAdapter.MyGroupViewHolder, ExpandableRespEquipementDemandesParEquipementAdapter.MyChildViewHolder> {

	// Le manager des objets déroulés ou non
	private RecyclerViewExpandableItemManager mExpandableItemManager;

	// Listener de clic sur les boutons contenus dans les listes déroulées
	private View.OnClickListener mItemOnClickListener = this::onClickItemView;

	// Map contenant le viewholderchild par numero de ligne
	private SparseArray<ExpandableRespEquipementDemandesParEquipementAdapter.MyGroupViewHolder> saViewHolders = new SparseArray<>();

	// Map associant les numeros de lignes (groupPosition) aux equipements
	// On porura ensuite relié l'equipement à la liste des demandes
	@SuppressLint( "UseSparseArrays" )
	private Map<Integer, CRMPhoneEquipementDTO> mapLignes = new HashMap<>();

	private Map<CRMPhoneEquipementDTO, List<CRMPhoneEquipementSalarieDTO>> mapDemandesParEquipements;

	/**
	 * Constructeur
	 *
	 * @param expandableItemManager Le manager des différentes listes déroulantes
	 */
	public ExpandableRespEquipementDemandesParEquipementAdapter( RecyclerViewExpandableItemManager expandableItemManager, List<CRMPhoneEquipementSalarieDTO> listeDemandesEquipements ) {
		this.mExpandableItemManager = expandableItemManager;

		@SuppressLint( "UseSparseArrays" ) Map<Integer, CRMPhoneEquipementDTO> mapEquipementsParId = new HashMap<>();

		if ( null != listeDemandesEquipements && !listeDemandesEquipements.isEmpty() ) {

			// Construction d'une map de type Identifiant d'equipement associé à un equipement
			// Cette map est utile puisque chaque demande d'equipement instancie un nouvel equipement meme si celui ci existe deja
			for ( CRMPhoneEquipementSalarieDTO demandeEquipement : listeDemandesEquipements ) {
				mapEquipementsParId.put( demandeEquipement.getEquipement().getId(), demandeEquipement.getEquipement() );
			}

			mapDemandesParEquipements = new HashMap<>();
			// On créé ensuite une map associant chaque demande d'equipement à l'equipement en utilisant la liste des instances uniques d'equipement
			for ( CRMPhoneEquipementSalarieDTO demandeEquipement : listeDemandesEquipements ) {

				// Récupération de l'instance de l'equipement qu'on utilisera dans les listes affichées
				CRMPhoneEquipementDTO instancePrincipaleDeLEquipement = mapEquipementsParId.get( demandeEquipement.getEquipement().getId() );

				// Si l'equipement n'a pas encore de liste de demandes associée, on la créée
				if ( null == mapDemandesParEquipements.get( instancePrincipaleDeLEquipement ) ) {
					mapDemandesParEquipements.put( instancePrincipaleDeLEquipement, new ArrayList<>() );
				}

				List<CRMPhoneEquipementSalarieDTO> listeDemandes = mapDemandesParEquipements.get( instancePrincipaleDeLEquipement );

				listeDemandes.add( demandeEquipement );

				mapDemandesParEquipements.put( instancePrincipaleDeLEquipement, listeDemandes );

			}

			int position = 0;
			if ( !mapDemandesParEquipements.isEmpty() ) {
				for ( Map.Entry<CRMPhoneEquipementDTO, List<CRMPhoneEquipementSalarieDTO>> entries : mapDemandesParEquipements.entrySet() ) {
					mapLignes.put( position, entries.getKey() );
					position++;
				}
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
	public ExpandableRespEquipementDemandesParEquipementAdapter.MyGroupViewHolder onCreateGroupViewHolder( @NonNull ViewGroup parent, int viewType ) {
		final LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
		final View v = inflater.inflate( R.layout.list_resp_equipement_demandes, parent, false );

		return new ExpandableRespEquipementDemandesParEquipementAdapter.MyGroupViewHolder( v, mItemOnClickListener );
	}

	@Override
	@NonNull
	public ExpandableRespEquipementDemandesParEquipementAdapter.MyChildViewHolder onCreateChildViewHolder( @NonNull ViewGroup parent, int viewType ) {
		final LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
		final View v = inflater.inflate( R.layout.item_resp_equipement_demandes_expanded_par_equipement, parent, false );
		return new ExpandableRespEquipementDemandesParEquipementAdapter.MyChildViewHolder( v );
	}

	@SuppressLint( "SetTextI18n" )
	@Override
	public void onBindGroupViewHolder( @NonNull ExpandableRespEquipementDemandesParEquipementAdapter.MyGroupViewHolder holder, int groupPosition, int viewType ) {

		holder.tvEquipementDemandeEquipement.setText( mapLignes.get( groupPosition ).getEquipement() );

		saViewHolders.put( groupPosition, holder );

		// Ligne cliquable
		holder.itemView.setClickable( true );
	}

	@Override
	public void onBindChildViewHolder( @NonNull ExpandableRespEquipementDemandesParEquipementAdapter.MyChildViewHolder holder, int groupPosition, int childPosition, int viewType ) {

		// Création d'une map contenant les tailles et quantités demandées pour l'equipement selectionné
		List<CRMPhoneEquipementSalarieDTO> listeDemandesDeLEquipement = mapDemandesParEquipements.get( mapLignes.get( groupPosition ) );
		Map<String, Integer> mapTaillesNombre = new HashMap<>();
		for ( CRMPhoneEquipementSalarieDTO demandeSalarie : listeDemandesDeLEquipement ) {
			if ( null != demandeSalarie.getTaille() ) {
				if ( null == mapTaillesNombre.get( demandeSalarie.getTaille().getTaille() ) ) {
					mapTaillesNombre.put( demandeSalarie.getTaille().getTaille(), 0 );
				}

				Integer qteTotaleDemandee = mapTaillesNombre.get( demandeSalarie.getTaille().getTaille() );
				qteTotaleDemandee = qteTotaleDemandee + demandeSalarie.getQuantiteDemandee();

				mapTaillesNombre.put( demandeSalarie.getTaille().getTaille(), qteTotaleDemandee );
			}
		}

		// Organisation de la map par tailles
		TreeMap<String, Integer> mapSorted = new TreeMap<>( mapTaillesNombre );

		List<String> listeDemandes = new ArrayList<>();
		for ( Map.Entry<String, Integer> entry : mapSorted.entrySet() ) {
			if ( entry.getValue() > 0 ) {
				listeDemandes.add( entry.getValue().toString() + " en taille " + entry.getKey() );
			}
		}

		holder.setListeDemandes( listeDemandes );
	}

	@Override
	public boolean onCheckCanExpandOrCollapseGroup( @NonNull ExpandableRespEquipementDemandesParEquipementAdapter.MyGroupViewHolder holder, int groupPosition, int x, int y, boolean expand ) {
		return false;
	}

	/**
	 * Listener de selection utilisateur
	 *
	 * @param v La source de l'evenement
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

			if ( v.getId() == R.id.layoutLine ) {
				handleOnClickGroupItemContainerView( groupPosition );
			} else {
				throw new IllegalStateException( "Clic sur un element de l'IHM inconnu : " + v.getId() );
			}
		} else {
			throw new IllegalStateException( "ViewHolder à null..." );
		}
	}

	/**
	 * Ouverture ou fermeture d'une ligne de devis
	 *
	 * @param groupPosition Le numero de ligne
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
	static class MyGroupViewHolder extends ExpandableRespEquipementDemandesParEquipementAdapter.MyBaseViewHolder {

		@BindView( R.id.tvEquipementDemandeEquipement )
		TextView tvEquipementDemandeEquipement;

		/**
		 * Constructeur
		 *
		 * @param v                    Le layout utilisé pour cette IHM
		 * @param mItemOnClickListener Le listener de clic
		 */
		private MyGroupViewHolder( View v, View.OnClickListener mItemOnClickListener ) {
			super( v );

			v.setOnClickListener( mItemOnClickListener );
		}
	}

	/**
	 * Le contenu de la liste déroulant si cliqué par l'utilisateur
	 */
	static class MyChildViewHolder extends ExpandableRespEquipementDemandesParEquipementAdapter.MyBaseViewHolder {

		@BindView( R.id.layoutDemandes )
		LinearLayout layoutDemandes;

		/**
		 * Constructeur
		 *
		 * @param v Le layout de ligne enfant
		 */
		private MyChildViewHolder( View v ) {
			super( v );
		}

		public void setListeDemandes( List<String> listeDemandes ) {
			layoutDemandes.removeAllViews();

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );
			params.setMargins( 32, 5, 5, 5 );
			if ( null != listeDemandes && !listeDemandes.isEmpty() ) {
				TextView tv;
				for ( String demande : listeDemandes ) {
					tv = new TextView( itemView.getContext() );
					tv.setLayoutParams( params );
					tv.setText( demande );
					tv.setTextSize( 18 );
					layoutDemandes.addView( tv );
				}
			}
		}
	}
}