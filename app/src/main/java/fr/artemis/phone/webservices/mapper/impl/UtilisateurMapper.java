package fr.artemis.phone.webservices.mapper.impl;

import fr.artemis.phone.dto.UtilisateurDTO;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.webservices.mapper.AbstractDefaultMapper;

/**
 * Classe de mapping d'un utilisateur
 */
public class UtilisateurMapper extends AbstractDefaultMapper<UtilisateurDTO> {

	@Override
	public UtilisateurDTO map( SoapObject objectToMap ) {
		UtilisateurDTO dto = null;

		if ( null != objectToMap && objectToMap.getPropertyCount() > 0 ) {
			dto = new UtilisateurDTO();

			PropertyInfo pi;
			for ( int index = 0 ; index < objectToMap.getPropertyCount() ; index++ ) {
				pi = new PropertyInfo();
				objectToMap.getPropertyInfo( index, null, pi );
				dto.setProperty( pi.name, objectToMap.getProperty( index ) );
			}
		}

		return dto;
	}
}