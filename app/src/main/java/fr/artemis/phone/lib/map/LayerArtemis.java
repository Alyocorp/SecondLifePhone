package fr.artemis.phone.lib.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import fr.artemis.phone.lib.map.geojson.GeoJsonLineStringStyleArtemis;
import fr.artemis.phone.lib.map.geojson.GeoJsonPointStyleArtemis;
import fr.artemis.phone.lib.map.geojson.GeoJsonPolygonStyleArtemis;
import fr.artemis.phone.lib.map.geojson.GeoJsonRendererArtemis;
import fr.artemis.phone.lib.map.kml.KmlContainerArtemis;
import fr.artemis.phone.lib.map.kml.KmlGroundOverlayArtemis;
import fr.artemis.phone.lib.map.kml.KmlRendererArtemis;

public abstract class LayerArtemis {

	private RendererArtemis mRenderer;

	/**
	 * Adds the KML data to the map
	 */
	protected void addKMLToMap() throws IOException, XmlPullParserException {
		if ( mRenderer instanceof KmlRendererArtemis ) {
			( (KmlRendererArtemis) mRenderer ).addLayerToMap();
		} else {
			throw new UnsupportedOperationException( "Stored renderer is not a KmlRendererArtemis" );
		}
	}

	/**
	 * Adds GeoJson data to the map
	 */
	protected void addGeoJsonToMap() {
		if ( mRenderer instanceof GeoJsonRendererArtemis ) {
			( (GeoJsonRendererArtemis) mRenderer ).addLayerToMap();
		} else {
			throw new UnsupportedOperationException( "Stored renderer is not a GeoJsonRenderer" );
		}
	}

	public abstract void addLayerToMap() throws IOException, XmlPullParserException;

	/**
	 * Removes all the data from the map and clears all the stored placemarks
	 */
	public void removeLayerFromMap() {
		if ( mRenderer instanceof GeoJsonRendererArtemis ) {
			( (GeoJsonRendererArtemis) mRenderer ).removeLayerFromMap();
		} else if ( mRenderer instanceof KmlRendererArtemis ) {
			( (KmlRendererArtemis) mRenderer ).removeLayerFromMap();
		}
	}

	/**
	 * Sets a single click listener for the entire GoogleMap object, that will be called
	 * with the corresponding Feature object when an object on the map (Polygon,
	 * Marker, Polyline) is clicked.
	 * <p>
	 * If getFeature() returns null this means that either the object is inside a KMLContainer,
	 * or the object is a MultiPolygon, MultiLineString or MultiPoint and must
	 * be handled differently.
	 *
	 * @param listener Listener providing the onFeatureClick method to call.
	 */
	public void setOnFeatureClickListener( final OnFeatureClickListener listener ) {

		GoogleMap map = getMap();

		map.setOnPolygonClickListener( new GoogleMap.OnPolygonClickListener() {
			@Override
			public void onPolygonClick( Polygon polygon ) {
				if ( getFeature( polygon ) != null ) {
					listener.onFeatureClick( getFeature( polygon ) );
				} else if ( getContainerFeature( polygon ) != null ) {
					listener.onFeatureClick( getContainerFeature( polygon ) );
				} else {
					listener.onFeatureClick( getFeature( multiObjectHandler( polygon ) ) );
				}
			}
		} );

		map.setOnMarkerClickListener( new GoogleMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick( Marker marker ) {
				if ( getFeature( marker ) != null ) {
					listener.onFeatureClick( getFeature( marker ) );
				} else if ( getContainerFeature( marker ) != null ) {
					marker.setDraggable( true );
					listener.onFeatureClick( getContainerFeature( marker ) );
				} else {
					listener.onFeatureClick( getFeature( multiObjectHandler( marker ) ) );
				}
				return false;
			}
		} );

