interface Chargeable{double chargingFee(int kWh);}
abstract class Vehicle{
 private final String regNo;protected Vehicle(String r){if(r==null||r.isBlank())throw new IllegalArgumentException();regNo=r;}
 public String getRegNo(){return regNo;}protected abstract double ratePerHour();
 public double calculateFee(int m){if(m<0)throw new IllegalArgumentException();return ((m+59)/60)*ratePerHour();}
 public boolean equals(Object o){return o instanceof Vehicle v&&regNo.equals(v.regNo);}public int hashCode(){return regNo.hashCode();}
}
class TwoWheeler extends Vehicle{TwoWheeler(String r){super(r);}protected double ratePerHour(){return 10;}}
class Car extends Vehicle{Car(String r){super(r);}protected double ratePerHour(){return 30;}}
class ElectricCar extends Car implements Chargeable{ElectricCar(String r){super(r);}public double chargingFee(int k){if(k<0)throw new IllegalArgumentException();return 12.0*k;}}
public class FeeDemo{
 public static void main(String[]a){Vehicle[] vs={new TwoWheeler("TN01A1"),new Car("TN02B2"),new ElectricCar("TN03C3")};for(Vehicle v:vs){double fee=v.calculateFee(130);if(v instanceof Chargeable c)fee+=c.chargingFee(10);System.out.println(v.getClass().getSimpleName()+" "+v.getRegNo()+" -> Rs "+fee);}System.out.println(new Car("TN02B2").equals(new ElectricCar("TN02B2")));}
}