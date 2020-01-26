/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgab;

import com.mxrck.autocompleter.TextAutoCompleter;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringStack;
import java.awt.Color;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author FranciscoHernandez
 */
public class LibrosPanel extends javax.swing.JPanel {

    private Connection con = null;
    private PreparedStatement pstm = null;
    private ResultSet rs = null;

    private String ISBN;
    private String Titulo;
    private String Autor;
    private String Editorial;
    private int Edicion;
    private String Categoria;
    private int Anio;
    private int Existencia;
    private String[] botones = {"Si", "No"};
    private Validar val = new Validar();
    private int fila = 0;
    private static String user = "";
    private static String password = "";
   TextAutoCompleter ta;

    /**
     * Creates new form LibrosP
     */
    public LibrosPanel(String user, String password) {
        initComponents();
        this.user = user;
        this.password = password;
        eliminarBtnL.setBackground(Color.RED);
        eliminarBtnL.setForeground(Color.WHITE);

        modificarBtn.setBackground(new Color(32, 33, 79));
        modificarBtn.setForeground(Color.WHITE);

        tablaConsultaGen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ISBNModTxt.setEditable(false);
        limpiarTablaConsulta();
        consultaGeneral();
        consultarEditorial();
        ta = new TextAutoCompleter(autorTxtReg);
    }

