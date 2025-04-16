package org.pinguweb.frontend.view.Dashboard;

import com.storedobject.chart.*;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.pinguweb.DTO.VolunteerDTO;
import org.pinguweb.enums.TaskType;
import org.pinguweb.frontend.services.backend.BackendObject;
import org.pinguweb.frontend.services.backend.BackendService;
import org.pinguweb.frontend.view.NavigationBar;
import org.springframework.core.ParameterizedTypeReference;


import java.util.Arrays;
import java.util.List;

@Route("dashboard/volunteer-skills")
public class VolunteerSkills extends VerticalLayout {

    private List<VolunteerDTO> volunteers;
    private TaskType[] tasks = TaskType.values();

    public VolunteerSkills() {
        this.setSizeFull();

        VerticalLayout navBarLayout = new VerticalLayout();
        navBarLayout.setWidth("250px");
        navBarLayout.setHeightFull();
        navBarLayout.add(NavigationBar.createNavBar());

        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setSizeFull();
        mainContainer.setPadding(true);
        mainContainer.setSpacing(true);

        H1 title = new H1("Volunteer-Skills Dashboard");
        mainContainer.add(title);

        BackendObject<List<VolunteerDTO>> volunteerResponse = BackendService.getListFromBackend(BackendService.BACKEND + "/api/volunteers",
                new ParameterizedTypeReference<List<VolunteerDTO>>() {
                });

        this.volunteers = volunteerResponse.getData();

        SOChart barChart = createVolunteerSkillsBarChart();
        this.add(barChart);
    }

    public SOChart createVolunteerSkillsBarChart() {
        SOChart barChart = new SOChart();
        barChart.setSize("400px", "400px");
        String[] labels = Arrays.stream(tasks).map(TaskType::name).toArray(String[]::new);
        RectangularCoordinate rc = new RectangularCoordinate(new XAxis(DataType.CATEGORY), new YAxis(DataType.NUMBER));
        int[] data = new int[tasks.length];
        if (tasks != null) {
            for (VolunteerDTO volunteer : volunteers) {
                for ( String task: volunteer.getTaskPreferences()){
                    int index = Arrays.asList(labels).indexOf(task);
                    if (index >= 0) {
                        data[index]++;
                    }
                }
            }
        } else {labels = new String[0];System.out.println("no hay datos");}
        for (int i = 0; i < labels.length; i++) {
            BarChart bar = new BarChart(new CategoryData(labels[i]), new Data(data[i]));
            bar.setName(labels[i]);
            bar.setColors(new Color((int)(Math.random() * 256), (int)(Math.random() * 256), (int)(Math.random() * 256)));
            bar.plotOn(rc);
            barChart.add(bar);
        }

        return barChart;
    }
    public SOChart createPieChart() {
        SOChart pieChart = new SOChart();
        pieChart.setSize("400px", "400px");

        String[] labels = Arrays.stream(tasks).map(TaskType::name).toArray(String[]::new);
        int[] data = new int[tasks.length];
        for (VolunteerDTO volunteer : volunteers) {
            for (String task : volunteer.getTaskPreferences()) {
                int index = Arrays.asList(labels).indexOf(task);
                if (index >= 0) {
                    data[index]++;
                }
            }
        }
        for ( int i = 0; i < labels.length; i++) {
            PieChart pie = new PieChart(new CategoryData(labels[i]), new Data(data[i]));
            pie.setName(labels[i]);
            pie.setColors(new Color((int)(Math.random() * 256), (int)(Math.random() * 256), (int)(Math.random() * 256)));
            pieChart.add(pie);
        }

        Legend legend = new Legend();
        pieChart.add(legend);
        return pieChart;
    }
    public SOChart createLineChart() {
        SOChart lineChart = new SOChart();
        lineChart.setSize("400px", "400px");

        String[] labels = Arrays.stream(tasks).map(TaskType::name).toArray(String[]::new);
        int[] data = new int[tasks.length];
        for (VolunteerDTO volunteer : volunteers) {
            for (String task : volunteer.getTaskPreferences()) {
                int index = Arrays.asList(labels).indexOf(task);
                if (index >= 0) {
                    data[index]++;
                }
            }
        }
        for ( int i = 0; i < labels.length; i++) {
            LineChart line = new LineChart(new CategoryData(labels[i]), new Data(data[i]));
            line.setName(labels[i]);
            line.setColors(new Color((int)(Math.random() * 256), (int)(Math.random() * 256), (int)(Math.random() * 256)));
            lineChart.add(line);
            RectangularCoordinate rc = new RectangularCoordinate(new XAxis(DataType.CATEGORY), new YAxis(DataType.NUMBER));
            line.plotOn(rc);
            lineChart.add(line);
        }


        return lineChart;
    }
}