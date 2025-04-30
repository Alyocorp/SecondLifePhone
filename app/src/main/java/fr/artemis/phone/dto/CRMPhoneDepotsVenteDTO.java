package fr.artemis.phone.dto;

import java.math.BigDecimal;
import java.util.Hashtable;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;

public class CRMPhoneDepotsVenteDTO implements KvmSerializable {

	private Integer id;

	private String latitude;

	private String longitude;

	private String nom;

	private String contact;

	private String adresse;

	private String horaires;

	private boolean blacklist;

	private String telephone;

	private Boolean compteOuvert;

	public Object getProperty( int arg0 ) {

		switch ( arg0 ) {
			case 0:
				return id;
			case 1:
				return latitude;
			case 2:
				return longitude;
			case 3:
				return nom;
			case 4:
				return contact;
			case 5:
				return adresse;
			case 6:
				return horaires;
			case 7:
				return blacklist;
			case 8:
				return telephone;
			case 9:
				return compteOuvert;
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
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "latitude";
				break;
			case 2:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "longitude";
				break;
			case 3:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "nom";
				break;
			case 4:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "contact";
				break;
			case 5:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "adresse";
				break;
			case 6:
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "horaires";
				break;
			case 7:
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "blacklist";
			case 8:
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "telephone";
			case 9:
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "compteOuvert";
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
				latitude = value.toString();
				break;
			case 2:
				longitude = value.toString();
				break;
			case 3:
				nom = value.toString();
				break;
			case 4:
				contact = value.toString();
				break;
			case 5:
				adresse = value.toString();
				break;
			case 6:
				horaires = value.toString();
				break;
			case 7:
				blacklist = Boolean.parseBoolean( value.toString() );
				break;
			case 8:
				telephone = value.toString();
				break;
			case 9:
				compteOuvert = Boolean.parseBoolean( value.toString() );
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
				case "latitude":
					this.latitude = value.toString();
					break;
				case "longitude":
					this.longitude = value.toString();
					break;
				case "nom":
					this.nom = value.toString();
					break;
				case "contact":
					this.contact = value.toString();
					break;
				case "adresse":
					this.adresse = value.toString();
					break;
				case "horaires":
					this.horaires = value.toString();
					break;
				case "blacklist":
					this.blacklist = Boolean.parseBoolean( value.toString() );
					break;
				case "telephone":
					this.telephone = value.toString();
					break;
				case "compteOuvert":
					this.compteOuvert = Boolean.parseBoolean( value.toString() );
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

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude( String latitude ) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude( String longitude ) {
		this.longitude = longitude;
	}

	public String getNom() {
		return nom;
	}

	public void setNom( String nom ) {
		this.nom = nom;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse( String adresse ) {
		this.adresse = adresse;
	}

	public String getHoraires() {
		return horaires;
	}

	public void setHoraires( String horaires ) {
		this.horaires = horaires;
	}

	public boolean isBlacklist() {
		return blacklist;
	}

	public void setBlacklist( boolean blacklist ) {
		this.blacklist = blacklist;
	}

	public Boolean getCompteOuvert() {
		return compteOuvert;
	}

	public void setCompteOuvert( Boolean compteOuvert ) {
		this.compteOuvert = compteOuvert;
	}

	public String getContact() {
		return contact;
	}

	public void setContact( String contact ) {
		this.contact = contact;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone( String telephone ) {
		this.telephone = telephone;
	}
}
