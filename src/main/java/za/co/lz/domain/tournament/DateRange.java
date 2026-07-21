package za.co.lz.domain.tournament;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;

@Embeddable
public class DateRange {
    private LocalDateTime tournamentStartDate;
    private LocalDateTime tournamentEndDate;

    protected DateRange() {
    }

    private DateRange(Builder builder) {
        this.tournamentStartDate = builder.tournamentStartDate;
        this.tournamentEndDate = builder.tournamentEndDate;
    }

    public LocalDateTime getTournamentStartDate() {
        return tournamentStartDate;
    }

    public LocalDateTime getTournamentEndDate() {
        return tournamentEndDate;
    }

    public String toString() {
        return "DateRange{" +
                "tournamentStartDate=" + tournamentStartDate +
                ", tournamentEndDate=" + tournamentEndDate +
                '}';
    }

    public static class Builder {
        private LocalDateTime tournamentStartDate;
        private LocalDateTime tournamentEndDate;

        public Builder setTournamentStartDate(LocalDateTime tournamentStartDate) {
            this.tournamentStartDate = tournamentStartDate;
            return this;
        }

        public Builder setTournamentEndDate(LocalDateTime tournamentEndDate) {
            this.tournamentEndDate = tournamentEndDate;
            return this;
        }

        public DateRange build() {
            return new DateRange(this);
        }
    }

    public static Object of(LocalDate startDate, LocalDate endDate) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'of'");
    }
}
