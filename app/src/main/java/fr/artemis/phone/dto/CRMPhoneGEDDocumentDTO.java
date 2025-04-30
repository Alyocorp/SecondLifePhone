package fr.artemis.phone.dto;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Hashtable;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;

public class CRMPhoneGEDDocumentDTO implements KvmSerializable, Serializable {

	private Integer id;
	private String nom;
	private String description;
	private String url;

	@Override
	public int getPropertyCount() {
		return 4;
	}

	@Override
	public Object getProperty( int index ) {
		switch ( index ) {
			case 0 :
				return id;
			case 1 :
				return nom;
			case 2 :
				return description;
			case 3 :
				return url;
		}
		return null;
	}

	@Override
	public void getPropertyInfo( int index, Hashtable properties, PropertyInfo info ) {
		switch ( index ) {
			case 0 :
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "id";
				break;
			case 1 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "nom";
				break;
			case 2 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "description";
				break;
			case 3 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "url";
				break;
			default :
				break;
		}
	}

	@Override
	public void setProperty( int index, Object value ) {
		switch ( index ) {
			case 0 :
				id = Integer.parseInt( value.toString() );
				break;
			case 1 :
				nom = value.toString();
				break;
			case 2 :
				description = value.toString();
				break;
			case 3 :
				url = value.toString();
				break;
		}
	}

	public void setProperty( String name, Object value ) {
		if ( null != value && !value.toString().equals( "anyType{}" ) ) {
			switch ( name ) {
				case "id" :
					this.id = Integer.parseInt( value.toString() );
					break;
				case "nom" :
					this.nom = value.toString();
					break;
				case "description" :
					this.description = value.toString();
					break;
				case "url" :
					this.url = value.toString();
					break;
				default :
					throw new IllegalStateException( "Champ inconnu :" + name );
			}
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId( Integer id ) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom( String nom ) {
		this.nom = nom;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription( String description ) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl( String url ) {
		this.url = url;
	}
}