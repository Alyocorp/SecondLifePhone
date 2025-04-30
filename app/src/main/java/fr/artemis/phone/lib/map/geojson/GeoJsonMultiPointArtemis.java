package fr.artemis.phone.lib.map.geojson;

import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.MultiGeometry;

import java.util.ArrayList;
import java.util.List;

public class GeoJsonMultiPointArtemis extends MultiGeometry {
	/**
	 * Creates a GeoJsonMultiPoint object
	 *
	 * @param geoJsonPoints list of GeoJsonPoints to store
	 */
	public GeoJsonMultiPointArtemis(List<GeoJsonPointArtemis> geoJsonPoints) {
		super(geoJsonPoints);
		setGeometryType("MultiPoint");
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
	 * Gets a list of GeoJsonPoints
	 *
	 * @return list of GeoJsonPoints
	 */
	public List<GeoJsonPointArtemis> getPoints() {
		//convert list of Geometry types to list of GeoJsonPoint types
		List<Geometry> geometryList = getGeometryObject();
		ArrayList<GeoJsonPointArtemis> geoJsonPoints = new ArrayList<GeoJsonPointArtemis>();
		for ( Geometry geometry : geometryList) {
			GeoJsonPointArtemis point = (GeoJsonPointArtemis ) geometry;
			geoJsonPoints.add(point);
		}
		return geoJsonPoints;
	}
}
