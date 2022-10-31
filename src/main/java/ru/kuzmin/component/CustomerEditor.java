package ru.kuzmin.component;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ru.kuzmin.entity.CustomerEntity;
import ru.kuzmin.repository.CustomerRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;


@SpringComponent
@UIScope
public class CustomerEditor extends VerticalLayout implements KeyNotifier {
    private final CustomerRepository customerRepository;
    private CustomerEntity customerEntity;
    private TextField name = new TextField("", "Name");
    private TextField gender = new TextField("", "Gender");
    private TextField birthday = new TextField("", "Birthday");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private HorizontalLayout buttons = new HorizontalLayout(save, delete);
    private Binder<CustomerEntity> binder = new Binder<>(CustomerEntity.class);
    @Setter
    private ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public CustomerEditor(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        add(name, gender, birthday, buttons);
        binder.bindInstanceFields(this);
        setSpacing(true);
        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");
        addKeyPressListener(Key.ENTER, e -> save());
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        setVisible(false);
    }

    private void save() {
        customerRepository.save(customerEntity);
        changeHandler.onChange();
    }

    private void delete() {
        customerRepository.delete(customerEntity);
        changeHandler.onChange();
    }

    public void editCustomer(CustomerEntity customerEntity) {
        if (customerEntity == null) {
            setVisible(false);
            return;
        }
        if (customerEntity.getId() != null) {
            this.customerEntity = customerRepository.findById(customerEntity.getId()).orElse(customerEntity);
        } else {
            this.customerEntity = customerEntity;
        }
        binder.setBean(this.customerEntity);
        setVisible(true);
        gender.focus();
    }
}
