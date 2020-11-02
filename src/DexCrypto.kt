
import org.jf.dexlib2.AccessFlags
import org.jf.dexlib2.DexFileFactory
import org.jf.dexlib2.Opcodes
import org.jf.dexlib2.dexbacked.DexBackedMethod
import org.jf.dexlib2.dexbacked.raw.CodeItem
import org.jf.dexlib2.iface.ClassDef
import org.jf.dexlib2.iface.DexFile
import org.jf.dexlib2.iface.Method
import org.jf.dexlib2.iface.MethodParameter
import org.jf.dexlib2.immutable.ImmutableMethod
import org.jf.dexlib2.immutable.ImmutableMethodParameter
import org.jf.dexlib2.rewriter.*
import org.jf.dexlib2.writer.io.FileDataStore
import org.jf.dexlib2.writer.pool.ClassPool
import org.jf.dexlib2.writer.pool.DexPool
import java.io.File
import java.io.OutputStream

class DexCrypto(val inputDex:File,val outputDex:File){
    private val cryptMethod = mutableListOf<CryptMethodInfo>()



    fun run():Boolean{
        DexFileFactory.writeDexFile(inputDex.absolutePath,DexFileFactory.loadDexFile(inputDex, Opcodes.forApi(19)))
        val backDexFileSrc = DexFileFactory.loadDexFile(inputDex, Opcodes.forApi(19))

        val dexpool = DexPool(backDexFileSrc.opcodes)
        for(classDef in backDexFileSrc.classes){
            dexpool.internClass(classDef)
        }
        dexpool.classSection = ClassPool(dexpool)

        val dexRewriter=CryptDexRewriter(CryptRewriterModule())
        val dexFileRewrited = dexRewriter.rewriteDexFile(backDexFileSrc)
        for(classDef in dexFileRewrited.classes){
            dexpool.internClass(classDef)
        }
        dexpool.writeTo(FileDataStore(outputDex))


        return  true
    }

    fun writeCryptMethodInfo(out: OutputStream): Boolean{
        for (m in cryptMethod){
            m.writeTo(out)
        }
        return  true
    }

    fun methodCanBeEncrypted(method:DexBackedMethod):Boolean{
        if(method.implementation==null){
            return false
        }
        if(AccessFlags.NATIVE.isSet(method.accessFlags)){
            return false
        }
        if(method.name=="<clinit>"){
            return false
        }
        if(method.name=="<init>"){
            return false
        }
        return true
    }

    fun dumpMethodInfo(method: DexBackedMethod) : Boolean{
        cryptMethod.add(CryptMethodInfo(method))

        return  true
    }

    fun getRewriteMethod(method: DexBackedMethod):Method{

        val paras = mutableListOf<MethodParameter>()
        for (p in method.parameters){
            paras.add(ImmutableMethodParameter(p.type,null,null))
        }
        return ImmutableMethod(method.definingClass,method.name,paras,method.returnType,method.accessFlags or AccessFlags.NATIVE.value
        ,null,null);

    }
    internal inner  class CryptMethodInfo(method: DexBackedMethod){
        val dexMethod=method
        val code_item:Array<Byte>
        val align4:Array<Byte>

        init {
            val impl = method.implementation!!
            val dexFile = dexMethod.dexFile
            val code_offset = impl.codeOffset
            val code_item_size = impl.size-impl.debugInfo.size
            code_item = Array<Byte>(code_item_size,{i->dexFile.readByte(code_offset+i).toByte()})
            code_item[CodeItem.DEBUG_INFO_OFFSET]=0
            code_item[CodeItem.DEBUG_INFO_OFFSET+1]=0
            code_item[CodeItem.DEBUG_INFO_OFFSET+2]=0
            code_item[CodeItem.DEBUG_INFO_OFFSET+3]=0

            val align4_size = 4-code_item_size%4
            align4 = Array<Byte>(align4_size,{0})

        }
        fun writeTo(out:OutputStream):Boolean{
            //item_len method_idx code_item align
            val item_len = 4+4+code_item.size+align4.size
            out.writeInt(item_len)
            out.writeInt(dexMethod.methodIndex)
            out.write(code_item.toByteArray())
            out.write(align4.toByteArray())
            return true
        }
    }

    internal inner class CryptDexRewriter(module: RewriterModule):DexRewriter(module){
        override fun rewriteDexFile(dexFile: DexFile): DexFile {
            return super.rewriteDexFile(dexFile)
        }
    }
    internal inner class CryptRewriterModule:RewriterModule(){
        override fun getClassDefRewriter(rewriters: Rewriters): Rewriter<ClassDef> {
            return object:ClassDefRewriter(rewriters) {
                override fun rewrite(classDef: ClassDef): ClassDef {
                    return super.rewrite(classDef)
                }
            }
        }

        override fun getMethodRewriter(rewriters: Rewriters): Rewriter<Method> {
            return object : MethodRewriter(rewriters){
                override fun rewrite(value: Method): Method {
                    if(methodCanBeEncrypted(value as DexBackedMethod)){
                        dumpMethodInfo(value)
                        return  getRewriteMethod(value)
                    }
                    return super.rewrite(value)
                }
            }
        }
    }

}

fun OutputStream.writeInt(value:Int){
    write(value)
    write(value ushr 8)
    write(value ushr 16)
    write(value ushr 24)
}