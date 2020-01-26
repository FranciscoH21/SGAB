/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgab;

import com.mxrck.autocompleter.TextAutoCompleter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.Color;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import java.util.Calendar;
import java.util.Date;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author FranciscoHernandez
 */
public class MultasPanel extends javax.swing.JPanel {

    private Connection con = null;
    private PreparedStatement pstm = null;
    private ResultSet rs = null;
    /**
     * Creates new form MultasPanel
     */

    private String IdPago;
    private String IdUsuario;
    private String IdMulta;
    private String Motivo;
    private String FechaMulta;
    private String FechaPago;
    private String Estado;
    private int CostoM;
    private String NombreU;
    private String[] botones = {"SI", "NO"};
    Validar valid = new Validar();
    private int fila;
    private static String user = "";
    private static String password = "";
    TextAutoCompleter ta;

    public MultasPanel(String user, String password) {
        initComponents();
        this.user = user;
        this.password = password;

        //eliminarBtnL.setBackground(Color.RED);
        //eliminarBtnL.setForeground(Color.WHITE);
        modificarBtn.setBackground(new Color(32, 33, 79));
        modificarBtn.setForeground(Color.WHITE);

        tablaConsMultas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        idUsModTxt.setEditable(false);
        idPagoModTxt.setEditable(false);
        tipoMultaModTxt.setEditable(false);
        fechaMultaModTxt.setEditable(false);
        idPagoRegTxt.setEditable(false);
        generarID();
        limpiarTablaConsulta();
        consultaGeneral();
        montoMultaR.setEditable(false);
        montoMultaR.setText("$15");
        ta = new TextAutoCompleter(idUsRegTxt);
    }

