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
import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author FranciscoHernandez
 */
public class PrestamosPanel extends javax.swing.JPanel {

    private Connection con = null;
    private PreparedStatement pstm = null;
    private ResultSet rs = null;
    Validar valida = new Validar();

    private String IdPrestamo;
    private String IdUsuario;
    private String ISBN;
    private String FechaInicioP;
    private String FechaFinP;

    private String[] botones = {"Si", "No"};
    private int fila;
    private int fila2;
    private static String user = "";
    private static String password = "";
    private String libro1;
    private String libro2;
    private String libro3;
    private String isbn1;
    private String isbn2;
    private String isbn3;
    private String fechaDevolucionP1;
    private String fechaDevolucionP2;
    private String fechaDevolucionP3;
    int cant = 0;
    TextAutoCompleter ta;

    /**
     * Creates new form PrestamosPanel
     */
    public PrestamosPanel(String user, String password) {
        initComponents();
        this.user = user;
        this.password = password;

//        eliminarBtnL.setBackground(Color.RED);
        //      eliminarBtnL.setForeground(Color.WHITE);
        modificarBtn.setBackground(new Color(32, 33, 79));
        modificarBtn.setForeground(Color.WHITE);
        devolucionBtn.setBackground(new Color(115, 33, 105));
        devolucionBtn.setForeground(Color.WHITE);

        TablaConGenP.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ISBNRegTxt1.setEditable(false);
        ISBNRegTxt2.setEditable(false);
        ISBNRegTxt3.setEditable(false);
        titulo1DevTxt.setEditable(false);
        titulo2DevTxt.setEditable(false);
        titulo3DevTxt.setEditable(false);
        libro1ModTxt.setEditable(false);
        libro2ModTxt.setEditable(false);
        libro3ModTxt.setEditable(false);
        fechaInicioModTxt.setEditable(false);
        idprestamoDevTxt.setEditable(false);
        IdPrestamoModTxt.setEditable(false);
        IdUsuarioModTxt.setEditable(false);
        devolucion1txt.setEditable(false);
        devolucion2txt.setEditable(false);
        devolucion3txt.setEditable(false);
        IdPrestamoTxtReg.setEditable(false);
        generarID();
        limpiarTablaCon();
        consultaGeneral();
        eliminarLibroBtn1.setOpaque(false);
        eliminarLibroBtn1.setContentAreaFilled(false);
        eliminarLibroBtn1.setBorderPainted(false);
        eliminarLibroBtn1.setForeground(Color.WHITE);

        eliminarLibroBtn2.setOpaque(false);
        eliminarLibroBtn2.setContentAreaFilled(false);
        eliminarLibroBtn2.setBorderPainted(false);
        eliminarLibroBtn2.setForeground(Color.WHITE);

        eliminarLibroBtn3.setOpaque(false);
        eliminarLibroBtn3.setContentAreaFilled(false);
        eliminarLibroBtn3.setBorderPainted(false);
        eliminarLibroBtn3.setForeground(Color.WHITE);

        agregar1Btn.setOpaque(false);
        agregar1Btn.setContentAreaFilled(false);
        agregar1Btn.setBorderPainted(false);
        agregar1Btn.setForeground(Color.WHITE);

        agregar2Btn.setOpaque(false);
        agregar2Btn.setContentAreaFilled(false);
        agregar2Btn.setBorderPainted(false);
        agregar2Btn.setForeground(Color.WHITE);

        agregar3Btn.setOpaque(false);
        agregar3Btn.setContentAreaFilled(false);
        agregar3Btn.setBorderPainted(false);
        agregar3Btn.setForeground(Color.WHITE);
        ta = new TextAutoCompleter(IdUsuarioTxtReg);
        
        ISBNRegTxt1.setVisible(false);
        ISBNRegTxt2.setVisible(false);
        ISBNRegTxt3.setVisible(false);
        libro1nombre.setEditable(false);
        libro2nombre.setEditable(false);
        libro3nombre.setEditable(false);
        
    }

