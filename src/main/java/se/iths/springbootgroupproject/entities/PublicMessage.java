package se.iths.springbootgroupproject.entities;

import java.time.LocalDate;

public record PublicMessage(LocalDate date, String messageTitle, String messageBody, User user) {
}
