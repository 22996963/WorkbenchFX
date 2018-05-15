package com.dlsc.workbenchfx.custom.controls;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.module.Module;
import com.dlsc.workbenchfx.view.module.Tab;
import javafx.scene.control.Skin;

/**
 * Represents the standard control used to display {@link Module}s as Tabs in the toolbar.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class CustomTab extends Tab {
  /**
   * Constructs a new {@link CustomTab}.
   *
   * @param workbench which created this {@link CustomTab}
   */
  public CustomTab(Workbench workbench) {
    super(workbench);
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new CustomTabSkin(this);
  }
}
