package com.example.user.ui;

import com.example.user.User;
import com.example.user.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("users")
@PageTitle("User Management")
public class UserView extends VerticalLayout {

    public UserView(@Autowired UserService userService) {
        Grid<User> grid = new Grid<>(User.class);
        grid.setItems(userService.findAll());

        Button addButton = new Button("Add user");
        add(addButton, grid);
    }
}
