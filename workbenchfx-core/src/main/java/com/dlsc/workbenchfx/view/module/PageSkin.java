package com.dlsc.workbenchfx.view.module;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.module.Module;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TODO
 */
public class PageSkin extends SkinBase<Page> {
  private static final Logger LOGGER = LogManager.getLogger(PageSkin.class.getName());
  private static final int COLUMNS_PER_ROW = 3;

  private final ObservableList<Module> modules;
  private final ReadOnlyIntegerProperty pageIndex;

  private GridPane tilePane;

  /**
   * TODO Constructor for all SkinBase instances.
   *
   * @param control The control for which this Skin should attach to.
   */
  public PageSkin(Page control) {
    super(control);
    pageIndex = control.pageIndexProperty();
    Workbench workbench = control.getWorkbench();
    modules = workbench.getModules();

    initializeParts();

    setupSkin(workbench, pageIndex.get()); // initial setup
    setupListeners(workbench); // setup for changing modules

    getChildren().add(tilePane);
  }

  private void initializeParts() {
    tilePane = new GridPane();
    tilePane.getStyleClass().add("tile-page");
  }

  private void setupListeners(Workbench workbench) {
    LOGGER.trace("Add module listener");
    modules.addListener((InvalidationListener) observable -> {
      setupSkin(workbench, pageIndex.get());
    });
    pageIndex.addListener(observable -> {
      setupSkin(workbench, pageIndex.get());
    });
  }

  private void setupSkin(Workbench workbench, int pageIndex) {
    // remove any pre-existing tiles
    tilePane.getChildren().clear();

    int position = pageIndex * workbench.modulesPerPage;
    int count = 0;
    int column = 0;
    int row = 0;

    while (count < workbench.modulesPerPage && position < modules.size()) {
      Module module = modules.get(position);
      Node tile = workbench.getTile(module);
      tilePane.add(tile, column, row);

      position++;
      count++;
      column++;

      if (column == COLUMNS_PER_ROW) {
        column = 0;
        row++;
      }
    }
  }

}
