package fr.artemis.phone.lib.map.kml;

import com.google.android.gms.maps.model.GroundOverlay;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class KmlParserArtemis {

	private final static String STYLE = "Style";

	private final static String STYLE_MAP = "StyleMap";

	private final static String PLACEMARK = "Placemark";

	private final static String GROUND_OVERLAY = "GroundOverlay";

	private final static String CONTAINER_REGEX = "Folder|Document";

	private final XmlPullParser mParser;

	private final HashMap<KmlPlacemarkArtemis, Object> mPlacemarks;

	private final ArrayList<KmlContainerArtemis> mContainers;

	private final HashMap<String, KmlStyleArtemis> mStyles;

	private final HashMap<String, String> mStyleMaps;

	private final HashMap<KmlGroundOverlayArtemis, GroundOverlay> mGroundOverlays;

	private final static String UNSUPPORTED_REGEX = "altitude|altitudeModeGroup|altitudeMode|" +
													"begin|bottomFov|cookie|displayName|displayMode|displayMode|end|expires|extrude|" +
													"flyToView|gridOrigin|httpQuery|leftFov|linkDescription|linkName|linkSnippet|" +
													"listItemType|maxSnippetLines|maxSessionLength|message|minAltitude|minFadeExtent|" +
													"minLodPixels|minRefreshPeriod|maxAltitude|maxFadeExtent|maxLodPixels|maxHeight|" +
													"maxWidth|near|NetworkLink|NetworkLinkControl|overlayXY|range|refreshMode|" +
													"refreshInterval|refreshVisibility|rightFov|roll|rotationXY|screenXY|shape|sourceHref|" +
													"state|targetHref|tessellate|tileSize|topFov|viewBoundScale|viewFormat|viewRefreshMode|" +
													"viewRefreshTime|when";

	private final float mCustomMarkerColor;
	/**
	 * Creates a new KmlParser object
	 *
	 * @param parser parser containing the KML file to parse
	 */
	/* package */ KmlParserArtemis( XmlPullParser parser, float customMarkerColor ) {
		mParser = parser;
		mCustomMarkerColor = customMarkerColor;
		mPlacemarks = new HashMap<>();
		mContainers = new ArrayList<>();
		mStyles = new HashMap<>();
		mStyleMaps = new HashMap<>();
		mGroundOverlays = new HashMap<>();
	}

	/**
	 * Parses the KML file and stores the created KmlStyle and KmlPlacemark
	 */
	/* package */ void parseKml() throws XmlPullParserException, IOException {
		int eventType = mParser.getEventType();
		while( eventType != XmlPullParser.END_DOCUMENT ) {
			if( eventType == XmlPullParser.START_TAG ) {
				if( mParser.getName().matches( UNSUPPORTED_REGEX ) ) {
					skip( mParser );
				}
				if( mParser.getName().matches( CONTAINER_REGEX ) ) {
					mContainers.add( KmlContainerParserArtemis.createContainer( mParser, mCustomMarkerColor ) );
				}
				if( mParser.getName().equals( STYLE ) ) {
					KmlStyleArtemis style = KmlStyleParserArtemis.createStyle( mParser, mCustomMarkerColor );
					mStyles.put( style.getStyleId(), style );
				}
				if( mParser.getName().equals( STYLE_MAP ) ) {
					mStyleMaps.putAll( KmlStyleParserArtemis.createStyleMap( mParser ) );
				}
				if( mParser.getName().equals( PLACEMARK ) ) {
					mPlacemarks.put( KmlFeatureParserArtemis.createPlacemark( mParser, mCustomMarkerColor ), null );
				}
				if( mParser.getName().equals( GROUND_OVERLAY ) ) {
					mGroundOverlays.put( KmlFeatureParserArtemis.createGroundOverlay( mParser ), null );
				}
			}
			eventType = mParser.next();

		}
		//Need to put an empty new style
		mStyles.put( null, new KmlStyleArtemis( mCustomMarkerColor) );
	}

	/**
	 * @return List of styles created by the parser
	 */
	/* package */ HashMap<String, KmlStyleArtemis> getStyles() {
		return mStyles;
	}

	/**
	 * @return A list of Kml Placemark objects
	 */
	/* package */ HashMap<KmlPlacemarkArtemis, Object> getPlacemarks() {
		return mPlacemarks;
	}

	/**
	 * @return A list of Kml Style Maps
	 */
	/* package */ HashMap<String, String> getStyleMaps() {
		return mStyleMaps;
	}

	/**
	 * @return A list of Kml Folders
	 */
	/* package */ ArrayList<KmlContainerArtemis> getContainers() {
		return mContainers;
	}

	/**
	 * @return A list of Ground Overlays
	 */
	/* package */ HashMap<KmlGroundOverlayArtemis, GroundOverlay> getGroundOverlays() {
		return mGroundOverlays;
	}

	/**
	 * Skips tags from START TAG to END TAG
	 *
	 * @param parser XmlPullParser
	 */
	/*package*/
	static void skip( XmlPullParser parser )
	throws XmlPullParserException, IOException {
		if( parser.getEventType() != XmlPullParser.START_TAG ) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while( depth != 0 ) {
			switch( parser.next() ) {
				case XmlPullParser.END_TAG:
					depth--;
					break;
				case XmlPullParser.START_TAG:
					depth++;
					break;
			}
		}
	}
}
