import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;

public class SeamCarver {

    private int width;
    private int height;
    // using a dimensional array to present a matrix so that we
    // can use System.arrayCopy easily to opt performance.
    private int[] rgbMatrix;
    private double[] energyMatrix;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (null == picture) {
            throw new IllegalArgumentException();
        }

        width = picture.width();
        height = picture.height();

        // init rgbMatrix
        rgbMatrix = new int[width() * height()];
        int count = 0;
        for (int j = 0; j < height(); j++) {
            for (int i = 0; i < width(); i++) {
                rgbMatrix[count++] = picture.getRGB(i, j);
            }
        }
//        for (int j = 0; j < height(); j++) {
//            for (int i = 0; i < width(); i++) {
//                int rgb = picture.getRGB(i, j);
//                assert (rgb == picture.getRGB(i, j));
//                Color col = picture.get(i, j);
//                int r = (rgb >> 16) & 0xFF;
//                int g = (rgb >> 8) & 0xFF;
//                int b = rgb & 0xFF; //(rgb >> 0) & 0xFF;
//                assert (r == col.getRed());
//                assert (g == col.getGreen());
//                assert (b == col.getBlue());
//            }
//        }
        // initial energyMatrix
        energyMatrix = new double[width() * height()];
        count = 0;
        for (int j = 0; j < height(); j++) {
            for (int i = 0; i < width(); i++) {
                energyMatrix[count++] = calculateEnergy(i, j);
            }
        }
    }

    // current picture
    public Picture picture() {
        Picture pic = new Picture(width(), height());
        int c = 0;
        for (int j = 0; j < height(); j++) {
            for (int i = 0; i < width(); i++) {
                pic.setRGB(i, j, rgbMatrix[c++]);
            }
        }
        return pic;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        int i = x + y * width();
        return energyMatrix[i];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[][] pre = new int[width()][height()];
        double[][] totalEng = new double[width()][height()];
        for (int i = 1; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                totalEng[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        // initial the first col.
        for (int j = 0; j < height(); j++) {
            totalEng[0][j] = energy(0, j);
        }

        for (int i = 1; i < width(); i++) {
            // we do not need to consider the first row and the last row,
            // because the energy of these item is pretty big（1000）
            // so the index is [1,width-2]
            for (int j = 1; j < height() - 1; j++) {
                int min = j - 1;
                if (totalEng[i - 1][j] < totalEng[i - 1][min]) {
                    min = j;
                }
                if (totalEng[i - 1][j + 1] < totalEng[i - 1][min]) {
                    min = j + 1;
                }
                totalEng[i][j] = totalEng[i - 1][min] + energy(i, j);
                pre[i][j] = min;
            }
        }

        double minEnergy = Double.POSITIVE_INFINITY;
        int last = -1;
        for (int j = 1; j < height() - 1; j++) {
            if (totalEng[width() - 1][j] < minEnergy) {
                last = j;
            }
        }

        int[] res = new int[width()];
        for (int i = width() - 1; i >= 0; i--) {
            res[i] = last;
            last = pre[i][last];
        }

        return res;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {

        int[][] pre = new int[width()][height()];
        double[][] totalEng = new double[width()][height()];
        for (int j = 1; j < height(); j++) {
            for (int i = 0; i < width(); i++) {
                totalEng[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        // initial the first row.
        for (int i = 0; i < width(); i++) {
            totalEng[i][0] = energy(i, 0);
        }

        for (int j = 1; j < height(); j++) {
            // we do not need to consider the first col and the last col,
            // because the energy of these item is pretty big（1000）
            // so the index is [1,width-2]
            for (int i = 1; i < width() - 1; i++) {
                int min = i - 1;
                if (totalEng[i][j - 1] < totalEng[min][j - 1]) {
                    min = i;
                }
                if (totalEng[i + 1][j - 1] < totalEng[min][j - 1]) {
                    min = i + 1;
                }
                totalEng[i][j] = totalEng[min][j - 1] + energy(i, j);
                pre[i][j] = min;
            }
        }

        double minEnergy = Double.POSITIVE_INFINITY;
        int last = -1;
        for (int i = 1; i < width() - 1; i++) {
            if (totalEng[i][height() - 1] < minEnergy) {
                last = i;
            }
        }

        int[] res = new int[height()];
        for (int j = height() - 1; j >= 0; j--) {
            res[j] = last;
            last = pre[last][j];
        }

        return res;
    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (null == seam || width() < 2 || seam.length != width()) {
            throw new IllegalArgumentException();
        }
        // check seam
        int pre = -1;
        for (int i : seam) {
            if (i < 0 || i > width() - 1) {
                throw new IllegalArgumentException();
            }
            if (pre != -1 && Math.abs(pre - i) > 1) {
                throw new IllegalArgumentException();
            }
            pre = i;
        }

        // update rgbMatrix, energyMatrix, and height
        int len = seam.length;
        int[] posToRemove = new int[len];
        for (int i = 0; i < len; i++) {
            // (i, seam[i])
            posToRemove[i] = i + seam[i] * width();
        }
        Arrays.sort(posToRemove);

        // update rgbMatrix
        removeFromArray(rgbMatrix, posToRemove);
        // update energyMatrix
        removeFromArray(energyMatrix, posToRemove);
        // recalculate energy of some items.
        for (int i = 0; i < len; i++) {
            // reCalculate energy of (i, seam[i]-1) and (i, seam[i])
            setEnergy(i, seam[i] - 1, calculateEnergy(i, seam[i] - 1));
            setEnergy(i, seam[i], calculateEnergy(i, seam[i]));
        }

        height--;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (null == seam || height() < 2 || seam.length != height()) {
            throw new IllegalArgumentException();
        }
        // check seam
        int pre = -1;
        for (int i : seam) {
            if (i < 0 || i > height() - 1) {
                throw new IllegalArgumentException();
            }
            if (pre != -1 && Math.abs(pre - i) > 1) {
                throw new IllegalArgumentException();
            }
            pre = i;
        }

        // update rgbMatrix, energyMatrix, and width
        int len = seam.length;
        int[] posToRemove = new int[len];
        for (int i = 0; i < len; i++) {
            // (seam[i], i)
            posToRemove[i] = +seam[i] + i * width();
        }
        // Arrays.sort(posToRemove); // for vertical seam, posToRemove is already sorted

        // update rgbMatrix
        removeFromArray(rgbMatrix, posToRemove);
        // update energyMatrix
        removeFromArray(energyMatrix, posToRemove);
        // recalculate energy of some items.
        for (int i = 0; i < len; i++) {
            // reCalculate energy of (seam[i]-1, i) and (seam[i], i)
            setEnergy(seam[i] - 1, i, calculateEnergy(seam[i] - 1, i));
            setEnergy(seam[i], i, calculateEnergy(seam[i], i));
        }

        width--;
    }

    private static <T> void removeFromArray(int[] srcArray, int[] posToRemove) {
        int count = posToRemove.length;
        int[] posArray = new int[count];
        System.arraycopy(posToRemove, 0, posArray, 0, count);
        Arrays.sort(posArray);
        for (int i = 0; i < count; i++) {
            int lenToMove;
            if (i == count - 1) {
                lenToMove = srcArray.length - posArray[i] - 1;
            } else {
                lenToMove = posArray[i + 1] - posArray[i];
            }

            System.arraycopy(srcArray, posArray[i] + 1,
                    srcArray, posArray[i] - i,
                    lenToMove);
        }
    }

    private static <T> void removeFromArray(double[] srcArray, int[] posToRemove) {
        // posToRemove must be a from small to large sorted array
        int count = posToRemove.length;
        for (int i = 0; i < count; i++) {
            int lenToMove;
            if (i == count - 1) {
                lenToMove = srcArray.length - posToRemove[i] - 1;
            } else {
                lenToMove = posToRemove[i + 1] - posToRemove[i];
            }

            System.arraycopy(srcArray, posToRemove[i] + 1,
                    srcArray, posToRemove[i] - i,
                    lenToMove);
        }
    }

    private int getRGB(int x, int y) {
        int i = x + y * width();
        return rgbMatrix[i];
    }

    private void setRGB(int x, int y, int rgb) {
        int i = x + y * width();
        rgbMatrix[i] = rgb;
    }

    private void setEnergy(int x, int y, double eng) {
        int i = x + y * width();
        energyMatrix[i] = eng;
    }

    private double calculateEnergy(int x, int y) {
        if (x < 0 || x > width() - 1
                || y < 0 || y > height() - 1) {
            throw new IllegalArgumentException();
        }

        if (x == 0 || x == width() - 1
                || y == 0 || y == height() - 1) {
            return 1000.0;
        }

        int up = getRGB(x, y - 1);
        int down = getRGB(x, y + 1);
        int left = getRGB(x - 1, y);
        int right = getRGB(x + 1, y);

        double dVertical = Math.pow(getRed(up) - getRed(down), 2)
                + Math.pow(getGreen(up) - getGreen(down), 2)
                + Math.pow(getBlue(up) - getBlue(down), 2);
        double dHorizontal = Math.pow(getRed(left) - getRed(right), 2)
                + Math.pow(getGreen(left) - getGreen(right), 2)
                + Math.pow(getBlue(left) - getBlue(right), 2);
        return Math.sqrt(dVertical + dHorizontal);
    }

//    int r = (rgb >> 16) & 0xFF;
//    int g = (rgb >>  8) & 0xFF;
//    int b = (rgb >>  0) & 0xFF; // rgb & 0xFF;
    private int getRed(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    private int getGreen(int rgb) {
        return (rgb >>  8) & 0xFF;
    }

    private int getBlue(int rgb) {
        return rgb & 0xFF;  // (rgb >>  0) & 0xFF;
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        Picture picture = new Picture("6x5.png");
        StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());

        SeamCarver sc = new SeamCarver(picture);

        StdOut.printf("Printing energy calculated for each pixel.\n");

        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%9.0f ", sc.energy(col, row));
            StdOut.println();
        }
    }
}
