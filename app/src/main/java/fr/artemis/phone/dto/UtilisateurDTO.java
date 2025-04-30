package fr.artemis.phone.dto;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class UtilisateurDTO implements KvmSerializable {

	private boolean actif;

	private Integer id;

	private String idSession;

	private String nomUtilisateur;

	private String password;

	private RoleUser role;

	public Object getProperty( int arg0 ) {

		switch ( arg0 ) {
			case 0:
				return actif;
			case 1:
				return id;
			case 2:
				return idSession;
			case 3:
				return nomUtilisateur;
			case 4:
				return password;
			case 5:
				return role;
		}

		return null;
	}

	public int getPropertyCount() {
		return 5;
	}

	public void getPropertyInfo( int index, Hashtable arg1, PropertyInfo info ) {
		switch ( index ) {
			case 0:
				info.type = PropertyInfo.BOOLEAN_CLASS;
				info.name = "actif";
				break;
			case 1:
				info.type = PropertyInfo.INTEGER_CLASS;
				info.name = "id";
				break;
			case 2:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "idSession";
				break;
			case 3:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "nomUtilisateur";
				break;
			case 4:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "password";
				break;
			case 5:
				info.type = PropertyInfo.OBJECT_CLASS;
				info.name = "role";
				break;
			default:
				break;
		}
	}

	public void setProperty( int index, Object value ) {
		switch ( index ) {
			case 0:
				actif = Boolean.parseBoolean( value.toString() );
				break;
			case 1:
				id = Integer.parseInt( value.toString() );
				break;
			case 2:
				idSession = value.toString();
				break;
			case 3:
				nomUtilisateur = value.toString();
				break;
			case 4:
				password = value.toString();
				break;
			case 5:
				role = RoleUser.valueOf( value.toString() );
				break;
			default:
				break;
		}
	}

	public void setProperty( String name, Object value ) {
		if ( null != value && !value.toString().equals( "anyType{}" ) ) {
			switch ( name ) {
				case "actif":
					actif = Boolean.parseBoolean( value.toString() );
					break;
				case "id":
					id = Integer.parseInt( value.toString() );
					break;
				case "idSession":
					idSession = value.toString();
					break;
				case "nomUtilisateur":
					nomUtilisateur = value.toString();
					break;
				case "password":
					password = value.toString();
					break;
				case "role":
					role = RoleUser.valueOf( value.toString() );
					break;
				default:
					break;
			}
		}
	}

	public boolean isActif() {
		return actif;
	}

	public void setActif( boolean actif ) {
		this.actif = actif;
	}

	public Integer getId() {
		return id;
	}

	public void setId( Integer id ) {
		this.id = id;
	}

	public String getIdSession() {
		return idSession;
	}

	public void setIdSession( String idSession ) {
		this.idSession = idSession;
	}

	public String getNomUtilisateur() {
		return nomUtilisateur;
	}

	public void setNomUtilisateur( String nomUtilisateur ) {
		this.nomUtilisateur = nomUtilisateur;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword( String password ) {
		this.password = password;
	}

	public RoleUser getRole() {
		return role;
	}

	public void setRole( RoleUser role ) {
		this.role = role;
	}
}