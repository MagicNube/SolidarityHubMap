package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents;

import com.storedobject.chart.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.function.SerializablePredicate;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.DashboardData.ChartData;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.DashboardData.ChartPoint;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuperBuilder
@Getter
@Tag("DashboardComponent")
public class Dashboard extends InterfaceComponent{
    protected final Color colors;
    protected final RectangularCoordinate coordinateConfiguration;
    protected final ChartData data;
    protected ListDataProvider<ChartPoint> filteredData;
    protected final ChartType type;
    protected boolean hasFilter;
    protected final String width;
    protected final String height;
    protected SOChart chart;

    @Override
    public Component getComponent(){
        filteredData = new ListDataProvider<>(data.flatten());
        this.chart = new SOChart();
        this.chart.setSize(width, height);
        VerticalLayout layout = generateChartsComponents();



        System.out.println(filteredData.getItems());
        switch (type){
            case BAR -> {
                generateBarChart(filteredData);
                layout.add(this.chart);
                return layout;
            }
            default -> {return null;}
        }
    }

    private Component generateFilter(ChartData firstData){
        HorizontalLayout hmain = new HorizontalLayout();
        hmain.setAlignItems(FlexComponent.Alignment.END);

        // 1. ComboBox de clases
        ComboBox<Class<?>> classname = new ComboBox<>("Clase");
        // Aquí podrías añadir más clases aparte de ChartData

        assert(firstData.getValueClass() != null && firstData.getLabelClass() != null);
        classname.setItems(firstData.getValueClass(), firstData.getLabelClass());

        classname.setItemLabelGenerator(Class::getSimpleName);

        // 2. ComboBox de propiedades (se llena tras seleccionar clase)
        ComboBox<String> property = new ComboBox<>("Propiedad");

        // 3. Operaciones
        ComboBox<String> operations = new ComboBox<>("Operación");
        operations.setItems("=", "!=", ">", "<", "contains");

        // 4. Valor a comparar
        TextField value = new TextField("Valor");

        // 5. Botón de aplicar
        Button apply = new Button("Aplicar", evt -> {
            if (classname.isEmpty() || property.isEmpty() || operations.isEmpty() || value.isEmpty()) {
                Notification.show("Selecciona todos los campos del filtro");
                return;
            }
            // Construir y aplicar predicate
            SerializablePredicate<ChartPoint> filtro = buildPredicate(
                    classname.getValue(),
                    property.getValue(),
                    operations.getValue(),
                    value.getValue()
            );
            // Resetear y volver a setear el provider
            filteredData.clearFilters();
            filteredData.addFilter(filtro);
            filteredData.refreshAll();
            System.out.println(filteredData.getItems());
            generateBarChart(filteredData);
        });

        // Listener: al cambiar de clase, relleno propiedades
        classname.addValueChangeListener(evt -> {
            Class<?> cls = evt.getValue();
            if (cls == null) {
                property.clear();
                property.setItems(Collections.emptyList());
            } else {
                property.setItems(getFieldNames(cls));
            }
        });

        hmain.add(
                classname,
                property,
                operations,
                value,
                apply
        );

        return hmain;
    }

    /**
     * Devuelve la lista de nombres de campo de la clase (incluye privados).
     */
    private List<String> getFieldNames(Class<?> cls) {
        return Arrays.stream(cls.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toList());
    }

    /**
     * Crea un Predicate<ChartData> según los parámetros elegidos.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private SerializablePredicate<ChartPoint> buildPredicate(
            Class<?> cls, String fieldName, String operation, String rawValue) {

        return item -> {
            try {
                // Solo filtramos si el objeto es instancia de la clase
                if (!cls.isInstance(item)) {
                    return false;
                }
                Field f = item.getClass().getDeclaredField(fieldName);
                f.setAccessible(true);
                Object fieldValue = f.get(item);

                // Comparar según operación
                switch (operation) {
                    case "=":
                        return Objects.equals(fieldValue, parseValue(fieldValue, rawValue));
                    case "!=":
                        return !Objects.equals(fieldValue, parseValue(fieldValue, rawValue));
                    case ">":
                        return ((Comparable) fieldValue)
                                .compareTo(parseValue(fieldValue, rawValue)) > 0;
                    case "<":
                        return ((Comparable) fieldValue)
                                .compareTo(parseValue(fieldValue, rawValue)) < 0;
                    case "contains":
                        return String.valueOf(fieldValue)
                                .contains(rawValue);
                    default:
                        return true;
                }
            } catch (Exception e) {
                // En caso de error, no excluimos el elemento
                return true;
            }
        };
    }

    /**
     * Convierte rawValue al mismo tipo que fieldValue, si es posible.
     */
    private Object parseValue(Object fieldValue, String rawValue) {
        if (fieldValue instanceof Number) {
            // Para números, intentamos Double
            return Double.valueOf(rawValue);
        } else if (fieldValue instanceof Boolean) {
            return Boolean.valueOf(rawValue);
        } else {
            // Por defecto comparamos como String
            return rawValue;
        }
    }

    private VerticalLayout generateChartsComponents(){
        VerticalLayout vlayout = new VerticalLayout();
        HorizontalLayout hlayout = new HorizontalLayout();
        if (this.hasFilter) {
            hlayout.add(generateFilter(this.getData()));
            vlayout.add(hlayout);
        }
        return vlayout;
    }

    private void generateBarChart(ListDataProvider<ChartPoint> data){
        this.chart.clear();
        for (ChartPoint p : data.getItems()) {
            BarChart bar =
                    new BarChart(
                            Dashboard.castObjectByCoordinateType(this.coordinateConfiguration.getAxis(0).getDataType(), p.getLabel()),
                            Dashboard.castObjectByCoordinateType(this.coordinateConfiguration.getAxis(1).getDataType(), p.getValue()));
            bar.setName(p.getLabel().toString());
            bar.setColors(this.colors);
            bar.plotOn(this.coordinateConfiguration);
            this.chart.add(bar);
        }
        try {
            this.chart.update();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
