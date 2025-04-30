package fr.artemis.phone.utils;

public enum ConstantesKeys {

	KEY_PASSWORD( "password" ), KEY_USERNAME( "username" );

	private String keyName;

	private ConstantesKeys( String keyName ) {
		this.keyName = keyName;
	}

	public String getKeyName() {
		return keyName;
	}
}