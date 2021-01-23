package view;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import viewmodel.ViewModel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.net.URL;
import java.util.*;

public class FlightController implements Initializable, Observer {

    private static final String CVS_SPLIT_BY = ",";

    @FXML
    private Canvas airplane;
    @FXML
    private Canvas markX;
    @FXML
    private TextArea TextArea;
    @FXML
    private TextField port;
    @FXML
    private TextField ip;
    @FXML
    private Button submit;
    @FXML
    private Slider throttle;
    @FXML
    private Slider rudder;
    @FXML
    private RadioButton auto;
    @FXML
    private MapDisplayer map;
    @FXML
    private RadioButton manual;
    @FXML
    private Circle border;
    @FXML
    private Circle Joystick;
    @FXML
    private TitledPane background;
    private final Stage stage = new Stage();

    private static int identifier;
    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;
    public DoubleProperty markSceneX, markSceneY;
    public DoubleProperty aileron;
    public DoubleProperty elevator;
    public DoubleProperty airplaneX;
    public DoubleProperty airplaneY;
    public DoubleProperty startX;
    public DoubleProperty startY;
    public DoubleProperty offset;
    public DoubleProperty heading;
    private BooleanProperty path;
    public double lastX;
    public double lastY;
    public int[][] mapData;
    private Image[] plane;
    private Image mark;
    private ViewModel viewModel;
    private String[] solution;

    public void LoadDate() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        fileChooser.setCurrentDirectory(new File("./"));

        int v = fileChooser.showOpenDialog(null);
        if (v == JFileChooser.APPROVE_OPTION) {
            BufferedReader bufferedReader;
            String line = "";
            List<String[]> numbers = new ArrayList<>();

            try {
                bufferedReader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()));
                String[] start = bufferedReader.readLine().split(CVS_SPLIT_BY);
                startX.setValue(Double.parseDouble(start[0]));
                startY.setValue(Double.parseDouble(start[1]));
                start = bufferedReader.readLine().split(CVS_SPLIT_BY);
                offset.setValue(Double.parseDouble(start[0]));

                while ((line = bufferedReader.readLine()) != null) {
                    numbers.add(line.split(CVS_SPLIT_BY));
                }

                mapData = new int[numbers.size()][];

                for (int i = 0; i < numbers.size(); i++) {
                    mapData[i] = new int[numbers.get(i).length];

                    for (int j = 0; j < numbers.get(i).length; j++) {
                        String tmp = numbers.get(i)[j];
                        mapData[i][j] = Integer.parseInt(tmp);

                    }
                }

