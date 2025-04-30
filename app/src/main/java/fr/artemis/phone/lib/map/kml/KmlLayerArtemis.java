package fr.artemis.phone.lib.map.kml;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

import fr.artemis.phone.lib.map.LayerArtemis;

public class KmlLayerArtemis extends LayerArtemis {

	private final KmlRendererArtemis mRenderer;
	private float mCustomColorMarker;

	/**
	 * Creates a new KmlLayer object - addLayerToMap() must be called to trigger rendering onto a map.
	 *
	 * @param map        GoogleMap object
	 * @param resourceId Raw resource KML file
	 * @param context    Context object
	 * @throws XmlPullParserException if file cannot be parsed
	 */
	public KmlLayerArtemis( GoogleMap map, int resourceId, Context context, float customColorMarker ) throws XmlPullParserException, IOException {
		this( map, context.getResources().openRawResource( resourceId ), context, customColorMarker );
	}

	/**
	 * Creates a new KmlLayer object
	 *
	 * @param map    GoogleMap object
	 * @param stream InputStream containing KML file
	 * @throws XmlPullParserException if file cannot be parsed
	 */
	public KmlLayerArtemis( GoogleMap map, InputStream stream, Context context, float customColorMarker ) throws XmlPullParserException, IOException {
		if ( stream == null ) {
			throw new IllegalArgumentException( "KML InputStream cannot be null" );
		}
		this.mCustomColorMarker = customColorMarker;
		mRenderer = new KmlRendererArtemis( map, context );
		XmlPullParser xmlPullParser = createXmlParser( stream );
		KmlParserArtemis parser = new KmlParserArtemis( xmlPullParser, customColorMarker );
		parser.parseKml();
		stream.close();

		mRenderer.storeKmlData( parser.getStyles(), parser.getStyleMaps(), parser.getPlacemarks(), parser.getContainers(), parser.getGroundOverlays() );
		storeRenderer( mRenderer );
	}

	public KmlPlacemarkArtemis addMarker( LatLng position ) throws IOException, XmlPullParserException {
		KmlPlacemarkArtemis placemark = KmlFeatureParserArtemis.createPlacemark( position, mCustomColorMarker );

		placemark.getMarkerOptions().draggable( true );
		mRenderer.addFeature( placemark );
		return placemark;
	}

	public KmlPlacemarkArtemis addRedMarker( LatLng position ) throws IOException, XmlPullParserException {
		KmlPlacemarkArtemis placemark = KmlFeatureParserArtemis.createPlacemark( position, 0xb02e2e );

		mRenderer.addFeature( placemark );
		return placemark;
	}

	public boolean deleteMarker( KmlPlacemarkArtemis markerToDelete ) {
		mRenderer.removeFeature( markerToDelete );
		return true;
	}

	public void addProperty( KmlPlacemarkArtemis placemark, String property, Object value ) {
		mRenderer.addProperty( placemark, property, value );
	}


	/**
	 * Creates a new XmlPullParser to allow for the KML file to be parsed
	 *
	 * @param stream InputStream containing KML file
	 * @return XmlPullParser containing the KML file
	 * @throws XmlPullParserException if KML file cannot be parsed
	 */
	private static XmlPullParser createXmlParser( InputStream stream ) throws XmlPullParserException {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware( true );
		XmlPullParser parser = factory.newPullParser();
		parser.setInput( stream, null );
		return parser;
	}

	/**
	 * Adds the KML data to the map
	 */
	@Override
	public void addLayerToMap() throws IOException, XmlPullParserException {
		super.addKMLToMap();
	}

	/**
	 * Checks if the layer contains placemarks
	 *
	 * @return true if there are placemarks, false otherwise
	 */
	public boolean hasPlacemarks() {
		return hasFeatures();
	}

	/**
	 * Gets an iterable of KmlPlacemark objects
	 *
	 * @return iterable of KmlPlacemark objects
	 */
	public Iterable<KmlPlacemarkArtemis> getPlacemarks() {
		return (Iterable<KmlPlacemarkArtemis>) getFeatures();
	}

	/**
	 * Checks if the layer contains any KmlContainers
	 *
	 * @return true if there is at least 1 container within the KmlLayer, false otherwise
	 */
	public boolean hasContainers() {
		return super.hasContainers();
	}

	/**
	 * Gets an iterable of KmlContainerInterface objects
	 *
	 * @return iterable of KmlContainerInterface objects
	 */
	public Iterable<KmlContainerArtemis> getContainers() {
		return super.getContainers();
	}

	/**
	 * Gets an iterable of KmlGroundOverlay objects
	 *
	 * @return iterable of KmlGroundOverlay objects
	 */
	public Iterable<KmlGroundOverlayArtemis> getGroundOverlays() {
		return super.getGroundOverlays();
	}
}