package fr.artemis.phone.lib.map.geojson;

import com.google.android.gms.maps.GoogleMap;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import fr.artemis.phone.lib.map.FeatureArtemis;
import fr.artemis.phone.lib.map.RendererArtemis;

public class GeoJsonRendererArtemis extends RendererArtemis implements Observer {

	private final static Object FEATURE_NOT_ON_MAP = null;

	/**
	 * Creates a new GeoJsonRender object
	 *
	 * @param map      map to place GeoJsonFeature objects on
	 * @param features contains a hashmap of features and objects that will go on the map
	 */
	public GeoJsonRendererArtemis( GoogleMap map, HashMap<GeoJsonFeatureArtemis, Object> features ) {
		super( map, features );

	}

	/**
	 * Changes the map that GeoJsonFeature objects are being drawn onto. Existing objects are
	 * removed from the previous map and drawn onto the new map.
	 *
	 * @param map GoogleMap to place GeoJsonFeature objects on
	 */
	public void setMap( GoogleMap map ) {
		super.setMap( map );
		for ( FeatureArtemis feature : super.getFeatures() ) {
			redrawFeatureToMap( (GeoJsonFeatureArtemis) feature, map );
		}
	}

	/**
	 * Adds all of the stored features in the layer onto the map if the layer is
	 * not already on the map.
	 */
	public void addLayerToMap() {
		if ( !isLayerOnMap() ) {
			setLayerVisibility( true );
			for ( FeatureArtemis feature : super.getFeatures() ) {
				addFeature( (GeoJsonFeatureArtemis) feature );
			}
		}
	}

	/**
	 * Adds a new GeoJsonFeature to the map if its geometry property is not null.
	 *
	 * @param feature feature to add to the map
	 */
	public void addFeature( GeoJsonFeatureArtemis feature ) {
		super.addFeature( feature );
		if ( isLayerOnMap() ) {
			feature.addObserver( this );
		}
	}

	/**
	 * Removes all GeoJsonFeature objects stored in the mFeatures hashmap from the map
	 */
	public void removeLayerFromMap() {
		if ( isLayerOnMap() ) {
			for ( FeatureArtemis feature : super.getFeatures() ) {
				removeFromMap( super.getAllFeatures().get( feature ) );
				feature.deleteObserver( this );
			}
			setLayerVisibility( false );
		}
	}

	/**
	 * Removes a GeoJsonFeature from the map if its geometry property is not null
	 *
	 * @param feature feature to remove from map
	 */
	public void removeFeature( GeoJsonFeatureArtemis feature ) {
		// Check if given feature is stored
		super.removeFeature( feature );
		if ( super.getFeatures().contains( feature ) ) {
			feature.deleteObserver( this );
		}
	}

	/**
	 * Redraws a given GeoJsonFeature onto the map. The map object is obtained from the mFeatures
	 * hashmap and it is removed and added.
	 *
	 * @param feature feature to redraw onto the map
	 */
	private void redrawFeatureToMap( GeoJsonFeatureArtemis feature ) {
		redrawFeatureToMap( feature, getMap() );
	}

	private void redrawFeatureToMap( GeoJsonFeatureArtemis feature, GoogleMap map ) {
		removeFromMap( getAllFeatures().get( feature ) );
		putFeatures( feature, FEATURE_NOT_ON_MAP );
		if ( map != null && feature.hasGeometry() ) {
			putFeatures( feature, addGeoJsonFeatureToMap( feature, feature.getGeometry() ) );
		}
	}

	/**
	 * Update is called if the developer sets a style or geometry in a GeoJsonFeature object
	 *
	 * @param observable GeoJsonFeature object
	 * @param data       null, no extra argument is passed through the notifyObservers method
	 */
	public void update( Observable observable, Object data ) {
		if ( observable instanceof GeoJsonFeatureArtemis ) {
			GeoJsonFeatureArtemis feature = ( (GeoJsonFeatureArtemis) observable );
			boolean featureIsOnMap = getAllFeatures().get( feature ) != FEATURE_NOT_ON_MAP;
			if ( featureIsOnMap && feature.hasGeometry() ) {
				// Checks if the feature has been added to the map and its geometry is not null
				// TODO: change this so that we don't add and remove
				redrawFeatureToMap( feature );
			} else if ( featureIsOnMap && !feature.hasGeometry() ) {
				// Checks if feature is on map and geometry is null
				removeFromMap( getAllFeatures().get( feature ) );
				putFeatures( feature, FEATURE_NOT_ON_MAP );
			} else if ( !featureIsOnMap && feature.hasGeometry() ) {
				// Checks if the feature isn't on the map and geometry is not null
				addFeature( feature );
			}
		}
	}
}