    public void registrarMulta() {
        //getFechaMultaMod();
        //getFechapagoMod();
        String sql = "INSERT INTO Pagos (IdPago,IdUsuario,IdMulta,FechaMulta,FechaPago,Estado) VALUES (?,?,?,?,?,?)";
        con = UConnection.getConnection(user, password);
        try {
            pstm = con.prepareStatement(sql);
            String idMul = "";
            if (jComboCoxMotivo.getSelectedItem().toString().equals("Daño")) {
                idMul = "100";
            } else if (jComboCoxMotivo.getSelectedItem().toString().equals("Retardo")) {
                idMul = "102";
            } else if (jComboCoxMotivo.getSelectedItem().toString().equals("Extravio")) {
                idMul = "101";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            //String fechaM = sdf.format(fechamultaR.getFecha());
            //String fechaP = sdf.format(fechaPagoRegCal.getDate());

            String fechaP = "";
            if (estadoBoxReg.getSelectedItem().equals("Pagada")) {
                fechaP = fechamultaR.getFecha();
            }
            pstm.setString(1, idPagoRegTxt.getText());
            pstm.setString(2, idUsRegTxt.getText());
            pstm.setString(3, idMul);
            pstm.setString(4, fechamultaR.getFecha());
            pstm.setString(5, fechaP);
            pstm.setString(6, estadoBoxReg.getSelectedItem().toString());
            int rtdo = pstm.executeUpdate();
            if (rtdo == 1) {
                JOptionPane.showMessageDialog(null, "Multa registrada");
                limpiarTxt();
                generarID();
                limpiarTablaConsulta();
                consultaGeneral();
            }
            pstm.close();
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private void consultaGeneral() {
        String usuario = nomUConTxt.getText();
        String tipo = tipoMultaConsultaBox.getSelectedItem().toString();
        if (tipo.equals("Todas las multas")) {
            tipo = "";
        }
        String est = estadoConBox.getSelectedItem().toString();
        if (est.equals("Todos")) {
            est = "";
        }
        String sql = "SELECT * FROM MultasP WHERE NombreU like '" + usuario + "%' AND Motivo like '" + tipo + "%' AND Estado like '"+est+"%'";
        con = UConnection.getConnection(user, password);
        try {
            pstm = con.prepareStatement(sql);
            //pstm = con.prepareStatement(sql1);

            fila = 0;
            int col = 0;
            rs = pstm.executeQuery();

            while (rs.next()) {
                tablaConsMultas.setValueAt(rs.getString("IdPago"), fila, col);
                col++;
                tablaConsMultas.setValueAt(rs.getString("IdUsuario"), fila, col);
                col++;
                tablaConsMultas.setValueAt(rs.getString("NombreU"), fila, col);
                col++;
                tablaConsMultas.setValueAt(rs.getString("Motivo"), fila, col);
                col++;
                tablaConsMultas.setValueAt(rs.getString("CostoM"), fila, col);
                col++;
                tablaConsMultas.setValueAt(rs.getString("FechaMulta"), fila, col);
                col++;
                tablaConsMultas.setValueAt(rs.getString("Fechapago"), fila, col);
                col++;
                tablaConsMultas.setValueAt(rs.getString("Estado"), fila, col);
                col = 0;
                if (fila == tablaConsMultas.getRowCount() - 1) {
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

    public void modificarMulta() {
        String sql = "UPDATE Pagos SET FechaPago=?, Estado=? WHERE IdPago=? and IdUsuario=?";
        con = UConnection.getConnection(user, password);
        try {
            pstm = con.prepareStatement(sql);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            //String fechaP = sdf.format(fechaPagoModCal.getDate());
            pstm.setString(1, fechaPagoP.getFecha());
            pstm.setString(2, "Pagada");
            pstm.setString(3, idPagoModTxt.getText());
            pstm.setString(4, idUsModTxt.getText());

            int rtdo = pstm.executeUpdate();
            if (rtdo == 1) {
                JOptionPane.showMessageDialog(null, "Pago registrado");
                limpiarTxtActualizar();
            }

            pstm.close();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void consultaIndividual(String IdPagoAux, String IdUsuarioAux) {
        String sql = "SELECT * FROM MultasP WHERE IdPago=? and IdUsuario=?";
        con = UConnection.getConnection(user, password);
        limpiarVariables();
        try {
            pstm = con.prepareStatement(sql);
            pstm.setString(1, IdPagoAux);
            pstm.setString(2, IdUsuarioAux);

            rs = pstm.executeQuery();
            while (rs.next()) {
                IdPago = rs.getString("IdPago");
                IdUsuario = rs.getString("IdUsuario");
                //IdMulta = rs.getString("IdMulta");
                NombreU = rs.getString("NombreU");
                Motivo = rs.getString("Motivo");
                FechaMulta = rs.getString("FechaMulta");
                FechaPago = rs.getString("Fechapago");
                Estado = rs.getString("Estado");
            }
            pstm.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void consultarIDs() {
        String sql = "SELECT IdUsuario FROM Usuarios WHERE IdUsuario like '" + idUsRegTxt.getText() + "%'";
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

    public void eliminarMulta(String idM, String idU, int row) {
        String sql = "DELETE FROM pagos WHERE IdPago=? and IdUsuario=?";
        con = UConnection.getConnection(user, password);
        try {
            pstm = con.prepareStatement(sql);
            pstm.setString(1, idM);
            pstm.setString(2, idU);

            int rtdo = pstm.executeUpdate();
            if (rtdo == 1) {
                JOptionPane.showMessageDialog(null, "Multa eliminada");
                DefaultTableModel m = (DefaultTableModel) tablaConsMultas.getModel();
                m.removeRow(row);
                m.addRow(new String[8]);
            }

            pstm.close();
        } catch (HeadlessException | SQLException e) {
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

    public void limpiarVariables() {
        IdMulta = "";
        IdUsuario = "";
        Motivo = "";
        FechaMulta = "";
        FechaPago = "";
        Estado = "";
        CostoM = 0;
    }

    public void limpiarTxt() {
        idUsRegTxt.setText("");
        idPagoRegTxt.setText("");
        jComboCoxMotivo.setSelectedIndex(0);
        //fechaMultaRegCal.setDate(null);
        //fechaPagoRegCal.setDate(null);
        estadoBoxReg.setSelectedIndex(0);
    }

    public void limpiarTxtActualizar() {
        idPagoModTxt.setText("");
        idUsModTxt.setText("");
        //fechaPagoModCal.setDate(null);
        fechaMultaModTxt.setText("");
        //estadoBoxMod.setSelectedIndex(0);
        tipoMultaModTxt.setText("");
    }

    public void limpiarTablaConsulta() {
        for (int x = 0; x < 8; x++) {
            for (int i = 0; i < fila; i++) {
                tablaConsMultas.setValueAt(null, i, x);
            }
        }
    }

    public boolean camposRegistrar() {
        boolean campos = false;
        if (idPagoRegTxt.getText().length() > 0 && idUsRegTxt.getText().length() > 0) {
            campos = true;
        }
        return campos;
    }

    public String ultimoID() {
        String sql = "SELECT MAX(IdPago) as ID FROM Pagos";
        String rtn = "";
        con = UConnection.getConnection(user, password);
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
            ultimo = "M0001";
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
            ultimo = "M" + idInicio + Integer.toString(digitos);
        }
        idPagoRegTxt.setText(ultimo);
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
        multasTabbed = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        idPagoRegTxt = new javax.swing.JTextField();
        idUsRegTxt = new javax.swing.JTextField();
        GuardarRegBtn = new javax.swing.JButton();
        jComboCoxMotivo = new javax.swing.JComboBox<>();
        estadoBoxReg = new javax.swing.JComboBox<>();
        fechamultaR = new rojeru_san.RSLabelFecha();
        montoMultaR = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        nomUConTxt = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaConsMultas = new javax.swing.JTable();
        modificarBtn = new javax.swing.JButton();
        tipoMultaConsultaBox = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        estadoConBox = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        idPagoModTxt = new javax.swing.JTextField();
        idUsModTxt = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        actulizarMulBtn = new javax.swing.JButton();
        tipoMultaModTxt = new javax.swing.JTextField();
        fechaMultaModTxt = new javax.swing.JTextField();
        fechaPagoP = new rojeru_san.RSLabelFecha();

        setPreferredSize(new java.awt.Dimension(909, 614));

        jLabel1.setFont(new java.awt.Font("Century Gothic", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("MULTAS");

        jLabel2.setText("ID Multa:");

        jLabel3.setText("ID Usuario:");

        jLabel4.setText("Tipo multa:");

        jLabel5.setText("Fecha multa:");

        jLabel6.setText("Estado:");

        idPagoRegTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                idPagoRegTxtKeyTyped(evt);
            }
        });

        idUsRegTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                idUsRegTxtKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                idUsRegTxtKeyTyped(evt);
            }
        });

        GuardarRegBtn.setText("Guardar");
        GuardarRegBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GuardarRegBtnActionPerformed(evt);
            }
        });

        jComboCoxMotivo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Daño", "Extravio", "Retardo" }));
        jComboCoxMotivo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboCoxMotivoItemStateChanged(evt);
            }
        });

        estadoBoxReg.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "En proceso", "Pagada" }));

        fechamultaR.setForeground(new java.awt.Color(0, 0, 0));
        fechamultaR.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        jLabel15.setText("Monto:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(37, 37, 37))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(21, 21, 21))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(idUsRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(idPagoRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(GuardarRegBtn)
                            .addComponent(estadoBoxReg, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jComboCoxMotivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(jLabel15)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(montoMultaR, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(fechamultaR, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(561, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(idPagoRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(idUsRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jComboCoxMotivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(montoMultaR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel5))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(fechamultaR, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(estadoBoxReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(GuardarRegBtn)
                .addContainerGap(244, Short.MAX_VALUE))
        );

        multasTabbed.addTab("Registrar", jPanel1);

        jLabel14.setText("Nombre Usuario:");

        nomUConTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nomUConTxtKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nomUConTxtKeyTyped(evt);
            }
        });

        tablaConsMultas.setModel(new javax.swing.table.DefaultTableModel(
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
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Multa", "ID Usuario", "Usuario", "Multa", "Costo", "Fecha multa", "Fecha pago", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        tablaConsMultas.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tablaConsMultas);

        modificarBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgab/img/pencil.png"))); // NOI18N
        modificarBtn.setText("Pagos");
        modificarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificarBtnActionPerformed(evt);
            }
        });

        tipoMultaConsultaBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas las multas", "Daño", "Extravio", "Retardo" }));
        tipoMultaConsultaBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                tipoMultaConsultaBoxItemStateChanged(evt);
            }
        });
        tipoMultaConsultaBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoMultaConsultaBoxActionPerformed(evt);
            }
        });

        jLabel7.setText("Estado:");

        estadoConBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "En proceso", "Pagada" }));
        estadoConBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                estadoConBoxItemStateChanged(evt);
            }
        });

        jLabel13.setText("Tipo:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 897, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nomUConTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tipoMultaConsultaBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(estadoConBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(modificarBtn)
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(nomUConTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(modificarBtn)
                    .addComponent(tipoMultaConsultaBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(estadoConBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(4, 4, 4)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        multasTabbed.addTab("Consultar", jPanel2);

        jLabel8.setText("ID Multa:");

        jLabel9.setText("ID Usuario:");

        idPagoModTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                idPagoModTxtKeyTyped(evt);
            }
        });

        idUsModTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                idUsModTxtKeyTyped(evt);
            }
        });

        jLabel10.setText("Tipo multa:");

        jLabel11.setText("Fecha multa:");

        jLabel12.setText("Fecha pago:");

        actulizarMulBtn.setText("Registrar");
        actulizarMulBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actulizarMulBtnActionPerformed(evt);
            }
        });

        fechaPagoP.setForeground(new java.awt.Color(0, 0, 0));
        fechaPagoP.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(actulizarMulBtn)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(idPagoModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(idUsModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(fechaMultaModTxt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                        .addComponent(tipoMultaModTxt, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(fechaPagoP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(565, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(idPagoModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idUsModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(tipoMultaModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(fechaMultaModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel12))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(fechaPagoP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28)
                .addComponent(actulizarMulBtn)
                .addContainerGap(262, Short.MAX_VALUE))
        );

        multasTabbed.addTab("Pagos", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(multasTabbed)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(multasTabbed))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void GuardarRegBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GuardarRegBtnActionPerformed
        if (camposRegistrar()) {
            if (valid.validarEnvioID(idPagoRegTxt.getText()) && valid.validarEnvioID(idUsRegTxt.getText())) {
                registrarMulta();
            } else {
                JOptionPane.showMessageDialog(null, "El ID debe contener 1 letra mayúscula y 4 dígitos");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Faltan campos por completar");
        }
    }//GEN-LAST:event_GuardarRegBtnActionPerformed

    private void nomUConTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomUConTxtKeyReleased
        // TODO add your handling code here:
        limpiarTablaConsulta();
        //if (!nomUConTxt.getText().equals("") || tipoMultaConsultaBox.getSelectedIndex() != 0) {
        consultaGeneral();
        //}
    }//GEN-LAST:event_nomUConTxtKeyReleased

    private void actulizarMulBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actulizarMulBtnActionPerformed
        if (idPagoModTxt.getText().length() > 0) {
            modificarMulta();
            limpiarTablaConsulta();
            consultaGeneral();
        }
        
    }//GEN-LAST:event_actulizarMulBtnActionPerformed

    private void idPagoRegTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idPagoRegTxtKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (valid.id(c) || idPagoRegTxt.getText().length() >= 5) {
            evt.consume();
        }
    }//GEN-LAST:event_idPagoRegTxtKeyTyped

    private void idUsRegTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idUsRegTxtKeyTyped
        // TODO add your handling code here:
        String nombre = idUsRegTxt.getText().concat(Character.toString(evt.getKeyChar()));
        //char c = evt.getKeyChar();
        if (!valid.IDUsuario(nombre) || idUsRegTxt.getText().length() >= 5) {
            evt.consume();
        }
    }//GEN-LAST:event_idUsRegTxtKeyTyped

    private void nomUConTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomUConTxtKeyTyped
        // TODO add your handling code here:
        String nombre = nomUConTxt.getText().concat(Character.toString(evt.getKeyChar()));
        //char c = evt.getKeyChar();
        if (!valid.validarNombre(nombre) || nomUConTxt.getText().length() >= 50) {
            evt.consume();
        }
    }//GEN-LAST:event_nomUConTxtKeyTyped

    private void idPagoModTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idPagoModTxtKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (valid.letrasNumeros(c) || idPagoModTxt.getText().length() == 5) {
            evt.consume();
        }
    }//GEN-LAST:event_idPagoModTxtKeyTyped

    private void idUsModTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idUsModTxtKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (valid.letrasNumeros(c) || idUsModTxt.getText().length() == 5) {
            evt.consume();
        }
    }//GEN-LAST:event_idUsModTxtKeyTyped

    private void modificarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificarBtnActionPerformed
        if (tablaConsMultas.getSelectedRow() != -1) {
            try {
                int f = tablaConsMultas.getSelectedRow();
                String idM = tablaConsMultas.getValueAt(f, 0).toString();
                String idU = tablaConsMultas.getValueAt(f, 1).toString();

                if (tablaConsMultas.getValueAt(f, 7).toString().equals("Pagada")) {
                    JOptionPane.showMessageDialog(null, "La multa ya fue pagada");
                } else {
                    consultaIndividual(idM, idU);
                    idPagoModTxt.setText(IdPago);
                    idUsModTxt.setText(IdUsuario);
                    tipoMultaModTxt.setText(Motivo);
                    fechaMultaModTxt.setText(FechaMulta);
                    multasTabbed.setSelectedIndex(2);
                }
                //fechaPagoModCal.setDate(convertirFecha(FechaPago));
                //EstadoModTxt.setText(Estado);
                //JOptionPane.showMessageDialog(null, "Realice los cambios en el apartado de modificar");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "La fila seleccionada esta vacía");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un registro");
        }
    }//GEN-LAST:event_modificarBtnActionPerformed

    private void tipoMultaConsultaBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_tipoMultaConsultaBoxItemStateChanged
        limpiarTablaConsulta();
