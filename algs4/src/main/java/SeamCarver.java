import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    private double[][] energyMatrix;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (null == picture) {
            throw new IllegalArgumentException();
        }
        this.picture = new Picture(picture);
        energyMatrix = new double[width()][height()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                energyMatrix[i][j] = energy(i, j);
            }
        }
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > width() - 1
                || y < 0 || y > height() - 1) {
            throw new IllegalArgumentException();
        }

        if (x == 0 || x == width() - 1
                || y == 0 || y == height() - 1) {
            return 1000.0;
        }

//        Color up = picture.get(x, y - 1);
//        Color down = picture.get(x, y + 1);
//        Color left = picture.get(x - 1, y);
//        Color right = picture.get(x + 1, y);
//        double dVertical = Math.pow(up.getRed() - down.getRed(), 2)
//                + Math.pow(up.getGreen() - down.getGreen(), 2)
//                + Math.pow(up.getBlue() - down.getBlue(), 2);
//        double dHorizontal = Math.pow(left.getRed() - right.getRed(), 2)
//                + Math.pow(left.getGreen() - right.getGreen(), 2)
//                + Math.pow(left.getBlue() - right.getBlue(), 2);
//        return Math.sqrt(dVertical + dHorizontal);
        int up = picture.getRGB(x, y - 1);
        int down = picture.getRGB(x, y + 1);
        int left = picture.getRGB(x - 1, y);
        int right = picture.getRGB(x + 1, y);
//        int r = (rgb >> 16) & 0xFF;
//        int g = (rgb >>  8) & 0xFF;
//        int b = (rgb >>  0) & 0xFF;
        double dVertical = Math.pow((up >> 16) & 0xFF - (down >> 16) & 0xFF, 2)
                + Math.pow((up >> 8) & 0xFF - (down >> 8) & 0xFF, 2)
                + Math.pow((up) & 0xFF - (down) & 0xFF, 2);
        double dHorizontal = Math.pow((left >> 16) & 0xFF - (right >> 16) & 0xFF, 2)
                + Math.pow((left >> 8) & 0xFF - (right >> 8) & 0xFF, 2)
                + Math.pow((left) & 0xFF - (right) & 0xFF, 2);
        return Math.sqrt(dVertical + dHorizontal);

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
            if (totalEng[width()-1][j] < minEnergy) {
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

        // update picture
        // update energyMatrix
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

        // update picture
        // update energyMatrix
    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }
}
