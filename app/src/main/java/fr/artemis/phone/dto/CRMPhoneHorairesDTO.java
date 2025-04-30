package fr.artemis.phone.dto;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.utils.DateUtils;
import fr.artemis.phone.webservices.mapper.impl.EquipementTailleMapper;

import static fr.artemis.phone.lib.ksoap.kobjects.isodate.IsoDate.DATE_TIME;
import static fr.artemis.phone.lib.ksoap.kobjects.isodate.IsoDate.TIME;

public class CRMPhoneHorairesDTO implements KvmSerializable, Serializable {

	private Integer id;
	private Integer fkSalarie;
	private Calendar horaireDate;
	private Date heureDebutMatin;
	private Date heureFinMatin;
	private Date heureDebutAprem;
	private Date heureFinAprem;
	private boolean recup;
	private boolean conges;
	private boolean arret;

	@Override
	public int getPropertyCount() {
		return 10;
	}

	@Override
	public Object getProperty( int index ) {
		switch ( index ) {
			case 0:
				return id;
			case 1:
				return fkSalarie;
			case 2:
				return horaireDate;
			case 3:
				return heureDebutMatin;
			case 4:
				return heureFinMatin;
			case 5:
				return heureDebutAprem;
			case 6:
				return heureFinAprem;
			case 7:
				return recup;
			case 8:
				return conges;
			case 9:
				return arret;

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
				info.name = "fkSalarie";
				break;
			case 2:
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "horaireDate";
				break;
			case 3:
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "heureDebutMatin";
				break;
			case 4:
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "heureFinMatin";
				break;
			case 5:
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "heureDebutAprem";
				break;
			case 6:
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "heureFinAprem";
				break;
			case 7:
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "recup";
				break;
			case 8:
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "conges";
				break;
			case 9:
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "arret";
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
				fkSalarie = Integer.parseInt( value.toString() );
				break;
			case 2:
				horaireDate = (Calendar) value;
				break;
			case 3:
				heureDebutMatin = (Date) value;
				break;
			case 4:
				heureFinMatin = (Date) value;
				break;
			case 5:
				heureDebutAprem = (Date) value;
				break;
			case 6:
				heureFinAprem = (Date) value;
				break;
			case 7:
				recup = Boolean.parseBoolean( value.toString() );
				break;
			case 8:
				conges = Boolean.parseBoolean( value.toString() );
				break;
			case 9:
				arret = Boolean.parseBoolean( value.toString() );
				break;
		}
	}

	public void setProperty( String name, Object value ) throws ParseException {
		if ( null != value && !value.toString().equals( "anyType{}" ) ) {
			switch ( name ) {
				case "id":
					this.id = Integer.parseInt( value.toString() );
					break;
				case "fkSalarie":
					this.fkSalarie = Integer.parseInt( value.toString() );
					break;
				case "horairesDate":
					this.horaireDate = DateUtils.stringToCalendar( value.toString(), DATE_TIME );
					break;
				case "heureDebutMatin":
					this.heureDebutMatin = DateUtils.stringTimeToDate( value.toString().substring( 11, 16 ) );
					break;
				case "heureFinMatin":
					this.heureFinMatin = DateUtils.stringTimeToDate( value.toString().substring( 11, 16 ) );
					break;
				case "heureDebutAprem":
					this.heureDebutAprem = DateUtils.stringTimeToDate( value.toString().substring( 11, 16 ) );
					break;
				case "heureFinAprem":
					this.heureFinAprem = DateUtils.stringTimeToDate( value.toString().substring( 11, 16 ) );
					break;
				case "recup":
					this.recup = Boolean.parseBoolean( value.toString() );
					break;
				case "conges":
					this.conges = Boolean.parseBoolean( value.toString() );
					break;
				case "arret":
					this.arret = Boolean.parseBoolean( value.toString() );
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

	public Integer getFkSalarie() {
		return fkSalarie;
	}

	public void setFkSalarie( Integer fkSalarie ) {
		this.fkSalarie = fkSalarie;
	}

	public Calendar getHoraireDate() {
		return horaireDate;
	}

	public void setHoraireDate( Calendar horaireDate ) {
		this.horaireDate = horaireDate;
	}

	public Date getHeureDebutMatin() {
		return heureDebutMatin;
	}

	public void setHeureDebutMatin( Date heureDebutMatin ) {
		this.heureDebutMatin = heureDebutMatin;
	}

	public Date getHeureFinMatin() {
		return heureFinMatin;
	}

	public void setHeureFinMatin( Date heureFinMatin ) {
		this.heureFinMatin = heureFinMatin;
	}

	public Date getHeureDebutAprem() {
		return heureDebutAprem;
	}

	public void setHeureDebutAprem( Date heureDebutAprem ) {
		this.heureDebutAprem = heureDebutAprem;
	}

	public Date getHeureFinAprem() {
		return heureFinAprem;
	}

	public void setHeureFinAprem( Date heureFinAprem ) {
		this.heureFinAprem = heureFinAprem;
	}

	public boolean isRecup() {
		return recup;
	}

	public void setRecup( boolean recup ) {
		this.recup = recup;
	}

	public boolean isConges() {
		return conges;
	}

	public void setConges( boolean conges ) {
		this.conges = conges;
	}

	public boolean isArret() {
		return arret;
	}

	public void setArret( boolean arret ) {
		this.arret = arret;
	}
}