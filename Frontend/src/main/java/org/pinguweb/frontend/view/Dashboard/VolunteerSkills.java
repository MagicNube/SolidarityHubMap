package org.pinguweb.frontend.view.Dashboard;

import com.storedobject.chart.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.pinguweb.DTO.SkillDTO;
import org.pinguweb.DTO.VolunteerDTO;
import org.pinguweb.enums.TaskType;
import org.pinguweb.frontend.services.backend.BackendObject;
import org.pinguweb.frontend.services.backend.BackendService;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

@Route("dashboard/volunteer-skills")
public class VolunteerSkills extends VerticalLayout {

    private List<VolunteerDTO> volunteers;
    private List<SkillDTO> skills;
    private TaskType[] tasks = TaskType.values();

    public VolunteerSkills() {
        this.setSizeFull();

        BackendObject<List<VolunteerDTO>> volunteerResponse = BackendService.getListFromBackend(BackendService.BACKEND + "/api/volunteers",
                new ParameterizedTypeReference<List<VolunteerDTO>>() {
                });
        BackendObject<List<SkillDTO>> skillResponse = BackendService.getListFromBackend(BackendService.BACKEND + "/api/skills",
                new ParameterizedTypeReference<List<SkillDTO>>() {
                });

        this.volunteers = volunteerResponse.getData();
        this.skills = skillResponse.getData();

        SOChart barChart = createVolunteerSkillsBarChart();
        this.add(barChart);
    }

    public SOChart createVolunteerSkillsBarChart() {
        SOChart barChart = new SOChart();
        barChart.setSize("400px", "400px");
        String[] labels = null;
        RectangularCoordinate rc = new RectangularCoordinate(new XAxis(DataType.CATEGORY), new YAxis(DataType.NUMBER));

        if (skills != null) {
             labels = skills.stream()
                    .filter(skill -> skill != null && skill.getName() != null)
                    .map(SkillDTO::getName)
                    .toArray(String[]::new);
        } else {

             labels = new String[0];
            System.out.println("no hay datos");
        }
        int[] data = new int[skills.size()];

        for (VolunteerDTO volunteer : volunteers) {
            for ( String skill: volunteer.getTaskPreferences()) {
                int index = skills.indexOf(skill);
                if (index >= 0) {
                    data[index]++;
                }
            }
        }

        for (int i = 0; i < labels.length; i++) {
            BarChart bar = new BarChart(new CategoryData(labels[i]), new Data(data[i]));
            bar.setName(labels[i]);
            bar.setColors(new Color((int)(Math.random() * 256), (int)(Math.random() * 256), (int)(Math.random() * 256)));
            bar.plotOn(rc);
            barChart.add(bar);
        }

        return barChart;
    }
}



