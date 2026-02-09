package src.test.java.com.rental;

import src.main.java.com.rental.model.Car;
import src.main.java.com.rental.model.CarType;
import src.main.java.com.rental.model.Reservation;
import src.main.java.com.rental.service.CarRentalSystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class CarRentalSystemTest {

    private CarRentalSystem rentalSystem;
    private LocalDateTime baseDateTime;

    @BeforeEach
    void setUp() {
        rentalSystem = new CarRentalSystem();
        baseDateTime = LocalDateTime.of(2026, 1, 26, 10, 0);
    }

    private void setupInventory() {
        rentalSystem.addCar(new Car("SEDAN-1", CarType.SEDAN, "ABC-001", "Toyota", "Camry"));
        rentalSystem.addCar(new Car("SEDAN-2", CarType.SEDAN, "ABC-002", "Honda", "Accord"));
        rentalSystem.addCar(new Car("SUV-1", CarType.SUV, "DEF-001", "Toyota", "RAV4"));
        rentalSystem.addCar(new Car("SUV-2", CarType.SUV, "DEF-002", "Honda", "CR-V"));
        rentalSystem.addCar(new Car("VAN-1", CarType.VAN, "GHI-001", "Toyota", "Sienna"));
    }

    @Nested
    @DisplayName("Requirement 1: Reservation of a car of a given type at a desired date")
    class ReservationTests {

        @Test
        @DisplayName("Should successfully reserve a Sedan for 3 days")
        void shouldReserveSedanForThreeDays() {
            setupInventory();

            Reservation reservation = rentalSystem.makeReservation(
                CarType.SEDAN, baseDateTime, 3, "John Doe"
            );

            assertNotNull(reservation);
            assertEquals(CarType.SEDAN, reservation.getCar().getType());
            assertEquals(baseDateTime, reservation.getStartDateTime());
            assertEquals(3, reservation.getNumberOfDays());
            assertEquals("John Doe", reservation.getCustomerName());
            assertEquals(baseDateTime.plusDays(3), reservation.getEndDateTime());
        }

        @Test
        @DisplayName("Should successfully reserve an SUV for 5 days")
        void shouldReserveSuvForFiveDays() {
            setupInventory();

            Reservation reservation = rentalSystem.makeReservation(
                CarType.SUV, baseDateTime, 5, "Jane Smith"
            );

            assertNotNull(reservation);
            assertEquals(CarType.SUV, reservation.getCar().getType());
            assertEquals(5, reservation.getNumberOfDays());
        }

        @Test
        @DisplayName("Should successfully reserve a Van for 7 days")
        void shouldReserveVanForSevenDays() {
            setupInventory();

            Reservation reservation = rentalSystem.makeReservation(
                CarType.VAN, baseDateTime, 7, "Bob Wilson"
            );

            assertNotNull(reservation);
            assertEquals(CarType.VAN, reservation.getCar().getType());
            assertEquals(7, reservation.getNumberOfDays());
        }

        @Test
        @DisplayName("Should reserve at specific date and time")
        void shouldReserveAtSpecificDateTime() {
            setupInventory();
            LocalDateTime specificDateTime = LocalDateTime.of(2026, 2, 15, 14, 30);

            Reservation reservation = rentalSystem.makeReservation(
                CarType.SEDAN, specificDateTime, 2, "Alice Brown"
            );

            assertEquals(specificDateTime, reservation.getStartDateTime());
            assertEquals(specificDateTime.plusDays(2), reservation.getEndDateTime());
        }

        @Test
        @DisplayName("Should calculate correct total cost based on car type and days")
        void shouldCalculateCorrectTotalCost() {
            setupInventory();

            Reservation sedanReservation = rentalSystem.makeReservation(
                CarType.SEDAN, baseDateTime, 3, "Customer 1");
            assertEquals(150.0, sedanReservation.getTotalCost());

            Reservation suvReservation = rentalSystem.makeReservation(
                CarType.SUV, baseDateTime.plusDays(10), 2, "Customer 2");
            assertEquals(150.0, suvReservation.getTotalCost());

            Reservation vanReservation = rentalSystem.makeReservation(
                CarType.VAN, baseDateTime.plusDays(20), 4, "Customer 3");
            assertEquals(400.0, vanReservation.getTotalCost());
        }

        @Test
        @DisplayName("Should reject reservation with zero or negative days")
        void shouldRejectInvalidNumberOfDays() {
            setupInventory();

            assertThrows(IllegalArgumentException.class, () ->
                rentalSystem.makeReservation(CarType.SEDAN, baseDateTime, 0, "Customer"));

            assertThrows(IllegalArgumentException.class, () ->
                rentalSystem.makeReservation(CarType.SEDAN, baseDateTime, -1, "Customer"));
        }
    }

    @Nested
    @DisplayName("Requirement 2: Three types of cars (Sedan, SUV, Van)")
    class CarTypeTests {

        @Test
        @DisplayName("System should support all three car types")
        void shouldSupportAllThreeCarTypes() {
            CarType[] types = CarType.values();

            assertEquals(3, types.length);
            assertTrue(containsType(types, CarType.SEDAN));
            assertTrue(containsType(types, CarType.SUV));
            assertTrue(containsType(types, CarType.VAN));
        }

        private boolean containsType(CarType[] types, CarType target) {
            for (CarType type : types) {
                if (type == target) return true;
            }
            return false;
        }

        @Test
        @DisplayName("Each car type should have a display name")
        void eachTypeShouldHaveDisplayName() {
            assertEquals("Sedan", CarType.SEDAN.getDisplayName());
            assertEquals("SUV", CarType.SUV.getDisplayName());
            assertEquals("Van", CarType.VAN.getDisplayName());
        }

        @Test
        @DisplayName("Each car type should have a daily rate")
        void eachTypeShouldHaveDailyRate() {
            assertTrue(CarType.SEDAN.getDailyRate() > 0);
            assertTrue(CarType.SUV.getDailyRate() > 0);
            assertTrue(CarType.VAN.getDailyRate() > 0);
        }

        @Test
        @DisplayName("Should be able to add and reserve each car type")
        void shouldAddAndReserveEachType() {
            rentalSystem.addCar(new Car("S1", CarType.SEDAN, "S-001", "Toyota", "Camry"));
            rentalSystem.addCar(new Car("U1", CarType.SUV, "U-001", "Honda", "CR-V"));
            rentalSystem.addCar(new Car("V1", CarType.VAN, "V-001", "Toyota", "Sienna"));

            Reservation sedan = rentalSystem.makeReservation(CarType.SEDAN, baseDateTime, 1, "C1");
            Reservation suv = rentalSystem.makeReservation(CarType.SUV, baseDateTime, 1, "C2");
            Reservation van = rentalSystem.makeReservation(CarType.VAN, baseDateTime, 1, "C3");

            assertEquals(CarType.SEDAN, sedan.getCar().getType());
            assertEquals(CarType.SUV, suv.getCar().getType());
            assertEquals(CarType.VAN, van.getCar().getType());
        }
}
        @Nested
        @DisplayName("Requirement 3: Limited number of cars of each type")
        class LimitedInventoryTests {

            @Test
            @DisplayName("Should track inventory count per car type")
            void shouldTrackInventoryCountPerType() {
                assertEquals(0, rentalSystem.getInventoryCount(CarType.SEDAN));
                assertEquals(0, rentalSystem.getInventoryCount(CarType.SUV));
                assertEquals(0, rentalSystem.getInventoryCount(CarType.VAN));

                rentalSystem.addCar(new Car("S1", CarType.SEDAN, "S-001", "Toyota", "Camry"));
                rentalSystem.addCar(new Car("S2", CarType.SEDAN, "S-002", "Honda", "Accord"));
                rentalSystem.addCar(new Car("U1", CarType.SUV, "U-001", "Toyota", "RAV4"));

                assertEquals(2, rentalSystem.getInventoryCount(CarType.SEDAN));
                assertEquals(1, rentalSystem.getInventoryCount(CarType.SUV));
                assertEquals(0, rentalSystem.getInventoryCount(CarType.VAN));
            }

            @Test
            @DisplayName("Should fail reservation when no cars of requested type are available")
            void shouldFailWhenNoCarTypeAvailable() {
                rentalSystem.addCar(new Car("S1", CarType.SEDAN, "S-001", "Toyota", "Camry"));

                assertThrows(NoSuchElementException.class, () ->
                    rentalSystem.makeReservation(CarType.SUV, baseDateTime, 1, "Customer"));
            }

            @Test
            @DisplayName("Should fail reservation when all cars of type are already reserved for the period")
            void shouldFailWhenAllCarsReserved() {
                rentalSystem.addCar(new Car("S1", CarType.SEDAN, "S-001", "Toyota", "Camry"));

                rentalSystem.makeReservation(CarType.SEDAN, baseDateTime, 5, "Customer 1");

                assertThrows(NoSuchElementException.class, () ->
                    rentalSystem.makeReservation(
                        CarType.SEDAN,
                        baseDateTime.plusDays(2),
                        2,
                        "Customer 2"
                    ));
            }

            @Test
            @DisplayName("Should allow reservation when another car of same type is available")
            void shouldAllowReservationWhenAnotherCarAvailable() {
                rentalSystem.addCar(new Car("S1", CarType.SEDAN, "S-001", "Toyota", "Camry"));
                rentalSystem.addCar(new Car("S2", CarType.SEDAN, "S-002", "Honda", "Accord"));

                Reservation res1 =
                    rentalSystem.makeReservation(CarType.SEDAN, baseDateTime, 5, "Customer 1");
                Reservation res2 =
                    rentalSystem.makeReservation(CarType.SEDAN, baseDateTime, 3, "Customer 2");

                assertNotNull(res1);
                assertNotNull(res2);
                assertNotEquals(res1.getCar().getId(), res2.getCar().getId());
            }

            @Test
            @DisplayName("Should allow reservation for non-overlapping periods on same car")
            void shouldAllowNonOverlappingReservations() {
                rentalSystem.addCar(new Car("S1", CarType.SEDAN, "S-001", "Toyota", "Camry"));

                Reservation res1 = rentalSystem.makeReservation(
                    CarType.SEDAN, baseDateTime, 3, "Customer 1");
                Reservation res2 = rentalSystem.makeReservation(
                    CarType.SEDAN, baseDateTime.plusDays(5), 2, "Customer 2");

                assertNotNull(res1);
                assertNotNull(res2);
                assertEquals(res1.getCar().getId(), res2.getCar().getId());
            }

            @Test
            @DisplayName("Should correctly handle overlapping reservation attempts")
            void shouldHandleOverlappingReservations() {
                rentalSystem.addCar(new Car("S1", CarType.SEDAN, "S-001", "Toyota", "Camry"));

                rentalSystem.makeReservation(
                    CarType.SEDAN, baseDateTime, 5, "Customer 1");

                assertThrows(NoSuchElementException.class, () ->
                    rentalSystem.makeReservation(
                        CarType.SEDAN, baseDateTime.plusDays(4), 3, "Customer 2"));

                assertThrows(NoSuchElementException.class, () ->
                    rentalSystem.makeReservation(
                        CarType.SEDAN, baseDateTime.minusDays(1), 3, "Customer 3"));

                assertThrows(NoSuchElementException.class, () ->
                    rentalSystem.makeReservation(
                        CarType.SEDAN, baseDateTime.plusDays(1), 2, "Customer 4"));
            }

            @Test
            @DisplayName("Should prevent adding duplicate car IDs")
            void shouldPreventDuplicateCarIDs() {
                rentalSystem.addCar(new Car("S1", CarType.SEDAN, "S-001", "Toyota", "Camry"));

                assertThrows(IllegalArgumentException.class, () ->
                    rentalSystem.addCar(
                        new Car("S1", CarType.SEDAN, "S-002", "Honda", "Accord")));
            }
        }

    @Nested
    @DisplayName("Additional functionality tests")
    class AdditionalTests {

        @Test
        @DisplayName("Should be able to cancel a reservation")
        void shouldCancelReservation() {
            setupInventory();

            Reservation reservation = rentalSystem.makeReservation(
                CarType.SEDAN, baseDateTime, 3, "John Doe");

            assertTrue(rentalSystem.cancelReservation(reservation.getId()));
            assertTrue(rentalSystem.getReservation(reservation.getId()).isEmpty());
        }

        @Test
        @DisplayName("Should return false when cancelling non-existent reservation")
        void shouldReturnFalseForNonExistentCancellation() {
            assertFalse(rentalSystem.cancelReservation("NON-EXISTENT"));
        }

        @Test
        @DisplayName("Should allow new reservation after cancellation")
        void shouldAllowReservationAfterCancellation() {
            rentalSystem.addCar(
                new Car("S1", CarType.SEDAN, "S-001", "Toyota", "Camry"));

            Reservation res1 = rentalSystem.makeReservation(
                CarType.SEDAN, baseDateTime, 5, "Customer 1");
            rentalSystem.cancelReservation(res1.getId());

            Reservation res2 = rentalSystem.makeReservation(
                CarType.SEDAN, baseDateTime, 3, "Customer 2");

            assertNotNull(res2);
        }

        @Test
        @DisplayName("Should retrieve all reservations")
        void shouldRetrieveAllReservations() {
            setupInventory();
            rentalSystem.makeReservation(CarType.SEDAN, baseDateTime, 2, "Customer 1");
            rentalSystem.makeReservation(CarType.SUV, baseDateTime, 3, "Customer 2");
            rentalSystem.makeReservation(CarType.VAN, baseDateTime, 4, "Customer 3");

            List<Reservation> allReservations = rentalSystem.getAllReservations();
            assertEquals(3, allReservations.size());
        }

        @Test
        @DisplayName("Should retrieve reservations by customer")
        void shouldRetrieveReservationsByCustomer() {
            setupInventory();

            rentalSystem.makeReservation(
                CarType.SEDAN, baseDateTime, 2, "John Doe");
            rentalSystem.makeReservation(
                CarType.SUV, baseDateTime.plusDays(10), 3, "John Doe");
            rentalSystem.makeReservation(
                CarType.VAN, baseDateTime, 4, "Jane Smith");

            List<Reservation> johnReservations =
                rentalSystem.getReservationsByCustomer("John Doe");
            assertEquals(2, johnReservations.size());

            List<Reservation> janeReservations =
                rentalSystem.getReservationsByCustomer("Jane Smith");
            assertEquals(1, janeReservations.size());
        }

        @Test
        @DisplayName("Should get available cars for a given period")
        void shouldGetAvailableCars() {
            setupInventory();

            List<Car> availableSedans =
                rentalSystem.getAvailableCars(CarType.SEDAN, baseDateTime, 3);
            assertEquals(2, availableSedans.size());

            rentalSystem.makeReservation(
                CarType.SEDAN, baseDateTime, 3, "Customer 1");

            availableSedans =
                rentalSystem.getAvailableCars(CarType.SEDAN, baseDateTime, 3);
            assertEquals(1, availableSedans.size());
        }
    }
}
