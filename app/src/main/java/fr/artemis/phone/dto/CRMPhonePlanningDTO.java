package fr.artemis.phone.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.webservices.mapper.impl.PlanningDevisMapper;
import fr.artemis.phone.webservices.mapper.impl.PlanningInterventionMapper;
import fr.artemis.phone.webservices.mapper.impl.SecteurMapper;
import fr.artemis.phone.webservices.mapper.impl.TypeEvenementMapper;

public class CRMPhonePlanningDTO implements Serializable, KvmSerializable {

	private static final long serialVersionUID = 1348477768969089956L;

	private Integer id;
	private List<CRMPhonePlanningDevisDTO> listeDevis = new ArrayList<>();
	private List<CRMPhonePlanningInterventionDTO> listeInterventions = new ArrayList<>();
	private SecteurDTO secteur;
	private PlanningTypeEvenementDTO typeEvenement;
	private String annee;
	private String numeroSemaine;
	private String label;
	private String labelCourt;

	public Object getProperty( int arg0 ) {

		switch ( arg0 ) {
			case 0:
				return id;
			case 1:
				return listeDevis;
			case 2:
				return listeInterventions;
			case 3:
				return secteur;
			case 4:
				return typeEvenement;
			case 5:
				return annee;
			case 6:
				return numeroSemaine;
			case 7:
				return label;
			case 8:
				return labelCourt;
		}

		return null;
	}

	public int getPropertyCount() {
		return 9;
	}

	public void getPropertyInfo( int index, Hashtable arg1, PropertyInfo info ) {
		switch ( index ) {
			case 0:
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "id";
				break;
			case 1:
				info.type = PropertyInfo.VECTOR_CLASS;
				info.name = "listeDevis";
				break;
			case 2:
				info.type = PropertyInfo.VECTOR_CLASS;
				info.name = "listeInterventions";
				break;
			case 3:
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "secteur";
				break;
			case 4:
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "typeEvenement";
				break;
			case 5:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "annee";
				break;
			case 6:
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "numeroSemaine";
				break;
			case 7:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "label";
				break;
			case 8:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "labelCourt";
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
				listeDevis = (List<CRMPhonePlanningDevisDTO>) value;
				break;
			case 2:
				listeInterventions = (List<CRMPhonePlanningInterventionDTO>) value;
				break;
			case 3:
				secteur = (SecteurDTO) value;
				break;
			case 4:
				typeEvenement = (PlanningTypeEvenementDTO) value;
				break;
			case 5:
				annee = value.toString();
				break;
			case 6:
				numeroSemaine = value.toString();
				break;
			case 7:
				label = value.toString();
				break;
			case 8:
				labelCourt = value.toString();
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
				case "listeDevis":
					this.listeDevis.add( new PlanningDevisMapper().map( (SoapObject) value ) );
					break;
				case "listeInterventions":
					this.listeInterventions.add( new PlanningInterventionMapper().map( (SoapObject) value ) );
					break;
				case "secteur":
					this.secteur = new SecteurMapper().map( (SoapObject) value );
					break;
				case "typeEvenement":
					this.typeEvenement = new TypeEvenementMapper().map( (SoapObject) value );
					break;
				case "annee":
					this.annee = value.toString();
					break;
				case "numeroSemaine":
					this.numeroSemaine = value.toString();
					break;
				case "label":
					this.label = value.toString();
					break;
				case "labelCourt":
					this.labelCourt = value.toString();
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

	public List<CRMPhonePlanningDevisDTO> getListeDevis() {
		return listeDevis;
	}

	public void setListeDevis( List<CRMPhonePlanningDevisDTO> listeDevis ) {
		this.listeDevis = listeDevis;
	}

	public List<CRMPhonePlanningInterventionDTO> getListeInterventions() {
		return listeInterventions;
	}

	public void setListeInterventions( List<CRMPhonePlanningInterventionDTO> listeInterventions ) {
		this.listeInterventions = listeInterventions;
	}

	public SecteurDTO getSecteur() {
		return secteur;
	}

	public void setSecteur( SecteurDTO secteur ) {
		this.secteur = secteur;
	}

	public PlanningTypeEvenementDTO getTypeEvenement() {
		return typeEvenement;
	}

	public void setTypeEvenement( PlanningTypeEvenementDTO typeEvenement ) {
		this.typeEvenement = typeEvenement;
	}

	public String getAnnee() {
		return annee;
	}

	public void setAnnee( String annee ) {
		this.annee = annee;
	}

	public String getNumeroSemaine() {
		return numeroSemaine;
	}

	public void setNumeroSemaine( String numeroSemaine ) {
		this.numeroSemaine = numeroSemaine;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel( String label ) {
		this.label = label;
	}

	public String getLabelCourt() {
		return labelCourt;
	}

	public void setLabelCourt( String labelCourt ) {
		this.labelCourt = labelCourt;
	}
}