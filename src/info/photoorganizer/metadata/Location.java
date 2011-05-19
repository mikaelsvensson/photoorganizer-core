package info.photoorganizer.metadata;

public class Location
{
    private String _city = null;
    private String _country = null;
    private Double _latitude = 0.0;
    private Double _longitude = 0.0;
    
    public Location(String city, String country, Double latitude, Double longitude)
    {
        super();
        _city = city;
        _country = country;
        _latitude = latitude;
        _longitude = longitude;
    }

    public String getCity()
    {
        return _city;
    }
    
    public String getCountry()
    {
        return _country;
    }
    
    public Double getLatitude()
    {
        return _latitude;
    }
    
    public Double getLongitude()
    {
        return _longitude;
    }
}
