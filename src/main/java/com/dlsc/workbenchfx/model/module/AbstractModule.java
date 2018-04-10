package com.dlsc.workbenchfx.model.module;

import com.dlsc.workbenchfx.util.WorkbenchFxUtils;
import com.dlsc.workbenchfx.view.module.TabControl;
import com.dlsc.workbenchfx.view.module.TileControl;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Skeletal implementation of a {@link Module}.
 * Extend this class to simply implement a new module and override methods as needed.
 */
public abstract class AbstractModule implements Module {
  private final String name;

  private final Node tile;
  private final Node tab;

  /**
   * Super constructor to be called by the implementing class.
   *
   * @param name of this module
   * @param icon of this module
   */
  protected AbstractModule(String name, Image icon) {
    this.name = name;
    this.tile = new TileControl(name, new ImageView(icon));
    this.tab = new TabControl(name, new ImageView(icon));
  }

  /**
   * Super constructor to be called by the implementing class.
   *
   * @param name of this module
   * @param icon of this module
   */
  protected AbstractModule(String name, FontAwesomeIcon icon) {
    this.name = name;
    this.tile = new TileControl(name, new FontAwesomeIconView(icon));
    this.tab = new TabControl(name, new FontAwesomeIconView(icon));
  }

  /**
   * Super constructor to be called by the implementing class.
   */
  protected AbstractModule(String name, Node tileIcon, Node tabIcon) {
    WorkbenchFxUtils.assertNodeNotSame(tileIcon, tabIcon);
    this.name = name;
    this.tile = new TileControl(name, tileIcon);
    this.tab = new TabControl(name, tabIcon);
  }

  /**
   * Super constructor to be called by the implementing class.
   */
  protected AbstractModule(Node tile, Node tab) {
    WorkbenchFxUtils.assertNodeNotSame(tile, tab);
    this.name = null;
    this.tile = tile;
    this.tab = tab;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Node getTab() {
    return tab;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Node getTile() {
    return tile;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void activate() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deactivate() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void destroy() {

  }

}
