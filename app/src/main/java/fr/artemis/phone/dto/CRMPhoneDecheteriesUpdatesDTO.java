package fr.artemis.phone.dto;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Hashtable;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;

public class CRMPhoneDecheteriesUpdatesDTO implements KvmSerializable {

	private Integer id;
	private Integer version;
	private String sqlUpdates;

	@Override
	public int getPropertyCount() {
		return 3;
	}

	@Override
	public Object getProperty( int index ) {
		switch ( index ) {
			case 0:
				return id;
			case 1:
				return version;
			case 2:
				return sqlUpdates;
		}
		return null;
	}

	@Override
	public void getPropertyInfo( int index, Hashtable properties, PropertyInfo info ) {
		switch ( index ) {
			case 0:
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "id";
				break;
			case 1:
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "version";
				break;
			case 2:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "sqlUpdates";
				break;
			default:
				break;
		}
	}

	@Override
	public void setProperty( int index, Object value ) {
		switch ( index ) {
			case 0:
				id = Integer.parseInt( value.toString() );
				break;
			case 1:
				version = Integer.parseInt( value.toString() );
				break;
			case 2:
				sqlUpdates = value.toString();
				break;
		}
	}

	public void setProperty( String name, Object value ) {
		if ( null != value && !value.toString().equals( "anyType{}" ) ) {
			switch ( name ) {
				case "id":
					this.id = Integer.parseInt( value.toString() );
					break;
				case "version":
					this.version = Integer.parseInt( value.toString() );
					break;
				case "sqlUpdates":
					this.sqlUpdates = value.toString();
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion( Integer version ) {
		this.version = version;
	}

	public String getSqlUpdates() {
		return sqlUpdates;
	}

	public void setSqlUpdates( String sqlUpdates ) {
		this.sqlUpdates = sqlUpdates;
	}
}