    public void consultarIDs() {
        String sql = "SELECT IdUsuario FROM Usuarios WHERE IdUsuario like '" + IdUsuarioTxtReg.getText() + "%'";
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

    public void registrarPrestamo(String isbnL) {
        String sql = "INSERT INTO Prestamos (IdPrestamo,ISBN,FechaDevolucionP) VALUES (?,?,?)";
        con = UConnection.getConnection(user, password);
        try {
            pstm = con.prepareStatement(sql);

            pstm.setString(1, IdPrestamoTxtReg.getText());
            pstm.setString(2, isbnL);
            pstm.setString(3, "");

            int rtdo = pstm.executeUpdate();
            if (rtdo == 1) {
                // JOptionPane.showMessageDialog(null, "Prestamo registrado");

            }
            pstm.close();
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void registrarDetalles() {
        String sql = "INSERT INTO Detalle_Prestamos (IdPrestamo,IdUsuario,FechaInicioP,FechaFinP,Cantidad) VALUES (?,?,?,?,?)";
        con = UConnection.getConnection(user, password);
        try {
            pstm = con.prepareStatement(sql);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            //String fechaI = sdf.format(FechaInicioTxtReg.getDate());
            String fechaF = sdf.format(FechaFinTxtReg.getDate());
            pstm.setString(1, IdPrestamoTxtReg.getText());
            pstm.setString(2, IdUsuarioTxtReg.getText());
            pstm.setString(3, fechaInicioR.getFecha());
            pstm.setString(4, fechaF);
            pstm.setInt(5, cant);

            int rtdo = pstm.executeUpdate();
            if (rtdo == 1) {
                JOptionPane.showMessageDialog(null, "Préstamo registrado");
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
        String usuario = UsuarioConGenTxt.getText();
        String cant = numLibrosConsultaBox.getSelectedItem().toString();
        if (cant.equals("Sin filtro")) {
            cant = "";
        }
        String sql = "SELECT DP.IdPrestamo,DP.IdUsuario,NombreU,Cantidad,FechaInicioP,"
                + "FechaFinP FROM Detalle_Prestamos as DP INNER JOIN "
                + "Usuarios as U ON DP.IdUsuario=U.IdUsuario WHERE NombreU like '" + usuario + "%' AND Cantidad like '" + cant + "%'";
        con = UConnection.getConnection(user, password);
        try {
            pstm = con.prepareStatement(sql);

            fila = 0;
            int col = 0;
            rs = pstm.executeQuery();

            while (rs.next()) {
                TablaConGenP.setValueAt(rs.getString("IdPrestamo"), fila, col);
                col++;
                TablaConGenP.setValueAt(rs.getString("IdUsuario"), fila, col);
                col++;
                TablaConGenP.setValueAt(rs.getString("NombreU"), fila, col);
                col++;
                TablaConGenP.setValueAt(rs.getString("Cantidad"), fila, col);
                col++;
                TablaConGenP.setValueAt(rs.getString("FechaInicioP"), fila, col);
                col++;
                TablaConGenP.setValueAt(rs.getString("FechaFinP"), fila, col);
                col = 0;
                if (fila == TablaConGenP.getRowCount() - 1) {
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

    public void modificarPrestamo() {
        String sql = "UPDATE Detalle_Prestamos SET FechaFinP=? WHERE IdPrestamo=? and IdUsuario=?";
        con = UConnection.getConnection(user, password);
        try {
            pstm = con.prepareStatement(sql);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String fechaF = sdf.format(FechaFinModTxt.getDate());

            pstm.setString(1, fechaF);
            pstm.setString(2, IdPrestamoModTxt.getText());
            pstm.setString(3, IdUsuarioModTxt.getText());

            int rtdo = pstm.executeUpdate();
            if (rtdo == 1) {
                JOptionPane.showMessageDialog(null, "Préstamo actualizado");
                limpiarTxtActualizar();
            }
            pstm.close();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void registrarDevolucion(String idprestamoAux, String isbnAux, String fechaDev) {
        String sql = "UPDATE Prestamos SET FechaDevolucionP=? WHERE IdPrestamo=? AND ISBN=?";
        con = UConnection.getConnection(user, password);
        try {
            pstm = con.prepareStatement(sql);

            pstm.setString(1, fechaDev);
            pstm.setString(2, idprestamoAux);
            pstm.setString(3, isbnAux);

            int rtdo = pstm.executeUpdate();
            if (rtdo == 1) {
                //idprestamoDevTxt.setText("");
                //titulo1DevTxt.setText("");
                //titulo2DevTxt.setText("");
                //titulo3DevTxt.setText("");
                //fechaDev1.setDate(null);
                //fechaDev2.setDate(null);
                //fechaDev3.setDate(null);
                //devolucion1Check.setSelected(false);
                //devolucion2check.setSelected(false);
                //devolucion3check.setSelected(false);
            }
            pstm.close();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void consultaIndividual(String IdPrestamoRAux, String IdUsuarioRAux) {
        String sql = "SELECT IdPrestamo,IdUsuario,FechaInicioP,FechaFinP FROM Detalle_Prestamos WHERE IdPrestamo=? and IdUsuario=?";
        con = UConnection.getConnection(user, password);
        try {
            pstm = con.prepareStatement(sql);
            pstm.setString(1, IdPrestamoRAux);
            pstm.setString(2, IdUsuarioRAux);

            rs = pstm.executeQuery();
            while (rs.next()) {
                IdPrestamo = rs.getString("IdPrestamo");
                IdUsuario = rs.getString("IdUsuario");
                FechaInicioP = rs.getString("FechaInicioP");
                FechaFinP = rs.getString("FechaFinP");
            }
            pstm.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void consultaLibros(String IdPrestamoRAux) {
        String sql = "SELECT P.ISBN,L.Titulo,P.FechaDevolucionP FROM Prestamos as P INNER JOIN"
                + " Libros AS L ON P.ISBN=L.ISBN WHERE IdPrestamo=?";
        con = UConnection.getConnection(user, password);
        limpiarVariables();
        try {
            pstm = con.prepareStatement(sql);
            pstm.setString(1, IdPrestamoRAux);
            int cont = 1;
            rs = pstm.executeQuery();
            while (rs.next()) {
                if (cont == 1) {
                    isbn1 = rs.getString("ISBN");
                    libro1 = rs.getString("Titulo");
                    fechaDevolucionP1 = rs.getString("FechaDevolucionP");
                } else if (cont == 2) {
                    isbn2 = rs.getString("ISBN");
                    libro2 = rs.getString("Titulo");
                    fechaDevolucionP2 = rs.getString("FechaDevolucionP");
                } else if (cont == 3) {
                    isbn3 = rs.getString("ISBN");
                    libro3 = rs.getString("Titulo");
                    fechaDevolucionP3 = rs.getString("FechaDevolucionP");
                }
                cont++;
            }
            pstm.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void eliminarPrestamo(String idP, String idU, int row) {
        String sql = "DELETE FROM Prestamos WHERE IdPrestamo=? and IdUsuario=?";
        con = UConnection.getConnection(user, password);
        try {
            pstm = con.prepareStatement(sql);
            pstm.setString(1, idP);
            pstm.setString(2, idU);

            int rtdo = pstm.executeUpdate();
            if (rtdo == 1) {
                JOptionPane.showMessageDialog(null, "Préstamo eliminado");
                DefaultTableModel m = (DefaultTableModel) TablaConGenP.getModel();
                m.removeRow(row);
                m.addRow(new String[8]);
            }
            pstm.close();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void consultarLibros() {
        String titulo = tituloTxt.getText();
        String sql = "SELECT ISBN,Titulo FROM Libros WHERE Titulo like '" + titulo + "%'";
        con = UConnection.getConnection(user, password);
        try {
            pstm = con.prepareStatement(sql);

            fila2 = 0;
            int col = 0;
            rs = pstm.executeQuery();

            while (rs.next()) {
                tablaLibros.setValueAt(rs.getString("ISBN"), fila2, col);
                col++;
                tablaLibros.setValueAt(rs.getString("Titulo"), fila2, col);
                col = 0;
                if (fila2 == tablaLibros.getRowCount() - 1) {
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

    public void limpiarVariables() {
        IdPrestamo = "";
        IdUsuario = "";
        ISBN = "";
        FechaInicioP = "";
        FechaFinP = "";
        fechaDevolucionP1 = "";
        fechaDevolucionP2 = "";
        fechaDevolucionP3 = "";
        libro1 = "";
        libro2 = "";
        libro3 = "";
        isbn1 = "";
        isbn2 = "";
        isbn3 = "";
    }

    public void limpiarTxt() {
        IdUsuarioTxtReg.setText("");
        IdPrestamoTxtReg.setText("");
        ISBNRegTxt1.setText("");
        ISBNRegTxt2.setText("");
        ISBNRegTxt3.setText("");
        //FechaInicioTxtReg.setDate(null);
        FechaFinTxtReg.setDate(null);
        libro1nombre.setText("");
        libro2nombre.setText("");
        libro3nombre.setText("");
    }

    public void limpiarTxtActualizar() {
        IdPrestamoModTxt.setText("");
        IdUsuarioModTxt.setText("");
        libro1ModTxt.setText("");
        libro2ModTxt.setText("");
        libro3ModTxt.setText("");
        devolucion1txt.setText("");
        devolucion2txt.setText("");
        devolucion3txt.setText("");
        fechaInicioModTxt.setText("");
        FechaFinModTxt.setDate(null);
    }

    public void limpiarTablaConsulta() {
        for (int x = 0; x < 2; x++) {
            for (int i = 0; i < fila2; i++) {
                tablaLibros.setValueAt(null, i, x);
            }
        }
    }

    public void limpiarTablaCon() {
        for (int x = 0; x < 6; x++) {
            for (int i = 0; i < fila; i++) {
                TablaConGenP.setValueAt(null, i, x);
            }
        }
    }

    public boolean camposRegistrar() {
        boolean campos = false;
        if (IdPrestamoTxtReg.getText().length() > 0 && IdUsuarioTxtReg.getText().length() > 0 && FechaFinTxtReg.getDate() != null) {
            campos = true;
        }
        if (ISBNRegTxt1.getText().length() == 0 && ISBNRegTxt2.getText().length() == 0 && ISBNRegTxt3.getText().length() == 0) {
            campos = false;
        }
        return campos;
    }

    public String ultimoID() {
        con = UConnection.getConnection(user, password);
        String sql = "SELECT MAX(IdPrestamo) as ID FROM Prestamos";
        String rtn = "";
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
            ultimo = "P0001";
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
            ultimo = "P" + idInicio + Integer.toString(digitos);
        }
        IdPrestamoTxtReg.setText(ultimo);
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
        prestamosTabbed = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        IdPrestamoTxtReg = new javax.swing.JTextField();
        IdUsuarioTxtReg = new javax.swing.JTextField();
        ISBNRegTxt1 = new javax.swing.JTextField();
        PrestamoRegBoton = new javax.swing.JButton();
        FechaFinTxtReg = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        ISBNRegTxt2 = new javax.swing.JTextField();
        ISBNRegTxt3 = new javax.swing.JTextField();
        agregar1Btn = new javax.swing.JButton();
        agregar2Btn = new javax.swing.JButton();
        agregar3Btn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaLibros = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        tituloTxt = new javax.swing.JTextField();
        eliminarLibroBtn1 = new javax.swing.JButton();
        eliminarLibroBtn2 = new javax.swing.JButton();
        eliminarLibroBtn3 = new javax.swing.JButton();
        fechaInicioR = new rojeru_san.RSLabelFecha();
        libro1nombre = new javax.swing.JTextField();
        libro2nombre = new javax.swing.JTextField();
        libro3nombre = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        UsuarioConGenTxt = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaConGenP = new javax.swing.JTable();
        modificarBtn = new javax.swing.JButton();
        devolucionBtn = new javax.swing.JButton();
        numLibrosConsultaBox = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        IdPrestamoModTxt = new javax.swing.JTextField();
        libro1ModTxt = new javax.swing.JTextField();
        ActualizarBtn = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        IdUsuarioModTxt = new javax.swing.JTextField();
        FechaFinModTxt = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        libro2ModTxt = new javax.swing.JTextField();
        libro3ModTxt = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        fechaInicioModTxt = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        devolucion1txt = new javax.swing.JTextField();
        devolucion2txt = new javax.swing.JTextField();
        devolucion3txt = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        idprestamoDevTxt = new javax.swing.JTextField();
        titulo1DevTxt = new javax.swing.JTextField();
        titulo2DevTxt = new javax.swing.JTextField();
        titulo3DevTxt = new javax.swing.JTextField();
        guardarBtnDev = new javax.swing.JButton();
        fechaDvolucionD = new rojeru_san.RSLabelFecha();
        devolucion1Check = new javax.swing.JCheckBox();
        devolucion2check = new javax.swing.JCheckBox();
        devolucion3check = new javax.swing.JCheckBox();
        jLabel26 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(909, 619));

        jLabel1.setFont(new java.awt.Font("Century Gothic", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("PRÉSTAMOS");

        jLabel2.setText("ID Préstamo:");

        jLabel3.setText("ID Usuario:");

        jLabel4.setText("Libro 1:");

        jLabel5.setText("Fecha de inicio:");

        jLabel6.setText("Fecha de fin:");

        IdPrestamoTxtReg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                IdPrestamoTxtRegKeyTyped(evt);
            }
        });

        IdUsuarioTxtReg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                IdUsuarioTxtRegKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                IdUsuarioTxtRegKeyTyped(evt);
            }
        });

        PrestamoRegBoton.setText("Guardar");
        PrestamoRegBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrestamoRegBotonActionPerformed(evt);
            }
        });

        FechaFinTxtReg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                FechaFinTxtRegKeyTyped(evt);
            }
        });

        jLabel14.setText("Libro 2:");

        jLabel15.setText("Libro 3:");

        agregar1Btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgab/img/add.png"))); // NOI18N
        agregar1Btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregar1BtnActionPerformed(evt);
            }
        });

        agregar2Btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgab/img/add.png"))); // NOI18N
        agregar2Btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregar2BtnActionPerformed(evt);
            }
        });

        agregar3Btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgab/img/add.png"))); // NOI18N
        agregar3Btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregar3BtnActionPerformed(evt);
            }
        });

        tablaLibros.setModel(new javax.swing.table.DefaultTableModel(
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
                "ISBN", "Título"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaLibros.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tablaLibros);

        jLabel17.setText("Título:");

        tituloTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tituloTxtKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tituloTxtKeyTyped(evt);
            }
        });

        eliminarLibroBtn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgab/img/delete.png"))); // NOI18N
        eliminarLibroBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarLibroBtn1ActionPerformed(evt);
            }
        });

        eliminarLibroBtn2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgab/img/delete.png"))); // NOI18N
        eliminarLibroBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarLibroBtn2ActionPerformed(evt);
            }
        });

        eliminarLibroBtn3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgab/img/delete.png"))); // NOI18N
        eliminarLibroBtn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarLibroBtn3ActionPerformed(evt);
            }
        });

        fechaInicioR.setForeground(new java.awt.Color(0, 0, 0));
        fechaInicioR.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(IdUsuarioTxtReg, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(IdPrestamoTxtReg, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 211, Short.MAX_VALUE))
                            .addComponent(libro1nombre)
                            .addComponent(libro2nombre)
                            .addComponent(libro3nombre))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(agregar1Btn)
                            .addComponent(agregar2Btn)
                            .addComponent(agregar3Btn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(eliminarLibroBtn1)
                            .addComponent(eliminarLibroBtn2)
                            .addComponent(eliminarLibroBtn3))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(ISBNRegTxt1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(ISBNRegTxt2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(ISBNRegTxt3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(tituloTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(57, 57, 57))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(FechaFinTxtReg, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fechaInicioR, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(389, 389, 389)
                .addComponent(PrestamoRegBoton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(IdPrestamoTxtReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ISBNRegTxt1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ISBNRegTxt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ISBNRegTxt3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(tituloTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                        .addComponent(PrestamoRegBoton)
                        .addGap(104, 104, 104))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(IdUsuarioTxtReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(libro1nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel14)
                                            .addComponent(libro2nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(27, 27, 27)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel15)
                                            .addComponent(libro3nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(107, 107, 107))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(agregar1Btn)
                                            .addComponent(eliminarLibroBtn1))
                                        .addGap(10, 10, 10)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(eliminarLibroBtn2)
                                                .addGap(19, 19, 19)
                                                .addComponent(eliminarLibroBtn3))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(agregar2Btn)
                                                .addGap(19, 19, 19)
                                                .addComponent(agregar3Btn)))))
                                .addGap(40, 40, 40)
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fechaInicioR, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel6))
                            .addComponent(FechaFinTxtReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );

        prestamosTabbed.addTab("Registrar", jPanel1);

        jLabel13.setText("Nombre Usuario:");

        UsuarioConGenTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                UsuarioConGenTxtKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                UsuarioConGenTxtKeyTyped(evt);
            }
        });

        TablaConGenP.setModel(new javax.swing.table.DefaultTableModel(
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
                "ID Préstamo", "ID Usuario", "Usuario", "Cantidad Libros", "Fecha Inicio", "Fecha Fin"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
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
        TablaConGenP.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(TablaConGenP);

        modificarBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgab/img/pencil.png"))); // NOI18N
        modificarBtn.setText("Detalles");
        modificarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificarBtnActionPerformed(evt);
            }
        });

        devolucionBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgab/img/checkmark.png"))); // NOI18N
        devolucionBtn.setText("Devolución");
        devolucionBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                devolucionBtnActionPerformed(evt);
            }
        });

        numLibrosConsultaBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sin filtro", "1", "2", "3" }));
        numLibrosConsultaBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                numLibrosConsultaBoxItemStateChanged(evt);
            }
        });

        jLabel25.setText("Cantidad libros:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1029, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addComponent(UsuarioConGenTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numLibrosConsultaBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(devolucionBtn)
                        .addGap(18, 18, 18)
                        .addComponent(modificarBtn)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(UsuarioConGenTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(numLibrosConsultaBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(modificarBtn)
                            .addComponent(devolucionBtn))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        prestamosTabbed.addTab("Consultar", jPanel2);

        jLabel7.setText("ID Préstamo:");

        jLabel8.setText("Libro 1:");

        jLabel9.setText("Fecha de incio:");

        jLabel10.setText("Fecha de fin:");

        IdPrestamoModTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                IdPrestamoModTxtKeyTyped(evt);
            }
        });

        libro1ModTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                libro1ModTxtKeyTyped(evt);
            }
        });

        ActualizarBtn.setText("Actualizar");
        ActualizarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ActualizarBtnActionPerformed(evt);
            }
        });

        jLabel12.setText("ID Usuario:");

        IdUsuarioModTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                IdUsuarioModTxtKeyTyped(evt);
            }
        });

        FechaFinModTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                FechaFinModTxtKeyTyped(evt);
            }
        });

        jLabel16.setText("Libro 2:");

        jLabel18.setText("Libro 3:");

        jLabel11.setText("Devolución:");

        jLabel19.setText("Devolución:");

        jLabel20.setText("Devolución:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(991, 991, 991))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel8)
                                .addComponent(jLabel7)
                                .addComponent(jLabel9))
                            .addGap(51, 51, 51)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(FechaFinModTxt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ActualizarBtn, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                            .addComponent(IdPrestamoModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabel12)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(IdUsuarioModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(fechaInicioModTxt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(libro1ModTxt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                                        .addComponent(libro2ModTxt, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(libro3ModTxt, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addGap(29, 29, 29)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                            .addComponent(jLabel11)
                                            .addGap(18, 18, 18)
                                            .addComponent(devolucion2txt))
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                            .addComponent(jLabel19)
                                            .addGap(18, 18, 18)
                                            .addComponent(devolucion1txt, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                            .addComponent(jLabel20)
                                            .addGap(18, 18, 18)
                                            .addComponent(devolucion3txt)))
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel10)
                            .addGap(425, 425, 425)))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(IdPrestamoModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(IdUsuarioModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(libro1ModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(libro2ModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(libro3ModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18)
                            .addComponent(jLabel20)
                            .addComponent(devolucion3txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(34, 34, 34)
                                .addComponent(jLabel10))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(fechaInicioModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(FechaFinModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(42, 42, 42)
                        .addComponent(ActualizarBtn))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(devolucion1txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(devolucion2txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(181, Short.MAX_VALUE))
        );

        prestamosTabbed.addTab("Detalles", jPanel3);

        jLabel21.setText("ID Préstamo:");

        jLabel22.setText("Libro 1:");

        jLabel23.setText("Libro 2:");

        jLabel24.setText("Libro 3:");

        guardarBtnDev.setText("Guardar");
        guardarBtnDev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarBtnDevActionPerformed(evt);
            }
        });

        fechaDvolucionD.setForeground(new java.awt.Color(0, 0, 0));
        fechaDvolucionD.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        devolucion1Check.setText("Devolución");

        devolucion2check.setText("Devolución");

        devolucion3check.setText("Devolución");
        devolucion3check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                devolucion3checkActionPerformed(evt);
            }
        });

        jLabel26.setText("Fecha:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(guardarBtnDev)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel24)
                            .addComponent(jLabel23)
                            .addComponent(jLabel22)
                            .addComponent(jLabel21))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(idprestamoDevTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(titulo2DevTxt)
                            .addComponent(titulo3DevTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
                            .addComponent(titulo1DevTxt))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fechaDvolucionD, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(devolucion2check)
                    .addComponent(devolucion1Check)
                    .addComponent(devolucion3check))
                .addContainerGap(483, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel21)
                        .addComponent(idprestamoDevTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel26))
                    .addComponent(fechaDvolucionD, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel22)
                        .addComponent(titulo1DevTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(devolucion1Check))
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(titulo2DevTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(titulo3DevTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(devolucion2check)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(devolucion3check)))
                .addGap(38, 38, 38)
                .addComponent(guardarBtnDev)
                .addContainerGap(248, Short.MAX_VALUE))
        );

        prestamosTabbed.addTab("Devoluciones", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(prestamosTabbed)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prestamosTabbed))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void PrestamoRegBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrestamoRegBotonActionPerformed
        if (camposRegistrar()) {
            boolean detalle = false;
            cant = 0;
            if (valida.validarEnvioID(IdPrestamoTxtReg.getText()) && valida.validarEnvioID(IdUsuarioTxtReg.getText())) {

                if (convertirFecha(fechaInicioR.getFecha()).compareTo(FechaFinTxtReg.getDate()) < 0) {
                    if (!ISBNRegTxt1.getText().equals("")) {
                        registrarPrestamo(ISBNRegTxt1.getText());
                        detalle = true;
                        cant++;
                    }
                    if (!ISBNRegTxt2.getText().equals("")) {
                        registrarPrestamo(ISBNRegTxt2.getText());
                        detalle = true;
                        cant++;
                    }
                    if (!ISBNRegTxt3.getText().equals("")) {
                        registrarPrestamo(ISBNRegTxt3.getText());
                        detalle = true;
                        cant++;
                    }
                    if (detalle) {
                        registrarDetalles();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Las fechas están mal");
                }
            } else {
                JOptionPane.showMessageDialog(null, "El ID debe contener 1 letra mayúscula y 4 dígitos");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Faltan campos por completar");
        }

    }//GEN-LAST:event_PrestamoRegBotonActionPerformed

    private void ActualizarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ActualizarBtnActionPerformed
        if (FechaFinModTxt.getDate() != null) {
            if (convertirFecha(fechaInicioModTxt.getText()).compareTo(FechaFinModTxt.getDate()) < 0) {
                modificarPrestamo();
                limpiarTablaCon();
                consultaGeneral();
            } else {
                JOptionPane.showMessageDialog(null, "Las fechas están mal");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Faltan campos por completar");
        }
    }//GEN-LAST:event_ActualizarBtnActionPerformed

    private void UsuarioConGenTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UsuarioConGenTxtKeyReleased
        limpiarTablaCon();
//        if (!UsuarioConGenTxt.getText().equals("") || numLibrosConsultaBox.getSelectedIndex() != 0) {
        consultaGeneral();
        //      }

    }//GEN-LAST:event_UsuarioConGenTxtKeyReleased

    private void IdPrestamoTxtRegKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IdPrestamoTxtRegKeyTyped
        char c = evt.getKeyChar();
        if (valida.id(c) || IdPrestamoTxtReg.getText().length() >= 5) {
            evt.consume();
        }
    }//GEN-LAST:event_IdPrestamoTxtRegKeyTyped

    private void IdUsuarioTxtRegKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IdUsuarioTxtRegKeyTyped
        //char c = evt.getKeyChar();
        String nombre = IdUsuarioTxtReg.getText().concat(Character.toString(evt.getKeyChar()));
        if (!valida.IDUsuario(nombre) || IdUsuarioTxtReg.getText().length() >= 5) {
            evt.consume();
        }
    }//GEN-LAST:event_IdUsuarioTxtRegKeyTyped

    private void UsuarioConGenTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UsuarioConGenTxtKeyTyped
        String nombre = UsuarioConGenTxt.getText().concat(Character.toString(evt.getKeyChar()));
        //char c = evt.getKeyChar();
        if (!valida.validarNombre(nombre) || UsuarioConGenTxt.getText().length() >= 40) {
            evt.consume();
        }
    }//GEN-LAST:event_UsuarioConGenTxtKeyTyped

    private void IdPrestamoModTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IdPrestamoModTxtKeyTyped
        char c = evt.getKeyChar();
        if (valida.letrasNumeros(c) || IdPrestamoModTxt.getText().length() == 5) {
            evt.consume();
        }
    }//GEN-LAST:event_IdPrestamoModTxtKeyTyped

    private void IdUsuarioModTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IdUsuarioModTxtKeyTyped
        char c = evt.getKeyChar();
        if (valida.letrasNumeros(c) || IdUsuarioModTxt.getText().length() == 5) {
            evt.consume();
        }
    }//GEN-LAST:event_IdUsuarioModTxtKeyTyped

    private void libro1ModTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_libro1ModTxtKeyTyped
        char c = evt.getKeyChar();
        if (valida.letrasNumeros(c) || libro1ModTxt.getText().length() == 13) {
            evt.consume();
        }
    }//GEN-LAST:event_libro1ModTxtKeyTyped

    private void FechaFinTxtRegKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FechaFinTxtRegKeyTyped
        char c = evt.getKeyChar();
        if (c < '0' || c > '9') {
            evt.consume();
        }
    }//GEN-LAST:event_FechaFinTxtRegKeyTyped

    private void FechaFinModTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FechaFinModTxtKeyTyped
        char c = evt.getKeyChar();
        if (c < '0' || c > '9') {
            evt.consume();
        }
    }//GEN-LAST:event_FechaFinModTxtKeyTyped

    private void modificarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificarBtnActionPerformed
        if (TablaConGenP.getSelectedRow() != -1) {
            try {
                int f = TablaConGenP.getSelectedRow();
                String idP = TablaConGenP.getValueAt(f, 0).toString();
                String idU = TablaConGenP.getValueAt(f, 1).toString();
                consultaLibros(idP);
                consultaIndividual(idP, idU);
                IdPrestamoModTxt.setEditable(false);
                IdPrestamoModTxt.setText(IdPrestamo);
                IdUsuarioModTxt.setText(IdUsuario);
                IdUsuarioModTxt.setEditable(false);
                libro1ModTxt.setText(libro1);
                libro2ModTxt.setText(libro2);
                libro3ModTxt.setText(libro3);
                devolucion1txt.setText(fechaDevolucionP1);
                devolucion2txt.setText(fechaDevolucionP2);

                devolucion3txt.setText(fechaDevolucionP3);

                fechaInicioModTxt.setText(FechaInicioP);
                FechaFinModTxt.setDate(convertirFecha(FechaFinP));

                //JOptionPane.showMessageDialog(null, "Realice los cambios en el apartado de modificar");
                prestamosTabbed.setSelectedIndex(2);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "La fila seleccionada esta vacía");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un registro");
        }
    }//GEN-LAST:event_modificarBtnActionPerformed

    private void tituloTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tituloTxtKeyReleased
        limpiarTablaConsulta();
        if (!tituloTxt.getText().equals("")) {
            consultarLibros();
        }
    }//GEN-LAST:event_tituloTxtKeyReleased

    private void agregar1BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregar1BtnActionPerformed
        if (tablaLibros.getSelectedRow() != -1) {
            try {
                int f = tablaLibros.getSelectedRow();
                String isbnString = tablaLibros.getValueAt(f, 0).toString();
                if (isbnString.equals(ISBNRegTxt2.getText())) {
                    JOptionPane.showMessageDialog(null, "Este libro fue agregado como número 2");
                } else if (isbnString.equals(ISBNRegTxt3.getText())) {
                    JOptionPane.showMessageDialog(null, "Este libro fue agregado como número 3");
                } else {
                    ISBNRegTxt1.setText(isbnString);
                    libro1nombre.setText(tablaLibros.getValueAt(f, 1).toString());
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "La fila seleccionada esta vacía");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un registro");
        }
    }//GEN-LAST:event_agregar1BtnActionPerformed

    private void agregar2BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregar2BtnActionPerformed
        if (tablaLibros.getSelectedRow() != -1) {
            try {
                int f = tablaLibros.getSelectedRow();
                String isbnString = tablaLibros.getValueAt(f, 0).toString();
                if (isbnString.equals(ISBNRegTxt1.getText())) {
                    JOptionPane.showMessageDialog(null, "Este libro fue agregado como número 1");
                } else if (isbnString.equals(ISBNRegTxt3.getText())) {
                    JOptionPane.showMessageDialog(null, "Este libro fue agregado como número 3");
                } else {
                    ISBNRegTxt2.setText(isbnString);
                    libro2nombre.setText(tablaLibros.getValueAt(f, 1).toString());
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "La fila seleccionada esta vacía");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un registro");
        }
    }//GEN-LAST:event_agregar2BtnActionPerformed

    private void agregar3BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregar3BtnActionPerformed
        if (tablaLibros.getSelectedRow() != -1) {
            try {
                int f = tablaLibros.getSelectedRow();
                String isbnString = tablaLibros.getValueAt(f, 0).toString();
                if (isbnString.equals(ISBNRegTxt1.getText())) {
                    JOptionPane.showMessageDialog(null, "Este libro fue agregado como número 1");
                } else if (isbnString.equals(ISBNRegTxt2.getText())) {
                    JOptionPane.showMessageDialog(null, "Este libro fue agregado como número 2");
                } else {
                    ISBNRegTxt3.setText(isbnString);
                    libro3nombre.setText(tablaLibros.getValueAt(f, 1).toString());
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "La fila seleccionada esta vacía");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un registro");
        }
    }//GEN-LAST:event_agregar3BtnActionPerformed

    private void eliminarLibroBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarLibroBtn1ActionPerformed
        ISBNRegTxt1.setText("");
        libro1nombre.setText("");
    }//GEN-LAST:event_eliminarLibroBtn1ActionPerformed

    private void eliminarLibroBtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarLibroBtn2ActionPerformed
        ISBNRegTxt2.setText("");
        libro2nombre.setText("");
    }//GEN-LAST:event_eliminarLibroBtn2ActionPerformed

    private void eliminarLibroBtn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarLibroBtn3ActionPerformed
        ISBNRegTxt3.setText("");
        libro3nombre.setText("");
    }//GEN-LAST:event_eliminarLibroBtn3ActionPerformed

    private void devolucionBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_devolucionBtnActionPerformed
        if (TablaConGenP.getSelectedRow() != -1) {
            try {
                int f = TablaConGenP.getSelectedRow();
                String idP = TablaConGenP.getValueAt(f, 0).toString();

                int cantidadL = Integer.parseInt(TablaConGenP.getValueAt(f, 3).toString());
                consultaLibros(idP);
                int devolucionReg = 0;

                if (!"".equals(fechaDevolucionP1)) {
                    devolucionReg++;
                }
                if (!"".equals(fechaDevolucionP2)) {
                    devolucionReg++;
                }
                if (!"".equals(fechaDevolucionP3)) {
                    devolucionReg++;
                }

                if (devolucionReg == cantidadL) {
                    JOptionPane.showMessageDialog(null, "Las devoluciones ya fueron registradas");
                } else if (cantidadL != devolucionReg) {
                    idprestamoDevTxt.setText(idP);
                    if (fechaDevolucionP1.equals("")) {
                        titulo1DevTxt.setText(libro1);
                    }
                    if (fechaDevolucionP2.equals("")) {
                        titulo2DevTxt.setText(libro2);
                    }
                    if (fechaDevolucionP3.equals("")) {
                        titulo3DevTxt.setText(libro3);
                    }
                    //JOptionPane.showMessageDialog(null, "Consulte el apartado Devoluciones");
                    prestamosTabbed.setSelectedIndex(3);
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "La fila seleccionada esta vacía");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un registro");
        }
    }//GEN-LAST:event_devolucionBtnActionPerformed

    private void guardarBtnDevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarBtnDevActionPerformed
        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        boolean msj = false;
        if (!titulo1DevTxt.getText().equals("")) {
            if (devolucion1Check.isSelected()) {
                registrarDevolucion(idprestamoDevTxt.getText(), isbn1, fechaDvolucionD.getFecha());
                msj = true;
            }

        }
        if (!titulo2DevTxt.getText().equals("")) {
            if (devolucion2check.isSelected()) {
                registrarDevolucion(idprestamoDevTxt.getText(), isbn2, fechaDvolucionD.getFecha());
                msj = true;
            }

        }
        if (!titulo3DevTxt.getText().equals("")) {
            if (devolucion3check.isSelected()) {
                registrarDevolucion(idprestamoDevTxt.getText(), isbn3, fechaDvolucionD.getFecha());
                msj = true;
            }

        }

        if (msj) {
            idprestamoDevTxt.setText("");
            titulo1DevTxt.setText("");
            titulo2DevTxt.setText("");
            titulo3DevTxt.setText("");
            devolucion1Check.setSelected(false);
            devolucion2check.setSelected(false);
            devolucion3check.setSelected(false);
            JOptionPane.showMessageDialog(null, "Devolución registrada");
            if (IdPrestamoModTxt.getText().length() > 0) {
                consultaLibros(IdPrestamoModTxt.getText());
                consultaIndividual(IdPrestamoModTxt.getText(), IdUsuarioModTxt.getText());
                IdPrestamoModTxt.setEditable(false);
                IdPrestamoModTxt.setText(IdPrestamo);
                IdUsuarioModTxt.setText(IdUsuario);
                IdUsuarioModTxt.setEditable(false);
                libro1ModTxt.setText(libro1);
                libro2ModTxt.setText(libro2);
                libro3ModTxt.setText(libro3);
                devolucion1txt.setText(fechaDevolucionP1);
                devolucion2txt.setText(fechaDevolucionP2);
                devolucion3txt.setText(fechaDevolucionP3);

                fechaInicioModTxt.setText(FechaInicioP);
                FechaFinModTxt.setDate(convertirFecha(FechaFinP));
            }
        } else {
            JOptionPane.showMessageDialog(null, "Faltan campos por completar");
        }
    }//GEN-LAST:event_guardarBtnDevActionPerformed

    private void tituloTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tituloTxtKeyTyped
        String nombre = tituloTxt.getText().concat(Character.toString(evt.getKeyChar()));
        if (!valida.validarNombreNum(nombre) || tituloTxt.getText().length() >= 50) {
            evt.consume();
        }
    }//GEN-LAST:event_tituloTxtKeyTyped

    private void numLibrosConsultaBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_numLibrosConsultaBoxItemStateChanged
        limpiarTablaCon();
