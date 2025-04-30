package fr.artemis.phone.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.utils.DateUtils;
import fr.artemis.phone.webservices.mapper.impl.RapportChantierEquipeMapper;
import fr.artemis.phone.webservices.mapper.impl.RapportChantierPhotosMapper;

import static fr.artemis.phone.lib.ksoap.kobjects.isodate.IsoDate.DATE_TIME;

public class CRMPhoneRapportChantierDTO implements KvmSerializable, Serializable {

	// Map servant à stocker la relation entre les images miniatures affichées et les vrais photos stockées sur le téléphone.
	// Si l'utilisateur ne sauvegarde pas les photos sur le serveur, on conserve la relation afin d'enregistrer les vrais photos ultérieurement
	// sur le serveur
	// La map est sérialisée et stocké dans un fichier sur le telephone et n'est jamais transférée sur le serveur
	private Map<String, String> mapPhotosThumbnailsSource = null;

	private Integer id;
	private Integer fkIntervention;
	private Integer fkUser;
	private Calendar dateSaisie;
	private Calendar dateRapport;

	private List<CRMPhoneRapportChantierPhotoDTO> listePhotosAvant = new ArrayList<>();
	private List<CRMPhoneRapportChantierPhotoDTO> listePhotosRue = new ArrayList<>();
	private List<CRMPhoneRapportChantierPhotoDTO> listePhotosApres = new ArrayList<>();

	private Boolean controleKit;
	private Boolean devisReajuste;
	private BigDecimal devisReajusteVolume;
	private BigDecimal devisReajusteValeur;
	private BigDecimal devisReajusteTotal;

	private Integer fkChefDeChantier;
	private List<CRMPhoneRapportChantierEquipeDTO> listeEquipe = new ArrayList<>();

	private Boolean retard;
	private Boolean retardClientPrevenu;
	private String retardDetails;

	private String heureArriveeChantier;
	private String heureDepartChantier;

	private Boolean deterioration;
	private String deteriorationDetails;

	private Boolean dechetPartic;
	private BigDecimal volumeTraiteDechetPartic;

	private Boolean dechetPro;
	private BigDecimal volumeTraiteDechetProDib;
	private BigDecimal volumeTraiteDechetProBois;
	private BigDecimal volumeTraiteDechetProFerraille;
	private BigDecimal volumeTraiteDechetProPapier;
	private BigDecimal volumeTraiteDechetProAutres;

	private Boolean bennes;
	private BigDecimal volumeTraiteBennesDib;
	private BigDecimal volumeTraiteBennesBois;
	private BigDecimal volumeTraiteBennesFerraille;
	private BigDecimal volumeTraiteBennesPapier;
	private BigDecimal volumeTraiteBennesAutres;

	private Boolean depotVente;
	private BigDecimal volumeTraiteDepotVente;

	private Boolean association;
	private BigDecimal volumeTraiteAssociation;

	private Boolean ramene;
	private BigDecimal volumeTraiteRamene;

	private BigDecimal volumeCamionRamene;
	private BigDecimal volumeCamionFerraille;
	private BigDecimal volumeCamionPapier;
	private BigDecimal volumeCamionDecheterie;

	private Boolean menage;
	private String menageTemps;

	private Boolean respectConsignes;
	private String respectConsignesDetails;

	private Boolean reglementRecuOuIndemnisation;
	private BigDecimal montantRecuOuIndemnise;
	private String detailsReglement;

	private String remarques;

