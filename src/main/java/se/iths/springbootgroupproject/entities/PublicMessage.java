package se.iths.springbootgroupproject.entities;

import java.time.LocalDate;

public record PublicMessage(LocalDate createdDate, String messageTitle, String messageBody, User user) {
}
