package src.main.java.com.rental.model;

import java.util.Objects;

public class Car {
  private final String id;
   private final CarType type;
   private final String licensePlate;
   private final String brand;
   private final String model;

public Car(String id, CarType type, String licensePlate, String brand, String model){
  this.id=Objects.requireNonNull(id, "Car ID cannot be null");
  this.type = Objects.requireNonNull(type, "Car Type cannot be null");
  this.licensePlate = Objects.requireNonNull(licensePlate, "Car licensePlate cannot be null");
  this.brand = Objects.requireNonNull(brand, "Car brand cannot be null");
  this.model = Objects.requireNonNull(model, "Car model cannot be null");
}

public String getId(){
  return id;
}

public CarType getType(){
  return type;
}

public String getLicensePlate(){
  return licensePlate;
}

public String getBrand(){
  return brand;
}

public String getModel(){
  return model;
}

@Override
  public boolean equals(Object o) {
    if (this==o) return true;
    if (o==null || getClass() !=o.getClass()) return false;
    Car car= (Car)o;
    return Objects.equals(id, car.id);
  }

@Override
  public String toString(){
    return String.format("Car{id='%s', type=%s, licensePlate='%s',brand='%s',model='%s'}", id,type,licensePlate,brand,model);
  }
}
