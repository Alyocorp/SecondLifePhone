package fr.artemis.phone.fragments.responsable.equipements.liste.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import android.annotation.SuppressLint;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import fr.artemis.phone.dto.CRMPhoneEquipementTailleTypeDTO;
import fr.artemis.phone.fragments.FragmentProvider;
import fr.artemis.phone.fragments.FragmentTags;
import fr.artemis.phone.fragments.responsable.equipements.liste.FragmentRespEquipementListe;
import fr.artemis.phone.webservices.WsCaller;
import fr.artemis.phone.webservices.WsName;
import fr.artemis.phone.webservices.WsUtil;

/**
 * Adapter affichant un tableau déroulant à chaque ligne
 */
public class ExpandableRespEquipementListeAdapter extends AbstractExpandableItemAdapter<ExpandableRespEquipementListeAdapter.MyGroupViewHolder, ExpandableRespEquipementListeAdapter.MyChildViewHolder> implements WsCaller {

	// Le manager des objets déroulés ou non
	private RecyclerViewExpandableItemManager mExpandableItemManager;

	// Listener de clic sur les boutons contenus dans les listes déroulées
	private View.OnClickListener mItemOnClickListener = this::onClickItemView;

	// La liste des types de tailles
	private List<CRMPhoneEquipementTailleTypeDTO> listeTypesTailles;

	// La liste des types de tailles dans les child view holder
	private List<String> listeTailles;

	// Map contenant le viewholderchild par numero de ligne
	private SparseArray<MyGroupViewHolder> saViewHolders = new SparseArray<>();

	// Map associant les numeros de lignes (groupPosition) aux identifiants des equipements
	private Map<Integer, CRMPhoneEquipementDTO> mapLignes = new HashMap<>();

	private FragmentRespEquipementListe fragment;

