package org.pinguweb.frontend.interfaceBuilders.Directors;

import com.storedobject.chart.*;
import com.vaadin.flow.component.Component;
import org.pinguweb.frontend.interfaceBuilders.Builders.DashboardBuilder;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.ChartType;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.DashboardData.ChartData;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.DashboardData.TestString;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.InterfaceComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DashboardBuilderDirector {
    DashboardBuilder builder = new DashboardBuilder();

    public Component buildTest(){
        TestString fs = new TestString("TasksCr");
        TestString sn = new TestString("TasksCo");
        TestString tr = new TestString("NeedsCr");
        TestString fr = new TestString("NeedsCo");


        Dashboard first = Dashboard.builder()
                .name("test 1")
                .colors(new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)))
                .data(
                        new ChartData<>(
                                new Object[]{fs.getName(), sn.getName(), tr.getName(), fr.getName()},
                                new Object[]{1,2,3,4},
                                new TestString[]{fs, sn, tr, fr},
                                new Integer[]{1,2,3,4}
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
                        new ChartData<>(
                                new Object[]{fs.getName(), sn.getName(), tr.getName(), fr.getName()},
                                new Object[]{1,2,3,4},
                                new TestString[]{fs, sn, tr, fr},
                                new Integer[]{1,2,3,4}
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
                        new ChartData<>(
                                new Object[]{fs.getName(), sn.getName(), tr.getName(), fr.getName()},
                                new Object[]{1,2,3,4},
                                new TestString[]{fs, sn, tr, fr},
                                new Integer[]{1,2,3,4}
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
                        new ChartData<>(
                                new Object[]{fs.getName(), sn.getName(), tr.getName(), fr.getName()},
                                new Object[]{1,2,3,4},
                                new TestString[]{fs, sn, tr, fr},
                                new Integer[]{1,2,3,4}
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
