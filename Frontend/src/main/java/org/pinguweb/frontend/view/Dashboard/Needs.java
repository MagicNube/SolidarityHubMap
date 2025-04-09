package org.pinguweb.frontend.view.Dashboard;

import com.storedobject.chart.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.pinguweb.DTO.NeedDTO;
import org.pinguweb.DTO.TaskDTO;
import org.pinguweb.frontend.services.backend.BackendObject;
import org.pinguweb.frontend.services.backend.BackendService;
import org.pinguweb.frontend.view.NavigationBar;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Route("dashboard/needs")
public class Needs extends HorizontalLayout {

    BackendObject<List<NeedDTO>> needs = BackendService.getListFromBackend(BackendService.BACKEND + "/api/need/(id)",
            new ParameterizedTypeReference<List<NeedDTO>>() {
            });
    BackendObject<List<TaskDTO>> tasks = BackendService.getListFromBackend(BackendService.BACKEND + "/api/task",
            new ParameterizedTypeReference<List<TaskDTO>>() {
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

        chartLayout.add(createPieChart( getTaskCR(),getTaskCO(),50,20), createBarChart(10,24,50,20), createLineChart(10,24,50,20));
        chartsContainer.add(chartLayout);
        this.add(navBarLayout, chartsContainer);


        //Listeners
        filterButton.addClickListener(e -> {
            String startDate = startDatePicker.getValue() != null ? startDatePicker.getValue().toString() : null;
            String endDate = endDatePicker.getValue() != null ? endDatePicker.getValue().toString() : null;
            String priority = priorityBox.getValue() != null ? priorityBox.getValue().toString() : null;
            String category = categoryBox.getValue() != null ? categoryBox.getValue().toString() : null;
            String responsible = responsibleBox.getValue() != null ? responsibleBox.getValue().toString() : null;

            // Apply filters to the data
            List<TaskDTO> filteredTasks = new ArrayList<>();
            for (TaskDTO task : tasks.getData()) {
                if ((startDate == null || task.getStartTimeDate().toString().compareTo(startDate) >= 0) &&
                        (endDate == null || task.getEstimatedEndTimeDate().toString().compareTo(endDate) <= 0) &&
                        (priority == null || task.getPriority().equals(priority)) &&
                        (category == null || task.getType().equals(category)) &&
                        (responsible == null || task.getCoordinates().equals(responsible))) {
                    filteredTasks.add(task);
                }
            }

            List<NeedDTO> filteredNeeds = new ArrayList<>();
            TaskDTO tarea = null;
            for (NeedDTO need : needs.getData()) {
                for ( TaskDTO t : tasks.getData()){
                    if ( need.getTask() == t.getId()){tarea = t; break;}
                }
                if ((startDate == null || (tarea.getStartTimeDate().toString()).compareTo(startDate) >= 0) &&
                        (endDate == null || tarea.getEstimatedEndTimeDate().toString().compareTo(endDate) <= 0) &&
                        (priority == null || tarea.getPriority().equals(priority)) &&
                        (category == null || tarea.getType().equals(category)) &&
                        (responsible == null || tarea.getCoordinates().equals(responsible))) {
                    filteredNeeds.add(need);
                }
            }

            // Update the charts with the filtered data
            int taskCR = (int) filteredTasks.stream().filter(task -> task.getStatus().equals("IN_PROGRESS")).count();
            int taskCO = (int) filteredTasks.stream().filter(task -> task.getStatus().equals("FINISHED")).count();
           int needsCR = (int) filteredNeeds.stream().filter(need -> need.getId() != 0).count();
           int needsCO = (int) filteredNeeds.stream().filter(need -> need.getId() == 0).count();

            chartLayout.removeAll();
            chartLayout.add(createPieChart(taskCR, taskCO, needsCR, needsCO), createBarChart(taskCR, taskCO, needsCR, needsCO), createLineChart(taskCR, taskCO, needsCR, needsCO));
        });

    }
    public SOChart createBarChart(int TaskCR, int TaskCO, int NeedsCR, int NeedsCO) {
        SOChart barChart = new SOChart();
        barChart.setSize("400px", "400px");

        RectangularCoordinate rc = new RectangularCoordinate(new XAxis(DataType.CATEGORY), new YAxis(DataType.NUMBER));

        String[] labels = {"TasksCr", "TasksCo", "NeedsCr", "NeedsCo"};
        int[] data = {TaskCR, TaskCO, NeedsCR, NeedsCO};

        for (int i = 0; i < labels.length; i++) {
            BarChart bar = new BarChart(new CategoryData(labels[i]), new Data(data[i]));
            bar.setName(labels[i]);
            bar.setColors(new Color((int)(Math.random() * 256), (int)(Math.random() * 256), (int)(Math.random() * 256)));
            bar.plotOn(rc);
            barChart.add(bar);
        }

        return barChart;
    }
    public SOChart createPieChart(int TaskCR, int TaskCO, int NeedsCR, int NeedsCO) {
        SOChart pieChart = new SOChart();
        pieChart.setSize("400px", "400px");

        Data data = new Data(TaskCR, TaskCO, NeedsCR, NeedsCO);
        CategoryData labels = new CategoryData("TasksCr", "TasksCo", "NeedsCr", "NeedsCo");
        PieChart pie = new PieChart(labels, data);

        pie.setName("Needs");

        Legend legend = new Legend();
        pieChart.add(pie, legend);

        return pieChart;
    }

    public SOChart createLineChart(int TaskCR, int TaskCO, int NeedsCR, int NeedsCO) {
        SOChart lineChart = new SOChart();
        lineChart.setSize("400px", "400px");

        CategoryData labels = new CategoryData("TasksCr", "TasksCo", "NeedsCr", "NeedsCo");
        Data data = new Data(TaskCR, TaskCO, NeedsCR, NeedsCO);

        LineChart line = new LineChart(labels,data);

        RectangularCoordinate rc = new RectangularCoordinate(new XAxis(DataType.CATEGORY), new YAxis(DataType.NUMBER));
        line.plotOn(rc);
        lineChart.add(line);

        return lineChart;
    }
  
   public int getTaskCR() {
        if (tasks.getStatusCode() == HttpStatus.OK) {
            int count = 0;
            for (TaskDTO task : tasks.getData()) {
                if (task.getStatus() == "IN_PROGRESS") {
                    count++;
                }
            }
            return count;
        } else {
            return 0;
        }
    }
    public int getTaskCO() {
        if (needs.getStatusCode() == HttpStatus.OK) {
            int count = 0;
            for (TaskDTO task : tasks.getData()) {
                if (task.getStatus() == "FINISHED") {
                    count++;
                }
            }
            return count;
        } else {
            return 0;
        }
    }
/*
    public int getNeedsCR() {
        if (needs.getStatusCode() == HttpStatus.OK) {
            int count = 0;
            TaskDTO tarea = null;
            for (NeedDTO need : needs.getData()) {
                for ( TaskDTO t : tasks.getData()){
                    if ( need.getTask() == t.getId()){tarea = t; break;}
                }
                return count;
            }
        }

    public int getNeedsCO() {
        if (needs.getStatusCode() == HttpStatus.OK) {
            int count = 0;
            for (NeedDTO need : needs.getData()) {
                if (need.getID() == null) {
                    count++;
                }
            }
            return count;
        } else {
            return 0;
        }
    }*/
}
