package fr.artemis.phone.fragments.utilisateur.depotsvente;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.artemis.phone.R;
import fr.artemis.phone.dto.CRMPhoneDepotsVenteDTO;
import fr.artemis.phone.lib.map.kml.KmlLayerArtemis;
import fr.artemis.phone.lib.map.kml.KmlPlacemarkArtemis;
import fr.artemis.phone.utils.ConstantesMaps;
import fr.artemis.phone.utils.PhoneUtil;
import fr.artemis.phone.utils.StorageUtil;
import fr.artemis.phone.webservices.WsCaller;
import fr.artemis.phone.webservices.WsName;
import fr.artemis.phone.webservices.WsUtil;

/**
 * Fragment des dépots vente
 */
public class FragmentDepotsVenteBeforeRefactoring extends Fragment implements OnMapReadyCallback, WsCaller {

	@BindView( R.id.mapViewDepotsVente )
	MapView mapView;

	@BindView( R.id.depotsVenteScrollView )
	ScrollView depotsVenteScrollView;

	@BindView( R.id.etDepotsVenteTitre )
	TextView etTitre;

	@BindView( R.id.etDepotsVenteAdresse )
	TextView etAdresse;

	@BindView( R.id.etDepotsVenteHoraires )
	TextView etHoraires;

	@BindView( R.id.spDepotsVenteBlacklist )
	Spinner spBlacklist;

	@BindView( R.id.spDepotsVenteCompte )
	Spinner spCompte;

	@BindView( R.id.tvLatitude )
	TextView tvLatitude;

	@BindView( R.id.tvLongitude )
	TextView tvLongitude;

	@BindView( R.id.btUpdateDepotsVente )
	Button btUpdate;

	// La map
	private GoogleMap map;

	// Le numéro de version de la map stocké dans le telephone
	private int versionStockee;

	// Le flux contenant les dépots vente
	private InputStream streamDepotsVente;

	// La surcouche affichée sur la carte comportant les depots vente
	private KmlLayerArtemis layerDepotsVente;

	// Le marker créé par l'utilisateur
	private KmlPlacemarkArtemis markerCreated = null;

	// Le marqueur sélectionné par l'utilisateur (permet d'afficher ses informations)
	private KmlPlacemarkArtemis placemarkSelected = null;

	// Le dépot vente selectionnée
	private final CRMPhoneDepotsVenteDTO depotsVente = new CRMPhoneDepotsVenteDTO();

	@Nullable
	@Override
	public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
		View view = inflater.inflate( R.layout.fragment_depots_vente, container, false );

		ButterKnife.bind( this, view );

		assert null != getActivity();

		loadDepotsVente();

		mapView.onCreate( savedInstanceState );
		mapView.getMapAsync( this );

		initView();

