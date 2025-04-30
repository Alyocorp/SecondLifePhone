package fr.artemis.phone.lib.map.kml;

import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.MultiGeometry;

import java.util.ArrayList;
import java.util.List;

public class KmlMultiGeometryArtemis extends MultiGeometry {

	/**
	 * Creates a new MultiGeometry object
	 *
	 * @param geometries array of Geometry objects contained in the MultiGeometry
	 */
	public KmlMultiGeometryArtemis( ArrayList<Geometry> geometries ) {
		super( geometries );
	}

	/**
	 * Gets an ArrayList of Geometry objects
	 *
	 * @return ArrayList of Geometry objects
	 */
	public ArrayList<Geometry> getGeometryObject() {
		List<Geometry> geometriesList = super.getGeometryObject();
		return new ArrayList<>( geometriesList );
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder( getGeometryType() ).append( "{" );
		sb.append( "\n geometries=" ).append( getGeometryObject() );
		sb.append( "\n}\n" );
		return sb.toString();
	}
}
