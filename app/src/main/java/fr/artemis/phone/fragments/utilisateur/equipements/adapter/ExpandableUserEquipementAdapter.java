package fr.artemis.phone.fragments.utilisateur.equipements.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.artemis.phone.R;
import fr.artemis.phone.components.advancedrecyclerview.RecyclerViewAdapterUtils;
import fr.artemis.phone.components.advancedrecyclerview.RecyclerViewExpandableItemManager;
import fr.artemis.phone.components.advancedrecyclerview.base.adapters.AbstractExpandableItemAdapter;
import fr.artemis.phone.components.advancedrecyclerview.base.viewholders.AbstractExpandableItemViewHolder;
import fr.artemis.phone.dto.CRMPhoneEquipementDTO;
import fr.artemis.phone.dto.CRMPhoneEquipementSalarieDTO;
import fr.artemis.phone.dto.CRMPhoneEquipementTailleDTO;
import fr.artemis.phone.dto.CRMPhoneEquipementTailleTypeDTO;
import fr.artemis.phone.fragments.FragmentProvider;
import fr.artemis.phone.fragments.FragmentTags;
import fr.artemis.phone.webservices.WsCaller;
import fr.artemis.phone.webservices.WsName;
import fr.artemis.phone.webservices.WsUtil;

/**
 * Adapter affichant un tableau déroulant à chaque ligne
 */
public class ExpandableUserEquipementAdapter extends AbstractExpandableItemAdapter<ExpandableUserEquipementAdapter.MyGroupViewHolder, ExpandableUserEquipementAdapter.MyChildViewHolder> implements WsCaller {

	// Le manager des objets déroulés ou non
	private RecyclerViewExpandableItemManager mExpandableItemManager;

	// Listener de clic sur les boutons contenus dans les listes déroulées
	private View.OnClickListener mItemOnClickListener = this::onClickItemView;

	// Map contenant le viewholderchild par numero de ligne
	private SparseArray<MyGroupViewHolder> saViewHolders = new SparseArray<>();

	// Map associant les numeros de lignes (groupPosition) aux identifiants des equipements
	private Map<Integer, CRMPhoneEquipementSalarieDTO> mapLignes = new HashMap<>();

	// La liste des types de tailles avec leurs tailles
	private List<CRMPhoneEquipementTailleTypeDTO> listeTypesTailles;

	private List<String> listeEquipements = new ArrayList<>();

	private Map<String, List<String>> mapTailles = new HashMap<>();

	private Map<String, CRMPhoneEquipementDTO> mapEquipements = new HashMap<>();

	private Fragment fragment;

