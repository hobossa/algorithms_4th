import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (null == picture) {
            throw new IllegalArgumentException();
        }
        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return picture;
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
        return 0.0;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return null;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return null;
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

    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }
}
