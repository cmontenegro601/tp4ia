
/**
 * @file HoughLines.java
 * @brief This program demonstrates line finding with the Hough transform
 */
import org.opencv.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

class TransformadaHLineas {
    //Variables de la clase

    String archivo = "c:/Users/Public/Img/l3.jpg";
    Mat dst = new Mat();
    Mat cdst = new Mat();
    Mat cdstP;
    Mat src;

    Mat lineas;
    Mat lineasP;

    public void run(String[] args) {

        cargarArchivo(args);

        identificarRectas();

        mostrarResultados();

    }

    public void cargarArchivo(String[] args) {
        //cargar el archivo

        String filename = ((args.length > 0) ? args[0] : archivo);
        System.out.println(filename);

        src = Imgcodecs.imread(filename, Imgcodecs.IMREAD_GRAYSCALE);

        if (src.empty()) {
            System.out.println("No se pudo cargar la imagen ");
            System.out.println("Error de ruta  "
                    + archivo + "] \n");
            System.exit(-1);
        }

        Imgproc.Canny(src, dst, 50, 200, 3, false);

        Imgproc.cvtColor(dst, cdst, Imgproc.COLOR_GRAY2BGR);
        cdstP = cdst.clone();
    }

    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        new TransformadaHLineas().run(args);
    }

    private void mostrarResultados() {
        HighGui.imshow("Imagen Original para analizar", src);

        HighGui.imshow("Lineas Identifacdas Transformada Hough Basico ", cdst);

        HighGui.imshow("Lineas Identifacdas Transformada Hough Avanzada", cdstP);

        HighGui.waitKey();
        System.exit(0);
    }

    private void identificarRectas() {
        lineas = new Mat();
        Imgproc.HoughLines(dst, lineas, 1, Math.PI / 180, 150);

        for (int x = 0; x < lineas.rows(); x++) {
            double rho = lineas.get(x, 0)[0],
                    theta = lineas.get(x, 0)[1];

            double a = Math.cos(theta), b = Math.sin(theta);
            double x0 = a * rho, y0 = b * rho;
            Point pt1 = new Point(Math.round(x0 + 1000 * (-b)), Math.round(y0 + 1000 * (a)));
            Point pt2 = new Point(Math.round(x0 - 1000 * (-b)), Math.round(y0 - 1000 * (a)));
            Imgproc.line(cdst, pt1, pt2, new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }
        //Dibujo de las lineas
        lineasP = new Mat();
        Imgproc.HoughLinesP(dst, lineasP, 1, Math.PI / 180, 50, 50, 10);
        //Dibjo de lineas transformada avanzada
        for (int x = 0; x < lineasP.rows(); x++) {
            double[] l = lineasP.get(x, 0);
            Imgproc.line(cdstP, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }
    }
}
