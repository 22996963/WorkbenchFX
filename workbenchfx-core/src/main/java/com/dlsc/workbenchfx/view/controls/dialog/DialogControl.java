package com.dlsc.workbenchfx.view.controls.dialog;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchDialog;
import com.dlsc.workbenchfx.view.controls.GlassPane;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the standard control used to display dialogs in the {@link Workbench}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class DialogControl extends Control {
  private static final Logger LOGGER =
      LogManager.getLogger(DialogControl.class.getName());

  private final BooleanProperty buttonTextUppercase =
      new SimpleBooleanProperty(this, "buttonTextUppercase", false);

  private final ObjectProperty<Workbench> workbench =
      new SimpleObjectProperty<>(this, "workbench");
  private final ObjectProperty<WorkbenchDialog> dialog =
      new SimpleObjectProperty<>(this, "dialog");
  private final ObjectProperty<EventHandler<Event>> onHidden =
      new SimpleObjectProperty<>(this, "onHidden");
  private final ObjectProperty<EventHandler<Event>> onShown =
      new SimpleObjectProperty<>(this, "onShown");

  private final ObservableList<Button> buttons = FXCollections.observableArrayList();
  private final Map<ButtonType, Button> buttonNodes = new WeakHashMap<>();

  private InvalidationListener dialogChangedListener;

  /**
   * Creates a dialog control.
   */
  public DialogControl() {
    // makes sure that when clicking on transparent pixels outside of dialog, GlassPane will still
    // receive click events!
    setPickOnBounds(false);

    setupChangeListeners();
  }

  private void setupChangeListeners() {
    // update buttons whenever dialog, buttonTypes, workbench, or buttonTextUppercase changes
    dialogChangedListener = observable -> {
      buttonNodes.clear(); // force re-creation of buttons
      updateButtons(getDialog());
      fireOnShown();
    };
    dialog.addListener((observable, oldDialog, newDialog) -> {
      updateButtons(newDialog);
      if (!Objects.isNull(oldDialog)) {
        oldDialog.getButtonTypes().removeListener(dialogChangedListener);
      }
      if (!Objects.isNull(newDialog)) {
        newDialog.getButtonTypes().addListener(dialogChangedListener);
      }
      fireOnShown();
    });
    buttonTextUppercase.addListener(dialogChangedListener);

    // fire onHidden event, when dialog is hidden
    visibleProperty().addListener((observable, oldVisible, newVisible) -> {
      if (oldVisible && !newVisible) {
        // dialog has been hidden
        fireOnHidden();
      }
    });
  }

  private void updateButtons(WorkbenchDialog dialog) {
    LOGGER.trace("Updating buttons");

    buttons.clear();

    if (Objects.isNull(dialog)) {
      return;
    }

    boolean hasDefault = false;
    for (ButtonType cmd : dialog.getButtonTypes()) {
      Node button = buttonNodes.computeIfAbsent(cmd, dialogButton -> createButton(cmd));

      // keep only first default button
      if (button instanceof Button) {
        ButtonBar.ButtonData buttonType = cmd.getButtonData();

        ((Button) button).setDefaultButton(
            !hasDefault && buttonType != null && buttonType.isDefaultButton()
        );
        ((Button) button).setCancelButton(buttonType != null && buttonType.isCancelButton());
        ((Button) button).setOnAction(evt -> {
          getDialog().getOnResult().accept(cmd);
          hide();
        });

        hasDefault |= buttonType != null && buttonType.isDefaultButton();
      }
      buttons.add(button);
    }
  }

  private void fireOnShown() {
    if (!Objects.isNull(getDialog()) && !Objects.isNull(getOnShown())) {
      LOGGER.trace("Firing onShown event - Dialog is initialized and being shown");
      getOnShown().handle(new Event(this, this, Event.ANY));
    }
  }

  private void fireOnHidden() {
    if (!Objects.isNull(getOnHidden())) {
      LOGGER.trace("Firing onHidden event - Dialog has been hidden");
      getOnHidden().handle(new Event(this, this, Event.ANY));
    }
  }

  private Node createButton(ButtonType buttonType) {
    LOGGER.trace("Create Button: " + buttonType.getText());
    String buttonText;
    if (isButtonTextUppercase()) {
      buttonText = buttonType.getText().toUpperCase();
    } else {
      buttonText = buttonType.getText();
    }
    final Button button = new Button(buttonText);
    final ButtonBar.ButtonData buttonData = buttonType.getButtonData();
    ButtonBar.setButtonData(button, buttonData);
    button.setDefaultButton(buttonData.isDefaultButton());
    button.setCancelButton(buttonData.isCancelButton());
    return button;
  }

  public final void hide() {
    getWorkbench().hideDialog(getDialog());
  }

  // EventHandler

  /**
   * The dialog's action, which is invoked whenever the dialog has been fully initialized and is
   * being shown. Whenever the {@link #dialogProperty()}, {@link WorkbenchDialog#buttonTypes},
   * {@link #buttonTextUppercaseProperty()} or {@link #workbenchProperty()} changes, the dialog will
   * be rebuilt and upon completion, an event will be fired.
   *
   * @return the property to represent the event, which is invoked whenever the dialog has been
   *         fully initialized and is being shown.
   */
  public final ObjectProperty<EventHandler<Event>> onShownProperty() {
    return onShown;
  }

  public final void setOnShown(EventHandler<Event> value) {
    onShown.set(value);
  }

  public final EventHandler<Event> getOnShown() {
    return onShown.get();
  }

  /**
   * The dialog's action, which is invoked whenever the dialog has been hidden in the scene graph.
   * An event will be fired whenever {@link #hide()} or
   * {@link Workbench#hideDialog(WorkbenchDialog)} has been called or the dialog has been closed by
   * clicking on its corresponding {@link GlassPane}.
   *
   * @return the property to represent the event, which is invoked whenever the dialog has been
   *         hidden in the scene graph.
   */
  public final ObjectProperty<EventHandler<Event>> onHiddenProperty() {
    return onHidden;
  }

  public final void setOnHidden(EventHandler<Event> value) {
    onHidden.set(value);
  }

  public final EventHandler<Event> getOnHidden() {
    return onHidden.get();
  }

  // Accessors and mutators

  public WorkbenchDialog getDialog() {
    return dialog.get();
  }

  public ObjectProperty<WorkbenchDialog> dialogProperty() {
    return dialog;
  }

  public void setDialog(WorkbenchDialog dialog) {
    this.dialog.set(dialog);
  }

  public ObservableList<Node> getButtons() {
    return FXCollections.unmodifiableObservableList(buttons);
  }

  public boolean isButtonTextUppercase() {
    return buttonTextUppercase.get();
  }

  public BooleanProperty buttonTextUppercaseProperty() {
    return buttonTextUppercase;
  }

  public void setButtonTextUppercase(boolean buttonTextUppercase) {
    this.buttonTextUppercase.set(buttonTextUppercase);
  }

  public Workbench getWorkbench() {
    return workbench.get();
  }

  public final void setWorkbench(Workbench workbench) {
    this.workbench.set(workbench);
  }

  public ObjectProperty<Workbench> workbenchProperty() {
    return workbench;
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new DialogSkin(this);
  }
}
