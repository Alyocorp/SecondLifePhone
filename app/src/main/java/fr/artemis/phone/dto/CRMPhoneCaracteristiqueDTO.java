package fr.artemis.phone.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.webservices.mapper.impl.CaracteristiqueNoteMapper;

public class CRMPhoneCaracteristiqueDTO implements Serializable, KvmSerializable {

	private Integer id;
	private String caracteristique;
	private List<CRMPhoneCaracteristiqueNoteDTO> listeNotes = new ArrayList<>();


	public Object getProperty( int arg0 ) {

		switch ( arg0 ) {
			case 0:
				return id;
			case 1:
				return caracteristique;
			case 2:
				return listeNotes;
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
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "caracteristique";
				break;
			case 2:
				info.type = PropertyInfo.VECTOR_CLASS;
				info.name = "listeNotes";
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
				caracteristique = value.toString();
				break;
			case 2:
				listeNotes = (List<CRMPhoneCaracteristiqueNoteDTO>) value;
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
				case "caracteristique":
					this.caracteristique = value.toString();
					break;
				case "listeNotes":
					listeNotes.add( new CaracteristiqueNoteMapper().map( (SoapObject) value ) );
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

	public String getCaracteristique() {
		return caracteristique;
	}

	public void setCaracteristique( String caracteristique ) {
		this.caracteristique = caracteristique;
	}

	public List<CRMPhoneCaracteristiqueNoteDTO> getListeNotes() {
		return listeNotes;
	}

	public void setListeNotes( List<CRMPhoneCaracteristiqueNoteDTO> listeNotes ) {
		this.listeNotes = listeNotes;
	}
}
