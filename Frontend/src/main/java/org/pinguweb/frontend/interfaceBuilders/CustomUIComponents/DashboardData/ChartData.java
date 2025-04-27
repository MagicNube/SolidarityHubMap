package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.DashboardData;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Getter
@AllArgsConstructor
public class ChartData {
    private final Object[] label;
    private final Object[] value;

    /// La clase de la que sacas el dato de label
    private final Class<?> labelClass;

    /// La clase de la que sacas el dato de value
    private final Class<?> valueClass;
    
    public List<ChartPoint> flatten(){
        List<ChartPoint> points = new LinkedList<>();
        
        for (int i = 0; i < label.length; i++){
            ChartPoint point = new ChartPoint(label[i], value[i]);
            points.add(point);
        }
        return points;
    }
}
