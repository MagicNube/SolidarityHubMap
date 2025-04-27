package org.pinguweb.frontend.interfaceBuilders.Directors;

import com.storedobject.chart.*;
import com.vaadin.flow.component.Component;
import org.pinguweb.frontend.interfaceBuilders.Builders.DashboardBuilder;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.ChartType;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.DashboardData.ChartData;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.InterfaceComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DashboardBuilderDirector {
    DashboardBuilder builder = new DashboardBuilder();

    public Component buildTest(){
        Dashboard first = Dashboard.builder()
                .name("test 1")
                .colors(new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)))
                .data(
                        new ChartData(
                                new Object[]{"TasksCr","TasksCo","NeedsCr","NeedsCo"},
                                new Object[]{0,1,2,3},
                                Dashboard.class,
                                ChartData.class
                        )
                )
                .coordinateConfiguration(
                        new RectangularCoordinate(
                                new XAxis(DataType.CATEGORY),
                                new YAxis(DataType.NUMBER)
                        )
                )
                .type(ChartType.BAR)
                .hasFilter(true)
                .width("400px")
                .height("400px")
                .build();

        Dashboard second = Dashboard.builder()
                .name("test 2")
                .colors(new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)))
                .data(
                        new ChartData(
                                new Object[]{"TasksCr","TasksCo","NeedsCr","NeedsCo"},
                                new Object[]{0,1,2,3},
                                Dashboard.class,
                                ChartData.class
                        )
                )
                .coordinateConfiguration(
                        new RectangularCoordinate(
                                new XAxis(DataType.CATEGORY),
                                new YAxis(DataType.NUMBER)
                        )
                )
                .type(ChartType.BAR)
                .width("400px")
                .height("400px")
                .hasFilter(false)
                .build();

        Dashboard third = Dashboard.builder()
                .name("test 3")
                .colors(new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)))
                .data(
                        new ChartData(
                                new Object[]{"TasksCr","TasksCo","NeedsCr","NeedsCo"},
                                new Object[]{0,1,2,3},
                                Dashboard.class,
                                ChartData.class
                        )
                )
                .coordinateConfiguration(
                        new RectangularCoordinate(
                                new XAxis(DataType.CATEGORY),
                                new YAxis(DataType.NUMBER)
                        )
                )
                .type(ChartType.BAR)
                .width("400px")
                .height("400px")
                .hasFilter(false)
                .build();

        Dashboard forth = Dashboard.builder()
                .name("test 4")
                .colors(new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)))
                .data(
                        new ChartData(
                                new Object[]{"TasksCr","TasksCo","NeedsCr","NeedsCo"},
                                new Object[]{0,1,2,3},
                                Dashboard.class,
                                ChartData.class
                        )
                )
                .coordinateConfiguration(
                        new RectangularCoordinate(
                                new XAxis(DataType.CATEGORY),
                                new YAxis(DataType.NUMBER)
                        )
                )
                .type(ChartType.BAR)
                .width("400px")
                .height("400px")
                .hasFilter(false)
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
