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
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class UsuariosPanel extends javax.swing.JPanel {

    private Connection con = null;
    private PreparedStatement pstm = null;
    private ResultSet rs = null;

    private String IdUsuario;
    private String NombreU;
    private int Edad;
    private String Sexo;
    private String Escolaridad;
    private String Discapacidad;
    private String CP;
    private String Colonia;
    private String Calle;
    private int Numero;
    private String Telefono;
    private String[] botones = {"Si", "No"};
    Validar valid = new Validar();
    private int fila;
    private static String user = "";
    private static String password = "";
    TextAutoCompleter codp;
    TextAutoCompleter col;
    TextAutoCompleter calle;

    public UsuariosPanel(String user, String password) {
        initComponents();
        this.user = user;
        this.password = password;
        con = UConnection.getConnection(user, password);

        eliminarBtnL.setBackground(Color.RED);
        eliminarBtnL.setForeground(Color.WHITE);

        modificarBtn.setBackground(new Color(32, 33, 79));
        modificarBtn.setForeground(Color.WHITE);

        TablaConsultaGenUs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        IdModUsTxt.setEditable(false);
        IdUsRegTxt.setEditable(false);
        generarID();
        limpiarTablaCon();
        consultaGeneral();
        codp = new TextAutoCompleter(CpUsRegTxt);
        col = new TextAutoCompleter(ColoniaUSRegTxt);
        calle = new TextAutoCompleter(CalleUsRegTxt);
    }

    public void registrarUsuario() {
        getSexo();
        String sql = "INSERT INTO Usuarios (IdUsuario,NombreU,Edad,Sexo,Escolaridad,Discapacidad,Telefono,CP,Colonia,Calle,Numero) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        //con = UConnection.getConnection();
        try {
            pstm = con.prepareStatement(sql);

            pstm.setString(1, IdUsRegTxt.getText());
            pstm.setString(2, NomUsRegTxt.getText());
            pstm.setInt(3, (int) EdadUsRegTxt.getValue());
            pstm.setString(4, Sexo);
            pstm.setString(5, jComboEsco.getSelectedItem().toString());
            pstm.setString(6, jComboBoxDisc.getSelectedItem().toString());
            pstm.setString(7, TelUsRegTxt.getText());
            pstm.setInt(8, Integer.parseInt(CpUsRegTxt.getText()));
            pstm.setString(9, ColoniaUSRegTxt.getText());
            pstm.setString(10, CalleUsRegTxt.getText());
            pstm.setString(11, NumUsRegTxt.getText());
            int rtdo = pstm.executeUpdate();
            if (rtdo == 1) {
                JOptionPane.showMessageDialog(null, "Usuario registrado");
                limpiarTxt();
                generarID();
                limpiarTablaCon();
                consultaGeneral();
            }
            pstm.close();
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void consultaGeneral() {
        String nom = nomConsUsTxt.getText();
        String esc = escolaridadConsultaBox.getSelectedItem().toString();
        String disc = discapacidadConsultaBox.getSelectedItem().toString();
        if (esc.equals("Todos los niveles")) {
            esc = "";
        }
        if (disc.equals("Sin filtro")) {
            disc = "";
        }
        String sql = "SELECT * FROM Usuarios WHERE NombreU like '" + nom + "%' AND Escolaridad like '" + esc + "%' AND Discapacidad like '" + disc + "%'";
        //con = UConnection.getConnection();
        try {
            pstm = con.prepareStatement(sql);

            fila = 0;
            int col = 0;
            rs = pstm.executeQuery();

            while (rs.next()) {
                TablaConsultaGenUs.setValueAt(rs.getString("IdUsuario"), fila, col);
                col++;
                TablaConsultaGenUs.setValueAt(rs.getString("NombreU"), fila, col);
                col++;
                TablaConsultaGenUs.setValueAt(rs.getString("Edad"), fila, col);
                col++;
                TablaConsultaGenUs.setValueAt(rs.getString("Sexo"), fila, col);
                col++;
                TablaConsultaGenUs.setValueAt(rs.getString("Escolaridad"), fila, col);
                col++;
                TablaConsultaGenUs.setValueAt(rs.getString("Discapacidad"), fila, col);
                col++;
                TablaConsultaGenUs.setValueAt(rs.getString("Telefono"), fila, col);
                col++;
                TablaConsultaGenUs.setValueAt(rs.getString("CP"), fila, col);
                col++;
                TablaConsultaGenUs.setValueAt(rs.getString("Colonia"), fila, col);
                col++;
                TablaConsultaGenUs.setValueAt(rs.getString("Calle"), fila, col);
                col++;
                TablaConsultaGenUs.setValueAt(rs.getString("Numero"), fila, col);
                col = 0;
                if (fila == TablaConsultaGenUs.getRowCount() - 1) {
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

    public void modificarUsuario() {
        //getSexo();
        String sql = "UPDATE Usuarios SET NombreU=?,Edad=?,Sexo=?,Escolaridad=?,Discapacidad=?,Telefono=?,CP=?,Colonia=?,Calle=?,Numero=? WHERE IdUsuario=?";
        //con = UConnection.getConnection();
        try {
            pstm = con.prepareStatement(sql);
            pstm.setString(1, nomModTxt.getText());
            pstm.setInt(2, (int) edadModTxt.getValue());
            pstm.setString(3, getSexoMod());
            pstm.setString(4, EscMod.getSelectedItem().toString());
            pstm.setString(5, DicMod.getSelectedItem().toString());
            pstm.setString(6, TelModTxt.getText());
            pstm.setInt(7, Integer.parseInt(cpModtxt.getText()));
            pstm.setString(8, coloniaModTxt.getText());
            pstm.setString(9, calleModTxt.getText());
            pstm.setInt(10, Integer.parseInt(numeroModTxt.getText()));
            pstm.setString(11, IdModUsTxt.getText());

            int rtdo = pstm.executeUpdate();
            if (rtdo == 1) {
                JOptionPane.showMessageDialog(null, "Usuario actualizado");
                limpiarTxtActualizar();
            }

            pstm.close();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void consultaIndividual(String IdUsuarioAux) {
        String sql = "SELECT * FROM Usuarios WHERE IdUsuario=?";
        //con = UConnection.getConnection();
        limpiarVariables();
        try {
            pstm = con.prepareStatement(sql);
            pstm.setString(1, IdUsuarioAux);

            rs = pstm.executeQuery();
            while (rs.next()) {
                IdUsuario = rs.getString("IdUsuario");
                NombreU = rs.getString("NombreU");
                Edad = rs.getInt("Edad");
                Sexo = rs.getString("Sexo");
                Escolaridad = rs.getString("Escolaridad");
                Discapacidad = rs.getString("Discapacidad");
                Telefono = rs.getString("Telefono");
                CP = rs.getString("CP");
                Colonia = rs.getString("Colonia");
                Calle = rs.getString("Calle");
                Numero = rs.getInt("Numero");
            }
            pstm.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void eliminarUsuario(String id, int row) {
        String sql = "DELETE FROM Usuarios WHERE IdUsuario=?";
        //con = UConnection.getConnection();
        try {
            pstm = con.prepareStatement(sql);
            pstm.setString(1, id);

            int rtdo = pstm.executeUpdate();
            if (rtdo == 1) {
                JOptionPane.showMessageDialog(null, "Usuario eliminado");
                DefaultTableModel m = (DefaultTableModel) TablaConsultaGenUs.getModel();
                m.removeRow(row);
                m.addRow(new String[8]);
            }
            pstm.close();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    
    public void consultarCP() {
        String sql = "SELECT DISTINCT CP FROM Usuarios WHERE CP like '" +CpUsRegTxt.getText()+"%'";
        con = UConnection.getConnection(user, password);
        limpiarVariables();
        try {
            pstm = con.prepareStatement(sql);
            //pstm.setString(1, ISBNAux);

            rs = pstm.executeQuery();
            codp.removeAllItems();
            while (rs.next()) {
                codp.addItem(rs.getString("CP"));
            }
            pstm.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    
    public void consultarColonia() {
        String sql = "SELECT DISTINCT Colonia FROM Usuarios WHERE Colonia like '" +ColoniaUSRegTxt.getText()+"%'";
        con = UConnection.getConnection(user, password);
        limpiarVariables();
        try {
            pstm = con.prepareStatement(sql);
            //pstm.setString(1, ISBNAux);

            rs = pstm.executeQuery();
            col.removeAllItems();
            while (rs.next()) {
                col.addItem(rs.getString("Colonia"));
            }
            pstm.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    
    public void consultarCalle() {
        String sql = "SELECT DISTINCT Calle FROM Usuarios WHERE Calle like '" +CalleUsRegTxt.getText()+"%'";
        con = UConnection.getConnection(user, password);
        limpiarVariables();
        try {
            pstm = con.prepareStatement(sql);
            //pstm.setString(1, ISBNAux);

            rs = pstm.executeQuery();
            calle.removeAllItems();
            while (rs.next()) {
                calle.addItem(rs.getString("Calle"));
            }
            pstm.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void limpiarVariables() {
        IdUsuario = "";
        NombreU = "";
        Edad = 0;
        Sexo = "";
        Escolaridad = "";
        Discapacidad = "";
        Telefono = "";
        CP = "";
        Colonia = "";
        Calle = "";
        Numero = 0;
    }

    public void limpiarTablaCon() {
        for (int x = 0; x < 11; x++) {
            for (int i = 0; i < fila; i++) {
                TablaConsultaGenUs.setValueAt(null, i, x);
            }
        }
    }

    public void limpiarTxt() {
        IdUsRegTxt.setText("");
        NomUsRegTxt.setText("");
        EdadUsRegTxt.setValue(1);
        jRadBtnHombre.setSelected(false);
        jRadBtnMujer.setSelected(false);
        jComboEsco.setSelectedIndex(0);
        jComboBoxDisc.setSelectedIndex(0);
        CalleUsRegTxt.setText("");
        NumUsRegTxt.setText("");
        CpUsRegTxt.setText("");
        TelUsRegTxt.setText("");
        ColoniaUSRegTxt.setText("");
    }

    public void limpiarTxtActualizar() {
        IdModUsTxt.setText("");
        nomModTxt.setText("");
        HombreMod.setSelected(false);
        MujerMod.setSelected(false);
        EscMod.setSelectedIndex(0);
        DicMod.setSelectedIndex(0);
        cpModtxt.setText("");
        coloniaModTxt.setText("");
        calleModTxt.setText("");
        numeroModTxt.setText("");
        TelModTxt.setText("");
        edadModTxt.setValue(1);
    }

    public boolean camposRegistrar() {
        boolean campos = false;
        if (IdUsRegTxt.getText().length() > 0 && NomUsRegTxt.getText().length() > 0 && CpUsRegTxt.getText().length() > 0
                && ColoniaUSRegTxt.getText().length() > 0 && TelUsRegTxt.getText().length() > 0 && NumUsRegTxt.getText().length() > 0 && CalleUsRegTxt.getText().length() > 0) {
            campos = true;
        }
        if (!jRadBtnMujer.isSelected() && !jRadBtnHombre.isSelected()) {
            campos = false;
        }
        return campos;
    }

    public boolean camposModificar() {
        boolean campos = false;
        if (nomModTxt.getText().length() > 0 && cpModtxt.getText().length() > 0 && calleModTxt.getText().length() > 0
                && numeroModTxt.getText().length() > 0 && coloniaModTxt.getText().length() > 0) {
            campos = true;
        }
        if (!HombreMod.isSelected() && !MujerMod.isSelected()) {
            campos = false;
        }
        return campos;
    }

    public String ultimoID() {
        String sql = "SELECT MAX(IdUsuario) as IdUsuario FROM Usuarios";
        String rtn = "";
        try {
            pstm = con.prepareStatement(sql);

            rs = pstm.executeQuery();
            while (rs.next()) {
                rtn = rs.getString("IdUsuario");

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
            ultimo = "U0001";
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
            ultimo = "U" + idInicio + Integer.toString(digitos);
        }
        IdUsRegTxt.setText(ultimo);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        SexoBtn = new javax.swing.ButtonGroup();
        SexoRad = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        usuariosTabbed = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        NomUsRegTxt = new javax.swing.JTextField();
        IdUsRegTxt = new javax.swing.JTextField();
        ColoniaUSRegTxt = new javax.swing.JTextField();
        CpUsRegTxt = new javax.swing.JTextField();
        CalleUsRegTxt = new javax.swing.JTextField();
        NumUsRegTxt = new javax.swing.JTextField();
        TelUsRegTxt = new javax.swing.JTextField();
        RegUsuarioBtn = new javax.swing.JButton();
        jRadBtnHombre = new javax.swing.JRadioButton();
        jRadBtnMujer = new javax.swing.JRadioButton();
        jComboEsco = new javax.swing.JComboBox<>();
        jComboBoxDisc = new javax.swing.JComboBox<>();
        EdadUsRegTxt = new javax.swing.JSpinner();
        jPanel5 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        nomConsUsTxt = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaConsultaGenUs = new javax.swing.JTable();
        modificarBtn = new javax.swing.JButton();
        eliminarBtnL = new javax.swing.JButton();
        escolaridadConsultaBox = new javax.swing.JComboBox<>();
        discapacidadConsultaBox = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        IdModUsTxt = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        nomModTxt = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        cpModtxt = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        coloniaModTxt = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        calleModTxt = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        numeroModTxt = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        TelModTxt = new javax.swing.JTextField();
        ModUsBtn = new javax.swing.JButton();
        HombreMod = new javax.swing.JRadioButton();
        MujerMod = new javax.swing.JRadioButton();
        EscMod = new javax.swing.JComboBox<>();
        DicMod = new javax.swing.JComboBox<>();
        edadModTxt = new javax.swing.JSpinner();

        setPreferredSize(new java.awt.Dimension(1069, 619));

        jLabel1.setFont(new java.awt.Font("Century Gothic", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("USUARIOS");

        jLabel2.setText("ID Usuario:");

        jLabel3.setText("Nombre:");

        jLabel4.setText("Edad:");

        jLabel5.setText("Sexo:");

        jLabel6.setText("Escolaridad:");

        jLabel7.setText("Discapacidad:");

        jLabel8.setText("Calle:");

        jLabel9.setText("Colonia:");

        jLabel10.setText("Código Postal:");

        jLabel11.setText("Teléfono:");

        jLabel12.setText("Número:");

        NomUsRegTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                NomUsRegTxtKeyTyped(evt);
            }
        });

        IdUsRegTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                IdUsRegTxtKeyTyped(evt);
            }
        });

        ColoniaUSRegTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ColoniaUSRegTxtKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ColoniaUSRegTxtKeyTyped(evt);
            }
        });

        CpUsRegTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                CpUsRegTxtKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                CpUsRegTxtKeyTyped(evt);
            }
        });

        CalleUsRegTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                CalleUsRegTxtKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                CalleUsRegTxtKeyTyped(evt);
            }
        });

        NumUsRegTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                NumUsRegTxtKeyTyped(evt);
            }
        });

        TelUsRegTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TelUsRegTxtKeyTyped(evt);
            }
        });

        RegUsuarioBtn.setText("Guardar");
        RegUsuarioBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RegUsuarioBtnActionPerformed(evt);
            }
        });

        SexoBtn.add(jRadBtnHombre);
        jRadBtnHombre.setText("Masculino");
        jRadBtnHombre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jRadBtnHombreMouseClicked(evt);
            }
        });

        SexoBtn.add(jRadBtnMujer);
        jRadBtnMujer.setText("Femenino");
        jRadBtnMujer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jRadBtnMujerMouseClicked(evt);
            }
        });

        jComboEsco.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Primaria", "Secundaria", "Bachillerato", "Licenciatura", "Maestría", "Doctorado" }));

        jComboBoxDisc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ninguna", "Sindrome de Down", "Sordo-mudos", "Discapacidad Física", "Autismo" }));

        EdadUsRegTxt.setModel(new javax.swing.SpinnerNumberModel(1, 1, 100, 1));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(60, 60, 60)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TelUsRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(ColoniaUSRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(CalleUsRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(NumUsRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(RegUsuarioBtn)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(IdUsRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(EdadUsRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jRadBtnHombre)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadBtnMujer))
                            .addComponent(NomUsRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel11)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel10)
                            .addComponent(jLabel6))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboEsco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CpUsRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxDisc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(453, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(IdUsRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(NomUsRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(EdadUsRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jRadBtnHombre)
                    .addComponent(jRadBtnMujer))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jComboEsco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jComboBoxDisc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(CpUsRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(ColoniaUSRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(CalleUsRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(NumUsRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(TelUsRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(RegUsuarioBtn)
                .addContainerGap(71, Short.MAX_VALUE))
        );

        usuariosTabbed.addTab("Registrar", jPanel1);

        jLabel24.setText("Nombre:");

        nomConsUsTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nomConsUsTxtKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nomConsUsTxtKeyTyped(evt);
            }
        });

        TablaConsultaGenUs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Edad", "Sexo", "Escolaridad", "Discapacidad", "Teléfono", "CP", "Colonia", "Calle", "Número"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaConsultaGenUs.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(TablaConsultaGenUs);
        if (TablaConsultaGenUs.getColumnModel().getColumnCount() > 0) {
            TablaConsultaGenUs.getColumnModel().getColumn(0).setMinWidth(50);
            TablaConsultaGenUs.getColumnModel().getColumn(0).setMaxWidth(50);
            TablaConsultaGenUs.getColumnModel().getColumn(2).setMinWidth(40);
            TablaConsultaGenUs.getColumnModel().getColumn(2).setMaxWidth(40);
            TablaConsultaGenUs.getColumnModel().getColumn(3).setMinWidth(40);
            TablaConsultaGenUs.getColumnModel().getColumn(3).setMaxWidth(40);
            TablaConsultaGenUs.getColumnModel().getColumn(6).setMinWidth(80);
            TablaConsultaGenUs.getColumnModel().getColumn(6).setMaxWidth(80);
            TablaConsultaGenUs.getColumnModel().getColumn(7).setMinWidth(60);
            TablaConsultaGenUs.getColumnModel().getColumn(7).setMaxWidth(60);
            TablaConsultaGenUs.getColumnModel().getColumn(10).setMinWidth(60);
            TablaConsultaGenUs.getColumnModel().getColumn(10).setMaxWidth(60);
        }

        modificarBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgab/img/pencil.png"))); // NOI18N
        modificarBtn.setText("Modificar");
        modificarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificarBtnActionPerformed(evt);
            }
        });

        eliminarBtnL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgab/img/trash_can.png"))); // NOI18N
        eliminarBtnL.setText("Eliminar");
        eliminarBtnL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarBtnLActionPerformed(evt);
            }
        });

        escolaridadConsultaBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos los niveles", "Primaria", "Secundaria", "Bachillerato", "Licenciatura", "Maestría", "Doctorado" }));
        escolaridadConsultaBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                escolaridadConsultaBoxItemStateChanged(evt);
            }
        });

        discapacidadConsultaBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sin filtro", "Ninguna", "Sindrome de Down", "Sordo-mudos", "Discapacidad Física", "Autismo" }));
        discapacidadConsultaBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                discapacidadConsultaBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(nomConsUsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(escolaridadConsultaBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(discapacidadConsultaBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 252, Short.MAX_VALUE)
                        .addComponent(modificarBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(eliminarBtnL)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(nomConsUsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(escolaridadConsultaBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(discapacidadConsultaBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(modificarBtn)
                            .addComponent(eliminarBtnL))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        usuariosTabbed.addTab("Consultar", jPanel5);

        jLabel13.setText("ID Usuario:");

        IdModUsTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                IdModUsTxtKeyTyped(evt);
            }
        });

        jLabel14.setText("Nombre:");

        nomModTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nomModTxtKeyTyped(evt);
            }
        });

        jLabel15.setText("Edad:");

        jLabel16.setText("Sexo:");

        jLabel17.setText("Escolaridad:");

        jLabel18.setText("Discapacidad:");

        jLabel19.setText("Código Postal:");

        cpModtxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cpModtxtKeyTyped(evt);
            }
        });

        jLabel20.setText("Colonia:");

        coloniaModTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                coloniaModTxtKeyTyped(evt);
            }
        });

        jLabel21.setText("Calle:");

        calleModTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                calleModTxtKeyTyped(evt);
            }
        });

        jLabel22.setText("Número:");

        numeroModTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                numeroModTxtKeyTyped(evt);
            }
        });

        jLabel23.setText("Teléfono:");

        TelModTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TelModTxtKeyTyped(evt);
            }
        });

        ModUsBtn.setText("Guardar");
        ModUsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModUsBtnActionPerformed(evt);
            }
        });

        SexoRad.add(HombreMod);
        HombreMod.setText("Masculino");

        SexoRad.add(MujerMod);
        MujerMod.setText("Femenino");

        EscMod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Primaria", "Secundaria", "Bachillerato", "Licenciatura", "Maestría", "Doctorado" }));

        DicMod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ninguna", "Sindrome de Down", "Sordo-mudos", "Discapacidad Física", "Autismo" }));

        edadModTxt.setModel(new javax.swing.SpinnerNumberModel(1, 1, 100, 1));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addGap(18, 18, 18)
                        .addComponent(cpModtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addGap(53, 53, 53)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TelModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(coloniaModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ModUsBtn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(calleModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(numeroModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(IdModUsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edadModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(HombreMod)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(MujerMod))
                            .addComponent(nomModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(jLabel17))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(DicMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(EscMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(454, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(IdModUsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(nomModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(edadModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(HombreMod)
                        .addComponent(MujerMod)))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(EscMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(DicMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(cpModtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(coloniaModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(calleModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(numeroModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(TelModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(ModUsBtn)
                .addContainerGap(72, Short.MAX_VALUE))
        );

        usuariosTabbed.addTab("Modificar", jPanel6);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(usuariosTabbed)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usuariosTabbed)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void RegUsuarioBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RegUsuarioBtnActionPerformed
        if (camposRegistrar()) {
            if (CpUsRegTxt.getText().length() == 5) {
                registrarUsuario();
            } else {
                JOptionPane.showMessageDialog(null, "El código postal es de 5 dígitos");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Faltan campos por completar");
        }
    }//GEN-LAST:event_RegUsuarioBtnActionPerformed

    private void ModUsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModUsBtnActionPerformed
        if (camposModificar()) {
            modificarUsuario();
            limpiarTablaCon();
            consultaGeneral();
        } else {
            JOptionPane.showMessageDialog(null, "Faltan campos por completar");
        }
    }//GEN-LAST:event_ModUsBtnActionPerformed

    private void jRadBtnHombreMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadBtnHombreMouseClicked
        // TODO add your handling code here:
        Sexo = getName();
    }//GEN-LAST:event_jRadBtnHombreMouseClicked

    private void jRadBtnMujerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadBtnMujerMouseClicked
        // TODO add your handling code here:
        Sexo = getName();
    }//GEN-LAST:event_jRadBtnMujerMouseClicked

    private void NomUsRegTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NomUsRegTxtKeyTyped
        // TODO add your handling code here:
        String nombre = NomUsRegTxt.getText().concat(Character.toString(evt.getKeyChar()));
        if (!valid.validarNombre(nombre) || NomUsRegTxt.getText().length() >= 50) {
            evt.consume();
        }
    }//GEN-LAST:event_NomUsRegTxtKeyTyped

    private void CpUsRegTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CpUsRegTxtKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) || CpUsRegTxt.getText().length() >= 5) {
            evt.consume();
        }
    }//GEN-LAST:event_CpUsRegTxtKeyTyped

    private void ColoniaUSRegTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ColoniaUSRegTxtKeyTyped
        // TODO add your handling code here:
        //char c = evt.getKeyChar();
        String nombre = ColoniaUSRegTxt.getText().concat(Character.toString(evt.getKeyChar()));
        if (!valid.validarNombreNum(nombre) || ColoniaUSRegTxt.getText().length() >= 30) {
            evt.consume();
        }
    }//GEN-LAST:event_ColoniaUSRegTxtKeyTyped

    private void CalleUsRegTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CalleUsRegTxtKeyTyped
        // TODO add your handling code here:
        //char c = evt.getKeyChar();
        String nombre = CalleUsRegTxt.getText().concat(Character.toString(evt.getKeyChar()));
        if (!valid.validarNombreNum(nombre) || CalleUsRegTxt.getText().length() >= 40) {
            evt.consume();
        }
    }//GEN-LAST:event_CalleUsRegTxtKeyTyped

    private void NumUsRegTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NumUsRegTxtKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) || NumUsRegTxt.getText().length() >= 3) {
            evt.consume();
        }
    }//GEN-LAST:event_NumUsRegTxtKeyTyped

    private void TelUsRegTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TelUsRegTxtKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) || TelUsRegTxt.getText().length() >= 10) {
            evt.consume();
        }
    }//GEN-LAST:event_TelUsRegTxtKeyTyped

    private void IdUsRegTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IdUsRegTxtKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (valid.id(c) || IdUsRegTxt.getText().length() >= 5) {
            evt.consume();
        }
    }//GEN-LAST:event_IdUsRegTxtKeyTyped

    private void IdModUsTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IdModUsTxtKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (valid.id(c) || IdModUsTxt.getText().length() >= 5) {
            evt.consume();
        }
    }//GEN-LAST:event_IdModUsTxtKeyTyped

    private void nomModTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomModTxtKeyTyped
        // TODO add your handling code here:
        //char c = evt.getKeyChar();
        String nombre = nomModTxt.getText().concat(Character.toString(evt.getKeyChar()));
        if (!valid.validarNombreNum(nombre) || nomModTxt.getText().length() >= 50) {
            evt.consume();
        }
    }//GEN-LAST:event_nomModTxtKeyTyped

    private void cpModtxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cpModtxtKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) || cpModtxt.getText().length() >= 5) {
            evt.consume();
        }
    }//GEN-LAST:event_cpModtxtKeyTyped

    private void coloniaModTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_coloniaModTxtKeyTyped
        // TODO add your handling code here:
        //char c = evt.getKeyChar();
        String nombre = coloniaModTxt.getText().concat(Character.toString(evt.getKeyChar()));
        if (!valid.validarNombreNum(nombre) || coloniaModTxt.getText().length() >= 30) {
            evt.consume();
        }
    }//GEN-LAST:event_coloniaModTxtKeyTyped

    private void calleModTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_calleModTxtKeyTyped
        // TODO add your handling code here:
        //char c = evt.getKeyChar();
        String nombre = calleModTxt.getText().concat(Character.toString(evt.getKeyChar()));
        if (!valid.validarNombreNum(nombre) || calleModTxt.getText().length() >= 40) {
            evt.consume();
        }
    }//GEN-LAST:event_calleModTxtKeyTyped

    private void numeroModTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numeroModTxtKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) || numeroModTxt.getText().length() >= 3) {
            evt.consume();
        }
    }//GEN-LAST:event_numeroModTxtKeyTyped

    private void TelModTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TelModTxtKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) || TelModTxt.getText().length() >= 10) {
            evt.consume();
        }
    }//GEN-LAST:event_TelModTxtKeyTyped

    private void nomConsUsTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomConsUsTxtKeyReleased
        // TODO add your handling code here:
        limpiarTablaCon();
