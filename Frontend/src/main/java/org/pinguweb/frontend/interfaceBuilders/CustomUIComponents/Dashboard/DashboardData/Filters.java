package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.DashboardData;

import com.storedobject.chart.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.function.SerializablePredicate;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.Dashboard;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.InterfaceComponent;
import org.yaml.snakeyaml.util.Tuple;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@SuperBuilder
public class Filters extends InterfaceComponent {

    private final List<Dashboard> dashboards = new ArrayList<>();

    protected Filters(InterfaceComponentBuilder<?, ?> b) {
        super(b);
    }

    public void addDashboard(InterfaceComponent dashboard){
        if (!(dashboard instanceof Dashboard)){return;}
        this.dashboards.add((Dashboard) dashboard);
    }

    public void addDashboard(List<InterfaceComponent> dashboards){
        for (InterfaceComponent d : dashboards){
            if (d instanceof Dashboard){
                this.dashboards.add((Dashboard) d);
            }
        }
    }

    public Component generateFilter(List<ChartData<?, ?> > firstData) {
        HorizontalLayout hmain = new HorizontalLayout();
        hmain.setAlignItems(FlexComponent.Alignment.END);

        // 1. ComboBox de clases
        ComboBox<Class<?>> classname = new ComboBox<>("Clase");

        Set<Class<?>> classes = new HashSet<>();

        ChartData<?,?> d = firstData.get(0);
        classes.add(d.getLabelObjects()[0][0].getClass());
        classes.add(d.getPointObjects()[0][0].getClass());

        classname.setItems(
            classes
        );

        classname.setItemLabelGenerator(Class::getSimpleName);

        // 2. ComboBox de propiedades (se llena tras seleccionar clase)
        ComboBox<String> property = new ComboBox<>("Propiedad");

        // 3. Operaciones
        ComboBox<String> operations = new ComboBox<>("Operación");
        operations.setItems("=", "!=", ">", "<", "contains");

        // 4. Contenedor para el campo de valor dinámico
        Div valueContainer = new Div();

        // 5. Botón de aplicar
        Button apply = new Button("Aplicar", evt -> {
            Component valueField = valueContainer.getChildren().findFirst().orElse(null);
            if (classname.isEmpty() || property.isEmpty()
                    || operations.isEmpty() || valueField == null
                    || getValue(valueField) == null) {
                Notification.show("Selecciona todos los campos del filtro");
                return;
            }

            SerializablePredicate<ChartPoint<?, ?>> filtro = buildPredicate(
                    classname.getValue().getName(),
                    property.getValue(),
                    operations.getValue(),
                    getValue(valueField).toString()
            );

            //TODO: Cambiar aqui
            List<AbstractChart> data = new ArrayList<>();

            for (Dashboard dashboard : dashboards) {
                for (Tuple<AbstractChart, ChartData<?,?>> pair : dashboard.getPairs()) {

                    List<Object> filteredX = pair._2().flatten().stream()
                            .filter(filtro::test)
                            .map(ChartPoint::getXValue)
                            .toList();

                    AbstractDataProvider<?> xFiltrado = new AbstractDataStream<>(
                            dashboard.getCoordinateConfiguration().getAxis(0).getDataType(),
                            filteredX.stream());

                    List<Object> filteredY = pair._2().flatten().stream()
                            .filter(filtro::test)
                            .map(ChartPoint::getYValue)
                            .toList();

                    AbstractDataProvider<?> yFiltrado = new AbstractDataStream<>(
                            dashboard.getCoordinateConfiguration().getAxis(1).getDataType(),
                            filteredY.stream()
                    );

                    addUpdatedChart(dashboard, pair, xFiltrado, yFiltrado, data);
                }
                dashboard.update(data.toArray(AbstractChart[]::new));
            }
        });

        // 6. Botón de reiniciar
        Button reset = new Button(VaadinIcon.CLOSE.create());
        reset.addThemeVariants(ButtonVariant.LUMO_ERROR);
        reset.getElement().setAttribute("aria-label", "Reiniciar gráfica");
        reset.addClickListener(evt -> {
            for(Dashboard dashboard : dashboards){
                List<AbstractChart> data = new ArrayList<>();
                for (Tuple<AbstractChart, ChartData<?,?>> pair : dashboard.getPairs()) {
                    AbstractDataProvider<?> xAxis = Dashboard.castObjectByCoordinateType(dashboard.getCoordinateConfiguration().getAxis(0).getDataType(), pair._2().flatten().stream().map(ChartPoint::getXValue).toArray());
                    AbstractDataProvider<?> yAxis = Dashboard.castObjectByCoordinateType(dashboard.getCoordinateConfiguration().getAxis(1).getDataType(), pair._2().flatten().stream().map(ChartPoint::getYValue).toArray());

                    addUpdatedChart(dashboard, pair, xAxis, yAxis, data);
                }

                dashboard.update(data.toArray(AbstractChart[]::new));
            }
        });

        // Listener: al cambiar de clase, relleno propiedades
        classname.addValueChangeListener(evt -> {
            Class<?> cls = evt.getValue();
            property.clear();
            property.setItems(
                    cls == null ? Collections.emptyList() : getFieldNames(cls)
            );
            valueContainer.removeAll();
        });

        // Listener: al cambiar propiedad, crear campo según tipo
        property.addValueChangeListener(evt -> {
            valueContainer.removeAll();
            String prop = evt.getValue();
            if (prop == null || classname.isEmpty()) {
                return;
            }
            try {
                Class<?> cls = classname.getValue();
                Field field = cls.getDeclaredField(prop);
                valueContainer.add(createFieldByType(field.getType()));
            } catch (NoSuchFieldException e) {
                Notification.show("Error al obtener el tipo de propiedad");
            }
        });

        hmain.add(
                classname,
                property,
                operations,
                valueContainer,
                apply,
                reset
        );
        return hmain;
    }
//a
    private void addUpdatedChart(Dashboard dashboard, Tuple<AbstractChart, ChartData<?, ?>> pair, AbstractDataProvider<?> xFiltrado, AbstractDataProvider<?> yFiltrado, List<AbstractChart> data) {
        switch (dashboard.getType()) {
            case BAR, STACKED_BAR -> {
                BarChart bar = (BarChart) pair._1();
                bar.setXData(xFiltrado);
                bar.setYData(yFiltrado);
                data.add(bar);
            }
            case PIE -> {
                PieChart pie = (PieChart) pair._1();
                pie.setItemNames(xFiltrado);

                Number[] nums = yFiltrado.stream()
                        .map(v -> (Number) v)
                        .toArray(Number[]::new);

                pie.setData(new Data(nums));

                data.add(pie);
            }
        }
    }

