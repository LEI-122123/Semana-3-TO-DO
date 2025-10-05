package com.example.user.ui;

import com.example.user.User;
import com.example.user.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

//  @Menu icon - https://vaadin.com/docs/latest/components/icons/default-icons

@Route("users") //  Application URL path /users
@PageTitle("User Management")
@Menu(order = 1, icon = "vaadin:users", title = "User Manager")
public class UserView extends VerticalLayout {

    public UserView(@Autowired UserService userService) {
        Grid<User> grid = new Grid<>(User.class);
        grid.setItems(userService.findAll());

        Button addButton = new Button("Add user");
        add(addButton, grid);
    }
}
