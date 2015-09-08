/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.noemalife.chile.servlet;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
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
 
        XYDataset dataset = createDataSet();
        JFreeChart chart = ChartFactory.createXYLineChart("Audiometria", "Frecuencia", "Intensidad", dataset);
        
        XYPlot plot = chart.getXYPlot();
        
//        plot.getRenderer().setSeriesStroke(0, new BasicStroke(2.0f));
        
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesLinesVisible(1, true);
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesLinesVisible(2, true);
        renderer.setSeriesShapesVisible(2, true); 
        renderer.setSeriesLinesVisible(3, true);
        renderer.setSeriesShapesVisible(3, true); 
        renderer.setSeriesShape(0, ShapeUtilities.createDiamond(3));
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesShape(1, new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0));
        renderer.setSeriesPaint(1, Color.BLUE);
        renderer.setSeriesShape(2, ShapeUtilities.createDiamond(3));
        renderer.setSeriesPaint(2, Color.RED);
        renderer.setSeriesShape(3, new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0));
        renderer.setSeriesPaint(3, Color.BLUE);
        plot.setRenderer(renderer);

//        renderer.setSeriesVisibleInLegend(0, false);
        
        //Configuración eje Y
        plot.getRangeAxis().setInverted(true);
        plot.getRangeAxis().setRange(-10,110);
        NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
        rangeAxis.setTickUnit(new NumberTickUnit(10));
        
        //Configuración eje X
        plot.getDomainAxis().setRange(0, 8000);
        NumberAxis domainAxis = new NumberAxis(plot.getDomainAxis().getLabel()){

            @Override
            public List refreshTicks(Graphics2D g2, AxisState state, Rectangle2D dataArea, RectangleEdge edge) {
                List  allTicks = super.refreshTicks(g2, state, dataArea, edge);
                List myTicks = new ArrayList();
                
                for(Object tick : allTicks){
                    NumberTick numberTick = (NumberTick) tick;
                    if(TickType.MAJOR.equals(numberTick.getTickType())&& (numberTick.getValue()% 50 !=0)){
                        myTicks.add(new NumberTick(TickType.MINOR, numberTick.getValue(), "", numberTick.getTextAnchor(),numberTick.getRotationAnchor(),numberTick.getAngle()));
                        continue;
                    }
                    myTicks.add(tick);
                }
                return myTicks;
            }
        };
        plot.setDomainAxis(domainAxis);
        

        RenderedImage imagenGrafico = chart.createBufferedImage(1000, 300);
        ImageIO.write(imagenGrafico, "png", os);
        os.flush();
        os.close();
    }

    public XYDataset createDataSet() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("VAOD");
        XYSeries series2 = new XYSeries("VAOI");
        XYSeries series3 = new XYSeries("VOOD");
        XYSeries series4 = new XYSeries("VOOI");

        series1.add(125, 15);
        series1.add(250, 20);
        series1.add(500, 30);
        series1.add(1000, 20);
        series1.add(2000, 40);

        series2.add(125, 20);
        series2.add(250, 45);
        series2.add(500, 30);
        series2.add(1000, 55);
        series2.add(2000, 35);

        series3.add(125, 15);
        series3.add(250, 20);
        series3.add(500, 15);
        series3.add(1000, 30);
        series3.add(2000, 35);
        
        series4.add(125, 25);
        series4.add(250, 30);
        series4.add(500, 35);
        series4.add(1000, 10);
        series4.add(2000, 10);

        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);
        dataset.addSeries(series4);

        return dataset;
    }
}
