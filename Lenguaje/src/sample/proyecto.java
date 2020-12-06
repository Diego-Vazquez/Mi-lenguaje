package sample;

import org.apache.commons.io.FilenameUtils;

import java.io.*;

public class proyecto {
    private File proyecto=null;
    private String nombre;
    private BufferedReader reader=null;
    private File carpeta=null;



    public proyecto(String nombre) {
        this.nombre = nombre;
        proyecto=new File(crear_carpeta_dafault()+nombre+".panda");
        existe(FilenameUtils.getName(proyecto.getName()),1);

    }

    public File getProyecto(){
        return proyecto;
    }



    public void existe(String cadena, int cont){
        if (proyecto.exists()){
            proyecto=new File(crear_carpeta_dafault()+nombre+"("+cont+").panda");
            existe(cadena,(cont+1));
        }else {
            try {
                proyecto.createNewFile();
                escribir(nombre.replace(" ","_")+"{\n\nint main(void){\nprint(\"Hello World\");\n\n}\n}");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }




    public void escribir(String texto){
        String mensaje=null;
        try {
            FileOutputStream salida=new FileOutputStream(proyecto);
            byte[] text=texto.getBytes();
            salida.write(text);
            System.out.println("archivo actualizado");
        } catch (Exception e) { }
    }

    public String leer(){
        String documento="";
        try {
            FileInputStream entrada=new FileInputStream(proyecto);
            int ascci;
            while ((ascci=entrada.read())!=-1){
                char caracter=(char)ascci;
                documento+=caracter;
            }
        }catch (Exception e){ }
        return documento;
    }

    public String crear_carpeta_dafault(){
        String ruta="c:"+File.separator+"Users"+File.separator+System.getProperty("user.name")+File.separator+"Documents"+File.separator+"Panda projects";
        carpeta=new File(ruta.replace(File.separator,"/"));
        ruta=ruta.concat(File.separator);
        boolean a=carpeta.mkdir();
        if (a){
            System.out.println("se creo la carpeta default");
        }else {
            System.out.println("no se creo la carpeta default");
        }
        return ruta.replace(File.separator,"/");
    }

}
