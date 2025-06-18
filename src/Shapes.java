public class Shapes {

    private final int[][] LShapeX = {{1, 0, 0, 0}, {0, 1, 2, 2}, {1, 1, 1, 0}, {0, 0, 1, 2}};
    private final int[][] LShapeY = {{2, 2, 1, 0}, {1, 1, 1, 0}, {2, 1, 0, 0}, {1, 0, 0, 0}};

    private final int[][] SShapeX = {{0, 1, 1, 2}, {1, 1, 0, 0}, {0, 1, 1, 2}, {1, 1, 0, 0}};
    private final int[][] SShapeY = {{1, 1, 0, 0}, {2, 1, 1, 0}, {1, 1, 0, 0}, {2, 1, 1, 0}};

    private final int[][] ZShapeX = {{2, 1, 1, 0}, {1, 1, 0, 0}, {2, 1, 1, 0}, {1, 1, 0, 0}};
    private final int[][] ZShapeY = {{1, 1, 0, 0}, {0, 1, 1, 2}, {1, 1, 0, 0}, {0, 1, 1, 2}};

    private final int[][] RevLShapeX = {{0, 1, 1, 1}, {2, 2, 1, 0}, {0, 0, 0, 1}, {2, 1, 0, 0}};
    private final int[][] RevLShapeY = {{2, 2, 1, 0}, {1, 0, 0, 0}, {2, 1, 0, 0}, {1, 1, 1, 0}};

    private final int[][] TShapeX = {{1, 2, 1, 0}, {0, 0, 0, 1}, {2, 1, 0, 1}, {1, 1, 1, 0}};
    private final int[][] TShapeY = {{1, 0, 0, 0}, {2, 1, 0, 1}, {1, 1, 1, 0}, {2, 1, 0, 1}};

    private final int[][] SquareShapeX = {{1, 0, 1, 0}, {1, 0, 1, 0}, {1, 0, 1, 0}, {1, 0, 1, 0}};
    private final int[][] SquareShapeY = {{1, 1, 0, 0}, {1, 1, 0, 0}, {1, 1, 0, 0}, {1, 1, 0, 0}};

    private final int[][] IShapeX = {{0, 0, 0, 0}, {0, 1, 2, 3}, {0, 0, 0, 0}, {0, 1, 2, 3}};
    private final int[][] IShapeY = {{3, 2, 1, 0}, {0, 0, 0, 0}, {3, 2, 1, 0}, {0, 0, 0, 0}};

    private final int[] shapePosition = {4, 0};

    private final String shape;
    private int angle;

    private boolean status;

    public Shapes(String shape, int angle) {
        this.shape = shape;
        this.angle = angle;
        this.status = false;
    }

    public int[] getPosition() {
        return shapePosition;
    }

    public void setStatus(boolean st) {
        this.status = st;
    }

    public boolean hasReached() {
        return this.status;
    }

    private int getMinX() {
        int[] tempX = getX();
        int minValue = tempX[0];
        for (int i = 1; i < tempX.length; i++) {
            if (tempX[i] < minValue) {
                minValue = tempX[i];
            }
        }
        return minValue;
    }

    private int getMaxX() {
        int[] tempX = getX();
        int maxValue = tempX[0];
        for (int i = 1; i < tempX.length; i++) {
            if (tempX[i] > maxValue) {
                maxValue = tempX[i];
            }
        }
        return maxValue;
    }

    private int getMaxY() {
        int[] tempY = getY();
        int maxValue = tempY[0];
        for (int i = 1; i < tempY.length; i++) {
            if (tempY[i] > maxValue) {
                maxValue = tempY[i];
            }
        }
        return maxValue;
    }

    public void dropElement(Board b) {
        int[] tempPosition = getPosition();

        int tempY = getMaxY();
        int bottom = tempY + tempPosition[1];

        boolean obs = false;
        boolean[][] squares = b.returnGrid();
        int[] X = getX();
        int[] Y = getY();
        for (int i = 0; i < X.length; i++) {
            int x = tempPosition[0] + X[i];
            int y = tempPosition[1] + Y[i];
            int temp = y + 1;
            if (temp < 20) {
                if (squares[temp][x]) {
                    obs = true;
                    break;
                }
            }
        }

        if (bottom < 19 && (!obs)) {
            shapePosition[1]++;
        } else {
            setStatus(true);
        }
    }

    public void moveRight(Board b) {
        int[] tempPosition = getPosition();

        int tempX = getMaxX();
        int right = tempX + tempPosition[0];

        boolean obs = false;
        boolean[][] squares = b.returnGrid();
        int[] X = getX();
        int[] Y = getY();
        for (int i = 0; i < X.length; i++) {
            int x = tempPosition[0] + X[i];
            int y = tempPosition[1] + Y[i];
            int temp = x + 1;
            if (temp < 10) {
                if (squares[y][temp]) {
                    obs = true;
                    break;
                }
            }
        }

        if ((!obs) && right < 9) {
            shapePosition[0]++;
        }
    }

    public void moveLeft(Board b) {
        int[] tempPosition = getPosition();

        int tempX = getMinX();
        int left = tempX + tempPosition[0];

        boolean obs = false;
        boolean[][] squares = b.returnGrid();
        int[] X = getX();
        int[] Y = getY();
        for (int i = 0; i < X.length; i++) {
            int x = tempPosition[0] + X[i];
            int y = tempPosition[1] + Y[i];
            int temp = x - 1;
            if (temp >= 0) {
                if (squares[y][temp]) {
                    obs = true;
                    break;
                }
            }
        }

        if ((!obs) && left > 0) {
            shapePosition[0]--;
        }
    }

    public int getAngle() {
        return this.angle;
    }

    private void rotationValidator(int a, Board b) {
        int[] p = getPosition();
        int[] x = getX();
        int[] y = getY();
        boolean[][] squares = b.returnGrid();
        boolean obs = false;
        for (int i = 0; i < 4; i++) {
            int tempX = p[0] + x[i];
            int tempY = p[1] + y[i];
            if (tempX > 9) {
                obs = true;
                break;
            }
            if (squares[tempY][tempX]) {
                obs = true;
                break;
            }
        }
        if (obs) {
            this.angle = a;
        }
    }

    public void rotate(Board b) {
        int tempY = getMaxY();
        int[] tempPosition = getPosition();
        int bottom = tempY + tempPosition[1];
        int a = getAngle();
        if (bottom < 19) {
            if (a == 0) {
                this.angle = 3;
                rotationValidator(a, b);
            } else {
                this.angle--;
                rotationValidator(a, b);
            }
        }
    }

    public int[] getX() {
        return switch (shape) {
            case "L" -> this.LShapeX[angle];
            case "RL" -> this.RevLShapeX[angle];
            case "S" -> this.SShapeX[angle];
            case "Z" -> this.ZShapeX[angle];
            case "T" -> this.TShapeX[angle];
            case "SQ" -> this.SquareShapeX[angle];
            case "I" -> this.IShapeX[angle];
            default -> null;
        };
    }

    public int[] getY() {
        return switch (shape) {
            case "L" -> this.LShapeY[angle];
            case "RL" -> this.RevLShapeY[angle];
            case "S" -> this.SShapeY[angle];
            case "Z" -> this.ZShapeY[angle];
            case "T" -> this.TShapeY[angle];
            case "SQ" -> this.SquareShapeY[angle];
            case "I" -> this.IShapeY[angle];
            default -> null;
        };
    }
}