//        if (!nomConsUsTxt.getText().equals("") || !escolaridadConsultaBox.getSelectedItem().equals("Todos los niveles") || !discapacidadConsultaBox.getSelectedItem().equals("Sin filtro")) {
        consultaGeneral();
        //      }
    }//GEN-LAST:event_nomConsUsTxtKeyReleased

    private void modificarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificarBtnActionPerformed
        if (TablaConsultaGenUs.getSelectedRow() != -1) {
            try {
                int f = TablaConsultaGenUs.getSelectedRow();
                String id = TablaConsultaGenUs.getValueAt(f, 0).toString();
                consultaIndividual(id);
                IdModUsTxt.setText(IdUsuario);
                boolean banH = false;
                boolean banM = false;
                if ("M".equals(Sexo)) {
                    banH = true;
                    banM = false;
                } else if ("F".equals(Sexo)) {
                    banM = true;
                    banH = false;
                }
                IdModUsTxt.setEditable(false);
                nomModTxt.setText(NombreU);
                edadModTxt.setValue(Edad);
                HombreMod.setSelected(banH);
                MujerMod.setSelected(banM);
                EscMod.setSelectedItem(Escolaridad);
                DicMod.setSelectedItem(Discapacidad);
                cpModtxt.setText(CP);
                coloniaModTxt.setText(Colonia);
                calleModTxt.setText(Calle);
                numeroModTxt.setText(Integer.toString(Numero));
                TelModTxt.setText(Telefono);

                //JOptionPane.showMessageDialog(null, "Realice los cambios en el apartado de modificar");
                usuariosTabbed.setSelectedIndex(2);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "La fila seleccionada esta vacía");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un registro");
        }
    }//GEN-LAST:event_modificarBtnActionPerformed

    private void eliminarBtnLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarBtnLActionPerformed
        if (TablaConsultaGenUs.getSelectedRow() != -1) {
            try {
                int f = TablaConsultaGenUs.getSelectedRow();
                String id = TablaConsultaGenUs.getValueAt(f, 0).toString();
                int eleccion = JOptionPane.showOptionDialog(this, "¿Desea eliminar el registro?", null, 0, 0, null, botones, this);
                if (eleccion == JOptionPane.YES_OPTION) {
                    eliminarUsuario(id, f);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "La fila seleccionada esta vacía");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un registro");
        }
    }//GEN-LAST:event_eliminarBtnLActionPerformed

    private void nomConsUsTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomConsUsTxtKeyTyped
        String nombre = nomConsUsTxt.getText().concat(Character.toString(evt.getKeyChar()));
        if (!valid.validarNombreNum(nombre) || nomConsUsTxt.getText().length() >= 50) {
            evt.consume();
        }
    }//GEN-LAST:event_nomConsUsTxtKeyTyped

    private void escolaridadConsultaBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_escolaridadConsultaBoxItemStateChanged
        limpiarTablaCon();
