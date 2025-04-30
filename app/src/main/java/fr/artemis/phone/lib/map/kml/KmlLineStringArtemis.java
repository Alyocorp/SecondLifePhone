package fr.artemis.phone.lib.map.kml;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.data.LineString;

import java.util.ArrayList;
import java.util.List;

public class KmlLineStringArtemis extends LineString {

	private final ArrayList<Double> mAltitudes;

	/**
	 * Creates a new KmlLineString object
	 *
	 * @param coordinates array of coordinates
	 */
	public KmlLineStringArtemis( ArrayList<LatLng> coordinates ) {
		this( coordinates, null );
	}

	/**
	 * Creates a new KmlLineString object
	 *
	 * @param coordinates array of coordinates
	 * @param altitudes   array of altitudes
	 */
	public KmlLineStringArtemis( ArrayList<LatLng> coordinates, ArrayList<Double> altitudes ) {
		super( coordinates );

		this.mAltitudes = altitudes;
	}

	/**
	 * Gets the altitudes
	 *
	 * @return ArrayList of Double
	 */
	public ArrayList<Double> getAltitudes() {
		return mAltitudes;
	}

	/**
	 * Gets the coordinates
	 *
	 * @return ArrayList of LatLng
	 */
	public ArrayList<LatLng> getGeometryObject() {
		List<LatLng> coordinatesList = super.getGeometryObject();
		return new ArrayList<>( coordinatesList );
	}
}