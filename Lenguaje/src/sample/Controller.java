package sample;

import JFlex.anttask.JFlexTask;
import java_cup.runtime.Symbol;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import jdk.nashorn.internal.runtime.regexp.joni.Syntax;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.Extension;
import java.util.Optional;

public class Controller {
    private Text text=null;
    proyecto proyecto=null;
    private File archivo_temporal=null;
    private boolean primerinicio=false;
    private String ruta_temporal="";
    @FXML
    TextArea consola;
    @FXML
    Label lb_ruta;
    @FXML
    VBox editor;
    TextInputDialog inputDialog=new TextInputDialog();
    Configuraciones configuraciones=null;
    public static Configuraciones auxiliar=null;
    public static String ruta_global;
    public static TextArea consola_aux;

    @FXML protected void initialize(){

        consola_aux=consola;
        ruta_global=crear_carpeta_dafault();
        configuraciones=new Configuraciones(editor,(int) editor.getPrefWidth(),(int) editor.getPrefHeight());
        auxiliar=configuraciones;
        primerinicio=true;

    }

    public void ejecutar(){
       /*String ruta="C:/Users/Diegoo/IdeaProjects/Lenguaje/src/sample/Lexer.flex";
        File archivoo=new File(ruta);
        JFlex.Main.generate(archivoo);*/
        try {
            analizar_sintaxis();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void analizar_sintaxis() throws IOException {
        //aun no terminado
        Sintax.Imprimir="";
        if (!consola.getText().isEmpty()){consola.clear();}
        String ST=(String) configuraciones.obtener_texto();

        Sintax s=new Sintax(new sample.Cup(new StringReader(ST)));
        try {
            s.parse();
            consola.setText(Sintax.Imprimir);
            consola.appendText("    \n\n\n** La sintaxis es correcta **");




        } catch (Exception e) {
          //  Symbol symbol= s.getS();
            try {
                Symbol symbol= s.getS();
                consola.setText("   Error de sintaxis en linea: "+(symbol.right+1) +"      No se esperaba: "+symbol.value);
                if(symbol==null) {
                    consola.setText("   Error de Analisis");
                }
            }catch (Exception a){
                switch (Sintax.codigo_error){
                    case "0p":{ consola.setText(" ERROR "+Sintax.mensaje_error); break;}
                    case "0i":{ consola.setText(" ERROR "+Sintax.mensaje_error); break;}
                    case "0d":{ consola.setText(" ERROR "+Sintax.mensaje_error); break;}
                    case "0s":{ consola.setText(" ERROR "+Sintax.mensaje_error); break;}
                    default:{consola.setText("error desconocido ");}
                }

            }


        }
        Sintax.colector_de_basura();


    }

    public void analizar_lexico_presiso() throws IOException {
        int cont = 1;

        String expr = (String) configuraciones.obtener_texto();
        Lexer lexer = new Lexer (new StringReader (expr));
        String resultado = "LINEA " + cont + "\n";
        while (true) {
            Tokens tokens = lexer.yylex();
            if (tokens == null) {
                consola.setText (resultado);
                return;
            }
            switch (tokens){
                case ERROR:{resultado +="<simbolo no definido>\t\t\n";break;}
                case Suma:{resultado+="<operador aritmetico>\t\t"+lexer.lexico+"\n";break;}
                case Igual:{resultado+="<operador aritmetico>\t\t"+lexer.lexico+"\n";break;}
                case Resta:{resultado+="<operador aritmetico>\t\t"+lexer.lexico+"\n";break;}
                case Numero:{resultado+= "<numero>\t\t"+lexer.lexico+"\n";break;}
                case Dobl:{resultado+= "<tipo dato>\t\t"+lexer.lexico+"\n";break;}
                case If:{resultado+= "<Reservada>\t\t"+lexer.lexico+"\n";break;}
                case For:{resultado+= "<Reservada>\t\t"+lexer.lexico+"\n";break;}
                case Int:{resultado+= "<tipo dato>\t\t"+lexer.lexico+"\n";break;}
                case Str:{resultado+= "<tipo dato>\t\t"+lexer.lexico+"\n";break;}
                case Else:{resultado+="<Reservada>\t\t"+lexer.lexico+"\n";break;}
                case Print:{resultado+= "<Reservada>\t\t"+lexer.lexico+"\n";break;}
                case While:{resultado+= "<Reservada>\t\t"+lexer.lexico+"\n";break;}
                case Identificador:{resultado+= "<identificador>\t\t"+lexer.lexico+"\n";break;}
                case Cadena:{resultado+= "<Cadena>\t\t"+lexer.lexico+"\n";break;}
                case bloque_abierto:{resultado+= "<inicio bloque>\t\t"+lexer.lexico+"\n";break;}
                case bloque_cerrado:{resultado+= "<fin bloque>\t\t"+lexer.lexico+"\n";break;}
                case Multiplicacion:{resultado+= "<operador aritmetico>\t\t"+lexer.lexico+"\n";;}
                case clase_abierta:{resultado+= "<inicio clase>\t\t"+lexer.lexico+"\n";break;}
                case clase_cerrada:{resultado+= "<fin clase>\t\t"+lexer.lexico+"\n";break;}
                case Division:{resultado+= "<operador aritmetico>\t\t"+lexer.lexico+"\n";break;}
                case Do:{resultado+= "<Reservada>\t\t"+lexer.lexico+"\n";break;}
                case Bol:{resultado+= "<tipo dato>\t\t"+lexer.lexico+"\n";break;}
                case Case:{resultado+= "<Resevada>\t\t"+lexer.lexico+"\n";break;}
                case Coma:{resultado+= "<caracter especial>\t\t"+lexer.lexico+"\n";break;}
                case Main:{resultado+= "<Reservada>\t\t"+lexer.lexico+"\n";break;}
                case punto:{resultado+= "<caracter especial>\t\t"+lexer.lexico+"\n";break;}
                case Switch:{resultado+= "<Reservada>\t\t"+lexer.lexico+"\n";break;}
                case decremento:{resultado+= "<operador aignacion>\t\t"+lexer.lexico+"\n";break;}
                case dos_puntos:{resultado+= "<caracter especial>\t\t"+lexer.lexico+"\n";break;}
                case incremento:{resultado+= "<operador asignacion>\t\t"+lexer.lexico+"\n";break;}
                case punto_y_coma:{resultado+= "<fin sentencia>\t\t"+lexer.lexico+"\n";break;}
                case operador_logico:{resultado+= "<operador logico>\t\t"+lexer.lexico+"\n";break;}
                case operador_boleano:{resultado+= "<operador boleano>\t\t"+lexer.lexico+"\n";break;}
                case operador_relacional:{resultado+= "<operador relacional>\t\t"+lexer.lexico+"\n";break;}
                case Linea:{cont++; resultado+="LINEA "+cont+"\n";break;}
                case comilla:{resultado+="<caracter especial>\t\t"+lexer.lexico+"\n";break;}
                case Void:{resultado+="<Reservada>\t\t"+lexer.lexico+"\n";break;}
                default:{resultado+="< "+lexer.lexico+" >\n"; break;}
            }
        }
    }


































    public void nuevo(){
      inputDialog.setTitle("Nuevo Proyecto");
        inputDialog.setHeaderText("Nombre del proyecto:");
        inputDialog.setContentText(null);
        Optional<String> a = inputDialog.showAndWait();
        try {

            if (a.get().isEmpty()){
                proyecto=new proyecto("Nuevo Proyecto");
                configuraciones.remplazar_texto(proyecto.leer());
                lb_ruta.setText("Nuevo Proyecto");
                primerinicio=false;

            }else {
                proyecto=new proyecto(a.get());
                configuraciones.remplazar_texto(proyecto.leer());
                lb_ruta.setText(a.get());
                primerinicio=false;

            }
        }catch (Exception e){

        }



    }
    public void guardar(){
        //agregar donde quiere guardar el archivo

        if (configuraciones.obtener_texto().isEmpty()){

        }else {
            if (primerinicio){
                //aqui poner el file save
                FileChooser fileChooser=new FileChooser();
                fileChooser.setInitialDirectory(new File(ruta_global));
                fileChooser.setInitialFileName("Nuevo proyecto");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Panda project","*.panda"));
                File destino=fileChooser.showSaveDialog(Main.stage);
                if (destino!=null){
                    try {
                        destino.createNewFile();
                        guardar_archivo(destino,configuraciones.obtener_texto());
                        archivo_temporal=destino;
                        primerinicio=false;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {

                }
            }else{

                if (!(proyecto ==null)){
                    proyecto.escribir(configuraciones.obtener_texto());
                }else {
                    guardar_archivo(archivo_temporal,configuraciones.obtener_texto());
                }
            }

        }


    }






    public void abrir(){
        FileChooser fileChooser=new FileChooser();
        fileChooser.setInitialDirectory(new File(ruta_global));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PANDA Files (*.panda)","*.panda"));
       File archivo_abierto=null;
       if (!(proyecto==null)){
           archivo_abierto=proyecto.getProyecto();
       }else {
           if (!(archivo_temporal ==null)){
               archivo_abierto=archivo_temporal;
           }else {

           }

       }
       if (archivo_abierto==null){
           archivo_temporal=fileChooser.showOpenDialog(Main.stage);
           if (!(archivo_temporal ==null)){
               if (!(proyecto ==null)){
                   proyecto=null;
                   configuraciones.remplazar_texto(Abrir_archivo(archivo_temporal));
                   lb_ruta.setText(FilenameUtils.getBaseName(archivo_temporal.getName()));
                   primerinicio=false;




               }else {
                   configuraciones.remplazar_texto(Abrir_archivo(archivo_temporal));
                   lb_ruta.setText(FilenameUtils.getBaseName(archivo_temporal.getName()));
                   primerinicio=false;

               }
           }else {

           }
       }else {
           archivo_temporal=fileChooser.showOpenDialog(Main.stage);
           if (archivo_temporal.getName().equals(archivo_abierto.getName())){
               System.out.println("archivo ya abierto");

           }else {
               if (!(archivo_temporal ==null)){
                   if (!(proyecto ==null)){
                       proyecto=null;
                       configuraciones.remplazar_texto(Abrir_archivo(archivo_temporal));
                       lb_ruta.setText(FilenameUtils.getBaseName(archivo_temporal.getName()));
                       primerinicio=false;


                   }else {
                       configuraciones.remplazar_texto(Abrir_archivo(archivo_temporal));
                       lb_ruta.setText(FilenameUtils.getBaseName(archivo_temporal.getName()));
                       primerinicio=false;

                   }
               }else {

               }
           }

       }



    }





    public String Abrir_archivo(File archivo){
        String documento="";
        try {
            FileInputStream entrada=new FileInputStream(archivo);
            int ascci;
            while ((ascci=entrada.read())!=-1){
                char caracter=(char)ascci;
                documento+=caracter;
            }
        }catch (Exception e){ }
        return documento;
    }

    public void guardar_archivo(File archivo, String documento){
        String doc=documento.replace("proyecto",FilenameUtils.getBaseName(archivo.getName()));
        String mensaje=null;
        try {
            FileOutputStream salida=new FileOutputStream(archivo);
            byte[] text=doc.getBytes();
            salida.write(text);
            System.out.println("archivo guardado");
            configuraciones.remplazar_texto(configuraciones.obtener_texto().replace("proyecto",FilenameUtils.getBaseName(archivo.getName())));
        } catch (Exception e) { }

    }

    public String crear_carpeta_dafault(){
        String unidad=System.getProperty("user.dir").toString().substring(0,1);
        String ruta=unidad+":"+File.separator+"Users"+File.separator+System.getProperty("user.name")+File.separator+"Documents"+File.separator+"Panda projects";
        ruta=ruta.replace(File.separator,"/");
        File carpeta=new File(ruta);
        boolean a=carpeta.mkdir();
        if (a){
            System.out.println("se creo la carpeta default");
        }else {
            System.out.println("la carpeta default ya existe");
        }
        return ruta;
    }


}