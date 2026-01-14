package com.sems.frontend.view;

import com.sems.frontend.dto.EventDTO;
import com.sems.frontend.service.EventRestService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.ParentLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Vaadin View for displaying and managing Events.
 * Features: data binding via Grid component, search filtering.
 */
@Route(value = "", layout = MainLayout.class)
public class EventsView extends VerticalLayout {

    private final EventRestService eventRestService;
    private final Grid<EventDTO> eventGrid;
    private List<EventDTO> allEvents;

    @Autowired
    public EventsView(EventRestService eventRestService) {
        this.eventRestService = eventRestService;

        // Layout configuration
        setWidthFull();
        setPadding(true);
        setSpacing(true);

        // Title
        H1 title = new H1("Events Management");
        add(title);

        // Search field with data filtering
        TextField searchField = new TextField();
        searchField.setPlaceholder("Search events by title...");
        searchField.setWidthFull();

        // Grid component for data binding
        eventGrid = new Grid<>(EventDTO.class, false);
        eventGrid.setWidthFull();
        eventGrid.setHeight("600px");

        // Column definitions with data binding
        eventGrid.addColumn(EventDTO::getId)
                .setHeader("ID")
                .setWidth("80px");
        eventGrid.addColumn(EventDTO::getName)
                .setHeader("Name")
                .setFlexGrow(1);
        eventGrid.addColumn(EventDTO::getVenueName)
                .setHeader("Venue")
                .setFlexGrow(1);
        eventGrid.addColumn(EventDTO::getEventType)
                .setHeader("Type");
        eventGrid.addColumn(event -> event.getStartDateTime() != null ? event.getStartDateTime().toString() : "N/A")
                .setHeader("Start Date/Time")
                .setFlexGrow(1);

        // Load and bind data
        refreshEventData();

        // Search filtering with data binding
        searchField.addValueChangeListener(event -> {
            String filterValue = event.getValue().toLowerCase();
            if (eventGrid.getDataProvider() instanceof ListDataProvider) {
                @SuppressWarnings("unchecked")
                ListDataProvider<EventDTO> dataProvider = (ListDataProvider<EventDTO>) eventGrid.getDataProvider();
                dataProvider.setFilter(eventDTO -> eventDTO.getName().toLowerCase().contains(filterValue));
            }
        });

        // Add components
        add(new Paragraph("Browse and manage all events in the system."));
        add(searchField);
        add(eventGrid);
    }

    /**
     * Refresh event data from REST API and rebind to Grid.
     */
    private void refreshEventData() {
        try {
            allEvents = eventRestService.getAllEvents();
            ListDataProvider<EventDTO> dataProvider = new ListDataProvider<>(allEvents);
            eventGrid.setDataProvider(dataProvider);
        } catch (Exception e) {
            add(new Paragraph("Error loading events: " + e.getMessage()));
        }
    }
}