    // TODO: No pilla si es enum porque los DTOs lo guardan como string
    private Component createFieldByType(Class<?> type) {
        if (type.isEnum()) {
            log.debug("Es enum");
            Class<? extends Enum<?>> enumType = (Class<? extends Enum<?>>) type;
            ComboBox<Enum<?>> combo = new ComboBox<>();
            combo.setItems(enumType.getEnumConstants());
            combo.setItemLabelGenerator(Enum::name);
            return combo;
        }
        else if (Boolean.class.equals(type) || boolean.class.equals(type)) {
            return new Checkbox();
        }
        else if (Number.class.isAssignableFrom(type)
                || (type.isPrimitive() && !boolean.class.equals(type))) {

            if (Integer.class.equals(type) || int.class.equals(type)) {
                return new IntegerField();
            } else {
                return new NumberField();
            }
        }
        else if (LocalDate.class.equals(type) || Date.class.equals(type)) {
            return new DatePicker();
        } else if (LocalDateTime.class.equals(type)) {
            return new DateTimePicker();
        }
        else {
            log.debug("No enum?");
            return new TextField();
        }
    }

    /** Obtiene el valor del componente dinámico */
    private Object getValue(Component comp) {
        if (comp instanceof Checkbox cb) {
            return cb.getValue();
        } else if (comp instanceof IntegerField ifld) {
            return ifld.getValue();
        } else if (comp instanceof NumberField nf) {
            return nf.getValue();
        } else if (comp instanceof DatePicker dp) {
            return dp.getValue();
        } else if (comp instanceof DateTimePicker dtp) {
            return dtp.getValue();
        } else if (comp instanceof TextField tf) {
            return tf.getValue();
        }
        return null;
    }

    /** Devuelve la lista de nombres de campo de la clase (incluye privados). */
    private List<String> getFieldNames(Class<?> cls) {
        return Arrays.stream(cls.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toList());
    }

    /** Crea un Predicate<ChartPoint> según los parámetros elegidos. */
    private SerializablePredicate<ChartPoint<?, ?>> buildPredicate(
            String className,
            String propertyName,
            String operation,
            String rawValue) {

        return cp -> {
            Object target = cp.getXObject()[0].getClass().getName().equals(className)
                    ? cp.getXObject()[0]
                    : cp.getYObject().length > 0 ? cp.getYObject()[0] : null;

            if (target == null) {return false;}

            PropertyDescriptor pd = null;
            Object fieldValue;

            try {
                // 1. Intentamos con JavaBeans
                pd = new PropertyDescriptor(propertyName, target.getClass());
                Method getter = pd.getReadMethod();
                fieldValue = getter.invoke(target);
            } catch (IntrospectionException ex) {
                try {
                    Field f = target.getClass().getDeclaredField(propertyName);
                    f.setAccessible(true);
                    fieldValue = f.get(target);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(
                            "No se pudo acceder a la propiedad '" + propertyName + "' en " +
                                    target.getClass().getName(), e);
                }
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            assert pd != null;
            Method getter = pd.getReadMethod();
            try {
                fieldValue = getter.invoke(target);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }

            Object compareTo = parseValue(fieldValue, rawValue);

            switch (operation) {
                case "=":
                    return Objects.equals(fieldValue, compareTo);
                case "!=":
                    return !Objects.equals(fieldValue, compareTo);
                case ">":
                    return ((Comparable) fieldValue).compareTo(compareTo) > 0;
                case "<":
                    return ((Comparable) fieldValue).compareTo(compareTo) < 0;
                case "contains":
                    return fieldValue.toString().contains(rawValue);
                default:
                    return false;
            }
        };
    }

    /** Convierte rawValue al mismo tipo que fieldValue, si es posible. */
    private Object parseValue(Object fieldValue, String rawValue) {
        Class<?> type = fieldValue.getClass();
        if (type == Integer.class) {
            return Integer.valueOf(rawValue);
        } else if (type == Double.class) {
            return Double.valueOf(rawValue);
        } else if (type == Boolean.class) {
            return Boolean.valueOf(rawValue);
        } else if (type == LocalDate.class) {
            return LocalDate.parse(rawValue);
        } else if (type == LocalDateTime.class) {
            return LocalDateTime.parse(rawValue);
        }
        return rawValue;
    }

    @Override
    public Component getComponent() {
        HorizontalLayout hlayout = new HorizontalLayout();
        hlayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        hlayout.setWidthFull();
        //TODO: Cambiar aquí
        hlayout.add(this.generateFilter(dashboards.get(0).getData()));
        return hlayout;
    }
}


