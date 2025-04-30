package fr.artemis.phone.dto;

import java.util.Hashtable;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;

public class SecteurDTO implements KvmSerializable {

	private Integer id;
	private String secteur;

	public Object getProperty( int arg0 ) {

		switch ( arg0 ) {
			case 0:
				return id;
			case 1:
				return secteur;
		}

		return null;
	}

	public int getPropertyCount() {
		return 2;
	}

	public void getPropertyInfo( int index, Hashtable arg1, PropertyInfo info ) {
		switch ( index ) {
			case 0:
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "id";
				break;
			case 1:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "secteur";
				break;
			default:
				break;
		}
	}

	public void setProperty( int index, Object value ) {
		switch ( index ) {
			case 0:
				id = Integer.parseInt( value.toString() );
				break;
			case 1:
				secteur = value.toString();
				break;
			default:
				break;
		}
	}


	public void setProperty( String name, Object value ) {
		if ( null != value && !value.toString().equals( "anyType{}" ) ) {
			switch ( name ) {
				case "id":
					id = Integer.parseInt( value.toString() );
					break;
				case "secteur":
					secteur = value.toString();
					break;
				default:
					break;
			}
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId( Integer id ) {
		this.id = id;
	}

	public String getSecteur() {
		return secteur;
	}

	public void setSecteur( String secteur ) {
		this.secteur = secteur;
	}
}