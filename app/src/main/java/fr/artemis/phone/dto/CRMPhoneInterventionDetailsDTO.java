package fr.artemis.phone.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Hashtable;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;

public class CRMPhoneInterventionDetailsDTO implements KvmSerializable, Serializable {

	private Integer id;

	private String numeroDevis;

	private String fichierDevis;

	private String civilite;

	private String nom;

	private String prenom;

	private String adresse;

	private String adresseComplement;

	private String codePostal;

	private String ville;

	private String realisePar;

	private Integer nbDouilles;

	private BigDecimal tarifDouilles;

	private boolean autorisationVoirie;

	private String materielNecessaire;

	private String remarques;

	private String noteAcces;

	private String noteTri;

	private String noteSalubrite;

	private BigDecimal volume;

	private BigDecimal valeur;

	private BigDecimal tarif;

	private BigDecimal remise;

	private BigDecimal total;

	private BigDecimal accompte;

	private BigDecimal resteARegler;

	private BigDecimal montantPrestaSup;

	private Boolean devisSigne;

	public Object getProperty( int arg0 ) {

		switch ( arg0 ) {
			case 0 :
				return id;
			case 1 :
				return numeroDevis;
			case 2 :
				return fichierDevis;
			case 3 :
				return civilite;
			case 4 :
				return nom;
			case 5 :
				return prenom;
			case 6 :
				return adresse;
			case 7 :
				return adresseComplement;
			case 8 :
				return codePostal;
			case 9 :
				return ville;
			case 10 :
				return realisePar;
			case 11 :
				return nbDouilles;
			case 12 :
				return tarifDouilles;
			case 13 :
				return autorisationVoirie;
			case 14 :
				return materielNecessaire;
			case 15 :
				return remarques;
			case 16 :
				return noteAcces;
			case 17 :
				return noteTri;
			case 18 :
				return noteSalubrite;
			case 19 :
				return volume;
			case 20 :
				return valeur;
			case 21 :
				return tarif;
			case 22 :
				return remise;
			case 23 :
				return total;
			case 24 :
				return accompte;
			case 25 :
				return resteARegler;
			case 26 :
				return montantPrestaSup;
			case 27 :
				return devisSigne;
		}

		return null;
	}

	public int getPropertyCount() {
		return 28;
	}

