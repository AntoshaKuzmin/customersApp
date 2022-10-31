package ru.kuzmin.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import ru.kuzmin.component.CustomerEditor;
import ru.kuzmin.entity.CustomerEntity;
import ru.kuzmin.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")//отображает компоненты как страницы
public class CustomerList extends VerticalLayout {//выставляет элементы в виде столбика
    private final CustomerRepository customerRepository;
    private final CustomerEditor customerEditor;
    private Grid<CustomerEntity> customerGrid= new Grid<>(CustomerEntity.class);//таблица
    private final TextField filter = new TextField();
    private final Button addNewButton = new Button("New customer", VaadinIcon.PLUS.create());
    private final HorizontalLayout toolbar = new HorizontalLayout(filter, addNewButton);
    //выставляет элементы в виде строки
    @Autowired
    public CustomerList(CustomerRepository customerRepository, CustomerEditor customerEditor) {
        this.customerRepository = customerRepository;
        this.customerEditor = customerEditor;
        filter.setPlaceholder("Type to filter");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(field -> fillList(field.getValue()));
        add(toolbar, customerGrid, customerEditor);
        customerGrid
                .asSingleSelect()
                .addValueChangeListener(e -> customerEditor.editCustomer(e.getValue()));
        addNewButton.addClickListener(e -> customerEditor.editCustomer(new CustomerEntity()));
        customerEditor.setChangeHandler(() -> {
            customerEditor.setVisible(false);
            fillList(filter.getValue());
        });
        fillList("");
    }

    private void fillList(String name) {
        if (name.isEmpty()) {
            customerGrid.setItems(this.customerRepository.findAll());
        } else {
            customerGrid.setItems(this.customerRepository.findByName(name));
        }
    }
}
