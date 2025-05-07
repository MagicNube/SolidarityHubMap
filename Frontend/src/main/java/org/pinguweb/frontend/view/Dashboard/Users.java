package org.pinguweb.frontend.view.Dashboard;

import com.storedobject.chart.*;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.pinguweb.frontend.mapObjects.Affected;
import org.pinguweb.frontend.mapObjects.Volunteer;
import org.pinguweb.frontend.view.NavigationBar;

@Route("dashboard/users-affected")
public class Users extends HorizontalLayout {

    public Users() {
        this.setSizeFull();
        VerticalLayout navBarLayout = new VerticalLayout();
        navBarLayout.setWidth("250px");
        navBarLayout.setHeightFull();
        navBarLayout.add(NavigationBar.createNavBar());

        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setSizeFull();
        mainContainer.setPadding(true);
        mainContainer.setSpacing(true);

        H1 title = new H1("Volunteer-Affected Dashboard");
        mainContainer.add(title);

        HorizontalLayout chartLayout = new HorizontalLayout();
        chartLayout.setWidthFull();
        chartLayout.setAlignItems(Alignment.CENTER);
        chartLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        chartLayout.add(
                createPieChart(Volunteer.getAllFromServer().size(), Affected.getAllFromServer().size()),
                createBarChart(Volunteer.getAllFromServer().size(), Affected.getAllFromServer().size()),
                createLineChart(Volunteer.getAllFromServer().size(), Affected.getAllFromServer().size())
        );

        mainContainer.add(chartLayout);
        this.add(navBarLayout, mainContainer);
    }
  
    public SOChart createBarChart(int VolunteerData, int AffectedData) {
        SOChart barChart = new SOChart();
        barChart.setSize("400px", "400px");

        RectangularCoordinate rc = new RectangularCoordinate(new XAxis(DataType.CATEGORY), new YAxis(DataType.NUMBER));

        String[] labels = {"Volunteer", "Affected"};
        int[] data = {VolunteerData, AffectedData};

        for (int i = 0; i < labels.length; i++) {
            BarChart bar = new BarChart(new CategoryData(labels[i]), new Data(data[i]));
            bar.setName(labels[i]);
            bar.setColors(new Color((int)(Math.random() * 256), (int)(Math.random() * 256), (int)(Math.random() * 256)));
            bar.plotOn(rc);
            barChart.add(bar);
        }

        return barChart;
    }
    public SOChart createPieChart(int  VolunteerData, int AffectedData) {
        SOChart pieChart = new SOChart();
        pieChart.setSize("400px", "400px");

        Data data = new Data(VolunteerData, AffectedData);
        CategoryData labels = new CategoryData("Volunteer", "Affected");
        PieChart pie = new PieChart(labels, data);

        pie.setName("Volunteer vs Affected");

        Legend legend = new Legend();
        pieChart.add(pie, legend);

        return pieChart;
    }

    public SOChart createLineChart(int VolunteerData, int AffectedData) {
        SOChart lineChart = new SOChart();
        lineChart.setSize("400px", "400px");

        Data data = new Data(VolunteerData, AffectedData);
        CategoryData labels = new CategoryData("Volunteer", "Affected");

        LineChart line = new LineChart(labels,data);

        RectangularCoordinate rc = new RectangularCoordinate(new XAxis(DataType.CATEGORY), new YAxis(DataType.NUMBER));
        line.plotOn(rc);
        lineChart.add(line);

        return lineChart;
    }

    
}