	public Object getProperty( int arg0 ) {

		switch ( arg0 ) {
			case 0 :
				return id;
			case 1 :
				return fkIntervention;
			case 2 :
				return fkUser;
			case 3 :
				return dateSaisie;
			case 4 :
				return dateRapport;
			case 5 :
				return listePhotosAvant;
			case 6 :
				return listePhotosRue;
			case 7 :
				return listePhotosApres;
			case 8 :
				return controleKit;
			case 9 :
				return devisReajuste;
			case 10 :
				return devisReajusteVolume;
			case 11 :
				return devisReajusteValeur;
			case 12 :
				return devisReajusteTotal;
			case 13 :
				return fkChefDeChantier;
			case 14 :
				return listeEquipe;
			case 15 :
				return retard;
			case 16 :
				return retardClientPrevenu;
			case 17 :
				return retardDetails;
			case 18 :
				return deterioration;
			case 19 :
				return deteriorationDetails;
			case 20 :
				return dechetPartic;
			case 21 :
				return volumeTraiteDechetPartic;
			case 22 :
				return dechetPro;
			case 23 :
				return volumeTraiteDechetProDib;
			case 24 :
				return volumeTraiteDechetProBois;
			case 25 :
				return volumeTraiteDechetProFerraille;
			case 26 :
				return volumeTraiteDechetProPapier;
			case 27 :
				return volumeTraiteDechetProAutres;
			case 28 :
				return bennes;
			case 29 :
				return volumeTraiteBennesDib;
			case 30 :
				return volumeTraiteBennesBois;
			case 31 :
				return volumeTraiteBennesFerraille;
			case 32 :
				return volumeTraiteBennesPapier;
			case 33 :
				return volumeTraiteBennesAutres;
			case 34 :
				return depotVente;
			case 35 :
				return volumeTraiteDepotVente;
			case 36 :
				return association;
			case 37 :
				return volumeTraiteAssociation;
			case 38 :
				return ramene;
			case 39 :
				return volumeTraiteRamene;
			case 40 :
				return volumeCamionRamene;
			case 41 :
				return volumeCamionFerraille;
			case 42 :
				return volumeCamionPapier;
			case 43 :
				return volumeCamionDecheterie;
			case 44 :
				return menage;
			case 45 :
				return menageTemps;
			case 46 :
				return respectConsignes;
			case 47 :
				return respectConsignesDetails;
			case 48 :
				return reglementRecuOuIndemnisation;
			case 49 :
				return montantRecuOuIndemnise;
			case 50 :
				return detailsReglement;
			case 51 :
				return remarques;
			case 52 :
				return heureArriveeChantier;
			case 53 :
				return heureDepartChantier;
		}

		return null;
	}

	public int getPropertyCount() {
		return 54;
	}

