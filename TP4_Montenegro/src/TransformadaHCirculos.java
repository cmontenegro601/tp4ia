
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

class TransformadaHCirculos {

    String archCargar;
    String archivo;

    Mat src;

    Mat gray;
    Mat circulos;

    public void run(String[] args) {

        cargarImagen(args);

        colocarEscalaGrises();
       
        identificarYDibujarCirculos();
       
    }

    public void cargarImagen(String[] args) {
        archCargar = "c:/Users/Public/Img/c2.png";
        archivo = ((args.length > 0) ? args[0] : archCargar);
        src = Imgcodecs.imread(archivo, Imgcodecs.IMREAD_COLOR);

        if (src.empty()) {
            System.out.println("No se encuentra la imagen");
            System.out.println("Error de imagen "
                    + archCargar + "] \n");
            System.exit(-1);
        }

    }

    public static void main(String[] args) {
        
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new TransformadaHCirculos().run(args);
    }

    private void colocarEscalaGrises() {
         gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.medianBlur(gray, gray, 5);
    }

    private void identificarYDibujarCirculos() {
        circulos = new Mat();
        
        Imgproc.HoughCircles(gray, circulos, Imgproc.HOUGH_GRADIENT, 1.0,
                (double) gray.rows() / 16,
                100.0, 30.0, 1, 30);

        for (int x = 0; x < circulos.cols(); x++) {
            double[] c = circulos.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));

            Imgproc.circle(src, center, 1, new Scalar(0, 100, 100), 3, 8, 0);
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(src, center, radius, new Scalar(255, 0, 255), 3, 8, 0);
        }
        HighGui.imshow("Circulos ", src);
        HighGui.waitKey();
        System.exit(0);
    }
}
