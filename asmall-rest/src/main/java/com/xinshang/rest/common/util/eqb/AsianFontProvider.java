package com.xinshang.rest.common.util.eqb;
import com.itextpdf.text.Font;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.xinshang.rest.config.properties.EqbProperties;
import org.springframework.beans.factory.annotation.Autowired;

public class AsianFontProvider extends XMLWorkerFontProvider {
    @Autowired
    private EqbProperties eqbProperties;

    @Override
    public Font getFont(final String fontname, String encoding, float size, final int style) {
        String fntname = fontname;
        if (fntname == null) {

            /*使用的windows里的宋体，可将其文件放资源文件中引入*/

            fntname =eqbProperties.getFontPath() ;
        }
        if (size == 0) {
            size = 4;
        }
        return super.getFont(fntname, encoding, size, style);
    }
}
