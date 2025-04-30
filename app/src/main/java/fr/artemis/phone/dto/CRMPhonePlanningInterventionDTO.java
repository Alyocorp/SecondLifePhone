package fr.artemis.phone.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.utils.DateUtils;
import fr.artemis.phone.webservices.mapper.impl.ClientMapper;
import fr.artemis.phone.webservices.mapper.impl.DevisCaracteristiquesMapper;

import static fr.artemis.phone.lib.ksoap.kobjects.isodate.IsoDate.DATE_TIME;

public class CRMPhonePlanningInterventionDTO implements Serializable, KvmSerializable {

	private Integer id;
	private Integer fkPlanning;
	private Calendar dateDebut;
	private Calendar dateFin;
	private List<CRMPhoneDevisCaracteristiqueDTO> listeCaracteristiques = new ArrayList<>();
	private CRMPhoneClientDTO client;
	private String adresse;
	private String adresseComplement;
	private String codePostal;
	private String ville;
	private String fichierDevis;
	private String remarques;
	private String materiel;
	private BigDecimal volume;
	private BigDecimal valeur;
	private BigDecimal montantTotal;
	private boolean devisInter;
	private Integer fkDevis;
	private BigDecimal montantPrestaSup;

	public Object getProperty( int arg0 ) {

		switch ( arg0 ) {
			case 0 :
				return id;
			case 1 :
				return fkPlanning;
			case 2 :
				return dateDebut;
			case 3 :
				return dateFin;
			case 4 :
				return listeCaracteristiques;
			case 5 :
				return client;
			case 6 :
				return adresse;
			case 7 :
				return adresseComplement;
			case 8 :
				return codePostal;
			case 9 :
				return ville;
			case 10 :
				return fichierDevis;
			case 11 :
				return remarques;
			case 12 :
				return materiel;
			case 13 :
				return volume;
			case 14 :
				return valeur;
			case 15 :
				return montantTotal;
			case 16 :
				return devisInter;
			case 17 :
				return fkDevis;
			case 18 :
				return montantPrestaSup;
		}

		return null;
	}

	public int getPropertyCount() {
		return 19;
	}

	public void getPropertyInfo( int index, Hashtable arg1, PropertyInfo info ) {
		switch ( index ) {
			case 0 :
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "id";
				break;
			case 1 :
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "fkPanning";
				break;
			case 2 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "dateDebut";
				break;
			case 3 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "dateFin";
				break;
			case 4 :
				info.type = PropertyInfo.VECTOR_CLASS;
				info.name = "listeCaracteristiques";
				break;
			case 5 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "client";
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
				info.name = "fichierDevis";
				break;
			case 11 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "remarques";
				break;
			case 12 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "materiel";
				break;
			case 13 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "volume";
				break;
			case 14 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "valeur";
				break;
			case 15 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "montantTotal";
				break;
			case 16 :
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "devisInter";
				break;
			case 17 :
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "fkDevis";
				break;
			case 18 :
				info.type = PropertyInfo.BIGDECIMAL_CLASS;
				info.name = "montantPrestaSup";
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
				fkPlanning = Integer.parseInt( value.toString() );
				break;
			case 2 :
				dateDebut = (Calendar) value;
				break;
			case 3 :
				dateFin = (Calendar) value;
				break;
			case 4 :
				listeCaracteristiques = (List<CRMPhoneDevisCaracteristiqueDTO>) value;
				break;
			case 5 :
				client = (CRMPhoneClientDTO) value;
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
				fichierDevis = value.toString();
				break;
			case 11 :
				remarques = value.toString();
				break;
			case 12 :
				materiel = value.toString();
				break;
			case 13 :
				volume = new BigDecimal( value.toString() );
				break;
			case 14 :
				valeur = new BigDecimal( value.toString() );
				break;
			case 15 :
				montantTotal = new BigDecimal( value.toString() );
				break;
			case 16 :
				devisInter = Boolean.parseBoolean( value.toString() );
				break;
			case 17 :
				fkDevis = Integer.parseInt( value.toString() );
				break;
			case 18 :
				montantPrestaSup = new BigDecimal( value.toString() );
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
				case "fkPlanning" :
					this.fkPlanning = Integer.parseInt( value.toString() );
					break;
				case "dateDebut" :
					this.dateDebut = DateUtils.stringToCalendar( value.toString(), DATE_TIME );
					break;
				case "dateFin" :
					this.dateFin = DateUtils.stringToCalendar( value.toString(), DATE_TIME );
					break;
				case "listeCaracteristiques" :
					this.listeCaracteristiques.add( new DevisCaracteristiquesMapper().map( (SoapObject) value ) );
					break;
				case "client" :
					this.client = new ClientMapper().map( (SoapObject) value );
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
				case "fichierDevis" :
					this.fichierDevis = value.toString();
					break;
				case "remarques" :
					this.remarques = value.toString();
					break;
				case "materiel" :
					this.materiel = value.toString();
					break;
				case "volume" :
					this.volume = new BigDecimal( value.toString() );
					break;
				case "valeur" :
					this.valeur = new BigDecimal( value.toString() );
					break;
				case "montantTotal" :
					this.montantTotal = new BigDecimal( value.toString() );
					break;
				case "devisInter" :
					this.devisInter = Boolean.parseBoolean( value.toString() );
					break;
				case "fkDevis" :
					this.fkDevis = Integer.parseInt( value.toString() );
					break;
				case "montantPrestaSup" :
					this.montantPrestaSup = new BigDecimal( value.toString() );
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

	public Integer getFkPlanning() {
		return fkPlanning;
	}

	public void setFkPlanning( Integer fkPlanning ) {
		this.fkPlanning = fkPlanning;
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

	public List<CRMPhoneDevisCaracteristiqueDTO> getListeCaracteristiques() {
		return listeCaracteristiques;
	}

	public void setListeCaracteristiques( List<CRMPhoneDevisCaracteristiqueDTO> listeCaracteristiques ) {
		this.listeCaracteristiques = listeCaracteristiques;
	}

	public CRMPhoneClientDTO getClient() {
		return client;
	}

	public void setClient( CRMPhoneClientDTO client ) {
		this.client = client;
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

	public String getFichierDevis() {
		return fichierDevis;
	}

	public void setFichierDevis( String fichierDevis ) {
		this.fichierDevis = fichierDevis;
	}

	public String getRemarques() {
		return remarques;
	}

	public void setRemarques( String remarques ) {
		this.remarques = remarques;
	}

	public String getMateriel() {
		return materiel;
	}

	public void setMateriel( String materiel ) {
		this.materiel = materiel;
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

	public BigDecimal getMontantTotal() {
		return montantTotal;
	}

	public void setMontantTotal( BigDecimal montantTotal ) {
		this.montantTotal = montantTotal;
	}

	public boolean isDevisInter() {
		return devisInter;
	}

	public void setDevisInter( boolean devisInter ) {
		this.devisInter = devisInter;
	}

	public Integer getFkDevis() {
		return fkDevis;
	}

	public void setFkDevis( Integer fkDevis ) {
		this.fkDevis = fkDevis;
	}

	public BigDecimal getMontantPrestaSup() {
		return montantPrestaSup;
	}

	public void setMontantPrestaSup( BigDecimal montantPrestaSup ) {
		this.montantPrestaSup = montantPrestaSup;
	}
}