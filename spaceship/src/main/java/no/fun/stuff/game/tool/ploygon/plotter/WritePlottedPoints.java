package no.fun.stuff.game.tool.ploygon.plotter;

import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WritePlottedPoints {
    private static String delimiter = "#";
    final String fileName = "save_#.txt";

    public void writePoints(final List<Vector2D> worldCoordinatePoints) {
        final File fileName = makeNumberedFileName(this.fileName);
        try(FileWriter outFile = new FileWriter(fileName)) {
            for(final Vector2D v : worldCoordinatePoints) {
                outFile.write("" + v.getX() + "#" + v.getY() +  "\n");
            }
            outFile.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public File makeNumberedFileName(String fileName) {
        int i = 1;
        String replace = fileName.replace("#", "" + i);
        File file = new File(replace);
        while(file.exists()) {
            replace = fileName.replace("#", "" + ++i);
            file = new File(replace);
        }
        return file;
    }

    public List<Vector2D> readPoints(String fileName) {
        List<Vector2D> ret = new ArrayList<>();
        try (BufferedReader read = new BufferedReader(new FileReader(fileName))) {
            String line = read.readLine();
            while(line !=null) {
                String[] split = line.split(delimiter);
                if(split.length == 2) {
                    float x = Float.parseFloat(split[0]);
                    float y = Float.parseFloat(split[1]);
                    ret.add(new Vector2D(x, y));
                }
                line = read.readLine();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }
}
