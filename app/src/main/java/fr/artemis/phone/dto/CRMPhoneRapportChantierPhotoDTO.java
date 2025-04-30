package fr.artemis.phone.dto;

import java.io.Serializable;
import java.util.Hashtable;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;

public class CRMPhoneRapportChantierPhotoDTO implements KvmSerializable, Serializable {

	private Integer id;
	private Integer fkRapport;
	private String typePhoto;
	private String fileName;
	private String contentThumbnail;
	private String content;

	public Object getProperty( int arg0 ) {

		switch ( arg0 ) {
			case 0:
				return id;
			case 1:
				return fkRapport;
			case 2:
				return typePhoto;
			case 3:
				return fileName;
			case 4:
				return contentThumbnail;
			case 5:
				return content;

		}

		return null;
	}

	public void getPropertyInfo( int index, Hashtable arg1, PropertyInfo info ) {
		switch ( index ) {
			case 0:
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "id";
				break;
			case 1:
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "fkRapport";
				break;
			case 2:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "typePhoto";
				break;
			case 3:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "fileName";
				break;
			case 4:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "contentThumbnail";
				break;
			case 5:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "content";
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
				fkRapport = Integer.parseInt( value.toString() );
				break;
			case 2:
				typePhoto = value.toString();
				break;
			case 3:
				fileName = value.toString();
				break;
			case 4:
				contentThumbnail = value.toString();
				break;
			case 5:
				content = value.toString();
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
				case "fkRapport":
					fkRapport = Integer.parseInt( value.toString() );
					break;
				case "typePhoto":
					typePhoto = value.toString();
					break;
				case "fileName":
					fileName = value.toString();
					break;
				case "contentThumbnail":
					contentThumbnail = value.toString();
					break;
				case "content":
					content = value.toString();
					break;
				default:
					throw new IllegalStateException( "Champ inconnu :" + name );
			}
		}
	}

	public int getPropertyCount() {
		return 6;
	}

	public Integer getId() {
		return id;
	}

	public void setId( Integer id ) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName( String fileName ) {
		this.fileName = fileName;
	}

	public String getContent() {
		return content;
	}

	public void setContent( String content ) {
		this.content = content;
	}

	public Integer getFkRapport() {
		return fkRapport;
	}

	public void setFkRapport( Integer fkRapport ) {
		this.fkRapport = fkRapport;
	}

	public String getTypePhoto() {
		return typePhoto;
	}

	public void setTypePhoto( String typePhoto ) {
		this.typePhoto = typePhoto;
	}

	public String getContentThumbnail() {
		return contentThumbnail;
	}

	public void setContentThumbnail(String contentThumbnail) {
		this.contentThumbnail = contentThumbnail;
	}
}