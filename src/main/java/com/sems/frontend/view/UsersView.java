package com.sems.frontend.view;

import com.sems.frontend.dto.UserDTO;
import com.sems.frontend.service.UserRestService;
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
 * Vaadin View for displaying and managing Users.
 * Features: data binding via Grid component, search filtering.
 */
@Route(value = "users", layout = MainLayout.class)
public class UsersView extends VerticalLayout {

    private final UserRestService userRestService;
    private final Grid<UserDTO> userGrid;
    private List<UserDTO> allUsers;

    @Autowired
    public UsersView(UserRestService userRestService) {
        this.userRestService = userRestService;

        // Layout configuration
        setWidthFull();
        setPadding(true);
        setSpacing(true);

        // Title
        H1 title = new H1("Users Management");
        add(title);

        // Search field with data filtering
        TextField searchField = new TextField();
        searchField.setPlaceholder("Search users by email or name...");
        searchField.setWidthFull();

        // Grid component for data binding
        userGrid = new Grid<>(UserDTO.class, false);
        userGrid.setWidthFull();
        userGrid.setHeight("600px");

        // Column definitions with data binding
        userGrid.addColumn(UserDTO::getId)
                .setHeader("ID")
                .setWidth("80px");
        userGrid.addColumn(UserDTO::getName)
                .setHeader("Name")
                .setFlexGrow(1);
        userGrid.addColumn(UserDTO::getEmail)
                .setHeader("Email")
                .setFlexGrow(1);

        // Load and bind data
        refreshUserData();

        // Search filtering with data binding
        searchField.addValueChangeListener(event -> {
            String filterValue = event.getValue().toLowerCase();
            if (userGrid.getDataProvider() instanceof ListDataProvider) {
                @SuppressWarnings("unchecked")
                ListDataProvider<UserDTO> dataProvider = (ListDataProvider<UserDTO>) userGrid.getDataProvider();
                dataProvider.setFilter(userDTO ->
                        userDTO.getEmail().toLowerCase().contains(filterValue) ||
                        userDTO.getName().toLowerCase().contains(filterValue)
                );
            }
        });

        // Add components
        add(new Paragraph("Browse and manage all users in the system."));
        add(searchField);
        add(userGrid);
    }

    /**
     * Refresh user data from REST API and rebind to Grid.
     */
    private void refreshUserData() {
        try {
            allUsers = userRestService.getAllUsers();
            ListDataProvider<UserDTO> dataProvider = new ListDataProvider<>(allUsers);
            userGrid.setDataProvider(dataProvider);
        } catch (Exception e) {
            add(new Paragraph("Error loading users: " + e.getMessage()));
        }
    }
}
