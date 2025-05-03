package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard;

import com.storedobject.chart.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.DashboardData.ChartData;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.DashboardData.ChartPoint;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.InterfaceComponent;
import org.yaml.snakeyaml.util.Tuple;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Slf4j
@SuperBuilder
@Getter
@Tag("DashboardComponent")
public class Dashboard extends InterfaceComponent {
    protected final RectangularCoordinate coordinateConfiguration;
    protected final List<ChartData<?,?>> data = new ArrayList<>();

    protected final ChartType type;
    protected final String width;
    protected final String height;

    protected SOChart chart;
    protected final List<Tuple<AbstractChart, ChartData<?,?>>> pairs = new ArrayList<>();

    @Override
    public Component getComponent(){
        this.chart = new SOChart();
        this.chart.setSize(width, height);
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.addClassName("coloredBorder");
        switch (type){
            case BAR -> {
                generateBarChart();
                layout.add(this.chart);
                return layout;
            }
            default -> {return null;}
        }
    }

    public void update(AbstractDataProvider<?> x, AbstractDataProvider<?> y){
        log.error("Los filtros están desactivados por ahora, sorry");
        try{
            switch (type){
                case BAR -> {
                }
                default -> {return;}
            }
            this.chart.update(false);
            UI.getCurrent().push();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void generateBarChart(){
        this.chart.clear();
        for (ChartData<?,?> d : data) {
            AbstractDataProvider<?> xAxis = castObjectByCoordinateType(this.coordinateConfiguration.getAxis(0).getDataType(), d.flatten().stream().map(ChartPoint::getXValue).toArray());
            AbstractDataProvider<?> yAxis = castObjectByCoordinateType(this.coordinateConfiguration.getAxis(1).getDataType(), d.flatten().stream().map(ChartPoint::getYValue).toArray());
            BarChart bar = new BarChart(xAxis, yAxis);
            bar.setColors(d.getColor());
            bar.setName(d.getLabel());
            bar.plotOn(this.coordinateConfiguration);
            this.chart.add(bar);
        }
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

    public static Dashboard createSimpleDashboard(String name, ChartType type, RectangularCoordinate coordinatesConfig ){
        return Dashboard.builder().name(name).type(type).coordinateConfiguration(coordinatesConfig).build();
    }

    public <T,J> void addData(Object[] XData, T[] XObjects, Object[] YData, J[] YObjects, String name, Color color){
        this.data.add(new ChartData<>(XData, YData, XObjects, YObjects, color, name));
    }
}
