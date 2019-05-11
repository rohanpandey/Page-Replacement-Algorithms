/*
 * @author  :  Rohan Pandey
 * @roll no :  1710110419
 */

package osgla2;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.*;
import javax.swing.table.TableColumnModel;

public class PageReplacement extends JFrame {    
    JPanel fifo_panel = new JPanel(new BorderLayout());
    JPanel lru_panel = new JPanel(new BorderLayout());
    JPanel opt_panel = new JPanel(new BorderLayout());
    JComboBox no_of_frame_combobox;
    JTextField reference_string_text;
    public PageReplacement(){
    fifo_panel.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
    lru_panel.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
    opt_panel.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
    this.setLayout(new GridLayout(4,1));
    JPanel panel_top = new JPanel(new GridLayout(4,1));
    JPanel panel_top_1 = new JPanel(new GridLayout(1,2));
    JPanel panel_top_2 = new JPanel(new GridLayout(1,2));
    JPanel panel_top_3 = new JPanel(new FlowLayout());
    JLabel no_of_frame_label = new JLabel("No. of Frames: ");
    String[] frame_list = {"3","4","5","6","7"};
    no_of_frame_combobox = new JComboBox(frame_list);
    JLabel reference_string_label = new JLabel("Reference String:");
    reference_string_text = new JTextField(20);
    JButton compute = new JButton("Compute");
    compute.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            display();
            repaint();
        }
    });
    JLabel top = new JLabel("Replacement Algorithms");
    JPanel inside_1 = new JPanel();
    JPanel inside_2 = new JPanel();
    JPanel inside_3 = new JPanel();
    JPanel inside_4 = new JPanel();
    JPanel inside_5 = new JPanel();
    inside_1.add(no_of_frame_label);
    inside_2.add(no_of_frame_combobox);
    inside_3.add(reference_string_label);
    inside_4.add(reference_string_text);
    inside_5.add(top);
    panel_top_1.add(inside_1);
    panel_top_1.add(inside_2);
    panel_top_2.add(inside_3);
    panel_top_2.add(inside_4);
    panel_top_3.add(compute);
    panel_top.add(inside_5);
    panel_top.add(panel_top_1);
    panel_top.add(panel_top_2);
    panel_top.add(panel_top_3);
    this.add(panel_top);
    this.add(fifo_panel);
    this.add(lru_panel);
    this.add(opt_panel);
    }
    
    void display()
    {
        fifo();
        lru();
        opt();
    }
    
    void fifo()
    {
        fifo_panel.removeAll();
        int frame_no  = Integer.parseInt((String)(no_of_frame_combobox.getSelectedItem()));
        int page_fault=0;
        ArrayList<Integer> values = new ArrayList<>();
        String reference_string=reference_string_text.getText();
        for(int i=0;i<reference_string.length();i++)
        {
            if(reference_string.charAt(i)==',')
                ;
            else
            {
                String s="";
                s=s+reference_string.charAt(i);
                values.add(Integer.parseInt(s));
            }
        }
        JTable fifo_table = new JTable(frame_no+1,values.size()+1);
        for(int r=0;r<frame_no;r++)
        {
            fifo_table.setValueAt("Frame: "+(r+1), r, 0);
        }

        ArrayList<Integer> FIFO = new ArrayList<>();
        int var=0;
        for(int i=0;i<values.size();i++)
        {
            boolean pf=false;
            if(FIFO.size()<frame_no)
            {
                if(!FIFO.contains(values.get(i)))
                {
                    FIFO.add(values.get(i));
                    page_fault++;
                    pf=true;
                }
            }
            else
            {
                if(!FIFO.contains(values.get(i)))
                {
                    FIFO.remove(var);
                    FIFO.add(var,values.get(i));
                    page_fault++;
                    var++;
                    var=var%frame_no;
                    pf=true;
                }
            }
            for(int j=0;j<FIFO.size();j++)
            {
                fifo_table.setValueAt(FIFO.get(j), j, i+1);
            }
            if(pf==true)
                fifo_table.setValueAt("Miss", frame_no, i+1);
            else
                fifo_table.setValueAt("Hit", frame_no, i+1);
        }
        JLabel fifo_label= new JLabel("  FIFO:");
        JLabel fifo_pf = new JLabel("  Page Faults: "+page_fault);
        TableColumnModel column = fifo_table.getColumnModel();
        javax.swing.table.TableColumn tablecolumn = column.getColumn(0);
        tablecolumn.setHeaderValue("Reference String"); 
        for(int c=1;c<values.size()+1;c++)
        {
            tablecolumn = column.getColumn(c);
            tablecolumn.setHeaderValue(values.get(c-1));
        }
        fifo_panel.add(fifo_label,BorderLayout.NORTH);
        fifo_panel.add(fifo_pf,BorderLayout.SOUTH);
        fifo_panel.add(new JScrollPane(fifo_table),BorderLayout.CENTER);
        repaint();
        revalidate();
    }
    
    void lru()
    {
        lru_panel.removeAll();
        int frame_no = Integer.parseInt((String)(no_of_frame_combobox.getSelectedItem()));
        int page_fault=0;
        ArrayList<Integer> values = new ArrayList<>();
        String reference_string=reference_string_text.getText();
        for(int i=0;i<reference_string.length();i++)
        {
            if(reference_string.charAt(i)==',')
                ;
            else
            {
                String s="";
                s=s+reference_string.charAt(i);
                values.add(Integer.parseInt(s));
            }
        }
        LinkedHashSet<Integer> s = new LinkedHashSet<>(frame_no);
        LinkedHashMap<Integer, Integer> LRU = new LinkedHashMap<>();
        HashMap<Integer, Integer> positions = new HashMap<>();
        JTable lru_table = new JTable(frame_no+1,values.size()+1);
        for(int r=0;r<frame_no;r++)
        {
            lru_table.setValueAt("Frame: "+(r+1), r, 0);
        }
        int k=0;
        for(int i=0;i<values.size();i++)
        {

            boolean pf=false;
            if(s.size()<frame_no)
            {
                if(!s.contains(values.get(i)))
                {
                    s.add(values.get(i));
                    page_fault++;
                    pf=true;
                    positions.put(k,values.get(i));
                    LRU.put(values.get(i),k);
                    k++;
                }    
            }
            else
            {
                if(!s.contains(values.get(i)))
                {
                    int lru = Integer.MAX_VALUE, val=Integer.MIN_VALUE;
                    Iterator<Integer> itr = s.iterator(); 
                    while (itr.hasNext()) { 
                        int temp = itr.next(); 
                        if (LRU.get(temp) < lru) 
                        { 
                            lru = LRU.get(temp); 
                            val = temp; 
                        } 
                    }
                    s.remove(val); 
                    s.add(values.get(i));
                    int remove=Integer.MIN_VALUE;
                    for (Integer key : positions.keySet()) {
                        if(positions.get(key)==val)
                            remove=key;
                    }
                    positions.replace(remove, values.get(i));
                    page_fault++; 
                    pf=true;
                }
            }
            LRU.put(values.get(i), i);
            for(int j=0;j<positions.size();j++)
            {
                    lru_table.setValueAt(positions.get(j), j, i+1);                
            }
            if(pf==true)
                lru_table.setValueAt("Miss", frame_no, i+1);
            else
                lru_table.setValueAt("Hit", frame_no, i+1);
        }
        TableColumnModel column = lru_table.getColumnModel();
        javax.swing.table.TableColumn tablecolumn = column.getColumn(0);
        tablecolumn.setHeaderValue("Reference String"); 
        for(int c=1;c<values.size()+1;c++)
        {
            tablecolumn = column.getColumn(c);
            tablecolumn.setHeaderValue(values.get(c-1));
        }
        JLabel lru_label= new JLabel("  LRU:");
        JLabel lru_pf = new JLabel("  Page Faults: "+page_fault);
        lru_panel.add(lru_label,BorderLayout.NORTH);
        lru_panel.add(new JScrollPane(lru_table),BorderLayout.CENTER);
        lru_panel.add(lru_pf,BorderLayout.SOUTH);
        repaint();
        revalidate();       
    }
    
    void opt()
    {
        opt_panel.removeAll();
        int frame_no = Integer.parseInt((String)(no_of_frame_combobox.getSelectedItem()));
        int page_fault=0;
        ArrayList<Integer> values = new ArrayList<>();
        String reference_string=reference_string_text.getText();
        for(int i=0;i<reference_string.length();i++)
        {
            if(reference_string.charAt(i)==',')
                ;
            else
            {
                String s="";
                s=s+reference_string.charAt(i);
                values.add(Integer.parseInt(s));
            }
        }
        JTable opt_table = new JTable(frame_no+1,values.size()+1);
        for(int i=0;i<frame_no;i++)
        {
            opt_table.setValueAt("Frame: "+(i+1), i, 0);
        }
        ArrayList<String> list=new ArrayList<>();
        ArrayList<Integer> OPT=new ArrayList<>();
        for(int i=0;i<frame_no;i++)
          OPT.add(Integer.MIN_VALUE);
        for(int i=0;i<values.size();i++)
        {
            boolean inlist=false;
            int temp=values.get(i);
            for(int j=0;j<OPT.size();j++)
            {
                if(OPT.get(j)==(temp))
                {
                    inlist=true;
                    break;
                }
            }
            if(inlist)
            {
                list.add("Hit");
            }
            else
            {
                list.add("Miss");
                boolean changed=false;
                for(int j=0;j<OPT.size();j++)
                {
                    if(OPT.get(j)==-1000)
                    {
                        OPT.set(j,values.get(i));
                        changed=true;
                        break;
                    }
                }
                if(!changed)
                {
                    ArrayList<Integer> positions=new ArrayList<>();
                    for(int j=0;j<OPT.size();j++)
                    {
                        temp=OPT.get(j);
                        boolean found=false;
                        for(int k=i+1;k<values.size();k++)
                        {
                            if(temp==values.get(k))
                            {
                                positions.add(k);found=true;
                                break;
                            }
                        }
                        if(!found)
                        {
                            positions.add(Integer.MAX_VALUE);
                        }
                    }
                    int maxim=-1;int maxv=-1;
                    for(int j=0;j<positions.size();j++)
                    {
                        if(positions.get(j)>=maxv)
                        {
                            maxim=j;
                            maxv=positions.get(j);
                        }
                        if(positions.get(j)==Integer.MAX_VALUE)
                            break;
                    }
                    OPT.set(maxim,values.get(i));
                }
            }
            for(int j=0;j<OPT.size();j++)
            {
                if(OPT.get(j)!= Integer.MIN_VALUE)
                    opt_table.setValueAt(OPT.get(j),j,i+1);
            }
        }
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).equalsIgnoreCase("Miss"))
                page_fault++;
            opt_table.setValueAt(list.get(i),frame_no,i+1);
        }
        TableColumnModel column = opt_table.getColumnModel();
        javax.swing.table.TableColumn tablecolumn = column.getColumn(0);
        tablecolumn.setHeaderValue("Reference String"); 
        for(int c=1;c<values.size()+1;c++)
        {
            tablecolumn = column.getColumn(c);
            tablecolumn.setHeaderValue(values.get(c-1));
        }
        JLabel opt_label= new JLabel("  OPT: ");
        JLabel opt_pf = new JLabel("  Page Faults: "+page_fault);
        opt_panel.add(opt_label,BorderLayout.NORTH);
        opt_panel.add(opt_pf,BorderLayout.SOUTH);
        opt_panel.add(new JScrollPane(opt_table));
        repaint();
        revalidate();
    }
    
    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        PageReplacement frame = new PageReplacement();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(screenSize.width,screenSize.height-50);
        frame.setTitle("Page Replacement Algorithms");
    }
}