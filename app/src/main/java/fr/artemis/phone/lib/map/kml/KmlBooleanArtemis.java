package fr.artemis.phone.lib.map.kml;

public class KmlBooleanArtemis {

	public static boolean parseBoolean( String text ) {
		return "1".equals( text ) || "true".equals( text );
	}
}
