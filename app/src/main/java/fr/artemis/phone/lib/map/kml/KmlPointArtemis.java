package fr.artemis.phone.lib.map.kml;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.data.Point;

public class KmlPointArtemis extends Point {

	private final Double mAltitude;

	/**
	 * Creates a new KmlPointArtemis
	 *
	 * @param coordinates coordinates of the KmlPointArtemis
	 */
	public KmlPointArtemis( LatLng coordinates ) {
		this( coordinates, null );
	}

	/**
	 * Creates a new KmlPointArtemis
	 *
	 * @param coordinates coordinates of the KmlPointArtemis
	 * @param altitude    altitude of the KmlPointArtemis
	 */
	public KmlPointArtemis( LatLng coordinates, Double altitude ) {
		super( coordinates );

		this.mAltitude = altitude;
	}

	public Double getAltitude() {
		return mAltitude;
	}
}