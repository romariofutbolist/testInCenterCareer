import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println(FlightBuilder.createFlights());
        System.out.println(filterFlightsByDepartureTime(FlightBuilder.createFlights()));
        System.out.println(filterFlightsByArrivalDate(FlightBuilder.createFlights()));
        System.out.println(filterFlightsWhereTimeSpentOnGroundMoreThanTwoHours(FlightBuilder.createFlights()));
    }


    //Вылет до текущего момента времени
    public static List<Flight> filterFlightsByDepartureTime(List<Flight> setFlights) {
        List<Flight> filteredFlights = new ArrayList<>();
        for (Flight flight : setFlights) {
            List<Segment> segments = flight.getSegments();
            boolean isSegmentCorrect = true;
            for (int i = 0; i < segments.size(); i++) {
                Segment segment = segments.get(i);
                if (segment.getDepartureDate().isBefore(LocalDateTime.now())) {
                    isSegmentCorrect = false;
                }
            }
            if (isSegmentCorrect) {
                filteredFlights.add(flight);
            }
        }
        return filteredFlights;
    }

    //Сегменты с датой прилёта раньше даты вылета.
    public static List<Flight> filterFlightsByArrivalDate(List<Flight> setFlights) {
        List<Flight> filteredFlights = new ArrayList<>();
        for (Flight flight : setFlights) {
            List<Segment> segments = flight.getSegments();
            boolean isSegmentCorrect = true;
            for (int i = 0; i < segments.size(); i++) {
                Segment segment = segments.get(i);
                if (segment.getArrivalDate().isBefore(segment.getDepartureDate())) {
                    isSegmentCorrect = false;
                }
            }
            if (isSegmentCorrect) {
                filteredFlights.add(flight);
            }
        }
        return filteredFlights;
    }

    //Перелеты, где общее время, проведённое на земле, превышает два часа (время на земле —
    // это интервал между прилётом одного сегмента и вылетом следующего за ним).
    public static List<Flight> filterFlightsWhereTimeSpentOnGroundMoreThanTwoHours(List<Flight> setFlights) {
        List<Flight> filteredFlights = new ArrayList<>();
        Duration totalGroundTime = Duration.ZERO;
        for (Flight flight : setFlights) {
            List<Segment> segments = flight.getSegments();
                for (int i = 1; i < segments.size(); i++) {
                    LocalDateTime timeDeparture = segments.get(i).getDepartureDate();
                    LocalDateTime timeArrival = segments.get(i - 1).getArrivalDate();

                    Duration groundTime = Duration.between(timeArrival, timeDeparture);
                    totalGroundTime = totalGroundTime.plus(groundTime);
            }
                if (totalGroundTime.toHours() > 2) {
                    filteredFlights.add(flight);
                }
            totalGroundTime = Duration.ZERO;
            }
        return filteredFlights;
    }
}