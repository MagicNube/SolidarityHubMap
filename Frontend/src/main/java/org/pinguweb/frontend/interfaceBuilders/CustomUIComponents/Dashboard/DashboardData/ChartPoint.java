package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.DashboardData;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChartPoint<X, Y> {
    private X[] XObject;
    private final Object XValue;

    private Y[] YObject;
    private final Object YValue;
}
