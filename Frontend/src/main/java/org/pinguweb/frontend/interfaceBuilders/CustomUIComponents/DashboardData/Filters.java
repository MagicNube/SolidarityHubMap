package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.DashboardData;

import com.storedobject.chart.AbstractDataProvider;
import com.storedobject.chart.AbstractDataStream;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.function.SerializablePredicate;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Filters {

    private Dashboard dashboard;

    public Filters(Dashboard dashboard){
        this.dashboard = dashboard;
    }
    public Component generateFilter(ChartData<?,?> firstData){
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

        // 4. Valor a comparar
        TextField value = new TextField("Valor");

        // 5. Botón de aplicar
        Button apply = new Button("Aplicar", evt -> {
            if (classname.isEmpty() || property.isEmpty()
                    || operations.isEmpty() || value.isEmpty()) {
                Notification.show("Selecciona todos los campos del filtro");
                return;
            }
            // Construir y aplicar predicate
            SerializablePredicate<ChartPoint<?,?>> filtro = buildPredicate(
                    classname.getValue().getName(),
                    property.getValue(),
                    operations.getValue(),
                    value.getValue()
            );

            // Filtrar datos X
            List<Object> filteredX = this.dashboard.getData().flatten().stream()
                    .filter(filtro::test)
                    .map(ChartPoint::getXValue)
                    .toList();
            AbstractDataProvider<?> xFiltrado = new AbstractDataStream<>(
                    this.dashboard.getXAxis().getDataType(),
                    filteredX.stream()
            );

            // Filtrar datos Y
            List<Object> filteredY = this.dashboard.getData().flatten().stream()
                    .filter(filtro::test)
                    .map(ChartPoint::getYValue)
                    .toList();
            AbstractDataProvider<?> yFiltrado = new AbstractDataStream<>(
                    this.dashboard.getYAxis().getDataType(),
                    filteredY.stream()
            );

            // Actualizar dashboard con los datos filtrados
            this.dashboard.update(xFiltrado, yFiltrado);
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

        // 6. Botón de reiniciar (X en rojo)
        Button reset = new Button(VaadinIcon.CLOSE.create());
        reset.addThemeVariants(ButtonVariant.LUMO_ERROR); // Botón rojo
        reset.getElement().setAttribute("aria-label", "Reiniciar gráfica");
        reset.addClickListener(evt -> {
            this.dashboard.update(this.dashboard.getXAxis(), this.dashboard.getYAxis());
        });

        hmain.add(
                classname,
                property,
                operations,
                value,
                apply,
                reset
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
    private SerializablePredicate<ChartPoint<?, ?>> buildPredicate(
            String className,
            String propertyName,
            String operation,
            String rawValue) {

        return cp -> {
            Object target = cp.getXObject().getClass().getName().equals(className)
                    ? cp.getXObject()
                    : cp.getYObject();

            PropertyDescriptor pd =
                    null;
            try {
                pd = new PropertyDescriptor(propertyName, target.getClass());
            } catch (IntrospectionException e) {
                throw new RuntimeException(e);
            }
            Method getter = pd.getReadMethod();
            Object fieldValue = null;
            try {
                fieldValue = getter.invoke(target);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }

            Object compareTo = parseValue(fieldValue, rawValue);

            // 3) Comparar según la operación elegida
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

    /**
     * Convierte rawValue al mismo tipo que fieldValue, si es posible.
     */
    private Object parseValue(Object fieldValue, String rawValue) {
        Class<?> type = fieldValue.getClass();
        if (type == Integer.class) {
            return Integer.valueOf(rawValue);
        } else if (type == Double.class) {
            return Double.valueOf(rawValue);
        } else if (type == Boolean.class) {
            return Boolean.valueOf(rawValue);
        } // … más tipos según tu modelo
        return rawValue; // por defecto, comparar con la cadena
    }
}
