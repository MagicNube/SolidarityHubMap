package org.pinguweb.frontend.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


public class InfoPopup {
    public static Button DashboardPopup() {
        Dialog dialogo = new Dialog();
        dialogo.setWidth("60em");
        dialogo.setHeight("80vh");
        dialogo.getElement().getStyle().set("overflow", "auto");

        Div dashboardInfo = createDashboardInfo();
        Div dashboardElementsInfo = createDashboardElementsInfo();

        Button cerrar = new Button("Cerrar", e -> dialogo.close());

        dialogo.add(new VerticalLayout(dashboardInfo, dashboardElementsInfo, cerrar));

        // FAB (Floating Action Button)
        Button fab = new Button(new Icon(VaadinIcon.INFO_CIRCLE));
        fab.getStyle()
                .set("position", "fixed")
                .set("bottom", "2rem")
                .set("right", "2rem")
                .set("border-radius", "50%")
                .set("width", "56px")
                .set("height", "56px")
                .set("box-shadow", "0 2px 5px rgba(0,0,0,0.3)")
                .set("background-color", "#007BFF")
                .set("color", "white");

        fab.addClickListener(e -> dialogo.open());

        return fab;
    }

    private static Div createDashboardInfo() {
        Div section = new Div();
        section.add(new H3("Dashboard General"));
        section.add(new Paragraph(
                "El dashboard principal de Solidarity Hub es el centro de mando para la gestión de emergencias. " +
                        "Permite obtener una visión general y detallada de la situación, facilitando la toma de decisiones " +
                        "y la coordinación de recursos."
        ));
        section.add(new Paragraph(
                "Ofrece la capacidad de filtrar y visualizar diferentes tipos de elementos cruciales " +
                        "como zonas operativas, rutas logísticas, puntos de almacenamiento (storages) y necesidades reportadas. " +
                        "Se pueden aplicar filtros dinámicos para concentrarse en los elementos de mayor interés y realizar " +
                        "búsquedas específicas por nombre o atributos."
        ));
        section.add(new Paragraph(
                "Desde el dashboard se pueden iniciar flujos de trabajo para la creación de nuevos elementos (zonas, rutas, almacenes) " +
                        "o para administrar y actualizar los ya existentes en el sistema."
        ));
        section.getStyle().set("margin-bottom", "15px");
        return section;
    }

    private static Div createDashboardElementsInfo() {
        Div section = new Div();
        section.add(new H3("Indicadores Clave del Dashboard"));
        section.add(new Paragraph(
                "El dashboard presenta varios indicadores y visualizaciones para monitorizar el estado de las operaciones:"
        ));

        UnorderedList list = new UnorderedList(
                new ListItem(
                        new H3("Tareas con Necesidad Asignada (Ejemplo base)"),
                        new Paragraph("Muestra las tareas que están directamente vinculadas a una necesidad específica.")
                ),
                new ListItem(
                        new H3("Tareas Completadas"),
                        new Paragraph("Contabiliza o lista las tareas que han sido marcadas como finalizadas.")
                ),
                new ListItem(
                        new H3("Necesidades no Cubiertas"),
                        new Paragraph("Identifica las necesidades reportadas que aún no tienen una tarea asignada o no han sido satisfechas.")
                ),
                new ListItem(
                        new H3("Tareas no Terminadas"),
                        new Paragraph("Presenta un resumen de todas las tareas que se encuentran pendientes o en progreso.")
                ),
                new ListItem(
                        new H3("Voluntarios por Tipo de Tarea"),
                        new Paragraph("Visualización (posiblemente un gráfico) que desglosa la cantidad de voluntarios asignados a cada categoría o tipo de tarea existente.")
                ),
                new ListItem(
                        new H3("Voluntarios frente a Afectados"),
                        new Paragraph("Comparativa numérica o gráfica entre la cantidad total de voluntarios disponibles/activos y el número total de personas o entidades afectadas registradas.")
                )
        );
        section.add(list);
        section.getStyle().set("margin-bottom", "15px");
        return section;
    }

