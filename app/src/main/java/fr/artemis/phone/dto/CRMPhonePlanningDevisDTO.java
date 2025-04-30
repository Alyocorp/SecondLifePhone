package fr.artemis.phone.dto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Hashtable;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.utils.DateUtils;
import fr.artemis.phone.webservices.mapper.impl.ClientMapper;

import static fr.artemis.phone.lib.ksoap.kobjects.isodate.IsoDate.DATE_TIME;

public class CRMPhonePlanningDevisDTO implements Serializable, KvmSerializable {

	private Integer id;
	private Integer fkPlanning;
	private CRMPhoneClientDTO client;
	private Calendar dateDebut;
	private Calendar dateFin;
	private String informationsComplementaires;
	private String adresse;
	private String adresseComplement;
	private String codePostal;
	private String ville;

	public Object getProperty( int arg0 ) {

		switch ( arg0 ) {
			case 0:
				return id;
			case 1:
				return fkPlanning;
			case 2:
				return client;
			case 3:
				return dateDebut;
			case 4:
				return dateFin;
			case 5:
				return informationsComplementaires;
			case 6:
				return adresse;
			case 7:
				return adresseComplement;
			case 8:
				return codePostal;
			case 9:
				return ville;
		}

		return null;
	}

	public int getPropertyCount() {
		return 10;
	}

	public void getPropertyInfo( int index, Hashtable arg1, PropertyInfo info ) {
		switch ( index ) {
			case 0:
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "id";
				break;
			case 1:
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "fkPlanning";
				break;
			case 2:
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "client";
				break;
			case 3:
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "dateDebut";
				break;
			case 4:
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "dateFin";
				break;
			case 5:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "informationsComplementaires";
				break;
			case 6:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "adresse";
				break;
			case 7:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "adresseComplement";
				break;
			case 8:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "codePostal";
				break;
			case 9:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "ville";
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
				fkPlanning = Integer.parseInt( value.toString() );
				break;
			case 2:
				client = (CRMPhoneClientDTO) value;
				break;
			case 3:
				dateDebut = (Calendar) value;
				break;
			case 4:
				dateFin = (Calendar) value;
				break;
			case 5:
				informationsComplementaires = value.toString();
				break;
			case 6:
				adresse = value.toString();
				break;
			case 7:
				adresseComplement = value.toString();
				break;
			case 8:
				codePostal = value.toString();
				break;
			case 9:
				ville = value.toString();
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
				case "fkPlanning":
					this.fkPlanning = Integer.parseInt( value.toString() );
					break;
				case "client":
					this.client = new ClientMapper().map( (SoapObject) value );
					break;
				case "dateDebut":
					this.dateDebut = DateUtils.stringToCalendar( value.toString(), DATE_TIME );
					break;
				case "dateFin":
					this.dateFin = DateUtils.stringToCalendar( value.toString(), DATE_TIME );
					break;
				case "informationsComplementaires":
					this.informationsComplementaires = value.toString();
					break;
				case "adresse":
					this.adresse = value.toString();
					break;
				case "adresseComplement":
					this.adresseComplement = value.toString();
					break;
				case "codePostal":
					this.codePostal = value.toString();
					break;
				case "ville":
					this.ville = value.toString();
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

	public Integer getFkPlanning() {
		return fkPlanning;
	}

	public void setFkPlanning( Integer fkPlanning ) {
		this.fkPlanning = fkPlanning;
	}

	public CRMPhoneClientDTO getClient() {
		return client;
	}

	public void setClient( CRMPhoneClientDTO client ) {
		this.client = client;
	}

	public Calendar getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut( Calendar dateDebut ) {
		this.dateDebut = dateDebut;
	}

	public Calendar getDateFin() {
		return dateFin;
	}

	public void setDateFin( Calendar dateFin ) {
		this.dateFin = dateFin;
	}

	public String getInformationsComplementaires() {
		return informationsComplementaires;
	}

	public void setInformationsComplementaires( String informationsComplementaires ) {
		this.informationsComplementaires = informationsComplementaires;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse( String adresse ) {
		this.adresse = adresse;
	}

	public String getAdresseComplement() {
		return adresseComplement;
	}

	public void setAdresseComplement( String adresseComplement ) {
		this.adresseComplement = adresseComplement;
	}

	public String getCodePostal() {
		return codePostal;
	}

	public void setCodePostal( String codePostal ) {
		this.codePostal = codePostal;
	}

	public String getVille() {
		return ville;
	}

	public void setVille( String ville ) {
		this.ville = ville;
	}
}