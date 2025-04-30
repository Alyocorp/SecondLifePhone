package fr.artemis.phone.fragments.utilisateur.decheterie;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.artemis.phone.R;
import fr.artemis.phone.activities.main.MainActivity;
import fr.artemis.phone.components.textview.EmbeddingEditText;
import fr.artemis.phone.database.SQLiteDatabaseDecheteriesHelper;
import fr.artemis.phone.dto.CRMPhoneDecheterieDTO;
import fr.artemis.phone.dto.CRMPhoneDecheteriesTarifsDTO;
import fr.artemis.phone.dto.CRMPhoneDecheteriesUpdatesDTO;
import fr.artemis.phone.lib.map.kml.KmlFeatureParserArtemis;
import fr.artemis.phone.lib.map.kml.KmlLayerArtemis;
import fr.artemis.phone.lib.map.kml.KmlPlacemarkArtemis;
import fr.artemis.phone.lib.map.kml.KmlPointArtemis;
import fr.artemis.phone.utils.Constantes;
import fr.artemis.phone.utils.ConstantesMaps;
import fr.artemis.phone.utils.StorageUtil;
import fr.artemis.phone.webservices.WsCaller;
import fr.artemis.phone.webservices.WsName;
import fr.artemis.phone.webservices.WsUtil;

/**
 * Fragment des decheteries
 */
public class FragmentDecheterie extends Fragment implements OnMapReadyCallback, WsCaller {

	@BindView( R.id.sp_decheterie_type )
	Spinner spType;

	@BindView( R.id.sp_decheterie_carte )
	Spinner spCarte;

	@BindView( R.id.sp_decheterie_barriere )
	Spinner spBarriere;

	@BindView( R.id.sp_decheterie_blacklist )
	Spinner spBlacklist;

	@BindView( R.id.mapViewDecheteries )
	MapView mapView;

	@BindView( R.id.et_decheterie_titre )
	EditText etTitre;

	@BindView( R.id.et_decheterie_adresse )
	EditText etAdresse;

	@BindView( R.id.et_decheterie_horaires )
	EditText etHoraires;

	@BindView( R.id.tvLatitude )
	TextView tvLatitude;

	@BindView( R.id.tvLongitude )
	TextView tvLongitude;

	@BindView( R.id.bt_updateDechet )
	Button btUpdate;

	@BindView( R.id.dechetScrollView )
	ScrollView dechetScrollView;

	@BindView( R.id.layoutPro )
	RelativeLayout blocPro;

	@BindView( R.id.lbHoraires )
	TextView lbHoraires;

	private final static String PROPERTY_REGEX = "name|description|drawOrder|visibility|open|address|phoneNumber|horaires|type|barriere|carte|blacklist|id|compteOuvert";
	private final static String EXTENDED_DATA = "ExtendedData|tarifs";
	private final static String GEOMETRY_REGEX = "Point|LineString|Polygon|MultiGeometry|Track|MultiTrack";
	private final static String LAT_LNG_ALT_SEPARATOR = ",";
	private final static int LONGITUDE_INDEX = 0;
	private final static int LATITUDE_INDEX = 1;
	private final static int ALTITUDE_INDEX = 2;

	// La surcouche affichée sur la carte comportant les decheteries
	private KmlLayerArtemis layerDecheteries;

	// La map
	private GoogleMap map;

	// La version de la map stockee
	private int versionStockee;

	// Le flux comportant les decheteries
	private InputStream streamDecheteries;

	// Le marker créé par l'utilisateur
	private KmlPlacemarkArtemis markerCreated = null;

	// Le marqueur sélectionné par l'utilisateur (permet d'afficher ses
	// informations)
	private KmlPlacemarkArtemis placemarkSelected = null;

	// La decheterie selectionnée
	private final CRMPhoneDecheterieDTO decheterie = new CRMPhoneDecheterieDTO();

	// Instance d'accès à la base de données locale
	private SQLiteDatabaseDecheteriesHelper dbHelper;

	@Nullable
	@Override
	public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
		View view = inflater.inflate( R.layout.fragment_decheterie, container, false );

		ButterKnife.bind( this, view );

		dbHelper = SQLiteDatabaseDecheteriesHelper.getIntance( getActivity() );

		databaseSetUp();

		assert null != getActivity();

		mapView.onCreate( savedInstanceState );
		mapView.getMapAsync( this );

		ArrayAdapter<String> adapterTypeDechet = new ArrayAdapter<>( getActivity(), R.layout.spinner_textview, getResources().getStringArray( R.array.choix_type_decheterie ) );
		adapterTypeDechet.setDropDownViewResource( R.layout.spinner_textview );
		spType.setAdapter( adapterTypeDechet );

		ArrayAdapter<String> adapterBarriere = new ArrayAdapter<>( getActivity(), R.layout.spinner_textview, getResources().getStringArray( R.array.choix_barriere ) );
		adapterBarriere.setDropDownViewResource( R.layout.spinner_textview );
		spBarriere.setAdapter( adapterBarriere );

		ArrayAdapter<String> adapterCarte = new ArrayAdapter<>( getActivity(), R.layout.spinner_textview, getResources().getStringArray( R.array.choix_carte ) );
		adapterCarte.setDropDownViewResource( R.layout.spinner_textview );
		spCarte.setAdapter( adapterCarte );

		ArrayAdapter<String> adapterBlacklist = new ArrayAdapter<>( getActivity(), R.layout.spinner_textview, getResources().getStringArray( R.array.choix_blacklist ) );
		adapterBlacklist.setDropDownViewResource( R.layout.spinner_textview );
		spBlacklist.setAdapter( adapterBlacklist );

