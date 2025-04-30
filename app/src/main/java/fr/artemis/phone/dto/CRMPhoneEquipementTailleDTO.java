package fr.artemis.phone.dto;

import java.util.Hashtable;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.webservices.mapper.impl.EquipementTailleMapper;

public class CRMPhoneEquipementTailleDTO implements KvmSerializable {

	private Integer id;
	private Integer fkTypeTaille;
	private String taille;

	public Object getProperty( int arg0 ) {

		switch ( arg0 ) {
			case 0:
				return id;
			case 1:
				return fkTypeTaille;
			case 2:
				return taille;
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
				info.name = "fkTypeTaille";
				break;
			case 2:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "taille";
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
				case "fkTypeTaille":
					this.fkTypeTaille = Integer.parseInt( value.toString() );
					break;
				case "taille":
					this.taille = value.toString();
					break;
				default:
					throw new IllegalStateException( "Champ inconnu :" + name );
			}
		}
	}

	public void setProperty( int index, Object value ) {
		switch ( index ) {
			case 0:
				id = Integer.parseInt( value.toString() );
				break;
			case 1:
				fkTypeTaille = Integer.parseInt( value.toString() );
				break;
			case 2:
				taille = value.toString();
				break;
			default:
				break;
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId( Integer id ) {
		this.id = id;
	}

	public Integer getFkTypeTaille() {
		return fkTypeTaille;
	}

	public void setFkTypeTaille( Integer fkTypeTaille ) {
		this.fkTypeTaille = fkTypeTaille;
	}

	public String getTaille() {
		return taille;
	}

	public void setTaille( String taille ) {
		this.taille = taille;
	}
}