
/**
 * This is Vehicle class, which contains vehicle registration number, owner name, owner type('E' for employee and 'D' for visiter)
    there are some set and get funtions, which helps other funtion to change or show the variables.
 *
 * @author: Yuting GUO (102893820)
 * @version: 29/02/2020
 */
public class Vehicle
{

    // instance variables - replace the example below with your own
    private String registrationNum, owner, ownerType;

    public Vehicle( String registrationNum, String owner, String ownerType ){
        this.registrationNum = registrationNum;
        this.owner = owner;
        this.ownerType = ownerType;
    }

    public void setVehicleRegistration(String registrationNum){
        this.registrationNum = registrationNum;
    }

    public void setOwner(String owner){
        this.owner = owner;
    }

    public void setOwnerType(String ownerType){
        this.ownerType = ownerType;
    }

    public String getVehicleRegistration(){
        return this.registrationNum;
    }

    public String getOwner(){
        return this.owner;
    }

    public String getOwnerTpye(){
        return this.ownerType;
    }

    
}
