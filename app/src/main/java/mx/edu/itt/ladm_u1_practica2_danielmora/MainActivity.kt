package mx.edu.itt.ladm_u1_practica2_danielmora

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ContextCompat
                .checkSelfPermission(this,android
                    .Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
            //Si entra entonces no se otorgaron los permisos
            ActivityCompat.requestPermissions(
                this,//en que proyecto
                arrayOf(       //Arreglo con todos los permisos
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE),
                0)//metodo para la comprobacion de solicitud
        }else{
            //si entra aqui,se otorgaron los permisos
            mensaje("Permisos ya otorgados ")
        }

        guardar.setOnClickListener {
          if(!ValidarG()){return@setOnClickListener}
            if(radioButton.isChecked){
                guardarAchInterno()
            }
            else{
                if(radioButton2.isChecked){
                    guardarArcSD()
                }
            }//else
        }
        abrir.setOnClickListener {
            if(!ValidarA()){return@setOnClickListener}
            if(radioButton.isChecked){
                leearArchInterno()
            }
            else{
                leerArchivoSD()
            }//else

        }
    }

    fun noSD ():Boolean{
        var estado = Environment.getExternalStorageState()
        if(estado!= Environment.MEDIA_MOUNTED){return true}
        return false
    }
    fun guardarAchInterno(){
        var nameFilee =nombre.text.toString()
        try {
            var flujoSalida= OutputStreamWriter(openFileOutput("${nameFilee}.txt", Context.MODE_PRIVATE))
            var data=frase.text.toString()
            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()
            mensaje("Exito! se guardó correctamente")
            ponerTextos("","")
        }catch(error : IOException) {
            mensaje(error.message.toString())
        }

    }
    fun leearArchInterno (){
        var nameFilee =nombre.text.toString()
        try {
            var flujoentrada = BufferedReader (
                InputStreamReader(openFileInput("${nameFilee}.txt"))
            )
            var data = flujoentrada.readLine()

            ponerTextos(data,"")
            flujoentrada.close()

        }catch (error: IOException){
            mensaje(error.message.toString()+"\n Archivo no encontrado")
        }

    }
    fun guardarArcSD(){
        var nameFilee =nombre.text.toString()
        if(noSD()){
            mensaje("no hay memoria externa")
            return
        }
        try {
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivos = File(rutaSD.absolutePath,"${nameFilee}.txt")
            var flujoSalida= OutputStreamWriter(
                FileOutputStream(datosArchivos)

            )
            var data= frase.text.toString()
            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()
            mensaje("Exito! se guardó correctamente")
            ponerTextos("","")
        }catch(error : IOException) {
            mensaje(error.message.toString())
        }
    }
    fun leerArchivoSD(){
        var nameFilee =nombre.text.toString()
        if(noSD()){
            mensaje("no hay memoria externa")
            return
        }
        try {
            var rutaSD= Environment.getExternalStorageDirectory()
            var datosArchi= File(rutaSD.absolutePath,"${nameFilee}.txt")
            var flujoentrada = BufferedReader (
                InputStreamReader(   FileInputStream(datosArchi)  )
            )
            var data = flujoentrada.readLine()


            ponerTextos(data,"")
            flujoentrada.close()

        }catch (error: IOException){
            mensaje(error.message.toString()+"\n Archivo no encontrado")
        }
    }
    fun ValidarG() :Boolean{
        if(frase.text.isEmpty()){Toast.makeText(this,"Escribe una frase",Toast.LENGTH_LONG).show()
            return false }
        if(nombre.text.isEmpty()){Toast.makeText(this,"Escribe el nombre del archivo",Toast.LENGTH_LONG).show()
            return false
        }
        if(!radioButton.isChecked and  !radioButton2.isChecked){
            Toast.makeText(this,"Selecciona la memoria ",Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }
    fun ValidarA() :Boolean{

        if(nombre.text.isEmpty()){Toast.makeText(this,"Escribe el nombre del archivo",Toast.LENGTH_LONG).show()
            return false
        }
        if(!radioButton.isChecked and !radioButton2.isChecked){
            Toast.makeText(this,"Selecciona la memoria ",Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }
    fun mensaje( m :String){
        AlertDialog.Builder(this)
            .setTitle("Atencion")
            .setMessage(m)
            .setPositiveButton("OK"){d,i->}
            .show()

    }
    fun ponerTextos(t1:String,t2:String){

        frase.setText(t1)
        nombre.setText(t2)


    }

}
