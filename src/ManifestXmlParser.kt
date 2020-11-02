import org.jdom2.Namespace
import org.jdom2.input.SAXBuilder
import org.jdom2.output.XMLOutputter
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

object ManifestXmlParser {
    private val proxyApp = "com.swkhackl.jk.ProxyApplication"


    private val NAME_SPACE = "http://schemas.android.com/apk/res/android"
    private val NAME_PREFIX = "android"
    private val NAME = "name"

    //处理AndroidMainfest.xml文件
    @Throws(Exception::class)
    fun modify(xmlFile: File): String {
        val builder = SAXBuilder()
        var oldApplication: String = "android.app.Application"

        val doc = builder.build(xmlFile)
        val root = doc.getRootElement()    //mainfest
        val application = root.getChild("application")

        val ns = Namespace.getNamespace(NAME_PREFIX, NAME_SPACE)

        val attribute = application.getAttribute(NAME, ns)
        if (attribute != null) {
            oldApplication = attribute.getValue()
            attribute.setValue(proxyApp)
        } else {
            application.setAttribute(NAME, proxyApp, ns)
        }

        val XMLOutput = XMLOutputter()
        XMLOutput.output(doc, FileOutputStream(xmlFile))

        return oldApplication;
    }
}