//        if (!nomUConTxt.getText().equals("") || tipoMultaConsultaBox.getSelectedIndex() != 0) {
        consultaGeneral();
        //      }
    }//GEN-LAST:event_tipoMultaConsultaBoxItemStateChanged

    private void jComboCoxMotivoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboCoxMotivoItemStateChanged
        if (jComboCoxMotivo.getSelectedItem().equals("Daño")) {
            montoMultaR.setText("$15");
        }
        if (jComboCoxMotivo.getSelectedItem().equals("Extravio")) {
            montoMultaR.setText("Variable");
        }
        if (jComboCoxMotivo.getSelectedItem().equals("Retardo")) {
            montoMultaR.setText("$10");
        }
    }//GEN-LAST:event_jComboCoxMotivoItemStateChanged

    private void idUsRegTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idUsRegTxtKeyReleased
        consultarIDs();
    }//GEN-LAST:event_idUsRegTxtKeyReleased

    private void tipoMultaConsultaBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoMultaConsultaBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tipoMultaConsultaBoxActionPerformed

    private void estadoConBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_estadoConBoxItemStateChanged
        limpiarTablaConsulta();
//        if (!nomUConTxt.getText().equals("") || tipoMultaConsultaBox.getSelectedIndex() != 0) {
        consultaGeneral();
    }//GEN-LAST:event_estadoConBoxItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton GuardarRegBtn;
    private javax.swing.JButton actulizarMulBtn;
    private javax.swing.JComboBox<String> estadoBoxReg;
    private javax.swing.JComboBox<String> estadoConBox;
    private javax.swing.JTextField fechaMultaModTxt;
    private rojeru_san.RSLabelFecha fechaPagoP;
    private rojeru_san.RSLabelFecha fechamultaR;
    private javax.swing.JTextField idPagoModTxt;
    private javax.swing.JTextField idPagoRegTxt;
    private javax.swing.JTextField idUsModTxt;
    private javax.swing.JTextField idUsRegTxt;
    private javax.swing.JComboBox<String> jComboCoxMotivo;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton modificarBtn;
    private javax.swing.JTextField montoMultaR;
    private javax.swing.JTabbedPane multasTabbed;
    private javax.swing.JTextField nomUConTxt;
    private javax.swing.JTable tablaConsMultas;
    private javax.swing.JComboBox<String> tipoMultaConsultaBox;
    private javax.swing.JTextField tipoMultaModTxt;
    // End of variables declaration//GEN-END:variables
}