	/**
	 * Constructeur
	 *
	 * @param expandableItemManager
	 *            Le manager des différentes listes déroulantes
	 * @param listeEquipements
	 *            La liste des equipements existants
	 * @param listeTypesTailles
	 *            La liste des types de tailles existants
	 */
	public ExpandableRespEquipementListeAdapter( FragmentRespEquipementListe fragment, RecyclerViewExpandableItemManager expandableItemManager, List<CRMPhoneEquipementDTO> listeEquipements,
			List<CRMPhoneEquipementTailleTypeDTO> listeTypesTailles ) {
		this.fragment = fragment;
		this.mExpandableItemManager = expandableItemManager;
		this.listeTypesTailles = listeTypesTailles;

		listeTailles = new ArrayList<>();

		for ( CRMPhoneEquipementTailleTypeDTO typeTaille : listeTypesTailles ) {
			listeTailles.add( typeTaille.getTailleType() );
		}

		int position = 0;
		if ( null != listeEquipements && !listeEquipements.isEmpty() ) {
			for ( CRMPhoneEquipementDTO equipement : listeEquipements ) {
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
	public ExpandableRespEquipementListeAdapter.MyGroupViewHolder onCreateGroupViewHolder( @NonNull ViewGroup parent, int viewType ) {
		final LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
		final View v = inflater.inflate( R.layout.list_resp_equipement, parent, false );

		return new ExpandableRespEquipementListeAdapter.MyGroupViewHolder( v, mItemOnClickListener );
	}

	@Override
	@NonNull
	public ExpandableRespEquipementListeAdapter.MyChildViewHolder onCreateChildViewHolder( @NonNull ViewGroup parent, int viewType ) {
		final LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
		final View v = inflater.inflate( R.layout.item_resp_equipement_liste_expanded, parent, false );
		return new ExpandableRespEquipementListeAdapter.MyChildViewHolder( v, listeTailles, mItemOnClickListener );
	}

	@SuppressLint( "SetTextI18n" )
	@Override
	public void onBindGroupViewHolder( @NonNull ExpandableRespEquipementListeAdapter.MyGroupViewHolder holder, int groupPosition, int viewType ) {

		holder.tvEquipement.setText( Objects.requireNonNull( mapLignes.get( groupPosition ) ).getEquipement() );

		saViewHolders.put( groupPosition, holder );

		// Ligne cliquable
		holder.itemView.setClickable( true );
	}

	@Override
	public void onBindChildViewHolder( @NonNull ExpandableRespEquipementListeAdapter.MyChildViewHolder holder, int groupPosition, int childPosition, int viewType ) {

		holder.etEquipement.setText( Objects.requireNonNull( mapLignes.get( groupPosition ) ).getEquipement() );

		for ( String typeTaille : listeTailles ) {
			if ( null != Objects.requireNonNull( mapLignes.get( groupPosition ) ).getTailleType()
					&& Objects.requireNonNull( mapLignes.get( groupPosition ) ).getTailleType().getTailleType().equals( typeTaille ) ) {
				holder.selectTaille( typeTaille );
			}
		}
	}

	@Override
	public boolean onCheckCanExpandOrCollapseGroup( @NonNull ExpandableRespEquipementListeAdapter.MyGroupViewHolder holder, int groupPosition, int x, int y, boolean expand ) {
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
				case R.id.layoutLine :
					handleOnClickGroupItemContainerView( groupPosition );
					break;

				case R.id.btSaveRespEquipement :
					MyChildViewHolder holder = (MyChildViewHolder) vh;
					saveDatasFromHolder( groupPosition, holder );
					break;

				case R.id.btRemoveEquipement :
					new AlertDialog.Builder( mExpandableItemManager.getRecyclerView().getContext() ).setTitle( "Suppression" ).setMessage( "Êtes-vous sûr de vouloir supprimer cet equipement ?" )
							.setPositiveButton( "Oui", ( dialog, which ) -> deleteEquipement( groupPosition ) ).setNegativeButton( "Non", null ).show();
					break;

				default :
					throw new IllegalStateException( "Clic sur un element de l'IHM inconnu : " + v.getId() );
			}
		} else {
			throw new IllegalStateException( "ViewHolder à null..." );
		}
	}

	/**
	 * Suppression d'un equipement
	 *
	 * @param groupPosition
	 *            La position de l'equipement à supprimer
	 */
	private void deleteEquipement( int groupPosition ) {

		try {
			Map<String, Object> mapResources = new HashMap<>();

			mapResources.put( "groupPosition", groupPosition );

			WsUtil.isEquipementDeletable( this, mapResources, mapLignes.get( groupPosition ).getId() );

		} catch ( Exception ex ) {
			ex.printStackTrace();
			FirebaseCrashlytics.getInstance().recordException( ex );
			Log.d( "TAG", "Erreur lors de la suppression d'un equipement." );
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
	private void saveDatasFromHolder( int position, MyChildViewHolder holder ) {

		try {
			CRMPhoneEquipementDTO equipement = mapLignes.get( position );

			assert equipement != null;

			boolean equipementExistantDejaPresent = false;

			for ( Map.Entry<Integer, CRMPhoneEquipementDTO> equipementExistantEntrySet : mapLignes.entrySet() ) {
				if ( null != equipementExistantEntrySet && null != equipementExistantEntrySet.getValue() && null != equipementExistantEntrySet.getValue().getEquipement()
						&& equipementExistantEntrySet.getValue().getEquipement().equals( equipement.getEquipement() ) ) {
					equipementExistantDejaPresent = true;
				}
			}

			if ( !equipementExistantDejaPresent ) {
				for ( CRMPhoneEquipementTailleTypeDTO tailleType : listeTypesTailles ) {
					if ( tailleType.getTailleType().equals( holder.spTypeTaille.getSelectedItem() ) ) {
						equipement.setTailleType( tailleType );
					}
				}

				equipement.setEquipement( holder.etEquipement.getText().toString() );
				RecyclerViewAdapterUtils.getParentViewHolderItemView( holder.itemView );
				mExpandableItemManager.collapseAll();

				saViewHolders.get( position ).tvEquipement.setText( equipement.getEquipement() );

				WsUtil.saveEquipement( this, null, equipement );
			} else {
				Toast.makeText( FragmentProvider.getFragment( FragmentTags.EQUIPEMENTS_RESP_LISTE.getTagName() ).getContext(), "Cet equipement existe déjà.", Toast.LENGTH_LONG ).show();
			}

		} catch ( Exception ex ) {
			ex.printStackTrace();
			FirebaseCrashlytics.getInstance().recordException( ex );
			Log.d( "TAG", "Erreur lors de l'enregistrement d'un equipement." );
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
	 * Ajout d'une ligne à la fin
	 */
	public void handleOnClickGroupItemAddBelowButton() {
		try {
			WsUtil.generateEmptyEquipement( this, null );
		} catch ( Exception ex ) {
			ex.printStackTrace();
			FirebaseCrashlytics.getInstance().recordException( ex );
			Log.d( "BUG", "Erreur lors de la création d'un equipement vide." );
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
	static class MyGroupViewHolder extends ExpandableRespEquipementListeAdapter.MyBaseViewHolder {

		@BindView( R.id.tvEquipement )
		TextView tvEquipement;

		@BindView( R.id.btRemoveEquipement )
		Button btRemoveEquipement;

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

			btRemoveEquipement.setOnClickListener( mItemOnClickListener );
		}
	}

	/**
	 * Le contenu de la liste déroulant si cliqué par l'utilisateur
	 */
	static class MyChildViewHolder extends ExpandableRespEquipementListeAdapter.MyBaseViewHolder {

		@BindView( R.id.etEquipement )
		TextView etEquipement;

		@BindView( R.id.spTypeTailleEquipement )
		Spinner spTypeTaille;

		@BindView( R.id.btSaveRespEquipement )
		Button btSave;

		/**
		 * Constructeur
		 *
		 * @param v
		 *            Le layout de ligne enfant
		 * @param listeTypesTailles
		 *            La liste des types de tailles
		 * @param mItemOnClickListener
		 *            Le listener de clics
		 */
		private MyChildViewHolder( View v, List<String> listeTypesTailles, View.OnClickListener mItemOnClickListener ) {
			super( v );

			// Injection de la liste des types de client dans le spinner
			ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>( v.getContext(), R.layout.spinner_item, listeTypesTailles );
			spTypeTaille.setAdapter( adapterSpinner );
			spTypeTaille.setSelection( 0 );

			btSave.setOnClickListener( mItemOnClickListener );
		}

		/**
		 * Selection de la taille de l'equipement
		 *
		 * @param typeTaille
		 *            Le type de taille selectionné
		 */
		private void selectTaille( String typeTaille ) {
			for ( int index = 0 ; index < spTypeTaille.getCount() ; index++ ) {
				if ( spTypeTaille.getItemAtPosition( index ).equals( typeTaille ) ) {
					spTypeTaille.setSelection( index );
				}
			}
		}
	}

	@Override
	public void notifyResponse( WsName wsName, Map<String, Object> mapResources, Object response ) {
		switch ( wsName ) {
			case IS_EQUIPEMENT_DELETABLE : {

				Integer groupPosition = (Integer) mapResources.get( "groupPosition" );

				if ( null != groupPosition ) {
					boolean isSupprimable = (boolean) response;
					if ( isSupprimable ) {
						WsUtil.deleteEquipement( this, mapResources, mapLignes.get( groupPosition ).getId() );
					} else {
						Toast.makeText( FragmentProvider.getFragment( FragmentTags.EQUIPEMENTS_RESP_LISTE.getTagName() ).getContext(), "Impossible de supprimer l'equipement.", Toast.LENGTH_LONG )
								.show();
					}
				} else {
					throw new IllegalStateException( "Impossible de récupérer la position de l'element supprimé" );
				}
				break;
			}
			case DELETE_EQUIPEMENT : {
				Integer groupPosition = (Integer) mapResources.get( "groupPosition" );

				if ( null != groupPosition ) {

					boolean deleted = (boolean) response;

					if ( deleted ) {
						// Suppression de l'equipement à supprimer dans les listes des equipements affichés
						mapLignes.remove( groupPosition );

						// Création d'une liste des equipements restants qui sont à reordonner suite à la suppression
						List<CRMPhoneEquipementDTO> listeEquipementAReordonner = new ArrayList<>();
						for ( Map.Entry<Integer, CRMPhoneEquipementDTO> equipementsConnusEntry : mapLignes.entrySet() ) {
							if ( null != equipementsConnusEntry ) {
								listeEquipementAReordonner.add( equipementsConnusEntry.getValue() );
							}
						}

						// Nettoyage de la map puis reorganisation avec les equipements restants
						mapLignes.clear();
						int newPos = 0;
						for ( CRMPhoneEquipementDTO equipementAReordonner : listeEquipementAReordonner ) {
							mapLignes.put( newPos, equipementAReordonner );
							newPos++;
						}

						notifyDataSetChanged();
					} else {
						Toast.makeText( FragmentProvider.getFragment( FragmentTags.EQUIPEMENTS_RESP_LISTE.getTagName() ).getContext(), "Erreur lors de la suppression de l'equipement.",
								Toast.LENGTH_LONG ).show();
					}
				} else {
					throw new IllegalStateException( "Impossible de récupérer la position de l'element supprimé" );
				}
				break;
			}
			case GENERATE_EMPTY_EQUIPEMENT : {
				CRMPhoneEquipementDTO newEquipement = (CRMPhoneEquipementDTO) response;

				int newPosition = mapLignes.size();

				mapLignes.put( newPosition, newEquipement );

				mExpandableItemManager.notifyGroupItemInserted( newPosition );

				notifyDataSetChanged();

				break;
			}
			case SAVE_EQUIPEMENT : {
				// Nothing to do
				break;
			}
			default :
				throw new IllegalStateException( "Reception d'une reponse de webservice provenant de : " + wsName + ". Les données associées sont : " + response );
		}
	}

	@Override
	public Fragment getFragmentSource() {
		return fragment;
	}
}