	public void getPropertyInfo( int index, Hashtable arg1, PropertyInfo info ) {

		switch ( index ) {
			case 0 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "id";
				break;
			case 1 :
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "fkIntervention";
				break;
			case 2 :
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "fkUser";
				break;
			case 3 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "dateSaisie";
				break;
			case 4 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "dateRapport";
				break;
			case 5 :
				info.type = PropertyInfo.VECTOR_CLASS;
				info.name = "listePhotosAvant";
				break;
			case 6 :
				info.type = PropertyInfo.VECTOR_CLASS;
				info.name = "listePhotosRue";
				break;
			case 7 :
				info.type = PropertyInfo.VECTOR_CLASS;
				info.name = "listePhotosApres";
				break;
			case 8 :
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "controleKit";
				break;
			case 9 :
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "devisReajuste";
				break;
			case 10 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "devisReajusteVolume";
				break;
			case 11 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "devisReajusteValeur";
				break;
			case 12 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "devisReajusteTotal";
				break;
			case 13 :
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "fkChefDeChantier";
				break;
			case 14 :
				info.type = PropertyInfo.VECTOR_CLASS;
				info.name = "listeEquipe";
				break;
			case 15 :
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "retard";
				break;
			case 16 :
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "retardClientPrevenu";
				break;
			case 17 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "retardDetails";
				break;
			case 18 :
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "deterioration";
				break;
			case 19 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "deteriorationDetails";
				break;
			case 20 :
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "dechetPartic";
				break;
			case 21 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "volumeTraiteDechetPartic";
				break;
			case 22 :
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "dechetPro";
				break;
			case 23 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "volumeTraiteDechetProDib";
				break;
			case 24 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "volumeTraiteDechetProBois";
				break;
			case 25 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "volumeTraiteDechetProFerraille";
				break;
			case 26 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "volumeTraiteDechetProPapier";
				break;
			case 27 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "volumeTraiteDechetProAutres";
				break;
			case 28 :
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "bennes";
				break;
			case 29 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "volumeTraiteBennesDib";
				break;
			case 30 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "volumeTraiteBennesBois";
				break;
			case 31 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "volumeTraiteBennesFerraille";
				break;
			case 32 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "volumeTraiteBennesPapier";
				break;
			case 33 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "volumeTraiteBennesAutres";
				break;
			case 34 :
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "depotVente";
				break;
			case 35 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "volumeTraiteDepotVente";
				break;
			case 36 :
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "association";
				break;
			case 37 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "volumeTraiteAssociation";
				break;
			case 38 :
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "ramene";
				break;
			case 39 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "volumeTraiteRamene";
				break;
			case 40 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "volumeCamionRamene";
				break;
			case 41 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "volumeCamionFerraille";
				break;
			case 42 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "volumeCamionPapier";
				break;
			case 43 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "volumeCamionDecheterie";
				break;
			case 44 :
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "menage";
				break;
			case 45 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "menageTemps";
				break;
			case 46 :
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "respectConsignes";
				break;
			case 47 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "respectConsignesDetails";
				break;
			case 48 :
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "reglementRecuOuIndemnisation";
				break;
			case 49 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "montantRecuOuIndemnise";
				break;
			case 50 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "detailsReglement";
				break;
			case 51 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "remarques";
				break;
			case 52 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "heureArriveeChantier";
				break;
			case 53 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "heureDepartChantier";
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
				fkIntervention = Integer.parseInt( value.toString() );
				break;

			case 2 :
				fkUser = Integer.parseInt( value.toString() );
				break;

			case 3 :
				dateSaisie = (Calendar) value;
				break;

			case 4 :
				dateRapport = (Calendar) value;
				break;

			case 5 :
				listePhotosAvant = (List<CRMPhoneRapportChantierPhotoDTO>) value;
				break;

			case 6 :
				listePhotosRue = (List<CRMPhoneRapportChantierPhotoDTO>) value;
				break;

			case 7 :
				listePhotosApres = (List<CRMPhoneRapportChantierPhotoDTO>) value;
				break;

			case 8 :
				controleKit = Boolean.parseBoolean( value.toString() );
				break;

			case 9 :
				devisReajuste = Boolean.parseBoolean( value.toString() );
				break;

			case 10 :
				devisReajusteVolume = new BigDecimal( value.toString() );
				break;

			case 11 :
				devisReajusteValeur = new BigDecimal( value.toString() );
				break;

			case 12 :
				devisReajusteTotal = new BigDecimal( value.toString() );
				break;

			case 13 :
				fkChefDeChantier = Integer.parseInt( value.toString() );
				break;

			case 14 :
				listeEquipe = (List<CRMPhoneRapportChantierEquipeDTO>) value;
				break;

			case 15 :
				retard = Boolean.parseBoolean( value.toString() );
				break;

			case 16 :
				retardClientPrevenu = Boolean.parseBoolean( value.toString() );
				break;

			case 17 :
				retardDetails = value.toString();
				break;

			case 18 :
				deterioration = Boolean.parseBoolean( value.toString() );
				break;

			case 19 :
				deteriorationDetails = value.toString();
				break;

			case 20 :
				dechetPartic = Boolean.parseBoolean( value.toString() );
				break;

			case 21 :
				volumeTraiteDechetPartic = new BigDecimal( value.toString() );
				break;

			case 22 :
				dechetPro = Boolean.parseBoolean( value.toString() );
				break;

			case 23 :
				volumeTraiteDechetProDib = new BigDecimal( value.toString() );
				break;

			case 24 :
				volumeTraiteDechetProBois = new BigDecimal( value.toString() );
				break;

			case 25 :
				volumeTraiteDechetProFerraille = new BigDecimal( value.toString() );
				break;

			case 26 :
				volumeTraiteDechetProPapier = new BigDecimal( value.toString() );
				break;

			case 27 :
				volumeTraiteDechetProAutres = new BigDecimal( value.toString() );
				break;

			case 28 :
				bennes = Boolean.parseBoolean( value.toString() );
				break;

			case 29 :
				volumeTraiteBennesDib = new BigDecimal( value.toString() );
				break;

			case 30 :
				volumeTraiteBennesBois = new BigDecimal( value.toString() );
				break;

			case 31 :
				volumeTraiteBennesFerraille = new BigDecimal( value.toString() );
				break;

			case 32 :
				volumeTraiteBennesPapier = new BigDecimal( value.toString() );
				break;

			case 33 :
				volumeTraiteBennesAutres = new BigDecimal( value.toString() );
				break;

			case 34 :
				depotVente = Boolean.parseBoolean( value.toString() );
				break;

			case 35 :
				volumeTraiteDepotVente = new BigDecimal( value.toString() );
				break;

			case 36 :
				association = Boolean.parseBoolean( value.toString() );
				break;

			case 37 :
				volumeTraiteAssociation = new BigDecimal( value.toString() );
				break;

			case 38 :
				ramene = Boolean.parseBoolean( value.toString() );
				break;

			case 39 :
				volumeTraiteRamene = new BigDecimal( value.toString() );
				break;

			case 40 :
				volumeCamionRamene = new BigDecimal( value.toString() );
				break;

			case 41 :
				volumeCamionFerraille = new BigDecimal( value.toString() );
				break;

			case 42 :
				volumeCamionPapier = new BigDecimal( value.toString() );
				break;

			case 43 :
				volumeCamionDecheterie = new BigDecimal( value.toString() );
				break;

			case 44 :
				menage = Boolean.parseBoolean( value.toString() );
				break;

			case 45 :
				menageTemps = value.toString();
				break;

			case 46 :
				respectConsignes = Boolean.parseBoolean( value.toString() );
				break;

			case 47 :
				respectConsignesDetails = value.toString();
				break;

			case 48 :
				reglementRecuOuIndemnisation = Boolean.parseBoolean( value.toString() );
				break;

			case 49 :
				montantRecuOuIndemnise = new BigDecimal( value.toString() );
				break;

			case 50 :
				detailsReglement = value.toString();
				break;

			case 51 :
				remarques = value.toString();
				break;

			case 52 :
				heureArriveeChantier = value.toString();
				break;

			case 53 :
				heureDepartChantier = value.toString();
				break;
		}
	}