	public void getPropertyInfo( int index, Hashtable arg1, PropertyInfo info ) {
		switch ( index ) {
			case 0 :
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "id";
				break;
			case 1 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "numeroDevis";
				break;
			case 2 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "fichierDevis";
				break;
			case 3 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "civilite";
				break;
			case 4 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "nom";
				break;
			case 5 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "prenom";
				break;
			case 6 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "adresse";
				break;
			case 7 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "adresseComplement";
				break;
			case 8 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "codePostal";
				break;
			case 9 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "ville";
				break;
			case 10 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "realisePar";
				break;
			case 11 :
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "nbDouilles";
				break;
			case 12 :
				info.type = PropertyInfo.BIGDECIMAL_CLASS;
				info.name = "tarifDouilles";
				break;
			case 13 :
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "autorisationVoirie";
				break;
			case 14 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "materielNecessaire";
				break;
			case 15 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "remarques";
				break;
			case 16 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "noteAcces";
				break;
			case 17 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "noteTri";
				break;
			case 18 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "noteSalubrite";
				break;
			case 19 :
				info.type = PropertyInfo.BIGDECIMAL_CLASS;
				info.name = "volume";
				break;
			case 20 :
				info.type = PropertyInfo.BIGDECIMAL_CLASS;
				info.name = "valeur";
				break;
			case 21 :
				info.type = PropertyInfo.BIGDECIMAL_CLASS;
				info.name = "tarif";
				break;
			case 22 :
				info.type = PropertyInfo.BIGDECIMAL_CLASS;
				info.name = "remise";
				break;
			case 23 :
				info.type = PropertyInfo.BIGDECIMAL_CLASS;
				info.name = "total";
				break;
			case 24 :
				info.type = PropertyInfo.BIGDECIMAL_CLASS;
				info.name = "accompte";
				break;
			case 25 :
				info.type = PropertyInfo.BIGDECIMAL_CLASS;
				info.name = "resteARegler";
				break;
			case 26 :
				info.type = PropertyInfo.BIGDECIMAL_CLASS;
				info.name = "montantPrestaSup";
				break;
			case 27 :
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "devisSigne";
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
				numeroDevis = value.toString();
				break;
			case 2 :
				fichierDevis = value.toString();
				break;
			case 3 :
				civilite = value.toString();
				break;
			case 4 :
				nom = value.toString();
				break;
			case 5 :
				prenom = value.toString();
				break;
			case 6 :
				adresse = value.toString();
				break;
			case 7 :
				adresseComplement = value.toString();
				break;
			case 8 :
				codePostal = value.toString();
				break;
			case 9 :
				ville = value.toString();
				break;
			case 10 :
				realisePar = value.toString();
				break;
			case 11 :
				nbDouilles = Integer.parseInt( value.toString() );
				break;
			case 12 :
				tarifDouilles = new BigDecimal( value.toString() );
				break;
			case 13 :
				autorisationVoirie = Boolean.parseBoolean( value.toString() );
				break;
			case 14 :
				materielNecessaire = value.toString();
				break;
			case 15 :
				remarques = value.toString();
				break;
			case 16 :
				noteAcces = value.toString();
				break;
			case 17 :
				noteTri = value.toString();
				break;
			case 18 :
				noteSalubrite = value.toString();
				break;
			case 19 :
				volume = new BigDecimal( value.toString() );
				break;
			case 20 :
				valeur = new BigDecimal( value.toString() );
				break;
			case 21 :
				tarif = new BigDecimal( value.toString() );
				break;
			case 22 :
				remise = new BigDecimal( value.toString() );
				break;
			case 23 :
				total = new BigDecimal( value.toString() );
				break;
			case 24 :
				accompte = new BigDecimal( value.toString() );
				break;
			case 25 :
				resteARegler = new BigDecimal( value.toString() );
				break;
			case 26 :
				montantPrestaSup = new BigDecimal( value.toString() );
				break;
			case 27 :
				devisSigne = Boolean.parseBoolean( value.toString() );
				break;
			default :
				break;
		}
	}

