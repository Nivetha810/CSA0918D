import java.time.*;import java.util.*;
interface ParkingPass{boolean validAt(LocalDate d);}
record OneDayPass(LocalDate date)implements ParkingPass{public boolean validAt(LocalDate d){return date.equals(d);}}
abstract class ParkedVehicle{final String reg;ParkedVehicle(String r){if(r==null||r.isBlank())throw new IllegalArgumentException();reg=r;}abstract double fee(long mins,LocalDateTime in,LocalDateTime out);}
class BikeVehicle extends ParkedVehicle{BikeVehicle(String r){super(r);}double fee(long m,LocalDateTime i,LocalDateTime o){return ((m+59)/60)*10.0;}}
class CarVehicle extends ParkedVehicle{CarVehicle(String r){super(r);}double fee(long m,LocalDateTime i,LocalDateTime o){return ((m+59)/60)*30.0;}}
class EBike extends ParkedVehicle{EBike(String r){super(r);}double fee(long m,LocalDateTime i,LocalDateTime o){return 25.0;}}
class DeliveryVan extends ParkedVehicle{DeliveryVan(String r){super(r);}double fee(long m,LocalDateTime i,LocalDateTime o){long day=0;for(LocalDateTime t=i;t.isBefore(o);t=t.plusMinutes(1))if(!t.toLocalTime().isBefore(LocalTime.of(9,0))&&t.toLocalTime().isBefore(LocalTime.of(17,0)))day++;long other=Math.max(0,Duration.between(i,o).toMinutes()-day);return ((day+59)/60)*60.0+((other+59)/60)*30.0;}}
record Ticket(long id,ParkedVehicle vehicle,int floor,int slot,LocalDateTime entry,ParkingPass pass){}
public class SmartParkingSystem{
 final String[][] slots;final Map<String,Ticket> active=new HashMap<>();long next=1;
 SmartParkingSystem(int[] sizes){slots=new String[sizes.length][];for(int i=0;i<sizes.length;i++){if(sizes[i]<0)throw new IllegalArgumentException();slots[i]=new String[sizes[i]];}}
 Ticket enter(ParkedVehicle v,ParkingPass pass,LocalDateTime now){String k=v.reg.toUpperCase(Locale.ROOT);if(active.containsKey(k))throw new IllegalArgumentException("Duplicate registration");for(int f=0;f<slots.length;f++)for(int s=0;s<slots[f].length;s++)if(slots[f][s]==null){slots[f][s]=k;Ticket t=new Ticket(next++,v,f,s,now,pass);active.put(k,t);return t;}throw new IllegalStateException("Full");}
 double exit(String r,LocalDateTime now){Ticket t=active.remove(r.toUpperCase(Locale.ROOT));if(t==null)throw new IllegalArgumentException("No ticket");slots[t.floor()][t.slot()]=null;if(t.pass()!=null&&t.pass().validAt(now.toLocalDate()))return 0;return t.vehicle().fee(Math.max(0,Duration.between(t.entry(),now).toMinutes()),t.entry(),now);}
 public static void main(String[]a){SmartParkingSystem p=new SmartParkingSystem(new int[]{2,1,3,2,2});LocalDateTime n=LocalDateTime.of(2026,11,19,10,0);p.enter(new EBike("TN01AA1111"),null,n);p.enter(new DeliveryVan("TN02BB2222"),null,n);p.enter(new CarVehicle("TN03CC3333"),new OneDayPass(n.toLocalDate()),n);System.out.println("E-bike Rs "+p.exit("TN01AA1111",n.plusHours(2)));System.out.println("Van Rs "+p.exit("TN02BB2222",n.plusHours(2)));System.out.println("Pass Rs "+p.exit("TN03CC3333",n.plusHours(2)));}
}