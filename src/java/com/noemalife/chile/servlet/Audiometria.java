/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.noemalife.chile.servlet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTick;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickType;
import org.jfree.chart.plot.SeriesRenderingOrder;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.ShapeUtilities;

/**
 *
 * @author Juan
 */
public class Audiometria extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("image/png");
        ServletOutputStream os = resp.getOutputStream();

        String sVaod = req.getParameter("VAOD");
        String sVaoi = req.getParameter("VAOI");
        String sVood = req.getParameter("VOOD");
        String sVooi = req.getParameter("VOOI");


//        String sVaod = "VAOD=[(150,45),(250,45),(500,45),(1000,-10),(2000,25),(3000,20),(4000,),(6000,30),(8000,)]";
//        String sVaoi = "VAOI=[(150,20),(250,25),(500,30),(1000,10),(2000,40),(3000,20),(4000,15),(6000,25),(8000,30)]";
//        String sVood = "VOOD=[(150,35),(250,40),(500,45),(1000,35),(2000,25),(3000,20),(4000,15),(6000,10),(8000,15)]";
//        String sVooi = "VOOI=[(150,40),(250,45),(500,20),(1000,35),(2000,65),(3000,30),(4000,5),(6000,10),(8000,15)]";

        XYSeries serieVaod = parseCoordinates(sVaod, "VAOD");
        ArrayList<Unidad> aVaod = crearUnidad(sVaod, "VAOD");
        XYSeries ocultaTest = crearSerieOculta(aVaod);

        XYSeries serieVaoi = parseCoordinates(sVaoi, "VAOI");
        ArrayList<Unidad> aVaoi = crearUnidad(sVaoi, "VAOI");
        XYSeries ocultaTest2 = crearSerieOculta(aVaoi);

        XYSeries serieVood = parseCoordinates(sVood, "VOOD");
        ArrayList<Unidad> aVood = crearUnidad(sVood, "VOOD");
        XYSeries ocultaTest3 = crearSerieOculta(aVood);

        XYSeries serieVooi = parseCoordinates(sVooi, "VOOI");
        ArrayList<Unidad> aVooi = crearUnidad(sVooi, "VOOI");
        XYSeries ocultaTest4 = crearSerieOculta(aVooi);


        XYSeriesCollection dataset = new XYSeriesCollection();

        dataset.addSeries(serieVaod);
        dataset.addSeries(serieVaoi);
        dataset.addSeries(serieVood);
        dataset.addSeries(serieVooi);
        dataset.addSeries(ocultaTest);
        dataset.addSeries(ocultaTest2);
        dataset.addSeries(ocultaTest3);
        dataset.addSeries(ocultaTest4);

        JFreeChart chart = ChartFactory.createXYLineChart("Audiograma", "Frecuencia (Hz)", "Intensidad (dB(HL))", dataset);

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setSeriesRenderingOrder(SeriesRenderingOrder.FORWARD);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        System.out.println("Cantidad de series: " + dataset.getSeriesCount());
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            renderer.setSeriesLinesVisible(i, true);
            renderer.setSeriesShapesVisible(i, true);
            if (i == 0) {
                renderer.setSeriesShape(0, new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0));
                renderer.setSeriesPaint(0, Color.RED);
            } else if (i == 1) {
                renderer.setSeriesShape(1, ShapeUtilities.createDiagonalCross(3, 0.5f));
                renderer.setSeriesPaint(1, Color.BLUE);
            } else if (i == 2) {
                renderer.setSeriesStroke(
                        2, new BasicStroke(
                        1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{10.0f, 6.0f}, 0.0f));
//                renderer.setSeriesShape(2, ShapeUtilities.createDiamond(3));
                renderer.setSeriesShape(2, generateShapeFromText(new Font("Garamond", Font.BOLD, 11), "<"));
                renderer.setSeriesPaint(2, Color.RED);
            } else if (i == 3) {
                renderer.setSeriesStroke(
                        3, new BasicStroke(
                        1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{10.0f, 6.0f}, 0.0f));
                renderer.setSeriesShape(3, generateShapeFromText(new Font("Garamond", Font.BOLD, 11), ">"));
                renderer.setSeriesPaint(3, Color.BLUE);
            } else {
                renderer.setSeriesPaint(i, Color.LIGHT_GRAY);
                renderer.setSeriesStroke(i, new BasicStroke(2.0f));
                renderer.setSeriesVisibleInLegend(i, false);
            }
        }
        plot.setRenderer(renderer);

        //Configuración eje Y
        ValueMarker markerY = new ValueMarker(25);
        markerY.setPaint(Color.BLACK);
        markerY.setStroke(new BasicStroke(
                        1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{6.0f, 6.0f}, 0.0f));
        plot.addRangeMarker(markerY);
        plot.getRangeAxis().setInverted(true);
        plot.getRangeAxis().setRange(-10, 120);
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickLabelFont(new Font("Times", Font.PLAIN, 9));
        rangeAxis.setTickUnit(new NumberTickUnit(10));

        //Configuración eje X
