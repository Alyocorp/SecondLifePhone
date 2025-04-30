package fr.artemis.phone.dto;

import java.math.BigDecimal;
import java.util.Hashtable;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.webservices.mapper.impl.EquipementMapper;
import fr.artemis.phone.webservices.mapper.impl.EquipementTailleMapper;
import fr.artemis.phone.webservices.mapper.impl.SalarieMapper;

public class CRMPhoneDecheteriesTarifsDTO implements KvmSerializable {

	private Integer id;

	private Integer fkDecheterie;

	private String matiere;

	private BigDecimal prixTonne;

	public Object getProperty( int arg0 ) {

		switch ( arg0 ) {
			case 0:
				return id;
			case 1:
				return fkDecheterie;
			case 2:
				return matiere;
			case 3:
				return prixTonne;

		}

		return null;
	}

	public int getPropertyCount() {
		return 4;
	}

	public void getPropertyInfo( int index, Hashtable arg1, PropertyInfo info ) {
		switch ( index ) {
			case 0:
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "id";
				break;
			case 1:
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "fkDecheterie";
				break;
			case 2:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "matiere";
				break;
			case 3:
				info.type = PropertyInfo.BIGDECIMAL_CLASS;
				info.name = "prixTonne";
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
				fkDecheterie = Integer.parseInt( value.toString() );
				break;
			case 2:
				matiere = value.toString();
				break;
			case 3:
				prixTonne = new BigDecimal( value.toString() );
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
				case "fkDecheterie":
					this.fkDecheterie = Integer.parseInt( value.toString() );
					break;
				case "matiere":
					this.matiere = value.toString();
					break;
				case "prixTonne":
					this.prixTonne = new BigDecimal( value.toString() );
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

	public Integer getFkDecheterie() {
		return fkDecheterie;
	}

	public void setFkDecheterie( Integer fkDecheterie ) {
		this.fkDecheterie = fkDecheterie;
	}

	public String getMatiere() {
		return matiere;
	}

	public void setMatiere( String matiere ) {
		this.matiere = matiere;
	}

	public BigDecimal getPrixTonne() {
		return prixTonne;
	}

	public void setPrixTonne( BigDecimal prixTonne ) {
		this.prixTonne = prixTonne;
	}
}