		btUpdate.setOnClickListener( evt -> saveDecheterie() );

		Places.initialize( getActivity(), "AIzaSyCABSScTERRdLYZbpjvAdg4qRkx8wavyss" );

		PlacesClient placesClient = Places.createClient( getActivity() );

		AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById( R.id.autocomplete_fragment );

		autocompleteFragment.setPlaceFields( Arrays.asList( Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG ) );
		// Set up a PlaceSelectionListener to handle the response.
		autocompleteFragment.setOnPlaceSelectedListener( new PlaceSelectionListener() {

			@Override
			public void onPlaceSelected( @NotNull Place place ) {
				try {
					map.animateCamera( CameraUpdateFactory.newLatLngZoom( place.getLatLng(), 14 ) );
					layerDecheteries.addRedMarker( place.getLatLng() );
				} catch ( Exception ex ) {
					ex.printStackTrace();
				}
			}

			@Override
			public void onError( @NotNull Status status ) {
				Log.e( "ERROR", "An error occurred: " + status );
			}
		} );

		return view;
	}

	private void databaseSetUp() {
		try {
			final InputStream is = getActivity().getAssets().open( "dechetterie.kml" );
			StringBuilder textBuilder = new StringBuilder();

			Reader reader = new BufferedReader( new InputStreamReader( is, Charset.forName( StandardCharsets.UTF_8.name() ) ) );
			int c = 0;
			while( ( c = reader.read() ) != -1 ) {
				textBuilder.append( (char) c );
			}

			String kmlToParse = textBuilder.toString();
			kmlToParse = kmlToParse.replace( "<kml xmlns=\"http://www.opengis.net/kml/2.2\">", "" );
			kmlToParse = kmlToParse.replace( "<Document><name>Decheteries</name><description/>", "" );
			kmlToParse = kmlToParse.replace( "<Folder>", "" );
			kmlToParse = kmlToParse.replace( "<name>decheterie.kml</name>", "" );
			kmlToParse = kmlToParse.replace( "</Folder>", "" );
			kmlToParse = kmlToParse.replace( "</Document>", "" );
			kmlToParse = kmlToParse.replace( "</kml>", "" );

			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature( XmlPullParser.FEATURE_PROCESS_NAMESPACES, false );
			parser.setInput( new StringReader( kmlToParse ) );
			parser.nextTag();

			List<CRMPhoneDecheterieDTO> liste = readKml( parser );

			dbHelper.insertBaseDecheteries( liste );
		} catch ( Exception ex ) {
			ex.printStackTrace();
			Log.e( "BUG", "Erreur lors de la lecture du fichier des decheteries" );
		}
	}

	private List<CRMPhoneDecheterieDTO> readKml( XmlPullParser parser ) throws IOException, XmlPullParserException {
		List<CRMPhoneDecheterieDTO> entries = new ArrayList<>();
		int eventType = parser.getEventType();
		while( eventType != XmlPullParser.END_DOCUMENT ) {
			if ( eventType == XmlPullParser.START_TAG ) {
				if ( parser.getName().equals( "Placemark" ) ) {

					HashMap<String, Object> properties = new HashMap<String, Object>();
					KmlPointArtemis geometry = null;
					int eventType2 = parser.getEventType();

					while( ! ( eventType2 == END_TAG && parser.getName().equals( "Placemark" ) ) ) {
						if ( eventType2 == START_TAG ) {
							if ( parser.getName().matches( PROPERTY_REGEX ) ) {
								properties.put( parser.getName(), parser.nextText() );
							} else if ( parser.getName().matches( GEOMETRY_REGEX ) ) {
								geometry = createGeometry( parser, parser.getName() );
							} else if ( parser.getName().matches( EXTENDED_DATA ) ) {
								properties.putAll( setTarifsProperties( parser ) );
							}
						}
						eventType2 = parser.next();
					}
					CRMPhoneDecheterieDTO decheterie = new CRMPhoneDecheterieDTO();
					decheterie.setId( Integer.parseInt( (String) properties.get( "id" ) ) );
					decheterie.setNom( (String) properties.get( "name" ) );
					decheterie.setDescription( (String) properties.get( "description" ) );
					decheterie.setHoraires( (String) properties.get( "horaires" ) );
					decheterie.setProOuPartic( (String) properties.get( "type" ) );
					decheterie.setBarriere( (String) properties.get( "barriere" ) );
					decheterie.setCompteOuvert( Boolean.getBoolean( (String) properties.get( "compteOuvert" ) ) );
					decheterie.setCarte( (String) properties.get( "carte" ) );
					decheterie.setBlacklist( Boolean.getBoolean( (String) properties.get( "blacklist" ) ) );
					decheterie.setLatitude( String.valueOf( geometry.getGeometryObject().latitude ) );
					decheterie.setLongitude( String.valueOf( geometry.getGeometryObject().longitude ) );
					if ( null != properties.get( "tarifs" ) ) {
						decheterie.setTarifs( (List<CRMPhoneDecheteriesTarifsDTO>) properties.get( "tarifs" ) );
					}

					entries.add( decheterie );
				}
			}
			eventType = parser.next();

		}
		return entries;
	}

	private static HashMap<String, List<CRMPhoneDecheteriesTarifsDTO>> setTarifsProperties( XmlPullParser parser ) throws XmlPullParserException, IOException {
		List<CRMPhoneDecheteriesTarifsDTO> listeTarifs = new ArrayList<>();
		CRMPhoneDecheteriesTarifsDTO tarif = null;

		int eventType = parser.getEventType();
		while( ! ( eventType == END_TAG && parser.getName().matches( EXTENDED_DATA ) ) ) {
			if ( eventType == START_TAG ) {
				if ( parser.getName().equals( "id" ) ) {
					tarif = new CRMPhoneDecheteriesTarifsDTO();
					tarif.setId( Integer.parseInt( parser.nextText() ) );
				} else if ( parser.getName().equals( "fkDecheterie" ) ) {
					tarif.setFkDecheterie( Integer.valueOf( parser.nextText() ) );
				} else if ( parser.getName().equals( "matiere" ) ) {
					String matiere = parser.nextText();
					if ( null != matiere && !"null".equals( matiere ) ) {
						tarif.setMatiere( matiere );
					} else {
						tarif.setMatiere( "" );
					}
				} else if ( parser.getName().equals( "prix" ) ) {
					String prixTonne = parser.nextText();
					if ( null != prixTonne && !"null".equals( prixTonne ) ) {
						tarif.setPrixTonne( new BigDecimal( prixTonne ) );
					} else {
						tarif.setPrixTonne( null );
					}
					listeTarifs.add( tarif );
				}
			}
			eventType = parser.next();
		}

		HashMap<String, List<CRMPhoneDecheteriesTarifsDTO>> properties = new HashMap<String, List<CRMPhoneDecheteriesTarifsDTO>>();
		properties.put( "tarifs", listeTarifs );

		return properties;
	}

	/**
	 * Creates a new Geometry object (Created if "Point", "LineString", "Polygon" or
	 * "MultiGeometry" tag is detected by the XmlPullParser)
	 *
	 * @param geometryType
	 *            Type of geometry object to create
	 */
	private static KmlPointArtemis createGeometry( XmlPullParser parser, String geometryType ) throws IOException, XmlPullParserException {
		int eventType = parser.getEventType();
		while( ! ( eventType == END_TAG && parser.getName().equals( geometryType ) ) ) {
			if ( eventType == START_TAG ) {
				if ( parser.getName().equals( "Point" ) ) {
					return createPoint( parser );
				}
			}
			eventType = parser.next();
		}
		return null;
	}

	/**
	 * Creates a new KmlPoint object
	 *
	 * @return KmlPoint object
	 */
	private static KmlPointArtemis createPoint( XmlPullParser parser ) throws XmlPullParserException, IOException {
		KmlFeatureParserArtemis.LatLngAlt latLngAlt = null;
		int eventType = parser.getEventType();
		while( ! ( eventType == END_TAG && parser.getName().equals( "Point" ) ) ) {
			if ( eventType == START_TAG && parser.getName().equals( "coordinates" ) ) {
				latLngAlt = convertToLatLngAlt( parser.nextText() );
			}
			eventType = parser.next();
		}
		return new KmlPointArtemis( latLngAlt.latLng, latLngAlt.altitude );
	}

	/**
	 * Convert a string coordinate from a string into a LatLngAlt object
	 *
	 * @param coordinateString
	 *            coordinate string to convert from
	 * @return LatLngAlt object created from given coordinate string
	 */
	private static KmlFeatureParserArtemis.LatLngAlt convertToLatLngAlt( String coordinateString ) {
		return convertToLatLngAlt( coordinateString, LAT_LNG_ALT_SEPARATOR );
	}

	/**
	 * Convert a string coordinate from a string into a LatLngAlt object
	 *
	 * @param coordinateString
	 *            coordinate string to convert from
	 * @param separator
	 *            separator to use when splitting coordinates
	 * @return LatLngAlt object created from given coordinate string
	 */
	private static KmlFeatureParserArtemis.LatLngAlt convertToLatLngAlt( String coordinateString, String separator ) {
		String[] coordinate = coordinateString.split( separator );
		Double lat = Double.parseDouble( coordinate[LATITUDE_INDEX] );
		Double lon = Double.parseDouble( coordinate[LONGITUDE_INDEX] );
		Double alt = ( coordinate.length > 2 ) ? Double.parseDouble( coordinate[ALTITUDE_INDEX] ) : null;
		LatLng latLng = new LatLng( lat, lon );
		return new KmlFeatureParserArtemis.LatLngAlt( latLng, alt );
	}

	@Override
	public void onMapReady( GoogleMap googleMap ) {
		assert null != getContext() && null != getActivity();

		try {

			if ( getActivity().checkSelfPermission( Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED
					&& getActivity().checkSelfPermission( Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
				return;
			}

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

			map.setOnMarkerClickListener( marker -> {
				if ( null != markerCreated ) {
					updateBlocInfos( markerCreated );
				} else {
					updateBlocInfos( marker );
				}
				marker.setDraggable( true );
				return false;
			} );

			// Simple clic sur la carte en dehors des marqueurs = on déselectionne le
			// marqueur selectionné précédemment
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
				placemarkSelected = markerCreated;
			} );

			// Récupération des décheteries
			try {
				// Récupération du numero de version de la carte dans la mémoire du téléphone
				Object tmpObj = StorageUtil.readObjectInternal( getContext(), ConstantesMaps.KEY_VERSION_MAP_DECHET.getValue() );
				if ( null != tmpObj && Integer.parseInt( tmpObj.toString() ) >= Constantes.NUMERO_VERSION_DECHETERIES_IN_APK ) {
					versionStockee = Integer.parseInt( tmpObj.toString() );
				} else {
					versionStockee = Constantes.NUMERO_VERSION_DECHETERIES_IN_APK;
				}

				// Récupération du numéro de version de la carte sur le serveur
				// WsUtil.getVersionMapDecheterie( this, null );
				WsUtil.getDecheteriesUpdates( this, null, versionStockee );
			} catch ( Exception ex ) {
				ex.printStackTrace();
				FirebaseCrashlytics.getInstance().recordException( ex );
				Log.d( "TAG", "Erreur lors de la recuperation de la liste des decheteries." );
			}

			( (MainActivity) this.getActivity() ).removeWaitingScreen();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
	}

	private void setViewWithData() {
		try {
			layerDecheteries = new KmlLayerArtemis( map, streamDecheteries, getContext(), 0x5454f2 );
			layerDecheteries.addLayerToMap();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
	}

	// Enregistrement ou mise à jour d'une décheterie
	private void saveDecheterie() {

		assert null != getActivity();

		decheterie.setBarriere( spBarriere.getSelectedItem().toString() );
		decheterie.setBlacklist( "Oui".equals( spBlacklist.getSelectedItem().toString() ) );
		decheterie.setCarte( spCarte.getSelectedItem().toString() );
		decheterie.setDescription( etAdresse.getText().toString() );
		decheterie.setHoraires( etHoraires.getText().toString() );
		decheterie.setNom( etTitre.getText().toString() );
		decheterie.setProOuPartic( spType.getSelectedItem().toString() );
		decheterie.setLatitude( String.valueOf( ( (KmlPointArtemis) placemarkSelected.getGeometry() ).getGeometryObject().latitude ) );
		decheterie.setLongitude( String.valueOf( ( (KmlPointArtemis) placemarkSelected.getGeometry() ).getGeometryObject().longitude ) );

		if ( null != placemarkSelected.getProperty( "tarifs" ) ) {
			List<CRMPhoneDecheteriesTarifsDTO> tarifs = (List<CRMPhoneDecheteriesTarifsDTO>) placemarkSelected.getProperty( "tarifs" );
			decheterie.setTarifs( tarifs );
		}

		if ( null != placemarkSelected.getProperty( "compteOuvert" ) ) {
			String compteOuvert = (String) placemarkSelected.getProperty( "compteOuvert" );
			if ( compteOuvert.equals( "Oui" ) ) {
				decheterie.setCompteOuvert( true );
			} else if ( compteOuvert.equals( "Non" ) ) {
				decheterie.setCompteOuvert( false );
			} else {
				decheterie.setCompteOuvert( null );
			}
		}

		try {
			WsUtil.updateDecheterie( this, null, decheterie );
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
	}

	/**
	 * Suppression d'un marker venant d'être créé (ex: Demande de création puis
	 * affichage d'une autre decheterie = on supprime le marqueur qui n'a pas été
	 * enregistré)
	 */
	private void deleteMarkerCreated() {
		if ( null != markerCreated ) {
			layerDecheteries.deleteMarker( markerCreated );
		}
		markerCreated = null;
	}

	/**
	 * Création d'un nouveau marker
	 *
	 * @param latLng
	 *            La position GPS du marker à créer
	 */
	private void createMarker( LatLng latLng ) {
		try {
			btUpdate.setEnabled( true );
			btUpdate.setText( R.string.enregistrer );

			markerCreated = layerDecheteries.addMarker( latLng );
			decheterie.setId( null );

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
		spType.setSelection( 0 );
		spCarte.setSelection( 0 );
		spBarriere.setSelection( 0 );
		spBlacklist.setSelection( 0 );

		hideTarifs();

		dechetScrollView.setBackgroundColor( Color.TRANSPARENT );
	}

	/**
	 * Mise à jour du bloc d'information de la decheterie
	 *
	 * @param marker
	 *            Le marker selectionné par l'utilisateur
	 */
	private void updateBlocInfos( Marker marker ) {
		KmlPlacemarkArtemis placemark = (KmlPlacemarkArtemis) layerDecheteries.getContainerFeature( marker );
		if ( null == placemark ) {
			placemark = (KmlPlacemarkArtemis) layerDecheteries.getFeature( marker );
		}
		this.updateBlocInfos( placemark );
	}

	/**
	 * Mise à jour du bloc d'information
	 *
	 * @param kmlPlacemark
	 *            Le marqueur sur lequel l'utilisateur a appuyé
	 */
	private void updateBlocInfos( KmlPlacemarkArtemis kmlPlacemark ) {

		this.placemarkSelected = kmlPlacemark;

		btUpdate.setEnabled( true );

		if ( null != kmlPlacemark ) {

			showOrHideTarifs();

			btUpdate.setText( R.string.enregistrer );

			if ( null != kmlPlacemark.getProperty( "id" ) ) {
				decheterie.setId( Integer.parseInt( String.valueOf( kmlPlacemark.getProperty( "id" ) ) ) );
			}
			String latitude = String.valueOf( ( (LatLng) kmlPlacemark.getGeometry().getGeometryObject() ).latitude );
			String longitude = String.valueOf( ( (LatLng) kmlPlacemark.getGeometry().getGeometryObject() ).longitude );
			decheterie.setLatitude( latitude );
			decheterie.setLongitude( longitude );

			String type = String.valueOf( kmlPlacemark.getProperty( "type" ) );
			String barriere = String.valueOf( kmlPlacemark.getProperty( "barriere" ) );
			String carte = String.valueOf( kmlPlacemark.getProperty( "carte" ) );
			String blacklist = String.valueOf( kmlPlacemark.getProperty( "blacklist" ) );

			// Suivant le type de décheterie (pro ou partic), on affiche ou non un bloc
			// contenant les tarifs
			spType.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected( AdapterView<?> adapterView, View view, int i, long l ) {
					showOrHideTarifs();
				}

				@Override
				public void onNothingSelected( AdapterView<?> adapterView ) {
					showOrHideTarifs();
				}
			} );

			if ( type.equals( "null" ) ) {
				spType.setSelection( 0 );
			} else {
				for ( int pointeur = 0 ; pointeur < spType.getCount() ; pointeur++ ) {
					if ( type.equals( spType.getItemAtPosition( pointeur ) ) ) {
						spType.setSelection( pointeur );
					}
				}
			}

			if ( barriere.equals( "null" ) ) {
				spBarriere.setSelection( 2 );
			} else {
				for ( int pointeur = 0 ; pointeur < spBarriere.getCount() ; pointeur++ ) {
					if ( barriere.equals( spBarriere.getItemAtPosition( pointeur ) ) ) {
						spBarriere.setSelection( pointeur );
					}
				}
			}

			if ( carte.equals( "null" ) ) {
				spCarte.setSelection( 2 );
			} else {
				for ( int pointeur = 0 ; pointeur < spCarte.getCount() ; pointeur++ ) {
					if ( carte.equals( spCarte.getItemAtPosition( pointeur ) ) ) {
						spCarte.setSelection( pointeur );
					}
				}
			}

			if ( blacklist.equals( "true" ) ) {
				dechetScrollView.setBackgroundColor( Color.RED );
				blacklist = "Oui";
			} else if ( blacklist.equals( "false" ) || blacklist.equals( "null" ) ) {
				dechetScrollView.setBackgroundColor( Color.TRANSPARENT );
				blacklist = "Non";
			}

			for ( int pointeur = 0 ; pointeur < spBlacklist.getCount() ; pointeur++ ) {
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

	/**
	 * Affiche ou cache le bloc des tarifs de decheterie suivant la decheterie
	 * selectionnée (pro ou partic)
	 */
	private void showOrHideTarifs() {
		if ( null != placemarkSelected ) {
			if ( spType.getSelectedItem().equals( "PRO" ) ) {
				showTarifs();
			} else {
				hideTarifs();
			}
		}
	}

	/**
	 * Masquage du bloc de tarifs pour les decheteries de type particuliers
	 */
	private void hideTarifs() {
		blocPro.removeAllViews();
	}

	/**
	 * Affichage du bloc des tarifs de la décheterie sélectionnée
	 */
	private void showTarifs() {
		assert null != getActivity();

		Integer idLastEt = null;
		Integer idLastBt = null;
		final int lbTarifId = View.generateViewId();
		final int lbCompteOuvertId = View.generateViewId();
		final int spCompteOuvertId = View.generateViewId();
		final Button btAddTarif = new Button( getContext() );
		blocPro.removeAllViewsInLayout();

		// Affichage de l'état de l'ouverture de compte
		TextView tvCompteOuvert = new TextView( getContext() );
		RelativeLayout.LayoutParams paramC = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
		tvCompteOuvert.setId( lbCompteOuvertId );
		tvCompteOuvert.setMinWidth( lbHoraires.getMinimumWidth() );
		tvCompteOuvert.setGravity( Gravity.START );
		tvCompteOuvert.setLayoutParams( paramC );
		tvCompteOuvert.setTextSize( 14 );
		tvCompteOuvert.setText( R.string.compte_2p );
		blocPro.addView( tvCompteOuvert, paramC );

		Spinner spCompteOuvert = new Spinner( getContext() );
		ArrayAdapter<String> adapterBarriere = new ArrayAdapter<>( getActivity(), R.layout.spinner_textview, getResources().getStringArray( R.array.choix_compte ) );
		adapterBarriere.setDropDownViewResource( R.layout.spinner_textview );
		spCompteOuvert.setId( spCompteOuvertId );
		spCompteOuvert.setAdapter( adapterBarriere );
		RelativeLayout.LayoutParams paramSpinner = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
		paramSpinner.addRule( RelativeLayout.BELOW, lbHoraires.getId() );
		paramSpinner.addRule( RelativeLayout.END_OF, lbCompteOuvertId );
		blocPro.addView( spCompteOuvert, paramSpinner );

		if ( null != this.placemarkSelected && null != placemarkSelected.getProperty( "compteOuvert" ) ) {
			for ( int pointeur = 0 ; pointeur < spCompteOuvert.getCount() ; pointeur++ ) {
				if ( placemarkSelected.getProperty( "compteOuvert" ).equals( spCompteOuvert.getItemAtPosition( pointeur ) ) ) {
					spCompteOuvert.setSelection( pointeur );
				}
			}
		}

		// Affichage d'un label 'Tarifs'
		TextView tvTarifs = new TextView( getContext() );
		RelativeLayout.LayoutParams paramT = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
		paramT.addRule( RelativeLayout.BELOW, lbCompteOuvertId );
		tvTarifs.setId( lbTarifId );
		tvTarifs.setMinWidth( lbHoraires.getMinimumWidth() );
		tvTarifs.setGravity( Gravity.START );
		tvTarifs.setLayoutParams( paramT );
		tvTarifs.setTextSize( 14 );
		tvTarifs.setText( R.string.tarifs_2p );
		blocPro.addView( tvTarifs, paramT );

		// Si la décheterie contient des tarifs, on les affiche
		if ( null != placemarkSelected.getProperty( "tarifs" ) ) {
			final List<CRMPhoneDecheteriesTarifsDTO> listeTarifs = (List<CRMPhoneDecheteriesTarifsDTO>) placemarkSelected.getProperty( "tarifs" );

			if ( !listeTarifs.isEmpty() ) {

				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT );

				Button btDeleteLine;

				int newIdLb;
				Integer newIdEt;
				Integer newIdBt;

				// Parcours des tarifs
				for ( CRMPhoneDecheteriesTarifsDTO tarif : listeTarifs ) {

					newIdLb = View.generateViewId();
					newIdEt = View.generateViewId();
					newIdBt = View.generateViewId();

					String matiere = tarif.getMatiere();
					String value = null != tarif.getPrixTonne() ? String.valueOf( tarif.getPrixTonne() ) : "";

					final EmbeddingEditText<CRMPhoneDecheteriesTarifsDTO> lbMatiere = new EmbeddingEditText<>( getActivity(), Constantes.LABEL_MATIERE, tarif );
					lbMatiere.setId( newIdLb );
					lbMatiere.setMinWidth( lbHoraires.getMinimumWidth() + 50 );
					lbMatiere.setGravity( Gravity.START );
					lbMatiere.setPadding( lbMatiere.getPaddingLeft(), 0, lbMatiere.getPaddingRight(), lbMatiere.getPaddingBottom() );
					lbMatiere.setLayoutParams( param );
					lbMatiere.setTextSize( 14 );
					lbMatiere.setText( matiere );

					RelativeLayout.LayoutParams paramsLb = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
					if ( null != idLastBt ) {
						paramsLb.addRule( RelativeLayout.BELOW, idLastBt );
						paramsLb.addRule( RelativeLayout.START_OF, idLastEt );
					} else {
						paramsLb.addRule( RelativeLayout.END_OF, tvTarifs.getId() );
						paramsLb.addRule( RelativeLayout.BELOW, lbCompteOuvertId );
					}

					lbMatiere.setLayoutParams( paramsLb );

					blocPro.addView( lbMatiere, paramsLb );

					final EmbeddingEditText<CRMPhoneDecheteriesTarifsDTO> etPrix = new EmbeddingEditText<>( getContext(), Constantes.LABEL_PRIX, tarif );
					etPrix.setId( newIdEt );
					etPrix.setTextSize( 14 );
					etPrix.setText( value );
					etPrix.setMinWidth( lbHoraires.getMinimumWidth() );
					etPrix.setPadding( etPrix.getPaddingLeft(), 0, etPrix.getPaddingRight(), etPrix.getPaddingBottom() );

					RelativeLayout.LayoutParams paramsEt = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
					if ( null != idLastBt ) {
						paramsEt.addRule( RelativeLayout.BELOW, idLastBt );
						paramsEt.addRule( RelativeLayout.END_OF, newIdLb );
					} else {
						paramsEt.addRule( RelativeLayout.END_OF, lbMatiere.getId() );
						paramsEt.addRule( RelativeLayout.BELOW, lbCompteOuvertId );
					}
					etPrix.setLayoutParams( paramsEt );

					blocPro.addView( etPrix, paramsEt );

					btDeleteLine = new Button( getContext() );
					btDeleteLine.setPadding( 0, 0, 0, 0 );
					btDeleteLine.setId( newIdBt );
					btDeleteLine.setText( "-" );

					RelativeLayout.LayoutParams paramsBt = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
					if ( null != idLastBt ) {
						paramsBt.addRule( RelativeLayout.BELOW, idLastBt );
						paramsBt.addRule( RelativeLayout.END_OF, etPrix.getId() );
					} else {
						paramsBt.addRule( RelativeLayout.END_OF, etPrix.getId() );
						paramsBt.addRule( RelativeLayout.BELOW, lbCompteOuvertId );
					}

					btDeleteLine.setLayoutParams( paramsBt );

					blocPro.addView( btDeleteLine, paramsBt );

					// Si clic sur 'Suppression du tarif', on supprime la ligne et on recharge
					// l'affichage du bloc des tarifs
					btDeleteLine.setOnClickListener( v -> {
						( (List<CRMPhoneDecheteriesTarifsDTO>) placemarkSelected.getProperty( "tarifs" ) ).remove( lbMatiere.getAttribute() );
						showTarifs();
					} );

					idLastEt = newIdEt;

					idLastBt = newIdBt;

					lbMatiere.addTextChangedListener( new TextWatcher() {

						@Override
						public void beforeTextChanged( CharSequence charSequence, int i, int i1, int i2 ) {}

						@Override
						public void onTextChanged( CharSequence charSequence, int i, int i1, int i2 ) {}

						@Override
						public void afterTextChanged( Editable editable ) {
							lbMatiere.getAttribute().setMatiere( editable.toString() );
						}
					} );

					etPrix.addTextChangedListener( new TextWatcher() {

						@Override
						public void beforeTextChanged( CharSequence charSequence, int i, int i1, int i2 ) {}

						@Override
						public void onTextChanged( CharSequence charSequence, int i, int i1, int i2 ) {}

						@Override
						public void afterTextChanged( Editable editable ) {
							Pattern mPattern = Pattern.compile( "[0-9]{0," + ( 5 - 1 ) + "}+((\\.[0-9]{0," + ( 2 - 1 ) + "})?)||(\\.)?" );
							Matcher matcher = mPattern.matcher( editable.toString() );
							if ( matcher.matches() ) {
								etPrix.getAttribute().setPrixTonne( new BigDecimal( editable.toString() ) );
							}
						}
					} );

				}
			}
		}

		final Integer finalLastEtId = idLastEt;
		final Integer finalLastBtId = idLastBt;
		btAddTarif.setId( View.generateViewId() );
		btAddTarif.setOnClickListener( view -> addLineTarif( (Button) view, finalLastEtId, finalLastBtId, lbTarifId ) );
		RelativeLayout.LayoutParams paramsBt = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
		if ( null != finalLastEtId ) {
			paramsBt.addRule( RelativeLayout.BELOW, finalLastBtId );
		} else {
			paramsBt.addRule( RelativeLayout.BELOW, spCompteOuvertId );
		}
		paramsBt.addRule( RelativeLayout.END_OF, lbTarifId );
		btAddTarif.setText( "+" );
		blocPro.addView( btAddTarif, paramsBt );
	}

	/**
	 * Ajout d'une ligne de tarif de decheterie
	 *
	 * @param button
	 *            Le bouton d'ajout (qui sera à déplacer en dessous une fois la
	 *            nouvelle ligne insérée)
	 * @param idLastEditText
	 *            L'identifiant du dernier composant de prix permettant
	 *            de savoir où placer le composant prix de la ligne suivante
	 * @param idLastButtonDelete
	 *            L'identifiant du dernier composant de matière
	 *            permettant de savoir où placer le composant de matiere de la ligne
	 *            suivante
	 * @param idTvTarifs
	 *            L'identifiant du label des tarifs dans le cas où aucune
	 *            ligne de tarif ne soit déjà présente
	 */
	private void addLineTarif( Button button, Integer idLastEditText, Integer idLastButtonDelete, final Integer idTvTarifs ) {
		int newIdLb = View.generateViewId();

		CRMPhoneDecheteriesTarifsDTO tarif = new CRMPhoneDecheteriesTarifsDTO();
		if ( null == placemarkSelected.getProperty( "tarifs" ) ) {
			placemarkSelected.setProperty( "tarifs", new ArrayList<CRMPhoneDecheteriesTarifsDTO>() );
		}
		( (List<CRMPhoneDecheteriesTarifsDTO>) placemarkSelected.getProperty( "tarifs" ) ).add( tarif );

		final EmbeddingEditText<CRMPhoneDecheteriesTarifsDTO> lbMatiere = new EmbeddingEditText<>( getContext(), Constantes.LABEL_MATIERE, tarif );
		lbMatiere.setId( newIdLb );
		lbMatiere.setMinWidth( lbHoraires.getMinimumWidth() + 50 );
		lbMatiere.setGravity( Gravity.START );
		lbMatiere.setPadding( lbMatiere.getPaddingLeft(), 0, lbMatiere.getPaddingRight(), lbMatiere.getPaddingBottom() );
		lbMatiere.setTextSize( 14 );

		RelativeLayout.LayoutParams paramsLb = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
		if ( null != idLastButtonDelete ) {
			paramsLb.addRule( RelativeLayout.BELOW, idLastButtonDelete );
			paramsLb.addRule( RelativeLayout.START_OF, idLastEditText );
		} else {
			paramsLb.addRule( RelativeLayout.END_OF, idTvTarifs );
		}

		lbMatiere.setLayoutParams( paramsLb );

		blocPro.addView( lbMatiere );

		final int newIdEt = View.generateViewId();

		final EmbeddingEditText<CRMPhoneDecheteriesTarifsDTO> etPrix = new EmbeddingEditText<>( getContext(), Constantes.LABEL_PRIX, tarif );
		etPrix.setId( newIdEt );
		etPrix.setTextSize( 14 );
		etPrix.setMinWidth( lbHoraires.getMinimumWidth() );
		etPrix.setPadding( etPrix.getPaddingLeft(), 0, etPrix.getPaddingRight(), etPrix.getPaddingBottom() );

		RelativeLayout.LayoutParams paramsEt = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
		if ( null != idLastButtonDelete ) {
			paramsEt.addRule( RelativeLayout.BELOW, idLastButtonDelete );
			paramsEt.addRule( RelativeLayout.END_OF, newIdLb );
		} else {
			paramsEt.addRule( RelativeLayout.END_OF, lbMatiere.getId() );
		}

		etPrix.setLayoutParams( paramsEt );

		blocPro.addView( etPrix, paramsEt );

		lbMatiere.addTextChangedListener( new TextWatcher() {

			@Override
			public void beforeTextChanged( CharSequence charSequence, int i, int i1, int i2 ) {}

			@Override
			public void onTextChanged( CharSequence charSequence, int i, int i1, int i2 ) {}

			@Override
			public void afterTextChanged( Editable editable ) {
				lbMatiere.getAttribute().setMatiere( editable.toString() );
			}
		} );

		etPrix.addTextChangedListener( new TextWatcher() {

			@Override
			public void beforeTextChanged( CharSequence charSequence, int i, int i1, int i2 ) {}

			@Override
			public void onTextChanged( CharSequence charSequence, int i, int i1, int i2 ) {}

			@Override
			public void afterTextChanged( Editable editable ) {
				Pattern mPattern = Pattern.compile( "[0-9]{0," + ( 5 - 1 ) + "}+((\\.[0-9]{0," + ( 2 - 1 ) + "})?)||(\\.)?" );
				Matcher matcher = mPattern.matcher( editable.toString() );
				if ( matcher.matches() ) {
					etPrix.getAttribute().setPrixTonne( new BigDecimal( editable.toString() ) );
				}
			}
		} );

		final int newIdBt = View.generateViewId();

		Button btDelete = new Button( getContext() );
		btDelete.setPadding( 0, 0, 0, 0 );
		btDelete.setText( "-" );
		btDelete.setId( newIdBt );

		RelativeLayout.LayoutParams paramsBtDelete = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
		if ( null != idLastButtonDelete ) {
			paramsBtDelete.addRule( RelativeLayout.BELOW, idLastButtonDelete );
			paramsBtDelete.addRule( RelativeLayout.END_OF, etPrix.getId() );
		} else {
			paramsBtDelete.addRule( RelativeLayout.END_OF, etPrix.getId() );
		}

		btDelete.setLayoutParams( paramsBtDelete );

		btDelete.setOnClickListener( v -> {
			( (List<CRMPhoneDecheteriesTarifsDTO>) placemarkSelected.getProperty( "tarifs" ) ).remove( lbMatiere.getAttribute() );
			showTarifs();
		} );

		blocPro.addView( btDelete, paramsBtDelete );

		// Mise à jour des parametres du bouton d'ajout
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) button.getLayoutParams();
		params.addRule( RelativeLayout.BELOW, newIdLb );

		button.setOnClickListener( view -> addLineTarif( (Button) view, newIdEt, newIdBt, idTvTarifs ) );
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
			// case GET_VERSION_MAP_DECHETERIE: {
			// // Récupération du numéro de version de la carte sur le serveur
			// Integer version = (Integer) response;
			// try {
			// // Si la version a évolué, on recharge la carte et on la stocke dans le
			// // téléphone
			// String kmlString;
			// if ( version == Constantes.NUMERO_VERSION_DECHETERIES_IN_APK ) {
			// versionStockee = Constantes.NUMERO_VERSION_DECHETERIES_IN_APK;
			// final InputStream is = getActivity().getAssets().open( "dechetterie.kml" );
			// StringBuilder textBuilder = new StringBuilder();
			// try ( Reader reader = new BufferedReader( new InputStreamReader( is,
			// Charset.forName( StandardCharsets.UTF_8.name() ) ) ) ) {
			// int c = 0;
			// while ( ( c = reader.read() ) != -1 ) {
			// textBuilder.append( (char) c );
			// }
			// }
			//
			// kmlString = textBuilder.toString();
			//
			// streamDecheteries = new ByteArrayInputStream( kmlString.getBytes() );
			//
			// ( (MainActivity) getActivity() ).removeWaitingScreen();
			//
			// } else if ( versionStockee < version ) {
			// Map<String, Object> mapRessources = new HashMap<>();
			// mapRessources.put( "version", version );
			//
			// ( (MainActivity) getActivity() ).showWaitingScreen();
			//
			// WsUtil.getDecheteriesUpdates( this, mapRessources, versionStockee );
			//
			// } else {
			//
			// ( (MainActivity) getActivity() ).showWaitingScreen();
			// kmlString = (String) StorageUtil.readObjectInternal( getContext(),
			// ConstantesMaps.KEY_DECHET_STORAGE.getValue() );
			//
			// assert null != kmlString;
			//
			// streamDecheteries = new ByteArrayInputStream( kmlString.getBytes() );
			//
			// ( (MainActivity) getActivity() ).removeWaitingScreen();
			// }
			// } catch( IOException | ClassNotFoundException e ) {
			// e.printStackTrace();
			// }
			//
			// setViewWithData();
			//
			// break;
			// }

			// case GET_KML_DECHETERIE: {
			// try {
			// String kmlString = (String) response;
			//
			// streamDecheteries = new ByteArrayInputStream( kmlString.getBytes() );
			//
			// StorageUtil.writeObjectInternal( getContext(),
			// ConstantesMaps.KEY_DECHET_STORAGE.getValue(), kmlString );
			// StorageUtil.writeObjectInternal( getContext(),
			// ConstantesMaps.KEY_VERSION_MAP_DECHET.getValue(), callbackResources.get(
			// "version" ) );
			//
			// setViewWithData();
			//
			// } catch( Exception e ) {
			// e.printStackTrace();
			// }
			// break;
			// }

			case GET_DECHETERIES_UPDATES :
				if ( null != response ) {
					try {
						List<CRMPhoneDecheteriesUpdatesDTO> listeUpdates = (List<CRMPhoneDecheteriesUpdatesDTO>) response;
						Collections.sort( listeUpdates, ( u1, u2 ) -> u1.getVersion().compareTo( u2.getVersion() ) );

						Integer lastVersion = listeUpdates.get( listeUpdates.size() - 1 ).getVersion();

						dbHelper.update( listeUpdates );

						StorageUtil.writeObjectInternal( getContext(), ConstantesMaps.KEY_VERSION_MAP_DECHET.getValue(), lastVersion );

					} catch ( Exception ex ) {
						ex.printStackTrace();
					}
				}

				streamDecheteries = dbHelper.readDecheteriesFromDatabase();

				setViewWithData();

				break;

			case UPDATE_DECHETERIE :
				// Nothing to do
				break;

			default :
				throw new IllegalStateException( "Reception d'une reponse de webservice provenant de : " + wsName + ". Les données associées sont : " + response );
		}
	}

	@Override
	public Fragment getFragmentSource() {
		return this;
	}
}