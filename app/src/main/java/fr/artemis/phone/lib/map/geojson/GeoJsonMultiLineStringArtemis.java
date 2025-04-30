package fr.artemis.phone.lib.map.geojson;

import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.MultiGeometry;

import java.util.ArrayList;
import java.util.List;

public class GeoJsonMultiLineStringArtemis extends MultiGeometry {

	/**
	 * Creates a new GeoJsonMultiLineString object
	 *
	 * @param geoJsonLineStrings list of GeoJsonLineStrings to store
	 */
	public GeoJsonMultiLineStringArtemis( List<GeoJsonLineStringArtemis> geoJsonLineStrings ) {
		super( geoJsonLineStrings );
		setGeometryType( "MultiLineString" );
	}

	/**
	 * Gets the type of geometry. The type of geometry conforms to the GeoJSON 'type'
	 * specification.
	 *
	 * @return type of geometry
	 */
	public String getType() {
		return getGeometryType();
	}

	/**
	 * Gets a list of GeoJsonLineStrings
	 *
	 * @return list of GeoJsonLineStrings
	 */
	public List<GeoJsonLineStringArtemis> getLineStrings() {
		// Convert list of Geometry types to list of GeoJsonLineString types
		List<Geometry> geometryList = getGeometryObject();
		ArrayList<GeoJsonLineStringArtemis> geoJsonLineStrings = new ArrayList<GeoJsonLineStringArtemis>();
		for( Geometry geometry : geometryList ) {
			GeoJsonLineStringArtemis lineString = ( GeoJsonLineStringArtemis ) geometry;
			geoJsonLineStrings.add( lineString );
		}

		return geoJsonLineStrings;
	}
}