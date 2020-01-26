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
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;

/**
 *
 * @author FranciscoHernandez
 */
public class VisitasPanel extends javax.swing.JPanel {

    private Connection con = null;
    private PreparedStatement pstm = null;
    private ResultSet rs = null;
    private static String user = "";
    private static String password = "";
    Validar val = new Validar();
    private TextAutoCompleter ta;

    /**
     * Creates new form VisitasPanel
     */
    public VisitasPanel(String user, String password) {
        initComponents();
        this.user = user;
        this.password = password;
        con = UConnection.getConnection(user, password);
        limpiarTablaConsulta();
        consultaGeneral();
        ta = new TextAutoCompleter(idUsuarioRegTxt);
    }

    public void registrarVisita() {
        String sql = "INSERT INTO Visitas (IdUsuario,IdMaterial,FechaV,HoraV) VALUES (?,?,Date_format(now(),'%d/%m/%Y'),time (NOW()))";
        //con = UConnection.getConnection();
        try {
            pstm = con.prepareStatement(sql);

            pstm.setString(1, idUsuarioRegTxt.getText());
            pstm.setString(2, getIdMaterial(materialBoxReg.getSelectedItem().toString()));
            //pstm.setString(3, getFecha());
            //pstm.setString(4, getHora());

            int rtdo = pstm.executeUpdate();
            if (rtdo == 1) {
                JOptionPane.showMessageDialog(null, "Visita registrada");
                limpiarTxt();
                limpiarTablaConsulta();
        //if (!nombreBusTxt.getText().equals("") || fechaBusCal.getDate() != null) {
            consultaGeneral();
            }
            pstm.close();
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void consultaGeneral() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaI;
        if (fechaBusCal.getDate() == null) {
            fechaI = "";
        } else {
            fechaI = sdf.format(fechaBusCal.getDate());
        }

        String nombre = nombreBusTxt.getText();
        String sql = "Select NombreU,NombreM,FechaV,HoraV From Visitas as V "
                + "inner join Usuarios as U on V.IdUsuario=U.IdUsuario "
                + "inner Join Materiales as M on M.IdMaterial=V.IdMaterial "
                + "where NombreU like '" + nombre + "%' and "
                + "FechaV like '" + fechaI + "%'";
        //con = UConnection.getConnection();
        try {
            pstm = con.prepareStatement(sql);

            int fila = 0;
            int col = 0;
            rs = pstm.executeQuery();

            while (rs.next()) {
                tablaCon.setValueAt(rs.getString("NombreU"), fila, col);
                col++;
                tablaCon.setValueAt(rs.getString("NombreM"), fila, col);
                col++;
                tablaCon.setValueAt(rs.getString("FechaV"), fila, col);
                col++;
                tablaCon.setValueAt(rs.getString("HoraV"), fila, col);
                col = 0;
                if (fila == 18) {
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
    
    public void consultarIDs() {
        String sql = "SELECT IdUsuario FROM Usuarios WHERE IdUsuario like '" +idUsuarioRegTxt.getText()+"%'";
        con = UConnection.getConnection(user, password);
        //limpiarVariables();
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

    public String getIdMaterial(String material) {
        String id = "";
        if (material.equals("Libro")) {
            id = "1";
        } else if (material.equals("Computadora")) {
            id = "2";
        } else if (material.equals("Publicaciones Periódicas")) {
            id = "3";
        } else if (material.equals("Videos")) {
            id = "4";
        } else if (material.equals("Otro")) {
            id = "5";
        }
        return id;
    }

    public void limpiarTablaConsulta() {
        for (int x = 0; x < 4; x++) {
            for (int i = 0; i < 19; i++) {
                tablaCon.setValueAt("", i, x);
            }
        }
    }

    public void limpiarTxt() {
        idUsuarioRegTxt.setText("");
        materialBoxReg.setSelectedIndex(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        idUsuarioRegTxt = new javax.swing.JTextField();
        guardarBtnReg = new javax.swing.JButton();
        materialBoxReg = new javax.swing.JComboBox<>();
        rSLabelHora1 = new rojeru_san.RSLabelHora();
        rSLabelFecha1 = new rojeru_san.RSLabelFecha();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaCon = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        nombreBusTxt = new javax.swing.JTextField();
        fechaBusCal = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(909, 614));

        jLabel3.setText("ID Usuario:");

        jLabel4.setText("Material:");

        jLabel5.setText("Hora:");

        jLabel6.setText("Fecha:");

        idUsuarioRegTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                idUsuarioRegTxtKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                idUsuarioRegTxtKeyTyped(evt);
            }
        });

        guardarBtnReg.setText("Guardar");
        guardarBtnReg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarBtnRegActionPerformed(evt);
            }
        });

        materialBoxReg.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Libro", "Computadora", "Publicaciones Periódicas", "Videos", "Otro" }));

        rSLabelHora1.setForeground(new java.awt.Color(0, 0, 0));
        rSLabelHora1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        rSLabelFecha1.setForeground(new java.awt.Color(0, 0, 0));
        rSLabelFecha1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6))
                                .addGap(72, 72, 72)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(rSLabelHora1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rSLabelFecha1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(39, 39, 39)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(materialBoxReg, 0, 211, Short.MAX_VALUE)
                                    .addComponent(idUsuarioRegTxt)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(179, 179, 179)
                        .addComponent(guardarBtnReg)))
                .addContainerGap(572, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(idUsuarioRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(materialBoxReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel5))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(rSLabelHora1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel6))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(rSLabelFecha1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(guardarBtnReg)
                .addContainerGap(228, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Registrar", jPanel1);

        jLabel7.setText("Fecha:");

        tablaCon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Usuario", "Material", "Fecha", "Hora"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaCon.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tablaCon);

        jLabel2.setText("Nombre Usuario:");

        nombreBusTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nombreBusTxtKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nombreBusTxtKeyTyped(evt);
            }
        });

        fechaBusCal.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fechaBusCalPropertyChange(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nombreBusTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fechaBusCal, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 897, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(nombreBusTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7))
                    .addComponent(fechaBusCal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Consultar", jPanel2);

        jLabel1.setFont(new java.awt.Font("Century Gothic", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("VISITAS");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 555, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void guardarBtnRegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarBtnRegActionPerformed
        if (idUsuarioRegTxt.getText().length() > 0) {
            if (val.validarEnvioID(idUsuarioRegTxt.getText())) {
                registrarVisita();
            } else {
                JOptionPane.showMessageDialog(null, "El ID debe contener 1 letra mayúscula y 4 dígitos");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Faltan campos por completar");
        }

    }//GEN-LAST:event_guardarBtnRegActionPerformed

    private void nombreBusTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombreBusTxtKeyReleased
        limpiarTablaConsulta();
        //if (!nombreBusTxt.getText().equals("") || fechaBusCal.getDate() != null) {
            consultaGeneral();
        //}

    }//GEN-LAST:event_nombreBusTxtKeyReleased

    private void fechaBusCalPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fechaBusCalPropertyChange
        limpiarTablaConsulta();
       // if (!nombreBusTxt.getText().equals("") || fechaBusCal.getDate() != null) {

            consultaGeneral();
        //}
    }//GEN-LAST:event_fechaBusCalPropertyChange

    private void idUsuarioRegTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idUsuarioRegTxtKeyTyped
        //char c = evt.getKeyChar();
        String nombre = idUsuarioRegTxt.getText().concat(Character.toString(evt.getKeyChar()));
        if (!val.IDUsuario(nombre) || idUsuarioRegTxt.getText().length() >= 5) {
            evt.consume();
        }
    }//GEN-LAST:event_idUsuarioRegTxtKeyTyped

    private void nombreBusTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombreBusTxtKeyTyped
        //char c = evt.getKeyChar();
        String nombre = nombreBusTxt.getText().concat(Character.toString(evt.getKeyChar()));
        if (!val.validarNombre(nombre) || nombreBusTxt.getText().length() >= 50) {
            evt.consume();
        }
    }//GEN-LAST:event_nombreBusTxtKeyTyped

    private void idUsuarioRegTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idUsuarioRegTxtKeyReleased
      consultarIDs();
    }//GEN-LAST:event_idUsuarioRegTxtKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser fechaBusCal;
    private javax.swing.JButton guardarBtnReg;
    private javax.swing.JTextField idUsuarioRegTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JComboBox<String> materialBoxReg;
    private javax.swing.JTextField nombreBusTxt;
    private rojeru_san.RSLabelFecha rSLabelFecha1;
    private rojeru_san.RSLabelHora rSLabelHora1;
    private javax.swing.JTable tablaCon;
    // End of variables declaration//GEN-END:variables
}
