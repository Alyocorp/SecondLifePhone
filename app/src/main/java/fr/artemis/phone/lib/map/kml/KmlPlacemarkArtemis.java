package fr.artemis.phone.lib.map.kml;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.data.Geometry;

import java.util.HashMap;

import fr.artemis.phone.lib.map.FeatureArtemis;

public class KmlPlacemarkArtemis extends FeatureArtemis {

	private final String mStyle;

	private final KmlStyleArtemis mInlineStyle;

	/**
	 * Creates a new KmlPlacemark object
	 *
	 * @param geometry   geometry object to store
	 * @param style      style id to store
	 * @param properties properties hashmap to store
	 */
	public KmlPlacemarkArtemis( Geometry geometry, String style, KmlStyleArtemis inlineStyle, HashMap<String, Object> properties ) {
		super( geometry, style, properties );
		mStyle = style;
		mInlineStyle = inlineStyle;
	}

	/**
	 * Gets the style id associated with the basic_placemark
	 *
	 * @return style id
	 */
	public String getStyleId() {
		return super.getId();
	}

	/**
	 * Gets the inline style that was found
	 *
	 * @return InlineStyle or null if not found
	 */
	public KmlStyleArtemis getInlineStyle() {
		return mInlineStyle;
	}

	/**
	 * Gets a PolygonOption
	 *
	 * @return new PolygonOptions
	 */
	public PolygonOptions getPolygonOptions() {
		if ( mInlineStyle == null ) {
			return null;
		}
		return mInlineStyle.getPolygonOptions();
	}

	/**
	 * Gets a MarkerOption
	 *
	 * @return A new MarkerOption
	 */
	public MarkerOptions getMarkerOptions() {
		if ( mInlineStyle == null ) {
			return null;
		}
		return mInlineStyle.getMarkerOptions();
	}

	/**
	 * Gets a PolylineOption
	 *
	 * @return new PolylineOptions
	 */
	public PolylineOptions getPolylineOptions() {
		if ( mInlineStyle == null ) {
			return null;
		}
		return mInlineStyle.getPolylineOptions();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder( "Placemark" ).append( "{" );
		sb.append( "\n style id=" ).append( mStyle );
		sb.append( ",\n inline style=" ).append( mInlineStyle );
		sb.append( "\n}\n" );
		return sb.toString();
	}
}