	public void setProperty( String name, Object value ) {
		if ( null != value && !value.toString().equals( "anyType{}" ) ) {
			switch ( name ) {
				case "id" :
					this.id = Integer.parseInt( value.toString() );
					break;
				case "numeroDevis" :
					this.numeroDevis = value.toString();
					break;
				case "fichierDevis" :
					this.fichierDevis = value.toString();
					break;
				case "civilite" :
					this.civilite = value.toString();
					break;
				case "nom" :
					this.nom = value.toString();
					break;
				case "prenom" :
					this.prenom = value.toString();
					break;
				case "adresse" :
					this.adresse = value.toString();
					break;
				case "adresseComplement" :
					this.adresseComplement = value.toString();
					break;
				case "codePostal" :
					this.codePostal = value.toString();
					break;
				case "ville" :
					this.ville = value.toString();
					break;
				case "realisePar" :
					this.realisePar = value.toString();
					break;
				case "nbDouilles" :
					this.nbDouilles = Integer.valueOf( value.toString() );
					break;
				case "tarifDouilles" :
					this.tarifDouilles = new BigDecimal( value.toString() );
					break;
				case "autorisationVoirie" :
					this.autorisationVoirie = Boolean.parseBoolean( value.toString() );
					break;
				case "materielNecessaire" :
					this.materielNecessaire = value.toString();
					break;
				case "remarques" :
					this.remarques = value.toString();
					break;
				case "noteAcces" :
					this.noteAcces = value.toString();
					break;
				case "noteTri" :
					this.noteTri = value.toString();
					break;
				case "noteSalubrite" :
					this.noteSalubrite = value.toString();
					break;
				case "volume" :
					this.volume = new BigDecimal( value.toString() );
					break;
				case "valeur" :
					this.valeur = new BigDecimal( value.toString() );
					break;
				case "tarif" :
					this.tarif = new BigDecimal( value.toString() );
					break;
				case "remise" :
					this.remise = new BigDecimal( value.toString() );
					break;
				case "total" :
					this.total = new BigDecimal( value.toString() );
					break;
				case "accompte" :
					this.accompte = new BigDecimal( value.toString() );
					break;
				case "resteARegler" :
					this.resteARegler = new BigDecimal( value.toString() );
					break;
				case "montantPrestaSup" :
					this.montantPrestaSup = new BigDecimal( value.toString() );
					break;
				case "devisSigne" :
					this.devisSigne = Boolean.parseBoolean( value.toString() );
					break;
				default :
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

	public String getNumeroDevis() {
		return numeroDevis;
	}

	public void setNumeroDevis( String numeroDevis ) {
		this.numeroDevis = numeroDevis;
	}

	public String getFichierDevis() {
		return fichierDevis;
	}

	public void setFichierDevis( String fichierDevis ) {
		this.fichierDevis = fichierDevis;
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

	public String getRealisePar() {
		return realisePar;
	}

	public void setRealisePar( String realisePar ) {
		this.realisePar = realisePar;
	}

	public Integer getNbDouilles() {
		return nbDouilles;
	}

	public void setNbDouilles( Integer nbDouilles ) {
		this.nbDouilles = nbDouilles;
	}

	public BigDecimal getTarifDouilles() {
		return tarifDouilles;
	}

	public void setTarifDouilles( BigDecimal tarifDouilles ) {
		this.tarifDouilles = tarifDouilles;
	}

	public boolean isAutorisationVoirie() {
		return autorisationVoirie;
	}

	public void setAutorisationVoirie( boolean autorisationVoirie ) {
		this.autorisationVoirie = autorisationVoirie;
	}

	public String getMaterielNecessaire() {
		return materielNecessaire;
	}

	public void setMaterielNecessaire( String materielNecessaire ) {
		this.materielNecessaire = materielNecessaire;
	}

	public String getRemarques() {
		return remarques;
	}

	public void setRemarques( String remarques ) {
		this.remarques = remarques;
	}

	public String getNoteAcces() {
		return noteAcces;
	}

	public void setNoteAcces( String noteAcces ) {
		this.noteAcces = noteAcces;
	}

	public String getNoteTri() {
		return noteTri;
	}

	public void setNoteTri( String noteTri ) {
		this.noteTri = noteTri;
	}

	public String getNoteSalubrite() {
		return noteSalubrite;
	}

	public void setNoteSalubrite( String noteSalubrite ) {
		this.noteSalubrite = noteSalubrite;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume( BigDecimal volume ) {
		this.volume = volume;
	}

	public BigDecimal getValeur() {
		return valeur;
	}

	public void setValeur( BigDecimal valeur ) {
		this.valeur = valeur;
	}

	public BigDecimal getTarif() {
		return tarif;
	}

	public void setTarif( BigDecimal tarif ) {
		this.tarif = tarif;
	}

	public BigDecimal getRemise() {
		return remise;
	}

	public void setRemise( BigDecimal remise ) {
		this.remise = remise;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal( BigDecimal total ) {
		this.total = total;
	}

	public BigDecimal getAccompte() {
		return accompte;
	}

	public void setAccompte( BigDecimal accompte ) {
		this.accompte = accompte;
	}

	public BigDecimal getResteARegler() {
		return resteARegler;
	}

	public void setResteARegler( BigDecimal resteARegler ) {
		this.resteARegler = resteARegler;
	}

	public BigDecimal getMontantPrestaSup() {
		return montantPrestaSup;
	}

	public void setMontantPrestaSup( BigDecimal montantPrestaSup ) {
		this.montantPrestaSup = montantPrestaSup;
	}

	public Boolean getDevisSigne() {
		return devisSigne;
	}

	public void setDevisSigne( Boolean devisSigne ) {
		this.devisSigne = devisSigne;
	}
}