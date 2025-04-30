package fr.artemis.phone.dto;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.webservices.mapper.impl.DecheterieTarifMapper;

public class CRMPhoneDecheterieDTO implements KvmSerializable {

	private Integer id;

	private String latitude;

	private String longitude;

	private String nom;

	private String description;

	private String horaires;

	private String proOuPartic;

	private boolean blacklist;

	private String barriere;

	private String carte;

	private Boolean compteOuvert;

	private List<CRMPhoneDecheteriesTarifsDTO> tarifs = new ArrayList<>();

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
				return description;
			case 5:
				return horaires;
			case 6:
				return proOuPartic;
			case 7:
				return blacklist;
			case 8:
				return barriere;
			case 9:
				return carte;
			case 10:
				return tarifs;
			case 11:
				return compteOuvert;
		}

		return null;
	}

	public int getPropertyCount() {
		return 11;
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
				info.name = "description";
				break;
			case 5:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "horaires";
				break;
			case 6:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "proOuPartic";
				break;
			case 7:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "blacklist";
				break;
			case 8:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "barriere";
				break;
			case 9:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "carte";
				break;
			case 10:
				info.type = PropertyInfo.VECTOR_CLASS;
				info.name = "tarifs";
				break;
			case 11:
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
				description = value.toString();
				break;
			case 5:
				horaires = value.toString();
				break;
			case 6:
				proOuPartic = value.toString();
				break;
			case 7:
				blacklist = Boolean.parseBoolean( value.toString() );
				break;
			case 8:
				barriere = value.toString();
				break;
			case 9:
				carte = value.toString();
				break;
			case 10:
				tarifs = (List<CRMPhoneDecheteriesTarifsDTO>) value;
				break;
			case 11:
				if ( value.toString().equals( "Oui" ) ) {
					compteOuvert = true;
				} else if ( value.toString().equals( "Non" ) ) {
					compteOuvert = false;
				} else {
					compteOuvert = null;
				}
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
				case "description":
					this.description = value.toString();
					break;
				case "horaires":
					this.horaires = value.toString();
					break;
				case "proOuPartic":
					this.proOuPartic = value.toString();
					break;
				case "blacklist":
					this.blacklist = Boolean.parseBoolean( value.toString() );
					break;
				case "barriere":
					this.barriere = value.toString();
					break;
				case "carte":
					this.carte = value.toString();
					break;
				case "tarifs":
					this.tarifs.add( new DecheterieTarifMapper().map( (SoapObject) value ) );
					break;
				case "compteOuvert":
					if ( value.toString().equals( "Oui" ) ) {
						compteOuvert = true;
					} else if ( value.toString().equals( "Non" ) ) {
						compteOuvert = false;
					} else {
						compteOuvert = null;
					}
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

	public String getDescription() {
		return description;
	}

	public void setDescription( String description ) {
		this.description = description;
	}

	public String getHoraires() {
		return horaires;
	}

	public void setHoraires( String horaires ) {
		this.horaires = horaires;
	}

	public String getProOuPartic() {
		return proOuPartic;
	}

	public void setProOuPartic( String proOuPartic ) {
		this.proOuPartic = proOuPartic;
	}

	public boolean isBlacklist() {
		return blacklist;
	}

	public void setBlacklist( boolean blacklist ) {
		this.blacklist = blacklist;
	}

	public String getBarriere() {
		return barriere;
	}

	public void setBarriere( String barriere ) {
		this.barriere = barriere;
	}

	public String getCarte() {
		return carte;
	}

	public void setCarte( String carte ) {
		this.carte = carte;
	}

	public void setTarifs( List<CRMPhoneDecheteriesTarifsDTO> tarifs ) {
		this.tarifs = tarifs;
	}

	public List<CRMPhoneDecheteriesTarifsDTO> getTarifs() {
		return tarifs;
	}

	public void setCompteOuvert( Boolean compteOuvert ) {
		this.compteOuvert = compteOuvert;
	}

	public Boolean getCompteOuvert() {
		return compteOuvert;
	}
}