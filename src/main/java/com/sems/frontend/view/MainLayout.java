package com.sems.frontend.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;

/**
 * Main application layout with navigation (routing).
 * Provides the app shell with sidebar navigation and view switching.
 */
public class MainLayout extends AppLayout {

    public MainLayout() {
        // Create drawer toggle
        DrawerToggle toggle = new DrawerToggle();

        // Create app title
        H1 title = new H1("SEMS - Social Events Management System");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        // Create navigation
        SideNav nav = new SideNav();
        nav.setCollapsible(true);
        nav.setLabel("Navigation");

        // Navigation items with routing
        nav.addItem(new SideNavItem("Dashboard", EventsView.class));
        nav.addItem(new SideNavItem("Events", EventsView.class));
        nav.addItem(new SideNavItem("Venues", VenuesView.class));
        nav.addItem(new SideNavItem("Users", UsersView.class));
        nav.addItem(new SideNavItem("Tickets", TicketsView.class));

        // Set drawer content
        addToDrawer(nav);

        // Set app bar content
        addToNavbar(toggle, title);
    }
}
