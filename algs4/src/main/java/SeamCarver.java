import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

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
        if (x < 0 || x > width() - 1
                || y < 0 || y > height() - 1) {
            throw new IllegalArgumentException();
        }
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
            for (int j = 0; j < height(); j++) {
                int min = j;
                if (j - 1 >= 0 && totalEng[i - 1][j - 1] < totalEng[i - 1][min]) {
                    min = j - 1;
                }
                if (j + 1 < height() && totalEng[i - 1][j + 1] < totalEng[i - 1][min]) {
                    min = j + 1;
                }
                totalEng[i][j] = totalEng[i - 1][min] + energy(i, j);
                pre[i][j] = min;
            }
        }

        double minEnergy = Double.POSITIVE_INFINITY;
        int last = -1;
        for (int j = 0; j < height(); j++) {
            if (totalEng[width() - 1][j] < minEnergy) {
                last = j;
                minEnergy = totalEng[width() - 1][j];
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
            for (int i = 0; i < width(); i++) {
                int min = i;
                if (i - 1 >= 0 && totalEng[i - 1][j - 1] < totalEng[min][j - 1]) {
                    min = i - 1;
                }
                if (i + 1 < width() && totalEng[i + 1][j - 1] < totalEng[min][j - 1]) {
                    min = i + 1;
                }
                totalEng[i][j] = totalEng[min][j - 1] + energy(i, j);
                pre[i][j] = min;
            }
        }

        double minEnergy = Double.POSITIVE_INFINITY;
        int last = -1;
        for (int i = 0; i < width(); i++) {
            if (totalEng[i][height() - 1] < minEnergy) {
                last = i;
                minEnergy = totalEng[i][height() - 1];
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
        if (null == seam || height() < 2 || seam.length != width()) {
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

        // update rgbMatrix, energyMatrix, and height
        int len = seam.length;
        int[] posToRemove = new int[len];
        for (int i = 0; i < len; i++) {
            // (i, seam[i])
            posToRemove[i] = i + seam[i] * width();
        }
        Arrays.sort(posToRemove);

        // update rgbMatrix
        horizontalRemove(rgbMatrix, posToRemove);
        // update energyMatrix
        horizontalRemove(energyMatrix, posToRemove);

        // recalculate energy of some items.
        for (int i = 0; i < len; i++) {
            // reCalculate energy of (i, seam[i]-1) and (i, seam[i])
            if (seam[i] - 1 >= 0) {
                setEnergy(i, seam[i] - 1, calculateEnergy(i, seam[i] - 1));
            }
            setEnergy(i, seam[i], calculateEnergy(i, seam[i]));
        }

        height--;
    }

    private void horizontalRemove(int[] srcArray, int[] posToRemove) {
        // posToRemove must be a from small to large sorted array
        // int minLine = posToRemove[0] / width();
        int maxLine = posToRemove[posToRemove.length - 1] / width();
        for (int pos : posToRemove) {
            int nMove = pos + width();
            while (nMove < (maxLine + 1) * width()) {
                srcArray[nMove - width()] = srcArray[nMove];
                nMove += width();
            }
        }
        // move the rest part.
        System.arraycopy(srcArray, (maxLine + 1) * width(),
                srcArray, maxLine * width(),
                width() * height() - (maxLine + 1) * width());
    }

    private void horizontalRemove(double[] srcArray, int[] posToRemove) {
        // posToRemove must be a from small to large sorted array
        // int minLine = posToRemove[0] / width();
        int maxLine = posToRemove[posToRemove.length - 1] / width();
        for (int pos : posToRemove) {
            int nMove = pos + width();
            while (nMove < (maxLine + 1) * width()) {
                srcArray[nMove - width()] = srcArray[nMove];
                nMove += width();
            }
        }
        // move the rest part.
        System.arraycopy(srcArray, (maxLine + 1) * width(),
                srcArray, maxLine * width(),
                width() * height() - (maxLine + 1) * width());
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (null == seam || width() < 2 || seam.length != height()) {
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

        // update rgbMatrix, energyMatrix, and width
        int len = seam.length;
        int[] posToRemove = new int[len];
        for (int i = 0; i < len; i++) {
            // (seam[i], i)
            posToRemove[i] = +seam[i] + i * width();
        }
        // Arrays.sort(posToRemove); // for vertical seam, posToRemove is already sorted

        // update rgbMatrix
        verticalRemove(rgbMatrix, posToRemove);
        // update energyMatrix
        verticalRemove(energyMatrix, posToRemove);
        // recalculate energy of some items.
        for (int i = 0; i < len; i++) {
            // reCalculate energy of (seam[i]-1, i) and (seam[i], i)
            if (seam[i] - 1 >= 0) {
                setEnergy(seam[i] - 1, i, calculateEnergy(seam[i] - 1, i));
            }
            setEnergy(seam[i], i, calculateEnergy(seam[i], i));
        }

        width--;
    }

    private void verticalRemove(int[] srcArray, int[] posToRemove) {
        // posToRemove must be a from small to large sorted array
        int count = posToRemove.length;
        for (int i = 0; i < count; i++) {
            int lenToMove;
            if (i == count - 1) {
                lenToMove = width() * height() - posToRemove[i] - 1;
            } else {
                lenToMove = posToRemove[i + 1] - posToRemove[i];
            }

            System.arraycopy(srcArray, posToRemove[i] + 1,
                    srcArray, posToRemove[i] - i,
                    lenToMove);
        }
    }

    private void verticalRemove(double[] srcArray, int[] posToRemove) {
        // posToRemove must be a from small to large sorted array
        int count = posToRemove.length;
        for (int i = 0; i < count; i++) {
            int lenToMove;
            if (i == count - 1) {
                lenToMove = width() * height() - posToRemove[i] - 1;
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
        return (rgb >> 8) & 0xFF;
    }

    private int getBlue(int rgb) {
        return rgb & 0xFF;  // (rgb >>  0) & 0xFF;
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        Picture picture = new Picture("6x5.png");
        StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());
        StdOut.println(picture.toString());

        SeamCarver sc = new SeamCarver(picture);
        int[] seam = {1, 2, 1, 2, 1, 0};
        sc.removeHorizontalSeam(seam);
        StdOut.println(sc.picture().toString());

//        StdOut.printf("Printing energy calculated for each pixel.\n");
//
//        for (int row = 0; row < sc.height(); row++) {
//            for (int col = 0; col < sc.width(); col++)
//                StdOut.printf("%9.0f ", sc.energy(col, row));
//            StdOut.println();
//        }

//        StdOut.println();
//        int[] seam = sc.findHorizontalSeam();
//        //int[] seam = {0};
//        for (int n : seam) {
//            StdOut.printf("%9d ", n);
//        }
//        StdOut.println();

//        int[] aA = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
//        int[] tM = {0, 5, 10, 14};
//        removeFromArray(aA, tM);
//        for (int n : aA) {
//            StdOut.printf("%9d ", n);
//        }
//        StdOut.println();
//        sc.removeVerticalSeam(seam);
//        sc.picture().save("7x1.png");
//        StdOut.println();
    }
}
