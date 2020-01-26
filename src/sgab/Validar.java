
package sgab;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validar {
    public String nombreRegEx = "^([A-ZÑÁÉÍÓÚ][a-zñáéíóú]{0,20}[.]?[ ]?){1,5}$";
    public String nombreNumRegEx = "^([A-ZÑÁÉÍÓÚ0-9][a-zñáéíóú0-9]{0,20}[.]?[ ]?){1,5}$";
    public String titulo = "^([A-ZÑÁÉÍÓÚ0-9][a-zñáéíóú0-9]{0,20}[.]?[ ]?){1,5}$";
    public String fechaRegEx = "^([0-2][0-9]|3[0-1])(\\/|-)(0[1-9]|1[0-2])\\2(\\d{4})$";
    public String isbnRegEx = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$";
    public String idRegEx = "^([A-Z][0-9]{4})$";
    public String idusuario = "^(U[0-9]{0,4})$";
    public String idactividad = "^(A[0-9]{0,4})$";
    // Valida letras, espacios, puntos, acentos  
    public boolean letras(char key){
        boolean validado = false;
        boolean letrasMayus = key >= 'A' && key <= 'Z';
        boolean letrasMinus = key >= 'a' && key <= 'z';
        boolean caracteresEspeciales = key == ' ' || key == '.';
        boolean acentos = key == 'á' || key == 'é' || key == 'í' || key =='ó' || key=='ú' || key=='Á' || key=='É' || key=='Í' || key=='Ó' || key=='Ú';
        boolean letra = key == 'ñ' || key == 'Ñ';
        if (!letrasMayus && !letrasMinus && !caracteresEspeciales && !acentos && !letra) {
            validado = true;
        }  
        return validado;
    }
    
    // Valida letras,espacios, puntos, acentos, numeros
    public boolean letrasNumeros(char key){
        boolean validado = false;
        boolean letrasMayus = key >= 'A' && key <= 'Z';
        boolean letrasMinus = key >= 'a' && key <= 'z';
        boolean digitos = Character.isDigit(key);
        boolean caracteresEspeciales = key == ' ' || key == '.';
        boolean acentos = key == 'á' || key == 'é' || key == 'í' || key =='ó' || key=='ú' || key=='Á' || key=='É' || key=='Í' || key=='Ó' || key=='Ú';
        boolean letra = key == 'ñ' || key == 'Ñ';
        if (!letrasMayus && !letrasMinus && !caracteresEspeciales && !digitos && !acentos && !letra) {
            validado = true;
        }  
        return validado;
    }
    
    // Valida las fechas (numeros y la diagonal)
    public boolean fecha(char key){
       boolean validado = false;
       boolean digitos = Character.isDigit(key);
       boolean slash = key == '/';
        if (!digitos && !slash) {
            validado = true;
        }
       return validado;
    }
    
    // Valida que sean dos horas establecidas
    public boolean horaCompuesta(char key){
        boolean validado = false;
       boolean digitos = Character.isDigit(key);
       boolean especiales = key == '-' || key == ':';
        if (!digitos && !especiales) {
            validado = true;
        }
       return validado;
    }
    
    // Valida el id
    public boolean id(char key){
        boolean validado = false;
        boolean digitos = Character.isDigit(key);
        boolean letrasMayus = key >= 'A' && key <= 'Z';
        //boolean guion = key == '-';
        if (!digitos && !letrasMayus) {
            validado = true;
        }
        return validado;
    }
    
    public boolean validarNombre(String nombre){
        Pattern p = Pattern.compile(nombreRegEx);
        Matcher m = p.matcher(nombre);
        return m.lookingAt();
        //return nombre.matches("^([A-ZÑ]{1}[a-zñáéíóú]+[.]?[ ]?){2,5}$");
    }
    public boolean validarNombreNum(String nombre){
        Pattern p = Pattern.compile(nombreNumRegEx);
        Matcher m = p.matcher(nombre);
        return m.lookingAt();
        //return nombre.matches("^([A-ZÑ]{1}[a-zñáéíóú]+[.]?[ ]?){2,5}$");
    }
    public boolean validarEnvioFecha(String fecha){
        return fecha.matches(fechaRegEx);
    }
    public boolean validarEnvioISBN(String isbn){
        return isbn.matches(isbnRegEx);
    }
    public boolean validarEnvioID(String id){
        return id.matches(idRegEx);
    }
    public boolean secuenciaID(String id){
        Pattern p = Pattern.compile(idRegEx);
        Matcher m = p.matcher(id);
        return m.find();
    }
    public boolean IDUsuario(String id){
        Pattern p = Pattern.compile(idusuario);
        Matcher m = p.matcher(id);
        return m.lookingAt();
    }
    public boolean IDActividad(String id){
        Pattern p = Pattern.compile(idactividad);
        Matcher m = p.matcher(id);
        return m.lookingAt();
    }
}
