package fr.artemis.phone.dto;

import static fr.artemis.phone.lib.ksoap.kobjects.isodate.IsoDate.DATE_TIME;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.utils.DateUtils;

public class CRMPhonePlanningRapportDTO implements Serializable, KvmSerializable {

	private Integer id;
	private Integer fkDevis;
	private Integer fkPlanningInter;
	private List<Integer> listFkRapportsDeChantier = new ArrayList<>();
	private List<Integer> listFkChefEquipe = new ArrayList<>();
	private List<Integer> listFkMembreEquipe = new ArrayList<>();
	private Calendar dateRapport;
	private Boolean rapportComplet;
	private String codePostal;
	private String client;
	private String ville;
	private String numeroDevis;

	public Object getProperty( int arg0 ) {

		switch ( arg0 ) {
			case 0 :
				return id;
			case 1 :
				return fkDevis;
			case 2 :
				return fkPlanningInter;
			case 3 :
				return listFkRapportsDeChantier;
			case 4 :
				return listFkChefEquipe;
			case 5 :
				return listFkMembreEquipe;
			case 6 :
				return dateRapport;
			case 7 :
				return rapportComplet;
			case 8 :
				return codePostal;
			case 9 :
				return client;
			case 10 :
				return ville;
			case 11 :
				return numeroDevis;
		}

		return null;
	}

	public int getPropertyCount() {
		return 12;
	}

	public void getPropertyInfo( int index, Hashtable arg1, PropertyInfo info ) {
		switch ( index ) {
			case 0 :
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "id";
				break;
			case 1 :
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "fkDevis";
				break;
			case 2 :
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "fkPlanningInter";
				break;
			case 3 :
				info.type = PropertyInfo.VECTOR_CLASS;
				info.name = "listFkRapportsDeChantier";
				break;
			case 4 :
				info.type = PropertyInfo.VECTOR_CLASS;
				info.name = "listFkChefEquipe";
				break;
			case 5 :
				info.type = PropertyInfo.VECTOR_CLASS;
				info.name = "listFkMembreEquipe";
				break;
			case 6 :
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "dateRapport";
				break;
			case 7 :
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "rapportComplet";
				break;
			case 8 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "codePostal";
				break;
			case 9 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "client";
				break;
			case 10 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "ville";
				break;
			case 11 :
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "numeroDevis";
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
				fkDevis = Integer.parseInt( value.toString() );
				break;
			case 2 :
				fkPlanningInter = Integer.parseInt( value.toString() );
				break;
			case 3 :
				listFkRapportsDeChantier = (List<Integer>) value;
				break;
			case 4 :
				listFkChefEquipe = (List<Integer>) value;
				break;
			case 5 :
				listFkMembreEquipe = (List<Integer>) value;
				break;
			case 6 :
				dateRapport = (Calendar) value;
				break;
			case 7 :
				rapportComplet = Boolean.parseBoolean( value.toString() );
				break;
			case 8 :
				codePostal = value.toString();
				break;
			case 9 :
				client = value.toString();
				break;
			case 10 :
				ville = value.toString();
				break;
			case 11 :
				numeroDevis = value.toString();
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
				case "fkDevis" :
					this.fkDevis = Integer.parseInt( value.toString() );
					break;
				case "fkPlanningInter" :
					this.fkPlanningInter = Integer.parseInt( value.toString() );
					break;
				case "listFkRapportsDeChantier" :
					throw new IllegalStateException( "TODO list integer à mapper" );
//                    this.listFkRapportsDeChantier = (List<Integer>) value;
//                    break;
				case "listFkChefEquipe" :
					throw new IllegalStateException( "TODO list integer à mapper" );
//                    this.listFkChefEquipe = (List<Integer>) value;
//                    break;
				case "listFkMembreEquipe" :
					throw new IllegalStateException( "TODO list integer à mapper" );
//                    this.listFkMembreEquipe = (List<Integer>) value;
//                    break;
				case "dateRapport" :
					this.dateRapport = DateUtils.stringToCalendar( value.toString(), DATE_TIME );
					break;
				case "rapportComplet" :
					this.rapportComplet = Boolean.parseBoolean( value.toString() );
					break;
				case "codePostal" :
					this.codePostal = value.toString();
					break;
				case "client" :
					this.client = value.toString();
					break;
				case "ville" :
					this.ville = value.toString();
					break;
				case "numeroDevis" :
					this.numeroDevis = value.toString();
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

	public Integer getFkDevis() {
		return fkDevis;
	}

	public void setFkDevis( Integer fkDevis ) {
		this.fkDevis = fkDevis;
	}

	public Integer getFkPlanningInter() {
		return fkPlanningInter;
	}

	public void setFkPlanningInter( Integer fkPlanningInter ) {
		this.fkPlanningInter = fkPlanningInter;
	}

	public List<Integer> getListFkRapportsDeChantier() {
		return listFkRapportsDeChantier;
	}

	public void setListFkRapportsDeChantier( List<Integer> listFkRapportsDeChantier ) {
		this.listFkRapportsDeChantier = listFkRapportsDeChantier;
	}

	public List<Integer> getListFkChefEquipe() {
		return listFkChefEquipe;
	}

	public void setListFkChefEquipe( List<Integer> listFkChefEquipe ) {
		this.listFkChefEquipe = listFkChefEquipe;
	}

	public List<Integer> getListFkMembreEquipe() {
		return listFkMembreEquipe;
	}

	public void setListFkMembreEquipe( List<Integer> listFkMembreEquipe ) {
		this.listFkMembreEquipe = listFkMembreEquipe;
	}

	public Calendar getDateRapport() {
		return dateRapport;
	}

	public void setDateRapport( Calendar dateRapport ) {
		this.dateRapport = dateRapport;
	}

	public Boolean getRapportComplet() {
		return rapportComplet;
	}

	public void setRapportComplet( Boolean rapportComplet ) {
		this.rapportComplet = rapportComplet;
	}

	public String getCodePostal() {
		return codePostal;
	}

	public void setCodePostal( String codePostal ) {
		this.codePostal = codePostal;
	}

	public String getClient() {
		return client;
	}

	public void setClient( String client ) {
		this.client = client;
	}

	public String getVille() {
		return ville;
	}

	public void setVille( String ville ) {
		this.ville = ville;
	}

	public String getNumeroDevis() {
		return numeroDevis;
	}

	public void setNumeroDevis( String numeroDevis ) {
		this.numeroDevis = numeroDevis;
	}
}
