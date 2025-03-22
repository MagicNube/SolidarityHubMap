package es.pingu.map.views.Dashboard;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("dashboard/pending-task")
public class PendingTaskView extends VerticalLayout{

    public PendingTaskView() {
        this.setSizeFull();
        Div content = new Div();
        content.setText("Aquí se mostrarán las tareas pendientes.");
        this.add(content);
    }
}