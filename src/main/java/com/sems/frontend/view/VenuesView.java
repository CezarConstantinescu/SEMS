package com.sems.frontend.view;

import com.sems.frontend.dto.VenueDTO;
import com.sems.frontend.service.VenueRestService;
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
 * Vaadin View for displaying and managing Venues.
 * Features: data binding via Grid component, search filtering.
 */
@Route(value = "venues", layout = MainLayout.class)
public class VenuesView extends VerticalLayout {

    private final VenueRestService venueRestService;
    private final Grid<VenueDTO> venueGrid;
    private List<VenueDTO> allVenues;

    @Autowired
    public VenuesView(VenueRestService venueRestService) {
        this.venueRestService = venueRestService;

        // Layout configuration
        setWidthFull();
        setPadding(true);
        setSpacing(true);

        // Title
        H1 title = new H1("Venues Management");
        add(title);

        // Search field with data filtering
        TextField searchField = new TextField();
        searchField.setPlaceholder("Search venues by name...");
        searchField.setWidthFull();

        // Grid component for data binding
        venueGrid = new Grid<>(VenueDTO.class, false);
        venueGrid.setWidthFull();
        venueGrid.setHeight("600px");

        // Column definitions with data binding
        venueGrid.addColumn(VenueDTO::getId)
                .setHeader("ID")
                .setWidth("80px");
        venueGrid.addColumn(VenueDTO::getName)
                .setHeader("Name")
                .setFlexGrow(1);
        venueGrid.addColumn(VenueDTO::getAddress)
                .setHeader("Address")
                .setFlexGrow(1);
        venueGrid.addColumn(VenueDTO::getCapacity)
                .setHeader("Capacity")
                .setWidth("120px");

        // Load and bind data
        refreshVenueData();

        // Search filtering with data binding
        searchField.addValueChangeListener(event -> {
            String filterValue = event.getValue().toLowerCase();
            if (venueGrid.getDataProvider() instanceof ListDataProvider) {
                @SuppressWarnings("unchecked")
                ListDataProvider<VenueDTO> dataProvider = (ListDataProvider<VenueDTO>) venueGrid.getDataProvider();
                dataProvider.setFilter(venueDTO -> venueDTO.getName().toLowerCase().contains(filterValue));
            }
        });

        // Add components
        add(new Paragraph("Browse and manage all venues in the system."));
        add(searchField);
        add(venueGrid);
    }

    /**
     * Refresh venue data from REST API and rebind to Grid.
     */
    private void refreshVenueData() {
        try {
            allVenues = venueRestService.getAllVenues();
            ListDataProvider<VenueDTO> dataProvider = new ListDataProvider<>(allVenues);
            venueGrid.setDataProvider(dataProvider);
        } catch (Exception e) {
            add(new Paragraph("Error loading venues: " + e.getMessage()));
        }
    }
}
