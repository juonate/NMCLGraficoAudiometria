/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.noemalife.chile.servlet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.image.RenderedImage;
import java.io.IOException;
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
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.SeriesRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
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
        String sVaod = "VAOD=[(150,45),(250,45),(500,45),(1000,10),(2000,25),(3000,20),(4000,),(6000,),(8000,)]";
        String sVaoi = "VAOI=[(150,20),(250,25),(500,30),(1000,10),(2000,40),(3000,20),(4000,15),(6000,25),(8000,30)]";
        String sVood = "VOOD=[(150,35),(250,40),(500,45),(1000,35),(2000,25),(3000,20),(4000,15),(6000,10),(8000,15)]";
        String sVooi = "VOOI=[(150,40),(250,45),(500,20),(1000,35),(2000,65),(3000,30),(4000,5),(6000,10),(8000,15)]";
        
        XYSeries serieVaod = parseCoordinates(sVaod, "VAOD");
        XYSeries serieVaoi = parseCoordinates(sVaoi, "VAOI");
        XYSeries serieVood = parseCoordinates(sVood, "VOOD");
        XYSeries serieVooi = parseCoordinates(sVooi, "VOOI");
        XYSeries serieOculta = new XYSeries("Serie Oculta");
        
        serieOculta.add(1000,10);
        serieOculta.add(2000,25);
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        
        
        dataset.addSeries(serieVaod);
        dataset.addSeries(serieVaoi);
        dataset.addSeries(serieVood);
        dataset.addSeries(serieVooi);
        dataset.addSeries(serieOculta);
        
        
        JFreeChart chart = ChartFactory.createXYLineChart("Audiometria", "Frecuencia", "Intensidad", dataset);

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setSeriesRenderingOrder(SeriesRenderingOrder.FORWARD );
        

        
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
//        renderer.setSeriesLinesVisible(0, true);
//        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesLinesVisible(1, true);
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesLinesVisible(2, true);
        renderer.setSeriesShapesVisible(2, true);
        renderer.setSeriesLinesVisible(3, true);
        renderer.setSeriesShapesVisible(3, true);
        renderer.setSeriesLinesVisible(4, true);
        renderer.setSeriesShapesVisible(4, true);
//        renderer.setSeriesPaint(0, Color.LIGHT_GRAY);
//        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesShape(0, ShapeUtilities.createDiamond(3));
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesShape(1, new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0));
        renderer.setSeriesPaint(1, Color.BLUE);
        renderer.setSeriesShape(2, ShapeUtilities.createDiamond(3));
        renderer.setSeriesPaint(2, Color.RED);
        renderer.setSeriesShape(3, new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0));
        renderer.setSeriesPaint(3, Color.BLUE);
        renderer.setSeriesPaint(4, Color.LIGHT_GRAY);
        renderer.setSeriesStroke(4, new BasicStroke(2.0f));
        
        plot.setRenderer(renderer);

//        renderer.setSeriesVisibleInLegend(0, false);

        //Configuración eje Y
        plot.getRangeAxis().setInverted(true);
        plot.getRangeAxis().setRange(-10, 110);
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickUnit(new NumberTickUnit(10));

        //Configuración eje X
        NumberAxis axis = (NumberAxis)plot.getDomainAxis();
        axis.setRange(0, 8000);

        


        RenderedImage imagenGrafico = chart.createBufferedImage(800, 300);
        ImageIO.write(imagenGrafico, "png", os);
        os.flush();
        os.close();
    }
    
    public XYSeries parseCoordinates(String input, String label) {
        XYSeries returnSerie = new XYSeries(label);
        Pattern pattern = Pattern.compile("(\\d+),(-?\\d+\\*?|\\d+\\*?)");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));
            returnSerie.add(x, y);
        }
        return returnSerie;
    }
    
    public XYSeries crearSerieOculta(String x, String Y){
        
        return null;
    }
}
