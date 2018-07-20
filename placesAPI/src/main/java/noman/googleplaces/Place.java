package noman.googleplaces;

import android.location.Location;

import java.util.Arrays;
import java.util.List;

/**
 * Created by nor on 8/25/2016.
 */
public class Place {
    /**
     * coordinates of place
     */
    private noman.googleplaces.Location location;
    /**
     * icon of place, not all places have icon.
     */
    private String icon;
    /**
     * name of place
     */
    private String name;
    /**
     * place_id, can get place details using this id
     */
    private String placeId;
    /**
     * types of place
     */
    private List<String> types;
    /**
     * near area of place
     */
    private String vicinity;

    public Place() {
    }

    private Place(Builder builder) {
        setLocation(builder.location);
        setIcon(builder.icon);
        setName(builder.name);
        setPlaceId(builder.placeId);
        setTypes(builder.types);
        setVicinity(builder.vicinity);
    }

    @Override
    public String toString() {
        return "Place{" +
                "location=" + location +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", placeId='" + placeId + '\'' +
                ", types=" + types.toArray().toString() +
                ", vicinity='" + vicinity + '\'' +
                '}';
    }

    /**
     * Get lat,lat - location of the place
     *
     * @return Location
     */
    public noman.googleplaces.Location getLocation() {
        return location;
    }

    /**
     * set location of place
     *
     * @param location
     */
    public void setLocation(noman.googleplaces.Location location) {
        this.location = location;
    }

    /**
     * Get icon of the place
     * Note : Not all places have icons.
     *
     * @return url of icon e.g. https://maps.gstatic.com/mapfiles/place_api/icons/cafe-71.png
     */
    public String getIcon() {
        return icon;
    }

    /**
     * set icon of location
     *
     * @param icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * returns name of location
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * set name of location
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get placeid of location.
     * placeid is string id and can be used to get details of a place
     *
     * @return
     */
    public String getPlaceId() {
        return placeId;
    }

    /**
     * set place id
     *
     * @param placeId
     */
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    /**
     * returns types of location e.g. types ["cafe","food","point_of_interest","establishment"]
     *
     * @return String array types
     */
    public List<String> getTypes() {
        return types;
    }

    /**
     * set location types
     *
     * @param types
     */
    public void setTypes(List<String> types) {
        this.types = types;
    }

    /**
     * returns address/surrounding of place
     *
     * @return vicinity of place
     */
    public String getVicinity() {
        return vicinity;
    }

    /**
     * set vicinity
     *
     * @param vicinity
     */
    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public double getLatitude(){
        return location.getLatitude();
    }

    public double getLongitude(){
        return location.getLongitude();
    }

    @Override
    public boolean equals(Object obj) {

        boolean isMatch=false;
        if(obj!=null&&obj instanceof Place){
            Place hp=(Place) obj;
            isMatch=hp.getPlaceId().equalsIgnoreCase(this.getPlaceId());
        }
        return isMatch;    }

    @Override
    public int hashCode() {
        int result = 17;

        //hash code for checking rollno
        //result = 31 * result + (this.s_rollNo == 0 ? 0 : this.s_rollNo);

        //hash code for checking fname
        result = 31 * result + (this.getPlaceId() == null ? 0 : this.getPlaceId().hashCode());

        return result;    }

    public static final class Builder {
        private noman.googleplaces.Location location;
        private String icon;
        private String name;
        private String placeId;
        private List<String> types;
        private String vicinity;

        public Builder() {
        }

        public Builder location(noman.googleplaces.Location val) {
            location = val;
            return this;
        }

        public Builder icon(String val) {
            icon = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder placeId(String val) {
            placeId = val;
            return this;
        }

        public Builder types(List<String> val) {
            types = val;
            return this;
        }

        public Builder vicinity(String val) {
            vicinity = val;
            return this;
        }

        public Place build() {
            return new Place(this);
        }
    }
}
