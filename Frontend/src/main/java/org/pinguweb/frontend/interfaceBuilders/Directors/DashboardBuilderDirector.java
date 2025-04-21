package org.pinguweb.frontend.interfaceBuilders.Directors;

import com.storedobject.chart.*;
import com.vaadin.flow.component.Component;
import org.pinguweb.frontend.interfaceBuilders.Builders.DashboardBuilder;
import org.pinguweb.frontend.interfaceBuilders.customUIComponents.ChartType;
import org.pinguweb.frontend.interfaceBuilders.customUIComponents.Dashboard;
import org.pinguweb.frontend.interfaceBuilders.customUIComponents.InterfaceComponent;


import java.util.ArrayList;
import java.util.List;

public class DashboardBuilderDirector {
    DashboardBuilder builder = new DashboardBuilder();

    public Component buildTest(){
        Dashboard first = Dashboard.builder()
                .chart(new SOChart())
                .name("test 1")
                .colors(new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)))
                .labels(new String[]{"TasksCr", "TasksCo", "NeedsCr", "NeedsCo"})
                .data(new Integer[]{0, 1, 2, 3})
                .coordinateConfiguration(
                        new RectangularCoordinate(
                                new XAxis(DataType.CATEGORY),
                                new YAxis(DataType.NUMBER)
                        )
                )
                .width("500px")
                .height("400px")
                .type(ChartType.BAR)
                .build();

        Dashboard second = Dashboard.builder()
                .chart(new SOChart())
                .name("test 1")
                .colors(new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)))
                .labels(new String[]{"TasksCr", "TasksCo", "NeedsCr", "NeedsCo"})
                .data(new Integer[]{0, 1, 2, 3})
                .coordinateConfiguration(
                        new RectangularCoordinate(
                                new XAxis(DataType.CATEGORY),
                                new YAxis(DataType.NUMBER)
                        )
                )
                .width("250px")
                .height("400px")
                .type(ChartType.BAR)
                .build();

        Dashboard third = Dashboard.builder()
                .chart(new SOChart())
                .name("test 1")
                .colors(new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)))
                .labels(new String[]{"TasksCr", "TasksCo", "NeedsCr", "NeedsCo"})
                .data(new Integer[]{0, 1, 2, 3})
                .coordinateConfiguration(
                        new RectangularCoordinate(
                                new XAxis(DataType.CATEGORY),
                                new YAxis(DataType.NUMBER)
                        )
                )
                .width("250px")
                .height("200px")
                .type(ChartType.BAR)
                .build();

        Dashboard forth = Dashboard.builder()
                .chart(new SOChart())
                .name("test 1")
                .colors(new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)))
                .labels(new String[]{"TasksCr", "TasksCo", "NeedsCr", "NeedsCo"})
                .data(new Integer[]{0, 1, 2, 3})
                .coordinateConfiguration(
                        new RectangularCoordinate(
                                new XAxis(DataType.CATEGORY),
                                new YAxis(DataType.NUMBER)
                        )
                )
                .type(ChartType.BAR)
                .width("250px")
                .height("200px")
                .build();

        builder.reset();

        third.addBelowComponent(forth);

        List<InterfaceComponent> sides = new ArrayList<>();
        sides.add(second);
        sides.add(third);

        return builder
            .setTile("Test")
            .setSubtitle("Doble test")
            .addBelow(first)
            .addSide(sides)
            .build();
    }
}
