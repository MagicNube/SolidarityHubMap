package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents;

import com.storedobject.chart.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.DashboardData.ChartData;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.DashboardData.ChartPoint;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.DashboardData.Filters;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;


@SuperBuilder
@Getter
@Tag("DashboardComponent")
public class Dashboard extends InterfaceComponent{
    protected final Color colors;
    protected final RectangularCoordinate coordinateConfiguration;
    protected final ChartData<?,?> data;
    private AbstractDataProvider<?> xAxis;
    private AbstractDataProvider<?> yAxis;

    protected final ChartType type;
    protected boolean hasFilter;
    protected final String width;
    protected final String height;
    protected SOChart chart;
    private XYChart chartobject;

    @Override
    public Component getComponent(){
        xAxis = castObjectByCoordinateType(this.coordinateConfiguration.getAxis(0).getDataType(), data.flatten().stream().map(ChartPoint::getXValue).toArray());
        yAxis = castObjectByCoordinateType(this.coordinateConfiguration.getAxis(1).getDataType(), data.flatten().stream().map(ChartPoint::getYValue).toArray());

        this.chart = new SOChart();
        this.chart.setSize(width, height);
        VerticalLayout layout = generateChartsComponents();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.addClassName("coloredBorder");
        switch (type){
            case BAR -> {
                BarChart bar = generateBarChart(this.xAxis, this.yAxis);
                this.chartobject = bar;
                this.chart.add(bar);
                this.chart.disableDefaultLegend();
                layout.add(this.chart);
                return layout;
            }
            default -> {return null;}
        }
    }

    public void update(AbstractDataProvider<?> x, AbstractDataProvider<?> y){
        try{
            switch (type){
                case BAR -> {
                    this.chartobject.setYData(y);
                    this.chartobject.setXData(x);
                    this.chart.updateData(chartobject);
                }
                default -> {return;}
            }
            this.chart.update(false);
            UI.getCurrent().push();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private VerticalLayout generateChartsComponents(){
        VerticalLayout vlayout = new VerticalLayout();
        HorizontalLayout hlayout = new HorizontalLayout();
        Filters filter = new Filters(this);
        if (this.hasFilter) {
            hlayout.add(filter.generateFilter(this.getData()));
            vlayout.add(hlayout);
        }
        return vlayout;
    }

    private BarChart generateBarChart(AbstractDataProvider<?> xAxis, AbstractDataProvider<?> yAxis){
        BarChart bar = new BarChart(xAxis, yAxis);
        bar.setColors(this.colors);
        bar.plotOn(this.coordinateConfiguration);
        return bar;
    }

    public static AbstractDataProvider<?> castObjectByCoordinateType(
            DataType tipo, Object[] valor) {

        return switch (tipo) {
            case CATEGORY -> {
                // Convertir each Object to String y llamar al constructor varargs
                String[] cats = Arrays.stream(valor)
                        .map(Object::toString)
                        .toArray(String[]::new);
                yield new CategoryData(cats);
            }
            case NUMBER, LOGARITHMIC -> {
                // Convertir each Object a Number
                Number[] nums = Arrays.stream(valor)
                        .map(v -> (Number) v)
                        .toArray(Number[]::new);
                yield new Data(nums);
            }
            case DATE -> {
                // Convertir each Object a LocalDate
                LocalDate[] dates = Arrays.stream(valor)
                        .map(v -> (LocalDate) v)
                        .toArray(LocalDate[]::new);
                yield new DateData(dates);
            }
            case TIME -> {
                // Convertir each Object a LocalDateTime
                LocalDateTime[] times = Arrays.stream(valor)
                        .map(v -> (LocalDateTime) v)
                        .toArray(LocalDateTime[]::new);
                yield new TimeData(times);
            }
            default -> throw new IllegalArgumentException(
                    "Tipo no soportado: " + tipo +
                            " (valores: " + Arrays.toString(valor) + ")");
        };
    }
}
