package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.DashboardData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TestString {
    public String name;
    public LocalDate date;
    public LocalDateTime dateTime;
    public Integer number;
    public boolean check;
}
