package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents;

import com.storedobject.chart.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SuperBuilder
@Getter
@Tag("DashboardComponent")
public class Dashboard extends InterfaceComponent{
    protected final Color colors;
    protected final RectangularCoordinate coordinateConfiguration;
    protected final Object[] labels;
    protected final Object[] data;
    protected final ChartType type;
    protected final String width;
    protected final String height;

    @Override
    public Component getComponent(){
        switch (type){
            case BAR -> {return generateBarChart();}
            default -> {return null;}
        }
    }

    private SOChart generateBarChart(){
        SOChart chart = new SOChart();
        chart.setSize(width, height);

        for (int i = 0; i < labels.length; i++) {
            BarChart bar =
                    new BarChart(
                            Dashboard.castObjectByCoordinateType(this.coordinateConfiguration.getAxis(0).getDataType(), labels[i]),
                            Dashboard.castObjectByCoordinateType(this.coordinateConfiguration.getAxis(1).getDataType(), data[i]));
            bar.setName(labels[i].toString());
            bar.setColors(this.colors);
            bar.plotOn(this.coordinateConfiguration);
            chart.add(bar);
        }
        return chart;
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
