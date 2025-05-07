package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.DashboardData;

import com.storedobject.chart.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Getter
@AllArgsConstructor
public class ChartData<T, J> {
    private final Object[] labelValues;
    private final Object[] pointValues;

    /// La clase de la que sacas el dato de label
    private final T[][] labelObjects;

    /// La clase de la que sacas el dato de value
    private final J[][] pointObjects;

    private Color[] color;
    private String label;
    
    public List<ChartPoint<?, ?>> flatten(){
        List<ChartPoint<?, ?>> points = new LinkedList<>();

        //TODO : APAÑAR ESTO

        for (int i = 0; i < labelValues.length; i++){
            T[] lobjects = labelObjects.length > i ? labelObjects[i] : null;
            J[] pobjects = pointObjects.length > i ? pointObjects[i] : null;

            ChartPoint<T, J> point = new ChartPoint<>(lobjects, labelValues[i], pobjects, pointValues[i]);
            points.add(point);
        }
        return points;
    }

}
