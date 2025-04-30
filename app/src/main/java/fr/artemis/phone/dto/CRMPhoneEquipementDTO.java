package fr.artemis.phone.dto;

import java.util.Hashtable;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.webservices.mapper.impl.EquipementTypeTailleMapper;

public class CRMPhoneEquipementDTO implements KvmSerializable {

	private Integer id;
	private CRMPhoneEquipementTailleTypeDTO tailleType;
	private String equipement;

	public Object getProperty( int arg0 ) {

		switch ( arg0 ) {
			case 0:
				return id;
			case 1:
				return tailleType;
			case 2:
				return equipement;
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
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "tailleType";
				break;
			case 2:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "equipement";
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
				tailleType = (CRMPhoneEquipementTailleTypeDTO) value;
				break;
			case 2:
				equipement = value.toString();
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
				case "tailleType":
					this.tailleType = new EquipementTypeTailleMapper().map( (SoapObject) value );
					break;
				case "equipement":
					this.equipement = value.toString();
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

	public CRMPhoneEquipementTailleTypeDTO getTailleType() {
		return tailleType;
	}

	public void setTailleType( CRMPhoneEquipementTailleTypeDTO tailleType ) {
		this.tailleType = tailleType;
	}

	public String getEquipement() {
		return equipement;
	}

	public void setEquipement( String equipement ) {
		this.equipement = equipement;
	}
}