		map.setOnPolylineClickListener( new GoogleMap.OnPolylineClickListener() {
			@Override
			public void onPolylineClick( Polyline polyline ) {
				if ( getFeature( polyline ) != null ) {
					listener.onFeatureClick( getFeature( polyline ) );
				} else if ( getContainerFeature( polyline ) != null ) {
					listener.onFeatureClick( getContainerFeature( polyline ) );
				} else {
					listener.onFeatureClick( getFeature( multiObjectHandler( polyline ) ) );
				}
			}
		} );
	}

	/**
	 * Called if the map object is a MultiPolygon, MultiLineString or a MultiPoint and returns
	 * the corresponding ArrayList containing the singular Polygons, LineStrings or Points
	 * respectively.
	 *
	 * @param mapObject Object
	 * @return an ArrayList of the individual
	 */
	private ArrayList<?> multiObjectHandler( Object mapObject ) {
		for ( Object value : mRenderer.getValues() ) {
			Class c = value.getClass();
			if ( c.getSimpleName().equals( "ArrayList" ) ) {
				ArrayList<?> mapObjects = (ArrayList<?>) value;
				if ( mapObjects.contains( mapObject ) ) {
					return mapObjects;
				}
			}
		}
		return null;
	}

	/**
	 * Callback interface for when a map object is clicked.
	 */
	public interface OnFeatureClickListener {

		void onFeatureClick( FeatureArtemis feature );
	}

	/**
	 * Stores a new Renderer object into mRenderer
	 *
	 * @param renderer the new Renderer object that belongs to this Layer
	 */
	protected void storeRenderer( RendererArtemis renderer ) {
		mRenderer = renderer;
	}

	/**
	 * Gets an iterable of all Feature elements that have been added to the layer
	 *
	 * @return iterable of Feature elements
	 */
	public Iterable<? extends FeatureArtemis> getFeatures() {
		return mRenderer.getFeatures();
	}

	/**
	 * Retrieves a corresponding Feature instance for the given Object
	 * Allows maps with multiple layers to determine which layer the Object
	 * belongs to.
	 *
	 * @param mapObject Object
	 * @return Feature for the given object
	 */
	public FeatureArtemis getFeature( Object mapObject ) { return mRenderer.getFeature( mapObject ); }

	public FeatureArtemis getContainerFeature( Object mapObject ) {
		return mRenderer.getContainerFeature( mapObject );
	}

	/**
	 * Checks if there are any features currently on the layer
	 *
	 * @return true if there are features on the layer, false otherwise
	 */
	protected boolean hasFeatures() {
		return mRenderer.hasFeatures();
	}

	/**
	 * Checks if the layer contains any KmlContainers
	 *
	 * @return true if there is at least 1 container within the KmlLayer, false otherwise
	 */
	protected boolean hasContainers() {
		if ( mRenderer instanceof KmlRendererArtemis ) {
			return ( (KmlRendererArtemis) mRenderer ).hasNestedContainers();
		}
		return false;
	}

	/**
	 * Gets an iterable of KmlContainerInterface objects
	 *
	 * @return iterable of KmlContainerInterface objects
	 */
	protected Iterable<KmlContainerArtemis> getContainers() {
		if ( mRenderer instanceof KmlRendererArtemis ) {
			return ( (KmlRendererArtemis) mRenderer ).getNestedContainers();
		}
		return null;
	}

	/**
	 * Gets an iterable of KmlGroundOverlay objects
	 *
	 * @return iterable of KmlGroundOverlay objects
	 */
	protected Iterable<KmlGroundOverlayArtemis> getGroundOverlays() {
		if ( mRenderer instanceof KmlRendererArtemis ) {
			return ( (KmlRendererArtemis) mRenderer ).getGroundOverlays();
		}
		return null;
	}

	/**
	 * Gets the map on which the layer is rendered
	 *
	 * @return map on which the layer is rendered
	 */
	public GoogleMap getMap() {
		return mRenderer.getMap();
	}

	/**
	 * Renders the layer on the given map. The layer on the current map is removed and
	 * added to the given map.
	 *
	 * @param map to render the layer on, if null the layer is cleared from the current map
	 */
	public void setMap( GoogleMap map ) {
		mRenderer.setMap( map );
	}

	/**
	 * Checks if the current layer has been added to the map
	 *
	 * @return true if the layer is on the map, false otherwise
	 */
	public boolean isLayerOnMap() {
		return mRenderer.isLayerOnMap();
	}

	/**
	 * Adds a provided feature to the map
	 *
	 * @param feature feature to add to map
	 */
	protected void addFeature( FeatureArtemis feature ) {
		mRenderer.addFeature( feature );
	}

	/**
	 * Remove a specified feature from the map
	 *
	 * @param feature feature to be removed
	 */
	protected void removeFeature( FeatureArtemis feature ) {
		mRenderer.removeFeature( feature );
	}

	/**
	 * Gets the default style used to render GeoJsonPoints. Any changes to this style will be
	 * reflected in the features that use it.
	 *
	 * @return default style used to render GeoJsonPoints
	 */
	public GeoJsonPointStyleArtemis getDefaultPointStyle() {
		return mRenderer.getDefaultPointStyle();
	}

	/**
	 * Gets the default style used to render GeoJsonLineStrings. Any changes to this style will be
	 * reflected in the features that use it.
	 *
	 * @return default style used to render GeoJsonLineStrings
	 */
	public GeoJsonLineStringStyleArtemis getDefaultLineStringStyle() {
		return mRenderer.getDefaultLineStringStyle();
	}

	/**
	 * Gets the default style used to render GeoJsonPolygons. Any changes to this style will be
	 * reflected in the features that use it.
	 *
	 * @return default style used to render GeoJsonPolygons
	 */
	public GeoJsonPolygonStyleArtemis getDefaultPolygonStyle() {
		return mRenderer.getDefaultPolygonStyle();
	}
}