		return view;
	}

	@Override
	public void onMapReady( GoogleMap googleMap ) {

		assert null != getContext() && null != getActivity();

		try {
			map = googleMap;

			map.setMyLocationEnabled( true );
			map.setMinZoomPreference( 7 );
			map.setMapType( GoogleMap.MAP_TYPE_HYBRID );
			map.getUiSettings().setCompassEnabled( true );
			map.getUiSettings().setMapToolbarEnabled( true );
			map.getUiSettings().setMyLocationButtonEnabled( true );
			map.getUiSettings().setZoomControlsEnabled( true );

			LocationManager locationManager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );
			Location lastLocation = locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );

			// Recherche de la dernière position GPS connue dans le téléphone
			Object objOldLat = StorageUtil.readObjectInternal( getContext(), ConstantesMaps.KEY_LAST_POSITION_LATITUDE.getValue() );
			Object objOldLong = StorageUtil.readObjectInternal( getContext(), ConstantesMaps.KEY_LAST_POSITION_LONGITUDE.getValue() );

			double oldLatitude;
			double oldLongitude;

			if ( null != objOldLat && null != objOldLong ) {
				oldLatitude = Double.parseDouble( objOldLat.toString() );
				oldLongitude = Double.parseDouble( objOldLong.toString() );
			} else if ( null != lastLocation ) {
				oldLatitude = lastLocation.getLatitude();
				oldLongitude = lastLocation.getLongitude();
			} else {
				oldLatitude = 48.484672;
				oldLongitude = -2.676853;
			}

			map.animateCamera( CameraUpdateFactory.newLatLngZoom( new LatLng( oldLatitude, oldLongitude ), 9 ) );

			layerDepotsVente = new KmlLayerArtemis( map, streamDepotsVente, getContext(), 0x5454f2 );
			layerDepotsVente.addLayerToMap();

			map.setOnMarkerClickListener( marker -> {
				if ( null != markerCreated ) {
					updateBlocInfos( markerCreated );
				} else {
					updateBlocInfos( marker );
				}
				marker.setDraggable( true );
				return false;
			} );

			// Simple clic sur la carte en dehors des marqueurs = on déselectionne le marqueur selectionné précédemment
			map.setOnMapClickListener( latLng -> {
				if ( null != markerCreated ) {
					deleteMarkerCreated();
				}
				clearSelection();
			} );

			// Long clic sur un marqueur = on le déplace
			// Long clic en dehors d'un marqueur = on en créé un nouveau
			map.setOnMapLongClickListener( latLng -> {
				if ( null != markerCreated ) {
					deleteMarkerCreated();
				}
				clearSelection();
				createMarker( latLng );
			} );

		} catch ( SecurityException | IOException | ClassNotFoundException | XmlPullParserException ex ) {
			ex.printStackTrace();
		}
	}

	/**
	 * Récupération d'un flux content les dépôts vente
	 *
	 * @return Le flux des dépots vente
	 */
	private InputStream loadDepotsVente() {
		assert null != getContext();

		// Récupération des décheteries
		try {
			// Récupération du numero de version de la carte dans la mémoire du téléphone
			Object tmpObj = StorageUtil.readObjectInternal( getContext(), ConstantesMaps.KEY_VERSION_MAP_DEPOTS_VENTE.getValue() );
			if ( null != tmpObj ) {
				versionStockee = Integer.parseInt( tmpObj.toString() );
			}

			// Récupération du numéro de version de la carte sur le serveur
			WsUtil.getVersionMapDepotsVente( this, null );

		} catch ( Exception ex ) {
			ex.printStackTrace();
			FirebaseCrashlytics.getInstance().recordException(ex);
			Log.d( "TAG", "Erreur lors de la recuperation de la liste des depots vente." );
		}

		return null;
	}

	/**
	 * Initialisation de la vue
	 */
	private void initView() {
		assert null != getActivity();

		ArrayAdapter<String> adapterBlacklist = new ArrayAdapter<>( getActivity(), R.layout.spinner_textview, getResources().getStringArray( R.array.choix_blacklist ) );
		adapterBlacklist.setDropDownViewResource( R.layout.spinner_textview );
		spBlacklist.setAdapter( adapterBlacklist );

		ArrayAdapter<String> adapterCompte = new ArrayAdapter<>( getActivity(), R.layout.spinner_textview, getResources().getStringArray( R.array.choix_compte ) );
		adapterCompte.setDropDownViewResource( R.layout.spinner_textview );
		spCompte.setAdapter( adapterCompte );

		btUpdate.setOnClickListener( view -> saveDepotVente() );
	}

	/**
	 * Enregistrement d'un dépot vente
	 */
	private void saveDepotVente() {

		assert null != getActivity();

		depotsVente.setBlacklist( "Oui".equals( spBlacklist.getSelectedItem().toString() ) );
		depotsVente.setCompteOuvert( "Oui".equals( spCompte.getSelectedItem().toString() ) );
		depotsVente.setAdresse( etAdresse.getText().toString() );
		depotsVente.setHoraires( etHoraires.getText().toString() );
		depotsVente.setNom( etTitre.getText().toString() );

		if ( null != placemarkSelected.getProperty( "compteOuvert" ) ) {
			String compteOuvert = (String) placemarkSelected.getProperty( "compteOuvert" );
			if ( compteOuvert.equals( "Oui" ) ) {
				depotsVente.setCompteOuvert( true );
			} else if ( compteOuvert.equals( "Non" ) ) {
				depotsVente.setCompteOuvert( false );
			} else {
				depotsVente.setCompteOuvert( null );
			}
		}

		try {
			WsUtil.updateDepotVente( this, null, depotsVente );
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
	}

	/**
	 * Suppression d'un marker venant d'être créé (ex: Demande de création puis affichage d'un autre depot vente = on supprime le marqueur qui n'a pas été enregistré)
	 */
	private void deleteMarkerCreated() {
		if ( null != markerCreated ) {
			layerDepotsVente.deleteMarker( markerCreated );
		}
		markerCreated = null;
	}

	/**
	 * Création d'un nouveau marker
	 *
	 * @param latLng La position GPS du marker à créer
	 */
	private void createMarker( LatLng latLng ) {
		try {
			btUpdate.setEnabled( true );
			btUpdate.setText( R.string.enregistrer );

			markerCreated = layerDepotsVente.addMarker( latLng );
			depotsVente.setId( null );

		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
	}

	/**
	 * Deselection d'un marker
	 */
	private void clearSelection() {
		placemarkSelected = null;

		btUpdate.setEnabled( false );
		etTitre.setText( "" );
		etAdresse.setText( "" );
		etHoraires.setText( "" );
		spCompte.setSelection( 0 );
		spBlacklist.setSelection( 0 );

		depotsVenteScrollView.setBackgroundColor( Color.TRANSPARENT );
	}

	/**
	 * Mise à jour du bloc d'information du depot vente
	 *
	 * @param marker Le marker selectionné par l'utilisateur
	 */
	private void updateBlocInfos( Marker marker ) {
		KmlPlacemarkArtemis placemark = (KmlPlacemarkArtemis) layerDepotsVente.getContainerFeature( marker );
		if ( null == placemark ) {
			placemark = (KmlPlacemarkArtemis) layerDepotsVente.getFeature( marker );
		}
		this.updateBlocInfos( placemark );
	}

	/**
	 * Mise à jour du bloc d'information
	 *
	 * @param kmlPlacemark Le marqueur sur lequel l'utilisateur a appuyé
	 */
	private void updateBlocInfos( KmlPlacemarkArtemis kmlPlacemark ) {

		this.placemarkSelected = kmlPlacemark;

		btUpdate.setEnabled( true );

		if ( null != kmlPlacemark ) {

			btUpdate.setText( R.string.enregistrer );

			if ( null != kmlPlacemark.getProperty( "id" ) ) {
				depotsVente.setId( Integer.parseInt( String.valueOf( kmlPlacemark.getProperty( "id" ) ) ) );
			}
			String latitude = String.valueOf( ( (LatLng) kmlPlacemark.getGeometry().getGeometryObject() ).latitude );
			String longitude = String.valueOf( ( (LatLng) kmlPlacemark.getGeometry().getGeometryObject() ).longitude );
			depotsVente.setLatitude( latitude );
			depotsVente.setLongitude( longitude );

			String blacklist = String.valueOf( kmlPlacemark.getProperty( "blacklist" ) );
			String compte = String.valueOf( kmlPlacemark.getProperty( "compte" ) );

			if ( blacklist.equals( "true" ) ) {
				depotsVenteScrollView.setBackgroundColor( Color.RED );
				blacklist = "Oui";
			} else if ( blacklist.equals( "false" ) || blacklist.equals( "null" ) ) {
				depotsVenteScrollView.setBackgroundColor( Color.TRANSPARENT );
				blacklist = "Non";
			}

			if ( compte.equals( "null" ) ) {
				spCompte.setSelection( 2 );
			} else {
				for ( int pointeur = 0; pointeur < spCompte.getCount(); pointeur++ ) {
					if ( compte.equals( spCompte.getItemAtPosition( pointeur ) ) ) {
						spCompte.setSelection( pointeur );
					}
				}
			}

			for ( int pointeur = 0; pointeur < spBlacklist.getCount(); pointeur++ ) {
				if ( blacklist.equals( spBlacklist.getItemAtPosition( pointeur ) ) ) {
					spBlacklist.setSelection( pointeur );
				}
			}

			etTitre.setText( null != kmlPlacemark.getProperty( "name" ) ? String.valueOf( kmlPlacemark.getProperty( "name" ) ) : "" );
			etAdresse.setText( null != kmlPlacemark.getProperty( "description" ) ? String.valueOf( kmlPlacemark.getProperty( "description" ) ) : "" );
			etHoraires.setText( null != kmlPlacemark.getProperty( "horaires" ) ? String.valueOf( kmlPlacemark.getProperty( "horaires" ) ) : "" );
			tvLatitude.setText( latitude );
			tvLongitude.setText( longitude );
		}
	}

	@Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
	}


	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}

	@Override
	public void notifyResponse( WsName wsName, Map<String, Object> callbackResources, Object response ) {
		switch ( wsName ) {
			case GET_VERSION_MAP_DEPOTS_VENTE: {
				Integer version = (Integer) response;

				// Si la version a évolué, on recharge la carte et on la stocke dans le téléphone
				String kmlString;
				try {
					if ( versionStockee < version ) {
						Map<String, Object> mapRessources = new HashMap<>();
						mapRessources.put( "version", version );

						WsUtil.getDepotsVente( this, mapRessources );

					} else {
						kmlString = (String) StorageUtil.readObjectInternal( getContext(), ConstantesMaps.KEY_DEPOT_VENTE_STORAGE.getValue() );

						assert null != kmlString;

						streamDepotsVente = new ByteArrayInputStream( kmlString.getBytes() );
					}
				} catch ( IOException | ClassNotFoundException ex ) {
					ex.printStackTrace();
				}
				break;
			}

			case GET_KML_DEPOTS_VENTE: {
				try {
					String kmlString = (String) response;
					streamDepotsVente = new ByteArrayInputStream( kmlString.getBytes() );

					StorageUtil.writeObjectInternal( getContext(), ConstantesMaps.KEY_DEPOT_VENTE_STORAGE.getValue(), kmlString );
					StorageUtil.writeObjectInternal( getContext(), ConstantesMaps.KEY_VERSION_MAP_DEPOTS_VENTE.getValue(), callbackResources.get( "version" ) );
					break;

				} catch ( IOException ex ) {
					ex.printStackTrace();
				}
			}

			case UPDATE_DEPOT_VENTE: {
				CRMPhoneDepotsVenteDTO depotVenteUpdated = (CRMPhoneDepotsVenteDTO) response;

				String blackList;
				if ( depotVenteUpdated.isBlacklist() ) {
					blackList = "Oui";
				} else {
					blackList = "Non";
				}

				String compteOuvert;
				if ( null == depotVenteUpdated.getCompteOuvert() ) {
					compteOuvert = "Inconnu";
				} else if ( depotVenteUpdated.getCompteOuvert() ) {
					compteOuvert = "Oui";
				} else {
					compteOuvert = "Non";
				}

				// On injecte les propriétés de la décheterie qui ont été enregistré sur le serveur dans le marker
				if ( null != markerCreated ) {
					layerDepotsVente.addProperty( markerCreated, "id", String.valueOf( depotVenteUpdated.getId() ) );
					layerDepotsVente.addProperty( markerCreated, "compteOuvert", compteOuvert );
					layerDepotsVente.addProperty( markerCreated, "blacklist", blackList );
					layerDepotsVente.addProperty( markerCreated, "name", depotVenteUpdated.getNom() );
					layerDepotsVente.addProperty( markerCreated, "description", depotVenteUpdated.getAdresse() );
					layerDepotsVente.addProperty( markerCreated, "horaires", depotVenteUpdated.getHoraires() );
				} else if ( null != this.placemarkSelected ) {
					placemarkSelected.setProperty( "id", String.valueOf( depotVenteUpdated.getId() ) );
					placemarkSelected.setProperty( "compteOuvert", compteOuvert );
					placemarkSelected.setProperty( "blacklist", blackList );
					placemarkSelected.setProperty( "name", depotVenteUpdated.getNom() );
					placemarkSelected.setProperty( "description", depotVenteUpdated.getAdresse() );
					placemarkSelected.setProperty( "horaires", depotVenteUpdated.getHoraires() );

					layerDepotsVente.addProperty( placemarkSelected, "id", String.valueOf( depotVenteUpdated.getId() ) );
					layerDepotsVente.addProperty( placemarkSelected, "compteOuvert", compteOuvert );
					layerDepotsVente.addProperty( placemarkSelected, "blacklist", blackList );
					layerDepotsVente.addProperty( placemarkSelected, "name", depotVenteUpdated.getNom() );
					layerDepotsVente.addProperty( placemarkSelected, "description", depotVenteUpdated.getAdresse() );
					layerDepotsVente.addProperty( placemarkSelected, "horaires", depotVenteUpdated.getHoraires() );
				}

				// Si enregistrement d'une nouvelle decheterie, on réinitialise afin de préparer un nouvel enregistrement si nécessaire
				markerCreated = null;

				clearSelection();

				// On masque le clavier Android
				PhoneUtil.hideKeyboard( getActivity() );
				break;
			}

			default:
				throw new IllegalStateException( "Reception d'une reponse de webservice provenant de : " + wsName + ". Les données associées sont : " + response );
		}
	}

	@Override
	public Fragment getFragmentSource() {
		return this;
	}
}