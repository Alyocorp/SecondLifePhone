package fr.artemis.phone.dto;

import java.io.Serializable;
import java.util.Hashtable;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;

public class CRMPhoneClientDTO implements Serializable, KvmSerializable {

	private Integer id;
	private String civilite;
	private String nom;
	private String prenom;
	private String adresse;
	private String adresseComplement;
	private String codePostal;
	private String ville;
	private String telephone;
	private String email;

	public Object getProperty( int arg0 ) {

		switch ( arg0 ) {
			case 0:
				return id;
			case 1:
				return civilite;
			case 2:
				return nom;
			case 3:
				return prenom;
			case 4:
				return adresse;
			case 5:
				return adresseComplement;
			case 6:
				return codePostal;
			case 7:
				return ville;
			case 8:
				return telephone;
			case 9:
				return email;
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
				info.name = "civilite";
				break;
			case 2:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "nom";
				break;
			case 3:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "prenom";
				break;
			case 4:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "adresse";
				break;
			case 5:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "adresseComplement";
				break;
			case 6:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "codePostal";
				break;
			case 7:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "ville";
				break;
			case 8:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "telephone";
				break;
			case 9:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "email";
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
				civilite = value.toString();
				break;
			case 2:
				nom = value.toString();
				break;
			case 3:
				prenom = value.toString();
				break;
			case 4:
				adresse = value.toString();
				break;
			case 5:
				adresseComplement = value.toString();
				break;
			case 6:
				codePostal = value.toString();
				break;
			case 7:
				ville = value.toString();
				break;
			case 8:
				telephone = value.toString();
				break;
			case 9:
				email = value.toString();
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
				case "civilite":
					this.civilite = value.toString();
					break;
				case "nom":
					this.nom = value.toString();
					break;
				case "prenom":
					this.prenom = value.toString();
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
				case "telephone":
					this.telephone = value.toString();
					break;
				case "email":
					this.email = value.toString();
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

	public String getCivilite() {
		return civilite;
	}

	public void setCivilite( String civilite ) {
		this.civilite = civilite;
	}

	public String getNom() {
		return nom;
	}

	public void setNom( String nom ) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom( String prenom ) {
		this.prenom = prenom;
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

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone( String telephone ) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail( String email ) {
		this.email = email;
	}
}