//        NumberAxis axis = (NumberAxis) plot.getDomainAxis();
//        axis.setRange(0, 8000);
//        axis.setStandardTickUnits(tickUnits);
//        axis.setTickUnit(new NumberTickUnit(1000));
//        axis.setAutoTickUnitSelection(false);
//        TickUnits units = new TickUnits();
//        units.add(new NumberTickUnit(100));
//        axis.setStandardTickUnits(units);
//        axis.setVerticalTickLabels(true);
        
        ValueMarker markerX = new ValueMarker(403);
        markerX.setPaint(Color.BLACK);
        markerX.setStroke(new BasicStroke(2));
        plot.addDomainMarker(markerX);
        
        NumberAxis myAxis = new NumberAxis(plot.getDomainAxis().getLabel()) {

            @Override
            public List refreshTicks(Graphics2D g2, AxisState state,
                    Rectangle2D dataArea, RectangleEdge edge) {

                List allTicks = super.refreshTicks(g2, state, dataArea, edge);
                List myTicks = new ArrayList();

                for (Object tick : allTicks) {
                    NumberTick numberTick = (NumberTick) tick;
                    System.out.println(numberTick.getNumber()+" <==> "+numberTick.getTickType());
                    if (TickType.MAJOR.equals(numberTick.getTickType())
                            && (numberTick.getValue() != 100 && numberTick.getValue() != 200 &&
                            numberTick.getValue() != 300 && numberTick.getValue() != 400 &&
                            numberTick.getValue() != 500 && numberTick.getValue() != 600 &&
                            numberTick.getValue() != 700 && numberTick.getValue() != 800 &&
                            numberTick.getValue() != 900)) {
                        myTicks.add(new NumberTick(TickType.MINOR, numberTick.getValue(), "",
                                numberTick.getTextAnchor(), numberTick.getRotationAnchor(),
                                numberTick.getAngle()));
                        continue;
                    }
                    if(numberTick.getValue() == 100){
                        System.out.println(tick.toString());
                        myTicks.add(new NumberTick(TickType.MAJOR, numberTick.getValue(), "125",
                                numberTick.getTextAnchor(), numberTick.getRotationAnchor(),
                                numberTick.getAngle()));
                    } if(numberTick.getValue() == 200){
                        System.out.println(tick.toString());
                        myTicks.add(new NumberTick(TickType.MAJOR, numberTick.getValue(), "250",
                                numberTick.getTextAnchor(), numberTick.getRotationAnchor(),
                                numberTick.getAngle()));
                    }  if(numberTick.getValue() == 300){
                        System.out.println(tick.toString());
                        myTicks.add(new NumberTick(TickType.MAJOR, numberTick.getValue(), "500",
                                numberTick.getTextAnchor(), numberTick.getRotationAnchor(),
                                numberTick.getAngle()));
                    } if(numberTick.getValue() == 400){
                        System.out.println(tick.toString());
                        myTicks.add(new NumberTick(TickType.MAJOR, numberTick.getValue(), "1000",
                                numberTick.getTextAnchor(), numberTick.getRotationAnchor(),
                                numberTick.getAngle()));
                    } if(numberTick.getValue() == 500){
                        System.out.println(tick.toString());
                        myTicks.add(new NumberTick(TickType.MAJOR, numberTick.getValue(), "2000",
                                numberTick.getTextAnchor(), numberTick.getRotationAnchor(),
                                numberTick.getAngle()));
                    } if(numberTick.getValue() == 600){
                        System.out.println(tick.toString());
                        myTicks.add(new NumberTick(TickType.MAJOR, numberTick.getValue(), "3000",
                                numberTick.getTextAnchor(), numberTick.getRotationAnchor(),
                                numberTick.getAngle()));
                    } if(numberTick.getValue() == 700){
                        System.out.println(tick.toString());
                        myTicks.add(new NumberTick(TickType.MAJOR, numberTick.getValue(), "4000",
                                numberTick.getTextAnchor(), numberTick.getRotationAnchor(),
                                numberTick.getAngle()));
                    } if(numberTick.getValue() == 800){
                        System.out.println(tick.toString());
                        myTicks.add(new NumberTick(TickType.MAJOR, numberTick.getValue(), "6000",
                                numberTick.getTextAnchor(), numberTick.getRotationAnchor(),
                                numberTick.getAngle()));
                    } if(numberTick.getValue() == 900){
                        System.out.println(tick.toString());
                        myTicks.add(new NumberTick(TickType.MAJOR, numberTick.getValue(), "8000",
                                numberTick.getTextAnchor(), numberTick.getRotationAnchor(),
                                numberTick.getAngle()));
                    } 
                    //myTicks.add(tick);
                }
                return myTicks;
            }
        };
