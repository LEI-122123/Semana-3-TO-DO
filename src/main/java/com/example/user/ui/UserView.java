package com.example.user.ui;

import com.example.user.User;
import com.example.user.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import static com.vaadin.flow.spring.data.VaadinSpringDataHelpers.toSpringPageRequest;

@Route("users")
@PageTitle("User Management")
@Menu(order = 1, icon = "vaadin:users", title = "User Manager")
public class UserView extends VerticalLayout {

    private final UserService userService;
    private final Grid<User> userGrid;

    public UserView(UserService userService) {
        this.userService = userService;

        // Grid setup
        userGrid = new Grid<>(User.class, false);
        userGrid.addColumn(User::getId).setHeader("ID");
        userGrid.addColumn(User::getName).setHeader("Name");
        userGrid.addColumn(User::getEmail).setHeader("Email");

        // Set data provider
//        userGrid.setItems(userService.findAll());
        userGrid.setItems(query -> userService.list(toSpringPageRequest(query)).stream());

        // Add User button
        Button addUserBtn = new Button("Add User", e -> openAddUserDialog());
        addUserBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add(addUserBtn, userGrid);
        setSizeFull();
    }

    private void openAddUserDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Add New User");

        // Input fields
        TextField nameField = new TextField("Name");
        TextField emailField = new TextField("Email");
        PasswordField passwordField = new PasswordField("Password");

        FormLayout formLayout = new FormLayout(nameField, emailField, passwordField);
        formLayout.setWidth("400px");

        // Buttons
        Button saveBtn = new Button("Save", event -> {
            String name = nameField.getValue();
            String email = emailField.getValue();
            String password = passwordField.getValue();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Notification.show("All fields are required", 3000, Notification.Position.MIDDLE);
                return;
            }

            // Use service method to create user
            userService.createUser(name, email, password);

            // Refresh grid and clear fields
            userGrid.getDataProvider().refreshAll();
            nameField.clear();
            emailField.clear();
            passwordField.clear();
            dialog.close();

            Notification.show("User added", 3000, Notification.Position.BOTTOM_END)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });

        Button cancelBtn = new Button("Cancel", event -> dialog.close());
        HorizontalLayout buttons = new HorizontalLayout(cancelBtn, saveBtn);

        dialog.add(formLayout);
        dialog.getFooter().add(buttons);
        dialog.open();
    }
}
