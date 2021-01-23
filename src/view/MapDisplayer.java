package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class MapDisplayer extends Canvas {
    int[][] mapData;
    double min = Double.MAX_VALUE;
    double max = 0;


    public void setMapData(int[][] mapData) {
        this.mapData = mapData;

        for (int[] mapDatum : mapData) {
            for (int i : mapDatum) {
                if (min > i) {
                    min = i;
                }
                if (max < i) {
                    max = i;
                }
            }
        }

        double new_max = 255;
        double new_min = 0;
        for (int i = 0; i < mapData.length; i++) {
            for (int j = 0; j < mapData[i].length; j++) {
                mapData[i][j] = (int) ((mapData[i][j] - min) / (max - min) * (new_max - new_min) + new_min);
            }
        }

        redraw();
    }

    public void redraw() {
        if (mapData != null) {
            double height = getHeight();
            double width = getWidth();
            double h = height / mapData.length;
            double w = width / mapData[0].length;
            GraphicsContext graphicsContext2D = getGraphicsContext2D();

            for (int i = 0; i < mapData.length; i++)
                for (int j = 0; j < mapData[i].length; j++) {
                    int tmp = mapData[i][j];
                    graphicsContext2D.setFill(Color.rgb(255 - tmp, tmp, 0));
                    graphicsContext2D.fillRect(j * w, i * h, w, h);
                }
        }
    }
}
