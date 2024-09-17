import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        System.out.println(FlightBuilder.createFlights());
        System.out.println(filterFlightsByDepartureTime(FlightBuilder.createFlights()));
        System.out.println(filterFlightsByArrivalDate(FlightBuilder.createFlights()));
        System.out.println(filterFlightsWhereTimeSpentOnGroundMoreThanTwoHours(FlightBuilder.createFlights()));
    }

    //Список полетов, у которых отсутствует Вылет раньше текущего момента времени
    public static List<Flight> filterFlightsByDepartureTime(List<Flight> setFlights) {
        return setFlights.stream()
                .filter(flight -> flight.getSegments().stream()
                        .noneMatch(segment -> segment.getDepartureDate().isBefore(LocalDateTime.now())))
                .collect(Collectors.toList());
    }

    //Список полетов, у которых отсутствуют Сегменты с датой прилёта раньше даты вылета.
    public static List<Flight> filterFlightsByArrivalDate(List<Flight> setFlights) {
        return setFlights.stream().
                filter(flight -> flight.getSegments().stream()
                        .noneMatch(segment -> segment.getArrivalDate().isBefore(segment.getDepartureDate())))
                .collect(Collectors.toList());
    }

    //Список полетов, где общее время, проведённое на земле, не превышает два часа
    public static List<Flight> filterFlightsWhereTimeSpentOnGroundMoreThanTwoHours(List<Flight> setFlights) {
        return setFlights.stream()
                .filter(flight -> calculateTotalGroundTime(flight).toHours() <= 2)
                .collect(Collectors.toList());
    }

    //Метод,возвращающий общее время на земле между перелетами (сегментами)
    private static Duration calculateTotalGroundTime(Flight flight) {
        Duration totalGroundTime = Duration.ZERO;
        for (int i = 1; i < flight.getSegments().size(); i++) {
            Duration groundTime = Duration.between(flight.getSegments().get(i - 1).getArrivalDate(), flight.getSegments().get(i).getDepartureDate());
            totalGroundTime = totalGroundTime.plus(groundTime);
        }
        return totalGroundTime;
    }
}