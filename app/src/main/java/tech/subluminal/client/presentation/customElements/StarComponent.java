package tech.subluminal.client.presentation.customElements;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import tech.subluminal.client.presentation.controller.MainController;
import tech.subluminal.shared.stores.records.game.Coordinates;
import tech.subluminal.shared.util.DrawingUtils;

public class StarComponent extends Group {

  public static final int BORDER_WIDTH = 3;
  private final int sizeAll = 60;
  private final DoubleProperty sizeProperty = new SimpleDoubleProperty();
  private final DoubleProperty xProperty = new SimpleDoubleProperty();
  private final DoubleProperty yProperty = new SimpleDoubleProperty();
  private final StringProperty starID = new SimpleStringProperty();
  private final StringProperty ownerID = new SimpleStringProperty();
  private final DoubleProperty possession = new SimpleDoubleProperty();
  private final ObjectProperty color = new SimpleObjectProperty();
  private final DoubleProperty jump = new SimpleDoubleProperty();


  private final IntegerProperty parentWidthProperty = new SimpleIntegerProperty();
  private final IntegerProperty parentHeightProperty = new SimpleIntegerProperty();

  private final BooleanProperty hoverShown = new SimpleBooleanProperty();

  private final String name;
  private final Group border;
  private Circle jumpCircle;

  //private final ObjectProperty

  public StarComponent(String ownerID, String name, double possession, Coordinates coordinates,
      String id, double jump, MainController main) {

    setPossession(possession);
    setXProperty(coordinates.getX());
    setYProperty(coordinates.getY());
    setSizeProperty(0.2);
    setStarID(id);
    setOwnerID(ownerID);
    setJump(jump);

    setColor(Color.GRAY);

    Platform.runLater(() -> {
      parentHeightProperty.bind(main.getPlayArea().heightProperty());
      parentWidthProperty.bind(main.getPlayArea().widthProperty());

      this.layoutXProperty().bind(DrawingUtils.getXPosition(main.getPlayArea(), xProperty));
      this.layoutYProperty().bind(DrawingUtils.getYPosition(main.getPlayArea(), yProperty));
    });

    this.name = name;

    Circle star = new Circle();
    star.setFill(Color.GRAY);

    star.radiusProperty().bind(Bindings.createDoubleBinding(
        () -> sizeProperty.doubleValue() * sizeAll, sizeProperty));

    Circle possessionCount = new Circle();
    possessionCount.setOpacity(0.7);
    possessionCount.setFill(Color.GRAY);
    possessionCount.fillProperty().bind(colorProperty());
    possessionCount.radiusProperty().bind(Bindings
        .createDoubleBinding(() -> star.getRadius() * Math.pow(getPossession(), 0.8),
            possessionProperty(), sizeProperty));

    Group starGroup = new Group();

    Pane glowBox = new Pane();
    glowBox.setPrefHeight(sizeAll);
    glowBox.setPrefWidth(sizeAll);
    glowBox.setTranslateX(-sizeAll / 2);
    glowBox.setTranslateY(-sizeAll / 2);

    border = makeBorder();
    jumpCircle = new Circle();
    jumpCircle.radiusProperty().bind(Bindings
        .createDoubleBinding(() -> jump * parentHeightProperty.getValue(), parentHeightProperty,
            jumpProperty()));
    jumpCircle.setFill(Color.TRANSPARENT);
    jumpCircle.setStroke(Color.RED);
    jumpCircle.setMouseTransparent(true);

    setOnHover(false);

    //starGroup.setB(new Background(new BackgroundFill(Color.RED,CornerRadii.EMPTY,Insets.EMPTY)));

    starGroup.setOnMouseEntered(event -> {
      if(!isHoverShown()){
        setOnHover(true);
      }
    });

    starGroup.setOnMouseExited(event -> {

      if(!isHoverShown()){
        setOnHover(false);
      }

    });

    hoverShownProperty().addListener((observable, oldValue, newValue) -> {
      if(newValue){
        setOnHover(true);
      }else{
        setOnHover(false);
      }
    });

    Label starName = new Label(name);
    starName.getStyleClass().add("starname-label");
    starName.setLayoutY(30);
    Platform.runLater(() -> {
      starName.setLayoutX(-starName.getWidth() / 2);
    });

    starName.setFont(new Font("PxPlus IBM VGA9", 11));

    starGroup.getChildren().addAll(glowBox, border, star, starName, possessionCount);
    Effect glow = new Bloom();
    starGroup.setEffect(glow);
    this.getChildren().addAll(jumpCircle, starGroup);

  }

  public void setOnHover(boolean b) {
    border.setVisible(b);
    jumpCircle.setVisible(b);
  }

  public boolean isHoverShown() {
    return hoverShown.get();
  }

  public BooleanProperty hoverShownProperty() {
    return hoverShown;
  }

  public void setHoverShown(boolean hoverShown) {
    this.hoverShown.set(hoverShown);
  }

  public double getJump() {
    return jump.get();
  }

  public void setJump(double jump) {
    this.jump.set(jump);
  }

  public DoubleProperty jumpProperty() {
    return jump;
  }

  public Object getColor() {
    return color.get();
  }

  public void setColor(Object color) {
    this.color.set(color);
  }

  public ObjectProperty colorProperty() {
    return color;
  }

  public String getName() {
    return name;
  }

  public double getSizeProperty() {
    return sizeProperty.get();
  }

  public void setSizeProperty(double sizeProperty) {
    this.sizeProperty.set(sizeProperty);
  }

  public DoubleProperty sizePropertyProperty() {
    return sizeProperty;
  }

  public double getxProperty() {
    return xProperty.get();
  }

  public DoubleProperty xPropertyProperty() {
    return xProperty;
  }

  public void setXProperty(double xProperty) {
    this.xProperty.set(xProperty);
  }

  public double getyProperty() {
    return yProperty.get();
  }

  public DoubleProperty yPropertyProperty() {
    return yProperty;
  }

  public void setYProperty(double yProperty) {
    this.yProperty.set(yProperty);
  }

  public String getOwnerID() {
    return ownerID.get();
  }

  public void setOwnerID(String ownerIDProperty) {
    this.ownerID.set(ownerIDProperty);
  }

  public StringProperty ownerIDProperty() {
    return ownerID;
  }

  public String getStarID() {
    return starID.get();
  }

  public void setStarID(String starID) {
    this.starID.set(starID);
  }

  public StringProperty starIDProperty() {
    return starID;
  }

  public double getPossession() {
    return possession.get();
  }

  public void setPossession(double possession) {
    this.possession.set(possession);
  }

  public DoubleProperty possessionProperty() {
    return possession;
  }

  private Group makeBorder() {
    Group border = new Group();

    for (int y = 0; y < 2; y++) {
      for (int x = 0; x < 2; x++) {
        Rectangle focus = new Rectangle(BORDER_WIDTH, sizeAll / 5);

        focus.setFill(Color.RED);
        focus.setX(x * (sizeAll - BORDER_WIDTH) - (sizeAll / 2));
        focus.setY(y * (sizeAll - sizeAll / 5) - (sizeAll / 2));
        border.getChildren().add(focus);
      }
    }
    for (int y = 0; y < 2; y++) {
      for (int x = 0; x < 2; x++) {
        Rectangle focus = new Rectangle(sizeAll / 5, BORDER_WIDTH);

        focus.setFill(Color.RED);
        focus.setX(x * (sizeAll - sizeAll / 5) - (sizeAll / 2));
        focus.setY(y * (sizeAll - BORDER_WIDTH) - (sizeAll / 2));
        border.getChildren().add(focus);
      }
    }

    return border;
  }
}
