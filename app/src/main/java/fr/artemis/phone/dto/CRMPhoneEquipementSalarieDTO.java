package fr.artemis.phone.dto;

import java.util.Hashtable;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.webservices.mapper.impl.EquipementMapper;
import fr.artemis.phone.webservices.mapper.impl.EquipementTailleMapper;
import fr.artemis.phone.webservices.mapper.impl.SalarieMapper;

public class CRMPhoneEquipementSalarieDTO implements KvmSerializable {

	private Integer id;
	private SalarieLightDTO salarie;
	private CRMPhoneEquipementTailleDTO taille;
	private CRMPhoneEquipementDTO equipement;
	private Integer quantitePossedee;
	private Integer quantiteDemandee;
	private Boolean demandeCloturee;

	public Object getProperty( int arg0 ) {

		switch ( arg0 ) {
			case 0:
				return id;
			case 1:
				return salarie;
			case 2:
				return taille;
			case 3:
				return equipement;
			case 4:
				return quantitePossedee;
			case 5:
				return quantiteDemandee;
			case 6:
				return demandeCloturee;
		}

		return null;
	}

	public int getPropertyCount() {
		return 7;
	}

	public void getPropertyInfo( int index, Hashtable arg1, PropertyInfo info ) {
		switch ( index ) {
			case 0:
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "id";
				break;
			case 1:
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "salarie";
				break;
			case 2:
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "taille";
				break;
			case 3:
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "equipement";
				break;
			case 4:
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "quantitePossedee";
				break;
			case 5:
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "quantiteDemandee";
				break;
			case 6:
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "demandeCloturee";
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
				salarie = (SalarieLightDTO) value;
				break;
			case 2:
				taille = (CRMPhoneEquipementTailleDTO) value;
				break;
			case 3:
				equipement = (CRMPhoneEquipementDTO) value;
				break;
			case 4:
				quantitePossedee = Integer.parseInt( value.toString() );
				break;
			case 5:
				quantiteDemandee = Integer.parseInt( value.toString() );
				break;
			case 6:
				demandeCloturee = Boolean.parseBoolean( value.toString() );
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
				case "salarie":
					this.salarie = new SalarieMapper().map( (SoapObject) value );
					break;
				case "taille":
					this.taille = new EquipementTailleMapper().map( (SoapObject) value );
					break;
				case "equipement":
					this.equipement = new EquipementMapper().map( (SoapObject) value );
					break;
				case "quantitePossedee":
					this.quantitePossedee = Integer.parseInt( value.toString() );
					break;
				case "quantiteDemandee":
					this.quantiteDemandee = Integer.parseInt( value.toString() );
					break;
				case "demandeCloturee":
					this.demandeCloturee = Boolean.parseBoolean( value.toString() );
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

	public SalarieLightDTO getSalarie() {
		return salarie;
	}

	public void setSalarie( SalarieLightDTO salarie ) {
		this.salarie = salarie;
	}

	public CRMPhoneEquipementTailleDTO getTaille() {
		return taille;
	}

	public void setTaille( CRMPhoneEquipementTailleDTO taille ) {
		this.taille = taille;
	}

	public CRMPhoneEquipementDTO getEquipement() {
		return equipement;
	}

	public void setEquipement( CRMPhoneEquipementDTO equipement ) {
		this.equipement = equipement;
	}

	public Integer getQuantitePossedee() {
		return quantitePossedee;
	}

	public void setQuantitePossedee( Integer quantitePossedee ) {
		this.quantitePossedee = quantitePossedee;
	}

	public Integer getQuantiteDemandee() {
		return quantiteDemandee;
	}

	public void setQuantiteDemandee( Integer quantiteDemandee ) {
		this.quantiteDemandee = quantiteDemandee;
	}

	public Boolean getDemandeCloturee() {
		return demandeCloturee;
	}

	public void setDemandeCloturee( Boolean demandeCloturee ) {
		this.demandeCloturee = demandeCloturee;
	}
}