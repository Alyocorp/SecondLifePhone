package fr.artemis.phone.lib.map.geojson;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.data.Point;

public class GeoJsonPointArtemis extends Point {

	private final Double mAltitude;

	/**
	 * Creates a new GeoJsonPoint
	 *
	 * @param coordinates coordinates of GeoJsonPoint to store
	 */
	public GeoJsonPointArtemis( LatLng coordinates ) {
		this( coordinates, null );
	}

	/**
	 * Creates a new GeoJsonPoint
	 *
	 * @param coordinates coordinates of the KmlPointArtemis
	 * @param altitude    altitude of the KmlPointArtemis
	 */
	public GeoJsonPointArtemis( LatLng coordinates, Double altitude ) {
		super( coordinates );

		this.mAltitude = altitude;
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
	 * Gets the coordinates of the GeoJsonPoint
	 *
	 * @return coordinates of the GeoJsonPoint
	 */
	public LatLng getCoordinates() {
		return getGeometryObject();
	}

	/**
	 * Gets the altitude of the GeoJsonPoint
	 *
	 * @return altitude of the GeoJsonPoint
	 */
	public Double getAltitude() {
		return mAltitude;
	}
}