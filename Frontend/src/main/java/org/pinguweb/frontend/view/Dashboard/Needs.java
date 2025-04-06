package org.pinguweb.frontend.view.Dashboard;

import com.storedobject.chart.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.pinguweb.frontend.services.backend.BackendObject;
import org.pinguweb.frontend.services.backend.BackendService;
import org.pinguweb.frontend.view.NavigationBar;
import com.vaadin.flow.component.button.Button;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;

import java.util.List;

@Route("dashboard/needs")
public class Needs extends HorizontalLayout {

    BackendObject<List<NeedDTO>> needs = BackendService.getListFromBackend(BackendService.BACKEND + "/api/need",
            new ParameterizedTypeReference<List<NeedDTO>>() {
            });


    public Needs() {
        this.setSizeFull();
        VerticalLayout navBarLayout = new VerticalLayout();
        navBarLayout.setWidth("250px");
        navBarLayout.setHeightFull();
        navBarLayout.add(NavigationBar.createNavBar());

        VerticalLayout chartsContainer = new VerticalLayout();
        chartsContainer.setSizeFull();

        //Filtros
        HorizontalLayout filtersLayout = new HorizontalLayout();
        DatePicker startDatePicker = new DatePicker("Start Date");
        DatePicker endDatePicker = new DatePicker("End Date");
        ComboBox<String> priorityBox = new ComboBox<>("Priority");
        priorityBox.setItems("Low", "Medium", "High");
        ComboBox<String> categoryBox = new ComboBox<>("Category");
        categoryBox.setItems("Category 1", "Category 2", "Category 3");
        ComboBox<String> responsibleBox = new ComboBox<>("Responsible");
        responsibleBox.setItems("User 1", "User 2", "User 3");
        Button filterButton = new Button("Apply Filters");

        filtersLayout.add(startDatePicker, endDatePicker, priorityBox, categoryBox, responsibleBox, filterButton);
        chartsContainer.add(filtersLayout);

        HorizontalLayout chartLayout = new HorizontalLayout();
        chartLayout.setWidthFull();
        chartLayout.setAlignItems(Alignment.CENTER);
        chartLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        CategoryData labels = new CategoryData("TasksCr", "TasksCo", "NeedsCr", "NeedsCo");
        Data needsData = new Data(25, 40, getNeedsCR(), getNeedsCO()) ;


        //PieChart
        SOChart pieChart = new SOChart();
        pieChart.setSize("400px", "400px");
        PieChart needspc = new PieChart(labels, needsData);
        needspc.setName("Needs");

        //Legend
        Legend legend = new Legend();
        pieChart.add(needspc, legend);

        //BarChart
        SOChart barChart = new SOChart();
        barChart.setSize("400px", "400px");

        BarChart tasksCrBar = new BarChart(new CategoryData("TasksCr"), new Data(25));
        tasksCrBar.setName("TasksCr");
        tasksCrBar.setColors(new Color(0, 0, 255));

        BarChart tasksCoBar = new BarChart(new CategoryData("TasksCo"), new Data(40));
        tasksCoBar.setName("TasksCo");
        tasksCoBar.setColors(new Color(0, 128, 0));

        BarChart needsCrBar = new BarChart(new CategoryData("NeedsCr"), new Data(20));
        needsCrBar.setName("NeedsCr");
        needsCrBar.setColors(new Color(255, 0, 0));

        BarChart needsCoBar = new BarChart(new CategoryData("NeedsCo"), new Data(50));
        needsCoBar.setName("NeedsCo");
        needsCoBar.setColors(new Color(255, 255, 0));

        RectangularCoordinate rc = new RectangularCoordinate(new XAxis(DataType.CATEGORY), new YAxis(DataType.NUMBER));

        tasksCoBar.plotOn(rc);
        tasksCrBar.plotOn(rc);
        needsCrBar.plotOn(rc);
        needsCoBar.plotOn(rc);
        barChart.add(tasksCrBar, tasksCoBar, needsCrBar, needsCoBar);


        //LineChart
        SOChart lineChart = new SOChart();
        lineChart.setSize("400px", "400px");
        LineChart lc = new LineChart(labels, needsData);
        RectangularCoordinate rc2 = new RectangularCoordinate(new XAxis(DataType.CATEGORY), new YAxis(DataType.NUMBER));
        lc.plotOn(rc2);
        lineChart.add(lc);

        chartLayout.add(pieChart, barChart, lineChart);
        chartsContainer.add(chartLayout);
        this.add(navBarLayout, chartsContainer);

        filterButton.addClickListener(e -> {
            {
                System.out.println("Filters applied: ");
            }
        });

    }

    public int getNeedsCR() {
        if (needs.getStatusCode() == HttpStatusCode.OK) {
            int count = 0;
            for (NeedDTO need : needs.getData()) {
                if (!need.getID()) {
                    count++;
                }
            }
            return count;
        } else return 0;
    }

    public int getNeedsCO() {
        if (needs.getStatusCode() == HttpStatusCode.OK) {
            int count = 0;
            for (NeedDTO need : needs.getAll()) {
                if (!need.getID()) {
                    count++;
                }
            }
            return count;
        } else return 0;
    }

}