	/**
	 * Constructeur
	 *
	 * @param expandableItemManager
	 *            Le manager des différentes listes déroulantes
	 * @param listeEquipements
	 *            La liste des equipements existants
	 * @param listeTypesTailles
	 *            La liste des types de tailles avec leurs tailles possibles
	 */
	public ExpandableUserEquipementAdapter( Fragment fragment, RecyclerViewExpandableItemManager expandableItemManager, List<CRMPhoneEquipementDTO> listeEquipements,
			List<CRMPhoneEquipementSalarieDTO> listeEquipementsSalarie, List<CRMPhoneEquipementTailleTypeDTO> listeTypesTailles ) {
		this.fragment = fragment;
		this.mExpandableItemManager = expandableItemManager;
		this.listeTypesTailles = listeTypesTailles;

		int position = 0;
		for ( CRMPhoneEquipementSalarieDTO equipement : listeEquipementsSalarie ) {
			mapLignes.put( position, equipement );
			position++;
		}

		if ( null != listeEquipements && !listeEquipements.isEmpty() ) {
			for ( CRMPhoneEquipementDTO equipement : listeEquipements ) {
				if ( null != equipement.getEquipement() ) {
					this.listeEquipements.add( equipement.getEquipement() );
					this.mapEquipements.put( equipement.getEquipement(), equipement );
				}
			}
		}

		for ( CRMPhoneEquipementTailleTypeDTO typeTaille : listeTypesTailles ) {
			if ( null != typeTaille.getListeTailles() && !typeTaille.getListeTailles().isEmpty() ) {
				for ( CRMPhoneEquipementTailleDTO taille : typeTaille.getListeTailles() ) {

					List<String> listeTailles = mapTailles.get( typeTaille.getTailleType() );
					if ( null == listeTailles ) {
						listeTailles = new ArrayList<>();
					}
					listeTailles.add( taille.getTaille() );

					mapTailles.put( typeTaille.getTailleType(), listeTailles );
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
		return mapLignes.get( groupPosition ).getId();
	}

	@Override
	public long getChildId( int groupPosition, int childPosition ) {
		// Numero d'equipement + 10000 histoire de ne pas les confondre avec les lignes d'equipement
		return mapLignes.get( groupPosition ).getId() + 10000;
	}

	@Override
	@NonNull
	public ExpandableUserEquipementAdapter.MyGroupViewHolder onCreateGroupViewHolder( @NonNull ViewGroup parent, int viewType ) {
		final LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
		final View v = inflater.inflate( R.layout.list_user_equipement, parent, false );

		return new ExpandableUserEquipementAdapter.MyGroupViewHolder( v, mItemOnClickListener );
	}

	@Override
	@NonNull
	public ExpandableUserEquipementAdapter.MyChildViewHolder onCreateChildViewHolder( @NonNull ViewGroup parent, int viewType ) {
		final LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
		final View v = inflater.inflate( R.layout.item_user_equipement_expanded, parent, false );
		return new ExpandableUserEquipementAdapter.MyChildViewHolder( v, mapTailles, listeEquipements, mapEquipements, mItemOnClickListener );
	}

	@SuppressLint( "SetTextI18n" )
	@Override
	public void onBindGroupViewHolder( @NonNull ExpandableUserEquipementAdapter.MyGroupViewHolder holder, int groupPosition, int viewType ) {

		if ( null != mapLignes.get( groupPosition ) && null != mapLignes.get( groupPosition ).getEquipement() ) {
			holder.tvEquipement.setText( mapLignes.get( groupPosition ).getEquipement().getEquipement() );
		}

		saViewHolders.put( groupPosition, holder );

		// Ligne cliquable
		holder.itemView.setClickable( true );
	}

	@Override
	public void onBindChildViewHolder( @NonNull ExpandableUserEquipementAdapter.MyChildViewHolder holder, int groupPosition, int childPosition, int viewType ) {

		if ( null != mapLignes.get( groupPosition ) ) {
			if ( null != mapLignes.get( groupPosition ).getEquipement() ) {
				holder.selectEquipement( mapLignes.get( groupPosition ).getEquipement().getEquipement() );
			} else {
				holder.selectEquipement( null );
			}
			if ( null != mapLignes.get( groupPosition ).getTaille() ) {
				holder.selectTaille( mapLignes.get( groupPosition ).getTaille().getTaille() );
			} else {
				holder.selectTaille( null );
			}
		}

		if ( null != mapLignes.get( groupPosition ).getQuantiteDemandee() ) {
			holder.etQteDemandee.setText( String.valueOf( mapLignes.get( groupPosition ).getQuantiteDemandee() ) );
		} else {
			holder.etQteDemandee.setText( "0" );
		}

		if ( null != mapLignes.get( groupPosition ).getQuantitePossedee() ) {
			holder.tvQtePossedee.setText( String.valueOf( mapLignes.get( groupPosition ).getQuantitePossedee() ) );
			if ( 0 != mapLignes.get( groupPosition ).getQuantitePossedee() ) {
				holder.spTaille.setEnabled( false );
			} else {
				holder.spTaille.setEnabled( true );
			}
		} else {
			holder.tvQtePossedee.setText( "0" );
			holder.spTaille.setEnabled( true );
		}

	}

	@Override
	public boolean onCheckCanExpandOrCollapseGroup( @NonNull ExpandableUserEquipementAdapter.MyGroupViewHolder holder, int groupPosition, int x, int y, boolean expand ) {
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

				case R.id.btSaveUserEquipement : {
					MyChildViewHolder holder = (MyChildViewHolder) vh;
					saveDatasFromHolder( groupPosition, holder );
					break;
				}

				case R.id.btAddEquipement : {
					handleOnClickGroupItemAddBelowButton();
					break;
				}

				case R.id.btRemoveEquipement : {
					new AlertDialog.Builder( mExpandableItemManager.getRecyclerView().getContext() ).setTitle( "Suppression" )
							.setMessage( "Êtes-vous sûr de vouloir supprimer cet demande d'equipement ?" ).setPositiveButton( "Oui", ( dialog, which ) -> deleteEquipementSalarie( groupPosition ) )
							.setNegativeButton( "Non", null ).show();
					break;
				}

				case R.id.btAddQteDemandee : {
					CRMPhoneEquipementSalarieDTO equipementSalarie = mapLignes.get( groupPosition );
					assert equipementSalarie != null;
					int qteDemandee = equipementSalarie.getQuantiteDemandee();
					if ( qteDemandee >= 0 && qteDemandee < 15 ) {
						qteDemandee++;
						equipementSalarie.setQuantiteDemandee( qteDemandee );
						MyChildViewHolder holder = (MyChildViewHolder) vh;
						holder.etQteDemandee.setText( String.valueOf( qteDemandee ) );
					}
					break;
				}

				case R.id.btRemoveQteDemandee : {
					CRMPhoneEquipementSalarieDTO equipementSalarie = mapLignes.get( groupPosition );
					assert equipementSalarie != null;
					int qteDemandee = equipementSalarie.getQuantiteDemandee();
					if ( qteDemandee > 0 && qteDemandee <= 15 ) {
						qteDemandee--;
						equipementSalarie.setQuantiteDemandee( qteDemandee );
						MyChildViewHolder holder = (MyChildViewHolder) vh;
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
	 * Suppression d'un equipement
	 *
	 * @param groupPosition
	 *            La position de l'equipement à supprimer
	 */
	private void deleteEquipementSalarie( int groupPosition ) {

		try {
			Map<String, Object> mapResources = new HashMap<>();
			mapResources.put( "groupPosition", groupPosition );
			WsUtil.isEquipementSalarieDeletable( this, mapResources, mapLignes.get( groupPosition ).getId() );
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
		CRMPhoneEquipementSalarieDTO equipementSalarie = mapLignes.get( position );

		String equipementSelectedString = holder.spEquipement.getSelectedItem().toString();

		CRMPhoneEquipementDTO equipementSelected = mapEquipements.get( equipementSelectedString );

		assert equipementSelected != null;

		CRMPhoneEquipementTailleTypeDTO typeDeTailleDeLequipement = equipementSelected.getTailleType();

		CRMPhoneEquipementTailleDTO tailleToSelect = null;
		for ( CRMPhoneEquipementTailleTypeDTO typeTaillePossible : listeTypesTailles ) {
			if ( typeDeTailleDeLequipement.getId().equals( typeTaillePossible.getId() ) ) {
				for ( CRMPhoneEquipementTailleDTO taillePossible : typeDeTailleDeLequipement.getListeTailles() ) {
					if ( taillePossible.getTaille().equals( holder.spTaille.getSelectedItem().toString() ) ) {
						tailleToSelect = taillePossible;
					}
				}
			}
		}

		assert equipementSalarie != null;

		equipementSalarie.setTaille( tailleToSelect );
		equipementSalarie.setEquipement( equipementSelected );
		equipementSalarie.setQuantiteDemandee( Integer.parseInt( holder.etQteDemandee.getText().toString() ) );

		try {
			WsUtil.saveEquipementSalarie( this, null, equipementSalarie );

			MyGroupViewHolder vh = saViewHolders.get( position );
			vh.tvEquipement.setText( equipementSelectedString );

			mExpandableItemManager.collapseAll();

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

	/**
	 * Ajout d'une ligne à la fin
	 */
	public void handleOnClickGroupItemAddBelowButton() {
		try {
			WsUtil.generateEmptyEquipementSalarie( this, null );
		} catch ( Exception ex ) {
			ex.printStackTrace();
			FirebaseCrashlytics.getInstance().recordException( ex );
			Log.d( "BUG", "Erreur lors de la création d'une demande d'equipement vide." );
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
	static class MyGroupViewHolder extends ExpandableUserEquipementAdapter.MyBaseViewHolder {

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
	static class MyChildViewHolder extends ExpandableUserEquipementAdapter.MyBaseViewHolder {

		@BindView( R.id.tvQtePossedee )
		TextView tvQtePossedee;

		@BindView( R.id.etQteDemandee )
		EditText etQteDemandee;

		@BindView( R.id.btRemoveQteDemandee )
		Button btRemoveQteDemandee;

		@BindView( R.id.btAddQteDemandee )
		Button btAddQteDemandee;

		@BindView( R.id.btSaveUserEquipement )
		Button btSaveUserEquipement;

		@BindView( R.id.spEquipement )
		Spinner spEquipement;

		@BindView( R.id.spTaille )
		Spinner spTaille;

		private String tailleSelected;

		/**
		 * Constructeur
		 *
		 * @param v
		 *            Le layout de ligne enfant
		 * @param mapEquipements
		 *            Map des equipements
		 * @param mItemOnClickListener
		 *            Le listener de clics
		 */
		private MyChildViewHolder( View v, Map<String, List<String>> mapTailles, List<String> listeEquipements, Map<String, CRMPhoneEquipementDTO> mapEquipements,
				View.OnClickListener mItemOnClickListener ) {
			super( v );

			// Injection de la liste des equipements dans le spinner
			ArrayAdapter<String> adapterSpinnerEquipement = new ArrayAdapter<>( v.getContext(), R.layout.spinner_item, listeEquipements );
			if ( null != listeEquipements && !listeEquipements.isEmpty() ) {
				spEquipement.setAdapter( adapterSpinnerEquipement );
			}

			handleFuckedEvents( v, mapTailles, mapEquipements );

			btRemoveQteDemandee.setOnClickListener( mItemOnClickListener );
			btAddQteDemandee.setOnClickListener( mItemOnClickListener );
			btSaveUserEquipement.setOnClickListener( mItemOnClickListener );
		}

		/**
		 * On magouille avec les listeners :
		 * <p>
		 * Le listener sur les equipements permet d'alimenter la bonne liste des tailles en fonction de la selection de l'equipement
		 * (ex : chaussures -> pointures du 36 au 49 ; Veste -> de XS à XXL)
		 * <p>
		 * La logique devrait être la suivante :
		 * 1 - On instancie un listener de selection d'equipement sur le spinner
		 * 2 - On injecte la valeur à selectionner dans le spinner de l'equipement
		 * 3 - Le listener est déclenché et charge la liste du spinner des tailles
		 * 4 - On injecte la valeur à selectionner dans le spinner des tailles et on la selectionne.
		 * </p>
		 * <p>
		 * Hors, le fonctionnement est le suivant :
		 * 1 - On instancie le listener de selection d'equipement sur le spinner
		 * 2 - On injecte la valeur à selectionner dans le spinner l'equipement
		 * 3 - Le listener ne se déclenche pas (peut-être qu'Android créé un autre thread qui attend la fin du programme avant de se déclencher)
		 * 4 - On injecte la valeur à selectionner dans le spinner des tailles
		 * 5 - Le spinner des tailles n'est pas alimenté donc impossible de selectionner la valeur injectée (De ce fait on stocke la valeur dans un objet en attendant que le listener réagisse)
		 * 6 - Le listener se déclenche mais trop tard, on a stocké la valeur dans un objet qui alourdit inutilement l'application puisqu'elle ne sera pas garbage collect
		 * 7 - Le listener remplit le spinner des tailles avec les bonnes valeurs
		 * 8 - On selectionne la valeur stockée precedement dans le spinner des tailles
		 * </p>
		 *
		 * @param mapTailles
		 *            Map des tailles
		 * @param mapEquipements
		 *            Map des equipements
		 */
		private void handleFuckedEvents( View v, Map<String, List<String>> mapTailles, Map<String, CRMPhoneEquipementDTO> mapEquipements ) {

			spEquipement.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected( AdapterView<?> adapterView, View view, int i, long l ) {
					if ( null != spEquipement.getSelectedItem() ) {
						ArrayAdapter<String> adapterSpinnerTailles = new ArrayAdapter<>( v.getContext(), R.layout.spinner_item_center,
								mapTailles.get( mapEquipements.get( spEquipement.getSelectedItem().toString() ).getTailleType().getTailleType() ) );
						spTaille.setAdapter( adapterSpinnerTailles );

						if ( null != tailleSelected ) {
							for ( int index = 0 ; index < spTaille.getCount() ; index++ ) {
								if ( spTaille.getItemAtPosition( index ).equals( tailleSelected ) ) {
									spTaille.setSelection( index );
								}
							}
						}
					}
				}

				@Override
				public void onNothingSelected( AdapterView<?> adapterView ) {}
			} );
		}

		/**
		 * Injection de la taille de l'equipement.
		 * On stocke la valeur puisque le listener ne réagit pas comme il le devrait
		 *
		 * @param taille
		 *            La taille selectionné
		 */
		private void selectTaille( String taille ) {
			this.tailleSelected = taille;
		}

		/**
		 * Selection de l'equipement
		 *
		 * @param equipement
		 *            L'equipement
		 */
		private void selectEquipement( String equipement ) {
			if ( null != equipement ) {
				for ( int index = 0 ; index < spEquipement.getCount() ; index++ ) {
					if ( spEquipement.getItemAtPosition( index ).equals( equipement ) ) {
						if ( spEquipement.getSelectedItemPosition() == index ) {
							spEquipement.setSelection( -1 );
						}
						spEquipement.setSelection( index );
					}
				}
			} else {
				spEquipement.setSelection( 0 );
			}
		}
	}

	@Override
	public void notifyResponse( WsName wsName, Map<String, Object> callbackResources, Object response ) {
		switch ( wsName ) {
			case IS_EQUIPEMENT_SALARIE_DELETABLE : {

				Integer groupPosition = (Integer) callbackResources.get( "groupPosition" );

				if ( null != groupPosition ) {
					boolean result = (boolean) response;

					if ( result ) {
						WsUtil.deleteEquipementSalarie( this, callbackResources, mapLignes.get( groupPosition ).getId() );
					} else {
						Toast.makeText( FragmentProvider.getFragment( FragmentTags.EQUIPEMENTS_USER.getTagName() ).getContext(),
								"Impossible de supprimer la demande d'equipement. (equipement possédé > 0).", Toast.LENGTH_LONG ).show();
					}
				} else {
					throw new IllegalStateException( "Impossible de récupérer la position de l'element supprimé" );
				}
				break;
			}
			case DELETE_EQUIPEMENT_SALARIE : {

				Integer groupPosition = (Integer) callbackResources.get( "groupPosition" );

				if ( null != groupPosition ) {

					boolean result = (boolean) response;

					if ( result ) {

						// Suppression de l'equipement à supprimer dans les listes des equipements affichés
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
					} else {
						Toast.makeText( FragmentProvider.getFragment( FragmentTags.EQUIPEMENTS_USER.getTagName() ).getContext(), "Erreur lors de la suppression de la demande d'equipement salarié.",
								Toast.LENGTH_LONG ).show();
					}
				} else {
					throw new IllegalStateException( "Impossible de récupérer la position de l'element supprimé" );
				}
				break;
			}
			case GENERATE_EMPTY_EQUIPEMENT_SALARIE : {
				CRMPhoneEquipementSalarieDTO newEquipement = (CRMPhoneEquipementSalarieDTO) response;

				int newPosition = mapLignes.size();

				mapLignes.put( newPosition, newEquipement );

				mExpandableItemManager.notifyGroupItemInserted( newPosition );

				notifyDataSetChanged();
				break;
			}

			case SAVE_EQUIPEMENT_SALARIE : {
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