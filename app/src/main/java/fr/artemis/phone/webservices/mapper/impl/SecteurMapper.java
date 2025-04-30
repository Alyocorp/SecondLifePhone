package fr.artemis.phone.webservices.mapper.impl;

import fr.artemis.phone.dto.SecteurDTO;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.webservices.mapper.AbstractDefaultMapper;

/**
 * CLasse de mapping d'un secteur
 */
public class SecteurMapper extends AbstractDefaultMapper<SecteurDTO> {

	@Override
	public SecteurDTO map( SoapObject objectToMap ) {
		SecteurDTO dto = null;

		if ( null != objectToMap && objectToMap.getPropertyCount() > 0 ) {
			dto = new SecteurDTO();

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