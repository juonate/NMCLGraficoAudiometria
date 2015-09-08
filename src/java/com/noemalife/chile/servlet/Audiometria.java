/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.noemalife.chile.servlet;

import java.awt.image.RenderedImage;
import java.io.IOException;
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
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

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

         /*XYPlot xyPlot = chart.getXYPlot();
                NumberAxis domainAxis = (NumberAxis) xyPlot.getDomainAxis();            
                domainAxis.setRange(125, 200);
                domainAxis.setTickUnit(new NumberTickUnit(100));*/


        RenderedImage imagenGrafico = chart.createBufferedImage(500, 250);
        ImageIO.write(imagenGrafico, "png", os);
        os.flush();
        os.close();
    }

    public XYDataset createDataSet() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("Serie 1");
        XYSeries series2 = new XYSeries("Serie 2");
        XYSeries series3 = new XYSeries("Serie 3");

        series1.add(125, 15);
        series1.add(250, 20);
        series1.add(500, 30);
        series1.add(1000, 20);
        series1.add(2000, 40);

        series2.add(125, 35);
        series2.add(250, 45);
        series2.add(500, 50);
        series2.add(1000, 55);
        series2.add(2000, 35);

        series2.add(125, 15);
        series2.add(250, 20);
        series2.add(500, 15);
        series2.add(1000, 30);
        series2.add(2000, 35);

        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);

        return dataset;
    }
}
