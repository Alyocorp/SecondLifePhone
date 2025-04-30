package fr.artemis.phone.fragments.responsable.equipements.demandes.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import android.annotation.SuppressLint;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.artemis.phone.R;
import fr.artemis.phone.components.advancedrecyclerview.RecyclerViewAdapterUtils;
import fr.artemis.phone.components.advancedrecyclerview.RecyclerViewExpandableItemManager;
import fr.artemis.phone.components.advancedrecyclerview.base.adapters.AbstractExpandableItemAdapter;
import fr.artemis.phone.components.advancedrecyclerview.base.viewholders.AbstractExpandableItemViewHolder;
import fr.artemis.phone.dto.CRMPhoneEquipementSalarieDTO;
import fr.artemis.phone.fragments.FragmentProvider;
import fr.artemis.phone.fragments.FragmentTags;
import fr.artemis.phone.webservices.WsCaller;
import fr.artemis.phone.webservices.WsName;
import fr.artemis.phone.webservices.WsUtil;

public class ExpandableRespEquipementDemandesParSalarieAdapter extends AbstractExpandableItemAdapter<ExpandableRespEquipementDemandesParSalarieAdapter.MyGroupViewHolder, ExpandableRespEquipementDemandesParSalarieAdapter.MyChildViewHolder> implements WsCaller {

	// Le manager des objets déroulés ou non
	private RecyclerViewExpandableItemManager mExpandableItemManager;

	// Listener de clic sur les boutons contenus dans les listes déroulées
	private View.OnClickListener mItemOnClickListener = this::onClickItemView;

	// Map contenant le viewholderchild par numero de ligne
	private SparseArray<ExpandableRespEquipementDemandesParSalarieAdapter.MyGroupViewHolder> saViewHolders = new SparseArray<>();

	// Map associant les numeros de lignes (groupPosition) aux identifiants des equipements
	@SuppressLint( "UseSparseArrays" )
	private Map<Integer, CRMPhoneEquipementSalarieDTO> mapLignes = new HashMap<>();

	private Fragment fragment;

