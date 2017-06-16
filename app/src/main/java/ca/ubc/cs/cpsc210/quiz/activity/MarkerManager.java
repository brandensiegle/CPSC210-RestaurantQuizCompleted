package ca.ubc.cs.cpsc210.quiz.activity;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pcarter on 14-11-06.
 *
 * Manager for markers plotted on map
 */
public class MarkerManager {
    private GoogleMap map;
    private float hue;
    private Marker marker;
    private List<Marker> markers;


    /**
     * Constructor initializes manager with map for which markers are to be managed.
     *
     * @param map the map for which markers are to be managed
     */
    public MarkerManager(GoogleMap map) {
        this.map = map;
        markers=new ArrayList<Marker>();


    }

    /**
     * Get last marker added to map
     *
     * @return last marker added
     */
    public Marker getLastMarker() {
        return null;
    }

    /**
     * Add green marker to show position of restaurant
     *
     * @param point the point at which to add the marker
     * @param title the marker's title
     */
    public void addRestaurantMarker(LatLng point, String title) {
        map.addMarker(new MarkerOptions()
                .position(point)
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));


    }

    /**
     * Add a marker to mark latest guess from user.  Only the most recent two positions selected
     * by the user are marked.  The distance from the restaurant is used to create the marker's title
     * of the form "xxxx m away" where xxxx is the distance from the restaurant in metres (truncated to
     * an integer).
     * <p/>
     * The colour of the marker is based on the distance from the restaurant:
     * - red, if the distance is 3km or more
     * - somewhere between red (at 3km) and green (at 0m) (on a linear scale) for other distances
     *
     * @param latLng
     * @param distance
     */
    public void addMarker(LatLng latLng, double distance) {

        int d=(int)distance;
        marker=map.addMarker(new MarkerOptions()
                .position(latLng)
                .title(d + " m away")
                .icon(BitmapDescriptorFactory.defaultMarker(getColour(distance))));
        markers.add(marker);
        if(markers.size()>2){
            markers.get(0).remove();
            markers.remove(0);
        }
        if(distance<50){
            removeMarkers();
        }
    }

    /**
     * Remove markers that mark user guesses from the map
     */
    public void removeMarkers() {
           for(Marker current:markers){
               current.remove();
           }
    }

    /**
     * Produce a colour on a linear scale between red and green based on distance:
     * <p/>
     * - red, if distance is 3km or more
     * - somewhere between red (at 3km) and green (at 0m) (on a linear scale) for other distances
     *
     * @param distance distance from restaurant
     * @return colour of marker
     */
    private float getColour(double distance) {

        if (distance >= 3000) {
            hue = BitmapDescriptorFactory.HUE_RED;
        } else if (distance == 0) {
            hue = BitmapDescriptorFactory.HUE_GREEN;
        } else if (distance > 0 && distance < 3000) {
            double d = ((3000-distance) / 3000) * 120;
            hue = (float) d;
        }
        return hue;



    }
}
