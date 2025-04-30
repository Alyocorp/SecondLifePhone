package fr.artemis.phone.utils;

public enum ConstantesMaps {

	KEY_DECHET_STORAGE( "storeDechet" ),
	KEY_DEPOT_VENTE_STORAGE( "storeDepotVente" ),
	KEY_LAST_POSITION_LATITUDE( "lastPositionLatitude" ),
	KEY_LAST_POSITION_LONGITUDE( "lastPositionLongitude" ),
	KEY_VERSION_MAP_DECHET( "versionDechet" ),
	KEY_VERSION_MAP_DEPOTS_VENTE( "versionDepotVente" );

	private String value;

	ConstantesMaps( String value ) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
