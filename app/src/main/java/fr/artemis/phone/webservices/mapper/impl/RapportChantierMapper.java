package fr.artemis.phone.webservices.mapper.impl;

import fr.artemis.phone.dto.CRMPhonePlanningDTO;
import fr.artemis.phone.dto.CRMPhoneRapportChantierDTO;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.webservices.mapper.AbstractDefaultMapper;

/**
 * Classe de mapping d'un rapport de chantier
 */
public class RapportChantierMapper extends AbstractDefaultMapper<CRMPhoneRapportChantierDTO> {

	@Override
	public CRMPhoneRapportChantierDTO map( SoapObject objectToMap ) {
		CRMPhoneRapportChantierDTO dto = null;

		if ( null != objectToMap && objectToMap.getPropertyCount() > 0 ) {
			dto = new CRMPhoneRapportChantierDTO();

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