package com.dlsc.workbenchfx.view.controls;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * TODO: create description
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class ToolbarControl extends HBox {

  private HBox toolbarControlLeftBox;
  private HBox toolbarControlRightBox;

  /**
   * TODO: create description
   */
  public ToolbarControl() {
    initializeParts();
    layoutParts();
  }

  private void initializeParts() {
    getStyleClass().add("toolbar-control");

    toolbarControlLeftBox = new HBox();
    toolbarControlLeftBox.getStyleClass().add("toolbar-control-left-box");

    toolbarControlRightBox = new HBox();
    toolbarControlRightBox.getStyleClass().add("toolbar-control-right-box");
  }

  public void layoutParts() {
    getChildren().addAll(
        toolbarControlLeftBox,
        toolbarControlRightBox
    );
    HBox.setHgrow(toolbarControlLeftBox, Priority.ALWAYS);
  }

  public HBox getToolbarControlLeftBox() {
    return toolbarControlLeftBox;
  }

  public void setToolbarControlLeftBox(HBox toolbarControlLeftBox) {
    this.toolbarControlLeftBox = toolbarControlLeftBox;
  }

  public HBox getToolbarControlRightBox() {
    return toolbarControlRightBox;
  }

  public void setToolbarControlRightBox(HBox toolbarControlRightBox) {
    this.toolbarControlRightBox = toolbarControlRightBox;
  }
}
