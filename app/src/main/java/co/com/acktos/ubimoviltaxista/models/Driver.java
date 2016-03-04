package co.com.acktos.ubimoviltaxista.models;

/**
 * Created by Acktos on 2/19/16.
 */
public class Driver {


    private String id;
    private String name;
    private String email;
    private String phone;
    private String photo;
    private String agencyId;
    private String country;
    private String city;

    public Driver(String id,String name,String email,String phone,String photo,String agencyId,String country,String city){

        setId(id);
        setName(name);
        setEmail(email);
        setPhone(phone);
        setPhoto(photo);
        setAgencyId(agencyId);
        setCountry(country);
        setCity(city);
    }

    @Override
    public String toString(){
        return getName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