//        if (!nomConsUsTxt.getText().equals("") || !escolaridadConsultaBox.getSelectedItem().equals("Todos los niveles") || !discapacidadConsultaBox.getSelectedItem().equals("Sin filtro")) {
        consultaGeneral();
        //      }
    }//GEN-LAST:event_escolaridadConsultaBoxItemStateChanged

    private void discapacidadConsultaBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_discapacidadConsultaBoxItemStateChanged
        limpiarTablaCon();
//        if (!nomConsUsTxt.getText().equals("") || !escolaridadConsultaBox.getSelectedItem().equals("Todos los niveles") || !discapacidadConsultaBox.getSelectedItem().equals("Sin filtro")) {
        consultaGeneral();
        //      }
    }//GEN-LAST:event_discapacidadConsultaBoxItemStateChanged

    private void CpUsRegTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CpUsRegTxtKeyReleased
        consultarCP();
    }//GEN-LAST:event_CpUsRegTxtKeyReleased

    private void ColoniaUSRegTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ColoniaUSRegTxtKeyReleased
        consultarColonia();
    }//GEN-LAST:event_ColoniaUSRegTxtKeyReleased

    private void CalleUsRegTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CalleUsRegTxtKeyReleased
        consultarCalle();
    }//GEN-LAST:event_CalleUsRegTxtKeyReleased

    private void getSexo() {
        if (jRadBtnHombre.isSelected()) {
            Sexo = "M";
        } else if (jRadBtnMujer.isSelected()) {
            Sexo = "F";
        }
    }

    private String getSexoMod() {
        String sexo = "";
        if (HombreMod.isSelected()) {
            sexo = "M";
        } else if (MujerMod.isSelected()) {
            sexo = "F";
        }
        return sexo;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CalleUsRegTxt;
    private javax.swing.JTextField ColoniaUSRegTxt;
    private javax.swing.JTextField CpUsRegTxt;
    private javax.swing.JComboBox<String> DicMod;
    private javax.swing.JSpinner EdadUsRegTxt;
    private javax.swing.JComboBox<String> EscMod;
    private javax.swing.JRadioButton HombreMod;
    private javax.swing.JTextField IdModUsTxt;
    private javax.swing.JTextField IdUsRegTxt;
    private javax.swing.JButton ModUsBtn;
    private javax.swing.JRadioButton MujerMod;
    private javax.swing.JTextField NomUsRegTxt;
    private javax.swing.JTextField NumUsRegTxt;
    private javax.swing.JButton RegUsuarioBtn;
    private javax.swing.ButtonGroup SexoBtn;
    private javax.swing.ButtonGroup SexoRad;
    private javax.swing.JTable TablaConsultaGenUs;
    private javax.swing.JTextField TelModTxt;
    private javax.swing.JTextField TelUsRegTxt;
    private javax.swing.JTextField calleModTxt;
    private javax.swing.JTextField coloniaModTxt;
    private javax.swing.JTextField cpModtxt;
    private javax.swing.JComboBox<String> discapacidadConsultaBox;
    private javax.swing.JSpinner edadModTxt;
    private javax.swing.JButton eliminarBtnL;
    private javax.swing.JComboBox<String> escolaridadConsultaBox;
    private javax.swing.JComboBox<String> jComboBoxDisc;
    private javax.swing.JComboBox<String> jComboEsco;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JRadioButton jRadBtnHombre;
    private javax.swing.JRadioButton jRadBtnMujer;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton modificarBtn;
    private javax.swing.JTextField nomConsUsTxt;
    private javax.swing.JTextField nomModTxt;
    private javax.swing.JTextField numeroModTxt;
    private javax.swing.JTabbedPane usuariosTabbed;
    // End of variables declaration//GEN-END:variables
}
