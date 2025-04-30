package fr.artemis.phone.dto;

import java.io.Serializable;
import java.util.Hashtable;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.webservices.mapper.impl.CaracteristiqueMapper;
import fr.artemis.phone.webservices.mapper.impl.CaracteristiqueNoteMapper;

public class CRMPhoneDevisCaracteristiqueDTO implements Serializable, KvmSerializable {

	private Integer id;
	private Integer fkIntervention;
	private CRMPhoneCaracteristiqueDTO caracteristique;
	private CRMPhoneCaracteristiqueNoteDTO note;

	public Object getProperty( int arg0 ) {

		switch ( arg0 ) {
			case 0:
				return id;
			case 1:
				return fkIntervention;
			case 2:
				return caracteristique;
			case 3:
				return note;
		}

		return null;
	}

	public int getPropertyCount() {
		return 4;
	}

	public void getPropertyInfo( int index, Hashtable arg1, PropertyInfo info ) {
		switch ( index ) {
			case 0:
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "id";
				break;
			case 1:
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "fkIntervention";
				break;
			case 2:
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "caracteristique";
				break;
			case 3:
				info.type = PropertyInfo.OBJECT_CLASS;
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
				fkIntervention = Integer.valueOf( value.toString() );
				break;
			case 2:
				caracteristique = (CRMPhoneCaracteristiqueDTO) value;
				break;
			case 3:
				note = (CRMPhoneCaracteristiqueNoteDTO) value;
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
				case "fkIntervention":
					this.fkIntervention = Integer.parseInt( value.toString() );
					break;
				case "caracteristique":
					this.caracteristique = new CaracteristiqueMapper().map( (SoapObject) value );
					break;
				case "note":
					this.note = new CaracteristiqueNoteMapper().map( (SoapObject) value );
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

	public Integer getFkIntervention() {
		return fkIntervention;
	}

	public void setFkIntervention( Integer fkIntervention ) {
		this.fkIntervention = fkIntervention;
	}

	public CRMPhoneCaracteristiqueDTO getCaracteristique() {
		return caracteristique;
	}

	public void setCaracteristique( CRMPhoneCaracteristiqueDTO caracteristique ) {
		this.caracteristique = caracteristique;
	}

	public CRMPhoneCaracteristiqueNoteDTO getNote() {
		return note;
	}

	public void setNote( CRMPhoneCaracteristiqueNoteDTO note ) {
		this.note = note;
	}
}