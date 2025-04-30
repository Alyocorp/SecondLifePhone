package fr.artemis.phone.webservices.mapper.impl;

import fr.artemis.phone.dto.CRMPhoneRapportChantierEquipeDTO;
import fr.artemis.phone.dto.CRMPhoneRapportChantierPhotoDTO;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.webservices.mapper.AbstractDefaultMapper;

/**
 * Classe de mapping d'un membre d'equipe sur un chantier
 */
public class RapportChantierEquipeMapper extends AbstractDefaultMapper<CRMPhoneRapportChantierEquipeDTO> {

	@Override
	public CRMPhoneRapportChantierEquipeDTO map( SoapObject objectToMap ) {
		CRMPhoneRapportChantierEquipeDTO dto = null;

		if ( null != objectToMap && objectToMap.getPropertyCount() > 0 ) {
			dto = new CRMPhoneRapportChantierEquipeDTO();

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