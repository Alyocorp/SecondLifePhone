package fr.artemis.phone.dto;

import java.io.Serializable;
import java.util.Hashtable;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;

public class CRMPhoneRapportChantierEquipeDTO implements KvmSerializable, Serializable {

	private Integer id;
	private Integer fkRapport;
	private Integer fkSalarie;

	public Object getProperty( int arg0 ) {

		switch ( arg0 ) {
			case 0 :
				return id;
			case 1 :
				return fkRapport;
			case 2 :
				return fkSalarie;
		}

		return null;
	}

	public void getPropertyInfo( int index, Hashtable arg1, PropertyInfo info ) {
		switch ( index ) {
			case 0 :
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "id";
				break;
			case 1 :
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "fkRapport";
				break;
			case 2 :
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "fkSalarie";
				break;
			default :
				break;
		}
	}

	public void setProperty( int index, Object value ) {
		switch ( index ) {
			case 0 :
				id = Integer.parseInt( value.toString() );
				break;
			case 1 :
				fkRapport = Integer.parseInt( value.toString() );
				break;
			case 2 :
				fkSalarie = Integer.parseInt( value.toString() );
				break;
			default :
				break;
		}
	}

	public void setProperty( String name, Object value ) {
		if ( null != value && !value.toString().equals( "anyType{}" ) ) {
			switch ( name ) {
				case "id" :
					id = Integer.parseInt( value.toString() );
					break;
				case "fkRapport" :
					fkRapport = Integer.parseInt( value.toString() );
					break;
				case "fkSalarie" :
					fkSalarie = Integer.parseInt( value.toString() );
					break;
				default :
					throw new IllegalStateException( "Champ inconnu :" + name );
			}
		}
	}

	public int getPropertyCount() {
		return 3;
	}

	public Integer getId() {
		return id;
	}

	public void setId( Integer id ) {
		this.id = id;
	}

	public Integer getFkRapport() {
		return fkRapport;
	}

	public void setFkRapport( Integer fkRapport ) {
		this.fkRapport = fkRapport;
	}

	public Integer getFkSalarie() {
		return fkSalarie;
	}

	public void setFkSalarie( Integer fkSalarie ) {
		this.fkSalarie = fkSalarie;
	}
}