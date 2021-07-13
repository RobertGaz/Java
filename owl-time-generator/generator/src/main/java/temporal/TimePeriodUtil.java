package temporal;

import org.semanticweb.owlapi.model.TimePeriod;

import java.time.Period;

public class TimePeriodUtil {
    public static final Long NO_BEGINNING = -1L;
    public static final Long NO_END = Long.MAX_VALUE;

    public static boolean hasStart(TimePeriod period) {
        return period.getStart() != null;
    }

    public static boolean hasEnd(TimePeriod period) {
        return period.getEnd() != null;
    }

    public static Long duration(TimePeriod period) {
        if (hasStart(period) && hasEnd(period)) {
            return period.getEnd() - period.getStart();
        }

        return null;
    }

    private static void check(TimePeriod period) {
        if (period == null)
            throw new RuntimeException();

        if (hasStart(period) && period.getStart() < 0 || hasEnd(period) && period.getEnd() < 0)
            throw new RuntimeException();

        if (hasStart(period) && hasEnd(period) && duration(period) < 0)
            throw new RuntimeException();
    }

    public static TimePeriod get(Long start, Long end) {
        TimePeriod period = new TimePeriod();
        period.setStart((long)start);
        period.setEnd((long)end);
        check(period);

        return period;
    }

    public static TimePeriod random(Long min, Long max) {
        TimePeriod period = new TimePeriod();
        period.setStart(min + (long) (Math.random() * (max - min)));
        period.setEnd(period.getStart() + (long) (Math.random() * (max - period.getStart())));
        check(period);

        return period;
    }

    public static TimePeriod random(Long startMin, Long startMax, Long duration) {
        TimePeriod period = new TimePeriod();
        period.setStart(startMin + (long) (Math.random() * (startMax - startMin)));
        period.setEnd(period.getStart() + duration);
        check(period);

        return period;
    }

    public static TimePeriod getThatIntersects(TimePeriod other) {
        check(other);

        TimePeriod period = new TimePeriod();
        if (hasEnd(other)) {
            period.setStart(other.getEnd() - (long) (Math.random() * duration(other) * 0.5));
            period.setEnd(other.getEnd() + (long) (Math.random() * duration(other) * 0.5));
        } else if (hasStart(other) && other.getStart() > 0) {
            long start = other.getStart() - (long) (Math.random() * duration(other) * 0.5);
            if (start < 0) {
                start = 0;
            }
            period.setStart(start);
            period.setEnd(other.getStart() + (long) (Math.random() * duration(other) * 0.5));
        } else {
            period = random(other.getStart(), other.getEnd());
        }

        check(period);
        return period;
    }

}