	public void setProperty( String name, Object value ) {
		if ( null != value && !value.toString().equals( "anyType{}" ) ) {
			switch ( name ) {
				case "id" :
					this.id = Integer.parseInt( value.toString() );
					break;
				case "fkIntervention" :
					this.fkIntervention = Integer.parseInt( value.toString() );
					break;
				case "fkUser" :
					this.fkUser = Integer.parseInt( value.toString() );
					break;
				case "dateSaisie" :
					this.dateSaisie = DateUtils.stringToCalendar( value.toString(), DATE_TIME );
					break;
				case "dateRapport" :
					this.dateRapport = DateUtils.stringToCalendar( value.toString(), DATE_TIME );
					break;
				case "listePhotosAvant" :
					this.listePhotosAvant.add( new RapportChantierPhotosMapper().map( (SoapObject) value ) );
					break;
				case "listePhotosRue" :
					this.listePhotosRue.add( new RapportChantierPhotosMapper().map( (SoapObject) value ) );
					break;
				case "listePhotosApres" :
					this.listePhotosApres.add( new RapportChantierPhotosMapper().map( (SoapObject) value ) );
					break;
				case "controleKit" :
					this.controleKit = Boolean.parseBoolean( value.toString() );
					break;
				case "devisReajuste" :
					this.devisReajuste = Boolean.parseBoolean( value.toString() );
					break;
				case "devisReajusteVolume" :
					this.devisReajusteVolume = new BigDecimal( value.toString() );
					break;
				case "devisReajusteValeur" :
					this.devisReajusteValeur = new BigDecimal( value.toString() );
					break;
				case "devisReajusteTotal" :
					this.devisReajusteTotal = new BigDecimal( value.toString() );
					break;
				case "fkChefDeChantier" :
					this.fkChefDeChantier = Integer.parseInt( value.toString() );
					break;
				case "listeEquipe" :
					this.listeEquipe.add( new RapportChantierEquipeMapper().map( (SoapObject) value ) );
					break;
				case "retard" :
					this.retard = Boolean.parseBoolean( value.toString() );
					break;
				case "retardClientPrevenu" :
					this.retardClientPrevenu = Boolean.parseBoolean( value.toString() );
					break;
				case "retardDetails" :
					this.retardDetails = value.toString();
					break;
				case "deterioration" :
					this.deterioration = Boolean.parseBoolean( value.toString() );
					break;
				case "deteriorationDetails" :
					this.deteriorationDetails = value.toString();
					break;
				case "dechetPartic" :
					this.dechetPartic = Boolean.parseBoolean( value.toString() );
					break;
				case "volumeTraiteDechetPartic" :
					this.volumeTraiteDechetPartic = new BigDecimal( value.toString() );
					break;
				case "dechetPro" :
					this.dechetPro = Boolean.parseBoolean( value.toString() );
					break;
				case "volumeTraiteDechetProDib" :
					this.volumeTraiteDechetProDib = new BigDecimal( value.toString() );
					break;
				case "volumeTraiteDechetProBois" :
					this.volumeTraiteDechetProBois = new BigDecimal( value.toString() );
					break;
				case "volumeTraiteDechetProFerraille" :
					this.volumeTraiteDechetProFerraille = new BigDecimal( value.toString() );
					break;
				case "volumeTraiteDechetProPapier" :
					this.volumeTraiteDechetProPapier = new BigDecimal( value.toString() );
					break;
				case "volumeTraiteDechetProAutres" :
					this.volumeTraiteDechetProAutres = new BigDecimal( value.toString() );
					break;
				case "bennes" :
					this.bennes = Boolean.parseBoolean( value.toString() );
					break;
				case "volumeTraiteBennesDib" :
					this.volumeTraiteBennesDib = new BigDecimal( value.toString() );
					break;
				case "volumeTraiteBennesBois" :
					this.volumeTraiteBennesBois = new BigDecimal( value.toString() );
					break;
				case "volumeTraiteBennesFerraille" :
					this.volumeTraiteBennesFerraille = new BigDecimal( value.toString() );
					break;
				case "volumeTraiteBennesPapier" :
					this.volumeTraiteBennesPapier = new BigDecimal( value.toString() );
					break;
				case "volumeTraiteBennesAutres" :
					this.volumeTraiteBennesAutres = new BigDecimal( value.toString() );
					break;
				case "depotVente" :
					this.depotVente = Boolean.parseBoolean( value.toString() );
					break;
				case "volumeTraiteDepotVente" :
					this.volumeTraiteDepotVente = new BigDecimal( value.toString() );
					break;
				case "association" :
					this.association = Boolean.parseBoolean( value.toString() );
					break;
				case "volumeTraiteAssociation" :
					this.volumeTraiteAssociation = new BigDecimal( value.toString() );
					break;
				case "ramene" :
					this.ramene = Boolean.parseBoolean( value.toString() );
					break;
				case "volumeTraiteRamene" :
					this.volumeTraiteRamene = new BigDecimal( value.toString() );
					break;
				case "volumeCamionRamene" :
					this.volumeCamionRamene = new BigDecimal( value.toString() );
					break;
				case "volumeCamionFerraille" :
					this.volumeCamionFerraille = new BigDecimal( value.toString() );
					break;
				case "volumeCamionPapier" :
					this.volumeCamionPapier = new BigDecimal( value.toString() );
					break;
				case "volumeCamionDecheterie" :
					this.volumeCamionDecheterie = new BigDecimal( value.toString() );
					break;
				case "menage" :
					this.menage = Boolean.parseBoolean( value.toString() );
					break;
				case "menageTemps" :
					this.menageTemps = value.toString();
					break;
				case "respectConsignes" :
					this.respectConsignes = Boolean.parseBoolean( value.toString() );
					break;
				case "respectConsignesDetails" :
					this.respectConsignesDetails = value.toString();
					break;
				case "reglementRecuOuIndemnisation" :
					this.reglementRecuOuIndemnisation = Boolean.parseBoolean( value.toString() );
					break;
				case "montantRecuOuIndemnise" :
					this.montantRecuOuIndemnise = new BigDecimal( value.toString() );
					break;
				case "detailReglement" :
					this.detailsReglement = value.toString();
					break;
				case "remarques" :
					this.remarques = value.toString();
					break;
				case "heureArriveeChantier" :
					this.heureArriveeChantier = value.toString();
					break;
				case "heureDepartChantier" :
					this.heureDepartChantier = value.toString();
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

	public Integer getFkIntervention() {
		return fkIntervention;
	}

	public void setFkIntervention( Integer fkIntervention ) {
		this.fkIntervention = fkIntervention;
	}

	public Calendar getDateRapport() {
		return dateRapport;
	}

	public void setDateRapport( Calendar dateRapport ) {
		this.dateRapport = dateRapport;
	}

	public Calendar getDateSaisie() {
		return dateSaisie;
	}

	public void setDateSaisie( Calendar dateSaisie ) {
		this.dateSaisie = dateSaisie;
	}

	public Integer getFkUser() {
		return fkUser;
	}

	public void setFkUser( Integer fkUser ) {
		this.fkUser = fkUser;
	}

	public List<CRMPhoneRapportChantierPhotoDTO> getListePhotosAvant() {
		return listePhotosAvant;
	}

	public void setListePhotosAvant( List<CRMPhoneRapportChantierPhotoDTO> listePhotosAvant ) {
		this.listePhotosAvant = listePhotosAvant;
	}

	public List<CRMPhoneRapportChantierPhotoDTO> getListePhotosRue() {
		return listePhotosRue;
	}

	public void setListePhotosRue( List<CRMPhoneRapportChantierPhotoDTO> listePhotosRue ) {
		this.listePhotosRue = listePhotosRue;
	}

	public List<CRMPhoneRapportChantierPhotoDTO> getListePhotosApres() {
		return listePhotosApres;
	}

	public void setListePhotosApres( List<CRMPhoneRapportChantierPhotoDTO> listePhotosApres ) {
		this.listePhotosApres = listePhotosApres;
	}

	public Boolean isControleKit() {
		return controleKit;
	}

	public void setControleKit( Boolean controleKit ) {
		this.controleKit = controleKit;
	}

	public Boolean isDevisReajuste() {
		return devisReajuste;
	}

	public void setDevisReajuste( Boolean devisReajuste ) {
		this.devisReajuste = devisReajuste;
	}

	public BigDecimal getDevisReajusteVolume() {
		return devisReajusteVolume;
	}

	public void setDevisReajusteVolume( BigDecimal devisReajusteVolume ) {
		this.devisReajusteVolume = devisReajusteVolume;
	}

	public BigDecimal getDevisReajusteValeur() {
		return devisReajusteValeur;
	}

	public void setDevisReajusteValeur( BigDecimal devisReajusteValeur ) {
		this.devisReajusteValeur = devisReajusteValeur;
	}

	public BigDecimal getDevisReajusteTotal() {
		return devisReajusteTotal;
	}

	public void setDevisReajusteTotal( BigDecimal devisReajusteTotal ) {
		this.devisReajusteTotal = devisReajusteTotal;
	}

	public Integer getFkChefDeChantier() {
		return fkChefDeChantier;
	}

	public void setFkChefDeChantier( Integer fkChefDeChantier ) {
		this.fkChefDeChantier = fkChefDeChantier;
	}

	public List<CRMPhoneRapportChantierEquipeDTO> getListeEquipe() {
		return listeEquipe;
	}

	public void setListeEquipe( List<CRMPhoneRapportChantierEquipeDTO> listeEquipe ) {
		this.listeEquipe = listeEquipe;
	}

	public Boolean isRetard() {
		return retard;
	}

	public void setRetard( Boolean retard ) {
		this.retard = retard;
	}

	public Boolean isRetardClientPrevenu() {
		return retardClientPrevenu;
	}

	public void setRetardClientPrevenu( Boolean retardClientPrevenu ) {
		this.retardClientPrevenu = retardClientPrevenu;
	}

	public String getRetardDetails() {
		return retardDetails;
	}

	public void setRetardDetails( String retardDetails ) {
		this.retardDetails = retardDetails;
	}

	public Boolean isDeterioration() {
		return deterioration;
	}

	public void setDeterioration( Boolean deterioration ) {
		this.deterioration = deterioration;
	}

	public String getDeteriorationDetails() {
		return deteriorationDetails;
	}

	public void setDeteriorationDetails( String deteriorationDetails ) {
		this.deteriorationDetails = deteriorationDetails;
	}

	public Boolean isDechetPartic() {
		return dechetPartic;
	}

	public void setDechetPartic( Boolean dechetPartic ) {
		this.dechetPartic = dechetPartic;
	}

	public BigDecimal getVolumeTraiteDechetPartic() {
		return volumeTraiteDechetPartic;
	}

	public void setVolumeTraiteDechetPartic( BigDecimal volumeTraiteDechetPartic ) {
		this.volumeTraiteDechetPartic = volumeTraiteDechetPartic;
	}

	public Boolean isDechetPro() {
		return dechetPro;
	}

	public void setDechetPro( Boolean dechetPro ) {
		this.dechetPro = dechetPro;
	}

	public BigDecimal getVolumeTraiteDechetProDib() {
		return volumeTraiteDechetProDib;
	}

	public void setVolumeTraiteDechetProDib( BigDecimal volumeTraiteDechetProDib ) {
		this.volumeTraiteDechetProDib = volumeTraiteDechetProDib;
	}

	public BigDecimal getVolumeTraiteDechetProBois() {
		return volumeTraiteDechetProBois;
	}

	public void setVolumeTraiteDechetProBois( BigDecimal volumeTraiteDechetProBois ) {
		this.volumeTraiteDechetProBois = volumeTraiteDechetProBois;
	}

	public BigDecimal getVolumeTraiteDechetProFerraille() {
		return volumeTraiteDechetProFerraille;
	}

	public void setVolumeTraiteDechetProFerraille( BigDecimal volumeTraiteDechetProFerraille ) {
		this.volumeTraiteDechetProFerraille = volumeTraiteDechetProFerraille;
	}

	public BigDecimal getVolumeTraiteDechetProPapier() {
		return volumeTraiteDechetProPapier;
	}

	public void setVolumeTraiteDechetProPapier( BigDecimal volumeTraiteDechetProPapier ) {
		this.volumeTraiteDechetProPapier = volumeTraiteDechetProPapier;
	}

	public BigDecimal getVolumeTraiteDechetProAutres() {
		return volumeTraiteDechetProAutres;
	}

	public void setVolumeTraiteDechetProAutres( BigDecimal volumeTraiteDechetProAutres ) {
		this.volumeTraiteDechetProAutres = volumeTraiteDechetProAutres;
	}

	public Boolean isBennes() {
		return bennes;
	}

	public void setBennes( Boolean bennes ) {
		this.bennes = bennes;
	}

	public BigDecimal getVolumeTraiteBennesDib() {
		return volumeTraiteBennesDib;
	}

	public void setVolumeTraiteBennesDib( BigDecimal volumeTraiteBennesDib ) {
		this.volumeTraiteBennesDib = volumeTraiteBennesDib;
	}

	public BigDecimal getVolumeTraiteBennesBois() {
		return volumeTraiteBennesBois;
	}

	public void setVolumeTraiteBennesBois( BigDecimal volumeTraiteBennesBois ) {
		this.volumeTraiteBennesBois = volumeTraiteBennesBois;
	}

	public BigDecimal getVolumeTraiteBennesFerraille() {
		return volumeTraiteBennesFerraille;
	}

	public void setVolumeTraiteBennesFerraille( BigDecimal volumeTraiteBennesFerraille ) {
		this.volumeTraiteBennesFerraille = volumeTraiteBennesFerraille;
	}

	public BigDecimal getVolumeTraiteBennesPapier() {
		return volumeTraiteBennesPapier;
	}

	public void setVolumeTraiteBennesPapier( BigDecimal volumeTraiteBennesPapier ) {
		this.volumeTraiteBennesPapier = volumeTraiteBennesPapier;
	}

	public BigDecimal getVolumeTraiteBennesAutres() {
		return volumeTraiteBennesAutres;
	}

	public void setVolumeTraiteBennesAutres( BigDecimal volumeTraiteBennesAutres ) {
		this.volumeTraiteBennesAutres = volumeTraiteBennesAutres;
	}

	public Boolean isDepotVente() {
		return depotVente;
	}

	public void setDepotVente( Boolean depotVente ) {
		this.depotVente = depotVente;
	}

	public BigDecimal getVolumeTraiteDepotVente() {
		return volumeTraiteDepotVente;
	}

	public void setVolumeTraiteDepotVente( BigDecimal volumeTraiteDepotVente ) {
		this.volumeTraiteDepotVente = volumeTraiteDepotVente;
	}

	public Boolean isAssociation() {
		return association;
	}

	public void setAssociation( Boolean association ) {
		this.association = association;
	}

	public BigDecimal getVolumeTraiteAssociation() {
		return volumeTraiteAssociation;
	}

	public void setVolumeTraiteAssociation( BigDecimal volumeTraiteAssociation ) {
		this.volumeTraiteAssociation = volumeTraiteAssociation;
	}

	public Boolean isRamene() {
		return ramene;
	}

	public void setRamene( Boolean ramene ) {
		this.ramene = ramene;
	}

	public BigDecimal getVolumeTraiteRamene() {
		return volumeTraiteRamene;
	}

	public void setVolumeTraiteRamene( BigDecimal volumeTraiteRamene ) {
		this.volumeTraiteRamene = volumeTraiteRamene;
	}

	public BigDecimal getVolumeCamionRamene() {
		return volumeCamionRamene;
	}

	public void setVolumeCamionRamene( BigDecimal volumeCamionRamene ) {
		this.volumeCamionRamene = volumeCamionRamene;
	}

	public BigDecimal getVolumeCamionFerraille() {
		return volumeCamionFerraille;
	}

	public void setVolumeCamionFerraille( BigDecimal volumeCamionFerraille ) {
		this.volumeCamionFerraille = volumeCamionFerraille;
	}

	public BigDecimal getVolumeCamionPapier() {
		return volumeCamionPapier;
	}

	public void setVolumeCamionPapier( BigDecimal volumeCamionPapier ) {
		this.volumeCamionPapier = volumeCamionPapier;
	}

	public BigDecimal getVolumeCamionDecheterie() {
		return volumeCamionDecheterie;
	}

	public void setVolumeCamionDecheterie( BigDecimal volumeCamionDecheterie ) {
		this.volumeCamionDecheterie = volumeCamionDecheterie;
	}

	public Boolean isMenage() {
		return menage;
	}

	public void setMenage( Boolean menage ) {
		this.menage = menage;
	}

	public String getMenageTemps() {
		return menageTemps;
	}

	public void setMenageTemps( String menageTemps ) {
		this.menageTemps = menageTemps;
	}

	public Boolean isRespectConsignes() {
		return respectConsignes;
	}

	public void setRespectConsignes( Boolean respectConsignes ) {
		this.respectConsignes = respectConsignes;
	}

	public String getRespectConsignesDetails() {
		return respectConsignesDetails;
	}

	public void setRespectConsignesDetails( String respectConsignesDetails ) {
		this.respectConsignesDetails = respectConsignesDetails;
	}

	public Boolean isReglementRecuOuIndemnisation() {
		return reglementRecuOuIndemnisation;
	}

	public void setReglementRecuOuIndemnisation( Boolean reglementRecuOuIndemnisation ) {
		this.reglementRecuOuIndemnisation = reglementRecuOuIndemnisation;
	}

	public BigDecimal getMontantRecuOuIndemnise() {
		return montantRecuOuIndemnise;
	}

	public void setMontantRecuOuIndemnise( BigDecimal montantRecuOuIndemnise ) {
		this.montantRecuOuIndemnise = montantRecuOuIndemnise;
	}

	public String getDetailsReglement() {
		return detailsReglement;
	}

	public void setDetailsReglement( String detailsReglement ) {
		this.detailsReglement = detailsReglement;
	}

	public String getRemarques() {
		return remarques;
	}

	public void setRemarques( String remarques ) {
		this.remarques = remarques;
	}

	public String getHeureArriveeChantier() {
		return heureArriveeChantier;
	}

	public void setHeureArriveeChantier( String heureArriveeChantier ) {
		this.heureArriveeChantier = heureArriveeChantier;
	}

	public String getHeureDepartChantier() {
		return heureDepartChantier;
	}

	public void setHeureDepartChantier( String heureDepartChantier ) {
		this.heureDepartChantier = heureDepartChantier;
	}

	public Map<String, String> getMapPhotosThumbnailsSource() {
		return mapPhotosThumbnailsSource;
	}

	public void setMapPhotosThumbnailsSource( Map<String, String> mapPhotosThumbnailsSource ) {
		this.mapPhotosThumbnailsSource = mapPhotosThumbnailsSource;
	}
}