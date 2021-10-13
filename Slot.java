
/**
    This is the slot class, which contains the slot ID, slot type, if the slot is occupied, and the vhicel information in this slot
        vehicle is a Vehicle class object
        there are some set and get funtions, which helps other funtion to change or show the variables.
 *
 * @author: Yuting GUO (102893820)
 * @version: 29/02/2020
 */

public class Slot{
    private String slotId, slotType;
    private boolean occupied;
    private Vehicle vehicle = null;

    /**
     * Constructor for objects of class Slot
     */
    public Slot()
    {
        
    }

    public Slot( String slotId, String slotType, Vehicle vehicle){
        this.slotId = slotId;
        this.slotType = slotType;

        //initially the slot is empty, and vehicle is null
        this.occupied = false;
        this.vehicle = vehicle;
    }

    public void setOccupied(boolean occupied){
        this.occupied = occupied;
    }

    public void setVehicle(Vehicle vehicle){
        this.vehicle = vehicle;
    }

    public String getSlotId(){
        return this.slotId;
    }

    public String getSlotType(){
        return this.slotType;
    }

    public boolean getOccupied(){
        return this.occupied;
    }

    public Vehicle getVehicle(){
        return this.vehicle;
    }

    

}
