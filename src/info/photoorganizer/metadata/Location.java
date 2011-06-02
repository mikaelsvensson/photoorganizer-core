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
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_city == null) ? 0 : _city.hashCode());
        result = prime * result + ((_country == null) ? 0 : _country.hashCode());
        result = prime * result + ((_latitude == null) ? 0 : _latitude.hashCode());
        result = prime * result + ((_longitude == null) ? 0 : _longitude.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Location other = (Location) obj;
        if (_city == null)
        {
            if (other._city != null)
                return false;
        }
        else if (!_city.equals(other._city))
            return false;
        if (_country == null)
        {
            if (other._country != null)
                return false;
        }
        else if (!_country.equals(other._country))
            return false;
        if (_latitude == null)
        {
            if (other._latitude != null)
                return false;
        }
        else if (!_latitude.equals(other._latitude))
            return false;
        if (_longitude == null)
        {
            if (other._longitude != null)
                return false;
        }
        else if (!_longitude.equals(other._longitude))
            return false;
        return true;
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
