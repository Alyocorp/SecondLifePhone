package fr.artemis.phone.lib.map.kml;

import com.google.maps.android.data.Geometry;

import java.util.ArrayList;

public class KmlMultiTrackArtemis extends KmlMultiGeometryArtemis {

	/**
	 * Creates a new MultiGeometry object
	 *
	 * @param tracks array of KmlTrack objects contained in the MultiGeometry
	 */
	public KmlMultiTrackArtemis( ArrayList<KmlTrackArtemis> tracks ) {
		super( createGeometries( tracks ) );
	}

	private static ArrayList<Geometry> createGeometries( ArrayList<KmlTrackArtemis> tracks ) {
		ArrayList<Geometry> geometries = new ArrayList<>();

		if( tracks == null ) {
			throw new IllegalArgumentException( "Tracks cannot be null" );
		}

		for( KmlTrackArtemis track : tracks ) {
			geometries.add( track );
		}

		return geometries;
	}
}