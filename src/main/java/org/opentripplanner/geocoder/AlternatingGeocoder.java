package org.opentripplanner.geocoder;

import org.locationtech.jts.geom.Envelope;


public class AlternatingGeocoder implements Geocoder {
    
    private Geocoder geocoder1;
    private Geocoder geocoder2;
    private boolean useFirstGeocoder;

    
    public AlternatingGeocoder(Geocoder geocoder1, Geocoder geocoder2) {
        this.geocoder1 = geocoder1;
        this.geocoder2 = geocoder2;
        this.useFirstGeocoder = false;
    }

    @Override
    public GeocoderResults geocode(String address, Envelope bbox) {
        useFirstGeocoder = !useFirstGeocoder;
        Geocoder g = useFirstGeocoder ? geocoder1 : geocoder2;
        return g.geocode(address, bbox);
    }

}
