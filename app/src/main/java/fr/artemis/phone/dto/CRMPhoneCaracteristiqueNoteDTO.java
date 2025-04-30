package fr.artemis.phone.dto;

import java.io.Serializable;
import java.util.Hashtable;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;

public class CRMPhoneCaracteristiqueNoteDTO implements Serializable, KvmSerializable {

	private Integer id;
	private Integer fkCaracteristique;
	private String note;

	public Object getProperty( int arg0 ) {

		switch ( arg0 ) {
			case 0:
				return id;
			case 1:
				return fkCaracteristique;
			case 2:
				return note;
		}

		return null;
	}

	public int getPropertyCount() {
		return 3;
	}

	public void getPropertyInfo( int index, Hashtable arg1, PropertyInfo info ) {
		switch ( index ) {
			case 0:
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "id";
				break;
			case 1:
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "fkCaracteristique";
				break;
			case 2:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "note";
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
				fkCaracteristique = Integer.parseInt( value.toString() );
				break;
			case 2:
				note = value.toString();
				break;
			default:
				break;
		}
	}

	public void setProperty( String name, Object value ) {
		if ( null != value && !value.toString().equals( "anyType{}" ) ) {
			switch ( name ) {
				case "id":
					this.id = Integer.parseInt( value.toString() );
					break;
				case "fkCaracteristique":
					this.fkCaracteristique = Integer.parseInt( value.toString() );
					break;
				case "note":
					this.note = value.toString();
					break;
				default:
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

	public Integer getFkCaracteristique() {
		return fkCaracteristique;
	}

	public void setFkCaracteristique( Integer fkCaracteristique ) {
		this.fkCaracteristique = fkCaracteristique;
	}

	public String getNote() {
		return note;
	}

	public void setNote( String note ) {
		this.note = note;
	}
}