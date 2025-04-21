package org.pinguweb.frontend.interfaceBuilders.customUIComponents;

import com.storedobject.chart.*;
import com.vaadin.flow.component.Tag;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Tag("DashboardComponent")
public class Dashboard extends InterfaceComponent{
    protected SOChart chart;
    protected final String name;
    protected final Color colors;
    protected final RectangularCoordinate coordinateConfiguration;
    protected final Object[] labels;
    protected final Object[] data;
    protected final ChartType type;
    private String width;
    private String height;

    public SOChart generateChart(){
        switch (type){
            case BAR -> {return generateBarChart();}
            default -> {return null;}
        }
    }

    private SOChart generateBarChart(){
        chart = new SOChart();
        chart.setSize(width, height);
        for (int i = 0; i < labels.length; i++) {
            BarChart bar =
                    new BarChart(
                            Dashboard.castObjectByCoordinateType(this.coordinateConfiguration.getAxis(0).getDataType(), labels[i]),
                            Dashboard.castObjectByCoordinateType(this.coordinateConfiguration.getAxis(1).getDataType(), data[i]));
            bar.setName(labels[i].toString());
            bar.setColors(this.colors);
            bar.plotOn(this.coordinateConfiguration);
            this.chart.add(bar);
        }
        return this.chart;
    }

    public static AbstractData<?> castObjectByCoordinateType(DataType tipo, Object valor) {
        return switch (tipo) {
            case CATEGORY -> new CategoryData((String) valor);
            case NUMBER, LOGARITHMIC -> new Data((Number) valor);
            case DATE -> new DateData((LocalDate) valor);
            case TIME -> new TimeData((LocalDateTime) valor);
            default -> throw new IllegalArgumentException("Tipo no soportado: " + tipo);
        };
    }
}