    public static Button MapPopup() {
        Dialog dialogo = new Dialog();
        dialogo.setWidth("60em");
        dialogo.setHeight("80vh");
        dialogo.getElement().getStyle().set("overflow", "auto");

        Div dashboardInfo = createMapInfo();
        Div dashboardElementsInfo = createEditDeleteInfo();

        Button cerrar = new Button("Cerrar", e -> dialogo.close());

        dialogo.add(new VerticalLayout(dashboardInfo, dashboardElementsInfo, cerrar));

        // FAB (Floating Action Button)
        Button fab = new Button(new Icon(VaadinIcon.INFO_CIRCLE));
        fab.getStyle()
                .set("position", "fixed")
                .set("bottom", "2rem")
                .set("right", "2rem")
                .set("border-radius", "50%")
                .set("width", "56px")
                .set("height", "56px")
                .set("box-shadow", "0 2px 5px rgba(0,0,0,0.3)")
                .set("background-color", "#007BFF")
                .set("color", "white");

        fab.addClickListener(e -> dialogo.open());

        return fab;
    }

    private static Div createMapInfo() {
        Div section = new Div();
        section.add(new H3("Mapa Interactivo"));
        section.add(new Paragraph(
                "La vista del mapa proporciona una representación geográfica en tiempo real de todos los elementos relevantes " +
                        "para la gestión de la emergencia. Permite visualizar la distribución espacial de zonas, rutas, " +
                        "almacenamientos y necesidades."
        ));
        section.add(new Paragraph(
                "Características principales del mapa:"
        ));
        UnorderedList mapFeatures = new UnorderedList(
                new ListItem(
                        new H3("Visualización de Elementos:"),
                        new Paragraph("Iconos y formas específicas representan cada tipo de elemento para una fácil identificación. " +
                                "Se observan marcadores rojos (posiblemente almacenes o necesidades críticas) concentrados en áreas urbanas como Valencia y alrededores, " +
                                "y múltiples marcadores triangulares (posiblemente necesidades individuales o tareas) distribuidos ampliamente.")
                ),
                new ListItem(
                        new H3("Controles de Navegación:"),
                        new Paragraph("Botones de Zoom (+/-) para ajustar el nivel de detalle y un selector de capas para filtrar la información mostrada (activar/desactivar la visualización de zonas, rutas, etc.).")
                ),
                new ListItem(
                        new H3("Creación Directa en Mapa:"),
                        new Paragraph("Funcionalidad para crear nuevas zonas, rutas y almacenamientos directamente dibujando o seleccionando puntos sobre el mapa. " +
                                "También se pueden visualizar las necesidades existentes en su ubicación geográfica.")
                ),
                new ListItem(
                        new H3("Barra de Acciones:"),
                        new Paragraph("Una barra inferior provee acceso rápido a funciones esenciales: 'Crear zona', 'Crear almacen', 'Crear ruta', 'Editar' (para modificar elementos existentes), " +
                                "'Borrar' (para eliminar elementos), y botones de 'Deshacer'/'Rehacer' para revertir o repetir acciones.")
                ),
                new ListItem(
                        new H3("Tecnología y Datos:"),
                        new Paragraph("El mapa utiliza la tecnología Leaflet con datos base de OpenStreetMap, asegurando información geográfica actualizada y detallada.")
                )
        );
        section.add(mapFeatures);
        section.getStyle().set("margin-bottom", "15px");
        return section;
    }

    private static Div createEditDeleteInfo() {
        Div section = new Div();
        section.add(new H3("Edición y Eliminación de Elementos"));
        section.add(new Paragraph(
                "Para editar o eliminar elementos (zonas, rutas, almacenamientos, etc.), primero debes presionar el boton, " +
                        "que se encuentra en la barra de acciones del mapa "
        ));
        section.add(new Paragraph(
                "Una vez clicado cualquiera de los botones podras hacer click en una ruta, zona o almacen. " +
                        "Estas acciones suelen abrir diálogos modales donde se pueden modificar los detalles del elemento o confirmar la eliminación, " +
                        "garantizando que los cambios se realicen de forma controlada y segura."
        ));
        section.getStyle().set("margin-bottom", "15px");
        return section;
    }
}
