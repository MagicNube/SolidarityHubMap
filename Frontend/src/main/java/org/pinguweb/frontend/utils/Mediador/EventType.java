package org.pinguweb.frontend.utils.Mediador;

public enum EventType {
    SHOW, // Muestra un objeto en el mapa
    SHOW_DIALOG, // Muestra un dialogo
    SHOW_EDIT, // Muestra un dialogo de edición
    CREATE, // A partir de un DTO, crea un objeto en la BD
    EDIT, // Activa el modo edición
    DELETE, // Activa el modo eliminar
    EXIT, // Sale del modo editar/eliminar
    LOAD, // Carga los objetos de la BD
    ENABLE_BUTTONS, // Activa todos los botones
    DISABLE_BUTTONS, // Desactiva un botón indicado
    REQUEST_CLICK, // Pide que el mapa espere un click
    END_CLICK // Pide el fin de un evento de entrada
}