	/**
	 * Constructeur
	 *
	 * @param expandableItemManager
	 *            Le manager des différentes listes déroulantes
	 */
	public ExpandableRespEquipementDemandesParSalarieAdapter( Fragment fragmentSource, RecyclerViewExpandableItemManager expandableItemManager,
			List<CRMPhoneEquipementSalarieDTO> listeDemandesEquipements ) {
		this.fragment = fragmentSource;
		this.mExpandableItemManager = expandableItemManager;

		int position = 0;
		if ( null != listeDemandesEquipements && !listeDemandesEquipements.isEmpty() ) {
			for ( CRMPhoneEquipementSalarieDTO demandeEquipement : listeDemandesEquipements ) {
				mapLignes.put( position, demandeEquipement );
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
	public ExpandableRespEquipementDemandesParSalarieAdapter.MyGroupViewHolder onCreateGroupViewHolder( @NonNull ViewGroup parent, int viewType ) {
		final LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
		final View v = inflater.inflate( R.layout.list_resp_equipement_demandes, parent, false );

		return new ExpandableRespEquipementDemandesParSalarieAdapter.MyGroupViewHolder( v, mItemOnClickListener );
	}

	@Override
	@NonNull
	public ExpandableRespEquipementDemandesParSalarieAdapter.MyChildViewHolder onCreateChildViewHolder( @NonNull ViewGroup parent, int viewType ) {
		final LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
		final View v = inflater.inflate( R.layout.item_resp_equipement_demandes_expanded_par_salarie, parent, false );
		return new ExpandableRespEquipementDemandesParSalarieAdapter.MyChildViewHolder( v, mItemOnClickListener );
	}

	@SuppressLint( "SetTextI18n" )
	@Override
	public void onBindGroupViewHolder( @NonNull ExpandableRespEquipementDemandesParSalarieAdapter.MyGroupViewHolder holder, int groupPosition, int viewType ) {

		holder.tvEquipementDemandeEquipement.setText( Objects.requireNonNull( mapLignes.get( groupPosition ) ).getEquipement().getEquipement() );

		saViewHolders.put( groupPosition, holder );

		// Ligne cliquable
		holder.itemView.setClickable( true );
	}

	@Override
	public void onBindChildViewHolder( @NonNull ExpandableRespEquipementDemandesParSalarieAdapter.MyChildViewHolder holder, int groupPosition, int childPosition, int viewType ) {
		holder.etQteDemandee.setText( String.valueOf( mapLignes.get( groupPosition ).getQuantiteDemandee() ) );
		holder.etQtePossedee.setText( String.valueOf( mapLignes.get( groupPosition ).getQuantitePossedee() ) );
		holder.tvTaille.setText( String.valueOf( mapLignes.get( groupPosition ).getTaille().getTaille() ) );
	}

	@Override
	public boolean onCheckCanExpandOrCollapseGroup( @NonNull ExpandableRespEquipementDemandesParSalarieAdapter.MyGroupViewHolder holder, int groupPosition, int x, int y, boolean expand ) {
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

			switch ( v.getId() ) {

				case R.id.layoutLine : {
					handleOnClickGroupItemContainerView( groupPosition );
					break;
				}

				case R.id.btUpdate : {
					ExpandableRespEquipementDemandesParSalarieAdapter.MyChildViewHolder holder = (ExpandableRespEquipementDemandesParSalarieAdapter.MyChildViewHolder) vh;

					if ( Integer.parseInt( holder.etQteDemandee.getText().toString() ) == 0 ) {
						mapLignes.get( groupPosition ).setDemandeCloturee( false );
						saveDatasFromHolder( groupPosition, holder );
						Toast.makeText( FragmentProvider.getFragment( FragmentTags.EQUIPEMENTS_RESP_DEMANDES.getTagName() ).getContext(),
								"Mise à jour de la demande d'equipement. La demande du salarié étant à 0, il est possible de clôturer cette demande.", Toast.LENGTH_LONG ).show();
					} else {
						mapLignes.get( groupPosition ).setDemandeCloturee( false );
						saveDatasFromHolder( groupPosition, holder );
						Toast.makeText( FragmentProvider.getFragment( FragmentTags.EQUIPEMENTS_RESP_DEMANDES.getTagName() ).getContext(), "Mise à jour effectuée.", Toast.LENGTH_LONG ).show();
					}

					break;
				}

				case R.id.btEnd : {
					ExpandableRespEquipementDemandesParSalarieAdapter.MyChildViewHolder holder = (ExpandableRespEquipementDemandesParSalarieAdapter.MyChildViewHolder) vh;

					if ( Integer.parseInt( holder.etQteDemandee.getText().toString() ) > 0 ) {
						mapLignes.get( groupPosition ).setDemandeCloturee( false );
						Toast.makeText( FragmentProvider.getFragment( FragmentTags.EQUIPEMENTS_RESP_DEMANDES.getTagName() ).getContext(),
								"Impossible de clôturer la demande étant donné que le salarié à toujours une demande.", Toast.LENGTH_LONG ).show();
					} else {
						mapLignes.get( groupPosition ).setDemandeCloturee( true );
						saveDatasFromHolder( groupPosition, holder );
						mapLignes.remove( groupPosition );

						// Création d'une liste des equipements restants qui sont à reordonner suite à la suppression
						List<CRMPhoneEquipementSalarieDTO> listeEquipementAReordonner = new ArrayList<>();
						for ( Map.Entry<Integer, CRMPhoneEquipementSalarieDTO> equipementsConnusEntry : mapLignes.entrySet() ) {
							if ( null != equipementsConnusEntry ) {
								listeEquipementAReordonner.add( equipementsConnusEntry.getValue() );
							}
						}

						// Nettoyage de la map puis reorganisation avec les equipements restants
						mapLignes.clear();
						int newPos = 0;
						for ( CRMPhoneEquipementSalarieDTO equipementAReordonner : listeEquipementAReordonner ) {
							mapLignes.put( newPos, equipementAReordonner );
							newPos++;
						}
						notifyDataSetChanged();
					}
					break;
				}

				case R.id.btAddQtePossedee : {
					ExpandableRespEquipementDemandesParSalarieAdapter.MyChildViewHolder holder = (ExpandableRespEquipementDemandesParSalarieAdapter.MyChildViewHolder) vh;

					int qtePossedee = Integer.parseInt( holder.etQtePossedee.getText().toString() );

					qtePossedee++;

					if ( qtePossedee <= 10 ) {
						holder.etQtePossedee.setText( String.valueOf( qtePossedee ) );
					}

					break;
				}

				case R.id.btRemoveQtePossedee : {
					ExpandableRespEquipementDemandesParSalarieAdapter.MyChildViewHolder holder = (ExpandableRespEquipementDemandesParSalarieAdapter.MyChildViewHolder) vh;

					int qtePossedee = Integer.parseInt( holder.etQtePossedee.getText().toString() );

					qtePossedee--;

					if ( qtePossedee >= 0 ) {
						holder.etQtePossedee.setText( String.valueOf( qtePossedee ) );
					}

					break;
				}

				case R.id.btAddQteDemandee : {
					ExpandableRespEquipementDemandesParSalarieAdapter.MyChildViewHolder holder = (ExpandableRespEquipementDemandesParSalarieAdapter.MyChildViewHolder) vh;

					int qteDemandee = Integer.parseInt( holder.etQteDemandee.getText().toString() );

					qteDemandee++;

					if ( qteDemandee <= 10 ) {
						holder.etQteDemandee.setText( String.valueOf( qteDemandee ) );
					}
					break;
				}

				case R.id.btRemoveQteDemandee : {
					ExpandableRespEquipementDemandesParSalarieAdapter.MyChildViewHolder holder = (ExpandableRespEquipementDemandesParSalarieAdapter.MyChildViewHolder) vh;

					int qteDemandee = Integer.parseInt( holder.etQteDemandee.getText().toString() );

					qteDemandee--;

					if ( qteDemandee >= 0 ) {
						holder.etQteDemandee.setText( String.valueOf( qteDemandee ) );
					}
					break;
				}

				default :
					throw new IllegalStateException( "Clic sur un element de l'IHM inconnu : " + v.getId() );
			}
		} else {
			throw new IllegalStateException( "ViewHolder à null..." );
		}
	}

	/**
	 * Injection des données saisies par l'utilisateur dans l'equipement
	 *
	 * @param position
	 *            La position
	 * @param holder
	 *            Les données
	 */
	private void saveDatasFromHolder( int position, ExpandableRespEquipementDemandesParSalarieAdapter.MyChildViewHolder holder ) {

		try {
			CRMPhoneEquipementSalarieDTO demandeSalarie = mapLignes.get( position );

			assert demandeSalarie != null;

			demandeSalarie.setQuantitePossedee( Integer.parseInt( holder.etQtePossedee.getText().toString() ) );
			demandeSalarie.setQuantiteDemandee( Integer.parseInt( holder.etQteDemandee.getText().toString() ) );

			WsUtil.saveEquipementSalarie( this, null, demandeSalarie );

		} catch ( Exception ex ) {
			ex.printStackTrace();
			FirebaseCrashlytics.getInstance().recordException( ex );
			Log.d( "TAG", "Erreur lors de l'enregistrement de la demande d'equipement." );
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

	@Override
	public Fragment getFragmentSource() {
		return this.fragment;
	}

	@Override
	public void notifyResponse( WsName wsName, Map<String, Object> callbackResources, Object response ) {
		switch ( wsName ) {
			case SAVE_EQUIPEMENT_SALARIE :
				// Nothing to interpret
				break;
			default :
				throw new IllegalStateException( "Reception d'une reponse de webservice provenant de : " + wsName + ". Les données associées sont : " + response );

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
	static class MyGroupViewHolder extends ExpandableRespEquipementDemandesParSalarieAdapter.MyBaseViewHolder {

		@BindView( R.id.tvEquipementDemandeEquipement )
		TextView tvEquipementDemandeEquipement;

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
	static class MyChildViewHolder extends ExpandableRespEquipementDemandesParSalarieAdapter.MyBaseViewHolder {

		@BindView( R.id.tvTaille )
		TextView tvTaille;

		@BindView( R.id.btRemoveQtePossedee )
		Button btRemoveQtePossedee;

		@BindView( R.id.etQtePossedee )
		TextView etQtePossedee;

		@BindView( R.id.btAddQtePossedee )
		Button btAddQtePossedee;

		@BindView( R.id.btRemoveQteDemandee )
		Button btRemoveQteDemandee;

		@BindView( R.id.etQteDemandee )
		TextView etQteDemandee;

		@BindView( R.id.btAddQteDemandee )
		Button btAddQteDemandee;

		@BindView( R.id.btUpdate )
		Button btUpdate;

		@BindView( R.id.btEnd )
		Button btEnd;

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

			btAddQteDemandee.setOnClickListener( mItemOnClickListener );
			btAddQtePossedee.setOnClickListener( mItemOnClickListener );
			btRemoveQteDemandee.setOnClickListener( mItemOnClickListener );
			btRemoveQtePossedee.setOnClickListener( mItemOnClickListener );
			btUpdate.setOnClickListener( mItemOnClickListener );
			btEnd.setOnClickListener( mItemOnClickListener );
		}
	}
}