    public void registrarLibro() {
        String sql = "INSERT INTO Libros (ISBN,Titulo,Autor,Editorial,Edicion,Categoria,Anio,Existencia) VALUES (?,?,?,?,?,?,?,?)";
        con = UConnection.getConnection(user, password);
        try {
            pstm = con.prepareStatement(sql);

            String editorialAux = editorialTxtReg.getText();
            if (editorialComboReg.getSelectedIndex()!=0) {
                editorialAux = editorialComboReg.getSelectedItem().toString();
            }
            pstm.setString(1, ISBNTxtReg.getText());
            pstm.setString(2, tituloTxtReg.getText());
            pstm.setString(3, autorTxtReg.getText());
            pstm.setString(4, editorialAux);
            pstm.setInt(5, Integer.parseInt(edicionBoxReg.getSelectedItem().toString()));
            pstm.setString(6, categoriaBoxReg.getSelectedItem().toString());
            pstm.setInt(7, Integer.parseInt(anioRegBox.getSelectedItem().toString()));
            pstm.setInt(8, Integer.parseInt(existenciaRegBox.getSelectedItem().toString()));

            int rtdo = pstm.executeUpdate();
            if (rtdo == 1) {
                JOptionPane.showMessageDialog(null, "Libro registrado");
                limpiarTxt();
                limpiarTablaConsulta();
                consultaGeneral();
            }
            pstm.close();
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void consultaGeneral() {
        String titulo = tituloConGenTxt.getText();
        String aut = autorConTxt.getText();
        String cat = categoriaConBox.getSelectedItem().toString();
        if (cat.equals("Todas las categorías")) {
            cat = "";
        }
        String sql = "SELECT * FROM Libros WHERE Titulo like '" + titulo + "%' AND Autor like '" + aut + "%' AND Categoria like '" + cat + "%'";
        con = UConnection.getConnection(user, password);
        try {
            pstm = con.prepareStatement(sql);

            fila = 0;
            int col = 0;
            rs = pstm.executeQuery();

            while (rs.next()) {
                tablaConsultaGen.setValueAt(rs.getString("ISBN"), fila, col);
                col++;
                tablaConsultaGen.setValueAt(rs.getString("Titulo"), fila, col);
                col++;
                tablaConsultaGen.setValueAt(rs.getString("Autor"), fila, col);
                col++;
                tablaConsultaGen.setValueAt(rs.getString("Editorial"), fila, col);
                col++;
                tablaConsultaGen.setValueAt(rs.getString("Edicion"), fila, col);
                col++;
                tablaConsultaGen.setValueAt(rs.getString("Categoria"), fila, col);
                col++;
                tablaConsultaGen.setValueAt(rs.getString("Anio"), fila, col);
                col++;
                tablaConsultaGen.setValueAt(rs.getString("Existencia"), fila, col);
                col = 0;
                if (fila == tablaConsultaGen.getRowCount() - 1) {
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

    public void modificarLibro() {
        String sql = "UPDATE Libros SET Titulo=?,Autor=?,Editorial=?,Edicion=?,Categoria=?,Anio=?,Existencia=? WHERE ISBN=?";
        con = UConnection.getConnection(user, password);
        try {
            pstm = con.prepareStatement(sql);
            String editorialAux = editorialModTxt.getText();
            if (editorialComboMod.getSelectedIndex()!=0) {
                editorialAux = editorialComboMod.getSelectedItem().toString();
            }
            pstm.setString(1, tituloModTxt.getText());
            pstm.setString(2, autorModTxt.getText());
            pstm.setString(3, editorialAux);
            pstm.setInt(4, Integer.parseInt(edicionModBox.getSelectedItem().toString()));
            pstm.setString(5, categoriaModBox.getSelectedItem().toString());
            pstm.setInt(6, Integer.parseInt(anioModBox.getSelectedItem().toString()));
            pstm.setInt(7, Integer.parseInt(existenciaModBox.getSelectedItem().toString()));
            pstm.setString(8, ISBNModTxt.getText());

            int rtdo = pstm.executeUpdate();
            if (rtdo == 1) {
                JOptionPane.showMessageDialog(null, "Libro actualizado");
                limpiarTxtActualizar();
            }
            pstm.close();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void consultaIndividual(String ISBNAux) {
        String sql = "SELECT * FROM Libros WHERE ISBN=?";
        con = UConnection.getConnection(user, password);
        limpiarVariables();
        try {
            pstm = con.prepareStatement(sql);
            pstm.setString(1, ISBNAux);

            rs = pstm.executeQuery();
            while (rs.next()) {
                ISBN = rs.getString("ISBN");
                Titulo = rs.getString("Titulo");
                Autor = rs.getString("Autor");
                Editorial = rs.getString("Editorial");
                Edicion = rs.getInt("Edicion");
                Categoria = rs.getString("Categoria");
                Anio = rs.getInt("Anio");
                Existencia = rs.getInt("Existencia");
            }
            pstm.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
public void consultarEditorial() {
        String sql = "SELECT DISTINCT Editorial FROM Libros";
        con = UConnection.getConnection(user, password);
        limpiarVariables();
        try {
            pstm = con.prepareStatement(sql);
            //pstm.setString(1, ISBNAux);

            rs = pstm.executeQuery();
            editorialComboMod.removeAllItems();
            editorialComboReg.removeAllItems();
            editorialComboReg.addItem("Nueva Editorial");
            editorialComboMod.addItem("Nueva Editorial");
            //ta.removeAllItems();
            while (rs.next()) {
                String edi = rs.getString("Editorial");
                editorialComboReg.addItem(edi);
                editorialComboMod.addItem(edi);
                //ta.addItem(edi);
            }
            pstm.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

public void consultarAutor() {
        String sql = "SELECT DISTINCT Autor FROM Libros WHERE Autor like '" +autorTxtReg.getText()+"%'";
        con = UConnection.getConnection(user, password);
        limpiarVariables();
        try {
            pstm = con.prepareStatement(sql);
            //pstm.setString(1, ISBNAux);

            rs = pstm.executeQuery();
            ta.removeAllItems();
            while (rs.next()) {
                ta.addItem(rs.getString("Autor"));
            }
            pstm.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    public void eliminarLibro(String isbn, int row) {
        String sql = "DELETE FROM Libros WHERE ISBN=?";
        con = UConnection.getConnection(user, password);
        try {
            pstm = con.prepareStatement(sql);
            pstm.setString(1, isbn);

            int rtdo = pstm.executeUpdate();
            if (rtdo == 1) {
                JOptionPane.showMessageDialog(null, "Libro eliminado");
                DefaultTableModel m = (DefaultTableModel) tablaConsultaGen.getModel();
                m.removeRow(row);
                m.addRow(new String[8]);
            }

            pstm.close();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void limpiarTxt() {
        ISBNTxtReg.setText("");
        tituloTxtReg.setText("");
        autorTxtReg.setText("");
        editorialTxtReg.setText("");
        edicionBoxReg.setSelectedIndex(0);
        categoriaBoxReg.setSelectedIndex(0);
        anioRegBox.setSelectedIndex(0);
        existenciaRegBox.setSelectedIndex(0);
    }

    public void limpiarTxtActualizar() {
        ISBNModTxt.setText("");
        tituloModTxt.setText("");
        autorModTxt.setText("");
        editorialModTxt.setText("");
        edicionModBox.setSelectedIndex(0);
        categoriaModBox.setSelectedIndex(0);
        existenciaModBox.setSelectedIndex(0);
        anioModBox.setSelectedIndex(0);
    }

    public void limpiarVariables() {
        ISBN = "";
        Titulo = "";
        Autor = "";
        Editorial = "";
        Edicion = 0;
        Categoria = "";
        Anio = 0;
        Existencia = 0;
    }

    public void limpiarTablaConsulta() {
        for (int x = 0; x < 8; x++) {
            for (int i = 0; i < fila; i++) {
                tablaConsultaGen.setValueAt(null, i, x);
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
        librosTabbed = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        ISBNTxtReg = new javax.swing.JTextField();
        tituloTxtReg = new javax.swing.JTextField();
        autorTxtReg = new javax.swing.JTextField();
        editorialTxtReg = new javax.swing.JTextField();
        ISBNLbl = new javax.swing.JLabel();
        tituloLbl = new javax.swing.JLabel();
        autorlbl = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        libroRegBtn = new javax.swing.JButton();
        edicionBoxReg = new javax.swing.JComboBox<>();
        categoriaBoxReg = new javax.swing.JComboBox<>();
        existenciaRegBox = new javax.swing.JComboBox<>();
        anioRegBox = new javax.swing.JComboBox<>();
        editorialComboReg = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        tituloConGenTxt = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaConsultaGen = new javax.swing.JTable();
        eliminarBtnL = new javax.swing.JButton();
        modificarBtn = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        autorConTxt = new javax.swing.JTextField();
        categoriaConBox = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        ISBNModTxt = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tituloModTxt = new javax.swing.JTextField();
        autorModTxt = new javax.swing.JTextField();
        editorialModTxt = new javax.swing.JTextField();
        actualizarBtn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        edicionModBox = new javax.swing.JComboBox<>();
        categoriaModBox = new javax.swing.JComboBox<>();
        existenciaModBox = new javax.swing.JComboBox<>();
        anioModBox = new javax.swing.JComboBox<>();
        editorialComboMod = new javax.swing.JComboBox<>();

        setPreferredSize(new java.awt.Dimension(909, 614));

        jLabel1.setFont(new java.awt.Font("Century Gothic", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("LIBROS");

        ISBNTxtReg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ISBNTxtRegKeyTyped(evt);
            }
        });

        tituloTxtReg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tituloTxtRegKeyTyped(evt);
            }
        });

        autorTxtReg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                autorTxtRegKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                autorTxtRegKeyTyped(evt);
            }
        });

        editorialTxtReg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                editorialTxtRegKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                editorialTxtRegKeyTyped(evt);
            }
        });

        ISBNLbl.setText("ISBN:");

        tituloLbl.setText("Título:");

        autorlbl.setText("Autor:");

        jLabel5.setText("Editorial:");

        jLabel6.setText("Edición:");

        jLabel7.setText("Categoría");

        jLabel8.setText("Año:");

        jLabel9.setText("Existencia:");

        libroRegBtn.setText("Guardar");
        libroRegBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                libroRegBtnActionPerformed(evt);
            }
        });

        edicionBoxReg.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15" }));

        categoriaBoxReg.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Filosofía y Psicología", "Ciencias Sociales", "Lenguas", "Tecnología (Ciencias Aplicadas)", "Ciencias Naturales y Matemáticas", "Literatura y Retórica", "Bellas Artes", "Geografía e Historia", "Area de Consulta", "Infantil" }));

        existenciaRegBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" }));

        anioRegBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019" }));

        editorialComboReg.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                editorialComboRegItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(tituloLbl))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addGap(3, 3, 3)
                                    .addComponent(autorlbl)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9)
                                    .addComponent(ISBNLbl))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ISBNTxtReg, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edicionBoxReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(categoriaBoxReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(existenciaRegBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(anioRegBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(autorTxtReg, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                            .addComponent(tituloTxtReg)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(editorialComboReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(editorialTxtReg))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(129, 129, 129)
                        .addComponent(libroRegBtn)))
                .addContainerGap(660, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ISBNTxtReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ISBNLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tituloTxtReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tituloLbl))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(autorTxtReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(autorlbl))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editorialTxtReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(editorialComboReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(edicionBoxReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(categoriaBoxReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(anioRegBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(existenciaRegBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(libroRegBtn)
                .addContainerGap(103, Short.MAX_VALUE))
        );

        librosTabbed.addTab("Registrar", jPanel1);

        jLabel16.setText("Título:");

        tituloConGenTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tituloConGenTxtKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tituloConGenTxtKeyTyped(evt);
            }
        });

        tablaConsultaGen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ISBN", "Título", "Autor", "Editorial", "Edición", "Categoría", "Año", "Existencia"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaConsultaGen.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tablaConsultaGen);
        if (tablaConsultaGen.getColumnModel().getColumnCount() > 0) {
            tablaConsultaGen.getColumnModel().getColumn(4).setMaxWidth(55);
            tablaConsultaGen.getColumnModel().getColumn(6).setMaxWidth(50);
            tablaConsultaGen.getColumnModel().getColumn(7).setMaxWidth(70);
        }

        eliminarBtnL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgab/img/trash_can.png"))); // NOI18N
        eliminarBtnL.setText("Eliminar");
        eliminarBtnL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarBtnLActionPerformed(evt);
            }
        });

        modificarBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgab/img/pencil.png"))); // NOI18N
        modificarBtn.setText("Modificar");
        modificarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificarBtnActionPerformed(evt);
            }
        });

        jLabel15.setText("Autor:");

        autorConTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                autorConTxtKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                autorConTxtKeyTyped(evt);
            }
        });

        categoriaConBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas las categorías", "Filosofía y Psicología", "Ciencias Sociales", "Lenguas", "Tecnología (Ciencias Aplicadas)", "Ciencias Naturales y Matemáticas", "Literatura y Retórica", "Bellas Artes", "Geografía e Historia", "Area de Consulta", "Infantil" }));
        categoriaConBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                categoriaConBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tituloConGenTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(autorConTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(categoriaConBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(modificarBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(eliminarBtnL)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(tituloConGenTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15)
                            .addComponent(autorConTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(categoriaConBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(eliminarBtnL)
                            .addComponent(modificarBtn))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                .addContainerGap())
        );

        librosTabbed.addTab("Consultar", jPanel2);

        jLabel2.setText("ISBN:");

        ISBNModTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ISBNModTxtKeyTyped(evt);
            }
        });

        jLabel3.setText("Título:");

        tituloModTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tituloModTxtKeyTyped(evt);
            }
        });

        autorModTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                autorModTxtKeyTyped(evt);
            }
        });

        editorialModTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                editorialModTxtKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                editorialModTxtKeyTyped(evt);
            }
        });

        actualizarBtn.setText("Actualizar");
        actualizarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actualizarBtnActionPerformed(evt);
            }
        });

        jLabel4.setText("Autor:");

        jLabel10.setText("Editorial:");

        jLabel11.setText("Edición:");

        jLabel12.setText("Categoría:");

        jLabel13.setText("Año:");

        jLabel14.setText("Existencia:");

        edicionModBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15" }));

        categoriaModBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Filosofía y Psicología", "Ciencias Sociales", "Lenguas", "Tecnología (Ciencias Aplicadas)", "Ciencias Naturales y Matemáticas", "Literatura y Retórica", "Bellas Artes", "Geografía e Historia", "Area de Consulta", "Infantil" }));

        existenciaModBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" }));

        anioModBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019" }));

        editorialComboMod.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                editorialComboModItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(actualizarBtn)
                            .addComponent(categoriaModBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(anioModBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edicionModBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(existenciaModBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel10)
                                    .addGap(25, 25, 25))
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2))
                                .addGap(15, 15, 15)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ISBNModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(autorModTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                            .addComponent(tituloModTxt)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(editorialComboMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(editorialModTxt)))))
                .addContainerGap(630, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ISBNModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(31, 31, 31)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tituloModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(autorModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(editorialComboMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editorialModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(edicionModBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(categoriaModBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(anioModBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(existenciaModBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(actualizarBtn)
                .addContainerGap(78, Short.MAX_VALUE))
        );

        librosTabbed.addTab("Modificar", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(librosTabbed)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(librosTabbed)
                .addGap(5, 5, 5))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void libroRegBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_libroRegBtnActionPerformed
        if (ISBNTxtReg.getText().length() == 10 || ISBNTxtReg.getText().length() == 13) {
            if (autorTxtReg.getText().length() > 0 && tituloTxtReg.getText().length() > 0 && ((editorialComboReg.getSelectedIndex()==0 && editorialTxtReg.getText().length() > 0) || editorialComboReg.getSelectedIndex()!=0)) {
                registrarLibro();
                consultarEditorial();
            } else {
                JOptionPane.showMessageDialog(null, "Faltan campos por completar");
            }

        } else {
            JOptionPane.showMessageDialog(null, "El ISBN debe ser de 10 o 13 digitos");
        }
    }//GEN-LAST:event_libroRegBtnActionPerformed

    private void actualizarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actualizarBtnActionPerformed
        if (autorModTxt.getText().length() > 0 && tituloModTxt.getText().length() > 0 && ((editorialComboMod.getSelectedIndex()==0 && editorialModTxt.getText().length() > 0) || editorialComboMod.getSelectedIndex()!=0)) {
            modificarLibro();
            limpiarTablaConsulta();
            consultaGeneral();
            consultarEditorial();
        } else {
            JOptionPane.showMessageDialog(null, "Faltan campos por completar");
        }

    }//GEN-LAST:event_actualizarBtnActionPerformed


    private void ISBNTxtRegKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ISBNTxtRegKeyTyped
        if (!Character.isDigit(evt.getKeyChar()) || ISBNTxtReg.getText().length() >= 13) {
            evt.consume();
        }
    }//GEN-LAST:event_ISBNTxtRegKeyTyped

    private void ISBNModTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ISBNModTxtKeyTyped
        if (!Character.isDigit(evt.getKeyChar()) || ISBNModTxt.getText().length() >= 13) {
            evt.consume();
        }
    }//GEN-LAST:event_ISBNModTxtKeyTyped

    private void tituloTxtRegKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tituloTxtRegKeyTyped

        String nombre = tituloTxtReg.getText().concat(Character.toString(evt.getKeyChar()));
        if (!val.validarNombreNum(nombre) || tituloTxtReg.getText().length() >= 50) {
            evt.consume();
        }
    }//GEN-LAST:event_tituloTxtRegKeyTyped

    private void autorTxtRegKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_autorTxtRegKeyTyped
        String nombre = autorTxtReg.getText().concat(Character.toString(evt.getKeyChar()));
        if (!val.validarNombre(nombre) || autorTxtReg.getText().length() >= 50) {
            evt.consume();
        }
    }//GEN-LAST:event_autorTxtRegKeyTyped

    private void editorialTxtRegKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_editorialTxtRegKeyTyped
        String nombre = editorialTxtReg.getText().concat(Character.toString(evt.getKeyChar()));
        if (!val.validarNombreNum(nombre) || editorialTxtReg.getText().length() >= 30) {
            evt.consume();
        }
    }//GEN-LAST:event_editorialTxtRegKeyTyped

    private void tituloConGenTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tituloConGenTxtKeyTyped
        String nombre = tituloConGenTxt.getText().concat(Character.toString(evt.getKeyChar()));
        if (!val.validarNombreNum(nombre) || tituloConGenTxt.getText().length() >= 50) {
            evt.consume();
        }
    }//GEN-LAST:event_tituloConGenTxtKeyTyped

    private void tituloModTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tituloModTxtKeyTyped
        String nombre = tituloModTxt.getText().concat(Character.toString(evt.getKeyChar()));
        if (!val.validarNombreNum(nombre) || tituloModTxt.getText().length() >= 50) {
            evt.consume();
        }
    }//GEN-LAST:event_tituloModTxtKeyTyped

    private void autorModTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_autorModTxtKeyTyped
        String nombre = autorModTxt.getText().concat(Character.toString(evt.getKeyChar()));
        if (!val.validarNombre(nombre) || autorModTxt.getText().length() >= 50) {
            evt.consume();
        }
    }//GEN-LAST:event_autorModTxtKeyTyped

    private void editorialModTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_editorialModTxtKeyTyped
        String nombre = editorialModTxt.getText().concat(Character.toString(evt.getKeyChar()));
        if (!val.validarNombreNum(nombre) || editorialModTxt.getText().length() >= 30) {
            evt.consume();
        }
    }//GEN-LAST:event_editorialModTxtKeyTyped

    private void tituloConGenTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tituloConGenTxtKeyReleased
        limpiarTablaConsulta();
        //if (!tituloConGenTxt.getText().equals("") || !autorConTxt.getText().equals("") || !categoriaConBox.getSelectedItem().toString().equals("Todas las categorías")) {
        consultaGeneral();
        //}
    }//GEN-LAST:event_tituloConGenTxtKeyReleased

    private void eliminarBtnLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarBtnLActionPerformed
        if (tablaConsultaGen.getSelectedRow() != -1) {
            try {
                int f = tablaConsultaGen.getSelectedRow();
                String id = tablaConsultaGen.getValueAt(f, 0).toString();
                int eleccion = JOptionPane.showOptionDialog(this, "¿Desea eliminar el registro?", null, 0, 0, null, botones, this);
                if (eleccion == JOptionPane.YES_OPTION) {
                    eliminarLibro(id, f);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "La fila seleccionada esta vacía");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un registro");
        }

    }//GEN-LAST:event_eliminarBtnLActionPerformed

    private void modificarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificarBtnActionPerformed
        if (tablaConsultaGen.getSelectedRow() != -1) {
            try {
                int f = tablaConsultaGen.getSelectedRow();
                String id = tablaConsultaGen.getValueAt(f, 0).toString();
                consultaIndividual(id);
                ISBNModTxt.setEditable(false);
                ISBNModTxt.setText(ISBN);
                tituloModTxt.setText(Titulo);
                autorModTxt.setText(Autor);
                //editorialModTxt.setText(Editorial);
                editorialComboMod.setSelectedItem(Editorial);
                edicionModBox.setSelectedItem(Integer.toString(Edicion));
                categoriaModBox.setSelectedItem(Categoria);
                anioModBox.setSelectedItem(Integer.toString(Anio));
                existenciaModBox.setSelectedItem(Integer.toString(Existencia));
                //JOptionPane.showMessageDialog(null, "Realice los cambios en el apartado de modificar");
                librosTabbed.setSelectedIndex(2);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "La fila seleccionada esta vacía");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un registro");
        }
    }//GEN-LAST:event_modificarBtnActionPerformed

    private void autorConTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_autorConTxtKeyTyped
        String nombre = autorConTxt.getText().concat(Character.toString(evt.getKeyChar()));
        if (!val.validarNombreNum(nombre) || autorConTxt.getText().length() >= 50) {
            evt.consume();
        }
    }//GEN-LAST:event_autorConTxtKeyTyped

    private void autorConTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_autorConTxtKeyReleased
        limpiarTablaConsulta();
        //if (!tituloConGenTxt.getText().equals("") || !autorConTxt.getText().equals("") || !categoriaConBox.getSelectedItem().toString().equals("Todas las categorías")) {
        consultaGeneral();
        //}
    }//GEN-LAST:event_autorConTxtKeyReleased

    private void categoriaConBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_categoriaConBoxItemStateChanged
        limpiarTablaConsulta();
        //if (!tituloConGenTxt.getText().equals("") || !autorConTxt.getText().equals("") || !categoriaConBox.getSelectedItem().toString().equals("Todas las categorías")) {
        consultaGeneral();
        //}
    }//GEN-LAST:event_categoriaConBoxItemStateChanged

    private void editorialModTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_editorialModTxtKeyReleased
        //consultarEditorial();
    }//GEN-LAST:event_editorialModTxtKeyReleased

    private void editorialTxtRegKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_editorialTxtRegKeyReleased
        //consultarEditorial();
    }//GEN-LAST:event_editorialTxtRegKeyReleased

    private void autorTxtRegKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_autorTxtRegKeyReleased
        consultarAutor();
    }//GEN-LAST:event_autorTxtRegKeyReleased

    private void editorialComboRegItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_editorialComboRegItemStateChanged
        repaint();
        if (editorialComboReg.getSelectedIndex() != 0) {
            editorialTxtReg.setVisible(false);
        }
        else
            editorialTxtReg.setVisible(true);
    }//GEN-LAST:event_editorialComboRegItemStateChanged

    private void editorialComboModItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_editorialComboModItemStateChanged
        //revalidate();
        repaint();
        if (editorialComboMod.getSelectedIndex() == 0) {
            editorialModTxt.setVisible(true);
        }
        else
            editorialModTxt.setVisible(false);
    }//GEN-LAST:event_editorialComboModItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ISBNLbl;
    private javax.swing.JTextField ISBNModTxt;
    private javax.swing.JTextField ISBNTxtReg;
    private javax.swing.JButton actualizarBtn;
    private javax.swing.JComboBox<String> anioModBox;
    private javax.swing.JComboBox<String> anioRegBox;
    private javax.swing.JTextField autorConTxt;
    private javax.swing.JTextField autorModTxt;
    private javax.swing.JTextField autorTxtReg;
    private javax.swing.JLabel autorlbl;
    private javax.swing.JComboBox<String> categoriaBoxReg;
    private javax.swing.JComboBox<String> categoriaConBox;
    private javax.swing.JComboBox<String> categoriaModBox;
    private javax.swing.JComboBox<String> edicionBoxReg;
    private javax.swing.JComboBox<String> edicionModBox;
    private javax.swing.JComboBox<String> editorialComboMod;
    private javax.swing.JComboBox<String> editorialComboReg;
    private javax.swing.JTextField editorialModTxt;
    private javax.swing.JTextField editorialTxtReg;
    private javax.swing.JButton eliminarBtnL;
    private javax.swing.JComboBox<String> existenciaModBox;
    private javax.swing.JComboBox<String> existenciaRegBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton libroRegBtn;
    private javax.swing.JTabbedPane librosTabbed;
    private javax.swing.JButton modificarBtn;
    private javax.swing.JTable tablaConsultaGen;
    private javax.swing.JTextField tituloConGenTxt;
    private javax.swing.JLabel tituloLbl;
    private javax.swing.JTextField tituloModTxt;
    private javax.swing.JTextField tituloTxtReg;
    // End of variables declaration//GEN-END:variables
}
