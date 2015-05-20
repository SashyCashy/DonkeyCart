package com.sdsu.spatialProject;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

class Panel extends JPanel{
    private List<Shape> objList = new ArrayList<Shape>();                
    protected int conversionFactorY = 7, conversionFactorX = 10;
    Graphics viewPic = null;
    public void updateList(List<Shape> pList){
        this.objList = pList;
    }
    
    public void updateShapeList(Graphics g) {
        paintComponent(g);
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
    }
    
    protected void paintComponent(Graphics arg0) {
        super.paintComponent(arg0);
       
        viewPic = arg0;
        for(int i=0;i<objList.size();i++){
            Shape content = objList.get(i);
            
            if("rect".equalsIgnoreCase(content.m_strType)){
                for(int j=0;j<content.m_intCordinate.length;j+=4){
                    int x1 = (int)content.m_intCordinate[j]*conversionFactorX +20;
                    int y1 = (int)content.m_intCordinate[j+1]*conversionFactorY-100;
                    int x2 = (int)content.m_intCordinate[j+2]*conversionFactorX+20;
                    int y2 = (int)content.m_intCordinate[j+3]*conversionFactorY-100;
                    int minX = 0;
                    int minY = 0;
                    if(x1 > x2){
                        //minX = x1;
                    	minX = x2;
                        minY = y1;
                    }else{
                        //minX = x2;
                    	minX = x1;
                        minY = y2;
                    }
                    
                    int rectHeight = Math.abs(y2-y1);
                    int rectWidth = Math.abs(x2-x1);

                    arg0.drawRect(minX, minY, rectWidth, rectHeight);
                }
            }
            
            else if("arc1".equalsIgnoreCase(content.m_strType)){
            	float[] x = new float[(content.m_intCordinate.length/2)];
            	float[] y = new float[(content.m_intCordinate.length/2)];
                int xCount = 0;
                int yCount = 0;
            	
                for( int j=0;j<content.m_intCordinate.length;j+=2){                        
                    x[xCount++] =(content.m_intCordinate[j])*conversionFactorX+20;
                    y[yCount++] =content.m_intCordinate[j+1]*conversionFactorY-100;
                }
                
                for(int k=0; k<xCount-1;k+=2)
                {
                Path2D.Double path = new Path2D.Double();
                path.moveTo((int)x[k],(int)y[k]);
                path.curveTo((int)x[k], (int)y[k], (int)x[k+1], (int)y[k+1], (int)x[k+2], (int)y[k+2]);
                Graphics2D gTmp = (Graphics2D) arg0;
                gTmp.draw(path);
                }
                
            }
            else if("arc2".equalsIgnoreCase(content.m_strType)){
            	float[] x = new float[(content.m_intCordinate.length/2)];
            	float[] y = new float[(content.m_intCordinate.length/2)];
                int xCount = 0;
                int yCount = 0;
            	
                for( int j=0;j<content.m_intCordinate.length;j+=2){                        
                    x[xCount++] =(content.m_intCordinate[j])*conversionFactorX+20;
                    y[yCount++] =content.m_intCordinate[j+1]*conversionFactorY-100;
                }
                for(int k=0; k<xCount-2;k+=3)
                {
                Path2D.Double path = new Path2D.Double();
                path.moveTo((int)x[k], (int)y[k]);
                path.curveTo((int)x[k], (int)y[k], (int)x[k+1], (int)y[k+1], (int)x[k+2], (int)y[k+2]);
                Graphics2D gTmp = (Graphics2D) arg0;
                gTmp.draw(path);
                }
                
            }
            else if("circle".equalsIgnoreCase(content.m_strType)){
                
                float[] x = new float[(content.m_intCordinate.length/2)];
                float[] y = new float[(content.m_intCordinate.length/2)];
              int xCount = 0;
              int yCount = 0;
              for( int j=0;j<content.m_intCordinate.length;j+=2){   
            	 
                  x[xCount] = (content.m_intCordinate[j])*conversionFactorX+20;
                  y[yCount] = content.m_intCordinate[j+1]*conversionFactorY-100;
                  
                  xCount++;
                  yCount++;
              }
              
              float X = calculateH(x[0]+1, y[0], x[1], y[1], x[2], y[2]);
              float Y = (int) calculateK(x[0], y[0], x[1], y[1], x[2], y[2]);
                float radius =  (float)Math.sqrt((X-x[0])*(X-x[0]) + (Y-y[0])*(Y-y[0]));
                //System.out.println(X + " " + Y + " " + radius);
                float cornerX = X-radius;
                float cornerY = Y-radius;
                arg0.drawOval((int)cornerX,(int)cornerY,(int)radius*2,(int)radius*2);  // x,y,w,h
      }
            else if("line1".equalsIgnoreCase(content.m_strType)){                    
            	float[] xArray = new float[(content.m_intCordinate.length/2)];
            	float[] yArray = new float[(content.m_intCordinate.length/2)];
                int xCount = 0;
                int yCount = 0;
                for(int j=0;j<content.m_intCordinate.length;j+=2){                        
                    xArray[xCount++] = (content.m_intCordinate[j])*conversionFactorX+20;
                    yArray[yCount++] = content.m_intCordinate[j+1]*conversionFactorY-100;
                }
                for(int k=0; k<xCount; k+=2)
                {
                	arg0.drawLine((int)xArray[k], (int)yArray[k], (int)xArray[k+1], (int)yArray[k+1]);
                }
            }
            else if("line2".equalsIgnoreCase(content.m_strType)){                    
                float[] xArray = new float[(content.m_intCordinate.length/2)];
                float[] yArray = new float[(content.m_intCordinate.length/2)];
                int xCount = 0;
                int yCount = 0;
                for(int j=0;j<content.m_intCordinate.length;j+=2){                        
                    xArray[xCount++] = (content.m_intCordinate[j])*conversionFactorX+20;
                    yArray[yCount++] = content.m_intCordinate[j+1]*conversionFactorY-100;
                }
                for(int k=0; k<xCount-1; k+=1)
                {
                	arg0.drawLine((int)xArray[k], (int)yArray[k], (int)xArray[k+1], (int)yArray[k+1]);
                }
            }
        }
    }
    
    private float calculateH(float x1, float y1, float x2, float y2, float x3, float y3)
    {
        float numerator = (x2*x2+y2*y2)*y3 - (x3*x3+y3*y3)*y2 - ((x1*x1+y1*y1)*y3 - (x3*x3+y3*y3)*y1) + (x1*x1+y1*y1)*y2 - (x2*x2+y2*y2)*y1;
        float denominator = (x2*y3-x3*y2) - (x1*y3-x3*y1) + (x1*y2-x2*y1);
        denominator *= 2;
        return numerator / denominator;
    }
    private float calculateK(float x1, float y1, float x2, float y2, float x3, float y3)
    {
        float numerator = x2*(x3*x3+y3*y3) - x3*(x2*x2+y2*y2) - (x1*(x3*x3+y3*y3) - x3*(x1*x1+y1*y1)) + x1*(x2*x2+y2*y2) - x2*(x1*x1+y1*y1);
        float denominator = (x2*y3-x3*y2) - (x1*y3-x3*y1) + (x1*y2-x2*y1);
        denominator *= 2;
        return numerator / denominator;
    }
}