package com.tokmeninov.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ListDataProvider;
import com.tokmeninov.gwt.shared.PersonResp;

import java.util.List;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GwtWebApp implements EntryPoint {
  /**
   * The message displayed to the user when the server cannot be reached or
   * returns an error.
   */

  /**
   * Create a remote service proxy to talk to the server-side Greeting service.
   */
  private final PersonServiceAsync personService = GWT.create(PersonService.class);

  private final TextBox nameField = new TextBox();
  private final TextBox surnameField = new TextBox();
  private final TextBox patronymicField = new TextBox();

  private int id = -1;

  private ListDataProvider<PersonResp> createTable(CellTable<PersonResp> table){
    TextColumn<PersonResp> nameColumn = new TextColumn<PersonResp>() {
      @Override
      public String getValue(PersonResp person) {
        return person.getName();
      }
    };
    TextColumn<PersonResp> surnameColumn = new TextColumn<PersonResp>() {
      @Override
      public String getValue(PersonResp person) {
        return person.getSurname();
      }
    };
    TextColumn<PersonResp> patronymicColumn = new TextColumn<PersonResp>() {
      @Override
      public String getValue(PersonResp person) {
        return person.getPatronymic();
      }
    };
    table.addColumn(nameColumn, "Name");
    table.addColumn(surnameColumn, "Surname");
    table.addColumn(patronymicColumn, "Patronymic");
    ListDataProvider<PersonResp> dataProvider = new ListDataProvider<>();
    dataProvider.addDataDisplay(table);
    personService.list(new AsyncCallback<List<PersonResp>>() {
      @Override
      public void onFailure(Throwable throwable) {
        GWT.log("error",throwable);
      }

      @Override
      public void onSuccess(List<PersonResp> people) {
        dataProvider.getList().addAll(people);
      }
    });
    return dataProvider;
  }
  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    CellTable<PersonResp> table = new CellTable<>();
    ListDataProvider<PersonResp> dataProvider = createTable(table);
    DialogBox dialog = editDialog(dataProvider);
    Button add = new Button("Добавить", new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        nameField.setValue("");
        surnameField.setValue("");
        patronymicField.setValue("");
        id=-1;
        dialog.center();
        dialog.show();
      }
    });
    Button edit = new Button("Редактировать", new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        PersonResp personResp = dataProvider.getList().get(table.getKeyboardSelectedRow());
        nameField.setValue(personResp.getName());
        surnameField.setValue(personResp.getSurname());
        id = (int)personResp.getId();
        dialog.show();
        dialog.center();
      }
    });
    Button delete = new Button("Удалить", new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        final int index = table.getKeyboardSelectedRow();
        PersonResp person = dataProvider.getList().get(index);
        personService.delete(person, new AsyncCallback<Boolean>() {
          @Override
          public void onFailure(Throwable throwable) {
            GWT.log("error",throwable);
          }

          @Override
          public void onSuccess(Boolean aBoolean) {
            dataProvider.getList().remove(index);
          }
        });
      }
    });
    HorizontalPanel control = new HorizontalPanel();
    control.add(add);
    control.add(edit);
    control.add(delete);
    VerticalPanel panel = new VerticalPanel();
    panel.add(control);
    panel.add(table);
    RootPanel.get().add(panel);
  }

  private DialogBox editDialog(ListDataProvider<PersonResp> dataProvider){
    final DialogBox dialogBox = new DialogBox();
    dialogBox.setText("Добавить запись");
    dialogBox.setAnimationEnabled(true);
    VerticalPanel dPanel = new VerticalPanel();
    HorizontalPanel namePanel = new HorizontalPanel();
    Label label= new Label("Name");
    label.setWidth("200px");
    namePanel.add(label);
    namePanel.add(nameField);
    dPanel.add(namePanel);

    HorizontalPanel surnamePanel = new HorizontalPanel();
    label= new Label("Surname");
    label.setWidth("200px");
    surnamePanel.add(label);
    surnamePanel.add(surnameField);
    dPanel.add(surnamePanel);

    HorizontalPanel patronymicPanel = new HorizontalPanel();
    label= new Label("Patronymic");
    label.setWidth("200px");
    patronymicPanel.add(label);
    patronymicPanel.add(patronymicField);
    dPanel.add(patronymicPanel);

    HorizontalPanel dControl = new HorizontalPanel();
    dControl.add(new Button("Сохранить", new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        personService.save(new PersonResp(id, nameField.getValue(), surnameField.getValue(), patronymicField.getValue()), new AsyncCallback<PersonResp>() {
          @Override
          public void onFailure(Throwable throwable) {
            GWT.log("error,",throwable);
          }

          @Override
          public void onSuccess(PersonResp person) {
            if (id != -1){
              dataProvider.getList().set(
                      dataProvider.getList().indexOf(person),
                      person
              );
            }else {
              dataProvider.getList().add(person);
            }
            dialogBox.hide();
          }
        });
      }
    }));
    dControl.add(new Button("Отменить", new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        dialogBox.hide();
      }
    }));
    dPanel.add(dControl);

    dialogBox.setWidget(dPanel);
    return dialogBox;
  }
}
