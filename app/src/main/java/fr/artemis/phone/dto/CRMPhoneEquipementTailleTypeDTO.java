package fr.artemis.phone.dto;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.webservices.mapper.impl.EquipementTailleMapper;

public class CRMPhoneEquipementTailleTypeDTO implements KvmSerializable {

	private Integer id;
	private String tailleType;
	private List<CRMPhoneEquipementTailleDTO> listeTailles = new ArrayList<>();

	public Object getProperty( int arg0 ) {

		switch ( arg0 ) {
			case 0:
				return id;
			case 1:
				return tailleType;
			case 2:
				return listeTailles;
		}

		return null;
	}

	public int getPropertyCount() {
		return 3;
	}

	public void setProperty( String name, Object value ) {
		switch ( name ) {
			case "id":
				this.id = Integer.parseInt( value.toString() );
				break;
			case "tailleType":
				this.tailleType = value.toString();
				break;
			case "listeTailles":
				this.listeTailles.add( new EquipementTailleMapper().map( (SoapObject) value ) );
				break;
			default:
				throw new IllegalStateException( "Champ inconnu :" + name );
		}
	}

	public void getPropertyInfo( int index, Hashtable arg1, PropertyInfo info ) {
		switch ( index ) {
			case 0:
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "id";
				break;
			case 1:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "tailleType";
				break;
			case 2:
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "listeTailles";
				break;
			default:
				break;
		}
	}

	public void setProperty( int index, Object value ) {
		if ( null != value && !value.toString().equals( "anyType{}" ) ) {
			switch ( index ) {
				case 0:
					id = Integer.parseInt( value.toString() );
					break;
				case 1:
					tailleType = value.toString();
					break;
				case 2:
					listeTailles.add( new EquipementTailleMapper().map( (SoapObject) value ) );
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

	public String getTailleType() {
		return tailleType;
	}

	public void setTailleType( String tailleType ) {
		this.tailleType = tailleType;
	}

	public List<CRMPhoneEquipementTailleDTO> getListeTailles() {
		return listeTailles;
	}

	public void setListeTailles( List<CRMPhoneEquipementTailleDTO> listeTailles ) {
		this.listeTailles = listeTailles;
	}
}