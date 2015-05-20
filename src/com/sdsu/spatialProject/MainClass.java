package com.sdsu.spatialProject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import java.lang.Object;

import oracle.sql.*;

public class MainClass extends JFrame {

    protected static Connection connectionObject = ConnectionManager.getConnection();
    protected int conversionFactor = 5;
    protected ArrayList<Shape> shapeList = null;
    protected static Panel panel = null;
    
    protected void frameInit() {
        super.frameInit();        
        this.setTitle("Sashank Pindiproli Project");
       
        try{
            buildListNew();
            this.setBounds(0,0, 1350, 730);
            JMenuBar menubar = new JMenuBar();
            this.setJMenuBar(menubar);
            panel = new Panel();
            panel.updateList(shapeList);
            panel.setBackground(Color.CYAN);
            JPanel objRightPanel = new JPanel(new BorderLayout());
            objRightPanel.setPreferredSize(new Dimension(600,500));            
            objRightPanel.add(panel);
            
            JPanel objLeftPanel = new JPanel(new FlowLayout());
            objLeftPanel.setLayout(new BoxLayout(objLeftPanel, BoxLayout.Y_AXIS));
                        
            JSplitPane sheetSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            sheetSplit.setRightComponent(rightPartCreation(objLeftPanel));
            sheetSplit.setLeftComponent(objRightPanel);
            getContentPane().add(sheetSplit, BorderLayout.CENTER);            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void buildListNew(){
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        java.sql.Struct geometryObject = null;
        oracle.sql.ARRAY coordinates = null;
        
        try{
            shapeList = new ArrayList<Shape>();
            String strQuery = "SELECT * FROM donkeycart_818494008 order by shp_id";
            statement = connectionObject.prepareStatement(strQuery);
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                Shape objContent = new Shape();
                objContent.m_intId = resultSet.getInt(1);
                objContent.m_strDesc = "Rect";
                objContent.m_strType = resultSet.getString(2);
                
                geometryObject = (java.sql.Struct) resultSet.getObject(3);                
                coordinates = (oracle.sql.ARRAY) geometryObject.getAttributes()[4];
                float[] l_tempArray = coordinates.getFloatArray();
                objContent.m_intCordinate = new float[l_tempArray.length];
                
                for(int i=0;i<l_tempArray.length;i+=2){
                    objContent.m_intCordinate[i] = l_tempArray[i];
                        objContent.m_intCordinate[i+1] =100-l_tempArray[i+1];
                }
                if(objContent.m_strType == "rect1")
                {
                	objContent.m_strType = "rect";
                }
                
                shapeList.add(objContent);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{                
                if(resultSet != null){            
                    resultSet.close();
                }                
                if(statement != null){            
                    statement.close();
                }                
            } catch (SQLException e) {
                    e.printStackTrace();
            }
        }
    }
    private JPanel rightPartCreation(JPanel leftPanel){
        
        JButton objDisjoint = new JButton("Disjoint");
        leftPanel.setLayout(null);
        leftPanel.add(objDisjoint);
        objDisjoint.addActionListener(new DisjointListner());
        objDisjoint.setSize(100, 30);
        objDisjoint.setLocation(200, 50);
        
        JButton objInside = new JButton("Inside");
        leftPanel.add(objInside);
        objInside.addActionListener(new InsideListner());
        objInside.setSize(100, 30);
        objInside.setLocation(400, 50);
        
        JButton objTouch = new JButton("Touch");
        leftPanel.add(objTouch);
        objTouch.addActionListener(new TouchListner());
        objTouch.setSize(100, 30);
        objTouch.setLocation(200, 100);
        
        
        JButton objUnion = new JButton("Union");
        leftPanel.add(objUnion);
        objUnion.addActionListener(new UnionListner());
        objUnion.setSize(100, 30);
        objUnion.setLocation(400, 150);
        
        
        JButton objInsert = new JButton("Insert");
        leftPanel.add(objInsert);
        objInsert.addActionListener(new InsertListner());
        objInsert.setSize(100, 30);
        objInsert.setLocation(400, 100);
        
        
        JButton objDelete = new JButton("Delete");
        leftPanel.add(objDelete);
        objDelete.addActionListener(new DeleteListner());
        objDelete.setSize(100, 30);
        objDelete.setLocation(200, 150);
       
        JButton intersection = new JButton("Intersection");
        leftPanel.add(intersection);
        intersection.addActionListener(new IntersectionListner());
        intersection.setSize(120, 30);
        intersection.setLocation(200, 200);
        
        JButton equal = new JButton("Length");
        leftPanel.add(equal);
        equal.addActionListener(new LengthListner());
        equal.setSize(100, 30);
        equal.setLocation(400, 200);
        
        JButton distance = new JButton("Distance");
        leftPanel.add(distance);
        distance.addActionListener(new DistanceListner());
        distance.setSize(100, 30);
        distance.setLocation(200, 250);
        
        JButton area = new JButton("Area");
        leftPanel.add(area);
        area.addActionListener(new AreaListner());
        area.setSize(100, 30);
        area.setLocation(400, 250);
        
        JButton contains = new JButton("MBR");
        leftPanel.add(contains);
        contains.addActionListener(new MBRListener());
        contains.setSize(100, 30);
        contains.setLocation(200, 300);
        
        JButton covers = new JButton("Within Distance");
        leftPanel.add(covers);
        covers.addActionListener(new DistanceWithinListner());
        covers.setSize(150, 30);
        covers.setLocation(400, 300);
        
        return leftPanel;
    }
    
    public void drawPoly(Graphics graphicObject, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4){
        int[] x = new int[4];
        int[] y = new int[4];
        x[0] = x1;
        y[0] = y1;
        x[1] = x2;
        y[1] = y2;
        x[2] = x3;
        y[2] = y3;
        x[3] = x4;
        y[3] = y4;
        
        graphicObject.drawPolygon(x, y, x.length);
    }
    
    public static void main(String args[]){
        MainClass objHouse = new MainClass();
        CloseListener objAdaptor = objHouse.new CloseListener();
        try {
            objHouse.addWindowListener(objAdaptor);
            objHouse.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void finalize(){
        try{
            if(connectionObject != null){
                connectionObject.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public class CloseListener extends WindowAdapter{
        public void windowClosing(WindowEvent we) {
            System.exit(0);
        }
    }
    
    private class InsertListner implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            String strQuery = "INSERT INTO donkeycart_818494008 VALUES (32,'circle',MDSYS.SDO_GEOMETRY (2003,null,null,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,4),MDSYS.SDO_ORDINATE_ARRAY(36,37,37,39,36,39)))";

            PreparedStatement prepareObject = null;
           
            try{                
                
                prepareObject  = connectionObject.prepareStatement(strQuery);
                prepareObject.execute();
                buildListNew();
                panel.updateList(shapeList);
                panel.removeAll();
                panel.updateUI();                
                
            }catch(Exception ex){
                ex.printStackTrace();
            }
            
        }
        
    }
    
    private class DeleteListner implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            String strQuery = "DELETE FROM donkeycart_818494008 WHERE shp_id=32";
            PreparedStatement prepareObject = null;
            try{                
                
                prepareObject  = connectionObject.prepareStatement(strQuery);
                prepareObject.execute();
                buildListNew();
                panel.updateList(shapeList);
                panel.removeAll();
                panel.updateUI();                
                
            }catch(Exception ex){
                ex.printStackTrace();
            }
                        
        }
        
    }
    
    private class DisjointListner implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            String strSelectQuery = "SELECT SDO_GEOM.RELATE(pict1.shape, 'Disjoint', pict2.shape, 0.005)FROM donkeycart_818494008 pict1, donkeycart_818494008 pict2 WHERE  pict1.shp_id = 3 AND pict2.shp_id = 4";
            PreparedStatement prepareObject = null;
            ResultSet resultObject = null;
            try{
                JLabel objLabel = new JLabel();
                prepareObject  = connectionObject.prepareStatement(strSelectQuery);
                resultObject = prepareObject.executeQuery();
                if(resultObject.next()){
                    String strContains = resultObject.getString(1);
                    
                    if(strContains.equalsIgnoreCase("TRUE")){
                        objLabel.setText("Wheel and Cart are disjoint.");
                    }else{
                        objLabel.setText("Wheel and Cart are joint.");
                    }
                    panel.removeAll();
                    panel.add(objLabel);                    
                    panel.updateUI();
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
    }
    
    private class InsideListner implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            String strSelectQuery = "SELECT SDO_GEOM.RELATE(pict1.shape, 'Inside', pict2.shape, 0.005)FROM donkeycart_818494008 pict1, donkeycart_818494008 pict2 WHERE  pict1.shp_id = 7 AND pict2.shp_id = 6";
            PreparedStatement objPrep = null;
            ResultSet objRs = null;
            try{
                JLabel objLabel = new JLabel();
                objPrep  = connectionObject.prepareStatement(strSelectQuery);
                objRs = objPrep.executeQuery();
                if(objRs.next()){
                    String strContains = objRs.getString(1);
                    objLabel.setText("X is " + strContains + " the rectangle block.");
                    panel.removeAll();
                    panel.add(objLabel);                    
                    panel.updateUI();
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
    }
    
    private class UnionListner implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            String strSelectQuery = "Select SDO_GEOM.SDO_UNION(pict1.shape , pict2.shape, 0.005)FROM donkeycart_818494008 pict1,donkeycart_818494008 pict2 WHERE pict1.shp_id = 26 AND pict2.shp_id = 27";
            PreparedStatement objPrep = null;
            ResultSet objRs = null;
            try{
                Graphics g = getGraphics();
                objPrep  = connectionObject.prepareStatement(strSelectQuery);
                objRs = objPrep.executeQuery();
                while(objRs.next()){
                    java.sql.Struct o6 = (java.sql.Struct)objRs.getObject(1);
                    oracle.sql.ARRAY oa4_6 = (oracle.sql.ARRAY)o6.getAttributes()[4];
                    int oa4_6len = oa4_6.length();
                    int[] ia4_6 = oa4_6.getIntArray();
                    
                    int[] x_6 = new int[100];
                    int j6 = 0;
                    int k6 = 0;
                    int[] y_6 = new int[100];


                    for (int i6 = 0 ; i6<oa4_6len-2 ; i6+=2)
                    {
                      x_6[j6] = 10*ia4_6[i6]+29;
                      
                      j6++;
                    }
                    for (int i6= 1; i6<oa4_6len-2; i6+=2)
                    {
                      y_6[k6] = (50 - ia4_6[i6])*7+283;
                     
                      k6++;
                    }
                    g.setColor(Color.green);

                    g.fillPolygon(x_6, y_6, (oa4_6len - 2)/2);
                
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
    }
    
    private class IntersectionListner implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            String strSelectQuery = "SELECT SDO_GEOM.SDO_INTERSECTION(c_a.shape, c_c.shape, 0.005) FROM donkeycart_818494008 c_a, donkeycart_818494008 c_c WHERE c_a.shp_id = 31 and c_c.shp_id = 4";
            PreparedStatement objPrep = null;
            ResultSet resultObject = null;
            try{
                Graphics g = getGraphics();
                objPrep  = connectionObject.prepareStatement(strSelectQuery);
                resultObject = objPrep.executeQuery();
                while(resultObject.next()){
                    java.sql.Struct o6 = (java.sql.Struct)resultObject.getObject(1);
                    oracle.sql.ARRAY oa4_6 = (oracle.sql.ARRAY)o6.getAttributes()[4];
                    int oa4_6len = oa4_6.length();
                    int[] ia4_6 = oa4_6.getIntArray();
                    
                    int[] x_6 = new int[(oa4_6len - 1)/2];
                    int j6 = 0;
                    int k6 = 0;
                    int[] y_6 = new int[(oa4_6len - 1)/2];

                    for (int i6 = 0 ; i6<oa4_6len-2 ; i6+=2)
                    {
                      x_6[j6] = 10*ia4_6[i6]+25;
                      y_6[j6] = 7*(50 - ia4_6[i6+1])+275;
                      j6++;
                    }
                    
                    g.setColor(Color.black);

                   
                    g.fillOval(x_6[0], y_6[0],12,12);
                  
                   
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
    }
    
    private class ContainsListner implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            String query = "SELECT SDO_GEOM.RELATE(pict1.shape, 'Contains', pict2.shape, 0.005)FROM donkeycart_818494008 pict1, donkeycart_818494008 pict2 WHERE  pict1.shp_id = 6 AND pict2.shp_id = 20";
            PreparedStatement stmt = null;
            ResultSet resultSet = null;
            try{
                JLabel objLabel = new JLabel();
                stmt  = connectionObject.prepareStatement(query);
                resultSet = stmt.executeQuery();
                if(resultSet.next()){
                    String strContains = resultSet.getString(1);
                    objLabel.setText("Rectangle " + strContains + " M");
                    panel.removeAll();
                    panel.add(objLabel);                    
                    panel.updateUI();
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
    }
    
    private class MBRListener implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            String strQuery = "SELECT SDO_GEOM.SDO_MBR(pict1.shape) FROM donkeycart_818494008 pict1 WHERE pict1.shp_id=4";
            PreparedStatement objPrep = null;
            ResultSet objRs = null;
            try{
            	
                //System.out.println("strSelectQuery = " + strSelectQuery);
            	    JLabel objLabel = new JLabel();
            	    objPrep  = connectionObject.prepareStatement(strQuery);
                    objRs = objPrep.executeQuery();
                    if(objRs.next()){
                        java.sql.Struct geometryObject = (java.sql.Struct) objRs.getObject(1);
                        oracle.sql.ARRAY coordinates = (oracle.sql.ARRAY) geometryObject.getAttributes()[4];
                        float[] l_tempArray = coordinates.getFloatArray();
                       // ShapeContents objContent.m_intCordinate = new float[l_tempArray.length];
                        objLabel.setText("MBR of Wheel is:\t  XMIN:  " + l_tempArray[0] +" YMIN:  "+ l_tempArray[1] + " XMAX:  "+ l_tempArray[2] + " YMAX:  "+ l_tempArray[3]);
                        panel.removeAll();
                        panel.add(objLabel);                    
                        panel.updateUI();
                    }
            }catch(SQLException ex){
            	System.out.println("SQLException");
            }
            }
        
    }
    
    private class DistanceWithinListner implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            String strSelectQuery = "SELECT SDO_GEOM.WITHIN_DISTANCE(pict1.shape,0,pict2.shape, 0.005)FROM donkeycart_818494008 pict1, donkeycart_818494008 pict2 WHERE  pict1.shp_id = 3 AND pict2.shp_id = 4";
            PreparedStatement objPrep = null;
            ResultSet objRs = null;
            try{
                JLabel objLabel = new JLabel();
                objPrep  = connectionObject.prepareStatement(strSelectQuery);
                objRs = objPrep.executeQuery();
                if(objRs.next()){
                    String strContains = objRs.getString(1);
                    if("FALSE".equalsIgnoreCase(strContains)){
                        strContains = "is NOT at 0";
                    }else{
                        strContains = "is at 0";
                    }
                    objLabel.setText("Wheel of the cart " + strContains + " distance from cart.");
                    panel.removeAll();
                    panel.add(objLabel);                    
                    panel.updateUI();
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
    }
    
    private class DistanceListner implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            String strSelectQuery = "Select SDO_GEOM.SDO_DISTANCE(pict1.shape,pict2.shape,0.005) FROM donkeycart_818494008 pict1, donkeycart_818494008 pict2 WHERE pict1.shp_id = 9 and pict2.shp_id = 25";
            PreparedStatement objPrep = null;
            ResultSet objRs = null;
            try{
                JLabel objLabel = new JLabel();
                objPrep  = connectionObject.prepareStatement(strSelectQuery);
                objRs = objPrep.executeQuery();
                if(objRs.next()){
                    double dblArea = objRs.getDouble(1);
                    objLabel.setText("Distance between Tail and face of donkey is : " + (dblArea*conversionFactor));
                    panel.removeAll();
                    panel.add(objLabel);                    
                    panel.updateUI();
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
    }
    
    private class AreaListner implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            String query = "Select SDO_GEOM.SDO_AREA(shape ,0.005) FROM donkeycart_818494008 WHERE shp_id=6";
            PreparedStatement objPrep = null;
            ResultSet resultObject = null;
            try{
                JLabel objLabel = new JLabel();
                objPrep  = connectionObject.prepareStatement(query);
                resultObject = objPrep.executeQuery();
                if(resultObject.next()){
                    double dblArea = resultObject.getDouble(1);
                    objLabel.setText("Area of boxes containing XMAS is : " + (dblArea*8) + " approx.");
                    panel.removeAll();
                    panel.add(objLabel);                    
                    panel.updateUI();
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
    }
    
    private class TouchListner implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            String strSelectQuery = "SELECT SDO_GEOM.RELATE(pict1.shape, 'TOUCH', pict2.shape, 0.5)FROM donkeycart_818494008 pict1, donkeycart_818494008 pict2 WHERE  pict1.shp_id=3 AND pict2.shp_id=5";
            PreparedStatement objPrep = null;
            ResultSet resultObject = null;
            try{
                JLabel objLabel = new JLabel();
                objPrep  = connectionObject.prepareStatement(strSelectQuery);
                resultObject = objPrep.executeQuery();
                if(resultObject.next()){
                    String strContains = resultObject.getString(1);
                    objLabel.setText("Boxes on the cart " + strContains + " the cart.");
                    panel.removeAll();
                    panel.add(objLabel);                    
                    panel.updateUI();
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
    }
    
    private class LengthListner implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            String strSelectQuery = "SELECT SDO_GEOM.SDO_LENGTH(c.shape,0.005)FROM donkeycart_818494008 c WHERE c.shp_id = 30";
            PreparedStatement objPrep = null;
            ResultSet resultObject = null;
            try{
                JLabel objLabel = new JLabel();
                objPrep  = connectionObject.prepareStatement(strSelectQuery);
                resultObject = objPrep.executeQuery();
                if(resultObject.next()){
                    String strContains = resultObject.getString(1);
                    
                    objLabel.setText("Length of Apple is " + strContains);
                    panel.removeAll();
                    panel.add(objLabel);                    
                    panel.updateUI();
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
    }
    
    
    public class MessageBox extends JDialog {

        private JButton ok = new JButton("OK");
        private JLabel errMsg = new JLabel("",JLabel.CENTER);
        private JPanel jp = new JPanel();
        private ActionListener actLis;
        
        public MessageBox(String msg) {
            setTitle("Message Box");
            setBounds(690,400, 200, 100);
            setModal(true);
            
            addWindowListener(new WindowAdapter(){
                public void windowClosed(WindowEvent we){
                    setVisible(false);
                }
            });
            
            actLis = new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    Object source = ae.getSource();
                    if(source == ok){
                        setVisible(false);
                    }    
                }    
            };
            ok.addActionListener(actLis);
            jp.add(ok);
            getContentPane().add(jp,BorderLayout.SOUTH);
            errMsg.setText(msg);
            getContentPane().add(errMsg,BorderLayout.CENTER);
        }
    }
}