//        if (!UsuarioConGenTxt.getText().equals("") || numLibrosConsultaBox.getSelectedIndex() != 0) {
        consultaGeneral();
        //      }
    }//GEN-LAST:event_numLibrosConsultaBoxItemStateChanged

    private void IdUsuarioTxtRegKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IdUsuarioTxtRegKeyReleased
        consultarIDs();
    }//GEN-LAST:event_IdUsuarioTxtRegKeyReleased

    private void devolucion3checkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_devolucion3checkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_devolucion3checkActionPerformed
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ActualizarBtn;
    private com.toedter.calendar.JDateChooser FechaFinModTxt;
    private com.toedter.calendar.JDateChooser FechaFinTxtReg;
    private javax.swing.JTextField ISBNRegTxt1;
    private javax.swing.JTextField ISBNRegTxt2;
    private javax.swing.JTextField ISBNRegTxt3;
    private javax.swing.JTextField IdPrestamoModTxt;
    private javax.swing.JTextField IdPrestamoTxtReg;
    private javax.swing.JTextField IdUsuarioModTxt;
    private javax.swing.JTextField IdUsuarioTxtReg;
    private javax.swing.JButton PrestamoRegBoton;
    private javax.swing.JTable TablaConGenP;
    private javax.swing.JTextField UsuarioConGenTxt;
    private javax.swing.JButton agregar1Btn;
    private javax.swing.JButton agregar2Btn;
    private javax.swing.JButton agregar3Btn;
    private javax.swing.JCheckBox devolucion1Check;
    private javax.swing.JTextField devolucion1txt;
    private javax.swing.JCheckBox devolucion2check;
    private javax.swing.JTextField devolucion2txt;
    private javax.swing.JCheckBox devolucion3check;
    private javax.swing.JTextField devolucion3txt;
    private javax.swing.JButton devolucionBtn;
    private javax.swing.JButton eliminarLibroBtn1;
    private javax.swing.JButton eliminarLibroBtn2;
    private javax.swing.JButton eliminarLibroBtn3;
    private rojeru_san.RSLabelFecha fechaDvolucionD;
    private javax.swing.JTextField fechaInicioModTxt;
    private rojeru_san.RSLabelFecha fechaInicioR;
    private javax.swing.JButton guardarBtnDev;
    private javax.swing.JTextField idprestamoDevTxt;
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
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField libro1ModTxt;
    private javax.swing.JTextField libro1nombre;
    private javax.swing.JTextField libro2ModTxt;
    private javax.swing.JTextField libro2nombre;
    private javax.swing.JTextField libro3ModTxt;
    private javax.swing.JTextField libro3nombre;
    private javax.swing.JButton modificarBtn;
    private javax.swing.JComboBox<String> numLibrosConsultaBox;
    private javax.swing.JTabbedPane prestamosTabbed;
    private javax.swing.JTable tablaLibros;
    private javax.swing.JTextField titulo1DevTxt;
    private javax.swing.JTextField titulo2DevTxt;
    private javax.swing.JTextField titulo3DevTxt;
    private javax.swing.JTextField tituloTxt;
    // End of variables declaration//GEN-END:variables
}
