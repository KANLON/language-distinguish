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
        String str = "I FOUND A SECRET MAP IN FORTNITE! (Creative Mode Parkour) with TBNRfrags   \n" +
                "   SUBSCRIBE for more videos!    http://bitly.com/TBNRMY\n" +
                "\n" +
                " ️ FRIENDS\n" +
                "   Kenny - http://bit.ly/2qX1krn\n" +
                "\n" +
                "  Be sure to check out the map/creator!\n" +
                "   https://dropnite.com/map.php?id=80\n" +
                "\n" +
                "  ️ Submit your Fortnite maps here! \n" +
                "   http://bit.ly/PrestonMaps \n" +
                "\n" +
                "   Join my Fan Discord! \n" +
                "   http://discord.gg/Preston\n" +
                "\n" +
                "   \"FIRE\" Merchandise logo clothing line! \n" +
                "   http://www.PrestonsStylez.com \n" +
                "\n" +
                "  ️ MY OTHER YOUTUBE CHANNELS!\n" +
                "   Preston - http://youtube.com/PrestonPlayz\n" +
                "   PrestonRoblox - http://youtube.com/PrestonRoblox\n" +
                "   PrestonMinecraft - http://youtube.com/PrestonMinecraft\n" +
                "   BriannaPlayz - http://bit.ly/Sub2Brianna\n" +
                "   KeeleyPlayz - http://bit.ly/Sub2Keeley\n" +
                "\n" +
                "   FOLLOW ME HERE!\n" +
                "   Instagram - https://instagram.com/realtbnrfrags\n" +
                "   Twitter - https://twitter.com/Preston\n" +
                "   Snapchat - https://www.snapchat.com/add/PrestoSnaps\n" +
                "\n" +
                "\n" +
                "------------------------------\n" +
                "\n" +
                "ALL MUSIC USED IN THIS VIDEO:\n" +
                "\n" +
                "Intro Song\n" +
                "Rob Gasser - Ricochet [NCS Release] \n" +
                "Music provided by NoCopyrightSounds.\n" +
                "Video: https://youtu.be/T4Gq9pkToS8\n" +
                "Download: http://http://ncs.io/Ricochet\n" +
                "\n" +
                "Additional music provided by Epidemic Sound\n" +
                "Click here for a free trial!    http://share.epidemicsound.com/TBNR\n" +
                "\n" +
                "#UseCodeTBNRFrags";
        String reg = "[a-zA-z]+://[^\\s]*";
        str = str.replaceAll(reg, "");
        str = str.replaceAll("\\s+", " ");
        System.out.println("长度为：" + str.length() + "," + str);
        str = str.replaceAll("️ ", " ");
        System.out.println("再次替换：" + "长度为：" + str.length() + "," + str);
        System.out.println(Integer.toHexString((int) '️') + "-->" + (int) ' ');
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
