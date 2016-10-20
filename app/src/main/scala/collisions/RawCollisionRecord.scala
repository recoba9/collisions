package collisions

/**
  * Created by Rostislav on 15.10.2016.
  */
case class RawCollisionRecord(
  date: String,
  time: String,
  borough: String,
  zipCode: String,
  latitude: String,
  longtitude: String,
  location: String,
  onStreetName: String,
  crossStreetName: String,
  offStreetName: String,
  numberOfPersonsInjured: String,
  numberOfPersonsKilled: String,
  numberOfPedestriansInjured: String,
  numberOfPedestriansKilled: String,
  numberOfCyclistInjured: String,
  numberOfCyclistKilled: String,
  numberOfMotoristInjured: String,
  numberOfMotoristKilled: String,
  contributingFactorVehicle1: String,
  contributingFactorVehicle2: String,
  contributingFactorVehicle3: String,
  contributingFactorVehicle4: String,
  contributingFactorVehicle5: String,
  uniqueKey: String,
  vehicleTypeCode1: String,
  vehicleTypeCode2: String,
  vehicleTypeCode3: String,
  vehicleTypeCode4: String,
  vehicleTypeCode5: String
)
