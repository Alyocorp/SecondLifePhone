package fr.artemis.phone.dto;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.webservices.mapper.impl.SecteurMapper;
import fr.artemis.phone.webservices.mapper.impl.UtilisateurMapper;

public class SalarieLightDTO implements KvmSerializable, Serializable {

	private Integer id;
	private UtilisateurDTO utilisateurLight;
	private String civilite;
	private String nom;
	private String prenom;
	private String adresse;
	private String adresseComplement;
	private String codePostal;
	private String ville;
	private String telephone;
	private String telephoneProfessionnel;
	private String email;
	private String emailProfessionnel;
	private String motDePasseEmail;
	private String signature;
	private SecteurDTO secteur;
	private Calendar dateAnciennete;
	private Boolean actif;
	private String sheetsFile;


	public Object getProperty( int arg0 ) {

		switch ( arg0 ) {
			case 0:
				return id;
			case 1:
				return utilisateurLight;
			case 2:
				return civilite;
			case 3:
				return nom;
			case 4:
				return prenom;
			case 5:
				return adresse;
			case 6:
				return adresseComplement;
			case 7:
				return codePostal;
			case 8:
				return ville;
			case 9:
				return telephone;
			case 10:
				return telephoneProfessionnel;
			case 11:
				return email;
			case 12:
				return emailProfessionnel;
			case 13:
				return motDePasseEmail;
			case 14:
				return signature;
			case 15:
				return secteur;
			case 16:
				return dateAnciennete;
			case 17:
				return actif;
			case 18:
				return sheetsFile;
		}

		return null;
	}

	public int getPropertyCount() {
		return 19;
	}

	public void getPropertyInfo( int index, Hashtable arg1, PropertyInfo info ) {
		switch ( index ) {
			case 0:
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "id";
				break;
			case 1:
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "utilisateurLight";
				break;
			case 2:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "civilite";
				break;
			case 3:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "nom";
				break;
			case 4:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "prenom";
				break;
			case 5:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "adresse";
				break;
			case 6:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "adresseComplement";
				break;
			case 7:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "codePostal";
				break;
			case 8:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "ville";
				break;
			case 9:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "telephone";
				break;
			case 10:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "telephoneProfessionnel";
				break;
			case 11:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "email";
				break;
			case 12:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "emailProfessionnel";
				break;
			case 13:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "motDePasseEmail";
				break;
			case 14:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "signature";
				break;
			case 15:
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "secteur";
				break;
			case 16:
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "dateAnciennete";
				break;
			case 17:
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "actif";
				break;
			case 18:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "sheetsFile";
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
				utilisateurLight = (UtilisateurDTO) value;
				break;
			case 2:
				civilite = value.toString();
				break;
			case 3:
				nom = value.toString();
				break;
			case 4:
				prenom = value.toString();
				break;
			case 5:
				adresse = value.toString();
				break;
			case 6:
				adresseComplement = value.toString();
				break;
			case 7:
				codePostal = value.toString();
				break;
			case 8:
				ville = value.toString();
				break;
			case 9:
				telephone = value.toString();
				break;
			case 10:
				telephoneProfessionnel = value.toString();
				break;
			case 11:
				email = value.toString();
				break;
			case 12:
				emailProfessionnel = value.toString();
				break;
			case 13:
				motDePasseEmail = value.toString();
				break;
			case 14:
				signature = value.toString();
				break;
			case 15:
				secteur = (SecteurDTO) value;
				break;
			case 16:
				dateAnciennete = (Calendar) value;
				break;
			case 17:
				actif = Boolean.parseBoolean( value.toString() );
				break;
			case 18:
				sheetsFile = value.toString();
				break;
			default:
				break;
		}
	}

	public void setProperty( String name, Object value ) {
		if ( null != value && !value.toString().equals( "anyType{}" ) ) {
			switch ( name ) {
				case "id":
					id = Integer.parseInt( value.toString() );
					break;
				case "utilisateurLight":
					utilisateurLight = new UtilisateurMapper().map( (SoapObject) value );
					break;
				case "civilite":
					civilite = value.toString();
					break;
				case "nom":
					nom = value.toString();
					break;
				case "prenom":
					prenom = value.toString();
					break;
				case "adresse":
					adresse = value.toString();
					break;
				case "adresseComplement":
					adresseComplement = value.toString();
					break;
				case "codePostal":
					codePostal = value.toString();
					break;
				case "ville":
					ville = value.toString();
					break;
				case "telephone":
					telephone = value.toString();
					break;
				case "telephoneProfessionnel":
					telephoneProfessionnel = value.toString();
					break;
				case "email":
					email = value.toString();
					break;
				case "emailProfessionnel":
					emailProfessionnel = value.toString();
					break;
				case "motDePasseEmail":
					motDePasseEmail = value.toString();
					break;
				case "signature":
					signature = value.toString();
					break;
				case "secteur":
					secteur = new SecteurMapper().map( (SoapObject) value );
					break;
				case "dateAnciennete":
					try {
						SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
						Calendar cal = Calendar.getInstance();
						cal.setTime( sdf.parse( value.toString() ) );
						dateAnciennete = cal;
					} catch ( ParseException e ) {
						e.printStackTrace();
					}
					break;
				case "actif":
					actif = Boolean.parseBoolean( value.toString() );
					break;
				case "sheetsFile":
					sheetsFile = value.toString();
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

	public SecteurDTO getSecteur() {
		return secteur;
	}

	public void setSecteur( SecteurDTO secteur ) {
		this.secteur = secteur;
	}

	public Calendar getDateAnciennete() {
		return dateAnciennete;
	}

	public void setDateAnciennete( Calendar dateAnciennete ) {
		this.dateAnciennete = dateAnciennete;
	}

	public Boolean getActif() {
		return actif;
	}

	public void setActif( Boolean actif ) {
		this.actif = actif;
	}

	public String getCivilite() {
		return civilite;
	}

	public void setCivilite( String civilite ) {
		this.civilite = civilite;
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

	public String getTelephoneProfessionnel() {
		return telephoneProfessionnel;
	}

	public void setTelephoneProfessionnel( String telephoneProfessionnel ) {
		this.telephoneProfessionnel = telephoneProfessionnel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail( String email ) {
		this.email = email;
	}

	public String getEmailProfessionnel() {
		return emailProfessionnel;
	}

	public void setEmailProfessionnel( String emailProfessionnel ) {
		this.emailProfessionnel = emailProfessionnel;
	}

	public String getMotDePasseEmail() {
		return motDePasseEmail;
	}

	public void setMotDePasseEmail( String motDePasseEmail ) {
		this.motDePasseEmail = motDePasseEmail;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature( String signature ) {
		this.signature = signature;
	}

	public UtilisateurDTO getUtilisateurLight() {
		return utilisateurLight;
	}

	public void setUtilisateurLight( UtilisateurDTO utilisateurLight ) {
		this.utilisateurLight = utilisateurLight;
	}

	public String getSheetsFile() {
		return sheetsFile;
	}

	public void setSheetsFile( String sheetsFile ) {
		this.sheetsFile = sheetsFile;
	}

	@Override
	public String toString() {
		return nom + " " + prenom;
	}
}
