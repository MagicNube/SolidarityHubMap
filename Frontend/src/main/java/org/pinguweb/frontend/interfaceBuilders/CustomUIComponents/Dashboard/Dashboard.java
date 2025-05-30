package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard;

import com.storedobject.chart.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
            case PIE -> {
                generatePieChart();
                layout.add(this.chart);
                return layout;
            }
            case DONUTCHART -> {
                generateDonutChart();
                layout.add(this.chart);
                return layout;
            }
            case STACKED_BAR -> {
                generateStackedBarChart();
                layout.add(this.chart);
                return layout;
            }
            case LINECHART -> {
                generateLineChart();
                layout.add(this.chart);
                return layout;
            }
            default -> {throw new RuntimeException("Tipo de chart no añadida");}
        }
    }

    public void update(AbstractChart[] data){
        try{
            this.chart.updateData(data);
            this.chart.update(false);
        } catch (Exception e) {
            log.error(e.getMessage(), Arrays.toString(e.getStackTrace()));
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
            this.pairs.add(new Tuple<>(bar, d));
            this.chart.add(bar);
        }
    }
    private void generateLineChart(){
        this.chart.clear();
        if (data.isEmpty()) {return;}
        for(ChartData<?,?> d: data){
            AbstractDataProvider<?> xAxis = castObjectByCoordinateType(this.coordinateConfiguration.getAxis(0).getDataType(), d.flatten().stream().map(ChartPoint::getXValue).toArray());
            AbstractDataProvider<?> yAxis = castObjectByCoordinateType(this.coordinateConfiguration.getAxis(1).getDataType(), d.flatten().stream().map(ChartPoint::getYValue).toArray());
            LineChart lineChart = new LineChart(xAxis, yAxis);
            lineChart.setColors(d.getColor());
            lineChart.setName(d.getLabel());
            lineChart.plotOn(this.coordinateConfiguration);
            this.pairs.add(new Tuple<>(lineChart, d));
            this.chart.add(lineChart);
        }
    }
    private void generateDonutChart() {
        this.chart.clear();
        for (ChartData<?,?> d : data) {
            AbstractDataProvider<?> xAxis = castObjectByCoordinateType(this.coordinateConfiguration.getAxis(0).getDataType(), d.flatten().stream().map(ChartPoint::getXValue).toArray());
            AbstractDataProvider<?> yAxis = castObjectByCoordinateType(this.coordinateConfiguration.getAxis(1).getDataType(), d.flatten().stream().map(ChartPoint::getYValue).toArray());
            DonutChart donut = new DonutChart(xAxis, (DataProvider) yAxis);
            donut.setColors(d.getColor());
            donut.setName(d.getLabel());
            donut.plotOn(this.coordinateConfiguration);
            this.pairs.add(new Tuple<>(donut, d));
            this.chart.add(donut);
        }
    }

    private void generatePieChart(){
        this.chart.clear();
        for (ChartData<?,?> d : data) {
            AbstractDataProvider<?> xAxis = castObjectByCoordinateType(this.coordinateConfiguration.getAxis(0).getDataType(), d.flatten().stream().map(ChartPoint::getXValue).toArray());
            AbstractDataProvider<?> yAxis = castObjectByCoordinateType(this.coordinateConfiguration.getAxis(1).getDataType(), d.flatten().stream().map(ChartPoint::getYValue).toArray());
            PieChart pie = new PieChart(xAxis, (DataProvider) yAxis);
            pie.setColors(d.getColor());
            pie.setName(d.getLabel());
            pie.plotOn(this.coordinateConfiguration);
            this.pairs.add(new Tuple<>(pie, d));
            this.chart.add(pie);
        }
    }


    private void generateStackedBarChart(){
        this.chart.clear();
        for (ChartData<?,?> d : data) {
            AbstractDataProvider<?> xAxis = castObjectByCoordinateType(this.coordinateConfiguration.getAxis(0).getDataType(), d.flatten().stream().map(ChartPoint::getXValue).toArray());
            AbstractDataProvider<?> yAxis = castObjectByCoordinateType(this.coordinateConfiguration.getAxis(1).getDataType(), d.flatten().stream().map(ChartPoint::getYValue).toArray());
            BarChart bar = new BarChart(xAxis, yAxis);
            bar.setStackName(d.getLabel());
            bar.setColors(d.getColor());
            bar.setName(d.getLabel());
            bar.plotOn(this.coordinateConfiguration);
            this.pairs.add(new Tuple<>(bar, d));
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

    /**
     * Crea un dashboard sencillo con configuración básica de tamaño, nombre, tipo y coordenadas.
     *
     * @param name                 el nombre que se asignará al dashboard
     * @param type                 el tipo de gráfico que contendrá el dashboard
     * @param coordinatesConfig    la configuración de coordenadas rectangulares a usar
     * @return un nuevo {@link Dashboard} configurado con los parámetros indicados,
     *         con ancho al 100% y altura fija de 700px
     */
    public static Dashboard createSimpleDashboard(
            String name,
            ChartType type,
            RectangularCoordinate coordinatesConfig
    ) {
        return Dashboard.builder()
                .width("100%")
                .height("700px")
                .name(name)
                .type(type)
                .coordinateConfiguration(coordinatesConfig)
                .build();
    }

    /**
     * Añade un conjunto de datos al dashboard para su representación en un gráfico.
     * <p>
     * Este método es genérico en los tipos {@code T} y {@code J}, que representan
     * metadatos (o “objetos”) asociados a los ejes X e Y, respectivamente.
     * </p>
     *
     * @param <T>       el tipo de los objetos asociados a los valores del eje X
     * @param <J>       el tipo de los objetos asociados a los valores del eje Y
     * @param XData     array de valores para el eje X (por ejemplo, fechas o categorías)
     * @param XObjects  array de objetos de tipo {@code T} relacionados a cada valor de X
     * @param YData     array de valores para el eje Y (por ejemplo, medidas o cantidades)
     * @param YObjects  array de objetos de tipo {@code J} relacionados a cada valor de Y
     * @param name      etiqueta o nombre de la serie de datos
     * @param color     color para representar la serie en el gráfico
     */
    public <T, J> void addData(
            Object[] XData,
            T[][] XObjects,
            Object[] YData,
            J[][] YObjects,
            String name,
            Color[] color
    ) {
        this.data.add(new ChartData<>(
                XData,
                YData,
                XObjects,
                YObjects,
                color,
                name
        ));
    }

}