/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.SystemColor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author FranciscoHernandez
 */
public class ReportesPanel extends javax.swing.JPanel {

    private Connection con = null;
    private PreparedStatement pstm = null;
    private ResultSet rs = null;
    private static String user = "";
    private static String password = "";
    private int fila = 0;
    private int filaAct = 0;

    /**
     * Creates new form ReportesPanel
     */
    public ReportesPanel(String user, String password) {
        initComponents();
        this.user = user;
        this.password = password;
        con = UConnection.getConnection(user, password);
        graficaPanel.setLayout(new BorderLayout());
        panelLibrosGra.setLayout(new BorderLayout());
    }

    public String reporteEstudios(String sexo, String mes, String anio, String escolaridad) {
        String mesAux = "__";
        String anioAux = "____";
        if (!mes.equals("Cualquiera")) {
            mesAux = decifrarMes(mes);
        }
        if (!anio.equals("Cualquiera")) {
            anioAux = anio;
        }
        String sql = "SELECT COUNT(*) AS EC FROM reportes WHERE Sexo = '" + sexo + "' AND FechaV LIKE '__/" + mesAux + "/" + anioAux + "' AND Escolaridad='" + escolaridad + "'";
        String res = "";
        //con = UConnection.getConnection();
        try {
            pstm = con.prepareStatement(sql);

            rs = pstm.executeQuery();
            while (rs.next()) {
                res = rs.getString("EC");
            }
            pstm.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return res;
    }

    public String reporteDiscapacitados(String sexo, String mes, String anio) {
        String sql = "SELECT COUNT(*) AS EC FROM reportes WHERE Sexo = '" + sexo + "' AND FechaV LIKE '__/" + decifrarMes(mes) + "/" + anio + "' AND Discapacidad !='Ninguna'";
        String res = "";
        //con = UConnection.getConnection();
        try {
            pstm = con.prepareStatement(sql);

            rs = pstm.executeQuery();
            while (rs.next()) {
                res = rs.getString("EC");
            }
            pstm.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return res;
    }

    public String decifrarMes(String mes) {
        String digito = "";
        if (mes.equals("Enero")) {
            digito = "01";
        } else if (mes.equals("Febrero")) {
            digito = "02";
        } else if (mes.equals("Marzo")) {
            digito = "03";
        } else if (mes.equals("Abril")) {
            digito = "04";
        } else if (mes.equals("Mayo")) {
            digito = "05";
        } else if (mes.equals("Junio")) {
            digito = "06";
        } else if (mes.equals("Julio")) {
            digito = "07";
        } else if (mes.equals("Agosto")) {
            digito = "08";
        } else if (mes.equals("Septiembre")) {
            digito = "09";
        } else if (mes.equals("Octubre")) {
            digito = "10";
        } else if (mes.equals("Noviembre")) {
            digito = "11";
        } else if (mes.equals("Diciembre")) {
            digito = "12";
        }
        return digito;
    }

    public void reportePretamos() {
        String tipo = "";
        if (masMenosPrestados.getSelectedIndex() == 0) {
            tipo = "DESC";
        } else {
            tipo = "ASC";
        }
        String categoria = "";
        String m = "";
        if (categoriaBox.getSelectedIndex() != 0) {
            categoria = "AND Categoria like '" + categoriaBox.getSelectedItem().toString() + "%' ";
            m = categoriaBox.getSelectedItem().toString() + " ";
        }
        String mes = "__";
        String me = "";
        String anio = "____";
        String an = "";
        if (mesPrestamos.getSelectedIndex() != 0) {
            mes = decifrarMes(mesPrestamos.getSelectedItem().toString());
            me = mesPrestamos.getSelectedItem().toString() + " ";
        }
        if (anioPrestamos.getSelectedIndex() != 0) {
            anio = anioPrestamos.getSelectedItem().toString();
            an = anioPrestamos.getSelectedItem().toString() + " ";
        }
        String fecha = "AND FechaInicioP like '__/" + mes + "/" + anio + "'";
        String sql = "SELECT Titulo,COUNT(*) as Top from prestamos as P,libros as L,detalle_prestamos as D WHERE L.isbn=P.isbn AND D.IdPrestamo=P.IdPrestamo " + categoria + fecha + " GROUP BY L.isbn ORDER BY Top " + tipo + " LIMIT 10";
        String res = "";
        try {
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            String nombre = "Libros " + masMenosPrestados.getSelectedItem().toString() + " " + m + me + an;
            while (rs.next()) {
                int top = rs.getInt("Top");
                String t = rs.getString("Titulo");
                dataset.setValue(top, t, t);
            }
            JFreeChart chart = ChartFactory.createStackedBarChart3D(nombre, "Libro", "Cantidad de préstamos", dataset, PlotOrientation.VERTICAL, true, true, true);
            CategoryPlot catPlot = chart.getCategoryPlot();
            //catPlot.setRangeMinorGridlinePaint(Color.BLUE);

            CategoryPlot cplot = (CategoryPlot) chart.getPlot();
            cplot.setBackgroundPaint(SystemColor.inactiveCaption);//change background color

            ((BarRenderer) cplot.getRenderer()).setBarPainter(new StandardBarPainter());

            BarRenderer r = (BarRenderer) chart.getCategoryPlot().getRenderer();
            r.setSeriesPaint(0, Color.BLUE);
            r.setSeriesPaint(1, Color.magenta);
            r.setSeriesPaint(2, Color.yellow);
            r.setSeriesPaint(3, Color.red);
            r.setSeriesPaint(4, Color.green);
            r.setSeriesPaint(5, Color.cyan);
            r.setSeriesPaint(6, Color.black);
            r.setSeriesPaint(7, Color.gray);
            r.setSeriesPaint(8, Color.orange);
            r.setSeriesPaint(9, Color.pink);

            ChartPanel cp = new ChartPanel(chart);
            panelLibrosGra.removeAll();
            panelLibrosGra.add(cp, BorderLayout.CENTER);
            panelLibrosGra.revalidate();
            pstm.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }

    }

    public void generarGrafica(String mes, String anio) {
        if (mes.equals("Cualquiera")) {
            mes = "";
        }
        if (anio.equals("Cualquiera")) {
            anio = "";
        }
        String nombre = "Visitas " + mes + " " + anio;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < 7; i++) {
            dataset.setValue(Integer.parseInt(tablaReportes.getValueAt(i, 1).toString()), "Hombres", tablaReportes.getValueAt(i, 0).toString());
            dataset.setValue(Integer.parseInt(tablaReportes.getValueAt(i, 2).toString()), "Mujeres", tablaReportes.getValueAt(i, 0).toString());
        }

        //JFreeChart chart = ChartFactory.createBarChart3D("Estadisticas", "Hola", "PRueba", dataset, PlotOrientation.VERTICAL, true, true, false);
        JFreeChart chart = ChartFactory.createStackedBarChart3D(nombre, "Clasificación", "Número Personas", dataset, PlotOrientation.HORIZONTAL, true, true, true);
        CategoryPlot catPlot = chart.getCategoryPlot();
        catPlot.setRangeMinorGridlinePaint(Color.BLUE);

        CategoryPlot cplot = (CategoryPlot) chart.getPlot();
        cplot.setBackgroundPaint(SystemColor.inactiveCaption);//change background color

        ((BarRenderer) cplot.getRenderer()).setBarPainter(new StandardBarPainter());

        BarRenderer r = (BarRenderer) chart.getCategoryPlot().getRenderer();
        r.setSeriesPaint(0, Color.BLUE);
        r.setSeriesPaint(1, Color.magenta);

        ChartPanel cp = new ChartPanel(chart);
        graficaPanel.removeAll();
        graficaPanel.add(cp, BorderLayout.CENTER);
        graficaPanel.revalidate();
    }

    public void reporteMultas() {
        String motivo = "";
        String mes = "__";
        String anio = "____";
        String ordenar = "ORDER BY ";
        if (mesMultaBox.getSelectedIndex() != 0) {
            mes = decifrarMes(mesMultaBox.getSelectedItem().toString());
        }
        if (anioMultaBox.getSelectedIndex() != 0) {
            anio = anioMultaBox.getSelectedItem().toString();
        }
        if (tipoMultaBox.getSelectedIndex() != 0) {
            motivo = tipoMultaBox.getSelectedItem().toString();
        }
        if (ordenarMultas.getSelectedIndex() == 0) {
            ordenar = ordenar + "NombreU ASC";
        }
        if (ordenarMultas.getSelectedIndex() == 1) {
            ordenar = ordenar + "NombreU DESC";
        }
        if (ordenarMultas.getSelectedIndex() == 2) {
            ordenar = ordenar + "Motivo ASC";
        }
        if (ordenarMultas.getSelectedIndex() == 3) {
            ordenar = ordenar + "Motivo DESC";
        }
        String fecha = "AND FechaMulta like '__/" + mes + "/" + anio + "' ";
        String sql = "SELECT U.IdUsuario,NombreU,Motivo,CostoM,FechaMulta from usuarios as U,pagos as P,multas as M WHERE U.IdUsuario=P.IdUsuario AND M.IdMulta=P.IdMulta AND Estado='En proceso' AND Motivo like '" + motivo + "%' " + fecha + ordenar;
        //con = UConnection.getConnection(user,password);
        try {
            pstm = con.prepareStatement(sql);
            fila = 0;
            int col = 0;
            rs = pstm.executeQuery();

            while (rs.next()) {
                tablaMultas.setValueAt(rs.getString("IdUsuario"), fila, col);
                col++;
                tablaMultas.setValueAt(rs.getString("NombreU"), fila, col);
                col++;
                tablaMultas.setValueAt(rs.getString("Motivo"), fila, col);
                col++;
                tablaMultas.setValueAt(rs.getString("CostoM"), fila, col);
                col++;
                tablaMultas.setValueAt(rs.getString("FechaMulta"), fila, col);
                col = 0;
                if (fila == tablaMultas.getRowCount() - 1) {
                    break;
                }
                fila++;
            }
            pstm.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void limpiarTablaMultas() {
        for (int x = 0; x < 5; x++) {
            for (int i = 0; i < fila; i++) {
                tablaMultas.setValueAt(null, i, x);
            }
        }
    }

    public void reporteActividades() {
        String motivo = "";
        String mes = "__";
        String anio = "____";
        String ordenar = "ORDER BY ";
        if (mesActividades.getSelectedIndex() != 0) {
            mes = decifrarMes(mesActividades.getSelectedItem().toString());
        }
        if (anioActividad.getSelectedIndex() != 0) {
            anio = anioActividad.getSelectedItem().toString();
        }

        if (ordenarActividad.getSelectedIndex() == 0) {
            ordenar = ordenar + "NombreU ASC";
        }
        if (ordenarActividad.getSelectedIndex() == 1) {
            ordenar = ordenar + "NombreU DESC";
        }
        if (ordenarActividad.getSelectedIndex() == 2) {
            ordenar = ordenar + "NombreA ASC";
        }
        if (ordenarActividad.getSelectedIndex() == 3) {
            ordenar = ordenar + "NombreA DESC";
        }
        if (ordenarActividad.getSelectedIndex() == 4) {
            ordenar = ordenar + "CostoTotalA-SumAbono ASC";
        }
        if (ordenarActividad.getSelectedIndex() == 5) {
            ordenar = ordenar + "CostoTotalA-SumAbono DESC";
        }
        String fecha = "AND FechaR like '__/" + mes + "/" + anio + "' ";
        String sql = "SELECT U.IdUsuario,NombreU,NombreA,(CostoTotalA-SumAbono) as Adeudo,FechaR from usuarios as U,actividades as A,registros as R WHERE U.IdUsuario=R.IdUsuario AND R.IdActividad=A.IdActividad AND CostoTotalA-SumAbono != 0 " + fecha + ordenar;
//con = UConnection.getConnection(user,password);
        try {
            pstm = con.prepareStatement(sql);
            filaAct = 0;
            int col = 0;
            rs = pstm.executeQuery();

            while (rs.next()) {
                tablaActividades.setValueAt(rs.getString("IdUsuario"), filaAct, col);
                col++;
                tablaActividades.setValueAt(rs.getString("NombreU"), filaAct, col);
                col++;
                tablaActividades.setValueAt(rs.getString("NombreA"), filaAct, col);
                col++;
                tablaActividades.setValueAt(rs.getString("Adeudo"), filaAct, col);
                col++;
                tablaActividades.setValueAt(rs.getString("FechaR"), filaAct, col);
                col = 0;
                if (filaAct == tablaActividades.getRowCount() - 1) {
                    break;
                }
                filaAct++;
            }
            pstm.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void limpiarTablaAct() {
        for (int x = 0; x < 5; x++) {
            for (int i = 0; i < filaAct; i++) {
                tablaActividades.setValueAt(null, i, x);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        mesBox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        anioBox = new javax.swing.JComboBox<>();
        generarBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaReportes = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        graficaPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        masMenosPrestados = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        categoriaBox = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        mesPrestamos = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        anioPrestamos = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        panelLibrosGra = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        mesMultaBox = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        anioMultaBox = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        tipoMultaBox = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaMultas = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        ordenarMultas = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        mesActividades = new javax.swing.JComboBox<>();
        anioActividad = new javax.swing.JComboBox<>();
        generarActividad = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaActividades = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        ordenarActividad = new javax.swing.JComboBox<>();

        setPreferredSize(new java.awt.Dimension(909, 614));

        jLabel1.setFont(new java.awt.Font("Century Gothic", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("REPORTES");

        jLabel2.setText("Mes:");

        mesBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cualquiera", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" }));

        jLabel3.setText("Año:");

        anioBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cualquiera", "2018", "2019", "2020", "2021", "2022", "2023" }));

        generarBtn.setText("Generar");
        generarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generarBtnActionPerformed(evt);
            }
        });

        tablaReportes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Primaria", null, null},
                {"Secundaria", null, null},
                {"Bachillerato", null, null},
                {"Licenciatura", null, null},
                {"Maestría", null, null},
                {"Doctorado", null, null},
                {"Discapacidad", null, null}
            },
            new String [] {
                "Clasificación", "Hombres", "Mujeres"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaReportes);
        if (tablaReportes.getColumnModel().getColumnCount() > 0) {
            tablaReportes.getColumnModel().getColumn(0).setMinWidth(90);
            tablaReportes.getColumnModel().getColumn(0).setMaxWidth(90);
        }

        jLabel9.setText("CANTIDAD");

        graficaPanel.setPreferredSize(new java.awt.Dimension(650, 500));

        javax.swing.GroupLayout graficaPanelLayout = new javax.swing.GroupLayout(graficaPanel);
        graficaPanel.setLayout(graficaPanelLayout);
        graficaPanelLayout.setHorizontalGroup(
            graficaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 724, Short.MAX_VALUE)
        );
        graficaPanelLayout.setVerticalGroup(
            graficaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 460, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mesBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel9))
                            .addComponent(anioBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(graficaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(generarBtn)
                        .addContainerGap(675, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(generarBtn)
                    .addComponent(mesBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(anioBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 294, Short.MAX_VALUE))
                    .addComponent(graficaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Visitas", jPanel3);

        jLabel4.setText("Libros:");

        masMenosPrestados.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Más prestados", "Menos prestados" }));

        jLabel5.setText("Categoría:");

        categoriaBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas las categorías", "Filosofía y Psicología", "Ciencias Sociales", "Lenguas", "Tecnología (Ciencias Aplicadas)", "Ciencias Naturales y Matemáticas", "Literatura y Retórica", "Bellas Artes", "Geografía e Historia", "Area de Consulta", "Infantil" }));

        jLabel6.setText("Mes:");

        mesPrestamos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cualquiera", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" }));

        jLabel7.setText("Año:");

        anioPrestamos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cualquiera", "2018", "2019", "2020", "2021", "2022", "2023" }));

        jButton1.setText("Generar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLibrosGraLayout = new javax.swing.GroupLayout(panelLibrosGra);
        panelLibrosGra.setLayout(panelLibrosGraLayout);
        panelLibrosGraLayout.setHorizontalGroup(
            panelLibrosGraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelLibrosGraLayout.setVerticalGroup(
            panelLibrosGraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 437, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelLibrosGra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(masMenosPrestados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(categoriaBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mesPrestamos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(anioPrestamos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addGap(0, 178, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(masMenosPrestados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(categoriaBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(mesPrestamos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(anioPrestamos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(31, 31, 31)
                .addComponent(panelLibrosGra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Libros", jPanel4);

        jLabel8.setText("Mes:");

        mesMultaBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cualquiera", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" }));

        jLabel10.setText("Año:");

        anioMultaBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cualquiera", "2018", "2019", "2020", "2021", "2022", "2023" }));

        jLabel11.setText("Motivo:");

        tipoMultaBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas las multas", "Daño", "Extravio", "Retardo" }));

        jButton2.setText("Generar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        tablaMultas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Usuario", "Nombre", "Motivo", "Costo", "Fecha"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaMultas.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tablaMultas);

        jLabel14.setText("Ordenar por:");

        ordenarMultas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre Ascendente", "Nombre Descendente", "Motivo Ascendente", "Motivo Descendente" }));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 993, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tipoMultaBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mesMultaBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(anioMultaBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ordenarMultas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(mesMultaBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(anioMultaBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(tipoMultaBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(jLabel14)
                    .addComponent(ordenarMultas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Deudores Multas", jPanel5);

        jLabel12.setText("Mes:");

        jLabel13.setText("Año:");

        mesActividades.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cualquiera", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" }));

        anioActividad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cualquiera", "2018", "2019", "2020", "2021", "2022", "2023" }));

        generarActividad.setText("Generar");
        generarActividad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generarActividadActionPerformed(evt);
            }
        });

        tablaActividades.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Usuario", "Nombre", "Actividad", "Adeudo", "Fecha de registro"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaActividades.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tablaActividades);

        jLabel15.setText("Ordenar por:");

        ordenarActividad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre Ascendente", "Nombre Descendente", "Actividad Ascendente", "Actividad Descendente", "Adeudo Ascendente", "Adeudo Descendente" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mesActividades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(anioActividad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ordenarActividad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(generarActividad)
                        .addGap(0, 425, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(mesActividades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(anioActividad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(generarActividad)
                    .addComponent(jLabel15)
                    .addComponent(ordenarActividad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Deudores Actividades", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1039, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        reportePretamos();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void generarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generarBtnActionPerformed
        String MES = mesBox.getSelectedItem().toString();
        String ANIO = anioBox.getSelectedItem().toString();

        tablaReportes.setValueAt(reporteEstudios("M", MES, ANIO, "Primaria"), 0, 1);
        tablaReportes.setValueAt(reporteEstudios("F", MES, ANIO, "Primaria"), 0, 2);
        tablaReportes.setValueAt(reporteEstudios("M", MES, ANIO, "Secundaria"), 1, 1);
        tablaReportes.setValueAt(reporteEstudios("F", MES, ANIO, "Secundaria"), 1, 2);
        tablaReportes.setValueAt(reporteEstudios("M", MES, ANIO, "Bachillerato"), 2, 1);
        tablaReportes.setValueAt(reporteEstudios("F", MES, ANIO, "Bachillerato"), 2, 2);
        tablaReportes.setValueAt(reporteEstudios("M", MES, ANIO, "Licenciatura"), 3, 1);
        tablaReportes.setValueAt(reporteEstudios("F", MES, ANIO, "Licenciatura"), 3, 2);
        tablaReportes.setValueAt(reporteEstudios("M", MES, ANIO, "Maestria"), 4, 1);
        tablaReportes.setValueAt(reporteEstudios("F", MES, ANIO, "Maestria"), 4, 2);
        tablaReportes.setValueAt(reporteEstudios("M", MES, ANIO, "Doctorado"), 5, 1);
        tablaReportes.setValueAt(reporteEstudios("F", MES, ANIO, "Doctorado"), 5, 2);
        tablaReportes.setValueAt(reporteDiscapacitados("M", MES, ANIO), 6, 1);
        tablaReportes.setValueAt(reporteDiscapacitados("F", MES, ANIO), 6, 2);
        generarGrafica(MES, ANIO);
    }//GEN-LAST:event_generarBtnActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        limpiarTablaMultas();
        reporteMultas();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void generarActividadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generarActividadActionPerformed
        limpiarTablaAct();
        reporteActividades();
    }//GEN-LAST:event_generarActividadActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> anioActividad;
    private javax.swing.JComboBox<String> anioBox;
    private javax.swing.JComboBox<String> anioMultaBox;
    private javax.swing.JComboBox<String> anioPrestamos;
    private javax.swing.JComboBox<String> categoriaBox;
    private javax.swing.JButton generarActividad;
    private javax.swing.JButton generarBtn;
    private javax.swing.JPanel graficaPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JComboBox<String> masMenosPrestados;
    private javax.swing.JComboBox<String> mesActividades;
    private javax.swing.JComboBox<String> mesBox;
    private javax.swing.JComboBox<String> mesMultaBox;
    private javax.swing.JComboBox<String> mesPrestamos;
    private javax.swing.JComboBox<String> ordenarActividad;
    private javax.swing.JComboBox<String> ordenarMultas;
    private javax.swing.JPanel panelLibrosGra;
    private javax.swing.JTable tablaActividades;
    private javax.swing.JTable tablaMultas;
    private javax.swing.JTable tablaReportes;
    private javax.swing.JComboBox<String> tipoMultaBox;
    // End of variables declaration//GEN-END:variables
}
