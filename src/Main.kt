

import java.io.File
import java.io.FileOutputStream

object main{
    @JvmStatic
    fun main(args:Array<String>){

        val input = File("C:\\Users\\swkhack\\temp\\source.dex")
        val output = File("C:\\Users\\swkhack\\temp\\encrypt.dex")
        val cryptData = File("C:\\Users\\swkhack\\temp\\encrypt.dat")
        val crypt = DexCrypto(input,output)
        crypt.run()
        val fileOut = FileOutputStream(cryptData)
        crypt.writeCryptMethodInfo(fileOut)
        fileOut.close()
//        val inputApk = File("C:\\Users\\swkhack\\src\\《Android应用加固保护开发入门实验文件》\\第一章\\第六节\\sample.apk");
//        val outputApk = File("C:\\Users\\swkhack\\src\\《Android应用加固保护开发入门实验文件》\\第一章\\第六节\\sample-enc.apk")
//
//        val tmpFolder = File(inputApk.parentFile,"SwkShell");
//        decompile(inputApk,tmpFolder);
//
//        val assetFolder = File(tmpFolder,"assets");
//        assetFolder.mkdir()
//
//        val dexFile = File(tmpFolder,"classes.dex")
//        dexFile.renameTo(File(assetFolder,"encrypt.dex"));
//
//        val shellDex = File(Resources.getResource("classes2.dex").toURI())
//        shellDex.copyTo(dexFile,true)
//
//        val shellLib = File(Resources.getResource("lib").toURI());
//        shellLib.copyRecursively(File(tmpFolder,"lib"),true);
//
//        val manifest = File(tmpFolder,"AndroidManifest.xml")
//        val oldAppName = ManifestXmlParser.modify(manifest)
//
//        val df = DexFileFactory.loadDexFile(dexFile, Opcodes.forApi(19))
//        val dexPool = DexPool(df.opcodes)
//        for(def in df.classes){
//            dexPool.internClass(def)
//        }
//        dexPool.internClass(buildShellInfo(oldAppName))
//
//        dexPool.writeTo(FileDataStore(dexFile))
//        compile(tmpFolder,outputApk)
//    }
//    fun decompile(input:File,output:File){
//        com.rover12421.shaka.cli.Main.main(arrayOf<String>(
//            "d","-f","-df","-s","-o",output.absolutePath,input.absolutePath
//        ));
//
//    }
//    fun compile(input:File,output:File){
//        com.rover12421.shaka.cli.Main.main(arrayOf<String>(
//            "b","-o",output.absolutePath,input.absolutePath
//        ))
//
//    }
//    fun buildShellInfo(appName:String ):ClassDef{
//        val shellInfoName = "Lcom/swkhackl/jk/ShellInfo;"
//        val superInfoName = "Ljava/lang/Object;"
//
//        val init = ImmutableMethod(
//            shellInfoName, "<init>", null, "V", AccessFlags.PUBLIC.value or AccessFlags.CONSTRUCTOR.value,
//            null,
//            ImmutableMethodImplementation(1,
//                mutableListOf<Instruction>(ImmutableInstruction35c(Opcode.INVOKE_DIRECT,1,0,0,0,0,0,
//                    ImmutableMethodReference(superInfoName,"<init>",null,"V")),
//                    ImmutableInstruction10x(Opcode.RETURN_VOID)
//                    ),null,null
//
//
//                )
//        )
//        if(appName!=""){
//            val getAppName = ImmutableMethod(
//                shellInfoName,
//                "getApplicationName",
//                null,
//                "Ljava/lang/String;",
//                AccessFlags.STATIC.value or AccessFlags.PUBLIC.value,
//                null,
//                ImmutableMethodImplementation(1, mutableListOf<Instruction>(
//                    ImmutableInstruction21c(Opcode.CONST_STRING,0,ImmutableStringReference(appName)),
//                    ImmutableInstruction11x(Opcode.RETURN_OBJECT,0)
//                ),null,null)
//            )
//            return ImmutableClassDef(shellInfoName,AccessFlags.PUBLIC.value,superInfoName,null,null,null,null,arrayListOf(init,getAppName))
//
//        }
//
//        return ImmutableClassDef(
//            shellInfoName,
//            AccessFlags.PUBLIC.value,
//            superInfoName,
//            null,
//            null,
//            null,
//            null,
//            null,
//            arrayListOf(init),
//            null
//
//        );


    }
}