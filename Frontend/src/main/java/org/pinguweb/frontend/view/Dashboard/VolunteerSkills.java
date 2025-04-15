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
import org.pinguweb.DTO.SkillDTO;
import org.pinguweb.DTO.VolunteerDTO;
import org.pinguweb.enums.TaskType;
import org.pinguweb.frontend.services.backend.BackendObject;
import org.pinguweb.frontend.services.backend.BackendService;
import org.pinguweb.frontend.view.NavigationBar;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Route("dashboard/volunteer-skills")
public class VolunteerSkills extends HorizontalLayout {

    BackendObject<List<NeedDTO>> needs = BackendService.getListFromBackend(BackendService.BACKEND + "/api/need/(id)",
            new ParameterizedTypeReference<List<NeedDTO>>() {
            });
    BackendObject<List<TaskDTO>> tasks = BackendService.getListFromBackend(BackendService.BACKEND + "/api/task",
            new ParameterizedTypeReference<List<TaskDTO>>() {
            });

    public VolunteerSkills() {
        this.setSizeFull();
        VerticalLayout navBarLayout = new VerticalLayout();
        navBarLayout.setWidth("250px");
        navBarLayout.setHeightFull();
        navBarLayout.add(NavigationBar.createNavBar());

        VerticalLayout chartsContainer = new VerticalLayout();
        chartsContainer.setSizeFull();


        HorizontalLayout chartLayout = new HorizontalLayout();
        chartLayout.setWidthFull();
        chartLayout.setAlignItems(Alignment.CENTER);
        chartLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        chartLayout.add(createPieChart(2, 1, 4, 2, 1, 3), createBarChart(2, 1, 4, 2, 1, 3), createLineChart(2, 1, 4, 2, 1, 3));
        chartsContainer.add(chartLayout);
        this.add(navBarLayout, chartsContainer);


    }

    public SOChart createBarChart(int skill1, int skill2, int skill3, int skill4, int skill5, int skill6) {
        SOChart barChart = new SOChart();
        barChart.setSize("400px", "400px");

        RectangularCoordinate rc = new RectangularCoordinate(new XAxis(DataType.CATEGORY), new YAxis(DataType.NUMBER));

        String[] labels = {"MEDICAL", "SEARCH", "SAFETY", "LOGISTICS", "COMMUNICATION", "PSYCHOLOGICAL"};
        int[] data = {skill1, skill2, skill3, skill4, skill5, skill6};

        for (int i = 0; i < labels.length; i++) {
            BarChart bar = new BarChart(new CategoryData(labels[i]), new Data(data[i]));
            bar.setName(labels[i]);
            bar.setColors(new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
            bar.plotOn(rc);
            barChart.add(bar);
        }

        return barChart;
    }

    public SOChart createPieChart(int skill1, int skill2, int skill3, int skill4, int skill5, int skill6) {
        SOChart pieChart = new SOChart();
        pieChart.setSize("400px", "400px");

        Data data = new Data(skill1, skill2, skill3, skill4, skill5, skill6);
        CategoryData labels = new CategoryData("MEDICAL", "SEARCH", "SAFETY", "LOGISTICS", "COMMUNICATION", "PSYCHOLOGICAL");
        PieChart pie = new PieChart(labels, data);

        pie.setName("Volunteer Skills");

        Legend legend = new Legend();
        pieChart.add(pie, legend);

        return pieChart;
    }

    public SOChart createLineChart(int skill1, int skill2, int skill3, int skill4, int skill5, int skill6) {
        SOChart lineChart = new SOChart();
        lineChart.setSize("400px", "400px");

        Data data = new Data(skill1, skill2, skill3, skill4, skill5, skill6);
        CategoryData labels = new CategoryData("MEDICAL", "SEARCH", "SAFETY", "LOGISTICS", "COMMUNICATION", "PSYCHOLOGICAL");

        LineChart line = new LineChart(labels, data);

        RectangularCoordinate rc = new RectangularCoordinate(new XAxis(DataType.CATEGORY), new YAxis(DataType.NUMBER));
        line.plotOn(rc);
        lineChart.add(line);

        return lineChart;
    }
}
