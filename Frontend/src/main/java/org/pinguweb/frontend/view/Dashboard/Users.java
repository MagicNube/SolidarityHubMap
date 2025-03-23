package es.pingu.map.views.Dashboard;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("dashboard/users-affected")
public class Users extends VerticalLayout{
    public Users(){
        this.setSizeFull();

        Chart pieChart = new Chart(ChartType.PIE);
        Configuration pieConf = pieChart.getConfiguration();
        pieConf.setTitle("Users Distribution");

        DataSeries pieSeries = new DataSeries();
        //TODO: Aqui tengo que extraer de la base de datos para rellenar la tarta
        pieSeries.add(new DataSeriesItem("User 1", 10));
        pieSeries.add(new DataSeriesItem("Affected 1", 20));
        pieSeries.add(new DataSeriesItem("User 3", 30));
        pieSeries.add(new DataSeriesItem("Affected 2", 40));
        pieConf.setSeries(pieSeries);

        Chart barChart = new Chart(ChartType.BAR);
        Configuration barConfig = barChart.getConfiguration();
        barConfig.setTitle("User Distribution");

        DataSeries barSeries = new DataSeries();
        //TODO: Aqui tengo que extraer de la base de datos para rellenar la barra
        barSeries.add(new DataSeriesItem("User 1", 10));
        barSeries.add(new DataSeriesItem("User 2", 20));
        barSeries.add(new DataSeriesItem("Affected 3", 30));
        barSeries.add(new DataSeriesItem("Affected 4", 40));
        barConfig.setSeries(barSeries);

        this.add(pieChart, barChart);
    }
}