                this.viewModel.setData(mapData);
                this.drawAirplane();
                map.setMapData(mapData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void LoadText() {
        TextArea.clear();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("./"));
        int value = fileChooser.showOpenDialog(null);

        if (value == JFileChooser.APPROVE_OPTION) {
            try {
                Scanner s = new Scanner(new BufferedReader(new FileReader(fileChooser.getSelectedFile())));
                while (s.hasNextLine()) {
                    TextArea.appendText(s.nextLine());
                    TextArea.appendText("\n");
                }
                viewModel.parse();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void Connect() {
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Popup.fxml"));
            root = fxmlLoader.load();
            FlightController flightController = fxmlLoader.getController();
            flightController.viewModel = this.viewModel;
            stage.setTitle("Connect");
            stage.setScene(new Scene(root));

            if (!stage.isShowing()) {
                stage.show();
                identifier = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Calc() {
        Parent root;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Popup.fxml"));
            root = fxmlLoader.load();
            FlightController flightController = fxmlLoader.getController();
            flightController.viewModel = this.viewModel;
            flightController.mapData = this.mapData;
            flightController.markX = this.markX;
            flightController.path = new SimpleBooleanProperty();
            flightController.path.bindBidirectional(this.path);
            stage.setTitle("Calculate Path");
            stage.setScene(new Scene(root));
            if (!stage.isShowing()) {
                identifier = 1;
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Submit() {
        this.viewModel.ip.bindBidirectional(ip.textProperty());
        this.viewModel.port.bindBidirectional(port.textProperty());

        if (identifier == 0) {
            viewModel.connect();
            Stage stage = (Stage) submit.getScene().getWindow();
            stage.close();
        }

        if (identifier == 1) {
            double H = markX.getHeight();
            double W = markX.getWidth();
            double h = H / mapData.length;
            double w = W / mapData[0].length;
            viewModel.findPath(h, w);
            path.setValue(true);
            Stage stage = (Stage) submit.getScene().getWindow();
            stage.close();
        }

        ip.clear();
        port.clear();
    }

    public void AutoPilot() {
        Select("auto");
    }

    public void Manual() {
        Select("manual");
    }

    public void Select(final String flightState) {
        if (flightState.equals("auto")) {
            if (manual.isSelected()) {
                manual.setSelected(false);
                auto.setSelected(true);
            }
            viewModel.execute();
        } else if (flightState.equals("manual")) {
            if (auto.isSelected()) {
                auto.setSelected(false);
                manual.setSelected(true);
                viewModel.stopAutoPilot();
            }

        }
    }

    public void drawAirplane() {
        if (airplaneX.getValue() != null && airplaneY.getValue() != null) {
            double H = airplane.getHeight();
            double W = airplane.getWidth();
            double h = H / mapData.length;
            double w = W / mapData[0].length;
            GraphicsContext graphicsContext2D = airplane.getGraphicsContext2D();
            lastX = airplaneX.getValue();
            lastY = airplaneY.getValue() * -1;
            graphicsContext2D.clearRect(0, 0, W, H);

            if (heading.getValue() >= 0 && heading.getValue() < 39) {
                graphicsContext2D.drawImage(plane[0], w * lastX, lastY * h, 25, 25);
            }
            if (heading.getValue() >= 39 && heading.getValue() < 80) {
                graphicsContext2D.drawImage(plane[1], w * lastX, lastY * h, 25, 25);
            }
            if (heading.getValue() >= 80 && heading.getValue() < 129) {
                graphicsContext2D.drawImage(plane[2], w * lastX, lastY * h, 25, 25);
            }
            if (heading.getValue() >= 129 && heading.getValue() < 170) {
                graphicsContext2D.drawImage(plane[3], w * lastX, lastY * h, 25, 25);
            }
            if (heading.getValue() >= 170 && heading.getValue() < 219) {
                graphicsContext2D.drawImage(plane[4], w * lastX, lastY * h, 25, 25);
            }
            if (heading.getValue() >= 219 && heading.getValue() < 260) {
                graphicsContext2D.drawImage(plane[5], w * lastX, lastY * h, 25, 25);
            }
            if (heading.getValue() >= 260 && heading.getValue() < 309) {
                graphicsContext2D.drawImage(plane[6], w * lastX, lastY * h, 25, 25);
            }
            if (heading.getValue() >= 309) {
                graphicsContext2D.drawImage(plane[7], w * lastX, lastY * h, 25, 25);
            }
        }
    }

    public void drawMark() {
        double height = markX.getHeight();
        double width = markX.getWidth();
        double h = height / mapData.length;
        double w = width / mapData[0].length;

        GraphicsContext graphicsContext2D = markX.getGraphicsContext2D();
        graphicsContext2D.clearRect(0, 0, width, height);
        graphicsContext2D.drawImage(mark, markSceneX.getValue() - 13, markSceneY.getValue(), 25, 25);

        if (path.getValue()) {
            viewModel.findPath(h, w);
        }
    }

    public void drawLine() {
        double height = markX.getHeight();
        double width = markX.getWidth();
        double h = height / mapData.length;
        double w = width / mapData[0].length;

        GraphicsContext graphicsContext2D = markX.getGraphicsContext2D();
        String move = solution[1];

        double x = airplaneX.getValue() * w + 10 * w;
        double y = airplaneY.getValue() * -h + 6 * h;

        for (int i = 2; i < solution.length; i++) {
            switch (move) {
                case "Right":
                    graphicsContext2D.setStroke(Color.BLACK.darker());
                    graphicsContext2D.strokeLine(x, y, x + w, y);
                    x += w;
                    break;
                case "Left":
                    graphicsContext2D.setStroke(Color.BLACK.darker());
                    graphicsContext2D.strokeLine(x, y, x - w, y);
                    x -= w;
                    break;
                case "Up":
                    graphicsContext2D.setStroke(Color.BLACK.darker());
                    graphicsContext2D.strokeLine(x, y, x, y - h);
                    y -= h;
                    break;
                case "Down":
                    graphicsContext2D.setStroke(Color.BLACK.darker());
                    graphicsContext2D.strokeLine(x, y, x, y + h);
                    y += h;
            }
            move = solution[i];
        }
    }

    EventHandler<MouseEvent> mapClick = new EventHandler<>() {
        @Override
        public void handle(MouseEvent event) {
            markSceneX.setValue(event.getX());
            markSceneY.setValue(event.getY());
            drawMark();
        }
    };

    EventHandler<MouseEvent> joystickClick =
            new EventHandler<>() {
                @Override
                public void handle(MouseEvent event) {
                    orgSceneX = event.getSceneX();
                    orgSceneY = event.getSceneY();
                    orgTranslateX = ((Circle) (event.getSource())).getTranslateX();
                    orgTranslateY = ((Circle) (event.getSource())).getTranslateY();
                }
            };

    EventHandler<MouseEvent> joystickMove =
            new EventHandler<>() {
                @Override
                public void handle(MouseEvent event) {
                    double offsetX = event.getSceneX() - orgSceneX;
                    double offsetY = event.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;
                    if (isInCircle(newTranslateX, newTranslateY)) {
                        ((Circle) (event.getSource())).setTranslateX(newTranslateX);
                        ((Circle) (event.getSource())).setTranslateY(newTranslateY);
                        if (manual.isSelected()) {
                            aileron.setValue(normalizationX(newTranslateX));
                            elevator.setValue(normalizationY(newTranslateY));
                            viewModel.setJoystick();
                        }
                    }
                }
            };

    private double normalizationX(double num) {
        double max = (border.getRadius() - Joystick.getRadius()) + border.getCenterX();
        double min = border.getCenterX() - (border.getRadius() - Joystick.getRadius());
        double new_max = 1;
        double new_min = -1;
        return (((num - min) / (max - min) * (new_max - new_min) + new_min));
    }

    private double normalizationY(double num) {
        double min = (border.getRadius() - Joystick.getRadius()) + border.getCenterY();
        double max = border.getCenterY() - (border.getRadius() - Joystick.getRadius());
        double new_max = 1;
        double new_min = -1;
        return (((num - min) / (max - min) * (new_max - new_min) + new_min));
    }

    private boolean isInCircle(double x, double y) {
        return (Math.pow((x - border.getCenterX()), 2) + Math.pow((y - border.getCenterY()), 2)) <= Math.pow(border.getRadius() - Joystick.getRadius(), 2);
    }

    EventHandler<MouseEvent> joystickRelease = new EventHandler<>() {
        @Override
        public void handle(MouseEvent t) {

            ((Circle) (t.getSource())).setTranslateX(orgTranslateX);
            ((Circle) (t.getSource())).setTranslateY(orgTranslateY);
        }
    };

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
        throttle.valueProperty().bindBidirectional(viewModel.throttle);
        rudder.valueProperty().bindBidirectional(viewModel.rudder);
        aileron = new SimpleDoubleProperty();
        elevator = new SimpleDoubleProperty();
        aileron.bindBidirectional(viewModel.aileron);
        elevator.bindBidirectional(viewModel.elevator);
        airplaneX = new SimpleDoubleProperty();
        airplaneY = new SimpleDoubleProperty();
        startX = new SimpleDoubleProperty();
        startY = new SimpleDoubleProperty();
        airplaneX.bindBidirectional(viewModel.airplaneX);
        airplaneY.bindBidirectional(viewModel.airplaneY);
        startX.bindBidirectional(viewModel.startX);
        startY.bindBidirectional(viewModel.startY);
        offset = new SimpleDoubleProperty();
        offset.bindBidirectional(viewModel.offset);
        viewModel.script.bindBidirectional(TextArea.textProperty());
        heading = new SimpleDoubleProperty();
        heading.bindBidirectional(viewModel.heading);
        markSceneX = new SimpleDoubleProperty();
        markSceneY = new SimpleDoubleProperty();
        markSceneY.bindBidirectional(viewModel.markSceneY);
        markSceneX.bindBidirectional(viewModel.markSceneX);
        path = new SimpleBooleanProperty();
        path.bindBidirectional(viewModel.path);
        path.setValue(false);
        plane = new Image[8];

        try {
            plane[0] = new Image(new FileInputStream("./resources/plane0.png"));
            plane[1] = new Image(new FileInputStream("./resources/plane45.png"));
            plane[2] = new Image(new FileInputStream("./resources/plane90.png"));
            plane[3] = new Image(new FileInputStream("./resources/plane135.png"));
            plane[4] = new Image(new FileInputStream("./resources/plane180.png"));
            plane[5] = new Image(new FileInputStream("./resources/plane225.png"));
            plane[6] = new Image(new FileInputStream("./resources/plane270.png"));
            plane[7] = new Image(new FileInputStream("./resources/plane315.png"));
            mark = new Image(new FileInputStream("./resources/mark.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (location.getPath().contains("Flight.fxml")) {
            throttle.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (manual.isSelected()) {
                    viewModel.setThrottle();
                }
            });

            rudder.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (manual.isSelected()) {
                    viewModel.setRudder();
                }
            });

            Joystick.setOnMousePressed(joystickClick);
            Joystick.setOnMouseDragged(joystickMove);
            Joystick.setOnMouseReleased(joystickRelease);
            markX.setOnMouseClicked(mapClick);
            identifier = -1;
        }
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (observable == viewModel) {
            if (arg == null)
                drawAirplane();
            else {
                solution = (String[]) arg;
                this.drawLine();
            }
        }
    }
}
