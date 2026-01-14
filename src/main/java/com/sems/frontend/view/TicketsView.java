package com.sems.frontend.view;

import com.sems.frontend.dto.TicketDTO;
import com.sems.frontend.service.TicketRestService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Vaadin View for displaying and managing Tickets.
 * Features: data binding via Grid component, search filtering.
 */
@Route(value = "tickets", layout = MainLayout.class)
public class TicketsView extends VerticalLayout {

    private final TicketRestService ticketRestService;
    private final Grid<TicketDTO> ticketGrid;
    private List<TicketDTO> allTickets;

    @Autowired
    public TicketsView(TicketRestService ticketRestService) {
        this.ticketRestService = ticketRestService;

        // Layout configuration
        setWidthFull();
        setPadding(true);
        setSpacing(true);

        // Title
        H1 title = new H1("Tickets Management");
        add(title);

        // Search field with data filtering
        TextField searchField = new TextField();
        searchField.setPlaceholder("Search tickets by event or user...");
        searchField.setWidthFull();

        // Grid component for data binding
        ticketGrid = new Grid<>(TicketDTO.class, false);
        ticketGrid.setWidthFull();
        ticketGrid.setHeight("600px");

        // Column definitions with data binding
        ticketGrid.addColumn(TicketDTO::getId)
                .setHeader("ID")
                .setWidth("80px");
        ticketGrid.addColumn(TicketDTO::getEventTitle)
                .setHeader("Event")
                .setFlexGrow(1);
        ticketGrid.addColumn(TicketDTO::getUserName)
                .setHeader("User")
                .setFlexGrow(1);
        ticketGrid.addColumn(TicketDTO::getStatus)
                .setHeader("Status")
                .setWidth("100px");

        // Load and bind data
        refreshTicketData();

        // Search filtering with data binding
        searchField.addValueChangeListener(event -> {
            String filterValue = event.getValue().toLowerCase();
            if (ticketGrid.getDataProvider() instanceof ListDataProvider) {
                @SuppressWarnings("unchecked")
                ListDataProvider<TicketDTO> dataProvider = (ListDataProvider<TicketDTO>) ticketGrid.getDataProvider();
                dataProvider.setFilter(ticketDTO ->
                        ticketDTO.getEventTitle().toLowerCase().contains(filterValue) ||
                        ticketDTO.getUserName().toLowerCase().contains(filterValue)
                );
            }
        });

        // Add components
        add(new Paragraph("Browse and manage all tickets in the system."));
        add(searchField);
        add(ticketGrid);
    }

    /**
     * Refresh ticket data from REST API and rebind to Grid.
     */
    private void refreshTicketData() {
        try {
            allTickets = ticketRestService.getAllTickets();
            ListDataProvider<TicketDTO> dataProvider = new ListDataProvider<>(allTickets);
            ticketGrid.setDataProvider(dataProvider);
        } catch (Exception e) {
            add(new Paragraph("Error loading tickets: " + e.getMessage()));
        }
    }
}
