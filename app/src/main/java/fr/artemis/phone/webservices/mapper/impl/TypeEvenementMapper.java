package fr.artemis.phone.webservices.mapper.impl;

import fr.artemis.phone.dto.CRMPhonePlanningInterventionDTO;
import fr.artemis.phone.dto.PlanningTypeEvenementDTO;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.webservices.mapper.AbstractDefaultMapper;

/**
 * Classe de mapping d'un type d'evenement de planning (intervention / devis)
 */
public class TypeEvenementMapper extends AbstractDefaultMapper<PlanningTypeEvenementDTO> {

	@Override
	public PlanningTypeEvenementDTO map( SoapObject objectToMap ) {
		PlanningTypeEvenementDTO dto = null;

		if ( null != objectToMap && objectToMap.getPropertyCount() > 0 ) {
			dto = new PlanningTypeEvenementDTO();

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