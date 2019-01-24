package test.com.yy.utils;

import com.yy.language.LanguageDistinguish;
import com.yy.utils.ExcelUtil;
import com.yy.utils.TxtUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * ExcelUtil Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since 2019-1-23
 */
public class ExcelUtilTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     *
     * Method: writeTestDataToExcel(List<JSONObject> jsonObjects)
     *
     */
    @Test
    public void testWriteTestDataToExcel() throws Exception {
//TODO: Test goes here...

        ExcelUtil.writeTestDataToExcel(TxtUtil.getTestJson());
    }

    /**
     *
     * Method: getTestStr()
     *
     */
    @Test
    public void testGetTestStr() throws Exception {
        char[] unicodes = {'➜', 'ಌ', '►', '©', '®', 'ⓒ', '►', '↠', '✯', '♥', '♬', '♥', '★', '→', '�', ')', '（', ',', '，', '【', '{',};
        for (int i = 0; i < unicodes.length; i++) {
            System.out.print(Integer.toHexString((int) unicodes[i]) + "-->" + unicodes[i]);
            LanguageDistinguish languageDistinguish = new LanguageDistinguish();
            System.out.println("，语言是：" + languageDistinguish.getOneLanguageByUnicode((int) unicodes[i], ExcelUtil.getSpecialLanguageAndUnicodeFromFromExcel()));
        }

        //替换字符中所有链接为空
        String str = " 블핑하우스 BLACKPINK HOUSE EP.1ㅤㅤ ㅤㅤ ㅤㅤ Online release On air SAT.PM KST YouTube, V LIVE SUN.AM KST JTBC 블핑하우스 BLACKPINK HOUSE BLACKPINK More about BLACKPINK ";
        String reg = "[a-zA-z]+://[^\\s]*";
        String signReg = "[!@#\\$%\\^&\\*\\(\\)_\\+=\\{\\}\\\\\\[\\]\\?\\/\\|#:><;\\-\'\",́`~\\.]";
        str = str.replaceAll(signReg, " ");
        str = str.replaceAll(reg, " ");
        str = str.replaceAll("\\s+", " ");
        System.out.println("长度为：" + str.length() + "," + str);
        str = str.replaceAll(" ", " ");
        System.out.println("再次替换：" + "长度为：" + str.length() + "," + str);
        System.out.println(Integer.toHexString(' ') + "-->" + Integer.toHexString('ㅤ'));
    }

    /**
     *
     * Method: getTranslateLanguageAndCodeFromExcel(Enum translateType)
     *
     */
    @Test
    public void testGetTranslateLanguageAndCodeFromExcel() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getLanguageAndUnicodeFromExcel()
     */
    @Test
    public void testGetLanguageAndUnicodeFromExcel() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getInputStream(String filePath)
     */
    @Test
    public void testGetInputStream() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getOutputStream(String filePath)
     */
    @Test
    public void testGetOutputStream() throws Exception {
//TODO: Test goes here... 
    }


    /**
     * Method: getExcelInfo()
     */
    @Test
    public void testGetExcelInfo() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = ExcelUtil.getClass().getMethod('getExcelInfo'); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

} 
