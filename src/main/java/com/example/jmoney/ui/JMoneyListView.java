package com.example.jmoney.ui;

import com.example.base.ui.component.ViewToolbar;
import com.example.jmoney.JMoney;
import com.example.jmoney.JMoneyService;
import com.example.pdfexporter.PdfExporter;
import com.example.user.User;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.joda.money.Money;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Comparator;
import java.util.Optional;

import static com.vaadin.flow.spring.data.VaadinSpringDataHelpers.toSpringPageRequest;

@Route("wallet")
@PageTitle("My Wallet (€)")
@Menu(order = 9, icon = "vaadin:money", title = "Wallet")
class JMoneyListView extends Main {

    private final JMoneyService jMoneyService;

    private final ComboBox<String> type;
    private final TextField description;
    private final NumberField amount;
    private final DatePicker transactionDate;
    private final Button createBtn;
    private final Grid<JMoney> jMoneyGrid;
    private final Span balanceDisplay;

    JMoneyListView(JMoneyService jMoneyService) {
        this.jMoneyService = jMoneyService;

        // === Inputs ===
        type = new ComboBox<>("Type");
        type.setItems("Expense", "Income");
        type.setPlaceholder("Select type");

        description = new TextField("Description");
        description.setPlaceholder("What was this for?");
        description.setMinWidth("12em");

        amount = new NumberField("Amount (€)");
        amount.setPrefixComponent(new Span("€"));
        amount.setMin(0);

        transactionDate = new DatePicker("Date");
        transactionDate.setPlaceholder("Transaction date");

        createBtn = new Button("Add Record", event -> createJMoney());
        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // PDF Exporter button
        PdfExporter<JMoney> exporter = new PdfExporter<>();
        Button downloadBtn = exporter.pdfExportButton(this, "wallet", jMoneyService);
        downloadBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);

        // === Buttons stacked vertically ===
        VerticalLayout buttonsLayout = new VerticalLayout(downloadBtn, createBtn);
        buttonsLayout.setPadding(false);
        buttonsLayout.setSpacing(true);
        buttonsLayout.setAlignItems(FlexComponent.Alignment.START);

        // === Inputs row with buttons at the end ===
        HorizontalLayout inputsLayout = new HorizontalLayout(new ViewToolbar("My Wallet (€)",ViewToolbar.group(type, description, amount, transactionDate, buttonsLayout)));
        inputsLayout.setAlignItems(FlexComponent.Alignment.END); // aligns button stack with bottom of inputs
        inputsLayout.setSpacing(true);
        inputsLayout.setVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        // === Balance Display ===
        balanceDisplay = new Span();
        balanceDisplay.getStyle().set("font-weight", "bold");
        updateBalance();

        // === Toolbar vertical layout: inputs row + balance display row ===
        VerticalLayout toolbar = new VerticalLayout(inputsLayout, balanceDisplay);
        toolbar.setPadding(false);
        toolbar.setSpacing(true);

        // === Grid ===
        var dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                .withLocale(getLocale());

        jMoneyGrid = new Grid<>(JMoney.class, false);
        jMoneyGrid.addColumn(JMoney::getType).setHeader("Type").setAutoWidth(true);
        jMoneyGrid.addColumn(JMoney::getDescription).setHeader("Description").setAutoWidth(true);
        jMoneyGrid.addColumn(j -> j.getMoney().toString()).setHeader("Amount (€)").setAutoWidth(true);
        jMoneyGrid.addColumn(j ->
                        Optional.ofNullable(j.getTransactionDate())
                                .map(dateFormatter::format)
                                .orElse("No date"))
                .setHeader("Transaction Date")
                .setAutoWidth(true);

        jMoneyGrid.setItems(query ->
                jMoneyService.list(toSpringPageRequest(query)).stream()
                        .sorted(Comparator.comparing(JMoney::getTransactionDate,
                                Comparator.nullsLast(Comparator.reverseOrder())))
        );
        jMoneyGrid.setSizeFull();

        // === Layout for the view ===
        setSizeFull();
        addClassNames(LumoUtility.BoxSizing.BORDER, LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.COLUMN, LumoUtility.Padding.MEDIUM,
                LumoUtility.Gap.SMALL);

        add(toolbar, jMoneyGrid);
    }


    private void createJMoney() {
        if (type.isEmpty() || amount.isEmpty()) {
            Notification.show("Please fill all required fields (Type, Amount)")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        jMoneyService.createJMoney(
                type.getValue(),
                description.getValue(),
                amount.getValue(),
                transactionDate.getValue()
        );

        jMoneyGrid.getDataProvider().refreshAll();
        clearForm();
        updateBalance();

        Notification.show("Transaction added", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void clearForm() {
        type.clear();
        description.clear();
        amount.clear();
        transactionDate.clear();
    }

    private void updateBalance() {
        Money balance = jMoneyService.calculateBalance();
        balanceDisplay.setText("Current balance: " + balance);
    }
}
