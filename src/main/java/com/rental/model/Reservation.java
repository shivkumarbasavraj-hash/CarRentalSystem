package src.main.java.com.rental.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {
  private final String id;
  private final Car car;
  private final String customerName;
  private final LocalDateTime startDateTime;
  private final int numberOfDays;

public Reservation (String id, Car car, String customerName, LocalDateTime startDateTime, int numberOfDays) {
  this.id=Objects.requireNonNull(id, "Reservation ID cannot be null");
  this.car =Objects.requireNonNull(car, "Car cannot be null");
  this.customerName=Objects.requireNonNull(customerName, "customerName cannot be null");
   this.startDateTime=Objects.requireNonNull(startDateTime, "start Date/Time cannot be null");

  if(numberOfDays <=0){
    throw new IllegalArgumentException("Number of days must be positive");
  }
  this.numberOfDays=numberOfDays;
}
  public String getId() {
    return id;
  }

  public Car getCar() {
    return car;
  }

  public String getCustomerName() {
    return customerName;
  }

public LocalDateTime getStartDateTime(){
  return startDateTime;
}

public int getNumberOfDays() {
    return numberOfDays;
}

public LocalDateTime getEndDateTime(){
  return startDateTime.plusDays(numberOfDays);
}

public double getTotalCost(){
  return car.getType().getDailyRate()*numberOfDays;
}

public boolean overlaps(LocalDateTime otherStart, LocalDateTime otherEnd){
  LocalDateTime thisEnd=getEndDateTime();
  return startDateTime.isBefore(otherEnd) && thisEnd.isAfter(otherStart);
}

@Override
  public boolean equals(Object o){
    if (this==o) return true;
    if(o==null || getClass()!=o.getClass()) return false;
    Reservation that=(Reservation)o;
    return Objects.equals(id, that.id);
  }

@Override
  public int hashCode(){
    return Objects.hash(id);
  }

@Override
  public String toString(){
    return String.format("Reservation{id='%s', car='%s',customer='%s', start='%s',days=%d, total=%.2f}",id,car.getLicensePlate(),customerName,startDateTime,numberOfDays,getTotalCost());
  }
}
