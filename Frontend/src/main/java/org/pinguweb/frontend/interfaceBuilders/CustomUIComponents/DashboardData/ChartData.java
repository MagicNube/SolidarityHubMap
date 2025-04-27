package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.DashboardData;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Getter
@AllArgsConstructor
public class ChartData<T, J> {
    private final Object[] labelValues;
    private final Object[] pointValues;

    /// La clase de la que sacas el dato de label
    private final T[] labelObjects;

    /// La clase de la que sacas el dato de value
    private final J[] pointObjects;
    
    public List<ChartPoint<?, ?>> flatten(){
        List<ChartPoint<?,?>> points = new LinkedList<>();
        
        for (int i = 0; i < labelObjects.length; i++){
            ChartPoint<T,J> point = new ChartPoint<>(labelObjects[i], pointObjects[i], labelValues[i], pointValues[i]);
            points.add(point);
        }
        return points;
    }
}
