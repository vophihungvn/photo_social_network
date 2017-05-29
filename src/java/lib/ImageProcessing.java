/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import static java.lang.Math.max;
import java.lang.reflect.Field;
import java.util.Vector;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;

import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;


/**
 *
 * @author Hung-PC
 */
public class ImageProcessing {
    
//    public static void main(String[] args) {
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        Blur("C:/Users/tuana/Desktop/lena.jpg", "");
//        GrayScale("C:/Users/tuana/Desktop/lena.jpg", "");
//        Brightness("C:/Users/tuana/Desktop/lena.jpg", "");
//        Sharpness("C:/Users/Hung-PC/Documents/NetBeansProjects/Chagram_v2/build/web/img/Image.png", "");
//
////        Contrast();
////        Rotate(180);
////        Zoom();
//
//    }
    private static Field loadedLibraryNames;
    public static void getLibrary() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
       boolean exist = false;
        loadedLibraryNames = ClassLoader.class.getDeclaredField("loadedLibraryNames");
        loadedLibraryNames.setAccessible(true);
        Vector<String> result = (Vector<String>) loadedLibraryNames.get(null);
        for (String temp : result) {
            System.out.println("Library: " + temp);
            int index = temp.lastIndexOf('\\');
            String libraryName = temp.substring(index + 1, temp.length());
            System.out.println("Name library: " + libraryName);
            if(libraryName.equals("opencv_java2410.dll")) {
                exist = true;
            }
        }
        
        System.out.println("Exist lib ========= "+ exist) ;
        
        if(!exist) {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        }
    }
    
    
    public static void Blur(String locationInput, String locationOutput) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        getLibrary();
        int index = locationInput.lastIndexOf('/');
        String locationTemp = locationInput.substring(0, index + 1);
        Mat source = Highgui.imread(locationInput);
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        Imgproc.GaussianBlur(source, destination, new Size(13, 13), 0);
        
        Highgui.imwrite(locationOutput, destination);
        destination.release();
        System.gc();
    }

    public static void GrayScale(String locationInput, String locationOutput) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        getLibrary();
        int index = locationInput.lastIndexOf('/');
        String locationTemp = locationInput.substring(0, index + 1);
        Mat source = Highgui.imread(locationInput);
        Mat destination = new Mat();
        Imgproc.cvtColor(source, destination, Imgproc.COLOR_RGB2GRAY);
        Imgproc.equalizeHist(destination, destination);
        
        Highgui.imwrite(locationOutput, destination);
    }

//    public static void Contrast(String locationInput, String locationOutput) {
//
//        int index = locationInput.lastIndexOf('/');
//        String locationTemp = locationInput.substring(0, index + 1);
//        Mat source = Highgui.imread(locationInput, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
//        Mat destination = new Mat(source.rows(), source.cols(), source.type());
//        Imgproc.equalizeHist(source, destination);
//        locationOutput = locationTemp + "Contrast.png";
//        Highgui.imwrite(locationOutput, destination);
//    }
    public static void Brightness(String locationInput, String locationOutput) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        getLibrary();
        int index = locationInput.lastIndexOf('/');
        String locationTemp = locationInput.substring(0, index + 1);
        double alpha = 2;
        double beta = 50;

        Mat source = Highgui.imread(locationInput, Highgui.CV_LOAD_IMAGE_COLOR);
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        source.convertTo(destination, -1, alpha, beta);
        
        Highgui.imwrite(locationOutput, destination);
    }

    public static void Sharpness(String locationInput, String locationOutput) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        System.out.println(locationInput);
        int index = locationInput.lastIndexOf('/');
        getLibrary();
        Mat source = Highgui.imread(locationInput, Highgui.CV_LOAD_IMAGE_COLOR);
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        Imgproc.GaussianBlur(source, destination, new Size(0, 0), 10);
        Core.addWeighted(source, 2.5, destination, -1.5, 0, destination);
        //locationOutput = locationTemp + "Sharpness.png";
        Highgui.imwrite(locationOutput, destination); 
    }

    public static void Rotate(double angle, String locationInput, String locationOutput) {

        int index = locationInput.lastIndexOf('/');
        String locationTemp = locationInput.substring(0, index + 1);
        Mat source = Highgui.imread(locationInput, Highgui.CV_LOAD_IMAGE_COLOR);
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
//        Core.flip(source, destination, 1); 
        int len = max(source.cols(), source.rows());
        Point pt = new Point(source.cols() / 2, source.rows() / 2);
        Mat M = Imgproc.getRotationMatrix2D(pt, angle, 1.0);
        Imgproc.warpAffine(source, destination, M, source.size());
        locationOutput = locationTemp + "Rotate.png";
        Highgui.imwrite(locationOutput, destination);
    }

    public static void Zoom(String locationInput, String locationOutput) { //not

        int index = locationInput.lastIndexOf('/');
        String locationTemp = locationInput.substring(0, index + 1);
        double zoomingFactor = 0.2;
        int zoomTemp = (int) (zoomingFactor * 2);

        Mat source = Highgui.imread(locationInput, Highgui.CV_LOAD_IMAGE_COLOR);
        Mat destination = new Mat(source.rows() * zoomTemp, source.cols() * zoomTemp, source.type());
        Imgproc.resize(source, destination, destination.size(), zoomingFactor, zoomingFactor, Imgproc.INTER_NEAREST);
        locationOutput = locationTemp + "Zoom.png";
        Highgui.imwrite(locationOutput, destination);
    }
}