//        myAxis.setTickUnit(new NumberTickUnit(500));
        myAxis.setTickLabelFont(new Font("Times", Font.PLAIN, 9));
        myAxis.setVerticalTickLabels(true);
        plot.setDomainAxis(myAxis);
        myAxis.configure();
        
        RenderedImage imagenGrafico = chart.createBufferedImage(500, 400);
        ImageIO.write(imagenGrafico, "png", os);
        os.flush();
        os.close();
    }

    public static Shape generateShapeFromText(Font font, String string) {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();

        try {
            GlyphVector vect = font.createGlyphVector(g2.getFontRenderContext(), string);
            Shape shape = vect.getOutline(0f, (float) -vect.getVisualBounds().getY());

            return shape;
        } finally {
            g2.dispose();
        }
    }

    public XYSeries parseCoordinates(String input, String label) {
        XYSeries returnSerie = new XYSeries(label);

        Pattern pattern = Pattern.compile("(\\d+),(-?\\d+\\*?|\\d+\\*?)");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2).toString().replace("*", ""));
            returnSerie.add(x, y);
        }
        return returnSerie;
    }

    public XYSeries crearSerieOculta(ArrayList<Unidad> serie) {
        XYSeries returnSerie = new XYSeries(UUID.randomUUID().toString());
        for (int i = 0; i < serie.size(); i++) {
            if (serie.get(i).getIntensidad().toString().matches("(-?\\d+\\*|\\d+\\*)")) {
                try {
                    System.out.println("Intensidad con ausencia de ruido: " + serie.get(i).getFrecuencia() + "," + serie.get(i).getIntensidad());
                    System.out.println("Intensidad siguiente con ausencia de ruido: " + serie.get(i + 1).getFrecuencia() + "," + serie.get(i + 1).getIntensidad());
                    returnSerie.add(Integer.parseInt(serie.get(i).getFrecuencia().toString()), Integer.parseInt(serie.get(i).getIntensidad().toString().replace("*", "")));
                    returnSerie.add(Integer.parseInt(serie.get(i + 1).getFrecuencia().toString()), Integer.parseInt(serie.get(i + 1).getIntensidad().toString().replace("*", "")));
                } catch (IndexOutOfBoundsException ex) {
                    System.out.println("Fuera de rango: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }

        return returnSerie;
    }

    public ArrayList<Unidad> crearUnidad(String input, String label) {
        ArrayList<Unidad> array = new ArrayList<Unidad>();

        Pattern pattern = Pattern.compile("(\\d+),(-?\\d+\\*?|\\d+\\*?)");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            Unidad unidad = new Unidad();
            unidad.setFrecuencia(matcher.group(1));
            unidad.setIntensidad(matcher.group(2));
            array.add(unidad);
        }
        return array;
    }
}
