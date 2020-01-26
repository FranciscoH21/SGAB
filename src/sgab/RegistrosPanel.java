/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgab;

import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.Color;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author FranciscoHernandez
 */
public class RegistrosPanel extends javax.swing.JPanel {

    private Connection con = null;
    private PreparedStatement pstm = null;
    private ResultSet rs = null;

    private String[] botones = {"SI", "NO"};
    private String IdActividad;
    private String IdUsuario;
    private String FechaR;
    private int Abono;
    private int CostoTotalA;
    private String NombreU;
    private String NombreA;
    private int fila;
    private int fila2;
    private int fila3;
    private static String user = "";
    private static String password = "";
    private String fechaAux;
    Validar val = new Validar();
    private TextAutoCompleter ta;
    private TextAutoCompleter tb;

    /**
     * Creates new form RegistrosPanel
     */
    public RegistrosPanel(String user, String password) {
        initComponents();
        this.user = user;
        this.password = password;
        con = UConnection.getConnection(user, password);

        detallesBtn.setBackground(new Color(21, 153, 66));
        detallesBtn.setForeground(Color.WHITE);

        aAbonoBtn.setBackground(new Color(32, 33, 79));
        aAbonoBtn.setForeground(Color.WHITE);

        tablaConsultaGen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaActividades.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        idRegistroAbonosTxt.setEditable(false);
        costoActividadAbonosTxt.setEditable(false);
        cantidadPagadaAbonosTxt.setEditable(false);
        usuarioDetallesTxt.setEditable(false);
        actividadDetallesTxt.setEditable(false);
        idRegistrosTxtReg.setEditable(false);
        jButton1.setOpaque(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setBorderPainted(false);
        jButton1.setForeground(Color.WHITE);
        generarID();
        limpiarTablaConsulta();
        consultaGeneral();
        ta = new TextAutoCompleter(idUsuarioRegTxt);
        tb = new TextAutoCompleter(idActividadRegTxt);
    }

    public void agregarRegistro() {
        String sql = "INSERT INTO Registros (IdRegistro,IdActividad,IdUsuario,FechaR,SumAbono) VALUES (?,?,?,?,?)";
        //con = UConnection.getConnection(user,password);
        try {
            pstm = con.prepareStatement(sql);
            //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            //String fecha = sdf.format(fechaRegCal.getDate());
            

            pstm.setString(1, idRegistrosTxtReg.getText());
            pstm.setString(2, idActividadRegTxt.getText());
            pstm.setString(3, idUsuarioRegTxt.getText());
            pstm.setString(4, fechaAgregarR.getFecha());
            pstm.setInt(5, 0);

            int rtdo = pstm.executeUpdate();
            if (rtdo == 1) {
                JOptionPane.showMessageDialog(null, "Registro agregado");
                limpiarTxt();
                generarID();
                limpiarTablaConsulta();
                //if (!actividadTxt.getText().equals("")) {
                consultaGeneral();
            }
            pstm.close();
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void agregarAbono() {
        String sql = "INSERT INTO Detalle_Registros (IdRegistro,Abono,FechaAbonoR) VALUES (?,?,?)";
        //con = UConnection.getConnection(user,password);
        try {
            pstm = con.prepareStatement(sql);
            //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            //String fecha = sdf.format(fechaAbonosTxt.getDate());

            pstm.setString(1, idRegistroAbonosTxt.getText());
            pstm.setString(2, abonoAbonosTxt.getText());
            pstm.setString(3, fechaAbonoA.getFecha());

            int rtdo = pstm.executeUpdate();
            if (rtdo == 1) {
                JOptionPane.showMessageDialog(null, "Abono registrado");
                //limpiarTxt();
            }
            pstm.close();
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void consultaGeneral() {
        String actividad = actividadTxt.getText();
        String usuario = nomUsuarioTextCon.getText();
        String sql = "SELECT R.IdRegistro,NombreU,NombreA,CostoTotalA,SumAbono,FechaR FROM "
                + "Registros AS R INNER JOIN Actividades AS A ON "
                + "R.IdActividad=A.IdActividad INNER JOIN Usuarios AS U ON "
                + "R.IdUsuario=U.IdUsuario WHERE NombreA like '" + actividad + "%' AND NombreU like '"+usuario+"%'";
        //con = UConnection.getConnection(user,password);
        try {
            pstm = con.prepareStatement(sql);

            fila = 0;
            int col = 0;
            rs = pstm.executeQuery();

            while (rs.next()) {
                tablaConsultaGen.setValueAt(rs.getString("IdRegistro"), fila, col);
                col++;
                tablaConsultaGen.setValueAt(rs.getString("NombreU"), fila, col);
                col++;
                tablaConsultaGen.setValueAt(rs.getString("NombreA"), fila, col);
                col++;
                tablaConsultaGen.setValueAt(rs.getInt("CostoTotalA"), fila, col);
                col++;
                tablaConsultaGen.setValueAt(rs.getInt("SumAbono"), fila, col);
                col++;
                tablaConsultaGen.setValueAt(rs.getString("FechaR"), fila, col);
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

    public void consultarDetalles(String idRegistro) {
        String sql = "SELECT Abono,FechaAbonoR FROM Detalle_Registros "
                + "WHERE IdRegistro=?";
        //con = UConnection.getConnection(user,password);
        try {
            pstm = con.prepareStatement(sql);
            pstm.setString(1, idRegistro);

            fila3 = 0;
            int col = 0;
            rs = pstm.executeQuery();

            while (rs.next()) {
                tablaDetalles.setValueAt(rs.getString("Abono"), fila3, col);
                col++;
                tablaDetalles.setValueAt(rs.getString("FechaAbonoR"), fila3, col);
                col = 0;
                if (fila3 == tablaDetalles.getRowCount() - 1) {
                    break;
                }
                fila3++;
            }
            pstm.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    /*public void modificarRegistro() {
        String sql = "UPDATE Registros SET Abono=?,FechaR=? WHERE IdUsuario=? AND IdActividad=?";
        //con = UConnection.getConnection(user,password);
        try {
            pstm = con.prepareStatement(sql);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String fecha = sdf.format(fechaModCal.getDate());

            pstm.setString(1, abonoModTxt.getText());
            pstm.setString(2, fecha);
            pstm.setString(3, idUsuarioModTxt.getText());
            pstm.setString(4, idActividadModTxt.getText());

            int rtdo = pstm.executeUpdate();
            if (rtdo == 1) {
                JOptionPane.showMessageDialog(null, "Libro actualizado");
            }
            pstm.close();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }*/
    public void consultaIndividual(String IdUsuAux, String IdActAux) {
        String sql = "SELECT R.IdUsuario,R.IdActividad,NombreU,NombreA,CostoTotalA,Abono,FechaR FROM "
                + "Registros AS R INNER JOIN Actividades AS A ON "
                + "R.IdActividad=A.IdActividad INNER JOIN Usuarios AS U ON "
                + "R.IdUsuario=U.IdUsuario WHERE R.IdUsuario=? AND R.IdActividad=?";
        //con = UConnection.getConnection();
        limpiarVariables();
        try {
            pstm = con.prepareStatement(sql);
            pstm.setString(1, IdUsuAux);
            pstm.setString(2, IdActAux);

            rs = pstm.executeQuery();
            while (rs.next()) {
                IdUsuario = rs.getString("IdUsuario");
                IdActividad = rs.getString("IdActividad");
                NombreU = rs.getString("NombreU");
                NombreA = rs.getString("NombreA");
                CostoTotalA = rs.getInt("CostoTotalA");
                Abono = rs.getInt("Abono");
                FechaR = rs.getString("FechaR");
            }
            pstm.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void actualizarAbonoTotal() {
        String sql = "UPDATE Registros SET SumAbono=SumAbono+? WHERE IdRegistro=?";
        //con = UConnection.getConnection(user,password);
        try {
            pstm = con.prepareStatement(sql);
            pstm.setString(1, abonoAbonosTxt.getText());
            pstm.setString(2, idRegistroAbonosTxt.getText());

            int rtdo = pstm.executeUpdate();
            if (rtdo == 1) {
                limpiarTxtAbonos();
            }
            pstm.close();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void eliminarRegistro(String idU, String idA, int row) {
        String sql = "DELETE FROM Registros WHERE IdUsuario=? AND IdActividad=?";
        //con = UConnection.getConnection();
        try {
            pstm = con.prepareStatement(sql);
            pstm.setString(1, idU);
            pstm.setString(2, idA);

            int rtdo = pstm.executeUpdate();
            if (rtdo == 1) {
                JOptionPane.showMessageDialog(null, "Registro Eliminado");
                DefaultTableModel m = (DefaultTableModel) tablaConsultaGen.getModel();
                m.removeRow(row);
                m.addRow(new String[8]);
            }

            pstm.close();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public int consultarCapacidad(String ACTIVIDAD) {
        String sql = "SELECT CapacidadUsuarios From Actividades WHERE IdActividad=?";
        int rtn = 0;
        try {
            pstm = con.prepareStatement(sql);
            pstm.setString(1, ACTIVIDAD);

            rs = pstm.executeQuery();
            while (rs.next()) {
                rtn = rs.getInt("CapacidadUsuarios");
            }
            pstm.close();
            rs.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return rtn;
    }

    public int consultarRegistros(String ACTIVIDAD) {
        String sql = "SELECT COUNT(*) AS Num From Registros WHERE IdActividad=?";
        int rtn = 0;
        try {
            pstm = con.prepareStatement(sql);
            pstm.setString(1, ACTIVIDAD);

            rs = pstm.executeQuery();
            while (rs.next()) {
                rtn = rs.getInt("Num");
            }
            pstm.close();
            rs.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return rtn;
    }

    public int verificarRegistro(String ACTIVIDAD, String USUARIO) {
        String sql = "SELECT COUNT(*) AS Num From Registros WHERE IdActividad=? AND IdUsuario=?";
        int rtn = 0;
        try {
            pstm = con.prepareStatement(sql);
            pstm.setString(1, ACTIVIDAD);
            pstm.setString(2, USUARIO);

            rs = pstm.executeQuery();
            while (rs.next()) {
                rtn = rs.getInt("Num");
            }
            pstm.close();
            rs.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return rtn;
    }
    
    public void consultarIDs() {
        String sql = "SELECT IdUsuario FROM Usuarios WHERE IdUsuario like '" +idUsuarioRegTxt.getText()+"%'";
        con = UConnection.getConnection(user, password);
        limpiarVariables();
        try {
            pstm = con.prepareStatement(sql);
            //pstm.setString(1, ISBNAux);

            rs = pstm.executeQuery();
            ta.removeAllItems();
            while (rs.next()) {
                ta.addItem(rs.getString("IdUsuario"));
            }
            pstm.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    
    public void consultarIDAct() {
        String sql = "SELECT IdActividad FROM Actividades WHERE IdActividad like '" +idActividadRegTxt.getText()+"%'";
        con = UConnection.getConnection(user, password);
        limpiarVariables();
        try {
            pstm = con.prepareStatement(sql);
            //pstm.setString(1, ISBNAux);

            rs = pstm.executeQuery();
            tb.removeAllItems();
            while (rs.next()) {
                tb.addItem(rs.getString("IdActividad"));
            }
            pstm.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    /*public int consultarCostoTotal (String idActividad) {
        String sql = "SELECT CostoTotalA From Registros WHERE IdRegistro=?";
        int rtn = 0;
        try {
            pstm = con.prepareStatement(sql);
            pstm.setString(1, ABONO);

            rs = pstm.executeQuery();
            while (rs.next()) {
                rtn = rs.getInt("SumAbono");
            }
            pstm.close();
            rs.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return rtn;
    }
    public int consultarAbonoTotal(String idRegistro) {
        String sql = "SELECT SumAbono From Registros WHERE IdRegistro=?";
        int rtn = 0;
        try {
            pstm = con.prepareStatement(sql);
            pstm.setString(1, idRegistro);

            rs = pstm.executeQuery();
            while (rs.next()) {
                rtn = rs.getInt("SumAbono");
            }
            pstm.close();
            rs.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return rtn;
    }*/
    public void limpiarTablaConsulta() {
        for (int x = 0; x < 6; x++) {
            for (int i = 0; i < fila + 1; i++) {
                tablaConsultaGen.setValueAt(null, i, x);
            }
        }
    }

    public void limpiarTablaDetalles() {
        for (int x = 0; x < 2; x++) {
            for (int i = 0; i < fila3; i++) {
                tablaDetalles.setValueAt(null, i, x);
            }
        }
    }

    public void limpiarTablaActividades() {
        for (int x = 0; x < 2; x++) {
            for (int i = 0; i < fila2; i++) {
                tablaActividades.setValueAt(null, i, x);
            }
        }
    }

    public void limpiarTxt() {
        idRegistrosTxtReg.setText("");
        idUsuarioRegTxt.setText("");
        idActividadRegTxt.setText("");
        //fechaRegCal.setDate(null);
    }

    public void limpiarTxtAbonos() {
        idRegistroAbonosTxt.setText("");
        abonoAbonosTxt.setText("");
        cantidadPagadaAbonosTxt.setText("");
        costoActividadAbonosTxt.setText("");
        //fechaAbonosTxt.setDate(null);
    }

    public boolean camposRegistrar() {
        boolean campos = false;
        if (idUsuarioRegTxt.getText().length() > 0 && idActividadRegTxt.getText().length() > 0 && idRegistrosTxtReg.getText().length() > 0) {
            campos = true;
        }
        return campos;
    }

    public boolean camposAbonos() {
        boolean campos = false;
        if (abonoAbonosTxt.getText().length() > 0) {
            campos = true;
        }
        return campos;
    }

    public void consultarActividades() {
        String nombre = actividadBusTxt.getText();
        String sql = "SELECT IdActividad,NombreA FROM Actividades WHERE NombreA like '" + nombre + "%'";
        con = UConnection.getConnection(user, password);
        try {
            pstm = con.prepareStatement(sql);

            fila2 = 0;
            int col = 0;
            rs = pstm.executeQuery();

            while (rs.next()) {
                tablaActividades.setValueAt(rs.getString("IdActividad"), fila2, col);
                col++;
                tablaActividades.setValueAt(rs.getString("NombreA"), fila2, col);
                col = 0;
                if (fila2 == tablaActividades.getRowCount() - 1) {
                    break;
                }
                fila2++;
            }
            pstm.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public Date convertirFecha(String fecha) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = formatter.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String ultimoID() {
        String sql = "SELECT MAX(IdRegistro) as ID FROM Registros";
        String rtn = "";
        //con = UConnection.getConnection(user, password);
        try {
            pstm = con.prepareStatement(sql);

            rs = pstm.executeQuery();
            while (rs.next()) {
                rtn = rs.getString("ID");

            }
            pstm.close();
            rs.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return rtn;
    }

    public void generarID() {
        String ultimo = ultimoID();
        if (ultimo == null) {
            ultimo = "R0001";
        } else {
            String idInicio = "";
            ultimo = ultimo.substring(1);
            int digitos = Integer.parseInt(ultimo);
            digitos++;
            if (Integer.toString(digitos).length() == 1) {
                idInicio = "000";
            }
            if (Integer.toString(digitos).length() == 2) {
                idInicio = "00";
            }
            if (Integer.toString(digitos).length() == 3) {
                idInicio = "0";
            }
            ultimo = "R" + idInicio + Integer.toString(digitos);
        }
        idRegistrosTxtReg.setText(ultimo);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        registrosTabbed = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        idUsuarioRegTxt = new javax.swing.JTextField();
        idActividadRegTxt = new javax.swing.JTextField();
        guardarBtn = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        actividadBusTxt = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaActividades = new javax.swing.JTable();
        idRegistrosTxtReg = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        fechaAgregarR = new rojeru_san.RSLabelFecha();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        actividadTxt = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaConsultaGen = new javax.swing.JTable();
        detallesBtn = new javax.swing.JButton();
        aAbonoBtn = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        nomUsuarioTextCon = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaDetalles = new javax.swing.JTable();
        actividadDetallesTxt = new javax.swing.JTextField();
        usuarioDetallesTxt = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        idRegistroAbonosTxt = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        abonoAbonosTxt = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        registrarAbonoBtn = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        costoActividadAbonosTxt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cantidadPagadaAbonosTxt = new javax.swing.JTextField();
        fechaAbonoA = new rojeru_san.RSLabelFecha();
        jLabel1 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(909, 614));

        jLabel2.setText("ID Usuario:");

        jLabel3.setText("ID Actividad:");

        jLabel5.setText("Fecha:");

        idUsuarioRegTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                idUsuarioRegTxtKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                idUsuarioRegTxtKeyTyped(evt);
            }
        });

        idActividadRegTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                idActividadRegTxtKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                idActividadRegTxtKeyTyped(evt);
            }
        });

        guardarBtn.setText("Guardar");
        guardarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarBtnActionPerformed(evt);
            }
        });

        jLabel11.setText("Nombre Actividad:");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgab/img/add.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        actividadBusTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                actividadBusTxtKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                actividadBusTxtKeyTyped(evt);
            }
        });

        tablaActividades.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ID Actividad", "Nombre"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaActividades.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tablaActividades);

        idRegistrosTxtReg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                idRegistrosTxtRegKeyTyped(evt);
            }
        });

        jLabel12.setText("ID Registro");

        fechaAgregarR.setForeground(new java.awt.Color(0, 0, 0));
        fechaAgregarR.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabel2))
                            .addComponent(jLabel12)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3))
                        .addGap(45, 45, 45)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(idRegistrosTxtReg, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(idUsuarioRegTxt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(fechaAgregarR, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(idActividadRegTxt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(guardarBtn)
                        .addGap(71, 71, 71)))
                .addGap(44, 44, 44)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(actividadBusTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(137, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(actividadBusTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(idRegistrosTxtReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(idUsuarioRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(idActividadRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3))
                            .addComponent(jButton1))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fechaAgregarR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(jLabel5)))
                        .addGap(40, 40, 40)
                        .addComponent(guardarBtn)))
                .addContainerGap(219, Short.MAX_VALUE))
        );

        registrosTabbed.addTab("Agregar", jPanel1);

        jLabel10.setText("Nombre Actividad:");

        actividadTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                actividadTxtKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                actividadTxtKeyTyped(evt);
            }
        });

        tablaConsultaGen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID Registro", "Usuario", "Actividad", "Costo Total", "Cantidad Pagada", "Fecha"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaConsultaGen.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tablaConsultaGen);

        detallesBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgab/img/view_mode_list.png"))); // NOI18N
        detallesBtn.setText("Detalles");
        detallesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detallesBtnActionPerformed(evt);
            }
        });

        aAbonoBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgab/img/add.png"))); // NOI18N
        aAbonoBtn.setText("Abono");
        aAbonoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aAbonoBtnActionPerformed(evt);
            }
        });

        jLabel15.setText("Nombre Usuario:");

        nomUsuarioTextCon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nomUsuarioTextConKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nomUsuarioTextConKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 864, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(actividadTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nomUsuarioTextCon)
                        .addGap(18, 18, 18)
                        .addComponent(detallesBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(aAbonoBtn)
                        .addGap(11, 11, 11)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(actividadTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(detallesBtn)
                    .addComponent(aAbonoBtn)
                    .addComponent(jLabel15)
                    .addComponent(nomUsuarioTextCon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        registrosTabbed.addTab("Consultar", jPanel2);

        tablaDetalles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Abono", "Fecha"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaDetalles.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tablaDetalles);

        jLabel8.setText("Actividad:");

        jLabel9.setText("Usuario:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(actividadDetallesTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usuarioDetallesTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(266, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(actividadDetallesTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(usuarioDetallesTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                .addContainerGap())
        );

        registrosTabbed.addTab("Detalles", jPanel4);

        jLabel4.setText("ID Registro:");

        jLabel13.setText("Abono: $");

        abonoAbonosTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                abonoAbonosTxtKeyTyped(evt);
            }
        });

        jLabel14.setText("Fecha:");

        registrarAbonoBtn.setText("Registrar");
        registrarAbonoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registrarAbonoBtnActionPerformed(evt);
            }
        });

        jLabel6.setText("Costo Actividad:");

        jLabel7.setText("Cantidad Pagada:");

        fechaAbonoA.setForeground(new java.awt.Color(0, 0, 0));
        fechaAbonoA.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(registrarAbonoBtn)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(abonoAbonosTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                                    .addComponent(fechaAbonoA, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                                .addGap(146, 146, 146)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel6))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(costoActividadAbonosTxt)
                                    .addComponent(cantidadPagadaAbonosTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(idRegistroAbonosTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(335, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(idRegistroAbonosTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(28, 28, 28)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(abonoAbonosTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(costoActividadAbonosTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel14))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cantidadPagadaAbonosTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(fechaAbonoA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(47, 47, 47)
                .addComponent(registrarAbonoBtn)
                .addContainerGap(277, Short.MAX_VALUE))
        );

        registrosTabbed.addTab("Abonos", jPanel5);

        jLabel1.setFont(new java.awt.Font("Century Gothic", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("REGISTROS");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(registrosTabbed)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(registrosTabbed))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void actividadTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_actividadTxtKeyReleased
        limpiarTablaConsulta();
        //if (!actividadTxt.getText().equals("")) {
        consultaGeneral();
        //}
    }//GEN-LAST:event_actividadTxtKeyReleased

    private void actividadBusTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_actividadBusTxtKeyReleased
        limpiarTablaActividades();
        if (!actividadBusTxt.getText().equals("")) {
            consultarActividades();
        }
    }//GEN-LAST:event_actividadBusTxtKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (tablaActividades.getSelectedRow() != -1) {
            try {
                int f = tablaActividades.getSelectedRow();
                String id = tablaActividades.getValueAt(f, 0).toString();
                idActividadRegTxt.setText(id);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "La fila seleccionada esta vacía");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un registro");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void guardarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarBtnActionPerformed
        if (camposRegistrar()) {
            if (val.validarEnvioID(idActividadRegTxt.getText()) && val.validarEnvioID(idRegistrosTxtReg.getText()) && val.validarEnvioID(idUsuarioRegTxt.getText())) {
                int c = consultarCapacidad(idActividadRegTxt.getText());
                int r = consultarRegistros(idActividadRegTxt.getText());
                if (r < c) {
                    if (verificarRegistro(idActividadRegTxt.getText(), idUsuarioRegTxt.getText()) == 0) {
                        agregarRegistro();
                    } else {
                        JOptionPane.showMessageDialog(null, "El usuario ya está registrado en la actividad");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "La capacidad de usuarios para esa actividad llegó a su límite");
                }
            } else {
                JOptionPane.showMessageDialog(null, "El ID debe contener 1 letra mayúscula y 4 dígitos");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Faltan campos por completar");
        }

    }//GEN-LAST:event_guardarBtnActionPerformed

    private void aAbonoBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aAbonoBtnActionPerformed
        if (tablaConsultaGen.getSelectedRow() != -1) {
            try {
                int f = tablaConsultaGen.getSelectedRow();
                String idR = tablaConsultaGen.getValueAt(f, 0).toString();
                String costoAct = tablaConsultaGen.getValueAt(f, 3).toString();
                String abonoTotal = tablaConsultaGen.getValueAt(f, 4).toString();
                fechaAux = tablaConsultaGen.getValueAt(f, 5).toString();
                if (costoAct.equals(abonoTotal)) {
                    JOptionPane.showMessageDialog(null, "La actividad ya fue pagada");
                } else {
                    idRegistroAbonosTxt.setText(idR);
                    costoActividadAbonosTxt.setText(costoAct);
                    cantidadPagadaAbonosTxt.setText(abonoTotal);
                    //JOptionPane.showMessageDialog(null, "Consulte el apartado Abonos");
                    registrosTabbed.setSelectedIndex(3);
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "La fila seleccionada esta vacía");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un registro");
        }
    }//GEN-LAST:event_aAbonoBtnActionPerformed

    private void registrarAbonoBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registrarAbonoBtnActionPerformed
        if (camposAbonos()) {
            if (true) {
                int c = Integer.parseInt(costoActividadAbonosTxt.getText());
                int r = Integer.parseInt(cantidadPagadaAbonosTxt.getText());
                r += Integer.parseInt(abonoAbonosTxt.getText());
                if (r <= c) {
                    agregarAbono();
                    actualizarAbonoTotal();
                    limpiarTablaConsulta();
                    consultaGeneral();
                    limpiarTablaDetalles();
                    if (tablaConsultaGen.getSelectedRow() != -1) {
                        try {
                            int f = tablaConsultaGen.getSelectedRow();
                            String idR = tablaConsultaGen.getValueAt(f, 0).toString();
                            String usu = tablaConsultaGen.getValueAt(f, 1).toString();
                            String act = tablaConsultaGen.getValueAt(f, 2).toString();

                            limpiarTablaDetalles();
                            consultarDetalles(idR);
                            usuarioDetallesTxt.setText(usu);
                            actividadDetallesTxt.setText(act);
                        } catch (Exception e) {
                        }

                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Excedió la cantidad total a pagar");
                }
            } else {
                JOptionPane.showMessageDialog(null, "La fecha esta mal");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Faltan campos por completar");
        }

    }//GEN-LAST:event_registrarAbonoBtnActionPerformed

    private void detallesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detallesBtnActionPerformed
        if (tablaConsultaGen.getSelectedRow() != -1) {
            try {
                int f = tablaConsultaGen.getSelectedRow();
                String idR = tablaConsultaGen.getValueAt(f, 0).toString();
                String usu = tablaConsultaGen.getValueAt(f, 1).toString();
                String act = tablaConsultaGen.getValueAt(f, 2).toString();

                limpiarTablaDetalles();
                consultarDetalles(idR);
                usuarioDetallesTxt.setText(usu);
                actividadDetallesTxt.setText(act);
                //JOptionPane.showMessageDialog(null, "Consulte el apartado Detalles");
                registrosTabbed.setSelectedIndex(2);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "La fila seleccionada esta vacía");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un registro");
        }
    }//GEN-LAST:event_detallesBtnActionPerformed

    private void idRegistrosTxtRegKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idRegistrosTxtRegKeyTyped
        if (val.id(evt.getKeyChar()) || idRegistrosTxtReg.getText().length() >= 5) {
            evt.consume();
        }
    }//GEN-LAST:event_idRegistrosTxtRegKeyTyped

    private void idUsuarioRegTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idUsuarioRegTxtKeyTyped
        String nombre = idUsuarioRegTxt.getText().concat(Character.toString(evt.getKeyChar()));
        if (!val.IDUsuario(nombre) || idUsuarioRegTxt.getText().length() >= 5) {
            evt.consume();
        }
    }//GEN-LAST:event_idUsuarioRegTxtKeyTyped

    private void idActividadRegTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idActividadRegTxtKeyTyped
        String nombre = idActividadRegTxt.getText().concat(Character.toString(evt.getKeyChar()));
        if (!val.IDActividad(nombre)|| idActividadRegTxt.getText().length() >= 5) {
            evt.consume();
        }
    }//GEN-LAST:event_idActividadRegTxtKeyTyped

    private void actividadBusTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_actividadBusTxtKeyTyped
        String nombre = actividadBusTxt.getText().concat(Character.toString(evt.getKeyChar()));
        if (!val.validarNombreNum(nombre) || actividadBusTxt.getText().length() >= 40) {
            evt.consume();
        }
    }//GEN-LAST:event_actividadBusTxtKeyTyped

    private void actividadTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_actividadTxtKeyTyped
        String nombre = actividadTxt.getText().concat(Character.toString(evt.getKeyChar()));
        if (!val.validarNombreNum(nombre) || actividadTxt.getText().length() >= 40) {
            evt.consume();
        }
    }//GEN-LAST:event_actividadTxtKeyTyped

    private void abonoAbonosTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_abonoAbonosTxtKeyTyped
        if (!Character.isDigit(evt.getKeyChar()) || abonoAbonosTxt.getText().length() >= 5) {
            evt.consume();
        }
    }//GEN-LAST:event_abonoAbonosTxtKeyTyped

    private void idUsuarioRegTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idUsuarioRegTxtKeyReleased
        consultarIDs();
    }//GEN-LAST:event_idUsuarioRegTxtKeyReleased

    private void idActividadRegTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idActividadRegTxtKeyReleased
       consultarIDAct();
    }//GEN-LAST:event_idActividadRegTxtKeyReleased

    private void nomUsuarioTextConKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomUsuarioTextConKeyReleased
        limpiarTablaConsulta();
        //if (!actividadTxt.getText().equals("")) {
        consultaGeneral();
    }//GEN-LAST:event_nomUsuarioTextConKeyReleased

    private void nomUsuarioTextConKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomUsuarioTextConKeyTyped
        String nombre = nomUsuarioTextCon.getText().concat(Character.toString(evt.getKeyChar()));
        if (!val.validarNombre(nombre) || nomUsuarioTextCon.getText().length() >= 50) {
            evt.consume();
        }
    }//GEN-LAST:event_nomUsuarioTextConKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aAbonoBtn;
    private javax.swing.JTextField abonoAbonosTxt;
    private javax.swing.JTextField actividadBusTxt;
    private javax.swing.JTextField actividadDetallesTxt;
    private javax.swing.JTextField actividadTxt;
    private javax.swing.JTextField cantidadPagadaAbonosTxt;
    private javax.swing.JTextField costoActividadAbonosTxt;
    private javax.swing.JButton detallesBtn;
    private rojeru_san.RSLabelFecha fechaAbonoA;
    private rojeru_san.RSLabelFecha fechaAgregarR;
    private javax.swing.JButton guardarBtn;
    private javax.swing.JTextField idActividadRegTxt;
    private javax.swing.JTextField idRegistroAbonosTxt;
    private javax.swing.JTextField idRegistrosTxtReg;
    private javax.swing.JTextField idUsuarioRegTxt;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField nomUsuarioTextCon;
    private javax.swing.JButton registrarAbonoBtn;
    private javax.swing.JTabbedPane registrosTabbed;
    private javax.swing.JTable tablaActividades;
    private javax.swing.JTable tablaConsultaGen;
    private javax.swing.JTable tablaDetalles;
    private javax.swing.JTextField usuarioDetallesTxt;
    // End of variables declaration//GEN-END:variables

    private void limpiarVariables() {
        IdActividad = "";
        IdUsuario = "";
        FechaR = "";
        Abono = 0;
        NombreA = "";
        NombreU = "";
        CostoTotalA = 0;
        fechaAux = "";
    }
}
