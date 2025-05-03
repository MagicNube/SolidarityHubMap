package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard;

import com.storedobject.chart.AbstractDataProvider;
import com.storedobject.chart.AbstractDataStream;
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
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.DashboardData.ChartData;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.DashboardData.ChartPoint;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.InterfaceComponent;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@SuperBuilder
public class Filters extends InterfaceComponent {

    private final List<Dashboard> dashboards = new ArrayList<>();

    protected Filters(InterfaceComponentBuilder<?, ?> b) {
        super(b);
    }

    public void addDashboard(Dashboard dashboard){
        this.dashboards.add(dashboard);
    }

    public Component generateFilter(ChartData<?, ?> firstData) {
        HorizontalLayout hmain = new HorizontalLayout();
        hmain.setAlignItems(FlexComponent.Alignment.END);

        // 1. ComboBox de clases
        ComboBox<Class<?>> classname = new ComboBox<>("Clase");
        classname.setItems(
                firstData.getLabelObjects()[0].getClass(),
                firstData.getPointObjects()[0].getClass()
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

            for (Dashboard dashboard : dashboards) {
                List<Object> filteredX = dashboard.getData().flatten().stream()
                        .filter(filtro::test)
                        .map(ChartPoint::getXValue)
                        .toList();
                AbstractDataProvider<?> xFiltrado = new AbstractDataStream<>(
                        dashboard.getXAxis().getDataType(),
                        filteredX.stream()
                );

                List<Object> filteredY = dashboard.getData().flatten().stream()
                        .filter(filtro::test)
                        .map(ChartPoint::getYValue)
                        .toList();
                AbstractDataProvider<?> yFiltrado = new AbstractDataStream<>(
                        dashboard.getYAxis().getDataType(),
                        filteredY.stream()
                );

                dashboard.update(xFiltrado, yFiltrado);
            }
        });

        // 6. Botón de reiniciar
        Button reset = new Button(VaadinIcon.CLOSE.create());
        reset.addThemeVariants(ButtonVariant.LUMO_ERROR);
        reset.getElement().setAttribute("aria-label", "Reiniciar gráfica");
        reset.addClickListener(evt -> {
            for(Dashboard dashboard : dashboards){
                dashboard.update(dashboard.getXAxis(), dashboard.getYAxis());
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

    /** Crea un campo de input acorde al tipo */
    private Component createFieldByType(Class<?> type) {
        if (Boolean.class.equals(type) || boolean.class.equals(type)) {
            return new Checkbox();
        } else if (Number.class.isAssignableFrom(type)
                || (type.isPrimitive() && !boolean.class.equals(type))) {
            if (Integer.class.equals(type) || int.class.equals(type)) {
                return new IntegerField();
            } else if (Double.class.equals(type) || double.class.equals(type)
                    || Float.class.equals(type) || float.class.equals(type)) {
                return new NumberField();
            }
            return new NumberField();
        } else if (LocalDate.class.equals(type) || Date.class.equals(type)) {
            return new DatePicker();
        } else if (LocalDateTime.class.equals(type)) {
            return new DateTimePicker();
        } else {
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
            Object target = cp.getXObject().getClass().getName().equals(className)
                    ? cp.getXObject()
                    : cp.getYObject();

            PropertyDescriptor pd;
            try {
                pd = new PropertyDescriptor(propertyName, target.getClass());
            } catch (IntrospectionException e) {
                throw new RuntimeException(e);
            }
            Method getter = pd.getReadMethod();
            Object fieldValue;
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
        hlayout.add(this.generateFilter(dashboards.get(0).getData()));
        return hlayout;
    }
}


